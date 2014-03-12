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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;


public class PanelTabelleBewegungsvorschau
    extends PanelTabelle {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private ArtikelDto artikelDto = null;

  // das soll in der optionalen zweiten Zeile stehen
  private WrapperLabel wlaEmpty = null;
  private WrapperLabel wlaIstBestand = null;
  private WrapperLabel wlaSollBestand = null;
  private WrapperLabel wlaMindestBestand = null;

  private WrapperLabel wlaKritAuswertung = null;

  /**
   * PanelTabelle.
   *
   * @param iUsecaseIdI die eindeutige UseCase ID
   * @param sTitelTabbedPaneI der Titel des aktuellen TabbedPane
   * @param oInternalFrameI der uebergeordente InternalFrame
   * @throws java.lang.Throwable Ausnahme
   */
  public PanelTabelleBewegungsvorschau(
      int iUsecaseIdI,
      String sTitelTabbedPaneI,
      InternalFrame oInternalFrameI)
      throws Throwable {
    super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
    jbInitPanel();
    initComponents();
  }


  /**
   * Initialisiere alle Komponenten; braucht der JBX-Designer;
   *
   * @throws Exception
   * @throws ExceptionLP
   * @throws Throwable
   */
  private void jbInitPanel()
      throws Throwable {

    wlaKritAuswertung = new WrapperLabel();

    wlaEmpty = new WrapperLabel();
    wlaEmpty.setMaximumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
                                          Defaults.getInstance().getControlHeight()));
    wlaEmpty.setMinimumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
                                          Defaults.getInstance().getControlHeight()));
    wlaEmpty.setPreferredSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
                                            Defaults.getInstance().getControlHeight()));

    wlaIstBestand = new WrapperLabel();
    wlaSollBestand = new WrapperLabel();
    wlaMindestBestand = new WrapperLabel();

    getPanelOptionaleZweiteZeile().add(wlaIstBestand,
                                       new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    getPanelOptionaleZweiteZeile().add(wlaSollBestand,
                                       new GridBagConstraints(2, 0, 1, 1, 0.1, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    getPanelOptionaleZweiteZeile().add(wlaMindestBestand,
                                       new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

    setFirstColumnVisible(false);
  }


  /**
   * eventActionRefresh
   *
   * @param e ActionEvent
   * @param bNeedNoRefreshI boolean
   * @throws Throwable
   */
  protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
      throws Throwable {
    super.eventActionRefresh(e, bNeedNoRefreshI);
    artikelDto = DelegateFactory.getInstance().getArtikelDelegate().
        artikelFindByPrimaryKey(Integer.valueOf(
            getDefaultFilter()[0].value));
    wlaKritAuswertung.setText(LPMain.getTextRespectUISPr("lp.artikel") + ": " +
                              artikelDto.formatArtikelbezeichnung());
    wlaKritAuswertung.setMaximumSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setMinimumSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setPreferredSize(new Dimension(350,
        Defaults.getInstance().getControlHeight()));
    wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

    getPanelFilterKriterien().add(wlaKritAuswertung);

    wlaIstBestand.setText(LPMain.getTextRespectUISPr("lp.lagerstand") + ": " +
                          DelegateFactory.getInstance().getLagerDelegate().
                          getLagerstandAllerLagerEinesMandanten(artikelDto.getIId()));
    wlaIstBestand.setHorizontalAlignment(SwingConstants.CENTER);
    wlaSollBestand.setText(LPMain.getTextRespectUISPr("artikel.lagersollstand") + ":" + " " +
                           ((artikelDto.getFLagersoll() != null) ? artikelDto.getFLagersoll() :
                            "-"));
    wlaSollBestand.setHorizontalAlignment(SwingConstants.CENTER);
    wlaMindestBestand.setText(LPMain.getTextRespectUISPr("artikel.lagermindeststand") + ":" +
                              " " +
                              ((artikelDto.getFLagermindest() != null) ? artikelDto.getFLagermindest() :
                               "-"));
    wlaMindestBestand.setHorizontalAlignment(SwingConstants.CENTER);
  }


}
