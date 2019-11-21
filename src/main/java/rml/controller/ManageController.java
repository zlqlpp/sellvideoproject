package rml.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import rml.RedisUtil;
import rml.Util;
import rml.bean.User;
import rml.bean.Video;


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
		if(Util.ifMLogin(session)){
			return "m/mmain";
		}else if(null!=code&&"1234qwer".equals(code)){
			session.setAttribute("muser", code);
			return "m/mmain";
		}else{
			return "m/mlogin";
		}
	}
	
	@RequestMapping(value="/mgotopage")
	public String mgotopage(Model model,HttpServletRequest request,HttpSession session) {
		if(!Util.ifMLogin(session)){
			return "m/mlogin";
		}
		
		String page = request.getParameter("page");
		
		if("dwnvideo".equals(page)){
			return "m/dwnvideo";
		}/*else if("crtpasswd".equals(page)){
			return "m/crtpasswd";
		}*/else if("crtgg".equals(page)){
			return "m/crtgg";
		} else if("clsvideo".equals(page)){
			return "m/clsvideo";
		} 
		
		return "m/mmain";
	}
	
	@RequestMapping(value="/crtgg")
	public String crtgg(Model model,HttpServletRequest request,HttpSession session) {

		
		 Long t = new Date().getTime();
		 Logger.getLogger(ManageController.class).info("新的的crtgg："+t);
		 
		 String reqeustT = request.getParameter("t");
		 if(null!=reqeustT&&t-Long.parseLong(reqeustT)<300000){
			 
			 model.addAttribute("videolist",Util.getVideoListFromTxt(session));
			 return "m/crtggdetail";
		 }
		 
			if(!Util.ifMLogin(session)){
				return "m/mlogin";
			}
		 
		 Long crtgg = (Long) session.getAttribute("crtgg");
		 Logger.getLogger(ManageController.class).info("session中的crtgg："+crtgg);
		 
		 if(null==crtgg||(t-crtgg)>300000) {
			 crtgg=t;
			 Logger.getLogger(ManageController.class).info("新的crtgg："+crtgg);
		 } 
		 session.setAttribute("crtgg", crtgg);
		 Logger.getLogger(ManageController.class).info("url："+request.getRequestURL());
		 Logger.getLogger(ManageController.class).info("uri："+request.getRequestURI());
		 model.addAttribute("t",request.getRequestURL()+"?t="+crtgg);
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
			prop = Util.getProp(session);
		}
		Thread thread = new Thread(new MusicImplements(url,prop));
		thread.start();
		
        retMap.put("stat", "suc");
		return retMap;
	}
	
	@RequestMapping(value="/regetvideolist")
	public String regetvideolist(HttpServletRequest request,HttpSession session) {
		if(!Util.ifMLogin(session)){
			return "m/mlogin";
		}
		
		Logger.getLogger(ManageController.class).info("刷新视频列表");
		
		session.setAttribute("videolist", Util.getVideoListFromTxt( session));
		
		return "m/mmain";  
	}
	
	@RequestMapping(value="/clean")
	public String clean(HttpServletRequest request,HttpSession session) {
		if(!Util.ifMLogin(session)){
			return "m/mlogin";
		}
		
		Logger.getLogger(ManageController.class).info("清空视频列表");
		Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = Util.getProp(session);
		}
		File file = new File(prop.getProperty("videoPath"));
		
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			files[i].delete();
		}
		
		Thread thread = new Thread(new CleanVideo(prop));
		thread.start();
		
		session.setAttribute("videolist", new ArrayList());
		return "m/mmain";  
	}
	
	@RequestMapping(value="/crtpasswd")
	public String crtpasswd(Model model,HttpServletRequest request,HttpSession session) {
		if(!Util.ifMLogin(session)){
			return "m/mlogin";
		}
		
		Logger.getLogger(ManageController.class).info("创建观看码");
		
		String count = request.getParameter("count");
		Date date = new Date();
		Long code = date.getTime();
		User user = new User();
		user.setCode(code.toString());
		user.setCount(Double.parseDouble(count));
		user.setCrtDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		
		Jedis jedis = RedisUtil.getJedis();
		
		
		
		String str = jedis.get("codemap");
		Map map = new HashMap();
		if(StringUtils.isNotBlank(str)){
		    map = JSON.parseObject(str,HashMap.class);
		}
		map.put(user.getCode(), user);
		jedis.set("codemap", JSON.toJSONString(map));
		
		
		str = jedis.get("codelist");
		List codelist = new ArrayList();
		if(StringUtils.isNotBlank(str)){
		    codelist = JSON.parseObject(str,ArrayList.class);
		}
			codelist.add(user);
		jedis.set("codelist", JSON.toJSONString(codelist));
		
		
		Logger.getLogger(ManageController.class).info("创建观看码,code:"+user.getCode()+",观看次数，count:"+user.getCount()+",并存放redis,一份map,一份list");

		RedisUtil.returnResource(jedis);
		
		model.addAttribute("newcode", user.getCode());
		
		return "m/crtpasswd";  
	}
	
	@RequestMapping(value="/lispasswd")
	public String lispasswd(Model model,HttpServletRequest request,HttpSession session) {
		if(!Util.ifMLogin(session)){
			return "m/mlogin";
		}
		
		Logger.getLogger(ManageController.class).info("列出所有观看码");
		//readCodes(session);
		
		Jedis jedis = RedisUtil.getJedis();
		String str = jedis.get("codelist");
		
		List codelist = new ArrayList();
		if(StringUtils.isNotBlank(str)){
		    codelist = JSON.parseObject(str,ArrayList.class);
		}
		RedisUtil.returnResource(jedis);
		Collections.reverse(codelist);
		model.addAttribute("passwdlist",codelist);
		
		return "m/crtpasswd";  
	}
	
	//---------------------------------------工具方法-------------------------

