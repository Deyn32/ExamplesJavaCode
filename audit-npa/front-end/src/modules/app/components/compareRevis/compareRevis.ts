/**
 * Created by dmihaylov on 27.02.2018.
 */
import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'comrev-app',
    templateUrl: './compareRevis.html'
})

export class CompareRevisComponent implements OnInit {
    constructor() {}

    ngOnInit() {

    }

    @Input() strHtml: string;

}