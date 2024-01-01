package com.isoft.gateway.service;

import com.isoft.gateway.domain.criteria.VehicleLicenseTypeCriteria;
import com.isoft.gateway.service.dto.VehicleLicenseTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.isoft.gateway.domain.VehicleLicenseType}.
 */
public interface VehicleLicenseTypeService {
    /**
     * Save a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<VehicleLicenseTypeDTO> save(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Updates a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<VehicleLicenseTypeDTO> update(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Partially updates a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<VehicleLicenseTypeDTO> partialUpdate(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Get all the vehicleLicenseTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VehicleLicenseTypeDTO> findAll(Pageable pageable);

    /**
     * Find vehicleLicenseTypes by criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VehicleLicenseTypeDTO> findByCriteria(VehicleLicenseTypeCriteria criteria, Pageable pageable);

    /**
     * Find the count of vehicleLicenseTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of vehicleLicenseTypes
     */
    public Mono<Long> countByCriteria(VehicleLicenseTypeCriteria criteria);

    /**
     * Returns the number of vehicleLicenseTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" vehicleLicenseType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<VehicleLicenseTypeDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleLicenseType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
