angular.module('mysqldumper').controller('MenuController', function ($scope, Dialogs, $mdSidenav) {

    $scope.toggleMenu = function() {
        $mdSidenav('left').toggle();
    };

    $scope.showSettings = function (event) {
        Dialogs.showSettings(event);
    };

});