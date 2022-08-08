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
 * Created by dmihaylov on 11.04.2017.
 */
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var createHeaderService_1 = require("../services/createHeaderService");
var requestService_1 = require('../services/requestService');
var RevisService = (function () {
    function RevisService(http, head, req) {
        this.http = http;
        this.head = head;
        this.req = req;
        this.fzRev = "/directory/fzRev";
        this.fzRevList = "/directory/fzRevList";
        this.fzView = "/directory/fzView";
        this.fzMergeRev = "/directory/fzMergeRev";
        this.fzCheckIsDeletedRev = "/directory/fzCheckIsDeletedRev";
        this.confPath = this.req.getWindow();
        this.location = this.req.getLocation();
    }
    /** Запрос к БД на обновление списка редакций*/
    RevisService.prototype.getRevList = function (id) {
        return this.http.get(this.location + this.confPath + this.fzRevList + "/" + id);
    };
    /** Заброс к БД на добавление редакции*/
    RevisService.prototype.getPostRev = function (obj) {
        var body = JSON.stringify(obj);
        return this.http.post(this.location + this.confPath + this.fzRev, body, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на удаление редакции*/
    RevisService.prototype.getDeleteRev = function (idRev) {
        var rev = this.checkForNull(idRev);
        return this.http.delete(this.location + this.confPath + this.fzRev + rev, { headers: this.head.createCsrfHeader() });
    };
    /** Запрос к БД на обновление редакции*/
    RevisService.prototype.getPutRev = function (idRev, obj) {
        var rev = this.checkForNull(idRev);
        var body = JSON.stringify(obj);
        return this.http.put(this.location + this.confPath + this.fzRev + rev, body, { headers: this.head.createCsrfHeader() });
    };
    /**Запрос к БД на получение образа документа редакции*/
    RevisService.prototype.getViewHTML = function (id, idRev) {
        var rev = this.checkForNull(idRev);
        var idDoc = this.checkForNull(id);
        return this.http.get(this.location + this.confPath + this.fzView + idDoc + rev);
    };
    /** Запрос к БД на признак удаления редакции*/
    RevisService.prototype.getCheckIsDeletedRev = function (idRev) {
        return this.http.get(this.location + this.confPath + this.fzCheckIsDeletedRev + "/" + idRev, { headers: this.head.createCsrfHeader() });
    };
    RevisService.prototype.checkForNull = function (check) {
        if (check == null) {
            return check = "/0";
        }
        else {
            return check = "/" + check;
        }
    };
    /* Сравнение редакций */
    RevisService.prototype.compareRevisions = function (id, idRev1, idRev2) {
        var myParams = new http_1.URLSearchParams();
        myParams.append('docId', id.toString());
        if (idRev1 != null)
            myParams.append('revId1', idRev1.toString());
        if (idRev2 != null)
            myParams.append('revId2', idRev2.toString());
        var args = new http_1.RequestOptions({ params: myParams });
        return this.http.get(this.location + this.confPath + this.fzMergeRev, args);
    };
    RevisService = __decorate([
        core_1.Injectable(),
        __param(0, core_1.Inject(http_1.Http)),
        __param(1, core_1.Inject(createHeaderService_1.Head)),
        __param(2, core_1.Inject(requestService_1.RequestService))
    ], RevisService);
    return RevisService;
}());
exports.RevisService = RevisService;
//# sourceMappingURL=revisionRequest.js.map