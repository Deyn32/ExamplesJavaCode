import {Component, ElementRef, Inject, OnInit, ViewChild, HostListener} from '@angular/core';
import { Message, SelectItem } from 'primeng/primeng';
import 'rxjs/add/operator/catch';
import 'file-saver';
import {isUndefined} from 'util';
import { Doc } from '../classes/Docs';
import { Error } from '../classes/error';
import { MergedTree } from '../classes/mergedTree';
import { ModelFormDocument } from '../classes/modelFormDocument';
import { ModelFormRevision} from '../classes/modelFormRevision';
import { ModelTree } from '../classes/modelTree';
import { ModelTreeFile } from '../classes/ModelTreeFile';
import { DataTree } from "../classes/dataTree";
import { RevSubmit } from '../classes/RevSubmit';
import { TableRevValue } from '../classes/tableRevValue';
import { TableStructFull } from '../classes/tableStructFull';
import { Orgs } from '../classes/Orgs';
import { DocumentNPARequests } from '../requests/documentNPARequests';
import { RevisService } from '../requests/revisionRequest';
import { TreesRequests } from '../requests/treeRequests';
import { OrganizationRequests } from '../requests/organizationRequests';
import { CalendarService } from '../services/calendarService';
import { Head } from '../services/createHeaderService';
import { DateConvertService } from '../services/dateConvertService';
import { GrowlService } from '../services/growlService';
import { Dictionary } from '../requests/dictionaryRequests';
import { RoutingService } from "../services/routingService";
import {DictinaryElementType} from "../services/dictionaryService";
import { saveAs } from 'file-saver/FileSaver.js';

@Component({
    selector: 'sg-comp-app',
    templateUrl: './appComp.html'
})

export class AuditComponent implements OnInit {
    constructor(@Inject (CalendarService) private  ru: CalendarService,
                @Inject (DocumentNPARequests) private docreq: DocumentNPARequests,
                @Inject (RevisService) private revisserv: RevisService,
                @Inject (GrowlService) private growlserv: GrowlService,
                @Inject (TreesRequests) private treeserv: TreesRequests,
                @Inject (DateConvertService) private dateconv: DateConvertService,
                @Inject (OrganizationRequests) private orgReq: OrganizationRequests,
                @Inject (Dictionary) private dict: Dictionary,
                @Inject (Head) private head: Head,
                @Inject (Error) private error: Error) {
        this.modelFormDocs = new ModelFormDocument();
        this.modelFormRevision = new ModelFormRevision();
        this.modelTreeFile = new ModelTreeFile();
        this.mergedTree = new MergedTree();
        this.selectedDocList = new Doc();
    }

    /*Переменные элементов HTML */
    @ViewChild('addForm') private form: ElementRef;
    @ViewChild('RevForm') private revForm: ElementRef;
    @ViewChild('fileload') private fileload: ElementRef;
    @ViewChild('gb') private gb: ElementRef;
    @ViewChild('StrForm') private strForm: ElementRef;

