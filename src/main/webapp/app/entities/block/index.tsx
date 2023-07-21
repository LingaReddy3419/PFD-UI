import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Block from './block';
import BlockDetail from './block-detail';
import BlockUpdate from './block-update';
import BlockDeleteDialog from './block-delete-dialog';

const BlockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Block />} />
    <Route path="new" element={<BlockUpdate />} />
    <Route path=":id">
      <Route index element={<BlockDetail />} />
      <Route path="edit" element={<BlockUpdate />} />
      <Route path="delete" element={<BlockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BlockRoutes;
