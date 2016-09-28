define(["angular"], function(angular) {
	return function($scope,$filter,$modal, API){
		var role = API.getCookies("k_role");
		var userid = API.getCookies('k_subjectid');
		$scope.activeTab = !!API.getCookies("k_activeTab")?API.getCookies("k_activeTab"):'tab2';
		$scope.innerTab = $scope.activeTab;

		$scope.pat = {};
		$scope.doc = {};
		$scope.dev = {};


		function showMessage(msg){
			$("#myModalLabel").html(msg);
			$('#myModal').modal('toggle')
		}

		$scope.detail = function(flag, des){
			console.log(flag,des)
			$scope.detailTab = 'tab2';
			$scope.currPatient=des;
			switch(flag){
			case 'patient':{
				// $scope.activeTab = "hide";
				$scope.detailTab = 'tab1';
				$scope.innerTab='tab2-det';
				// $scope.patientTmp = des;
				API.getPatient(des.patientId).then(function(res){
					$scope.patientTmp = res;
				});
				// $scope.spatientid = des.patientId;
				API.getDoctorsOfPatient(des.patientId).then(function(res){
					$scope.pdoctors = res.items;
				});

				// 测试demo
				// des.patientId = "9b7d8e61-ba5c-4e4a-903e-85cb75e49813";
				API.getAllTestsOfPatint(des.patientId,{
					returnTotal: true,
					pageno:  $scope.pTestConf.currentPage-1,
					pagesize:  $scope.pTestConf.itemsPerPage
				}).then(function(res){
					$scope.pTestConf.data = res.items;
					$scope.pTestConf.totalItems = res.totalCounts;
					$scope.patientTests = res.items;
				});

				API.getPatientSettings(des.patientId).then(function(res){
					$scope.patientSettings = res;
					$scope.patientSettingsJson = JSON.stringify(res, null, 2);
				})
				
				// QRcode
				$("#qrcode").empty();
				var qrText = 'patient:'+des.patientId;
				var vOption = {text:qrText, width:80, height:80};
				new QRCode(document.getElementById("qrcode"), vOption);
			}
			break;
			case 'doctor':{
				// $scope.activeTab = "hide";
				$scope.innerTab = 'tab3-det';
				// $scope.doctorTmp = des;
				API.getDoctor(des.doctorId).then(function(res){
					$scope.doctorTmp = res;
				});

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
			API.getAllDoctors({
				filter: !!$scope.querydoctor?$scope.querydoctor: "",
						pageSize:1000
			}).then(function(res){
				$scope.sdoctors = res.items;
			})
			// $scope.sdoctors = $filter('filter')($scope.doctorConf.data,{name:$scope.querydoctor});

		}

		$scope.searchPatient = function(e){
			e.stopPropagation();
			API.getAllPatients({
				filter:!!$scope.querypatient?$scope.querypatient: "",
						pageSize:1000
			}).then(function(res){
				$scope.spatients = res.items;
			})
			// $scope.spatients = $filter('filter')($scope.patientConf.data,{name:$scope.querypatient});
		}

		$scope.cancelSave = function(){
			$scope.pat = {gender: "MALE"};
			$scope.doc = {gender: "MALE"};
			$scope.dev = {};

			$scope.innerTab = $scope.backInnerTab;
		}

		$scope.getQR = function(patient){
			var qrText = 'patient:'+patient.patientId;
			console.log(qrText)
			$scope.currPatient=patient;
			$("#myModalLabel").html('');
			new QRCode(document.getElementById("myModalLabel"), qrText);
			$('#myModal').modal('toggle')
		}

		$scope.getDoctorForPatient = function(){
			$('#searchDoctor').modal('toggle')
			$scope.sdoctors =[];
			$scope.doctorPatient={};
		}
		$scope.updatePatientSettings = function() {
			$('#updatePatientSettings').modal('toggle')
		}

		$scope.savePatientSettings = function(id) {
			$scope.patientSettings = JSON.parse($scope.patientSettingsJson);
			API.savePatientSettings(id, $scope.patientSettings)
			.then(function(res){
				$scope.patientSettings = res;
				$scope.patientSettingsJson = JSON.stringify(res, null, 2);
			});
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
				}).then(function(){

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

		$scope.delRelationship = function(id, switchtype){
			API.delRelationShips(id).then(function(res){
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
			})
		}

		$scope.getParam = function(param) {
			console.log(param)
			$("#myModalLabel").html(param);
			$('#myModal').modal('toggle')
		}

		$scope.mod = function(flag, des){
			console.log(flag,des)
			$scope.backInnerTab = $scope.innerTab;
			switch(flag){
			case 'patient':{
				$scope.innerTab = 'tab2-mod';

				// $scope.pat = des;
				API.getPatient(des.patientId).then(function(res){
					$scope.pat = res;
					$scope.pat.birthdate = $filter('date')(des.birthdate,'yyyy-MM-dd');
				})

			}
			break;
			case 'doctor':{
				$scope.innerTab = 'tab3-mod';
				// $scope.doc = des;

				API.getDoctor(des.doctorId).then(function(res){
					$scope.doc = res;
				})
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

			if(role === "ADMIN"){
				API.getAllPatients().then(function(res){
					if(!!res){
						$scope.patientConf.data = res.items;
						$scope.patientConf.totalItems = res.items.length;
						$scope.allpatients = res.items.slice(0,$scope.patientConf.itemsPerPage)
					}
				})
			} else if(role === "DOCTOR") {
				API.getPatientsOfDoctor(userid).then(function(res){
					if(!!res){
						$scope.patientConf.data = res.items;
						$scope.patientConf.totalItems = res.items.length;
						$scope.allpatients = res.items.slice(0,$scope.patientConf.itemsPerPage)
					}
				})
			}

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


		$scope.regStart = function(tab){
			$scope.pat = {gender: "MALE"};
			$scope.doc = {gender: "MALE"};
			$scope.dev = {};

			$scope.backInnerTab = tab;
		}

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
					"username": $scope.pat.username,
					"userid": $scope.pat.userid
			};

			if(type === "add") {
				API.addPatient(obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else {
						updatePatients();
						$scope.innerTab = $scope.backInnerTab;
					}

				})
			} else {
				API.modPatient($scope.pat.patientId,obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else {
						updatePatients();
						obj.patientId = $scope.pat.patientId;
						$scope.pat = obj;
						$scope.innerTab = $scope.backInnerTab;
					}
				})
			}
		}
		$scope.resetPatientSettings = function(patientId) {
			API.resetPatientSettings(patientId).then(function(res){
				$scope.patientSettings = res;
				$scope.patientSettingsJson = JSON.stringify(res, null, 2);
			})			
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
					"username": $scope.doc.username,
					"userid": $scope.doc.uesrid
			};

			if(type === "add"){
				API.addDoctor(obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else {
						updateDoctors();
						$scope.innerTab = $scope.backInnerTab;

					}
					// console.log(res)

				})
			} else {
				API.modDoctor($scope.doc.doctorId,obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else{
						updateDoctors();
						obj.doctorId = $scope.doc.doctorId;
						$scope.doc = obj;
						$scope.innerTab = $scope.backInnerTab;
					}
					// console.log(res)

				})


			}

		}

		$scope.regdevice = function(e, type){
			e.stopPropagation();
			var obj = {
					"deviceId": $scope.dev.deviceId,
					"deviceSettings": $scope.dev.deviceSettings,
					"deviceType": $scope.dev.deviceType,
					"description": $scope.dev.description,
					"status": "ACTIVE"
			};

			if(type === "add"){
				API.addDevice(obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else {
						updateDevices();
						$scope.innerTab = $scope.backInnerTab;
					}

				})
			} else {
				API.modDevice($scope.dev.deviceId,obj).then(function(res){
					if(res.code == 400 && res.status == 400){
						showMessage(res.message);
					} else {
						updateDevices();
						obj.deviceId = $scope.dev.deviceId;
						$scope.dev = obj;

						$scope.innerTab = $scope.backInnerTab;
					}

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
				API.getDoctor(userid).then(function(res){
					$scope.mineInfo = res;
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

		// 病人 分页配置
		$scope.patientConf = {
				currentPage: 1,
				itemsPerPage: 10
		};

		$scope.$watch('patientConf.currentPage + patientConf.itemsPerPage', function(){
			// console.log($scope.patientConf.currentPage, $scope.patientConf.itemsPerPage)
			var currentPage = $scope.patientConf.currentPage;
			var itemsPerPage = $scope.patientConf.itemsPerPage;
			var currentIndex = (currentPage-1)*itemsPerPage;

			updatePatients();
		});

		function updatePatients(){

			if(role === "ADMIN"){
				API.getAllPatients({
					returnTotal: true,
					pageno:  $scope.patientConf.currentPage-1,
					pagesize:  $scope.patientConf.itemsPerPage
				}).then(function(res){
					if(!!res){
						$scope.patientConf.data = res.items;
						$scope.patientConf.totalItems = res.totalCounts;
						$scope.allpatients = res.items
					}
				})
			} else if(role === "DOCTOR") {
				API.getPatientsOfDoctor(userid).then(function(res){
					if(!!res){
						$scope.patientConf.data = res.items;
						$scope.patientConf.totalItems = res.items.length;
						$scope.allpatients = res.items.slice(0,$scope.patientConf.itemsPerPage)
					}
				})
			}

		}

		function updateDoctors(){
			API.getAllDoctors({
				returnTotal: true,
				pageno:  $scope.doctorConf.currentPage-1,
				pagesize:  $scope.doctorConf.itemsPerPage
			}).then(function(res){
				// console.log(res)
				if(!!res){
					$scope.doctorConf.data = res.items;
					$scope.doctorConf.totalItems = res.totalCounts;
					$scope.alldoctors = res.items;
				}
			})
		}

		function updateDevices(){
			API.getAllDevices({
				returnTotal: true,
				pageno:  $scope.deviceConf.currentPage-1,
				pagesize:  $scope.deviceConf.itemsPerPage
			}).then(function(res){
				// console.log(res)
				if(!!res){
					$scope.deviceConf.data = res.items;
					$scope.deviceConf.totalItems = res.totalCounts;
					$scope.alldevices = res.items;
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

			updatePatientTests($scope.currPatient.patientId);
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

		$(".myform_datetime").datetimepicker({
			format: 'yyyy-mm-dd',
			// language:  'zh-CN', 
			minView: 2,   //时间选择器最小可以选择的范围：0分钟、1小时、2天
			weekStart: 1,
			todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			showMeridian: 1
		});



		if(role === "ADMIN"){


			// 医生 分页配置
			$scope.doctorConf = {
					currentPage: 1,
					itemsPerPage: 5
			};

			$scope.$watch('doctorConf.currentPage + doctorConf.itemsPerPage', function(){
				var currentPage = $scope.doctorConf.currentPage;
				var itemsPerPage = $scope.doctorConf.itemsPerPage;
				var currentIndex = (currentPage-1)*itemsPerPage

				// $scope.alldoctors = $scope.doctorConf.data&&$scope.doctorConf.data.slice(currentIndex,currentIndex+itemsPerPage)


				API.getAllDoctors({
					returnTotal: true,
					pageno: currentPage-1,
					pagesize: itemsPerPage
				}).then(function(res){
					$scope.doctorConf.data = res.items;
					$scope.doctorConf.totalItems = res.totalCounts;
					$scope.alldoctors = res.items;
				});

			});

			// 医生 分页配置
			$scope.deviceConf = {
					currentPage: 1,
					itemsPerPage: 5
			};

			$scope.$watch('deviceConf.currentPage + deviceConf.itemsPerPage', function(){
				var currentPage = $scope.deviceConf.currentPage;
				var itemsPerPage = $scope.deviceConf.itemsPerPage;
				var currentIndex = (currentPage-1)*itemsPerPage

				$scope.alldevices = $scope.deviceConf.data&&$scope.deviceConf.data.slice(currentIndex,currentIndex+itemsPerPage)
				API.getAllDevices({
					returnTotal: true,
					pageno: currentPage-1,
					pagesize: itemsPerPage
				}).then(function(res){
					$scope.deviceConf.data = res.items;
					$scope.deviceConf.totalItems = res.totalCounts;
					$scope.alldevices = res.items;
				});
			});


			//admin

			// updateDoctors();
			// updateDevices();
			updateHospitals();
		} else if(role === "DOCTOR") {
			// doctor
			$scope.getMineInfo(2);
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
