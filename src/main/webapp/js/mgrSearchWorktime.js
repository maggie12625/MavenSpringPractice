$(function(){
    var year=$('#m_s_wt_month').val().split('-')[0];
    //搜尋
    
    $('#wt_div tbody tr>td:first-child').find('span:first-child').each(function(){
        var date=$(this).text();
        
        
        var year=parseInt(date.split('-')[0]),
            month=parseInt(date.split('-')[1]),
            day=parseInt(date.split('-')[2]);
        
        endDate=new Date(year,month-1,day);
        endDate.setTime(endDate.getTime()+(6*24*60*60*1000));
        
        e_mon=endDate.getMonth()+1;
        if(e_mon==13){
            e_mon='01';
        }
        e_day=endDate.getDate();
        
        end_str=e_mon+'/'+e_day;
        $(this).text(month+'/'+day);
        $(this).siblings('span').text(end_str);
    });
    
    
    //打開子畫面(詳細工時)
    $('#wt_div table input').on('click',function(){
       var $this_tr=$(this).parent().parent();
        var first_day=$this_tr.find('span').eq(0),
            id=$this_tr.find('td').eq('1').html().trim();
        var a=first_day.text().split('/');
        var month=parseInt(a[0]),
            day=parseInt(a[1]);
        
        //選擇的該筆顏色
        $this_tr.addClass('select').siblings().removeClass('select');
        
        
        
        if(month<10)
        	month='0'+month;
        if(day<10)
        	day='0'+day;
        var date=year+'-'+month+'-'+day;
        //src要改
        var src='./WorktimeDetail.do?action=searchDetailWorktime&date='+date+'&id='+id;
       
        $('#cover_div iframe').attr('src',src);
        $('#cover_div').stop().fadeIn(300);
    });
    $('#cover_div').on('click',function(){
        $(this).stop().fadeOut(300).hide(0);
    })
});