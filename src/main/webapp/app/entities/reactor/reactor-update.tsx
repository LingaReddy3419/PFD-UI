import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnit } from 'app/shared/model/unit.model';
import { getEntities as getUnits } from 'app/entities/unit/unit.reducer';
import { IBlock } from 'app/shared/model/block.model';
import { getEntities as getBlocks } from 'app/entities/block/block.reducer';
import { IMOC } from 'app/shared/model/moc.model';
import { getEntities as getMOcs } from 'app/entities/moc/moc.reducer';
import { IImpellerType } from 'app/shared/model/impeller-type.model';
import { getEntities as getImpellerTypes } from 'app/entities/impeller-type/impeller-type.reducer';
import { IReactor } from 'app/shared/model/reactor.model';
import { getEntity, updateEntity, createEntity, reset } from './reactor.reducer';

export const ReactorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const units = useAppSelector(state => state.unit.entities);
  const blocks = useAppSelector(state => state.block.entities);
  const mOCS = useAppSelector(state => state.mOC.entities);
  const impellerTypes = useAppSelector(state => state.impellerType.entities);
  const reactorEntity = useAppSelector(state => state.reactor.entity);
  const loading = useAppSelector(state => state.reactor.loading);
  const updating = useAppSelector(state => state.reactor.updating);
  const updateSuccess = useAppSelector(state => state.reactor.updateSuccess);

  const handleClose = () => {
    navigate('/reactor' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUnits({}));
    dispatch(getBlocks({}));
    dispatch(getMOcs({}));
    dispatch(getImpellerTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...reactorEntity,
      ...values,
      unit: units.find(it => it.id.toString() === values.unit.toString()),
      block: blocks.find(it => it.id.toString() === values.block.toString()),
      moc: mOCS.find(it => it.id.toString() === values.moc.toString()),
      impellerType: impellerTypes.find(it => it.id.toString() === values.impellerType.toString()),
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
          ...reactorEntity,
          unit: reactorEntity?.unit?.id,
          block: reactorEntity?.block?.id,
          moc: reactorEntity?.moc?.id,
          impellerType: reactorEntity?.impellerType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pfdTest2App.reactor.home.createOrEditLabel" data-cy="ReactorCreateUpdateHeading">
            <Translate contentKey="pfdTest2App.reactor.home.createOrEditLabel">Create or edit a Reactor</Translate>
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
                  id="reactor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('pfdTest2App.reactor.workingVolume')}
                id="reactor-workingVolume"
                name="workingVolume"
                data-cy="workingVolume"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.reactor.vesselId')}
                id="reactor-vesselId"
                name="vesselId"
                data-cy="vesselId"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.reactor.bottomImpellerStirringVolume')}
                id="reactor-bottomImpellerStirringVolume"
                name="bottomImpellerStirringVolume"
                data-cy="bottomImpellerStirringVolume"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.reactor.minimumTempSensingVolume')}
                id="reactor-minimumTempSensingVolume"
                name="minimumTempSensingVolume"
                data-cy="minimumTempSensingVolume"
                type="text"
              />
              <ValidatedField id="reactor-unit" name="unit" data-cy="unit" label={translate('pfdTest2App.reactor.unit')} type="select">
                <option value="" key="0" />
                {units
                  ? units.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="reactor-block" name="block" data-cy="block" label={translate('pfdTest2App.reactor.block')} type="select">
                <option value="" key="0" />
                {blocks
                  ? blocks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="reactor-moc" name="moc" data-cy="moc" label={translate('pfdTest2App.reactor.moc')} type="select">
                <option value="" key="0" />
                {mOCS
                  ? mOCS.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reactor-impellerType"
                name="impellerType"
                data-cy="impellerType"
                label={translate('pfdTest2App.reactor.impellerType')}
                type="select"
              >
                <option value="" key="0" />
                {impellerTypes
                  ? impellerTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reactor" replace color="info">
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

export default ReactorUpdate;
