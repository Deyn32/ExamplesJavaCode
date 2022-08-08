/**
 * Created by dmihaylov on 27.06.2017.
 * Сервис работы с датой
 */
import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { isUndefined } from 'util';
import { ModelTree } from '../classes/modelTree';

@Injectable()
export class DateConvertService {

    private dp: DatePipe = new DatePipe('ru-RU');

    /*Конвертация в универсальную дату */
    public InUniversalDate(date: Date) {
        if (!isUndefined(date) && date != null)
            return date = new Date(date.toString());
        return null;
    }

    /*Конвертация даты в русскую локаль */
    public InRussianDate(date: Date) {
        if (!isUndefined(date) && date != null)
            return this.dp.transform(date, 'dd.MM.yyyy');
        return null;
    }

    /*Конвертация даты в формат хранения в БД */
    public InDBDate(date: Date) {
        if (!isUndefined(date) && date != null)
            return this.dp.transform(date, 'yyyy-MM-dd');
        return null;
    }

    public dateParseTree(model: ModelTree[]): ModelTree[] {
        for (const mod of model) {
            mod.data.dtBegin = this.InUniversalDate(mod.data.dtBegin);
            mod.data.dtEnd = this.InUniversalDate(mod.data.dtEnd);
            if (mod.children.length != 0) {
                this.dateParseTree(mod.children);
            }
        }
        return model;
    }
}
