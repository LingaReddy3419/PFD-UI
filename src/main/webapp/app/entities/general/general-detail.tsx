import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './general.reducer';

export const GeneralDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const generalEntity = useAppSelector(state => state.general.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="generalDetailsHeading">
          <Translate contentKey="pfdTest2App.general.detail.title">General</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{generalEntity.id}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.general.operations">Operations</Translate>
          </dt>
          <dd>{generalEntity.operations ? generalEntity.operations.id : ''}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.general.action">Action</Translate>
          </dt>
          <dd>{generalEntity.action ? generalEntity.action.id : ''}</dd>
          <dt>
            <Translate contentKey="pfdTest2App.general.modeOfCharging">Mode Of Charging</Translate>
          </dt>
          <dd>{generalEntity.modeOfCharging ? generalEntity.modeOfCharging.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/general" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/general/${generalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GeneralDetail;
