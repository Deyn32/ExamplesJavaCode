/**
 * Created by dmihaylov on 22.03.2018.
 */
import { ElementRevision } from './elementRevision';
import {TreeNode} from "primeng/api";

export class DetailLinkTree {
    children: DetailLinkTree[];
    data: ElementRevision;
}