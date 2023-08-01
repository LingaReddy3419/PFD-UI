import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './moc.reducer';
import '../TableStyles.css';

export const MOCDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mOCEntity = useAppSelector(state => state.mOC.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="mOCDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.mOC.detail.title">MOC</Translate>
          </h4>
          <div className="table-edit-form-container">
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
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/moc" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/moc/${mOCEntity.id}/edit`} replace color="warning">
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

export default MOCDetail;
