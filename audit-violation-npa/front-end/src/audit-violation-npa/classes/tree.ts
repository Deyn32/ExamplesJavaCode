/**
 * Created by dmihaylov on 14.03.2018.
 */
import {NodeData} from "./nodeData";
import {Diffs} from "./diffs";

export class Tree {
    children: Tree[];
    data: NodeData;
    diffs: Diffs;
}