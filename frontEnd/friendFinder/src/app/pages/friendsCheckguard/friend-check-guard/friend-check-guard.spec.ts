import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendCheckGuard } from './friend-check-guard';

describe('FriendCheckGuard', () => {
  let component: FriendCheckGuard;
  let fixture: ComponentFixture<FriendCheckGuard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FriendCheckGuard],
    }).compileComponents();

    fixture = TestBed.createComponent(FriendCheckGuard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
