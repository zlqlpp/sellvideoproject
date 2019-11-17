<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>videoList</title>
<script type="text/javascript">
function go(v){
	document.getElementById('video').value=v;
	var form = document.getElementById('f');
	 
	form.submit();
	 
}

window.onload=function(){
 
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
			 alert('请用手机浏览器打开链接');
			 document.getElementById('msg').style='block';
	 	} else{
	 		document.getElementById('content').style='block';
	 	}
    
	}
</script>
</head>
<body>
<div style="display:none" id="msg"><h1>请使用手机浏览器打开链接</h1></div>
<div style="display:none" id="content">
  	<h3>列表</h3>  <a href="#">用户：${user.code },剩余观看次数：${user.count }</a>
  	<br/><br/>
  	 <form action="/c/openvideo.do" method="get"  id="f">
  	 	<input type="hidden" id="video" name="video" />
  	 </form>
  	 
  	 
	<table border="1" width="70%">
 
   		<c:forEach items="${videolist}" var="v">
   		<tr>
   			<td height="70px"  ><a  onclick="go('${v.vname}')" href="#">${v.vtitle }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
</div>   
</body>
</html>