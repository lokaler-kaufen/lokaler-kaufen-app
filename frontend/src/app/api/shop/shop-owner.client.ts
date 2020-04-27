import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ShopOwnerDetailDto} from '../../data/client/model/shopOwnerDetailDto';
import {wrapRequest} from '../api-utilities';
import {UpdateShopDto} from '../../data/client/model/updateShopDto';

const API = '/api/shop';

@Injectable({providedIn: 'root'})
export class ShopOwnerClient {

  constructor(private http: HttpClient) {
  }

  getShopSettings(): Promise<ShopOwnerDetailDto> {
    return wrapRequest(
      this.http.get(`${API}/me`),
      '[ShopOwnerClient] failed to retrieve shop settings'
    );
  }

  updateShop(request: UpdateShopDto): Promise<ShopOwnerDetailDto> {
    return wrapRequest(
      this.http.put(API, request),
      '[ShopOwnerClient] failed to update shop'
    );
  }

}
