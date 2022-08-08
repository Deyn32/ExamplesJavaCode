/**
 * Created by dmihaylov on 11.04.2017.
 */
import { Inject, Injectable } from '@angular/core';
import {Http, RequestOptions, URLSearchParams } from '@angular/http';
import { Head } from '../services/createHeaderService';
import { RequestService } from '../services/requestService';

@Injectable()
export class RevisService {
    constructor(@Inject(Http) private http: Http,
                @Inject(Head) private head: Head,
                @Inject(RequestService) private req: RequestService) { }

    private fzRev: string = '/directory/fzRev';
    private baseUrl: string = this.req.baseUrl;

    /* Запрос к БД на обновление списка редакций*/
    public getRevList(id: number) {
        let fzRevList: string = '/directory/fzRevList';
        return this.http.get(this.baseUrl + fzRevList + '/' + id);
    }
    /* Заброс к БД на добавление редакции*/
    public getPostRev(obj: Object) {
        const body = JSON.stringify(obj);
        return this.http.post(this.baseUrl + this.fzRev, body, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на удаление редакции*/
    public getDeleteRev(idRev: number) {
        const rev: string = this.checkForNull(idRev);
        return this.http.delete(this.baseUrl + this.fzRev + rev, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на обновление редакции*/
    public getPutRev(idRev: number, obj: Object) {
        const rev: string = this.checkForNull(idRev);
        const body = JSON.stringify(obj);
        return this.http.put(this.baseUrl + this.fzRev + rev, body, {headers: this.head.createCsrfHeader()});
    }

    /* Запрос к БД на получение образа документа редакции*/
    public getViewHTML(id: number, idRev: number) {
        let fzView: string = '/directory/fzView';
        const rev: string = this.checkForNull(idRev);
        const idDoc: string = this.checkForNull(id);
        return this.http.get(this.baseUrl + fzView + idDoc + rev);
    }

    /* Запрос к БД на признак удаления редакции*/
    public getCheckIsDeletedRev(id: number, idRev: number) {
        let fzCheckIsDeletedRev: string = '/directory/fzCheckIsDeletedRev';
        const doc = this.checkForNull(id);
        const rev = this.checkForNull(idRev);
        return this.http.get(this.baseUrl + fzCheckIsDeletedRev + doc + rev, {headers: this.head.createCsrfHeader()});
    }

    /* Сравнение редакций */
    public compareRevisions(id: number, idRev1: number, idRev2: number) {
        let fzMergeRev: string = '/directory/fzMergeRev';
        const myParams = new URLSearchParams();
        myParams.append('docId', id.toString());
        if (idRev1 != null)
            myParams.append('revId1', idRev1.toString());
        if (idRev2 != null)
            myParams.append('revId2', idRev2.toString());
        const args = new RequestOptions({ params: myParams });
        return this.http.get(this.baseUrl + fzMergeRev, args);
    }

    public compareStruct(idDocMT: number, idRev1: number, idRev2: number) {
        let fzcompareStruct: string = '/directory/fzcompareStruct';
        const myParams = new URLSearchParams();
        myParams.append('docId', idDocMT.toString());
        if (idRev1 != null)
            myParams.append('revId1', idRev1.toString());
        if (idRev2 != null)
            myParams.append('revId2', idRev2.toString());
        const args = new RequestOptions({ params: myParams });
        return this.http.get(this.baseUrl + fzcompareStruct, args);
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
