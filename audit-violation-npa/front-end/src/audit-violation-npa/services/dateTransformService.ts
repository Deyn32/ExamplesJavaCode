/**
 * Created by dmihaylov on 16.05.2018.
 */
import { Injectable } from '@angular/core';
import {DatePipe} from "@angular/common";

@Injectable()
export class DateTransformService {
    private dp: DatePipe = new DatePipe('ru-RU');

    transformIso(date: Date) {
        return this.dp.transform(date, "yyyy-MM-dd");
    }
}