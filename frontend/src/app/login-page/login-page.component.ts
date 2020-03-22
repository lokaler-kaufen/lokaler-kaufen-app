import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

interface LoginFormValue {
  email : string;
  password : string;
}

@Component({
  selector: 'login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {

  formController = new FormGroup({
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  constructor () { }

  onSubmit () : void {
    const formValue : LoginFormValue = this.formController.value;
    console.log(formValue);
  }

}
