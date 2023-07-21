import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/block">
        <Translate contentKey="global.menu.entities.block" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document">
        <Translate contentKey="global.menu.entities.document" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/image">
        <Translate contentKey="global.menu.entities.image" />
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
      <MenuItem icon="asterisk" to="/video">
        <Translate contentKey="global.menu.entities.video" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/action">
        <Translate contentKey="global.menu.entities.action" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/general">
        <Translate contentKey="global.menu.entities.general" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mode-of-charging">
        <Translate contentKey="global.menu.entities.modeOfCharging" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/operations">
        <Translate contentKey="global.menu.entities.operations" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
