//  this is a sort example

(function(){
var app = angular.module("Unicorn")
app.controller("Sorter",['$scope', 'orderByFilter','ClientoSrvc',SorterCtor])

function SorterCtor($scope,orderBy,ClientoSrvc){
    self=this
    self.purchased = []

    ClientoSrvc.getPurchased().then(function(data){
         purchased = data
    })

    $scope.propertyName = 'price';
    $scope.reverse = true;


    $scope.sortBy = function(propertyName) {
      $scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
      $scope.propertyName = propertyName;
    };
}
})();