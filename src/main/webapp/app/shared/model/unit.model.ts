import { IReactor } from 'app/shared/model/reactor.model';

export interface IUnit {
  id?: number;
  title?: string | null;
  description?: string | null;
  reactor?: IReactor | null;
}

export const defaultValue: Readonly<IUnit> = {};
