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
import { IModeOfCharging } from 'app/shared/model/mode-of-charging.model';
import { getEntity, updateEntity, createEntity, reset } from './mode-of-charging.reducer';

export const ModeOfChargingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const generals = useAppSelector(state => state.general.entities);
  const modeOfChargingEntity = useAppSelector(state => state.modeOfCharging.entity);
  const loading = useAppSelector(state => state.modeOfCharging.loading);
  const updating = useAppSelector(state => state.modeOfCharging.updating);
  const updateSuccess = useAppSelector(state => state.modeOfCharging.updateSuccess);

  const handleClose = () => {
    navigate('/mode-of-charging');
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
      ...modeOfChargingEntity,
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
          ...modeOfChargingEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pfdTest2App.modeOfCharging.home.createOrEditLabel" data-cy="ModeOfChargingCreateUpdateHeading">
            <Translate contentKey="pfdTest2App.modeOfCharging.home.createOrEditLabel">Create or edit a ModeOfCharging</Translate>
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
                  id="mode-of-charging-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('pfdTest2App.modeOfCharging.title')}
                id="mode-of-charging-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.modeOfCharging.description')}
                id="mode-of-charging-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mode-of-charging" replace color="info">
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

export default ModeOfChargingUpdate;
