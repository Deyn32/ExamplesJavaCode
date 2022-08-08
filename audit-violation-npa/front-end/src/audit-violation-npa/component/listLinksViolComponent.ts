/**
 * Created by dmihaylov on 19.03.2018.
 */
import {Component, OnInit, Inject, ViewChild, ElementRef}from '@angular/core';
import { EmptyLinkRequest } from '../requests/emptyLinkRequest';
import { MainTableRequests } from '../requests/mainTableRequests';
import { CalendarService } from '../services/calendarService';
import { DateTransformService } from '../services/dateTransformService';
import { ExpandTreeService } from '../services/expandTreeService';
import { ListLinks } from '../classes/listLinks';
import { ListRevsLinks } from '../classes/listRevsLinks';
import { DetailLinkTree } from '../classes/detailLinkTree';
import { Message } from 'primeng/api';
import { MessageService } from 'primeng/components/common/messageservice';
import { SelectItem} from "primeng/api";
import { Revision } from "../classes/revision";
import { NPA } from "../classes/npa";
import { CompareTrees } from "../classes/compareTrees";
import { SaveStatusModel } from "../classes/saveStatusModel";
import { ViolationTreeData } from "../classes/violationTreeData";
import { LinkNPA } from "../classes/linkNPA";
import { ListRevLinksData } from "../classes/ListRevLinksData";
import {analyseData} from "../classes/analyseData";

@Component({
    selector: 'list-links-app',
    templateUrl: './listLinksViol.html',
    providers: [MessageService],
})

export class ListLinksViolComponent implements OnInit {
    constructor(
        @Inject (EmptyLinkRequest) private elr: EmptyLinkRequest,
        @Inject (MainTableRequests) private mainTablReq : MainTableRequests,
        @Inject (CalendarService) private ru: CalendarService,
        @Inject (DateTransformService) private dateTransform: DateTransformService,
        @Inject (ExpandTreeService) private expandService: ExpandTreeService,
        private messageService: MessageService
    ) {
        this.npa = new NPA();
        this.compareTrees = new CompareTrees();
        this.compareText = new CompareTrees();
        this.valueTree = new CompareTrees();
        this.initStatusDropDown();
        this.initStructElemStatusRev();
        this.saveState = new SaveStatusModel();
        this.violTreeData = new ViolationTreeData(null);
        this.selectedRevsLinks = new ListRevsLinks();
        this.selectedLinks = new LinkNPA();
        this.selectedViol = new ListLinks(null);
    }

    @ViewChild('EditDateViolForm') private editDateViolForm: ElementRef;

    public disRefrash: boolean = true;
    public disSaveState: boolean = false;
    public disMergeTreeDialog: boolean = false;
    public disCompareStruct: boolean = true;
    public disMergeRevs: boolean = true;
    public disRegrouping: boolean = true;
    public isProgressBar: boolean = false;
    public visibleSideBar: boolean = false;
    public isSelectStatus: boolean = false;
    public disableStatus: boolean = false;
    public disEditDateViol: boolean = false;
    public disDialogMergeStruct: boolean = false;
    public checked: boolean = false;
    public isDiffs: boolean = true;
    public dateFilter: string;
    public statusDropDown: SelectItem[] = [];
    public dtBegin1: Date;
    public dtBegin2: Date;
    public dtEnd1: Date;
    public dtEnd2: Date;
    public compareTrees: CompareTrees;
    public compareText: CompareTrees;
    public valueTree: CompareTrees;
    public valuesForMerge: any[] = [];
    public npa: NPA;
    public linkText: string = "";
    public statusBar: string = "";
    public gridViolTable: number = 12;
    public gridRevsTable: number = 0;
    public gridLeftMenu: number = 11;
    public gridRigthMenu: number = 0;
    public lengthDataViol: number = 0;
    public lengthFilter: number = 0;
    public text1: string = "";
    public text2: string = "";
    public blockedPanel: boolean = false;
    public listLinks: ListLinks[] = [];
    public selectedViol: ListLinks;
    public selectedLinks: LinkNPA;
    public listRevsLinks: ListRevsLinks[] = [];
    public selectedRevsLinks: ListRevsLinks;
    public structLink: DetailLinkTree[] = [];
    public msgs: Message[] = [];
    public selectRowViol: ListLinks[] = [];
    public rus: any;
    public dtBeginViol: Date;
    public dtEndViol: Date;
    public statusStructElemRev: SelectItem[] = [];
    public sizeLevel: boolean = false;
    public hashDiffs: string = "Отличий не найдено";


