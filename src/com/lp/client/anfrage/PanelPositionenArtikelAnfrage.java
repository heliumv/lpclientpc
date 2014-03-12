/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.client.anfrage;


import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;


/**
 * @todo MB eigentlich koennte man diese klasse loeschen.
 *
 *
 * <p>Panel fuer Artikelpositionen in der Anfrage</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 22.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelPositionenArtikelAnfrage
    extends PanelPositionenArtikelEinkauf
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * Konstruktor.
   *
   * @param internalFrame der InternalFrame auf dem das Panel sitzt
   * @param add2TitleI der default Titel des Panels
   * @param key PK der Position
   * @param sLockMeWer String
   * @param iSpaltenbreite1I die Breite der ersten Spalte
   * @throws Throwable Ausnahme
   */
  public PanelPositionenArtikelAnfrage(InternalFrame internalFrame, String add2TitleI,
                                       Object key, String sLockMeWer, int iSpaltenbreite1I)
      throws Throwable {
    super(internalFrame, add2TitleI, key, sLockMeWer, iSpaltenbreite1I);
  }


//  public void artikelDto2components()
//      throws Throwable {
//    wtfArtikel.setText(getArtikelDto().getCNr());
//
//    if (getArtikelDto().getArtikelsprDto() != null) {
//      wtfBezeichnung.setText(getArtikelDto().getArtikelsprDto().getCBez());
//      wtfZusatzbezeichnung.setText(getArtikelDto().getArtikelsprDto().getCZbez());
//    }
//    else {
//      wtfBezeichnung.setText("");
//      wtfZusatzbezeichnung.setText("");
//    }
//    wcoEinheit.setKeyOfSelectedItem(getArtikelDto().getEinheitCNr());
//  }
}
