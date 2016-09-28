define(["angular"], function(angular) {
    function Http($http, $q) {
        var API_ADDRESS = "http://121.40.177.67:8190";
        API_ADDRESS = "restapi/";
        // var API_ADDRESS = "http://outer.api/api.php";
        var HOST_ADDRESS = "http://121.40.177.67:8190";

        function request(method, url, data){
            url += (url.indexOf("?")!==-1 ? "&apiKey=rock2016" : "?apiKey=rock2016");
            return $http({
                method: method,
                headers: {'Content-Type': 'application/json'},
                url: API_ADDRESS + url,
                data: data
            }).then(function(res) {
                return $q.when(res.data)
            }, function(res) {
                return $q.reject(res.data)
            }, function() {
                return false;
            })
        }

        function proxy(method, url, data){
            url += (url.indexOf("?")!==-1 ? "&apiKey=rock2016" : "?apiKey=rock2016");
            return $http({
                method: method,
                headers: {'Content-Type': 'application/json'},
                url: API_ADDRESS+'?mode=native&url='+encodeURIComponent(HOST_ADDRESS+url),
                data: data
            }).then(function(res) {
                return $q.when(res.data)
            }, function(res) {
                return $q.reject(res.data)
            }, function() {
                return false;
            })
        }

        this.post = function(url, data) {
            return request("POST", url, data);
        }
        
        this.get = function(url,data) {
            if(data){
                var paramsArray = [];
                //Object.keys(data).forEach(key => paramsArray.push(key + '=' + encodeURIComponent(data[key])))
                
                Object.keys(data).forEach(function (key) {
                	paramsArray.push(key + '=' + encodeURIComponent(data[key]))
                });
                
                if (url.search(/\?/) === -1) {
                    url += '?' + paramsArray.join('&')
                } else {
                    url += '&' + paramsArray.join('&')
                }
            }
           return request("GET", url);
        }

        this.delete = function(url) {
            return request("DELETE", url);
        }

        

    }
    return Http;
});
