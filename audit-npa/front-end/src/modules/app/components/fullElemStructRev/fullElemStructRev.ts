/**
 * Created by dmihaylov on 28.02.2018.
 */
import {Component, OnInit, Input, Inject, ElementRef, ViewChild} from '@angular/core';
import {SelectItem} from "primeng/api";
import {CalendarService} from "../../services/calendarService";
import {Doc} from "../../classes/Docs";
import {ModelFormRevision} from "../../classes/modelFormRevision";
import { ModelTree } from "../../classes/modelTree";
import {TableStructFull} from "../../classes/tableStructFull";
import {DataTree} from "../../classes/dataTree";
import { GrowlService } from '../../services/growlService';
import {Dictionary} from "../../requests/dictionaryRequests";
import {DictinaryElementType} from "../../services/dictionaryService";
import {TreesRequests} from "../../requests/treeRequests";

@Component({
    selector: 'fullelem-app',
    templateUrl: './fullElemStructRev.html'
})

export class FullElemStructRevComponent implements OnInit {
    constructor(
        @Inject (CalendarService) private  ru: CalendarService,
        @Inject (Dictionary) private dict: Dictionary,
        @Inject (TreesRequests) private treeserv: TreesRequests,
        @Inject (GrowlService) private growlserv: GrowlService
    ) {}

    ngOnInit() {
        this.ru.settings();
        this.rus = this.ru.getRuLocale();
        this.initTable(this.get);
    }

    @ViewChild('StrForm') private strForm: ElementRef;

    @Input() selectedDocList: Doc;
    @Input() modelFormRevision: ModelFormRevision;
    @Input() selectedTree: ModelTree;
    @Input() elemTypes: SelectItem[];
    @Input() get: any;

    public tabRevfull: TableStructFull[];

    public selectedElemType: SelectItem;
    public elemDisabled: boolean = false;
    public disAddElemType = true;
    public isComponent = true;

    public rus: any;

    private initTable(get: any) {
        this.tabRevfull = [];
        this.tabRevfull[0] = DataTree.strVal(get.text(), this.selectedTree.data);
        if (this.selectedTree.data.type != "") {
            this.elemDisabled = true;
            for (let i: number = 0; i < this.elemTypes.length; i++) {
                if (this.selectedTree.data.type == this.elemTypes[i].label) {
                    this.selectedElemType = this.elemTypes[i];
                    break;
                }
            }
        }
        else {
            this.selectedElemType = this.elemTypes[0];
            this.elemDisabled = false;
        }
    }

    public onChangeElemType(event) {
        if(this.selectedElemType.label == "" && this.selectedElemType.value != "")
            this.disAddElemType = false;
        else
            this.disAddElemType = true;
    }

    private checkOnElemType(): string {
        for (let i: number = 1; i < this.elemTypes.length; i++) {
            if (this.elemTypes[i].label == this.selectedElemType.value)
                return null;
        }
        return this.selectedElemType.value;

    }

    private getDict() {
        this.elemTypes = [];
        this.dict.getElemTypes().subscribe( get => {
            this.elemTypes = DictinaryElementType.getDict(get);
        });
    }

    /*Кнопка добавления елемента в словарь*/
    public btnAddElem(): void {
        const str = this.checkOnElemType();
        if (str != null) {
            this.dict.postElemType(str).subscribe(post => {
                this.getDict();
                for(let i: number = 0; i < this.elemTypes.length; i++) {
                    if(this.elemTypes[i].label == str)
                        this.selectedElemType = this.elemTypes[i];
                }
                this.disAddElemType = true;
            })
        }
        else
            this.growlserv.showError("Такой элемент уже существует!");
    }

    /*Сабмит кнопки Сохранить в диалоге полной версии единицы структуры редакции */
    public strSubmit() {
        const idStr: string = this.selectedTree.data.id;
        const idDoc: number = this.modelFormRevision.idDoc;
        const idRev: number = this.modelFormRevision.idRev;
        const obj: any = DataTree.objSubmit(this.strForm['form'], this.selectedTree.data, this.tabRevfull[0].label);
        this.treeserv.postDocStrDates(obj, idStr, idDoc, idRev).subscribe(post => {
            this.isComponent = false;
        });
    }

   btnBack() {
       this.isComponent = false;
   }
}
