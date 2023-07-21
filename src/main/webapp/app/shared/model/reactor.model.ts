import { IUnit } from 'app/shared/model/unit.model';
import { IBlock } from 'app/shared/model/block.model';
import { IMOC } from 'app/shared/model/moc.model';
import { IImpellerType } from 'app/shared/model/impeller-type.model';
import { IImage } from 'app/shared/model/image.model';
import { IVideo } from 'app/shared/model/video.model';
import { IDocument } from 'app/shared/model/document.model';

export interface IReactor {
  id?: number;
  workingVolume?: number | null;
  vesselId?: string | null;
  bottomImpellerStirringVolume?: number | null;
  minimumTempSensingVolume?: number | null;
  unit?: IUnit | null;
  block?: IBlock | null;
  moc?: IMOC | null;
  impellerType?: IImpellerType | null;
  images?: IImage[] | null;
  videos?: IVideo[] | null;
  documents?: IDocument[] | null;
}

export const defaultValue: Readonly<IReactor> = {};
