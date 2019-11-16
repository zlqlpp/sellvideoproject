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
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>videoList</title>
<script type="text/javascript">
function go(v){
	document.getElementById('video').value=v;
	var form = document.getElementById('f');
	 
	form.submit();
	 
}
</script>
</head>
<body>
  	<h3>列表</h3>  <a href="#">用户：${user.code },剩余观看次数：${user.count }</a>
  	<br/><br/>
  	 <form action="/c/openvideo.do" method="post"  id="f">
  	 	<input type="hidden" id="video" name="video" />
  	 </form>
  	 
  	 
 
 
   		<c:forEach items="${videolist}" var="v">
 
   			<div height="40px"   onclick="go('${v.vname}')">${v.vtitle }</div>
 
   		</c:forEach>
 
   
</body>
</html>