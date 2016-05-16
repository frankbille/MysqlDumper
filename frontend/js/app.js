const remote = require('electron').remote;
var settings = remote.getGlobal('settings');
var mysqlClient = remote.getGlobal('mysqlclient');

var app = angular.module('mysqldumper', ['ngMaterial', 'ngMessages']);

app.config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
        .primaryPalette('blue')
        .accentPalette('pink');
});

app.factory('Settings', function ($q) {
    return {
        get: function (key) {
            var deferred = $q.defer();
            settings.has(key, function (error, hasKey) {
                if (error) {
                    deferred.reject(error);
                } else if (!hasKey) {
                    deferred.resolve(null);
                } else {
                    settings.get(key, function (error, value) {
                        if (error) {
                            deferred.reject(error);
                        } else {
                            deferred.resolve(value);
                        }
                    });
                }
            });
            return deferred.promise;
        },

        set: function (key, value) {
            var deferred = $q.defer();
            settings.set(key, value, function (error) {
                if (error) {
                    deferred.reject(error);
                } else {
                    deferred.resolve();
                }
            });
            return deferred.promise;
        },

        remove: function (key) {
            var deferred = $q.defer();
            settings.remove(key, function (error) {
                if (error) {
                    deferred.reject(error);
                } else {
                    deferred.resolve();
                }
            });
            return deferred.promise;
        }
    }
});


app.factory('Dialogs', function ($mdDialog) {
    return {
        showSettings: function (event) {
            return $mdDialog.show({
                controller: 'MysqlBinDirController',
                templateUrl: 'mysqlbindialog.html',
                parent: angular.element(document.body),
                targetEvent: event
            });
        },

        showConnection: function (event, connection) {
            return $mdDialog.show({
                controller: 'ConnectionController',
                templateUrl: 'connection.html',
                parent: angular.element(document.body),
                targetEvent: event,
                locals: {
                    connection: connection
                }
            });
        },

        confirmRemoveConnection: function (event, connection) {
           var confirm = $mdDialog.confirm()
               .title('Remove connection?')
               .textContent('Are you sure you want to remove the connection with the name "'+connection.name+'"')
               .targetEvent(event)
               .ok('Remove')
               .cancel('Cancel');

            return $mdDialog.show(confirm);
        }
    }
});

app.factory('MysqlClient', function() {
    return {
        query: function(sql, connection) {
            return mysqlClient.query(sql, connection);
        }
    }
});

