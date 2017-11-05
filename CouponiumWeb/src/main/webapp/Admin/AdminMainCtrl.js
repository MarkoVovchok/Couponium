var module = angular.module("Unicorn")

module.controller("AdmMainCtrl",['$scope','LogoutSrvc',AdmMainCtor])

function AdmMainCtor($scope,LogoutSrvc){


    self=this

    self.logOut = function(){
        LogoutSrvc.logOut();
    }


}