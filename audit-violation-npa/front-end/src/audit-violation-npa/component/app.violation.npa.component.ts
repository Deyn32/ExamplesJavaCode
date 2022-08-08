/**
 * Created by dmihaylov on 15.02.2018.
 */
import { Component, OnInit, Inject }from '@angular/core';
import { MainTableRequests } from '../requests/mainTableRequests';
import { MainTable } from "../classes/mainTable";
import { Revision } from '../classes/revision';
import { analyseData } from '../classes/analyseData';
import {MenuItem, SelectItem} from 'primeng/api';
import { Message } from 'primeng/api';
import {DatePipe} from "@angular/common";

@Component({
    selector: 'viol-npa-app',
    templateUrl: './violationNPA.html'
})

export class AuditViolationNpaComponent implements OnInit {
    constructor(
        @Inject (MainTableRequests) private mtr: MainTableRequests
    ) {

    }

    public ngOnInit() {
        this.selectedMainTable = new MainTable();
        this.initDropDownNpa();
        this.initDropDownViolations();
    }

    public mainTable: MainTable[] = [];
    public selectedMainTable: MainTable;
    public obj: any;
    public isProgressBar: boolean = false;
    public disStart: boolean = false;
    public disStop: boolean = true;
    public disSelect: boolean = true;
    public status:string;
    public npaDropDown: SelectItem[] = [];
    public selectNpa: string = null;
    public violDropDown: SelectItem[] = [];
    public selectViol: string = null;
    public items: MenuItem[];
    public lengthViol: number = 0;
    public lengthFind: number = 0;
    public msgs: Message[] = [];

    private errorStatus: string = "";
    private idNpa: number;
    private idViol: number;
    private dp: DatePipe = new DatePipe('ru-RU');

    btnStart() {
        this.isProgressBar = true;
        this.status = null;
        this.errorStatus = null;
        this.disStart = true;
        this.disStop = false;
        this.lengthViol = this.mainTable.length;
        this.lengthFind = this.mainTable.length;
        this.recursiveWaitCall(null);
    }

    private recursiveWaitCall( idRequest:string) {
        this.mtr.getDataTable(idRequest, this.idNpa, this.idViol).subscribe( a => {
            if (a.text() == null || a.text() == ''){
                if (idRequest == null){
                    this.errorStatus = "Запрос в обработке";
                }
                else{
                    this.errorStatus = "Запрос удален из очереди";
                }
                this.disStart = false;
                this.disStop = true;
                this.isProgressBar = false;
                return;
            }
            let data : analyseData = a.json();
            if (data.completed) {
                if (data.error != null) {
                    this.errorStatus = data.error;
                    this.mainTable = [];
                }
                else {
                    this.status = "Запрос обработан";
                    this.mainTable = Revision.checkRevDate(data.data);
                }
                if (data.data != null)
                    this.lengthViol = this.lengthFind = data.data.length;
                else
                    this.lengthViol = this.lengthFind = 0;
                this.isProgressBar = false;
                this.disStart = false;
                this.disStop = true;
            }
            else{
                this.status = data.status;
                setTimeout(() => this.recursiveWaitCall(data.id), 3500);
            }
        }, e => {
            this.errorStatus = e.status  + " " + e.statusText + " " + e.statusMessage;
            console.log(e);
            this.isProgressBar = false;
        });
    }

    btnStop() {
        this.disStop = true;
        this.isProgressBar = true;
        this.mtr.getStopAnalise().subscribe( () => {
            this.disStart = false;
            this.isProgressBar = false;
        });
    }

    public onFilterViol(event) {
        this.lengthFind = event.filteredValue.length;
    }

    btnFixLink(row: MainTable) {
        this.isProgressBar = true;
        this.mtr.postRepairLink(row.idLink).subscribe(post => {
            if (post.json()) {
                this.msgs.push({severity: 'success',
                    summary: 'Внимание',
                    detail: 'Исправление ссылки прошло успешно!'});
                row.result = "Исправлено";
            } else {
                this.msgs.push({severity: 'error',
                    summary: 'Внимание',
                    detail: 'Не удалось исправить ссылку!'});
            }
            this.isProgressBar = false;
        }, e => {
            this.errorStatus = e.status  + " " + e.statusText + " " + e.statusMessage;
            console.log(e);
            this.isProgressBar = false;
        })
    }

    public clear() {
        this.msgs = [];
    }

    initDropDownNpa() {
        this.isProgressBar = true;
        this.npaDropDown = [];
        this.npaDropDown.push({label: 'По всем документам', value: 0});
        this.mtr.getFindAllNpa().subscribe(get => {
            let obj = get.json();
            for(let i = 0; i < obj.length; i++) {
                if(obj[i].date != null) {
                    obj[i].date = this.dp.transform(obj[i].date, 'dd.MM.yyyy');
                }
                this.npaDropDown.push({label: `${ "№ " + obj[i].number + " " + obj[i].type.name} от ${obj[i].date}`, value: `${obj[i].id}`});
            }
            this.isProgressBar = false;
        });
    }

    onChangeNpa(){
        if(this.selectNpa != null)
            this.idNpa = parseInt(this.selectNpa);
        else
            this.idNpa = 0;
    }

    initDropDownViolations() {
        this.isProgressBar = true;
        this.violDropDown = [];
        this.violDropDown.push({label: 'По всем нарушениям', value: 0});
        this.mtr.getFindAllViolations().subscribe(get => {
            let obj = get.json();
            for(let i = 0; i < obj.length; i++) {
                this.violDropDown.push({label: `${obj[i].violationText}`, value: `${obj[i].id }`});
            }
            this.isProgressBar = false;
        })
    }

    onChangeViol() {
        if(this.selectViol != null)
            this.idViol = parseInt(this.selectViol);
        else this.idViol = 0;
    }
}