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
import { IOperations } from 'app/shared/model/operations.model';
import { getEntity, updateEntity, createEntity, reset } from './operations.reducer';
import '../TableStyles.css';

export const OperationsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const generals = useAppSelector(state => state.general.entities);
  const operationsEntity = useAppSelector(state => state.operations.entity);
  const loading = useAppSelector(state => state.operations.loading);
  const updating = useAppSelector(state => state.operations.updating);
  const updateSuccess = useAppSelector(state => state.operations.updateSuccess);

  const handleClose = () => {
    navigate('/operations');
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
      ...operationsEntity,
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
          ...operationsEntity,
        };

  return (
    <div className="table-edit-container">
      <Row className="justify-content-center">
        <Col md="12">
          <h4
            className="table-view-edit-heading"
            id="pfdTest2App.operations.home.createOrEditLabel"
            data-cy="OperationsCreateUpdateHeading"
          >
            Create or Edit a Operations
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
                    id="operations-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                    style={{ borderRadius: '6px', border: '1px solid #000000', cursor: 'not-allowed' }}
                    disabled
                  />
                ) : null}
                <ValidatedField
                  label={translate('pfdTest2App.operations.title')}
                  id="operations-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  label={translate('pfdTest2App.operations.description')}
                  id="operations-description"
                  name="description"
                  data-cy="description"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <div className="table-buttons-container">
                  <Button
                    className="border-radius"
                    color="secondary"
                    tag={Link}
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    to="/operations"
                    replace
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

export default OperationsUpdate;
