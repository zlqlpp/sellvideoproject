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
 
function down(){
	$.ajax({
	    type : 'POST',
	    url :'down.do',
	    data :{"url":$('#url').val()},
	    dataType : 'JSON',
	    success : function(dto) {
	    	alert('已加入下载任务列表，请稍候刷新页面查看下载结果');
	    }});
}
 
function clean(){
		$("#d").attr("action","clean.do");
     	$("#d").submit();
}
</script>
</head>


<body>
清空视频
<div class="container">
<div class="row">
	<div class="col-*-*" style="height: 100px"></div> 
</div>
 
    

<div class="row">
	  <form id="d" class="form-inline"  action="getFileList.do">
	  <div class="col-6">
	    <input type="url" class="form-control" style="width:auto" id="url" placeholder="复制要下载视频的地址到这里">
	    </div>
	    <div class="col-2">
	    <button type="button" class="btn btn-primary" onclick="down();">下载</button>
	    </div>
	    <div class="col-2">
	    <button type="submit" class="btn btn-primary"  >查看文件列表</button>
	    </div>
	    <div class="col-2">
	    <button type="button" class="btn btn-primary"  onclick="clean();">清理</button>
	    </div>
	  </form>
</div>
<div class="row">
	<div class="col-*-*" style="height: 100px"></div> 
</div>
<c:forEach items="${files}" var="f" varStatus="status">
<div class="row">
	<div class="col-4"> </div>
  	<div class="col-4"><h3><a href="/downyoutube/video/${f.name }">${f.name }</a></h3></div>
  	<div class="col-4"> </div>
</div>
<hr/>
</c:forEach>

</div>

</body>
</html>