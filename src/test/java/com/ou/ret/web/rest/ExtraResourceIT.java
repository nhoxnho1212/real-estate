package com.ou.ret.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ou.ret.IntegrationTest;
import com.ou.ret.domain.Extra;
import com.ou.ret.domain.Project;
import com.ou.ret.repository.ExtraRepository;
import com.ou.ret.service.criteria.ExtraCriteria;
import com.ou.ret.service.dto.ExtraDTO;
import com.ou.ret.service.mapper.ExtraMapper;
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
 * Integration tests for the {@link ExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraResourceIT {

    private static final String DEFAULT_EXTRA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtraRepository extraRepository;

    @Autowired
    private ExtraMapper extraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraMockMvc;

    private Extra extra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createEntity(EntityManager em) {
        Extra extra = new Extra().extraName(DEFAULT_EXTRA_NAME);
        return extra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createUpdatedEntity(EntityManager em) {
        Extra extra = new Extra().extraName(UPDATED_EXTRA_NAME);
        return extra;
    }

    @BeforeEach
    public void initTest() {
        extra = createEntity(em);
    }

    @Test
    @Transactional
    void createExtra() throws Exception {
        int databaseSizeBeforeCreate = extraRepository.findAll().size();
        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);
        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraDTO)))
            .andExpect(status().isCreated());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeCreate + 1);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getExtraName()).isEqualTo(DEFAULT_EXTRA_NAME);
    }

    @Test
    @Transactional
    void createExtraWithExistingId() throws Exception {
        // Create the Extra with an existing ID
        extra.setId(1L);
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        int databaseSizeBeforeCreate = extraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExtras() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extra.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraName").value(hasItem(DEFAULT_EXTRA_NAME)));
    }

    @Test
    @Transactional
    void getExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get the extra
        restExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, extra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extra.getId().intValue()))
            .andExpect(jsonPath("$.extraName").value(DEFAULT_EXTRA_NAME));
    }

    @Test
    @Transactional
    void getExtrasByIdFiltering() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        Long id = extra.getId();

        defaultExtraShouldBeFound("id.equals=" + id);
        defaultExtraShouldNotBeFound("id.notEquals=" + id);

        defaultExtraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExtraShouldNotBeFound("id.greaterThan=" + id);

        defaultExtraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExtraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameIsEqualToSomething() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName equals to DEFAULT_EXTRA_NAME
        defaultExtraShouldBeFound("extraName.equals=" + DEFAULT_EXTRA_NAME);

        // Get all the extraList where extraName equals to UPDATED_EXTRA_NAME
        defaultExtraShouldNotBeFound("extraName.equals=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName not equals to DEFAULT_EXTRA_NAME
        defaultExtraShouldNotBeFound("extraName.notEquals=" + DEFAULT_EXTRA_NAME);

        // Get all the extraList where extraName not equals to UPDATED_EXTRA_NAME
        defaultExtraShouldBeFound("extraName.notEquals=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameIsInShouldWork() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName in DEFAULT_EXTRA_NAME or UPDATED_EXTRA_NAME
        defaultExtraShouldBeFound("extraName.in=" + DEFAULT_EXTRA_NAME + "," + UPDATED_EXTRA_NAME);

        // Get all the extraList where extraName equals to UPDATED_EXTRA_NAME
        defaultExtraShouldNotBeFound("extraName.in=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName is not null
        defaultExtraShouldBeFound("extraName.specified=true");

        // Get all the extraList where extraName is null
        defaultExtraShouldNotBeFound("extraName.specified=false");
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameContainsSomething() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName contains DEFAULT_EXTRA_NAME
        defaultExtraShouldBeFound("extraName.contains=" + DEFAULT_EXTRA_NAME);

        // Get all the extraList where extraName contains UPDATED_EXTRA_NAME
        defaultExtraShouldNotBeFound("extraName.contains=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void getAllExtrasByExtraNameNotContainsSomething() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        // Get all the extraList where extraName does not contain DEFAULT_EXTRA_NAME
        defaultExtraShouldNotBeFound("extraName.doesNotContain=" + DEFAULT_EXTRA_NAME);

        // Get all the extraList where extraName does not contain UPDATED_EXTRA_NAME
        defaultExtraShouldBeFound("extraName.doesNotContain=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void getAllExtrasByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        extra.addProject(project);
        extraRepository.saveAndFlush(extra);
        Long projectId = project.getId();

        // Get all the extraList where project equals to projectId
        defaultExtraShouldBeFound("projectId.equals=" + projectId);

        // Get all the extraList where project equals to (projectId + 1)
        defaultExtraShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExtraShouldBeFound(String filter) throws Exception {
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extra.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraName").value(hasItem(DEFAULT_EXTRA_NAME)));

        // Check, that the count call also returns 1
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExtraShouldNotBeFound(String filter) throws Exception {
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExtra() throws Exception {
        // Get the extra
        restExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra
        Extra updatedExtra = extraRepository.findById(extra.getId()).get();
        // Disconnect from session so that the updates on updatedExtra are not directly saved in db
        em.detach(updatedExtra);
        updatedExtra.extraName(UPDATED_EXTRA_NAME);
        ExtraDTO extraDTO = extraMapper.toDto(updatedExtra);

        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getExtraName()).isEqualTo(UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void putNonExistingExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getExtraName()).isEqualTo(DEFAULT_EXTRA_NAME);
    }

    @Test
    @Transactional
    void fullUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeUpdate = extraRepository.findAll().size();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        partialUpdatedExtra.extraName(UPDATED_EXTRA_NAME);

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
        Extra testExtra = extraList.get(extraList.size() - 1);
        assertThat(testExtra.getExtraName()).isEqualTo(UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtra() throws Exception {
        int databaseSizeBeforeUpdate = extraRepository.findAll().size();
        extra.setId(count.incrementAndGet());

        // Create the Extra
        ExtraDTO extraDTO = extraMapper.toDto(extra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(extraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtra() throws Exception {
        // Initialize the database
        extraRepository.saveAndFlush(extra);

        int databaseSizeBeforeDelete = extraRepository.findAll().size();

        // Delete the extra
        restExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, extra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Extra> extraList = extraRepository.findAll();
        assertThat(extraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
