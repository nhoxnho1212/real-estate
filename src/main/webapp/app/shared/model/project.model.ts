import { IExtra } from 'app/shared/model/extra.model';
import { IHomeType } from 'app/shared/model/home-type.model';

export interface IProject {
  id?: number;
  address?: string | null;
  city?: string | null;
  rooms?: number | null;
  price?: number | null;
  floorSpace?: number | null;
  attachment?: string | null;
  extras?: IExtra[] | null;
  homeType?: IHomeType | null;
}

export const defaultValue: Readonly<IProject> = {};
