/**
 * Created by dmihaylov on 01.12.2017.
 */
import { TreeNode } from 'primeng/primeng';
import { isUndefined } from 'util';
import { DataCompareTree } from './dataCompareTree';
import { DiffsTree } from './diffsTree';
import {TableRevValue} from "./tableRevValue";
import {Doc} from "./Docs";

export class MergedTree {
    constructor() {
        this.children = [];
    }
    public children: MergedTree[];
    public diffs: DiffsTree[];
    public htmlLeft: string;
    public htmlRight: string;
    public nodeLeft: DataCompareTree;
    public nodeRight: DataCompareTree;

    public createMergedTree(mergedTree: MergedTree, compare: any) {
        mergedTree = this.checkDataMergeTree(mergedTree, compare);
        mergedTree = this.expandAll(mergedTree);
        return mergedTree;
    }

    public sortedMergedRevision(values: number[], doc: Doc, tableRevValue: TableRevValue[]): number[] {
        let idRev1: number = values[0];
        let idRev2: number = values[1];
        let rev1 = TableRevValue.findRevision(tableRevValue, idRev1);
        let rev2 = TableRevValue.findRevision(tableRevValue, idRev2);
        if (rev1.dtRev == null) {
            rev1.dtRev = doc.dt;
        }
        if (rev2.dtRev == null) {
            rev2.dtRev = doc.dt;
        }
        if (rev1.dtRev.valueOf() > rev2.dtRev.valueOf()) {
            values[0] = idRev2;
            values[1] = idRev1;
        }
        return values;
    }

    private checkDataMergeTree(mergedTree: MergedTree, compare: any): MergedTree {
        mergedTree = compare;
        for (let i: number = 0; i < compare.children.length; i++) {
            mergedTree.children[i] = this.recursion–°heckDataMergeTree(mergedTree.children[i], compare.children[i]);
        }
        return mergedTree;
    }

    private recursion–°heckDataMergeTree(merged: MergedTree, compare: any): MergedTree {
        merged = this.copyChild(merged, compare);
        if (merged.nodeLeft.data != null) {
            if (merged.nodeLeft.data.num != "")
                merged.nodeLeft.data.name = merged.nodeLeft.data.num + " " + merged.nodeLeft.data.name;
        }
        if (merged.nodeRight.data != null) {
            if (merged.nodeRight.data.num != "")
                merged.nodeRight.data.name = merged.nodeRight.data.num + " " + merged.nodeRight.data.name;
        }
        if (!isUndefined(merged.diffs)) {
            merged.htmlLeft = this.createDiffTextLeft(merged.diffs);
            merged.htmlRight = this.createDiffTextRight(merged.diffs);
        }
        if (!isUndefined(merged.children)) {
            for (let i: number = 0; i < merged.children.length; i++) {
                merged.children[i] = this.recursion–°heckDataMergeTree(merged.children[i], compare.children[i]);
            }
        }
        return merged;
    }

    /*–ü—Ä–ĺ–≤–Ķ—Ä–ļ–į –Ņ–ĺ—Ā—ā—É–Ņ–ł–≤—ą–ł—Ö –ī–į–Ĺ–Ĺ—č—Ö –ł–∑ –ī–∂–Ķ–Ļ—Ā–ĺ–Ĺ–į*/
    private copyChild(mergeTree: MergedTree, mergeCompare: MergedTree): MergedTree {
        mergeTree.children = mergeCompare.children;
        if (!isUndefined(mergeCompare.diffs)) {
            mergeTree.diffs = mergeCompare.diffs;
            mergeTree.htmlLeft = '';
            mergeTree.htmlRight = '';
        }
        mergeTree.nodeLeft = this.copyNode(mergeCompare.nodeLeft);
        mergeTree.nodeRight = this.copyNode(mergeCompare.nodeRight);

        return mergeTree;
    }

    /*–ü—Ä–ĺ–≤–Ķ—Ä–ļ–į –Ņ–ĺ—Ā—ā—É–Ņ–ł–≤—ą—č—Ö –ī–į–Ĺ–Ĺ—č—Ö –Ĺ–ĺ–ī–ĺ–≤*/
    private copyNode(node): DataCompareTree {
        const obj: DataCompareTree = new DataCompareTree();
        if (!isUndefined(node)) {
            obj.children = node.children;
            obj.data = node.data;
        }
        return obj;
    }
    private createDiffTextLeft(diffs: DiffsTree[]): string {
        let left = '<span>';
        for (const diff of diffs) {
            switch (diff.operation) {
                case 'DELETE':
                    left += this.appendDelete(diff.text);
                    break;
                case 'EQUAL':
                    left += this.appendEqual(diff.text);
                    break;
                default:
                    break;
            }
        }
        left += '</span>';
        return left;
    }

    private createDiffTextRight(diffs: DiffsTree[]): string {
        let right = '<span>';
        for (const diff of diffs) {
            switch (diff.operation) {
                case 'INSERT':
                    right += this.appendInsert(diff.text);
                    break;
                case 'EQUAL':
                    right += this.appendEqual(diff.text);
                    break;
                default:
                    break;
            }
        }
        right += '</span>';
        return right;
    }

    private appendDelete(text: string): string {
        const str: string = '<span class="deleteTextNode">' + text + '</span>';
        return str;
    }

    private appendEqual(text: string): string {
        const str = '<span>' + text + '</span>';
        return str;
    }

    private appendInsert(text: string): string {
        const str = '<span class="insertTextNode">' + text + '</span>';
        return str;
    }

    private expandAll(tree: MergedTree): MergedTree {
        tree.children.forEach( node => {
            this.expandRecursive(node, true);
        });
        return tree;
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
