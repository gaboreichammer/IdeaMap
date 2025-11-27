import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { Login } from './login';
import { AuthService } from '../../services/auth.service';

describe('Login', () => {
  let fixture: ComponentFixture<Login>;
  let component: Login;
  let mockAuthService: Partial<AuthService>;
  let router: Router;

  beforeEach(async () => {
    mockAuthService = {
      login: jasmine.createSpy('login'),
      storeToken: jasmine.createSpy('storeToken')
    };

    await TestBed.configureTestingModule({
      imports: [Login, RouterTestingModule.withRoutes([])],
      providers: [{ provide: AuthService, useValue: mockAuthService }]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService.login, store token, navigate and set success on successful login', () => {
    // Arrange
    component.username.set('alice');
    component.password.set('secret');
    (mockAuthService.login as jasmine.Spy).and.returnValue(of({ token: 'jwt-token', message: 'Welcome' }));

    spyOn(router, 'navigate');

    // Act
    component.login();
    fixture.detectChanges();

    // Assert
    expect(mockAuthService.login).toHaveBeenCalledWith({ username: 'alice', password: 'secret' });
    expect(mockAuthService.storeToken).toHaveBeenCalledWith('jwt-token');
    expect(component.message()).toBe('Welcome');
    expect(component.isSuccess()).toBeTrue();
    expect(router.navigate).toHaveBeenCalledWith(['/landing']);
    expect(component.isLoading()).toBeFalse();
  });

  it('should set error message from 401 response and not set success', () => {
    // Arrange
    component.username.set('bob');
    component.password.set('bad');
    const httpErr = new HttpErrorResponse({ status: 401, error: 'Invalid username or password.' });
    (mockAuthService.login as jasmine.Spy).and.returnValue(throwError(() => httpErr));

    // Act
    component.login();
    fixture.detectChanges();

    // Assert
    expect(component.error()).toBe('Invalid username or password.');
    expect(component.isSuccess()).toBeFalse();
    expect(component.isLoading()).toBeFalse();
    expect(mockAuthService.storeToken).not.toHaveBeenCalled();
  });

  it('should set general connection error message for non-401 failures', () => {
    // Arrange
    component.username.set('carol');
    component.password.set('xyz');
    const httpErr = new HttpErrorResponse({ status: 500, error: 'Server down' });
    (mockAuthService.login as jasmine.Spy).and.returnValue(throwError(() => httpErr));

    // Act
    component.login();
    fixture.detectChanges();

    // Assert
    expect(component.error()).toContain('Connection error. Please check the server status.');
    expect(component.error()).toContain('Server down');
    expect(component.isSuccess()).toBeFalse();
    expect(component.isLoading()).toBeFalse();
    expect(mockAuthService.storeToken).not.toHaveBeenCalled();
  });
});
