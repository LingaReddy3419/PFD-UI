import { IGeneral } from 'app/shared/model/general.model';

export interface IOperations {
  id?: number;
  title?: string | null;
  description?: string | null;
  general?: IGeneral | null;
}

export const defaultValue: Readonly<IOperations> = {};
