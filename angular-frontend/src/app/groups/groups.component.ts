import {Component, Input} from '@angular/core';
import {AuthServiceService} from "../Services/auth.service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GroupService} from "../Services/group.service";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent {
 groups:any;
 searchKeyword: string = ''; // For the search input
 errorMessage: string = '';
 b = 0;

searchName: string = '';
searchDescription: string = '';
searchPdfContent: string = ''; // New input for searching PDF content
searchOperator: string = 'OR'; 
advancedResults: any[] = [];

  constructor(
    private authService: AuthServiceService,
    private router: Router,
    private route: ActivatedRoute,
    private groupService: GroupService,

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
      returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
      this.router.navigate([returnUrl + '/group',elementId]);
    }


  }
  async ngOnInit() {


    this.groupService.getAll().subscribe((data) => {
      this.groups= data;

      this.b=1;
    });

  }


  onSearch() {
    if (this.searchKeyword.trim() === '') {
      this.errorMessage = 'Please enter a search keyword';
      return;
    }
    this.errorMessage = '';
  
    this.groupService.simpleSearch([this.searchKeyword])
      .subscribe({
        next: (response) => {
          console.log('Search Result:', response);  // Log the response for debugging
          if (response && response.content && Array.isArray(response.content)) {
            this.groups = response.content;  // Extract the content array
          } else {
            this.groups = [];
          }
          this.b = 1;
        },
        error: (err) => {
          this.errorMessage = 'Error occurred during search';
          console.error(err);  // Log error for debugging
        }
      });
  }

  onAdvancedSearch() {
    if (!this.searchName && !this.searchDescription && !this.searchPdfContent) {
      this.errorMessage = 'Please enter at least one search criterion';
      return;
    }
    this.errorMessage = '';
    
    const keywords = [
      `name:${this.searchName}`,
      `descriptionEn:${this.searchDescription}`,
      `pdfsranje:${this.searchPdfContent}`,
      this.searchOperator
    ];
  
    this.groupService.advancedSearch(keywords).subscribe({
      next: (response) => {
        console.log('Advanced Search Result:', response); // Log response for debugging
        this.advancedResults = response.content || [];
      },
      error: (err) => {
        this.errorMessage = 'Error occurred during search';
        console.error(err);
      }
    });
  }
  
}