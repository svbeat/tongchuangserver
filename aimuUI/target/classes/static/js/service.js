angular.module('aimuApp')
	   .service('visionService', function($http) {
		   
		   var self = this;
		   
		   self.getPerimetryTests = function(pageno, limit){
			   return $http.get("http://121.40.177.67:8190/dummypatient/perimetrytests/?apiKey=rock2016&pageno="+(pageno-1)+"&limit="+limit);
		   };

		   self.getNotifications = function(){
			   return $http.get("http://121.40.177.67:8190/dummypatient/notifications/?apiKey=rock2016");
		   };

		   self.getDoctors = function(){
			   return $http.get("http://121.40.177.67:8190/dummypatient/doctors/?apiKey=rock2016");
		   };
		   
		   self.getAvailableTests = function(){
			   return $http.get("http://121.40.177.67:8190/tests/?apiKey=rock2016");
		   };
		   
		   self.getPerimetryTestsTotal = function(){
			   return $http.get("http://121.40.177.67:8190/dummypatient/perimetrytests/count?apiKey=rock2016");
		   };
	   });