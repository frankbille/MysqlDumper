angular.module('mysqldumper').controller('AppController', function ($scope, Dialogs, Settings) {

    // Settings.remove('mysqlbindir');

    Settings.get('mysqlbindir').then(function (mysqlBinDir) {
        if (mysqlBinDir == null) {
            Dialogs.showSettings();
        }
    });

});