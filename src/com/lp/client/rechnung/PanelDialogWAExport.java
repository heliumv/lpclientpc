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
package com.lp.client.rechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.Helper;


/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer den Rechnung WA Export.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>15. 07. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelDialogWAExport
    extends PanelDialog implements PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static String ACTION_SPECIAL_KUNDE_EINER = "action_special_kunde_einer";
  private final static String ACTION_SPECIAL_OK = "action_" + ALWAYSENABLED + "ok";

  private final static int BREITE_BUTTONS = 105;

  private PanelQueryFLR panelQueryFLRKunde = null;

  private KundeDto kundeDto = null;

  private WrapperLabel wlaVon1 = new WrapperLabel();
  private WrapperLabel wlaBis1 = new WrapperLabel();

  protected WrapperDateField wdfVon = new WrapperDateField();
  protected WrapperDateField wdfBis = new WrapperDateField();
  protected WrapperButton wbuKunde = new WrapperButton();
  protected WrapperTextField wtfKunde = new WrapperTextField();
  private JPanel jpanelWorkingOn = new JPanel();
  private WrapperDateRangeController wdrBereich = null;

  public PanelDialogWAExport(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    jbInitPanel();
    initComponents();
    LockStateValue lockstateValue = new LockStateValue(null, null, LOCK_NO_LOCKING);
    this.updateButtons(lockstateValue);
  }


  private void jbInitPanel()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
    wbuKunde.setToolTipText(LPMain.getTextRespectUISPr("button.kunde.tooltip"));
    wbuKunde.setMinimumSize(new Dimension(BREITE_BUTTONS,
                                          Defaults.getInstance().getControlHeight()));
    wbuKunde.setPreferredSize(new Dimension(BREITE_BUTTONS,
                                            Defaults.getInstance().getControlHeight()));

    jpanelWorkingOn.setLayout(new GridBagLayout());
    wlaVon1.setText(LPMain.getTextRespectUISPr("lp.von"));
    wlaBis1.setText(LPMain.getTextRespectUISPr("lp.bis"));
    wlaVon1.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaVon1.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis1.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
    wlaBis1.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));

    jpanelWorkingOn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    wtfKunde.setActivatable(false);
    wtfKunde.setEditable(false);

    wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_EINER);
    wbuKunde.addActionListener(this);

    wdfVon.getDisplay().addPropertyChangeListener(this);
    wdfBis.getDisplay().addPropertyChangeListener(this);

    wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

    createAndSaveAndShowButton("/com/lp/client/res/check2.png",
                               LPMain.getTextRespectUISPr("lp.tooltip.kriterienuebernehmen"),
                               ACTION_SPECIAL_OK,
                               null);

    String[] aWhichButtonIUse = {
        ACTION_SPECIAL_OK};

    enableToolsPanelButtons(aWhichButtonIUse);
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    this.add(jpanelWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpanelWorkingOn.add(wlaVon1,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpanelWorkingOn.add(wdfVon,
                     new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpanelWorkingOn.add(wlaBis1,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpanelWorkingOn.add(wdfBis,
                     new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpanelWorkingOn.add(wdrBereich,
                     new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
    jpanelWorkingOn.add(wbuKunde,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    jpanelWorkingOn.add(wtfKunde,
                     new GridBagConstraints(1, iZeile, 4, 1, 1.0, 0.0,
                                            GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_EINER)) {
      dialogQueryKunde();
    }
    else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
      Integer kundeIId = null;
      if (kundeDto != null) {
        kundeIId = kundeDto.getIId();
      }
      byte[] data = DelegateFactory.getInstance().getRechnungDelegate().exportWAJournal(
          kundeIId,
          wdfVon.getDate(),
          wdfBis.getDate(), new Integer(Helper.SORTIERUNG_NACH_IDENT)).getBytes();
     JFileChooser fc = new JFileChooser();
      fc.setDialogType(JFileChooser.SAVE_DIALOG);
      fc.setDialogTitle(LPMain.getTextRespectUISPr("rechnung.warenausgangsjournalexport"));
      int returnVal = fc.showSaveDialog(getInternalFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        LPMain.getInstance().saveFile(getInternalFrame(), file.getAbsolutePath(), data, false);
      }
      getInternalFrame().closePanelDialog();
    }
    super.eventActionSpecial(e);
  }


  private void dialogQueryKunde()
      throws Throwable {
    panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(
        getInternalFrame(), true, true);
    new DialogQuery(panelQueryFLRKunde);
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
      if (e.getSource() == panelQueryFLRKunde) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
        if (key != null) {
          kundeDto = DelegateFactory.getInstance().getKundeDelegate().
              kundeFindByPrimaryKey( (Integer) key);
          dto2ComponentsKunde();
        }
      }
    }
  }


  /**
   * Traegt die Daten fuer die Kostenstelle ein.
   */
  private void dto2ComponentsKunde() {
    if (kundeDto != null) {
      wtfKunde.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
    }
    else {
      wtfKunde.setText(null);
    }
  }


  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getSource() == wdfVon.getDisplay() && evt.getPropertyName().equals("date")) {
      wdfBis.setMinimumValue(wdfVon.getDate());
    }
    else if (evt.getSource() == wdfBis.getDisplay() &&
             evt.getPropertyName().equals("date")) {
      wdfVon.setMaximumValue(wdfBis.getDate());
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfVon;
  }

}
