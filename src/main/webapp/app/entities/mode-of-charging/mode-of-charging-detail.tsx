import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mode-of-charging.reducer';
import '../TableStyles.css';

export const ModeOfChargingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const modeOfChargingEntity = useAppSelector(state => state.modeOfCharging.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="modeOfChargingDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.modeOfCharging.detail.title">ModeOfCharging</Translate>
          </h4>
          <div className="table-edit-form-container">
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
            <div className="table-buttons-container">
              <Button
                className="border-radius"
                tag={Link}
                to="/mode-of-charging"
                replace
                color="secondary"
                data-cy="entityDetailsBackButton"
              >
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/mode-of-charging/${modeOfChargingEntity.id}/edit`} replace color="warning">
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

export default ModeOfChargingDetail;
