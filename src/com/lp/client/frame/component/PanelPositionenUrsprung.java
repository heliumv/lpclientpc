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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
/**
 * <p>Fenster fuer die Erfassung einer Belegposition vom Typ Ursprung.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-03-22</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelPositionenUrsprung
    extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public WrapperLabel wlaUrsprung = null;
  public WrapperTextField wtfUrsprung = null;

  private int iSpaltenbreite1;

  /**
   * Konstruktor.
   * @param internalFrame der InternalFrame auf dem das Panel sitzt
   * @param iSpaltenbreite1I die Breite der ersten Spalte
   * @throws java.lang.Throwable Ausnahme
   */
  public PanelPositionenUrsprung(InternalFrame internalFrame,
      int iSpaltenbreite1I) throws Throwable {
    super();

    iSpaltenbreite1 = iSpaltenbreite1I;

    jbInit();
  }

  private void jbInit() throws Throwable {
    setLayout(new GridBagLayout());

    wlaUrsprung = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.ursprung"));
    wlaUrsprung.setMaximumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
    wlaUrsprung.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));
    wlaUrsprung.setPreferredSize(new Dimension(iSpaltenbreite1, Defaults.getInstance().getControlHeight()));

    wtfUrsprung = new WrapperTextField();

    add(wlaUrsprung,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    add(wtfUrsprung,  new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
  }
}
