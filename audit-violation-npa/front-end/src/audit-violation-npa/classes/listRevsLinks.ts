/**
 * Created by dmihaylov on 20.03.2018.
 */
import { Revision } from './revision';

export class ListRevsLinks {
    id: number;
    revisions: Revision[] = [];
    state: string;
    structElemRevState: string;
    dtStart: Date;
    dtEnd: Date;
    disabled: boolean = false;
    editable: boolean = true;

}