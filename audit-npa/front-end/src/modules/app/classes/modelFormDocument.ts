/**
 * Created by dmihaylov on 17.04.2017.
 * Модель для формы правовых документов
 */
import { DataFileUpload } from './dataFileUpload';

export class ModelFormDocument extends DataFileUpload{
    constructor() {
        super();
        this.editing = false;
        this.type = '1';

    }

    private id: number;
    private dt: Date;
    private editing: boolean;
    private error: Error;

    public initOfField(id: number, name: string, dt: Date, num: string, type: string, editing: boolean ) {
        this.id = id;
        this.name = name;
        this.dt = dt;
        this.num = num;
        this.type = type;
        this.editing = editing;
    }
}
