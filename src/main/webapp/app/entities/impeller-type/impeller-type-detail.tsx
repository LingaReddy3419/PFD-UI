import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './impeller-type.reducer';

export const ImpellerTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const impellerTypeEntity = useAppSelector(state => state.impellerType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="impellerTypeDetailsHeading">
          <Translate contentKey="pfdTest2App.impellerType.detail.title">ImpellerType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{impellerTypeEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.impellerType.title">Title</Translate>
            </span>
          </dt>
          <dd>{impellerTypeEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.impellerType.description">Description</Translate>
            </span>
          </dt>
          <dd>{impellerTypeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/impeller-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/impeller-type/${impellerTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImpellerTypeDetail;
