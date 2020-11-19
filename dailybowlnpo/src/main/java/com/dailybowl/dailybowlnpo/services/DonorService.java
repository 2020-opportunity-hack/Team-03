package com.dailybowl.dailybowlnpo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class DonorService {
    @Resource
    public RestTemplate restTemplate;

    private static final String BASEURL = "https://api.airtable.com/v0/";

    private final String apiKey = "api_key";
    private final String appId = "app_id";

    public String makeGETApiRequest(String query) {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        return restTemplate.getForObject(queryFor(query), String.class, entity);
    }

    private String queryFor(String query) {
        return BASEURL + appId + "/" +  query + "?api_key=" + apiKey;
    }

    private HttpHeaders signRequest(HttpHeaders headers) {
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;

    }

}