    private dtNPA: Date = null;
    private id: number = 0;
    private idRev: number = 0;
    private idList: number = 0;
    private idRevs: number[] = [];
    private values: number[] = [];
    private revs: Revision[] = [];
    private status: string = "";
    private oldState: string = "";
    private errorStatus: string = "";
    private saveState: SaveStatusModel;
    private violTreeData: ViolationTreeData;

    ngOnInit() {
        this.ru.settings();
        this.rus = this.ru.getRuLocale();
        this.refrash();
        this.disRegrouping = false;
        this.disRefrash = false;
    }

    showProgressBar() {
        this.statusBar = "";
        this.isProgressBar = true;
    }

    hideProgressBar() {
        this.statusBar = "";
        this.isProgressBar = false;
    }

    initStructElemStatusRev() {
        this.elr.getFindStructElemStatus().subscribe(get => {
            let statuses = get.json();
            for (let status of statuses){
                switch (status){
                    case "NONE": {
                        this.statusStructElemRev.push({label: '', value: null});
                        break;
                    }
                    case "NOT_ANALYSED": {
                        this.statusStructElemRev.push({label: 'Не анализировалась', value: 'NOT_ANALYSED'});
                        break;
                    }
                    case "NOT_FOUND": {
                        this.statusStructElemRev.push({label: 'Не найдено положение', value: 'NOT_FOUND'});
                        break;
                    }
                }
            }
        });
    }

    initStatusDropDown() {
        this.elr.getStatusDropDown().subscribe( get => {
            let statuses = get.json();
            for(let status of statuses) {
                switch (status){
                    case "YES": {
                        this.statusDropDown.push({label: 'Да', value: `${status}`});
                        break;
                    }
                    case "NO": {
                        this.statusDropDown.push({label: 'Нет', value: `${status}`});
                        break;
                    }
                    case "NOT_KNOWN": {
                        this.statusDropDown.push({label: 'Не определено', value: `${status}`});
                        break;
                    }
                }
            }
        });
    }

    btnRefrash() {
        this.disRefrash = true;
        this.refrash();
    }

    private refrash() {
        this.dateFilter = "";
        this.disRefrash = true;
        this.showProgressBar();
        this.elr.getListLinks().subscribe(get => {
            this.listLinks = get.json();
            this.listLinks = this.processingListLinks(this.listLinks);
            this.disRefrash = false;
            this.hideProgressBar();
            this.lengthDataViol = this.listLinks.length;
            this.lengthFilter = this.listLinks.length;
        }, e => {
            console.log(e);
            this.hideProgressBar();
        });
    }

    private processingListLinks(list: ListLinks[]) {
        for (let link of list) {
            link.dtBegin = new Date(link.dtBegin);
            if(link.dtEnd != null){
                link.dtEnd = new Date(link.dtEnd);
            }
            else {
                link.dtEnd = null;
            }
            for(let linkNpa of link.links) {
                if(link.filterText == null)
                    link.filterText = linkNpa.npa.num;
                else
                    link.filterText += linkNpa.npa.num;
            }
        }
        return list;
    }

    onRowSelectLink(event) {
        this.refrashRevs(event);
    }

    private initListRevsLinks(id: number) {
        this.showProgressBar();
        this.elr.getListRevsLinks(id).subscribe( list => {
            this.listRevsLinks = list.json();
            this.listRevsLinks = this.processingListRevsLinks(this.listRevsLinks);
            if(this.listRevsLinks.length == 1 && this.listRevsLinks[0].editable != false){
                this.valuesForMerge[0] = this.listRevsLinks[0];
                this.onMergeSelected();
            }
            this.hideProgressBar();
        });
    }

    private refrashRevs(event){
        this.clearListRevs();
        this.valuesForMerge.length = 0;
        this.onMergeSelected();
        this.showProgressBar();
        this.showRevsGrid();
        this.showRigthMenuGrid();
        this.id = event.data.id;
        this.npa = new NPA();
        this.npa = event.data.npa;
        this.linkText = event.data.text;
        this.dtNPA = event.data.npa.date;
        this.initListRevsLinks(this.id);
    }

