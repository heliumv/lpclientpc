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
package com.lp.client.finanz;

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

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
public class PanelFinanzMahnlaufKopfdaten
    extends PanelBasis {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaDatum = new WrapperLabel();
  private WrapperDateField wdfDatum = new WrapperDateField();
  private Border border1;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jPanelWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private TabbedPaneMahnwesen tpMahnwesen=null;

  public PanelFinanzMahnlaufKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key, TabbedPaneMahnwesen tpMahnwesen) throws Throwable{
    super(internalFrame, add2TitleI, key);
    this.tpMahnwesen=tpMahnwesen;
    jbInit();
    initComponents();
  }


  /**
   * jbInit
   *
   * @throws Throwable
   */
  private void jbInit()
      throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(gridBagLayout1);
    this.setBorder(BorderFactory.createEtchedBorder());
    JPanel panelButtonAction = getToolsPanel();
    String[] aWhichButtonIUse = {
        ACTION_DELETE};
    enableToolsPanelButtons(aWhichButtonIUse);
    //... bis hier ist's immer gleich
    //wegen dialogFLR
    getInternalFrame().addItemChangedListener(this);

    jPanelWorkingOn.setBorder(border1);
    jPanelWorkingOn.setOpaque(true);
    jPanelWorkingOn.setLayout(gridBagLayout3);
    wdfDatum.setActivatable(false);
    wlaDatum.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaDatum.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));

    this.add(panelButtonAction,
             new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jPanelWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(),
             new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wlaDatum,
                        new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
    jPanelWorkingOn.add(wdfDatum,
                        new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                               GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(2, 2, 2, 2), 0, 0));
  }

  protected String getLockMeWer() {
    return HelperClient.LOCKME_FINANZ_MAHNLAUF;
  }

  private void dto2Components() {
    if(tpMahnwesen.getMahnlaufDto()!=null) {
      wdfDatum.setDate(tpMahnwesen.getMahnlaufDto().getTAnlegen());
    }
    else {
      wdfDatum.setDate(null);
    }
  }

  /**
   * Loeschen einer Zahlung
   *
   * @param e ActionEvent
   * @param bAdministrateLockKeyI boolean
   * @param bNeedNoDeleteI boolean
   * @throws Throwable
   */
  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
      throws Throwable {
    if (tpMahnwesen.getMahnlaufDto() != null) {
      if (tpMahnwesen.getMahnlaufDto().getIId() != null) {
        if (!isLockedDlg()) {
          DelegateFactory.getInstance().getMahnwesenDelegate().removeMahnlauf(tpMahnwesen.
              getMahnlaufDto());
          tpMahnwesen.setMahnlaufDto(null);
          this.leereAlleFelder(this);
          super.eventActionDelete(e, false, false);
        }
      }
    }
  }

  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
    Object key = getKeyWhenDetailPanel();
    if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
      // einen neuen Eintrag anlegen oder die letzte Position wurde geloescht.
      leereAlleFelder(this);
      dto2Components();
      clearStatusbar();
    }
    else {
      // einen alten Eintrag laden.
      tpMahnwesen.setMahnlaufDto (DelegateFactory.getInstance().getMahnwesenDelegate().mahnlaufFindByPrimaryKey( (
          Integer) key));
      dto2Components();
    }
    super.eventYouAreSelected(false);
  }

  protected void eventItemchanged(EventObject eI)
      throws Throwable {
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
