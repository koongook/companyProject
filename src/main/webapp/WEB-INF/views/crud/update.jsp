<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="updateForm" action="/update" method="post">
	<input type="hidden" name="seq" value="${boardVO.seq}">
	<table>
		<tr>
			<td>작성자 :<c:out value="${boardVO.mem_name}" /></td>
		</tr>
		<tr>
			<td>ID :<c:out value="${boardVO.mem_id}" /></td>
		</tr>
		<tr>
			<td>제목 :<input type="text" id="seq" name="board_subject" value="${boardVO.board_subject}"></td>
		</tr>
		<tr>
			<td>내용 :<textarea rows="5" cols="50" name="board_content" >${boardVO.board_content}</textarea></td>
		</tr>
		<c:if test="${not empty fileVOList}">
				<tr>
					<td colspan="2">첨부 파일:</td>
				</tr>
				<c:forEach items="${fileVOList}" var="file">
					<tr>
						<td colspan="2"><img src="C:/imgFile/${file.save_name}"
							alt="${file.real_name}"></td>
						<td>
							<button type="button">파일수정</button>
						</td>
					</tr>
				</c:forEach>
		</c:if>
		
		<tr>
			<td><input type="submit" value="수정완료"></td>
		</tr>
	</table>
</form>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    $('#updateForm').submit(function(event) {
        // 폼 제출 이벤트 방지
        event.preventDefault();
        
        // 폼 데이터 가져오기
        var formData = $(this).serialize();
        
        // AJAX 요청 보내기
        $.ajax({
            type: 'POST',
            url: '/update',
            data: formData,
            success: function(response) {
                // 성공 시 동작
                alert('글이 성공적으로 수정되었습니다.');
                // 페이지 리로드 또는 다른 동작 수행
                window.location.href = '/board/search'; // 리스트 페이지로 이동
            },
            error: function(xhr, status, error) {
                // 실패 시 동작
                alert('글 수정에 실패하였습니다.');
            }
        });
    });

    // 파일 수정 버튼 클릭 시
    $(document).on('click', 'button', function() {
        // 클릭된 버튼이 속한 행에 있는 첨부 파일 삭제
        $(this).closest('tr').find('img').remove();
        // 파일 입력 요소 나타내기
        $(this).closest('tr').append('<input type="file" name="file" id="file">');
    });
});

</script>
</html>