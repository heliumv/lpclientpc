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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.bestellung.TabbedPaneBestellung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse bietet Felder zur Eingabe und Anzeige von Identnummern und
 * Bezeichnungen
 * </p>
 * Zum flexiblen GUI-Einbau ist das IdentField keine GUI-Komponente, sondern
 * bietet ihre Bestandteile an. Button, Identnummernfeld und die 3
 * Bezeichnungsfelder koennen damit in jedem Panel beliebig angeordnet werden.
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 10.04.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/01/23 12:41:05 $
 */
public class WrapperIdentField implements ActionListener, FocusListener {
	protected InternalFrame internalFrame = null;
	private PanelBasis panelBasis = null;
	private String belegartCNr = null;
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRKundenidentnummer = null;
	private ArtikelDto artikelDto = null;
	private LinkedList<WrapperLabel> listEinheitLabels;
	private FilterKriterium[] defaultFilter = null;
	private FilterKriterium[] defaultFilterArtikelLieferant = null;

	// identfield: 0 das identfield verwaltet die gui-komponenten, fuer alle
	// gibt es einen getter
	private WrapperGotoButton wbuArtikel = null;
	private WrapperTextField wtfIdent = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperTextField wtfZusatzBezeichnung = null;
	private WrapperTextField wtfZusatzBezeichnung2 = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperIdentField_panelQueryFLRArtikel_eventItemChangedAdapter eventAdapter = null;
	private Integer kundeIId = null;

	private WrapperCheckBox wcbAlleLieferanten = new WrapperCheckBox();

	private boolean bMitLeerenButton = false;

	public boolean isBMitLeerenButton() {
		return bMitLeerenButton;
	}

	public void setBMitLeerenButton(boolean bMitLeerenButton) {
		this.bMitLeerenButton = bMitLeerenButton;
	}

	private WrapperButton buttonKunde = null;
	private static final String ACTION_KUNDE = "ACTION_KUNDE";

	public final static String ACTION_ARTIKEL = "WRAPPERIDENTFIELD_ACTION_ARTIKEL";

	public WrapperIdentField() {
		// do nothing, just for testing.
	}

