<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>詳細工時${requestScope.errorMsgs==null}</title>
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
<link rel="stylesheet" type="text/css" href="css/detailWorktime.css">
<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script>
	
	$(function() {
		<c:if test="${not empty requestScope.errorMsgs}"> swal('查尋失敗', '${requestScope.errorMsgs}', 'error')</c:if>
		var $date = $('table').eq(0).find('tr td input:first-child').eq(0);
		var date = $date.val();
		if (date != "") {
			var year = date.split("-")[0], month = date.split("-")[1], day = date
					.split("-")[2];
	        endDate=new Date(year,month-1,day);
	        endDate.setTime(endDate.getTime()+(6*24*60*60*1000));
	        
	        e_year=endDate.getFullYear();
	        e_mon=endDate.getMonth()+1;
	        if(e_mon==13){
	            e_mon='01';
	        }
	        e_day=endDate.getDate();

			$date.val(year + "/" + month + '/' + day + ' ~ ' + e_year + "/"
					+ e_mon + '/' + e_day);
		}

		$('#close').on(
				'click',
				function() {
					$(window.parent.document).find('#cover_div').stop()
							.fadeOut(300).hide(0);
				});
	});
</script>
</head>
<body>
	<table>
		<tr>
			<td colspan='2' class='bg_color'>日期</td>
			<td colspan='3'><input type='text'
				value='${requestScope.DetailWorktime.weekFirstDay }' disabled></td>
			<td colspan='2' class='bg_color'>狀態</td>
			<td colspan='3'><input type='text'
				value='${requestScope.DetailWorktime.status }' disabled></td>
		</tr>
		<tr>
			<td colspan='2' class='bg_color'>員編</td>
			<td colspan='3'><input type='text'
				value='${requestScope.DetailWorktime.empNo }' disabled></td>
			<td colspan='2' class='bg_color'>姓名</td>
			<td colspan='3'><input type='text'
				value='${requestScope.DetailWorktime.employee.name }' disabled></td>
		</tr>
	</table>
	<div>
		<table>
			<thead>
				<tr>
					<td rowspan="2" colspan='1' class='bg_color'>工作名稱</td>
					<td rowspan="2" colspan='1' class='bg_color'>工作內容</td>
					<td colspan='2' class='bg_color'>星期日</td>
					<td colspan='2' class='bg_color'>星期一</td>
					<td colspan='2' class='bg_color'>星期二</td>
					<td colspan='2' class='bg_color'>星期三</td>
					<td colspan='2' class='bg_color'>星期四</td>
					<td colspan='2' class='bg_color'>星期五</td>
					<td colspan='2' class='bg_color'>星期六</td>
				</tr>
				<tr>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
					<td class='bg_color'>平</td>
					<td class='bg_color'>加</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var='wtd'
					items='${requestScope.DetailWorktime.worktimeDetailList }'>
					<tr>
						<!--               專編-->
						<td>${wtd.workName }</td>
						<!--                工作內容-->
						<td><textarea disabled>${wtd.workContent }</textarea></td>
						<!--               工作時數-->
						<!--               日-->
						<c:choose>
							<c:when test="${requestScope.holidays.sun.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.sunNormal }</span>
									<span>${wtd.sunOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						
						<!--                一-->
						<c:choose>
							<c:when test="${requestScope.holidays.mon.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.monNormal }</span>
									<span>${wtd.monOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						<!--                二-->
						<c:choose>
							<c:when test="${requestScope.holidays.tue.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.tueNormal }</span>
									<span>${wtd.tueOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						<!--                三-->
						<c:choose>
							<c:when test="${requestScope.holidays.wed.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.wedNormal }</span>
									<span>${wtd.wedOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						<!--                四-->
						<c:choose>
							<c:when test="${requestScope.holidays.thu.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.thuNormal }</span>
									<span>${wtd.thuOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						<!--                五-->
						<c:choose>
							<c:when test="${requestScope.holidays.fri.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.friNormal }</span>
									<span>${wtd.friOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
						<!--                六-->
						<c:choose>
							<c:when test="${requestScope.holidays.sat.hours==8 }">
								<td colspan='2' class="holiday">
									<span></span>
									<span></span>
								</td>
							</c:when>
							<c:otherwise>
								<td colspan='2'
								class="<c:if test='${requestScope.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
									<span>${wtd.satNormal }</span>
									<span>${wtd.satOvertime }</span>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>

				</c:forEach>

			</tbody>
		</table>
	</div>
	<i id='close' class="fa fa-times-circle-o" aria-hidden="true"></i>
</body>
</html>