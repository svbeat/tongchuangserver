'use strict';
require.config({
    paths:{
        "angular": "/bower_components/angular/angular.min",
        "angular-route": "/bower_components/angular-route/angular-route.min",
        "angular-cookies": "/bower_components/angular-cookies/angular-cookies.min",
        "ngBootstrap" : 'http://bootzee.com/js/ui-bootstrap-tpls-0.11.2',
        
        "Pagination": "/js/directives/angular.pagination",
        "AllFilter":"/js/filters/AllFilter",

        "Http":"/js/service/Http",
        "API":"/js/service/API",

        "indexCtrl":"/js/controllers/indexCtrl",
        "adminCtrl":"/js/controllers/adminCtrl",
        "doctorCtrl":"/js/controllers/doctorCtrl",
        "patientCtrl":"/js/controllers/patientCtrl",
        // "instrumentCtrl":"/js/controllers/instrumentCtrl",
        "loginCtrl":"/js/controllers/loginCtrl",
        "testReportCtrl":"/js/controllers/testReportCtrl"
    },
    shim:{
        "angular": {
            exports: "angular"
        },
        "angular-route": {
            deps: ["angular"],
            exports: "angular-route"
        },
        "angular-cookies": {
            deps: ["angular"],
            exports: "angular-cookies"
        },
        "ngBootstrap": { exports: 'ngBootstrap', deps: ['angular'] }
    },
    urlArgs: "version=" + Math.random(),
    waitSeconds: 0
});

require(["angular", "angular-route", "angular-cookies","ngBootstrap","Pagination",
    "AllFilter",
    "Http","API",
    "indexCtrl", "adminCtrl", "doctorCtrl", "patientCtrl", "loginCtrl","testReportCtrl"],
    function(angular,angular_route,angular_cookies,ngBootstrap,Pagination,
        AllFilter,
        Http,API,
        indexCtrl, adminCtrl, doctorCtrl, patientCtrl, loginCtrl, testReportCtrl){

var app = angular.module('app', ['ngRoute',"ngCookies",'ui.bootstrap']);

app.directive('tmPagination',Pagination);

app.filter("sex", AllFilter.sex)
app.filter("age", AllFilter.age)

app.service("Http", Http)
app.service("API", API)

app.controller("indexCtrl",indexCtrl)
app.controller("adminCtrl",adminCtrl)
app.controller("doctorCtrl",doctorCtrl)
app.controller("patientCtrl",patientCtrl)
app.controller("loginCtrl",loginCtrl)
app.controller("testReportCtrl",testReportCtrl)

app.run(function($rootScope, $location, $routeParams, $route,API){
    var curusername = API.getCookies("k_username");
    if(curusername){
        $rootScope.curusername = curusername;
        switch(API.getCookies("k_role")){
            case "ADMIN":{//userRoleId 0
                $location.url('/admin');
            }
            break;
            case "DOCTOR":{//userRoleId 0
                $location.url('/doctor');
            }
            break;
            case "PATIENT":{//userRoleId 0
                $location.url('/patient');
            }
            break;
        }
    }

    $rootScope.footprint = function(mark){
        API.setCookies("k_activeTab",mark, (new Date()).getTime() +30*24*3600000);
    }

    $rootScope.go = function(url, e){
        e.stopPropagation();
        $location.url(url);
    }

    $rootScope.logout = function(e){
        e.stopPropagation();
        $rootScope.curusername = undefined;
        API.clearCookies('k_username');
        API.clearCookies('k_token');
        API.clearCookies('k_role');
        $location.url('/login');
    }

    $rootScope.$on("$locationChangeStart", function(evt, next, current) {
        var path = $location.path().toUpperCase();
        var username = API.getCookies("k_role");
        if(username){
            if(path.indexOf(username)===-1){
                evt.preventDefault()
            }
        }else{
            $location.url('/login')
        }
        
    });

    $rootScope.$on("$routeChangeStart", function(evt, next, current) {


    });
})

app.config(['$routeProvider', '$locationProvider', "$httpProvider",function ($routeProvider, $locationProvider,$httpProvider) {
 


    $routeProvider
        .when('/', {
            templateUrl: 'views/tpl/index.html', 
            controller: 'indexCtrl'
        })
        .when('/index', {
            templateUrl: 'views/tpl/index.html', 
            controller: 'indexCtrl'
        })
        .when('/index.html', {
            templateUrl: 'views/tpl/index.html', 
            controller: 'indexCtrl'
        })

        .when('/admin', {
         templateUrl: 'views/tpl/admin.html', 
         controller: 'adminCtrl'
        })

        .when('/doctor', {
            templateUrl: 'views/tpl/doctor.html', 
            controller: 'adminCtrl'
        })
        .when('/patient', {
            templateUrl: 'views/tpl/patient.html', 
            controller: 'patientCtrl'
        })
        .when('/instrument', {
            templateUrl: 'views/tpl/instrument.html', 
            controller: 'instrumentCtrl'
        })

        .when('/login', {
         templateUrl: 'views/tpl/login.html', 
         controller: 'loginCtrl'
        })

        .otherwise({redirectTo: '/login'});

        // $httpProvider.defaults.headers.post["Content-Type"] = "application/json;charset=utf-8";
        //     var param = function(obj) {
        //         var name, value, fullSubName, subName, subValue, innerObj, i, query = "";
        //         for (name in obj)
        //             if (value = obj[name],
        //             value instanceof Array)
        //                 for (i = 0; i < value.length; ++i)
        //                     subValue = value[i],
        //                     fullSubName = name + "[" + i + "]",
        //                     innerObj = {},
        //                     innerObj[fullSubName] = subValue,
        //                     query += param(innerObj) + "&";
        //             else if (value instanceof Object)
        //                 for (subName in value)
        //                     subValue = value[subName],
        //                     fullSubName = name + "[" + subName + "]",
        //                     innerObj = {},
        //                     innerObj[fullSubName] = subValue,
        //                     query += param(innerObj) + "&";
        //             else
        //                 void 0 !== value && null  !== value && (query += encodeURIComponent(name) + "=" + encodeURIComponent(value) + "&");
        //         return query.length ? query.substr(0, query.length - 1) : query
        //     }
  
        //     $httpProvider.defaults.transformRequest = [function(data) {
        //         return angular.isObject(data) && "[object File]" !== String(data) ? param(data) : data
        //     }],
            // $httpProvider.defaults.headers.common.Accept = "version=1.0.1&client_type=wap";
    
    console.log('config suc');
    // $locationProvider.html5Mode(true);
}]);

angular.bootstrap($('html')[0],["app"]);




})

