var module = angular.module("Unicorn")
module.config(['$locationProvider', function ($locationProvider) {
        $locationProvider.hashPrefix('');
    }])
module.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('AdmCust', {
                url: "/admCust",
                templateUrl: "AdminCustomer.html",
                controller: "AdminCustCtrl as cust"
            })
            .state('AdmComp',{
                url:"/admComp",
                templateUrl:"AdminCompany.html",
                controller:"AdmCompCtr as adm"
            })
    }]);
