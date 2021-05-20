import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { routing } from './edit-entry.routing';
import { SharedModule } from '../../shared/shared.module';
import { EditEntryComponent } from './edit-entry.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        SharedModule,
        routing,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        EditEntryComponent
    ]
})
export class EditEntryModule { }
