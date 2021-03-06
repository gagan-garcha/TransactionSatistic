package com.n26.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StatisticControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String url;
    private String urlTransaction;
    private static final String EMPTY_RESULT = "{\"sum\":\"0.00\",\"avg\":\"0.00\",\"max\":\"0.00\",\"min\":\"0.00\",\"count\":0}";
    private static final String ONE_RESULT = "{\"sum\":\"1.00\",\"avg\":\"1.00\",\"max\":\"1.00\",\"min\":\"1.00\",\"count\":1}";

    @Before
    public void setUp() {
        url = "http://localhost:" + port + "/statistics";
        urlTransaction = "http://localhost:" + port + "/transactions";
        restTemplate.delete(urlTransaction);
    }

    @Test
    public void statisticsLastMinuteEmpty() {
        ResponseEntity<String> statistics = restTemplate.getForEntity(url, String.class);
        Assert.assertEquals(200, statistics.getStatusCodeValue());
        Assert.assertEquals(EMPTY_RESULT, statistics.getBody());
    }

    @Test
    public void statisticsLastMinute() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");
        json.put("timestamp", Instant.now().toString());
        Assert.assertEquals(201, sendPost(json).getStatusCodeValue());

        ResponseEntity<String> statistics = restTemplate.getForEntity(url, String.class);
        Assert.assertEquals(200, statistics.getStatusCodeValue());
        Assert.assertEquals(ONE_RESULT, statistics.getBody());
    }

    @Test
    public void createTransactionPostNotSupported() {
        Assert.assertEquals(405, restTemplate.postForEntity(url, null, String.class).getStatusCodeValue());
    }

    private ResponseEntity sendPost(Map<String, Object> json) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonString = new ObjectMapper().writeValueAsString(json);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

        return restTemplate.postForEntity(urlTransaction, entity, String.class);
    }
}
