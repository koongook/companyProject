<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
    function displayAlert() {
        var error = document.getElementById("error").value;
        if (error === "invalid_id") {
            alert("등록된 아이디가 아닙니다.");
        } else if (error === "incorrect_password") {
            alert("비밀번호가 일치하지 않습니다.");
        }
    }
    window.onload = displayAlert;
</script>
</head>
<body>
    <form id="loginForm" action="/eplogin" method="post">
        <div class="logform">
            <label>LOGIN</label>
            <br>
            <label>
                ID : <input type="text" name="id" id="id" required>
            </label><br>
            <label>
                Password : <input type="password" name="password" id="password" required>
            </label><br>
            <input type="submit" value="로그인">
        </div>
    </form>
    <input type="hidden" id="error" value="${error}">
</body>
</html>
