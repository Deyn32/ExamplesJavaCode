/**
 * Created by dmihaylov on 28.02.2018.
 */

import { Component, OnInit, Input, Inject } from '@angular/core';
import { TreesRequests } from "../requests/treeRequests";
import { ModelTree } from "../classes/modelTree";
import {SelectItem} from "primeng/api";
import {Doc} from "../classes/Docs";
import {ModelFormRevision} from "../classes/modelFormRevision";
import {RoutingService} from "../services/routingService";
import {DictinaryElementType} from "../services/dictionaryService";
import { Dictionary } from '../requests/dictionaryRequests';
import { DateConvertService } from '../services/dateConvertService';

@Component({
    selector: 'structrev-app',
    templateUrl: './treeStructRev.html'
})
export class TreeStructRevComponent implements OnInit{

    constructor(
        @Inject (TreesRequests) private treeserv: TreesRequests,
        @Inject (DateConvertService) private dateconv: DateConvertService,
        @Inject (Dictionary) private dict: Dictionary
    ) {
        this.selectedTree = new ModelTree();
    }

    ngOnInit() {
        this.getDict();
        this.initTable();
    }

    @Input() selectedDocList: Doc;
    @Input() modelFormRevision: ModelFormRevision;

    public switchboard: string = "main";

    public displayLoading: boolean = false;
    public disFullViewing: boolean = true;
    public selectedElemType: SelectItem;
    public modelTree: ModelTree[] = [];
    public selectedTree: ModelTree;
    public disAddElemType: boolean = false;
    public elemTypes: SelectItem[];
    public get: any;

    private getDict() {
        this.elemTypes = [];
        this.dict.getElemTypes().subscribe( get => {
            this.elemTypes = DictinaryElementType.getDict(get);
        });
    }

    /*Обработка кнопки просмотка полной версии редакции документа */
    public btnFullViewing() {
        this.showProgress();
        if (this.selectedTree.data.type != ""){ //Добавочная проверка, т.к. выбранный элемент теряет label (если нажать Сохранить и сразу открыть для просмотра)
            if (this.selectedElemType != null)
                this.selectedElemType.label = this.selectedTree.data.type;
        }
        const idStr: string = this.selectedTree.data.id;
        const idDoc: number = this.selectedDocList.id;
        const idRev: number = this.modelFormRevision.idRev;
        this.treeserv.getDocStrText(idStr, idDoc, idRev).subscribe(a => {
            this.get = a;
            this.hideProgress();
            this.switchboard = "fullver";
        });
    }

    private initTable() {
        this.treeserv.getDataTree(this.selectedDocList.id, this.modelFormRevision.idRev).subscribe(tr => {
            this.modelTree = this.dateconv.dateParseTree(tr.json());
        });
    }

    public onNodeTreeSelect(event) {
        this.disFullViewing = false;
    }

    private showProgress() {
        this.displayLoading = true;
    }

    private hideProgress() {
        this.displayLoading = false;
    }

    btnBack() {
        this.switchboard = RoutingService.baseComp;
    }

}