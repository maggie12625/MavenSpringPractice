<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>員工工時查詢</title>
<link rel="stylesheet" type="text/css" href="css/searchEmpWorktime.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script src="js/searchEmpWorktime.js"></script>
</head>
<body>
	<div id="cover_div" style="display: none">
		<iframe name="wt_iframe" src=""></iframe>
	</div>
	<div id='e_s_wt'>
		<h1>工時查詢</h1>
		<hr>
		<!--       搜尋區塊-->
		<div id="e_s_wt_select_div">
			<form action='./Worktime.do?action=searchEmpWorktime' method="post">
				<input id="e_s_wt_month" type="month"
					value="${requestScope.thisMonth}" name='date' required> <input
					id='e_s_wt_search_btn' type="submit" value="查詢">
			</form>
		</div>
		<!--        搜尋結果區塊-->
		<div id="e_s_wt_result_div">
			<div id="wt_div" class="worktime_div">

				<!--          min-w:40rem     工時標題-->
				<form action=''>
					<table>
						<thead>
							<tr>
								<td>日期</td>
								<td>員編</td>
								<td>姓名</td>
								<td>星期日</td>
								<td>星期一</td>
								<td>星期二</td>
								<td>星期三</td>
								<td>星期四</td>
								<td>星期五</td>
								<td>星期六</td>
								<td></td>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${requestScope.notfirst}">
									<c:choose>
										<c:when test="${not empty requestScope.worktimeList}">
											<c:forEach var="worktime"
												items="${requestScope.worktimeList}">
												<tr>
													<td><span>${worktime.weekFirstDay}</span>~<span></span></td>
													<td>${requestScope.empno}</td>
													<td>${requestScope.name}</td>

													<c:choose>
														<c:when test='${worktime.holidays.sun.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.sun.hours>0 }">partOfHoliday</c:if>'><span>${worktime.sunNormal}</span><span>${worktime.sunOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>


													<c:choose>
														<c:when test='${worktime.holidays.mon.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.mon.hours>0 }">partOfHoliday</c:if>'><span>${worktime.monNormal}</span><span>${worktime.monOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test='${worktime.holidays.tue.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.tue.hours>0 }">partOfHoliday</c:if>'><span>${worktime.tueNormal}</span><span>${worktime.tueOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test='${worktime.holidays.wed.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.wed.hours>0 }">partOfHoliday</c:if>'><span>${worktime.wedNormal}</span><span>${worktime.wedOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test='${worktime.holidays.thu.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.thu.hours>0 }">partOfHoliday</c:if>'><span>${worktime.thuNormal}</span><span>${worktime.thuOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test='${worktime.holidays.fri.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.fri.hours>0 }">partOfHoliday</c:if>'><span>${worktime.friNormal}</span><span>${worktime.friOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<c:choose>
														<c:when test='${worktime.holidays.sat.hours==8 }'>
															<td class='holiday'><span></span><span></span></td>
														</c:when>
														<c:otherwise>
															<td
																class='<c:if test="${worktime.holidays.sat.hours>0 }">partOfHoliday</c:if>'><span>${worktime.satNormal}</span><span>${worktime.satOvertime}</span>
															</td>
														</c:otherwise>
													</c:choose>

													<td><input type="button" value="詳細"></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="11">查無此資料</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise></c:otherwise>
							</c:choose>

						</tbody>
					</table>
				</form>


			</div>
		</div>
	</div>

</body>
</html>