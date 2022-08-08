/**
 * Created by dmihaylov on 01.06.2018.
 */
import {Component, OnInit, Inject, ViewChild, ElementRef} from '@angular/core';
import { Message } from 'primeng/primeng';
import { DownloadNpaRequests } from '../../requests/downloadNpaRequests';
import {TreesRequests} from "../../requests/treeRequests";
import {Head} from "../../services/createHeaderService";
import {StateDownloadNpa} from "../../classes/StateDownloadNpa";

@Component({
    selector: 'download-doc-app',
    templateUrl: './downloadNpa.html'
})
export class DownloadNpa implements OnInit{
    constructor(
        @Inject (DownloadNpaRequests) private downloadNpaReq: DownloadNpaRequests,
        @Inject (TreesRequests) private treeserv: TreesRequests,
        @Inject (Head) private head: Head
    ) {}

    @ViewChild('fileload') private fileload: ElementRef;

    public isProgressBar: boolean = false;
    public isProblem: boolean = false;
    public msgs: Message[] = [];
    public stateDownloadNpa: StateDownloadNpa[] = [];

    ngOnInit(){}

    /*Событие подмены хедера */
    public onBeforeSendUploadFile(event) {
        const obj: Object = this.treeserv.createHeader(this.head.createCsrfHeader());
        event.xhr.setRequestHeader(obj['key'][0], obj['value'][0]);
    }

    /*Событие подмены URL адреса */
    public onSelectUploadFile(event) {
        let maxsize = 2097152;
        if(event.files[0].size <= maxsize){
            this.isProgressBar = true;
            this.fileload['url'] = this.downloadNpaReq.formattedURL();
        }
        else {
            this.msgs = [];
            this.msgs.push({severity: 'error',
                summary: 'Внимание',
                detail: 'Размер файла не должен превышать 2Мб!'})
        }
    }

    /*Событие загрузки файла */
    public onUploadFile(event) {
        this.uploadFile(event);
    }

    private uploadFile(event): void {
        this.stateDownloadNpa = [];
        if(event.xhr.status == 200){
            this.stateDownloadNpa = JSON.parse(event.xhr.response);
            this.isProgressBar = false;
        }
    }
}