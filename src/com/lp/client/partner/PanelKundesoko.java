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
package com.lp.client.partner;


import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.mengenstaffel.PanelMengenstaffel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;


/**
 * <p>In diesem Fenster wird eine Kundensonderkondition fuer einen bestimmten Zeitraum erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 19.06.2006</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelKundesoko
    extends PanelMengenstaffel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected InternalFrameKunde internalFrameKunde = null;

  protected KundesokoDto kundesokoDto = null;
  protected KundesokomengenstaffelDto mengenstaffelDto = null;

  public PanelKundesoko(InternalFrame internalFrame, String add2TitleI, Object key)
      throws Throwable {
    super(internalFrame, add2TitleI, key,true);

    internalFrameKunde = (InternalFrameKunde) internalFrame;

    jbInit();
    initComponents();
  }


  void jbInit()
      throws Throwable {
    panelArtikel.addZeilenBemerkungUndOptionen(8);
    panelArtgru.addZeilenBemerkungUndOptionen(8);
  }


  protected void setDefaults()
      throws Throwable {
    super.setDefaults();

    kundesokoDto = new KundesokoDto();
    mengenstaffelDto = new KundesokomengenstaffelDto();
  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws
      Throwable {
    if (allMandatoryFieldsSetDlg()) {
      kundesokoDto.setKundeIId(internalFrameKunde.getKundeDto().getIId());

      if (wcoArt.getKeyOfSelectedItem().equals(ART_ARTIKEL)) {
        // bei Verlassen des Dialogs mit Strg+S
        panelArtikel.preisSetzen();

        kundesokoDto =
            panelArtikel.components2kundesokoDto(kundesokoDto);
        kundesokoDto.setCKundeartikelnummer(wtfKundeartikelnummer.getText());
        mengenstaffelDto =
            panelArtikel.components2mengenstaffelDto(
                mengenstaffelDto);
      }
      else { // Artikelgruppe
        kundesokoDto =
            panelArtgru.components2kundesokoDto(kundesokoDto);

        mengenstaffelDto =
            panelArtgru.components2mengenstaffelDto(
                mengenstaffelDto);
      }

      if (kundesokoDto.getIId() == null) {
        Integer kundesokoIId =
            DelegateFactory.getInstance().getKundesokoDelegate().createKundesoko(
                kundesokoDto, mengenstaffelDto);
        kundesokoDto =
            DelegateFactory.getInstance().getKundesokoDelegate().
            kundesokoFindByPrimaryKey(kundesokoIId);
        setKeyWhenDetailPanel(kundesokoIId);

        KundesokomengenstaffelDto[] aKundesokomengenstaffelDtos =
            DelegateFactory.getInstance().getKundesokoDelegate().
            kundesokomengenstaffelFindByKundesokoIId(kundesokoDto.getIId());

        if (aKundesokomengenstaffelDtos != null && aKundesokomengenstaffelDtos.length > 0) {
          mengenstaffelDto = aKundesokomengenstaffelDtos[0];
        }
      }
      else {
        DelegateFactory.getInstance().getKundesokoDelegate().updateKundesoko(kundesokoDto,
            mengenstaffelDto);
      }

      // buttons schalten
      super.eventActionSave(e, true);

      eventYouAreSelected(false);
    }
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws
      Throwable {
    super.eventActionNew(eventObject, true, false);

    setDefaults();

  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {
    super.eventYouAreSelected(false);

    // neu einlesen, ausloeser war ev. ein refresh
    Object key = getKeyWhenDetailPanel();

    // zuerst alles zuruecksetzen, ausloeser war ev. ein discard
    setDefaults();

    if (key != null && !key.equals(LPMain.getLockMeForNew())) {
      kundesokoDto = DelegateFactory.getInstance().getKundesokoDelegate().
          kundesokoFindByPrimaryKey( (Integer) key);

      KundesokomengenstaffelDto[] aDtos = DelegateFactory.getInstance().
          getKundesokoDelegate().
          kundesokomengenstaffelFindByKundesokoIId( (Integer) key);

      if (aDtos != null && aDtos.length > 0) {
        mengenstaffelDto = aDtos[0];
      }

      if (kundesokoDto.getArtikelIId() != null) {
        wcoArt.setKeyOfSelectedItem(ART_ARTIKEL);

        panelArtikel.kundesokoDto2components(kundesokoDto);
        panelArtikel.artikelDto2Components(
            DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(
                kundesokoDto.getArtikelIId()));
        panelArtikel.mengenstaffelDto2components(mengenstaffelDto);
        wtfKundeartikelnummer.setText(kundesokoDto.getCKundeartikelnummer());
      }
      else {
        wcoArt.setKeyOfSelectedItem(ART_ARTIKELGRUPPE);

        panelArtgru.kundesokoDto2components(kundesokoDto);
        panelArtgru.artgruDto2components(kundesokoDto.getArtgruIId());
        panelArtgru.mengenstaffelDto2components(
            mengenstaffelDto);
      }

      aktualisiereStatusbar();
    } else {
      wcoArt.setKeyOfSelectedItem(ART_ARTIKELGRUPPE);
    }

    String sTitel =
        internalFrameKunde.getKundeDto().getPartnerDto().formatFixTitelName1Name2();

    if (wcoArt.getKeyOfSelectedItem().equals(ART_ARTIKEL)) {
      sTitel += panelArtikel.baueTitel();
    }
    else {
      sTitel += panelArtgru.baueTitel();
    }

    getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitel);
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_KUNDESOKO;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return null;
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    if (kundesokoDto != null && kundesokoDto.getIId() != null) {
      DelegateFactory.getInstance().getKundesokoDelegate().removeKundesoko(kundesokoDto);
    }

    super.eventActionDelete(e, false, false); // keyWasForLockMe nicht ueberschreiben
  }


  protected void aktualisiereStatusbar()
      throws Throwable {
    setStatusbarPersonalIIdAendern(kundesokoDto.getPersonalIIdAendern());
    setStatusbarTAendern(kundesokoDto.getTAendern());
  }

  public KundesokoDto getKundesokoDto() {
    return kundesokoDto;
  }
}
