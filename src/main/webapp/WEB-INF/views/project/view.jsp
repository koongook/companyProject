<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <form id="epWriteForm" action="/approve" method="post">
        <input type="hidden" id="seq" name="seq" value="${epboardVO.seq}">
        <table>
            <tr>
                <th>결재요청</th>
                <th>과장</th>
                <th>부장</th>
            </tr>
            <tr>
                <td><input type="checkbox" id="requestCheckbox"></td>
                <td><input type="checkbox" id="managerCheckbox"></td>
                <td><input type="checkbox" id="directorCheckbox"></td>
            </tr>
        </table>
        <table>
            <tr>
                <td>번호 : <c:out value="${epboardVO.seq}" /></td>
            </tr>
            <tr>
                <td>작성자 : <c:out value="${epboardVO.name}" /></td>
            </tr>
            <tr>
                <td>제목 : <c:out value="${epboardVO.title}" /></td>
            </tr>
            <tr>
                <td>내용 : <c:out value="${epboardVO.content}" /></td>
            </tr>
            <tr>
                <td>
                    <c:if test="${epboardVO.wait != '결재완료' && (grade != '사원' && grade != '대리')}">
                        <!-- '결재완료'가 아니고 grade가 '사원'이나 '대리'가 아닐 때만 버튼을 보여줌 -->
                        <form id="rejectForm" method="post" style="display:inline;">
                            <input type="hidden" id="seq" name="seq" value="${epboardVO.seq}">
                            <input type="hidden" id="c_name" name="c_name" value="${name}">
                            <button type="button" onclick="submitRejectForm()" style="display:${epboardVO.wait == '반려' ? 'none' : 'inline'}">반려</button>
                        </form>
                        <form id="approveForm" action="/approve" method="post" style="display:inline;">
                            <input type="hidden" id="seq" name="seq" value="${epboardVO.seq}">
                            <input type="hidden" id="c_name" name="c_name" value="${name}">
                            <button type="submit" style="display:${epboardVO.wait == '반려' ? 'none' : 'inline'}">결재</button>
                        </form>
                    </c:if>
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
        $(document).ready(function() {
            var waitStatus = "${epboardVO.wait}";

            // 결재대기 상태일 때 결재요청에 체크
            if (waitStatus === "결재대기") {
                $("#requestCheckbox").prop("checked", true);
                $("input[type='checkbox']").prop("disabled", true);
            } else if (waitStatus === "임시저장") {
                $("#requestCheckbox").prop("checked", true);
                $("input[type='checkbox']").prop("disabled", true);
            }
            // 결재중 상태일 때 과장에 체크
            else if (waitStatus === "결재중") {
            	$("#requestCheckbox").prop("checked", true);
                $("#managerCheckbox").prop("checked", true);
                $("input[type='checkbox']").prop("disabled", true);
            }
            // 결재완료 상태일 때 부장에 체크
            else if (waitStatus === "결재완료") {
            	$("#requestCheckbox").prop("checked", true);
                $("#managerCheckbox").prop("checked", true);
                $("#directorCheckbox").prop("checked", true);
                // 결재완료일 때는 모든 체크박스를 비활성화
                $("input[type='checkbox']").prop("disabled", true);
            }
            
            else if (waitStatus === "반려") {
            	$("input[type='checkbox']").prop("disabled", true);
            }
        });

        function submitRejectForm() {
            var seq = "${epboardVO.seq}";
            var c_name = "${name}";

            // AJAX 요청
            $.ajax({
                type: "POST",
                url: "/reject",
                data: {
                    seq: seq,
                    c_name: c_name
                },
                success: function(response) {
                    // 성공 시 필요한 처리를 여기에 추가합니다.
                    window.location.replace("/main");
                    // 예를 들어 페이지 새로고침 또는 다른 동작을 수행할 수 있습니다.
                },
                error: function(xhr, status, error) {
                    // 실패 시 필요한 처리를 여기에 추가합니다.
                    alert("반려 처리 중 오류가 발생하였습니다.");
                    console.error(xhr.responseText);
                }
            });

            return false; // 폼 제출을 막기 위해 false 반환
        }
    </script>

</body>
</html>
