import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IExtra } from 'app/shared/model/extra.model';
import { getEntities as getExtras } from 'app/entities/extra/extra.reducer';
import { IHomeType } from 'app/shared/model/home-type.model';
import { getEntities as getHomeTypes } from 'app/entities/home-type/home-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './project.reducer';
import { IProject } from 'app/shared/model/project.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const extras = useAppSelector(state => state.extra.entities);
  const homeTypes = useAppSelector(state => state.homeType.entities);
  const projectEntity = useAppSelector(state => state.project.entity);
  const loading = useAppSelector(state => state.project.loading);
  const updating = useAppSelector(state => state.project.updating);
  const updateSuccess = useAppSelector(state => state.project.updateSuccess);

  const handleClose = () => {
    props.history.push('/project' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getExtras({}));
    dispatch(getHomeTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...projectEntity,
      ...values,
      extras: mapIdList(values.extras),
      homeType: homeTypes.find(it => it.id.toString() === values.homeTypeId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...projectEntity,
          extras: projectEntity?.extras?.map(e => e.id.toString()),
          homeTypeId: projectEntity?.homeType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="realEstateApp.project.home.createOrEditLabel" data-cy="ProjectCreateUpdateHeading">
            Create or edit a Project
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="project-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Address" id="project-address" name="address" data-cy="address" type="text" />
              <ValidatedField label="City" id="project-city" name="city" data-cy="city" type="text" />
              <ValidatedField label="Rooms" id="project-rooms" name="rooms" data-cy="rooms" type="text" />
              <ValidatedField label="Price" id="project-price" name="price" data-cy="price" type="text" />
              <ValidatedField label="Floor Space" id="project-floorSpace" name="floorSpace" data-cy="floorSpace" type="text" />
              <ValidatedField label="Attachment" id="project-attachment" name="attachment" data-cy="attachment" type="text" />
              <ValidatedField label="Extra" id="project-extra" data-cy="extra" type="select" multiple name="extras">
                <option value="" key="0" />
                {extras
                  ? extras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.extraName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="project-homeType" name="homeTypeId" data-cy="homeType" label="Home Type" type="select">
                <option value="" key="0" />
                {homeTypes
                  ? homeTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/project" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProjectUpdate;
