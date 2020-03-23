import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

interface LoginFormValue {
  email: string;
  password: string;
}

enum LoginState {
  BLANK, PENDING, FAILED
}

@Component({
  selector: 'login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {

  formController = new FormGroup({
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  loginState: LoginState = LoginState.BLANK;

  constructor(private client: HttpClient, private router: Router) {
  }

  onSubmit(): void {
    if (!this.formController.valid) {
      return;
    }

    this.loginState = LoginState.PENDING;
    const credentials: LoginFormValue = this.formController.value;

    this.client.post('/api/shop/login', credentials)
      .subscribe(
        () => {
          this.loginState = LoginState.BLANK;
          this.router.navigate(['manage-shop']);
        },
        error => {
          console.error('Login request failed.', error);
          this.loginState = LoginState.FAILED;
        }
      );
  }

  isFailed(): boolean {
    return this.loginState === LoginState.FAILED;
  }

  isPending(): boolean {
    return this.loginState === LoginState.PENDING;
  }

}
