<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<link rel="stylesheet" href="css/empAdd.css" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
 <script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<c:if test="${requestScope.result}"	>
 <script>
			$(function(){ 
				swal({
					  title: '新增員工成功',
					  type: 'success',
					  confirmButtonColor: '#3085d6',
					  confirmButtonText: '確定'
					}).then(function () {
						window.location.href = './Employee.do?action=addEmp';
					})
			});
</script>
</c:if>	
</head>
<body>
	<h1>新增員工</h1>
	<hr>
	<form action="./Employee.do?action=validateInsertEmp" method="post">
		<table>
			<tr>
				<td class="bg_b_cl_w">員編:</td>
				<td><input id='e_id' type="text" name="e_id"
					value="${sessionScope.maxEmpNo}" disabled='disabled'></td>
				<td class="bg_b_cl_w">姓名:</td>
				<td><input type="text" id="name" name="name"
					placeholder="請輸入姓名" value="${sessionScope.name}" required>
					<span class="addEmp_error"> <c:if
							test="${not empty requestScope.errorMsgs.name }">
                		${requestScope.errorMsgs.name }
              		</c:if></span></td>
			</tr>
			<tr>
				<td class="bg_b_cl_w">身分證:</td>
				<td><input type="text" id="id" name="id" placeholder="請輸入身分證"
					value="${sessionScope.id}" required> <span class="addEmp_error">
						<c:if test="${not empty requestScope.errorMsgs.id }">
                		${requestScope.errorMsgs.id }
              		</c:if>
				</span></td>
				<td class="bg_b_cl_w">職位:</td>
				<td><c:choose>
						<c:when test="${sessionScope.positionME =='manager'}">
							<input id="m" type="radio" value="manager" name="positionME" checked="checked">
							<label for="m">主管</label>
							<input id="e" type="radio" value="employee" name="positionME">
							<label for="e">員工</label>						
						</c:when>
						<c:otherwise>
							<input id="m" type="radio" value="manager" name="positionME">
							<label for="m">主管</label>
							<input id="e" type="radio" value="employee" name="positionME" checked="checked">
							<label for="e">員工</label>
						</c:otherwise>
					</c:choose> <c:choose>
						<c:when test="${sessionScope.positionSys=='on'}">
							<input id="positionSys" type="checkbox" value="on" name="positionSys"
								checked="checked">
							<label for="positionSys">系統管理員</label>
						</c:when>
						<c:otherwise>
							<input id="positionSys" type="checkbox" value="on" name="positionSys">
							<label for="positionSys">系統管理員</label>
						</c:otherwise>
					</c:choose> <span class="addEmp_error"> <c:if
							test="${not empty requestScope.errorMsgs.position }">
                		${requestScope.errorMsgs.position}
              		</c:if></span></td>
			</tr>
			<tr>
				<td class="bg_b_cl_w">email:</td>
				<td colspan="3"><input type="text" id="email" name="email"
					placeholder="請輸入e-mail" value="${sessionScope.email}" required>
					<span class="addEmp_error"> <c:if
							test="${not empty requestScope.errorMsgs.email }">
                		${requestScope.errorMsgs.email}
              		</c:if></span></td>
			</tr>

		</table>
		<input id="submit" type="submit" value="新增">
	</form>
</body>
</html>