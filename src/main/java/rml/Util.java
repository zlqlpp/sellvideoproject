package rml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import rml.bean.Video;
import rml.controller.MUserController;


public  class Util {
	
	
		public static boolean ifLogin(HttpSession session){
			if(null!=session.getAttribute("user")){
				return true;
			}
			return false;
		}
		
		public static boolean ifMLogin(HttpSession session){
			if(null!=session.getAttribute("muser")){
				return true;
			}
			return false;
		}
		
	    public static List getVideoListFromTxt(HttpSession session){
	    	 
	    	Properties prop = (Properties) session.getAttribute("prop");
			if(prop==null) {
				prop = getProp(session);
			}
			
	    	Jedis jedis = RedisUtil.getJedis();
			String videolist = jedis.get("videolist");
			
			List vlist = new ArrayList();
			if(StringUtils.isNotBlank(videolist)){
			    vlist = JSON.parseObject(videolist,ArrayList.class);
				
			}

			      
			
			File file = new File(prop.getProperty("videoPath"));
	        File[] fileNamesArray = file.listFiles();
	        
	        Map map = new HashMap<String,String>();
	        if(null != fileNamesArray){
	        for (int i = 0; i < fileNamesArray.length; i++) {
	            if (fileNamesArray[i].isFile() ) {
	            	map.put(fileNamesArray[i].getName().split("\\.")[0], fileNamesArray[i].getName());
	            }
	        }
	        }
	        
	       
	        
	        Set set = map.keySet();
	        Iterator ite = set.iterator();
	        Video video = null;
	        String vname = null;
	        String vid = null;
	        List newvlist = new ArrayList();
	        while(ite.hasNext()){
	        	video = null;
	        	vid = ite.next().toString();
        		vname = map.get(vid).toString();
	        	for(int i=0;i<vlist.size();i++){
	        		video = JSON.parseObject(vlist.get(i).toString(),Video.class);
	        		if(video.getVid().equals(vid)){
	        			break;
	        		}
	        		video = null;
	        	}
	        	if(video!=null){
	        		video.setVname(vname);
	        	}else{
	        		video = new Video();
	        		video.setVid(vid);
	        		video.setVtitle(vid);
	        		video.setVname(vname);
	        	}
	        	newvlist.add(video);
	        	
	        }
	        
	        jedis.set("videolist", JSON.toJSONString(newvlist));
	        
	      RedisUtil.returnResource(jedis);
	        
	        return newvlist;
	        
	    }
	   /* public static String readCodes(HttpSession session){
	     	 
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
	        
	    }*/
	    
	    public static Properties getProp(HttpSession session){
	      	 
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