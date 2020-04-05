import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-footer',
  templateUrl: './app-footer.component.html',
  styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {

  // version info
  versionInfo: VersionInfo;

  constructor(private client: HttpClient) {
  }

  ngOnInit(): void {
    this.client.get<VersionInfo>('/api/info/version').toPromise().then(info => {
      this.versionInfo = info;
    });
  }

}

export interface VersionInfo {
  commitHash: string;
  commitTime: string;
  localChanges: string;
  version: string;
}
