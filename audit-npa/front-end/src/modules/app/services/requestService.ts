/**
 * Created by dmihaylov on 04.09.2017.
 */
import { Inject, Injectable } from '@angular/core';
import {isUndefined} from 'util';

@Injectable()
export class RequestService {
    constructor(@Inject ('Window') private  window) { }

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

    public baseUrl: string = this.getLocation() + this.getWindow();
}