	public WrapperIdentField(InternalFrame internalFrame, PanelBasis panelBasis)
			throws Throwable {
		this.internalFrame = internalFrame;
		this.panelBasis = panelBasis;
		jbInit();
		setDefaults();
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	private void setDefaults() throws Throwable {
		defaultFilter = ArtikelFilterFactory.getInstance()
				.createFKArtikelliste();
		// Default ist die Ident ein Pflichtfeld
		wtfIdent.setMandatoryField(true);
		// Default: Eingabelaenge beschraenken
		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;
		// Eingabelaenge auf die maximale Stellenanzahl beschraenken
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		if (parameter.getCWertAsObject() != null) {
			iLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		if (parameter.getCWertAsObject() != null) {
			iLaenge += ((Integer) parameter.getCWertAsObject()).intValue();
		}

		wtfIdent.setColumnsMax(iLaenge);
		// Identnummern nur in Blockschrift
		wtfIdent.setUppercaseField(true);
	}

	private void jbInit() {
		listEinheitLabels = new LinkedList<WrapperLabel>();
		wbuArtikel = new WrapperGotoButton(
				WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);
		wtfIdent = new WrapperTextField();
		wtfBezeichnung = new WrapperTextField();
		wtfZusatzBezeichnung = new WrapperTextField();
		wtfZusatzBezeichnung2 = new WrapperTextField();
		wlaBezeichnung = new WrapperLabel();

		buttonKunde = new WrapperButton();
		buttonKunde.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/handshake16x16.png")));
		buttonKunde.setActionCommand(ACTION_KUNDE);
		buttonKunde.addActionListener(this);
		wcbAlleLieferanten.setText(LPMain
				.getTextRespectUISPr("best.artikelauswahl.allelieferanten"));
		wcbAlleLieferanten.addActionListener(this);
		wcbAlleLieferanten.setPreferredSize(new Dimension(150, 0));

		wtfBezeichnung.setActivatable(false);
		wtfZusatzBezeichnung.setActivatable(false);
		wtfZusatzBezeichnung2.setActivatable(false);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("label.bezeichnung"));
		wbuArtikel.setText(LPMain.getTextRespectUISPr("button.artikel"));
		wbuArtikel.setToolTipText(LPMain
				.getTextRespectUISPr("button.artikel.tooltip"));
		// ActionListener auf den Artikel
		// identfield: 1 actionlistener auf den artikelbutton
		// es koennen sich auch andere actionlistener anhaengen -> beachte
		// actionCommand
		wbuArtikel.setActionCommand(ACTION_ARTIKEL);
		wbuArtikel.addActionListener(this);
		// FocusListener IdentFeld
		wtfIdent.addFocusListener(this);
		// eventhandling
		eventAdapter = new WrapperIdentField_panelQueryFLRArtikel_eventItemChangedAdapter(
				this);
		// identfield: 2 allgemeiner ItemChangedListener fuer die Auswahlliste
		// damit kriegen auch die panels die events der liste mit
		internalFrame.addItemChangedListener(eventAdapter);
		// Strg-S aus PanelBasis mitkriegen
		if (panelBasis != null) {
			// Den Save-Button des Panels holen
			LPButtonAction item = (LPButtonAction) panelBasis.getHmOfButtons()
					.get(PanelBasis.ACTION_SAVE);
			item.getButton().addActionListener(this);
		}
	}

	private void dialogQueryArtikel(String sArtikelnummerVorbesetzt)
			throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		if (bMitLeerenButton == false) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };
		}

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_DEFAULT_ARTIKELAUSWAHL);
		boolean bDefaultArtikelauswahl = (java.lang.Boolean) parameter
				.getCWertAsObject();

		boolean bArtikelDesLieferantenAnzeigen = false;

		if (bDefaultArtikelauswahl == false && internalFrame != null
				&& internalFrame instanceof InternalFrameBestellung) {
			// Wenn ich in den Bestellpositionen bin, dann nur die Artikel des
			// Lieferanten anzeigen
			InternalFrameBestellung intBest = (InternalFrameBestellung) internalFrame;
			if (intBest.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPaneBestellung) {
				TabbedPaneBestellung tpBest = (TabbedPaneBestellung) intBest
						.getTabbedPaneRoot().getSelectedComponent();
				if (tpBest.getSelectedIndex() == TabbedPaneBestellung.IDX_PANEL_BESTELLPOSITION) {
					bArtikelDesLieferantenAnzeigen = true;
					//
					FilterKriterium[] filters = defaultFilter.clone();

					defaultFilterArtikelLieferant = new FilterKriterium[filters.length + 1];
					for (int i = 0; i < filters.length; i++) {
						defaultFilterArtikelLieferant[i] = filters[i];
					}
					defaultFilterArtikelLieferant[filters.length] = ArtikelFilterFactory
							.getInstance().createFKArtikeleinesLieferanten(
									tpBest.getBesDto()
											.getLieferantIIdBestelladresse());
				}

			}
		}

		panelQueryFLRArtikel = new PanelQueryFLR(null, defaultFilter,
				QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				internalFrame,
				LPMain.getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel());
		panelQueryFLRArtikel = new PanelQueryFLR(null,
				bArtikelDesLieferantenAnzeigen ? defaultFilterArtikelLieferant
						: defaultFilter, QueryParameters.UC_ID_ARTIKELLISTE,
				aWhichButtonIUse, internalFrame,
				LPMain.getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel());
		panelQueryFLRArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));
		if (bArtikelDesLieferantenAnzeigen) {
			panelQueryFLRArtikel.getToolBar().getToolsPanelCenter()
					.add(wcbAlleLieferanten);
		}

		FilterKriteriumDirekt fkdArtikelnummer = ArtikelFilterFactory
				.getInstance().createFKDArtikelnummer(internalFrame);

		//PJ18332
		if (sArtikelnummerVorbesetzt != null
				&& sArtikelnummerVorbesetzt.length() > fkdArtikelnummer.iEingabebreite) {
			sArtikelnummerVorbesetzt = sArtikelnummerVorbesetzt.substring(0,
					fkdArtikelnummer.iEingabebreite);
		}
		
		if (sArtikelnummerVorbesetzt != null){
			sArtikelnummerVorbesetzt=sArtikelnummerVorbesetzt.trim();

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELNUMMER_AUSWAHL_ABSCHNEIDEN);
			int iAnzahlAbschneiden = (java.lang.Integer) parameter
					.getCWertAsObject();

			if(iAnzahlAbschneiden>0 && sArtikelnummerVorbesetzt.length()>=iAnzahlAbschneiden){
				sArtikelnummerVorbesetzt=sArtikelnummerVorbesetzt.substring(0, sArtikelnummerVorbesetzt.length()-iAnzahlAbschneiden);
			}
			
			
		}
		

		fkdArtikelnummer.value = sArtikelnummerVorbesetzt;

		panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
				fkdArtikelnummer, ArtikelFilterFactory.getInstance()
						.createFKDVolltextsuche());
		panelQueryFLRArtikel.addDirektFilter(ArtikelFilterFactory.getInstance()
				.createFKDLieferantennrBezeichnung());
		parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
		boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bDirektfilterAGAKStattReferenznummer) {
			panelQueryFLRArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDAKAG());
		} else {
			panelQueryFLRArtikel.addDirektFilter(ArtikelFilterFactory
					.getInstance().createFKDReferenznr());
		}
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_SI_WERT)) {

			SortierKriterium krit = new SortierKriterium("aspr.c_siwert", true,
					"ASC");

			panelQueryFLRArtikel.setzeErstesUebersteuerbaresSortierkriterium(
					LPMain.getTextRespectUISPr("artikel.auswahl.sortbysiwert"),
					krit);
		}

		if (artikelDto != null) {
			panelQueryFLRArtikel.setSelectedId(artikelDto.getIId());
		}
		/**
		 * @todo MB->MB das geht vielleicht noch ohne den zusaetzlichen refresh
		 *       PJ 5339
		 */
		// wenn vorbesetzt wurde, dann noch ein refresh inklusive
		// direktfilter
		if (fkdArtikelnummer.value != null
				&& !fkdArtikelnummer.value.trim().equals("")) {
			panelQueryFLRArtikel.eventActionRefresh(null, false);
		}

		panelQueryFLRArtikel.addStatusBar();

		new DialogQuery(panelQueryFLRArtikel);

	}

	public WrapperButton getKundenidentnummerButton() {
		return buttonKunde;
	}

	public WrapperGotoButton getWbuArtikel() {
		return wbuArtikel;
	}

	public WrapperTextField getWtfBezeichnung() {
		return wtfBezeichnung;
	}

	public WrapperTextField getWtfZusatzBezeichnung() {
		return wtfZusatzBezeichnung;
	}

	public WrapperTextField getWtfZusatzBezeichnung2() {
		return wtfZusatzBezeichnung2;
	}

	public WrapperTextField getWtfIdent() {
		return wtfIdent;
	}

	public WrapperLabel getWlaBezeichnumg() {
		return wlaBezeichnung;
	}

	/**
	 * identfield: 7 artikel reinsetzen
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @throws Throwable
	 */
	public void setArtikelDto(ArtikelDto artikelDto) throws Throwable {
		this.artikelDto = artikelDto;
		dto2Components();
	}

	private void dto2Components() throws Throwable {
		if (artikelDto != null) {
			wtfIdent.setText(artikelDto.getCNr());
			// Goto Button Ziel setzen
			wbuArtikel.setOKey(artikelDto.getIId());
			if (artikelDto.getArtikelsprDto() != null) {
				wtfBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
				wtfZusatzBezeichnung.setText(artikelDto.getArtikelsprDto()
						.getCZbez());
				wtfZusatzBezeichnung2.setText(artikelDto.getArtikelsprDto()
						.getCZbez2());
			} else {
				wtfBezeichnung.setText(null);
				wtfZusatzBezeichnung.setText(null);
				wtfZusatzBezeichnung2.setText(null);
			}
			// setEinheitToLabels(artikelDto.getEinheitCNr());
			if (artikelDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.einheitFindByPrimaryKey(artikelDto.getEinheitCNr());
				setEinheitToLabels(einheitDto.formatBez());
			} else {
				setEinheitToLabels(null);
			}
		} else {
			wtfIdent.setText(null);
			wtfBezeichnung.setText(null);
			wtfZusatzBezeichnung.setText(null);
			wtfZusatzBezeichnung2.setText(null);
			setEinheitToLabels(null);
			wbuArtikel.setOKey(null);
		}
	}

	/**
	 * identfield: 6 artikel auslesen
	 * 
	 * @return ArtikelDto
	 */
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	/**
	 * identfield: 5 jedes hier hinzugefuegt wrapperLabel zeigt die einheit des
	 * artikels an
	 * 
	 * @param wlaEinheit
	 *            WrapperLabel
	 */
	public void addEinheitLabel(WrapperLabel wlaEinheit) {
		listEinheitLabels.add(wlaEinheit);
	}

	private void setEinheitToLabels(String sEinheit) {
		for (Iterator<WrapperLabel> iter = listEinheitLabels.iterator(); iter
				.hasNext();) {
			WrapperLabel item = (WrapperLabel) iter.next();
			item.setText(sEinheit);
		}
	}

	/**
	 * Das aktuelle QueryPanel. Zum behandeln von Events. // identfield: 3 die
	 * source aller events der artikelliste (zum mithoeren von aussen)
	 * 
	 * @return PanelQueryFLR
	 */
	public PanelQueryFLR getPanelQueryFLRArtikel() {
		return panelQueryFLRArtikel;
	}

	public PanelQueryFLR getPanelQueryFLRKundenidentnummer() {
		return panelQueryFLRKundenidentnummer;
	}

	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource().equals(wcbAlleLieferanten)) {
				if (panelQueryFLRArtikel != null) {

					if (wcbAlleLieferanten.isSelected()) {
						panelQueryFLRArtikel.setDefaultFilter(defaultFilter);
					} else {
						panelQueryFLRArtikel
								.setDefaultFilter(defaultFilterArtikelLieferant);
					}

					panelQueryFLRArtikel.eventActionRefresh(null, false);
				}
			} else {

				if (e.getActionCommand().equals(ACTION_ARTIKEL)) {
					dialogQueryArtikel(wtfIdent.getText());
				} else if (e.getActionCommand().equals(ACTION_KUNDE)) {

					try {
						panelQueryFLRKundenidentnummer = PartnerFilterFactory
								.getInstance().createPanelFLRKundenidentnummer(
										internalFrame, bMitLeerenButton,
										kundeIId);

						new DialogQuery(panelQueryFLRKundenidentnummer);
					} catch (Throwable ex) {
						internalFrame.handleException(ex, true);
					}

				}
			}

			// else if (e.getActionCommand().equals(PanelBasis.ACTION_SAVE)) {
			// // Bevor der Der Benutzer speichern kann muss noch die
			// eingegebene Ident geprueft werden
			// if (artikelDto == null) {
			// if (wtfIdent.getText() == null) {
			// // keine ident eingegeben -> passt
			// }
			// else {
			// ArtikelDto aDto =
			// DelegateFactory.getInstance().getArtikelDelegate().
			// artikelFindByCNr(wtfIdent.getText());
			// setArtikelDto(aDto);
			// }
			// }
			// }
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame, t);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		try {
			// focusLost am Ident-Feld, aber ich bleibe in HeliumV
			if (e.getSource() == wtfIdent) {
				if (wtfIdent.getText() == null) {
					// keine ident eingegeben
				} else {

					// Wenn auf den Button geklicht wurde, dann macht die
					// Auswahl die Liste
					if (e.getOppositeComponent() != null
							&& e.getOppositeComponent() == wbuArtikel
									.getWrapperButton()) {
						return;
					}

					try {
						ArtikelDto aDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artikelFindByCNr(wtfIdent.getText());

						if (Helper.short2boolean(aDto.getBVersteckt())) {

							if (DelegateFactory
									.getInstance()
									.getTheJudgeDelegate()
									.hatRecht(
											RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
								// Meldung
								boolean b = DialogFactory
										.showModalJaNeinDialog(
												internalFrame,
												LPMain.getTextRespectUISPr("artikel.versteckt.verwenden"));
								if (b == false) {
									wtfIdent.setText(null);
									aDto = null;
									return;
								}
							} else {
								wtfIdent.setText(null);
								aDto = null;
								return;
							}
						}

						if (belegartCNr != null) {
							aDto = DelegateFactory
									.getInstance()
									.getArtikelkommentarDelegate()
									.pruefeArtikel(aDto, belegartCNr,
											internalFrame);
						} else {
							aDto = DelegateFactory
									.getInstance()
									.getArtikelkommentarDelegate()
									.pruefeArtikel(aDto,
											internalFrame.getBelegartCNr(),
											internalFrame);
						}

						if (aDto != null) {

							this.setArtikelDto(aDto);
							// Benutzer informieren
							if (wtfIdent.isEditable() == true) {
								fireItemChangedEvent_GOTO_DETAIL_PANEL();
							}
						}
					} catch (ExceptionLP ex) {
						switch (ex.getICode()) {
						case EJBExceptionLP.FEHLER_BEI_FIND: {
							dialogQueryArtikel(wtfIdent.getText());
						}
							break;
						default: {
							throw ex;
						}
						}
					}
				}
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame);
		}
	}

	/**
	 * Filter auf die Artikelliste setzen. identfield: 4 beliebigen
	 * defaultfilter setzen
	 * 
	 * @param defaultFilter
	 *            FilterKriterium[]
	 */
	public void setDefaultFilter(FilterKriterium[] defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public void setComponentNames(String sFieldName) {
		wbuArtikel.setName(sFieldName + "_wbuArtikel");
		wlaBezeichnung.setName(sFieldName + "_wlaBezeichnung");
		wtfBezeichnung.setName(sFieldName + "_wtfBezeichnung");
		wtfIdent.setName(sFieldName + "_wtfIdent");
		wtfZusatzBezeichnung.setName(sFieldName + "_wtfZusatzBezeichnung");
		wtfZusatzBezeichnung2.setName(sFieldName + "_wtfZusatzBezeichnung2");
	}

	public void refresh() throws Throwable {
		if (wtfIdent.getText() != null) {
			ArtikelDto aDto = null;
			try {
				aDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByCNr(wtfIdent.getText());
			} catch (ExceptionLP ex) {
				switch (ex.getICode()) {
				case EJBExceptionLP.FEHLER_BEI_FIND: {
					// nothing here
				}
					break;
				default: {
					throw ex;
				}
				}
			}
			if (aDto != null) {
				setArtikelDto(aDto);
			} else {
				wtfIdent.setText(null);
				wbuArtikel.setOKey(null);
			}
		}
	}

	/**
	 * Die Eingabe pruefen. Falls im Textfeld Ident eine ungueltige
	 * Artikelnummer steht, wird das Feld geleert.
	 * 
	 * @throws Throwable
	 */
	public void validate() throws Throwable {
		// Bevor der Der Benutzer speichern kann muss noch die eingegebene Ident
		// geprueft werden
		if (artikelDto == null) {
			if (wtfIdent.getText() == null) {
				// keine ident eingegeben -> passt
			} else {
				ArtikelDto aDto = null;
				try {
					aDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByCNr(wtfIdent.getText());
				} catch (ExceptionLP ex) {
					switch (ex.getICode()) {
					case EJBExceptionLP.FEHLER_BEI_FIND: {
						// nothing here
					}
						break;
					default: {
						throw ex;
					}
					}
				}
				if (aDto != null) {
					setArtikelDto(aDto);
				} else {
					wtfIdent.setText(null);
					wbuArtikel.setOKey(null);
				}
			}
		}
	}

	protected void fireItemChangedEvent_GOTO_DETAIL_PANEL() {
		internalFrame.fireItemChanged(this, ItemChangedEvent.GOTO_DETAIL_PANEL);
	}
}

