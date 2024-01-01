package com.isoft.gateway.service.impl;

import com.isoft.gateway.domain.criteria.VehicleLicenseTypeCriteria;
import com.isoft.gateway.repository.VehicleLicenseTypeRepository;
import com.isoft.gateway.service.VehicleLicenseTypeService;
import com.isoft.gateway.service.dto.VehicleLicenseTypeDTO;
import com.isoft.gateway.service.mapper.VehicleLicenseTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.isoft.gateway.domain.VehicleLicenseType}.
 */
@Service
@Transactional
public class VehicleLicenseTypeServiceImpl implements VehicleLicenseTypeService {

    private final Logger log = LoggerFactory.getLogger(VehicleLicenseTypeServiceImpl.class);

    private final VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    private final VehicleLicenseTypeMapper vehicleLicenseTypeMapper;

    public VehicleLicenseTypeServiceImpl(
        VehicleLicenseTypeRepository vehicleLicenseTypeRepository,
        VehicleLicenseTypeMapper vehicleLicenseTypeMapper
    ) {
        this.vehicleLicenseTypeRepository = vehicleLicenseTypeRepository;
        this.vehicleLicenseTypeMapper = vehicleLicenseTypeMapper;
    }

    @Override
    public Mono<VehicleLicenseTypeDTO> save(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to save VehicleLicenseType : {}", vehicleLicenseTypeDTO);
        return vehicleLicenseTypeRepository
            .save(vehicleLicenseTypeMapper.toEntity(vehicleLicenseTypeDTO))
            .map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    public Mono<VehicleLicenseTypeDTO> update(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to update VehicleLicenseType : {}", vehicleLicenseTypeDTO);
        return vehicleLicenseTypeRepository
            .save(vehicleLicenseTypeMapper.toEntity(vehicleLicenseTypeDTO))
            .map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    public Mono<VehicleLicenseTypeDTO> partialUpdate(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to partially update VehicleLicenseType : {}", vehicleLicenseTypeDTO);

        return vehicleLicenseTypeRepository
            .findById(vehicleLicenseTypeDTO.getId())
            .map(existingVehicleLicenseType -> {
                vehicleLicenseTypeMapper.partialUpdate(existingVehicleLicenseType, vehicleLicenseTypeDTO);

                return existingVehicleLicenseType;
            })
            .flatMap(vehicleLicenseTypeRepository::save)
            .map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VehicleLicenseTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleLicenseTypes");
        return vehicleLicenseTypeRepository.findAllBy(pageable).map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VehicleLicenseTypeDTO> findByCriteria(VehicleLicenseTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all VehicleLicenseTypes by Criteria");
        return vehicleLicenseTypeRepository.findByCriteria(criteria, pageable).map(vehicleLicenseTypeMapper::toDto);
    }

    /**
     * Find the count of vehicleLicenseTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of vehicleLicenseTypes
     */
    public Mono<Long> countByCriteria(VehicleLicenseTypeCriteria criteria) {
        log.debug("Request to get the count of all VehicleLicenseTypes by Criteria");
        return vehicleLicenseTypeRepository.countByCriteria(criteria);
    }

    public Mono<Long> countAll() {
        return vehicleLicenseTypeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<VehicleLicenseTypeDTO> findOne(Long id) {
        log.debug("Request to get VehicleLicenseType : {}", id);
        return vehicleLicenseTypeRepository.findById(id).map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete VehicleLicenseType : {}", id);
        return vehicleLicenseTypeRepository.deleteById(id);
    }
}
