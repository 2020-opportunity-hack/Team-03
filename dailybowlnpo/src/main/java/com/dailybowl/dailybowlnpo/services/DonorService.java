package com.dailybowl.dailybowlnpo.services;

import com.dailybowl.dailybowlnpo.model.DonorOrgStat;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
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
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private static final String donor_dataCSV = "./target/donor_data.csv";

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
                String donorName = getDonorDetails(fields.getJSONArray("Source").getString(0), "Food donors");
                donorOrgStat.setName(donorName);
                String foodType = getFoodType(fields.getJSONArray("Items in General").getString(0), "Food Donation Types");
                donorOrgStat.setFoodType(foodType);
                String deliveredTo = getDeliveredTo(fields.getJSONArray("Delivered to").getString(0), "Organizations");
                donorOrgStat.setDeliveredTo(deliveredTo);
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

    private String getDeliveredTo(String deliveryId, String tableName) {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        ResponseEntity<String> response = restTemplate.exchange(queryFor(tableName), HttpMethod.GET, entity, String.class);
        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONArray records = jsonObj.getJSONArray("records");
        String deliveredTo = "";
        for(int i = 0; i<records.length(); i++){
            JSONObject record = records.getJSONObject(i);
            if(record.getJSONObject("fields").getString("Name").equals("New Bridge"))
                System.out.println(record.getJSONObject("fields").getString("Name"));
            if(deliveryId.equals(record.getString("id"))){
                deliveredTo = record.getJSONObject("fields").getString("Name");
                break;
            }
        }
        return deliveredTo;
    }

    private String getFoodType(String foodTypeId, String tableName) {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        ResponseEntity<String> response = restTemplate.exchange(queryFor(tableName), HttpMethod.GET, entity, String.class);
        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONArray records = jsonObj.getJSONArray("records");
        String foodType = "";
        for(int i = 0; i<records.length(); i++){
            JSONObject record = records.getJSONObject(i);
            if(foodTypeId.equals(record.getString("id"))){
                foodType = record.getJSONObject("fields").getString("Donation Type");
                break;
            }
        }
        return foodType;
    }

    private String getDonorDetails(String donorId, String tableName) {
        HttpEntity<String> entity = new HttpEntity<String>("parameters", signRequest(new HttpHeaders()));
        ResponseEntity<String> response = restTemplate.exchange(queryFor(tableName), HttpMethod.GET, entity, String.class);
        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONArray records = jsonObj.getJSONArray("records");
        String donorName = "";
        for(int i = 0; i<records.length(); i++){
            JSONObject record = records.getJSONObject(i);
            if(donorId.equals(record.getString("id"))){
                donorName = record.getJSONObject("fields").getString("Name Of Organization");
                break;
            }
        }
        return donorName;
    }

    public void generateCSV(){
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(donor_dataCSV));
        ){
            StatefulBeanToCsv<DonorOrgStat> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(donorOrgStats);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

}
