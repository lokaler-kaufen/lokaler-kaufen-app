import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';
import {ErrorReportingService} from '../shared/error-reporting.service';

@Component({
  selector: 'cancel-reservation',
  templateUrl: './cancel-reservation.component.html',
  styleUrls: ['./cancel-reservation.component.css']
})
export class CancelReservationComponent implements OnInit {

  token: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private client: HttpClient,
              private notificationsService: NotificationsService,
              private errorReportingService: ErrorReportingService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params.token;
    });
  }

  cancelReservation() {
    this.client.delete('/api/reservation?token=' + encodeURIComponent(this.token))
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Wir haben Ihre Buchung storniert. Der Laden wird von uns informiert.');
          this.router.navigate(['/']);
        },
        error => {
          console.log('Error cancelling reservation: ' + error.status + ', ' + error.message);
          this.errorReportingService.reportError(JSON.stringify(error.error), '/api/shop/send-create-link',
            error.status, error.headers.get('x-trace-id'));
          this.notificationsService.error('Tut uns leid!', 'Wir konnten Ihre Buchung leider nicht stornieren.');
        });
  }

}
