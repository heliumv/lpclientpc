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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access") 
public class DialogIstmaterialAendern
    extends JDialog
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private PanelQuery panelQueryLosistmaterial = null;
  private PanelBasis panelDetailLosistmaterial = null;
  private PanelBasis panelSplitLosistmaterial = null;
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private InternalFrame internalFrame = null;
  private Integer lossollmaterialIId = null;

  public DialogIstmaterialAendern() {
    // nothing here
  }


  public DialogIstmaterialAendern(Integer lossollmaterialIId, InternalFrame internalFrame)
      throws Throwable {
    super(LPMain.getInstance().getDesktop(),
          LPMain.getInstance().getTextRespectUISPr("fert.tooltip.istmaterialaendern"), true);

    this.internalFrame = internalFrame;
    this.lossollmaterialIId = lossollmaterialIId;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    jbInit();
    pack();

    panelQueryLosistmaterial.updateButtons(new LockStateValue(PanelBasis.
        LOCK_IS_NOT_LOCKED));
    panelDetailLosistmaterial.updateButtons(new LockStateValue(PanelBasis.
        LOCK_IS_NOT_LOCKED));

  }


  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      internalFrame.removeItemChangedListener(panelDetailLosistmaterial);
      panelDetailLosistmaterial = null;
      this.dispose();
    }
  }


  private void jbInit()
      throws Throwable {

    FilterKriterium[] kriterien = new FilterKriterium[1];

    kriterien[0] = new FilterKriterium(
        FertigungFac.FLR_LOSISTMATERIAL_LOSSOLLMATERIAL_I_ID,
        true,
        lossollmaterialIId + "",
        FilterKriterium.OPERATOR_EQUAL, false);

    panelQueryLosistmaterial = new PanelQuery(
        null,
        kriterien,
        QueryParameters.UC_ID_LOSISTMATERIAL,
        null,
        internalFrame,
        LPMain.getInstance().getTextRespectUISPr("fert.tooltip.istmaterialaendern"),
        true);
    panelQueryLosistmaterial.eventYouAreSelected(false);

    panelDetailLosistmaterial =
        new PanelIstmaterialAendern(
            internalFrame, lossollmaterialIId,
            LPMain.getInstance().getTextRespectUISPr(
                "fert.tooltip.istmaterialaendern"),panelQueryLosistmaterial);

    panelSplitLosistmaterial = new PanelSplit(
        internalFrame,
        panelDetailLosistmaterial,
        panelQueryLosistmaterial,
        400);

    panelDetailLosistmaterial.setKeyWhenDetailPanel(panelQueryLosistmaterial.getSelectedId());
    panelDetailLosistmaterial.eventYouAreSelected(false);

    this.getContentPane().setLayout(gridBagLayout2);

    this.getContentPane().add(panelSplitLosistmaterial,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
        0));

  }

}
