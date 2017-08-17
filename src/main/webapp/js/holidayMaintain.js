$(function() {
	$date = $('#hd_input_month');
	var nowDate = new Date(), month = nowDate.getMonth() + 1;
	if (month < 10)
		month = '0' + month;
	$date.val(nowDate.getFullYear() + '-' + month);
	hd_make($date);
	clearHolidayData();
	getHoliday();
	// 功能選擇
	$('#hd_ul li').on(
			'click',
			function() {
				var id = $(this).attr('id');
				if (!$(this).hasClass('hd_li_sel')) {
					$(this).addClass('hd_li_sel').parent().siblings()
							.find('li').removeClass('hd_li_sel');
				}
				if ('hd_tab2' == id ) {
					$('#cover_div').show().fadeIn(500);
				} else {
					$('#cover_div').stop().fadeOut(300).hide(0);
				}

			});
	//年假修改
	modifyYearHoliday();

	// 修改提交
	$('#hd_submit').on('click', function() {
		$('#hd_modify_cover_div').hide();
		$('#cover_div').stop().show();
	});

	/* 日期input */
	$('#hd_input_month').change(function() {
		hd_make($(this));
		clearHolidayData();
		getHoliday();
	});
	$('#hd_n_month').on('click', function() {
		var $d = $('#hd_input_month');
		var date = $d.val();
		var d = date.split('-');
		var year = d[0], month = d[1];
		month++;
		if (month == 13) {
			year++;
			month = 1;
		}
		if (month < 10) {
			month = "0" + month;
		}
		$d.val(year + '-' + month);
		hd_make($d);
		clearHolidayData();

		getHoliday();
	});
	$('#hd_p_month').on('click', function() {
		var $d = $('#hd_input_month');
		var date = $d.val();
		var d = date.split('-');
		var year = d[0], month = d[1];
		month--;
		if (month == 0) {
			year--;
			month = 12;
		}
		if (month < 10) {
			month = "0" + month;
		}
		$d.val(year + '-' + month);
		hd_make($d);
		clearHolidayData();
		getHoliday();
	});

	$('#tab2')
			.on(
					'click',
					function() {
						if (!$(this).hasClass('sel_tab')) {
							$(this).addClass('sel_tab').siblings().removeClass(
									'sel_tab');

							
						}
						;
						// 顯示年度假日填寫
						$('#hd_year_info').stop().fadeIn(300).show(0);
						$('#hd_content').css({
							backgroundColor : '#777777'
						});

						// 關閉年度假日填寫

					});

	//平日修改 鎖住 小時
	$('#hd_modify_div input[name="status"]').change(function(){
		var $select=$('#hd_modify_div select[name="time"]');
		if($(this).val()!='HOLIDAY'){
			//平日
			$select.val(0).attr('disabled',true);
		}else{
			//假日
			$select.val(8).attr('disabled',false);
			$select.find('option[value="0"]').attr('disabled',true);
		}
	});
	
	// 關閉cover_div
	$('#cover_div').on('click', function() {
		$(this).stop().fadeOut(300).hide(0);
	});

	$('#hd_year,#hd_modify_div').on('click', function(e) {
		e.stopPropagation();
	});

});

// 輸出月曆日期
function hd_make($date) {
	var d = new Date(), p_d = new Date(), n_d = new Date();
	d.setTime(Date.parse($date.val()));
	p_d.setTime(Date.parse($date.val()));
	n_d.setTime(Date.parse($date.val()));
	n_d.setMonth((d.getMonth() + 1) % 12);

	var year = d.getFullYear(), month = d.getMonth() + 1, start_day = d
			.getDay();

	n_d.setDate(0);
	end_date = n_d.getDate();

	var p_date, now_date = 1, n_date = 1;

	if (start_day != 0) {
		p_d.setDate(-(d.getDay() - 1));
		p_date = p_d.getDate();
	}

	var tr = $('#hd_date table tr');
	while ((end_date + start_day) > ((tr.length - 1) * 7)) {

		var $last_tr = $('#hd_date table tr:last');
		var clone = $last_tr.clone();
		$last_tr.after(clone);

		tr = $('#hd_date table tr');
	}
	while ((end_date + start_day) <= ((tr.length - 2) * 7)) {
		var $last_tr = $('#hd_date table tr:last');
		$last_tr.remove();
		tr = $('#hd_date table tr');
	}

	var m_day = $('.hd_day_td');

	$('.hd_day_td').each(
			function(index) {
				var $span = $(this).find('span');
				if (index < start_day) {
					var pre_month = month - 1, pre_year = year, day = p_date;
					if (pre_month < 10) {
						if (pre_month == 0) {
							pre_month = 12;
							pre_year--;
						} else {
							pre_month = '0' + pre_month;
						}
					}
					if (day < 10)
						day = '0' + day;
					$(this).find('input').attr('date',
							pre_year + '-' + pre_month + '-' + day);
					$span.text(p_date++).css({
						color : 'rgba(100, 100, 100, 0.7)'
					});
				} else if (index < end_date + start_day) {
					var now_month = month, day = now_date;
					if (now_month < 10)
						now_month = '0' + now_month;
					if (day < 10)
						day = '0' + day;
					$(this).find('input').attr('date',
							year + '-' + now_month + '-' + day);
					$span.text(now_date++).css({
						color : 'rgba(60, 60, 60, 0.89)'
					});
				} else {
					var next_month = month + 1, next_year = year, day = n_date;
					if (next_month < 10) {
						next_month = '0' + next_month;
					}
					if (next_month == 13) {
						next_year++;
						next_month = '01';
					}
					if (day < 10)
						day = '0' + day;
					$(this).find('input').attr('date',
							next_year + '-' + next_month + '-' + day);
					$span.text(n_date++).css({
						color : 'rgba(100, 100, 100, 0.7)'
					});
				}

			});
	// 重綁按鈕動畫
	btn_hover($('.hd_day_td'));
	// 重綁按鈕功能
	bind_modify_holiday_btn();
}

