/**
 * Created by dmihaylov on 01.03.2018.
 */
import {NPA} from "./npa";
import {Revision} from "./revision";

export class MainTable {
    constructor() {}

    idLink: number;
    rev: Revision;
    text: string;
    date_BEGIN: Date;
    date_END: Date;
    npa: NPA;
    result: string;
    violText: string;

    public static checkRevDate(table: MainTable[]) {
        for (let row of table) {
            if (row.rev != null) {
                if (row.rev.dtRev == null) {
                    row.rev.dtRev = row.npa.date;
                }
            }
            else {
                row.rev = new Revision();
            }

        }
        return table;
    }
}