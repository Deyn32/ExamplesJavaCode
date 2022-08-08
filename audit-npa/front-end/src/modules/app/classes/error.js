"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
/**
 * Created by dmihaylov on 08.08.2017.
 * Вспомогательный класс обработки ошибок
 */
var Error = (function () {
    function Error() {
    }
    Error.prototype.noDocuments = function () {
        var str = "Нет документов для отображения!";
        return str;
    };
    Error.prototype.documentWasDeleted = function () {
        var str = "Данный документ был удален другим пользователем!";
        return str;
    };
    Error.prototype.notAllData = function () {
        var str = "Введены не все данные!";
        return str;
    };
    Error.prototype.revisionWasDeleted = function () {
        var str = "Данная редакция была удалена другим пользователем!";
        return str;
    };
    Error.prototype.notSelectedDocument = function () {
        var str = "Не выбран документ!";
        return str;
    };
    Error.prototype.enteredWrongDate = function () {
        var str = "Введена неверная дата!";
        return str;
    };
    Error.prototype.notSelectedRevision = function () {
        var str = "Не выбрана редакция документа!";
        return str;
    };
    Error = __decorate([
        core_1.Injectable()
    ], Error);
    return Error;
}());
exports.Error = Error;
//# sourceMappingURL=error.js.map