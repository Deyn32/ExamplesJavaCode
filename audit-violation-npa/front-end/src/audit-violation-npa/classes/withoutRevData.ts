/**
 * Created by dmihaylov on 04.04.2018.
 */
import { WithoutRev } from './withoutRev';
import { RevData } from './RevData';

export class WithoutRevData extends RevData {
    constructor() {
        super();
    }

    data: WithoutRev[];

}