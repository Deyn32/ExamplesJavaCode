/**
 * Created by dmihaylov on 12.01.2018.
 */
 import { Inject, Injectable } from '@angular/core';
 import { Http } from '@angular/http';
 import { Head } from '../services/createHeaderService';
 import { RequestService } from '../services/requestService';

 @Injectable()
 export class Dictionary {
 	
 	constructor(@Inject(Http) private http: Http,
                @Inject(Head) private head: Head,
                @Inject(RequestService) private req: RequestService) { }

 	private fzDocTypes: string = '/directory/fzDocTypes';
    private fzElemTypes: string = '/directory/fzElemTypes';
    private fzUpdateElem: string = '/directory/fzUpdateElem';
 	private baseUrl = this.req.baseUrl;

 	public getDocTypes() {
       return this.http.get(this.baseUrl + this.fzDocTypes, {headers: this.head.createCsrfHeader()});
 	}

 	public getElemTypes() {
 	    return this.http.get(this.baseUrl + this.fzElemTypes);
    }

    public postElemType(str: string) {
    	return this.http.post(this.baseUrl + this.fzUpdateElem, str, {headers: this.head.createCsrfHeader()});
    }
 }
 
