/**
 * Created by oshesternikova on 15.03.2018.
 */

import {MainTable} from "./mainTable";
import {RevData} from "./RevData";

export class analyseData extends RevData{
    constructor() {
        super();
    }

    data: MainTable[];
}