<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Price</title>
</head>
<body>
<table border="0" width="100%">
    <tr>
        <td colspan="2" valign="top">
            <div style="background-color : lightgray; widht:100%; height:35px;">
                <h1>Price list</h1>
                <c:if test="${not empty message}">
                    <h4 style="float:right">${message}</h4>
                </c:if>
            </div>
        </td>
    </tr>
    <tr valign="top">
        <td height="200" width="20%">
            <div>
                <a href="<c:url value="/do/"/>" title="pricelist">pricelist</a>
            </div>
        </td>
        <td width="80%">
            <div style="align-content: inherit">
                <form name="price" method="POST" action="<c:url value="/do/filter"/>">
                    <input value="${category}" name="category" id="category" type="text" placeholder="Enter category"/>
                    <input value="${name}" name="name" id="name" type="text" placeholder="Enter name"/>
                    <input value="${price_from}" name="price_from" id="price_from" type="text"
                           placeholder="Enter the minimum price"/>
                    <input value="${price_to}" name="price_to" id="price_to" type="text"
                           placeholder="Enter the maximum price"/>
                    <input name="find" id="find" title="Find" type="submit">
                </form>
            </div>
            <table border="2" cols="3">
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td width="40%">${product.category}</td>
                        <td width="40%">${product.name}</td>
                        <td width="20%" align="right">${product.price}</td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2" valign="top">
            <div style="background-color : lightgray; widht:100%; height:35px;">
                <h3>September 2016. Maxim Frolov</h3>
            </div>
        </td>
    </tr>
</table>
</body>
</html>
