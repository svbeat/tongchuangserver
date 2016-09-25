define(["angular"], function(angular) {
	return function( $scope, $filter, API){
		var patientid = API.getCookies('k_subjectid');
		$scope.activeTab = !!API.getCookies("k_activeTab")?API.getCookies("k_activeTab"):'tab4';

		// function updateDoctors(){
		// 	// API.getAllDoctors().then(function(res){
		// 	// 	// console.log(res)
		// 	// 	if(!!res){
		// 	// 		$scope.alldoctors = res.items
		// 	// 	}
		// 	// })
		// 	API.getAllDoctors({
		// 		returnTotal: true,
  //       		pageno:  $scope.doctorConf.currentPage-1,
  //       		pagesize:  $scope.doctorConf.itemsPerPage
		// 	}).then(function(res){
		// 		// console.log(res)
		// 		if(!!res){
		// 			$scope.doctorConf.data = res.items;
		// 			$scope.doctorConf.totalItems = res.totalCounts;
		// 			$scope.alldoctors = res.items;
		// 		}
		// 	})
		// }

		function updateMyDoctors(){
			// 测试demo
			// userid = "9b7d8e61-ba5c-4e4a-903e-85cb75e49813";
			API.getDoctorsOfPatient(patientid).then(function(res){
				console.log(res)

				$scope.pdoctors = res.items;
			})
		}

		function getAllTest(){
			API.getAllTestsOfPatint(patientid).then(function(res){
				$scope.patientTests = res.items;
			})
		}


		// updateDoctors();
		updateMyDoctors()
		getAllTest()


		$scope.getDoctorForPatient = function(){
			$('#searchDoctor').modal('toggle')
			$scope.sdoctors =[];
			$scope.doctorPatient={};
		}

		$scope.searchDoctor = function(e){
			e.stopPropagation();
			API.getAllDoctors({
				filter: !!$scope.querydoctor?$scope.querydoctor: "",
				pageSize:1000
			}).then(function(res){
				$scope.sdoctors = res.items;
			})
			// $scope.sdoctors = $filter('filter')($scope.alldoctors,{name:$scope.querydoctor});

		}

		$scope.saveRelationship = function(docid, patid, switchtype) {
			if(docid) {
				API.addRelationShips({
					objectId: patientid,
					subjectId: docid,
					"relationshipType": "DOCTOR_PATIENT"
				}).then(function(res){


					API.getDoctorsOfPatient(patientid).then(function(res){
						$scope.pdoctors = res.items;
					});
						
	

				});
			}
		}
		

		
		
	}
});