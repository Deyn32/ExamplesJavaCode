/**
 * Created by dmihaylov on 29.03.2018.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BusyModule} from 'angular2-busy';
import { FormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { RouterComponent } from './components/routerComponent';
import { AuditViolationNPAModule } from '../audit-violation-npa/AuditViolationNPAModule';

const appRoutes: Routes = [

];

@NgModule({
    imports: [
        FormsModule,
        NoopAnimationsModule,
        CommonModule,
        AuditViolationNPAModule,
        BusyModule,
        BrowserModule,
        RouterModule.forRoot(appRoutes, { useHash: true })
    ],
    providers: [
        { provide: 'Window', useValue: window }
    ],
    declarations: [RouterComponent],
    bootstrap: [RouterComponent]

})
export class AuditViolModule {}