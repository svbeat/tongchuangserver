define(["angular"], function(angular) {
    function API($http, $q, $location, $cookies, Http) {

        // doctor
        this.getAllDoctors = function(data){
            return Http.get("/doctors",data);
        }

        this.getDoctor = function( id ) {
            return Http.get("/doctors/" + id)
        }

        this.addDoctor = function(data){
            return Http.post("/doctors",data)
        }

        this.delDoctor = function(id) {
            return Http.delete("/doctors/" + id);
        }

        this.modDoctor = function(id, data) {
            return Http.post('/doctors/' + id, data)
        }

        this.getPatientsOfDoctor = function(id) {
            return Http.get("/doctors/" + id + "/patients");
        }

        // patient
        this.getAllPatients = function(data){
            return Http.get("/patients",data);
        }

        this.getPatient = function( id ) {
            return Http.get("/patients/" + id)
        }

        this.addPatient = function(data){
            return Http.post("/patients",data)
        }

        this.delPatient = function(id) {
            return Http.delete("/patients/" + id);
        }

        this.modPatient = function(id, data) {
            return Http.post('/patients/' + id, data)
        }

        this.getDoctorsOfPatient = function(id) {
            return Http.get("/patients/" + id + "/doctors");
        }
        // tests
        this.getAllTestsOfPatint = function(id, data) {
            return Http.get("/patients/" + id + "/tests", data);
        }
        
        this.getTestsTimeline = function(id) {
        	return Http.get("/patients/" + id + "/tests/timeline");
        }
        
        this.delTest = function(id) {
        	return Http.delete("/perimetrytests/" + id);
        }
        
        // patientSettings
        this.getPatientSettings = function(id) {
        	return Http.get("/patients/" + id + "/examsettings?examCode=PERIMETRY");
        }
        this.resetPatientSettings = function(id) {
        	return Http.post("/patients/" + id + "/examsettings/default?examCode=PERIMETRY");
        }
        
        this.savePatientSettings = function(id, settings) {
        	return Http.post("/patients/" + id + "/examsettings?examCode=PERIMETRY", settings)
        }
        

        // device
        this.getAllDevices = function(data){
            return Http.get("/devices",data);
        }

        this.getDevice = function( id ) {
            return Http.get("/devices/" + id)
        }

        this.addDevice = function( data ){
            return Http.post("/devices" , data)
        }

        this.delDevice = function(id){
            return Http.delete("/devices/" + id);
        }

        this.modDevice = function(id, data){
            return Http.post("/devices/" + id, data);
        }

        this.getDeviceParam = function(id){
            return Http.get('/devices/' + id + "/examsettings");
        }

        // login
        this.login = function(data){
            return Http.post('/login', data);
        }

        // user
        this.getUser = function(id){
            return Http.get('/patients/'+id+"/examsettings");
        }

        this.getUserRoles = function(username) {
            return Http.get("/userroles?username=" + username);
        }

        // relationships
        this.addRelationShips = function(data){
            return Http.post("/relationships", data)
        }

        this.delRelationShips = function(id){
            return Http.delete("/relationships/" + id);
        }

        // hospital
        this.getAllHospitals = function(){
            return Http.get("/hospitals");
        }

        //cookie
        this.setCookies = function(k, v, e) {
            // $cookies.put(k, v, { expires: e })
            var expires = "expires="+e;
            document.cookie = k + "=" + v + "; " + expires;
        }

        this.getCookies = function(k) {
            // return $cookies.get(k)
            var name = k + "=";
            var ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0)==' ') c = c.substring(1);
                if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
            }
            return "";
        }

        this.clearCookies = function(k) {
            this.setCookies(k, "", -1);
        }
        
        this.fetchNewsShort = function() {
        	return Http.get("/news/short");
        }

        this.fetchNewsLong = function() {
        	return Http.get("/news/long");
        }
    }
    return API
});
