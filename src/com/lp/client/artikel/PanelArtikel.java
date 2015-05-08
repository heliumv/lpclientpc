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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.MultipleImageViewer;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.PanelFilterKriteriumDirekt;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.SiWertParser;
import com.lp.util.siprefixparser.BigDecimalSI;

public class PanelArtikel extends PanelBasis {

	private static final long serialVersionUID = 1L;
	private InternalFrameArtikel internalFrameArtikel = null;
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private JPanel jpaPanelWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperButton wbuKurzbezeichnung = new WrapperButton();
	private WrapperLabel wlaKurzbezeichnung = new WrapperLabel();
	private WrapperButton wbuBezeichnung = new WrapperButton();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperButton wbuZusatzbez = new WrapperButton();
	private WrapperLabel wlaZusatzbez = new WrapperLabel();
	private WrapperButton wbuZusatzbez2 = new WrapperButton();
	private WrapperLabel wlaZusatzbez2 = new WrapperLabel();
	private WrapperLabel wlaEinheit = new WrapperLabel();
	private WrapperLabel wlaReferenznummer = new WrapperLabel();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperTextField wtfKurzbezeichnung = new WrapperTextField();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperTextField wtfZusatzbez = new WrapperTextField();
	private WrapperTextField wtfZusatzbez2 = new WrapperTextField();
	private WrapperComboBox wcoEinheit = new WrapperComboBox();
	private WrapperTextField wtfReferenznummer = new WrapperTextField();
	private WrapperButton wbuArtikelklasse = new WrapperButton();
	private WrapperButton wbuArtikelgruppe = new WrapperButton();
	private WrapperTextField wtfArtikelklasse = new WrapperTextField();
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
	private WrapperButton wbuGeneriereArtikelnummer = new WrapperButton();
	private WrapperTextField wtfHerstellerKuerzel = new WrapperTextField();

	private WrapperLabel wlaVorzugsteil = new WrapperLabel();
	private WrapperComboBox wcoVorzugsteil = new WrapperComboBox();

	private WrapperButton wbuHersteller = new WrapperButton();
	private WrapperTextField wtfHersteller = new WrapperTextField();
	private WrapperLabel wlaArtikelart = new WrapperLabel();
	private WrapperComboBox wcoArtikelart = new WrapperComboBox();
	private WrapperComboBox wcoMehrwertsteuer = new WrapperComboBox();
	private WrapperLabel wlaMehrwertsteuersatz = new WrapperLabel();

	private WrapperCheckBox wcbBestellmengeneinheitInvers = new WrapperCheckBox();

	private WrapperLabel wlaDefaultbezeichnungen = new WrapperLabel();
	Integer defaultMwstsatz = null;
	private MultipleImageViewer pi = new MultipleImageViewer(null);

	private WrapperLabel wlaArtikelBezStd = new WrapperLabel();
	private WrapperTextField wtfArtikelBezStd = new WrapperTextField();
	private WrapperLabel wlaArtikelZBezStd = new WrapperLabel();
	private WrapperLabel wlaKurzbezeichnungStd = new WrapperLabel();
	private WrapperTextField wtfKurzbezeichnungStd = new WrapperTextField();
	private WrapperTextField wtfArtikelZBezStd = new WrapperTextField();
	private WrapperLabel wlaZBez2Std = new WrapperLabel();
	private WrapperTextField wtfArtikelZBez2Std = new WrapperTextField();

	private WrapperSelectField wsfShopgruppe = new WrapperSelectField(
			WrapperSelectField.SHOPGRUPPE, getInternalFrame(), true);

	private WrapperKeyValueField wkvfLagerstand = null;
	private WrapperKeyValueField wkvfReserviert = null;
	private WrapperKeyValueField wkvfFehlmenge = null;
	private WrapperKeyValueField wkvfVerfuegbar = null;

	private WrapperKeyValueField wkvfRahmenreserviert = null;
	private WrapperKeyValueField wkvfInfertigung = null;
	private WrapperKeyValueField wkvfBestellt = null;
	private WrapperKeyValueField wkvfRahmenbestellt = null;
	private WrapperKeyValueField wkvfRahmenbedarf = null;
	private WrapperKeyValueField wkvfPaternoster = null;

	protected WrapperGotoButton wbuStkl = new WrapperGotoButton(
			WrapperGotoButton.GOTO_STUECKLISTE_DETAIL);

	private WrapperLabel wlaEinheitBestellung = new WrapperLabel();
	private WrapperComboBox wcoEinheitBestellung = new WrapperComboBox();
	private WrapperLabel wlaUmrechnungsfaktor = new WrapperLabel();
	private WrapperNumberField wnfUmrechnungsfaktor = new WrapperNumberField();

	private WrapperLabel wlaSperren = new WrapperLabel();

	private WrapperLabel wlaIndex = new WrapperLabel();
	private WrapperTextField wtfIndex = new WrapperTextField();
	private WrapperLabel wlaRevision = new WrapperLabel();
	private WrapperTextField wtfRevision = new WrapperTextField();

	private WrapperSelectField wsfLiefergruppe = new WrapperSelectField(
			WrapperSelectField.LIEFERGRUPPE, getInternalFrame(), true);

	private PanelQueryFLR panelQueryFLRHersteller = null;
	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;

	private WrapperLabel wlaSiWert = new WrapperLabel();

	private boolean bPositionskontierung = false;

	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperCheckBox wcbReineMannzeit = new WrapperCheckBox();
	private WrapperCheckBox wcbNurZurInfo = new WrapperCheckBox();

	private WrapperCheckBox wcbKalkulatorisch = new WrapperCheckBox();

	private boolean bKeinFocusLost = true;

	private boolean bPaternosterVerfuegbar = false;

	static final public String ACTION_SPECIAL_HERSTELLER_FROM_LISTE = "action_hersteller_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE = "action_artikelgruppe_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE = "action_artikelklasse_from_liste";
	static final public String ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER = "ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER";

	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2 = "ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2";

	static final public String ACTION_SPECIAL_STKL_NEU_ERZEUGEN = "ACTION_SPECIAL_STKL_NEU_ERZEUGEN";

	private PanelQueryFLR panelQueryFLRVorschlagstextKBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextZBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextZBEZ2 = null;

	private SiWertParser siwertParser;
	// PJ18691
	private static final ImageIcon DOKUMENTE = HelperClient
			.createImageIcon("document_attachment_green16x16.png");
	private static final ImageIcon KEINE_DOKUMENTE = HelperClient
			.createImageIcon("document_attachment16x16.png");
	private JButton jbDokumente;
	public final static String MY_OWN_NEW_DOKUMENTENABLAGE = PanelBasis.LEAVEALONE
			+ "DOKUMENTENABLAGE";
	public final static String MY_OWN_NEW_GOTO_SOKO = PanelBasis.LEAVEALONE
			+ "GOTO_SOKO";

	public PanelArtikel(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;

		siwertParser = new SiWertParser(getSiOhneEinheitenFromParam(),
				getSiEinheitenFromParam());

		jbInit();
		setDefaults();
		initComponents();
	}

