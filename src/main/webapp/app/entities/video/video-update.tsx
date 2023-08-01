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
import { IVideo } from 'app/shared/model/video.model';
import { getEntity, updateEntity, createEntity, reset } from './video.reducer';
import '../TableStyles.css';
export const VideoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reactors = useAppSelector(state => state.reactor.entities);
  const videoEntity = useAppSelector(state => state.video.entity);
  const loading = useAppSelector(state => state.video.loading);
  const updating = useAppSelector(state => state.video.updating);
  const updateSuccess = useAppSelector(state => state.video.updateSuccess);

  const handleClose = () => {
    navigate('/video');
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
      ...videoEntity,
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
          ...videoEntity,
          reactor: videoEntity?.reactor?.id,
        };

  return (
    <div className="table-edit-container">
      <Row className="justify-content-center">
        <Col md="12">
          <h4 className="table-view-edit-heading" id="pfdTest2App.video.home.createOrEditLabel" data-cy="VideoCreateUpdateHeading">
            Create or Edit a Video
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
                    id="video-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                    style={{ borderRadius: '6px', border: '1px solid #000000', cursor: 'not-allowed' }}
                    disabled
                  />
                ) : null}
                <ValidatedField
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                  label={translate('pfdTest2App.video.title')}
                  id="video-title"
                  name="title"
                  data-cy="title"
                  type="text"
                />
                <ValidatedField
                  label={translate('pfdTest2App.video.description')}
                  id="video-description"
                  name="description"
                  data-cy="description"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  label={translate('pfdTest2App.video.fileName')}
                  id="video-fileName"
                  name="fileName"
                  data-cy="fileName"
                  type="text"
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                />
                <ValidatedField
                  style={{ borderRadius: '6px', border: '1px solid #000000' }}
                  label={translate('pfdTest2App.video.url')}
                  id="video-url"
                  name="url"
                  data-cy="url"
                  type="text"
                />
                <ValidatedField
                  id="video-reactor"
                  name="reactor"
                  data-cy="reactor"
                  label={translate('pfdTest2App.video.reactor')}
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
                    to="/video"
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

export default VideoUpdate;
