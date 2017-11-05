(function () {
    var module = angular.module("Unicorn")
    module.controller("ClientCtrl", ['$scope', 'ClientoSrvc', 'LogoutSrvc', 'orderByFilter', ClientCtrlCtor])

    function ClientCtrlCtor($scope, ClientoSrvc, LogoutSrvc, orderBy) {
        self = this
        self.AvailableCoupons = []
        self.PurchasedCoupons = []
        self.selectedCoup = {}
        self.buyMode = false

        // this shows available to purchase 
        ClientoSrvc.getCoupons().then(function (data) {
            self.AvailableCoupons = data
        })

        // this shows already owned by this client
        ClientoSrvc.getPurchased()
            .then(function (data) {
                self.PurchasedCoupons = data
            })


        //  Purchase logic

        self.selectIt = function (index) {
            self.selectedCoup = self.AvailableCoupons[index]
        }

        self.toggleBuy = function () {
            self.buyMode = !self.buyMode
        }


        self.buy = function () {
            self.toggleBuy()
            var purch = self.selectedCoup
            ClientoSrvc.purchaseIt(purch)
                .then(function (resp) {
                    self.purchSucc = "Purchased!"
                },
                function (err) {
                    self.noPurch = "Coupon is not available"
                }
                )
        }
        // coupons sorting 
        $scope.propertyName = 'price';
        $scope.reverse = true;
        $scope.coupons = orderBy(self.PurchasedCoupons, $scope.propertyName, $scope.reverse);

        $scope.sortBy = function (propertyName) {
            $scope.reverse = (propertyName !== null && $scope.propertyName === propertyName)
                ? !$scope.reverse : false;
            $scope.propertyName = propertyName;
            $scope.coupons = orderBy(self.PurchasedCoupons, $scope.propertyName, $scope.reverse);
        };
        
        // logout
        self.logOut = function () {
            LogoutSrvc.logOut();
        }


    }
})();