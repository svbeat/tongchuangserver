#项目文档

####运行环境
	
	对于环境没有特别要求，只要能提供网页服务都可以，比如：node、apache...

	包中包含项目中用到的所有文件

####目录结构

	├─bower_components
	│  ├─angular
	│  ├─angular-cookies
	│  ├─angular-route
	│  ├─bootstrap
	│  ├─jquery
	│  └─requirejs
	├─css
	├─images
	├─js
	│  ├─asset
	│  ├─controllers
	│  ├─directives
	│  ├─filters
	│  ├─lib
	│  └─service
	└─views
	    └─tpl


####入口文件

	./js/app.js

	1、文件中配置了angularjs相关的文件（控制器、指令、过滤器、服务）路径

	2、angular各自定义组件的注入也在此，包括路由

	3、后期新增相关文件可以配置在此处

####API请求的封装
	
	./js/service

		Http ---> API

		Http.js  封装了基础的请求格式，配置了API请求地址

		API.js  封装了模板网页中用到的数据接口，后期新增或修改数据接口在于此文件中进行


####模板网页Tab控制

	顶级 Tab导航    activeTab  
	内页 Tab导航    innerTab 
	其它 Tab导航	


#####其它倒没有要特别说明的，有问题可以随时QQ我
