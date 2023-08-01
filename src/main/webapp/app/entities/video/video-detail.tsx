import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './video.reducer';
import '../TableStyles.css';
export const VideoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const videoEntity = useAppSelector(state => state.video.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="8">
          <h4 data-cy="videoDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.video.detail.title">Video</Translate>
          </h4>
          <div className="table-edit-form-container">
            <dl className="jh-entity-details">
              <dt>
                <span id="id">
                  <Translate contentKey="global.field.id">ID</Translate>
                </span>
              </dt>
              <dd>{videoEntity.id}</dd>
              <dt>
                <span id="title">
                  <Translate contentKey="pfdTest2App.video.title">Title</Translate>
                </span>
              </dt>
              <dd>{videoEntity.title}</dd>
              <dt>
                <span id="description">
                  <Translate contentKey="pfdTest2App.video.description">Description</Translate>
                </span>
              </dt>
              <dd>{videoEntity.description}</dd>
              <dt>
                <span id="fileName">
                  <Translate contentKey="pfdTest2App.video.fileName">File Name</Translate>
                </span>
              </dt>
              <dd>{videoEntity.fileName}</dd>
              <dt>
                <span id="url">
                  <Translate contentKey="pfdTest2App.video.url">Url</Translate>
                </span>
              </dt>
              <dd>{videoEntity.url}</dd>
              <dt>
                <Translate contentKey="pfdTest2App.video.reactor">Reactor</Translate>
              </dt>
              <dd>{videoEntity.reactor ? videoEntity.reactor.id : ''}</dd>
            </dl>
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/video" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/video/${videoEntity.id}/edit`} replace color="warning">
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

export default VideoDetail;
