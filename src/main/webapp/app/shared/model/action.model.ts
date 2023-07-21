import { IGeneral } from 'app/shared/model/general.model';

export interface IAction {
  id?: number;
  title?: string | null;
  description?: string | null;
  param1?: string | null;
  param2?: string | null;
  param3?: string | null;
  general?: IGeneral | null;
}

export const defaultValue: Readonly<IAction> = {};
