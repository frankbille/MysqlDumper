const spawn = require('child_process').spawn;
const Promise = require('promise');
const parseString = require('xml2js').parseString;

module.exports = {
    query: function(sql, connection) {
        return new Promise(function(resolve, reject) {
            global.settings.get('mysqlbindir', function(error, mysqlBinDir) {
                var args = [];
                args.push('-h');
                args.push(connection.host);
                args.push('-P');
                args.push(connection.port);
                args.push('-u');
                args.push(connection.username);
                if (connection.password) {
                    args.push('-p');
                    args.push(connection.password);
                }
                // Output as XML
                args.push('-X');
                args.push(connection.database);

                var mysql = spawn(mysqlBinDir+'/mysql', args);
                mysql.stdin.write(sql);
                mysql.stdin.end();

                var xml = '';
                mysql.stdout.on('data', (data) => {
                    xml += data;
                });

                mysql.stderr.on('data', (err) => {
                    console.error(err);
                });

                mysql.on('close', (code) => {
                    if (code == 0) {
                        parseString(xml, function (err, parsedXml) {
                            if (err) {
                                reject(err);
                            } else {
                                var result = [];

                                var rows = parsedXml.resultset.row;
                                rows.forEach(function(row) {
                                    var resultRow = {};
                                    result.push(resultRow);

                                    var fields = row.field;
                                    fields.forEach(function(field) {
                                        resultRow[field.$.name] = field._;
                                    });
                                });

                                resolve(result);
                            }
                        });
                    } else {
                        reject(code);
                    }
                });
            });

        });
    }
};