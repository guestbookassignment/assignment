import { Routes, RouterModule } from '@angular/router';
import { ListEntryComponent } from './list-entry.component';

const childRoutes: Routes = [
    {
        path: '',
        component: ListEntryComponent
    }
];

export const routing = RouterModule.forChild(childRoutes);
