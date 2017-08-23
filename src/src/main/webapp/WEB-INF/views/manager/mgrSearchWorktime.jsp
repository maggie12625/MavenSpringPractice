<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主管工時查詢</title>
    <link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/mgrSearchWorktime.css">
    <link rel="stylesheet" type="text/css" href="css/page.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
	<script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
	<script src="js/mgrSearchWorktime.js"></script>
</head>
<body>
   <div id="cover_div" style="display:none">
       <iframe name="wt_iframe" src=""></iframe>
    </div>
    <div id="m_s_wt">
        <h1>工時查詢</h1><hr>
<!--       搜尋區塊-->
        <div id="m_s_wt_select_div" class="<c:if test='${!requestScope.hadVisited }'>first_visited</c:if>">
        <form action="./Worktime.do?action=mgrSearchWorktime_page" method="post">
<!--             依照 -->
<!--             <select id='m_s_wt_select' name="select"> -->
<!--                 <option  value='id'>員工編號</option> -->
<!--                 <option  value='name'>員工姓名</option> -->
<!--             </select> -->
<!--             查詢 -->
<!--             <input id='m_s_wt_search' type="search" name="text"> -->
<!--             <input id="m_s_wt_month" type="month" value="2017-05" name="yearmon"> -->
<!--             <input id='m_s_wt_search_btn' type="submit" value="查詢"> -->
			<input id="m_s_wt_month" type="month" value='${(requestScope.yearMonth==null)?"2017-06":requestScope.yearMonth }' name="yearmon"><c:if test="${!requestScope.hadVisited }"><br></c:if>
            <input id='m_s_wt_search' type="search" placeholder='請輸入員工編號或姓名' name="keyword" value='${requestScope.keyword }'>
            
            <button id='m_s_wt_search_btn' type="submit"><i class="fa fa-search" aria-hidden="true"></i></button>
            
        </form>
        </div>
        <c:if test="${(not empty requestScope.mgrgetempworktimeList) && (requestScope.hadVisited)}">
        	<script>
        		$(function(){
        			$('#exportWorktion_btn').on('click',function(){
        				var yearMonth=$('#month_input').val(),
        					keyword=$('#keyword_input').val(),
        					email=$('email_input').val();
        				var src="./Worktime.do?action=exportWorktimeExcel&yearMonth="+yearMonth
        						+"&keyword="+keyword;
        				var $a="<a id='excel_a' href="+src+">直接下載</a>"
        				swal({
        					  title: '選擇輸出方式',
        					  type: 'info',
        					  html:$a+"<br>or<br>發送至EMAIL "+
        					  		"<input id='send_btn' type='button' value='send' onclick='sendEmail()'>",
        					  showCancelButton: true,
        					  showConfirmButton:false,
        					  cancelButtonText:
        					    '取消'
        					});
        				
        			});
        			$('#excel_a').on('click',function(){
        				$('div.swal2-container').remove();
        			});
        		});
        		function sendEmail(){
        			swal({
  					  title: '寄送中請稍候',
  					  type: 'info',
  					  
  					  showCancelButton: false,
  					  showConfirmButton:false
  					});
        			var yearMonth=$('#month_input').val(),
						keyword=$('#keyword_input').val();
        			$.ajax({
        				url : "./Worktime.do",
        				type : "post",
        				data : {
        					action:'exportWorktimeExcelByEmail',
        					yearMonth : yearMonth,
        					keyword:keyword
        				},
        				success : function(data) {
        					swal('傳送成功', '', 'success');
        				},
        				error : function() {
        					swal('伺服器連線錯誤!', '請稍後再試。', 'error');
        				}
        			});
    			}
        	</script>
        	
        	<input id='month_input' type="hidden" name='month' value='${requestScope.yearMonth }' >
        	<input id='keyword_input' type="hidden" name='keyword' value='${requestScope.keyword }'>
        	<button id='exportWorktion_btn'><i class="fa fa-download" aria-hidden="true"></i> 匯出工時</button>
        </c:if>
        
<!--        搜尋結果區塊-->
        <div id="e_s_wt_result_div">
            <div id="wt_div" class="worktime_div">
<!--          min-w:40rem     工時標題-->
				<c:choose>
					<c:when test="${not empty requestScope.mgrgetempworktimeList }">
						<table>
	                       <thead>
	                           <tr>
	                               <td>
	                                   日期
	                               </td>
	                               <td>
	                                   員編
	                               </td>
	                               <td>
	                                   姓名
	                               </td>
	                               <td>
	                                   星期日
	                               </td>
	                               <td>
	                                   星期一
	                               </td>
	                               <td>
	                                   星期二
	                               </td>
	                               <td>
	                                   星期三
	                               </td>
	                               <td>
	                                   星期四
	                               </td>
	                               <td>
	                                   星期五
	                               </td>
	                               <td>
	                                   星期六
	                               </td>
	                               <td>
	                                   
	                               </td>
	                           </tr>
	                       </thead>
	                       <tbody>
	                       <c:forEach var="wt" items="${requestScope.mgrgetempworktimeList}">
	                           <tr>
	                               <td>
	                                 <span><c:out value="${wt.weekFirstDay }"></c:out></span>~<span><c:out value="${wt.weekFirstDay }"></c:out></span>
	                               </td>
	                               <td>
	                                  <c:out value="${wt.empNo}"></c:out>
	                               </td>
	                               <td>
	                                 <c:out value="${wt.employee.name}"></c:out>  
	                               </td>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.sun.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.sunNormal}"></c:out></span>
			                                    <span><c:out value="${wt.sunOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.mon.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.monNormal}"></c:out></span>
			                                    <span><c:out value="${wt.monOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.tue.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.tueNormal}"></c:out></span>
			                                    <span><c:out value="${wt.tueOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.wed.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.wedNormal}"></c:out></span>
			                                    <span><c:out value="${wt.wedOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.thu.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.thuNormal}"></c:out></span>
			                                    <span><c:out value="${wt.thuOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.fri.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.friNormal}"></c:out></span>
			                                    <span><c:out value="${wt.friOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <c:choose>
	                               		<c:when test="${wt.holidays.sat.hours==8 }">
	                               			<td class="holiday">
				                                 <span></span>
				                                 <span></span>
				                            </td>
	                               		</c:when>
	                               		<c:otherwise>
	                               			<td class="<c:if test='${wt.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
			                                    <span><c:out value="${wt.satNormal}"></c:out></span>
			                                    <span><c:out value="${wt.satOvertime}"></c:out></span>
			                                </td>
	                               		</c:otherwise>
	                               </c:choose>
	                               <td>
	                                   <input type="button" value="詳細">
	                               </td>
	                           </tr>
	                           </c:forEach>
	                                         
	                       </tbody>
	                   </table>
	                   <%@ include file="/WEB-INF/views/changepage/page.jsp" %>
					</c:when>
					<c:otherwise>
							<c:if test="${requestScope.hadVisited }">
								<i style='color: red'>很抱歉，查無資料!</i>
							</c:if>
					</c:otherwise>
				</c:choose>
            </div>
        </div>
    </div>
</body>
</html>