    private processingListRevsLinks(list: ListRevsLinks[]) {
        for(let i = 0; i < list.length; i++) {
            list[i].dtStart = new Date(list[i].dtStart);
            if(list[i].dtEnd != null)
                list[i].dtEnd = new Date(list[i].dtEnd);
            if(list[i].structElemRevState != "NONE")
                list[i].editable = false;
            list[i].id = i;
            if(list[i].structElemRevState == "NOT_ANALYSED" || list[i].structElemRevState == "NOT_FOUND")
                list[i].disabled = true;
            if(list[i].revisions.length > 0) {
                for(let rev of list[i].revisions) {
                    if(rev != null)
                        rev.dtNPA = this.dtNPA;
                    else {
                        rev = new Revision();
                    }
                }
            }
        }
        return list;
    }

    onRowSelectRevLink(event) {
        if(this.id != null) {
            this.showProgressBar();
            if(event.data.revisions != null) {
                this.elr.getDetailLinks(this.id, event.data.revisions[0].id).subscribe( get => {
                    this.getDetail(get);
                })
            }
            else {
                this.elr.getDetailLinks(this.id).subscribe( get => {
                    this.getDetail(get);
                })
            }
        }
    }

    btnViewStruct(row){
        if(this.id != null){
            this.idRev = row.revisions[0].id;
            this.dtBegin1 = row.dtStart;
            this.dtEnd1 = row.dtEnd;
            this.showProgressBar();
            this.elr.getDetailLinks(this.id, this.idRev).subscribe( get => {
                this.getDetail(get);
                this.disDialogMergeStruct = true;
            })
        }
    }

    private getDetail(get: any) {
        this.structLink = get.json();
        this.expandService.expandChildren(this.structLink);
        this.hideProgressBar();
    }

    onRowRevLinkCollapse() {
        this.idRev = null;
        this.valuesForMerge.length = 0;
    }

    onRowNPAExpand(event) {
        this.selectedViol = event.data;
        this.hideRevsGrid();
        this.hideRigthMenuGrid();
        this.clearListRevs();
    }

    onRowNPACollapse(event) {
        this.selectedViol = event.data;
        this.hideRevsGrid();
        this.hideRigthMenuGrid();
        this.clearListRevs();
    }

    private clearListRevs() {
        this.listRevsLinks = [];
    }

    private hideRigthMenuGrid() {
        this.gridLeftMenu = 11;
        this.gridRigthMenu = 0;
    }

    private showRigthMenuGrid() {
        this.gridLeftMenu = 6;
        this.gridRigthMenu = 5;
    }

    private hideRevsGrid() {
        this.gridViolTable = 12;
        this.gridRevsTable = 0;
    }

    private showRevsGrid() {
        this.gridViolTable = 7;
        this.gridRevsTable = 5;
    }

    btnMergeRevs() {
        this.hashDiffs = "Отличий не найдено";
        CompareTrees.flag = false;
        if(this.valuesForMerge.length == 2){
            this.sizeLevel = false;
            this.showProgressBar();
            this.elr.getCompareStructAbs(this.id, this.values[0], this.values[1]).subscribe(get => {
                try {
                    let obj = get.json();
                    this.compareTrees = this.initCompareValue(this.compareTrees, obj);
                    this.valueTree = this.compareTrees;
                    this.disMergeTreeDialog = true;
                }
                catch (error){
                    this.msgs = [];
                    this.msgs.push({severity:'error', summary:'Внимание', detail:'Произошла ошибка при сравнении реакций!'});
                }
                finally {
                    this.hideProgressBar();
                }
            })
        } else {
            this.msgs = [];
            this.msgs.push({severity:'error', summary:'Внимание', detail:'Выберите два положения!'})
        }
    }

    private initCompareValue(value: CompareTrees, obj: any){
        value = new CompareTrees();
        value = CompareTrees.createMergedTree(value, obj);
        this.isDiffs = !value.hasDiffs;
        this.expandService.expandChildren(value.children);
        return value;
    }

