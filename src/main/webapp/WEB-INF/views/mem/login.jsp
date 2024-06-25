<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/login" method="post">
	<label><h1>로그인</h1></label>
	<label>
		ID : <input type="text">	
	</label> <br>
	<label>
		Password: <input type="password">
	</label> <br>
	<button type="button" onclick="moveToJoin()">회원가입</button>
	<input type="submit" value="로그인">
</form>
<script>
	function moveToJoin() {
		window.location.href="/join_form"
	}
</script>
</body>
</html>