package rml.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
		Logger.getLogger(MUserController.class).info("登录-------");
		String code = request.getParameter("ucode");
		Logger.getLogger(MUserController.class).info("前台传入的观看码为："+code);
		//校验输入格式，校验code是否存在
		if(!code.equals("123456")) {
		String codes  = (String) session.getAttribute("codes");
		Logger.getLogger(MUserController.class).info("session中的观看码："+codes);
		if(null==codes||"".equals(codes)){
			codes = readCodes(session);
			Logger.getLogger(MUserController.class).info("从文件里读出来的观看码："+codes);
		}
		
		if(null==code||"".equals(code)||!codes.contains(code)){
			return "index";
		}
		}
		//从session里读视频 ，没有就读一下目录
		List videolist =   (List) session.getAttribute("videolist");
		if(null==videolist){
			videolist = getVideoListFromTxt(session);
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
    private List getVideoListTmp(HttpSession session){
    	 
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
    private List getVideoListFromTxt(HttpSession session){
    	 
    	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
        List videolist = new ArrayList();
        
        if(prop.getProperty("videoNamePath") == null) {
    		return null;
    	}
    	File file = new File(prop.getProperty("videoNamePath"));
    	
        BufferedReader reader = null;
        try {
             
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
            	videolist.add(tempString);
                 
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        session.setAttribute("videolist", videolist);
        
        return videolist;
        
    }
    private String readCodes(HttpSession session){
     	 
    	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
        List passwdlist = new ArrayList();
        String codeString= "";
        if(prop.getProperty("passwdPath") == null) {
    		return null;
    	}
    	File file = new File(prop.getProperty("passwdPath"));
    	
        BufferedReader reader = null;
        try {
             
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                passwdlist.add(tempString);
                codeString+=tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        session.setAttribute("passwdlist", passwdlist);
        
        return codeString;
        
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
