/**
 * Created by dmihaylov on 12.02.2018.
 * Интерфейс-контракт для древовидных структур (каждый наследующий класс реализует свой тип поля)
 */
export interface ITree {
    children: any[];
    data: any;
}