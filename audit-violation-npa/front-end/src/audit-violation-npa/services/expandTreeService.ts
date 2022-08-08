/**
 * Created by dmihaylov on 17.05.2018.
 */

import {TreeNode} from "primeng/api";
import {Injectable} from '@angular/core';

@Injectable()
export class ExpandTreeService{
    public expandChildren(child: any[]) {
        child.forEach(node => {
            this.expandRecursive(node, true);
        });
    }

    private expandRecursive(node: TreeNode, isExpand: boolean): void {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach( childNode => {
                this.expandRecursive(childNode, isExpand);
            } );
        }
    }
}