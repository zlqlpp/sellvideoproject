package rml.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/muserController")
public class MUserController {

	
	@RequestMapping(value="/listUser")
	public String listUser(HttpServletRequest request) {
		
		return "listUser";
	}
	
}
