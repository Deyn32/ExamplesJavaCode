/**
 * Created by dmihaylov on 14.08.2017.
 */
/*Это только заготовка, нужно будет переписать позже(если опять ничего не поменяют) */
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';

@Injectable()
export class NoopInterceptor implements HttpInterceptor {
    public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req);
    }
}
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