    onMergeSelected() {
        if(this.valuesForMerge.length == 1){
            this.disCompareStruct = false;
        }
        else {
            this.disCompareStruct = true;
        }
        if (this.valuesForMerge.length == 2) {
            this.disMergeRevs = false;
            this.text1 = "";
            this.text2 = "";
            this.dateClear();
            for(let i = 0; i < this.valuesForMerge.length; i++) {
                this.revs = this.valuesForMerge[i].revisions;
                if(i == 0) {
                    this.dtBegin1 = this.valuesForMerge[i].dtStart;
                    this.dtEnd1 = this.valuesForMerge[i].dtEnd;
                } else {
                    this.dtBegin2 = this.valuesForMerge[i].dtStart;
                    this.dtEnd2 = this.valuesForMerge[i].dtEnd;
                }
                this.values[i] = this.revs[this.revs.length - 1].id;
            }
        }
        else {
            this.disMergeRevs = true;
        }
    }

    btnShowCondition() {
        this.visibleSideBar = true;
    }

    onFocusStatus(row: ListRevsLinks) {
        this.status = row.state;
    }

    onChangeStatus(row: ListRevsLinks) {
        this.oldState = this.status;
        this.status = row.state;
        this.idList = row.id;
        this.idRevs = [];
        for(let i = 0; i < row.revisions.length; i++){
            this.idRevs[i] = row.revisions[i].id;
        }
        this.disableStatus = true;
        this.disSaveState = true;
    }

    btnYesSaveState(){
        this.showProgressBar();
        this.initSaveStateModel();
        this.elr.postSaveStatus(this.saveState).subscribe(post => {
            this.isSelectStatus = post.json();
            if(this.isSelectStatus) {
                this.msgs = [];
                this.msgs.push({severity:'success', summary:'Внимание', detail:'Статус сохранен!'});
                this.initListRevsLinks(this.id);
            }
            else {
                this.msgs = [];
                this.msgs.push({severity:'error', summary:'Внимание', detail:'Статус не был сохранен!'});
                this.listRevsLinks[this.idList].state = this.oldState;
            }
            this.hideProgressBar();
            this.hideDialogSaveState();
        });
    }

    private initSaveStateModel() {
        this.saveState = new SaveStatusModel();
        this.saveState.idNpaLink = this.id;
        this.saveState.idRevs = this.idRevs;
        this.saveState.oldState = this.oldState;
        this.saveState.newState = this.status;
    }

    btnCancelSaveState() {
        this.msgs = [];
        this.msgs.push({severity:'error', summary:'Внимание', detail:'Статус не был сохранен!'});
        this.listRevsLinks[this.idList].state = this.oldState;
        this.hideDialogSaveState();
    }

    private hideDialogSaveState() {
        this.disSaveState = false;
        this.disableStatus = false;
    }

    clear() {
        this.messageService.clear();
    }

    btnFilter() {
        this.showProgressBar();
        if(this.dateFilter != null && this.dateFilter != "")  {
            let lists: string[];
            lists = this.dateFilter.split('.');
            let date: string = "";
            if(lists != null)
                date = lists[2] + "-" + lists[1] + "-" + lists[0];
            if(date != "гггг-мм-дд"){
                this.elr.getFilterDate(date).subscribe(get => {
                    this.listLinks = get.json();
                    this.listLinks = this.processingListLinks(this.listLinks);
                    this.lengthFilter = this.listLinks.length;
                    this.hideProgressBar();
                });
            }
        }
        else {
            this.hideProgressBar();
            this.refrash();
        }
    }

    btnEditDateViol(row: ListLinks) {
        this.selectRowViol = [];
        this.selectRowViol[0] = row;
        this.dtBeginViol = row.dtBegin;
        this.dtEndViol = row.dtEnd;
        this.blockedPanel = true;
        this.disEditDateViol = true;
    }

    btnCancelDateViol(){
        this.disEditDateViol = false;
        this.blockedPanel = false;
    }

    Submit() {
        this.showProgressBar();
        let listLinks = new ListLinks(this.selectRowViol[0]);
        let obj = this.editDateViolForm['form']._value;
        obj = this.dateTransforming(obj);
        listLinks.dtBegin = obj.dtBegin;
        listLinks.dtEnd = obj.dtEnd;
        this.requestSaveDate(listLinks);
    }

    private dateTransforming(obj: any) {
        obj.dtBegin = this.dateTransform.transformIso(obj.dtBegin);
        if(obj.dtEnd != null){
            obj.dtEnd = this.dateTransform.transformIso(obj.dtEnd);
        }
        return obj;
    }

