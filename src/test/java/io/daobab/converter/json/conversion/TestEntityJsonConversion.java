package io.daobab.converter.json.conversion;

import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Actor;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class TestEntityJsonConversion implements SakilaTables {

    @Test
    void test(){
        EntityJsonConversion fieldJsonConversion=new EntityJsonConversion(tabActor);

        StringBuilder sb=new StringBuilder();

        Timestamp ts1=new Timestamp((new Date()).getTime());
        Timestamp ts2=new Timestamp((new Date()).getTime());

        Actor actor=new Actor();
        actor.setActorId(1).setFirstName("Klaudiusz").setLastName("Wojtkowiak");

        Actor actor2=new Actor();
        actor2.setActorId(2).setFirstName("Amiga").setLastName("Rulez");

        sb.append("{");
        fieldJsonConversion.toJson(sb, "myarr",Arrays.asList(actor,actor2));
        sb.append("}");
        System.out.println(sb.toString());
    }
}
