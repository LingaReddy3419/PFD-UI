import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGeneral } from 'app/shared/model/general.model';
import { getEntities as getGenerals } from 'app/entities/general/general.reducer';
import { IAction } from 'app/shared/model/action.model';
import { getEntity, updateEntity, createEntity, reset } from './action.reducer';

export const ActionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const generals = useAppSelector(state => state.general.entities);
  const actionEntity = useAppSelector(state => state.action.entity);
  const loading = useAppSelector(state => state.action.loading);
  const updating = useAppSelector(state => state.action.updating);
  const updateSuccess = useAppSelector(state => state.action.updateSuccess);

  const handleClose = () => {
    navigate('/action');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGenerals({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...actionEntity,
      ...values,
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
          ...actionEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pfdTest2App.action.home.createOrEditLabel" data-cy="ActionCreateUpdateHeading">
            <Translate contentKey="pfdTest2App.action.home.createOrEditLabel">Create or edit a Action</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="action-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('pfdTest2App.action.title')} id="action-title" name="title" data-cy="title" type="text" />
              <ValidatedField
                label={translate('pfdTest2App.action.description')}
                id="action-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.action.param1')}
                id="action-param1"
                name="param1"
                data-cy="param1"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.action.param2')}
                id="action-param2"
                name="param2"
                data-cy="param2"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.action.param3')}
                id="action-param3"
                name="param3"
                data-cy="param3"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/action" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ActionUpdate;
