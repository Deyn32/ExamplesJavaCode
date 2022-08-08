/**
 * Created by dmihaylov on 26.10.2017.
 */
import {isUndefined} from "util";
import {DateConvertService} from '../services/dateConvertService';
import {TableStructFull} from "./tableStructFull";

export class DataTree extends TableStructFull {
    constructor() {
        super();
}
    public id: string;
    public originType: string;

    public static strVal(text: string, data: DataTree): TableStructFull {
        const srt: TableStructFull = {
            label: '',
            dtBegin: null,
            dtEnd: null,
            path: '',
            type: ''
        };
        srt.label = text;
        srt.dtBegin = data.dtBegin;
        srt.dtEnd = data.dtEnd;
        srt.path = data.path;
        srt.type = data.type;
        return srt;
    }

    public static objSubmit(form: any, data: DataTree, label: string): any {
        let date: DateConvertService = new DateConvertService();
        const obj: any = {
            id: data.id,
            label: label,
            dtBegin: null,
            dtEnd: null,
            type: null
        };
        if (isUndefined(form._value.dtBegin) || form._value.dtBegin == null) {
            obj.dtBegin = null;
        }
        else {
            obj.dtBegin = date.InUniversalDate(form._value.dtBegin);
            obj.dtBegin = date.InDBDate(obj.dtBegin);
        }
        if (isUndefined(form._value.dtEnd) || form._value.dtEnd == null) {
            obj.dtEnd = null;
        }
        else {
            obj.dtEnd = date.InUniversalDate(form._value.dtEnd);
            obj.dtEnd = date.InDBDate(obj.dtEnd);
        }
        if (isUndefined(form._value.types)){
            obj.type = data.type;
        }
        else {
            obj.type = form._value.types;
        }
        return obj;
    }
}
