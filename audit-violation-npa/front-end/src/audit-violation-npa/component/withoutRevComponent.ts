/**
 * Created by dmihaylov on 04.04.2018.
 */
import { Component, OnInit, Inject }from '@angular/core';
import { WithoutRevRequest } from '../requests/withoutRevRequest';
import {WithoutRev} from "../classes/withoutRev";
import {WithoutRevData} from "../classes/withoutRevData";
import {RepairLink} from "../classes/repairLink";
import {Message} from "primeng/api";

@Component({
    selector: 'without-rev-app',
    templateUrl: './withoutRev.html'
})

export class WithoutRevComponent implements OnInit {
    constructor(
        @Inject(WithoutRevRequest) private wrr: WithoutRevRequest
    ) {}

    public isProgressBar: boolean = false;
    public isComponent: boolean = true;
    public status:string;
    public without: WithoutRev[] = [];
    public disStart: boolean = true;
    public disStop: boolean = false;
    public disRepairAll: boolean = true;
    public msgs: Message[] = [];
    public repairLinksTable: WithoutRev[] = [];

    private errorStatus: string = "";

    ngOnInit() {
        this.isProgressBar = true;
        this.recursiveWaitCall(null);
    }

    private recursiveWaitCall( idRequest:string){
        this.wrr.getListWithoutRev(idRequest).subscribe( a => {
            if (a.text() == null || a.text() == ''){
                    if (idRequest == null){
                        this.errorStatus = "Запрос в обработке";
                    }
                    else{
                        this.errorStatus = "Запрос удален из очереди";
                    }
                    this.isProgressBar = false;
                    return;
            }
            let data : WithoutRevData = a.json();
            if (data.completed){
                if (data.error != null){
                    this.errorStatus = data.error;
                    this.without = [];
                }
                else {
                    this.status = "Запрос обработан";
                }
                this.without = [];
                this.without = data.data;
                this.disStart = false;
                this.disStop = true;
                if(this.without.length > 0)
                    this.disRepairAll = false;
                this.isProgressBar = false;
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

    btnStop(){
        this.wrr.getStopWithoutRev().subscribe(get => {
            this.disStart = false;
            this.disStop = true;
        });
    }

    btnStart() {
        this.disStart = true;
        this.disStop = false;
        this.isProgressBar = true;
        this.recursiveWaitCall(null);
    }

    btnRepairAll() {
        this.isProgressBar = true;
        this.status = "Идет процесс исправления ссылок. Пожалуйста, подождите..."
        let ids: RepairLink[] = [];
        for (let i = 0; i < this.without.length; i++) {
            ids[i] = new RepairLink();
            ids[i].id = this.without[i].id;
            ids[i].idRev = this.without[i].idRevision;
        }
        this.wrr.postRepairAllLinks(ids).subscribe(post => {
            if(post.json() != null){
                let notRepairLinks = post.json();
                for (let link of notRepairLinks) {
                    for (let rowTable of this.without) {
                        if(link.idNpa == rowTable.idNpa && link.idRev == rowTable.idRevision) {
                            this.repairLinksTable.push(rowTable);
                        }
                    }
                }
                this.without = this.repairLinksTable;
            }
            if (this.repairLinksTable.length != 0)
                this.isComponent = false;
            else {
                this.msgs.push({severity: 'success',
                    summary: 'Внимание',
                    detail: 'Исправление ссылок прошло успешно!'});
                this.without = [];
            }
            this.isProgressBar = false;
        });
    }

    public clear() {
        this.msgs = [];
    }

    btnBack() {
        this.isComponent = true;
    }

}