(function () {
    var module = angular.module("Unicorn")

    module.controller('CompanyCtrl', ['$scope', 'CompanySrvc', CompanyCtrlCtor])

    function CompanyCtrlCtor($scope, CompanySrvc) {

        self = this
        self.Collection = []
        self.EditMode = false
        self.CreationMode = false
        self.DeleteMode = false
        self.selectedCoup = {}
        self.yesMessage = undefined
        self.noMessage = undefined
        self.yesMes = undefined
        self.noMes = undefined
        self.newCoup = {}
        self.zevelId = 0


        // here are all of the current company's coupons, which are displayed on a page load 
        CompanySrvc.viewCollection()
            .then(function (data) {
                self.Collection = data
            })

        //  This is an update option. Id is unchangeable.
        //  Display an alert when it is done
        self.startEdit = function () {
            self.EditMode = !self.EditMode
        }

        self.selectIt = function (index) {
            this.selectedCoup = this.Collection[index]
        }

        self.updateGo = function () {
            self.startEdit()
            var updt = this.selectedCoup
            CompanySrvc.saveUpdate(updt)
                .then(function (response) {
                    self.yesMessage = "Update Successesful"
                },
                function (error) {
                    self.noMessage = "Update error, your PC will self-destruct"
                })
        }

        // This will add a new coupon
        // Also displays an alert when done

        self.creatiOn = function () {
            self.CreationMode = !self.CreationMode
        }

        self.startNew = function () {
            self.creatiOn()
            var coupdata = self.newCoup
            CompanySrvc.createNewCoup(coupdata)
                .then(function (response) {
                    self.yesMes = "Coupon Issued and is now available"
                },
                function (error) {
                    self.noMes = "Creation error"
                }
                )
        }

        //Delete implementation
        self.DeleteIt = function () {
            self.DeleteMode = !self.DeleteMode
        }

        self.Rekt = function () {
            self.DeleteIt()
            CompanySrvc.deleteCoup(self.zevelId)
                .then(function (responce) {
                    self.purgedMes = "Coupon is no more"
                },
                function (error) {
                    self.notPurged = "Delete Unsuccessesful"
                }
                )
        }

    }
})();