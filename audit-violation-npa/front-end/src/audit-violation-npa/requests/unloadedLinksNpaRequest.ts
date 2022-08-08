/**
 * Created by dmihaylov on 02.04.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {RequestsService} from '../services/requestsService';

@Injectable()
export class UnloadedLinksNpaRequest {
    constructor(
        @Inject(Http) private http: Http,
        @Inject (RequestsService) private reqService: RequestsService
    ) {}

    private baseUrl: string = this.reqService.getBaseUrl();

    public getListUnloadedNPA() {
        let unloadLinksNpa = "/unloadLinksNpa";
        return this.http.get(this.baseUrl + unloadLinksNpa);
    }
}