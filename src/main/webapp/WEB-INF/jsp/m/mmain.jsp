<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html >
<html>
<%-- <head>
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
  	 <a href="/m/mgotopage.do?page=crtpasswd">生成密码</a>
  	 <a href="/m/mgotopage.do?page=crtgg">生成宣传页</a>
  	 <a href="/m/mgotopage.do?page=clsvideo">清空视频</a>
	<table border="1" width="70%">
 
   		<c:forEach items="${videolist}" var="v">
   		<tr>
   			<td height="40px" value="${v }" onclick="go('${v }')">${v }</a></td>
 
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html> --%>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <style>
        #iframeTop{
            width: 100%;
            height: 70px;
        }
        #iframeLeft{
            width: 15%;
            height: 700px;
            float: left;
        }
        #iframeContent{
            width: 84%;
            height: 700px;
        }
    </style>
<body>
<div>
    <iframe id="iframeTop" name="iframeTop" frameborder="0" src="m/dwnvideo.jsp"></iframe>
    <iframe id="iframeLeft" name="iframeLeft" frameborder="0" src="view/left.html"></iframe>
    <iframe id="iframeContent" name="iframeContent" frameborder="0" src="view/content.html"></iframe>
</div>
</body>
</html>

 