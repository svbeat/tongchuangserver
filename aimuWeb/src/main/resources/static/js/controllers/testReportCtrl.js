define(["angular"], function(angular) {
	return function($scope, $modalInstance, patient, test) {
		$scope.testResult = test.result;

		$scope.patientName = patient.name;
		var date = new Date(patient.birthdate);
		//$scope.birthdate = date.customFormat( "#YYYY/#MMM#/#DD#);
		$scope.birthdate = patient.birthdate;
		$scope.gender = patient.gender;
		$scope.testId = test.testId;
		$scope.patient = test.patientId;
		var beijing = test.testDate + (3600000 * 8);
		$scope.testDate = new Date(beijing);
		$scope.testDeviceId = test.testDeviceId;
 

		function endsWith(str, c) {
			return str.lastIndexOf(c) == (str.length-c.length);
		}
		
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
			    	
			    	if (endsWith(dbValue, "?")) {
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
		    	if (endsWith(db, "+")||endsWith(db, "-")) {
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
	}
});