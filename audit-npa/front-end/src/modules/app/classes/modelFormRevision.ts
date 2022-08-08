/**
 * Created by dmihaylov on 01.09.2017.
 * Модель для работы с редакциями документ НПА
 */

import { TableRevValue } from '../classes/tableRevValue';
import {Revision} from "./abstract/Revision";

export class ModelFormRevision extends Revision{
    public idRev: number;
    public loaded: boolean;
    public idDoc: number;
    public original: boolean;

    public initField(idRev: number, dtBegin: Date, dtRev: Date, dtEnd: Date, idNpa: number, original: boolean) {
        this.idRev = idRev;
        this.dtBegin = dtBegin;
        this.dtRev = dtRev;
        this.dtEnd = dtEnd;
        this.id_NPA_Revision = idNpa;
        this.original = original;
    }

    public getField(get: TableRevValue) {
        this.idRev = get.idRev;
        this.dtBegin = get.dtBegin;
        this.dtRev = get.dtRev;
        this.dtEnd = get.dtEnd;
        this.loaded = get.loaded;
        this.idDoc = get.idDoc;
        this.id_NPA_Revision = get.id_NPA_Revision;
        this.original = get.original;
    }
}
