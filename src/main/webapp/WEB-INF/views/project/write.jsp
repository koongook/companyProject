<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    table {
        border-collapse: collapse; /* 중복된 경계선을 결합 */
        width: 100%;
    }
    th, td {
        border: 1px solid black; /* 테이블 경계선 */
        padding: 8px; /* 셀 내부의 여백 */
        text-align: left; /* 텍스트 정렬 */
    }
    th {
        background-color: #f2f2f2; /* 헤더 배경색 */
    }
</style>
</head>
<body>
	<form id="epWriteForm" action="/EPwrite" method="post">
		<table>
			<tr>
				<th>결재요청</th>
				<th>과장</th>
				<th>부장</th>
			</tr>
			<tr>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
				<td><input type="checkbox"></td>
			</tr>
		</table>
		<table>
			<tr><td>번호 :<input type="text" name="seq" value="${seq}" readonly> </td>
			</tr>
			<tr><td>작성자 :<input type="text" name="name" value="${name}"readonly> </td>
			</tr>
			<tr><td>제목 :<input type="text" name="title"> </td></tr>
			<tr><td>내용 :<textarea name="content"></textarea> </td></tr>
			<tr><td>
				<button type="submit" name="action" value="임시저장">임시저장</button>
				<button type="submit" name="action" value="결재">결재</button>
				</td>
			</tr>
		</table>
	</form>
		<table>
			<tr>
				<th>번호</th>
				<th>결재일</th>
				<th>결재자</th>
				<th>결재상태</th>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
<script>
function submitForm() {
    var formData = $("#epWriteForm").serialize(); // form 데이터 가져오기
    $.ajax({
        type: "POST",
        url: "/EPwrite",
        data: formData, // 가져온 form 데이터 전송
        success: function(response) {
            window.location.href = "/main"; // 성공 시 main 페이지로 이동
        },
        error: function() {
            alert("글쓰기에 실패했습니다."); // 실패 시 알림
        }
    });
}
</script>

</body>
</html>