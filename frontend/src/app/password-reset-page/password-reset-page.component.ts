import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {AsyncNotificationService} from '../i18n/async-notification.service';

@Component({
  selector: 'password-reset-page',
  templateUrl: './password-reset-page.component.html',
  styleUrls: ['./password-reset-page.component.css']
})
export class PasswordResetPageComponent implements OnInit {
  form: FormGroup;
  token: string;
  passwordRegex: RegExp = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.{12,})');


  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private client: HttpClient,
              private router: Router,
              private notificationsService: AsyncNotificationService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params.token;
    });
    this.form = this.formBuilder.group({
      passwordCtrl: ['', [Validators.required, Validators.pattern(this.passwordRegex), Validators.minLength(12)]],
      confirmPasswordCtrl: ['', Validators.required]
    }, {validator: this.checkMatchingPasswords('passwordCtrl', 'confirmPasswordCtrl')});

  }

  onSubmit() {
    this.client.post('/api/shop/reset-password?token=' + encodeURIComponent(this.token), {
      password: this.form.controls.passwordCtrl.value
    }).subscribe(() => {
        this.router.navigate(['/login']);
      },
      error => {
        console.log('Error resetting password: ' + error.status + ', ' + error.message);
        this.notificationsService.error('password.reset.page.submit.error.title', 'password.reset.page.submit.error.message');
      });
  }

  // Validation password equals confirmed password
  checkMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
      const passwordInput = group.controls[passwordKey];
      const passwordConfirmationInput = group.controls[passwordConfirmationKey];

      if (passwordInput.value !== passwordConfirmationInput.value) {
        return passwordConfirmationInput.setErrors({notEquivalent: true});

      } else {
        return passwordConfirmationInput.setErrors(null);
      }
    };
  }

}