/*	private Map getVideoListTmp(HttpSession session){
   	 
    	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
        
        
    	File file = new File(prop.getProperty("videoPath"));
        File[] fileNamesArray = file.listFiles();
        
        //List<String> videolist = new ArrayList<String>();
        Map map = new HashMap<String,String>();
        if(null == fileNamesArray){return map;}
        for (int i = 0; i < fileNamesArray.length; i++) {
            if (fileNamesArray[i].isFile() ) {
            	//videolist.add( fileNamesArray[i].getName() );
            	map.put(fileNamesArray[i].getName().split("\\.")[0], fileNamesArray[i].getName());
            }
        }
        
        //session.setAttribute("videolist", videolist);
        
        return map;
    }
    private List getVideoListFromTxt(HttpSession session){
    	 
    	Properties prop = (Properties) session.getAttribute("prop");
		if(prop==null) {
			prop = getProp(session);
		}
		
		Map map = getVideoListTmp(session);
		
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
            Video v = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
            	v = new Video();
            	v.setVtitle(tempString.split("--------")[0]);
            	v.setVid(tempString.split("--------")[1]);
            	v.setVname((null==map.get(tempString.split("--------")[1]))?"":map.get(tempString.split("--------")[1]).toString());
            	v.setVlenght(tempString.split("--------")[2]);
            	videolist.add(v);
                 
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
        
    }*/
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
        	//Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"-%(title)s.%(ext)s "+durl);
        	Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"%(id)s.%(ext)s "+durl);
        	pro.waitFor();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger(MusicImplements.class).info("视频下载---下载完成" );
        
        List<String> processList = new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec("youtube-dl --get-id "+durl);
			p.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String id = processList.get(0).toString();
		Logger.getLogger(MusicImplements.class).info("视频下载---视频id："+processList.get(0).toString() );
 
          processList = new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec("youtube-dl --get-title "+durl);
			p.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String name = processList.get(0).toString();
		Logger.getLogger(MusicImplements.class).info("视频下载---视频名字："+ processList.get(0).toString());  
		//--get-duration
        processList = new ArrayList<String>();
		try {
			Process p = Runtime.getRuntime().exec("youtube-dl --get-duration "+durl);
			p.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String longth = processList.get(0).toString();
		Logger.getLogger(MusicImplements.class).info("视频下载---视频时长："+ processList.get(0).toString()); 
		
    	try {
         	//Process pro = Runtime.getRuntime().exec("youtube-dl -o "+p.getProperty("videoPath")+"-%(id)s.%(ext)s "+durl);
         	Process pro = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c","echo '"+name+"--------"+id+"--------"+longth+"' >>"+ p.getProperty("videoNamePath")}) ;
         	pro.waitFor();
         } catch ( Exception e) {
             e.printStackTrace();
         }
    	Logger.getLogger(MusicImplements.class).info("视频下载---视频写入文件："+ name+"--------"+id+"--------"+longth); 
    	//--get-duration  获取时长
    }  
} 

/*class WritePasswd implements Runnable{  
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
} */

class CleanVideo implements Runnable{  
	private Properties p;
	public CleanVideo(Properties p) {
		this.p = p;
	}
	
    public void run() {  
    	try {
         	Process pro = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c"," >"+ p.getProperty("videoNamePath")}) ;
         	pro.waitFor();
         } catch ( Exception e) {
             e.printStackTrace();
         }
          
    }  
} 
