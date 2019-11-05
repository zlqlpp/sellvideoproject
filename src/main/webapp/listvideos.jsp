<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	document.getelementbyid('video').value=v;
	var form = document.getElementById('f');
	 
	form.submit();
	 
}
</script>
</head>
<body>
  	<h3>列表</h3>
  	 <form action="/muserController/openvideo.do" method="post"  id="f">
  	 	<input type="hidden" id="video" name="video" />
  	 </form>
  	 
  	 
	<table border="1" width="70%">
 
   		<c:forEach items="${videolist}" var="v">
   		<tr>
   			<td height="40px" value="${v }" onclick="go('${v }')">${v }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html>