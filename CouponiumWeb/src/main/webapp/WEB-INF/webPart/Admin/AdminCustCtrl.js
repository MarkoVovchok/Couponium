(function () {
    var module = angular.module("Unicorn")
    module.controller('AdminCustCtrl', ['$scope', 'AdminCustSrvc', AdmCustCtor])

    function AdmCustCtor($scope, AdminCustSrvc) {
        self = this
        self.EditIsOn = false
        self.creationMode = false
        self.deleteMode = false
        self.CustomersToShow = []
        self.selectedCust = {}
        self.yesMessage = undefined
        self.noMessage = undefined
        self.yesMes = undefined
        self.noMes = undefined
        self.newCust = {}
        self.zevelId = 0

        // Here is a getAll implementation. 
        AdminCustSrvc.getCustomers().then(function (data) {
            self.CustomersToShow = data
        })
        // This here is get info of a particular customer.
        self.selectIt = function (index) {
            this.selectedCust = this.CustomersToShow[index]
        }

        // Update implementation. I did it by allowing  editing "on the go"
        // Id is unchangeable by purpose and the name is forbidden to change by the project.
        // It also shows a nice alert when it is done

        self.startEdit = function () {
            self.EditIsOn = !self.EditIsOn
        }

        self.updateStart = function () {
            self.startEdit()
            var updt = this.selectedCust
            AdminCustSrvc.saveUpdate(updt)
                .then(function () {
                    self.yesMessage = "Update Successesful"
                },
                function (error) {
                    self.noMessage = "No Update For You Mark"
                })
        }

        // this is a createCustomer implementation. 
        // I use creationMode flag to hide this part in html, when it is not needed or used.

        self.creatiOn = function () {
            self.creationMode = !self.creationMode
        }

        self.startNewCusto = function () {
            self.creatiOn()
            var custdata = self.selectedCust
            AdminCustSrvc.createNewCust(custdata)
                .then(function (response) {
                    self.yesMes = "Greetings, new Customer added"
                },
                function (error) {
                    self.noMes = "Creation error"
                }
                )
        }

        // this is a delete implementation. also a get customer by id
        self.DeleteIt = function () {
            self.deleteMode = !self.deleteMode
        }
        self.showDelete = function () {
            AdminCustSrvc.getCustomer(self.zevelId)
                .then(function (response) {
                    self.selectedCust = response
                },
                function (err) {
                    console.log(err)
                }
                )
        }

        self.Burn = function () {
            self.DeleteIt()
            self.showDelete()
            AdminCustSrvc.deleteCust(self.selectedCust)
                .then(function (responce) {
                    self.purgedMes = "Customer deleted"
                },
                function (error) {
                    self.notPurged = "Delete Unsuccessesful"
                }
                )
        }
    }


})();
