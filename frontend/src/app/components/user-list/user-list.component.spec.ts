import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserListComponent } from './user-list.component';
import { UserService } from '../../services/user.service';

describe('UserListComponent', () => {
  let component: UserListComponent;
  let fixture: ComponentFixture<UserListComponent>;
  let mockUserService: any;

  beforeEach(async () => {
    // Crie um mock do UserService
    mockUserService = jasmine.createSpyObj('UserService', ['getUsers', 'createUser']);
    mockUserService.getUsers.and.returnValue(of([])); 

    await TestBed.configureTestingModule({
      imports: [
        UserListComponent, // Para componentes standalone
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: UserService, useValue: mockUserService }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display "Novo Usuário" in the heading on init', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h2')?.textContent).toContain('Novo Usuário');
  });
});