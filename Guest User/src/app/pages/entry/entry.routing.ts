import { Routes, RouterModule } from '@angular/router';
import { EntryComponent } from './entry.component';

const childRoutes: Routes = [
    {
        path: '',
        component: EntryComponent
    }
];

export const routing = RouterModule.forChild(childRoutes);
