package com.isoft.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VehicleLicenseTypeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("rank", table, columnPrefix + "_rank"));
        columns.add(Column.aliased("eng_name", table, columnPrefix + "_eng_name"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));

        return columns;
    }
}
