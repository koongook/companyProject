<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main Page</title>
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
<script>
    function goToWriteForm() {
        window.location.href = '/writeForm';
    }
</script>
</head>
<body>
    <div>
        <p>
            <%
                String name = (String) session.getAttribute("name");
                String grade = (String) session.getAttribute("grade");
                if (name != null && grade != null) {
                    out.print(name + " " + grade + "님 환영합니다.");
                }
            %>
        </p>
        <form action="/logout" method="get">
            <button type="submit">로그아웃</button>
        </form>
        <br>
        <button type="submit" onclick="goToWriteForm()">글쓰기</button>
        <% if ("과장".equals(grade)) { %>
            <button>대리 결재</button>
        <% } %>
    </div>
    <div>
        <select>
            <option>선택</option>
            <option>작성자</option>
            <option>결재자</option>
            <option>제목+내용</option>
        </select>
        <input type="text">
        <select>
            <option>결재상태</option>
            <option>임시저장</option>
            <option>결재대기</option>
            <option>결재중</option>
            <option>결재완료</option>
            <option>반려</option>
        </select>
        <button>검색</button>
    </div>
    <div>
        <table>
            <tr>
                <th>번호</th>
                <th>작성자</th>
                <th>제목</th>
                <th>작성일</th>
                <th>결재일</th>
                <th>결재자</th>
                <th>결재상태</th>
            </tr>
           
            <c:forEach items="${writtenContent}" var="content">
                <c:choose>
                    <c:when test="${grade.equals('부장') || content.name eq name || content.c_name eq name || content.wait eq '결재대기' || content.wait eq '결재완료' || content.wait eq '반려'}">
                        <tr>
                            <td>${content.seq}</td>
                            <td>${content.name} ${content.grade}</td>
                            <td><a href="/EPView?seq=${content.seq}">${content.title}</a></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.reg_date}"/></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.com_date}"/></td>
                            <td>${content.c_name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${content.wait == '임시저장'}">임시저장</c:when>
                                    <c:when test="${content.wait == '결재대기'}">결재대기</c:when>
                                    <c:when test="${content.wait == '결재중'}">결재중</c:when>
                                    <c:when test="${content.wait == '결재완료'}">결재완료</c:when>
                                    <c:when test="${content.wait == '반려'}">반려</c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${grade.equals('과장') && content.wait eq '결재중' && content.c_name eq name}">
                        <tr>
                            <td>${content.seq}</td>
                            <td>${content.name} ${content.grade}</td>
                            <td><a href="/EPView?seq=${content.seq}">${content.title}</a></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.reg_date}"/></td>
                            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.com_date}"/></td>
                            <td>${content.c_name}</td>
                            <td>결재중</td>
                        </tr>
                    </c:when>
                </c:choose>
            </c:forEach>
        </table>
    </div>
</body>
</html>
