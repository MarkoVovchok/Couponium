(function(){
var module = angular.module("Unicorn")
module.service("LogoutSrvc",LogoutSrvcCtor)

function LogoutSrvcCtor($http,$window){
    self=this
    self.logOut = function(){
        promise = $http.post('http://localhost:8080/couponiumWeb/logout')
        promise.then(function(resp){
            $window.location.href = 'http://localhost:8080/couponiumWeb';
        },
    function(err){
        console.log(err)
    })
    }
}
})();