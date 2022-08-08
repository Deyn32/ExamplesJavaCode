/**
 * Created by dmihaylov on 13.03.2018.
 */
import { NPA } from './npa';
import {Revision} from "./revision";

export class TableEmptyLink {
    public npa: NPA[];
    public id: number;
    public text: string;
    public rev: Revision[];
}