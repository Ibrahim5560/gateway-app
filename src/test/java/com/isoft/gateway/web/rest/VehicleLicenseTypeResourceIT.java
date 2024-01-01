package com.isoft.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.isoft.gateway.IntegrationTest;
import com.isoft.gateway.domain.VehicleLicenseType;
import com.isoft.gateway.repository.EntityManager;
import com.isoft.gateway.repository.VehicleLicenseTypeRepository;
import com.isoft.gateway.service.dto.VehicleLicenseTypeDTO;
import com.isoft.gateway.service.mapper.VehicleLicenseTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link VehicleLicenseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VehicleLicenseTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;
    private static final Integer SMALLER_RANK = 1 - 1;

    private static final String DEFAULT_ENG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-license-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    @Autowired
    private VehicleLicenseTypeMapper vehicleLicenseTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private VehicleLicenseType vehicleLicenseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleLicenseType createEntity(EntityManager em) {
        VehicleLicenseType vehicleLicenseType = new VehicleLicenseType()
            .name(DEFAULT_NAME)
            .rank(DEFAULT_RANK)
            .engName(DEFAULT_ENG_NAME)
            .code(DEFAULT_CODE);
        return vehicleLicenseType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleLicenseType createUpdatedEntity(EntityManager em) {
        VehicleLicenseType vehicleLicenseType = new VehicleLicenseType()
            .name(UPDATED_NAME)
            .rank(UPDATED_RANK)
            .engName(UPDATED_ENG_NAME)
            .code(UPDATED_CODE);
        return vehicleLicenseType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(VehicleLicenseType.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        vehicleLicenseType = createEntity(em);
    }

    @Test
    void createVehicleLicenseType() throws Exception {
        int databaseSizeBeforeCreate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(DEFAULT_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    void createVehicleLicenseTypeWithExistingId() throws Exception {
        // Create the VehicleLicenseType with an existing ID
        vehicleLicenseType.setId(1L);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        int databaseSizeBeforeCreate = vehicleLicenseTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVehicleLicenseTypes() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(vehicleLicenseType.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].rank")
            .value(hasItem(DEFAULT_RANK))
            .jsonPath("$.[*].engName")
            .value(hasItem(DEFAULT_ENG_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE));
    }

    @Test
    void getVehicleLicenseType() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get the vehicleLicenseType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, vehicleLicenseType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(vehicleLicenseType.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.rank")
            .value(is(DEFAULT_RANK))
            .jsonPath("$.engName")
            .value(is(DEFAULT_ENG_NAME))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE));
    }

    @Test
    void getVehicleLicenseTypesByIdFiltering() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        Long id = vehicleLicenseType.getId();

        defaultVehicleLicenseTypeShouldBeFound("id.equals=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.notEquals=" + id);

        defaultVehicleLicenseTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultVehicleLicenseTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllVehicleLicenseTypesByNameIsEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where name equals to DEFAULT_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name equals to UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByNameIsInShouldWork() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vehicleLicenseTypeList where name equals to UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByNameIsNullOrNotNull() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where name is not null
        defaultVehicleLicenseTypeShouldBeFound("name.specified=true");

        // Get all the vehicleLicenseTypeList where name is null
        defaultVehicleLicenseTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllVehicleLicenseTypesByNameContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where name contains DEFAULT_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name contains UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByNameNotContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where name does not contain DEFAULT_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name does not contain UPDATED_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank equals to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank equals to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsInShouldWork() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the vehicleLicenseTypeList where rank equals to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsNullOrNotNull() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank is not null
        defaultVehicleLicenseTypeShouldBeFound("rank.specified=true");

        // Get all the vehicleLicenseTypeList where rank is null
        defaultVehicleLicenseTypeShouldNotBeFound("rank.specified=false");
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank is greater than or equal to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.greaterThanOrEqual=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is greater than or equal to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.greaterThanOrEqual=" + UPDATED_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsLessThanOrEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank is less than or equal to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.lessThanOrEqual=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is less than or equal to SMALLER_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.lessThanOrEqual=" + SMALLER_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsLessThanSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank is less than DEFAULT_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.lessThan=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is less than UPDATED_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.lessThan=" + UPDATED_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByRankIsGreaterThanSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where rank is greater than DEFAULT_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.greaterThan=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is greater than SMALLER_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.greaterThan=" + SMALLER_RANK);
    }

    @Test
    void getAllVehicleLicenseTypesByEngNameIsEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where engName equals to DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.equals=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName equals to UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.equals=" + UPDATED_ENG_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByEngNameIsInShouldWork() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where engName in DEFAULT_ENG_NAME or UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.in=" + DEFAULT_ENG_NAME + "," + UPDATED_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName equals to UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.in=" + UPDATED_ENG_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByEngNameIsNullOrNotNull() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where engName is not null
        defaultVehicleLicenseTypeShouldBeFound("engName.specified=true");

        // Get all the vehicleLicenseTypeList where engName is null
        defaultVehicleLicenseTypeShouldNotBeFound("engName.specified=false");
    }

    @Test
    void getAllVehicleLicenseTypesByEngNameContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where engName contains DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.contains=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName contains UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.contains=" + UPDATED_ENG_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByEngNameNotContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where engName does not contain DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.doesNotContain=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName does not contain UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.doesNotContain=" + UPDATED_ENG_NAME);
    }

    @Test
    void getAllVehicleLicenseTypesByCodeIsEqualToSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where code equals to DEFAULT_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code equals to UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    void getAllVehicleLicenseTypesByCodeIsInShouldWork() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the vehicleLicenseTypeList where code equals to UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    void getAllVehicleLicenseTypesByCodeIsNullOrNotNull() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where code is not null
        defaultVehicleLicenseTypeShouldBeFound("code.specified=true");

        // Get all the vehicleLicenseTypeList where code is null
        defaultVehicleLicenseTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    void getAllVehicleLicenseTypesByCodeContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where code contains DEFAULT_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code contains UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    void getAllVehicleLicenseTypesByCodeNotContainsSomething() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        // Get all the vehicleLicenseTypeList where code does not contain DEFAULT_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code does not contain UPDATED_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleLicenseTypeShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(vehicleLicenseType.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].rank")
            .value(hasItem(DEFAULT_RANK))
            .jsonPath("$.[*].engName")
            .value(hasItem(DEFAULT_ENG_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleLicenseTypeShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingVehicleLicenseType() {
        // Get the vehicleLicenseType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingVehicleLicenseType() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();

        // Update the vehicleLicenseType
        VehicleLicenseType updatedVehicleLicenseType = vehicleLicenseTypeRepository.findById(vehicleLicenseType.getId()).block();
        updatedVehicleLicenseType.name(UPDATED_NAME).rank(UPDATED_RANK).engName(UPDATED_ENG_NAME).code(UPDATED_CODE);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(updatedVehicleLicenseType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(UPDATED_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    void putNonExistingVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVehicleLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();

        // Update the vehicleLicenseType using partial update
        VehicleLicenseType partialUpdatedVehicleLicenseType = new VehicleLicenseType();
        partialUpdatedVehicleLicenseType.setId(vehicleLicenseType.getId());

        partialUpdatedVehicleLicenseType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicleLicenseType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleLicenseType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(DEFAULT_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    void fullUpdateVehicleLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();

        // Update the vehicleLicenseType using partial update
        VehicleLicenseType partialUpdatedVehicleLicenseType = new VehicleLicenseType();
        partialUpdatedVehicleLicenseType.setId(vehicleLicenseType.getId());

        partialUpdatedVehicleLicenseType.name(UPDATED_NAME).rank(UPDATED_RANK).engName(UPDATED_ENG_NAME).code(UPDATED_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVehicleLicenseType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleLicenseType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(UPDATED_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    void patchNonExistingVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().collectList().block().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVehicleLicenseType() {
        // Initialize the database
        vehicleLicenseTypeRepository.save(vehicleLicenseType).block();

        int databaseSizeBeforeDelete = vehicleLicenseTypeRepository.findAll().collectList().block().size();

        // Delete the vehicleLicenseType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, vehicleLicenseType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll().collectList().block();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
