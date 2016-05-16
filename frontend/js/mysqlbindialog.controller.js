angular.module('mysqldumper').controller('MysqlBinDirController', function ($scope, Settings, $mdDialog) {
    Settings.get('mysqlbindir').then(function (mysqlBinDir) {
        $scope.mysqlBinDir = mysqlBinDir;
    });

    $scope.test = function () {

    };

    $scope.save = function () {
        if ($scope.mysqlBinDirForm.$valid) {
            Settings.set('mysqlbindir', $scope.mysqlBinDir).then($mdDialog.hide);
        }
    };
});