package com.ou.ret.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ou.ret.IntegrationTest;
import com.ou.ret.domain.HomeType;
import com.ou.ret.domain.Project;
import com.ou.ret.repository.HomeTypeRepository;
import com.ou.ret.service.criteria.HomeTypeCriteria;
import com.ou.ret.service.dto.HomeTypeDTO;
import com.ou.ret.service.mapper.HomeTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HomeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HomeTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/home-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HomeTypeRepository homeTypeRepository;

    @Autowired
    private HomeTypeMapper homeTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHomeTypeMockMvc;

    private HomeType homeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomeType createEntity(EntityManager em) {
        HomeType homeType = new HomeType().typeName(DEFAULT_TYPE_NAME);
        return homeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomeType createUpdatedEntity(EntityManager em) {
        HomeType homeType = new HomeType().typeName(UPDATED_TYPE_NAME);
        return homeType;
    }

    @BeforeEach
    public void initTest() {
        homeType = createEntity(em);
    }

    @Test
    @Transactional
    void createHomeType() throws Exception {
        int databaseSizeBeforeCreate = homeTypeRepository.findAll().size();
        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);
        restHomeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        HomeType testHomeType = homeTypeList.get(homeTypeList.size() - 1);
        assertThat(testHomeType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
    }

    @Test
    @Transactional
    void createHomeTypeWithExistingId() throws Exception {
        // Create the HomeType with an existing ID
        homeType.setId(1L);
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        int databaseSizeBeforeCreate = homeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHomeTypes() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getHomeType() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get the homeType
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, homeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(homeType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getHomeTypesByIdFiltering() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        Long id = homeType.getId();

        defaultHomeTypeShouldBeFound("id.equals=" + id);
        defaultHomeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultHomeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHomeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultHomeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHomeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName equals to DEFAULT_TYPE_NAME
        defaultHomeTypeShouldBeFound("typeName.equals=" + DEFAULT_TYPE_NAME);

        // Get all the homeTypeList where typeName equals to UPDATED_TYPE_NAME
        defaultHomeTypeShouldNotBeFound("typeName.equals=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName not equals to DEFAULT_TYPE_NAME
        defaultHomeTypeShouldNotBeFound("typeName.notEquals=" + DEFAULT_TYPE_NAME);

        // Get all the homeTypeList where typeName not equals to UPDATED_TYPE_NAME
        defaultHomeTypeShouldBeFound("typeName.notEquals=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName in DEFAULT_TYPE_NAME or UPDATED_TYPE_NAME
        defaultHomeTypeShouldBeFound("typeName.in=" + DEFAULT_TYPE_NAME + "," + UPDATED_TYPE_NAME);

        // Get all the homeTypeList where typeName equals to UPDATED_TYPE_NAME
        defaultHomeTypeShouldNotBeFound("typeName.in=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName is not null
        defaultHomeTypeShouldBeFound("typeName.specified=true");

        // Get all the homeTypeList where typeName is null
        defaultHomeTypeShouldNotBeFound("typeName.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameContainsSomething() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName contains DEFAULT_TYPE_NAME
        defaultHomeTypeShouldBeFound("typeName.contains=" + DEFAULT_TYPE_NAME);

        // Get all the homeTypeList where typeName contains UPDATED_TYPE_NAME
        defaultHomeTypeShouldNotBeFound("typeName.contains=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllHomeTypesByTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        // Get all the homeTypeList where typeName does not contain DEFAULT_TYPE_NAME
        defaultHomeTypeShouldNotBeFound("typeName.doesNotContain=" + DEFAULT_TYPE_NAME);

        // Get all the homeTypeList where typeName does not contain UPDATED_TYPE_NAME
        defaultHomeTypeShouldBeFound("typeName.doesNotContain=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllHomeTypesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        homeType.addProject(project);
        homeTypeRepository.saveAndFlush(homeType);
        Long projectId = project.getId();

        // Get all the homeTypeList where project equals to projectId
        defaultHomeTypeShouldBeFound("projectId.equals=" + projectId);

        // Get all the homeTypeList where project equals to (projectId + 1)
        defaultHomeTypeShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomeTypeShouldBeFound(String filter) throws Exception {
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));

        // Check, that the count call also returns 1
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomeTypeShouldNotBeFound(String filter) throws Exception {
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHomeType() throws Exception {
        // Get the homeType
        restHomeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHomeType() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();

        // Update the homeType
        HomeType updatedHomeType = homeTypeRepository.findById(homeType.getId()).get();
        // Disconnect from session so that the updates on updatedHomeType are not directly saved in db
        em.detach(updatedHomeType);
        updatedHomeType.typeName(UPDATED_TYPE_NAME);
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(updatedHomeType);

        restHomeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, homeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
        HomeType testHomeType = homeTypeList.get(homeTypeList.size() - 1);
        assertThat(testHomeType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, homeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHomeTypeWithPatch() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();

        // Update the homeType using partial update
        HomeType partialUpdatedHomeType = new HomeType();
        partialUpdatedHomeType.setId(homeType.getId());

        partialUpdatedHomeType.typeName(UPDATED_TYPE_NAME);

        restHomeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomeType))
            )
            .andExpect(status().isOk());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
        HomeType testHomeType = homeTypeList.get(homeTypeList.size() - 1);
        assertThat(testHomeType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHomeTypeWithPatch() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();

        // Update the homeType using partial update
        HomeType partialUpdatedHomeType = new HomeType();
        partialUpdatedHomeType.setId(homeType.getId());

        partialUpdatedHomeType.typeName(UPDATED_TYPE_NAME);

        restHomeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomeType))
            )
            .andExpect(status().isOk());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
        HomeType testHomeType = homeTypeList.get(homeTypeList.size() - 1);
        assertThat(testHomeType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, homeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHomeType() throws Exception {
        int databaseSizeBeforeUpdate = homeTypeRepository.findAll().size();
        homeType.setId(count.incrementAndGet());

        // Create the HomeType
        HomeTypeDTO homeTypeDTO = homeTypeMapper.toDto(homeType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(homeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomeType in the database
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHomeType() throws Exception {
        // Initialize the database
        homeTypeRepository.saveAndFlush(homeType);

        int databaseSizeBeforeDelete = homeTypeRepository.findAll().size();

        // Delete the homeType
        restHomeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, homeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HomeType> homeTypeList = homeTypeRepository.findAll();
        assertThat(homeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
