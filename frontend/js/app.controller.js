angular.module('mysqldumper').controller('AppController', function ($scope, Dialogs, Settings, MysqlClient) {

    Settings.get('mysqlbindir').then(function (mysqlBinDir) {
        if (mysqlBinDir == null) {
            Dialogs.showSettings();
        }
    });

    $scope.$on('connection-selection-changed', function (event, connection) {
        $scope.selectedConnection = connection;
        $scope.loadingConnection = true;

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
            $scope.loadingConnection = false;
            console.log(result);
        });
    });


});