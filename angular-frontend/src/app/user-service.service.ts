import {Injectable} from '@angular/core';
import {ApiService} from './api-service.service';
import {ConfigServiceService} from './Services/config-service.service';
import {map} from 'rxjs/operators';
import {HttpHeaders} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  currentUser :any;

  constructor(
    private apiService: ApiService,
    private config: ConfigServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  getOne() {
    return this.apiService.get(this.config._myProfile_url);
  }

  saveUser(userToSave:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });

    if(userToSave.name.length > 4 && userToSave.content.length > 5){

    }
    else{
      alert("Display name or description too short!")
      return;
    }
    const body = {
      'displayName': userToSave.name,
      'description': userToSave.content
    };

    return this.apiService.post(this.config._saveuser_url, JSON.stringify(body), loginHeaders)
      .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("Error")
        }else {
          alert("Save success");
          let returnUrl : String;
          returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigate([returnUrl + "/HomePage"]);
        }
      });
  }
}
