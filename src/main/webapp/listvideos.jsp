<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<style type="text/css">
.vn{
	    margin-left: 25%;
    font-family: serif;
    font-size: 50px;
}
.t{
    font-family: serif;
    font-size: 40;
    margin-top: 8%;
    text-align: right;
}
</style>
<head>
	<meta charset="utf-8">
	<title></title>
	<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">  
	<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
	<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script>
	 
		function video(v){
			var f = $(v).attr('value');
			$('#video').val(f);
			if(''!=$('#u').val()){
				if(0==$('#g').val()){
					alert("请加VX:salinahk 进行充值");
					return;
				}
				if(confirm("此次观看将使用1金币")){
						$('#sf').submit();
					}
			}else{
				$('#sf').attr("action","login");
				$('#sf').submit();
			}
			
		}
	</script>
</head>
<body>
<form id="sf" action="goVideo" method="post">
<input id="video" name="video" type="hidden" ></input>
<input id="u" type="hidden" value="${user['user_name'] }"/>
<input id="g" type="hidden" value="${user['total_gold'] }"/>
</form>

<div>
<form role="form" class="t">
  <div class="form-group" style="margin-right: 10%;">
    <a href="frash">刷新列表</a>
    <label id="userName">用户：${user['user_name'] }</label>
    <label id="gold">金币:${user['total_gold'] }</label> 
    
  </div>
 </form>
</div>
<hr>
			<c:forEach items="${fileShortNameList}" var="f" varStatus="id">
				
					<div class="vn"><a onclick="video(this);" value="${f}">${f}</a> </div>
				
			</c:forEach>
  	

</body>
</html>
