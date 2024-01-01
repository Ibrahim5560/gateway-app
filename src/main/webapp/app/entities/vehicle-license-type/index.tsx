import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleLicenseType from './vehicle-license-type';
import VehicleLicenseTypeDetail from './vehicle-license-type-detail';
import VehicleLicenseTypeUpdate from './vehicle-license-type-update';
import VehicleLicenseTypeDeleteDialog from './vehicle-license-type-delete-dialog';

const VehicleLicenseTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleLicenseType />} />
    <Route path="new" element={<VehicleLicenseTypeUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleLicenseTypeDetail />} />
      <Route path="edit" element={<VehicleLicenseTypeUpdate />} />
      <Route path="delete" element={<VehicleLicenseTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleLicenseTypeRoutes;
