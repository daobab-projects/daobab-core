package io.daobab.test.dao.table.enhanced;

import io.daobab.model.Column;
import io.daobab.model.EnhancedEntity;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryJoin;
import io.daobab.test.dao.table.City;
import io.daobab.test.dao.table.Country;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountryCity extends City implements
        io.daobab.test.dao.column.Country<City>,
        EnhancedEntity {

    private static final Country tabCountry = new Country();


    @Override
    public List<Column> joinedColumns() {
        return Collections.singletonList(
                tabCountry.colCountry()
        );
    }


    @Override
    public <Q extends Query & QueryJoin<Q>> Q enhanceQuery(Q query) {
        return query
                .join(tabCountry, colCountryId());
    }
}
