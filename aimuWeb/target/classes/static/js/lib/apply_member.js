// JavaScript Document
$(document).ready(function(){
	//选择日期
	$("#myform_datetime").datetimepicker({
    	format: 'yyyy-mm-dd',
        language:  'zh-CN', 
		minView: 2,		//时间选择器最小可以选择的范围：0分钟、1小时、2天
		weekStart: 1,
		todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
		showMeridian: 1
    });
});
