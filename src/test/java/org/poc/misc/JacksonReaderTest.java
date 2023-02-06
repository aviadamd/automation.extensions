package org.poc.misc;

import lombok.extern.slf4j.Slf4j;
import org.files.jsonReader.JacksonHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.System.getProperty;

@Slf4j
public class JacksonReaderTest {

    @Test
    public void readJsonTest() {
        JacksonHelper<ObjectPojo> jacksonHelper = new JacksonHelper<>(getProperty("user.dir") + "/src/test/resources/json", "someJson.json", ObjectPojo.class);
        List<ObjectPojo> objectPojo = jacksonHelper.readAllFromJson();
        objectPojo.forEach(pojo -> log.info(pojo.toString()));
        jacksonHelper.writeToJson(new ArrayList<>(Arrays.asList(new ObjectPojo("2","ella"), new ObjectPojo("3", "adva"))));
        jacksonHelper.writeToJson(new ObjectPojo("5","orit"));
        List<ObjectPojo> byMany = jacksonHelper.findsBy(test -> test.getId().equals("3"));
        byMany.forEach(pojo -> log.info(pojo.toString()));
        Optional<ObjectPojo> bySingle = jacksonHelper.findBy(test -> test.getId().equals("3"));
        bySingle.ifPresent(value -> log.info(value.toString()));
    }

}
