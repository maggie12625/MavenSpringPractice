<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>工時審核_詳細</title>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet"
	href="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.css">
    <link rel="stylesheet" type="text/css" href="css/checkWorktimeDetail.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
    <script
	src="https://cdn.jsdelivr.net/sweetalert2/6.6.2/sweetalert2.min.js"></script>
	<c:if test='${requestScope.result=="success" }'>
		<script>
			$(function(){
	    		swal({
	    				title: '審核成功!',
	    				text: '',
	    				type: 'success'
	    			}).then(function(){
	    				window.parent.location.href = '${requestScope.pageAction }';
	    			});
			});
		</script>
	 </c:if>
    <script>
        $(function(){
        	
            $('#m_wt_iframe').load(function(){
                 $(window.frames['m_wt_iframe'].document).find('#close').remove();  
            });
           $('input[name="status"]').change(function(){
               $('#submit').prop('disabled',false);
              if('ok'!=$(this).val()){
                  $('#m_wt_div textarea').show().prop('required',true);
              }else{
                  $('#m_wt_div textarea').hide().prop('required',false);
              }
           });
            $('#close').on('click',function(){
               $(window.parent.document).find("#cover_div").stop().fadeOut(300).hide(0);
            });
            $('#submit').on('click',function(){  
                if(confirm('確定送出?')){
                    if(($('input[name="status"]:checked').val()=='not_ok')&&($('#m_wt_div textarea').val().length=='0')){
                        alert('沒填原因');
                        return false;
                    }
                }else{
                    return false;
                }       
            });
            
        });
    </script>
    
</head>
<body>
<i id='close' class="fa fa-times-circle-o" aria-hidden="true"></i>
<!--   審核區塊-->
    <div id='m_wt_div'>
        <form action="./WorktimeDetail.do?action=checkDetail_page&id=${requestScope.id }&firstday=${requestScope.firstDateOfWeek }" method="post">
            <input id='ok_radio' type="radio" name='status' value='ok' required><label for='ok_radio'>通過</label>
            <input id='not_ok_radio' type="radio" name='status' value='not_ok' required><label for='not_ok_radio'>不通過</label>
            <textarea name='reason' placeholder='請輸入未通過原因'  style='display:none'></textarea>
            <input type="hidden" name='pageAction' value='${requestScope.pageAction }'>
            <input id='submit'type='submit' value='確定' disabled='disabled'>
        </form>
    </div>
<!--    詳細工時區-->
    <iframe id='m_wt_iframe' name='m_wt_iframe' src='./WorktimeDetail.do?action=searchDetailWorktime&id=${requestScope.id }&date=${requestScope.firstDateOfWeek }'></iframe>
    
</body>
</html>