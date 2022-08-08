/**
 * Created by dmihaylov on 28.02.2018.
 */
import {Component, OnInit, Input, Inject, Output} from '@angular/core';
import {Doc} from "../classes/Docs";
import {ModelFormRevision} from "../classes/modelFormRevision";
import {DocumentNPARequests} from "../requests/documentNPARequests";
import { saveAs } from 'file-saver/FileSaver.js';

@Component({
    selector: 'imgview-app',
    templateUrl: './imageView.html'
})

export class ImageViewComponent implements OnInit {
    constructor(
        @Inject (DocumentNPARequests) private docreq: DocumentNPARequests,
    ) {}

    ngOnInit() {

    }

    @Input() doc: Doc;
    @Input() revis: ModelFormRevision;
    @Input() htmlText: string;
    @Output() selectDoc: Doc;
    isComponent: boolean = true;

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

    public btnBack() {
        this.selectDoc = this.doc;
        this.isComponent = false;
    }
}