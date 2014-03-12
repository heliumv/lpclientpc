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
package com.lp.client.lieferschein;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.BegruendungDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden Konditionen zum Lieferschein erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-04-10</p>
 * <p> </p>
 * @author uli walch
 */
public class PanelLieferscheinKonditionen2 extends PanelKonditionen implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private TabbedPaneLieferschein tpLieferschein = null;

	private WrapperLabel wlaPakete = null;

	private WrapperNumberField wnfPakete = null;

	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;

	private WrapperLabel wlaGewicht = null;

	private WrapperNumberField wnfGewicht = null;

	private WrapperLabel wlaEinheit = null;

	private WrapperLabel wlaRueckgabetermin = null;
	private WrapperDateField wdfRueckgabetermin = null;

	private WrapperLabel wlaVersandnummer = null;

	private WrapperTextField wtfVersandnummer = null;

	private WrapperCheckBox wcbMindermengenzuschlag = null;

	private WrapperLabel wlaVersteckterAufschlag = null;

	private WrapperNumberField wnfVersteckterAufschlag = null;
	private WrapperCheckBox wcbVerrechenbar = null;

	private WrapperLabel wlaProzent3 = null;

	private WrapperLabel wlaLieferscheinwertInMandantenwaehrung = null;

	private WrapperLabel wlaMandantenwaehrung0 = null;

	private WrapperLabel wlaGestehungswertInMandantenwaehrung = null;

	private WrapperLabel wlaMandantenwaehrung1 = null;

	private WrapperNumberField wnfLieferscheinwertInMandantenwaehrung = null;

	private WrapperNumberField wnfGestehungswertInMandantenwaehrung = null;

	private WrapperLabel wlaLieferscheinwertInLieferscheinwaehrung = null;

	private WrapperLabel wlaLieferscheinwaehrung = null;

	private WrapperNumberField wnfLieferscheinwertInLieferscheinwaehrung = null;

	private WrapperButton wbuBegruendung = new WrapperButton();
	private WrapperTextField wtfBegruendung = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRBegruendung = null;
	public final static String ACTION_SPECIAL_BEGRUENDUNG = "action_special_begruendung";

	public PanelLieferscheinKonditionen2(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {

		super(internalFrame, add2TitleI, key);
		tpLieferschein = ((InternalFrameLieferschein) getInternalFrame())
				.getTabbedPaneLieferschein();
		jbInit();
		initComponents();
		setDarfPreiseSehenSichtbarkeit();
	}

	private void jbInit() throws Throwable {
		wtfLieferart.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);

		remove(wlaAllgemeinerRabatt);
		remove(wnfAllgemeinerRabatt);
		remove(wlaProzent2);

		wlaPakete = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"ls.pakete"));
		wnfPakete = new WrapperNumberField();
		wnfPakete.setFractionDigits(0);
		wnfPakete.setMinimumValue(0);
		wlaLiefertermin = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.auslieferdatum"));

		wdfLiefertermin = new WrapperDateField();
		// wdfLiefertermin.setDate(datCurrentDate);
		// wdfLiefertermin.setMinimumValue(datCurrentDate);
		wdfLiefertermin.setMandatoryFieldDB(false);
		wdfLiefertermin.setShowRubber(false);
		wdfLiefertermin.getDisplay().addPropertyChangeListener(this);

		wlaGewicht = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaEinheit, 25);
		wlaRueckgabetermin = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.rueckgabetermin"));
		HelperClient.setDefaultsToComponent(wlaRueckgabetermin, 90);
		wdfRueckgabetermin = new WrapperDateField();
		wdfRueckgabetermin.getDisplay().addPropertyChangeListener(this);

		wbuBegruendung.setText(LPMain.getInstance().getTextRespectUISPr(
				"ls.begruendung")
				+ "...");
		wbuBegruendung.setActionCommand(ACTION_SPECIAL_BEGRUENDUNG);
		wbuBegruendung.addActionListener(this);
		wtfBegruendung.setColumnsMax(80);
		wtfBegruendung.setActivatable(false);

		wlaVersandnummer = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("ls.versandnummer"));
		wtfVersandnummer = new WrapperTextField();
		wtfVersandnummer.setColumnsMax(40);

		wlaVersteckterAufschlag = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));
		wnfVersteckterAufschlag = new WrapperNumberField();
		wlaProzent3 = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaProzent3.setHorizontalAlignment(SwingConstants.LEFT);

		wcbMindermengenzuschlag = new WrapperCheckBox();
		wcbMindermengenzuschlag.setMargin(new Insets(2, 0, 2, 2));
		wcbMindermengenzuschlag.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.mindermengenzuschlag"));

		wcbVerrechenbar = new WrapperCheckBox();
		wcbVerrechenbar.setMargin(new Insets(2, 0, 2, 2));
		wcbVerrechenbar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verrechenbar"));

		wlaLieferscheinwertInMandantenwaehrung = new WrapperLabel(LPMain
				.getInstance().getTextRespectUISPr("ls.lieferscheinwert"));

		wlaMandantenwaehrung0 = new WrapperLabel();
		wlaMandantenwaehrung0.setHorizontalAlignment(SwingConstants.LEFT);

		wlaGestehungswertInMandantenwaehrung = new WrapperLabel(LPMain
				.getInstance().getTextRespectUISPr("lp.gestehungswert"));

		wlaMandantenwaehrung1 = new WrapperLabel();
		wlaMandantenwaehrung1.setHorizontalAlignment(SwingConstants.LEFT);

		wnfLieferscheinwertInMandantenwaehrung = new WrapperNumberField();
		wnfLieferscheinwertInMandantenwaehrung.setActivatable(false);
		wnfLieferscheinwertInMandantenwaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseVK());

		wnfGestehungswertInMandantenwaehrung = new WrapperNumberField();
		wnfGestehungswertInMandantenwaehrung.setActivatable(false);
		wnfGestehungswertInMandantenwaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseVK());
		wlaLieferscheinwertInLieferscheinwaehrung = new WrapperLabel(LPMain
				.getInstance().getTextRespectUISPr("ls.lieferscheinwert"));
		HelperClient.setDefaultsToComponent(
				wlaLieferscheinwertInLieferscheinwaehrung, 90);

		wlaLieferscheinwaehrung = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaLieferscheinwaehrung, 25);

		wnfLieferscheinwertInLieferscheinwaehrung = new WrapperNumberField();
		wnfLieferscheinwertInLieferscheinwaehrung.setActivatable(false);
		wnfLieferscheinwertInLieferscheinwaehrung.setFractionDigits(Defaults
				.getInstance().getIUINachkommastellenPreiseVK());

		iZeile = 3; // mein eigener Teil beginnt nach den ersten drei Zeilen des
					// BasisPanel
		/*jPanelWorkingOn.add(wbuBegruendung, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));*/ //Wird nicht benoetigt da
		//im Menue bereits vorhanden
		jPanelWorkingOn.add(wtfBegruendung, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;

		jPanelWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLiefertermin, new GridBagConstraints(3, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfLiefertermin, new GridBagConstraints(4, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaRueckgabetermin, new GridBagConstraints(3,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfRueckgabetermin, new GridBagConstraints(4,
				iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaVersteckterAufschlag, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfVersteckterAufschlag, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent3, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbMindermengenzuschlag, new GridBagConstraints(3,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaAllgemeinerRabatt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAllgemeinerRabatt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbVerrechenbar, new GridBagConstraints(3, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaLieferscheinwertInMandantenwaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfLieferscheinwertInMandantenwaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung0, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLieferscheinwertInLieferscheinwaehrung,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfLieferscheinwertInLieferscheinwaehrung,
				new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLieferscheinwaehrung, new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaGestehungswertInMandantenwaehrung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGestehungswertInMandantenwaehrung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung1, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	private void setDarfPreiseSehenSichtbarkeit() {

		// Darf Preise Sehen Recht
		if (!getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			this.setVisibleAllgemeinerRabatt(false);
			// this.setVisibleZahlungsziel(false);

			wcoMehrwertsteuer.setVisible(false);
			wlaMehrwertsteuer.setVisible(false);
			wlaVersteckterAufschlag.setVisible(false);
			wnfVersteckterAufschlag.setVisible(false);
			wlaProzent3.setVisible(false);
			wcbMindermengenzuschlag.setVisible(false);

			wcbVerrechenbar.setVisible(false);
			wlaLieferscheinwertInMandantenwaehrung.setVisible(false);
			wlaMandantenwaehrung0.setVisible(false);
			wlaGestehungswertInMandantenwaehrung.setVisible(false);
			wlaMandantenwaehrung1.setVisible(false);

			wnfLieferscheinwertInMandantenwaehrung.setVisible(false);
			wnfGestehungswertInMandantenwaehrung.setVisible(false);
			wlaLieferscheinwertInLieferscheinwaehrung.setVisible(false);
			wlaLieferscheinwaehrung.setVisible(false);
			wnfLieferscheinwertInLieferscheinwaehrung.setVisible(false);
		}

	}

	/**
	 * Default Werte im Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

		wcbMindermengenzuschlag.setSelected(false);

		wcbVerrechenbar.setSelected(true);

		wnfPakete.setInteger(0);
		wnfGewicht.setDouble(new Double(0));

		String cNrMandantenwaehrung = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();

		wlaMandantenwaehrung0.setText(cNrMandantenwaehrung);
		wlaMandantenwaehrung1.setText(cNrMandantenwaehrung);

		wlaLieferscheinwaehrung.setText(tpLieferschein.getLieferscheinDto()
				.getWaehrungCNr());

		wefKopftext.setDefaultText(tpLieferschein.getKopftextDto()
				.getCTextinhalt());
		wefFusstext.setDefaultText(tpLieferschein.getFusstextDto()
				.getCTextinhalt());
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	/**
	 * Einen existierenden Lieferschein zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	public void dto2Components() throws Throwable {
		wnfVersteckterAufschlag.setDouble(tpLieferschein.getLieferscheinDto()
				.getFVersteckterAufschlag());
		wnfAllgemeinerRabatt.setDouble(tpLieferschein.getLieferscheinDto()
				.getFAllgemeinerRabattsatz());

		holeLieferart(tpLieferschein.getLieferscheinDto().getLieferartIId());
		holeZahlungsziel(tpLieferschein.getLieferscheinDto()
				.getZahlungszielIId());
		holeSpediteur(tpLieferschein.getLieferscheinDto().getSpediteurIId());

		wtfLieferartort.setText(tpLieferschein.getLieferscheinDto().getCLieferartort());
		
		wcbMindermengenzuschlag.setSelected(Helper.short2boolean(tpLieferschein
				.getLieferscheinDto().getBMindermengenzuschlag()));
		wcbVerrechenbar.setSelected(Helper.short2boolean(tpLieferschein
				.getLieferscheinDto().getBVerrechenbar()));
		wnfPakete.setDouble(new Double(tpLieferschein.getLieferscheinDto()
				.getIAnzahlPakete().doubleValue()));
		wnfGewicht.setDouble(tpLieferschein.getLieferscheinDto()
				.getFGewichtLieferung());
		wtfVersandnummer.setText(tpLieferschein.getLieferscheinDto()
				.getCVersandnummer());
		if (tpLieferschein.getLieferscheinDto().getTLiefertermin() != null) {
			wdfLiefertermin.setDate(tpLieferschein.getLieferscheinDto()
					.getTLiefertermin());
		}
		if (tpLieferschein.getLieferscheinDto().getTRueckgabetermin() != null) {
			wdfRueckgabetermin.setDate(Helper.extractDate(tpLieferschein
					.getLieferscheinDto().getTRueckgabetermin()));
		}

		// die werte des lieferscheins anzeigen
		BigDecimal bdLswertInLswaehrung = tpLieferschein.getLieferscheinDto()
				.getNGesamtwertInLieferscheinwaehrung();
		BigDecimal bdLswertInMandantenwaehrung = null;
		BigDecimal bdGestehungswertInMandantenwaehrung = tpLieferschein
				.getLieferscheinDto().getNGestehungswertInMandantenwaehrung();

		if (bdLswertInLswaehrung == null
				|| tpLieferschein.getLieferscheinDto().getStatusCNr()
						.equals(LieferscheinFac.LSSTATUS_STORNIERT)) {
			bdLswertInLswaehrung = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.berechneGesamtwertLieferschein(
							tpLieferschein.getLieferscheinDto().getIId());
		}

		bdLswertInMandantenwaehrung = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.rechneUmInAndereWaehrung(
						bdLswertInLswaehrung,
						tpLieferschein.getLieferscheinDto()
								.getWaehrungCNr(),
						LPMain.getTheClient().getSMandantenwaehrung());

		if (bdGestehungswertInMandantenwaehrung == null) {
			bdGestehungswertInMandantenwaehrung = DelegateFactory
					.getInstance()
					.getLsDelegate()
					.berechneGestehungswert(
							tpLieferschein.getLieferscheinDto().getIId());
		}

		wnfLieferscheinwertInMandantenwaehrung
				.setBigDecimal(bdLswertInMandantenwaehrung);
		wnfGestehungswertInMandantenwaehrung
				.setBigDecimal(bdGestehungswertInMandantenwaehrung);
		wnfLieferscheinwertInLieferscheinwaehrung
				.setBigDecimal(bdLswertInLswaehrung);

		// Kopftext fuer diesen Lieferschein in der Sprache des Kunden anzeigen
		if (tpLieferschein.getLieferscheinDto()
				.getCLieferscheinKopftextUeberschrieben() != null) {
			wefKopftext.setText(tpLieferschein.getLieferscheinDto()
					.getCLieferscheinKopftextUeberschrieben());
		} else {
			wefKopftext.setText(tpLieferschein.getDefaultKopftext(
					tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
							.getLocaleCNrKommunikation()).getCTextinhalt());
		}

		// Fusstext fuer diesen Lieferschein in der Sprache des Kunden anzeigen
		if (tpLieferschein.getLieferscheinDto()
				.getCLieferscheinFusstextUeberschrieben() != null) {
			wefFusstext.setText(tpLieferschein.getLieferscheinDto()
					.getCLieferscheinFusstextUeberschrieben());
		} else {
			wefFusstext.setText(tpLieferschein.getDefaultFusstext(
					tpLieferschein.getKundeLieferadresseDto().getPartnerDto()
							.getLocaleCNrKommunikation()).getCTextinhalt());
		}

		if(tpLieferschein.getLieferscheinDto().getBegruendungIId()!=null){
			BegruendungDto begruendungDto = DelegateFactory.getInstance()
					.getLieferscheinServiceDelegate()
					.begruendungFindByPrimaryKey(tpLieferschein.getLieferscheinDto().getBegruendungIId());
			wtfBegruendung.setText(begruendungDto.getBezeichnung());
		} else {
			wtfBegruendung.setText(null);
		}
		aktualisiereStatusbar();
	}

	private void components2Dto() throws Throwable {
		tpLieferschein.getLieferscheinDto().setFVersteckterAufschlag(
				wnfVersteckterAufschlag.getDouble());
		tpLieferschein.getLieferscheinDto().setFAllgemeinerRabattsatz(
				wnfAllgemeinerRabatt.getDouble());
		tpLieferschein.getLieferscheinDto().setLieferartIId(
				lieferartDto.getIId());
		tpLieferschein.getLieferscheinDto().setZahlungszielIId(
				zahlungszielDto.getIId());
		tpLieferschein.getLieferscheinDto().setSpediteurIId(
				spediteurDto.getIId());
		tpLieferschein.getLieferscheinDto().setCVersandnummer(
				wtfVersandnummer.getText());
		tpLieferschein.getLieferscheinDto().setBMindermengenzuschlag(
				Helper.boolean2Short(wcbMindermengenzuschlag.isSelected()));
		tpLieferschein.getLieferscheinDto().setBVerrechenbar(
				Helper.boolean2Short(wcbVerrechenbar.isSelected()));
		tpLieferschein.getLieferscheinDto().setIAnzahlPakete(
				new Integer(wnfPakete.getDouble().intValue()));
		tpLieferschein.getLieferscheinDto().setFGewichtLieferung(
				wnfGewicht.getDouble());
		tpLieferschein.getLieferscheinDto().setCLieferartort(wtfLieferartort.getText());
		if (wdfLiefertermin.getDate() != null) {
			tpLieferschein.getLieferscheinDto().setTLiefertermin(
					new Timestamp(this.wdfLiefertermin.getDate().getTime()));
		} else {
			tpLieferschein.getLieferscheinDto().setTLiefertermin(null);
		}
		if (wdfRueckgabetermin.getDate() != null) {
			tpLieferschein.getLieferscheinDto().setTRueckgabetermin(
					new Timestamp(this.wdfRueckgabetermin.getDate().getTime()));
		} else {
			tpLieferschein.getLieferscheinDto().setTRueckgabetermin(null);
		}
		tpLieferschein.getLieferscheinDto()
				.setLieferscheintextIIdDefaultKopftext(
						tpLieferschein.getKopftextDto().getIId());

		// wenn der Kopftext nicht ueberschrieben wurde -> null setzen
		if (wefKopftext.getText() != null
				&& wefKopftext.getText().equals(
						tpLieferschein.getDefaultKopftext(
								tpLieferschein.getKundeLieferadresseDto()
										.getPartnerDto()
										.getLocaleCNrKommunikation())
								.getCTextinhalt())) {
			tpLieferschein.getLieferscheinDto()
					.setCLieferscheinKopftextUeberschrieben(null);
		} else {
			tpLieferschein.getLieferscheinDto()
					.setCLieferscheinKopftextUeberschrieben(
							wefKopftext.getText());
		}

		tpLieferschein.getLieferscheinDto()
				.setLieferscheintextIIdDefaultFusstext(
						tpLieferschein.getFusstextDto().getIId());

		// wenn der Fusstext nicht ueberschrieben wurde -> null setzen
		if (wefFusstext.getText() != null
				&& wefFusstext.getText().equals(
						tpLieferschein.getDefaultFusstext(
								tpLieferschein.getKundeLieferadresseDto()
										.getPartnerDto()
										.getLocaleCNrKommunikation())
								.getCTextinhalt())) {
			tpLieferschein.getLieferscheinDto()
					.setCLieferscheinFusstextUeberschrieben(null);
		} else {
			tpLieferschein.getLieferscheinDto()
					.setCLieferscheinFusstextUeberschrieben(
							wefFusstext.getText());
		}
	}

	public void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_BEGRUENDUNG)) {
			panelQueryFLRBegruendung = LieferscheinFilterFactory.getInstance()
					.createPanelFLRBegruendung(getInternalFrame(), tpLieferschein.getLieferscheinDto().getBegruendungIId(), true);
			new DialogQuery(panelQueryFLRBegruendung);
		}
	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBegruendung) {
				Integer key = (Integer) panelQueryFLRBegruendung
						.getSelectedId();
				tpLieferschein.getLieferscheinDto().setBegruendungIId(key);
				BegruendungDto begruendungDto = DelegateFactory.getInstance()
						.getLieferscheinServiceDelegate()
						.begruendungFindByPrimaryKey(key);
				wtfBegruendung.setText(begruendungDto.getBezeichnung());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBegruendung) {
				tpLieferschein.getLieferscheinDto().setBegruendungIId(null);
				wtfBegruendung.setText(null);
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			// wenn der Lieferschein auftragsbezogen ist und der Auftrag keine
			// Teillieferung erlaubt...
			if (tpLieferschein.getLieferscheinDto().getLieferscheinartCNr()
					.equals(LieferscheinFac.LSART_AUFTRAG)) {
				AuftragDto auftragDto = DelegateFactory
						.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(
								tpLieferschein.getLieferscheinDto()
										.getAuftragIId());

				if (!Helper.short2boolean(auftragDto
						.getBTeillieferungMoeglich())) {
					MessageFormat mf = new MessageFormat(LPMain.getInstance()
							.getTextRespectUISPr("ls.hint.keineteillieferung"));

					mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());

					Object pattern[] = { auftragDto.getCNr() };

					DialogFactory
							.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.hint"), mf
									.format(pattern));
				}
			}

			DelegateFactory.getInstance().getLsDelegate()
					.updateLieferschein(tpLieferschein.getLieferscheinDto());

			DelegateFactory
					.getInstance()
					.getLsDelegate()
					.updateLieferscheinKonditionen(
							tpLieferschein.getLieferscheinDto().getIId());

			super.eventActionSave(e, false); // buttons schalten

			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpLieferschein.getLieferscheinDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		tpLieferschein.setLieferscheinDto(DelegateFactory.getInstance()
				.getLsDelegate().lieferscheinFindByPrimaryKey((Integer) oKey));

		dto2Components();

		tpLieferschein.setTitleLieferschein(LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"));

		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			eingabeFreigeben();
		}
		tpLieferschein.enablePanels(tpLieferschein.getLieferscheinDto(), true);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			super.eventActionUpdate(aE, false); // Buttons schalten

			eingabeFreigeben();
		}
	}

	/**
	 * Diese Methode steuert den Status der einzelnen Components.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void eingabeFreigeben() throws Throwable {
		// die Freischaltung der Felder wird bei auftragbezogenem Lieferschein
		// uebersteuert
		if (tpLieferschein.getLieferscheinDto().getLieferscheinartCNr()
				.equals(LieferscheinFac.LSART_AUFTRAG)) {
			setzeAuftragabhaengigeFelderAktiv(false);
		}
	}

	/**
	 * Je nach Lieferscheinart duerfen einzelne Felder in einem bestehnden
	 * Lieferschein nie geaendert werden.
	 * 
	 * @param bAktivI
	 *            true, wenn die Felder aktiv sein sollen
	 * @throws Throwable
	 */
	private void setzeAuftragabhaengigeFelderAktiv(boolean bAktivI) {
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpLieferschein.getLieferscheinDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpLieferschein.getLieferscheinDto().getTAendern());
		setStatusbarStatusCNr(tpLieferschein.getLieferscheinStatus());
	}

	/**
	 * Wenn in Liefertermin oder Finaltermin ein neues Datum gewaehlt wurde,
	 * dann landet man hier.
	 * 
	 * @param evt
	 *            Ereignis
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		/*
		 * if (evt.getSource() == wdfLiefertermin.getDisplay() &&
		 * evt.getPropertyName().equals("value")) {
		 * wdfRueckgabetermin.setMinimumValue(wdfLiefertermin.getDate());
		 * 
		 * if (wdfRueckgabetermin.getDate() != null && wdfLiefertermin.getDate()
		 * != null) { if
		 * (wdfRueckgabetermin.getDate().before(wdfLiefertermin.getDate())) {
		 * wdfRueckgabetermin.setDate(wdfLiefertermin.getDate()); } } } else if
		 * (evt.getSource() == wdfRueckgabetermin.getDisplay() &&
		 * evt.getPropertyName().equals("value")) {
		 * wdfLiefertermin.setMaximumValue(wdfRueckgabetermin.getDate()); }
		 */
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}
}
