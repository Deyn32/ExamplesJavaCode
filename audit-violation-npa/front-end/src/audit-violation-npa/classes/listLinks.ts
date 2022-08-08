/**
 * Created by dmihaylov on 20.03.2018.
 */
import { LinkNPA } from './linkNPA';

export class ListLinks {
    constructor(list: ListLinks) {
        if(list != null){
            this.id = list.id;
            this.dtBegin = list.dtBegin;
            this.dtEnd = list.dtEnd;
            this.filterText = list.filterText;
            this.links = list.links;
            this.text = list.text;
        }
    }
    id: number;
    links: LinkNPA[];
    text: string;
    filterText: string;
    dtBegin: Date;
    dtEnd: Date;
}