/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.blueprints.Blueprints;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.Datalayers;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.loketten.Geodesks;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * 
 * @author Oliver May
 *
 */
public class ManagerLayout extends VLayout implements EditSessionHandler {

	private TabSet tabSet;

	public ManagerLayout() {
		super();
		setWidth100();
		setHeight100();

		// ---- main section ----
		tabSet = new TabSet();
		tabSet.setWidth100();
		tabSet.setHeight100();

		//FIXME: i18n
		Tab loketten = new Tab("Geodesks");
		loketten.setPane(new Geodesks());
		tabSet.addTab(loketten);

		//FIXME: i18n
		Tab lagenBeheerTab = new Tab("Datalagen");
		lagenBeheerTab.setPane(new Datalayers());
		tabSet.addTab(lagenBeheerTab);

		//FIXME: i18n
		if (Role.ADMINISTRATOR.equals(ManagerApplicationEntryPoint.getInstance().getUserProfile()
				.getRole())) {
			Tab blueprintTab = new Tab("Blauwdrukken");
			blueprintTab.setPane(new Blueprints());
			tabSet.addTab(blueprintTab);
		}

		addMember(tabSet);
		
		NotificationWindow.getInstance().setWidth(300);
		NotificationWindow.getInstance().init(this);

		Whiteboard.registerHandler(this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}


	// -- EditSessionHandler--------------------------------------------------------

	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		for (Tab tab : tabSet.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
				tab.setDisabled(disabled);
			}
		}
	}

}