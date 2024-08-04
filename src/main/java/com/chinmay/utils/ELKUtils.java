package com.chinmay.utils;

import io.restassured.response.Response;
import org.joda.time.LocalDateTime;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ELKUtils {
    private ELKUtils() {
    }

    public static void sendDetailsToElk(String testname, String teststatus) {
        Map<String, String> map = new HashMap<>();
        map.put("testname", testname);
        map.put("teststatus", teststatus);
        map.put("executiontime", LocalDateTime.now().toString());

        Response response = given().header("Content-Type", "application/json")
                .log().all().body(map).post("http://localhost:9200/regression/result/");

        Assert.assertEquals(response.statusCode(), 201);
        response.prettyPrint();
    }
}
