angular.module('mysqldumper').controller('DumpDetailsController', function ($scope, dumpConfiguration, $mdDialog) {
    $scope.dumpCommands = dumpConfiguration.createDumpCommands(true);

    $scope.close = $mdDialog.cancel;
});