import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Operations from './operations';
import OperationsDetail from './operations-detail';
import OperationsUpdate from './operations-update';
import OperationsDeleteDialog from './operations-delete-dialog';

const OperationsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Operations />} />
    <Route path="new" element={<OperationsUpdate />} />
    <Route path=":id">
      <Route index element={<OperationsDetail />} />
      <Route path="edit" element={<OperationsUpdate />} />
      <Route path="delete" element={<OperationsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OperationsRoutes;
