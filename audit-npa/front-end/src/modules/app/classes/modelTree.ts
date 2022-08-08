/**
 * Created by dmihaylov on 02.05.2017.
 * Классы для работы с деревьями
 */
import { DataTree } from './dataTree';
import {ITree} from "../interface/ITree";

export class ModelTree implements ITree {
    public children: ModelTree[];
    public data: DataTree;
}
