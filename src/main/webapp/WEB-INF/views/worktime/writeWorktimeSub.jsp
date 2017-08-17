<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.Worktime"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>工時填寫子畫面</title>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
    <link rel="stylesheet" type="text/css" href="css/writeWorktimeSub.css">
    <link href="css/cover.css" rel="stylesheet">
    
    <script src="js/jquery.min.js"></script> 
    <script src='js/jquery.validate.min.js'></script>
    <script src='js/additional-methods.min.js'></script>  
	<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
	<script src="js/writeWorktimeSub.js"></script>
	<script>
		function loading(){
			var $cover=$('body>.holdon-overlay');
			$cover.delay( 150 ).fadeIn(50);
		};
	</script>
	<c:if test="${ requestScope.result!=null }">
		<c:choose>
			<%--成功 --%>
			<c:when test='${ requestScope.result=="success" }'>
				<script>
					$(function() {
						swal({
							  title: '${ requestScope.action}成功',
							  text: '',
							  type: 'success'
							}).then(function(){
								if('提交'=='${ requestScope.action}'){
									window.parent.location.href="./Worktime.do?action=writeWorktime_page";
								}
							});
					});
				</script>
			</c:when>
			<c:otherwise>
				<script>
					$(function() {
						swal('${ requestScope.action}失敗!', '${ requestScope.result }', 'error');
					});
				</script>
			</c:otherwise>
		</c:choose>
	</c:if>
	
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
<div>
    <form action="./Worktime.do?action=saveDetailWorktime" method="post" id='worktomeSubForm'>
    	<input type="hidden" name='firstDate' value='${requestScope.worktime.weekFirstDay}'>
        <div>
            
    <!--           專編標題-->
              <table id='pj_table'>
	              <thead>
	               <tr>
	                   <td rowspan="2" colspan='1' class='bg_color'>工作名稱</td>
	                   <td rowspan="2" colspan='4' class='bg_color'>工作內容</td>
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
    <!--           工時內容-->
    
	    <c:choose>
			<c:when test="${not empty requestScope.worktime.worktimeDetailList}">
				<c:forEach var='detail' items='${requestScope.worktime.worktimeDetailList }'>
					<tr project>
					   <td>
                         <i  style="display:none" class="fa fa-minus-circle del_btn" aria-hidden="true"></i>
                           <input type='text' name="workNames" value='${detail.workName }' required maxlength="8">
                       </td>
	                   <td colspan='4'>
	                       <textarea name='workContents' required>${detail.workContent }</textarea>
	                   </td>
	                   
	<!--                   星期天 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.sun.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='sunNormal' value='${detail.sunNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.sun.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.sunNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='sunOver' value='${detail.sunOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.sunOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>
	                   
	<!--                   星期一 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.mon.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='monNormal' value='${detail.monNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.mon.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.monNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='monOver' value='${detail.monOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.monOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>
	<!--                   星期二 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.tue.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='tueNormal' value='${detail.tueNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.tue.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.tueNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='tueOver' value='${detail.tueOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.tueOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>    
	                   
	<!--                   星期三 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.wed.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='wedNormal' value='${detail.wedNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.wed.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.wedNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='wedOver' value='${detail.wedOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.wedOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>   
	                   
	<!--                   星期四 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.thu.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='thuNormal' value='${detail.thuNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.thu.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.thuNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='thuOver' value='${detail.thuOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.thuOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose> 
	                       
	<!--                   星期五 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.fri.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='friNormal' value='${detail.friNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.fri.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.friNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='friOver' value='${detail.friOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.friOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>          
	                       
	<!--                   星期六 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.sat.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='satNormal' value='${detail.satNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.sat.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.satNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='satOver' value='${detail.satOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.satOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>  
	                   
	               </tr>
				</c:forEach>
				 
			</c:when>
			
			<c:otherwise>
				<tr project>
					   <td>
                         <i  style="display:none" class="fa fa-minus-circle del_btn" aria-hidden="true"></i>
                           <input type='text' name="workNames" value='${detail.workName }' required>
                       </td>
	                   <td colspan='4'>
	                       <textarea name='workContents' required>${detail.workContent }</textarea>
	                   </td>
	                   
	<!--                   星期天 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.sun.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='sunNormal' value='${detail.sunNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.sun.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.sunNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='sunOver' value='${detail.sunOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.sunOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>
	                   
	<!--                   星期一 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.mon.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='monNormal' value='${detail.monNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.mon.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.monNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='monOver' value='${detail.monOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.monOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>
	<!--                   星期二 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.tue.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='tueNormal' value='${detail.tueNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.tue.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.tueNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='tueOver' value='${detail.tueOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.tueOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>    
	                   
	<!--                   星期三 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.wed.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='wedNormal' value='${detail.wedNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.wed.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.wedNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='wedOver' value='${detail.wedOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.wedOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>   
	                   
	<!--                   星期四 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.thu.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='thuNormal' value='${detail.thuNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.thu.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.thuNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='thuOver' value='${detail.thuOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.thuOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose> 
	                       
	<!--                   星期五 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.fri.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='friNormal' value='${detail.friNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.fri.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.friNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='friOver' value='${detail.friOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.friOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>          
	                       
	<!--                   星期六 -->
	                   <c:choose>
	                   		<c:when test='${requestScope.worktime.holidays.sat.hours==8 }'>
	                   			<td class="holiday"></td>
		                        <td class="holiday"></td>
	                   		</c:when>
	                   		<c:otherwise>
	                   			<td class="<c:if test='${requestScope.worktime.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='satNormal' value='${detail.satNormal }' >
			                       		<c:forEach begin="0" end="${8-requestScope.worktime.holidays.sat.hours }" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.satNormal==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                     </td>
			                     <td class="<c:if test='${requestScope.worktime.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                       <select name='satOver' value='${detail.satOvertime }'>
			                           <c:forEach begin="0" end="4" var="index">
			                       			<c:choose>
			                       				<c:when test="${detail.satOvertime==index }">
			                       					<option value='${index}' selected>${index}</option>
			                       				</c:when>
			                       				<c:otherwise>
			                       					<option value='${index}'>${index}</option>
			                       				</c:otherwise>
			                       			</c:choose>
			                       			
			                       		</c:forEach>
			                       </select>
			                   </td>
	                   		</c:otherwise>
	                   </c:choose>  
	                   
	               </tr>
			</c:otherwise>
		</c:choose>
    
               
              
            </table>
        </div>
        <div id='btn_div'>
            
            <input id='add_btn' type="button" value="增加工作">
            <i id='delete_btn' class="fa fa-trash" aria-hidden="true"></i>
            <span>
            	<c:if test="${requestScope.worktime.notPassReason!=null}">
            		<span>未通過原因:</span><br>
	                <span>${requestScope.worktime.notPassReason}</span>
               </c:if>
            </span>
            <input id='save_btn' type="submit" name="do" value="暫存" onclick="">
            <input id='submit' type="submit" name="do" value="提交" onclick="">
        </div>
    </form>
  </div>
  
</body>
</html>
