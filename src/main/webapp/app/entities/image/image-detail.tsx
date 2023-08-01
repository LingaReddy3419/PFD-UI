import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './image.reducer';
import '../TableStyles.css';
export const ImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imageEntity = useAppSelector(state => state.image.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="imageDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.image.detail.title">Image</Translate>
          </h4>
          <div className="table-edit-form-container">
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
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/image" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/image/${imageEntity.id}/edit`} replace color="warning">
                <FontAwesomeIcon icon="pencil-alt" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.edit">Edit</Translate>
                </span>
              </Button>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default ImageDetail;
