<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	function moveToListPage() {
		window.location.href = "/boardList";
	}
	function update_form() {
		window.location.href = "/update_form?seq="
				+ document.getElementById('seq').value;
	}
	 // 파일 다운로드 함수
    function downloadFile(saveName) {
        window.location.href = "/downloadFile/" + saveName;
    }
	$(document).ready(function() {
		$('#deleteForm').submit(function(event) {
			// 폼 제출 이벤트 방지
			event.preventDefault();

			// 폼 데이터 가져오기
			var formData = $(this).serialize();

			// AJAX 요청 보내기
			$.ajax({
				type : 'POST',
				url : '/delete',
				data : formData,
				success : function(response) {
					// 성공 시 동작
					alert('글이 성공적으로 삭제되었습니다.');
					// 페이지 리로드 또는 다른 동작 수행
					window.location.href = '/board/search'; // 리스트 페이지로 이동
				},
				error : function(xhr, status, error) {
					// 실패 시 동작
					alert('글 삭제에 실패하였습니다.');
				}
			});
		});
	});
</script>
<style>
    .downloadButton {
        background-color: #008CBA;
        color: white;
        padding: 8px 20px;
        border: none;
        cursor: pointer;
        border-radius: 4px;
    }
</style>
</head>
<body>
	<form id="deleteForm" action="/delete" method="post">
		<input type="hidden" id="seq" name="seq" value="${boardVO.seq}">
		<table>
			<tr>
				<td>작성자 :<c:out value="${boardVO.mem_name}" /></td>
			</tr>
			<tr>
				<td>ID :<c:out value="${boardVO.mem_id}" /></td>
			</tr>
			<tr>
				<td>제목 :<c:out value="${boardVO.board_subject}" /></td>
			</tr>
			<tr>
				<td>내용 :<c:out value="${boardVO.board_content}" /></td>
			</tr>
			<!-- 첨부 파일 표시 -->
			<c:if test="${not empty fileVOList}">
				<tr>
					<td colspan="2">첨부 파일:</td>
				</tr>
				<c:forEach items="${fileVOList}" var="file">
					<tr>
						<td colspan="2"><img src="C:/imgFile/${file.save_name}"
							alt="${file.real_name}"></td>
						<td>
							<button type="button" class="downloadButton"
								onclick="downloadFile('${file.save_name}')">다운로드</button>
						</td>
					</tr>
				</c:forEach>
			</c:if>


		</table>
		<button type="button" onclick="moveToListPage()">목록</button>
		<button type="button" onclick="update_form()">수정</button>
		<input type="submit" value="삭제">
	</form>
</body>

</html>