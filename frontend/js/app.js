const remote = require('electron').remote;
var settings = remote.getGlobal('settings');
var mysqlClient = remote.getGlobal('mysqlclient');

var app = angular.module('mysqldumper', ['ngMaterial', 'ngMessages', 'md.data.table']);

app.config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
        .primaryPalette('blue')
        .accentPalette('pink');
});

app.filter('bytes', function () {
    return function (bytes, precision) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
        if (bytes === 0) return '0 bytes';
        if (typeof precision === 'undefined') precision = 1;
        var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
            number = Math.floor(Math.log(bytes) / Math.log(1024));
        return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number];
    }
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
                .textContent('Are you sure you want to remove the connection with the name "' + connection.name + '"')
                .targetEvent(event)
                .ok('Remove')
                .cancel('Cancel');

            return $mdDialog.show(confirm);
        },

        showDumpDetails: function(event, dumpConfiguration) {
            return $mdDialog.show({
                controller: 'DumpDetailsController',
                templateUrl: 'dumpdetails.html',
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                targetEvent: event,
                locals: {
                    dumpConfiguration
                }
            });
        }
    }
});

app.factory('MysqlClient', function () {
    return {
        query: function (sql, connection) {
            return mysqlClient.query(sql, connection);
        }
    }
});

app.factory('Dumper', function (MysqlClient, $q) {
    return {
        prepareDump: function (connection) {
            var deferred = $q.defer();

            var sql = 'SELECT\n' +
                '  TABLES.TABLE_NAME AS table_name,\n' +
                '  REFERENTIAL_CONSTRAINTS.TABLE_NAME AS dependent_table_name,\n' +
                '  (TABLES.DATA_LENGTH + TABLES.INDEX_LENGTH) AS table_size\n' +
                'FROM\n' +
                '  information_schema.TABLES\n' +
                '  LEFT JOIN\n' +
                '  information_schema.REFERENTIAL_CONSTRAINTS ON information_schema.TABLES.TABLE_SCHEMA = information_schema.REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA AND information_schema.TABLES.TABLE_NAME = information_schema.REFERENTIAL_CONSTRAINTS.REFERENCED_TABLE_NAME\n' +
                'WHERE\n' +
                '  TABLES.TABLE_SCHEMA = "' + connection.database + '"\n' +
                'ORDER BY\n' +
                '  TABLES.TABLE_NAME';

            MysqlClient.query(sql, connection).then(function (result) {
                var dumpConfiguration = new DumpConfiguration(connection, result);
                deferred.resolve(dumpConfiguration);
            }, deferred.reject);

            return deferred.promise;
        }
    };
});
