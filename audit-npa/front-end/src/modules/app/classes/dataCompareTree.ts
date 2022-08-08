/**
 * Created by dmihaylov on 31.10.2017.
 */
import {DataFileUpload} from './dataFileUpload';
import {ITree} from "../interface/ITree";

export class DataCompareTree implements ITree{
    public children: DataCompareTree[];
    public data: DataFileUpload;
    public diffs: any;
}
