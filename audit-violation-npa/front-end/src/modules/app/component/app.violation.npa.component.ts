/**
 * Created by dmihaylov on 15.02.2018.
 */
import { Component, OnInit, Inject }from '@angular/core';
import { MainTableRequests } from '../requests/mainTableRequests';
import { MainTable } from "../classes/mainTable";
import { Revision } from '../classes/revision';
import { analyseData } from '../classes/analyseData';
import { MenuItem } from 'primeng/api';

@Component({
    selector: 'viol-npa-app',
    templateUrl: './violationNPA.html'
})

export class AuditViolationNpaComponent implements OnInit {
    constructor(
        @Inject (MainTableRequests) private mtr: MainTableRequests
    ) {
        this.selectedMainTable = new MainTable();
    }

    public ngOnInit() {
        this.items = [
            {label: 'Анализ формулировок нарушений', icon: 'fa-file-o', command: (event) => {this.eventClickAnalizeMenuTab()}},
            {label: 'Список непостроенных ссылок', icon: 'fa-list-alt', command: (event) => {this.eventClickFindLinksMenuTab()}}
        ]
    }

    public mainTable: MainTable[] = [];
    public selectedMainTable: MainTable;
    private errorStatus: string = "";
    public isComponent: boolean = true;
    public isProgressBar: boolean = false;
    public disStart: boolean = false;
    public status:string;
    public items: MenuItem[];


    public obj: any;

    btnStart() {
        this.isProgressBar = true;
        this.status = null;
        this.errorStatus = null;
        this.disStart = true;
        this.recursiveWaitCall(null);
    }

    private recursiveWaitCall( idRequest:string){
        this.mtr.getDataTable(idRequest).subscribe( a => {
            if (a.text() == null || a.text() == ''){
                if (idRequest == null){
                    this.errorStatus = "Запрос в обработке";
                }
                else{
                    this.errorStatus = "Запрос удален из очереди";
                    this.disStart = false;
                }
                this.isProgressBar = false;
                return;
            }
                let data : analyseData = a.json();
                if (data.completed){
                    if (data.error != null){
                        this.errorStatus = data.error;
                        this.mainTable = [];
                    }
                    else {
                        this.status = "Запрос обработан";
                        this.mainTable = Revision.checkRevDate(data.data);
                    }

                    this.isProgressBar = false;
                    this.disStart = false;
                }
                else{
                    this.status = data.status;
                    setTimeout(this.recursiveWaitCall(data.id), 3500);
                }

            }, e => {
                this.errorStatus = e.status  + " " + e.statusText + " " + e.statusMessage;
                console.log(e);
                this.isProgressBar = false;
            }
        );
    }

    private eventClickAnalizeMenuTab() {
        this.isComponent = true;
    }

    private eventClickFindLinksMenuTab() {
        this.isComponent = false;
    }
}