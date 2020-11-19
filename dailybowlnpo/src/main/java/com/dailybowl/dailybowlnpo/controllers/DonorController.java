package com.dailybowl.dailybowlnpo.controllers;

import com.dailybowl.dailybowlnpo.services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DonorController {

    @Resource
    private DonorService donorService;

    @GetMapping("/donor")
    public String getDonors(Model model){

        return donorService.makeGETApiRequest("Produce Donations Tally");
    }

}
