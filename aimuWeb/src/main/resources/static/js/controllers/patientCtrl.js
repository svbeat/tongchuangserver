define(["angular"], function(angular) {
	return function( $scope, $filter, $modal, API){
		var patientid = API.getCookies('k_subjectid');

		$scope.patientId = patientid;
		$scope.activeTab = !!API.getCookies("k_activeTab")?API.getCookies("k_activeTab"):'tab3';


		function updateMyDoctors(){
			// 测试demo
			// userid = "9b7d8e61-ba5c-4e4a-903e-85cb75e49813";
			API.getDoctorsOfPatient(patientid).then(function(res){
				console.log(res)

				$scope.pdoctors = res.items;
			})
		}
		
		// 病人测试 分页配置
		$scope.pTestConf = {
				currentPage: 1,
				itemsPerPage: 10
		};

		$scope.$watch('pTestConf.currentPage + pTestConf.itemsPerPage', function(){
			// console.log($scope.patientConf.currentPage, $scope.patientConf.itemsPerPage)
			var currentPage = $scope.pTestConf.currentPage;
			var itemsPerPage = $scope.pTestConf.itemsPerPage;
			var currentIndex = (currentPage-1)*itemsPerPage;

			updatePatientTests($scope.patientId);
		});

		function updatePatientTests(patientId){
			API.getAllTestsOfPatint(patientId, {
				returnTotal: true,
				pageno:  $scope.pTestConf.currentPage-1,
				pagesize:  $scope.pTestConf.itemsPerPage
			}).then(function(res){
				// console.log(res)
				if(!!res){
					$scope.pTestConf.data = res.items;
					$scope.pTestConf.totalItems = res.totalCounts;
					$scope.patientTests = res.items;
				}
			})
		}


		function getAllTest(){
			API.getAllTestsOfPatint($scope.patientId,{
				returnTotal: true,
				pageno:  $scope.pTestConf.currentPage-1,
				pagesize:  $scope.pTestConf.itemsPerPage
			}).then(function(res){
				$scope.pTestConf.data = res.items;
				$scope.pTestConf.totalItems = res.totalCounts;
				$scope.patientTests = res.items;
			});
		}
		
		function getCurrentPatient(){
			API.getPatient(patientid).then(function(res){
				$scope.patient = res;
			})
		}


		// updateDoctors();
		updateMyDoctors()
		getAllTest()
		getCurrentPatient()

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
		
		$scope.openTestReport = function(patient, test) {
			console.log('opening pop up:'+patient);
			var modalInstance = $modal.open({
				templateUrl: 'views/detailreport.html',
				controller: 'testReportCtrl',
				resolve: {
					test: function () {
						return test;
					},
					patient: function() {
						return patient;
					}
				}
			});
		}
		
		
	}
});