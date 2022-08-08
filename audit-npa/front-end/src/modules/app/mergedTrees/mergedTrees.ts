/**
 * Created by dmihaylov on 21.02.2018.
 */
import {Component, Input, OnInit} from '@angular/core';
import {MergedTree} from "../classes/mergedTree";
import {Doc} from "../classes/Docs";

@Component({
    selector: 'mt-app',
    templateUrl: './mtComp.html'
})

export class MtComponent implements OnInit {
    constructor(){
        this.mergedTree = new MergedTree();
    }

    ngOnInit() {
        this.compareTrees(this.resp);
    }
    @Input() resp: any;
    @Input() doc: Doc;
    @Input() dtRev1: Date;
    @Input() dtRev2: Date;

    public mergedTree: MergedTree;
    public isComponent: boolean = true;
    public leftTable: Date;
    public rightTable: Date;

    private compareTrees(resp: any): void {
        this.leftTable = this.dtRev1;
        this.rightTable = this.dtRev2;
        this.mergedTree = new MergedTree();
        this.mergedTree = this.mergedTree.createMergedTree(this.mergedTree, resp);
    }

    public btnBack() {
        this.isComponent = false;
    }
}