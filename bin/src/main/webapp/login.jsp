<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>工時系統-登入</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/login.css" rel="stylesheet">
<link href="css/cover.css" rel="stylesheet">

</head>
<body>
<div class="holdon-overlay" style="display:none">
      <div class="holdon-content-container">
        <div class="holdon-content">
            <div class="sk-rect">
                <div class="rect1"></div> 
                <div class="rect2"></div> 
                <div class="rect3"></div> 
                <div class="rect4"></div>
                <div class="rect5"></div> 
            </div>
        </div>
        <div class="holdon-message">登入中，請稍候...</div>
      </div>
 </div>
	<div class="container-fluid">
		<div id='header_row_div' class="row">
			<div id='header_div' class="col-md-12">
				<h1 class="text-center">工時系統</h1>
				<hr>
			</div>
		</div>
		<div id='main_row_div' class="row">
			<div class="col-md-4"></div>
			<div id='form_div' class="col-md-4">

				<h1 class="text-center">登入</h1>
				<hr>


				<form id='loginForm' class="form-horizontal" role="form"
					action='./Login.do' method="post">
					<div class="form-group">

						<label for="id" class="col-sm-2 control-label"> 帳號: </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="id" name="id"
								placeholder="請輸入帳號" required value='${requestScope.employee.empno }'/>
						</div>
					</div>
					<div class="form-group">

						<label for="password" class="col-sm-2 control-label"> 密碼:
						</label>
						<div class="col-sm-10">
							<input type="password" class="form-control" id="password"
								name="password" placeholder="請輸入密碼" required />
						</div>
					</div>
					<hr>
					<c:if test="${not empty errorMsgs }">
						<div class="form-group text-center ">
							<span class='error'>${requestScope.errorMsgs }</span>
						</div>
					</c:if>
					

					<div class="form-group">

						<div class="col-sm-offset-1 col-sm-10">

							<a href="forgetPwd.jsp" class="btn btn-danger" type="button">忘記密碼?</a>


							<button type="submit" class="btn btn-success pull-right" onclick="loding()">
								登入</button>

						</div>
					</div>
				</form>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src='js/jquery.validate.min.js'></script>
	<script src='js/additional-methods.min.js'></script>
	<script src="js/login.js"></script>
	 <script>
          function loding(){
              $('.holdon-overlay').fadeIn();
          }
    </script>
</body>
</html>