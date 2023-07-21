import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MOC from './moc';
import MOCDetail from './moc-detail';
import MOCUpdate from './moc-update';
import MOCDeleteDialog from './moc-delete-dialog';

const MOCRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MOC />} />
    <Route path="new" element={<MOCUpdate />} />
    <Route path=":id">
      <Route index element={<MOCDetail />} />
      <Route path="edit" element={<MOCUpdate />} />
      <Route path="delete" element={<MOCDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MOCRoutes;
