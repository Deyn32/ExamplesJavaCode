/**
 * Created by dmihaylov on 14.02.2018.
 */
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuditComponent } from './components/auditComponent';
import { MergedTreesComponent } from './components/mergedTrees/mergedTreesComponent';
const routes: Routes = [
    {path: 'auditComponent', component: AuditComponent },
    {path: 'mergedTreesComponent', component: MergedTreesComponent }
];

@NgModule({
    exports: [RouterModule],
    imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule{}