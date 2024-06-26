<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
 <form id="updateForm" action="/updateEP" method="post">
        <input type="hidden" id="seq" name="seq" value="${epboardVO.seq}">
        <table>
            <tr>
                <td>번호 : <c:out value="${epboardVO.seq}" /></td>
            </tr>
            <tr>
                <td>작성자 : <c:out value="${epboardVO.name}" /></td>
            </tr>
            <tr>
                <td>제목 : <input type="text" name="title" value="${epboardVO.title}" /></td>
            </tr>
            <tr>
                <td>내용 : <textarea name="content">${epboardVO.content}</textarea></td>
            </tr>
            <tr>
                <td>
                    <button type="submit" name="action" value="임시저장">임시저장</button>
                    <button type="submit" name="action" value="결재">결재</button>
                </td>
            </tr>
        </table>
    </form>
		<table id="history">
    <tr>
        <th>번호</th>
        <th>결재일</th>
        <th>결재자</th>
        <th>결재상태</th>
    </tr>
    <c:forEach var="history" items="${historyList}">
        <tr>
            <td><c:out value="${history.h_seq}" /></td>
            <td><fmt:formatDate value="${history.reg_date}" pattern="yyyy-MM-dd" /></td>
            <td><c:out value="${history.name}" /></td>
            <td><c:out value="${history.wait}" /></td>
        </tr>
    </c:forEach>
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