package com.isoft.gateway.repository.rowmapper;

import com.isoft.gateway.domain.VehicleLicenseType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link VehicleLicenseType}, with proper type conversions.
 */
@Service
public class VehicleLicenseTypeRowMapper implements BiFunction<Row, String, VehicleLicenseType> {

    private final ColumnConverter converter;

    public VehicleLicenseTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link VehicleLicenseType} stored in the database.
     */
    @Override
    public VehicleLicenseType apply(Row row, String prefix) {
        VehicleLicenseType entity = new VehicleLicenseType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setRank(converter.fromRow(row, prefix + "_rank", Integer.class));
        entity.setEngName(converter.fromRow(row, prefix + "_eng_name", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        return entity;
    }
}
