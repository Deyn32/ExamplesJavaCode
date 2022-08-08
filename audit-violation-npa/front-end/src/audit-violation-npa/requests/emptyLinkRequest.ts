/**
 * Created by dmihaylov on 12.03.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, URLSearchParams, RequestOptions} from "@angular/http";
import { RequestsService } from '../services/requestsService';
import {isUndefined} from "util";
import {Head} from "./head";

@Injectable()
export class EmptyLinkRequest {
    constructor(
        @Inject(Http) private http: Http,
        @Inject(RequestsService) private reqService,
    ) {}

    private baseUrl: string = this.reqService.getBaseUrl();
    private head: Head = new Head();

    public getEmptyLinks() {
        let emptyLinks: string = "/emptyLinks";
        return this.http.get(this.baseUrl + emptyLinks);
    }

    public getListLinks() {
        let listLinks: string = "/listLinks";
        return this.http.get(this.baseUrl + listLinks);
    }

    public getListRevsLinks(id: number) {
        let listRevsLinks: string = "/listRevsLinks";
        let myParams = new URLSearchParams();
        if (!isUndefined(id) && id != null)
            myParams.append('id', id.toString());
        let options = new RequestOptions({ params: myParams });
        return this.http.get(this.baseUrl + listRevsLinks, options );
    }

    public getDetailLinks(id: number, idRev?: number) {
        let detailLinksRev: string = "/detailLinksRev";
        let myParams = new URLSearchParams();
        if(id != null)
            myParams.append('id', id.toString());
        if(idRev != null) {
            myParams.append('idRev', idRev.toString());
        }
        let options = new RequestOptions({ params: myParams});
        return this.http.get(this.baseUrl + detailLinksRev, options);
    }

    public  getCompareStructAbs(idLink: number, idRev1: number, idRev2: number) {
        let fzcompareStructAbs = "/fzcompareStructAbs";
        let myParams = new URLSearchParams();
        if(idLink != null)
            myParams.append('idLink', idLink.toString());
        if(idRev1 != null)
            myParams.append('revId1', idRev1.toString());
        else {
            idRev1 = 0;
            myParams.append('revId1', idRev1.toString());
        }
        if(idRev2 != null)
            myParams.append('revId2', idRev2.toString());
        else {
            idRev2 = 0;
            myParams.append('revId2', idRev2.toString());
        }
        let options = new RequestOptions({params: myParams});
        return this.http.get(this.baseUrl + fzcompareStructAbs, options);
    }

    public getStatusDropDown() {
        let findStatuses = "/findStatuses";
        return this.http.get(this.baseUrl + findStatuses);
    }

    public getFindStructElemStatus() {
        let findStructElemStatus = "/findStructElemStatus";
        return this.http.get(this.baseUrl + findStructElemStatus);
    }

    public getFilterDoubtful() {
        let filterDoubtful = "/filterDoubtful";
        return this.http.get(this.baseUrl + filterDoubtful);
    }

    public postSaveStatus(obj: any) {
        let saveStatus: string = "/saveStatus";
        const body = JSON.stringify(obj);
        return this.http.post(this.baseUrl + saveStatus, body, {headers: this.head.createCsrfHeader()});
    }

    public getFilterDate(date: string) {
        let filterDate = "/filterDate";
        let myParams = new URLSearchParams();
        myParams.append('date', date);
        let options = new RequestOptions({params: myParams});
        return this.http.get(this.baseUrl + filterDate, options);
    }

    public postEditDateViol(obj: any) {
        let editDateViol = "/editDateViol";
        const body = JSON.stringify(obj);
        return this.http.post(this.baseUrl + editDateViol, body, {headers: this.head.createCsrfHeader()});
    }

    public getCompareStructViolAbs(idLink: number, revId: number){
        let fzcompareViolAbs = "/fzcompareViolAbs";
        let myParams = new URLSearchParams();
        myParams.append('idLink', idLink.toString());
        myParams.append('revId', revId.toString());
        let options = new RequestOptions({params: myParams});
        return this.http.get(this.baseUrl + fzcompareViolAbs, options);
    }

    public getRegrouping(id: string, idNpaStruct: number, idNpa: number){
        let regrouping = "/regrouping";
        let myParams = new URLSearchParams();
        myParams.append('id', id);
        myParams.append('idNpaStruct', idNpaStruct.toString());
        myParams.append('idNpa', idNpa.toString());
        let options = new RequestOptions({params: myParams});
        return this.http.get(this.baseUrl + regrouping, options);
    }
}