<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대리 결재</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
    <div>
        <h2>대리 결재자 선택</h2>
        <form action="/project/delegateApproval" method="post">
            <label for="delegate">대리 결재자 :</label>
            <select name="delegate" id="delegate">
                <option value="">선택하세요</option>
                <option value="김승우">김승우 사원</option>
                <option value="이하얀">이하얀 사원</option>
                <option value="정희성">정희성 사원</option>
                <option value="한주성">한주성 대리</option>
                <option value="김영화">김영화 대리</option>
                <option value="김환">김환 과장</option>
                <option value="이우연">이우연 과장</option>
                <option value="김성회">김성회 부장</option>
            </select>
            <br>
            <label for="grade">직급 :</label>
            <input type="text" id="grade" name="grade" readonly value="${grade}">
            <br>
            <label for="substitute">대리자 :</label>
            <input type="text" id="substitute" name="substitute" required>
            <br><br>
            <a href="/main"><button type="button">취소</button></a>
            <button type="submit">승인</button>
        </form>
    </div>
</body>
</html>
