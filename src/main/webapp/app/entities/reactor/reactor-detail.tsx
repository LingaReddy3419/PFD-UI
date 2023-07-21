import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reactor.reducer';

export const ReactorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reactorEntity = useAppSelector(state => state.reactor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reactorDetailsHeading">
          <Translate contentKey="pfdTest2App.reactor.detail.title">Reactor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reactorEntity.id}</dd>
          <dt>
            <span id="workingVolume">
              <Translate contentKey="pfdTest2App.reactor.workingVolume">Working Volume</Translate>
            </span>
          </dt>
          <dd>{reactorEntity.workingVolume}</dd>
          <dt>
            <span id="vesselId">
              <Translate contentKey="pfdTest2App.reactor.vesselId">Vessel Id</Translate>
            </span>
          </dt>
          <dd>{reactorEntity.vesselId}</dd>
          <dt>
            <span id="bottomImpellerStirringVolume">
              <Translate contentKey="pfdTest2App.reactor.bottomImpellerStirringVolume">Bottom Impeller Stirring Volume</Translate>
            </span>
          </dt>
          <dd>{reactorEntity.bottomImpellerStirringVolume}</dd>
          <dt>
            <span id="minimumTempSensingVolume">
              <Translate contentKey="pfdTest2App.reactor.minimumTempSensingVolume">Minimum Temp Sensing Volume</Translate>
            </span>
          </dt>
          <dd>{reactorEntity.minimumTempSensingVolume}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.reactor.unit">Unit</Translate>
          </dt>
          <dd>{reactorEntity.unit ? reactorEntity.unit.id : ''}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.reactor.block">Block</Translate>
          </dt>
          <dd>{reactorEntity.block ? reactorEntity.block.id : ''}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.reactor.moc">Moc</Translate>
          </dt>
          <dd>{reactorEntity.moc ? reactorEntity.moc.id : ''}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.reactor.impellerType">Impeller Type</Translate>
          </dt>
          <dd>{reactorEntity.impellerType ? reactorEntity.impellerType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reactor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reactor/${reactorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReactorDetail;
