angular.module('mysqldumper').controller('SideMenuController', function ($scope, $rootScope, $mdSidenav, Settings, Dialogs) {
    Settings.get('connections').then(function(connections) {
        $scope.connections = connections;
    });

    $scope.addConnection = function(event) {
        Dialogs.showConnection(event, {port:3306}).then(function(connection) {
            if ($scope.connections == null) {
                $scope.connections = [];
            }
            $scope.connections.push(connection);
            Settings.set('connections', $scope.connections);
        });
    };

    $scope.editConnection = function(event, connection) {
        Dialogs.showConnection(event, connection).then(function(changedConnection) {
            connection.name = changedConnection.name;
            connection.host = changedConnection.host;
            connection.port = changedConnection.port;
            connection.database = changedConnection.database;
            connection.username = changedConnection.username;
            connection.password = changedConnection.password;
            Settings.set('connections', $scope.connections);
        });
    };

    $scope.removeConnection = function(event, connection) {
        Dialogs.confirmRemoveConnection(event, connection).then(function() {
            var index = $scope.connections.indexOf(connection);
            $scope.connections.splice(index, 1);
            Settings.set('connections', $scope.connections);
        });
    };

    $scope.test = function(connection) {
        $rootScope.$broadcast('connection-selection-changed', connection);

        $mdSidenav('left').close();

    };

});