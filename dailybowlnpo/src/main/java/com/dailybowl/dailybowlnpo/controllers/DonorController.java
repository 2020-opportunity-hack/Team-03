package com.dailybowl.dailybowlnpo.controllers;

import com.dailybowl.dailybowlnpo.model.DonorOrgStat;
import com.dailybowl.dailybowlnpo.model.User;
import com.dailybowl.dailybowlnpo.services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class DonorController {

    @Resource
    private DonorService donorService;

    @GetMapping("/donor")
    public String getDonors(@ModelAttribute("currentUser") User user, Model model) throws IOException {
//        String orgName = "Bharat Bazar";
        System.out.println(user.getName());
        donorService.makeGETApiRequest("Food Donors", user.getName());
//        List<String> dates = donorService.getDates();
//        List<Integer> weights = donorService.getWeights();
//        List<Integer> valuations = donorService.getValuations();
        List<DonorOrgStat> donorOrgStats = donorService.getDonorOrgStats();
        model.addAttribute("donorOrgStats", donorOrgStats);
        model.addAttribute("currentUserName", user.getName());
        Comparator<DonorOrgStat> compareByDate = (DonorOrgStat ds1, DonorOrgStat ds2) -> ds2.getDate().compareTo(ds1.getDate());
        Collections.sort(donorOrgStats, compareByDate);
        System.out.println(Arrays.toString(donorOrgStats.toArray()));
        donorService.generateCSV();
        return "donor";
    }


}
