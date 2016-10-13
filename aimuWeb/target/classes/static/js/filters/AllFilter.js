define(["angular"], function(angular) {
	return {
		sex: function(){
			return function(str){
				if(str === "MALE"){
					return "男"
				} else {
					return "女"
				}
			}
		},
		age: function(){
			return function(str){
				// 出生年份
				var birthYear = new Date(str).getFullYear();
				// 今年年份
				var thisYear = new Date().getFullYear()
				// 时间年差
				return thisYear - birthYear;
			}
		},
		beijingDate: function() {
			return function(val){
				var beijing = val + (3600000 * 8);
				return new Date(beijing);
			}
		},
		unsafe: ['$sce', function ($sce) {
		    return function (val) {
		        return $sce.trustAsHtml(val);
		    };
		}]
	}
})