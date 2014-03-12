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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um den Kunden.</I> </p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>xx.12.04</I></p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.24 $
 */
public class PanelKundeKonditionen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQueryFLR panelQueryFLRProvisionsempfaenger = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaWaehrung = null;
	private WrapperTextField wtfKontoerloese = null;
	private WrapperTextNumberField wtnfKontodebitoren = null;
	private WrapperLabel wlaKontodebitorenRangeVonBis = null;
	private WrapperButton wbtKontoerloese = null;
	private PanelQueryFLR panelQueryFLRKontoerloese = null;
	private WrapperTextField wtfSpedition = null;
	private WrapperButton wbuSpedition = null;
	private WrapperButton wbuZahlungsziel = null;
	private WrapperTextField wtfZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRAbbuchungsLager = null;
	private WrapperLabel wlaGarantieInMonaten = null;
	private WrapperNumberField wnfGarantieInMonaten = null;
	private WrapperLabel wlaEinheitMonat = null;
	private WrapperCheckBox wcbMindermengenzuschlag = null;
	private WrapperCheckBox wcbMonatrechnung = null;
	private WrapperCheckBox wcbSammelRE = null;
	private WrapperCheckBox wcbIstREEmpfaenger = null;
	private WrapperCheckBox wcbPreiseAnLSAndrucken = null;
	private WrapperCheckBox wcbREDruckMitRabatt = null;
	private WrapperCheckBox wcbDistributor = null;
	private WrapperCheckBox wcbAkzeptiertTeillieferung = null;
	private WrapperCheckBox wcbLSGewichtAngeben = null;
	private WrapperLabel wlaKommentar = null;
	private WrapperEditorField wefKommentar = null;
	private WrapperDateField wdfBonitaet = null;
	private WrapperDateField wdfLiefersperreAm = null;
	private WrapperLabel wlaBonitaet = null;
	private WrapperLabel wlaKundennummer = null;
	private WrapperTextNumberField wnfKundennummer = new WrapperTextNumberField();
	private WrapperLabel wlaLiefersperreAm = null;
	private WrapperLabel wlaDefaultKopienRE = null;
	private WrapperLabel wlaDefaultKopienLS = null;
	private WrapperNumberField wnfDefaultKopienLS = null;
	private WrapperNumberField wnfDefaultKopienRE = null;
	private WrapperTextField wtfKostenstelle = null;
	private WrapperButton wbtKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private WrapperLabel wlaMitarbeiteranzahl = null;
	private WrapperNumberField wnfMitarbeiteranzahl = null;
	private WrapperLabel wlaTour = null;
	private WrapperTextField wtfTour = null;
	private WrapperLabel wlaLieferantennummer = null;
	private WrapperTextField wtfLieferantennummer = null;
	private WrapperLabel wlaABC = null;
	private WrapperComboBox wcoABC = null;
	private WrapperLabel wlaAGBUebermittelung = null;
	private WrapperDateField wdfAGBUebermittelung = null;
	private WrapperLabel wlaFremsystemnr = null;
	private WrapperTextField wtfFremsystemnr = null;
	private WrapperComboBox wcoWaehrung = null;
	private WrapperLabel wlaRabattsatz = null;
	private WrapperNumberField wnfRabattsatz = null;
	private WrapperLabel wlaEinheitProzentRabatt = null;
	private WrapperNumberField wnfKreditlimit = null;
	private WrapperLabel wlaKreditlimit = null;
	private WrapperLabel wlaKreditlimitWaehrung = null;
	private WrapperButton wbuProvisionsempfaenger = null;
	private WrapperTextField wtfProvisionsempfaenger = null;
	private WrapperButton wbuLieferart = null;
	private WrapperTextField wtfLieferart = null;
	private PanelQueryFLR panelQueryFLRLieferart;
	private PanelQueryFLR panelQueryFLRSpediteur;
	private WrapperLabel wlaHinweistextintern = null;
	private WrapperTextField wtfHinweistextintern = null;
	private WrapperLabel wlaHinweistextextern = null;
	private WrapperTextField wtfHinweistextextern = null;
	private WrapperLabel wlaZessionsfaktor = null;
	private WrapperNumberField wnfZessionsfaktor = null;
	private WrapperLabel wlaEinheitProzentZessionsfaktor = null;
	private WrapperGotoButton wbuDebitorenkontoAutomatisch = null;
	private WrapperCheckBox wcbReversecharge = null;
	private WrapperButton wbuAbweichendesustland = null;
	private WrapperLabel wlaAbweichendesfinanzamt = null;
	private WrapperTextField wtfAbweichendesustland = null;
	private PanelQueryFLR panelQueryFLRAbweichendesustland = null;
	private WrapperCheckBox wcbGenerateKurznr = null;
	private WrapperTextField wtfKurznr = null;
	private WrapperCheckBox wcbZollpapiere = null;

	private WrapperButton wbuZiellager = null;
	private WrapperTextField wtfZiellager = null;

	private WrapperButton wbuAbbuchungslager = null;
	private WrapperTextField wtfAbbuchungslager = null;

	private WrapperLabel wlaErwerbsberechtigung = null;
	private WrapperDateField wdfErwerbsberechtigung = null;
	private WrapperLabel wlaBegruendung = null;
	private WrapperTextField wtfBegruendung = null;

	private WrapperLabel wlaLieferdauer = null;
	private WrapperNumberField wnfLieferdauer = null;

	private boolean bReversecharge = true;

	static final private String ACTION_SPECIAL_FLR_KONTOERLOESE = "action_special_flr_kontoerloese";
	static final private String ACTION_SPECIAL_FLR_ZAHLUNGSZIEL = "action_special_flr_zahlungsziel";
	static final private String ACTION_SPECIAL_FLR_KOSTENSTELLE = "action_special_flr_kostenstelle";
	static final private String ACTION_SPECIAL_FLR_PROVISIONSEMPFAENGER = "action_special_flr_personalprovisionsempfaenger";
	static final private String ACTION_SPECIAL_FLR_LIEFERART = "action_special_flr_lieferart";
	static final private String ACTION_SPECIAL_FLR_SPEDITEUR = "action_special_flr_spedituer";
	static final private String ACTION_SPECIAL_DEBITORENKONTOAUTOMATISCH = "action_special_debitorenkontoautomatisch";
	static final private String ACTION_SPECIAL_FLR_USTLAND = "action_special_flr_ustland";
	static final public String ACTION_SPECIAL_ZIELLAGER_FROM_LISTE = "action_ziellager_from_liste";
	static final public String ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE = "action_abbuchungslager_from_liste";

	public PanelKundeKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
	}

	private void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						getKundeDto().getPartnerDto().getLagerIIdZiellager(),
						true, false);

		new DialogQuery(panelQueryFLRLager);
	}

	private void dialogQueryAbbuchungsLagerFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRAbbuchungsLager = ArtikelFilterFactory
				.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						getKundeDto().getLagerIIdAbbuchungslager(), true, false);

		new DialogQuery(panelQueryFLRAbbuchungsLager);
	}

	private void jbInit() throws Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		// ... bis hier ist's immer gleich
		wtfKontoerloese = new WrapperTextField();
		wtfKontoerloese.setActivatable(false);
		wbtKontoerloese = new WrapperButton();
		wbtKontoerloese.setHorizontalAlignment(SwingConstants.CENTER);
		wbtKontoerloese.setText(LPMain.getInstance().getTextRespectUISPr(
				"finanz.erloesekonto"));
		// FLR kontoerloese lokal
		wbtKontoerloese.setActionCommand(ACTION_SPECIAL_FLR_KONTOERLOESE);
		wbtKontoerloese.addActionListener(this);

		wlaKontodebitorenRangeVonBis = new WrapperLabel();
		wlaKontodebitorenRangeVonBis
				.setHorizontalAlignment(SwingConstants.LEFT);

		wtnfKontodebitoren = new WrapperTextNumberField();
		Integer iS = DelegateFactory
				.getInstance()
				.getFinanzDelegate()
				.getAnzahlStellenVonKontoNummer(
						FinanzServiceFac.KONTOTYP_DEBITOR);
		wtnfKontodebitoren.setMaximumDigits(iS.intValue());

		getInternalFrame().addItemChangedListener(this);

		wcbZollpapiere = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr(
						"part.lieferant.zollimportpapiere.erforderlich"));
		
		wbuSpedition = new WrapperButton();
		wbuSpedition.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur"));
		wbuSpedition.setActionCommand(ACTION_SPECIAL_FLR_SPEDITEUR);
		wbuSpedition.addActionListener(this);

		wbuAbweichendesustland = new WrapperButton();
		wbuAbweichendesustland.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.abwustland"));
		wbuAbweichendesustland.setActionCommand(ACTION_SPECIAL_FLR_USTLAND);
		wbuAbweichendesustland.addActionListener(this);

		wlaAbweichendesfinanzamt = new WrapperLabel();
		wlaAbweichendesfinanzamt.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.abwfinanzamt"));

		wtfAbweichendesustland = new WrapperTextField();
		wtfAbweichendesustland.setActivatable(false);

		wtfSpedition = new WrapperTextField();
		wtfSpedition.setMandatoryFieldDB(true);
		wtfSpedition.setActivatable(false);

		wbuZahlungsziel = new WrapperButton();
		wbuZahlungsziel.setHorizontalAlignment(SwingConstants.CENTER);
		wbuZahlungsziel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbuZahlungsziel.setActionCommand(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL);
		wbuZahlungsziel.addActionListener(this);
		wtfZahlungsziel = new WrapperTextField();
		wtfZahlungsziel.setActivatable(false);

		wcbGenerateKurznr = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.kurznummer"));
		wtfKurznr = new WrapperTextField(KundeFac.MAX_KURZNR);
		wtfKurznr.setDependenceField(true);
		wtfKurznr.setActivatable(false);

		wbuZiellager = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.ziellager"));

		wbuZiellager.setActionCommand(ACTION_SPECIAL_ZIELLAGER_FROM_LISTE);
		wbuZiellager.addActionListener(this);

		wtfZiellager = new WrapperTextField();
		wtfZiellager.setActivatable(false);

		wbuAbbuchungslager = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.abbuchungslager"));
		wbuAbbuchungslager.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button..hint.abbuchungslager"));
		wbuAbbuchungslager
				.setActionCommand(ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE);
		wbuAbbuchungslager.addActionListener(this);
		wtfAbbuchungslager = new WrapperTextField();
		wtfAbbuchungslager.setActivatable(false);
		wtfAbbuchungslager.setMandatoryField(true);

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		bReversecharge = (Boolean) parametermandantDto.getCWertAsObject();

		wlaGarantieInMonaten = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kond.label.garantie"));

		wnfGarantieInMonaten = new WrapperNumberField();

		wnfGarantieInMonaten.setMaximumValue(new BigDecimal(
				KundeFac.MAX_GARANTIE_IN_MONATEN));
		wnfGarantieInMonaten.setMinimumValue(new BigDecimal(
				KundeFac.MIN_GARANTIE_IN_MONATEN));

		wlaEinheitMonat = new WrapperLabel();
		wlaEinheitMonat.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.monat"));
		wlaEinheitMonat.setHorizontalAlignment(SwingConstants.LEFT);

		wcbMindermengenzuschlag = new WrapperCheckBox();
		wcbMindermengenzuschlag.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.mindermengenzuschlag"));

		wcbMonatrechnung = new WrapperCheckBox();
		wcbMonatrechnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.monatsrechnung"));

		wcbSammelRE = new WrapperCheckBox();
		wcbSammelRE.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.bekommt_sammelrechnung"));

		wcbIstREEmpfaenger = new WrapperCheckBox();
		wcbIstREEmpfaenger.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.ist_rechnungempfaenger"));

		wcbPreiseAnLSAndrucken = new WrapperCheckBox();
		wcbPreiseAnLSAndrucken.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.preise_an_ls_andrucken"));

		wcbREDruckMitRabatt = new WrapperCheckBox();
		wcbREDruckMitRabatt.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.rechnungsdruck_mit_rabatt"));

		wcbDistributor = new WrapperCheckBox();
		wcbDistributor.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.ist_distributor"));

		wcbAkzeptiertTeillieferung = new WrapperCheckBox();
		wcbAkzeptiertTeillieferung.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.akzeptiert_teillieferung"));

		wcbLSGewichtAngeben = new WrapperCheckBox();
		wcbLSGewichtAngeben.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.ls_gewicht_angeben"));

		wcbReversecharge = new WrapperCheckBox();
		wcbReversecharge.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.reversecharge"));

		wlaKommentar = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kommentar"));
		wlaKommentar.setVerticalAlignment(SwingConstants.NORTH);
		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));

		wefKommentar.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wlaBonitaet = new WrapperLabel();
		wlaBonitaet.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.bonitaet.check.last"));
		wdfBonitaet = new WrapperDateField();

		wlaKundennummer = new WrapperLabel();
		wlaKundennummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kundennummer"));

		wlaLiefersperreAm = new WrapperLabel();
		wlaLiefersperreAm.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.liefersperre_am"));

		wdfLiefersperreAm = new WrapperDateField();

		wlaDefaultKopienRE = new WrapperLabel();
		wlaDefaultKopienRE.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.kopien_re"));
		wnfDefaultKopienRE = new WrapperNumberField();
		wnfDefaultKopienRE.setMinimumValue(new BigDecimal(KundeFac.MIN_KOPIEN));
		wnfDefaultKopienRE.setMaximumValue(new BigDecimal(KundeFac.MAX_KOPIEN));

		wlaDefaultKopienLS = new WrapperLabel();
		wlaDefaultKopienLS.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.kopien_ls"));

		wnfDefaultKopienLS = new WrapperNumberField();
		wnfDefaultKopienLS.setMinimumValue(new BigDecimal(KundeFac.MIN_KOPIEN));
		wnfDefaultKopienLS.setMaximumValue(new BigDecimal(KundeFac.MAX_KOPIEN));

		wbtKostenstelle = new WrapperButton();
		wbtKostenstelle.setActionCommand(ACTION_SPECIAL_FLR_KOSTENSTELLE);
		wbtKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbtKostenstelle.setHorizontalAlignment(SwingConstants.CENTER);
		wbtKostenstelle.addActionListener(this);

		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryFieldDB(true);

		wlaMitarbeiteranzahl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.mitarbeiter"));
		wnfMitarbeiteranzahl = new WrapperNumberField();
		wnfMitarbeiteranzahl.setMinimumValue(new BigDecimal(
				KundeFac.MIN_MITARBEITER_ANZAHL));
		wnfMitarbeiteranzahl.setMaximumValue(new BigDecimal(
				KundeFac.MAX_MITARBEITER_ANZAHL));
		wnfMitarbeiteranzahl.setFractionDigits(0);

		wlaTour = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.tour"));
		wtfTour = new WrapperTextField(KundeFac.MAX_TOUR);

		wlaLieferantennummer = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.lieferantennr"));
		wtfLieferantennummer = new WrapperTextField(KundeFac.MAX_LIEFERANTENNR);

		wlaABC = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.abc"));
		wcoABC = new WrapperComboBox();

		wlaAGBUebermittelung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.kund.agb_uebermittelt_am"));
		wdfAGBUebermittelung = new WrapperDateField();

		wlaFremsystemnr = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.kund.fremdsystemnr"));
		wtfFremsystemnr = new WrapperTextField(KundeFac.MAX_FREMDSYSTEMNR);

		wcoWaehrung = new WrapperComboBox();
		wcoWaehrung.setMandatoryFieldDB(true);

		wlaWaehrung = new WrapperLabel();
		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.waehrung"));

		wlaRabattsatz = new WrapperLabel();
		wlaRabattsatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.rabattsumme"));
		wnfRabattsatz = new WrapperNumberField();
		wnfRabattsatz.setMaximumValue(KundeFac.MAX_RABATTSATZ);
		wnfRabattsatz.setMinimumValue(KundeFac.MIN_RABATTSATZ);
		wnfRabattsatz.setFractionDigits(KundeFac.FRACTION_RABATTSATZ);

		wlaLieferdauer = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kund.lieferdauer"));
		wnfLieferdauer = new WrapperNumberField();
		wnfLieferdauer.setFractionDigits(0);
		wnfLieferdauer.setMandatoryField(true);

		wlaZessionsfaktor = new WrapperLabel();
		wlaZessionsfaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kund.zessionsfaktor"));
		wnfZessionsfaktor = new WrapperNumberField();
		wnfZessionsfaktor.setMaximumValue(KundeFac.MAX_ZESSIONSFAKTOR);
		wnfZessionsfaktor.setMinimumValue(KundeFac.MIN_ZESSIONSFAKTOR);
		wnfZessionsfaktor.setFractionDigits(KundeFac.FRACTION_ZESSIONSFAKTOR);

		wlaEinheitProzentRabatt = new WrapperLabel();
		wlaEinheitProzentRabatt.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaEinheitProzentRabatt.setHorizontalAlignment(SwingConstants.LEFT);

		wlaKreditlimit = new WrapperLabel();
		wlaKreditlimit.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kreditlimit"));
		wnfKreditlimit = new WrapperNumberField();
		wnfKreditlimit
				.setMaximumValue(new BigDecimal(KundeFac.MAX_KREDITLIMIT));
		wnfKreditlimit.setMinimumValue(KundeFac.MIN_KREDITLIMIT);
		wnfKreditlimit.setFractionDigits(KundeFac.FRACTION_KREDITLIMIT);

		wlaKreditlimitWaehrung = new WrapperLabel();
		wlaKreditlimitWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		wtfProvisionsempfaenger = new WrapperTextField();
		wtfProvisionsempfaenger.setMandatoryFieldDB(true);
		wtfProvisionsempfaenger.setActivatable(false);

		wbuProvisionsempfaenger = new WrapperButton();
		wbuProvisionsempfaenger.setHorizontalAlignment(SwingConstants.CENTER);
		wbuProvisionsempfaenger.addActionListener(this);
		wbuProvisionsempfaenger
				.setActionCommand(ACTION_SPECIAL_FLR_PROVISIONSEMPFAENGER);
		wbuProvisionsempfaenger.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.kunde.button.provisionsempfaenger"));

		wtfLieferart = new WrapperTextField();
		wtfLieferart.setMandatoryFieldDB(true);
		wtfLieferart.setActivatable(false);

		wbuLieferart = new WrapperButton();
		wbuLieferart.setHorizontalAlignment(SwingConstants.CENTER);
		wbuLieferart.addActionListener(this);
		wbuLieferart.setActionCommand(ACTION_SPECIAL_FLR_LIEFERART);
		wbuLieferart.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart"));

		wlaHinweistextextern = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.hinweistext.extern"));
		wtfHinweistextextern = new WrapperTextField(KundeFac.MAX_HINWEIS);
		wlaHinweistextintern = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.hinweistext.intern"));
		wtfHinweistextintern = new WrapperTextField(KundeFac.MAX_HINWEIS);

		wlaEinheitProzentZessionsfaktor = new WrapperLabel();
		wlaEinheitProzentZessionsfaktor.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaEinheitProzentZessionsfaktor
				.setHorizontalAlignment(SwingConstants.LEFT);

		boolean bFibuInstalliert = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG);
		wbuDebitorenkontoAutomatisch = new WrapperGotoButton(
				WrapperGotoButton.GOTO_DEBITORENKONTO_AUSWAHL);
		wbuDebitorenkontoAutomatisch.getWrapperButtonGoTo().setVisible(
				bFibuInstalliert);
		wbuDebitorenkontoAutomatisch.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.debitorenkontoanlegen"));
		wbuDebitorenkontoAutomatisch.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.debitorenkontoanlegen.tooltip"));
		wbuDebitorenkontoAutomatisch
				.setActionCommand(ACTION_SPECIAL_DEBITORENKONTOAUTOMATISCH);
		wbuDebitorenkontoAutomatisch.addActionListener(this);

		wlaErwerbsberechtigung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kunde.erwerbsberechtigung"));
		wdfErwerbsberechtigung = new WrapperDateField();
		wlaBegruendung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kunde.begruendung"));
		wtfBegruendung = new WrapperTextField(KundeFac.MAX_HINWEIS);

		jpaWorkingOn = new JPanel(new MigLayout("wrap 8", "[25%][15%][10%][10%][10%][10%][10%][10%]"));
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// ab hier einhaengen.
		
		jpaWorkingOn.add(wbtKostenstelle, "growx");
		jpaWorkingOn.add(wtfKostenstelle, "growx");
		jpaWorkingOn.add(wlaWaehrung, "skip, growx");
		jpaWorkingOn.add(wcoWaehrung, "growx, span");
		
		jpaWorkingOn.add(wbtKontoerloese, "growx");
		jpaWorkingOn.add(wtfKontoerloese, "growx");

		if (DelegateFactory.getInstance().getFinanzDelegate()
				.getAnzahlDerFinanzaemter() > 1) {
			// CK Projekt 7930
			if (DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

				jpaWorkingOn.add(wlaAbweichendesfinanzamt, "growx, span 2");
				jpaWorkingOn.add(wtfAbweichendesustland, "growx, span");
			} else {
				jpaWorkingOn.add(wbuAbweichendesustland, "growx, span 2");
				jpaWorkingOn.add(wtfAbweichendesustland, "growx, span");
			}
		}
		
		jpaWorkingOn.add(wbuDebitorenkontoAutomatisch, "newline, growx");
		jpaWorkingOn.add(wtnfKontodebitoren, "growx");
		jpaWorkingOn.add(wlaKontodebitorenRangeVonBis, "growx, span 2");
		if (bReversecharge == true) {
			jpaWorkingOn.add(wcbReversecharge, "growx, span");
		}

		jpaWorkingOn.add(wcbIstREEmpfaenger, "newline, growx");

		jpaWorkingOn.add(wcbSammelRE, "growx, span 2");
		jpaWorkingOn.add(wcbREDruckMitRabatt, "growx, span");
		// Zeile.
		
		jpaWorkingOn.add(wcbPreiseAnLSAndrucken, "growx");
		jpaWorkingOn.add(wcbLSGewichtAngeben, "growx, span 2");
		jpaWorkingOn.add(wcbMonatrechnung, "growx, span 2");
		jpaWorkingOn.add(wcbZollpapiere, "growx, span");
		// Zeile.

		jpaWorkingOn.add(wcbMindermengenzuschlag, "growx");
		jpaWorkingOn.add(wcbAkzeptiertTeillieferung, "growx, span 2");
		jpaWorkingOn.add(wcbDistributor, "growx, span 2");
		jpaWorkingOn.add(wlaLieferdauer, "growx");
		jpaWorkingOn.add(wnfLieferdauer, "growx");

		WrapperLabel wlaEinheitTage = new WrapperLabel();
		wlaEinheitTage.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.tage"));
		wlaEinheitTage.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaEinheitTage, "growx, span");

		// Zeile.
		jpaWorkingOn.add(wbuZahlungsziel, "growx");
		jpaWorkingOn.add(wtfZahlungsziel, "growx, span 4");

		jpaWorkingOn.add(wlaRabattsatz, "growx");
		jpaWorkingOn.add(wnfRabattsatz, "growx");
		jpaWorkingOn.add(wlaEinheitProzentRabatt, "growx, span");

		// Zeile.
		jpaWorkingOn.add(wbuLieferart, "growx");
		jpaWorkingOn.add(wtfLieferart, "growx");

		jpaWorkingOn.add(wlaTour, "growx, span 2");
		jpaWorkingOn.add(wtfTour, "growx, span");
		// Zeile.
		jpaWorkingOn.add(wbuSpedition, "growx");
		jpaWorkingOn.add(wtfSpedition, "growx");
		jpaWorkingOn.add(wlaLieferantennummer, "growx, span 2");
		jpaWorkingOn.add(wtfLieferantennummer, "growx");

		jpaWorkingOn.add(wlaZessionsfaktor, "growx");
		jpaWorkingOn.add(wnfZessionsfaktor, "growx");
		jpaWorkingOn.add(wlaEinheitProzentZessionsfaktor, "growx, wrap");

		// Zeile
		jpaWorkingOn.add(wlaDefaultKopienRE, "growx");
		jpaWorkingOn.add(wnfDefaultKopienRE, "growx");
		jpaWorkingOn.add(wlaDefaultKopienLS, "growx, span 2");
		jpaWorkingOn.add(wnfDefaultKopienLS, "growx");
		jpaWorkingOn.add(wlaGarantieInMonaten, "growx");
		jpaWorkingOn.add(wnfGarantieInMonaten, "growx");
		jpaWorkingOn.add(wlaEinheitMonat, "growx, span");

		// Zeile
		jpaWorkingOn.add(wlaMitarbeiteranzahl, "growx");
		jpaWorkingOn.add(wnfMitarbeiteranzahl, "growx");
		jpaWorkingOn.add(wlaAGBUebermittelung, "growx, span 2");
		jpaWorkingOn.add(wdfAGBUebermittelung, "growx");
		jpaWorkingOn.add(wlaABC, "growx");
		jpaWorkingOn.add(wcoABC, "growx, wrap");

		// Zeile
		jpaWorkingOn.add(wlaFremsystemnr, "growx");
		jpaWorkingOn.add(wtfFremsystemnr, "growx");
		jpaWorkingOn.add(wlaLiefersperreAm, "growx, span 2");
		jpaWorkingOn.add(wdfLiefersperreAm, "growx");
		jpaWorkingOn.add(wlaKreditlimit, "growx");
		jpaWorkingOn.add(wnfKreditlimit, "growx");
		jpaWorkingOn.add(wlaKreditlimitWaehrung, "growx, wrap");

		// Zeile
		jpaWorkingOn.add(wlaKommentar, "growx");
		jpaWorkingOn.add(wefKommentar, "growx, span");

		// Zeile
		jpaWorkingOn.add(wtfKurznr, "growx");
		jpaWorkingOn.add(wcbGenerateKurznr, "growx");
		jpaWorkingOn.add(wbuAbbuchungslager, "growx 40, span, split 4");
		jpaWorkingOn.add(wtfAbbuchungslager, "growx 50");
		jpaWorkingOn.add(wbuZiellager, "growx 40");
		jpaWorkingOn.add(wtfZiellager, "growx 50");

		// Zeile
		jpaWorkingOn.add(wbuProvisionsempfaenger, "growx");
		jpaWorkingOn.add(wtfProvisionsempfaenger, "growx");
		jpaWorkingOn.add(wlaBonitaet, "growx, span 2");
		jpaWorkingOn.add(wdfBonitaet, "growx");

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);

		boolean bKundeMitNummer = (java.lang.Boolean) parameter
				.getCWertAsObject();
		if (bKundeMitNummer) {
			wnfKundennummer.setMandatoryField(true);
			jpaWorkingOn.add(wlaKundennummer, "growx");
			jpaWorkingOn.add(wnfKundennummer, "growx, span");
		}

		// Zeile.
		jpaWorkingOn.add(wlaHinweistextextern, "newline, growx");
		jpaWorkingOn.add(wtfHinweistextextern, "growx, span");
		// Zeile.
		jpaWorkingOn.add(wlaHinweistextintern, "growx");
		jpaWorkingOn.add(wtfHinweistextintern, "growx, span");
		ParametermandantDto parameterErwerb = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDE_ERWERBSBERECHTIGUNG_ANZEIGEN);
		boolean bErwerbsberechtigung = (java.lang.Boolean) parameterErwerb
				.getCWertAsObject();
		if (bErwerbsberechtigung) {
			// Wenn Parameter gesetzt Felder fuer Erwerbsberechtigung anzeigen
			jpaWorkingOn.add(wlaErwerbsberechtigung, "growx");
			jpaWorkingOn.add(wdfErwerbsberechtigung, "growx");
			jpaWorkingOn.add(wlaBegruendung, "skip 2, growx");
			jpaWorkingOn.add(wtfBegruendung, "growx, span");
		}
	}

	protected void dto2Components() throws Throwable {

		wtfKurznr.setText(getKundeDto().getCKurznr());

		wbuDebitorenkontoAutomatisch.setOKey(getKundeDto()
				.getIidDebitorenkonto());
		wcbZollpapiere.setShort(getKundeDto().getBZollpapier());
		// Erloeskonto
		if (getKundeDto().getIidErloeseKonto() != null) {
			KontoDtoSmall kontoDtoSmall = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKeySmall(
							getKundeDto().getIidErloeseKonto());
			wtfKontoerloese.setText(kontoDtoSmall.getCNr());
		} else {
			wtfKontoerloese.setText(null);
		}

		// Zahlungsziel
		if (getKundeDto().getZahlungszielIId() != null) {
			ZahlungszielDto zahlungszielDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(
							getKundeDto().getZahlungszielIId());
			if (zahlungszielDto != null) {
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungsziel.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungsziel.setText(null);
			}
		}

		// Ziellager
		if (getKundeDto().getPartnerDto().getLagerIIdZiellager() != null) {
			LagerDto lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							getKundeDto().getPartnerDto()
									.getLagerIIdZiellager());
			wtfZiellager.setText(lagerDto.getCNr());
		} else {
			wtfZiellager.setText(null);
		}
		// Abbuchungslager
		if (getKundeDto().getLagerIIdAbbuchungslager() != null) {
			LagerDto lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							getKundeDto().getLagerIIdAbbuchungslager());
			wtfAbbuchungslager.setText(lagerDto.getCNr());
		} else {
			wtfAbbuchungslager.setText(null);
		}

		// Kostenstelle
		if (getKundeDto().getKostenstelleIId() != null) {
			KostenstelleDto kostenstelleDto = null;
			kostenstelleDto = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(
							getKundeDto().getKostenstelleIId());
			wtfKostenstelle.setText(kostenstelleDto.getCBez());
		} else {
			wtfKontoerloese.setText(null);
		}

		if (DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			// das Finanzamt wir angezeigt wenn Modul FIBU
			if (getKundeDto().getIidDebitorenkonto() != null) {

				KontoDto kontoDto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								getKundeDto().getIidDebitorenkonto());

				MandantDto mandant = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());

				if (kontoDto.getFinanzamtIId().equals(
						mandant.getPartnerIIdFinanzamt())) {
					wtfAbweichendesustland.setText(null);
				} else {
					wtfAbweichendesustland.setText(DelegateFactory
							.getInstance()
							.getFinanzDelegate()
							.finanzamtFindByPrimaryKey(
									kontoDto.getFinanzamtIId(),
									mandant.getCNr()).getPartnerDto()
							.formatName());
				}
			}
		} else {
			// Abweichendes UST-Land
			if (getKundeDto().getPartnerDto().getLandIIdAbweichendesustland() != null) {
				LandDto landDto = DelegateFactory
						.getInstance()
						.getSystemDelegate()
						.landFindByPrimaryKey(
								getKundeDto().getPartnerDto()
										.getLandIIdAbweichendesustland());

				wtfAbweichendesustland.setText(landDto.getCLkz());
			} else {
				wtfAbweichendesustland.setText(null);
			}
		}

		SpediteurDto s = DelegateFactory.getInstance().getMandantDelegate()
				.spediteurFindByPrimaryKey(getKundeDto().getSpediteurIId());
		wtfSpedition.setText(s.getCNamedesspediteurs());

		wnfGarantieInMonaten.setInteger(getKundeDto().getIGarantieinmonaten());
		wcbMindermengenzuschlag.setShort(getKundeDto()
				.getBMindermengenzuschlag());
		wcbMonatrechnung.setShort(getKundeDto().getBMonatsrechnung());
		wcbSammelRE.setShort(getKundeDto().getBSammelrechnung());
		wcbIstREEmpfaenger.setShort(getKundeDto().getBIstreempfaenger());
		wcbPreiseAnLSAndrucken
				.setShort(getKundeDto().getBPreiseanlsandrucken());
		wcbREDruckMitRabatt.setShort(getKundeDto()
				.getBRechnungsdruckmitrabatt());
		wcbReversecharge.setShort(getKundeDto().getBReversecharge());
		wcbDistributor.setShort(getKundeDto().getBDistributor());
		wcbAkzeptiertTeillieferung.setShort(getKundeDto()
				.getBAkzeptiertteillieferung());
		wcbLSGewichtAngeben.setShort(getKundeDto().getBLsgewichtangeben());
		wdfBonitaet.setDate(getKundeDto().getTBonitaet());
		wdfLiefersperreAm.setDate(getKundeDto().getTLiefersperream());
		wnfDefaultKopienRE.setInteger(getKundeDto()
				.getIDefaultrekopiendrucken());
		wnfDefaultKopienLS.setInteger(getKundeDto()
				.getIDefaultlskopiendrucken());

		wnfLieferdauer.setInteger(getKundeDto().getILieferdauer());

		wefKommentar.setText(getKundeDto().getXKommentar());

		wnfMitarbeiteranzahl.setInteger(getKundeDto().getIMitarbeiteranzahl());

		wtfTour.setText(getKundeDto().getCTour());

		wdfAGBUebermittelung.setDate(getKundeDto().getTAgbuebermittelung());
		wtfFremsystemnr.setText(getKundeDto().getCFremdsystemnr());

		wcoWaehrung.setKeyOfSelectedItem(getKundeDto().getCWaehrung());

		wtfLieferantennummer.setText(getKundeDto().getCLieferantennr());

		wcoABC.setSelectedItem(getKundeDto().getCAbc());

		wnfRabattsatz.setDouble(getKundeDto().getFRabattsatz());

		wnfKreditlimit.setBigDecimal(getKundeDto().getNKreditlimit());
		wnfKundennummer.setInteger(getKundeDto().getIKundennummer());

		// Provisionsempfaenger
		wtfProvisionsempfaenger
				.setText(getPersonalName1(getInternalFrameKunde().getKundeDto()
						.getPersonaliIdProvisionsempfaenger()));

		LieferartDto lieferartDto = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(getKundeDto().getLieferartIId());
		wtfLieferart.setText(lieferartDto.getCNr());

		KontoDto k = null;
		if (getKundeDto().getIidDebitorenkonto() != null) {
			k = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKey(getKundeDto().getIidDebitorenkonto());
		}
		Integer iD = null;
		if (k != null && k.getCNr() != null) {
			iD = new Integer(Integer.parseInt(k.getCNr()));
		}
		wtnfKontodebitoren.setInteger(iD);

		boolean bhatBuchungenDrauf = false;

		if (getKundeDto().getIidDebitorenkonto() != null) {
			bhatBuchungenDrauf = DelegateFactory
					.getInstance()
					.getBuchenDelegate()
					.hatPartnerBuchungenAufKonto(getKundeDto().getPartnerIId(),
							getKundeDto().getIidDebitorenkonto());
		}
		// --true: lf hat bereits buchungen drauf
		wtnfKontodebitoren.setActivatable(!bhatBuchungenDrauf);
		wbuDebitorenkontoAutomatisch.setActivatable(!bhatBuchungenDrauf);

		wtfHinweistextextern.setText(getKundeDto().getSHinweisextern());
		wtfHinweistextintern.setText(getKundeDto().getSHinweisintern());
		wnfZessionsfaktor.setDouble(getKundeDto().getFZessionsfaktor());
		wdfErwerbsberechtigung.setDate(getKundeDto().getTErwerbsberechtigung());
		wtfBegruendung.setText(getKundeDto()
				.getCErwerbsberechtigungsbegruendung());

	}

	private KundeDto getKundeDto() {
		return getInternalFrameKunde().getKundeDto();
	}

	protected void components2Dto() throws Throwable {

		getKundeDto().setIGarantieinmonaten(wnfGarantieInMonaten.getInteger());
		getKundeDto().setBMindermengenzuschlag(
				wcbMindermengenzuschlag.getShort());
		getKundeDto().setBMonatsrechnung(wcbMonatrechnung.getShort());
		getKundeDto().setBSammelrechnung(wcbSammelRE.getShort());
		getKundeDto().setBIstreempfaenger(wcbIstREEmpfaenger.getShort());
		getKundeDto()
				.setBPreiseanlsandrucken(wcbPreiseAnLSAndrucken.getShort());
		getKundeDto().setBRechnungsdruckmitrabatt(
				wcbREDruckMitRabatt.getShort());
		if (bReversecharge == true) {
			getKundeDto().setBReversecharge(wcbReversecharge.getShort());
		} else {
			getKundeDto().setBReversecharge(Helper.boolean2Short(false));
		}
		getKundeDto().setBDistributor(wcbDistributor.getShort());
		getKundeDto().setBAkzeptiertteillieferung(
				wcbAkzeptiertTeillieferung.getShort());
		getKundeDto().setBLsgewichtangeben(wcbLSGewichtAngeben.getShort());
		getKundeDto().setTBonitaet(wdfBonitaet.getDate());
		getKundeDto().setTLiefersperream(wdfLiefersperreAm.getDate());
		getKundeDto().setIDefaultrekopiendrucken(
				wnfDefaultKopienRE.getInteger());
		getKundeDto().setIDefaultlskopiendrucken(
				wnfDefaultKopienLS.getInteger());
		getKundeDto().setXKommentar(wefKommentar.getText());
		getKundeDto().setIMitarbeiteranzahl(wnfMitarbeiteranzahl.getInteger());
		getKundeDto().setCTour(wtfTour.getText());
		getKundeDto().setCLieferantennr(wtfLieferantennummer.getText());
		getKundeDto().setTAgbuebermittelung(wdfAGBUebermittelung.getDate());
		getKundeDto().setCFremdsystemnr(wtfFremsystemnr.getText());
		getKundeDto().setCWaehrung((String) wcoWaehrung.getKeyOfSelectedItem());
		getKundeDto().setCAbc((String) wcoABC.getKeyOfSelectedItem());
		getKundeDto().setFRabattsatz(wnfRabattsatz.getDouble());
		getKundeDto().setNKreditlimit(wnfKreditlimit.getBigDecimal());
		getKundeDto().setIDebitorenkontoAsIntegerNotiId(
				wtnfKontodebitoren.getInteger());
		getKundeDto().setSHinweisintern(wtfHinweistextintern.getText());
		getKundeDto().setSHinweisextern(wtfHinweistextextern.getText());
		getKundeDto().setFZessionsfaktor(wnfZessionsfaktor.getDouble());

		getKundeDto()
				.setBbGeneriereKurznr(
						new Boolean(Helper.short2boolean(wcbGenerateKurznr
								.getShort())));

		getKundeDto().setCKurznr(wtfKurznr.getText());
		getKundeDto().setIKundennummer(wnfKundennummer.getInteger());
		getKundeDto().setILieferdauer(wnfLieferdauer.getInteger());
		getKundeDto().setTErwerbsberechtigung(
				wdfErwerbsberechtigung.getTimestamp());
		getKundeDto().setCErwerbsberechtigungsbegruendung(
				wtfBegruendung.getText());
		getKundeDto().setBZollpapier(wcbZollpapiere.getShort());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {

			Object key = getKundeDto().getIId();

			if (key == null) {
				throw new Exception("key == null");
			}

			// muss: alter Kunde; einlesen.
			getInternalFrameKunde().setKundeDto(
					DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey((Integer) key));

			initPanel();

			setDefaults();

			wcbGenerateKurznr
					.setActivatable(getKundeDto().getCKurznr() == null);
			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getKundeDto().getPartnerDto().formatFixTitelName1Name2());

			setStatusbar();
		}
	}

	private InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

	/**
	 * Setze alle Defaultwerte.
	 * 
	 * @throws Throwable
	 */
	protected void setDefaults() throws Throwable {
		wnfDefaultKopienLS.setInteger(new Integer(1));
		wnfDefaultKopienRE.setInteger(new Integer(1));
	}

	private void initPanel() throws ExceptionLP, Throwable {
		Integer iiKontoDebitorenVon = null;
		Integer iiKontoDebitorenBis = null;

		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());
		Map<String, String> mABC = new TreeMap<String, String>();
		mABC.put("A", "A");
		mABC.put("B", "B");
		mABC.put("C", "C");
		wcoABC.setMap(mABC);

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_DEBITORENNUMMER_VON);

		iiKontoDebitorenVon = (Integer) parametermandantDto.getCWertAsObject();

		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_DEBITORENNUMMER_BIS);

		iiKontoDebitorenBis = (Integer) parametermandantDto.getCWertAsObject();

		wlaKontodebitorenRangeVonBis.setText(iiKontoDebitorenVon + " - "
				+ iiKontoDebitorenBis);
		wtnfKontodebitoren.setMinimumValue(new Integer(iiKontoDebitorenVon
				.intValue()));
		wtnfKontodebitoren.setMaximumValue(new Integer(iiKontoDebitorenBis
				.intValue()));

		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(getKundeDto().getMandantCNr());
		wlaKreditlimitWaehrung.setText(mandantDto.getWaehrungCNr());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			try {
				DelegateFactory.getInstance().getKundeDelegate()
						.updateKunde(getKundeDto());
			} catch (ExceptionLP efc) {
				int iJNC = -1;
				if (efc.getICode() == EJBExceptionLP.WARNUNG_KTO_BESETZT) {
					// einzmehrz: 0
					String sMsg = "";
					String sP = "";
					sP += " | ";
					for (int i = 0; i < efc.getAlInfoForTheClient().size(); i++) {
						// svr2clt: 1 hier werden die Daten gelesen.

						if (sP.length() < 1000) {

							sP += efc.getAlInfoForTheClient().get(i) + " | ";
						} else {
							sP += ".....";
							break;
						}
					}
					Object pattern[] = { sP };
					String sFS[] = {
							LPMain.getInstance().getTextRespectUISPr(
									"part.kd.konto_besetzt_1"),
							LPMain.getInstance().getTextRespectUISPr(
									"part.kd.konto_besetzt_n") };
					double limits[] = { 1, 2 };
					ChoiceFormat cf = new ChoiceFormat(limits, sFS);
					sMsg = cf.format(efc.getAlInfoForTheClient().size() > 1 ? 2
							: 1);
					sMsg = MessageFormat.format(sMsg, pattern);
					iJNC = DialogFactory.showMeldung(sMsg, "",
							javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
				} else {
					throw efc;
				}
				if (iJNC == javax.swing.JOptionPane.YES_OPTION) {
					getKundeDto().setUpdateModeDebitorenkonto(
							KundeDto.I_UPD_DEBITORENKONTO_UPDATE);
					DelegateFactory.getInstance().getKundeDelegate()
							.updateKunde(getKundeDto());
				} else if (iJNC == javax.swing.JOptionPane.NO_OPTION) {
					return;
				} else {
					return;
				}
			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKontoerloese) {
				// JA ist mein lokaler FLR
				// hol jetzt den kontokey
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					getKundeDto().setIidErloeseKonto(iId);
					KontoDto kontoDto = DelegateFactory.getInstance()
							.getFinanzDelegate().kontoFindByPrimaryKey(iId);
					wtfKontoerloese.setText(kontoDto.getCNr());
				}
			}

			else if (e.getSource() == panelQueryFLRZahlungsziel) {
				// JA ist mein lokaler FLR
				// hol jetzt den kontokey
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					getKundeDto().setZahlungszielIId(iId);
					ZahlungszielDto zahlungszielDto = null;
					zahlungszielDto = DelegateFactory.getInstance()
							.getMandantDelegate()
							.zahlungszielFindByPrimaryKey(iId);
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRAbweichendesustland) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landFindByPrimaryKey((Integer) key);
				getKundeDto().getPartnerDto().setLandIIdAbweichendesustland(
						landDto.getIID());
				wtfAbweichendesustland.setText(landDto.getCLkz());
			}

			else if (e.getSource() == panelQueryFLRKostenstelle) {
				// hol jetzt den kontokey
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					getKundeDto().setKostenstelleIId(iId);
					KostenstelleDto kostenstelleDto = DelegateFactory
							.getInstance().getSystemDelegate()
							.kostenstelleFindByPrimaryKey(iId);
					wtfKostenstelle.setText(kostenstelleDto.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRSpediteur) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					getKundeDto().setSpediteurIId(iId);
					SpediteurDto s = DelegateFactory.getInstance()
							.getMandantDelegate()
							.spediteurFindByPrimaryKey(iId);

					wtfSpedition.setText(s.getCNamedesspediteurs());
				}
			} else if (e.getSource() == panelQueryFLRLieferart) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				getKundeDto().setLieferartIId(iId);

				if (iId != null) {
					LieferartDto lieferartDto = DelegateFactory.getInstance()
							.getLocaleDelegate().lieferartFindByPrimaryKey(iId);
					wtfLieferart.setText(lieferartDto.getCNr());
				}
			} else if (e.getSource() == panelQueryFLRProvisionsempfaenger) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				if (iId != null) {
					getInternalFrameKunde().getKundeDto()
							.setPersonaliIdProvisionsempfaenger(iId);
					wtfProvisionsempfaenger.setText(getPersonalName1(iId));
				}
			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfZiellager.setText(lagerDto.getCNr());
				getKundeDto().getPartnerDto().setLagerIIdZiellager(
						lagerDto.getIId());
			} else if (e.getSource() == panelQueryFLRAbbuchungsLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfAbbuchungslager.setText(lagerDto.getCNr());
				getKundeDto().setLagerIIdAbbuchungslager(lagerDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAbweichendesustland) {
				wtfAbweichendesustland.setText(null);
				getKundeDto().getPartnerDto().setLandIIdAbweichendesustland(
						null);
			} else if (e.getSource() == panelQueryFLRLager) {
				wtfZiellager.setText(null);
				getKundeDto().getPartnerDto().setLagerIIdZiellager(null);
			} else if (e.getSource() == panelQueryFLRKontoerloese) {
				wtfKontoerloese.setText(null);
				getKundeDto().setIidErloeseKonto(null);
			}

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		super.eventActionUpdate(aE, false);

		wcbGenerateKurznr.setActivatable(getKundeDto().getCKurznr() == null);

		if (DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KONTOERLOESE)) {
			panelQueryFLRKontoerloese = FinanzFilterFactory.getInstance()
					.createPanelFLRFinanzKonto(getInternalFrame(),
							getKundeDto().getIidErloeseKonto(), true);
			new DialogQuery(panelQueryFLRKontoerloese);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL)) {
			panelQueryFLRZahlungsziel = SystemFilterFactory.getInstance()
					.createPanelFLRZahlungsziel(getInternalFrame(),
							getKundeDto().getZahlungszielIId());
			new DialogQuery(panelQueryFLRZahlungsziel);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_PROVISIONSEMPFAENGER)) {
			Integer iId = getInternalFrameKunde().getKundeDto()
					.getPersonaliIdProvisionsempfaenger();

			panelQueryFLRProvisionsempfaenger = PersonalFilterFactory
					.getInstance().createPanelFLRPersonal(getInternalFrame(),
							true, false, iId);

			new DialogQuery(panelQueryFLRProvisionsempfaenger);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERART)) {
			panelQueryFLRLieferart = SystemFilterFactory.getInstance()
					.createPanelFLRLieferart(getInternalFrame(),
							getKundeDto().getLieferartIId());
			new DialogQuery(panelQueryFLRLieferart);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SPEDITEUR)) {
			panelQueryFLRSpediteur = SystemFilterFactory.getInstance()
					.createPanelFLRSpediteur(getInternalFrame(),
							getKundeDto().getSpediteurIId());
			new DialogQuery(panelQueryFLRSpediteur);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KOSTENSTELLE)) {
			panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
					.createPanelFLRKostenstelle(getInternalFrame(), false,
							false);
			// vorbesetzen
			if (getKundeDto().getKostenstelleIId() != null) {
				panelQueryFLRKostenstelle.setSelectedId(getKundeDto()
						.getKostenstelleIId());
			}
			new DialogQuery(panelQueryFLRKostenstelle);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_USTLAND)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };

			panelQueryFLRAbweichendesustland = new PanelQueryFLR(null, null,
					QueryParameters.UC_ID_LAND, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("title.landauswahlliste"));
			if (getKundeDto().getPartnerDto().getLandIIdAbweichendesustland() != null) {
				panelQueryFLRAbweichendesustland.setSelectedId(getKundeDto()
						.getPartnerDto().getLandIIdAbweichendesustland());
			}
			new DialogQuery(panelQueryFLRAbweichendesustland);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_DEBITORENKONTOAUTOMATISCH)) {
			boolean bKontoAnlegen = true;
			if (getKundeDto().getIidDebitorenkonto() != null) {
				bKontoAnlegen = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"part.kunde.warning.hatschoneindebitorenkonto"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.frage"));
			}
			if (bKontoAnlegen) {
				// nur Nummer generieren!
				String kontoNr = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.createDebitorenkontoNummerZuKundenAutomatisch(
								getKundeDto().getIId(),
								wtnfKontodebitoren.getText());
				wtnfKontodebitoren.setText(kontoNr);
			}
			if (getKundeDto().getIidDebitorenkonto() != null) {
				if (DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

					KontoDto kontoDto = DelegateFactory
							.getInstance()
							.getFinanzDelegate()
							.kontoFindByPrimaryKey(
									getKundeDto().getIidDebitorenkonto());

					MandantDto mandant = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mandantFindByPrimaryKey(
									LPMain.getTheClient().getMandant());

					if (kontoDto.getFinanzamtIId().equals(
							mandant.getPartnerIIdFinanzamt())) {
						wtfAbweichendesustland.setText(null);
					} else {
						wtfAbweichendesustland.setText(DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.finanzamtFindByPrimaryKey(
										kontoDto.getFinanzamtIId(),
										mandant.getCNr()).getPartnerDto()
								.formatName());
					}
				}
			}

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ZIELLAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE)) {
			dialogQueryAbbuchungsLagerFromListe(e);
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KUNDE;
	}

	private void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getKundeDto().getPersonalAnlegenIId());
		setStatusbarPersonalIIdAendern(getKundeDto().getPersonalAendernIId());
		setStatusbarTAendern(getKundeDto().getTAendern());
		setStatusbarTAnlegen(getKundeDto().getTAnlegen());
	}

	private String getPersonalName1(Integer key) throws Throwable {

		String ret = null;
		if (key != null) {
			PersonalDto personalDto = DelegateFactory.getInstance()
					.getPersonalDelegate().personalFindByPrimaryKey(key);
			ret = personalDto.getPartnerDto().formatAnrede();
		}
		return ret;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbtKostenstelle;
	}

}
