import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reactor.reducer';
import '../TableStyles.css';
export const ReactorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reactorEntity = useAppSelector(state => state.reactor.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="reactorDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.reactor.detail.title">Reactor</Translate>
          </h4>
          <div className="table-edit-form-container">
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
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/reactor" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/reactor/${reactorEntity.id}/edit`} replace color="warning">
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

export default ReactorDetail;
