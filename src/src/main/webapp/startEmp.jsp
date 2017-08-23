<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>啟用帳號</title>

<link rel="stylesheet" type="text/css" href="css/test2.css">
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<link rel="stylesheet" href="css/startEmp.css">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<c:if test="${requestScope.result!='success' }">
	<script>
		function aa() {
			swal({
				title : '重新啟動',
				text : '請輸入員編',
				input : 'text',
				inputAttributes : {
					'id' : 'empno',
					'maxlength' : 6,
				},
				showCancelButton : true,
				confirmButtonText : '送出',
				showLoaderOnConfirm : true,
				inputValidator : function(value) {
					return new Promise(function(resolve, reject) {
						if (value.match("\\d{6}")) {
							$.ajax({
								url : './StartEmployee.do',
								type : 'GET',
								data : {
									empno : value,
									action : "restart"
								},
								dataType : "json",
								error : function() {
									swal({
										type : 'error',
										title : '重新啟動失敗!',
										text : '請稍後再試。'
									})
								},
								success : function(json) {
									if(json.result=='success'){
										swal({
											type : 'success',
											title : '重新啟動成功!',
											text : '已寄發電子郵件，請檢查電子郵件。'
										})	
									}else{
										var message=json.result;
										swal({
											type : 'error',
											title : '重新啟動失敗!',
											text : message
										}).then(function(){
											if(message.indexOf('帳號已經啟動')!=-1){
												window.parent.location.href = 'login.jsp';
											}
										})
									}
										
								}
							});

						} else {
							reject('請輸入員編')
						}
					})
				}
			});
			$('#empno').val('${requestScope.empno }');
		}
	</script>
</c:if>

</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<table>

					<c:choose>
						<c:when test="${requestScope.result=='success' }">
							<tr>
								<td colspan="2"><i class="fa fa-check-circle-o fa-5x"
									aria-hidden="true"></i></td>
							</tr>
							<tr>
								<td colspan="2" id="start_emp_succes">啟用成功</td>
							</tr>
							<tr>
								<td colspan="2" class='red'>提醒：登入密碼為身分證字號</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td colspan="2"><button
										onclick="location.href='login.jsp'">登入頁面</button></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="2"><i class="fa fa-times-circle-o fa-5x"
									aria-hidden="true"></i></td>
							</tr>
							<tr>
								<td colspan="2" id="start_emp_succes">啟用失敗</td>
							</tr>
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td colspan="2">
									<button onclick="aa()">重新啟動</button>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>



				</table>

			</div>
			<div class="col-md-4"></div>
		</div>
	</div>


</body>


</html>