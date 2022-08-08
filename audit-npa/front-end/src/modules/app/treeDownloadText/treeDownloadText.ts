/**
 * Created by dmihaylov on 01.03.2018.
 */
import {Component, OnInit, Input, Inject} from '@angular/core';
import { TreesRequests } from '../requests/treeRequests';
import {Doc} from "../classes/Docs";
import {ModelFormRevision} from "../classes/modelFormRevision";
import {ModelTreeFile} from "../classes/ModelTreeFile";

@Component({
    selector:'dload-app',
    templateUrl:'./treeDownloadText.html'
})
export class TreeDownloadTextComponent implements OnInit {
    constructor(
        @Inject (TreesRequests) private treeserv: TreesRequests
    ) {}

    ngOnInit() {

    }

    @Input() fileName: string;
    @Input() selectedDocList: Doc;
    @Input() modelFormRevision: ModelFormRevision;
    @Input() modelTreeFile: ModelTreeFile;

    public isComponent: boolean = true;

    /*Кнопка сохранения файла */
    public btnSaveLoadFile() {
        const id: number = this.selectedDocList.id;
        const idRev: number = this.modelFormRevision.idRev;
        this.treeserv.getDocConvert(id, idRev).subscribe(get => {
            this.isComponent = false;
        });
    }

    btnBack() {
        this.isComponent = false;
    }

}