    /*Глобальные переменные для работы с компонентами PrimeNG*/
    public docNum: string;
    public docDate: Date;
    public revis: ModelFormRevision;
    public modelTree: ModelTree[] = [];
    public htmlText: string = "";
    public lengthRev: number = 0;
    public lengthDoc: number = 0;
    public lengthDocFilter: number = 0;
    public doc: Doc = new Doc();
    public idRev1: number = 0;
    public idRev2: number = 0;
    public fileName: string;
    public switchboard: string = RoutingService.baseComp;
    public flagDocEdit: boolean = false;
    public flagRevEdit: boolean = false;
    public displayPaste: boolean = false;
    public displayTreeFile: boolean = false;
    public displayLoading: boolean = false;
    public displayAddRevis: boolean = false;
    public displayDelResponse: boolean = false;
    public displayDelDecResponse: boolean = false;
    public isProgressBar: boolean = false;
    public delFlag: boolean = true;
    public editFlag: boolean = true;
    public disEditRev: boolean = true;
    public disDelRev: boolean = true;
    public mergeDisabled: boolean = true;
    public compareDisabled: boolean = true;
    public disFullViewing: boolean = true;
    public selectedEditingDrop: string = "";
    public rus: any;
    public tableFlag: boolean = false;
    public arrTypes: SelectItem[];
    public idNPARev: SelectItem[];
    public docTypeFilters: SelectItem[];
    public organizations: SelectItem[];
    public selectOrganizations = [];
    public textType: string;
    public idType: string;
    public documentsForTable: Doc[];
    public selectedDocList: Doc;
    public id: string [];
    public msgs: Message[] = [];
    public formDocType: any;
    public res: string;
    public tableRevValue: TableRevValue[] = [];
    public modelFormDocs: ModelFormDocument;
    public modelFormRevision: ModelFormRevision;
    public modelTreeFile: ModelTreeFile;
    public tabRevfull: TableStructFull[];
    public valuesForMerge: number[];
    public mergedTree: MergedTree;
    public dtRev1: Date;
    public dtRev2: Date;
    public resp: any;
    public strHtml: string = "";
    public expanded: any[] = [];
    public selectedElemType: SelectItem;
    public selectedTree: ModelTree;
    public get: any;
    public flagFullView: boolean = true;
    public elemTypes: SelectItem[];
    public elemDisabled: boolean = false;
    public disAddElemType = true;
    public defaultLabel: SelectItem[] = [];
    public isOrgs = false;
    //Флаги режимов
    public flag: boolean = true;
    public flagMerge: boolean = false;
    public flagCompareRev: boolean = false;
    public flagImageView: boolean = false;
    public flagDloadText: boolean = false;
    public flagStructRev: boolean = false;
    public selectedItemsLabel: string = "";

    public leftTable: Date;
    public rightTable: Date;
    public scrollHeight: string;

    private idDoc: number = 0;
    private orgs: Orgs[] = [];


    /*Начальная инициализация */
    public ngOnInit() {
        this.filtersDocType();
        this.docTypeDropDown();
        this.ru.settings();
        this.rus = this.ru.getRuLocale();
        this.tableView();
        this.collapseRow();
        this.getDict();
    }

    private getDict() {
        this.elemTypes = [];
        this.dict.getElemTypes().subscribe( get => {
            this.elemTypes = DictinaryElementType.getDict(get);
        });
    }

