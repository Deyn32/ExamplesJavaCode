"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
/**
 * Created by dmihaylov on 12.04.2017.
 * Сервис запросов для построения структур деревьев
 */
var http_1 = require('@angular/http');
var core_1 = require('@angular/core');
var createHeaderService_1 = require("../services/createHeaderService");
var requestService_1 = require('../services/requestService');
var util_1 = require('util');
var TreesRequests = (function () {
    function TreesRequests(http, head, req) {
        this.http = http;
        this.head = head;
        this.req = req;
        this.fzDocumentStructureTree = "/directory/fzDocumentStructureTree";
        this.fzDocStrText = "/directory/fzDocStrText";
        this.fzDocStrDates = "/directory/fzDocStrDates";
        this.fzUpload = "/directory/fzSaveUploadFile";
        this.confPath = this.req.getWindow();
        this.location = this.req.getLocation();
    }
    TreesRequests.prototype.getDataTree = function (idDoc, idRev) {
        var doc = this.checkForNull(idDoc);
        var rev = this.checkForNull(idRev);
        return this.http.get(this.location + this.confPath + this.fzDocumentStructureTree + doc + rev, { headers: this.head.createCsrfHeader() });
    };
    TreesRequests.prototype.getDocStrText = function (idStr, idDoc, idRev) {
        var str = this.checkForNull(idStr);
        var rev = this.checkForNull(idRev);
        var doc = this.checkForNull(idDoc);
        if (rev == "/0")
            return this.http.get(this.location + this.confPath + this.fzDocStrText + str + doc, { headers: this.head.createCsrfHeader() });
        else
            return this.http.get(this.location + this.confPath + this.fzDocStrText + str + doc + rev, { headers: this.head.createCsrfHeader() });
    };
    TreesRequests.prototype.postDocStrDates = function (obj, idStr, idDoc, idRev) {
        var str = this.checkForNull(idStr);
        var rev = this.checkForNull(idRev);
        var doc = this.checkForNull(idDoc);
        var body = JSON.stringify(obj);
        return this.http.post(this.location + this.confPath + this.fzDocStrDates + str + doc + rev, body, { headers: this.head.createCsrfHeader() });
    };
    TreesRequests.prototype.getDocConvert = function (idDoc, idRev) {
        var id = this.checkForNull(idDoc);
        var rev = this.checkForNull(idRev);
        return this.http.get(this.location + this.confPath + this.fzUpload + id + rev, { headers: this.head.createCsrfHeader() });
    };
    TreesRequests.prototype.formattedURL = function () {
        return this.location + this.confPath + "/npaparse/uploadfile";
    };
    TreesRequests.prototype.createHeader = function (head) {
        var result = {
            key: "",
            value: ""
        };
        if (!util_1.isUndefined(head) || !head == null) {
            result['key'] = head.keys();
            result['value'] = head.values();
        }
        return result;
    };
    TreesRequests.prototype.checkForNull = function (check) {
        if (check == null) {
            return check = "/0";
        }
        else {
            return check = "/" + check;
        }
    };
    TreesRequests = __decorate([
        core_1.Injectable(),
        __param(0, core_1.Inject(http_1.Http)),
        __param(1, core_1.Inject(createHeaderService_1.Head)),
        __param(2, core_1.Inject(requestService_1.RequestService))
    ], TreesRequests);
    return TreesRequests;
}());
exports.TreesRequests = TreesRequests;
//# sourceMappingURL=treeRequests.js.map