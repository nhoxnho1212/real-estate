package com.ou.ret.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ou.ret.config.Constants;
import com.ou.ret.config.EncryptionConfig;
import com.ou.ret.domain.Authority;
import com.ou.ret.domain.User;
import com.ou.ret.repository.AuthorityRepository;
import com.ou.ret.repository.UserRepository;
import com.ou.ret.security.AuthoritiesConstants;
import com.ou.ret.security.SecurityUtils;
import com.ou.ret.service.dto.AdminUserDTO;
import com.ou.ret.service.dto.UserDTO;
import com.ou.ret.util.EncryptionUtil;
import com.ou.ret.web.rest.AccountResource;

import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;
    private final EncryptionUtil encryptionUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                }
                );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                }
                );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    return user;
                }
                );
    }

    public AdminUserDTO registerUser(AdminUserDTO userDTO, String password) {
        assertConflictingUser(userDTO);

        User newUser = convertUserDtoToUser(userDTO);
        String encryptedPassword = passwordEncoder.encode(password);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return userDTO;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
                )
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    userRepository.delete(user);
                    log.debug("Deleted User: {}", user);
                }
                      );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     */
    public void updateUser(AdminUserDTO user, String userLogin) {
        Optional<User> existedUser = userRepository.findOneByLogin(userLogin);
        if (existedUser.isPresent()) {
            Optional<User> userUsingCurrentEmail = userRepository.findOneByEmailIgnoreCase(existedUser.get().getEmail());
            if (userUsingCurrentEmail.isPresent() && !userUsingCurrentEmail.get().getId().equals(existedUser.get().getId())) {
                throw new EmailAlreadyUsedException();
            }
            convertUserDtoToUser(user, existedUser.get());
            userRepository.save(existedUser.get());
        } else {
            throw new UsernameNotFoundException("Username not found.");
        }

    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for User: {}", user);
                }
                      );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public AdminUserDTO getUserWithAuthorities() {
        Optional<User> currentLoginUser =  SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (currentLoginUser.isPresent()) {
            return convertUserToUserDto(currentLoginUser.get());
        }
        return null;
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                }
                    );
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void assertConflictingUser(AdminUserDTO userDTO) {
        String username = encryptionUtil.encrypt(userDTO.getLogin().toLowerCase());
        String email = encryptionUtil.encrypt(userDTO.getEmail().toLowerCase());
        userRepository
            .findOneByLogin(username)
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });

        userRepository
            .findOneByEmailIgnoreCase(email)
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
    }

    public User convertUserDtoToUser(AdminUserDTO userDto) {
        User user = new User();
        user.setLogin(encryptionUtil.encrypt(userDto.getLogin().toLowerCase()));
        user.setFirstName(encryptionUtil.encrypt(userDto.getFirstName()));
        user.setLastName(encryptionUtil.encrypt(userDto.getLastName()));
        user.setEmail(encryptionUtil.encrypt(Optional.of(userDto.getEmail().toLowerCase()).orElse(StringUtils.EMPTY)));
        user.setImageUrl(encryptionUtil.encrypt(userDto.getImageUrl()));
        return user;
    }

    public void convertUserDtoToUser(AdminUserDTO userDto, User user) {
        user.setLogin(encryptionUtil.encrypt(userDto.getLogin().toLowerCase()));
        user.setFirstName(encryptionUtil.encrypt(userDto.getFirstName()));
        user.setLastName(encryptionUtil.encrypt(userDto.getLastName()));
        user.setEmail(encryptionUtil.encrypt(Optional.of(userDto.getEmail().toLowerCase()).orElse(StringUtils.EMPTY)));
        user.setImageUrl(encryptionUtil.encrypt(userDto.getImageUrl()));
    }

    public AdminUserDTO convertUserToUserDto(User user) {
        AdminUserDTO userDto = new AdminUserDTO();
        // encryption properties
        userDto.setLogin(encryptionUtil.decrypt(user.getLogin()));
        userDto.setFirstName(encryptionUtil.decrypt(user.getFirstName()));
        userDto.setLastName(encryptionUtil.decrypt(user.getLastName()));
        userDto.setEmail(encryptionUtil.decrypt(user.getEmail()));
        userDto.setImageUrl(encryptionUtil.decrypt(user.getImageUrl()));
        userDto.setLastModifiedBy(encryptionUtil.decrypt(user.getLastModifiedBy()));
        // normal properties
        userDto.setId(user.getId());
        userDto.setLangKey(user.getLangKey());
        userDto.setCreatedBy(user.getCreatedBy());
        userDto.setCreatedDate(user.getCreatedDate());
        userDto.setLastModifiedDate(user.getLastModifiedDate());
        userDto.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return userDto;
    }
}
