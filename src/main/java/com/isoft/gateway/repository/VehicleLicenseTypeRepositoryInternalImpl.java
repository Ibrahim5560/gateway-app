package com.isoft.gateway.repository;

import com.isoft.gateway.domain.VehicleLicenseType;
import com.isoft.gateway.domain.criteria.VehicleLicenseTypeCriteria;
import com.isoft.gateway.repository.rowmapper.ColumnConverter;
import com.isoft.gateway.repository.rowmapper.VehicleLicenseTypeRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the VehicleLicenseType entity.
 */
@SuppressWarnings("unused")
class VehicleLicenseTypeRepositoryInternalImpl
    extends SimpleR2dbcRepository<VehicleLicenseType, Long>
    implements VehicleLicenseTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final VehicleLicenseTypeRowMapper vehiclelicensetypeMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("vehicle_license_type", EntityManager.ENTITY_ALIAS);

    public VehicleLicenseTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        VehicleLicenseTypeRowMapper vehiclelicensetypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(VehicleLicenseType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.vehiclelicensetypeMapper = vehiclelicensetypeMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<VehicleLicenseType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<VehicleLicenseType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = VehicleLicenseTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, VehicleLicenseType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<VehicleLicenseType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<VehicleLicenseType> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private VehicleLicenseType process(Row row, RowMetadata metadata) {
        VehicleLicenseType entity = vehiclelicensetypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends VehicleLicenseType> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<VehicleLicenseType> findByCriteria(VehicleLicenseTypeCriteria vehicleLicenseTypeCriteria, Pageable page) {
        return createQuery(page, buildConditions(vehicleLicenseTypeCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(VehicleLicenseTypeCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(VehicleLicenseTypeCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getRank() != null) {
                builder.buildFilterConditionForField(criteria.getRank(), entityTable.column("rank"));
            }
            if (criteria.getEngName() != null) {
                builder.buildFilterConditionForField(criteria.getEngName(), entityTable.column("eng_name"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
        }
        return builder.buildConditions();
    }
}
