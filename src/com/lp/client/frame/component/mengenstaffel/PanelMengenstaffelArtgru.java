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
package com.lp.client.frame.component.mengenstaffel;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p>Erfassung einer Mengenstaffel fuer eine Artikelgruppe.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 07.07.2006</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelMengenstaffelArtgru
    extends PanelMengenstaffelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private ArtgruDto artgruDto = null;

  private WrapperButton wbuArtikelgruppe = new WrapperButton();
  private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
  private WrapperTextField wtfArtikelgruppespr = new WrapperTextField();

  private PanelQueryFLR panelQueryFLRArtikelgruppe = null;

  private static final String ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE =
      "action_artikelgruppe_from_liste";

  public PanelMengenstaffelArtgru(InternalFrame internalFrameI, String addTitle2I)
      throws Throwable {
    super(internalFrameI, addTitle2I);
    jbInit();
  }


  private void jbInit()
      throws Throwable {
    getInternalFrame().addItemChangedListener(this);

    wbuArtikelgruppe.setText(
        LPMain.getInstance().getTextRespectUISPr("lp.artikelgruppe") + "...");
    wbuArtikelgruppe.setActionCommand(
        ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE);
    wbuArtikelgruppe.addActionListener(this);

    wtfArtikelgruppe = new WrapperTextField();
    wtfArtikelgruppe.setActivatable(false);
    wtfArtikelgruppespr = new WrapperTextField();
    wtfArtikelgruppespr.setActivatable(false);

    initMengeRabattsatz();

    addFormatierungszeile();

    iZeile++;
    add(wbuArtikelgruppe,
        new GridBagConstraints(0, iZeile, 2, 1, 0.2, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wtfArtikelgruppe,
        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wtfArtikelgruppespr,
        new GridBagConstraints(3, iZeile, 5, 1, 0.8, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    add(wlaMenge,
        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaRabattsatz,
        new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    add(wnfMenge,
        new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaMengeeinheit,
        new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wnfRabattsatz,
        new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaRabattsatzprozent,
        new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
                               GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));

    iZeile++;
    addZeileGueltigVonBis(iZeile);
  }


  public void setMandatoryFields(boolean bMandatoryI) {
    super.setMandatoryFields(bMandatoryI);

    wtfArtikelgruppe.setMandatoryField(bMandatoryI);
    wnfRabattsatz.setMandatoryField(bMandatoryI);
  }


  protected void setDefaults()
      throws Throwable {
    super.setDefaults();

    artgruDto = new ArtgruDto();

    wnfRabattsatz.setDouble(new Double(0)); // wird mit 0 initialisiert
  }


  public void artgruDto2components(Integer artgruIIdI)
      throws Throwable {
    if (artgruIIdI != null) {
      artgruDto = DelegateFactory.getInstance().getArtikelDelegate().
          artgruFindByPrimaryKey(
              artgruIIdI);

      wtfArtikelgruppe.setText(artgruDto.getCNr());

      if (artgruDto.getArtgrusprDto() != null) {
        wtfArtikelgruppespr.setText(artgruDto.getArtgrusprDto().getCBez());
      }
    }
    
  }


  private void dialogQueryArtikelgruppeFromListe(ActionEvent e)
      throws Throwable {
    panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance().
        createPanelFLRArtikelgruppe(getInternalFrame(),
                                    artgruDto.getIId());
    new DialogQuery(panelQueryFLRArtikelgruppe);
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equals(
        ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE)) {
      dialogQueryArtikelgruppeFromListe(e);
    }
  }


  public String baueTitel() {
    String sTitel = "";

    if (artgruDto.getArtgrusprDto() != null &&
        artgruDto.getArtgrusprDto().getCBez() != null) {
      sTitel += " | " + artgruDto.getArtgrusprDto().getCBez();
    }
    else if (artgruDto.getCNr() != null) {
      sTitel += " | " + artgruDto.getCNr();
    }

    return sTitel;
  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

      if (e.getSource() == panelQueryFLRArtikelgruppe) {
        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

        artgruDto = DelegateFactory.getInstance().getArtikelDelegate().
            artgruFindByPrimaryKey( (Integer) key);

        wtfArtikelgruppe.setText(artgruDto.getCNr());

        if (artgruDto.getArtgrusprDto() != null) {
          wtfArtikelgruppespr.setText(artgruDto.getArtgrusprDto().getCBez());
        }
      }
    }
  }

  public KundesokomengenstaffelDto components2mengenstaffelDto(KundesokomengenstaffelDto mengenstaffelDtoI) throws Throwable {
    //if (mengenstaffelDtoI.getKundesokoIId() == null) {
    //  throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundesokoIId == null"));
    //}

    mengenstaffelDtoI.setNMenge(wnfMenge.getBigDecimal());
    mengenstaffelDtoI.setFArtikelstandardrabattsatz(wnfRabattsatz.getDouble());

    return mengenstaffelDtoI;
  }

  public void mengenstaffelDto2components(KundesokomengenstaffelDto
      mengenstaffelDtoI)
      throws Throwable {
    wnfRabattsatz.setDouble(mengenstaffelDtoI.getFArtikelstandardrabattsatz());
    wnfMenge.setBigDecimal(mengenstaffelDtoI.getNMenge());
  }

  public KundesokoDto components2kundesokoDto(KundesokoDto kundesokoDtoI) throws Throwable {
    super.components2kundesokoDto(kundesokoDtoI);

    kundesokoDtoI.setArtgruIId(artgruDto.getIId());
    kundesokoDtoI.setArtikelIId(null);
    kundesokoDtoI.setBWirktNichtFuerPreisfindung(Helper.boolean2Short(false));

    return kundesokoDtoI;
  }

  public WrapperButton getWbuArtikelgruppe() {
    return wbuArtikelgruppe;
  }

  public WrapperTextField getWtfArtikelgruppe() {
    return wtfArtikelgruppe;
  }

  public WrapperTextField getWtfArtgruppespr() {
    return wtfArtikelgruppespr;
  }

  public ArtgruDto getArtgruDto() {
    return artgruDto;
  }

  public void setArtgruDto(ArtgruDto artgruDtoI) {
    artgruDto = artgruDtoI;
  }
}
