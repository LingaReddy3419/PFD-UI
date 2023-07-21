import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reactor from './reactor';
import ReactorDetail from './reactor-detail';
import ReactorUpdate from './reactor-update';
import ReactorDeleteDialog from './reactor-delete-dialog';

const ReactorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reactor />} />
    <Route path="new" element={<ReactorUpdate />} />
    <Route path=":id">
      <Route index element={<ReactorDetail />} />
      <Route path="edit" element={<ReactorUpdate />} />
      <Route path="delete" element={<ReactorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReactorRoutes;
