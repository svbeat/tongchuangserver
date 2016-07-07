angular.module('aimuApp')
	.controller('home', ['$rootScope', '$http', '$location', 'visionService', function($rootScope, $http, $location, visionService) {
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
			});
		};
		
		visionService.getNotifications()
			.then(
					function(response) {
						self.notifications = response.data;
					}
			);
		
		
		visionService.getPerimetryTests(1, 5)
			.then(
					function(response) {
						self.mytests = response.data;
					}
			);
		}])
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
.controller('visionTests',  ['visionService', function(visionService) {
	var self = this;
	self.tests= [];
	self.sortType     = 'testDate'; // set the default sort type
	self.sortReverse  = true;  // set the default sort order
	
	self.pageno=1;
	self.total_count = 0;
	self.itemsPerPage = 10;
	
	self.getTotal = function() {
		self.total_count = 0;
		visionService.getPerimetryTestsTotal()
			.then(
				function(response) {
					self.total_count = response.data;
				}
		);		
	}
	
	self.getData = function(pageno) {
		self.tests= [];
		
		visionService.getPerimetryTests(pageno, self.itemsPerPage)
			.then(
				function(response) {
					self.tests = response.data;
				}
		);		
	}

	self.getData(self.pageno); 
	self.getTotal();
}])
.controller('navigation',

		function($rootScope, $http, $location) {

	var self = this

	var authenticate = function(credentials, callback) {

		var headers = credentials ? {authorization : "Basic "
			+ btoa(credentials.username + ":" + credentials.password)
		} : {};

		$http.get('user', {headers : headers}).then(function(response) {
			if (response.data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback();
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
		});
	};

	self.getClass = function (path) {
		var locPath = $location.path().substr(0, path.length);
		return ((locPath === path)||(locPath=='/' && path=="/healthcenter")) ? 'active' : '';
	}
});