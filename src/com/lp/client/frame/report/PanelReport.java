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
package com.lp.client.frame.report;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.server.util.report.JasperPrintLP;

public class PanelReport
    extends PanelDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JasperPrintLP print = null;
  private ReportViewer jpaViewer = null;

  public ReportViewer getJpaViewer(){
	  return jpaViewer;
  }
  
  public PanelReport(InternalFrame internalFrame, String addTitleI,
                     JasperPrintLP print)
      throws Throwable {
    this(internalFrame, addTitleI, print, true);
  }

  public PanelReport(InternalFrame internalFrame, String addTitleI,
                     JasperPrintLP print, boolean bShowExitButton)
      throws Throwable {

    super(internalFrame, addTitleI, bShowExitButton);
    this.print = print;
    jbInit();
    initComponents();

    LockStateValue lockstateValue = new LockStateValue(null, null, LOCK_NO_LOCKING);
    updateButtons(lockstateValue);
  }

  private void jbInit() throws Throwable {
    jpaViewer = new ReportViewer(print.getPrint());
    /**
     * @todo scrollleisten nur sichtbar wenn notwendig  PJ 5099
     */
    jpaViewer.getBtnFitWidth().doClick();
    //Actionpanel von Superklasse holen und anhaengen.
    JPanel panelButtonAction = getToolBar().getToolsPanelLeft();
    jpaViewer.getBtnSave().setVisible(false);
    jpaViewer.getBtnPrint().setVisible(false);
    jpaViewer.getBtnReload().setVisible(false);
    panelButtonAction.add(jpaViewer.getBtnFirst());
    panelButtonAction.add(jpaViewer.getBtnPrevious());
    panelButtonAction.add(jpaViewer.getBtnNext());
    panelButtonAction.add(jpaViewer.getBtnLast());
    panelButtonAction.add(jpaViewer.getTxtGoto());
    panelButtonAction.add(jpaViewer.getBtnActualSize());
    panelButtonAction.add(jpaViewer.getBtnFitPage());
    panelButtonAction.add(jpaViewer.getBtnFitWidth());
    panelButtonAction.add(jpaViewer.getBtnZoomIn());
    panelButtonAction.add(jpaViewer.getBtnZoomOut());
    panelButtonAction.add(jpaViewer.getCmbZoom());

    jpaWorkingOn.add(jpaViewer, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
  }

  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    super.eventActionSpecial(e);
  }

  void doClickOnPrint() {
    jpaViewer.getBtnPrint().doClick();
  }

  void doClickOnSave() {
    jpaViewer.getBtnSave().doClick();
  }

  public JasperPrintLP getPrint() {
    return print;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
  }
}
