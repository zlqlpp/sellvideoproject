package rml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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
		
	    public static Map getVideoListTmp(HttpSession session){
	    	 
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
	    public static List getVideoListFromTxt(HttpSession session){
	    	 
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
	        
	        //session.setAttribute("videolist", videolist);
	        
	        return videolist;
	        
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