import {Component, OnInit} from '@angular/core';
import {InfoRestClient} from '../api/info/info-rest.client';
import {VersionDto} from '../data/client/model/versionDto';

@Component({
  selector: 'app-footer',
  templateUrl: './app-footer.component.html',
  styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {

  versionInfo: VersionDto;

  constructor(private client: InfoRestClient) {
  }

  ngOnInit(): void {
    this.client.version().then(info => {
      this.versionInfo = info;
    });
  }

}
