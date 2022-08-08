/**
 * Created by dmihaylov on 14.02.2018.
 */
import {Component, OnInit, Inject} from '@angular/core';
import {MergedTree} from "../../classes/mergedTree";
import {Message} from "primeng/primeng";
import {GrowlService} from "../../services/growlService";

@Component({
    templateUrl: './mergedTrees.html'
})

export class MergedTreesComponent implements OnInit {
    constructor(
        @Inject (GrowlService) private growlserv: GrowlService
    ) {
        this.mergedTree = new MergedTree();
    }
    public ngOnInit() {

    }
    public mergedTree: MergedTree;
    public msgs: Message[] = [];

    private compareTrees(resp: any): void {
        if (resp.json().children.length != 0) {
            this.mergedTree = new MergedTree();
            this.mergedTree = this.mergedTree.createMergedTree(this.mergedTree, resp.json());
        }
        else
            this.msgs.push(this.growlserv.showSuccess('Структуры идентичны!'));
    }
}