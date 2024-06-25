<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<div>
<label><input type= "checkbox" name="all" id="all">전체</label>
		<input type="button" name="btn" id="btn" value="버튼">
</div>
<div>
	<c:forEach begin="1" end="9" var="i" varStatus="num">
		<label><input type="checkbox" name="chk" id="chk${num.index}" value="체크${num.index}">체크</label>
	</c:forEach> 

</div>
</body>
</html>