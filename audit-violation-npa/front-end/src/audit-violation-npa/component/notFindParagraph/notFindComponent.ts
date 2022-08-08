/**
 * Created by dmihaylov on 12.03.2018.
 */
import {Component, OnInit, Inject} from '@angular/core';
import {TableEmptyLink} from "../../classes/tableEmptyLink";
import {Revision} from "../../classes/revision";
import {EmptyLinkRequest} from '../../requests/emptyLinkRequest';

@Component({
    selector: 'not-find-app',
    templateUrl: './notFind.html'
})

export class NotFindComponent implements OnInit {
    constructor(
        @Inject (EmptyLinkRequest) private elr: EmptyLinkRequest
    ) {
    }

    public emptyLinks: TableEmptyLink[] = [];
    public disRefrash: boolean = false;
    public isProgressBar: boolean = false;


    ngOnInit() {
        this.refrash();
    }

    private checkLinks(links: any) {
        for(let link of links) {
            if(link.text == null) {
                link.text = "";
            }
            if(link.rev == null) {
                link.rev = new Revision();
            }
        }
        return links;
    }

    btnRefrash() {
        this.refrash();
    }

    private refrash() {
        this.disRefrash = true;
        this.isProgressBar = true;
        this.elr.getEmptyLinks().subscribe( a => {
            let obj = this.checkLinks(a.json());
            this.emptyLinks = obj;
            this.disRefrash = false;
            this.isProgressBar = false;
        })
    }
}