import { Component } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PostService} from "../Services/post.service";
import {FormControl, FormGroup} from "@angular/forms";
import {AuthServiceService} from "../Services/auth.service.service";
import {UserServiceService} from "../user-service.service";
import {GroupService} from "../Services/group.service";
@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent {

  constructor(private _Activatedroute:ActivatedRoute, private groupService: GroupService, private router: Router, private userService : UserServiceService, private authService : AuthServiceService) {


    if(this.authService.isAuthenticated())
    {

    }
    else {
      let returnUrl : String;
      returnUrl = this._Activatedroute.snapshot.queryParams['returnUrl'] || '/';
      this.router.navigate([returnUrl]);
    }
  }
  formCurrentUser :any;
  currentUser: any;

  getElementId(event:any){

    // Get the source element
    let element = event.target || event.srcElement || event.currentTarget;
    // Get the id of the source element
    let elementId = element.id;
    if(element.innerText == "delete")
    {
      this.groupService.delete(elementId);
    }
    if(element.innerText == "edit")
    {
      let returnUrl : String;
      returnUrl = this._Activatedroute.snapshot.queryParams['returnUrl'] || '/';
      this.router.navigate([returnUrl + '/group',elementId]);
    }


  }
  async ngOnInit() {

      this.userService.getOne().subscribe((data) => {
      this.currentUser = data;
      this.formCurrentUser = new FormGroup({
      name: new FormControl(this.currentUser.displayName),
      content: new FormControl(this.currentUser.description),

      });
      this.renderable=1;
    });

  }
  id :any;
  user:any;
  renderable= 0;

  submitted = false;

  /**
   * Notification message from received
   * form request or router
   */




  private returnUrl: any;
  public a: any;
  back()
  {
    let returnUrl : String;
    returnUrl = this._Activatedroute.snapshot.queryParams['returnUrl'] || '/';
    this.router.navigate([returnUrl+"/HomePage"]);
  }
  onSubmit() {
    /**
     * Innocent until proven guilty
     */

    this.submitted = true;
    console.warn('Your order has been submitted', this.formCurrentUser.value);
    this.userService.saveUser(this.formCurrentUser.value);
  }
}
