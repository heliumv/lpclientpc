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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ToolTipManager;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButtonWithoutCornerInfo;
import com.lp.client.frame.component.WrapperCheckBoxWithoutCornerInfo;
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

public class DialogArtikelkopieren extends JDialog implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final int defaultDismissTimeout = ToolTipManager.sharedInstance()
			.getDismissDelay();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperLabel lNeueArtikelnummer = new WrapperLabel();
	private WrapperTextField neueArtikelnummer = new WrapperTextField(false);
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperCheckBoxWithoutCornerInfo cbMitHersteller = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitArtikelgruppe = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitArtikelklasse = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitReferenznummer = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitBestellmengeneinheit = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitLagermindeststand = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitLagersollstand = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitVerpackungsmenge = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitVerschnittfaktor = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitVerschnittbasis = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitJahresmenge = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitMwstsatz = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitMaterial = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitGewicht = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitMaterialgewicht = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitZugehoerigerartikel = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitVertreterprovision = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitMinutenfaktor1 = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitMinutenfaktor2 = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitMindestdeckungsbeitrag = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitVerkaufsean = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitWarenverkehrsnummer = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitRabattierbar = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitGarantiezeit = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitFarbcode = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitErsatzartikel = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitUrsprungsland = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitKatalog = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitVkpreise = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitEkpreise = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitKommentare = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitEigenschaften = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitBreite = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitHoehe = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitTiefe = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitBauform = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitVerpackungsart = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitAufschlag = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitSollverkauf = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitRasterliegend = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitRasterstehend = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitHochstellen = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitHochsetzen = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitIndex = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitRevision = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitPolarisiert = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitFertigungssatzgroesse = new WrapperCheckBoxWithoutCornerInfo();

	private WrapperCheckBoxWithoutCornerInfo cbMitSnrbehaftet = new WrapperCheckBoxWithoutCornerInfo();
	private WrapperCheckBoxWithoutCornerInfo cbMitChnrbehaftet = new WrapperCheckBoxWithoutCornerInfo();

	private JButton btnEinstellungenSpeichern = new JButton();

	private InternalFrameArtikel internalFrame = null;

	private WrapperButtonWithoutCornerInfo wbuGeneriereArtikelnummer = new WrapperButtonWithoutCornerInfo();

	private HashMap zuKopieren = new HashMap();

	static final public String ACTION_SPECIAL_HERSTELLER_FROM_LISTE = "action_hersteller_from_liste";

	private WrapperSelectField wbuHersteller = null;

	private WrapperLabel lQuestionMark = new WrapperLabel();

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

		if (neueArtikelnummer.getText() != null) {

			String artikelnummerAbschneiden = neueArtikelnummer.getText()
					.trim();

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELNUMMER_AUSWAHL_ABSCHNEIDEN);
			int iAnzahlAbschneiden = (java.lang.Integer) parameter
					.getCWertAsObject();

			if (iAnzahlAbschneiden > 0
					&& artikelnummerAbschneiden.length() >= iAnzahlAbschneiden) {
				artikelnummerAbschneiden = artikelnummerAbschneiden.substring(
						0, artikelnummerAbschneiden.length()
								- iAnzahlAbschneiden);
				neueArtikelnummer.setText(artikelnummerAbschneiden);
			}
		}

		this.setSize(Defaults.getInstance().bySizeFactor(800, 450));

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
						if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBoxWithoutCornerInfo) {

							WrapperCheckBoxWithoutCornerInfo wcb = (WrapperCheckBoxWithoutCornerInfo) this
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
					if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBoxWithoutCornerInfo) {

						WrapperCheckBoxWithoutCornerInfo wcb = (WrapperCheckBoxWithoutCornerInfo) this
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

			if (wbuHersteller.getWrapperTextField().getText() != null
					&& neueArtikelnummer.getText() != null) {

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

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		lNeueArtikelnummer
				.setText(getResText("artikel.artikelkopieren.neueartikelnummer"));

		wbuGeneriereArtikelnummer.setText("G");
		wbuGeneriereArtikelnummer
				.setToolTipText(getResText("artikel.generiereartikelnummer"));
		wbuGeneriereArtikelnummer.addActionListener(this);

		if (parameter.getCWertAsObject() != null) {
			neueArtikelnummer.setColumnsMax(((Integer) parameter
					.getCWertAsObject()).intValue());
		}
		neueArtikelnummer.setUppercaseField(true);

		javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(getClass()
				.getResource("/com/lp/client/res/question_mark_16x16.png"));
		lQuestionMark.setIcon(imageIcon);
		lQuestionMark.setToolTipText(getResText("artikel.question.mark"));
		lQuestionMark.addMouseListener(ml);

		btnEinstellungenSpeichern.setText(LPMain
				.getTextRespectUISPr("artikel.einstellungen.speichern"));
		btnEinstellungenSpeichern.addActionListener(this);

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

		cbMitBestellmengeneinheit.setText(getResText("artikel.bestelleinheit")
				+ "/" + getResText("artikel.umrfkt"));

		cbMitHersteller.setText(getResText("lp.hersteller"));

		cbMitArtikelgruppe.setText(getResText("lp.artikelgruppe"));
		cbMitArtikelklasse.setText(getResText("lp.artikelklasse"));
		cbMitReferenznummer.setText(getResText("lp.referenznummer"));
		cbMitLagermindeststand.setText(getResText("artikel.lagermindeststand"));
		cbMitLagersollstand.setText(getResText("artikel.lagersollstand"));
		cbMitVerschnittfaktor.setText(getResText("artikel.verschnittfaktor"));
		cbMitVerschnittbasis.setText(getResText("lp.verschnittbasis"));
		cbMitJahresmenge.setText(getResText("artikel.jahresmenge"));
		cbMitMwstsatz.setText(getResText("lp.mwst"));

		cbMitVerpackungsmenge
				.setText(getResText("artikel.sonstiges.verpackungsmenge"));
		cbMitMaterial.setText(getResText("label.material"));
		cbMitGewicht.setText(getResText("lp.gewicht"));
		cbMitMaterialgewicht
				.setText(getResText("artikel.technik.materialgewicht"));
		cbMitZugehoerigerartikel
				.setText(getResText("artikel.sonstiges.zugehoerigerartikel"));
		cbMitVertreterprovision
				.setText(getResText("artikel.sonstiges.maxvertreterprovision"));
		cbMitMinutenfaktor1
				.setText(getResText("artikel.sonstiges.minutenfaktor") + " 1");
		cbMitMinutenfaktor2
				.setText(getResText("artikel.sonstiges.minutenfaktor") + " 2");
		cbMitMindestdeckungsbeitrag
				.setText(getResText("artikel.sonstiges.mindestdeckungsbeitrag"));
		cbMitVerkaufsean.setText(getResText("artikel.sonstiges.verkaufsean"));
		cbMitWarenverkehrsnummer
				.setText(getResText("artikel.sonstiges.warenverkehrsnummer"));
		cbMitRabattierbar.setText(getResText("artikel.sonstiges.rabattierbar"));
		cbMitGarantiezeit.setText(getResText("artikel.sonstiges.garantiezeit"));
		cbMitFarbcode.setText(getResText("artikel.farbcode"));
		cbMitErsatzartikel
				.setText(getResText("artikel.sonstiges.ersatzartikel"));
		cbMitUrsprungsland.setText(getResText("artikel.ursprungsland"));
		cbMitKatalog.setText(getResText("lp.katalog"));
		cbMitVkpreise.setText(getResText("artikel.title.panel.preise"));
		cbMitEkpreise
				.setText(getResText("artikel.title.panel.artikellieferant"));
		cbMitKommentare
				.setText(getResText("artikel.artikelkopieren.mitkommentar"));
		cbMitEigenschaften.setText(getResText("lp.eigenschaften"));
		cbMitBreite.setText(getResText("artikel.technik.breite"));
		cbMitHoehe.setText(getResText("artikel.technik.hoehe"));
		cbMitTiefe.setText(getResText("artikel.technik.tiefe"));
		cbMitBauform.setText(getResText("artikel.technik.bauform"));
		cbMitVerpackungsart
				.setText(getResText("artikel.technik.verpackungsart"));
		cbMitAufschlag.setText(getResText("artikel.sonstiges.aufschlag"));
		cbMitSollverkauf.setText(getResText("artikel.sonstiges.soll"));
		cbMitRasterliegend.setText(getResText("artikel.technik.rasterliegend"));
		cbMitRasterstehend.setText(getResText("artikel.technik.rasterstehend"));
		cbMitHochstellen.setText(getResText("artikel.technik.hochstellen"));
		cbMitHochsetzen.setText(getResText("artikel.technik.hochsetzen"));
		cbMitPolarisiert.setText(getResText("artikel.technik.antistatic"));
		cbMitFertigungssatzgroesse
				.setText(getResText("artikel.fertigungssatzgroesse"));
		cbMitIndex.setText(getResText("artikel.index"));
		cbMitRevision.setText(getResText("artikel.revision"));

		cbMitSnrbehaftet
				.setText(getResText("artikel.sonstiges.seriennummernbehaftet"));
		cbMitChnrbehaftet
				.setText(getResText("artikel.sonstiges.chargennummernbehaftet"));

		btnOK.setText(getResText("button.ok"));
		btnAbbrechen.setText(getResText("lp.abbrechen"));

		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		// --------------------------
		int iZeile = 0;

		this.getContentPane().add(
				lNeueArtikelnummer,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 5, 50), 0, 0));

		this.getContentPane().add(
				wbuGeneriereArtikelnummer,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 20, 0));

		this.getContentPane().add(
				neueArtikelnummer,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
						new Insets(5, 5, 5, 5), 0, 0));

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
						GridBagConstraints.EAST, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 20, 0));

		// --------------------------
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

		this.getContentPane().add(
				lQuestionMark,
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 50), 0, 0));

	}

	private String getResText(String str) {
		return LPMain.getTextRespectUISPr(str);
	}

	MouseListener ml = new MouseAdapter() {

		public void mouseEntered(MouseEvent me) {
			ToolTipManager.sharedInstance().setDismissDelay(60000);
		}

		public void mouseExited(MouseEvent me) {
			ToolTipManager.sharedInstance().setDismissDelay(
					defaultDismissTimeout);
		}

	};

}
