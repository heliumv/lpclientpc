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
package com.lp.client.stueckliste;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;

@SuppressWarnings("static-access") 
/**
 * <p>Panel zum Bearbeiten der Laeger eines Loses</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>11. 10. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelStklagerentnahme
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private TabbedPaneStueckliste tabbedPaneStueckliste = null;

  private LagerDto lagerDto = null;

  private StklagerentnahmeDto stklagerDto =null;

  private static final String ACTION_SPECIAL_LAGER =
      "action_special_los_lager";

  private PanelQueryFLR panelQueryFLRLager = null;

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jPanelWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private Border border1;

  private WrapperTextField wtfLager = null;
  private WrapperButton wbuLager = null;


  public PanelStklagerentnahme(InternalFrame internalFrame,
                           String add2TitleI, Object key,
                           TabbedPaneStueckliste tabbedPaneStueckliste)
      throws Throwable {
    super(internalFrame, add2TitleI, key);
    this.tabbedPaneStueckliste = tabbedPaneStueckliste;
    jbInit();
    initComponents();
  }


  private TabbedPaneStueckliste getTabbedPaneStueckliste() {
    return tabbedPaneStueckliste;
  }


  private void jbInit()
      throws Throwable {
    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD
    };
    this.enableToolsPanelButtons(aWhichButtonIUse);

    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(gridBagLayout1);

    jPanelWorkingOn.setBorder(border1);
    jPanelWorkingOn.setLayout(gridBagLayout3);
    JPanel panelButtonAction = getToolsPanel();
    getInternalFrame().addItemChangedListener(this);

    // controls
    wbuLager=new WrapperButton();
    wtfLager=new WrapperTextField();


    wbuLager.setText(LPMain.getInstance().getTextRespectUISPr("button.lager"));
    wbuLager.setToolTipText(LPMain.getInstance().getTextRespectUISPr("button.lager.tooltip"));

    wtfLager.setActivatable(false);
    wtfLager.setMandatoryFieldDB(true);

    wbuLager.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wbuLager.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));


    wbuLager.addActionListener(this);
    wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);


    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    // statusbarneu: 1 an den unteren rand des panels haengen
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    iZeile++;
    jPanelWorkingOn.add(wbuLager,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    jPanelWorkingOn.add(wtfLager,
                        new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;

  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      components2Dto();
      if (stklagerDto != null) {
        StklagerentnahmeDto savedDto = DelegateFactory.getInstance().getStuecklisteDelegate().
            updateStklagerentnahme(stklagerDto);
        this.stklagerDto = savedDto;
        setKeyWhenDetailPanel(stklagerDto.getIId());
        super.eventActionSave(e, true);
        // jetz den anzeigen
        eventYouAreSelected(false);
      }
    }
  }


  /**
   * Die eingegebenen Daten in ein Dto schreiben
   *
   * @throws Throwable
   */
  private void components2Dto()
      throws Throwable {
    // den bestehenden Dto verwenden
    if(stklagerDto==null) {
      stklagerDto=new StklagerentnahmeDto();
      stklagerDto.setStuecklisteIId(getTabbedPaneStueckliste().getInternalFrameStueckliste().getStuecklisteDto().getIId());
    }
    stklagerDto.setLagerIId(lagerDto.getIId());

  }


  /**
   * Ein Dto-Objekt ins Panel uebertragen
   *
   * @throws Throwable
   */
  private void dto2Components()
      throws Throwable {
    if(stklagerDto!=null) {
      holeLager(stklagerDto.getLagerIId());
      
    }
  }



  private void dto2ComponentsLager() {
    if (lagerDto != null) {
      wtfLager.setText(lagerDto.getCNr());
    }
    else {
      wtfLager.setText(null);
    }
  }


  public void eventActionNew(EventObject eventObject, boolean bChangeKeyLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, false, false);
    stklagerDto = null;
    this.lagerDto = null;
    this.leereAlleFelder(this);
  }


  /**
   * Stornieren einer Rechnung bzw Gutschrift
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    if (this.stklagerDto != null) {
      if (stklagerDto.getIId() != null) {
        if (!isLockedDlg()) {
          DelegateFactory.getInstance().getStuecklisteDelegate().removeStklagerentnahme(stklagerDto);
          this.stklagerDto = null;
          this.leereAlleFelder(this);
          this.setKeyWhenDetailPanel(null);
          super.eventActionDelete(e, false, false);
        }
      }
    }
  }


  protected String getLockMeWer() {
    return HelperClient.LOCKME_LOS;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
      dialogQueryLager(e);
    }
  }


  /**
   * eventItemchanged.
   *
   * @param eI EventObject
   * @throws ExceptionForLPClients
   * @throws Throwable
   */
  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRLager) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        holeLager((Integer)key);
      }
    }
  }


  private void holeLager(Integer key)
      throws Throwable {
    if (key != null) {
      lagerDto = DelegateFactory.getInstance().getLagerDelegate().lagerFindByPrimaryKey(key);
    }
    else {
      lagerDto = null;
    }
    dto2ComponentsLager();
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);
    if (!bNeedNoYouAreSelectedI) {
      Object key=getKeyWhenDetailPanel();
      if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
        // einen neuen Eintrag anlegen oder die letzte Position wurde geloescht.
        leereAlleFelder(this);
        clearStatusbar();
      }
      else {
        // einen alten Eintrag laden.
        stklagerDto = DelegateFactory.getInstance().getStuecklisteDelegate().
            stklagerentnahmeFindByPrimaryKey( (Integer) key);
        dto2Components();
      }
    }
  }


  protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
      throws Throwable {
    super.eventActionUpdate(aE, bNeedNoUpdateI);
  }


  public boolean handleOwnException(ExceptionLP exfc)
      throws Throwable {
    return false;
  }


  private void dialogQueryLager(ActionEvent e)
      throws Throwable {
    panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(
      getInternalFrame(), (lagerDto != null) ? lagerDto.getIId() : null);
    new DialogQuery(panelQueryFLRLager);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuLager;
  }
}

