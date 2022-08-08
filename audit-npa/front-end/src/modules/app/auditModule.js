"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var platform_browser_1 = require('@angular/platform-browser');
var forms_1 = require('@angular/forms');
var animations_1 = require('@angular/platform-browser/animations');
var angular2_busy_1 = require('angular2-busy');
var primeng_1 = require('primeng/primeng');
var http_1 = require('@angular/http');
var primeng_2 = require('primeng/primeng');
var calendarService_1 = require('./services/calendarService');
var auditComponent_1 = require('./components/auditComponent');
var documentNPARequests_1 = require('./requests/documentNPARequests');
var referenceRequest_1 = require('./requests/referenceRequest');
var revisionRequest_1 = require('./requests/revisionRequest');
var treeRequests_1 = require('./requests/treeRequests');
var createHeaderService_1 = require('./services/createHeaderService');
var requestService_1 = require('./services/requestService');
var dateConvertService_1 = require('./services/dateConvertService');
var error_1 = require('./classes/error');
var AuditModule = (function () {
    function AuditModule() {
    }
    AuditModule = __decorate([
        core_1.NgModule({
            imports: [animations_1.NoopAnimationsModule,
                platform_browser_1.BrowserModule,
                forms_1.FormsModule,
                angular2_busy_1.BusyModule,
                http_1.HttpModule,
                primeng_1.InputTextModule,
                primeng_1.CalendarModule,
                primeng_1.CheckboxModule,
                primeng_1.GrowlModule,
                primeng_1.DialogModule,
                primeng_1.ListboxModule,
                primeng_1.ProgressBarModule,
                primeng_1.AccordionModule,
                primeng_1.DataTableModule,
                primeng_1.SharedModule,
                primeng_1.InputTextareaModule,
                primeng_1.DropdownModule,
                primeng_1.TreeTableModule,
                primeng_1.OverlayPanelModule,
                primeng_2.MenuModule,
                primeng_1.SlideMenuModule,
                primeng_1.FileUploadModule,
                primeng_1.TooltipModule,
                primeng_1.ToolbarModule],
            declarations: [auditComponent_1.AuditComponent],
            providers: [
                {
                    provide: 'Window', useValue: window
                },
                calendarService_1.CalendarService,
                documentNPARequests_1.DocumentNPARequests,
                referenceRequest_1.Reference,
                revisionRequest_1.RevisService,
                treeRequests_1.TreesRequests,
                createHeaderService_1.Head,
                requestService_1.RequestService,
                dateConvertService_1.DateConvertService,
                error_1.Error
            ],
            bootstrap: [auditComponent_1.AuditComponent]
        })
    ], AuditModule);
    return AuditModule;
}());
exports.AuditModule = AuditModule;
//# sourceMappingURL=auditModule.js.map