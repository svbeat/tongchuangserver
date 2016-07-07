angular.module('aimuApp', [ 'ngRoute', 'ui.bootstrap', 'angularUtils.directives.dirPagination' ])
	.config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
      templateUrl : 'login.html',
      controller : 'navigation',
      controllerAs: 'controller'
    }).when('/healthcenter', {
        templateUrl : 'healthcenter.html',
        controller : 'home',
        controllerAs: 'controller'
      }).when('/login', {
      templateUrl : 'login.html',
      controller : 'navigation',
      controllerAs: 'controller'
    }).when('/products', {
        templateUrl : 'products.html'
      }).when('/aboutus', {
        templateUrl : 'aboutus.html'
      }).otherwise('/');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

  });