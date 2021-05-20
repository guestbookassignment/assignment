import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Http } from '@angular/http';
import { FormsModule } from '@angular/forms';
import 'rxjs/add/operator/map'
import { environment } from "../../../environments/environment";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {


  model: any = {};

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private http: Http) {}

  ngOnInit() {
      localStorage.setItem('token', '');
  }

  login() {
      let url = environment.host+'login';
      let result = this.http.post(url, {
          "username": this.model.username,
          "password": this.model.password,
          "role": "USER"
      }). map(res => res.json()).
          subscribe(isValid => {
              if (isValid) {
                  localStorage.setItem('token', btoa(this.model.username + ':' + this.model.password));
                  this.router.navigate(['pages/entry']);
              } else {
                  alert("Authentication failed.")
              }
          },
          error=>{
            console.log(error);
          });
  }

}
