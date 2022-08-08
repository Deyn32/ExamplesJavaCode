/**
 * Created by dmihaylov on 01.03.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, URLSearchParams, RequestOptions} from "@angular/http";
import {RequestsService} from '../services/requestsService';
import {isUndefined} from "util";
import {Head} from "./head";

@Injectable()
export class MainTableRequests {
    constructor(
        @Inject(Http) private http: Http,
        @Inject(RequestsService) private reqService: RequestsService
    ) {}

    private baseUrl: string = this.reqService.getBaseUrl();



    private head: Head = new Head();

    public getDataTable(id: string, idNpa: number, idViol: number) {
        let npaStartAnalyse: string = "/npaStartAnalyse";
        let myParams = new URLSearchParams();
        if (!isUndefined(id) && id != null)
            myParams.append('id', id);
        if (idNpa != null)
            if(idNpa != 0)
                myParams.append('npa', idNpa.toString());
        if (idViol != null)
            if(idViol != 0)
                myParams.append('viol', idViol.toString());
        let options = new RequestOptions({ params: myParams });
        return this.http.get(this.baseUrl + npaStartAnalyse, options);
    }

    public postRepairLink(id: number) {
        let repairLink: string = "/repairLink";
        const body = JSON.stringify(id);
        return this.http.post(this.baseUrl + repairLink, body, {headers: this.head.createCsrfHeader()});
    }

    public getStopAnalise() {
        let npaStopAnalyse: string = "/npaStopAnalyse";
        return this.http.get(this.baseUrl + npaStopAnalyse);
    }

    public getFindAllNpa() {
        let findNpa: string = "/findNpa";
        return this.http.get(this.baseUrl + findNpa);
    }

    public getFindAllViolations() {
        let findAllViol: string = "/findAllViol";
        return this.http.get(this.baseUrl + findAllViol);
    }

}