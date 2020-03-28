import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NotificationsService} from 'angular2-notifications';

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
              private notificationsService: NotificationsService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params.token;
    });
  }

  cancelReservation() {
    this.client.delete('/api/reservation?token=' + encodeURIComponent(this.token))
      .subscribe(() => {
          this.notificationsService.success('Alles klar!', 'Wir haben deine Buchung storniert. Der Händler wird von uns informiert.');
          this.router.navigate(['/']);
        },
        error => {
          console.log('Error cancelling reservation: ' + error.status + ', ' + error.message);
          this.notificationsService.error('Tut uns leid!', 'Wir konnten deine Buchung leider nicht stornieren.');
        });
  }

}
