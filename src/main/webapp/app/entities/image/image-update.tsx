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
import { IImage } from 'app/shared/model/image.model';
import { getEntity, updateEntity, createEntity, reset } from './image.reducer';

export const ImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reactors = useAppSelector(state => state.reactor.entities);
  const imageEntity = useAppSelector(state => state.image.entity);
  const loading = useAppSelector(state => state.image.loading);
  const updating = useAppSelector(state => state.image.updating);
  const updateSuccess = useAppSelector(state => state.image.updateSuccess);

  const handleClose = () => {
    navigate('/image');
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
      ...imageEntity,
      ...values,
      reactor: reactors.find(it => it.id.toString() === values.reactor.toString()),
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
          ...imageEntity,
          reactor: imageEntity?.reactor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pfdTest2App.image.home.createOrEditLabel" data-cy="ImageCreateUpdateHeading">
            <Translate contentKey="pfdTest2App.image.home.createOrEditLabel">Create or edit a Image</Translate>
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
                  id="image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('pfdTest2App.image.title')} id="image-title" name="title" data-cy="title" type="text" />
              <ValidatedField
                label={translate('pfdTest2App.image.description')}
                id="image-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('pfdTest2App.image.fileName')}
                id="image-fileName"
                name="fileName"
                data-cy="fileName"
                type="text"
              />
              <ValidatedField label={translate('pfdTest2App.image.url')} id="image-url" name="url" data-cy="url" type="text" />
              <ValidatedField
                id="image-reactor"
                name="reactor"
                data-cy="reactor"
                label={translate('pfdTest2App.image.reactor')}
                type="select"
              >
                <option value="" key="0" />
                {reactors
                  ? reactors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/image" replace color="info">
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

export default ImageUpdate;
