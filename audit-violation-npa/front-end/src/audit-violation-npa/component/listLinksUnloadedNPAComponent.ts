/**
 * Created by dmihaylov on 02.04.2018.
 */
import { Component, OnInit, Inject } from '@angular/core';
import { UnloadedLinksNpaRequest } from '../requests/unloadedLinksNpaRequest';
import { UnloadedLinkNpa } from '../classes/unloadedLinkNpa';

@Component({
    selector: 'list-unloaded-app',
    templateUrl: './listLinksUnloadedNPA.html'
})

export class ListLinksUnloadedNPAComponent implements OnInit {
    constructor
    (
        @Inject (UnloadedLinksNpaRequest) private unloadReq: UnloadedLinksNpaRequest
    ) {}

    public unloadedLinksNpa: UnloadedLinkNpa[] = [];
    public isProgressBar: boolean = false;

    ngOnInit() {
        this.fillTable();
    }

    private fillTable() {
        this.isProgressBar = true;
        this.unloadReq.getListUnloadedNPA().subscribe(get => {
            this.unloadedLinksNpa = get.json();
            this.isProgressBar = false;
        });
    }
}