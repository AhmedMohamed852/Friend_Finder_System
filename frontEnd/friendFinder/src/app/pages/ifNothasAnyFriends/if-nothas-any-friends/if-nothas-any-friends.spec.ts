import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IfNothasAnyFriends } from './if-nothas-any-friends';

describe('IfNothasAnyFriends', () => {
  let component: IfNothasAnyFriends;
  let fixture: ComponentFixture<IfNothasAnyFriends>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IfNothasAnyFriends],
    }).compileComponents();

    fixture = TestBed.createComponent(IfNothasAnyFriends);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
