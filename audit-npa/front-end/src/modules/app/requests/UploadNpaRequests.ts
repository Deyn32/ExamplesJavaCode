/**
 * Created by dmihaylov on 01.06.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, ResponseContentType, RequestOptions, URLSearchParams} from '@angular/http';
import {RequestService } from '../services/requestService';

@Injectable()
export class UploadNpaRequests {
    constructor(@Inject(Http) private http: Http,
                @Inject(RequestService) private req: RequestService) { }

    private baseUrl = this.req.baseUrl;

    public getCreateFile(ids: number[]){
        let createFiles: string = "/createFiles";
        let myParam = new URLSearchParams();
        myParam.append('doc', ids.toString());
        let options = new RequestOptions({responseType : ResponseContentType.ArrayBuffer, params: myParam});
        return this.http.get(this.baseUrl + createFiles, options);
    }
}