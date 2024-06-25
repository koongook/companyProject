<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>회원가입 성공!</p>
	<button type="button" onclick="moveToLogin()">로그인하기</button>
</body>
<script>
	function moveToLogin() {
		window.location.href="/member"
	}
</script>
</html>