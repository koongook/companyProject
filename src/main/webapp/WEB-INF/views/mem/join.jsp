<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="joinForm" action="/join" method="post">
	<table>
		<tr>
			<th><h2>회원가입</h2></th>
		</tr>
	
		<tr>
			<td>ID : <input type="text" name="mem_id"></td>
		</tr>
		<tr>
			<td>Password : <input type="password" name="password"></td>
		</tr>
		<tr>
			<td>Nickname : <input type="text" name="nickname"></td>
		</tr>
		<tr>
			<td>Email : <input type="email" name="email"></td>
		</tr>
		<tr>
			<td>Phone : <input type="text" name="phone"></td>
		</tr>
		<tr>
			<td><input type="submit" value="가입하기"></td>
		</tr>
	</table>
</form>
<script>
	function successJoin() {
		alert("회원가입이 성공했습니다.");
		window.location.href="/member";
	}
	window.onload = function() {
		document.getElementById("joinForm").addEventListener("submit", function(event) {
			event.preventDefault();
			successJoin();
		});
	};
</script>
</body>

</html>