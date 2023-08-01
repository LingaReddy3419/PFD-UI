import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './action.reducer';
import '../TableStyles.css';

export const ActionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const actionEntity = useAppSelector(state => state.action.entity);
  return (
    <div className="table-view-container">
      <Row>
        <Col md="12">
          <h4 data-cy="actionDetailsHeading" className="table-view-edit-heading">
            <Translate contentKey="pfdTest2App.action.detail.title">Action</Translate>
          </h4>
          <div className="table-edit-form-container">
            <dl className="jh-entity-details">
              <dt>
                <span id="id">
                  <Translate contentKey="global.field.id">ID</Translate>
                </span>
              </dt>
              <dd>{actionEntity.id}</dd>
              <dt>
                <span id="title">
                  <Translate contentKey="pfdTest2App.action.title">Title</Translate>
                </span>
              </dt>
              <dd>{actionEntity.title}</dd>
              <dt>
                <span id="description">
                  <Translate contentKey="pfdTest2App.action.description">Description</Translate>
                </span>
              </dt>
              <dd>{actionEntity.description}</dd>
              <dt>
                <span id="param1">
                  <Translate contentKey="pfdTest2App.action.param1">Param 1</Translate>
                </span>
              </dt>
              <dd>{actionEntity.param1}</dd>
              <dt>
                <span id="param2">
                  <Translate contentKey="pfdTest2App.action.param2">Param 2</Translate>
                </span>
              </dt>
              <dd>{actionEntity.param2}</dd>
              <dt>
                <span id="param3">
                  <Translate contentKey="pfdTest2App.action.param3">Param 3</Translate>
                </span>
              </dt>
              <dd>{actionEntity.param3}</dd>
            </dl>
            <div className="table-buttons-container">
              <Button className="border-radius" tag={Link} to="/action" replace color="secondary" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button className="border-radius" tag={Link} to={`/action/${actionEntity.id}/edit`} replace color="warning">
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

export default ActionDetail;
