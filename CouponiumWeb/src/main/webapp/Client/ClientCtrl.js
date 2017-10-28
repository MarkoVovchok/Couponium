(function () {
    var module = angular.module("Unicorn")
    module.controller("ClientCtrl", ['$scope', 'ClientoSrvc', ClientCtrlCtor])

    function ClientCtrlCtor($scope, ClientoSrvc) {
        self = this
        self.AvailableCoupons = []
        self.PurchasedCoupons = []
        self.selectedCoup = {}
        self.buyMode = false

        // this shows available to purchase 
        ClientoSrvc.getCoupons().then(function (data) {
            self.AvailableCoupons = data
        })
        // this shows already owned by this client. Controlled through sorting Ctrl
        // ClientoSrvc.getPurchased()
        //     .then(function (data) {
        //         self.PurchasedCoupons = data
        //     })


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

    }
})();