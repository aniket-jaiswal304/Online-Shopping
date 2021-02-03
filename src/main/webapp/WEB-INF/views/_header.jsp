</nav><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
      <head>
        <title>xyz</title>

      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
      </head>
      <body>

      <nav class="navbar navbar-default">
        <div class="container-fluid">
          <div class="navbar-header">
            <a class="navbar-brand" href="#">xyz</a>
          </div>
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#">Product List</a></li>
            <li><a href="#">My Cart</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
          <c:if test="${pageContext.request.userPrincipal.name != null}">
            <li>Hello
            <a href="${pageContext.request.contextPath}/accountInfo">${pageContext.request.userPrincipal.name}</a>
                        &nbsp; |&nbsp;
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                    </c:if>
                    </li>
            <c:if test="${pageContext.request.userPrincipal.name == null}">
                <li><a href="${pageContext.request.contextPath}/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            </c:if>
          </ul>
        </div>
      </nav>
      </body>
      </html>
