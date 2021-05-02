package com.n26.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.TestConfig;
import com.n26.util.DateUtil;
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
public class TransactionControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String url;

    @Before
    public void setUp() {
        url = "http://localhost:" + port + "/transactions";
    }

    @Test
    public void createTransactionJSONInvalid() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");

        Assert.assertEquals(400, sendPost(json).getStatusCodeValue());
    }

    @Test
    public void createTransactionJSONParse() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");
        json.put("timestamp", "2018-");

        Assert.assertEquals(422, sendPost(json).getStatusCodeValue());
    }

    @Test
    public void createTransaction() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");
        json.put("timestamp", Instant.now().toString());

        Assert.assertEquals(201, sendPost(json).getStatusCodeValue());
    }

    @Test
    public void createTransactionOld() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");
        json.put("timestamp", Instant.now().minusSeconds(DateUtil.SECONDS_TRANSACTION_BECOME_OLD + 1).toString());

        Assert.assertEquals(204, sendPost(json).getStatusCodeValue());
    }

    @Test
    public void createTransactionFuture() throws JsonProcessingException {
        Map<String, Object> json = new HashMap<>();
        json.put("amount", "1");
        json.put("timestamp", Instant.now().plusSeconds(30).toString());

        Assert.assertEquals(422, sendPost(json).getStatusCodeValue());
    }

    @Test
    public void createTransactionGetNotSupported() {
        Assert.assertEquals(405, restTemplate.getForEntity(url, String.class).getStatusCodeValue());
    }

    private ResponseEntity sendPost(Map<String, Object> json) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonString = new ObjectMapper().writeValueAsString(json);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

        return restTemplate.postForEntity(url, entity, String.class);
    }
}
