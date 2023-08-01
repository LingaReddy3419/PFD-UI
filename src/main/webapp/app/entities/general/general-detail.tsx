import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './general.reducer';
import '../TableStyles.css';
export const GeneralDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const generalEntity = useAppSelector(state => state.general.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="generalDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.general.detail.title">General</Translate>
          </h4>
          <div className="table-edit-form-container">
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
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/general" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/general/${generalEntity.id}/edit`} replace color="warning">
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

export default GeneralDetail;
