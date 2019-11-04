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
<title>User list</title>
</head>
<body>
  	<h3>UserList</h3>
  	<a href="<%=path %>/addUser.jsp">Add User</a><br/>
	<table border="1" width="70%">
   		<tr>
   			<td>Id</td>
   			<td>Name</td>
   			<td>Age</td>
   			<td>Address</td>
   			<td>Delete</td>
   			<td>Update</td>
   		</tr>
   		<c:forEach items="${videolist}" var="f">
   		<tr>
   			<td>${f}</td>
   			<td>${f}</td>
   			<td>${f}</td>
   			<td>${f}</td>
   			<td><a href="<%=path %>/muserController/deleteUser.do?id=${f}">Delete</a></td>
   			<td><a href="<%=path %>/muserController/updateUserUI.do?id=${f}">Update</a></td>
   		</tr>
   		</c:forEach>
   </table>
   
</body>
</html>