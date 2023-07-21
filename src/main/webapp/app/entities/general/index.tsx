import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import General from './general';
import GeneralDetail from './general-detail';
import GeneralUpdate from './general-update';
import GeneralDeleteDialog from './general-delete-dialog';

const GeneralRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<General />} />
    <Route path="new" element={<GeneralUpdate />} />
    <Route path=":id">
      <Route index element={<GeneralDetail />} />
      <Route path="edit" element={<GeneralUpdate />} />
      <Route path="delete" element={<GeneralDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GeneralRoutes;
