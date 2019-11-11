package rml.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
	
	@RequestMapping(value="/crtgg")
	public String crtgg(Model model,HttpServletRequest request,HttpSession session) {
		
		 Long crtgg = (Long) session.getAttribute("crtgg");
		 Logger.getLogger(ManageController.class).info("session中的crtgg："+crtgg);
		 Long t = new Date().getTime();
		 Logger.getLogger(ManageController.class).info("新的的crtgg："+t);
		 if(null==crtgg||(t-crtgg)>300000) {
			 crtgg=t;
			 Logger.getLogger(ManageController.class).info("新的crtgg："+crtgg);
		 } 
		 session.setAttribute("crtgg", crtgg);
		 Logger.getLogger(ManageController.class).info("url："+request.getRequestURL());
		 model.addAttribute("t",request.getRequestURL()+"?t="+crtgg);
		return "m/crtgg";
	}
	@RequestMapping(value="/gotgg")
	public String gotgg(Model model,HttpServletRequest request,HttpSession session) {
		
		 Long crtgg = (Long) session.getAttribute("crtgg");
		 
		 Long t = new Date().getTime();
		 
		 if(null==crtgg||(t-crtgg)>300000) {
			 crtgg=t;
			 
		 } 
		 session.setAttribute("crtgg", crtgg);
		 
		return "m/crtgg";
	}
	
	@ResponseBody
	@RequestMapping(value="/down",method = RequestMethod.POST)
	public Map<String,Object> down(HttpServletRequest request,HttpSession session) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		String url = request.getParameter("url");
		Logger.getLogger(ManageController.class).info("新下载视频的URL："+url);
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		Thread thread = new Thread(new MusicImplements(url,prop));
		thread.start();
        retMap.put("stat", "suc");
		return retMap;
	}
	
	@RequestMapping(value="/regetvideolist")
	public String regetvideolist(HttpServletRequest request,HttpSession session) {
		
		Logger.getLogger(ManageController.class).info("刷新视频列表");
		getVideoList( session);
		
		return "m/mmain";  
	}
	
	@RequestMapping(value="/clean")
	public String clean(HttpServletRequest request,HttpSession session) {
		Logger.getLogger(ManageController.class).info("清空视频列表");
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		File file = new File(prop.getProperty("videoPath"));
		
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			files[i].delete();
		}
		return "m/mmain";  
	}
	
	@RequestMapping(value="/crtpasswd")
	public String crtpasswd(Model model,HttpServletRequest request,HttpSession session) {
		Logger.getLogger(ManageController.class).info("创建观看码");
		String passwd = writeCodes(session);
		model.addAttribute("passwd",passwd);
		return "m/crtpasswd";  
	}
	
	@RequestMapping(value="/lispasswd")
	public String lispasswd(HttpServletRequest request,HttpSession session) {
		Logger.getLogger(ManageController.class).info("列出所有观看码");
		readCodes(session);
		return "m/crtpasswd";  
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
        Collections.reverse(passwdlist);
        session.setAttribute("passwdlist", passwdlist);
        return codeString;
    }
    private String writeCodes(HttpSession session){
     	 Long passwd = new Date().getTime();
     	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		Logger.getLogger(ManageController.class).info("将新观看码写入文件："+"echo '"+passwd.toString()+"' >>"+ prop.getProperty("passwdPath"));
    	 
		Thread thread = new Thread(new WritePasswd(passwd.toString(),prop));
		thread.start();
         
		 
        return passwd.toString();
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


//--------------------------------------------------
class MusicImplements implements Runnable{  
	private String durl = "";
	private Properties p;
	public MusicImplements(String url,Properties p) {
		this.durl = url;
		this.p = p;
	}
	
    public void run() {  
        try {
        	//Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"-%(id)s.%(ext)s "+durl);
        	Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"%(title)s.%(ext)s "+durl);
        	pro.waitFor();
        } catch ( Exception e) {
            e.printStackTrace();
        }
          
    }  
} 

class WritePasswd implements Runnable{  
	private String passwd = "";
	private Properties p;
	public WritePasswd(String passwd,Properties p) {
		this.passwd = passwd;
		this.p = p;
	}
	
    public void run() {  
    	try {
         	//Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"-%(id)s.%(ext)s "+durl);
         	Process pro = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c","echo "+passwd.toString()+" >>"+ p.getProperty("passwdPath")}) ;
         	pro.waitFor();
         } catch ( Exception e) {
             e.printStackTrace();
         }
          
    }  
} 
