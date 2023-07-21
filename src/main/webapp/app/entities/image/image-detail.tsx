import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './image.reducer';

export const ImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imageEntity = useAppSelector(state => state.image.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageDetailsHeading">
          <Translate contentKey="pfdTest2App.image.detail.title">Image</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imageEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.image.title">Title</Translate>
            </span>
          </dt>
          <dd>{imageEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.image.description">Description</Translate>
            </span>
          </dt>
          <dd>{imageEntity.description}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="pfdTest2App.image.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{imageEntity.fileName}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="pfdTest2App.image.url">Url</Translate>
            </span>
          </dt>
          <dd>{imageEntity.url}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.image.reactor">Reactor</Translate>
          </dt>
          <dd>{imageEntity.reactor ? imageEntity.reactor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image/${imageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageDetail;
