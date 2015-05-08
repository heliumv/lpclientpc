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
package com.lp.client.frame.component;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;

import com.lp.service.DiagrammDto;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:39:22 $
 */
public abstract class PanelDiagramm
    extends PanelBasis implements ISourceEvent, ChartMouseListener
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private DiagrammDto diagrammDto = null;
  private JPanel jpaWorkingOn = null;
  private ChartPanel chartPanel = null;

  public PanelDiagramm(
      String sTitelTabbedPaneI,
      InternalFrame oInternalFrameI)
      throws Throwable {
    super(oInternalFrameI, sTitelTabbedPaneI);
    jbInit();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    jpaWorkingOn = new JPanel(new GridBagLayout());
    chartPanel = new ChartPanel(null);
    JPanel panelButtonAction = getToolsPanel();
    getInternalFrame().addItemChangedListener(this);
    this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                                    0));
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    jpaWorkingOn.removeAll();
    diagrammDto = buildDiagrammDto();
    if (diagrammDto.getJfcKapazitaetsvorschau() != null) {
      chartPanel = new ChartPanel(diagrammDto.getJfcKapazitaetsvorschau());
      jpaWorkingOn.add(chartPanel,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                                      0));
      chartPanel.addChartMouseListener(this);
    }
    // Refreshknopf reaktivieren
//    updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
    super.eventYouAreSelected(false);
  }


  public String getAspect() {
    return "";
  }


  /**
   * Da es in diesem Diagramm vorerst nicht geawehlt wird, kann null zurueckgegeben
   * werden.
   * @return Object ist immer null
   */
  public Object getIdSelected() {
    return null;
  }

  protected String getLockMeWer()
      throws Exception {
    return null;
  }


  public abstract DiagrammDto buildDiagrammDto()
      throws Throwable;


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    // nothing here
  }


  public void chartMouseMoved(ChartMouseEvent event) {
    // nothing here
  }

  public void chartMouseClicked(ChartMouseEvent event) {
    // nothing here
  }

  protected DiagrammDto getDiagrammDto() {
    return diagrammDto;
  }

}
