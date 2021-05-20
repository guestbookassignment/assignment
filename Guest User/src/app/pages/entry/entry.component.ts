import { Component, OnInit } from '@angular/core';
import { Http, RequestOptions, ResponseContentType, Headers } from '@angular/http';
import { environment } from "../../../environments/environment";
@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent implements OnInit {
  uploadedFile: File;
  data: String;
  fileUploadUrl = environment.host + 'guest/entry/file-upload';
  dataEntryUrl = environment.host + 'guest/entry';
  constructor(private http: Http) { }

  ngOnInit() { }
  readUrl(event) {
    if (!this.validateFile(event.target.files[0].name)) {
      alert("Selected file format is not supported. Kindly upload a png/jpg/jpeg file");
      return;
  }
    this.uploadedFile = event.target.files[0];
  }

  reset() {
    this.data = undefined;
    this.uploadedFile = undefined;
  }

  upload() {
    if(this.uploadedFile && this.data) {
      alert("Either upload the file or enter text but not both!");
      return;
    } 
    if(this.uploadedFile) {
      this.uploadFile(this.uploadedFile);
    } else if(this.data) {
      this.postText(this.data);
    } else {
      alert("Kindly enter valid text or upload an image and try again");
    }
  }

  validateFile(name: String) {
    var ext = name.substring(name.lastIndexOf('.') + 1);
    ext = ext.toLowerCase();
    if (ext == 'png' || ext == 'jpg' || ext == 'jpeg') {
        return true;
    }
    else {
        return false;
    }
  }

  postText(data: String) {
    var headers = new Headers();
    headers.append('Authorization', 'Basic ' +
        localStorage.getItem('token'));
    return this.http.post(this.dataEntryUrl, {
      'content': data
    }, {
        headers: headers
    }). map(res => res.json()).
    subscribe(isValid => {
      alert("Data posted successfully!");
    },
    error=>{
      alert("Something has gone wrong! Please try again.");
    });
  }

  uploadFile(file: File) {
    var headers = new Headers();
    headers.append('Authorization', 'Basic ' +
        localStorage.getItem('token'));
    let input = new FormData();
    input.append('file', file);
    return this.http.post(this.fileUploadUrl, input, { headers: headers }).subscribe(
        data => {
          alert("Image uploaded successfully");
        },
        error => console.log("Error downloading the file."),
        () => console.info("OK")
    );  
  }

}
