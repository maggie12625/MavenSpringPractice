<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	    <link rel="stylesheet" href="css/modifyEmpData1.css"type="text/css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"> </script>
	<script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>

<title>Insert title here</title>

</head>
<body>

	<h1>修改員工資料</h1>
	<hr>
	<form action="./Employee.do?action=doUpdateEmpInfo" method="post">
		<table>
		
            <tr>  
				<td class="bg_b_cl_w">員編</td> 
				<td>${Empno}</td>
				<td class="bg_b_cl_w">姓名</td>
				<td><input type="text" name="name" value="${modifyInfoList.name}" disabled="disabled"><!-- disabled不會有參數 所以用之前暫存的 -->
              	</td>
				  
			</tr>
			<tr>  
				<td class="bg_b_cl_w">身分證</td> 
				<td><input type="text"name="id"value="${modifyInfoList.id}" disabled="disabled">
				</td>
				
				<td class="bg_b_cl_w">職位</td>
				<td>
				
				   <c:choose>
				<c:when test="${modifyInfoList.positionME =='employee'}">
				<input id="m" type="radio"name="positionME" disabled="disabled"><label for="m" >主管</label>
				<input id="e" type="radio"name="positionME" disabled="disabled" checked="checked"><label for="e" >員工</label>
				</c:when>
				<c:otherwise>
				<input id="m" type="radio"name="positionME" disabled="disabled" checked="checked"><label for="m" >主管</label>
				<input id="e" type="radio"name="positionME" disabled="disabled"><label for="e" >員工</label>
				</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${modifyInfoList.positionSys=='on'}">
			<input id="positionSys" type="checkbox" name="positionSys" disabled="disabled" checked="checked"><label for="positionSys">系統管理員</label>
			</c:when>
			<c:otherwise>
			<input id="positionSys" type="checkbox" name="positionSys" disabled="disabled"><label for="positionSys">系統管理員</label>
			</c:otherwise>
			</c:choose>
					
						
				</td>
			</tr>
			<tr>  
				<td class="bg_b_cl_w">email</td> 
				<td>
				    <input type="text" name="email" value="${modifyInfoList.email}" disabled="disabled">
				</td>
				<td class="bg_b_cl_w">離職</td>
				<td>
				<c:choose>
						<c:when test="${modifyInfoList.end == 'unEnd'}">
							<input id="undrop" type="radio" value="unEnd" name="end" checked="checked" disabled="disabled">
							<label for="undrop">未離職</label>
							<input id="drop" type="radio" value="End" name="end" disabled="disabled">
							<label for="drop">離職</label>						
						</c:when>
						<c:otherwise>
							<input id="undrop" type="radio" value="unEnd" name="end" disabled="disabled">
							<label for="undrop">未離職</label>
							<input id="drop" type="radio" value="End" name="end"checked="checked" disabled="disabled">
							<label for="drop">離職</label>	
						</c:otherwise>
					</c:choose> 
				 </td>
			</tr>
			
		</table>
		<input id="submit"type="submit" value="確定修改">
        <a href="./Employee.do?action=cancel"><input id="cancel"type="button" value="取消修改"></a>
        <!-- UPDATE_EMP_PAGE1為可修改的畫面 -->
	</form>
</body>
</html>