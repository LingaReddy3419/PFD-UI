import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './impeller-type.reducer';
import '../TableStyles.css';

export const ImpellerType = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(location, 'id'), location.search));

  const impellerTypeList = useAppSelector(state => state.impellerType.entities);
  const loading = useAppSelector(state => state.impellerType.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="impeller-type-heading" data-cy="ImpellerTypeHeading">
        <h3 className="table-main-heading">
          {' '}
          <Translate contentKey="pfdTest2App.impellerType.home.title">Impeller Types</Translate>
        </h3>
        <div className="d-flex justify-content-end">
          <Button className="me-2 border-radius" color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pfdTest2App.impellerType.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/impeller-type/new"
            className="btn btn-success jh-create-entity border-radius"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pfdTest2App.impellerType.home.createLabel">Create new Impeller Type</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {impellerTypeList && impellerTypeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand font-size" onClick={sort('id')}>
                  <Translate contentKey="pfdTest2App.impellerType.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand font-size" onClick={sort('title')}>
                  <Translate contentKey="pfdTest2App.impellerType.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand font-size" onClick={sort('description')}>
                  <Translate contentKey="pfdTest2App.impellerType.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {impellerTypeList.map((impellerType, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td className="font-size">
                    <Button tag={Link} to={`/impeller-type/${impellerType.id}`} color="link" size="sm">
                      {impellerType.id}
                    </Button>
                  </td>
                  <td className="font-size">{impellerType.title}</td>
                  <td className="font-size">{impellerType.description}</td>
                  <td className="text-end buttons-padding">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/impeller-type/${impellerType.id}`}
                        size="sm"
                        data-cy="entityDetailsButton"
                        className="table-icon green"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        {/* <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span> */}
                      </Button>
                      <Button
                        tag={Link}
                        to={`/impeller-type/${impellerType.id}/edit`}
                        size="sm"
                        data-cy="entityEditButton"
                        className="table-icon blue"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        {/* <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span> */}
                      </Button>
                      <Button
                        tag={Link}
                        to={`/impeller-type/${impellerType.id}/delete`}
                        size="sm"
                        data-cy="entityDeleteButton"
                        className="table-icon red"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        {/* <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span> */}
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="pfdTest2App.impellerType.home.notFound">No Impeller Types found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ImpellerType;
