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
</script>
</head>
<body>
  	<h3>列表</h3>
  	 <form action="/c/openvideo.do" method="post"  id="f">
  	 	<input type="hidden" id="video" name="video" />
  	 </form>
  	 
  	 
	<table border="1" width="70%">
 
   		<c:forEach items="${videolist}" var="v">
   		<tr>
   			<c:set var="name" value="${fn:join(v, '--------')}" /> 
   		
   			<td height="40px" value="${name[0] }" onclick="go('${name[1] }')">${name[0] }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html>