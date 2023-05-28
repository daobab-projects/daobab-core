package io.daobab.converter.json.conversion;

import io.daobab.test.dao.SakilaTables;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class TestFieldJsonConversion implements SakilaTables {

    @Test
    void test(){
        FieldJsonConversion fieldJsonConversion=new FieldJsonConversion(tabActor.colActorId());

        StringBuilder sb=new StringBuilder();

        Timestamp ts1=new Timestamp((new Date()).getTime());
        Timestamp ts2=new Timestamp((new Date()).getTime());

        sb.append("{");
        fieldJsonConversion.toJson(sb, Arrays.asList(3,4,null,55));
        sb.append("}");
        System.out.println(sb);
    }
}
