package io.daobab.converter.json.conversion;

import io.daobab.creation.PlateCreator;
import io.daobab.model.Column;
import io.daobab.model.Plate;
import io.daobab.test.dao.SakilaTables;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestPlateJsonConversion implements SakilaTables {

    @Test
    void test() {
        Map<Column, Object> map = new HashMap<>();
        map.put(tabFilm.colLength(), 10);
        map.put(tabFilm.colDescription(), "lee? {,sdsd");
        map.put(tabFilm.colRentalRate(), new BigDecimal("234.234"));
        map.put(tabFilm.colLastUpdate(), Timestamp.valueOf(LocalDateTime.now()));
        Plate plate = PlateCreator.fromColumnMap(map);
        PlateJsonConversion fieldJsonConversion = new PlateJsonConversion(plate);

        StringBuilder sb = new StringBuilder();

        fieldJsonConversion.toJson(sb, plate);

        String json = sb.toString();
        System.out.println(json);

        Plate pt = fieldJsonConversion.fromJson(json);

        System.out.println(pt.getValue(tabFilm.colLength()));
        System.out.println(pt.getValue(tabFilm.colDescription()));
        System.out.println(pt.getValue(tabFilm.colRentalRate()));
        System.out.println(pt.getValue(tabFilm.colLastUpdate()));
    }
}
