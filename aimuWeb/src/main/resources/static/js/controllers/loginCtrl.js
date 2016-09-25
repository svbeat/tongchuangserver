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
		};
		
		API.fetchNewsShort().then(function(res){

			if(!!res){
				$scope.news = res;
				
				$scope.news='<div style="text-align:left;word-wrap:break-word;width:100%" class=baidu><a href="http://news.e23.cn/jnnews/2016-09-23/2016092300448.html" target="_blank">眼科专家进村义诊 桑梓店镇村民享医疗帮扶</a>&nbsp;<span>舜网&nbsp;2016-09-23 18:35:41</span><br><a href="http://nb.ifeng.com/a/20160923/5002636_0.shtml" target="_blank">宁波市眼科医院与浙江省残疾人福利基金会联合启动公益“艾欣瞳助...</a>&nbsp;<span>凤凰宁波站&nbsp;2016-09-23 16:09:00</span><br><a href="http://www.ln.xinhuanet.com/st/zixun/20160923/3455771_p.html" target="_blank">(网络媒体走转改)带你走进奥比斯 探秘全球唯一的&quot;飞机眼科医院&quot;</a>&nbsp;<span>新华网辽宁站&nbsp;2016-09-23 14:16:10</span><br><a href="http://news.gmw.cn/newspaper/2016-09/23/content_116435395.htm" target="_blank">“奇迹”让刘奶奶对生活又燃起了新希望 </a>&nbsp;<span>光明网&nbsp;2016-09-23 11:08:00</span><br><a href="http://news.hexun.com/2016-09-23/186158182.html" target="_blank">厦门眼科中心打造技术高地 全国年会“独占鳌头”</a>&nbsp;<span>和讯&nbsp;2016-09-23 10:29:00</span><br><div style="margin-top:5px;font-size:12px"><a href="http://news.baidu.com/ns?word=眼科&tn=news&ie=gb2312&sr=0&cl=2&rn=20&ct=0" target="_blank" class="more">&#x66f4;&#x591a;&gt;&gt;</a></div></div>';

				console.log($scope.news);
			}
		});
	}
});