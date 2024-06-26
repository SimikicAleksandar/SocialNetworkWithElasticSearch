import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";
import {ApiService} from "../api-service.service";
import {UserServiceService} from "../user-service.service";
import {ConfigServiceService} from "./config-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ReactionCommentService {

  constructor(  public jwtHelper: JwtHelperService,
                private apiService: ApiService,
                private userService: UserServiceService,
                private config: ConfigServiceService,
                private router: Router,
                private route: ActivatedRoute,) { }

  save(post:any, reactionInt: any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    const body = {
      'id': post,
      'reactionInt': reactionInt
    };

    return this.apiService.post(this.config._reactionsave_url, JSON.stringify(body))
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
  public g : any;
  // getAll() {
  //   return this.apiService.get(this.config._postAll_url);
  // }
  //
  // getAbsAll() {
  //   return this.apiService.get(this.config._abs_allpost_url);
  // }
  // getOne(a:any) {
  //   const body =a;
  //   return this.apiService.post(this.config._postone_url,body);
  // }
}
