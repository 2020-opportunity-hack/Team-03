package com.dailybowl.dailybowlnpo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dailybowl.dailybowlnpo.services.LoginService;
import com.dailybowl.dailybowlnpo.model.DonorOrgStat;
import com.dailybowl.dailybowlnpo.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignInController {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value={"/signin"},method = RequestMethod.GET)
	public String loadSignUpPage(Model model) {
		model.addAttribute("user", new User());
		return "signin";
	}
	
	@RequestMapping(value={"/signin"},method = RequestMethod.POST)
	public String confirmUserSubmission (@ModelAttribute User user, Model model, final RedirectAttributes redirectAttributes) throws IOException{
        boolean present = loginService.checkIfNamePresentInTable("Food Donors", user.getName());
        if(present){
//        	model.addAttribute("currentUser", user);
			redirectAttributes.addFlashAttribute("currentUser", user);
        	return "redirect:/donor";
        }
        model.addAttribute("errorMsg","User Not found");
    	return "signin"; 
	}
}
