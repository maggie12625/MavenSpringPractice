$(document).ready(function(){
      	//自己這個站的 js
      	$(".form").validate({
      		submitHandler: function(form) {
      			form.submit();
      		}/*驗證成功所做的事情*/,
      		errorPlacement: function(error, element) {
      			element.next('.error-msg').append(error);
      		}/*錯誤訊息的處理方式*/,
            rules: {
                old_pw: {
                    required: true,
                    
                },
                user_pwd_again: {
                    equalTo: '#user-pwd-input'
                }
            }/*驗證規則*/,
      	});
      
      });