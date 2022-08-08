/**
 * Created by dmihaylov on 06.04.2017.
 */
import { Inject, Injectable } from '@angular/core';
import {Http, ResponseContentType, RequestOptions, URLSearchParams} from '@angular/http';
import { Head } from '../services/createHeaderService';
import { RequestService } from '../services/requestService';

@Injectable()
export class DocumentNPARequests {

    constructor(@Inject(Http) private http: Http,
                @Inject(Head) private head: Head,
                @Inject(RequestService) private req: RequestService) { };
    private baseUrl: string = this.req.baseUrl;
    private fzAddNPA: string = '/directory/fzAddNPA';

    /* Запрос к БД на добавление документа */
    public getDataAddNPA(obj: Object) {
        const body = JSON.stringify(obj);
        return this.http.post(this.baseUrl + this.fzAddNPA, body, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на редактирование документа*/
    public putAddNPA(obj: Object, id: number) {
        const body = JSON.stringify(obj);
        return this.http.put(this.baseUrl + this.fzAddNPA + '/' + id, body, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на удаление документа */
    public  deleteAddNPA(id: number) {
        return this.http.delete(this.baseUrl + this.fzAddNPA + '/' + id, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на признак удаления документа*/
    public getCheckIsDeletedDoc(id: number) {
        let fzCheckIsDeletedDoc: string = '/directory/fzCheckIsDeletedDoc';
        return this.http.get(this.baseUrl + fzCheckIsDeletedDoc + '/' + id, {headers: this.head.createCsrfHeader()});
    }

    /** Запрос к БД на наличие документов */
    public getDataFindList() {
        let fzFindList: string = '/directory/fzFindList';
        return this.http.get(this.baseUrl + fzFindList);
    }

    /** Скачать докумет */
    public getDocumentDownload(id: number, idRev:number) {
        let fzDownloadNPA: string = '/directory/fzDownloadNPA';
        const myParams = new URLSearchParams();
        myParams.append('docId', id.toString());
        if (idRev != null)
            myParams.append('revId', idRev.toString());
        let options = new RequestOptions({responseType : ResponseContentType.ArrayBuffer, params: myParams });
        return this.http.get(this.baseUrl + fzDownloadNPA, options);
    }

    /**Скачать мануал*/
    public getDownloadManual() {
        let fzDownloadManual: string = '/directory/fzDownloadManual';
        let options = new RequestOptions({responseType : ResponseContentType.ArrayBuffer});
        return this.http.get(this.baseUrl + fzDownloadManual, options);
    }
}
