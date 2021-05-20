import { Routes, RouterModule } from '@angular/router';
import { PagesComponent } from './pages.component';
import { LoginComponent } from './login/login.component';

export const childRoutes: Routes = [
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'pages',
        component: PagesComponent,
        children: [
            { path: '', redirectTo: 'list-entry', pathMatch: 'full' },
            { path: 'list-entry', loadChildren: './list-entry/list-entry.module#ListEntryModule'},
            { path: 'edit-entry/:id', loadChildren: './edit-entry/edit-entry.module#EditEntryModule'}
        ]
    }
];

export const routing = RouterModule.forChild(childRoutes);
