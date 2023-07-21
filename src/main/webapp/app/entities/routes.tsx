import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Block from './block';
import Document from './document';
import Image from './image';
import ImpellerType from './impeller-type';
import MOC from './moc';
import Reactor from './reactor';
import Unit from './unit';
import Video from './video';
import Action from './action';
import General from './general';
import ModeOfCharging from './mode-of-charging';
import Operations from './operations';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="block/*" element={<Block />} />
        <Route path="document/*" element={<Document />} />
        <Route path="image/*" element={<Image />} />
        <Route path="impeller-type/*" element={<ImpellerType />} />
        <Route path="moc/*" element={<MOC />} />
        <Route path="reactor/*" element={<Reactor />} />
        <Route path="unit/*" element={<Unit />} />
        <Route path="video/*" element={<Video />} />
        <Route path="action/*" element={<Action />} />
        <Route path="general/*" element={<General />} />
        <Route path="mode-of-charging/*" element={<ModeOfCharging />} />
        <Route path="operations/*" element={<Operations />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
