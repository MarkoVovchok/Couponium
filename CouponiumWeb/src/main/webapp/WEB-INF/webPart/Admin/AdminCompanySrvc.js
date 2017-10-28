(function () {
    var module = angular.module("Unicorn")
    module.service("AdminCompSrvc", AdminCompSrvcCtor)

    function AdminCompSrvcCtor($http) {
        self = this


        self.getCompanies = function () {
            promise = $http.get('http://localhost:8080/couponiumWeb/webapi/admin/viewAllCompanies')
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
            promise = $http.put("http://localhost:8080/couponiumWeb/webapi/admin/updateCompany", data)
            return promise.then(function (response) {

            })
        }

        self.createNewComp = function (compdata) {
            promise = $http.post("http://localhost:8080/couponiumWeb/webapi/admin/newCompany", compdata)
            return promise.then(function (resp) {
            })
        }

        self.deleteComp = function (id) {
            promise = $http.delete("http://localhost:8080/couponiumWeb/webapi/admin/deleteCompany/" + id)
            return promise.then(function () {

            }
            )
        }

        self.getCompany = function (id){
            promise = $http.get("http://localhost:8080/couponiumWeb/webapi/admin/getCompany/"+id)
            obj =  promise.then(function(resp){
                return resp.data
            })
            return obj; 
       
        }

    }
})();