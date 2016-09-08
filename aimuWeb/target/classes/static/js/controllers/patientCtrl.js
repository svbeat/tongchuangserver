define(["angular"], function(angular) {
	return function($scope, API){
		var userid = API.getCookies('k_userid');
		var patientid = API.getCookies('k_subjectid')
		$scope.activeTab = !!API.getCookies("k_activeTab")?API.getCookies("k_activeTab"):'tab1';

		// 测试demo
		// userid = "9b7d8e61-ba5c-4e4a-903e-85cb75e49813";
		API.getDoctorsOfPatient(patientid).then(function(res){
			console.log(res)

			$scope.pdoctors = res.items;
		})

		
		API.getAllTestsOfPatint(patientid).then(function(res){
			$scope.patientTests = res.items;
		})
	}
});