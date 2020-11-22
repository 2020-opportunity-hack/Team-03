package com.dailybowl.dailybowlnpo.controllers;

import com.dailybowl.dailybowlnpo.model.DonorOrgStat;
import com.dailybowl.dailybowlnpo.services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class DonorController {

    @Resource
    private DonorService donorService;

    @GetMapping("/donor")
    public String getDonors(Model model) throws IOException {

        donorService.makeGETApiRequest("Food Donors");
//        List<String> dates = donorService.getDates();
//        List<Integer> weights = donorService.getWeights();
//        List<Integer> valuations = donorService.getValuations();
        List<DonorOrgStat> donorOrgStats = donorService.getDonorOrgStats();
        model.addAttribute("donorOrgStats", donorOrgStats);
        System.out.println(Arrays.toString(donorOrgStats.toArray()));
        return "donor";
    }

}
