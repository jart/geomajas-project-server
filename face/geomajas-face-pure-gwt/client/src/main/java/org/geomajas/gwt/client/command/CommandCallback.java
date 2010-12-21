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
package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;

/**
 * Execution function that can be passed on to the CommandDispatcher to be executed when a command successfully returns.
 *
 * @since 1.6.0
 * @author Pieter De Graef
 */
@Api(allMethods = true)
public interface CommandCallback {

	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 *
	 * @param response
	 */
	void onSuccess(CommandResponse response);
	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 *
	 * @param response
	 */
	void onFailure(CommandResponse response);
	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 *
	 * @param response
	 */
	void onError(Throwable error);
}
