/**
 * Created by dmihaylov on 12.03.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {Http} from "@angular/http";
import {isUndefined} from "util";

@Injectable()
export class EmptyLingRequest {
    constructor(
        @Inject(Http) private http: Http,
        @Inject ('Window') private  window
    ) {}

    private emptyLinks: string = "/emptyLinks";
    private confPath: string = this.getWindow();
    private location: string = this.getLocation();

    private getLocation() {
        return this.window.location.origin;
    }

    private getWindow() {
        if (isUndefined(this.window.configContextPath)) {
            this.window.configContextPath = '';
        }
        const confPath: string = this.window.configContextPath;
        return confPath;
    }

    public getEmptyLinks() {
        return this.http.get(this.location + this.confPath + this.emptyLinks);
    }
}