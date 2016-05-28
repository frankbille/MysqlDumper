angular.module('mysqldumper').controller('AppController', function ($scope, Dialogs, Settings, Dumper) {

    Settings.get('mysqlbindir').then(function (mysqlBinDir) {
        if (mysqlBinDir == null) {
            Dialogs.showSettings();
        }
    });

    $scope.$on('connection-selection-changed', function (event, connection) {
        setConnection(connection);
    });

    $scope.refresh = function(connection) {
        setConnection(connection);
    };

    $scope.showDetails = function(event) {
        Dialogs.showDumpDetails(event, $scope.dumpConfiguration);
    };

    var setConnection = function(connection) {
        $scope.selectedConnection = connection;

        $scope.dumpConfiguration = Dumper.prepareDump(connection).then(function(dumpConfiguration) {
            $scope.dumpConfiguration = dumpConfiguration;
        });
    };

});