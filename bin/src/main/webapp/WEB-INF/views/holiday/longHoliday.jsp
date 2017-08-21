<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>批次假日</title>
    <link rel="stylesheet" type="text/css" href="css/longHoliday.css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
	<script>
        $(function(){
            
            var date=new Date();
            var year=date.getFullYear(),
                month=parseInt(date.getMonth()+1),
                day=date.getDate();
            if(month<10){
                month='0'+month;
            }
            if(day<10){
                day='0'+day;
            }
           $('#start').attr('min',year+'-'+month+'-'+day);
            $('#start').change(function(){
                var start_day=$('#start').val();
                var a=start_day.split('-');
                var year=a[0],month=parseInt(a[1]),day=parseInt(a[2])+1;
                if(month<10){
                month='0'+month;
                }
                if(day<10){
                    day='0'+day;
                }
                $('#end').attr('min',year+'-'+month+'-'+day);
            });
            
            $('#close').on('click',function(){
                $(window.parent.document).find('#cover_div').fadeOut(300).hide(0);
            });
            
            //修改假日鎖小時
            $('table input[name="status"]').change(function(){
            	var $select=$('table select[name]');
        		if($(this).val()!='HOLIDAY'){
        			//平日
        			$select.val(0).attr('disabled',true);
        		}else{
        			//假日
        			$select.val(8).attr('disabled',false);
        			$select.find('option[value="0"]').attr('disabled',true);
        		}
        	});
        });
    </script>
</head>
<body>
<!-- h*w  260*650-->
   <i id='close' class="fa fa-times-circle-o" aria-hidden="true"></i>
    <form target="_parent" action='./modify_longHoliday' method="post">
        <table>
            <tr class="tr_color">
                <td>
                    起始 <input id='start' name='start' type="date" required>
                </td>
                <td>
                    <select name="start_time">
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
                <td>
                    結束 <input id='end' name='end' type="date" required>
                </td>
                <td>
                    <select name='end_time'>
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
            <tr class="tr_color">
                <td>
                    <input id='rd1' type='radio' name='status' value='ORDINARYDAY' checked><label for='rd1'> 平日</label>
                </td>
                <td rowspan="2"><textarea placeholder="請輸入原因" name='reason'></textarea></td>
            </tr>
            <tr class='tr_color'>
                <td>
                    <input id='rd2' type='radio' name='status' value='HOLIDAY' checked><label for='rd2'> 假日</label>
                </td>
            </tr>
        </table>
        <span class='red'>※注意:起始~結束之間為整天放假或上班。</span><br>
        <input id='submit' type="submit" value="修改">
    </form>
</body>
</html>