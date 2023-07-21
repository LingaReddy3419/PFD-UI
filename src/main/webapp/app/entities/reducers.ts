import block from 'app/entities/block/block.reducer';
import document from 'app/entities/document/document.reducer';
import image from 'app/entities/image/image.reducer';
import impellerType from 'app/entities/impeller-type/impeller-type.reducer';
import mOC from 'app/entities/moc/moc.reducer';
import reactor from 'app/entities/reactor/reactor.reducer';
import unit from 'app/entities/unit/unit.reducer';
import video from 'app/entities/video/video.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  block,
  document,
  image,
  impellerType,
  mOC,
  reactor,
  unit,
  video,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
