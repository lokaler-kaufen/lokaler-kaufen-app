import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {wrapRequest} from '../api-utilities';
import {ShopAdminDto} from '../../data/client/model/shopAdminDto';
import {ShopsAdminDto} from '../../data/client/model/shopsAdminDto';
import {UpdateShopDto} from '../../data/client/model/updateShopDto';

const API = '/api/admin/shop';

@Injectable({providedIn: 'root'})
export class ShopAdminClient {

  constructor(private http: HttpClient) {
  }

  getShopSettings(id: string): Promise<ShopAdminDto> {
    return wrapRequest(
      this.http.get(`${API}/${id}`),
      '[ShopAdminClient] failed to retrieve shop'
    );
  }

  listAll(): Promise<ShopsAdminDto> {
    return wrapRequest(
      this.http.get(API),
      '[ShopAdminClient] failed to retrieve shop list'
    );
  }

  changeApprove(id: string, approved: boolean): Promise<void> {
    return wrapRequest(
      this.http.put(`${API}/${id}/approve`, {
        params: new HttpParams().set('approved', `${approved}`)
      }),
      '[ShopAdminClient] failed to change shop approval'
    );
  }

  update(id: string, request: UpdateShopDto): Promise<ShopAdminDto> {
    return wrapRequest(
      this.http.put(`${API}/${id}`, request),
      '[ShopAdminClient] failed to update shop'
    );
  }

  // can't use "delete" directly as it is a JavaScript keyword
  ['delete'](id: string): Promise<void> {
    return wrapRequest(
      this.http.delete(`${API}/${id}`),
      '[ShopAdminClient] failed to delete shop'
    );
  }

}
