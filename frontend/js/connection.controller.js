angular.module('mysqldumper').controller('ConnectionController', function ($scope, Settings, $mdDialog, connection) {
    $scope.connection = angular.copy(connection);

    $scope.save = function() {
        if ($scope.connectionForm.$valid) {
            $mdDialog.hide($scope.connection);
        }
    };
});