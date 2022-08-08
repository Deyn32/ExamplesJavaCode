/**
 * Created by dmihaylov on 31.03.2017.
 * Сервис настройки локали календаря
 */
import { Injectable } from '@angular/core';
import { retry } from 'rxjs/operator/retry';

@Injectable()
export class CalendarService {
    private ru: any = {
        firstDayOfWeek: 1,
        dayNames: ['воскресение', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'],
        dayNamesShort: ['вс', 'пн', 'вт', 'ср', 'чт', 'пят', 'сб'],
        dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пят', 'Сб'],
        monthNames: ['январь', 'февраль', 'март', 'апрель', 'май', 'июнь', 'июль', 'август', 'сентябрь', 'октябрь', 'ноябрь', 'декабрь'],
        monthNamesShort: ['янв', 'фев', 'мар', 'апр', 'май', 'июнь', 'июль', 'авг', 'сен', 'окт', 'ноя', 'дек']
    };
    private minDate: Date;
    private maxDate: Date;

    public settings() {
        const today = new Date();
        const month = today.getMonth();
        const year = today.getFullYear();
        const prevMonth = (month === 0) ? 11 : month - 1;
        const prevYear = (prevMonth === 11) ? year - 1 : year;
        const nextMonth = (month === 11) ? 0 : month + 1;
        const nextYear = (nextMonth === 0) ? year + 1 : year;
        this.minDate = new Date();
        this.minDate.setMonth(prevMonth);
        this.minDate.setFullYear(prevYear);
        this.maxDate = new Date();
        this.maxDate.setMonth(nextMonth);
        this.maxDate.setFullYear(nextYear);
    }

    public getRuLocale() {
        return this.ru;
    }
}
