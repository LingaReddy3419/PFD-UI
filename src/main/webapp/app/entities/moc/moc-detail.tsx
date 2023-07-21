import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './moc.reducer';

export const MOCDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mOCEntity = useAppSelector(state => state.mOC.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mOCDetailsHeading">
          <Translate contentKey="pfdTest2App.mOC.detail.title">MOC</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mOCEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.mOC.title">Title</Translate>
            </span>
          </dt>
          <dd>{mOCEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.mOC.description">Description</Translate>
            </span>
          </dt>
          <dd>{mOCEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/moc" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/moc/${mOCEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MOCDetail;
