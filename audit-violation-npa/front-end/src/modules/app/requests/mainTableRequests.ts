/**
 * Created by dmihaylov on 01.03.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http, URLSearchParams, RequestOptions} from "@angular/http";
import {isUndefined} from "util";

@Injectable()
export class MainTableRequests {
    constructor(
        @Inject(Http) private http: Http,
        @Inject ('Window') private  window
    ) {}

    private confPath: string = this.getWindow();
    private location: string = this.getLocation();
    private npaStartAnalyse: string = "/npaStartAnalyse";

    private getWindow() {
        if (isUndefined(this.window.configContextPath)) {
            this.window.configContextPath = '';
        }
        const confPath: string = this.window.configContextPath;
        return confPath;
    }

    private getLocation() {
        return this.window.location.origin;
    }

    public getDataTable(id: string) {
        let myParams = new URLSearchParams();
        if (!isUndefined(id) && id != null)
            myParams.append('id', id);
        let options = new RequestOptions({ params: myParams });
        return this.http.get(this.location + this.confPath + this.npaStartAnalyse, options);
    }

}