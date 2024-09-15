import { NgModule } from '@angular/core';
import { PdfUploadComponent } from './pdf-upload/pdf-upload.component';
import { RouterModule, Routes } from '@angular/router';
import { PostPdfUploadComponent } from './post-pdf-upload/post-pdf-upload.component';

const routes: Routes = [
 {path: 'upload-pdf', component: PdfUploadComponent},
 {path: 'post-upload-pdf', component: PostPdfUploadComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
