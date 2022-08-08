/**
 * Created by dmihaylov on 02.05.2017.
 * Классы для работы с деревьями
 */
"use strict";
var ModelTree = (function () {
    function ModelTree() {
    }
    return ModelTree;
}());
exports.ModelTree = ModelTree;
var ModelTreeFile = (function () {
    function ModelTreeFile() {
        this.uncertainties = {
            header: "",
            name: ""
        };
        this.tree = new DataTreeFile();
    }
    return ModelTreeFile;
}());
exports.ModelTreeFile = ModelTreeFile;
var DataTree = (function () {
    function DataTree() {
    }
    return DataTree;
}());
var DataTreeFile = (function () {
    function DataTreeFile() {
    }
    return DataTreeFile;
}());
var DataFileUpload = (function () {
    function DataFileUpload() {
    }
    return DataFileUpload;
}());
//# sourceMappingURL=modelTree.js.map