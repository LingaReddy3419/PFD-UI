import { IReactor } from 'app/shared/model/reactor.model';

export interface IImpellerType {
  id?: number;
  title?: string | null;
  description?: string | null;
  reactor?: IReactor | null;
}

export const defaultValue: Readonly<IImpellerType> = {};
