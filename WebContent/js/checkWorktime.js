$(function(){
    //make_checkbox_value();
    sel_all_checkbox();
    check_ckeckbox();
    
    // 算首日
	calFirstDate();
    
    $('#content_div table .checkbox').change(function(){
        check_ckeckbox();
    });
    
    //打開詳細
    $('.detial_btn').on('click',function(){
    	var $this_tr=$(this).closest('tr');
        $this_tr.addClass('sel').siblings().removeClass('sel');
        
        var id=$this_tr.find('td:nth-child(2)').html();
        var firstday=$this_tr.find('td:first-child').find('input').val();
        
        var $form=$('#showDetail');
        $('#showDetail').find('input[name="id"]').val(id);
        $('#showDetail').find('input[name="date"]').val(firstday);
        $form.submit();
        $('#cover_div').stop().fadeIn(300);
    });
    
//    $('#m_wt_btn_div input').on('click',function(){
//        if(confirm('確定通過?')){
//            var all_checked=$('#content_div tbody .checkbox:checked').parent().parent().remove();
//            $(this).submit();
//        }
//    })
    
    //關閉cover_div
//    $('#cover_div').on('click',function(){
//        $(this).stop().fadeOut(300).hide(0);
//    });
    
    
});


//全選/取消全選
function sel_all_checkbox(){
   $('#content_div thead .checkbox').on('click',function(){
      var $all_checkbox=$('#content_div tbody td .checkbox');
      if($(this).prop('checked')){
          $all_checkbox.prop('checked',true);
      }else{
          $all_checkbox.prop('checked',false);
          
      }
   });
}

//確定有鉤子
function check_ckeckbox(){    
     $('#m_wt_btn_div input').prop('disabled',true);
        if($('#content_div tbody .checkbox:checked').length>0){
         $('#m_wt_btn_div input').prop('disabled',false);
    }
}

//製造checkbox的值
function make_checkbox_value(){
    $('#content_div tbody .checkbox').each(function(){
       var id=$(this).parent().parent().find('td').eq(1).html().trim();
        $(this).val(id);
    });
}

//計算首日
function calFirstDate() {
	var $all_td = $('tbody tr td:nth-child(1)');
	$all_td
			.each(function() {
				var firstDay = new Date($(this).find('span').text()), endDay = new Date(
						firstDay.getTime() + 6 * 24 * 60 * 60 * 1000);
				var dateString = (firstDay.getMonth() + 1) + '/'
						+ firstDay.getDate() + "~" + (endDay.getMonth() + 1)
						+ '/' + endDay.getDate();
				$(this).find('span').text(dateString);
			});
}



