package com.dailybowl.dailybowlnpo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.dailybowl.dailybowlnpo.model.DonorOrgStat;

@Service
public class LoginService {
	
    @Resource
    public RestTemplate restTemplate;

    private static final String BASEURL = "https://api.airtable.com/v0/";

    private final String apiKey = System.getenv("API_KEY");
    private final String appId = System.getenv("APP_ID");
    
    private String responseValue;
//    private ArrayList<String> names;

    
    public boolean checkIfNamePresentInTable(String tableName, String name) throws IOException {
    	makeGETApiRequest(tableName);
    	JSONObject jsonObj = new JSONObject(responseValue);
        JSONArray records = jsonObj.getJSONArray("records");
        for(int i = 0; i<records.length(); i++){
            JSONObject record = records.getJSONObject(i);
            JSONObject fields = record.getJSONObject("fields");
            if(name.equalsIgnoreCase(fields.getString("Name Of Organization"))) {
            	return true;
            }
        }
		return false;
    }

    public void makeGETApiRequest(String query) throws IOException {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        ResponseEntity<String> response = restTemplate.exchange(queryFor(query), HttpMethod.GET, entity, String.class);
        this.responseValue = response.getBody();
    }

    private String queryFor(String query) {
        return BASEURL + appId + "/" +  query + "?api_key=" + apiKey;
    }

    private HttpHeaders signRequest(HttpHeaders headers) {
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;

    }
}

