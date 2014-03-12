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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogArtikelkopieren extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperLabel lNeueArtikelnummer = new WrapperLabel();
	private WrapperTextField neueArtikelnummer = new WrapperTextField();
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();
	private WrapperCheckBox cbMitHersteller = new WrapperCheckBox();
	private WrapperCheckBox cbMitArtikelgruppe = new WrapperCheckBox();
	private WrapperCheckBox cbMitArtikelklasse = new WrapperCheckBox();
	private WrapperCheckBox cbMitReferenznummer = new WrapperCheckBox();

	private WrapperCheckBox cbMitBestellmengeneinheit = new WrapperCheckBox();
	private WrapperCheckBox cbMitLagermindeststand = new WrapperCheckBox();
	private WrapperCheckBox cbMitLagersollstand = new WrapperCheckBox();
	private WrapperCheckBox cbMitVerpackungsmenge = new WrapperCheckBox();

	private WrapperCheckBox cbMitVerschnittfaktor = new WrapperCheckBox();
	private WrapperCheckBox cbMitVerschnittbasis = new WrapperCheckBox();
	private WrapperCheckBox cbMitJahresmenge = new WrapperCheckBox();
	private WrapperCheckBox cbMitMwstsatz = new WrapperCheckBox();

	private WrapperCheckBox cbMitMaterial = new WrapperCheckBox();
	private WrapperCheckBox cbMitGewicht = new WrapperCheckBox();
	private WrapperCheckBox cbMitMaterialgewicht = new WrapperCheckBox();
	private WrapperCheckBox cbMitZugehoerigerartikel = new WrapperCheckBox();

	private WrapperCheckBox cbMitVertreterprovision = new WrapperCheckBox();
	private WrapperCheckBox cbMitMinutenfaktor1 = new WrapperCheckBox();
	private WrapperCheckBox cbMitMinutenfaktor2 = new WrapperCheckBox();
	private WrapperCheckBox cbMitMindestdeckungsbeitrag = new WrapperCheckBox();

	private WrapperCheckBox cbMitVerkaufsean = new WrapperCheckBox();
	private WrapperCheckBox cbMitWarenverkehrsnummer = new WrapperCheckBox();
	private WrapperCheckBox cbMitRabattierbar = new WrapperCheckBox();
	private WrapperCheckBox cbMitGarantiezeit = new WrapperCheckBox();

	private WrapperCheckBox cbMitFarbcode = new WrapperCheckBox();
	private WrapperCheckBox cbMitErsatzartikel = new WrapperCheckBox();
	private WrapperCheckBox cbMitUrsprungsland = new WrapperCheckBox();

	private WrapperCheckBox cbMitKatalog = new WrapperCheckBox();
	private WrapperCheckBox cbMitVkpreise = new WrapperCheckBox();
	private WrapperCheckBox cbMitEkpreise = new WrapperCheckBox();
	private WrapperCheckBox cbMitKommentare = new WrapperCheckBox();

	private WrapperCheckBox cbMitEigenschaften = new WrapperCheckBox();
	private WrapperCheckBox cbMitBreite = new WrapperCheckBox();
	private WrapperCheckBox cbMitHoehe = new WrapperCheckBox();
	private WrapperCheckBox cbMitTiefe = new WrapperCheckBox();

	private WrapperCheckBox cbMitBauform = new WrapperCheckBox();
	private WrapperCheckBox cbMitVerpackungsart = new WrapperCheckBox();
	private WrapperCheckBox cbMitAufschlag = new WrapperCheckBox();
	private WrapperCheckBox cbMitSollverkauf = new WrapperCheckBox();

	private WrapperCheckBox cbMitRasterliegend = new WrapperCheckBox();
	private WrapperCheckBox cbMitRasterstehend = new WrapperCheckBox();
	private WrapperCheckBox cbMitHochstellen = new WrapperCheckBox();
	private WrapperCheckBox cbMitHochsetzen = new WrapperCheckBox();

	private WrapperCheckBox cbMitIndex = new WrapperCheckBox();
	private WrapperCheckBox cbMitRevision = new WrapperCheckBox();

	private WrapperCheckBox cbMitPolarisiert = new WrapperCheckBox();
	private WrapperCheckBox cbMitFertigungssatzgroesse = new WrapperCheckBox();

	private WrapperCheckBox cbMitSnrbehaftet = new WrapperCheckBox();
	private WrapperCheckBox cbMitChnrbehaftet = new WrapperCheckBox();

	private JButton btnEinstellungenSpeichern = new JButton();

	private InternalFrameArtikel internalFrame = null;

	private WrapperButton wbuGeneriereArtikelnummer = new WrapperButton();
	private HashMap zuKopieren = new HashMap();

	static final public String ACTION_SPECIAL_HERSTELLER_FROM_LISTE = "action_hersteller_from_liste";

	private WrapperSelectField wbuHersteller = null;

	public Integer getHerstellerIIdNeu() {

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {
			return wbuHersteller.getIKey();
		} else {
			return null;
		}

	}

	public DialogArtikelkopieren(ArtikelDto arikelDto_AlterArtikel,
			InternalFrameArtikel internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "Artikel '"
				+ arikelDto_AlterArtikel.formatArtikelbezeichnung()
				+ "' kopieren", true);
		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		// Gespeicherte Werte setzen
		setKeyValueDtos(DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.keyvalueFindyByCGruppe(
						SystemServicesFac.KEYVALUE_ARTIKEL_KOPIEREN));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {

			cbMitHersteller.setSelected(false);
			cbMitHersteller.setEnabled(false);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
							ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());

			int maxLaenge = ((Integer) parameter.getCWertAsObject()).intValue();

			if (arikelDto_AlterArtikel.getCNr().length() > maxLaenge) {
				neueArtikelnummer.setText(arikelDto_AlterArtikel.getCNr()
						.substring(0, maxLaenge));
				wbuHersteller.getWrapperTextField().setText(
						arikelDto_AlterArtikel.getCNr().substring(maxLaenge));
			} else {
				neueArtikelnummer.setText(arikelDto_AlterArtikel.getCNr());
				wbuHersteller.getWrapperTextField().setText(null);
			}

		} else {
			neueArtikelnummer.setText(arikelDto_AlterArtikel.getCNr());
		}

		this.setSize(800, 450);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuGeneriereArtikelnummer)) {
			try {
				neueArtikelnummer
						.setText(DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.generiereNeueArtikelnummer(
										neueArtikelnummer.getText()));
			} catch (Throwable ex) {
				LPMain.getInstance().exitFrame(null, ex);
			}
		} else {
			if (e.getSource().equals(btnAbbrechen)) {
				neueArtikelnummer.setText(null);
				this.setVisible(false);
			} else if (e.getSource().equals(btnEinstellungenSpeichern)) {

				ArrayList al = null;
				;
				try {
					al = getKeyValueDtos();
					KeyvalueDto[] returnArray = new KeyvalueDto[al.size()];

					DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.speichereKeyValueDtos(
									(KeyvalueDto[]) al.toArray(returnArray));

				} catch (Throwable e1) {
					internalFrame.getTabbedPaneArtikel().handleException(e1,
							true);
				}

			} else {
				this.setVisible(false);
			}

		}

	}

	private void setKeyValueDtos(KeyvalueDto[] dtos) throws Throwable {

		for (int z = 0; z < dtos.length; z++) {

			for (int i = 0; i < this.getContentPane().getComponents().length; ++i) {

				{

					if (this.getContentPane().getComponents()[i].getName() != null
							&& this.getContentPane().getComponents()[i]
									.getName().equals(dtos[z].getCKey())) {
						if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBox) {

							WrapperCheckBox wcb = (WrapperCheckBox) this
									.getContentPane().getComponents()[i];
							wcb.setShort(new Short(dtos[z].getCValue()));

						}
					}
				}
			}
		}

	}

	private ArrayList getKeyValueDtos() throws Throwable {
		ArrayList alKomponenten = new ArrayList();

		for (int i = 0; i < this.getContentPane().getComponents().length; ++i) {

			{
				KeyvalueDto reportkonfDto = new KeyvalueDto();
				reportkonfDto
						.setCGruppe(SystemServicesFac.KEYVALUE_ARTIKEL_KOPIEREN);
				if (this.getContentPane().getComponents()[i].getName() != null) {
					if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBox) {

						WrapperCheckBox wcb = (WrapperCheckBox) this
								.getContentPane().getComponents()[i];
						reportkonfDto.setCValue(wcb.getShort() + "");
						reportkonfDto.setCKey(this.getContentPane()
								.getComponents()[i].getName());
						reportkonfDto.setCDatentyp("java.lang.Short");
						alKomponenten.add(reportkonfDto);
					}
				}
			}
		}
		return alKomponenten;
	}

	public String getArtikelnummerNeu() throws Throwable {
		String artikelnummerNeu = neueArtikelnummer.getText();

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
							ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());

			int maxLaenge = ((Integer) parameter.getCWertAsObject()).intValue();

			if (wbuHersteller.getWrapperTextField().getText() != null) {

				if (neueArtikelnummer.getText().length() < maxLaenge) {
					artikelnummerNeu = Helper.fitString2Length(
							neueArtikelnummer.getText(), maxLaenge, ' ')
							+ wbuHersteller.getWrapperTextField().getText();
				} else {
					artikelnummerNeu = neueArtikelnummer.getText()
							+ wbuHersteller.getWrapperTextField().getText();
				}
			} else {
				artikelnummerNeu = neueArtikelnummer.getText();
			}
		}

		return artikelnummerNeu;
	}

	@SuppressWarnings("unchecked")
	public HashMap getZuKopierendeFelder() {

		if (cbMitHersteller.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_HERSTELLER, "");
		}
		if (cbMitArtikelgruppe.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELGRUPPE, "");
		}
		if (cbMitArtikelklasse.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELKLASSE, "");
		}
		if (cbMitReferenznummer.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_REFERENZNUMMER, "");
		}
		if (cbMitBestellmengeneinheit.isSelected()) {
			zuKopieren
					.put(ArtikelFac.ARTIKEL_KOPIEREN_BESTELLMENGENEINHEIT, "");
		}
		if (cbMitLagermindeststand.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_LAGERMINDESTSTAND, "");
		}
		if (cbMitLagersollstand.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_LAGERSOLLSTAND, "");
		}

		if (cbMitVerpackungsmenge.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERPACKUNSMENGE, "");
		}
		if (cbMitVerschnittfaktor.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTFAKTOR, "");
		}
		if (cbMitVerschnittbasis.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTBASIS, "");
		}
		if (cbMitJahresmenge.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_JAHRESMENGE, "");
		}
		if (cbMitMwstsatz.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MWSTSATZ, "");
		}
		if (cbMitMaterial.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MATERIAL, "");
		}
		if (cbMitGewicht.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_GEWICHT, "");
		}
		if (cbMitMaterialgewicht.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MATERIALGEWICHT, "");
		}
		if (cbMitZugehoerigerartikel.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_ZUGEHOERIGERARTIKEL, "");
		}
		if (cbMitVertreterprovision.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERTRETERPROVISION, "");
		}
		if (cbMitMinutenfaktor1.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR1, "");
		}
		if (cbMitMinutenfaktor2.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR2, "");
		}
		if (cbMitMindestdeckungsbeitrag.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG,
					"");
		}
		if (cbMitVerkaufsean.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERKAUFSEAN, "");
		}
		if (cbMitWarenverkehrsnummer.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_WARENVERKEHRSNUMMER, "");
		}
		if (cbMitRabattierbar.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_RABATTIERBAR, "");
		}
		if (cbMitGarantiezeit.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_GARANTIEZEIT, "");
		}
		if (cbMitFarbcode.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_FARBCODE, "");
		}
		if (cbMitErsatzartikel.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_ERSATZARTIKEL, "");
		}
		if (cbMitUrsprungsland.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_URSPRUNGSLAND, "");
		}
		if (cbMitKatalog.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_KATALOG, "");
		}

		if (cbMitVkpreise.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VKPREISE, "");
		}
		if (cbMitEkpreise.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_EKPREISE, "");
		}
		if (cbMitKommentare.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE, "");
		}
		if (cbMitEigenschaften.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_EIGENSCHAFTEN, "");
		}
		if (cbMitBreite.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_BREITE, "");
		}
		if (cbMitHoehe.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_HOEHE, "");
		}
		if (cbMitTiefe.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_TIEFE, "");
		}
		if (cbMitBauform.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_BAUFORM, "");
		}
		if (cbMitVerpackungsart.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_VERPACKUNGSART, "");
		}
		if (cbMitAufschlag.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_AUFSCHLAG, "");
		}
		if (cbMitSollverkauf.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_SOLLVERKAUF, "");
		}
		if (cbMitRasterliegend.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_RASTERLIEGEND, "");
		}
		if (cbMitRasterstehend.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_RASTERSTEHEND, "");
		}
		if (cbMitHochstellen.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_HOCHSTELLEN, "");
		}
		if (cbMitHochsetzen.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_HOCHSETZEN, "");
		}
		if (cbMitPolarisiert.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_POLARISIERT, "");
		}
		if (cbMitFertigungssatzgroesse.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE,
					"");
		}
		if (cbMitIndex.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_INDEX, "");
		}
		if (cbMitRevision.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_REVISION, "");
		}
		if (cbMitSnrbehaftet.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_SNRBEHAFTET, "");
		}
		if (cbMitChnrbehaftet.isSelected()) {
			zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_CHNRBEHAFTET, "");
		}

		return zuKopieren;
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			neueArtikelnummer.setText(null);
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnEinstellungenSpeichern.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.report.save"));
		btnEinstellungenSpeichern.addActionListener(this);
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			neueArtikelnummer.setColumnsMax(((Integer) parameter
					.getCWertAsObject()).intValue());
		}
		lNeueArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelkopieren.neueartikelnummer"));

		wbuHersteller = new WrapperSelectField(WrapperSelectField.HERSTELLER,
				internalFrame, true);

		wbuHersteller.getWrapperTextField().setActivatable(false);

		// --
		cbMitHersteller.setSelected(true);
		cbMitArtikelgruppe.setSelected(true);
		cbMitArtikelklasse.setSelected(true);
		// cbMitReferenznummer.setSelected(true);

		// cbMitBestellmengeneinheit.setSelected(true);
		// cbMitLagermindeststand.setSelected(true);
		// cbMitLagersollstand.setSelected(true);
		// cbMitVerpackungsmenge.setSelected(true);

		// cbMitVerschnittfaktor.setSelected(true);
		// cbMitVerschnittbasis.setSelected(true);
		// cbMitJahresmenge.setSelected(true);
		// cbMitMwstsatz.setSelected(true);

		cbMitMaterial.setSelected(true);
		cbMitGewicht.setSelected(true);
		cbMitMaterialgewicht.setSelected(true);
		cbMitZugehoerigerartikel.setSelected(true);

		cbMitVertreterprovision.setSelected(true);
		// cbMitMinutenfaktor1.setSelected(true);
		// cbMitMinutenfaktor2.setSelected(true);
		cbMitMindestdeckungsbeitrag.setSelected(true);

		// cbMitVerkaufsean.setSelected(true);
		// cbMitWarenverkehrsnummer.setSelected(true);
		cbMitRabattierbar.setSelected(true);
		cbMitGarantiezeit.setSelected(true);

		cbMitFarbcode.setSelected(true);
		// cbMitUmrechnungsfaktor.setSelected(true);
		cbMitErsatzartikel.setSelected(true);
		cbMitUrsprungsland.setSelected(true);

		// cbMitKatalog.setSelected(true);
		// cbMitVkpreise.setSelected(true);
		// cbMitEkpreise.setSelected(true);
		// cbMitKommentare.setSelected(true);

		// cbMitEigenschaften.setSelected(true);
		cbMitBreite.setSelected(true);
		cbMitHoehe.setSelected(true);
		cbMitTiefe.setSelected(true);

		cbMitBauform.setSelected(true);
		cbMitVerpackungsart.setSelected(true);
		cbMitAufschlag.setSelected(true);
		cbMitSollverkauf.setSelected(true);

		cbMitRasterliegend.setSelected(true);
		cbMitRasterstehend.setSelected(true);
		cbMitHochstellen.setSelected(true);
		cbMitHochsetzen.setSelected(true);

		cbMitPolarisiert.setSelected(true);

		cbMitFertigungssatzgroesse.setSelected(true);

		cbMitIndex.setSelected(true);
		cbMitRevision.setSelected(true);

		// -

		cbMitBestellmengeneinheit.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.bestelleinheit")
				+ "/"
				+ LPMain.getInstance().getTextRespectUISPr("artikel.umrfkt"));
		cbMitHersteller.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.hersteller"));
		cbMitArtikelgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelgruppe"));
		cbMitArtikelklasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelklasse"));
		cbMitReferenznummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.referenznummer"));
		cbMitLagermindeststand.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.lagermindeststand"));
		cbMitLagersollstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.lagersollstand"));
		cbMitVerschnittfaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.verschnittfaktor"));
		cbMitVerschnittbasis.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verschnittbasis"));
		cbMitJahresmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.jahresmenge"));
		cbMitMwstsatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mwst"));

		cbMitVerpackungsmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verpackungsmenge"));
		cbMitMaterial.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.material"));
		cbMitGewicht.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gewicht"));
		cbMitMaterialgewicht.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.materialgewicht"));
		cbMitZugehoerigerartikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.zugehoerigerartikel"));
		cbMitVertreterprovision
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.maxvertreterprovision"));
		cbMitMinutenfaktor1.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.minutenfaktor")
				+ " 1");
		cbMitMinutenfaktor2.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.minutenfaktor")
				+ " 2");
		cbMitMindestdeckungsbeitrag
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.mindestdeckungsbeitrag"));
		cbMitVerkaufsean.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verkaufsean"));
		cbMitWarenverkehrsnummer.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.warenverkehrsnummer"));
		cbMitRabattierbar.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.rabattierbar"));
		cbMitGarantiezeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.garantiezeit"));
		cbMitFarbcode.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.farbcode"));
		cbMitErsatzartikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.ersatzartikel"));
		cbMitUrsprungsland.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.ursprungsland"));
		cbMitKatalog.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.katalog"));
		cbMitVkpreise.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.title.panel.preise"));
		cbMitEkpreise.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.title.panel.artikellieferant"));
		cbMitKommentare.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelkopieren.mitkommentar"));
		cbMitEigenschaften.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.eigenschaften"));
		cbMitBreite.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.breite"));
		cbMitHoehe.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.hoehe"));
		cbMitTiefe.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.tiefe"));
		cbMitBauform.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.bauform"));
		cbMitVerpackungsart.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.verpackungsart"));
		cbMitAufschlag.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.aufschlag"));
		cbMitSollverkauf.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.soll"));
		cbMitRasterliegend.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.rasterliegend"));
		cbMitRasterstehend.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.rasterstehend"));
		cbMitHochstellen.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.hochstellen"));
		cbMitHochsetzen.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.hochsetzen"));
		cbMitPolarisiert.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.antistatic"));
		cbMitFertigungssatzgroesse.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.fertigungssatzgroesse"));
		cbMitIndex.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.index"));
		cbMitRevision.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.revision"));

		cbMitSnrbehaftet.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.seriennummernbehaftet"));
		cbMitChnrbehaftet.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.chargennummernbehaftet"));

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		neueArtikelnummer.setUppercaseField(true);
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wbuGeneriereArtikelnummer.setText("G");
		wbuGeneriereArtikelnummer.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.generiereartikelnummer"));
		wbuGeneriereArtikelnummer.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		this.getContentPane().add(
				lNeueArtikelnummer,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 50),
						0, 0));

		this.getContentPane().add(
				wbuGeneriereArtikelnummer,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 30, 0));
		this.getContentPane().add(
				neueArtikelnummer,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {

			this.getContentPane().add(
					wbuHersteller.getWrapperTextField(),
					new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 30, 0));

			this.getContentPane().add(
					wbuHersteller.getWrapperButton(),
					new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
							GridBagConstraints.EAST,
							GridBagConstraints.HORIZONTAL, new Insets(5, 50, 5,
									5), 30, 0));
		}
		this.getContentPane().add(
				btnEinstellungenSpeichern,
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 20, 0));

		// --
		iZeile++;
		this.getContentPane().add(
				cbMitHersteller,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));
		this.getContentPane().add(
				cbMitArtikelgruppe,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));
		this.getContentPane().add(
				cbMitArtikelklasse,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));
		this.getContentPane().add(
				cbMitReferenznummer,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));
		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitBestellmengeneinheit,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitLagermindeststand,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitLagersollstand,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitVerpackungsmenge,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitVerschnittfaktor,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitVerschnittbasis,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitJahresmenge,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitMwstsatz,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitGewicht,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		this.getContentPane().add(
				cbMitMaterialgewicht,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitMaterial,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		this.getContentPane().add(
				cbMitZugehoerigerartikel,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitVertreterprovision,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitMinutenfaktor1,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitMinutenfaktor2,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitMindestdeckungsbeitrag,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitVerkaufsean,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitWarenverkehrsnummer,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitRabattierbar,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitGarantiezeit,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitFarbcode,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitPolarisiert,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitErsatzartikel,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitUrsprungsland,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitKatalog,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitVkpreise,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitEkpreise,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitKommentare,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitEigenschaften,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitBreite,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitHoehe,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitTiefe,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitBauform,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitVerpackungsart,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitAufschlag,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitSollverkauf,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitRasterliegend,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitRasterstehend,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitHochstellen,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitHochsetzen,
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		// --------------------------
		iZeile++;
		this.getContentPane().add(
				cbMitIndex,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitRevision,
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				cbMitFertigungssatzgroesse,
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		// --------------------------
		iZeile++;
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

			this.getContentPane().add(
					cbMitSnrbehaftet,
					new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
		}
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)) {
			this.getContentPane().add(
					cbMitChnrbehaftet,
					new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
		}

		// --------------------------

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
