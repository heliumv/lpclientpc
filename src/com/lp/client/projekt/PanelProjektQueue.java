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
package com.lp.client.projekt;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenTexteingabe;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 09:58:28 $
 */
public class PanelProjektQueue
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final InternalFrameProjekt intFrame;
  private final TabbedPaneProjekt tpProjekt;
  private PanelPositionenTexteingabe panelText = null;
  private WrapperTextField wtfTitel = null;
  private WrapperLabel wlfTitel=null;
  /**
   * Konstruktor.
   * @param internalFrame der InternalFrame auf dem das Panel sitzt
   * @param add2TitleI der default Titel des Panels
   * @param key PK des Projektes
   * @throws java.lang.Throwable Ausnahme
   */
  public PanelProjektQueue(InternalFrame internalFrame, String add2TitleI,
                             Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    intFrame = (InternalFrameProjekt) internalFrame;
    tpProjekt = intFrame.getTabbedPaneProjekt();
    jbInitPanel();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DISCARD, // btndiscard: 0 den Button am Panel anbringen
    };
    enableToolsPanelButtons(aWhichButtonIUse);

    // das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach innen
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));



    panelText = new PanelPositionenTexteingabe(
        getInternalFrame(),
        LocaleFac.POSITIONSART_TEXTEINGABE,
        getKeyWhenDetailPanel());
    wlfTitel = new WrapperLabel(LPMain.getTextRespectUISPr("proj.projekt.label.titel"));
    wtfTitel = new WrapperTextField();
    wtfTitel.setMandatoryField(true);
    wtfTitel.setColumnsMax(80);
    // Zeile - die Toolbar
    this.add(getToolsPanel(), new GridBagConstraints(0, iZeile, 3, 1, 1.0, 1.0
                                             , GridBagConstraints.NORTHWEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;
    this.add(wlfTitel, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));

    this.add(wtfTitel, new GridBagConstraints(1, iZeile, 2, 1, 1.9, 0.0
                                             , GridBagConstraints.CENTER,
                                             GridBagConstraints.BOTH,
                                             new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    this.add(panelText, new GridBagConstraints(0, iZeile, 3, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
    // Statusbar an den unteren Rand des Panels haengen
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
  }

  protected String getLockMeWer() {
    return HelperClient.LOCKME_PROJEKT;
  }


  private void setDefaults()
      throws Throwable {

    leereAlleFelder(this);
  }


  private void dto2Components()
      throws Throwable {
   panelText.setText(tpProjekt.getProjektDto().getXFreetext());
   wtfTitel.setText(tpProjekt.getProjektDto().getCTitel());
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws
      Throwable {
    super.eventActionNew(eventObject, true, false);
    setDefaults();
    clearStatusbar();
  }


	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (!panelText.hasContent()) {
				showDialogPflichtfelderAusfuellen();
				return;
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {

    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null || key.equals(LPMain.getLockMeForNew())) {
      // Neu.
      leereAlleFelder(this);
      panelText.setText("");
      clearStatusbar();
    } else {
      dto2Components();
    }
    tpProjekt.setTitleProjekt(LPMain.getTextRespectUISPr("proj.projekt.details"));
    aktualisiereStatusbar();
  }


  private void aktualisiereStatusbar()
      throws Throwable {
    setStatusbarPersonalIIdAnlegen(tpProjekt.getProjektDto().getPersonalIIdAnlegen());
    setStatusbarTAnlegen(tpProjekt.getProjektDto().getTAnlegen());
    setStatusbarPersonalIIdAendern(tpProjekt.getProjektDto().getPersonalIIdAendern());
    setStatusbarTAendern(tpProjekt.getProjektDto().getTAendern());
    setStatusbarStatusCNr(tpProjekt.getProjektDto().getStatusCNr());
  }
}
