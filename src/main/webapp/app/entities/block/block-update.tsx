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
import { IBlock } from 'app/shared/model/block.model';
import { getEntity, updateEntity, createEntity, reset } from './block.reducer';
import '../TableStyles.css';

export const BlockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reactors = useAppSelector(state => state.reactor.entities);
  const blockEntity = useAppSelector(state => state.block.entity);
  const loading = useAppSelector(state => state.block.loading);
  const updating = useAppSelector(state => state.block.updating);
  const updateSuccess = useAppSelector(state => state.block.updateSuccess);

  const handleClose = () => {
    navigate('/block');
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
      ...blockEntity,
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
          ...blockEntity,
        };

  return (
    <div className="table-edit-container">
      <Row className="justify-content-center">
        <Col md="12">
          <h4 className="table-view-edit-heading" id="pfdTest2App.block.home.createOrEditLabel" data-cy="BlockCreateUpdateHeading">
            Create or Edit a Block
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
                    label="ID"
                    name="id"
                    required
                    readOnly
                    id="block-id"
                    validate={{ required: true }}
                    style={{ borderRadius: '6px', border: '1px solid #000000', marginBottom: '10px', cursor: 'not-allowed' }}
                    disabled
                  />
                ) : null}
                <ValidatedField
                  label="Title"
                  id="block-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000', marginBottom: '10px' }}
                />
                <ValidatedField
                  id="block-description"
                  name="description"
                  label="Description"
                  data-cy="description"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000', marginBottom: '10px' }}
                />
                <div className="table-buttons-container">
                  <Button
                    className="border-radius"
                    color="secondary"
                    tag={Link}
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    to="/block"
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

export default BlockUpdate;
