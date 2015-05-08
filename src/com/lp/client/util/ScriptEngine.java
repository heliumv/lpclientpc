/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of theLicense, or
 * (at your option) any later version.
 *
 * According to sec. 7 of the GNU Affero General Public License, version 3,
 * the terms of the AGPL are supplemented with the following terms:
 *
 * "HELIUM V" and "HELIUM 5" are registered trademarks of
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.util;

import java.util.List;

import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

public class ScriptEngine {
	private ScriptingContainer engine ;

	public ScriptEngine() {
	}

	protected ScriptingContainer getEngine() {
		if(engine == null) {
			engine = new ScriptingContainer() ;
//			engine.setHomeDirectory("c:/bin/jruby-1.7.11") ;
//			engine.setHomeDirectory("c:/bin/hvjruby") ;
		}

		System.out.println("Supported ruby version: " + engine.getSupportedRubyVersion()) ;
		List<String> loadPaths = engine.getLoadPaths() ;
		System.out.println("loadPaths (" + loadPaths.size() + "): " + loadPaths.toString()) ;
		return engine ;
	}

	/**
	 * Eine Script-Variable setzen
	 *
	 * @param variableName der - in der Scriptssprache g&uuml;ltige Variablenname
	 * @param o ist der Wert/Instanz
	 */
	public void put(String variableName, Object o) {
		getEngine().put(variableName, o) ;
	}


	/**
	 * Das Script starten und ein Ergebnis vom angegebenen Typ erhalten
	 *
	 * @param scriptlet das Script
	 * @param returnType der R&uuml;ckgabetyp
	 * @return den R&uuml;ckgabewert
	 */
	public <T> T runEmbeddedScript(String scriptlet) {
		T value = (T) getEngine().runScriptlet(scriptlet) ;
		return value ;
	}

	public <T> T runScript(String scriptFilename) {
		T value = (T) getEngine().runScriptlet(PathType.RELATIVE, scriptFilename) ;
		return value ;
	}
}
