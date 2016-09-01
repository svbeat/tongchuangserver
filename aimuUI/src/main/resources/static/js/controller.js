angular.module('aimuApp')
.controller('home', ['$rootScope', '$http', '$location', '$modal', 'visionService', '$window', 
                     function($rootScope, $http, $location, $modal, visionService, $window) {
	if (!$rootScope.authenticated) {
		$location.path("/login");
	} 

	var self = this;
	self.tab = 1;

	self.setTab = function(newTab){
		self.tab = newTab;
	};

	self.isSet = function(tabNum){
		return self.tab === tabNum;
	};

	self.getNotificationIcon = function(notificationType) {
		if (notificationType=='message') {
			return "images/inbox_orange.png";
		} else {
			return "images/notifications_orange.png";
		}
	}

	self.getUserName = function() {
		return $rootScope.username;
	}

	self.tests= [
	             {
	            	 name: '视野检测',
	            	 description: '连续视野检查结果的比较是病情有无进展的敏感指标，视野缺损稳定无变化意味着治疗有效，而缺损进行性加重，是需要加强治疗力度的指征。',
	            	 price:'20元'
	             },
	             {
	            	 name: '眼底影像',
	            	 description: '从眼底图像我们可以诊断各种眼病以及内科病和脑系科的疾病。如视神经病变、视网膜病变、视网膜血管病变、黄斑病变、高血压动脉硬化、糖尿病视网膜病变、肾病、血液病、颅脑疾病等等',
	            	 price:'50元'
	             },
	             {
	            	 name: 'OCT扫描',
	            	 description: '',
	            	 price:'200元'
	             }
	             ];

	self.mydoctors= [
	                 {
	                	 name: '李华医生',
	                	 phone: '558-6901',
	                	 hospital:'北湾第一医院',
	                	 workHour: '每周一，三，五'
	                 },
	                 {
	                	 name: '张浩主治医师',
	                	 phone: '350-6666',
	                	 hospital:'南苑眼科中心',
	                	 workHour: '每周二，五'
	                 }
	                 ];

	self.logout = function() {
		$http.post('logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$location.path("/");
			$window.localStorage.removeItem("patientId");
		});
	};

	self.openUserDetails = function() {
		var modalInstance = $modal.open({
			templateUrl: 'views/userDetails.html',
			controller: 'userDetails'
		});
	};

	visionService.getNotifications()
	.then(
			function(response) {
				self.notifications = response.data;
			}
	);

	var patientId = $window.localStorage.getItem("patientId");
	visionService.getPerimetryTests(patientId, 1, 5)
	.then(
			function(response) {
				self.mytests = response.data;
			}
	);
}])
.controller('userDetails', function($rootScope, $scope, $modalInstance) {
	$scope.username = $rootScope.username;
	
	$scope.init = function() {
		var qrcode = new QRCode(document.getElementById('qrcode'));
		qrcode.makeCode($scope.username);
	};
	
	$scope.close = function () {
		$modalInstance.dismiss('cancel');
	}

	$scope.print = function () {
		window.print();
	}
})
.controller('CarouselDemoCtrl',

		function() {

	var self = this;
	self.myInterval = 3000;
	self.slides = [
	               {
	            	   image: 'images/perimetry.png',
	            	   text:'新一代智能视野仪'
	               },
	               {
	            	   image: 'images/fundus.png',
	            	   text:'便携式眼底相机'
	               },
	               {
	            	   image: 'images/oct.png',
	            	   text:'OCT扫描仪'
	               }
	               ];
}) 
.controller('visionTests',  ['visionService', '$modal', '$rootScope', '$window', function(visionService, $modal, $rootScope, $window) {
	var self = this;
	self.tests= [];
	self.sortType     = 'testDate'; // set the default sort type
	self.sortReverse  = true;  // set the default sort order

	self.pageno=1;
	self.total_count = 0;
	self.itemsPerPage = 10;
	self.patientId = $window.localStorage.getItem("patientId");

	self.getTotal = function() {
		self.total_count = 0;
		visionService.getPerimetryTestsTotal(self.patientId)
		.then(
				function(response) {
					self.total_count = response.data;
				}
		);		
	}

	self.getData = function(pageno) {
		self.tests= [];

		visionService.getPerimetryTests(self.patientId, pageno, self.itemsPerPage)
		.then(
				function(response) {
					self.tests = response.data;
				}
		);		
	}

	self.getData(self.pageno); 
	self.getTotal();

	self.openReport = function(index) {
		console.log('opening pop up:'+self.tests[index]);
		var modalInstance = $modal.open({
			templateUrl: 'views/detailreport.html',
			controller: 'detailReport',
			resolve: {
				test: function () {
					return self.tests[index];
				}
			}
		});
	};
}])
.controller('detailReport', function($scope, $modalInstance, test) {
	$scope.testResult = test.result;

	$scope.testId = test.testId;
	$scope.patient = test.patientId;
	$scope.testDate = test.testDateDisplay;

	function showResult(textCanvasId, shadeCanvasId, examResult) {
    	var myRegexp = /q([0-9]+)r([0-9]+)c([0-9]+)/g;
		var results = [];
		
		var displayInterval = 15;
		
		for (var posCode in examResult) {
		    if (examResult.hasOwnProperty(posCode)) {
		    	var match = myRegexp.exec(posCode);
		    	myRegexp.lastIndex = 0;
		    	var q = Number(match[1]);
		    	var r = Number(match[2]);
		    	var c = Number(match[3]);

		    	var x = (c-1+0.5)*displayInterval;
		    	var y = (r-1+0.5)*displayInterval;
		    	
		        if (q == 1) {
		            y = -y;
		        } else if (q == 2) {
		            x = -x;
		            y = -y;
		        } else if (q == 3) {
		            x = -x;
		        }
		    	
		    	var dbValue = examResult[posCode];
		    	var complete=true;
		    	if (examResult[posCode].endsWith("?")) {
		    		dbValue = dbValue.substring(0, dbValue.length-1);
		    		complete = false;
		    	}
		    	var p = {db:dbValue, x: x, y:y, complete:complete};
		    	results.push(p);
		    }
		}
		
		var canvas = document.getElementById(textCanvasId);
		var context = canvas.getContext('2d');
	
		centerX=100;
		centerY=100;

		var AMPLIFICATION = 0.1;

		




		for (var i = 0; i < results.length; i++) {
			results[i].x = centerX+results[i].x;
			results[i].y = centerY+results[i].y;
		}

		for (var i = 0; i < results.length; i++) {
			if (results[i].complete) {
				context.font = "bold 10px Arial";
				context.fillStyle="black";
			} else {
				context.font = "10px Arial";
				context.fillStyle="#4b78a6";
			}
			context.fillText(results[i].db, results[i].x, results[i].y);
		}

		canvas = document.getElementById(shadeCanvasId);
		context = canvas.getContext('2d');

		for (var i = 0; i < results.length; i++) {
			var db = results[i].db;
	    	if (db.endsWith("+")||db.endsWith("-")) {
	    		db = db.substring(0, db.length-1);
	    	}
			var shade = (db-10)*5;
			context.fillStyle='rgb('+shade+','+shade+','+shade+')';
			context.fillRect(results[i].x, results[i].y,displayInterval,displayInterval);
		}
		
	}
	
	$scope.close = function () {
		$modalInstance.dismiss('cancel');
	}

	$scope.print = function () {
		window.print();
	}

	$scope.init = function() {

		var data = angular.fromJson($scope.testResult);
		if (data.hasOwnProperty('examResultLeft')) {
			showResult('canvasTextLeft', 'canvasShadeLeft', data.examResultLeft);
		}

		if (data.hasOwnProperty('examResultRight')) {
			showResult('canvasTextRight', 'canvasShadeRight', data.examResultRight);
		}

		if (data.hasOwnProperty('examResultLeft') && data.hasOwnProperty('examResultRight')) {
			$scope.eye='双眼';
		} else if (data.hasOwnProperty('examResultLeft') ){
			$scope.eye='左眼';
		} else if (data.hasOwnProperty('examResultRight')){
			$scope.eye='右眼';
		}
	}




	function drawDot(context, data) {
		context.beginPath();
		context.arc(data.x, data.y, data.amount, 0, 2*Math.PI, false);
		context.fillStyle = "#ccddff";
		context.fill();
		context.lineWidth = 1;
		context.strokeStyle = "#666666";
		context.stroke();  
	}
})
.controller('navigation',  ['visionService', '$rootScope', '$http', '$location', '$window',
                            			function(visionService, $rootScope, $http, $location, $window) {

	var self = this

	var authenticate = function(credentials, callback) {

		var headers = credentials ? {authorization : "Basic "
			+ btoa(credentials.username + ":" + credentials.password)
		} : {};

		if (credentials!=null) {
			$rootScope.username = credentials.username;
		}

		$http.get('user', {headers : headers}).then(function(response) {
			if (response.data.name) {
				$rootScope.authenticated = true;
				var patientId = $window.localStorage.getItem("patientId");
				if (patientId == null) {
					visionService.getUserRole($rootScope.username)
					.then(
							function(response) {
								var patientId = 0;
								if (response.data.role=='PATIENT') {
									console.log('after getUserInfo: patientId_1=', response.data.subjectId);
									patientId = response.data.subjectId;
									console.log('after getUserInfo: patientId_2=', $rootScope.patientId);
								}
								$window.localStorage.setItem("patientId", patientId);
								callback && callback();
							}
					);					
				} else {
					callback && callback();
				}
			} else {
				$rootScope.authenticated = false;
				callback && callback();
			}
			
		}, function() {
			$rootScope.authenticated = false;
			callback && callback();
		});

	}

	authenticate();

	if ($rootScope.authenticated) {
		$location.path("/healthcenter");
	}

	self.credentials = {};
	self.login = function() {
		$window.localStorage.removeItem("patientId");
		authenticate(self.credentials, function() {
			if ($rootScope.authenticated) {
				$location.path("/healthcenter");
				self.error = false;
			} else {
				$location.path("/login");
				self.error = true;
			}
		});
	};

	self.logout = function() {
		$http.post('logout', {}).finally(function() {
			$rootScope.authenticated = false;
			$location.path("/");
			$window.localStorage.removeItem("patientId");
		});
	};

	self.getClass = function (path) {
		var locPath = $location.path().substr(0, path.length);
		return ((locPath === path)||(locPath=='/' && path=="/healthcenter")) ? 'active' : '';
	}
}]);