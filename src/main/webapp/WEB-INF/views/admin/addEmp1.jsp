<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<link rel="stylesheet" href="css/10-css3-progress-bar.css" type="text/css">
<link rel="stylesheet" href="css/empAdd1.css" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<script>
	$(function() {
		$('#submit').on('click', function() {

		 $('#cover_div').show();
		});
	});
</script>
</head>
<body>
	<div id="cover_div" style="display: none" >
		<div id="caseBlanche">
			<div id="rond">
				<div id="test"></div>
			</div>
			<div id="load">
				<p>loading</p>
			</div>
					<br><br><br>新增員工中
		</div>

	</div>
	<h1>新增員工</h1>
	<hr>
	<form action='./Employee.do?action=insertEmp' method="post">
		<table>
			<tr>
				<td class="bg_b_cl_w">員編:</td>
				<td>${sessionScope.maxEmpNo}</td>
				<td class="bg_b_cl_w">姓名:</td>
				<td><input type="text" name="name" value="${sessionScope.name}"
					disabled="disabled"></td>

			</tr>
			<tr>
				<td class="bg_b_cl_w">身分證:</td>
				<td><input type="text" name="id" value="${param.id}"
					disabled="disabled"></td>
				<td class="bg_b_cl_w">職位:</td>
				<td><c:choose>
						<c:when test="${param.positionME =='employee'}">
							<input id="m" type="radio" name="positionME" disabled="disabled">
							<label for="m">主管</label>
							<input id="e" type="radio" name="positionME" disabled="disabled"
								checked="checked">
							<label for="e">員工</label>
						</c:when>
						<c:otherwise>
							<input id="m" type="radio" name="positionME" disabled="disabled"
								checked="checked">
							<label for="m">主管</label>
							<input id="e" type="radio" name="positionME" disabled="disabled">
							<label for="e">員工</label>
						</c:otherwise>
					</c:choose> <c:choose>
						<c:when test="${param.positionSys=='on'}">
							<input id="positionSys" type="checkbox" name="positionSys"
								disabled="disabled" checked="checked">
							<label for="positionSys">系統管理員</label>
						</c:when>
						<c:otherwise>
							<input id="positionSys" type="checkbox" name="positionSys"
								disabled="disabled">
							<label for="positionSys">系統管理員</label>
						</c:otherwise>
					</c:choose></td>
			</tr>
			<tr>
				<td class="bg_b_cl_w">email:</td>
				<td colspan="3"><input id="email" type="text" value="${param.email}"
					disabled="disabled"></td>

			</tr>

		</table>
		<input id="submit" type="submit" value="確定新增"> <a
			href="./Employee.do?action=addEmp"><input id="cancel"
			type="button" value="取消新增"></a> <input type="hidden" name="ok"
			value="true">
	</form>

</body>
</html>