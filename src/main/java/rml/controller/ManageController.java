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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/m")
public class ManageController {

	Log logger = LogFactory.getLog(getClass());
	
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
	public String mgotopage(Model model,HttpServletRequest request) {
		
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
	
	@ResponseBody
	@RequestMapping(value="/down",method = RequestMethod.POST)
	public Map<String,Object> down(HttpServletRequest request,HttpSession session) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		String url = request.getParameter("url");
		logger.info(url);
 
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		Thread thread = new Thread(new MusicImplements(url,prop));
		thread.start();
        retMap.put("stat", "suc");
		return retMap;
	}
	
	@RequestMapping(value="/clean")
	public String clean(HttpServletRequest request,HttpSession session) {
		
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		File file = new File(prop.getProperty("videoPath"));
		
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			files[i].delete();
		}
		return "mmain";  
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
        
        session.setAttribute("prop", prop);
        
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
    
    private Properties getProp(HttpSession session){
      	 
    	String path3 = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"config.properties"; 
        System.out.println("getProp:"+path3);
 
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



class MusicImplements implements Runnable{  
	private String durl = "";
	private Properties p;
	public MusicImplements(String url,Properties p) {
		this.durl = url;
		this.p = p;
	}
	
    public void run() {  
        try {
        	Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"-%(id)s.%(ext)s "+durl);
        	pro.waitFor();
        } catch ( Exception e) {
            e.printStackTrace();
        }
          
    }  
} 
