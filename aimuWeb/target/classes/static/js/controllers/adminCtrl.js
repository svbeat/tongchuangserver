define(["angular"], function(angular) {
	return function($scope,$filter, API){
		var userid = API.getCookies('k_userid');
		$scope.activeTab = !!API.getCookies("k_activeTab")?API.getCookies("k_activeTab"):'tab1';
		$scope.innerTab = $scope.activeTab;



		// 病人 分页配置
        $scope.patientConf = {
            currentPage: 0,
            itemsPerPage: 5
        };

        $scope.$watch('patientConf.currentPage + patientConf.itemsPerPage', function(){
        	// console.log($scope.patientConf.currentPage, $scope.patientConf.itemsPerPage)
        	var currentPage = $scope.patientConf.currentPage;
        	var itemsPerPage = $scope.patientConf.itemsPerPage;

        	$scope.allpatients = $scope.patientConf.data&&$scope.patientConf.data.slice(currentPage-1,itemsPerPage)
        });

        // 医生 分页配置
        $scope.doctorConf = {
            currentPage: 0,
            itemsPerPage: 5
        };

        $scope.$watch('doctorConf.currentPage + doctorConf.itemsPerPage', function(){
        	var currentPage = $scope.doctorConf.currentPage;
        	var itemsPerPage = $scope.doctorConf.itemsPerPage;

        	$scope.allpatients = $scope.doctorConf.data&&$scope.doctorConf.data.slice(currentPage-1,itemsPerPage)
        });

        // 医生 分页配置
        $scope.deviceConf = {
            currentPage: 0,
            itemsPerPage: 5
        };

        $scope.$watch('deviceConf.currentPage + deviceConf.itemsPerPage', function(){
        	var currentPage = $scope.deviceConf.currentPage;
        	var itemsPerPage = $scope.deviceConf.itemsPerPage;

        	$scope.allpatients = $scope.deviceConf.data&&$scope.deviceConf.data.slice(currentPage-1,itemsPerPage)
        });



		$(".myform_datetime").datetimepicker({
			format: 'yyyy-mm-dd',
			language:  'zh-CN', 
			minView: 2,   //时间选择器最小可以选择的范围：0分钟、1小时、2天
			weekStart: 1,
			todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			showMeridian: 1
		});

		$scope.pat = {};
		$scope.doc = {};
		$scope.dev = {};

		$scope.detail = function(flag, des){
			console.log(flag,des)
			$scope.detailTab = 'tab2';
			switch(flag){
				case 'patient':{
					// $scope.activeTab = "hide";
					$scope.innerTab='tab2-det';
					$scope.patientTmp = des;
					// $scope.spatientid = des.patientId;
					API.getDoctorsOfPatient(des.patientId).then(function(res){
						$scope.pdoctors = res.items;
					});

					// 测试demo
					// des.patientId = "9b7d8e61-ba5c-4e4a-903e-85cb75e49813";
					API.getAllTestsOfPatint(des.patientId).then(function(res){
						$scope.patientTests = res.items;
					})
				}
				break;
				case 'doctor':{
					// $scope.activeTab = "hide";
					$scope.innerTab = 'tab3-det';
					$scope.doctorTmp = des;
					// $scope.sdoctorid = dex.doctorId;
					API.getPatientsOfDoctor(des.doctorId).then(function(res){
						$scope.dpatients = res.items;
					});
				}
				break;
				case 'device':{
					// $scope.activeTab = "hide";
					$scope.innerTab = 'tab4-det';
					$scope.deviceTmp = des;
				}
				break;
			}
		}

		$scope.searchDoctor = function(e){
			e.stopPropagation();
			$scope.sdoctors = $filter('filter')($scope.alldoctors,{name:$scope.querydoctor});
		}

		$scope.searchPatient = function(e){
			e.stopPropagation();
			$scope.spatients = $filter('filter')($scope.allpatients,{name:$scope.querypatient});
		}

		$scope.cancelSave = function(){
			$scope.pat = {gender: "MALE"};
			$scope.doc = {gender: "MALE"};
			$scope.dev = {};
		}

		$scope.getQR = function(str){
			console.log(str)
			$("#myModalLabel").html('');
			new QRCode(document.getElementById("myModalLabel"), str);
			$('#myModal').modal('toggle')
		}

		$scope.getDoctorForPatient = function(){
			$('#searchDoctor').modal('toggle')
			$scope.sdoctors =[];
			$scope.doctorPatient={};
		}

		$scope.getPatientForDoctor = function(){
			$('#searchPatient').modal('toggle')
			$scope.spatients =[];
			$scope.doctorPatient={};
		}

		$scope.saveRelationship = function(docid, patid, switchtype) {
			if(docid && patid) {
				API.addRelationShips({
					objectId: patid,
					subjectId: docid,
					"relationshipType": "DOCTOR_PATIENT"
				}).then(function(res){

					switch(switchtype){
						case 1:{
							API.getDoctorsOfPatient(patid).then(function(res){
								$scope.pdoctors = res.items;
							});
						}
						break;
						case 2:{
							API.getPatientsOfDoctor(docid).then(function(res){
								$scope.dpatients = res.items;
							});
						}
						break;
					}

				});
			}
			
		}

		$scope.delRelationship = function(id){
			API.delRelationShips(id).then(function(res){

			})
		}

		$scope.getParam = function(param) {
			console.log(param)
			$("#myModalLabel").html(param);
			$('#myModal').modal('toggle')
		}

		$scope.mod = function(flag, des){
			console.log(flag,des)
			switch(flag){
				case 'patient':{
					$scope.innerTab = 'tab2-mod';
					$scope.pat = des;
					$scope.pat.birthdate = $filter('date')(des.birthdate,'yyyy-mm-dd');
				}
				break;
				case 'doctor':{
					$scope.innerTab = 'tab3-mod';
					$scope.doc = des;
				}
				break;
				case 'device':{
					$scope.innerTab = 'tab4-mod';
					$scope.dev = des;
				}
				break;
			}
		}

		function updatePatients(){
			API.getAllPatients().then(function(res){
				if(!!res){
					$scope.patientConf.data = res.items;
					$scope.patientConf.totalItems = res.items.length;
					$scope.allpatients = res.items.slice(0,$scope.patientConf.itemsPerPage)
				}
			})
		}

		function updateDoctors(){
			API.getAllDoctors().then(function(res){
				// console.log(res)
				if(!!res){
					$scope.doctorConf.data = res.items;
					$scope.doctorConf.totalItems = res.items.length;
					$scope.alldoctors = res.items.slice(0,$scope.doctorConf.itemsPerPage);
				}
			})
		}

		function updateDevices(){
			API.getAllDevices().then(function(res){
				// console.log(res)
				if(!!res){
					$scope.deviceConf.data = res.items;
					$scope.deviceConf.totalItems = res.items.length;
					$scope.alldevices = res.items.slice(0,$scope.deviceConf.itemsPerPage);
				}
			})
		}

		function updateHospitals(){
			API.getAllHospitals().then(function(res){
				if(!!res){
					$scope.allhospitals = res.items;
				}
			})
		}

		updatePatients();
		updateDoctors();
		updateDevices();
		updateHospitals();


		$scope.regpatient = function(e, type){
		 	e.stopPropagation();
		 	var obj = {
			  "address": $scope.pat.address,
			  "birthdate": $scope.pat.birthdate,
			  "email": $scope.pat.email,
			  "gender": $scope.pat.gender,
			  "name": $scope.pat.name,
			  "password": $scope.pat.password,
			  "phone": $scope.pat.phone,
			  "username": $scope.pat.userName
			};

		 	if(type === "add") {
				API.addPatient(obj).then(function(res){
					console.log(res)
					updatePatients();
				})
		 	} else {
		 		API.modPatient($scope.pat.patientId,obj).then(function(res){
					console.log(res)
					updatePatients();
				})
		 	}
		 	
		}

		$scope.regdoctor = function(e, type){
			e.stopPropagation();
			var obj = {
				"description": $scope.doc.description,
				"email": $scope.doc.email,
				"gender": $scope.doc.gender,
				"hospitalId": $scope.doc.hospitalId,
				"name": $scope.doc.name,
				"password": $scope.doc.password,
				"phone": $scope.doc.phone,
				"username": $scope.doc.userName
			};

			if(type === "add"){
				API.addDoctor(obj).then(function(res){
					console.log(res)
					updateDoctors();
				})
			} else {
				API.modDoctor($scope.doc.doctorId,obj).then(function(res){
					console.log(res)
					updateDoctors();
				})
			}
			
		}

		$scope.regdevice = function(e, type){
			e.stopPropagation();
			var obj = {
			  "deviceId": $scope.dev.deviceId,
			  "deviceSettings": $scope.dev.deviceSettings,
			  "deviceType": $scope.dev.deviceType,
			  "status": "ACTIVE"
			};

			if(type === "add"){
				API.addDevice(obj).then(function(res){
					console.log(res)
					updateDevices();
				})
			} else {
				API.modDevice($scope.dev.deviceId,obj).then(function(res){
					console.log(res)
					updateDevices();
				})
			}
			
		}



		$scope.delPatient = function(e, id){
			e.stopPropagation();
			API.delPatient(id).then(function(res){
				updatePatients();
			})
		}

		$scope.delDoctor = function(e, id){
			e.stopPropagation();
			API.delDoctor(id).then(function(res){
				updateDoctors();
			})
		}

		$scope.delDevice = function(e, id){
			e.stopPropagation();
			API.delDevice(id).then(function(res){
				updateDevices();
			})
		}

		$scope.getMineInfo = function(switchtype){
			switch(switchtype){
				case 1:{

				}
				break;
				case 2:{
					API.getPatient(userid).then(function(res){
						// $scope.mineInfo = 
					})
				}
				break;
				case 3:{
					API.getPatient(userid).then(function(res){
						// $scope.mineInfo = 
					})
				}
				break;
			}
		}
	}
});