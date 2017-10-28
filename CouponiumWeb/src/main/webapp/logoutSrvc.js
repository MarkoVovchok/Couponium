(function(){
var module = angular.module("Unicorn")
module.service("LogoutSrvc",LogoutSrvcCtor)

function LogoutSrvcCtor($http){
    self=this
    self.logOut = function(){
        $http.post('http://localhost:8080/couponiumWeb/logout')
    }
}
})();