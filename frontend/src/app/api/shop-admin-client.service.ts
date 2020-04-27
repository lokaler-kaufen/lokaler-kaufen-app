import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ApiClientBase} from './api-client-base.service';
import {ShopAdminDto} from '../data/client/model/shopAdminDto';
import {ShopsAdminDto} from '../data/client/model/shopsAdminDto';
import {UpdateShopDto} from '../data/client/model/updateShopDto';

const API = '/api/admin/shop';

@Injectable({providedIn: 'root'})
export class ShopAdminClient {

  constructor(private http: HttpClient, private base: ApiClientBase) {
  }

  getShopSettings(id: string): Promise<ShopAdminDto> {
    return this.base.promisify(
      this.http.get(`${API}/${id}`),
      '[ShopAdminClient] failed to retrieve shop'
    );
  }

  listAll(): Promise<ShopsAdminDto> {
    return this.base.promisify(
      this.http.get(API),
      '[ShopAdminClient] failed to retrieve shop list'
    );
  }

  changeApprove(id: string, approved: boolean): Promise<void> {
    return this.base.promisify(
      this.http.put(`${API}/${id}/approve`, {
        params: new HttpParams().set('approved', `${approved}`)
      }),
      '[ShopAdminClient] failed to change shop approval'
    );
  }

  update(id: string, request: UpdateShopDto): Promise<ShopAdminDto> {
    return this.base.promisify(
      this.http.put(`${API}/${id}`, request),
      '[ShopAdminClient] failed to update shop'
    );
  }

  // can't use "delete" directly as it is a JavaScript keyword
  ['delete'](id: string): Promise<void> {
    return this.base.promisify(
      this.http.delete(`${API}/${id}`),
      '[ShopAdminClient] failed to delete shop'
    );
  }

}
