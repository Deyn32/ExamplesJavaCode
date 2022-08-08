/**
 * Created by dmihaylov on 01.03.2018.
 */
import { Inject } from '@angular/core';
import { Dictionary } from '../requests/dictionaryRequests';
import {SelectItem} from "primeng/api";

export class DictinaryElementType {
    constructor(
        @Inject (Dictionary) private dict: Dictionary
    ) {}

    public static getDict(get: any): SelectItem[] {
        let elemTypes: SelectItem[] = [];
            let str = get.json();
            elemTypes.push({label: ``, value: ``}); //Задаем дополнительно пустоту для проверки
            for (const s of str) {
                elemTypes.push({label: `${s.name}`, value: `${s.name}`});
            }
        return elemTypes;
    }
}