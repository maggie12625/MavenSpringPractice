<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <title>工時審核</title>
            <link rel="stylesheet" type="text/css" href="css/checkWorktime.css">
            <link rel="stylesheet" href="css/page.css" type="text/css">
            <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
            <link href="css/cover.css" rel="stylesheet">
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
            <script src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
            <script src="js/checkWorktime.js"></script>


            <style>
                table tbody tr td:not(:first-child) span {
                    display: inline-block;
                    width: 45%;
                }
            </style>
            

            <script type="text/javascript">
                    $(function(){
                    	$(window).bind('beforeunload',function(){
                    		$('body>.holdon-overlay').fadeIn();
                    		return ;
                    	});
                    	<c:if test='${requestScope.result=="success" }'>
	                    	swal('審核成功!','','success');
	                    </c:if>
                       $('.input-type-text').on('click',function(){
                    	   var btn = $(this);
                    	   swal({
                            title: '請輸入未通過原因',
                            input: 'text',
                            showCancelButton: true,
                            inputValidator: function(value) {
                                return new Promise(function(resolve, reject) {
                                    if (value) {
                                        resolve(value)
                                    } else {
                                        reject('退回工時請寫未通過之原因!!')
                                    }
                                })
                                
                            }
                        }).then(function(result) {
                        	var tr = btn.closest('tr');
                        	$('input[name=id]').val(tr.find('td:nth-child(2)').text())
                        	$('input[name=firstday]').val(tr.find('input[name=firstDay]').val())
                        	$('input[name=reason]').val(result)
                            $('#notPassReason').submit();
                        	$('body>.holdon-overlay').fadeIn(300);
                        })
                       })
                    });
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
            <div id="cover_div" style="display:none">
                <iframe name="wt_iframe" src=""></iframe>
            </div>
            <h1>工時審核</h1>
            <hr>
            <form id='search_form' action='./Worktime.do?action=searchWorktime_page' method='post'>
                <span style="font-size:25px;font-weight:900">選取周別:</span>
                <input type="week" name="week" id="week">
                <input type="hidden" name="weekBeginDate">
                <select name='by'>
                    <option value='name'>員工姓名</option>
                    <option value='empno'>員工編號</option>
                </select>
                <input type="text" name='keyword'>
                <input type="submit" value="查詢">
            </form>

            <!--    待審資料區-->
            <div id='content_div'>
                <form action='./Worktime.do?action=checkBox_page' method='post' id='m_form'>
                    <input type="hidden" name="test" value="test">
                    <table>
                        <!--           標題-->
                        <thead>
                            <tr>
                                <td class='min_width' rowspan="2">日期</td>
                                <td rowspan="2">員編</td>
                                <td rowspan="2">姓名</td>
                                <td class='min_width' colspan='2'>星期日</td>
                                <td class='min_width' colspan='2'>星期一</td>
                                <td class='min_width' colspan='2'>星期二</td>
                                <td class='min_width' colspan='2'>星期三</td>
                                <td class='min_width' colspan='2'>星期四</td>
                                <td class='min_width' colspan='2'>星期五</td>
                                <td class='min_width' colspan='2'>星期六</td>
                                <td rowspan="2">詳細</td>
                                <td rowspan="2">未通過</td>
                                <td rowspan="2">
                                    <input id='sel_all_cb' type="checkbox" class='checkbox'>
                                    <label for='sel_all_cb'>全選</label>
                                </td>
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

                        <!--            資料內容-->
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty empWeekWorktime}">
                                            <c:forEach var="empWeekWork" items="${empWeekWorktime}" >
                                                <tr id='${empWeekWork.employee.empno}'>
                                                    <td>
                                                        <span>${empWeekWork.weekFirstDay }</span>
                                                        <input type="hidden" name="firstDay" value='${empWeekWork.weekFirstDay }'>
                                                    </td>
                                                    <td>${empWeekWork.employee.empno}</td>
                                                    <td>${empWeekWork.employee.name}</td>

                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.sun.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.sun.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.sunNormal}</span>
                                                                <span>${empWeekWork.sunOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.mon.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.mon.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.monNormal}</span>
                                                                <span>${empWeekWork.monOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.tue.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.tue.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.tueNormal}</span>
                                                                <span>${empWeekWork.tueOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.wed.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.wed.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.wedNormal}</span>
                                                                <span>${empWeekWork.wedOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.thu.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.thu.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.thuNormal}</span>
                                                                <span>${empWeekWork.thuOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.fri.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.fri.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.friNormal}</span>
                                                                <span>${empWeekWork.friOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${empWeekWork.holidays.sat.hours==8 }">
                                                            <td class="holiday" colspan='2'>
                                                                <span></span>
                                                                <span></span>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td colspan='2' class="<c:if test='${empWeekWork.holidays.sat.status=="HOLIDAY" }'>partOfHoliday</c:if>">
                                                                <span>${empWeekWork.satNormal}</span>
                                                                <span>${empWeekWork.satOvertime}</span>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <td>
                                                        <input class='detial_btn' type='button' value='詳細'>
                                                    </td>
                                                    <td>
                                                    	 <input class='input-type-text' type='button' value='未通過'>
                                                    </td>
                                                    <td>
                                                        <!--  -->
                                                        <input class='checkbox' name='detailId' type='checkbox' value='${empWeekWork.worktimeDetailId}'>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                        </tbody>
                    </table>
                    <c:if test="${requestScope.page.nowPage>0}">
						<input id='pageAction' type="hidden" name='pageAction' value='${requestScope.page.action}page=${requestScope.page.nowPage}'>
					</c:if>
                     <%@ include file="/WEB-INF/views/changepage/page.jsp" %>
                            </script>
                            </c:when>
                            <c:otherwise>

                                </tbody>
                                </table>
                                <c:if test="${empty empWeekWorktime}">
                                    沒有您要的查詢結果
                                </c:if>
                            </c:otherwise>
                            </c:choose>


                            <div id='m_wt_btn_div'>
                                <input type="submit" form='m_form' value='通過'>
                            </div>
                </form>
                
                
                <form id='notPassReason' action="./WorktimeDetail.do?action=checkDetail_page&k=simple" method="post">
	                <input type='hidden' name='reason'>
	                <input type='hidden' name='status' value='not_ok'>
	                <input type='hidden' name='id' value=''>
	                <input type='hidden' name='firstday' value=''>
	                <c:if test="${requestScope.page.nowPage>0}">
						<input type="hidden" name='pageAction' value='${requestScope.page.action}page=${requestScope.page.nowPage}'>
					</c:if>
                </form>
                <form target="wt_iframe" id='showDetail' action="./WorktimeDetail.do?action=checkWorktimeDetail_page" method="post">
	                <input type='hidden' name='id'>
	                <input type='hidden' name='date'>
	                <c:if test="${requestScope.page.nowPage>0}">
						<input type="hidden" name='pageAction' value='${requestScope.page.action}page=${requestScope.page.nowPage}'>
					</c:if>
                </form>


            </div>



            <!--    一次通過-->

        </body>

        </html>