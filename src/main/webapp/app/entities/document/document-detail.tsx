import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document.reducer';

export const DocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentEntity = useAppSelector(state => state.document.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentDetailsHeading">
          <Translate contentKey="pfdTest2App.document.detail.title">Document</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.document.title">Title</Translate>
            </span>
          </dt>
          <dd>{documentEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.document.description">Description</Translate>
            </span>
          </dt>
          <dd>{documentEntity.description}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="pfdTest2App.document.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{documentEntity.fileName}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="pfdTest2App.document.url">Url</Translate>
            </span>
          </dt>
          <dd>{documentEntity.url}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.document.reactor">Reactor</Translate>
          </dt>
          <dd>{documentEntity.reactor ? documentEntity.reactor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentDetail;
