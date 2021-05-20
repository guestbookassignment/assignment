import { Injectable } from '@angular/core';
import { Http, RequestOptions, ResponseContentType, Headers } from '@angular/http';
import { environment } from "../../../environments/environment";

@Injectable()
export class EntryListService {
    constructor(private http: Http) { }

    entriesListUrl = environment.host + 'admin/entry/';

    getAllEntries() {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append('Authorization', 'Basic ' +
            localStorage.getItem('token'));
        return this.http
            .get(this.entriesListUrl, {
                headers: headers
            })
            .toPromise()
            .then((res) => res.json());
    }

    approveEntry(id) {
        let url = this.entriesListUrl + id + '/approve';
        var headers = new Headers();
        headers.append('Authorization', 'Basic ' +
            localStorage.getItem('token'));
        return this.http.put(url, {}, {
            headers: headers
        });
    }

    deleteEntry(id) {
        let url = this.entriesListUrl + id + '/delete';
        var headers = new Headers();
        headers.append('Authorization', 'Basic ' +
            localStorage.getItem('token'));
        return this.http.delete(url, {
            headers: headers
        });
    }
}
