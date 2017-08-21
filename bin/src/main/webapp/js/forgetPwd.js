$(function(){

     
    var validator = $("#forget_Pwd_form").validate({
      errorClass:"error",
        rules: {
          empno: "required",
          id:	"required"
        },
      messages: {
          empno: "請輸入員編",
          id:	 "請輸入身分證"
        },
      highlight: function(element, errorClass) {
        $(element).fadeOut(function() {$(element).fadeIn(); });
          
          $(element).addClass('error_bg').removeClass('success_bg').closest('div').addClass('has-error');
        },
      unhighlight:function(element, errorClass, validClass){

           $(element).addClass('success_bg').removeClass('error_bg').removeClass('error_bg').closest('div').addClass("has-success").removeClass('has-error');

        }
    });   

});