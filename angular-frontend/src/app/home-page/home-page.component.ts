import { Component } from '@angular/core';
import {AuthServiceService} from "../Services/auth.service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {PostService} from "../Services/post.service";
import {UserServiceService} from "../user-service.service";
import {GroupService} from "../Services/group.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {
  searchKeyword: string = ''; // For the search input
  errorMessage: string = '';
  b = 0;


  formPost = new FormGroup({
    post: new FormControl(''),
    groupList: new FormControl()
  });
  submitted = false;

  allFeedPosts:any;
  allGroups:any;
   allPosts:any;
  onSubmit() {

    this.submitted = true;
    console.warn('Your order has been submitted', this.formPost.value);
    this.postService.create(this.formPost.value)
    location.reload();
  }
  constructor(
    private authService: AuthServiceService,
    private userService: UserServiceService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private postService : PostService,
    private groupService : GroupService

  ) {
    if(this.authService.isAuthenticated())
    {

    }
    else {
      let returnUrl : String;
      returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
      this.router.navigate([returnUrl]);
    }

  }
  changePassword()
  {
    let returnUrl : String;
    returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/passchange';
    this.router.navigate([returnUrl]);
  }
  LogOut()
  {
    let returnUrl : String;
    localStorage.clear();
    returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.router.navigate([returnUrl]);
  }
  ngOnInit() {

    this.postService.getAll().subscribe((data) => {
    this.allPosts = data;

    });

    this.groupService.getAll().subscribe((data) => {
      this.allGroups = data;
    })

    this.postService.getAbsAll().subscribe((data) => {
      this.allFeedPosts = data;

    });
  }

  onSearch() {
    if (this.searchKeyword.trim() === '') {
      this.errorMessage = 'Please enter a search keyword';
      return;
    }
    this.errorMessage = '';

    this.postService.simpleSearch([this.searchKeyword]).subscribe({
      next: (response) => {
        console.log('Search Response:', response); // Check the structure here
        this.allPosts = response.content || []; // Extract content from the response
        this.b = 1;
      },
      error: (err) => {
        this.errorMessage = 'Error occurred during search';
        console.error(err);
      }
    });
  }
}
