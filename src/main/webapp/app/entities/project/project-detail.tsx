import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './project.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const projectEntity = useAppSelector(state => state.project.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectDetailsHeading">Project</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{projectEntity.id}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{projectEntity.address}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{projectEntity.city}</dd>
          <dt>
            <span id="rooms">Rooms</span>
          </dt>
          <dd>{projectEntity.rooms}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{projectEntity.price}</dd>
          <dt>
            <span id="floorSpace">Floor Space</span>
          </dt>
          <dd>{projectEntity.floorSpace}</dd>
          <dt>
            <span id="attachment">Attachment</span>
          </dt>
          <dd>{projectEntity.attachment}</dd>
          <dt>Extra</dt>
          <dd>
            {projectEntity.extras
              ? projectEntity.extras.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.extraName}</a>
                    {projectEntity.extras && i === projectEntity.extras.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Home Type</dt>
          <dd>{projectEntity.homeType ? projectEntity.homeType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectDetail;
