import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AsyncNotificationService} from '../i18n/async-notification.service';

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
              private notificationsService: AsyncNotificationService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params.token;
    });
  }

  cancelReservation() {
    this.client.delete('/api/reservation?token=' + encodeURIComponent(this.token))
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Der Beratungstermin wurde storniert.');
          this.router.navigate(['/']);
        },
        error => {
          console.log('Error cancelling reservation: ' + error.status + ', ' + error.message);
          this.notificationsService.error('reservation.cancel.error.title', 'reservation.cancel.error.message');
        });
  }

}
