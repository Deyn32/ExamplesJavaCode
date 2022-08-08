/**
 * Created by dmihaylov on 22.12.2017.
 */
import {Inject, Injectable} from '@angular/core';
import { Message } from 'primeng/primeng';

@Injectable()
export class GrowlService {

    public showError(detail: string): Message {
        let msg: Message = {
            severity: 'error',
            summary: 'Ошибка',
            detail: detail
        };
        return msg;
    }

    public showSuccess(detail: string): Message {
        let msg: Message = {
            severity: 'success',
            summary: 'Внимание',
            detail: detail
        };
        return msg;
    }
}
