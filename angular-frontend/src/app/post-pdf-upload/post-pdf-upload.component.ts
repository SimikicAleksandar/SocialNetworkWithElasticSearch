import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostService } from '../Services/post.service';

@Component({
  selector: 'app-post-pdf-upload',
  templateUrl: './post-pdf-upload.component.html',
  styleUrls: ['./post-pdf-upload.component.css']
})
export class PostPdfUploadComponent implements OnInit {
  selectedFile: File | null = null;
  posts: any[] = [];
  selectedPostId: number | null = null;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient, private postService: PostService) {}

  ngOnInit() {
    this.getPosts();
  }

   // Fetch posts for dropdown
   getPosts() {
    this.postService.getAll()
      .subscribe(
        (response) => {
          console.log('Posts fetched:', response);  // Debugging: log the response
          this.posts = response;
        },
        (error) => {
          console.error('Error fetching posts:', error);
        }
      );
  }

  // Handle file selection
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // Upload the selected file
  onUpload() {
    if (this.selectedFile && this.selectedPostId) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.http.post(`http://localhost:8080/api/post/add_file/${this.selectedPostId}`, formData)
        .subscribe(
          (response) => {
            this.successMessage = 'PDF uploaded successfully!';
            this.errorMessage = '';
          },
          (error) => {
            this.errorMessage = 'Error uploading PDF';
            this.successMessage = '';
          }
        );
    } else {
      this.errorMessage = 'Please select a post and a file to upload.';
    }
  }
}
