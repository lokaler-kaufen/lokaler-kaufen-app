export class ShopOverviewDto {
  constructor(id: string, name: string, distance: number, supportedContactTypes: string[]) {
    this.id = id;
    this.name = name;
    this.distance = distance;
    this.supportedContactTypes = supportedContactTypes;
  }

  id: string;
  name: string;
  distance: number;
  supportedContactTypes: string[];
}
