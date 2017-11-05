(function(){
    var module = angular.module("Unicorn")
    module.service("ClientoSrvc", ClientSrvcCtor)

    function ClientSrvcCtor($http) {
        self= this
        // this will show all available Coupons to a customer to buy
        self.getCoupons = function() {
            promise = $http.get('http://localhost:8080/couponiumWeb/webapi/customer/showAllAvailable')
            comps = promise.then(
                function (response) {
                    return response.data
                },
                function (error) {
                    console.log(error)
                });
            return comps;
        }


        // here are all the coupons that are currently purchased by customer
        self.getPurchased = function () {
            promise = $http.get('http://localhost:8080/couponiumWeb/webapi/customer/getAll')
            coupons = promise.then(
                function (response) {
                    return response.data
                },
                function (err) {
                    console.log(err)
                });
            return coupons;
        }

        // this is a purchase implmt
        self.purchaseIt = function (coupi) {
            promise = $http.post('http://localhost:8080/couponiumWeb/webapi/customer/buy', coupi)
            return promise.then(function (resp) { },
                function (err) {
                    console.log(err)
                }
            )
        }

        



    }
})();