    private initElemTable(get: any) {
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

    /*Заполнение дропбокса типов документа*/
    private docTypeDropDown(): void {
        this.arrTypes = [];
        this.dict.getDocTypes().subscribe(type => {
            this.formDocType = type.json();
            for (const docType of this.formDocType) {
                this.textType = docType.name;
                this.idType = docType.id;
                this.arrTypes.push({label: `${this.textType}`, value: `${this.idType}`});
            }
        });
    }

    private filtersDocType(): void {
        this.docTypeFilters = [];
        this.docTypeFilters.push({label: 'Все', value: null});
        this.docTypeFilters.push({label: 'Изм.', value: 'true'});
        this.docTypeFilters.push({label: 'Осн.', value: 'false'});
    }

    private showProgress(): void {
        this.displayLoading = true;
    }

    private hideProgress(): void {
        this.displayLoading = false;
    }

    /*Заполнение таблицы документов НПА */
    private tableView(): void {
        this.isProgressBar = true;
        this.editFlag = true;
        this.delFlag = true;
        this.docreq.getDataFindList().subscribe(d => {
            this.documentsForTable = this.dataFindList(d.json());
            if(this.documentsForTable.length == 0) {
                this.msgs.push(this.growlserv.showError(this.error.noDocuments()));
            }
            else {
                for (let doc of this.documentsForTable){
                    doc.date = this.dateconv.InRussianDate(doc.dt);
                }
                if (this.idDoc != 0) {
                    this.selectedDocList = Doc.findDoc(this.documentsForTable, this.idDoc);
                    Doc.sortDocIndex(this.documentsForTable, this.selectedDocList);
                    this.idDoc = 0;
                }
            }
            this.lengthDoc = this.documentsForTable.length;
            this.lengthDocFilter = this.documentsForTable.length;
            this.isProgressBar = false;
        });
    }

    private dataFindList(dataList: any): Doc[] {
        if (dataList.length == 0) {
                this.msgs.push(this.growlserv.showError(this.error.noDocuments()));
        }
        else {
            for (const docTab of dataList) {
                docTab.dt = this.dateconv.InUniversalDate(docTab.dt);
            }
        }
        return dataList;
    }

    public onClickRowTable(event) {
        this.collapseRow();
    }

    private collapseRow() {
        this.delFlag = true;
        this.editFlag = true;
        this.mergeDisabled = true;
        if (!isUndefined(this.valuesForMerge))
            this.valuesForMerge.length = 0;
    }

    private expandRow() {
        if (!isUndefined(this.valuesForMerge))
            this.valuesForMerge.length = 0;
        this.expanded[0] = this.selectedDocList;
        this.mergeDisabled = true;
        this.delFlag = false;
        this.editFlag = false;
        this.revView();
    }

    /*Событие свертывания строки таблицы документов НПА */
    public onRowCollapse(event) {
        this.collapseRow();
    }

    /*Событие развертывания строки таблицы документов НПА */
    public onRowExpand(event) {
        this.selectedDocList = event.data;
        this.expandRow();
    }

    /*Событие при фильтрации документов НПА */
    public onFilterDoc(event) {
        this.lengthDocFilter = event.filteredValue.length;
    }

    private showEdit(): void {
        this.displayPaste = true;
    }

    /*Кнопка добавления документа НПА */
    public btnPaste() {
        this.modelFormDocs = new ModelFormDocument();
        this.flagDocEdit = false;
        this.defaultLabel.length = 0;
        this.selectOrganizations.length = 0;
        this.isOrgs = false;
        this.organizations = [];
        this.selectedItemsLabel = "";
        this.orgReq.getOrganizations().subscribe( get => {
            this.orgs = get.json();
            for (let org of this.orgs) {
                this.organizations.push({label: `${org.shortName}`, value:`${org.shortName}`});
                this.showEdit();
            }
        });
    }

    /*Кнопка редактирования НПА */
    public btnEdit() {
        this.isOrgs = false;
        this.selectOrganizations.length = 0;
        this.editNPA();
    }

    private editNPA(): void {
        this.flagDocEdit = true;
        this.docreq.getCheckIsDeletedDoc(this.selectedDocList.id).subscribe(get => {
            if (!get.json()) {
                this.modelFormDocs = new ModelFormDocument();
                this.modelFormDocs.initOfField(this.selectedDocList.id, this.selectedDocList.name, this.selectedDocList.dt, this.selectedDocList.num, this.selectedDocList.docType['id'], this.selectedDocList.editing);
                let listId: number[] = [];
                this.defaultLabel.length = 0;
                this.organizations = [];
                this.orgReq.getOrganizations().subscribe( get => {
                    this.orgs = get.json();
                    for (let org of this.orgs) {
                        this.organizations.push({label: `${org.shortName}`, value:`${org.shortName}`});
                    }
                    this.orgReq.getOrgsNpa(this.selectedDocList.id).subscribe(get => {
                        listId = get.json();
                        if (listId.length != 0) {
                            for (let i: number = 0; i < listId.length; i++) {
                                for(let j: number = 0; j < this.orgs.length; j++) {
                                    if(listId[i] == this.orgs[j].id) {
                                        this.selectOrganizations[i] = this.orgs[j].shortName;
                                        break;
                                    }
                                }
                            }
                            this.selectedItemsLabel = "{0} организаций выбрано"
                        }
                        else {
                            this.selectOrganizations.length = 0;
                        }
                        this.showEdit();
                    });
                });
            }
            else {
                this.msgs.push(this.growlserv.showError(this.error.documentWasDeleted(this.selectedDocList.id)));

            }
        });
    }

    btnSelectOrgs() {
        this.isOrgs = true;
    }

    /*Обработка сабмита с формы документов НПА */
    public docSubmit() {
        this.submitDoc(this.form['form']._value);
    }

    private submitDoc(obj: any): void {
        if (isUndefined(obj.date) || obj.date == null) {
            this.msgs.push(this.growlserv.showError(this.error.enteredWrongDate()));
        }
        else {
            for (const docType of this.formDocType) {
                if (obj.type == docType.id) {
                    obj.type = docType;
                    break;
                }
            }
            obj.date = this.dateconv.InDBDate(obj.date);
            if (isUndefined(obj.number) || obj.number == null || obj.number == '') {
                this.msgs.push(this.growlserv.showError(this.error.notAllData()));
            }
            else {
                this.selectMetodDoc(obj);
            }
        }
    }

    private selectMetodDoc(obj: any): void {
        let arrayId: number[] = [];
        for (let i = 0; i < this.selectOrganizations.length; i++) {
            for (let j: number = 0; j < this.orgs.length; j++ ) {
                if( this.orgs[j].shortName == this.selectOrganizations[i].toString()) {
                    arrayId.push(this.orgs[j].id);
                    break;
                }
            }
        }
        if (this.flagDocEdit) {
            const id = this.selectedDocList.id;
            this.docreq.putAddNPA(obj, id).subscribe(put => {
                this.orgReq.getSaveOrgsNpa(arrayId, id).subscribe(get => {
                    this.displayPaste = false;
                    this.tableView();
                })
            });
        }
        else {
            this.docreq.getDataAddNPA(obj).subscribe(get => {
                this.idDoc = get.json();
                this.orgReq.getSaveOrgsNpa(arrayId, this.idDoc).subscribe(get => {
                    this.displayPaste = false;
                    this.tableView();
                })
            });
        }
    }

    private showDelDocResponse(): void {
        this.displayDelDecResponse = true;
    }

    private hideDelDocResponse(): void {
        this.displayDelDecResponse = false;
    }

    /*Кнопка удаления НПА */
    public btnDelete() {
        this.showDelDocResponse();
    }

    /*Кнопка Да в диалоге удаления документа */
    public btnDelDocAnswerYes() {
        this.delNPA();
    }

    private delNPA(): void {
        const id: number = this.selectedDocList.id;
        this.docreq.deleteAddNPA(id).subscribe(del => {
            this.selectedDocList = new Doc();
            this.tableView();
            this.editFlag = true;
            this.delFlag = true;
            this.hideDelDocResponse();
        });
    }

    /*Кнопка обновления таблицы */
    public btnRefresh() {
        this.selectedDocList = new Doc();
        this.tableView();
    }

    /*Заполнение таблицы редакций документа НПА */
    private revView(): void {
        this.isProgressBar = true;
        this.initVariables();
        const id: number = this.selectedDocList.id;
        let dateLabel: string;
        for (const docTab of this.documentsForTable) {
            if (docTab.editing) {
                dateLabel = this.dateconv.InRussianDate(docTab.dt);
                this.idNPARev.push({label: `${docTab.num + ' от ' + dateLabel}`, value: `${docTab.id}`});
            }
        }
        this.revisserv.getRevList(id).subscribe(rev => {
            this.tableRevValue = this.initRevView(rev.json(), this.documentsForTable);
            this.lengthRev = this.tableRevValue.length;
            this.isProgressBar = false;
        });
    }

    /* Инициализация переменных для редакций */
    private initVariables(): void {
        this.docDate = this.selectedDocList.dt;
        this.idNPARev = [];
        this.idNPARev.push({label: `Без документа`, value: ``});
        this.modelFormRevision = new ModelFormRevision();
    }

    /*Заполнение основной переменной редакций */
    private initRevView( obj: any, docs: Doc[]): TableRevValue[] {
        const tabRev: TableRevValue[] = [];
        for (let i: number = 0; i < obj.length; i++) {
            tabRev[i] = new TableRevValue();
            tabRev[i].idRev = obj[i].rev.id;
            tabRev[i].dtBegin = this.dateconv.InUniversalDate(obj[i].rev.dtBegin);
            tabRev[i].dtRev = this.dateconv.InUniversalDate(obj[i].rev.dtRev);
            tabRev[i].loaded = obj[i].loaded;
            tabRev[i].disabled = !obj[i].loaded;
            tabRev[i].idDoc = obj[i].rev.id_NPA;
            tabRev[i].id_NPA_Revision = obj[i].rev.id_NPA_Revision;
            if (obj[i].rev.dtEnd == null)
                tabRev[i].dtEnd = null;
            else
                tabRev[i].dtEnd = this.dateconv.InUniversalDate(obj[i].rev.dtEnd);
            if (tabRev[i].id_NPA_Revision != null)
                tabRev[i].npaRevDoc = docs.find(d => d.id == tabRev[i].id_NPA_Revision);
            else
                tabRev[i].npaRevDoc = null;
            if(tabRev[i].dtRev == null)
                tabRev[i].original = true;
            else
                tabRev[i].original = (tabRev[i].dtRev.valueOf() == this.selectedDocList.dt.valueOf());
        }
        return tabRev;
    }

    /*Событие выбора строки в таблице редакций */
     public onRowRevSelect(event) {
        this.modelFormRevision = event.data;
    }

    private showAddRevis() {
        this.displayAddRevis = true;
    }

    /*Кнопка добавления редакции */
    public btnAddRev() {
        if (this.selectedDocList != null) {
            this.modelFormRevision = new ModelFormRevision();
            this.flagRevEdit = false;
            this.showAddRevis();
        }
        else {
            this.msgs.push(this.growlserv.showError(this.error.notSelectedDocument()));
        }
    }

    /*Кнопка редактирования редакции */
    public btnEditRev(row: TableRevValue) {
        this.modelFormRevision = new ModelFormRevision();
        this.modelFormRevision.getField(row);
        this.editRev();
    }

    private editRev(): void {
        this.revisserv.getCheckIsDeletedRev(this.selectedDocList.id, this.modelFormRevision.idRev).subscribe(get => {
            if (!get.json()) {
                this.flagRevEdit = true;
                this.modelFormRevision.initField(this.modelFormRevision.idRev, this.modelFormRevision.dtBegin, this.modelFormRevision.dtRev, this.modelFormRevision.dtEnd, this.modelFormRevision.id_NPA_Revision, this.modelFormRevision.original);
                this.showAddRevis();
            }
            else {
                this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(this.modelFormRevision.idRev)));
            }
        });
    }

    /*Кнопка просмотра структуры редакции */
    public btnViewStruct(row: TableRevValue) {
        this.showProgress();
        this.revisserv.getCheckIsDeletedRev(this.selectedDocList.id, row.idRev).subscribe(get => {
            if (!get.json()) {
                this.modelFormRevision = new ModelFormRevision();
                this.modelFormRevision.getField(row);
                this.initTreeStructRev();
            }
            else {
                this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(row.idRev)));
            }

        });
    }

    private initTreeStructRev() {
        this.treeserv.getDataTree(this.selectedDocList.id, this.modelFormRevision.idRev).subscribe(get => {
            this.modelTree = this.dateconv.dateParseTree(get.json());
            this.flag = false;
            this.flagStructRev = true;
            this.switchboard = RoutingService.structRev;
            this.hideProgress();
        });
    }

    /*Обработка кнопки просмотка полной версии редакции документа */
    public btnFullViewing() {
        this.showProgress();
        if (this.selectedTree.data.type != ""){
            if (this.selectedElemType != null)
                this.selectedElemType.label = this.selectedTree.data.type;
        }
        const idStr: string = this.selectedTree.data.id;
        const idDoc: number = this.selectedDocList.id;
        const idRev: number = this.modelFormRevision.idRev;
        this.treeserv.getDocStrText(idStr, idDoc, idRev).subscribe(a => {
            this.initElemTable(a);
            this.flagFullView = false;
            this.switchboard = "fullver";
            this.hideProgress();

        });
    }

    public onNodeTreeSelect(event) {
        this.disFullViewing = false;
    }

    private showDelResponse() {
        this.displayDelResponse = true;
    }

    private hideDelResponse() {
        this.displayDelResponse = false;
    }

    /*Кнопка удаления редакции */
    public btnDelRev(row: TableRevValue) {
        this.modelFormRevision = new ModelFormRevision();
        this.modelFormRevision.getField(row);
        this.showDelResponse();
    }

    /*Обработка события при нажатии кнопки Да при удалении */
    public btnDelAnswerYes() {
        const idRev = this.modelFormRevision.idRev;
        this.revisserv.getDeleteRev(idRev).subscribe(del => {
            this.revView();
            this.hideDelResponse();
        });
    }

    /*Cабмит редакций */
    public revSubmit() {
        const obj = RevSubmit.getObjectRevSubmit(this.selectedDocList.id, this.revForm['form']);
        if (obj.dtRev != null) {
            if (obj.dtBegin != null) {
                if (this.revForm['form']._value.dtRev == null) {
                    obj.dtRev = this.selectedDocList.dt;
                }
                this.selectMetodRevSubmit(obj);
            }
            else {
                this.msgs.push(this.growlserv.showError('Не введена дата начала редакции!'));
            }
        }
        else {
            this.msgs.push(this.growlserv.showError('Не введена дата редакции!'));
        }
    }

    private selectMetodRevSubmit(obj: any) {
        if (this.flagRevEdit) {
            const idRev = this.modelFormRevision.idRev;
            this.revisserv.getPutRev(idRev, obj).subscribe(put => {
                this.displayAddRevis = false;
                this.revView();
            });
        }
        else {
            this.revisserv.getPostRev(obj).subscribe(post => {
                this.displayAddRevis = false;
                this.revView();
            });
        }
    }

    /*Событие подмены хедера */
    public onBeforeSendUploadFile(event) {
        this.showProgress();
        const obj: Object = this.treeserv.createHeader(this.head.createCsrfHeader());
        event.xhr.setRequestHeader(obj['key'][0], obj['value'][0]);
    }

    /*Событие подмены URL адреса */
    public onSelectUploadFile(event) {
        if(event.files[0].type == "text/plain"){
            this.fileload['url'] = this.treeserv.formattedURL();
        }
        else {
            this.msgs = [];
            this.msgs.push(this.growlserv.showError('Не верный формат файла!'));
        }
    }

    /*Событие загрузки файла */
    public onUploadFile(event) {
        this.uploadFile(event);
    }

    private uploadFile(event): void {
        if (!isUndefined((this.modelFormRevision.idRev) && this.modelFormRevision.idRev != null)) {
            this.fileName = event.files[0].name;
            if (event.xhr.status == 200) {
                this.modelTreeFile = JSON.parse(event.xhr.response);
                this.flag = false;
                this.flagDloadText = true;
                this.switchboard = RoutingService.dloadText;
            }
            else {
                this.msgs = [];
                this.msgs.push(this.growlserv.showError('Ошибка сервера: ' + event.xhr.status + '\n ' + event.xhr.statusText));
            }
            this.hideProgress();
        }
        else{
            this.msgs = [];
            this.msgs.push(this.growlserv.showError(this.error.notSelectedRevision()));
        }
        this.hideProgress();
    }

    /*Кнопка просмотра образа */
    public btnViewImage(row: TableRevValue) {
        this.revisserv.getCheckIsDeletedRev(this.selectedDocList.id, row.idRev).subscribe(resp => {
            if (!resp.json())
                this.viewImage(row);
            else
                this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(row.idRev)));
        });
    }

    /*Реализация просмотра образа*/
    private viewImage(row: TableRevValue): void {
        this.showProgress();
        this.modelFormRevision = new ModelFormRevision();
        this.modelFormRevision.getField(row);
        this.revisserv.getViewHTML(this.selectedDocList.id, this.modelFormRevision.idRev).subscribe(html => {
            if (html.text().length != 0) {
                this.doc = this.selectedDocList;
                this.revis = this.modelFormRevision;
                this.htmlText = html.text();
                this.flag = false;
                this.flagImageView = true;
                this.switchboard = RoutingService.imageView;
            }
            else {
                this.msgs.push(this.growlserv.showError(this.error.notLoadingRevision()));
            }
            this.hideProgress();
        });
    }

    /*Кнопка сравнения двух редакций */
    public btnMergeAndCompare() {
        const doc = this.selectedDocList.id;
        this.mergedTree = new MergedTree();
        this.valuesForMerge = this.mergedTree.sortedMergedRevision(this.valuesForMerge, this.selectedDocList, this.tableRevValue);
        const drevs = this.valuesForMerge;
        this.revisserv.getCheckIsDeletedRev(doc, +drevs[0]).subscribe(check1 => {
            if (!check1.json()) {
                this.revisserv.getCheckIsDeletedRev(doc, +drevs[1]).subscribe(check2 => {
                    if (!check2.json()) {
                        this.revisserv.compareRevisions(+doc, +drevs[0], +drevs[1]).subscribe(res => {
                            this.strHtml = res.text();
                            this.doc = this.selectedDocList;
                            this.findCompareRevs();
                            this.flag = false;
                            this.flagCompareRev = true;
                            this.switchboard = RoutingService.compareRevis;
                        });
                    }
                    else
                        this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(+drevs[1])));
                });
            }
            else
                this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(+drevs[0])));
        });
    }

    /*Доступ к кнопке сравнения*/
    public onMergeSelected() {
        this.mergeDisabled = this.valuesForMerge.length != 2;
        this.compareDisabled = this.valuesForMerge.length != 2;
    }

    /*Кнопка сравнения структур двух редакций */
    public btnCompareStruct() {
        this.showProgress();
        this.mergedTree = new MergedTree();
        this.valuesForMerge = this.mergedTree.sortedMergedRevision(this.valuesForMerge, this.selectedDocList, this.tableRevValue);
        const drevs = this.valuesForMerge;
        this.revisserv.getCheckIsDeletedRev(this.selectedDocList.id, +drevs[0]).subscribe(check1 => {
            if (!check1.json()) {
                this.revisserv.getCheckIsDeletedRev(this.selectedDocList.id, +drevs[1]).subscribe(check2 => {
                    if (!check2.json()) {
                        this.revisserv.compareStruct(this.selectedDocList.id, +drevs[0], +drevs[1]).subscribe(resp => {
                            this.compareTrees(resp);
                        });
                    }
                    else
                        this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(+drevs[1])));
                });
            }
            else
                this.msgs.push(this.growlserv.showError(this.error.revisionWasDeleted(+drevs[0])));
        });
    }

    /*Реализация сравнения структур двух редакций*/
    private compareTrees(resp: any): void {
        if (resp.json().children.length != 0) {
            this.resp = resp.json();
            this.doc = this.selectedDocList;
            this.findCompareRevs();
            this.hideProgress();
            this.flag = false;
            this.flagMerge = true;
            this.leftTable = this.dtRev1;
            this.rightTable = this.dtRev2;
            this.mergedTree = new MergedTree();
            this.mergedTree = this.mergedTree.createMergedTree(this.mergedTree, this.resp);
            this.switchboard = RoutingService.mergedTree;
        }
        else {
            this.msgs.push(this.growlserv.showSuccess('Структуры идентичны!'));
        }
        this.hideProgress();
    }

    private findCompareRevs() {
        let rev = TableRevValue.findRevision(this.tableRevValue, this.valuesForMerge[0]);
        this.dtRev1 = rev.dtRev;
        rev = TableRevValue.findRevision(this.tableRevValue, this.valuesForMerge[1]);
        this.dtRev2 = rev.dtRev;
    }

    rowTrackBy(index: any, item: any) {
        return item.id;
    }

    public clear() {
        this.msgs = [];
    }

    btnBack() {
        this.flag = true;
        this.flagMerge = false;
    }

    btnCompareRevBack() {
        this.flag = true;
        this.flagCompareRev = false;
    }

    btnImageViewBack() {
        this.flag = true;
        this.flagImageView = false;
    }

    btnDloadTextBack() {
        this.flag = true;
        this.flagDloadText = false;
    }

    btnStructRevBack() {
        this.flag = true;
        this.flagStructRev = false;
    }

    /*Скачать версию документа */
    public btnDownloadRev() {
        const id: number = this.doc.id;
        const idRev = this.revis.idRev;
        this.docreq.getDocumentDownload(id, idRev).subscribe(resp => {
            var data = resp.blob();
            var blob = new Blob([data], {type: 'application/pdf;charset=utf-8'});
            saveAs(blob, "output.pdf");
        });
    }

    /*Кнопка сохранения файла */
    public btnSaveLoadFile() {
        const id: number = this.selectedDocList.id;
        const idRev: number = this.modelFormRevision.idRev;
        this.treeserv.getDocConvert(id, idRev).subscribe(get => {
            this.flagDloadText = false;
            this.flag = true;
            this.msgs.push({severity: 'success',
                            summary: 'Внимание',
                            detail: 'Сохранение прошло успешно!'});
            this.revView();
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
            this.initTreeStructRev();
            this.switchboard = RoutingService.structRev;
        });
    }

    public onChangeElemType() {
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

    btnBackFullVer() {
        this.switchboard = RoutingService.structRev;
        this.initTreeStructRev();
    }

    setEventTypeLabel() {
        (document.getElementById('EventTypeSelection')
            .getElementsByClassName('ui-multiselect-label-container')[0]
            .getElementsByClassName('ui-multiselect-label')[0]).textContent = this.selectOrganizations.length + " организаций выбрано";
    }
}
