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
 package com.lp.client.stueckliste.importassistent;

import java.awt.Point;

public interface IColumnTypeDefiner {
	
	/**
	 * Weist den ColumnTypeDefiner an, die Spalte an Punkt p farblich hervorzuheben.
	 * Liegt p ausserhalb der Komponente, wird keine Spalte hervorgehoben
	 * @param p der Punkt  im Koordinatensystem der Komponente, kann null sein wenn nichts hervorzuheben ist.
	 */
	public void highlightColumnAt(Point p);
	
	/**
	 * Sagt dem ColumnTypeDefiner, dass die Spalte an Punkt <code>p</code> den SpaltenTyp <code>type</code>
	 * zugewiesen bekommt.
	 * @param p der Punkt im Koordinatensystem der Komponente, kann auch ausserhalb der Komponente liegen.
	 * @param type der Spaltentyp
	 */
	public void setColumnTypeAt(Point p, String type);
	
	
	public void addDefinitionListener(IDefinitionListener listener);
	
	public void removeDefinitionListener(IDefinitionListener listener);
	

}
