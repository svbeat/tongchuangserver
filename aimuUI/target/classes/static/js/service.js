angular.module('aimuApp')
	   .service('visionService', function($http) {
		   
		   var self = this;
		   var prodUrl="http://121.40.177.67:8190";
		   var devUrl="http://localhost:8190";
		   
		   var url = devUrl;
		   
		   self.getPerimetryTests = function(pageno, limit){
			   return $http.get(url+"/dummypatient/perimetrytests/?apiKey=rock2016&pageno="+(pageno-1)+"&limit="+limit);
		   };

		   self.getNotifications = function(){
			   return $http.get(url+"/dummypatient/notifications/?apiKey=rock2016");
		   };

		   self.getDoctors = function(){
			   return $http.get(url+"/dummypatient/doctors/?apiKey=rock2016");
		   };
		   
		   self.getAvailableTests = function(){
			   return $http.get(url+"/tests/?apiKey=rock2016");
		   };
		   
		   self.getPerimetryTestsTotal = function(){
			   return $http.get(url+"/dummypatient/perimetrytests/count?apiKey=rock2016");
		   };
	   });