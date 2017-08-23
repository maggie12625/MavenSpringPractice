jQuery.extend(jQuery.validator.messages, {
	required: "此欄位必填.",
	remote: "Please fix this field.",
	email: "請輸入正確的 Email 信箱.",
	url: "請輸入正確的網址.",
	date: "請輸入正確的日期.",
	dateISO: "請輸入正確的 (ISO) 日期格式.",
	number: "本欄位請填入數字.",
	digits: "本欄位請填入數字.",
	creditcard: "請輸入正確的信用卡號.",
	equalTo: "請再次輸入相同的值.",
	maxlength: $.validator.format("至多輸入 {0} 個字."),
	minlength: $.validator.format("至少輸入 {0} 個字."),
	rangelength: $.validator.format("請輸入 {0} 到 {1} 個字."),
	range: $.validator.format("請輸入 {0} 到 {1} 的數字."),
	max: $.validator.format("請輸入小於等於 {0} 的值."),
	min: $.validator.format("請輸入大於等於 {0} 的值.")
});
//覆蓋原本英文的錯誤訊息
jQuery.validator.addMethod("new-pw", function( value, element ) {
    return (
    /^[a-zA-Z0-9]{2,16}$/.test(value) /*檢查是否只有英文與數字*/
    && /[a-zA-Z]{1,16}/.test(value)     /*檢查是否包含英文*/
    && /[0-9]{1,16}/.test(value));       /*檢查是否包含數字*/

}, "密碼不得為空且小於16個字元的英文字母、數字混合，但不含空白鍵及標點符號。"/*回傳錯誤訊息   ,append(error)*/);