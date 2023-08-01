import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo" style={{ display: 'flex', alignItems: 'center' }}>
    <img
      src="https://www.sailife.com/wp-content/uploads/2022/07/sai-social-image-min.jpg"
      alt="menu-logo"
      style={{ height: '40px', width: '100%', marginRight: '10px' }}
    />
    <span className="brand-title">PFD</span>
    {/* <span className="navbar-version">{VERSION}</span> */}
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center" style={{ color: '#ffffff' }}>
      <FontAwesomeIcon icon="home" />
      <span>PFD</span>
    </NavLink>
  </NavItem>
);
