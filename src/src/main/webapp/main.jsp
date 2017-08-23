<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${sessionScope.login ==null}">
	<jsp:forward page="./Logout.do" />
</c:if>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="-1">

<title>工時系統</title>
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
<link href="css/cover.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="js/main.js"></script>
<script>
	function loading(){
		var $cover=$('body>.holdon-overlay');
		$cover.delay( 150 ).fadeIn(50);
		$('#main_ifram').load(function(){
			$cover.stop().fadeOut(150);
		});
	}
</script>
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
        <div class="holdon-message">處理中，請稍候...</div>
      </div>
 </div>
	<!--    個人資料-->
	<label id='label_cancel_personInfo' style='display: none'> <iframe
			id='info' src='' name="info"></iframe>
	</label>

	<div class="container-fluid">
		<div id="header_row_div" class="row">
			<div class="col-md-12">
				<nav id='bar' style="display:none"
					class="navbar navbar-default navbar-fixed-top navbar-inverse"
					role="navigation">
				<div class="navbar-header ">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target="#header_li">
						<span class="sr-only">open</span><span class="icon-bar"></span><span
							class="icon-bar"></span><span class="icon-bar"></span>
					</button>
					<a id="home_a" class="navbar-brand navbar-brand-active" href="#">首頁</a>
				</div>

				<div class="collapse navbar-collapse navbar-collapse-center"
					id="header_li">
					<ul class="nav navbar-nav navbar-center">
						<c:choose>
							<c:when test='${fn:contains(sessionScope.login.position,"員工")}'>
								<!--                            員工區塊-->
								<c:if test="${sessionScope.login.end==null }">
									<li id='writeWorktime' for_who="e"><a
									href="./Worktime.do?action=writeWorktime_page" target="iframe" onclick="loading()">工時填寫</a></li>
								</c:if>
								<li id='searchEmpWorktime' for_who="e"><a
									href="./Worktime.do?action=enterEmpWorktime" target="iframe">工時查詢</a></li>
							</c:when>
							<c:when test='${fn:contains(sessionScope.login.position, "主管")}'>
								<!--                            主管區塊-->
								<c:if test="${sessionScope.login.end==null }">
									<li id='checkWorktime' for_who="m"><a
									href="./Employee.do?action=mgrManageWorktime_page" target="iframe">工時審核</a></li>
								</c:if>
								<li id='mgrSearch' for_who="m"><a href="./Employee.do?action=mgrSearch_page"
									target="iframe">主管查詢</a></li>
							</c:when>
						</c:choose>
						<c:if test='${fn:contains(sessionScope.login.position, "系統管理員") && (sessionScope.login.end==null )}'>
							<!--                            系統管理員區-->
								<li id='holidayMaintain' for_who="a"><a
									href="./Holiday.do?action=holiday_page" target="iframe" onclick="loading()">假日維護</a></li>
								<li id='empManage' for_who="a"><a href="./Employee.do?action=empManage_page"
									target="iframe">員工管理</a></li>
						</c:if>


					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">${sessionScope.login.name }
								${sessionScope.login.position }<c:if test="${sessionScope.login.end!=null }">(離職)</c:if><strong class="caret"></strong>
						</a>
							<ul class="dropdown-menu">

								<li class="divider"></li>
								<li id='person_info' class='info'><a href="./Employee.do?action=info"
									target="info">個人資料</a></li>
								<li id='changePwd'><a href="./Employee.do?action=changPwd_page" target="iframe">變更密碼</a>
								</li>
								<li class="divider"></li>
							</ul></li>
						<li><a href="./Logout.do"><i
								class="fa fa-sign-out logout_li" aria-hidden="true">登出</i></a></li>
					</ul>
				</div>



				</nav>


				<nav id='first_bar'
					class="navbar navbar-default navbar-fixed-top navbar-inverse"
					role="navigation">
				<div class="navbar-header ">

					<a href="./Logout.do" class="navbar-toggle"><i
						class="fa fa-sign-out logout_li" aria-hidden="true">登出</i></a>
					<h1 class="text-center">工時系統</h1>
				</div>

				<div class="collapse navbar-collapse navbar-collapse-center"
					id="header_li_1">

					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">${sessionScope.login.name }
								${sessionScope.login.position }<c:if test="${sessionScope.login.end!=null }">(離職)</c:if><strong class="caret"></strong>
						</a>
							<ul class="dropdown-menu">

								<li class="divider"></li>
								<li class='info'><a href="./Employee.do?action=info"
									target="info">個人資料</a></li>
								<li class='change_pw_li'><a href="./Employee.do?action=changPwd_page"
									target="iframe">變更密碼</a></li>
								<li class="divider"></li>
							</ul></li>
						<li><a href="./Logout.do"><i
								class="fa fa-sign-out logout_li" aria-hidden="true">登出</i></a></li>
					</ul>
				</div>



				</nav>
			</div>
		</div>

		<!--        主選單-->
		<div id='main_row_div' class="row">
			<div class="col-md-1"></div>
			<div id='content_menu_div' class="col-md-10">
				<ul>
					<!--                          個人資料-->
					<li class='info' for_who="any"><div id='info_div'
							class="option">
							<i class="fa fa-id-card-o" aria-hidden="true"></i><br> 個人資料
						</div></li>

					<c:choose>
						<c:when test='${fn:contains(sessionScope.login.position,"員工")}'>
							<!--                           填寫工時 -->
							<c:if test="${sessionScope.login.end==null }">
								<li class='w_wt_li' for_who="e" onclick="loading()"><div id='write_worktime_div'
									class="option">
									<i class="fa fa-pencil-square-o" aria-hidden="true"></i><br>
									填寫工時
								</div></li>
							</c:if>
							<!--                           查詢功時-->
							<li class='em_s_wt_li' for_who="e"><div
									id='em_search_worktime_div' class="option">
									<i class="fa fa-search" aria-hidden="true"></i><br> 查詢工時
								</div></li>
						</c:when>
						<c:when test='${fn:contains(sessionScope.login.position, "主管")}'>
							<!--                           工時審核 -->
							<c:if test="${sessionScope.login.end==null }">
								<li class='m_wt_li' for_who="m"><div id='m_worktime_div'
									class="option">
									<i class="fa fa-calendar-check-o" aria-hidden="true"></i><br>
									工時審核
								</div></li>
							</c:if>
							<!--                           主管查詢 -->
							<li class='m_s_wt_li' for_who="m"><div id='m_search_div'
									class="option">
									<i class="fa fa-search" aria-hidden="true"></i><br> 主管查詢
								</div></li>
						</c:when>
					</c:choose>
					<c:if test='${fn:contains(sessionScope.login.position, "系統管理員")}'>
							<!--                           假日維護-->
							<c:if test="${sessionScope.login.end==null }">
							<!--                           假日維護-->
								<li class='hd_li' for_who="a" onclick="loading()"><div id='holiday_div'
									class="option">
									<i class="fa fa-calendar" aria-hidden="true"></i><br> 假日維護
								</div></li>
							<!--                           員工管理-->
							<li class='e_manage_li' for_who="a"><div id='e_manage_div'
									class="option">
									<i class="fa fa-users" aria-hidden="true"></i><br> 員工管理
								</div></li>
							</c:if>
					</c:if>

					<!--                           更變密碼 -->
					<li class='change_pw_li' for_who="any"><div
							id='change_password' class="option">
							<i class="fa fa-key" aria-hidden="true"></i><br> 變更密碼
						</div></li>

				</ul>
			</div>
			<div class="col-md-1"></div>
		</div>

		<!--        內容-->
		<div id='content_row_div' style="display: none" class="row">
			<!--       子視窗-->
			<iframe src="" id='main_ifram' name="iframe"> </iframe>

		</div>
	</div>

<script src="js/bootstrap.min.js"></script>
</body>
</html>