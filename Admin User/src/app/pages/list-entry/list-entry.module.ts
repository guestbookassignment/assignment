import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { routing } from './list-entry.routing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { ListEntryComponent } from './list-entry.component';



@NgModule({
    imports: [
        NgxPaginationModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        routing
    ],
    declarations: [
        ListEntryComponent
    ]
})
export class ListEntryModule { }
