"use strict";
/**
 * Created by dmihaylov on 23.05.2017.
 */
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var auditModule_1 = require('./modules/app/auditModule');
var core_1 = require('@angular/core');
core_1.enableProdMode();
platform_browser_dynamic_1.platformBrowserDynamic().bootstrapModule(auditModule_1.AuditModule);
//# sourceMappingURL=main.js.map