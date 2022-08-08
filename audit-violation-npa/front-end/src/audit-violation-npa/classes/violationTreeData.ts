/**
 * Created by dmihaylov on 11.05.2018.
 */
import {LinkNPA} from "./linkNPA";
import {ListLinks} from "./listLinks";

export class ViolationTreeData{
    constructor(obj: ListLinks){
        if(obj != null){
            this.id = obj.id;
            this.text = obj.text;
            this.dtBegin = obj.dtBegin;
            this.dtEnd = obj.dtEnd;
            this.links = obj.links;
        }
    }
    id: number;
    text: string;
    dtBegin: Date;
    dtEnd: Date;
    links: LinkNPA[];
}