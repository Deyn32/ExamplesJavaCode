import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Routes, RouterModule } from '@angular/router';
import { BusyModule} from 'angular2-busy';
import { AccordionModule, CheckboxModule, ContextMenuModule, DataTableModule, DialogModule, DropdownModule, FileUploadModule, GrowlModule, InputTextareaModule, InputTextModule } from 'primeng/primeng';
import { ListboxModule, MenuModule,  OverlayPanelModule, ProgressBarModule, SharedModule, SlideMenuModule, ToolbarModule, TooltipModule, TreeModule, TreeTableModule } from 'primeng/primeng';
import {CalendarModule} from 'primeng/calendar';
import { TableModule } from 'primeng/table';
import { TabMenuModule } from 'primeng/tabmenu';
import { MenubarModule } from 'primeng/menubar';
import { SidebarModule } from 'primeng/sidebar';
import {InputMaskModule} from 'primeng/inputmask';
import {BlockUIModule} from 'primeng/blockui';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import { AuditViolationNpaComponent } from './component/app.violation.npa.component';
import { NotFindComponent } from './component/notFindParagraph/notFindComponent';
import { ListLinksViolComponent } from './component/listLinksViolComponent';
import { ListLinksUnloadedNPAComponent } from './component/listLinksUnloadedNPAComponent';
import { WithoutRevComponent } from './component/withoutRevComponent';
import { MainTableRequests } from "./requests/mainTableRequests";
import { EmptyLinkRequest } from './requests/emptyLinkRequest';
import { UnloadedLinksNpaRequest } from './requests/unloadedLinksNpaRequest';
import { WithoutRevRequest } from './requests/withoutRevRequest';
import { RequestsService } from './services/requestsService';
import { CalendarService } from './services/calendarService';
import { DateTransformService } from './services/dateTransformService';
import { ExpandTreeService } from './services/expandTreeService';

const routes: Routes = [
    {path: '', component: AuditViolationNpaComponent, pathMatch: 'full'},
    {path: 'viol-npa-app', component: AuditViolationNpaComponent},
    {path: 'list-links-app', component: ListLinksViolComponent},
    {path: 'list-unloaded-app', component: ListLinksUnloadedNPAComponent},
    {path: 'without-rev-app', component: WithoutRevComponent},
    { path: '**',  redirectTo: 'no-found' }
];

@NgModule({
    imports: [ NoopAnimationsModule,
               CommonModule,
               BrowserModule,
               FormsModule,
               BusyModule,
               HttpModule,
               RouterModule.forChild(routes),
               //primeng module
               TableModule,
               InputTextModule,
               InputMaskModule,
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
               SidebarModule,
               FileUploadModule,
               TabMenuModule,
               TreeModule,
               TooltipModule,
               ToolbarModule,
               ScrollPanelModule,
               BlockUIModule],
    declarations: [ AuditViolationNpaComponent,
                    NotFindComponent,
                    ListLinksViolComponent,
                    WithoutRevComponent,
                    ListLinksUnloadedNPAComponent ],
    providers: [
        MainTableRequests,
        EmptyLinkRequest,
        UnloadedLinksNpaRequest,
        WithoutRevRequest,
        RequestsService,
        CalendarService,
        DateTransformService,
        ExpandTreeService
    ],
    exports: [ListLinksViolComponent,
              AuditViolationNpaComponent,
              ListLinksUnloadedNPAComponent,
              WithoutRevComponent,
              RouterModule],
})
export class AuditViolationNPAModule { }
