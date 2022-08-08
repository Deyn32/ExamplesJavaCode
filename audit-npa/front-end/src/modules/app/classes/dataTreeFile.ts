/**
 * Created by dmihaylov on 26.10.2017.
 */
import { DataFileUpload } from './dataFileUpload';
import {ITree} from "../interface/ITree";

export class DataTreeFile implements ITree{
    public children: DataTreeFile[];
    public data: DataFileUpload;
}
