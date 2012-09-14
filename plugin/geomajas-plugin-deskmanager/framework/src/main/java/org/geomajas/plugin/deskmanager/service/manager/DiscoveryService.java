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
package org.geomajas.plugin.deskmanager.service.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;

/**
 * @author Kristof Heirwegh
 */
public interface DiscoveryService {

	/**
	 * This will return a serverside Layer object (Geotools/WMS layer).
	 */
	Layer<?> createLayer(LayerConfiguration lc) throws Exception;

	VectorLayer createVectorLayer(VectorLayerConfiguration configuration) throws Exception;

	Map<String, Object> createBeanLayerDefinitionParameters(LayerConfiguration lc) throws Exception;

	List<VectorCapabilitiesInfo> getVectorCapabilities(Map<String, String> connectionProperties) throws IOException;

	VectorLayerConfiguration getVectorLayerConfiguration(Map<String, String> connectionProperties, String layerName)
			throws IOException;

	List<RasterCapabilitiesInfo> getRasterCapabilities(Map<String, String> connectionProperties) throws Exception;

	RasterLayerConfiguration getRasterLayerConfiguration(Map<String, String> connectionProperties,
		RasterCapabilitiesInfo rasterCapabilitiesInfo)
			throws IOException;
}
