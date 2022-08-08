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
 * Created by dmihaylov on 04.09.2017.
 */
var core_1 = require('@angular/core');
var util_1 = require("util");
var RequestService = (function () {
    function RequestService(window) {
        this.window = window;
        this.confPath = this.getWindow();
    }
    RequestService.prototype.getWindow = function () {
        if (util_1.isUndefined(this.window.configContextPath)) {
            this.window.configContextPath = "";
        }
        var confPath = this.window.configContextPath;
        return confPath;
    };
    RequestService.prototype.getLocation = function () {
        return this.window.location.origin;
    };
    RequestService = __decorate([
        core_1.Injectable(),
        __param(0, core_1.Inject('Window'))
    ], RequestService);
    return RequestService;
}());
exports.RequestService = RequestService;
//# sourceMappingURL=requestService.js.map