(function () {
    var module = angular.module("Unicorn")
    module.service("CompanySrvc", CompanySrvcCtor)

    function CompanySrvcCtor($http) {

        self = this

        //This is getAll implementation , i would like to show all the Company's coupons on the web-page
        self.viewCollection = function (){
            promise = $http.get('http://localhost:8080/couponiumWeb/webapi/Company/getAllCoupons')
            coups = promise.then(function (response) {
                return response.data
            },
                function (error) {
                    console.log(error)
                })
            return coups;
        }

        // Update Coupon
        self.saveUpdate = function (data) {
            promise = $http.put("http://localhost:8080/couponiumWeb/webapi/company/updateCoupon", data)
            return promise.then(function (response) {

            })
        }
        // Creation
        self.createNewCoup = function (data) {
            promise = $http.post("http://localhost:8080/couponiumWeb/webapi/company/newCoupon", data)
            return promise.then(function (resp) {
            })
        }
        // Delete
        self.deleteCoup = function (id) {
            promise = $http.delete("http://localhost:8080/couponiumWeb/webapi/company/removeCoupon/" + id)
            return promise.then(function () {
            }
            )
        }
        // Get Coupon by id
        self.getCoupon = function (id){
            promise = $http.get("http://localhost:8080/couponiumWeb/webapi/company/getCoupon/"+id)
            obj =  promise.then(function(resp){
                return resp.data
            })
            return obj; 
        }
    }
})();