class WrapperIdentField_panelQueryFLRArtikel_eventItemChangedAdapter implements
		ItemChangedListener {
	private WrapperIdentField adaptee;

	WrapperIdentField_panelQueryFLRArtikel_eventItemChangedAdapter(
			WrapperIdentField adaptee) {
		this.adaptee = adaptee;
	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == adaptee.getPanelQueryFLRArtikel()) {
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(key);

					artikelDto = DelegateFactory
							.getInstance()
							.getArtikelkommentarDelegate()
							.pruefeArtikel(artikelDto,
									adaptee.internalFrame.getBelegartCNr(),
									adaptee.internalFrame);
					if (artikelDto != null) {
						adaptee.setArtikelDto(artikelDto);
						// Benutzer informieren
						adaptee.fireItemChangedEvent_GOTO_DETAIL_PANEL();
					}
				}
				if (e.getSource() == adaptee
						.getPanelQueryFLRKundenidentnummer()) {
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					com.lp.server.partner.service.KundesokoDto kundesokoDto = DelegateFactory
							.getInstance().getKundesokoDelegate()
							.kundesokoFindByPrimaryKey(key);

					if (kundesokoDto.getArtikelIId() != null) {
						ArtikelDto artikelDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikelFindByPrimaryKey(
										kundesokoDto.getArtikelIId());

						artikelDto = DelegateFactory
								.getInstance()
								.getArtikelkommentarDelegate()
								.pruefeArtikel(artikelDto,
										adaptee.internalFrame.getBelegartCNr(),
										adaptee.internalFrame);
						if (artikelDto != null) {
							adaptee.setArtikelDto(artikelDto);
							// Benutzer informieren
							adaptee.fireItemChangedEvent_GOTO_DETAIL_PANEL();
						}

					}

				}
			} else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
				if (adaptee.getPanelQueryFLRArtikel() != null
						&& e.getSource().equals(
								adaptee.getPanelQueryFLRArtikel())
						&& adaptee.getPanelQueryFLRArtikel().getSelectedId() != null) {

					// Integer key = (Integer) ( (WrapperTable)
					// e.getSource()).getIdSelected();
					ArtikelDto artikelDto = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									(Integer) adaptee.getPanelQueryFLRArtikel()
											.getSelectedId());
					Integer materialIId = artikelDto.getMaterialIId();
					if (materialIId != null) {
						MaterialDto materialDto = DelegateFactory.getInstance()
								.getMaterialDelegate()
								.materialFindByPrimaryKey(materialIId);

						adaptee.getPanelQueryFLRArtikel()
								.setStatusbarSpalte4(
										LPMain.getTextRespectUISPr("fert.tab.oben.material.title")
												+ ": "
												+ materialDto.getBezeichnung());
					} else {
						adaptee.getPanelQueryFLRArtikel().setStatusbarSpalte4(
								"");

					}
					if (artikelDto.getVerpackungDto() != null
							&& artikelDto.getVerpackungDto()
									.getCVerpackungsart() != null) {
						adaptee.getPanelQueryFLRArtikel()
								.setStatusbarSpalte5(
										LPMain.getTextRespectUISPr("artikel.technik.verpackungsart")
												+ ": "
												+ artikelDto.getVerpackungDto()
														.getCVerpackungsart(),
										true);
					} else {
						adaptee.getPanelQueryFLRArtikel().setStatusbarSpalte5(
								"", true);

					}

					if (artikelDto.getVerpackungDto() != null
							&& artikelDto.getVerpackungDto().getCBauform() != null) {
						adaptee.getPanelQueryFLRArtikel()
								.setStatusbarSpalte6(
										LPMain.getTextRespectUISPr("artikel.technik.bauform")
												+ ": "
												+ artikelDto.getVerpackungDto()
														.getCBauform(), true);
					} else {
						adaptee.getPanelQueryFLRArtikel().setStatusbarSpalte6(
								"", true);

					}

				}
			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == adaptee.getPanelQueryFLRArtikel()) {
					adaptee.setArtikelDto(null);
				}
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(adaptee.internalFrame, t);
		}
	}
}
