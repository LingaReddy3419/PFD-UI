import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReactor } from 'app/shared/model/reactor.model';
import { getEntities as getReactors } from 'app/entities/reactor/reactor.reducer';
import { IImpellerType } from 'app/shared/model/impeller-type.model';
import { getEntity, updateEntity, createEntity, reset } from './impeller-type.reducer';
import '../TableStyles.css';

export const ImpellerTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reactors = useAppSelector(state => state.reactor.entities);
  const impellerTypeEntity = useAppSelector(state => state.impellerType.entity);
  const loading = useAppSelector(state => state.impellerType.loading);
  const updating = useAppSelector(state => state.impellerType.updating);
  const updateSuccess = useAppSelector(state => state.impellerType.updateSuccess);

  const handleClose = () => {
    navigate('/impeller-type');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReactors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...impellerTypeEntity,
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
          ...impellerTypeEntity,
        };

  return (
    <div className="table-edit-container">
      <Row className="justify-content-center">
        <Col md="12">
          <h4
            id="pfdTest2App.impellerType.home.createOrEditLabel"
            data-cy="ImpellerTypeCreateUpdateHeading"
            className="table-view-edit-heading"
          >
            Create or Edit a ImpellerType
          </h4>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="12">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <div className="table-edit-form-container">
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                {!isNew ? (
                  <ValidatedField
                    name="id"
                    required
                    readOnly
                    id="impeller-type-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                    style={{ borderRadius: '6px', border: '1px solid #000000', cursor: 'not-allowed' }}
                    disabled
                  />
                ) : null}
                <ValidatedField
                  label={translate('pfdTest2App.impellerType.title')}
                  id="impeller-type-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  label={translate('pfdTest2App.impellerType.description')}
                  id="impeller-type-description"
                  name="description"
                  data-cy="description"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <div className="table-buttons-container">
                  <Button
                    className="border-radius"
                    tag={Link}
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    to="/impeller-type"
                    replace
                    color="secondary"
                  >
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </span>
                  </Button>
                  &nbsp;
                  <Button
                    className="border-radius"
                    color="success"
                    id="save-entity"
                    data-cy="entityCreateSaveButton"
                    type="submit"
                    disabled={updating}
                  >
                    <FontAwesomeIcon icon="save" />
                    &nbsp;
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </Button>
                </div>
              </ValidatedForm>
            </div>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ImpellerTypeUpdate;
