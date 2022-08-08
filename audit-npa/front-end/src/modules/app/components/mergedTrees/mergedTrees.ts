/**
 * Created by dmihaylov on 21.02.2018.
 */
import {Component, Input, OnInit} from '@angular/core';
import {MergedTree} from "../../classes/mergedTree";
import {Doc} from "../../classes/Docs";

@Component({
    selector: 'mt-app',
    templateUrl: './mtComp.html'
})

export class MtComponent implements OnInit {
    constructor(){
        ;
    }

    ngOnInit() {
    }
    @Input() mergedTree: MergedTree;


    public ;
    public leftTable: Date;
    public rightTable: Date;
    public isComponent: boolean = true;


    btnBack() {
        this.isComponent = false;
    }
}