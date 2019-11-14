<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
 
  	 
    
    <div id="container" style="width:500px">
 
<div id="header" style="background-color:#FFA500;">
<h1 style="margin-bottom:0;">后台管理</h1></div>
 
<div id="menu" style="background-color:#FFD700;height:200px;width:100px;float:left;">
 </div>
 
<div id="content" style="background-color:#EEEEEE;height:200px;width:400px;float:left;">
	<form action="/m/mmain.do" method="post" >
    	<input type="text" name="passwd" id="passwd" value=""/>
    	<input type="submit" value="提交"/>
    </form>
 </div>
 
<div id="footer" style="background-color:#FFA500;clear:both;text-align:center;"> </div>
 
</div>
    
    
    
    
    
  </body>
</html>
