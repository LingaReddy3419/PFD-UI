import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './block.reducer';

export const BlockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const blockEntity = useAppSelector(state => state.block.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blockDetailsHeading">
          <Translate contentKey="pfdTest2App.block.detail.title">Block</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blockEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="pfdTest2App.block.title">Title</Translate>
            </span>
          </dt>
          <dd>{blockEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="pfdTest2App.block.description">Description</Translate>
            </span>
          </dt>
          <dd>{blockEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/block/${blockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BlockDetail;
