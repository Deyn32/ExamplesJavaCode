/**
 * Created by dmihaylov on 01.06.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, ResponseContentType, RequestOptions, URLSearchParams} from '@angular/http';
import {RequestService } from '../services/requestService';

@Injectable()
export class DownloadNpaRequests {
    constructor(@Inject(Http) private http: Http,
                @Inject(RequestService) private req: RequestService) { }

    private baseUrl = this.req.baseUrl;

    public formattedURL() {
        return this.baseUrl + '/downloadNpaZip';
    }
}