package com.dailybowl.dailybowlnpo.controllers;

import java.awt.List;
import java.io.Console;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import com.dailybowl.dailybowlnpo.model.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
	
	@RequestMapping(value={"/signup"},method = RequestMethod.GET)
	public String loadSignUpPage(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value={"/signup"},method = RequestMethod.POST)
	public String confirmUserSubmission (@ModelAttribute User user, Model model) {
		return "";
	}
}
