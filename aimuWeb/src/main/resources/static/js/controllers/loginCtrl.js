define(["angular"], function(angular) {
	return function($rootScope,$scope,$location, API){
		$scope.welcome = 'login here...';
		$scope.userLogin = function(){
			// console.log($scope.userPwd, $scope.userName)
			API.login({
				userName:$scope.userName,
				password:$scope.userPwd
			}).then(function(res){
				// console.log(res)
				if(!!res){
					$scope.loginerror = false;
					API.setCookies("k_username",$scope.userName, res.expiresTime);
					API.setCookies("k_token",res.token, res.expiresTime);
					$rootScope.curusername = $scope.userName;
					API.getUserRoles($scope.userName).then(function(resdata){
						if(!!resdata){
							API.setCookies("k_role",resdata.role, res.expiresTime);
							API.setCookies("k_subjectid",resdata.subjectId, res.expiresTime);
							API.setCookies("k_userid",resdata.userid, res.expiresTime);
							API.clearCookies('k_activeTab');
							switch(resdata.role){
								case "ADMIN":{//userRoleId 0
									$location.url('/admin');
								}
								break;
								case "DOCTOR":{//userRoleId 4
									$location.url('/doctor');
								}
								break;
								case "PATIENT":{//userRoleId 1
									$location.url('/patient');
								}
								break;
							}
						}
						
						
					})
				}else{
					$scope.loginerror = true;
				}
				
			})

			
		}
	}
});