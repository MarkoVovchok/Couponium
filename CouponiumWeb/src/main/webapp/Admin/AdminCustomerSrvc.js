// this is a complete clone of Company Srvc, but changed for customer operations

(function () {
    var module = angular.module("Unicorn")
    module.service("AdminCustSrvc", AdminCustSrvcCtor)

    function AdminCustSrvcCtor($http) {
        self = this
        

        self.getCustomers = function () {
            promise = $http.get('http://localhost:8080/couponiumWeb/webapi/admin/viewAllCustomers')
            comps = promise.then(
                function (response) {
                    return response.data
                },
                function (error) {
                    console.log(error)
                });
            return comps;
        }

        self.saveUpdate = function (data) {
            promise = $http.put("http://localhost:8080/couponiumWeb/webapi/admin/updateCustomer", data)
            return promise.then(function (response) {

            })
        }

        self.createNewCust = function (custdata) {
            promise = $http.post("http://localhost:8080/couponiumWeb/webapi/admin/newCustomer",custdata)
            return promise.then(function (resp) {
            })
        }

        // In my api i did delete function with an object as a parameter, so i had to make a delete implementation with a POST method
        self.deleteCust = function (custdata) {
            promise = $http.post("http://localhost:8080/couponiumWeb/webapi/admin/removeCustomer",custdata)
            return promise.then(function () {

            }
            )
        }

        self.getCustomer = function (id){
            promise = $http.get("http://localhost:8080/couponiumWeb/webapi/admin/getCustomer/"+id)
            obj =  promise.then(function(resp){
                return resp.data
            })
            return obj; 
       
        }

    }
})();