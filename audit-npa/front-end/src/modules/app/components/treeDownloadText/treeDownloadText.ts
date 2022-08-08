/**
 * Created by dmihaylov on 01.03.2018.
 */
import {Component, OnInit, Input} from '@angular/core';
import {ModelTreeFile} from "../../classes/ModelTreeFile";

@Component({
    selector:'dload-app',
    templateUrl:'./treeDownloadText.html'
})
export class TreeDownloadTextComponent implements OnInit {
    constructor() {}

    ngOnInit() {

    }


    @Input() modelTreeFile: ModelTreeFile;





}