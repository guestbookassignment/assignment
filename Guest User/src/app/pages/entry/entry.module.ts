import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { routing } from './entry.routing';
import { EntryComponent } from './entry.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        routing,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        EntryComponent
    ]
})
export class EntryModule { }
