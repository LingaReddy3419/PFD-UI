import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ImpellerType from './impeller-type';
import ImpellerTypeDetail from './impeller-type-detail';
import ImpellerTypeUpdate from './impeller-type-update';
import ImpellerTypeDeleteDialog from './impeller-type-delete-dialog';

const ImpellerTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ImpellerType />} />
    <Route path="new" element={<ImpellerTypeUpdate />} />
    <Route path=":id">
      <Route index element={<ImpellerTypeDetail />} />
      <Route path="edit" element={<ImpellerTypeUpdate />} />
      <Route path="delete" element={<ImpellerTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ImpellerTypeRoutes;
