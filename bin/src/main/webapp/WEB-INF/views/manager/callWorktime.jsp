<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>callWorktime</title>
<link rel="stylesheet" href="css/10-css3-progress-bar.css"
	type="text/css">
<link href="css/callWorktime.css" rel="stylesheet" />
<link rel="stylesheet" href="css/page.css" type="text/css">
<link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">

<style type="text/css">

</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script src="js/jquery-1.9.0.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<!-- 顯示修改成功訊息 -->
<c:if test="${requestScope.result}">
	<script>
		$(function() {
			swal({
				title : '催繳成功',
				type : 'success',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '確定'
			}).then(function() {
			})
		});
	</script>
	<!-- 全選 -->
</c:if>
<script>
	$(document).ready(function() {
		check_ckeckbox();
		calFirstDate();
		$("#CheckAll").click(function() {
			if ($("#CheckAll").prop("checked")) {//如果全選按鈕有被選擇的話（被選擇是true）
				$("input[name='emps']").each(function() {
					$(this).prop("checked", true);//把所有的核取方框的property都變成勾選
					$('#btn_submit').prop('disabled', false);
				})
			} else {
				$("input[name='emps']").each(function() {
					$(this).prop("checked", false);//把所有的核方框的property都取消勾選
					$('#btn_submit').prop('disabled', true);
				})
			}
		})
	})

	//確定有鉤子
	function check_ckeckbox() {
		$('input[name="emps"]').change(function() {

			$('#btn_submit').prop('disabled', true);
			if ($('tbody .box:checked').length > 0) {
				$('#btn_submit').prop('disabled', false);
			}
		});

	}
	///計算首日~end
	function calFirstDate() {
		var $all_td = $('tbody tr td:nth-child(1)');
		$all_td
				.each(function() {
					var firstDay = new Date($(this).find('span').text()), endDay = new Date(
							firstDay.getTime() + 6 * 24 * 60 * 60 * 1000);
					var dateString = (firstDay.getMonth() + 1) + '/'
							+ firstDay.getDate() + "~"
							+ (endDay.getMonth() + 1) + '/' + endDay.getDate();
					$(this).find('span').text(dateString);
				});
	}
</script>
<script>
	$(function() {
		$('#btn_submit,#call_all_a').on('click', function() {
			$('#cover_div').show();
		});
	});
</script>


</head>
<body>
	<h1>工時催繳</h1>
	<hr>
	<div class=" ">
		<form action="./Worktime.do?action=updateStatus" method="post">
			<table class="table table-hover table-bordered table-text">
							<!-- 轉圈 -->
	<div id="cover_div" style="display: none" >
		<div id="caseBlanche">
			<div id="rond">
				<div id="test"></div>
			</div>
			<div id="load">
				<p>loading</p>
			</div>
			<br>
			<br>
			<br>
			<br>
			催繳員工中
		</div>
	</div>
				<!--  -->
				<thead class="thead">
					<tr class="">
						<th>日期</th>
						<th>員工編號</th>
						<th>員工姓名</th>
						<th>催繳次數</th>
						<th>催繳/全選<input type="checkbox" class="box_all"
							name="CheckAll" id="CheckAll"></th>
					</tr>
				</thead>

				<tbody>

					<c:forEach var="UnsubmitEmp"
						items="${requestScope.UnsubmitEmpList}">
						<tr>
							<td><span>${UnsubmitEmp.firstday}</span></td>
							<td>${UnsubmitEmp.empno}</td>
							<td>${UnsubmitEmp.name}</td>
							<td>${UnsubmitEmp.calltimes}</td>
							<td><input type="checkbox" class="box" name="emps"
								value="${UnsubmitEmp.empno}~${UnsubmitEmp.firstday}~${UnsubmitEmp.email}~${UnsubmitEmp.name}"></td>
						</tr>

					</c:forEach>

				</tbody>
			</table>
			<a id='call_all_a' href="./Worktime.do?action=select_all&page=${requestScope.page.nowPage}" style="margin-right: 250px;"><input name="md"  type="button" value="全部催繳" ></a>
			<c:if test="${requestScope.page.nowPage>0}">
				<input type="hidden" name='page' value='${requestScope.page.nowPage}'>
			</c:if>
			<input type="submit" class="btn_alert" id="btn_submit" value="催繳"
				disabled='disabled'>
		    
			<%@ include file="/WEB-INF/views/changepage/page.jsp" %>
		</form>
	</div>
</body>
</html>