	private void dialogQueryHerstellerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRHersteller = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_ARTIKELHERSTELLER, aWhichButtonIUse,
				internalFrameArtikel,
				LPMain.getTextRespectUISPr("title.herstellerauswahlliste"));
		panelQueryFLRHersteller
				.befuellePanelFilterkriterienDirekt(ArtikelFilterFactory
						.getInstance().createFKDHersteller(),
						ArtikelFilterFactory.getInstance()
								.createFKDHerstellerPartner());
		panelQueryFLRHersteller.setSelectedId(internalFrameArtikel
				.getArtikelDto().getHerstellerIId());
		new DialogQuery(panelQueryFLRHersteller);
	}

	private void dialogQueryArtikelgruppeFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelgruppe(getInternalFrame(),
						internalFrameArtikel.getArtikelDto().getArtgruIId());
		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	private void dialogQueryArtikelklasseFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelklasse(getInternalFrame(),
						internalFrameArtikel.getArtikelDto().getArtklaIId());
		new DialogQuery(panelQueryFLRArtikelklasse);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_HERSTELLER_FROM_LISTE)) {
			dialogQueryHerstellerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE)) {
			dialogQueryArtikelgruppeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE)) {
			dialogQueryArtikelklasseFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER)) {
			wtfArtikelnummer.setText(DelegateFactory.getInstance()
					.getArtikelDelegate()
					.generiereNeueArtikelnummer(wtfArtikelnummer.getText()));
		} else if (e.getActionCommand().equals(MY_OWN_NEW_GOTO_SOKO)) {

			FilterKriterium[] fk = new FilterKriterium[] { new FilterKriterium(
					"kundesoko.flrartikel.i_id", false, internalFrameArtikel
							.getArtikelDto().getIId() + "",
					FilterKriterium.OPERATOR_EQUAL, false) };

			getInternalFrame()
					.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_KUNDENARTIKELNUMMERN,
							TabbedPaneKundenartikelnummern.IDX_PANEL_KUNDENARTIKELNUMMERN,
							null, null, fk);

			if (internalFrameArtikel.getTabbedPaneKundenartikelnummern() != null) {
				LinkedHashMap<Integer, PanelFilterKriteriumDirekt> hm = internalFrameArtikel
						.getTabbedPaneKundenartikelnummern().preislistennameTop
						.getHmDirektFilter();
				Iterator<Integer> it = hm.keySet().iterator();
				while (it.hasNext()) {
					PanelFilterKriteriumDirekt fdk = hm.get(it.next());
					if (fdk.fkd
							.equals(internalFrameArtikel
									.getTabbedPaneKundenartikelnummern().fkdArtikelnummer))
						fdk.wtfFkdirektValue1.setText(internalFrameArtikel
								.getArtikelDto().getCNr());
				}
				internalFrameArtikel.getTabbedPaneKundenartikelnummern().preislistennameTop
						.eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_DOKUMENTENABLAGE)) {
			PrintInfoDto values = DelegateFactory
					.getInstance()
					.getJCRDocDelegate()
					.getPathAndPartnerAndTable(
							internalFrameArtikel.getArtikelDto().getIId(),
							QueryParameters.UC_ID_ARTIKELLISTE);

			DocPath docPath = values.getDocPath();
			Integer iPartnerIId = values.getiId();
			String sTable = values.getTable();
			if (docPath != null) {
				PanelDokumentenablage panelDokumentenverwaltung = new PanelDokumentenablage(
						getInternalFrame(), internalFrameArtikel
								.getArtikelDto().getIId().toString(), docPath,
						sTable, internalFrameArtikel.getArtikelDto().getIId()
								.toString(), true, iPartnerIId);
				getInternalFrame().showPanelDialog(panelDokumentenverwaltung);
				getInternalFrame().addItemChangedListener(
						panelDokumentenverwaltung);
			} else {
				// Show Dialog
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("jcr.hinweis.keinpfad"));
			}
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ)) {
			panelQueryFLRVorschlagstextBEZ = ArtikelFilterFactory.getInstance()
					.createPanelFLRVorschlagstext(getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextBEZ);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ)) {
			panelQueryFLRVorschlagstextKBEZ = ArtikelFilterFactory
					.getInstance().createPanelFLRVorschlagstext(
							getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextKBEZ);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ)) {
			panelQueryFLRVorschlagstextZBEZ = ArtikelFilterFactory
					.getInstance().createPanelFLRVorschlagstext(
							getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextZBEZ);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2)) {
			panelQueryFLRVorschlagstextZBEZ2 = ArtikelFilterFactory
					.getInstance().createPanelFLRVorschlagstext(
							getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextZBEZ2);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_STKL_NEU_ERZEUGEN)) {
			if (DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							com.lp.server.benutzer.service.RechteFac.RECHT_STK_STUECKLISTE_CUD)) {
				StuecklisteDto stuecklisteDto = new StuecklisteDto();
				stuecklisteDto.setArtikelIId(internalFrameArtikel
						.getArtikelDto().getIId());
				stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT,
								ParameterFac.KATEGORIE_FERTIGUNG,
								LPMain.getTheClient().getMandant());

				if (parameter.getCWertAsObject() != null) {
					stuecklisteDto.setNDefaultdurchlaufzeit(new BigDecimal(
							(Integer) parameter.getCWertAsObject()));
				}

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN,
								ParameterFac.KATEGORIE_STUECKLISTE,
								LPMain.getTheClient().getMandant());
				stuecklisteDto.setBAusgabeunterstueckliste(Helper
						.boolean2Short((Boolean) parameter.getCWertAsObject()));

				stuecklisteDto.setLagerIIdZiellager(DelegateFactory
						.getInstance().getLagerDelegate()
						.getHauptlagerDesMandanten().getIId());

				MandantDto mDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());

				if (mDto.getKundeIIdStueckliste() != null) {

					KundeDto kundeDto = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(
									mDto.getKundeIIdStueckliste());

					stuecklisteDto.setPartnerIId(kundeDto.getPartnerIId());

				}

				stuecklisteDto
						.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG,
								ParameterFac.KATEGORIE_STUECKLISTE,
								LPMain.getTheClient().getMandant());
				stuecklisteDto.setBMaterialbuchungbeiablieferung(Helper
						.boolean2Short((Boolean) parameter.getCWertAsObject()));

				stuecklisteDto.setIErfassungsfaktor(1);

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_FERTIGUNGSGRUPPE_VORBESETZEN,
								ParameterFac.KATEGORIE_STUECKLISTE,
								LPMain.getTheClient().getMandant());

				if ((Boolean) parameter.getCWertAsObject()) {

					FertigungsgruppeDto[] fertigungsgruppeDtos = DelegateFactory
							.getInstance().getStuecklisteDelegate()
							.fertigungsgruppeFindByMandantCNr();

					if (fertigungsgruppeDtos.length > 0) {

						stuecklisteDto
								.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
										.getIId());
					}
				}

				Integer stuecklisteIId = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.createStueckliste(stuecklisteDto);

				wbuStkl.setOKey(stuecklisteIId);
				wbuStkl.actionPerformed(new ActionEvent(this, 1,
						WrapperGotoButton.ACTION_GOTO));
			}
		}

		if (e.getSource().equals(wcoArtikelart)) {
			if (wcoArtikelart.getKeyOfSelectedItem() != null
					&& wcoArtikelart.getKeyOfSelectedItem().equals(
							ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				wcbNurZurInfo.setVisible(true);
				wcbReineMannzeit.setVisible(true);
			} else {
				wcbNurZurInfo.setVisible(false);
				wcbReineMannzeit.setVisible(false);
			}
			jpaPanelWorkingOn.validate();
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRHersteller) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				HerstellerDto herstellerDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.herstellerFindBdPrimaryKey((Integer) key);
				wtfHersteller.setText(herstellerDto.getPartnerDto()
						.formatFixName1Name2());
				wtfHerstellerKuerzel.setText(herstellerDto.getCNr());
				internalFrameArtikel.getArtikelDto().setHerstellerIId(
						herstellerDto.getIId());
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artklaFindByPrimaryKey((Integer) key);
				wtfArtikelklasse.setText(artklaDto.getBezeichnung());
				internalFrameArtikel.getArtikelDto().setArtklaIId(
						artklaDto.getIId());

			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getBezeichnung());
				internalFrameArtikel.getArtikelDto().setArtgruIId(
						artgruDto.getIId());
			}

			else if (e.getSource() == panelQueryFLRVorschlagstextBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory
						.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfBezeichnung.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextKBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory
						.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfKurzbezeichnung.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextZBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory
						.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfZusatzbez.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextZBEZ2) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory
						.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfZusatzbez2.setText(vorschlagstextDto.getCBez());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {

				wtfArtikelgruppe.setText(null);

				internalFrameArtikel.getArtikelDto().setArtgruIId(null);
				internalFrameArtikel.getArtikelDto().setArtgruDto(null);
			}
			if (e.getSource() == panelQueryFLRArtikelklasse) {

				wtfArtikelklasse.setText(null);

				internalFrameArtikel.getArtikelDto().setArtklaDto(null);
				internalFrameArtikel.getArtikelDto().setArtklaIId(null);
			}
			if (e.getSource() == panelQueryFLRHersteller) {

				wtfHersteller.setText(null);
				internalFrameArtikel.getArtikelDto().setHerstellerIId(null);
				wtfHerstellerKuerzel.setText(null);
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameArtikel.getArtikelDto() != null) {
			key = internalFrameArtikel.getArtikelDto().getIId();
		}
		wsfShopgruppe.setAusgeschlosseneIds(null);
		wlaSiWert.setText("");
		if (key != null && !key.equals(LPMain.getLockMeForNew())) {
			wbuStkl.setVisible(true);
			internalFrameArtikel.setArtikelDto(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							internalFrameArtikel.getArtikelDto().getIId()));
			wsfShopgruppe.setAusgeschlosseneIds(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.getBereitsVerwendeteShopgruppen(
							internalFrameArtikel.getArtikelDto().getIId()));
			if (bPositionskontierung == true) {
				if (defaultMwstsatz == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("artikel.error.positionkoniertungkeindefaultmwstsatz"));
					LPMain.getInstance().getDesktop()
							.closeFrame(getInternalFrame());
				} else {

					if (internalFrameArtikel.getArtikelDto()
							.getMwstsatzbezIId() == null) {

						internalFrameArtikel.getArtikelDto().setMwstsatzbezIId(
								defaultMwstsatz);

						DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.updateArtikel(
										internalFrameArtikel.getArtikelDto());
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("artikel.error.positionkoniertungmwstsatzupgedated"));
					}

				}
			}

			dto2Components();
			String sBezeichnung = "";
			if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() != null) {
				sBezeichnung = internalFrameArtikel.getArtikelDto()
						.getArtikelsprDto().getCBez();
			}
			if (sBezeichnung == null) {
				sBezeichnung = "";
			}

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					internalFrameArtikel.getArtikelDto().getCNr() + ", "
							+ sBezeichnung);
		} else {

			leereAlleFelder(this);
			clearStatusbar();

			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

			// Default MWST-Satz setzen
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ,
							ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());

			if (defaultMwstsatz != null) {
				defaultMwstsatz = (Integer) parameter.getCWertAsObject();
				wcoMehrwertsteuer.setKeyOfSelectedItem(defaultMwstsatz);
			}

		}
	}

	private void jbInit() throws Throwable {

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wlaArtikelnummer.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));
		wtfArtikelnummer.setToken("artikelnummer");

		wbuKurzbezeichnung.setText(LPMain
				.getTextRespectUISPr("artikel.kurzbez"));
		wtfKurzbezeichnung.setToken("kurzbez");
		wbuKurzbezeichnung.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ);
		wbuKurzbezeichnung.addActionListener(this);

		wlaKurzbezeichnung.setText(LPMain
				.getTextRespectUISPr("artikel.kurzbez"));

		wbuBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wbuBezeichnung.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ);
		wbuBezeichnung.addActionListener(this);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setToken("bezeichnung");

		wbuZusatzbez
				.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wbuZusatzbez.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ);
		wbuZusatzbez.addActionListener(this);

		wlaZusatzbez
				.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfZusatzbez.setToken("zusatzbezeichnung");

		wbuZusatzbez2.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung2"));

		wbuZusatzbez2.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2);
		wbuZusatzbez2.addActionListener(this);

		wlaZusatzbez2.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung2"));
		wtfZusatzbez2.setToken("zusatzbezeichnung2");

		wlaEinheit.setText(LPMain.getTextRespectUISPr("lp.einheit"));
		wcoEinheit.setToken("einheit");
		wlaEinheitBestellung.setText(LPMain
				.getTextRespectUISPr("artikel.bestelleinheit"));
		wcoEinheitBestellung.setToken("bestelleinheit");
		wlaUmrechnungsfaktor.setText(LPMain
				.getTextRespectUISPr("artikel.umrechnungsfaktor"));
		wnfUmrechnungsfaktor.setToken("umrechnungsfaktor");

		wlaReferenznummer.setText(LPMain
				.getTextRespectUISPr("lp.referenznummer"));
		wtfReferenznummer.setToken("referenznummer");

		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));
		wcbVersteckt.setToken("versteckt");

		wcbKalkulatorisch.setText(LPMain
				.getTextRespectUISPr("artikel.kalkulatorisch"));
		wcbKalkulatorisch.setToken("kalkulatorisch");

		wcbNurZurInfo.setText(LPMain.getTextRespectUISPr("artikel.nurzurinfo"));
		wcbNurZurInfo.setToken("nurzurinfo");

		wcbReineMannzeit.setText(LPMain
				.getTextRespectUISPr("artikel.reinemannzeit"));
		wcbReineMannzeit.setToken("reinemannzeit");

		wtfArtikelnummer.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER);
		wnfUmrechnungsfaktor.setFractionDigits(6);
		wcoEinheitBestellung
				.addActionListener(new PanelArtikel_wcoEinheitBestellung_actionAdapter(
						this));

		// Default MWST-Satz setzen
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			defaultMwstsatz = (Integer) parameter.getCWertAsObject();
		}

		wtfHerstellerKuerzel.setActivatable(false);

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			wtfArtikelnummer.setColumnsMax(((Integer) parameter
					.getCWertAsObject()).intValue());
		}

		wtfArtikelnummer.setMandatoryField(true);
		wtfArtikelnummer.setUppercaseField(true);
		wtfArtikelnummer
				.addFocusListener(new PanelArtikel_wtfArtikelnummer_focusAdapter(
						this));
		wtfKurzbezeichnung
				.setColumnsMax(ArtikelFac.MAX_ARTIKEL_KURZBEZEICHNUNG);
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG);
		wtfZusatzbez.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG);
		wtfZusatzbez2.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG2);
		wtfReferenznummer.setColumnsMax(ArtikelFac.MAX_ARTIKEL_REFERENZNUMMER);

		installSiKeyListeners();

		wbuArtikelklasse.setText(LPMain.getTextRespectUISPr("lp.artikelklasse")
				+ "...");
		wbuArtikelklasse
				.setActionCommand(PanelArtikel.ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE);
		wbuArtikelklasse.addActionListener(this);
		wbuArtikelklasse.setToken("artikelklasse");
		wbuArtikelgruppe.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe")
				+ "...");
		wbuArtikelgruppe
				.setActionCommand(PanelArtikel.ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE);
		wbuArtikelgruppe.addActionListener(this);
		wbuArtikelgruppe.setToken("artikelgruppe");

		wtfArtikelklasse.setActivatable(false);
		wbuHersteller.setText(LPMain.getTextRespectUISPr("lp.hersteller")
				+ "...");
		wbuHersteller
				.setActionCommand(PanelArtikel.ACTION_SPECIAL_HERSTELLER_FROM_LISTE);
		wbuHersteller.addActionListener(this);
		wbuHersteller.setToken("hersteller");
		wtfHersteller.setText("");
		// wtfHersteller.setActivatable(false);
		wlaArtikelart.setText(LPMain.getTextRespectUISPr("lp.artikelart"));

		wlaIndex.setText(LPMain.getTextRespectUISPr("artikel.index"));
		wtfIndex.setColumnsMax(15);
		wtfIndex.setToken("index");
		wlaRevision.setText(LPMain.getTextRespectUISPr("artikel.revision"));
		wtfRevision.setColumnsMax(ArtikelFac.MAX_ARTIKEL_REVISION);
		wtfRevision.setToken("revision");

		wlaMehrwertsteuersatz.setText(LPMain.getTextRespectUISPr("lp.mwst"));
		wcoMehrwertsteuer.setToken("mwst");
		wlaDefaultbezeichnungen.setText(LPMain
				.getTextRespectUISPr("artikel.defaultbezeichnungen") + ":");
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		wtfArtikelgruppe.setMandatoryField((Boolean) parameter
				.getCWertAsObject());
		wtfArtikelgruppe.setActivatable(false);

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getTheClient().getMandant());
		bPositionskontierung = ((Boolean) parameter.getCWertAsObject());
		wcoMehrwertsteuer.setMandatoryField(bPositionskontierung);

		bPaternosterVerfuegbar = DelegateFactory.getInstance()
				.getAutoPaternosterDelegate().isPaternosterVerfuegbar();

		wcoArtikelart.setMandatoryField(true);
		wcoArtikelart.addActionListener(this);
		wcoArtikelart.setToken("artikelart");
		wcoEinheit.setMandatoryField(true);
		wbuGeneriereArtikelnummer.setText("G");
		wbuGeneriereArtikelnummer.setToolTipText(LPMain
				.getTextRespectUISPr("artikel.generiereartikelnummer"));
		wbuGeneriereArtikelnummer
				.setActionCommand(PanelArtikel.ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER);
		wbuGeneriereArtikelnummer.addActionListener(this);
		wbuGeneriereArtikelnummer.setToken("generiereartikelnummer");

		wlaArtikelBezStd.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfArtikelBezStd.setText("");
		wtfArtikelBezStd.setActivatable(false);
		wlaArtikelZBezStd.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaKurzbezeichnungStd.setText(LPMain
				.getTextRespectUISPr("label.kurzbezeichnung"));
		wtfKurzbezeichnungStd.setText("");
		wtfKurzbezeichnungStd.setActivatable(false);
		wtfArtikelZBezStd.setText("");
		wtfArtikelZBezStd.setActivatable(false);
		wlaZBez2Std.setRequestFocusEnabled(true);
		wlaZBez2Std.setText(LPMain.getTextRespectUISPr("lp.zusatzbez2"));
		wtfArtikelZBez2Std.setText("");
		wtfArtikelZBez2Std.setActivatable(false);
		wlaSperren.setText("");
		wlaSperren.setForeground(Color.RED);

		final int iBreite = Defaults.getInstance().bySizeFactor(100);
		wkvfLagerstand = new WrapperKeyValueField(iBreite);
		wkvfLagerstand.setKey(LPMain.getTextRespectUISPr("lp.lagerstand")
				+ ": ");
		wkvfLagerstand.setToken("lagerstand");

		wcbBestellmengeneinheitInvers.setText(LPMain
				.getTextRespectUISPr("artikel.bestellmengeneinheit.invers"));

		wkvfReserviert = new WrapperKeyValueField(iBreite);
		wkvfReserviert.setKey("- "
				+ LPMain.getTextRespectUISPr("lp.reserviert") + ": ");
		wkvfReserviert.setToken("reseviert");

		wkvfFehlmenge = new WrapperKeyValueField(iBreite);
		wkvfFehlmenge.setKey("- "
				+ LPMain.getTextRespectUISPr("label.fehlmenge") + ": ");
		wkvfFehlmenge.setToken("fehlmenge");

		wkvfVerfuegbar = new WrapperKeyValueField(iBreite);
		wkvfVerfuegbar.setKey("= "
				+ LPMain.getTextRespectUISPr("lp.verfuegbar") + ": ");
		wkvfVerfuegbar.setToken("verfuegbar");

		wkvfInfertigung = new WrapperKeyValueField(iBreite);
		wkvfInfertigung.setKey(LPMain.getTextRespectUISPr("lp.infertigung")
				+ ": ");
		wkvfInfertigung.setToken("infertigung");

		wkvfRahmenreserviert = new WrapperKeyValueField(iBreite);
		wkvfRahmenreserviert.setKey(LPMain
				.getTextRespectUISPr("lp.rahmenreserviert") + ": ");
		wkvfRahmenreserviert.setToken("rahmenreserviert");

		wkvfRahmenbestellt = new WrapperKeyValueField(iBreite);
		wkvfRahmenbestellt.setKey(LPMain
				.getTextRespectUISPr("lp.rahmenbestellt") + ": ");
		wkvfRahmenbestellt.setToken("rahmenbestellt");

		wkvfBestellt = new WrapperKeyValueField(iBreite);
		wkvfBestellt.setKey(LPMain.getTextRespectUISPr("lp.bestellt") + ": ");
		wkvfBestellt.setToken("bestellt");

		wlaVorzugsteil.setText(LPMain.getTextRespectUISPr("artikel.vorzug"));

		wkvfRahmenbedarf = new WrapperKeyValueField(iBreite);
		wkvfRahmenbedarf.setKey(LPMain
				.getTextRespectUISPr("artikel.rahmenbedarf") + ": ");
		wkvfRahmenbedarf.setToken("rahmenbedarf");

		wkvfPaternoster = new WrapperKeyValueField(iBreite);
		wkvfPaternoster.setKey(LPMain
				.getTextRespectUISPr("artikel.paternoster") + ": ");
		wkvfPaternoster.setToken("paternoster");

		wlaSperren.setHorizontalAlignment(SwingConstants.LEFT);

		wbuStkl.setActionCommand(ACTION_SPECIAL_STKL_NEU_ERZEUGEN);
		wbuStkl.addActionListener(this);
		wbuStkl.setActivatable(false);
		wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl"));

		jpaPanelWorkingOn = new JPanel(
				new MigLayout("wrap 6",
						"[22.5%,fill][15%,fill][10%,fill][17.5%,fill][17.5%,fill][17.5%,fill]"));
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0;

		jpaPanelWorkingOn.add(wlaArtikelnummer, "growx 75, split 2");
		jpaPanelWorkingOn.add(wbuGeneriereArtikelnummer, "growx 25");

		// jpaPanelWorkingOn.add(wlaArtikelnummer,
		// new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL,
		// new Insets(10, 2, 2, 52), 100, 0));
		// jpaPanelWorkingOn.add(wbuGeneriereArtikelnummer,
		// new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
		// GridBagConstraints.EAST, GridBagConstraints.NONE,
		// new Insets(10, 0, 0, 0), 40, 0));

		jpaPanelWorkingOn.add(wtfArtikelnummer);
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {
			jpaPanelWorkingOn.add(wtfHerstellerKuerzel);
		} else if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
			jpaPanelWorkingOn.add(wbuStkl);
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel());
		}

		jpaPanelWorkingOn.add(wbuHersteller);
		jpaPanelWorkingOn.add(wtfHersteller, "span");

		// PJ 16665

		boolean bVorschlagstexteVorhanden = DelegateFactory.getInstance()
				.getArtikelDelegate().sindVorschlagstexteVorhanden();

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuKurzbezeichnung
				: wlaKurzbezeichnung);
		jpaPanelWorkingOn.add(wtfKurzbezeichnung);
		jpaPanelWorkingOn.add(wlaRevision);
		jpaPanelWorkingOn.add(wtfRevision);
		jpaPanelWorkingOn.add(wlaArtikelart);
		jpaPanelWorkingOn.add(wcoArtikelart, "wrap");

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuBezeichnung
				: wlaBezeichnung);
		jpaPanelWorkingOn.add(wtfBezeichnung, "span 3");
		if (hasZusatzFunktionSiWert()) {
			wlaSiWert.setHorizontalAlignment(SwingConstants.LEFT);
			jpaPanelWorkingOn.add(wlaSiWert);
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel());
		}

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaPanelWorkingOn.add(wcbVersteckt, "wrap");
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel(), "wrap");
		}

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuZusatzbez
				: wlaZusatzbez);
		jpaPanelWorkingOn.add(wtfZusatzbez, "span 3");
		jpaPanelWorkingOn.add(wcbReineMannzeit);
		jpaPanelWorkingOn.add(wlaSperren, "wrap");

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuZusatzbez2
				: wlaZusatzbez2);
		jpaPanelWorkingOn.add(wtfZusatzbez2, "span 3");
		jpaPanelWorkingOn.add(wcbNurZurInfo);
		jpaPanelWorkingOn.add(wcbKalkulatorisch, "wrap");

		jpaPanelWorkingOn.add(wlaEinheit);
		jpaPanelWorkingOn.add(wcoEinheit);
		jpaPanelWorkingOn.add(new WrapperLabel("="));
		jpaPanelWorkingOn.add(wnfUmrechnungsfaktor);
		jpaPanelWorkingOn.add(wcoEinheitBestellung);
		jpaPanelWorkingOn.add(wcbBestellmengeneinheitInvers, "wrap");

		jpaPanelWorkingOn.add(wlaReferenznummer);
		jpaPanelWorkingOn.add(wtfReferenznummer);
		jpaPanelWorkingOn.add(wlaIndex);
		jpaPanelWorkingOn.add(wtfIndex);
		jpaPanelWorkingOn.add(wlaMehrwertsteuersatz);
		jpaPanelWorkingOn.add(wcoMehrwertsteuer, "wrap");

		jpaPanelWorkingOn.add(wbuArtikelklasse);
		jpaPanelWorkingOn.add(wtfArtikelklasse, "span 3");
		jpaPanelWorkingOn.add(wsfLiefergruppe.getWrapperButton());
		jpaPanelWorkingOn.add(wsfLiefergruppe.getWrapperTextField(), "wrap");

		jpaPanelWorkingOn.add(wbuArtikelgruppe);
		jpaPanelWorkingOn.add(wtfArtikelgruppe, "span 3");

		jpaPanelWorkingOn.add(wsfShopgruppe.getWrapperButton());
		jpaPanelWorkingOn.add(wsfShopgruppe.getWrapperTextField(), "wrap");

		jpaPanelWorkingOn.add(new WrapperLabel(""), "span 4");
		jpaPanelWorkingOn.add(wlaVorzugsteil);
		jpaPanelWorkingOn.add(wcoVorzugsteil, "wrap");

		if (!LPMain.getTheClient().getLocKonzern()
				.equals(LPMain.getTheClient().getLocUi())) {

			jpaPanelWorkingOn.add(wlaDefaultbezeichnungen, "wrap");
			jpaPanelWorkingOn.add(wlaKurzbezeichnungStd);
			jpaPanelWorkingOn.add(wtfKurzbezeichnungStd, "span 3, wrap");
			jpaPanelWorkingOn.add(wlaArtikelBezStd);
			jpaPanelWorkingOn.add(wtfArtikelBezStd, "span 3, wrap");
			jpaPanelWorkingOn.add(wlaArtikelZBezStd);
			jpaPanelWorkingOn.add(wtfArtikelZBezStd, "span 3, wrap");
			jpaPanelWorkingOn.add(wlaZBez2Std, "growx");
			jpaPanelWorkingOn.add(wtfArtikelZBez2Std, "span 3, wrap");
		} else {
			if (bPaternosterVerfuegbar) {
				jpaPanelWorkingOn.add(wkvfPaternoster,
						"gapright 14px, span 3, split 2");
				jpaPanelWorkingOn.add(new JLabel());
			}
			// default-Bild:

			jpaPanelWorkingOn.add(pi, (bPaternosterVerfuegbar ? "" : "skip 3,")
					+ "w min:0, span 5 10, wrap");

			jpaPanelWorkingOn.add(wkvfLagerstand, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfInfertigung, "wrap");
			jpaPanelWorkingOn.add(wkvfReserviert, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfBestellt, "wrap");
			jpaPanelWorkingOn.add(wkvfFehlmenge, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfRahmenreserviert, "wrap");
			jpaPanelWorkingOn.add(wkvfVerfuegbar, "top, span 3, split 2");
			jpaPanelWorkingOn.add(wkvfRahmenbestellt, "top, wrap");
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {
				jpaPanelWorkingOn.add(wkvfRahmenbedarf, "span 2, wrap");
			}
		}

		// xcvb: Zusaetzliche Buttons fuer Detail-Panel angeben
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

		// PJ18881
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)
				&& DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {

			getToolBar().addButtonCenter("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("artikel.goto.kundesosko"),
					MY_OWN_NEW_GOTO_SOKO, null, null);
			enableToolsPanelButtons(true, MY_OWN_NEW_GOTO_SOKO);
		}
		getToolBar().addButtonRight(
				"/com/lp/client/res/document_attachment16x16.png",
				LPMain.getTextRespectUISPr("lp.dokumentablage"),
				MY_OWN_NEW_DOKUMENTENABLAGE, null, null);
		jbDokumente = getHmOfButtons().get(MY_OWN_NEW_DOKUMENTENABLAGE)
				.getButton();

	}

	private boolean hasZusatzFunktionSiWert() {
		return LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_SI_WERT);
	}

	private void installSiKeyListeners() {
		if (!hasZusatzFunktionSiWert())
			return;

		KeyListener siListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					siWertBerechnenAusTextfeldern();
				} catch (Throwable t) {
					getInternalFrame().handleException(t, false);
				}
			}
		};

		wtfBezeichnung.addKeyListener(siListener);
		wtfZusatzbez.addKeyListener(siListener);
		wtfZusatzbez2.addKeyListener(siListener);
	}

	protected void setDefaults() throws Throwable {
		Map<?, ?> m = DelegateFactory.getInstance().getArtikelDelegate()
				.getAllSprArtikelarten();
		m.remove(ArtikelFac.ARTIKELART_HANDARTIKEL);
		wcoArtikelart.setMap(m);
		wcoMehrwertsteuer.setMap(DelegateFactory.getInstance()
				.getMandantDelegate()
				.getAllMwstsatzbez(LPMain.getTheClient().getMandant()));
		wcoEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten());
		wcoEinheitBestellung.setMap(DelegateFactory.getInstance()
				.getSystemDelegate().getAllEinheiten());

		wcoVorzugsteil.setMap(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllVorzug());

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {

			wcoEinheit.setKeyOfSelectedItem(Helper.fitString2Length(
					parameter.getCWert(), 15, ' '));
		}
		// Default-
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		wcoArtikelart.setKeyOfSelectedItem(parameter.getCWert());
		wlaSperren.setText("");

	}

	protected void components2Dto() throws Throwable {

		wtfArtikelnummer.setText(wtfArtikelnummer.getText().trim());

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

			if (wtfHerstellerKuerzel.getText() != null) {

				if (wtfArtikelnummer.getText().length() < maxLaenge) {
					internalFrameArtikel.getArtikelDto().setCNr(
							Helper.fitString2Length(wtfArtikelnummer.getText(),
									maxLaenge, ' ')
									+ wtfHerstellerKuerzel.getText());
				} else {
					internalFrameArtikel.getArtikelDto().setCNr(
							wtfArtikelnummer.getText()
									+ wtfHerstellerKuerzel.getText());
				}
			} else {
				internalFrameArtikel.getArtikelDto().setCNr(
						wtfArtikelnummer.getText());
			}
		} else {
			internalFrameArtikel.getArtikelDto().setCNr(
					wtfArtikelnummer.getText());
		}

		internalFrameArtikel.getArtikelDto().setLfliefergruppeIId(
				wsfLiefergruppe.getIKey());

		internalFrameArtikel.getArtikelDto().setShopgruppeIId(
				wsfShopgruppe.getIKey());

		internalFrameArtikel.getArtikelDto().setArtikelartCNr(
				(String) wcoArtikelart.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setBVersteckt(
				wcbVersteckt.getShort());
		internalFrameArtikel.getArtikelDto().setBKalkulatorisch(
				wcbKalkulatorisch.getShort());

		internalFrameArtikel.getArtikelDto().setbBestellmengeneinheitInvers(
				wcbBestellmengeneinheitInvers.getShort());

		internalFrameArtikel.getArtikelDto().setbNurzurinfo(
				wcbNurZurInfo.getShort());
		internalFrameArtikel.getArtikelDto().setbReinemannzeit(
				wcbReineMannzeit.getShort());

		// Wenn Arbeitszeitartikel, dann immer Einheit= Stunden(h) (->WH
		// 09.11.05)
		if (((String) wcoArtikelart.getKeyOfSelectedItem())
				.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			internalFrameArtikel.getArtikelDto().setEinheitCNr(
					SystemFac.EINHEIT_STUNDE);
		} else {
			internalFrameArtikel.getArtikelDto().setEinheitCNr(
					(String) wcoEinheit.getKeyOfSelectedItem());
		}

		if (wnfUmrechnungsfaktor.getBigDecimal() != null
				&& wnfUmrechnungsfaktor.getBigDecimal().doubleValue() == 0) {
			internalFrameArtikel.getArtikelDto().setNUmrechnungsfaktor(null);
			internalFrameArtikel.getArtikelDto().setEinheitCNrBestellung(null);

		} else {
			internalFrameArtikel.getArtikelDto().setNUmrechnungsfaktor(
					wnfUmrechnungsfaktor.getBigDecimal());

		}

		internalFrameArtikel.getArtikelDto().setEinheitCNrBestellung(
				(String) wcoEinheitBestellung.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setMwstsatzbezIId(
				(Integer) wcoMehrwertsteuer.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setVorzugIId(
				(Integer) wcoVorzugsteil.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setCReferenznr(
				wtfReferenznummer.getText());

		if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() == null) {
			internalFrameArtikel.getArtikelDto().setArtikelsprDto(
					new ArtikelsprDto());
		}

		internalFrameArtikel.getArtikelDto()
				.setCRevision(wtfRevision.getText());
		internalFrameArtikel.getArtikelDto().setCIndex(wtfIndex.getText());

		internalFrameArtikel.getArtikelDto().getArtikelsprDto()
				.setCBez(wtfBezeichnung.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto()
				.setCKbez(wtfKurzbezeichnung.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto()
				.setCZbez(wtfZusatzbez.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto()
				.setCZbez2(wtfZusatzbez2.getText());

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArtikelnummer;
	}

	protected void dto2Components() throws Throwable {
		wcbVersteckt.setShort(internalFrameArtikel.getArtikelDto()
				.getBVersteckt());

		wcbKalkulatorisch.setShort(internalFrameArtikel.getArtikelDto()
				.getBKalkulatorisch());

		wcbReineMannzeit.setShort(internalFrameArtikel.getArtikelDto()
				.getbReinemannzeit());
		wcbNurZurInfo.setShort(internalFrameArtikel.getArtikelDto()
				.getbNurzurinfo());
		wcbBestellmengeneinheitInvers.setShort(internalFrameArtikel
				.getArtikelDto().getbBestellmengeneinheitInvers());

		String sperren = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.getArtikelsperrenText(
						internalFrameArtikel.getArtikelDto().getIId());
		if (sperren != null) {
			wlaSperren.setText(LPMain.getTextRespectUISPr("lp.sperren") + ": "
					+ sperren);
		} else {
			wlaSperren.setText("");
		}
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

			if (internalFrameArtikel.getArtikelDto().getCNr().length() > maxLaenge) {
				wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto()
						.getCNr().substring(0, maxLaenge));
				wtfHerstellerKuerzel.setText(internalFrameArtikel
						.getArtikelDto().getCNr().substring(maxLaenge));
			} else {
				wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto()
						.getCNr());
				wtfHerstellerKuerzel.setText(null);
			}

		} else {
			wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto()
					.getCNr());
		}
		wtfReferenznummer.setText(internalFrameArtikel.getArtikelDto()
				.getCReferenznr());
		wsfLiefergruppe.setKey(internalFrameArtikel.getArtikelDto()
				.getLfliefergruppeIId());
		wsfShopgruppe.setKey(internalFrameArtikel.getArtikelDto()
				.getShopgruppeIId());

		if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null) {
			internalFrameArtikel.getArtikelDto().setArtgruDto(
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artgruFindByPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getArtgruIId()));
			if (internalFrameArtikel.getArtikelDto().getArtgruDto()
					.getArtgrusprDto() != null
					&& internalFrameArtikel.getArtikelDto().getArtgruDto()
							.getArtgrusprDto().getCBez() != null) {
				wtfArtikelgruppe.setText(internalFrameArtikel.getArtikelDto()
						.getArtgruDto().getArtgrusprDto().getCBez());
			} else {
				wtfArtikelgruppe.setText(internalFrameArtikel.getArtikelDto()
						.getArtgruDto().getCNr());
			}

		} else {
			wtfArtikelgruppe.setText("");

		}
		if (internalFrameArtikel.getArtikelDto().getArtklaIId() != null) {
			internalFrameArtikel.getArtikelDto().setArtklaDto(
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artklaFindByPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getArtklaIId()));
			wtfArtikelklasse.setText(internalFrameArtikel.getArtikelDto()
					.getArtklaDto().getCNr());
		} else {
			wtfArtikelklasse.setText("");

		}
		if (internalFrameArtikel.getArtikelDto().getHerstellerIId() != null) {
			internalFrameArtikel.getArtikelDto().setHerstellerDto(
					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.herstellerFindBdPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getHerstellerIId()));
			wtfHersteller.setText(internalFrameArtikel.getArtikelDto()
					.getHerstellerDto().getPartnerDto().formatFixName1Name2());
		} else {
			wtfHersteller.setText("");
		}
		wcoArtikelart.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto()
				.getArtikelartCNr());
		wcoEinheit.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto()
				.getEinheitCNr());
		wcoEinheitBestellung.setKeyOfSelectedItem(internalFrameArtikel
				.getArtikelDto().getEinheitCNrBestellung());
		wcoMehrwertsteuer.setKeyOfSelectedItem(internalFrameArtikel
				.getArtikelDto().getMwstsatzbezIId());

		wcoVorzugsteil.setKeyOfSelectedItem(internalFrameArtikel
				.getArtikelDto().getVorzugIId());

		wnfUmrechnungsfaktor.setBigDecimal(internalFrameArtikel.getArtikelDto()
				.getNUmrechnungsfaktor());

		wtfIndex.setText(internalFrameArtikel.getArtikelDto().getCIndex());
		wtfRevision
				.setText(internalFrameArtikel.getArtikelDto().getCRevision());

		if (internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor() == null) {
			wcoEinheitBestellung.setKeyOfSelectedItem(null);
			wcbBestellmengeneinheitInvers.setShort(Helper.boolean2Short(false));
		}

		if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() != null) {

			if (internalFrameArtikel.getArtikelDto().getArtikelsprDto()
					.getNSiwert() != null) {
				BigDecimalSI bdSI = new BigDecimalSI(internalFrameArtikel
						.getArtikelDto().getArtikelsprDto().getNSiwert());
				wlaSiWert.setText(bdSI.toSIString());
			} else {
				wlaSiWert.setText(null);
			}

			wtfKurzbezeichnung.setText(internalFrameArtikel.getArtikelDto()
					.getArtikelsprDto().getCKbez());
			wtfBezeichnung.setText(internalFrameArtikel.getArtikelDto()
					.getArtikelsprDto().getCBez());
			wtfZusatzbez.setText(internalFrameArtikel.getArtikelDto()
					.getArtikelsprDto().getCZbez());
			wtfZusatzbez2.setText(internalFrameArtikel.getArtikelDto()
					.getArtikelsprDto().getCZbez2());
			if (!internalFrameArtikel.getArtikelDto().getArtikelsprDto()
					.getLocaleCNr()
					.equals(LPMain.getTheClient().getLocKonzernAsString())) {

			} else {

				BigDecimal lagerstand = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerstandAllerLagerEinesMandanten(
								internalFrameArtikel.getArtikelDto().getIId(),
								false);

				BigDecimal fehlmengen = DelegateFactory
						.getInstance()
						.getFehlmengeDelegate()
						.getAnzahlFehlmengeEinesArtikels(
								internalFrameArtikel.getArtikelDto().getIId());
				BigDecimal reservierungen = DelegateFactory
						.getInstance()
						.getReservierungDelegate()
						.getAnzahlReservierungen(
								internalFrameArtikel.getArtikelDto().getIId());

				BigDecimal paternoster = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getPaternosterLagerstand(
								internalFrameArtikel.getArtikelDto().getIId());

				wkvfPaternoster.setValue(Helper.formatZahl(paternoster, 2,
						LPMain.getTheClient().getLocUi()));

				wkvfLagerstand.setValue(Helper.formatZahl(lagerstand, 2, LPMain
						.getTheClient().getLocUi()));

				if (reservierungen.doubleValue() < 0) {
					wkvfReserviert.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfReserviert.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfReserviert.setValue(Helper.formatZahl(reservierungen, 2,
						LPMain.getTheClient().getLocUi()));

				if (fehlmengen.doubleValue() < 0) {
					wkvfFehlmenge.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfFehlmenge.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfFehlmenge.setValue(Helper.formatZahl(fehlmengen, 2, LPMain
						.getTheClient().getLocUi()));

				BigDecimal verfuegbar = lagerstand.subtract(reservierungen)
						.subtract(fehlmengen);

				if (verfuegbar.doubleValue() < 0) {
					wkvfVerfuegbar.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfVerfuegbar.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfVerfuegbar.setValue(Helper.formatZahl(verfuegbar, 2, LPMain
						.getTheClient().getLocUi()));
				BigDecimal infertigung = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.getAnzahlInFertigung(
								internalFrameArtikel.getArtikelDto().getIId());
				wkvfInfertigung.setValue(Helper.formatZahl(infertigung, 2,
						LPMain.getTheClient().getLocUi()));

				BigDecimal bestellt = DelegateFactory
						.getInstance()
						.getArtikelbestelltDelegate()
						.getAnzahlBestellt(
								internalFrameArtikel.getArtikelDto().getIId());
				wkvfBestellt.setValue(Helper.formatZahl(bestellt, 2, LPMain
						.getTheClient().getLocUi()));

				BigDecimal rahmenres = DelegateFactory
						.getInstance()
						.getReservierungDelegate()
						.getAnzahlRahmenreservierungen(
								internalFrameArtikel.getArtikelDto().getIId());
				wkvfRahmenreserviert.setValue(Helper.formatZahl(rahmenres, 2,
						LPMain.getTheClient().getLocUi()));

				BigDecimal rahmenbestellt = null;
				Hashtable<?, ?> htAnzahlRahmenbestellt = DelegateFactory
						.getInstance()
						.getArtikelbestelltDelegate()
						.getAnzahlRahmenbestellt(
								internalFrameArtikel.getArtikelDto().getIId());
				if (htAnzahlRahmenbestellt
						.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					wkvfRahmenbestellt.setValue(Helper
							.formatZahl(rahmenbestellt, 2, LPMain
									.getTheClient().getLocUi()));
				}

				BigDecimal rahmenbedarf = DelegateFactory
						.getInstance()
						.getRahmenbedarfeDelegate()
						.getSummeAllerRahmenbedarfeEinesArtikels(
								internalFrameArtikel.getArtikelDto().getIId());
				wkvfRahmenbedarf.setValue(Helper.formatZahl(rahmenbedarf, 2,
						LPMain.getTheClient().getLocUi()));

			}
			if (Helper.short2boolean(internalFrameArtikel.getArtikelDto()
					.getBLagerbewirtschaftet()) == false) {

				wkvfVerfuegbar.setValue("----");
				wkvfLagerstand.setValue("----");
			}
		}

		ArtikelsprDto defaultSprDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.getDefaultArtikelbezeichnungen(
						internalFrameArtikel.getArtikelDto().getIId());

		if (defaultSprDto != null) {
			wtfKurzbezeichnungStd.setText(defaultSprDto.getCKbez());
			wtfArtikelBezStd.setText(defaultSprDto.getCBez());
			wtfArtikelZBezStd.setText(defaultSprDto.getCZbez());
			wtfArtikelZBez2Std.setText(defaultSprDto.getCZbez2());
		}

		// DefaultBild- Holen
		ArrayList<byte[]> b = DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.getArtikelBilder(internalFrameArtikel.getArtikelDto().getIId());
		pi.setImages(b);

		pi.setTextPDFVorhandenVisible(DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.sindTexteOderPDFsVorhanden(
						internalFrameArtikel.getArtikelDto().getIId()));

		// Goto Stkl

		StuecklisteDto stklDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						internalFrameArtikel.getArtikelDto().getIId());

		if (stklDto != null && stklDto.getIId() != null) {
			wbuStkl.setActivatable(false);
			wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl"));
			wbuStkl.getWrapperButtonGoTo().setVisible(true);
			wbuStkl.setOKey(stklDto.getIId());

		} else {
			wbuStkl.setActivatable(true);
			wbuStkl.setEnabled(true);
			wbuStkl.setText(LPMain
					.getTextRespectUISPr("artikel.detail.stkl.erz"));
			wbuStkl.getWrapperButtonGoTo().setVisible(false);
			wbuStkl.setOKey(null);
		}

		PrintInfoDto values = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.getPathAndPartnerAndTable(
						internalFrameArtikel.getArtikelDto().getIId(),
						QueryParameters.UC_ID_ARTIKELLISTE);

		JCRRepoInfo repoInfo = new JCRRepoInfo();
		// boolean hasFiles = false;
		if (values != null && values.getDocPath() != null) {
			repoInfo = DelegateFactory.getInstance().getJCRDocDelegate()
					.checkIfNodeExists(values.getDocPath());
			enableToolsPanelButtons(repoInfo.isOnline(),
					MY_OWN_NEW_DOKUMENTENABLAGE);
			// boolean online = DelegateFactory.getInstance()
			// .getJCRDocDelegate().isOnline();
			// enableToolsPanelButtons(online, MY_OWN_NEW_DOKUMENTENABLAGE);
			// if (online) {
			// hasFiles = DelegateFactory.getInstance()
			// .getJCRDocDelegate()
			// .checkIfNodeExists(values.getDocPath());
			// }
			// }
		}
		jbDokumente.setIcon(repoInfo.isExists() ? DOKUMENTE : KEINE_DOKUMENTE);

		this.setStatusbarPersonalIIdAendern(internalFrameArtikel
				.getArtikelDto().getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(internalFrameArtikel
				.getArtikelDto().getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(internalFrameArtikel.getArtikelDto()
				.getTAnlegen());
		this.setStatusbarTAendern(internalFrameArtikel.getArtikelDto()
				.getTAendern());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			String einheitVorher = internalFrameArtikel.getArtikelDto()
					.getEinheitCNr();
			components2Dto();
			try {
				if (internalFrameArtikel.getArtikelDto().getIId() == null) {
					internalFrameArtikel.getArtikelDto().setIId(
							DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.createArtikel(
											internalFrameArtikel
													.getArtikelDto()));
					setKeyWhenDetailPanel(internalFrameArtikel.getArtikelDto()
							.getIId());
					internalFrameArtikel.setArtikelDto(internalFrameArtikel
							.getArtikelDto());
				} else {

					ArtikelDto artikelDtoVorher = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getIId());

					// PJ 16901
					boolean bArtGruGeaendert = false;
					if (internalFrameArtikel.getArtikelDto().getArtgruIId() == null
							&& artikelDtoVorher.getArtgruIId() != null) {
						bArtGruGeaendert = true;
					}
					if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null
							&& artikelDtoVorher.getArtgruIId() == null) {
						bArtGruGeaendert = true;
					}

					if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null
							&& artikelDtoVorher.getArtgruIId() != null
							&& !internalFrameArtikel.getArtikelDto()
									.getArtgruIId()
									.equals(artikelDtoVorher.getArtgruIId())) {
						bArtGruGeaendert = true;
					}

					if (bArtGruGeaendert == true) {

						PaneldatenDto[] eigs = DelegateFactory
								.getInstance()
								.getPanelDelegate()
								.paneldatenFindByPanelCNrCKey(
										PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
										internalFrameArtikel.getArtikelDto()
												.getIId() + "");

						if (eigs != null && eigs.length > 0) {

							boolean b = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("artikel.artgru.geaendert"));

							if (b == false) {

								return;
							}
						}
					}

					DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.updateArtikel(internalFrameArtikel.getArtikelDto());

					// PJ 13562
					if (internalFrameArtikel.getArtikelDto().getIId() != null
							&& einheitVorher != null) {
						if (!einheitVorher.equals((String) wcoEinheit
								.getKeyOfSelectedItem())) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.info"),
											LPMain.getTextRespectUISPr("artikel.error.einheitgeaendert"));
						}
					}

				}

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFrameArtikel.getArtikelDto().getIId()
									.toString());
				}
				eventYouAreSelected(false);
				internalFrameArtikel.setArtikelDto(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								internalFrameArtikel.getArtikelDto().getIId()));

			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT) {
					String msg = LPMain
							.getTextRespectUISPr("lp.error.zeicheninartikelnummernichterlaubt");
					if (ex.getAlInfoForTheClient() != null) {
						msg = msg + " (" + ex.getAlInfoForTheClient().get(0)
								+ ")";
					}

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"), msg);

				} else if (ex.getICode() == EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ) {
					String msg = LPMain
							.getTextRespectUISPr("lp.error.artikelnrzukurz");
					if (ex.getAlInfoForTheClient() != null) {
						msg = msg + " (Mindestens "
								+ ex.getAlInfoForTheClient().get(0)
								+ " Stellen)";
					}

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"), msg);

				} else if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
					bKeinFocusLost = true;

					handleException(ex, false);
				} else {
					handleException(ex, false);
				}
			}

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		internalFrameArtikel.setArtikelDto(new ArtikelDto());
		leereAlleFelder(this);
		setDefaults();
		wbuStkl.setVisible(false);
	}

	public void siWertBerechnenAusTextfeldern() throws Throwable {
		siWertBerechnen(wtfBezeichnung.getText(), wtfZusatzbez.getText(),
				wtfZusatzbez2.getText());
	}

	private Boolean getSiOhneEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_OHNE_EINHEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return (Boolean) parameter.getCWertAsObject();
	}

	private String getSiEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_EINHEITEN,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return parameter.getCWert();
	}

	private void siWertBerechnen(String cBez, String cZbez, String cZbez2)
			throws Throwable {
		if (!hasZusatzFunktionSiWert())
			return;

		// PJ18155
		try {
			BigDecimalSI bdSi = siwertParser.berechneSiWertAusBezeichnung(cBez,
					cZbez, cZbez2);
			// BigDecimalSI bdSiO = Helper.berechneSiWertAusBezeichnung(
			// getSiOhneEinheitenFromParam(), getSiEinheitenFromParam(), cBez,
			// cZbez, cZbez2);
			wlaSiWert.setText(bdSi != null ? bdSi.toSIString() : null);
		} catch (IllegalArgumentException iae) {
			wlaSiWert.setText(null);
			myLogger.error(
					"Mandantenparameter SI_OHNE_EINHEIT und SI_EINHEITEN stehen in Konflikt!",
					iae);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeArtikel(internalFrameArtikel.getArtikelDto().getIId());
		super.eventActionDelete(e, true, true);
	}

	public void wtfArtikelnummer_focusLost(FocusEvent e) {
		if (bKeinFocusLost == false && wtfArtikelnummer.getText() != null
				&& internalFrameArtikel.getArtikelDto() != null
				&& internalFrameArtikel.getArtikelDto().getIId() == null) {

			try {

				ArtikelDto aDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByCNr(wtfArtikelnummer.getText());

				String text = LPMain
						.getTextRespectUISPr("artikel.existiertbereits.teil1")
						+ "'" + aDto.getCNr() + "'.";

				if (Helper.short2boolean(aDto.getBVersteckt())) {
					text += " "
							+ LPMain.getTextRespectUISPr("artikel.existiertbereits.teil2");
				}

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"), text);
			} catch (Throwable ex1) {
				// OK ->Artikel noch nicht vorhanden
			}

		} else {
			bKeinFocusLost = false;
		}

	}

	public void wcoEinheitBestellung_actionPerformed(ActionEvent e) {
		String key = (String) wcoEinheitBestellung.getKeyOfSelectedItem();
		if (key == null) {
			wnfUmrechnungsfaktor.setInteger(null);
			wnfUmrechnungsfaktor.setMandatoryField(false);
		} else {
			wnfUmrechnungsfaktor.setMandatoryField(true);
		}
	}

}

class PanelArtikel_wcoEinheitBestellung_actionAdapter implements ActionListener {
	private PanelArtikel adaptee;

	PanelArtikel_wcoEinheitBestellung_actionAdapter(PanelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoEinheitBestellung_actionPerformed(e);
	}
}

class PanelArtikel_wtfArtikelnummer_focusAdapter extends FocusAdapter {
	private PanelArtikel adaptee;

	PanelArtikel_wtfArtikelnummer_focusAdapter(PanelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfArtikelnummer_focusLost(e);
	}
}
