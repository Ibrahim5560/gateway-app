package com.isoft.gateway.repository;

import com.isoft.gateway.domain.VehicleLicenseType;
import com.isoft.gateway.domain.criteria.VehicleLicenseTypeCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the VehicleLicenseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleLicenseTypeRepository
    extends ReactiveCrudRepository<VehicleLicenseType, Long>, VehicleLicenseTypeRepositoryInternal {
    Flux<VehicleLicenseType> findAllBy(Pageable pageable);

    @Override
    <S extends VehicleLicenseType> Mono<S> save(S entity);

    @Override
    Flux<VehicleLicenseType> findAll();

    @Override
    Mono<VehicleLicenseType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VehicleLicenseTypeRepositoryInternal {
    <S extends VehicleLicenseType> Mono<S> save(S entity);

    Flux<VehicleLicenseType> findAllBy(Pageable pageable);

    Flux<VehicleLicenseType> findAll();

    Mono<VehicleLicenseType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<VehicleLicenseType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<VehicleLicenseType> findByCriteria(VehicleLicenseTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(VehicleLicenseTypeCriteria criteria);
}
