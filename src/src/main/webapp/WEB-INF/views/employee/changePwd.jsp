<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta charset="utf-8" />
    <title>變更密碼</title>
    <link href="css/bootstrap-1.css" rel="stylesheet" />
    <link href="css/font-awesome.min.css" rel="stylesheet" />
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
    <style>
        .panel{
            border-color: black!important;
        }
        .panel-heading{
            background-color: black!important;
            color:white!important;
        }
    </style>
    <script src="js/jquery-1.9.0.min.js"></script>

    <script type="text/javascript" src="js/validate.js"></script>
    <script type="text/javascript" src="js/modify_password.js"></script>
    <script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
    
    <c:if test="${requestScope.result }">
    	<script>
	    	$(function(){ 
				swal({
					  title: '密碼變更成功!',
					  text: '',
					  type: 'success'
					}).then(function(){
						window.parent.location.href = 'main.jsp';
					});
			});
		</script>
    </c:if>
	
	
    <script type="text/javascript" src="js/ready_modify.js"></script>
  </head>
  <body style="font-family: 'Noto Sans TC', sans-serif; font-weight:500;">
    <div class="header"></div>
    <div class="col-md-6" style="margin-top:50px;">
      <div class="panel panel-info">
        <div class="panel-heading" style="font-size:50px; text-align:center">
          <i class="fa fa-cog" aria-hidden="true"></i>  變更密碼
        </div>
        <div class="panel-body">
          <form class="form" role="form" action='./Employee.do?action=changPwd' method="post">
            <div class="form-group">
              <label>請輸入舊密碼</label>
              <input name="old_pw" id="old_pw" class="form-control old-pw" type="password" >
                <span class="error-msg" style="color: red">
                	<c:if test="${not empty requestScope.errorMsgs.old_pw }">
                		${requestScope.errorMsgs.old_pw }
              		</c:if>
                </span>
            </div>
            <div class="form-group">
              <label >請輸入新密碼</label>
              <input 
                id="user-pwd-input"
                name="user-pwd-input"
                type="password" 
                placeholder="請輸入不得為空且少於16位的數字與英文組合" class="new-pw form-control" />
              <span class="error-msg" style="color: red">
              	<c:if test="${not empty requestScope.errorMsgs.new_password }">
              		${requestScope.errorMsgs.new_password }
              	</c:if>
              </span>
            </div>
            <div class="form-group">
              <label>請再次輸入新密碼</label>
              <input placeholder="再次新確認密碼" class="form-control new-pw" type="password" name="user_pwd_again"/>
              <span class="error-msg" style="color: red">
              	<c:if test="${not empty requestScope.errorMsgs.new_again_password }">
              		${requestScope.errorMsgs.new_again_password }
              	</c:if>
              </span>
            </div>
            <button type="submit" class="btn btn-success" >確定變更</button>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>