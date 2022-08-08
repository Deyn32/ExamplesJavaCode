"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * Created by dmihaylov on 14.08.2017.
 */
/*Это только заготовка, нужно будет переписать позже(если опять ничего не поменяют) */
var core_1 = require('@angular/core');
var NoopInterceptor = (function () {
    function NoopInterceptor() {
    }
    NoopInterceptor.prototype.intercept = function (req, next) {
        return next.handle(req);
    };
    NoopInterceptor = __decorate([
        core_1.Injectable()
    ], NoopInterceptor);
    return NoopInterceptor;
}());
exports.NoopInterceptor = NoopInterceptor;
/* В новой версии не работает, но оставлю, вдруг опять поменяют
import { Injectable } from "@angular/core";
import { Http, Headers, Request, Response, RequestOptionsArgs, RequestOptions, ConnectionBackend } from "@angular/http";
import { Router } from "@angular/router";
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import * as $ from "jquery";*/
/*@Injectable()
export class HttpInterceptor extends Http {

    constructor(backend: ConnectionBackend, defaultOptions: RequestOptions, private _router: Router) {
        super(backend, defaultOptions);
    }

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.get(url,options));
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.post(url, body, this.getRequestOptionArgs(options)));
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.put(url, body, this.getRequestOptionArgs(options)));
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.delete(url, options));
    }

    getRequestOptionArgs(options?: RequestOptionsArgs) : RequestOptionsArgs {
        if (options == null) {
            options = new RequestOptions();
        }
        if (options.headers == null) {
            options.headers = new Headers();
        }
        options.headers.append('Content-Type', 'application/json');

        let token = $('meta[name="_csrf"]').attr('content');
        let headerName = $('meta[name="_csrf_header"]').attr('content');
        if (token.indexOf('$') >= 0 && headerName.indexOf('$') >= 0) {
            console.log('could not read csrf token or csrf header name!');
        } else
            options.headers.append(headerName, token);

        return options;
    }

    intercept(observable: Observable<Response>): Observable<Response> {
        return observable.catch((err, source) => {
            if (err.status  == 401) {
                this._router.navigate(['/login']);
                return Observable.throw(err);
            }
        });
    }
}*/
//# sourceMappingURL=httpInterceptService.js.map