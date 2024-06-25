<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<form id="deleteSelectedForm" action="/deleteSelected" method="post">
	<table border="1">
		<thead>
			<tr>
				<th><input type="checkbox" id="chkAll" name="allCheck"></th>
				<th>글번호</th>
				<th>작성자(ID)</th>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;제목&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th>작성일</th>
				<th>수정일</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${boardList}" var="board_study">
				<tr class="odd gradeX">
					<td><input type="checkbox" name ="chk"class="chkGrp" value="<c:out value="${board_study.seq}"/>"></td>
					<td><c:out value="${board_study.seq}" /></td>
					<td><c:out value="${board_study.mem_id}" /></td>
					<td><a href="/view?seq=${board_study.seq}"><c:out value="${board_study.board_subject}"/></a></td>
					<td class="center"><fmt:formatDate pattern="yyyy-MM-dd" value="${board_study.reg_date}" /></td>
					<td class="center"><fmt:formatDate pattern="yyyy-MM-dd" value="${board_study.upt_date}" /></td>
					<td><c:out value="${board_study.view_cnt}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<button type="button" onclick="deleteSelected()">삭제하기</button>
</form>

	<button type="button" onclick="moveToWritePage()">글 작성하기</button>
<div id="boardList">
<form name="search-form" autocomplete="">
	<!-- select 검색기능 -->
	<select id ="searchOption" name="type" class="type-box" onchange="optionChanged()">
		<!-- <option value="" selected>선택</option> -->
		<option value="M">작성자</option>
		<option value="S">제목</option>
		<option value="SC">제목 + 내용</option>
	</select>
	<input type="text" class="inputId" name="keyword" value="${keyword }">
	<input type="submit" class="submitBtn" value="검색"><br>
</form>

	<!-- datePicker 검색기능 -->

	<label for="startDate">Start Date:</label>
	<input type="text" id="startDate"value="${startDate}">
	 
	<label for="endDate">End Date:</label>
	<input type="text" id="endDate" value="${endDate}">	
	<button id="searchButton" onclick="searchByDate()">검색</button><br>
	
</div>
	<!-- 다중 보기 기능 -->	
	<select id="cntPerPage" name="sel" onchange="selChange()">
		<option value="10"
			<c:if test="${paging.cntPerPage == 10}">selected</c:if>>10개씩 보기</option>
		<option value="30"
			<c:if test="${paging.cntPerPage == 30}">selected</c:if>>30개씩 보기</option>
		<option value="50"
			<c:if test="${paging.cntPerPage == 50}">selected</c:if>>50개씩 보기</option>
	</select>
	
	<!-- 페이징 -->
	<div style="display: block; text-align: center;">
		<a href="/board/search?nowPage=1&cntPerPage=${paging.cntPerPage}&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}">맨처음</a>
		<c:if test="${paging.startPage != 1 }">		
			<a href="/board/search?nowPage=${paging.startPage - 1 }&cntPerPage=${paging.cntPerPage}&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}">&lt;</a>
		</c:if>
		<c:forEach begin="${paging.startPage }" end="${paging.endPage }" var="p">
			<c:choose>
				<c:when test="${p == paging.nowPage }">
					<b>${p }</b>
				</c:when>
				<c:when test="${p != paging.nowPage }">
					<a href="/board/search?nowPage=${p }&cntPerPage=${paging.cntPerPage}&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}">${p }</a>
				</c:when>
			</c:choose>
		</c:forEach>
		<c:if test="${paging.endPage != paging.lastPage}">
			<a href="/board/search?nowPage=${paging.endPage+1 }&cntPerPage=${paging.cntPerPage}&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}">&gt;</a>
		</c:if>
			<a href="/board/search?nowPage=${paging.lastPage}&cntPerPage=${paging.cntPerPage}&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}">마지막</a>
	</div>
	
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
    function moveToWritePage() {
        window.location.href = "/write_form";
    }
    $(function () {
        $("#chkAll").click(function(){
            $(".chkGrp").attr("checked", this.checked);	
        });
    });
    
    function selChange() {
        var sel = document.getElementById('cntPerPage').value;
        location.href="/board/search?nowPage=${paging.nowPage}&cntPerPage="+sel+"&type=${type}&keyword=${keyword}&startDate=${startDate}&endDate=${endDate}";
    }
    $(function() {
        $("#startDate").datepicker({
            dateFormat: 'yy-mm-dd' // yy/mm/dd 형식으로 날짜를 표시하려면 'yy-mm-dd'로 설정합니다.
        });

        $("#endDate").datepicker({
            dateFormat: 'yy-mm-dd' // yy/mm/dd 형식으로 날짜를 표시하려면 'yy-mm-dd'로 설정합니다.
        });
    });

    function searchByDate() {
        var startDate = document.getElementById("startDate").value;
        var endDate = document.getElementById("endDate").value;
        window.location.href = "/board/search?startDate=" + startDate + "&endDate=" + endDate; 
        // 선택한 날짜에 해당하는 결과 페이지로 이동
    }
    window.onload = function() {
        //저장된 옵션 값 가져오기
        var selectedOption = localStorage.getItem("selectedOption");
        //저장된 값이 있다면 해당 값으로 선택
        if(selectedOption) {
            document.getElementById("searchOption").value = selectedOption;
        }
    };
    //옵션 변경 시 호출되는 함수
    function optionChanged() {
        var selectedOption = document.getElementById("searchOption").value;
        //선택된 옵션을 로컬스토리지에 저장
        localStorage.setItem("selectedOption", selectedOption);
    }
    function deleteSelected() {
        var form = $("#deleteSelectedForm");
        var formData = form.serialize();
        $.ajax({
            type: 'POST',
            url: '/deleteSelected',
            data: formData,
            success: function(response) {
                alert('선택한 항목이 성공적으로 삭제되었습니다.');
                // 성공적으로 삭제된 후에 필요한 동작 수행
                window.location.href = '/board/search';
            },
            error: function(xhr, status, error) {
                alert('삭제 중 오류가 발생했습니다.');
            }
        });
    }
    function getSearchList(){
    	$.ajax({
    		type: 'GET',
    		url : "/getSearchList",
    		data : $("form[name=search-form]").serialize(),
    		success : function(result){
    			//테이블 초기화
    			$('#boardtable > tbody').empty();
    			if(result.length>=1){
    				result.forEach(function(item){
    					str='<tr>'
    					str += "<td>"+item.idx+"</td>";
    					str+="<td>"+item.writer+"</td>";
    					str+="<td><a href = '/board/detail?idx=" + item.idx + "'>" + item.title + "</a></td>";
    					str+="<td>"+item.date+"</td>";
    					str+="<td>"+item.hit+"</td>";
    					str+="</tr>"
    					$('#boardtable').append(str);
            		})				 
    			}
    		}
    	})
    }
</script>

</body>
</html>