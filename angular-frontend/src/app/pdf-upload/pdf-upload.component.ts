import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GroupService } from '../Services/group.service';

@Component({
  selector: 'app-pdf-upload',
  templateUrl: './pdf-upload.component.html',
  styleUrls: ['./pdf-upload.component.css']
})
export class PdfUploadComponent implements OnInit {
  selectedFile: File | null = null;
  selectedGroupId: number | null = null;
  allGroups: any[] = [];
  uploadSuccess = false;
  uploadError = '';

  constructor(private http: HttpClient, private groupService: GroupService) {}

  ngOnInit() {
    // Fetch all groups when the component initializes
    this.groupService.getAll().subscribe({
      next: (data) => {
        this.allGroups = data;
      },
      error: (err) => {
        this.uploadError = 'Error fetching groups';
        console.error(err);
      }
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onUpload() {
    if (!this.selectedFile || !this.selectedGroupId) {
      this.uploadError = 'Please select a file and group';
      return;
    }
  
    const formData = new FormData();
    formData.append('file', this.selectedFile);
  
    // Log to check file and group ID
    console.log('Selected file:', this.selectedFile);
    console.log('Selected group ID:', this.selectedGroupId);
  
    this.http.post(`http://localhost:8080/api/group/add_file/${this.selectedGroupId}`, formData)
      .subscribe({
        next: (response) => {
          console.log('Upload response:', response);  // Log response from the backend
          this.uploadSuccess = true;
          this.uploadError = '';
        },
        error: (err) => {
          this.uploadError = 'Error occurred while uploading PDF';
          console.error('Upload error:', err);  // Log errors to debug
        }
      });
  }
  
}
