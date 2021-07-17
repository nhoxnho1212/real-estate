import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Extra from './extra';
import ExtraDetail from './extra-detail';
import ExtraUpdate from './extra-update';
import ExtraDeleteDialog from './extra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExtraDetail} />
      <ErrorBoundaryRoute path={match.url} component={Extra} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExtraDeleteDialog} />
  </>
);

export default Routes;
