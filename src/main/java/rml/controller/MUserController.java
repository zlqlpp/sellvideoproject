package rml.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import rml.RedisUtil;
import rml.bean.User;
import rml.bean.Video;


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
		Logger.getLogger(MUserController.class).info("登录-- 前台传入的观看码为："+code);
		
		
		//从session里读视频 ，没有就读一下目录
		List videolist =   (List) session.getAttribute("videolist");
		if(null==videolist){
			videolist = getVideoListFromTxt(session);
		}
		Logger.getLogger(MUserController.class).info("登录--读视频列表，默认存在session中，没有就从文件读。完成：" );
		
		
		Jedis jedis = RedisUtil.getJedis();
		String str = jedis.get("codemap");
		
		Map codemap = new HashMap();
		if(StringUtils.isNotBlank(str)){
		    codemap = JSON.parseObject(str,HashMap.class);
		}
		Logger.getLogger(MUserController.class).info("登录--从redis中读观看码的codemap<code,user>。完成：" );
		
		
		User user = null;
		if(code!=null){
				if(codemap.containsKey(code)){
					user = JSON.parseObject(codemap.get(code).toString(),User.class)  ;
					session.setAttribute("user", user);
				}else{
					 return "index";
				}
					
		}else if(code==null&&null!=session.getAttribute("user")){
			user = JSON.parseObject(codemap.get(code).toString(),User.class)  ;
			session.setAttribute("user", user);
		}else{
			 return "index";
		}
		
		RedisUtil.returnResource(jedis);
		
		return "listvideos";
	}
	
	@RequestMapping(value="/openvideo")
	public String openvideo(Model model,HttpServletRequest request,HttpSession session) {
		if(!ifLogin(session)){
			return "index";
		}
		
		
		String videoname = request.getParameter("video");
		if(null==videoname||"".equals(videoname)){
			return "index";
		}
		
		
		//跳到播放页
		 model.addAttribute("video", videoname);
		 
		 User u = (User) session.getAttribute("user");
		 String code =  u.getCode();
		 
		 
		 
			Jedis jedis = RedisUtil.getJedis();
			String str = jedis.get("codelist");
			
			List codelist = new ArrayList();
			if(StringUtils.isNotBlank(str)){
			    codelist = JSON.parseObject(str,ArrayList.class);
			}
			User user = null;
			for(int i=0;i<codelist.size();i++){
				user =   JSON.parseObject(codelist.get(i).toString(),User.class);
				if(code.equals(user.getCode())){
					if(user.getCount()==0){
						return "index";
					}
					user.setCount(user.getCount()-1);
					jedis.set("codelist", JSON.toJSONString(codelist));
					break;
				}
			}
			
			 str = jedis.get("codemap");
			
			Map codemap = new HashMap();
			if(StringUtils.isNotBlank(str)){
			    codemap = JSON.parseObject(str,HashMap.class);
			}
			 user = null;
			 Set set = codemap.keySet();
			 Iterator iterator = set.iterator();
			 while(iterator.hasNext()){
				 user =  JSON.parseObject(codemap.get(iterator.next()).toString(),User.class);
				 if(code.equals(user.getCode())){
					 user.setCount(user.getCount()-1);
					 jedis.set("codemap", JSON.toJSONString(codemap));
						break;
				 }
			 }
	 
			RedisUtil.returnResource(jedis);
		 
		return "openvideo";
	}
	
	//---------------------------------------工具方法-------------------------
	private boolean ifLogin(HttpSession session){
		if(null!=session.getAttribute("user")){
			Logger.getLogger(this.getClass()).info("user:"+session.getAttribute("user"));
			return true;
		}
		return false;
	}
    private Map getVideoListTmp(HttpSession session){
    	 
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
            	Logger.getLogger(MUserController.class).info("读视频列表："+v.getVid());
            	Logger.getLogger(MUserController.class).info("读视频列表："+v.getVtitle());
            	Logger.getLogger(MUserController.class).info("读视频列表："+v.getVname());
            	Logger.getLogger(MUserController.class).info("读视频列表："+map.get(tempString.split("--------")[1]));
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
            String tempString = "";
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                passwdlist.add(tempString);
                codeString+=","+tempString;
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
        session.setAttribute("codes", codeString);
        
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
