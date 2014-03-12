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
package com.lp.client.fertigung;


import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.KapazitaetsvorschauDto;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich um die Interne Bestellung </I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>03.12.05</I></p>
 *
 * <p> </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class TabbedPaneKapazitaetsvorschau
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private PanelDiagrammKapazitaetsvorschau panelKapazitaetsvorschau = null;

  private KapazitaetsvorschauDto kapDto = null;

  private static final int IDX_PANEL_KAPAZITAETSVORSCHAU = 0;

  public TabbedPaneKapazitaetsvorschau(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr(
              "fert.tab.unten.kapazitaetsvorschau.title"));
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
    insertTab(
        LPMain.getInstance().getTextRespectUISPr(
        "fert.tab.unten.kapazitaetsvorschau.title"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr(
            "fert.tab.unten.kapazitaetsvorschau.tooltip"),
        IDX_PANEL_KAPAZITAETSVORSCHAU);

    getPanelKapazitaetsvorschau(true);

    addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);
  }


  protected KapazitaetsvorschauDto getInternebestellungDto() {
    return kapDto;
  }


  void setInternebestellungDto(KapazitaetsvorschauDto kapDto)
      throws Throwable {
    this.kapDto = kapDto;
  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
  }


  /**
   * eventStateChanged
   *
   * @param e ChangeEvent
   * @throws Throwable
   */
  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    switch (selectedIndex) {
      case IDX_PANEL_KAPAZITAETSVORSCHAU: {
        getPanelKapazitaetsvorschau(true).eventYouAreSelected(false);
        getPanelKapazitaetsvorschau(true).updateButtons(new LockStateValue(null, null,
            PanelBasis.LOCK_IS_NOT_LOCKED));
      }
      break;
    }
  }


  protected void lPActionEvent(java.awt.event.ActionEvent e) {
  }


  private PanelDiagrammKapazitaetsvorschau getPanelKapazitaetsvorschau(boolean
      bNeedInstantiationIfNull)
      throws Throwable {
    if (panelKapazitaetsvorschau == null && bNeedInstantiationIfNull) {
      panelKapazitaetsvorschau = new PanelDiagrammKapazitaetsvorschau(getInternalFrame());
      setComponentAt(IDX_PANEL_KAPAZITAETSVORSCHAU, panelKapazitaetsvorschau);
    }
    return panelKapazitaetsvorschau;
  }


  protected javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    return new WrapperMenuBar(this);
  }
}
