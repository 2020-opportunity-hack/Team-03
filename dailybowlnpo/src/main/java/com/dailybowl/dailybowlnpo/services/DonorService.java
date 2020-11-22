package com.dailybowl.dailybowlnpo.services;

import com.dailybowl.dailybowlnpo.model.DonorOrgStat;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class DonorService {
    @Resource
    public RestTemplate restTemplate;

    private static final String BASEURL = "https://api.airtable.com/v0/";

    private final String apiKey = "keywasKY5hNOekEkP";
    private final String appId = "appAbp85icmMHLwBW";

    private List<DonorOrgStat> donorOrgStats;

    public List<DonorOrgStat> getDonorOrgStats() {
        return donorOrgStats;
    }

    public String makeGETApiRequest(String query) throws IOException {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
//        System.out.println(entity.toString());
        ResponseEntity<String> response = restTemplate.exchange(queryFor(query), HttpMethod.GET, entity, String.class);

        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONArray records = jsonObj.getJSONArray("records");

        String donorOrg = "Bharat Bazar";
//        System.out.println(donorOrg);
//        System.out.println(records.getJSONObject(0).getJSONObject("fields").getString("Name Of Organization"));
        JSONArray produceDonationsTally = new JSONArray();
        for(int i = 0; i<records.length(); i++){
            JSONObject fields = records.getJSONObject(i).getJSONObject("fields");
            if(fields.getString("Name Of Organization").equals(donorOrg)){
                produceDonationsTally = fields.getJSONArray("Produce Donations Tally");
            }
        }
        System.out.println(produceDonationsTally.toString());
        getProduceDonationsTally(produceDonationsTally, "Produce Donations Tally");


        return response.getBody();
        //return restTemplate.getForObject(queryFor(query), String.class, entity);
    }

    private void getProduceDonationsTally(JSONArray produceDonationsTally, String tableName) {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        ResponseEntity<String> response = restTemplate.exchange(queryFor(tableName), HttpMethod.GET, entity, String.class);
        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONArray records = jsonObj.getJSONArray("records");
//        System.out.println(records.getJSONObject(0).toString());
        HashSet<String> tallySet = new HashSet<>();
        for(int i = 0; i<produceDonationsTally.length(); i++){
            tallySet.add(produceDonationsTally.getString(i));
        }
//        System.out.println(Arrays.toString(tallySet.toArray()));

        List<DonorOrgStat> dOrgStats = new ArrayList<>();
        for(int i = 0; i<records.length(); i++){
            JSONObject record = records.getJSONObject(i);
            if(tallySet.contains(record.getString("id"))){
                JSONObject fields = record.getJSONObject("fields");
                DonorOrgStat donorOrgStat = new DonorOrgStat();
//                System.out.println("Date = " + fields.getString("Date"));
//                System.out.println("Weight in lbs = " +fields.getInt("Weight (in lbs)"));
//                System.out.println("Average Valuation = " +fields.getInt("Average Valuation (at $2.50/lb)"));
//                System.out.println();
                donorOrgStat.setDate(fields.getString("Date"));
                donorOrgStat.setWeight(fields.getInt("Weight (in lbs)"));
                donorOrgStat.setValuation(fields.getInt("Average Valuation (at $2.50/lb)"));
                dOrgStats.add(donorOrgStat);
            }
        }
        this.donorOrgStats = dOrgStats;

    }

    private String queryFor(String query) {
        return BASEURL + appId + "/" +  query + "?api_key=" + apiKey;
    }

    private HttpHeaders signRequest(HttpHeaders headers) {
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;

    }

}
