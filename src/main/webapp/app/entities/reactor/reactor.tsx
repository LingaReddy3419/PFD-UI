import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './reactor.reducer';
import '../TableStyles.css';

export const Reactor = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const reactorList = useAppSelector(state => state.reactor.entities);
  const loading = useAppSelector(state => state.reactor.loading);
  const totalItems = useAppSelector(state => state.reactor.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h4 id="reactor-heading" data-cy="ReactorHeading">
        <h3 className="table-main-heading">
          {' '}
          <Translate contentKey="pfdTest2App.reactor.home.title">Reactors</Translate>
        </h3>
        <div className="d-flex justify-content-end">
          <Button className="me-2 border-radius" color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="pfdTest2App.reactor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/reactor/new"
            className="btn btn-success jh-create-entity  border-radius"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="pfdTest2App.reactor.home.createLabel">Create new Reactor</Translate>
          </Link>
        </div>
      </h4>
      <div className="table-responsive">
        {reactorList && reactorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand font-size" onClick={sort('id')}>
                  <Translate contentKey="pfdTest2App.reactor.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand font-size" onClick={sort('workingVolume')}>
                  <Translate contentKey="pfdTest2App.reactor.workingVolume">Working Volume</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('workingVolume')} />
                </th>
                <th className="hand font-size" onClick={sort('vesselId')}>
                  <Translate contentKey="pfdTest2App.reactor.vesselId">Vessel Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vesselId')} />
                </th>
                <th className="hand font-size" onClick={sort('bottomImpellerStirringVolume')}>
                  <Translate contentKey="pfdTest2App.reactor.bottomImpellerStirringVolume">Bottom Impeller Stirring Volume</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bottomImpellerStirringVolume')} />
                </th>
                <th className="hand font-size" onClick={sort('minimumTempSensingVolume')}>
                  <Translate contentKey="pfdTest2App.reactor.minimumTempSensingVolume">Minimum Temp Sensing Volume</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minimumTempSensingVolume')} />
                </th>
                <th className="font-size">
                  <Translate contentKey="pfdTest2App.reactor.unit">Unit</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="font-size">
                  <Translate contentKey="pfdTest2App.reactor.block">Block</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="font-size">
                  <Translate contentKey="pfdTest2App.reactor.moc">Moc</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="font-size">
                  <Translate contentKey="pfdTest2App.reactor.impellerType">Impeller Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reactorList.map((reactor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td className="font-size">
                    <Button tag={Link} to={`/reactor/${reactor.id}`} color="link" size="sm">
                      {reactor.id}
                    </Button>
                  </td>
                  <td className="font-size">{reactor.workingVolume}</td>
                  <td className="font-size">{reactor.vesselId}</td>
                  <td className="font-size">{reactor.bottomImpellerStirringVolume}</td>
                  <td className="font-size">{reactor.minimumTempSensingVolume}</td>
                  <td className="font-size">{reactor.unit ? <Link to={`/unit/${reactor.unit.id}`}>{reactor.unit.id}</Link> : ''}</td>
                  <td className="font-size">{reactor.block ? <Link to={`/block/${reactor.block.id}`}>{reactor.block.id}</Link> : ''}</td>
                  <td className="font-size">{reactor.moc ? <Link to={`/moc/${reactor.moc.id}`}>{reactor.moc.id}</Link> : ''}</td>
                  <td className="font-size">
                    {reactor.impellerType ? <Link to={`/impeller-type/${reactor.impellerType.id}`}>{reactor.impellerType.id}</Link> : ''}
                  </td>
                  <td className="text-end buttons-padding">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/reactor/${reactor.id}`} size="sm" data-cy="entityDetailsButton" className="table-icon green">
                        <FontAwesomeIcon icon="eye" />{' '}
                        {/* <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span> */}
                      </Button>
                      <Button
                        tag={Link}
                        to={`/reactor/${reactor.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/reactor/${reactor.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="pfdTest2App.reactor.home.notFound">No Reactors found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={reactorList && reactorList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-end d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-end d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Reactor;
