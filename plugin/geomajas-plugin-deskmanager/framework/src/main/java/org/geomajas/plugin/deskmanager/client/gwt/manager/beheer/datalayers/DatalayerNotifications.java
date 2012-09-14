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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.MailManagePanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.SaveButtonBar.WoaEventHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.CommService;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class DatalayerNotifications extends VLayout implements WoaEventHandler {

	private LayerModelDto lmd;

	private MailManagePanel mailManage;

	public DatalayerNotifications() {
		super(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		mailManage = new MailManagePanel();
		mailManage.setDisabled(true);
		mailManage.setWidth100();
		mailManage.setHeight100();

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle("Mail notificaties");
		group.addMember(mailManage);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(final LayerModelDto lmd) {
		this.lmd = lmd;
		if (lmd != null) {
			mailManage.setValues(lmd.getMailAddresses());
		} else {
			mailManage.setValues(null);
		}
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		mailManage.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (lmd != null) {
			if (mailManage.hasChanged()) {
				if (mailManage.hasErrors()) {
					SC.warn("De lijst bevat foutieve waarden. Gelieve deze op te lossen vooraleer op te slaan.");
					return false;
				} else {
					lmd.setMailAddresses(mailManage.getValues());
					CommService.saveLayerModel(lmd);
				}
			} else {
				NotificationWindow.showInfoMessage("Niets gewijzigd");
			}
		}
		mailManage.setDisabled(true);
		return true;
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		mailManage.setDisabled(true);
		return true;
	}
}
