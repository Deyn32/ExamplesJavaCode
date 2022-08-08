import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BusyModule} from 'angular2-busy';
import { AccordionModule, CalendarModule, CheckboxModule, ContextMenuModule, DataTableModule, DialogModule, DropdownModule, FileUploadModule, GrowlModule, InputTextareaModule, InputTextModule } from 'primeng/primeng';
import { MenuModule,  OverlayPanelModule,PanelModule, ProgressBarModule, SharedModule, SlideMenuModule, ToolbarModule, TooltipModule, TreeModule, TreeTableModule } from 'primeng/primeng';
import { TabMenuModule } from 'primeng/tabmenu';
import { MultiSelectModule } from 'primeng/multiselect';
import { BlockUIModule } from 'primeng/blockui';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { ListboxModule } from 'primeng/listbox';
import { Routes, RouterModule } from '@angular/router';
import { Error } from './classes/error';
import { AuditComponent } from './components/auditComponent';
import { MtComponent } from './components/mergedTrees/mergedTrees';
import { CompareRevisComponent } from './components/compareRevis/compareRevis';
import { ImageViewComponent } from './components/imageView/imageView';
import { TreeStructRevComponent} from "./components/treeStructRev/treeStructRev";
import { FullElemStructRevComponent } from './components/fullElemStructRev/fullElemStructRev';
import { TreeDownloadTextComponent } from './components/treeDownloadText/treeDownloadText';
import { UploadDocument } from './components/uploadNpa/uploadNpa';
import { DownloadNpa } from './components/downloadNpa/downloadNpa'
import { DocumentNPARequests } from './requests/documentNPARequests';
import { RevisService } from './requests/revisionRequest';
import { TreesRequests } from './requests/treeRequests';
import { Dictionary } from './requests/dictionaryRequests';
import { OrganizationRequests } from './requests/organizationRequests';
import { UploadNpaRequests } from './requests/UploadNpaRequests';
import { DownloadNpaRequests } from './requests/downloadNpaRequests';
import { CalendarService } from './services/calendarService';
import { Head } from './services/createHeaderService';
import { DateConvertService } from './services/dateConvertService';
import { GrowlService } from './services/growlService';
import { RequestService } from './services/requestService';
import { RoutingService } from './services/routingService';
import { TranslateModule } from 'ng2-translate';

const routes: Routes = [
    {path: '', component: AuditComponent, pathMatch: 'full'},
    {path: 'sg-comp-app', component: AuditComponent},
    {path: 'upload-doc-app', component: UploadDocument},
    {path: 'download-doc-app', component: DownloadNpa},
    {path: '**',  redirectTo: 'no-found'}];

@NgModule({
    imports:      [ NoopAnimationsModule,
        CommonModule,
        BrowserModule,
        TranslateModule.forRoot(),
        RouterModule.forChild(routes),
        FormsModule,
        BusyModule,
        HttpModule,
        InputTextModule,
        CalendarModule,
        CheckboxModule,
        BlockUIModule,
        ContextMenuModule,
        ScrollPanelModule,
        GrowlModule,
        DialogModule,
        ListboxModule,
        ProgressBarModule,
        MultiSelectModule,
        AccordionModule,
        DataTableModule,
        SharedModule,
        InputTextareaModule,
        DropdownModule,
        TreeTableModule,
        OverlayPanelModule,
        MenuModule,
        SlideMenuModule,
        FileUploadModule,
        TreeModule,
        TooltipModule,
        PanelModule,
        TabMenuModule,
        ToolbarModule],
    declarations: [ AuditComponent,
                    MtComponent,
                    CompareRevisComponent,
                    ImageViewComponent,
                    TreeStructRevComponent,
                    FullElemStructRevComponent,
                    TreeDownloadTextComponent,
                    UploadDocument,
                    DownloadNpa],
    providers: [
        {
            provide: 'Window', useValue: window
        },
        CalendarService,
        DocumentNPARequests,
        Dictionary,
        GrowlService,
        RevisService,
        TreesRequests,
        OrganizationRequests,
        UploadNpaRequests,
        DownloadNpaRequests,
        Head,
        RequestService,
        DateConvertService,
        RoutingService,
        Error
    ],
    exports: [
        TranslateModule,
        RouterModule,
        AuditComponent,
        UploadDocument,
        DownloadNpa
    ]
})
export class AuditModule { }
