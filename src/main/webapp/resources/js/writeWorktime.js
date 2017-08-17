$(function() {
	var $detail_tr = "<tr detail><td colspan='17'><iframe></iframe></td></tr>";
	var $cover="<div class='holdon-overlay' style='height: 300px;position: relative;'><div class='holdon-content-container'>"
		+"<div class='holdon-content'><div class='sk-rect'><div class='rect1'></div><div class='rect2'></div><div class='rect3'></div><div class='rect4'></div><div class='rect5'></div>" 
            +"</div></div><div class='holdon-message'>載入中，請稍候...</div></div></div>";

	// 算首日
	calFirstDate();

	$('tbody tr td.details-control').on(
			'click',
			function() {
				var tr = $(this).closest('tr');
				if (hasDetail(tr)) {
					var detail_tr = tr.next('[detail]');
					detail_tr.toggle();
					$(this).toggleClass('write');

				} else {
					tr.after($detail_tr);
					
					$(this).toggleClass('write');
					aa(tr);
					var detail_tr = tr.next('[detail]');
					var $iframe = detail_tr.find('iframe');
					var firstDay = tr.find('td').eq(1).find('input').val();
					//+載入
					$iframe.before($cover);
					$iframe.attr('src',
							'./Worktime.do?action=writeWorktime_sub_page&firstday='
									+ firstDay);
					$iframe.load(function(){
						detail_tr.find('.holdon-overlay').fadeOut(300, function(){
		                   // $(this).remove();
		                });
					});
				}
			})

	var h1_god = '<h1 style="width:100%;color:red">您暫時不需要填寫工時!</h1>';
	if ($('tbody tr').length == 0) {
		$('thead').remove();
		$('tbody').append(h1_god);
	}

});

// 是否有詳細區塊
function hasDetail(tr) {
	var next = tr.next('[detail]').length;
	return next == 0 ? false : true;
}

// 工時相加
function aa(tr) {
	var $iframe = tr.next('[detail]').find('iframe');

	if ($iframe.length != 0) {

		$iframe.load(function() {
			var $contents = $iframe.contents();
			var $all_select = $contents.find('select[name]');

			showSumTime($contents, tr);
			delete_calDetail($contents, tr);
			$contents.find('#add_btn').on('click', function() {
				delete_calDetail($contents, tr);
				showSumTime($contents, tr);
			});
		});

	} else {
		alert('找不到iframe');
	}
}

function sumTime($contents, name, tr) {
	var $select = $contents.find('[name="' + name + '"]');
	var total = 0;
	$select.each(function() {
		total += parseInt($(this).val());
	});
	if (name.substr(3, name.length) == 'Over') {
		// 加班
		if (total > 4) {
			tr.find('td[name="' + name + '"]').css('color', 'red').html(total);
		} else {
			tr.find('td[name="' + name + '"]').css('color', 'black')
					.html(total);
		}
	} else {
		// 平常
		var largestHour = tr.find('td[name="' + name + '"]').attr('mosthours');
		if (largestHour == "" || largestHour == null)
			largestHour = 8;
		if (total > largestHour) {
			tr.find('td[name="' + name + '"]').css('color', 'red').html(total);
		} else {
			tr.find('td[name="' + name + '"]').css('color', 'black')
					.html(total);
		}
	}
	return total;
}

function showSumTime($contents, tr) {
	var $all_select = $contents.find('select[name]');
	$all_select.unbind();

	$all_select.change(function() {
		var name = $(this).attr('name');
		var hour = sumTime($contents, name, tr);

		console.log(name + ':' + hour);
	});
}

// 刪除 重新計算
function delete_calDetail($contents, tr) {
	$contents.find('.del_btn').on('click', function() {
		var $tr =$(this).closest('tr');
		if(!tr.find('td[name="sunNormal"]').hasClass('holiday')){
			sumTime($contents, 'sunNormal', tr);
			sumTime($contents, 'sunOver', tr);
		}
		if(!tr.find('td[name="monNormal"]').hasClass('holiday')){
			sumTime($contents, 'monNormal', tr);
			sumTime($contents, 'monOver', tr);
		}
		if(!tr.find('td[name="tueNormal"]').hasClass('holiday')){
			sumTime($contents, 'tueNormal', tr);
			sumTime($contents, 'tueOver', tr);
		}
		if(!tr.find('td[name="wedNormal"]').hasClass('holiday')){
			sumTime($contents, 'wedNormal', tr);
			sumTime($contents, 'wedOver', tr);
		}
		if(!tr.find('td[name="thuNormal"]').hasClass('holiday')){
			sumTime($contents, 'thuNormal', tr);
			sumTime($contents, 'thuOver', tr);
		}
		if(!tr.find('td[name="friNormal"]').hasClass('holiday')){
			sumTime($contents, 'friNormal', tr);
			sumTime($contents, 'friOver', tr);
		}
		if(!tr.find('td[name="satNormal"]').hasClass('holiday')){
			sumTime($contents, 'satNormal', tr);
			sumTime($contents, 'satOver', tr);
		}
	});
}

// /計算首日~end
function calFirstDate() {
	var $all_td = $('tbody tr td:nth-child(2)');
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