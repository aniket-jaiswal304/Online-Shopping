<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <title>Online Store</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <style type="text/css">
      .bs-example{
      	margin: 20px;
  	width:300px;
      }
  </style>
</head>
<body>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Online Store</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="#">Product List</a></li>
      <li><a href="#">My Cart</a></li>
    </ul>
  </div>
</nav>

<center>
<div class="bs-example" style="margin-top:100px">
         <h2>User Login</h2>
         <c:if test="${param.error == 'true'}">
            <div style="color: red; margin: 10px 0px;">
                Login Failed!!!<br /> Reason : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
         </c:if>
         <form action="${pageContext.request.contextPath}/j_spring_security_check" method="post">
         <table>
            <tr class="form_group"><td>
                <label for="username">Username:&nbsp;</label></td><td>
                <input type="text" class="form-control" id="username" name="username"  required></td></tr>
            <tr class="form_group"><td>
                <label for="pass">Password:&nbsp;</label></td><td>
                <input type="password" class="form-control" id="pass"  name="pass"  required></td></tr>
            <tr><td>&nbsp;</tr>
            <tr style="margin-top:10px;"><td colspan="2" align="center" >
            <button type="submit" class="btn btn-primary">Login</button></td></tr>
         </table>
         <span class="error-message">${error }</span>
    </form>
</div>
</center>
</body>
</html>
