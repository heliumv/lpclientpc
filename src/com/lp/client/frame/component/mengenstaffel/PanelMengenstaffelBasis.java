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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.EventObject;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access") 
/**
 * <p>Basiselemente fuer die Erfassung von Mengenstaffeln fuer Artikel oder
 * Artikelgruppen.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 07.07.2006</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.10 $
 */
public abstract class PanelMengenstaffelBasis
    extends PanelBasis implements PropertyChangeListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected WrapperLabel wlaMenge = null;
  protected WrapperLabel wlaRabattsatz = null;

  protected WrapperNumberField wnfMenge = null;
  protected WrapperLabel wlaMengeeinheit = null;
  protected WrapperNumberField wnfRabattsatz = null;
  protected WrapperLabel wlaRabattsatzprozent = null;

  // Gueltigkeitsbereich der Mengenstaffel von, bis
  protected WrapperLabel wlaGueltigab = null;
  protected WrapperDateField wdfGueltigab = null;

  protected WrapperDateField wdfGueltigbis = null;
  protected WrapperLabel wlaGueltigbis = null;

  /** Die Anzahl der Nachkommastellen fuer Preisfelder kommt aus Mandantparameter. */
  protected final int iPreiseUINachkommastellen;
  /** Die Anzahl der Nachkommastellen fuer Mengenfelder kommt aus Mandantparameter. */
  protected final int iMengeUINachkommastellen;

  protected String waehrungCNr;

  public String getWaehrungCNr() {
	return waehrungCNr;
  }


  public void setWaehrungCNr(String waehrungCNr) {
	this.waehrungCNr = waehrungCNr;
  }
  
  
  protected Integer kundenpreislisteIId;

public Integer getKundenpreislisteIId() {
	return kundenpreislisteIId;
}


public void setKundenpreislisteIId(Integer kundenpreislisteIId) {
	this.kundenpreislisteIId = kundenpreislisteIId;
}