// 綁定修改按鈕，show出修改畫面
function bind_modify_holiday_btn() {
	// 假日填寫
	$('.hd_modify').on(
			'click',
			function() {
				var $td= $(this).parent('td');
		        var hours=$td.attr('time'),
		            reason=$td.find('p').text(),
		            status='';
		        
				var date = $(this).attr('date');
				$('#hd_reason').text(reason);
		        if(hours==null||hours=='')
		            hours=0;
		        if(hours>0){
		            status='holiday';
		        }
		        
		        
		        if(status=='holiday'){
		            $('#hd_rd_holiday').prop('checked',true);
		            $('#hd_rd_normal').prop('checked',false);
		        }else{
		        	$('#hd_rd_normal').prop('checked',true);
		            $('#hd_rd_holiday').prop('checked',false);
		        }
		        
		        $('#hd_modify_div select[name="time"]').val(hours);
		        
		        if($('#hd_modify_div input[name="status"]:checked').val()!='HOLIDAY'){
					$('#hd_modify_div select[name="time"]').val(0).attr('disabled',true);
				}else{
					$('#hd_modify_div select[name="time"]').attr('disabled',false);
				}
				
				$('#hd_modify_cover_div').stop().fadeIn(500).show().find(
						'#hd_modify_date_span').html(date);
				// 給按鈕日期
				$('#modify_date').val(date);

				$('#hd_modify_cover_div,#hd_cancel').on('click', function() {
					$('#hd_modify_cover_div').stop().fadeOut(500).hide(1);
				});

			});
}

// 修改按鈕動畫
function btn_hover($td) {
	$td.hover(

	function() {
		$(this).find('.hd_modify').show().stop().animate({
			right : 0.1 + 'rem'
		}, 100);
	}, function() {

		$(this).find('.hd_modify').stop().animate({
			right : -2.1 + 'rem'
		}, 300, function() {
			$(this).hide();
		});
	});
}

// 撈假日資料
function getHoliday() {
	var $all_date = $('.hd_day_td');
	$.ajax({
		url : "./Holiday.do?action=getHolidays",
		type : "post",
		data : {
			month : $('#hd_input_month').val()
		},
		dataType : "json",
		success : function(JsonData) {
			for (var i = 0; i < JsonData.length; i++) {
				$td = $all_date.find(
						'input[date="' + JsonData[i].changeDate + '"]')
						.parent();
				if ('HOLIDAY' == JsonData[i].status) {
					$td.attr('time',JsonData[i].hours);
					if(JsonData[i].hours==8){
						$td.addClass('holiday');
					}else{
						$td.addClass('partOfHoliday');
					}

				} else {// 平日
					if ($td.hasClass('holiday,partOfHoliday')) {
						$td.removeClass('holiday,partOfHoliday');
					}
				}
				$td.find('p').html(JsonData[i].reason);
			}
			//console.log('json: \n' + JSON.stringify(JsonData));
		},
		error : function() {
			swal('伺服器連線錯誤!', '請稍後再試。', 'error');
		}
	});
}

//修改年假
function modifyYearHoliday(){
	$('#hd_tab3').on('click',function(){
		swal({
			title : '年假新增',
			text : '請輸入年份',
			input : 'text',
			inputAttributes : {
				'id' : 'y_year',
				'maxlength' : 4,
			},
			showCancelButton : true,
			confirmButtonText : '送出',
			showLoaderOnConfirm : true,
			inputValidator :
				function(value) {
					return new Promise(function(resolve, reject) {
						if (value.match("\\d{4}")&&value<=2100&&value>=2010) {
							document.location.href='./Holiday.do?action=updateYearHoliday&year='+value;
						} else {
							reject('請輸入正確年份')
						}
					});
				}
			});
	});
}

// 清除假日資料
function clearHolidayData() {
	var $td=$('.hd_day_td');
	$td.attr('time','').find('p').html('');
	if($td.hasClass('holiday')){
		$td.removeClass('holiday');
	}
	if($td.hasClass('partOfHoliday')){
		$td.removeClass('partOfHoliday');
	}
}
