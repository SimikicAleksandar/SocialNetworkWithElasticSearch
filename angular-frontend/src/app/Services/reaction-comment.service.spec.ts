import { TestBed } from '@angular/core/testing';

import { ReactionCommentService } from './reaction-comment.service';

describe('ReactionCommentService', () => {
  let service: ReactionCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReactionCommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
