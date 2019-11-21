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
import rml.Util;
import rml.bean.User;
import rml.bean.Video;


@Controller
@RequestMapping("/c")
public class MUserController {

	
	@RequestMapping(value="/index")
	public String listUser(HttpServletRequest request) {
		
		return "index";
	}
	@RequestMapping(value="/t")
	public String t(HttpServletRequest request) {
		
		return "t";
	}
	
	@RequestMapping(value="/listvideos")
	public String listvideos(Model model,HttpServletRequest request,HttpSession session) {
		
		String code = request.getParameter("ucode");
		Logger.getLogger(MUserController.class).info("登录-- 前台传入的观看码为："+code);
		
		
		//从session里读视频 ，没有就读一下目录
		/*List videolist =    (List) session.getAttribute("videolist");
		if(null==videolist){
			videolist = getVideoListFromTxt(session);
		}*/
		List videolist = Util.getVideoListFromTxt(session);
		Logger.getLogger(MUserController.class).info("登录--读视频列表， 从文件读。完成：" );
		
		
		Jedis jedis = RedisUtil.getJedis();
		String str = jedis.get("codemap");
		
		Map codemap = new HashMap();
		if(StringUtils.isNotBlank(str)){
		    codemap = JSON.parseObject(str,HashMap.class);
		}
		
		User user = null;
		if(code!=null){
				if(codemap.containsKey(code)){
					user = JSON.parseObject(codemap.get(code).toString(),User.class)  ;
					Logger.getLogger(MUserController.class).info("登录--redis的codemap中存在此观看码" );
					session.setAttribute("user", user);
				}else{
					 return "index";
				}
					
		}else if(code==null&&null!=session.getAttribute("user")){
			User tu = (User) session.getAttribute("user");
			user = JSON.parseObject(codemap.get(tu.getCode()).toString(),User.class)  ;
			session.setAttribute("user", user);
		}else{
			 return "index";
		}
		      
		RedisUtil.returnResource(jedis);
		
		model.addAttribute("videolist",videolist);
		return "listvideos";
	}
	
	@RequestMapping(value="/openvideo")
	public String openvideo(Model model,HttpServletRequest request,HttpSession session) {
		if(!Util.ifLogin(session)){
			return "index";
		}
		
		
		String videoname = request.getParameter("video");
		if(null==videoname||"".equals(videoname)){
			return "index";
		}
		
		String wb = request.getParameter("wb");
		
		//跳到播放页
		 model.addAttribute("video", videoname);
		 
		 User u = (User) session.getAttribute("user");
		 String code =  u.getCode();
		 
		 boolean countflag = true;  //为false时，不能再观看
		 
			Jedis jedis = RedisUtil.getJedis();
			String str = jedis.get("codelist");
			
			List codelist = new ArrayList();
			List codelist2 = new ArrayList();
			if(StringUtils.isNotBlank(str)){
			    codelist = JSON.parseObject(str,ArrayList.class);
			}
			User user = null;
			for(int i=0;i<codelist.size();i++){
				user =   JSON.parseObject(codelist.get(i).toString(),User.class);
				if(code.equals(user.getCode())){
					if(user.getCount()==0){
						countflag = false;
					}else{
						if(wb.equals("vx")){
							user.setCount(user.getCount()-0.5);
						}else{
							user.setCount(user.getCount()-1);
						}
					}
					//break;
				}
				codelist2.add(user);
				jedis.set("codelist", JSON.toJSONString(codelist2));
			}
			
			 str = jedis.get("codemap");
			
			Map codemap = new HashMap();
			Map codemap2 = new HashMap();
			if(StringUtils.isNotBlank(str)){
			    codemap = JSON.parseObject(str,HashMap.class);
			}
			 user = null;
			 Set set = codemap.keySet();
			 Iterator iterator = set.iterator();
			 String key = "";
			 while(iterator.hasNext()){
				 key = iterator.next().toString();
				 user =  JSON.parseObject(codemap.get(key).toString(),User.class);
				 if(code.equals(user.getCode())){
					 if(user.getCount()==0){
							countflag = false;
						}else{
							if(wb.equals("vx")){
								user.setCount(user.getCount()-0.5);
							}else{
								user.setCount(user.getCount()-1);
							}
						session.setAttribute("user", user);
						}
				 }
				 codemap2.put(key, user);
				 jedis.set("codemap", JSON.toJSONString(codemap2));
			 }
	 
			RedisUtil.returnResource(jedis);
		 if(!countflag){
			 return "index";
		 }
		return "openvideo";
	}
	
	
}
