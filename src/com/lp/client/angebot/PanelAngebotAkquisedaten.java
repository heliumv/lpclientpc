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
package com.lp.client.angebot;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
/**
 * <p>Hier werden Zeilen fuer die Akquisedaten das Angebot zur Verfuegung
 * gestellt.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 14.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelAngebotAkquisedaten
    extends PanelDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperLabel wlaNachfasstermin = null;
  private WrapperLabel wlaRealisierungstermin = null;
  private WrapperLabel wlaAuftragswahrscheinlichkeit = null;
  private WrapperLabel wlaProzent = null;
  private WrapperLabel wlaAblageort = null;

  protected WrapperDateField wdfNachfasstermin = null;
  protected WrapperDateField wdfRealisierungstermin = null;
  protected WrapperNumberField wnfAuftragswahrscheinlichkeit = null;

  protected WrapperTextField wtfAblageort = null;

  public PanelAngebotAkquisedaten(InternalFrame internalFrame,
                                  String add2TitleI)
      throws Throwable {
    super(internalFrame, add2TitleI);

    jbInit();
    setDefaults();
    initComponents();
  }


  private void jbInit()
      throws Throwable {
    wlaNachfasstermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "angb.nachfasstermin"));
    HelperClient.setDefaultsToComponent(wlaNachfasstermin, 150);
    wlaRealisierungstermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "angb.realisierungstermin"));
    wlaAuftragswahrscheinlichkeit = new WrapperLabel(LPMain.getInstance().
        getTextRespectUISPr("angb.auftragswahrscheinlichkeit"));
    wlaProzent = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "label.prozent"));
    HelperClient.setDefaultsToComponent(wlaProzent, 150);
    wlaProzent.setHorizontalAlignment(SwingConstants.LEADING);
    wlaAblageort = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "angb.ablageort"));
    wtfAblageort = new WrapperTextField();
    wtfAblageort.setColumnsMax(40);

    wdfNachfasstermin = new WrapperDateField();
    wdfNachfasstermin.setMandatoryField(true);
    wdfRealisierungstermin = new WrapperDateField();
    wnfAuftragswahrscheinlichkeit = new WrapperNumberField();
    wnfAuftragswahrscheinlichkeit.setMaximumValue(100);
    HelperClient.setDefaultsToComponent(wnfAuftragswahrscheinlichkeit, 100);
    wnfAuftragswahrscheinlichkeit.setMandatoryField(true);

  }

  protected void setDefaults() throws Throwable {
    wdfNachfasstermin.setDate(new Date(System.currentTimeMillis()));
    wnfAuftragswahrscheinlichkeit.setBigDecimal(new BigDecimal(0));
  }

  public void addAkquisedaten(JPanel jpanelI, int iZeileI)
      throws Throwable {
    jpanelI.add(wlaNachfasstermin,
                new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpanelI.add(wdfNachfasstermin,
                new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));

    iZeileI++;
    jpanelI.add(wlaRealisierungstermin,
                new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpanelI.add(wdfRealisierungstermin,
                new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));

    iZeileI++;
    jpanelI.add(wlaAuftragswahrscheinlichkeit,
                new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpanelI.add(wnfAuftragswahrscheinlichkeit,
                new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));
    jpanelI.add(wlaProzent,
                new GridBagConstraints(2, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));

    iZeileI++;
    jpanelI.add(wlaAblageort,
                new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));

    jpanelI.add(wtfAblageort,
                new GridBagConstraints(1, iZeileI, 5, 1, 0.0, 0.0,
                                       GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(2, 2, 2, 2), 0, 0));

  }
}
