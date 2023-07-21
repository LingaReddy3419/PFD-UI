import { IOperations } from 'app/shared/model/operations.model';
import { IAction } from 'app/shared/model/action.model';
import { IModeOfCharging } from 'app/shared/model/mode-of-charging.model';

export interface IGeneral {
  id?: number;
  operations?: IOperations | null;
  action?: IAction | null;
  modeOfCharging?: IModeOfCharging | null;
}

export const defaultValue: Readonly<IGeneral> = {};