    private requestSaveDate(listLinks: any) {
        this.violTreeData = new ViolationTreeData(listLinks);
        this.elr.postEditDateViol(this.violTreeData).subscribe( post => {
            let flag: boolean = post.json();
            if(flag){
                let index = this.listLinks.indexOf(this.listLinks.find(d => d.id == listLinks.id));
                this.listLinks[index].dtBegin = listLinks.dtBegin;
                this.listLinks[index].dtEnd = listLinks.dtEnd;
            }
            else{
                this.msgs = [];
                this.msgs.push({severity:'error', summary:'Внимание', detail:'Произошла ошибка при сохранении отредактированных дат!'})
            }
            this.blockedPanel = false;
            this.disEditDateViol = false;
        });
        this.hideProgressBar();
    }

    btnCompareStructViolAbs(){
        this.text1 = "Текст нарушения";
        this.text2 = "Текст положения";
        this.hashDiffs = "";
        CompareTrees.flag = true;
        this.dateClear();
        if(this.valuesForMerge.length == 1){
            this.showProgressBar();
            this.elr.getCompareStructViolAbs(this.id, this.valuesForMerge[0].revisions[0].id).subscribe(get => {
                let obj = get.json();
                this.compareText = this.initCompareValue(this.compareText, obj);
                if(this.compareText.children.length == 1)
                    this.sizeLevel = true;
                this.valueTree = this.compareText;
                this.hideProgressBar();
                this.disMergeTreeDialog = true;
            });
        } else {
            this.msgs = [];
            this.msgs.push({severity:'error', summary:'Внимание', detail:'Выберите одно положение!'})
        }
    }

    onChange(){
        if(this.checked){
            this.showProgressBar();
            this.elr.getFilterDoubtful().subscribe(get => {
                this.listLinks = get.json();
                this.listLinks = this.processingListLinks(this.listLinks);
                this.lengthFilter = this.listLinks.length;
                this.hideProgressBar();
            })
        } else {
            this.refrash();
        }
    }

    dateClear(){
        this.dtBegin1 = null;
        this.dtBegin2 = null;
        this.dtEnd1 = null;
        this.dtEnd2 = null;
    }

    btnNotSelect(){
        this.msgs = [];
        this.msgs.push({severity:'error', summary:'Внимание', detail:'Нельзя выбрать данную группу редакций положения!'})
    }

    onFilterDoc(event){
        this.lengthFilter = event.filteredValue.length;
    }

    onFilterLinks(event){
        this.lengthFilter = event.filteredValue.length;
    }

    btnRegrouping(){
        this.showProgressBar();
        this.disRegrouping = true;
        this.disRefrash = true;
        let idNpaStruct = this.selectedLinks.id;
        let idNpa = this.selectedLinks.npa.id;
        this.listRevsLinks = [];
        this.recursiveWaitCell(null, idNpaStruct, idNpa);
    }

    private recursiveWaitCell(id: string, idNpaStruct: number, idNpa: number){
        this.elr.getRegrouping(id, idNpaStruct, idNpa).subscribe(get => {
            if(get.text() == null || get.text() == ''){
                if(idNpaStruct == null)
                    this.errorStatus = "Запрос в обработке";
                else
                    this.errorStatus = "Запрос удален из очереди";
                this.isProgressBar = false;
                return;
            }
            let data : ListRevLinksData = get.json();
            if(data.completed){
                if (data.error != null){
                    this.errorStatus = data.error;
                    this.listRevsLinks = [];
                }
                else {
                    this.status = "Запрос обработан";
                }
                if(data.data != null){
                    this.listRevsLinks = data.data;
                    this.listRevsLinks = this.processingListRevsLinks(this.listRevsLinks);
                    if(this.listRevsLinks.length == 1 && this.listRevsLinks[0].editable != false){
                        this.valuesForMerge[0] = this.listRevsLinks[0];
                        this.onMergeSelected();
                    }
                }
                else {
                    this.msgs = [];
                    this.msgs.push({severity:'error', summary:'Внимание', detail:'Не удалось перегруппировать редакции!'});
                }
                this.disRefrash = false;
                this.disRegrouping = false;
                this.hideProgressBar();
            }
            else{
                this.statusBar = data.status;
                setTimeout(() => this.recursiveWaitCell(data.id, idNpaStruct, idNpa), 3500);
            }
        },  e => {
            this.errorStatus = e.status  + " " + e.statusText + " " + e.statusMessage;
            console.log(e);
            this.disRegrouping = false;
            this.disRefrash = false;
            this.hideProgressBar();
            }
        )
    }
}