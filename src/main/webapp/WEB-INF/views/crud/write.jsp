<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="writeForm" action="/write" method="post" enctype="multipart/form-data">
	<table>
		<tr>
			<td>작성자 :<input type="text" id="mem_name" name="mem_name"></td>
		</tr>
		<tr>
			<td>ID :<input type="text" id="mem_id" name="mem_id"></td>
		</tr>
		<tr>
			<td>제목 :<input type="text" id="board_subject" name="board_subject"></td>
		</tr>
		<tr>
			<td>내용 :<textarea rows="5" cols="50" name="board_content"></textarea></td>
		</tr>
		<tr>
			<td><input type="submit" value="등록"></td>
		</tr>
		<tr>
			<td><button type="button" id="fileBtn">파일첨부</button></td>
		</tr>
		<tr id="fileInputsContainer">
			<td>
				<!-- 기본 파일 입력 -->
				<input type="file" name="files" class="fileInput" style="display: none;">
			</td>
		</tr>
	</table>
</form>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    var maxFileInputs = 5; // 최대 파일 입력 요소 수
    var fileInputIndex = 0; // 파일 입력 요소 인덱스

    $('#fileBtn').click(function() {
        if (fileInputIndex < maxFileInputs) {
            var fileInput = $('<input type="file" name="files" class="fileInput">');
            var deleteButton = $('<button type="button" class="deleteBtn">파일 삭제</button>');
            var fileContainer = $('<div class="fileContainer"></div>');
            fileContainer.append(fileInput);
            fileContainer.append(deleteButton);
            $('#fileInputsContainer').append(fileContainer);
            fileInputIndex++;
        } else {
            alert("최대 " + maxFileInputs + "개의 파일을 첨부할 수 있습니다.");
        }
    });

    // 파일 삭제 버튼 클릭 시 해당 파일 입력 요소 제거
    $(document).on('click', '.deleteBtn', function() {
        $(this).parent('.fileContainer').remove();
        fileInputIndex--;
    });

    // 파일 입력 요소에 onchange 이벤트 추가하여 파일 선택 시 유효성 검사 수행
    $(document).on('change', 'input[type=file]', function() {
        var file = this.files[0];
        var fileSize = file.size;
        var fileName = $(this).val();
        var ext = fileName.split('.').pop().toLowerCase();
        var allowedExt = ['jpg', 'jpeg', 'png', 'gif']; // 허용되는 이미지 파일 확장자 배열
        var maxFileSize = 1024 * 1024 * 5; // 5MB

        // 유효성 검사
        if ($.inArray(ext, allowedExt) == -1) {
            alert('이미지 파일만 업로드할 수 있습니다.');
            $(this).val(''); // 파일 입력 요소 초기화
            return;
        }

        if (fileSize > maxFileSize) {
            alert('이미지 파일 크기는 5MB 이하여야 합니다.');
            $(this).val(''); // 파일 입력 요소 초기화
            return;
        }

        // 이미지 파일이면서 크기가 유효한 경우 이미지 미리보기 표시
        var reader = new FileReader();
        reader.onload = function(e) {
            var img = new Image();
            img.src = e.target.result;
            img.onload = function() {
                if (this.width > 500 || this.height > 500) {
                    alert('이미지 크기가 500px x 500px 이하여야 합니다.');
                    $(this).val(''); // 파일 입력 요소 초기화
                }
            };
        };
        reader.readAsDataURL(file);
    });

    $('#writeForm').submit(function(event) {
        // 폼 제출 이벤트 방지
        event.preventDefault();
        
        // FormData 객체 생성
        var formData = new FormData(this);
        
        // AJAX 요청 보내기
        $.ajax({
            type: 'POST',
            url: '/write',
            data: formData,
            processData: false,  // 데이터를 query string으로 변환하지 않음
            contentType: false,  // 데이터 형식을 설정하지 않음
            success: function(response) {
                // 페이지 리로드 또는 다른 동작 수행
                window.location.href = '/board/search'; // 리스트 페이지로 이동
            },
            error: function(xhr, status, error) {
                // 실패 시 동작
                alert('글 등록에 실패하였습니다.');
            }
        });
    });
});
</script>
</html>