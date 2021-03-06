/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * Response object for {@link org.geomajas.command.geometry.GeometrySplitCommand}.
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometrySplitResponse extends CommandResponse {

	private static final long serialVersionUID = 1110L;

	private List<Geometry> geometries;

	/**
	 * Get geoemtries.
	 * 
	 * @return geometries
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set geometries.
	 * 
	 * @param geometries
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Get the string representation of this response.
	 * 
	 * @return string representation of this response
	 */
	@Override
	public String toString() {
		return "GeometrySplitResponse{" + "geometries=" + geometries + '}';
	}
}