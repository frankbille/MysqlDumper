MysqlDumper [![Build Status](https://travis-ci.org/frankbille/MysqlDumper.svg?branch=master)](https://travis-ci.org/frankbille/MysqlDumper)
===========

GUI tool for dumping a Mysql database with possibility to exclude data from
specific tables with their dependencies.


Features
--------

Not checked is planned!

- [x] Select which table structures and/or data to include
- [x] Add table dependencies so, deselecting a table also deselects tables that depends
      on it.
- [x] Show the commands to use for dumping based on table configuration
- [x] Connection management
- [x] Configurable mysql client version
- [ ] Actually dump the database
- [ ] Select if dump to file or another database


Download
--------

Alpha builds can be [downloaded here][download].


Development
-----------

Feel free to create pull request with fixes/improvements.

### Releasing

I currently use [Release It][releaseit].


License
-------

Released under GPLv3



[download]: https://github.com/frankbille/MysqlDumper/releases
[releaseit]: http://webpro.github.io/release-it/