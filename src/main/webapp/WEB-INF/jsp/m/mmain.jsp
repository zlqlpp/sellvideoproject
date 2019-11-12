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
	document.getElementById('video').value=v;
	var form = document.getElementById('f');
	 
	form.submit();
	 
}
</script>
</head>
<body>
  	<h3>列表</h3>
 
  	 
  	 <a href="/m/mgotopage.do?page=dwnvideo">下载视频</a>
  	 <a href="/m/regetvideolist.do">刷新前后台视频列表</a>
  	 <a href="/m/mgotopage.do?page=crtpasswd">观看码管理</a>
  	 <a href="/m/mgotopage.do?page=crtgg">生成宣传页</a>
  	 <a href="/m/mgotopage.do?page=clsvideo">清空视频</a>
  	 
  	 <hr/>
  	 
  	 
	<table border="1" width="70%">
 
   		<c:forEach items="${videolist}" var="v">
   		<tr>
   			<td height="40px"  onclick="go('${v.vid }')">${v.vtitle }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html>