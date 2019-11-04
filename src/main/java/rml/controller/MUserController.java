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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/muserController")
public class MUserController {

	
	@RequestMapping(value="/index")
	public String listUser(HttpServletRequest request) {
		
		return "index";
	}
	
	@RequestMapping(value="/listvideos")
	public String listvideos(ModelMap model,HttpServletRequest request,HttpSession session) {
		
	/*	String code = request.getParameter("ucode");
		
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
			//videolist = getVideoList(session);
		}
		//返回前台
		System.out.println("-------");
		model.addAttribute("videolist", videolist);
		
		return "listvideos";*/
		return "openvideo";
	}
	
	@RequestMapping(value="/openvideo")
	public String openvideo(HttpServletRequest request,HttpSession session) {
		
		String videoname = request.getParameter("videoname");
		if(null==videoname||"".equals(videoname)){
			return "index";
		}
		
		
		//从session里读一下code的使用量
		
		//code使用量-1，放入session
		
		//跳到播放页
		
		
		
		return "listvideos";
	}
	
	//---------------------------------------工具方法-------------------------

    private List getVideoList(HttpSession session){
    	 
    	String path3 = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"config.properties"; 
        System.out.println(path3);
        //logger.info(path3);
        Properties prop = new Properties();
        //读取资源文件
        try {
			prop.load(new FileInputStream(path3));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        String videoPath = prop.getProperty("videoPath");
        
    	File file = new File(videoPath);
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
   	 
    	String path3 = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"config.properties"; 
        System.out.println(path3);
        //logger.info(path3);
        Properties prop = new Properties();
        //读取资源文件
        try {
			prop.load(new FileInputStream(path3));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        String codes = prop.getProperty("codes");
        
        session.setAttribute("codes", codes);
        return codes;
        
    }
}
