<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>假日管理</title>
<link href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<link rel="stylesheet" type="text/css" href="css/holidayMaintain.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<script src="js/holidayMaintain.js"></script>

<c:if test="${ requestScope.result!=null }">

	<c:choose>
		<%--成功 --%>
		<c:when test='${ requestScope.result=="success" }'>
			<script>
				$(function() {
					swal('修改假日成功', '', 'success')
				});
			</script>
		</c:when>
		<%--一般修改失敗--%>
		<c:when test='${ requestScope.result=="normalError" }'>
			<script>
				$(function() {
					//一般

					swal('修改假日失敗', '${ requestScope.errorMsgs}', 'error')
					$('#hd_modify_cover_div').show();
					$('#hd_modify_cover_div,#hd_cancel').on(
							'click',
							function() {
								$('#hd_modify_cover_div').stop().fadeOut(500)
										.hide(1);
							});
					
					if ($('#hd_modify_div input[name="status"]:checked').val() != 'HOLIDAY') {
						$('#hd_modify_div select[name="time"]').val(0).attr(
								'disabled', true);
					} else {
						$('#hd_modify_div select[name="time"]').val('${ requestScope.Holiday.hours }').attr(
								'disabled', false);
					}
				});
			</script>
		</c:when>
		<%--連假修改失敗--%>
		<c:otherwise>
			<script>
				//  連假
				$(function() {
					swal('修改假日失敗', '${ requestScope.errorMsgs}', 'error');
					$('#cover_div').show().find('iframe').attr('src',
							'./Holiday.do?action=longHoliday_page');
					$('#cover_div iframe')
							.load(
									function() {
										var $iframContent = $(
												'#cover_div iframe').contents();
										$iframContent.find('#start').val(
												'${ requestScope.startDate}');
										$iframContent
												.find(
														'select[name="start_time"]')
												.val(
														'${ requestScope.start.hours}');
										$iframContent.find('#end').val(
												'${ requestScope.endDate}');
										$iframContent.find(
												'select[name="end_time"]').val(
												'${ requestScope.end.hours}');
										$iframContent
												.find(
														'input:radio[name="status"][value="${ requestScope.start.status}"]')
												.prop('checked', true);
										$iframContent
												.find('textarea')
												.text(
														'${ requestScope.start.reason}');

									});
				});
			</script>
		</c:otherwise>
	</c:choose>
</c:if>


</head>
<body>
	<h1>假日維護</h1>
	<hr>
	<!--              修改區塊-->
	<div id='hd_modify_cover_div' style="display: none">
		<div id='hd_modify_div'>
			<form action="./Holiday.do?action=modify_holiday" method="post">
				<input id="modify_date" type="hidden" name="date"
					value="${requestScope.date }">
				<table>
					<tr>
						<td id="hd_modify_date_span" colspan="2">${requestScope.date }</td>
					</tr>
					<tr>
						<td><input id='hd_rd_normal' type="radio" name="status"
							class="hd_rd_normal" value='ORDINARYDAY' checked><label
							for="hd_rd_normal" class="hd_rd_normal">平日</label></td>
						<td rowspan="2"><textarea id='hd_reason' class='hd_reason'
								name='reason'>${ requestScope.Holiday.reason }</textarea></td>
					</tr>
					<tr>
						<td><input id='hd_rd_holiday' type="radio" name="status"
							value='HOLIDAY' class="hd_rd_holiday"
							<c:if test='${requestScope.Holiday.status=="HOLIDAY" }'>checked</c:if>><label
							for="hd_rd_holiday" class="hd_rd_holiday">假日</label></td>
					</tr>
					<tr>
						<td colspan="2">放假時數 <select name='time' value=''>
								<option value="8">全天</option>
								<option value="7">7小時</option>
								<option value="6">6小時</option>
								<option value="5">5小時</option>
								<option value="4">4小時</option>
								<option value="3">3小時</option>
								<option value="2">2小時</option>
								<option value="1">1小時</option>
								<option value="0">0小時</option>
						</select>放假
						</td>
					</tr>
					<tr>
						<td colspan="2"><input id='hd_submit' type="submit"
							value="修改" class="hd_btn"> <input id='hd_cancel'
							type="button" value="取消" class="hd_btn"></td>
					</tr>
				</table>
			</form>

		</div>
	</div>
	<div id="cover_div" style="display: none">
		<iframe name="h_iframe" src="">請稍候...</iframe>
	</div>

	<div id='holiday'>
		<div>

			<ul id='hd_ul'>
				<a href='./Holiday.do?action=holiday_page'><li id="hd_tab1"
					class="hd_li_sel">假日維護</li></a>
				<a href='./Holiday.do?action=longHoliday_page' target="h_iframe"><li
					id="hd_tab2">連假修改</li></a>
				<a style="float: right"><li id="hd_tab3">年度假日新增</li></a>
			</ul>
			<div id='hd_content'>

				<!--               日期區塊-->
				<div id="hd_normal">
					<div id='hd_input_div'>
						<i id='hd_p_month' class="fa fa-chevron-left" aria-hidden="true"></i> <input
							id='hd_input_month' type='month' value="2017-04"> <i id='hd_n_month' class="fa fa-chevron-right" aria-hidden="true"></i>
					</div>
					<div id='hd_date'>
						<table>
							<tr id='hd_title_day'>
								<td>星期日</td>
								<td>星期一</td>
								<td>星期二</td>
								<td>星期三</td>
								<td>星期四</td>
								<td>星期五</td>
								<td>星期六</td>
							</tr>
							<tr>
								<td class="hd_day_td holiday"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
								<td class="hd_day_td holiday"><input class='hd_modify'
									type="button" value="修改" style="display: none">
									<div class="hd_day">
										<span>12</span>
									</div>
									<p></p></td>
							</tr>
						</table>
					</div>
				</div>





			</div>
		</div>
	</div>
</body>
</html>