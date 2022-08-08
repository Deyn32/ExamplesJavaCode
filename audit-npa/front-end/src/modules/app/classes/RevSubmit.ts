/**
 * Created by dmihaylov on 14.11.2017.
 */
import {DateConvertService} from '../services/dateConvertService';
import {Revision} from "./abstract/Revision";

 export class RevSubmit extends Revision{
     public id_NPA: number = 0;

     public constructor(id_NPA: number, dtBegin: Date, dtRev: Date, dtEnd: Date, id_Rev: number) {
         super();
         this.id_NPA = id_NPA;
         this.dtBegin = dtBegin;
         this.dtRev = dtRev;
         this.dtEnd = dtEnd;
         this.id_NPA_Revision = id_Rev;
     }

     public static getObjectRevSubmit(id: number, form: any): RevSubmit {
        let date: DateConvertService = new DateConvertService();
        const obj: RevSubmit = new RevSubmit(id, form._value.dtBegin, form._value.dtRev, form._value.dtEnd,  form._value.id_NPA_Revision);
        obj.dtBegin = new Date (date.InDBDate(obj.dtBegin));
        obj.dtRev = new Date (date.InDBDate(obj.dtRev));
        if (obj.dtEnd != null)
            obj.dtEnd = new Date (date.InDBDate(obj.dtEnd));
        return obj;
     }
 }
