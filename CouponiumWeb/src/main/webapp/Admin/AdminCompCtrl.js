(function () {
    var module = angular.module("Unicorn")
    module.controller('AdmCompCtr', ['$scope', 'AdminCompSrvc', AdmCompCtor])

    function AdmCompCtor($scope, AdminCompSrvc) {
        self = this
        self.EditIsOn = false
        self.creationMode = false
        self.deleteMode = false
        self.CompaniesToEdit = []
        self.selectedComp = {}
        self.yesMessage = undefined
        self.noMessage = undefined
        self.yesMes = undefined
        self.noMes = undefined
        self.newCompany = {}
        self.zevelId = 0


        // Here is a getAll implementation. 
        AdminCompSrvc.getCompanies().then(function (data) {
            self.CompaniesToEdit = data
        })
        // This here is get info of a particular company. Later i will do sort by or search for id implementation
        self.selectIt = function (index) {
            this.selectedComp = this.CompaniesToEdit[index]
        }

        //  update implementation. i did it by allowing  editing "on the go"
        // Id is unchangeable by purpose and the name is forbidden to change by the project.
        // It also shows a nice alert when it is done

        self.startEdit = function () {
            self.EditIsOn = !self.EditIsOn
        }

        self.updateStart = function () {
            self.startEdit()
            var updt = this.selectedComp
            AdminCompSrvc.saveUpdate(updt)
                .then(function () {
                    self.yesMessage = "Update Successesful"
                },
                function (error) {
                    self.noMessage = "No Update For You Mark"
                })
        }

        // this is a createCompany implementation. 
        // I use creationMode flag to hide this part in html, when it is not needed or used.

        self.creatiOn = function () {
            self.creationMode = !self.creationMode
        }

        self.startNew = function () {
            self.creatiOn()
            var compdata = self.newCompany
            AdminCompSrvc.createNewComp(compdata)
                .then(function (response) {
                    self.yesMes = "Greetings, new Company has been created"
                },
                function (error) {
                    self.noMes = "Creation error"
                }
                )
        }

        // this is a delete implementation. also a get company by id
        self.DeleteIt = function () {
            self.deleteMode = !self.deleteMode
        }
        self.showDelete = function () {
            AdminCompSrvc.getCompany(self.zevelId)
                .then(function (response) {
                    self.selectedComp = response
                },
                function (err) {
                    console.log(err)
                }
                )
        }

        self.Burn = function () {
            self.DeleteIt()
            AdminCompSrvc.deleteComp(self.zevelId)
                .then(function (responce) {
                    self.purgedMes = "Company deleted"
                },
                function (error) {
                    self.notPurged = "Delete Unsuccessesful"
                }
                )
        }
    }


})();
