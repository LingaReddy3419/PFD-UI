import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenuReactor = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/block">
        <Translate contentKey="global.menu.entities.block" />
      </MenuItem>

      <MenuItem icon="asterisk" to="/impeller-type">
        <Translate contentKey="global.menu.entities.impellerType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/moc">
        <Translate contentKey="global.menu.entities.moc" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reactor">
        <Translate contentKey="global.menu.entities.reactor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/unit">
        <Translate contentKey="global.menu.entities.unit" />
      </MenuItem>

      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenuReactor;
