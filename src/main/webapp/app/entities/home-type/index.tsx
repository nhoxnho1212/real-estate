import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HomeType from './home-type';
import HomeTypeDetail from './home-type-detail';
import HomeTypeUpdate from './home-type-update';
import HomeTypeDeleteDialog from './home-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HomeTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HomeTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HomeTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={HomeType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HomeTypeDeleteDialog} />
  </>
);

export default Routes;
