import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mode-of-charging.reducer';

export const ModeOfChargingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const modeOfChargingEntity = useAppSelector(state => state.modeOfCharging.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="modeOfChargingDetailsHeading">
          <Translate contentKey="pfdTest2App.modeOfCharging.detail.title">ModeOfCharging</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{modeOfChargingEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.modeOfCharging.title">Title</Translate>
            </span>
          </dt>
          <dd>{modeOfChargingEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.modeOfCharging.description">Description</Translate>
            </span>
          </dt>
          <dd>{modeOfChargingEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/mode-of-charging" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mode-of-charging/${modeOfChargingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ModeOfChargingDetail;
