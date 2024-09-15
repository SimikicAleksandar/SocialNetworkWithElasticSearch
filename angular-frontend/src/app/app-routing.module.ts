import { NgModule } from '@angular/core';
import { PdfUploadComponent } from './pdf-upload/pdf-upload.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
 {path: 'upload-pdf', component: PdfUploadComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
