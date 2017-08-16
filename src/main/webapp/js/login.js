$(function(){

    
    var validator = $("#loginForm").validate({
      errorClass:"error",
        rules: {
          id: "required",
          password: {
            required: true,
          }
        },
      messages: {
          id: "請輸入帳號!",
          password: {
            required: "請輸入密碼!"
          }
        },
      highlight: function(element, errorClass) {
        $(element).fadeOut(function() {$(element).fadeIn(); });
          
        $(element).addClass('error_bg').removeClass('success_bg').closest('div').addClass('has-error');
        $('.holdon-overlay').fadeOut(300);
      },
      unhighlight:function(element, errorClass, validClass){

           $(element).addClass('success_bg').removeClass('error_bg').removeClass('error_bg').closest('div').addClass("has-success").removeClass('has-error');

        }
    });
});