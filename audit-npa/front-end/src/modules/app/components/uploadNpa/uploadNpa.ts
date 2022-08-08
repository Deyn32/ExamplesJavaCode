/**
 * Created by dmihaylov on 22.05.2018.
 */
import { Component, OnInit, Inject} from '@angular/core';
import { DocumentNPARequests } from "../../requests/documentNPARequests";
import { UploadNpaRequests } from '../../requests/UploadNpaRequests';
import { DateConvertService } from "../../services/dateConvertService";
import { saveAs } from 'file-saver/FileSaver.js';
import { Doc } from "../../classes/Docs";

@Component({
    selector: 'upload-doc-app',
    templateUrl: './uploadNpa.html'
})
export class UploadDocument implements OnInit{
    constructor(
        @Inject (DocumentNPARequests) private docNpaReq: DocumentNPARequests,
        @Inject (UploadNpaRequests) private uploadDocReq: UploadNpaRequests,
        @Inject (DateConvertService) private dateTransform: DateConvertService
    ) {}

    public isProgressBar: boolean = false;
    public disCreateFile: boolean = true;
    public selectDocs: number = 0;
    public documents: Doc[] = [];
    public selectedDocs: Doc[] = [];

    ngOnInit() {
        this.loadTable();
    }

    loadTable(){
        this.isProgressBar = true;
        this.documents.length = 0;
        this.docNpaReq.getDataFindList().subscribe(get => {
            this.documents = get.json();
            for(let doc of this.documents){
                doc.dt = new Date(doc.dt);
            }
            this.isProgressBar = false;
        });
    }

    btnCreateFiles(){
        this.isProgressBar = true;
        this.uploadDocReq.getCreateFile(this.createListId()).subscribe(get =>{
            let data = get.blob();
            let blob = new Blob([data], {type: 'application/zip;charset=utf-8'});
            let date: Date = new Date();
            let fileName: string = this.dateTransform.InDBDate(date);
            saveAs(blob, fileName +  ".zip");
            this.isProgressBar = false;
        });
    }

    private createListId(){
        let ids: number[] = [];
        if(this.selectedDocs.length != 0 && this.selectedDocs != null){
            for(let doc of this.selectedDocs ){
                ids.push(doc.id);
            }
        }
        return ids;
    }

    onRowSelect(){
       this.selectDocs++;
        this.disCreateFile = false;
    }

    onRowUnselect(){
        if(this.selectedDocs.length == 0){
            this.disCreateFile = true;
        }
        this.selectDocs--;
    }
}