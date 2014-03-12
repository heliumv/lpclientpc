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
package com.lp.client.frame.component;




import java.util.EventObject;

import com.lp.client.frame.direktanlegen.DialogLagerplatzanlegen;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class PanelQueryFLRLagerplatzAnlegen
    extends PanelQueryFLR
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PanelQueryFLRLagerplatzAnlegen(QueryType[] typesI,
                       FilterKriterium[] filtersI,
                       String[] aWhichButtonIUseI,
                       InternalFrame internalFrameI,
                       String add2TitleI)
      throws Throwable {
    super(typesI, filtersI, QueryParameters.UC_ID_LAGERPLATZ, aWhichButtonIUseI, internalFrameI, add2TitleI);
  }


  /**
   * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz selektiert ist.
   * <br>Der Key dieses Datensatzes wird als Parameter uebergeben.
   * @param typesI die UI Filterkriterien fuer den Benutzer
   * @param filtersI die default Filterkriterien, die fuer den Benutzer nicht sichtbar sind
   * @param idUsecaseI die ID des gewuenschten UseCase
   * @param aWhichButtonIUseI welche Buttons sind auf dem Panel sichtbar
   * @param internalFrameI den InternalFrame als Kontext setzen
   * @param add2TitleI der Titel dieses Panels
   * @param oSelectedIdI der Datensatz mit diesem Key soll selektiert werden
   * @throws Throwable
   */
  public PanelQueryFLRLagerplatzAnlegen(QueryType[] typesI,
                       FilterKriterium[] filtersI,
                       String[] aWhichButtonIUseI,
                       InternalFrame internalFrameI,
                       String add2TitleI,
                       Object oSelectedIdI)
      throws Throwable {
    super(typesI, filtersI, QueryParameters.UC_ID_LAGERPLATZ, aWhichButtonIUseI, internalFrameI, add2TitleI,
          oSelectedIdI);
  }

  public void eventActionNew(EventObject eventObject,
                             boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
      DialogLagerplatzanlegen d = new DialogLagerplatzanlegen(getInternalFrame());
      d.setModal(true);
      d.setBounds(this.getBounds());
      d.setLocation(this.getLocationOnScreen());
      d.setVisible(true);

      if (d.getNeueLaagerplatzIId() != null) {
        this.eventActionRefresh(null,false);
        this.setSelectedId(d.getNeueLaagerplatzIId());
        getInternalFrame().fireItemChanged(this, ItemChangedEvent.GOTO_DETAIL_PANEL);
        dialog.dispose();
      }
      d.dispose();
  }


}
