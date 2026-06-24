import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyFriends } from './my-friends';

describe('MyFriends', () => {
  let component: MyFriends;
  let fixture: ComponentFixture<MyFriends>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyFriends],
    }).compileComponents();

    fixture = TestBed.createComponent(MyFriends);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
