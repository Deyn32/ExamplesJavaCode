/**
 * Created by dmihaylov on 04.04.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, RequestOptions, URLSearchParams} from "@angular/http";
import {RequestsService} from '../services/requestsService'
import {isUndefined} from "util";
import {Head} from './head';

@Injectable()
export class WithoutRevRequest {
    constructor(
        @Inject(Http) private http: Http,
        @Inject (RequestsService) private reqService: RequestsService
    ) {}

    private baseUrl: string = this.reqService.getBaseUrl();
    private head: Head = new Head();

    public getListWithoutRev(id: string) {
        let withoutRev: string = "/withoutRev";
        let myParams = new URLSearchParams();
        if (!isUndefined(id) && id != null)
            myParams.append('id', id);
        let options = new RequestOptions({ params: myParams });
        return this.http.get(this.baseUrl + withoutRev, options);
    }

    public getStopWithoutRev() {
        let stopWithoutRev: string = "/stopWithoutRev";
        return this.http.get(this.baseUrl + stopWithoutRev);
    }

    public postRepairAllLinks(ids: any[]) {
        let repairAllLinks = "/repairAllLinks";
        const body = JSON.stringify(ids);
        return this.http.post(this.baseUrl + repairAllLinks, body, {headers: this.head.createCsrfHeader()} )
    }

}