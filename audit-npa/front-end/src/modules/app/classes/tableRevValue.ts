/**
 * Created by dmihaylov on 12.02.2018.
 */
import {Doc} from "../classes/Docs";
import {Revision} from "./abstract/Revision";

export class TableRevValue extends Revision{
    idRev: number;
    loaded: boolean;
    disabled: boolean;
    idDoc: number;
    original: boolean;
    npaRevDoc: Doc;

    public static findRevision(revs: TableRevValue[], idRev: number) {
        let rev = revs.find(r => r.idRev == idRev);
        return rev;
    }
}
