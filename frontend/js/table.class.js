class Table {
    constructor(name, ownSize) {
        this.name = name;
        this.ownSize = parseInt(ownSize, 10);
        this._includeStructure = true;
        this._includeData = true;
        this.dependentTables = [];
    }

    addDependentTable(dependentTable) {
        this.dependentTables.push(dependentTable);
    }

    get includeData() {
        return this._includeData;
    }

    set includeData(includeData) {
        this._setIncludeData([], includeData);
    }

    _setIncludeData(visitedTables, includeData) {
        this.setIncludeDataNoCascade(includeData);

        visitedTables.push(this);

        this.dependentTables.forEach(function(dependentTable) {
            if (visitedTables.indexOf(dependentTable) === -1) {
                dependentTable._setIncludeData(visitedTables, includeData);
            }
        });
    }

    setIncludeDataNoCascade(includeData) {
        this._includeData = includeData;

        if (includeData) {
            this.setIncludeStructureNoCascade(true);
        }
    }

    get includeStructure() {
        return this._includeStructure;
    }

    set includeStructure(includeStructure) {
        this._setIncludeStructure([], includeStructure);
    }

    _setIncludeStructure(visitedTables, includeStructure) {
        this.setIncludeStructureNoCascade(includeStructure);

        visitedTables.push(this);

        this.dependentTables.forEach(function(dependentTable) {
            if (visitedTables.indexOf(dependentTable) === -1) {
                dependentTable._setIncludeStructure(visitedTables, includeStructure);
            }
        });
    }

    setIncludeStructureNoCascade(includeStructure) {
        this._includeStructure = includeStructure;

        if (!includeStructure) {
            this.setIncludeDataNoCascade(false);
        }
    }

    getTreeSize() {
        return this._treeSize([]);
    }

    _treeSize(visitedTables) {
        visitedTables.push(this);

        var treeSize = this.ownSize;
        this.dependentTables.forEach(function(dependentTable) {
            if (visitedTables.indexOf(dependentTable) === -1) {
                treeSize += dependentTable._treeSize(visitedTables);
            }
        });
        return treeSize;
    }
}