import { Injectable } from '@angular/core';
/**
 * Created by dmihaylov on 08.08.2017.
 * Вспомогательный класс обработки ошибок
 */
@Injectable()
export class Error {
    public noDocuments(): string {
        const str: string = 'Нет документов для отображения!';
        return str;
    }

    public documentWasDeleted(idDoc: number): string {
        const str: string = 'Документ № ' + idDoc + ' был удален другим пользователем!';
        return str;
    }

    public notAllData(): string {
        const str: string = 'Введены не все данные!';
        return str;
    }

    public revisionWasDeleted(idRev: number): string {
        let id: string = idRev.toString();
        if (idRev === null) {
            id = 'Первоначальная';
        }
        const str: string = 'Редакция № ' + id + ' была удалена другим пользователем!';
        return str;
    }

    public notSelectedDocument(): string {
        const str: string = 'Не выбран документ!';
        return str;
    }

    public enteredWrongDate(): string {
        const str: string = 'Введена неверная дата!';
        return str;
    }

    public notSelectedRevision(): string {
        const str: string = 'Не выбрана редакция документа!';
        return str;
    }

    public notLoadingRevision(): string {
        const str: string = 'Не загружена редакция документа!';
        return str;
    }

    public selectRevisionWithoutLoadDoc(): string {
        const str: string = 'Вы выбрали редакцию без загруженного документа!';
        return str;
    }
}
