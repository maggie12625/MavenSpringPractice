$(function() {
	var tr_s = $('.del_btn').closest('tr').siblings().length;
	$('.del_btn').on('click', function() {
		if (tr_s > 1) {
			$(this).closest('tr').remove();
			tr_s--;
		}
	});

	// 增加新專案
	$('#add_btn').on('click', function() {
		var last_tr = $('#pj_table tr[project]').last();
		var $add = last_tr.clone();
		$add.find('input[name="projectIds"]').val('');
		$add.find('textarea').val('');
		$add.find('select').val('0');
		last_tr.after($add);
		var tr_s = $('.del_btn').closest('tr').siblings().length;

		// 刪除
		$('.del_btn').on('click', function() {
			if (tr_s > 1) {
				$(this).closest('tr').remove();
				tr_s--;
			}
		});
	});

	// 提繳卻認
	$('#submit').on('click', function() {
		if (confirm('確認提交嗎?')) {
			if (!valid()) {
				swal('尚未填完','','error');
				$('body>.holdon-overlay').stop().fadeOut(150);
				return false;
			}else{
				$('body>.holdon-overlay').delay( 150 ).fadeIn(50);
			}
			$(this).submit();
		} else {
			return false;
		}
	});
	$('#save_btn').on('click', function() {
		if (!valid()) {
			swal('尚未填完','','error');
			$('body>.holdon-overlay').stop().fadeOut(150);
			return false;
		}else{
			$('body>.holdon-overlay').delay( 150 ).fadeIn(50);
		}

	});

	$('#delete_btn').on('click', function() {
		var all_del_btn = $('.del_btn');
		all_del_btn.toggle();
		$(this).toggleClass('delete');
	});

});

// 驗證是否有填寫
function valid() {
	var $all_project = $('#worktomeSubForm input[name="projectIds"]');
	var $all_conteat = $('#worktomeSubForm textarea[name="workContents"]');
	var isOK=true;
	
	$all_project.each(function() {
		if ($(this).val() == "" || $(this).val() == null) {
			isOK=false;
			return false; 
		}

	});
	$all_conteat.each(function() {
		if ($(this).val() == "" || $(this).val() == null) {
			isOK=false;
			return false; 
			
		}

	});

	return isOK;

}

// function jsonForm(tr){
// $('#forJson').html(tr.html());
// return $('#forJson');
// }
//
// function formToJson(formData){
// var o = {};
// var a = formData.serializeArray();
// $.each(a, function() {
// if (o[this.name] !== undefined) {
// if (!o[this.name].push) {
// o[this.name] = [o[this.name]];
// }
// o[this.name].push(this.value || '');
// } else {
// o[this.name] = this.value || '';
// }
// });
// return o;
// }
