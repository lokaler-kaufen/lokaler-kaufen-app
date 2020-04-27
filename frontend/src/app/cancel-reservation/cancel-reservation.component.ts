import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AsyncNotificationService} from '../i18n/async-notification.service';
import {ReservationClient} from '../api/reservation/reservation.client';

@Component({
  selector: 'cancel-reservation',
  templateUrl: './cancel-reservation.component.html',
  styleUrls: ['./cancel-reservation.component.css']
})
export class CancelReservationComponent implements OnInit {

  token: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private client: ReservationClient,
              private notification: AsyncNotificationService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params.token;
    });
  }

  cancelReservation() {
    this.client.cancelReservation(this.token)
      .then(() => {
        this.notification.success('Alles klar!', 'Der Beratungstermin wurde storniert.');
        this.router.navigate(['/']);
      })
      .catch(error => {
        console.log('Error cancelling reservation: ' + error.status + ', ' + error.message);
        this.notification.error('reservation.cancel.error.title', 'reservation.cancel.error.message');
      });
  }

}
