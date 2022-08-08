/**
 * Created by dmihaylov on 12.04.2017.
 * Сервис запросов для построения структур деревьев
 */
import { Inject, Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import {isUndefined} from 'util';
import { Head } from '../services/createHeaderService';
import {RequestService } from '../services/requestService';

@Injectable()
export  class TreesRequests {
    constructor(@Inject(Http) private http: Http,
                @Inject(Head) private head: Head,
                @Inject(RequestService) private req: RequestService) { }

    private baseUrl: string = this.req.baseUrl;

    public getDataTree(idDoc: number, idRev: number ) {
        let fzDocumentStructureTree: string = '/directory/fzDocumentStructureTree';
        const doc: string = this.checkForNull(idDoc);
        const rev: string = this.checkForNull(idRev);
        return this.http.get(this.baseUrl + fzDocumentStructureTree + doc + rev, {headers: this.head.createCsrfHeader()});
    }

    public getDocStrText(idStr: string, idDoc: number, idRev: number) {
        let fzDocStrText: string = '/directory/fzDocStrText';
        const str: string = this.checkForNull(idStr);
        const rev: string = this.checkForNull(idRev);
        const doc: string = this.checkForNull(idDoc);
        if (rev == '/0')
            return this.http.get(this.baseUrl + fzDocStrText + str + doc, {headers: this.head.createCsrfHeader()});
        else
            return this.http.get(this.baseUrl + fzDocStrText + str + doc + rev, {headers: this.head.createCsrfHeader()});
    }

    public postDocStrDates(obj: Object, idStr: string, idDoc: number, idRev: number) {
        let fzDocStrDates: string = '/directory/fzDocStrDates';
        const str: string = this.checkForNull(idStr);
        const rev: string = this.checkForNull(idRev);
        const doc: string = this.checkForNull(idDoc);
        const body = JSON.stringify(obj);
        return this.http.post(this.baseUrl + fzDocStrDates + str + doc + rev, body, {headers: this.head.createCsrfHeader()});
    }

    public getDocConvert(idDoc: number, idRev: number) {
        let fzUpload: string = '/directory/fzSaveUploadFile';
        const id: string = this.checkForNull(idDoc);
        const rev: string = this.checkForNull(idRev);
        return this.http.get(this.baseUrl + fzUpload + id + rev, {headers: this.head.createCsrfHeader()});
    }

    public formattedURL() {
        return this.baseUrl + '/npaparse/uploadfile';
    }

    public createHeader(head: Headers): Object {
        const result: Object  = {
            key: '',
            value: ''
        };
        if (!isUndefined(head) || !head == null) {
            result['key'] =  head.keys();
            result['value'] = head.values();
        }
        return result;
    }

    private checkForNull(check) {
        if (check == null) {
            return check = '/0';
        }
        else {
            return check = '/' + check;
        }
    }
}
