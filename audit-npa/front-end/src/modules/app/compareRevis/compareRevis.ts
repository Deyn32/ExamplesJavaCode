/**
 * Created by dmihaylov on 27.02.2018.
 */
import {Component, OnInit, Input} from '@angular/core';
import {Doc} from "../classes/Docs";

@Component({
    selector: 'comrev-app',
    templateUrl: './compareRevis.html'
})

export class CompareRevisComponent implements OnInit {
    constructor() {}

    ngOnInit() {
        this.compareRevis(this.comRevResp);
    }


    @Input() comRevResp: any;
    @Input() doc: Doc;
    @Input() dtRev1: Date;
    @Input() dtRev2: Date;

    isComponent: boolean = true;
    strHtml: string = "";

    private compareRevis(resp: any): void {
        this.strHtml = resp.text();
    }

    public btnBack() {
        this.isComponent = false;
    }
}