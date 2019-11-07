package rml.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/m")
public class ManageController {

	
	@RequestMapping(value="/mlogin")
	public String listUser(HttpServletRequest request) {
		
		return "m/mlogin";
	}
	
	@RequestMapping(value="/mmain")
	public String listvideos(Model model,HttpServletRequest request,HttpSession session) {
		
		String code = request.getParameter("passwd");
		
		return "m/mmain";
	}
	
	@RequestMapping(value="/mgotopage")
	public String mgotopage(Model model,HttpServletRequest request,HttpSession session) {
		
		String page = request.getParameter("page");
		
		if("dwnvideo".equals(page)){
			return "m/dwnvideo";
		}else if("crtpasswd".equals(page)){
			return "m/crtpasswd";
		}else if("crtgg".equals(page)){
			return "m/crtgg";
		} else if("clsvideo".equals(page)){
			return "m/clsvideo";
		} 
		
		return "m/mmain";
	}
	
	
}
