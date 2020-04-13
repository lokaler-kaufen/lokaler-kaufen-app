import {Injectable} from '@angular/core';
import {Notification, NotificationsService} from 'angular2-notifications';
import {TranslateService} from '@ngx-translate/core';

type PopupType = 'success' | 'error' | 'alert' | 'info' | 'warn' | 'bare';

interface KeyWithParams {
  key: string;
  params: object;
}

type Text = string | KeyWithParams;

/**
 * Convenience wrapper around the {@link NotificationsService} with i18n support.
 *
 * It will attempt to translate all given {@code title} and {@code content} Texts. If no translation can be found,
 * the original given string will be used instead.
 *
 * The Texts can either be flat strings or an object where "key" contains the text ID (string) and "params" contains
 * all values to be interpolated into the string. Take a look at "admin.details.shopApprovalSuccess.content" for an
 * example.
 */
@Injectable({providedIn: 'root'})
export class AsyncNotificationService {

  constructor(
    private ns: NotificationsService,
    private ts: TranslateService
  ) {
  }

  async success(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'success');
  }

  async error(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'error');
  }

  async alert(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'alert');
  }

  async info(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'info');
  }

  async warn(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'warn');
  }

  async bare(title: Text, content: Text): Promise<Notification> {
    return await this.openPopup(title, content, 'bare');
  }

  private async openPopup(title: Text, content: Text, type: PopupType): Promise<Notification> {
    const titleText = await this.translate(title);
    const contentText = await this.translate(content);

    return this.ns[type](titleText, contentText);
  }

  private async translate(text: Text): Promise<string> {
    if (typeof text === 'string') {
      return this.ts.get(text).toPromise();
    } else {
      return this.ts.get(text.key, text.params).toPromise();
    }
  }


}
