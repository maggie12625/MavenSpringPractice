<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>修改員工資料</title>
<link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<link rel="stylesheet" href="css/modifyEmpData.css" type="text/css">
<link rel="stylesheet" href="css/page.css" type="text/css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<c:if test="${requestScope.result}">
	<script>
		$(function() {
			swal({
				title : '修改員工成功',
				type : 'success',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '確定'
			}).then(function() {
				window.location.href = './Employee.do?action=updateEmp_page';
			})
		});
	</script>
</c:if>
</head>
<body>
	<h1>修改員工資料</h1>
	<hr>
	<form action='./Employee.do?action=FindUpdateEmp' method="post">
		<select name='by'>
			<option value='name'>員工姓名</option>
			<option value='empno'>員工編號</option>
		</select> <input type="text" name='input' value='${requestScope.Search}'>
		<input type="submit" value="查詢">
	</form>

	<table>
		<tr>
			<td>員編</td>
			<td>員工姓名</td>
			<td>職位</td>
			<td>身分證</td>
			<td>email</td>
			<td>離職</td>
			<td></td>

		</tr>
		<c:choose>
			<c:when test="${NotFirst}">
				<c:choose>
					<c:when test="${not empty UpdateEmpInfoList}">
						<c:forEach var="UpdateEmpInfoList"
							items="${sessionScope.UpdateEmpInfoList}">
							<tr>
								<td>${UpdateEmpInfoList.empno}</td>
								<td>${UpdateEmpInfoList.name}</td>
								<td>${UpdateEmpInfoList.position}</td>
								<td>${UpdateEmpInfoList.id}</td>
								<td>${UpdateEmpInfoList.email}</td>
								<td>${UpdateEmpInfoList.end}</td>

								<td><a
									href="./Employee.do?action=updateEmp_page1&modifyempno=${UpdateEmpInfoList.empno}">
										<input name="md" type="button" value="修改">
								</a></td>

							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="7">未查詢到資料！</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose>
	</table>
	<%@ include file="/WEB-INF/views/changepage/page.jsp" %>
</body>
</html>