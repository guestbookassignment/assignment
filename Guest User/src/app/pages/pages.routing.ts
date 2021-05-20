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
            { path: '', redirectTo: 'entry', pathMatch: 'full' },
            { path: 'entry', loadChildren: './entry/entry.module#EntryModule' },
        ]
    }
];

export const routing = RouterModule.forChild(childRoutes);
