import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BusyModule} from 'angular2-busy';
import { AccordionModule, CalendarModule, CheckboxModule, ContextMenuModule, DataTableModule, DialogModule, DropdownModule, FileUploadModule, GrowlModule, InputTextareaModule, InputTextModule } from 'primeng/primeng';
import { ListboxModule, MenuModule,  OverlayPanelModule, ProgressBarModule, SharedModule, SlideMenuModule, ToolbarModule, TooltipModule, TreeModule, TreeTableModule } from 'primeng/primeng';
import {TableModule} from 'primeng/table';
import {TabMenuModule} from 'primeng/tabmenu';
import {MenubarModule} from 'primeng/menubar';
import { AuditViolationNpaComponent } from './component/app.violation.npa.component';
import { NotFindComponent } from './component/notFindParagraph/notFindComponent';
import {MainTableRequests} from "./requests/mainTableRequests";
import {EmptyLingRequest} from './requests/emptyLinkRequest';

@NgModule({
    imports:      [ NoopAnimationsModule,
        TableModule,
        CommonModule,
        BrowserModule,
        FormsModule,
        BusyModule,
        HttpModule,
        InputTextModule,
        CalendarModule,
        CheckboxModule,
        ContextMenuModule,
        GrowlModule,
        DialogModule,
        ListboxModule,
        ProgressBarModule,
        AccordionModule,
        DataTableModule,
        SharedModule,
        InputTextareaModule,
        DropdownModule,
        TreeTableModule,
        OverlayPanelModule,
        MenuModule,
        MenubarModule,
        SlideMenuModule,
        FileUploadModule,
        TabMenuModule,
        TreeModule,
        TooltipModule,
        ToolbarModule],
    declarations: [ AuditViolationNpaComponent, NotFindComponent ],
    providers: [
        {
            provide: 'Window', useValue: window
        },
        MainTableRequests,
        EmptyLingRequest
    ],
    bootstrap:    [ AuditViolationNpaComponent ]
})
export class AuditViolationNPAModule { }
