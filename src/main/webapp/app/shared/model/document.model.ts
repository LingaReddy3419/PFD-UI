import { IReactor } from 'app/shared/model/reactor.model';

export interface IDocument {
  id?: number;
  title?: string | null;
  description?: string | null;
  fileName?: string | null;
  url?: string | null;
  reactor?: IReactor | null;
}

export const defaultValue: Readonly<IDocument> = {};
