/**
 * Created by dmihaylov on 10.04.2018.
 */
import {Tree} from './tree';
import {Diffs} from './diffs';
import { isUndefined } from 'util';
import {TreeNode} from "primeng/api";
import {createElementCssSelector} from "@angular/compiler/compiler";

export class CompareTrees {
    children: CompareTrees[];
    nodeLeft: Tree;
    nodeRight: Tree;
    htmlLeft: string;
    htmlRight: string;
    diffs: Diffs[];
    hasDiffs: boolean;
    static flag: boolean;

    public static createMergedTree(mergedTree: CompareTrees, compare: any) {
        mergedTree = this.checkDataMergeTree(mergedTree, compare);
        return mergedTree;
    }

    private static checkDataMergeTree(mergedTree: CompareTrees, compare: any): CompareTrees {
        mergedTree = compare;
        for (let i: number = 0; i < compare.children.length; i++) {
            mergedTree.children[i] = this.recursionСheckDataMergeTree(mergedTree.children[i], compare.children[i]);
        }
        return mergedTree;
    }

    private static recursionСheckDataMergeTree(merged: CompareTrees, compare: any): CompareTrees {
        merged = this.copyChild(merged, compare);
        if (merged.nodeLeft.data != null) {
            if (merged.nodeLeft.data.num != "" && merged.nodeLeft.data.num != null)
                merged.nodeLeft.data.name = merged.nodeLeft.data.num + " " + merged.nodeLeft.data.name;
        }
        if (merged.nodeRight.data != null) {
            if (merged.nodeRight.data.num != "" && merged.nodeRight.data.num != null)
                merged.nodeRight.data.name = merged.nodeRight.data.num + " " + merged.nodeRight.data.name;
        }
        if (!isUndefined(merged.diffs)) {
            merged.htmlLeft = this.createDiffTextLeft(merged.diffs);
            merged.htmlRight = this.createDiffTextRight(merged.diffs);
        }
        if (!isUndefined(merged.children)) {
            for (let i: number = 0; i < merged.children.length; i++) {
                merged.children[i] = this.recursionСheckDataMergeTree(merged.children[i], compare.children[i]);
            }
        }
        return merged;
    }

    /*Проверка поступивших данных из джейсона*/
    private static copyChild(mergeTree: CompareTrees, mergeCompare: CompareTrees): CompareTrees {
        mergeTree.children = mergeCompare.children;
        if (!isUndefined(mergeCompare.diffs)) {
            mergeTree.diffs = mergeCompare.diffs;
            mergeTree.htmlLeft = '';
            mergeTree.htmlRight = '';

        }
        mergeTree.nodeLeft = this.checkNode(mergeCompare.nodeLeft);
        mergeTree.nodeRight = this.checkNode(mergeCompare.nodeRight);

        return mergeTree;
    }

    /*Проверка поступившых данных нодов*/
    private static checkNode(node): Tree {
        const obj: Tree = new Tree();
        if (!isUndefined(node)) {
            obj.children = node.children;
            obj.data = node.data;
        }
        return obj;
    }

    private static createDiffTextLeft(diffs: Diffs[]): string {
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

    private static createDiffTextRight(diffs: Diffs[]): string {
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

    private static appendDelete(text: string): string {
        let str: string;
        if(this.flag)
            str = '<span>' + text + '</span>';
        else
            str = '<span class="deleteTextNode">' + text + '</span>';
        return str;
    }

    private static appendEqual(text: string): string {
        let str: string;
        if(this.flag)
            str = '<span class="insertTextNode">' + text + '</span>';
        else
            str = '<span>' + text + '</span>';
        return str;
    }

    private static appendInsert(text: string): string {
        let str: string;
        if(this.flag)
            str = '<span>' + text + '</span>';
        else
            str = '<span class="insertTextNode">' + text + '</span>';
        return str;
    }

    private static expandRecursive(node: TreeNode, isExpand: boolean): void {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach( childNode => {
                this.expandRecursive(childNode, isExpand);
            } );
        }
    }
}