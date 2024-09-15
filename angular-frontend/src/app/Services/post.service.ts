import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";
import {ApiService} from "../api-service.service";
import {UserServiceService} from "../user-service.service";
import {ConfigServiceService} from "./config-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import { HttpClient, HttpHeaders } from "@angular/common/http"; // Add HttpClient
import { Observable } from 'rxjs'; // Add Observable

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(  public jwtHelper: JwtHelperService,
                private apiService: ApiService,
                private userService: UserServiceService,
                private config: ConfigServiceService,
                private router: Router,
                private route: ActivatedRoute,
                private http: HttpClient // Inject HttpClient
              ) { }

  delete(id:any) {

    // const body = `username=${user.username}&password=${user.password}`;
    const body = id;
    return this.apiService.delete(this.config._deletepost_url, JSON.stringify(body))
      .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("Error")
        }else {
          alert("Delete success");
          window.location.reload();
        }
      });
  }
  create(user:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });

    if(user.groupList == null) {
      alert("Destination must be choosen!")
      return;
    }

    if(user.groupList == 0){
      const body = {
        'text': user.post,

      };
      alert("Post created on my profile!")
      return this.apiService.post(this.config._postcreate_url, JSON.stringify(body), loginHeaders)
        .subscribe((res) => {
          if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
          {
            alert("Wrong Details")
          }else {
            alert("Creation success");
            let returnUrl : String;
            returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
            this.router.navigate([returnUrl + "/HomePage"]);
          }
        });
    }
    else{
      const body = {
        'text': user.post,
        'groupID':user.groupList
      };
      alert("Post created in selected group!")
      return this.apiService.post(this.config._postsavetogroup_url, JSON.stringify(body), loginHeaders)
        .subscribe((res) => {
          if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
          {
            alert("Wrong Details")
          }else {
            alert("Creation success");
            let returnUrl : String;
            returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
            this.router.navigate([returnUrl + "/HomePage"]);
          }
        });
    }
  }
  save(post:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    // const body = `username=${user.username}&password=${user.password}`;
    const body = {
      'id': post.id,
      'groupList': post.groupList,
      'content': post.content,


    };

    return this.apiService.post(this.config._postsave_url, JSON.stringify(body), loginHeaders)
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
  getAll() {
     return this.apiService.get(this.config._postAll_url);
  }

  getAbsAll() {
    return this.apiService.get(this.config._abs_allpost_url);
  }
  getOne(a:any) {
    const body =a;
    return this.apiService.post(this.config._postone_url,body);
  }

   // Adjusted return type to Observable instead of subscribing here
 simpleSearch(keywords: string[]): Observable<any> {
  return this.http.post<any>('http://localhost:8080/api/searchPosts/simple', { keywords });
}


}
