package com.ou.ret.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ou.ret.IntegrationTest;
import com.ou.ret.domain.Extra;
import com.ou.ret.domain.HomeType;
import com.ou.ret.domain.Project;
import com.ou.ret.repository.ProjectRepository;
import com.ou.ret.service.ProjectService;
import com.ou.ret.service.criteria.ProjectCriteria;
import com.ou.ret.service.dto.ProjectDTO;
import com.ou.ret.service.mapper.ProjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;
    private static final Integer SMALLER_ROOMS = 1 - 1;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Float DEFAULT_FLOOR_SPACE = 1F;
    private static final Float UPDATED_FLOOR_SPACE = 2F;
    private static final Float SMALLER_FLOOR_SPACE = 1F - 1F;

    private static final String DEFAULT_ATTACHMENT = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Autowired
    private ProjectMapper projectMapper;

    @Mock
    private ProjectService projectServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .rooms(DEFAULT_ROOMS)
            .price(DEFAULT_PRICE)
            .floorSpace(DEFAULT_FLOOR_SPACE)
            .attachment(DEFAULT_ATTACHMENT);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .rooms(UPDATED_ROOMS)
            .price(UPDATED_PRICE)
            .floorSpace(UPDATED_FLOOR_SPACE)
            .attachment(UPDATED_ATTACHMENT);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProject.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProject.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testProject.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProject.getFloorSpace()).isEqualTo(DEFAULT_FLOOR_SPACE);
        assertThat(testProject.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);
        ProjectDTO projectDTO = projectMapper.toDto(project);

        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].floorSpace").value(hasItem(DEFAULT_FLOOR_SPACE.doubleValue())))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(DEFAULT_ATTACHMENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.floorSpace").value(DEFAULT_FLOOR_SPACE.doubleValue()))
            .andExpect(jsonPath("$.attachment").value(DEFAULT_ATTACHMENT));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address equals to DEFAULT_ADDRESS
        defaultProjectShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the projectList where address equals to UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address not equals to DEFAULT_ADDRESS
        defaultProjectShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the projectList where address not equals to UPDATED_ADDRESS
        defaultProjectShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultProjectShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the projectList where address equals to UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address is not null
        defaultProjectShouldBeFound("address.specified=true");

        // Get all the projectList where address is null
        defaultProjectShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByAddressContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address contains DEFAULT_ADDRESS
        defaultProjectShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the projectList where address contains UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address does not contain DEFAULT_ADDRESS
        defaultProjectShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the projectList where address does not contain UPDATED_ADDRESS
        defaultProjectShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city equals to DEFAULT_CITY
        defaultProjectShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the projectList where city equals to UPDATED_CITY
        defaultProjectShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllProjectsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city not equals to DEFAULT_CITY
        defaultProjectShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the projectList where city not equals to UPDATED_CITY
        defaultProjectShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllProjectsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city in DEFAULT_CITY or UPDATED_CITY
        defaultProjectShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the projectList where city equals to UPDATED_CITY
        defaultProjectShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllProjectsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city is not null
        defaultProjectShouldBeFound("city.specified=true");

        // Get all the projectList where city is null
        defaultProjectShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCityContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city contains DEFAULT_CITY
        defaultProjectShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the projectList where city contains UPDATED_CITY
        defaultProjectShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllProjectsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where city does not contain DEFAULT_CITY
        defaultProjectShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the projectList where city does not contain UPDATED_CITY
        defaultProjectShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms equals to DEFAULT_ROOMS
        defaultProjectShouldBeFound("rooms.equals=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms equals to UPDATED_ROOMS
        defaultProjectShouldNotBeFound("rooms.equals=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms not equals to DEFAULT_ROOMS
        defaultProjectShouldNotBeFound("rooms.notEquals=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms not equals to UPDATED_ROOMS
        defaultProjectShouldBeFound("rooms.notEquals=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms in DEFAULT_ROOMS or UPDATED_ROOMS
        defaultProjectShouldBeFound("rooms.in=" + DEFAULT_ROOMS + "," + UPDATED_ROOMS);

        // Get all the projectList where rooms equals to UPDATED_ROOMS
        defaultProjectShouldNotBeFound("rooms.in=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms is not null
        defaultProjectShouldBeFound("rooms.specified=true");

        // Get all the projectList where rooms is null
        defaultProjectShouldNotBeFound("rooms.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms is greater than or equal to DEFAULT_ROOMS
        defaultProjectShouldBeFound("rooms.greaterThanOrEqual=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms is greater than or equal to UPDATED_ROOMS
        defaultProjectShouldNotBeFound("rooms.greaterThanOrEqual=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms is less than or equal to DEFAULT_ROOMS
        defaultProjectShouldBeFound("rooms.lessThanOrEqual=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms is less than or equal to SMALLER_ROOMS
        defaultProjectShouldNotBeFound("rooms.lessThanOrEqual=" + SMALLER_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms is less than DEFAULT_ROOMS
        defaultProjectShouldNotBeFound("rooms.lessThan=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms is less than UPDATED_ROOMS
        defaultProjectShouldBeFound("rooms.lessThan=" + UPDATED_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByRoomsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rooms is greater than DEFAULT_ROOMS
        defaultProjectShouldNotBeFound("rooms.greaterThan=" + DEFAULT_ROOMS);

        // Get all the projectList where rooms is greater than SMALLER_ROOMS
        defaultProjectShouldBeFound("rooms.greaterThan=" + SMALLER_ROOMS);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price equals to DEFAULT_PRICE
        defaultProjectShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the projectList where price equals to UPDATED_PRICE
        defaultProjectShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price not equals to DEFAULT_PRICE
        defaultProjectShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the projectList where price not equals to UPDATED_PRICE
        defaultProjectShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProjectShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the projectList where price equals to UPDATED_PRICE
        defaultProjectShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price is not null
        defaultProjectShouldBeFound("price.specified=true");

        // Get all the projectList where price is null
        defaultProjectShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price is greater than or equal to DEFAULT_PRICE
        defaultProjectShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the projectList where price is greater than or equal to UPDATED_PRICE
        defaultProjectShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price is less than or equal to DEFAULT_PRICE
        defaultProjectShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the projectList where price is less than or equal to SMALLER_PRICE
        defaultProjectShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price is less than DEFAULT_PRICE
        defaultProjectShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the projectList where price is less than UPDATED_PRICE
        defaultProjectShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where price is greater than DEFAULT_PRICE
        defaultProjectShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the projectList where price is greater than SMALLER_PRICE
        defaultProjectShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace equals to DEFAULT_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.equals=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace equals to UPDATED_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.equals=" + UPDATED_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace not equals to DEFAULT_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.notEquals=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace not equals to UPDATED_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.notEquals=" + UPDATED_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace in DEFAULT_FLOOR_SPACE or UPDATED_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.in=" + DEFAULT_FLOOR_SPACE + "," + UPDATED_FLOOR_SPACE);

        // Get all the projectList where floorSpace equals to UPDATED_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.in=" + UPDATED_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace is not null
        defaultProjectShouldBeFound("floorSpace.specified=true");

        // Get all the projectList where floorSpace is null
        defaultProjectShouldNotBeFound("floorSpace.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace is greater than or equal to DEFAULT_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.greaterThanOrEqual=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace is greater than or equal to UPDATED_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.greaterThanOrEqual=" + UPDATED_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace is less than or equal to DEFAULT_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.lessThanOrEqual=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace is less than or equal to SMALLER_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.lessThanOrEqual=" + SMALLER_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace is less than DEFAULT_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.lessThan=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace is less than UPDATED_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.lessThan=" + UPDATED_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByFloorSpaceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where floorSpace is greater than DEFAULT_FLOOR_SPACE
        defaultProjectShouldNotBeFound("floorSpace.greaterThan=" + DEFAULT_FLOOR_SPACE);

        // Get all the projectList where floorSpace is greater than SMALLER_FLOOR_SPACE
        defaultProjectShouldBeFound("floorSpace.greaterThan=" + SMALLER_FLOOR_SPACE);
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment equals to DEFAULT_ATTACHMENT
        defaultProjectShouldBeFound("attachment.equals=" + DEFAULT_ATTACHMENT);

        // Get all the projectList where attachment equals to UPDATED_ATTACHMENT
        defaultProjectShouldNotBeFound("attachment.equals=" + UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment not equals to DEFAULT_ATTACHMENT
        defaultProjectShouldNotBeFound("attachment.notEquals=" + DEFAULT_ATTACHMENT);

        // Get all the projectList where attachment not equals to UPDATED_ATTACHMENT
        defaultProjectShouldBeFound("attachment.notEquals=" + UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment in DEFAULT_ATTACHMENT or UPDATED_ATTACHMENT
        defaultProjectShouldBeFound("attachment.in=" + DEFAULT_ATTACHMENT + "," + UPDATED_ATTACHMENT);

        // Get all the projectList where attachment equals to UPDATED_ATTACHMENT
        defaultProjectShouldNotBeFound("attachment.in=" + UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment is not null
        defaultProjectShouldBeFound("attachment.specified=true");

        // Get all the projectList where attachment is null
        defaultProjectShouldNotBeFound("attachment.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment contains DEFAULT_ATTACHMENT
        defaultProjectShouldBeFound("attachment.contains=" + DEFAULT_ATTACHMENT);

        // Get all the projectList where attachment contains UPDATED_ATTACHMENT
        defaultProjectShouldNotBeFound("attachment.contains=" + UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByAttachmentNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where attachment does not contain DEFAULT_ATTACHMENT
        defaultProjectShouldNotBeFound("attachment.doesNotContain=" + DEFAULT_ATTACHMENT);

        // Get all the projectList where attachment does not contain UPDATED_ATTACHMENT
        defaultProjectShouldBeFound("attachment.doesNotContain=" + UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByExtraIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Extra extra = ExtraResourceIT.createEntity(em);
        em.persist(extra);
        em.flush();
        project.addExtra(extra);
        projectRepository.saveAndFlush(project);
        Long extraId = extra.getId();

        // Get all the projectList where extra equals to extraId
        defaultProjectShouldBeFound("extraId.equals=" + extraId);

        // Get all the projectList where extra equals to (extraId + 1)
        defaultProjectShouldNotBeFound("extraId.equals=" + (extraId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByHomeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        HomeType homeType = HomeTypeResourceIT.createEntity(em);
        em.persist(homeType);
        em.flush();
        project.setHomeType(homeType);
        projectRepository.saveAndFlush(project);
        Long homeTypeId = homeType.getId();

        // Get all the projectList where homeType equals to homeTypeId
        defaultProjectShouldBeFound("homeTypeId.equals=" + homeTypeId);

        // Get all the projectList where homeType equals to (homeTypeId + 1)
        defaultProjectShouldNotBeFound("homeTypeId.equals=" + (homeTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].floorSpace").value(hasItem(DEFAULT_FLOOR_SPACE.doubleValue())))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(DEFAULT_ATTACHMENT)));

        // Check, that the count call also returns 1
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .rooms(UPDATED_ROOMS)
            .price(UPDATED_PRICE)
            .floorSpace(UPDATED_FLOOR_SPACE)
            .attachment(UPDATED_ATTACHMENT);
        ProjectDTO projectDTO = projectMapper.toDto(updatedProject);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProject.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testProject.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProject.getFloorSpace()).isEqualTo(UPDATED_FLOOR_SPACE);
        assertThat(testProject.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject.address(UPDATED_ADDRESS).price(UPDATED_PRICE).floorSpace(UPDATED_FLOOR_SPACE);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProject.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testProject.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProject.getFloorSpace()).isEqualTo(UPDATED_FLOOR_SPACE);
        assertThat(testProject.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .rooms(UPDATED_ROOMS)
            .price(UPDATED_PRICE)
            .floorSpace(UPDATED_FLOOR_SPACE)
            .attachment(UPDATED_ATTACHMENT);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProject.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testProject.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProject.getFloorSpace()).isEqualTo(UPDATED_FLOOR_SPACE);
        assertThat(testProject.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
