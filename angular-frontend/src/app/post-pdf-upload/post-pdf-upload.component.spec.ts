import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostPdfUploadComponent } from './post-pdf-upload.component';

describe('PostPdfUploadComponent', () => {
  let component: PostPdfUploadComponent;
  let fixture: ComponentFixture<PostPdfUploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PostPdfUploadComponent]
    });
    fixture = TestBed.createComponent(PostPdfUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
