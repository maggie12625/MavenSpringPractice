<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<title>工時填寫</title>
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/writeWorktime.css" rel="stylesheet">
<link href="css/cover.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<script src="js/writeWorktime.js"></script>
<script>
	$(function() {
		
	});
</script>
</head>
<body>

<h1>工時填寫</h1><hr>
  <div class="table-responsive">
       <table id='main_table' class="table">
       <thead>
           <tr class="thead">
              <th rowspan="2"></th>
               <th rowspan="2">日期</th>
               <th rowspan="2">狀態</th>
               <th colspan="2">星期日</th>
               <th colspan="2">星期一</th>
               <th colspan="2">星期二</th>
               <th colspan="2">星期三</th>
               <th colspan="2">星期四</th>
               <th colspan="2">星期五</th>
               <th colspan="2">星期六</th>
           </tr>
           <tr class="thead">
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
               <th>平時</th>
               <th>加班</th>
           </tr>
       </thead>
		<tbody>
			<c:if test="${not empty requestScope.worktimeList }">
	
				<c:forEach var="worktime" items="${requestScope.worktimeList}">				
					<tr>
		               <td class="details-control">
		                   <i class="fa fa-pencil" aria-hidden="true"></i>
		               </td>
		               <td>
		               		<span style='width: 100%,height:100%'>
		               			<c:out value="${worktime.weekFirstDay}"></c:out>
		               		</span>
		               		<input type="hidden" value='${worktime.weekFirstDay}'>
		               </td>
		               <td><c:out value="${worktime.status}"></c:out></td>
		               <c:choose>
		               		<c:when test="${worktime.holidays.sun.hours==8 }">
		               			<td class="holiday" name='sunNormal'></td>
		               			<td class="holiday" name='sunOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.sun.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.sun.hours}"class="partOfHoliday" name='sunNormal'>
		               				<c:out value="${worktime.sunNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='sunOver'>
		               				<c:out value="${worktime.sunOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='sunNormal'>
				               		<c:out value="${worktime.sunNormal}"></c:out>
				               	</td>
				               	<td name='sunOver'>
				               		<c:out value="${worktime.sunOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.mon.hours==8 }">
		               			<td class="holiday" name='monNormal'></td>
		               			<td class="holiday" name='monOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.mon.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.mon.hours}" class="partOfHoliday" name='monNormal'>
		               				<c:out value="${worktime.monNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='monOver'>
		               				<c:out value="${worktime.monOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='monNormal'>
				               		<c:out value="${worktime.monNormal}"></c:out>
				               	</td>
				               	<td name='monOver'>
				               		<c:out value="${worktime.monOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.tue.hours==8 }">
		               			<td class="holiday" name='tueNormal'></td>
		               			<td class="holiday" name='tueOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.tue.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.tue.hours}" class="partOfHoliday" name='tueNormal'>
		               				<c:out value="${worktime.tueNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='tueOver'>
		               				<c:out value="${worktime.tueOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='tueNormal'>
				               		<c:out value="${worktime.tueNormal}"></c:out>
				               	</td>
				               	<td name='tueOver'>
				               		<c:out value="${worktime.tueOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.wed.hours==8 }">
		               			<td class="holiday" name='wedNormal'></td>
		               			<td class="holiday" name='wedOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.wed.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.wed.hours}" class="partOfHoliday" name='wedNormal'>
		               				<c:out value="${worktime.wedNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='wedOver'>
		               				<c:out value="${worktime.wedOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='wedNormal'>
				               		<c:out value="${worktime.wedNormal}"></c:out>
				               	</td>
				               	<td name='wedOver'>
				               		<c:out value="${worktime.wedOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.thu.hours==8 }">
		               			<td class="holiday" name='thuNormal'></td>
		               			<td class="holiday" name='thuOver'></td>
		               		</c:when>
		               		<c:when  test="${worktime.holidays.thu.hours>0 }">
		               			<td  mosthours="${8-worktime.holidays.thu.hours}" class="partOfHoliday" name='thuNormal'>
		               				<c:out value="${worktime.thuNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='thuOver'>
		               				<c:out value="${worktime.thuOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='thuNormal'>
				               		<c:out value="${worktime.thuNormal}"></c:out>
				               	</td>
				               	<td name='thuOver'>
				               		<c:out value="${worktime.thuOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.fri.hours==8 }">
		               			<td class="holiday" name='friNormal'></td>
		               			<td class="holiday" name='friOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.fri.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.fri.hours}" class="partOfHoliday" name='friNormal'>
		               				<c:out value="${worktime.friNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='friOver'>
		               				<c:out value="${worktime.friOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='friNormal'>
				               		<c:out value="${worktime.friNormal}"></c:out>
				               	</td>
				               	<td name='friOver'>
				               		<c:out value="${worktime.friOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		               <c:choose>
		               		<c:when test="${worktime.holidays.sat.hours==8 }">
		               			<td class="holiday" name='satNormal'></td>
		               			<td class="holiday" name='satOver'></td>
		               		</c:when>
		               		<c:when test="${worktime.holidays.sat.hours>0 }">
		               			<td mosthours="${8-worktime.holidays.sat.hours}" class="partOfHoliday" name='satNormal'>
		               				<c:out value="${worktime.satNormal}"></c:out>
		               			</td>
		               			<td class="partOfHoliday" name='satOver'>
		               				<c:out value="${worktime.satOvertime}"></c:out>
		               			</td>
		               		</c:when>
		               		<c:otherwise>
		               			<td name='satNormal'>
				               		<c:out value="${worktime.satNormal}"></c:out>
				               	</td>
				               	<td name='satOver'>
				               		<c:out value="${worktime.satOvertime}"></c:out>
				                </td>
		               		</c:otherwise>
		               </c:choose>
		           </tr>
	
				</c:forEach>
			</c:if>
        </tbody>
   </table>
  </div>
   

</body>
</html>