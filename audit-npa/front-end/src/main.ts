/**
 * Created by dmihaylov on 23.05.2017.
 */
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AuditRouteModule }    from './app/module';
import { enableProdMode } from '@angular/core';

enableProdMode();
platformBrowserDynamic().bootstrapModule(AuditRouteModule);