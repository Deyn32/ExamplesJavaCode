/**
 * Created by dmihaylov on 12.02.2018.
 */

export class Doc {
    /** Номер документа */
    num: string;
    /** Дата документа */
    dt: Date;
    /** Название документа */
    name: string;
    dataCopy: Date;
    /** ID документа */
    id: number;
    /** Тип документа */
    docType: Object;
    /** Документы */
    docadocs: string [];
    /** Признак наличия в базе */
    hasData: boolean;
    /** Признак изменяющего документа */
    editing: boolean;

    date: string;

    public static findDoc (docs: Doc[], id: number): Doc {
        let doc = docs.find(d => d.id == id);
        return doc;
    }

    public static sortDocIndex(docs: Doc[], doc: Doc): void {
        let index = docs.indexOf(doc);
        let firstDoc: Doc = docs[0];
        docs[0] = docs[index];
        docs[index] = firstDoc;
    }
}
