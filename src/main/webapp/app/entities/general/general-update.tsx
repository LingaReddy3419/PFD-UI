import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOperations } from 'app/shared/model/operations.model';
import { getEntities as getOperations } from 'app/entities/operations/operations.reducer';
import { IAction } from 'app/shared/model/action.model';
import { getEntities as getActions } from 'app/entities/action/action.reducer';
import { IModeOfCharging } from 'app/shared/model/mode-of-charging.model';
import { getEntities as getModeOfChargings } from 'app/entities/mode-of-charging/mode-of-charging.reducer';
import { IGeneral } from 'app/shared/model/general.model';
import { getEntity, updateEntity, createEntity, reset } from './general.reducer';

export const GeneralUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const operations = useAppSelector(state => state.operations.entities);
  const actions = useAppSelector(state => state.action.entities);
  const modeOfChargings = useAppSelector(state => state.modeOfCharging.entities);
  const generalEntity = useAppSelector(state => state.general.entity);
  const loading = useAppSelector(state => state.general.loading);
  const updating = useAppSelector(state => state.general.updating);
  const updateSuccess = useAppSelector(state => state.general.updateSuccess);

  const handleClose = () => {
    navigate('/general');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOperations({}));
    dispatch(getActions({}));
    dispatch(getModeOfChargings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...generalEntity,
      ...values,
      operations: operations.find(it => it.id.toString() === values.operations.toString()),
      action: actions.find(it => it.id.toString() === values.action.toString()),
      modeOfCharging: modeOfChargings.find(it => it.id.toString() === values.modeOfCharging.toString()),
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
          ...generalEntity,
          operations: generalEntity?.operations?.id,
          action: generalEntity?.action?.id,
          modeOfCharging: generalEntity?.modeOfCharging?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pfdTest2App.general.home.createOrEditLabel" data-cy="GeneralCreateUpdateHeading">
            <Translate contentKey="pfdTest2App.general.home.createOrEditLabel">Create or edit a General</Translate>
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
                  id="general-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="general-operations"
                name="operations"
                data-cy="operations"
                label={translate('pfdTest2App.general.operations')}
                type="select"
              >
                <option value="" key="0" />
                {operations
                  ? operations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="general-action"
                name="action"
                data-cy="action"
                label={translate('pfdTest2App.general.action')}
                type="select"
              >
                <option value="" key="0" />
                {actions
                  ? actions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="general-modeOfCharging"
                name="modeOfCharging"
                data-cy="modeOfCharging"
                label={translate('pfdTest2App.general.modeOfCharging')}
                type="select"
              >
                <option value="" key="0" />
                {modeOfChargings
                  ? modeOfChargings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/general" replace color="info">
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

export default GeneralUpdate;
