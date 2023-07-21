import { IReactor } from 'app/shared/model/reactor.model';

export interface IImage {
  id?: number;
  title?: string | null;
  description?: string | null;
  fileName?: string | null;
  url?: string | null;
  reactor?: IReactor | null;
}

export const defaultValue: Readonly<IImage> = {};
