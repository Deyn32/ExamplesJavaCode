/**
 * Created by dmihaylov on 12.04.2017.
 * Хедер для аутентификации при запросах
 */
import { Injectable } from '@angular/core';
import { Headers } from '@angular/http';

@Injectable()
export class Head {
    public createCsrfHeader(): Headers {
        let headers = new Headers();
        return headers = this.createCsrfHeadersToken(headers);
    }

    private createCsrfHeadersToken( headers: Headers): Headers {
        const token: string = this.createToken();
        if (!token) return headers;

        const header: string = this.createHeader();
        if (!header) return headers;

        headers.append(header, token);
        headers.append('Content-Type', 'application/json');
        return headers;
    }

    private createToken(): string {
        const element: Element = document.querySelector('meta[name="_csrf"]');
        const token: string = element && element.getAttribute('content');
        return token;
    }

    private createHeader(): string {
        const element: Element = document.querySelector('meta[name="_csrf_header"]');
        const header: string = element && element.getAttribute('content');
        return header;
    }
}
