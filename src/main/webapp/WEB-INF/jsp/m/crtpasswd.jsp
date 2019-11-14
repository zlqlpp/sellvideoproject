<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<!-- 新 Bootstrap4 核心 CSS 文件 -->
<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/css/bootstrap.min.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
<!-- popper.min.js 用于弹窗、提示、下拉菜单 -->
<script src="https://cdn.staticfile.org/popper.js/1.12.5/umd/popper.min.js"></script>
<!-- 最新的 Bootstrap4 核心 JavaScript 文件 -->
<script src="https://cdn.staticfile.org/twitter-bootstrap/4.1.0/js/bootstrap.min.js"></script>


<script>
 var npd = "${passwd}";
window.onload=function(){
 if(npd!=''){
	 alert('新生成的观看码是:'+npd);
	 document.getElementById("rush").click();
 }
	   
}

</script>
</head>


<body>
  	<h3>功能列表</h3>
 
  	 <a href="/m/mgotopage.do?page=dwnvideo"  class="btn btn-primary">下载视频</a>
  	 <a href="/m/regetvideolist.do"   		  class="btn btn-success">刷新前后台视频列表</a>
  	 <a href="/m/mgotopage.do?page=crtpasswd" class="btn btn-info">观看码管理</a>
  	 <a href="/m/mgotopage.do?page=crtgg" 	  class="btn btn-warning">生成宣传页</a>
  	 <a href="/m/mgotopage.do?page=clsvideo"  class="btn btn-danger">清空视频</a>
  	 <br><br>
  	 <hr/>
  	 
生成观看码    
<hr>
	 <a id="rush" href="/m/lispasswd.do">刷新观看码列表</a>
	 <form id="f" action="/m/crtpasswd.do" method="post">
	 	<select name="count">
	 		<option value="1">1</option>
	 		<option value="2">2</option>
	 		<option value="3">3</option>
	 		<option value="4">4</option>
	 		<option value="5" default>5</option>
	 		<option value="6">6</option>
	 		<option value="7">7</option>
	 		<option value="8">8</option>
	 		<option value="9">9</option>
	 		<option value="10">10</option>
	 	</select>
	 	<input type="submit" value="生成新的观看码"/>
	 </form> 
  	 <hr>
  	 
  	 	<table border="1" width="70%">
 
   		<c:forEach items="${passwdlist}" var="v">
   		<tr>
   			<td height="40px" value="${v }"  >${v }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html>