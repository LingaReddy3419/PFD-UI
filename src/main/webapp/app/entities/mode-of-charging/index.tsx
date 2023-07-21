import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ModeOfCharging from './mode-of-charging';
import ModeOfChargingDetail from './mode-of-charging-detail';
import ModeOfChargingUpdate from './mode-of-charging-update';
import ModeOfChargingDeleteDialog from './mode-of-charging-delete-dialog';

const ModeOfChargingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModeOfCharging />} />
    <Route path="new" element={<ModeOfChargingUpdate />} />
    <Route path=":id">
      <Route index element={<ModeOfChargingDetail />} />
      <Route path="edit" element={<ModeOfChargingUpdate />} />
      <Route path="delete" element={<ModeOfChargingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ModeOfChargingRoutes;
