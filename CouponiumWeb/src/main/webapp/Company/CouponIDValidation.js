
// this is a custom directive that will validate id on new Coup, so it won't be doubled in the database

var vald = angular.module('idValidator', [])
vald.directive('idTor', ['CompanySrvc', ValidateIDFunc])

function ValidateIDFunc($http, $q) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attr, ngModel) {

            ngModel.$asyncValidators.notToday = function (modelValue, viewValue) {
                var couponID = viewValue
                var def = $q.defer();
                console.log("directive")

                // here is a getCoupon by id request if it is successesful, so the id is already used
                CompanySrvc.getCoupon(couponID)
                    .then(function (data) {
                        var obj = data
                        console.log(obj)
                    })
                if (obj.length == 0) {
                    def.resolve();
                }
                else {
                    def.reject();
                }

                return def.promise;
            }
        }
    }
}