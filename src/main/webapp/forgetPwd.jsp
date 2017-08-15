<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>忘記密碼</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/10-css3-progress-bar.css" type="text/css">
<link href="css/forgetPwd.css" rel="stylesheet" type="text/css">
<link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src='js/jquery.validate.min.js'></script>
<script src='js/additional-methods.min.js'></script>
<script src="js/forgetPwd.js"></script>
<script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<c:if test="${requestScope.result}">
	<script>
		$(function() {
			swal('完成!', '請至信箱收取忘記密碼信件', 'success')
		});
	</script>
</c:if>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div id='header_div' class="col-md-12">

				<h1 class="text-center">忘記密碼</h1>
				<hr>
			</div>
		</div>
		<div class="row">
			<ul class="breadcrumb">
				<li><a href="login.jsp">登入</a></li>
				<li class="active">忘記密碼</li>
			</ul>
			<div class="col-md-4"></div>
			<div id='main_div' class="col-md-4">
				<!-- style="display: none" -->
				<!--               action 到忘記密碼-->
				<form id='forget_Pwd_form' role="form" class="form-horizontal"
					action="./ForgetPwd.do" method="post">
					<div class="form-group">

						<label for="empno" class="col-sm-2 control-label"> 員編 </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="empno"
								value="${requestScope.empno}" name="empno" placeholder="請輸入員編"
								required /> <span class="fpwd_error"> <c:if
									test="${not empty requestScope.errorMsgs.error_empno}">${requestScope.errorMsgs.error_empno }</c:if>
							</span>
						</div>

						<label for="id" class="col-sm-2 control-label"> 身分證 </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="id"
								value="${requestScope.id}" name="id" placeholder="請輸入身分證"
								required /> <span class="fpwd_error"> <c:if
									test="${not empty requestScope.errorMsgs.error_id}">${requestScope.errorMsgs.error_id }</c:if>
							</span>
						</div>
					</div>
					<button id="submit" type="submit" class="btn btn-default col-sm-offset-5">
						送出</button>
				</form>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>


</body>
</html>