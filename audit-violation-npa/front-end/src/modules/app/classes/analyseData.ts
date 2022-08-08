/**
 * Created by oshesternikova on 15.03.2018.
 */

import {MainTable} from "./mainTable"

export class analyseData {
    constructor() {
    }

    data: MainTable[];
    completed: boolean;
    error: string;
    id: string;
    status: string;
    endTime: Date;
    startTime: Date;
}