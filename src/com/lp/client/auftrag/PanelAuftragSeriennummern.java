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
package com.lp.client.auftrag;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragseriennrnDto;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 07:36:13 $
 */
public class PanelAuftragSeriennummern
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final InternalFrameAuftrag intFrame;
  private final TabbedPaneAuftrag tpAuftrag;
  private AuftragseriennrnDto auftragseriennrnDto = null;
  private WrapperSNRField wtfSeriennr = null;
  private WrapperLabel wlaSeriennr = null;
  protected JPanel jpaWorkingOn = new JPanel();

  public PanelAuftragSeriennummern(InternalFrame internalFrame, String add2TitleI,
                             Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    intFrame = (InternalFrameAuftrag) internalFrame;
    tpAuftrag = intFrame.getTabbedPaneAuftrag();
    jbInitPanel();
    initComponents();
  }


  private void jbInitPanel()
      throws Throwable {

    // zusaetzliche buttons
    String[] aWhichButtonIUse = {
        PanelBasis.ACTION_NEW,
        PanelBasis.ACTION_UPDATE,
        PanelBasis.ACTION_SAVE,
        PanelBasis.ACTION_DISCARD, // btndiscard: 0 den Button am Panel anbringen
    };
    enableToolsPanelButtons(aWhichButtonIUse);

    // das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach innen
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

    wlaSeriennr = new WrapperLabel();
    wlaSeriennr.setText(LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern"));
    wlaSeriennr.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaSeriennr.setPreferredSize(new Dimension(100,
                                               Defaults.getInstance().getControlHeight()));
    wtfSeriennr = new WrapperSNRField();
    wtfSeriennr.setMinimumSize(new Dimension(200, Defaults.getInstance().getControlHeight()));
    wtfSeriennr.setPreferredSize(new Dimension(200, Defaults.getInstance().getControlHeight()));


    // Zeile - die Toolbar
    add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.NORTHWEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 0), 0, 0));

    iZeile++;

    this.add(wlaSeriennr, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
    this.add(wtfSeriennr, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

  }

  protected String getLockMeWer() {
    return HelperClient.LOCKME_AUFTRAGSERIENNUMMERN;
  }


  private void setDefaults()
      throws Throwable {
    auftragseriennrnDto = new AuftragseriennrnDto();
    leereAlleFelder(this);
  }


  private void dto2Components()
      throws Throwable {
  wtfSeriennr.setText(auftragseriennrnDto.getCSeriennr());
 }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws
      Throwable {
    if(eventObject == null){
       eventObject = new ItemChangedEvent(this, ItemChangedEvent.ACTION_NEW);
    }
    super.eventActionNew(eventObject, true, false);
    setDefaults();
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {

    }

  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
     this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  private void components2Dto()
      throws Throwable {
    auftragseriennrnDto.setCSeriennr(wtfSeriennr.getText());

  }


  public void enableNew() {
    LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_NEW);
    item.getButton().setEnabled(true);

  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null || key.equals(LPMain.getLockMeForNew())) {
      // Neu.
      leereAlleFelder(this);


    }
    else {
      // Update.
     auftragseriennrnDto = DelegateFactory.getInstance().getAuftragpositionDelegate().
        auftragseriennrnFindByAuftragpositionIId((Integer) key);
      dto2Components();
    }
  }
}
