import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'shop-search-page',
  templateUrl: './shop-search-page.component.html',
  styleUrls: ['./shop-search-page.component.css']
})
export class ShopSearchPageComponent implements OnInit {
  searchBusiness: string;
  selectedOrder = 'Entfernung';
  orderOptions: string[] = ['Entfernung', 'Alphabetisch'];

  constructor() {
  }

  ngOnInit(): void {
  }

}