protected int iBreiteErsteSpalte = 60;

  // eine unsichbare Zeile traegt die Formatierung
  private WrapperLabel wlaFormat0 = null;
  private WrapperLabel wlaFormat1 = null;
  private WrapperLabel wlaFormat2 = null;
  private WrapperLabel wlaFormat3 = null;
  private WrapperLabel wlaFormat4 = null;
  private WrapperLabel wlaFormat5 = null;
  private WrapperLabel wlaFormat6 = null;
  private WrapperLabel wlaFormat7 = null;

  // optionale Felder fuer Artikel und Artikelgruppe
  protected WrapperLabel wlaBemerkung = null;
  protected WrapperTextField wtfBemerkung = null;

  protected WrapperCheckBox wcbDrucken = null;
  protected WrapperCheckBox wcbBemerkungDrucken = null;
  protected WrapperCheckBox wcbRabattsichtbar = null;
  
  protected InternalFrameKunde internalFrameKunde = null;

  public PanelMengenstaffelBasis(InternalFrame internalFrameI, String addTitle2I)
      throws Throwable {
    super(internalFrameI, addTitle2I);
    internalFrameKunde = (InternalFrameKunde)internalFrameI;
    iPreiseUINachkommastellen = Defaults.getInstance().getIUINachkommastellenPreiseVK();
    iMengeUINachkommastellen = Defaults.getInstance().getIUINachkommastellenMenge();
    waehrungCNr = LPMain.getTheClient().getSMandantenwaehrung();
    jbInit();
  }


  private void jbInit()
      throws Throwable {
    setLayout(new GridBagLayout());
  }

  public void setMandatoryFields(boolean bMandatoryI) {

  }


  protected void setDefaults()
      throws Throwable {
    Date date = Helper.cutDate(new Date(System.currentTimeMillis()));

    if (wdfGueltigab != null) {
      wdfGueltigab.setDate(date);
    }

    if (wnfMenge != null) {
      wnfMenge.setBigDecimal(new BigDecimal(1));
    }
  }

  protected void addFormatierungszeile()
      throws Throwable {
    wlaFormat0 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat0, iBreiteErsteSpalte);
    wlaFormat1 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat1, 30); // fuer die Mengeneinheit
    wlaFormat2 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat2, 60); // fuer einen Preis
    wlaFormat3 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat3, 25); // fuer eine Waehrung
    wlaFormat4 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat4, 60); // Rabattsatz
    wlaFormat5 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat5, 15); // % Zeichen
    wlaFormat6 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat6, 60); // fuer einen Preis
    wlaFormat7 = new WrapperLabel();
    HelperClient.setDefaultsToComponent(wlaFormat7, 25); // fuer eine Waehrung

    add(wlaFormat0,
        new GridBagConstraints(0, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat1,
        new GridBagConstraints(1, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat2,
        new GridBagConstraints(2, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat3,
        new GridBagConstraints(3, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat4,
        new GridBagConstraints(4, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat5,
        new GridBagConstraints(5, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat6,
        new GridBagConstraints(6, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaFormat7,
        new GridBagConstraints(7, iZeile, 1, 0, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
  }

  protected void addZeileGueltigVonBis(int iZeileI)
      throws Throwable {
    wlaGueltigab = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("lp.gueltigab"));
    wdfGueltigab = new WrapperDateField();
    wdfGueltigab.setMandatoryField(true);
    wdfGueltigab.getDisplay().addPropertyChangeListener(this);
    wlaGueltigbis = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("lp.gueltigbis"));
    wdfGueltigbis = new WrapperDateField();

    add(wlaGueltigab,
        new GridBagConstraints(0, iZeileI, 2, 1, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wdfGueltigab,
        new GridBagConstraints(2, iZeileI, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wlaGueltigbis,
        new GridBagConstraints(4, iZeileI, 2, 1, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
    add(wdfGueltigbis,
        new GridBagConstraints(6, iZeileI, 1, 1, 0.0, 0.0
                               , GridBagConstraints.NORTH,
                               GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
  }

  protected void initMengeRabattsatz()
      throws Throwable {
    wlaMenge = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("lp.menge"));
    wlaMenge.setHorizontalAlignment(SwingConstants.LEADING);
    wlaMenge.setVerticalAlignment(SwingConstants.BOTTOM);

    wlaRabattsatz = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.rabattsumme"));
    wlaRabattsatz.setHorizontalAlignment(SwingConstants.LEADING);
    wlaRabattsatz.setVerticalAlignment(SwingConstants.BOTTOM);

    wnfMenge = new WrapperNumberField();
    wnfMenge.setFractionDigits(iMengeUINachkommastellen);
    HelperClient.setDefaultsToComponent(wnfMenge, iBreiteErsteSpalte);
//    wnfMenge.setMinimumValue(new BigDecimal(0.001));
    wnfMenge.setMinimumValue(BigDecimal.valueOf(0.001));
    wlaMengeeinheit = new WrapperLabel();
    wlaMengeeinheit.setHorizontalAlignment(SwingConstants.LEADING);

    wnfRabattsatz = new WrapperNumberField();
    wnfRabattsatz.setFractionDigits(2); // 2 Nachkommastellen
    wnfRabattsatz.setDependenceField(true);


    wlaRabattsatzprozent = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("label.prozent"));
    wlaRabattsatzprozent.setHorizontalAlignment(SwingConstants.LEADING);
  }

  public void addZeilenBemerkungUndOptionen(int iZeileI)
      throws Throwable {
    wlaBemerkung = new WrapperLabel(
        LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
    wtfBemerkung = new WrapperTextField(40);

    wcbDrucken = new WrapperCheckBox(
        LPMain.getInstance().getTextRespectUISPr("kunde.soko.drucken"));
    wcbBemerkungDrucken = new WrapperCheckBox(
        LPMain.getInstance().getTextRespectUISPr("kunde.soko.bemdrucken"));
    wcbRabattsichtbar = new WrapperCheckBox(
        LPMain.getInstance().getTextRespectUISPr("kunde.soko.rabattsichtbar"));

      add(wlaBemerkung,
          new GridBagConstraints(0, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(4, 2, 2, 2), 0, 0));
      add(wtfBemerkung,
          new GridBagConstraints(2, iZeileI, 7, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(4, 2, 2, 2), 0, 0));

      iZeileI++;
      add(wcbDrucken,
          new GridBagConstraints(2, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(0, 2, 2, 2), 0, 0));
      add(wcbRabattsichtbar,
          new GridBagConstraints(4, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(0, 2, 2, 2), 0, 0));
      add(wcbBemerkungDrucken,
          new GridBagConstraints(6, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(0, 2, 2, 2), 0, 0));

      iZeileI++;
      add(wlaGueltigab,
          new GridBagConstraints(0, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(2, 2, 2, 2), 0, 0));
      add(wdfGueltigab,
          new GridBagConstraints(2, iZeileI, 1, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(2, 2, 2, 2), 0, 0));
      add(wlaGueltigbis,
          new GridBagConstraints(4, iZeileI, 2, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                                 new Insets(2, 2, 2, 2), 0, 0));
      add(wdfGueltigbis,
          new GridBagConstraints(6, iZeileI, 1, 1, 0.0, 0.0
                                 , GridBagConstraints.NORTH,
                                 GridBagConstraints.HORIZONTAL,
                               new Insets(2, 2, 2, 2), 0, 0));
  }

  public void propertyChange(PropertyChangeEvent evt) {
    try {
      if (evt.getSource() == wdfGueltigab.getDisplay() &&
               evt.getPropertyName().equals("date")) {
        wdfGueltigbis.setMinimumValue(wdfGueltigab.getDate());

        // automatisch korrigieren
        if (wdfGueltigab.getDate() != null && wdfGueltigbis.getDate() != null) {
          if (wdfGueltigab.getDate().getTime() > wdfGueltigbis.getDate().getTime()) {
            wdfGueltigbis.setDate(wdfGueltigab.getDate());
          }
        }
      }
    }
    catch (Throwable t) {
      DialogFactory.showModalDialog(
          LPMain.getInstance().getTextRespectUISPr("lp.error"),
          LPMain.getInstance().getTextRespectUISPr(
              "vkpf.error.preisgueltigkeitsanzeigeab"));
    }
  }

  public KundesokoDto components2kundesokoDto(KundesokoDto kundesokoDtoI) throws Throwable {
    if (kundesokoDtoI.getKundeIId() == null) {
      throw new ExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIId == null"));
    }

    kundesokoDtoI.setTPreisgueltigab(wdfGueltigab.getDate());
    kundesokoDtoI.setTPreisgueltigbis(wdfGueltigbis.getDate());
    kundesokoDtoI.setCBemerkung(wtfBemerkung.getText());
    kundesokoDtoI.setBDrucken(Helper.boolean2Short(wcbDrucken.isSelected()));
    kundesokoDtoI.setBBemerkungdrucken(
        Helper.boolean2Short(wcbBemerkungDrucken.isSelected()));
    kundesokoDtoI.setBRabattsichtbar(
        Helper.boolean2Short(wcbRabattsichtbar.isSelected()));

    return kundesokoDtoI;
  }

  public void kundesokoDto2components(KundesokoDto kundesokoDtoI)
      throws Throwable {
    wdfGueltigab.setDate(kundesokoDtoI.getTPreisgueltigab());
    wdfGueltigbis.setDate(kundesokoDtoI.getTPreisgueltigbis());
    wtfBemerkung.setText(kundesokoDtoI.getCBemerkung());

    if (kundesokoDtoI.getBDrucken() == null) {
      wcbDrucken.setSelected(false);
    }
    else {
      wcbDrucken.setSelected(Helper.short2boolean(kundesokoDtoI.getBDrucken()));
    }

    if (kundesokoDtoI.getBBemerkungdrucken() == null) {
      wcbBemerkungDrucken.setSelected(false);
    }
    else {
      wcbBemerkungDrucken.setSelected(
          Helper.short2boolean(kundesokoDtoI.getBBemerkungdrucken()));
    }

    if (kundesokoDtoI.getBRabattsichtbar() == null) {
      wcbRabattsichtbar.setSelected(false);
    }
    else {
      wcbRabattsichtbar.setSelected(
          Helper.short2boolean(kundesokoDtoI.getBRabattsichtbar()));
    }
  }

  public WrapperDateField getWdfGueltigab() {
    return wdfGueltigab;
  }

  public WrapperDateField getWdfGueltigbis() {
    return wdfGueltigbis;
  }
  
  protected void eventItemchanged(EventObject eI)
  	throws Throwable {
	  /*	
	waehrungCNr = internalFrameKunde.getTpKunde().getKundeDto().getCWaehrung();
	if(LPMain.getTheClient().getSMandantenwaehrung().equals(waehrungCNr)){
		System.out.println();
	}
  
	  if(kundeIId != null){
	  KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);
	  waehrungCNr = kundeDto.getCWaehrung();
	   }
	*/ 
  }
   
  public BigDecimal berechnewertInKundenWaehrung(BigDecimal wert) throws ExceptionLP, Throwable{
	  	if(wert == null)
	  		return null;
	  	String  mandantenwaehrungCNr = LPMain.getTheClient().getSMandantenwaehrung();
	  	if(!mandantenwaehrungCNr.equals(waehrungCNr)){
	  	BigDecimal fKurs = DelegateFactory.getInstance().getLocaleDelegate().getWechselkurs2(
				waehrungCNr,
				mandantenwaehrungCNr);
		// den Kurs auf 6 Stellen runden
		BigDecimal bdKurs = fKurs.setScale(
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);

		wert = wert.divide(
				bdKurs, iPreiseUINachkommastellen,
				BigDecimal.ROUND_HALF_EVEN);
	  	}
	  return wert;
  }

  public BigDecimal berechnewertInMandantenWaehrung(BigDecimal wert) throws ExceptionLP, Throwable{
	  	if(wert == null)
	  		return null;
	  	String  mandantenwaehrungCNr = LPMain.getTheClient().getSMandantenwaehrung();
	  	if(!mandantenwaehrungCNr.equals(waehrungCNr)){
	  	BigDecimal fKurs = DelegateFactory.getInstance().getLocaleDelegate().getWechselkurs2(
	  			mandantenwaehrungCNr,
				waehrungCNr);
		// den Kurs auf 6 Stellen runden
		BigDecimal bdKurs = fKurs.setScale(
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);

		wert = wert.divide(
				bdKurs, iPreiseUINachkommastellen,
				BigDecimal.ROUND_HALF_EVEN);
	  	}
	  return wert;
}

}
