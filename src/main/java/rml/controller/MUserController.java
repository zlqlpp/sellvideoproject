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
@RequestMapping("/c")
public class MUserController {

	
	@RequestMapping(value="/index")
	public String listUser(HttpServletRequest request) {
		
		return "index";
	}
	
	@RequestMapping(value="/listvideos")
	public String listvideos(Model model,HttpServletRequest request,HttpSession session) {
		
		String code = request.getParameter("ucode");
		
		//校验输入格式，校验code是否存在
		String codes  = (String) session.getAttribute("codes");
		if(null==codes||"".equals(codes)){
			codes = getCodes(session);
		}
		if(null==code||"".equals(code)||!codes.contains(code)){
			return "index";
		}
		
		//从session里读视频 ，没有就读一下目录
		List videolist =   (List) session.getAttribute("videolist");
		if(null==videolist){
			videolist = getVideoList(session);
		}
		//返回前台
		Logger.getLogger(MUserController.class).info("----------log4j-------------");
		model.addAttribute("videolist", videolist);
		
		return "listvideos";
	}
	
	@RequestMapping(value="/openvideo")
	public String openvideo(Model model,HttpServletRequest request,HttpSession session) {
		
		String videoname = request.getParameter("video");
		if(null==videoname||"".equals(videoname)){
			return "index";
		}
		
		
		//从session里读一下code的使用量
		
		//code使用量-1，放入session
		
		//跳到播放页
		 model.addAttribute("video", videoname);
		
		
		return "openvideo";
	}
	
	//---------------------------------------工具方法-------------------------
    private List getVideoList(HttpSession session){
    	 
    	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
        
        
    	File file = new File(prop.getProperty("videoPath"));
        File[] fileNamesArray = file.listFiles();
        
        List<String> videolist = new ArrayList<String>();
        if(null == fileNamesArray){return videolist;}
        for (int i = 0; i < fileNamesArray.length; i++) {
            if (fileNamesArray[i].isFile() ) {
            	videolist.add( fileNamesArray[i].getName() );
            }
        }
        
        session.setAttribute("videolist", videolist);
        
        return videolist;
    }
    
    private String getCodes(HttpSession session){
      	 
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
        
        session.setAttribute("codes", prop.getProperty("codes"));
        
        return prop.getProperty("codes");
        
    }
    
    private Properties getProp(HttpSession session){
      	 
    	String path3 = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"config.properties"; 
 
        Properties prop = new Properties();
 
        try {
			prop.load(new FileInputStream(path3));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        session.setAttribute("prop", prop);
        
        return prop;
        
    }
}
