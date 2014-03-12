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
package com.lp.client.frame.report;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access") 
/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 *
 * <p> </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public abstract class ReportEtikett
    extends PanelBasis implements PanelReportIfJRDS
{
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = null;
	private WrapperLabel wlaExemplare = null;
	protected WrapperNumberField wnfExemplare=null;

  public ReportEtikett(InternalFrame internalFrame, String sTitle) throws Throwable {
    super(internalFrame, sTitle);
    jbInit();
    setDefaults();
  }
  
  public void setWnfExemplareIsEditable(boolean bIseditable){
	  wnfExemplare.setEditable(bIseditable);
  }


  private void jbInit()
      throws Throwable {
    jpaWorkingOn = new JPanel(new GridBagLayout());
    wlaExemplare=new WrapperLabel();
    wlaExemplare.setText(LPMain.getInstance().getTextRespectUISPr("report.exemplare"));
    wlaExemplare.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wlaExemplare.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
    wnfExemplare = new WrapperNumberField();
    wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
    wnfExemplare.setFractionDigits(0);
    wnfExemplare.setMaximumIntegerDigits(3);

    this.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn,
             new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaExemplare,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(wnfExemplare,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    jpaWorkingOn.add(new WrapperLabel(),
                     new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                            0, 0));
    iZeile++;
  }


  private void setDefaults() {
    wnfExemplare.setInteger(new Integer(1));
  }


  public abstract String getReportname();


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wnfExemplare;
  }


  protected Integer getAnzahlExemplare()
      throws Throwable {
    return wnfExemplare.getInteger();
  }
}
