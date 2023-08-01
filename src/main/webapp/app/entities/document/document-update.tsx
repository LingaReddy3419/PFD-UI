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
import { IDocument } from 'app/shared/model/document.model';
import { getEntity, updateEntity, createEntity, reset } from './document.reducer';
import '../TableStyles.css';

export const DocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reactors = useAppSelector(state => state.reactor.entities);
  const documentEntity = useAppSelector(state => state.document.entity);
  const loading = useAppSelector(state => state.document.loading);
  const updating = useAppSelector(state => state.document.updating);
  const updateSuccess = useAppSelector(state => state.document.updateSuccess);

  const handleClose = () => {
    navigate('/document');
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
      ...documentEntity,
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
          ...documentEntity,
          reactor: documentEntity?.reactor?.id,
        };

  return (
    <div className="table-edit-container">
      <Row className="justify-content-center">
        <Col md="12">
          <h4 className="table-view-edit-heading" id="pfdTest2App.document.home.createOrEditLabel" data-cy="DocumentCreateUpdateHeading">
            Create or Edit a Document
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
                    id="document-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                    style={{ borderRadius: '6px', border: '1px solid #000000', cursor: 'not-allowed' }}
                    disabled
                  />
                ) : null}
                <ValidatedField
                  label={translate('pfdTest2App.document.title')}
                  id="document-title"
                  name="title"
                  data-cy="title"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  label={translate('pfdTest2App.document.description')}
                  id="document-description"
                  name="description"
                  data-cy="description"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  label={translate('pfdTest2App.document.fileName')}
                  id="document-fileName"
                  name="fileName"
                  data-cy="fileName"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                  label={translate('pfdTest2App.document.url')}
                  id="document-url"
                  name="url"
                  data-cy="url"
                  type="text"
                />
                <ValidatedField
                  id="document-reactor"
                  name="reactor"
                  data-cy="reactor"
                  label={translate('pfdTest2App.document.reactor')}
                  type="select"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
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
                <div className="table-buttons-container">
                  <Button
                    className="border-radius"
                    color="secondary"
                    tag={Link}
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    to="/document"
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

export default DocumentUpdate;
