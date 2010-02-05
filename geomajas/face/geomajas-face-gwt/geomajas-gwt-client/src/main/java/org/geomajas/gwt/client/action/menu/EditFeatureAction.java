/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.action.menu;

import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.controller.editing.LineStringEditController;
import org.geomajas.gwt.client.controller.editing.MultiLineStringEditController;
import org.geomajas.gwt.client.controller.editing.MultiPolygonEditController;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.controller.editing.PointEditController;
import org.geomajas.gwt.client.controller.editing.PolygonEditController;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.i18n.MenuMessages;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.LayerType;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * Menu action that starts editing a selected feature on the map.
 *
 * @author Pieter De Graef
 */
public class EditFeatureAction extends MenuAction implements MenuItemIfFunction {

	private MapWidget mapWidget;

	private ParentEditController controller;

	private Feature feature;

	/**
	 * Constructor for the menu action to edit a selected feature on the map.
	 *
	 * @param mapWidget
	 *            The <code>MapWidget</code> on which editing is in progress.
	 * @param controller
	 *            The current parent editing controller active on the map.
	 */
	public EditFeatureAction(MapWidget mapWidget, ParentEditController controller) {
		super(((MenuMessages) GWT.create(MenuMessages.class)).editFeature(), "[ISOMORPHIC]/geomajas/vector-edit.png");
		this.mapWidget = mapWidget;
		this.controller = controller;
		setEnableIfCondition(this);
	}

	/**
	 * Activate editing, and set the correct child editing controller on the parent editing controller.
	 */
	public void onClick(MenuItemClickEvent event) {
		if (feature != null && feature.isSelected()) {
			FeatureTransaction ft = mapWidget.getMapModel().getFeatureEditor().startEditing(
					new Feature[] {feature.clone()}, new Feature[] {feature.clone()});
			mapWidget.render(ft, "all");
			VectorLayer vLayer = feature.getLayer();
			if (vLayer.getLayerInfo().getLayerType() == LayerType.POINT) {
				controller.setController(new PointEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.LINESTRING) {
				controller.setController(new LineStringEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.MULTILINESTRING) {
				controller.setController(new MultiLineStringEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.POLYGON) {
				controller.setController(new PolygonEditController(mapWidget, controller));
			} else if (vLayer.getLayerInfo().getLayerType() == LayerType.MULTIPOLYGON) {
				controller.setController(new MultiPolygonEditController(mapWidget, controller));
			}
			controller.setEditMode(EditMode.DRAG_MODE);
		}
	}

	/**
	 * Implementation of the <code>MenuItemIfFunction</code> interface. This will determine if the menu action should be
	 * enabled or not. In essence, this action will be enabled if a vector-layer is selected that allows the updating of
	 * existing features.
	 */
	public boolean execute(Canvas target, Menu menu, MenuItem item) {
		String selectionId = mapWidget.getGraphics().getRightButtonTarget();
		String featureId = Feature.getFeatureIdFromSelectionId(selectionId);
		if (featureId != null) {
			if (mapWidget.getMapModel().isFeatureSelected(featureId)) {
				feature = mapWidget.getMapModel().getFeatureById(featureId);
				return feature.isUpdatable();
			}
		}
		return false;
	}
}
