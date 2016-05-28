class DumpConfiguration {
    constructor(connection, result) {
        this.connection = connection;
        this.tables = [];

        var tableCache = {};
        var dependentTableCache = {};

        var self = this;
        result.forEach(function (row) {
            var tableName = row['table_name'];
            var tableSize = row['table_size'];
            var dependentTableName = row['dependent_table_name'];

            var table = tableCache[tableName];
            if (!table) {
                table = new Table(tableName, tableSize);
                tableCache[table.name] = table;
                self.tables.push(table);
            }

            if (!dependentTableCache[table.name]) {
                dependentTableCache[table.name] = [];
            }
            if (dependentTableName) {
                dependentTableCache[table.name].push(dependentTableName);
            }
        });

        angular.forEach(dependentTableCache, function (dependentTables, tableName) {
            var table = tableCache[tableName];

            dependentTables.forEach(function (dependentTableName) {
                var dependentTable = tableCache[dependentTableName];
                table.addDependentTable(dependentTable);
            });
        });
    }

    get includeAllStructure() {
        var includeAll = true;

        this.tables.forEach(function (table) {
            if (!table.includeStructure) {
                includeAll = false;
            }
        });

        return includeAll;
    }

    set includeAllStructure(includeAllStructure) {
        this.tables.forEach(function (table) {
            table.setIncludeStructureNoCascade(includeAllStructure);
        });
    }

    get includeAllData() {
        var includeAll = true;

        this.tables.forEach(function (table) {
            if (!table.includeData) {
                includeAll = false;
            }
        });

        return includeAll;
    }

    set includeAllData(includeAllData) {
        this.tables.forEach(function (table) {
            table.setIncludeDataNoCascade(includeAllData);
        });
    }

    get totalSize() {
        var total = 0;

        this.tables.forEach(function (table) {
            total += table.ownSize;
        });

        return total;
    }

    get selectedSize() {
        var selected = 0;

        this.tables.forEach(function (table) {
            if (table.includeData) {
                selected += table.ownSize;
            }
        });

        return selected;
    }

    createDumpCommands(secure) {
        var cmds = [];

        var includeOnlyStructureTables = [];
        var includeDataTables = [];
        this.tables.forEach(function (table) {
            if (table.includeStructure && !table.includeData) {
                includeOnlyStructureTables.push(table.name);
            }
            if (table.includeData) {
                includeDataTables.push(table.name);
            }
        });

        if (includeOnlyStructureTables.length > 0) {
            var cmd = "mysqldump";

            cmd += ' -h ' + this.connection.host;
            cmd += ' -P ' + this.connection.port;
            cmd += ' -u ' + this.connection.username;
            if (this.connection.password) {
                cmd += ' -p ' + (secure ? '********' : this.connection.password);
            }
            cmd += ' --single-transaction ';
            cmd += ' --no-data ';
            cmd += ' ' + this.connection.database;
            includeOnlyStructureTables.forEach(function (table) {
                cmd += ' ' + table;
            });
            cmds.push(cmd);
        }

        if (includeDataTables.length > 0) {
            cmd = "mysqldump";

            cmd += ' -h ' + this.connection.host;
            cmd += ' -P ' + this.connection.port;
            cmd += ' -u ' + this.connection.username;
            if (this.connection.password) {
                cmd += ' -p ' + (secure ? '********' : this.connection.password);
            }
            cmd += ' --single-transaction ';
            cmd += ' ' + this.connection.database;
            includeDataTables.forEach(function (table) {
                cmd += ' ' + table;
            });
            cmds.push(cmd);
        }

        return cmds;
    }

}