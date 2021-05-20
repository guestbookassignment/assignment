import { Routes, RouterModule } from '@angular/router';
import { EditEntryComponent } from './edit-entry.component';

const childRoutes: Routes = [
    {
        path: '',
        component: EditEntryComponent
    }
];

export const routing = RouterModule.forChild(childRoutes);
