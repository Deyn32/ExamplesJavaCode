/**
 * Created by dmihaylov on 05.03.2018.
 */

export class Revision {
    id: number;
    id_NPA: number;
    id_NPARevision: number;
    dtBegin: Date;
    dtEnd: Date;
    dtRev: Date;
    isDeleted: boolean;
    dateModify: Date;

    public static checkRevDate(table: any[]) {
        for (let row of table) {
            if (row.rev == null) {
                row.rev = new Revision();
            }
            if (row.rev.dtRev == null) {
                row.rev.dtRev = row.npa.date;
            }
        }
        return table;
    }
}