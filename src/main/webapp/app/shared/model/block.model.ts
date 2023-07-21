import { IReactor } from 'app/shared/model/reactor.model';

export interface IBlock {
  id?: number;
  title?: string | null;
  description?: string | null;
  reactor?: IReactor | null;
}

export const defaultValue: Readonly<IBlock> = {};
