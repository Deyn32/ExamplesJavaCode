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
 * Created by dmihaylov on 06.04.2017.
 */
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var createHeaderService_1 = require("../services/createHeaderService");
var requestService_1 = require("../services/requestService");
var DocumentNPARequests = (function () {
    function DocumentNPARequests(http, head, req) {
        this.http = http;
        this.head = head;
        this.req = req;
        this.confPath = this.req.getWindow();
        this.location = this.req.getLocation();
        this.fzFindList = "/directory/fzFindList";
        this.fzAddNPA = "/directory/fzAddNPA";
        this.fzCheckIsDeletedDoc = "/directory/fzCheckIsDeletedDoc";
    }
    /** Запрос к БД на добавление документа */
    DocumentNPARequests.prototype.getDataAddNPA = function (obj) {
        var body = JSON.stringify(obj);
        return this.http.post(this.location + this.confPath + this.fzAddNPA, body, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на редактирование документа*/
    DocumentNPARequests.prototype.putAddNPA = function (obj, id) {
        var body = JSON.stringify(obj);
        return this.http.put(this.location + this.confPath + this.fzAddNPA + "/" + id, body, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на удаление документа */
    DocumentNPARequests.prototype.deleteAddNPA = function (id) {
        return this.http.delete(this.location + this.confPath + this.fzAddNPA + "/" + id, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на признак удаления документа*/
    DocumentNPARequests.prototype.getCheckIsDeletedDoc = function (id) {
        return this.http.get(this.location + this.confPath + this.fzCheckIsDeletedDoc + "/" + id, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на наличие документов */
    DocumentNPARequests.prototype.getDataFindList = function () {
        return this.http.get(this.location + this.confPath + this.fzFindList);
    };
    DocumentNPARequests = __decorate([
        core_1.Injectable(),
        __param(0, core_1.Inject(http_1.Http)),
        __param(1, core_1.Inject(createHeaderService_1.Head)),
        __param(2, core_1.Inject(requestService_1.RequestService))
    ], DocumentNPARequests);
    return DocumentNPARequests;
}());
exports.DocumentNPARequests = DocumentNPARequests;
//# sourceMappingURL=documentNPARequests.js.map