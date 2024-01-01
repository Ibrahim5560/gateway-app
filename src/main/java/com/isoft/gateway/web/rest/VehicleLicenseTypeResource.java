package com.isoft.gateway.web.rest;

import com.isoft.gateway.domain.criteria.VehicleLicenseTypeCriteria;
import com.isoft.gateway.repository.VehicleLicenseTypeRepository;
import com.isoft.gateway.service.VehicleLicenseTypeService;
import com.isoft.gateway.service.dto.VehicleLicenseTypeDTO;
import com.isoft.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.isoft.gateway.domain.VehicleLicenseType}.
 */
@RestController
@RequestMapping("/api/vehicle-license-types")
public class VehicleLicenseTypeResource {

    private final Logger log = LoggerFactory.getLogger(VehicleLicenseTypeResource.class);

    private static final String ENTITY_NAME = "vehicleLicenseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleLicenseTypeService vehicleLicenseTypeService;

    private final VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    public VehicleLicenseTypeResource(
        VehicleLicenseTypeService vehicleLicenseTypeService,
        VehicleLicenseTypeRepository vehicleLicenseTypeRepository
    ) {
        this.vehicleLicenseTypeService = vehicleLicenseTypeService;
        this.vehicleLicenseTypeRepository = vehicleLicenseTypeRepository;
    }

    /**
     * {@code POST  /vehicle-license-types} : Create a new vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the vehicleLicenseTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleLicenseTypeDTO, or with status {@code 400 (Bad Request)} if the vehicleLicenseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<VehicleLicenseTypeDTO>> createVehicleLicenseType(@RequestBody VehicleLicenseTypeDTO vehicleLicenseTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleLicenseType : {}", vehicleLicenseTypeDTO);
        if (vehicleLicenseTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleLicenseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return vehicleLicenseTypeService
            .save(vehicleLicenseTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/vehicle-license-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /vehicle-license-types/:id} : Updates an existing vehicleLicenseType.
     *
     * @param id the id of the vehicleLicenseTypeDTO to save.
     * @param vehicleLicenseTypeDTO the vehicleLicenseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleLicenseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleLicenseTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleLicenseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VehicleLicenseTypeDTO>> updateVehicleLicenseType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleLicenseTypeDTO vehicleLicenseTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleLicenseType : {}, {}", id, vehicleLicenseTypeDTO);
        if (vehicleLicenseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleLicenseTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vehicleLicenseTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return vehicleLicenseTypeService
                    .update(vehicleLicenseTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /vehicle-license-types/:id} : Partial updates given fields of an existing vehicleLicenseType, field will ignore if it is null
     *
     * @param id the id of the vehicleLicenseTypeDTO to save.
     * @param vehicleLicenseTypeDTO the vehicleLicenseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleLicenseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleLicenseTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleLicenseTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleLicenseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<VehicleLicenseTypeDTO>> partialUpdateVehicleLicenseType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleLicenseTypeDTO vehicleLicenseTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleLicenseType partially : {}, {}", id, vehicleLicenseTypeDTO);
        if (vehicleLicenseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleLicenseTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vehicleLicenseTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<VehicleLicenseTypeDTO> result = vehicleLicenseTypeService.partialUpdate(vehicleLicenseTypeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /vehicle-license-types} : get all the vehicleLicenseTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleLicenseTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<VehicleLicenseTypeDTO>>> getAllVehicleLicenseTypes(
        VehicleLicenseTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get VehicleLicenseTypes by criteria: {}", criteria);
        return vehicleLicenseTypeService
            .countByCriteria(criteria)
            .zipWith(vehicleLicenseTypeService.findByCriteria(criteria, pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /vehicle-license-types/count} : count all the vehicleLicenseTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countVehicleLicenseTypes(VehicleLicenseTypeCriteria criteria) {
        log.debug("REST request to count VehicleLicenseTypes by criteria: {}", criteria);
        return vehicleLicenseTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /vehicle-license-types/:id} : get the "id" vehicleLicenseType.
     *
     * @param id the id of the vehicleLicenseTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleLicenseTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VehicleLicenseTypeDTO>> getVehicleLicenseType(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleLicenseType : {}", id);
        Mono<VehicleLicenseTypeDTO> vehicleLicenseTypeDTO = vehicleLicenseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleLicenseTypeDTO);
    }

    /**
     * {@code DELETE  /vehicle-license-types/:id} : delete the "id" vehicleLicenseType.
     *
     * @param id the id of the vehicleLicenseTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVehicleLicenseType(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleLicenseType : {}", id);
        return vehicleLicenseTypeService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
