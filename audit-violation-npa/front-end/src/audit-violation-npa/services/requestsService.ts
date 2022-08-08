/**
 * Created by dmihaylov on 23.04.2018.
 */
import {Inject, Injectable} from '@angular/core';
import {isUndefined} from "util";

@Injectable()
export class RequestsService {
    constructor(
        @Inject ('Window') private  window
    ) { }
    public getBaseUrl(): string {
        return this.getLocation() + this.getWindow();
    }

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
}