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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandResponse;

/**
 * @author Kristof Heirwegh
 */
public class GetVectorLayerConfigurationResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private VectorLayerConfiguration vectorLayerConfiguration;

	public VectorLayerConfiguration getVectorLayerConfiguration() {
		return vectorLayerConfiguration;
	}

	public void setVectorLayerConfiguration(VectorLayerConfiguration vectorLayerConfiguration) {
		this.vectorLayerConfiguration = vectorLayerConfiguration;
	}
}
