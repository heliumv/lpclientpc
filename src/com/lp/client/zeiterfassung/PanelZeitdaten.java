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
package com.lp.client.zeiterfassung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.PersonalzeitmodellDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitmodellDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelZeitdaten extends PanelBasis implements
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private ZeitdatenDto zeitdatenDto = null;
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRAuftragposition = null;
	private PanelQueryFLR panelQueryFLRAngebotposition = null;
	private PanelQueryFLR panelQueryFLRLosposition = null;
	private PanelQueryFLR panelQueryFLRZeitmodell = null;

	private PanelQueryFLR panelQueryFLRProjekt = null;
	private PanelQueryFLR panelQueryFLRAngebot = null;

	private boolean bRechtNurBuchen = true;
	private boolean bDarfKommtGehtAendern = true;
	private WrapperLabel wlaZeit = new WrapperLabel();
	private WrapperTimeField wtfZeit = new WrapperTimeField();
	private WrapperComboBox wcoSonderTaetigkeit = new WrapperComboBox();
	private WrapperComboBox wcoBeleg = new WrapperComboBox();
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperLabel wlaBetriebskalender = new WrapperLabel();
	private WrapperLabel wlaSonderzeiten = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private JButton wbuTagZurueck = new JButton();
	private JButton wbuNaechsterTag = new JButton();
	private WrapperCheckBox wcbRelativ = new WrapperCheckBox();
	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "action_taetigkeit_from_liste";
	static final public String ACTION_SPECIAL_BELEG_FROM_LISTE = "action_beleg_from_liste";
	static final public String ACTION_SPECIAL_POSITION_FROM_LISTE = "action_position_from_liste";
	static final public String ACTION_SPECIAL_ZEITMODELL_FROM_LISTE = "ACTION_SPECIAL_ZEITMODELL_FROM_LISTE";
	private WrapperLabel wlaKalenderwochewochentag = new WrapperLabel();
	private WrapperLabel wlaTagesarbeitszeit = new WrapperLabel();
	private WrapperGotoButton wbuBeleg = new WrapperGotoButton(-1);
	private WrapperButton wbuPosition = new WrapperButton();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperTextField wtfPosition = new WrapperTextField();
	private Integer selectedBeleg = null;
	private WrapperRadioButton wrbSondertaetigkeit = new WrapperRadioButton();
	private WrapperRadioButton wrbAuftragszeit = new WrapperRadioButton();
	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private WrapperButton wbuTaetigkeit = new WrapperButton();
	private WrapperTextField wtfTaetigkeit = new WrapperTextField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperLabel wlaErfuellungsgrad = new WrapperLabel();

	private boolean hatModulStueckrueckmeldung = false;
	private boolean bLosbuchungOhnePositionbezug = false;

	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(
			getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"));

	private JButton wbuZeitmodell = new JButton();
	private WrapperTextField wtfZeitmodell = new WrapperTextField();
	private int iOptionSollzeitpruefeung = 0;

	private WrapperLabel wlaFehlerInZeitdaten = new WrapperLabel();
	private WrapperLabel wlaOffeneZeitverteilung = new WrapperLabel();

	private Integer zeitmodellIId = null;
	private boolean bZeitdatenAufErledigteBuchbar = false;
	private boolean bZeitdatenAufAngelegteLoseBuchbar = false;

	Integer taetigkeitIIdKommt = null;
	Integer taetigkeitIIdUnter = null;
	Integer taetigkeitIIdGeht = null;

	private Map<Integer, String> sondertaetigkeitenOhneVersteckt;
	private Map<Integer, String> sondertaetigkeitenMitVersteckt;

	private boolean bArbeitszeitartikelauspersonalverfuegbarkeit = false;

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfZeit;
	}

	public WrapperDateField getWdfDatum() {
		return wdfDatum;
	}

	public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
		return internalFrameZeiterfassung;
	}

	public PanelZeitdaten(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		hatModulStueckrueckmeldung = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOSBUCHUNG_OHNE_POSITIONSBEZUG_BEI_STUECKRUECK,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		bLosbuchungOhnePositionbezug = (Boolean) parameter.getCWertAsObject();

		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;

		bRechtNurBuchen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN);
		bDarfKommtGehtAendern = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN);
		taetigkeitIIdKommt = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT)
				.getIId();
		taetigkeitIIdGeht = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT).getIId();
		taetigkeitIIdUnter = DelegateFactory.getInstance()
				.getZeiterfassungDelegate()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER)
				.getIId();
		jbInit();
		setDefaults();
		initComponents();

	}

	public void propertyChange(PropertyChangeEvent e) {
		// System.out.println(e.getPropertyName());
		if (e.getSource() == wdfDatum.getDisplay()
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfDatum.setDate((Date) e.getNewValue());
			try {

				// wrbSondertaetigkeit.setSelected(true);

				aktualisiereDaten();

				Object sKey = getInternalFrame().getKeyWasForLockMe();

				getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
						.refreshQPZeitdaten(
								getInternalFrameZeiterfassung()
										.getPersonalDto().getIId(),
								wdfDatum.getDate(),
								LPMain.getLockMeForNew().equals(sKey));

			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaErfuellungsgrad.setText(null);

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			clearStatusbar();
			wtfBeleg.setText("");
			wtfPosition.setText("");
			wtfTaetigkeit.setText("");
			wtfBemerkung.setText("");
			if (bDarfKommtGehtAendern == false) {
				if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
						taetigkeitIIdGeht)
						|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
								taetigkeitIIdKommt)
						|| wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
								taetigkeitIIdUnter)) {
					wtfZeit.setEnabled(false);
				} else {
					wtfZeit.setEnabled(true);
				}

			}
			if (wrbAuftragszeit.isSelected()) {
				wcbRelativ.setEnabled(true);
			} else {
				wcbRelativ.setEnabled(false);
			}
		} else {
			zeitdatenDto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.zeitdatenFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());

			if (zeitdatenDto.getCBelegartnr() != null) {
				wcbRelativ.setVisible(true);
			} else {
				wcbRelativ.setVisible(false);

			}

			dto2Components();
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.refreshTitle();

			if (bDarfKommtGehtAendern == false) {

				if (zeitdatenDto.getTaetigkeitIId() != null) {

					if (zeitdatenDto.getTaetigkeitIId().equals(
							taetigkeitIIdGeht)
							|| zeitdatenDto.getTaetigkeitIId().equals(
									taetigkeitIIdKommt)
							|| zeitdatenDto.getTaetigkeitIId().equals(
									taetigkeitIIdUnter)) {
						LPButtonAction o = getHmOfButtons().get(
								PanelBasis.ACTION_UPDATE);
						if (o != null) {
							o.getButton().setEnabled(false);
						}
						o = getHmOfButtons().get(PanelBasis.ACTION_DELETE);
						if (o != null) {
							o.getButton().setEnabled(false);
						}
					}
				}

			}

		}
		berechneTageszeit();
		wdfDatum.setEnabled(true);

	}

	protected void dto2Components() throws Throwable {
		wtfZeit.setTime(new java.sql.Time(zeitdatenDto.getTZeit().getTime()));
		wefKommentar.setText(zeitdatenDto.getXKommentar());

		if (zeitdatenDto.getTaetigkeitIId() != null) {
			wrbSondertaetigkeit.setSelected(true);

			wcoBeleg.setVisible(false);
			wcoBeleg.setMandatoryField(false);
			wcoSonderTaetigkeit.setVisible(true);

			jpaWorkingOn.repaint();

			TaetigkeitDto taetigkeit = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByPrimaryKey(zeitdatenDto.getTaetigkeitIId());
			if (taetigkeit != null
					&& Helper.short2boolean(taetigkeit.getBVersteckt())) {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);
			} else {
				wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
			}
			wcoSonderTaetigkeit.setKeyOfSelectedItem(zeitdatenDto
					.getTaetigkeitIId());
			wtfTaetigkeit.setText(null);
			wtfBeleg.setText(null);
			wtfPosition.setText(null);

			wtfPosition.setMandatoryField(false);
			wtfBeleg.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);
			wcoSonderTaetigkeit.setActivatable(true);
			wbuBeleg.setActivatable(false);
			wcoBeleg.setActivatable(false);
			wcbRelativ.setActivatable(false);
			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(false);
			wtfBemerkung.setText(zeitdatenDto.getCBemerkungZuBelegart());
		} else {
			wcoBeleg.setKeyOfSelectedItem(zeitdatenDto.getCBelegartnr());
			wbuBeleg.setOKey(zeitdatenDto.getIBelegartid());
			wrbAuftragszeit.setSelected(true);
			if (wcbRelativ.isSelected()) {
				java.sql.Time zeit = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.getRelativeZeitFuerRelativesAendernAmClient(
								zeitdatenDto.getPersonalIId(),
								zeitdatenDto.getTZeit());
				wtfZeit.setTime(zeit);
			}

			wcoSonderTaetigkeit.setVisible(false);
			wcoBeleg.setVisible(true);
			wcoBeleg.setMandatoryField(true);
			jpaWorkingOn.repaint();
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(zeitdatenDto.getArtikelIId());

			wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
			wtfBemerkung.setText(zeitdatenDto.getCBemerkungZuBelegart());

			if (zeitdatenDto.getCBelegartnr()
					.equals(LocaleFac.BELEGART_AUFTRAG)) {

				AuftragpositionDto auftragpositionDto = null;
				try {
					auftragpositionDto = DelegateFactory
							.getInstance()
							.getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey(
									zeitdatenDto.getIBelegartpositionid());
					wtfPosition.setText(auftragpositionDto.getCBez());
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										(LPMain.getTextRespectUISPr("zeiterfassung.auftragspositiongeloescht")));
					} else {
						handleException(ex, false);
					}
				}

				erfuellungsgradBerechnen(null, auftragpositionDto);

				AuftragDto auftragDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (auftragDto.getCBezProjektbezeichnung() != null) {
					sProjBez = ", " + auftragDto.getCBezProjektbezeichnung();
				}

				String kunde = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								auftragDto.getKundeIIdAuftragsadresse())
						.getPartnerDto().formatTitelAnrede();

				wtfBeleg.setText(auftragDto.getCNr() + sProjBez + ", " + kunde);
				selectedBeleg = auftragDto.getIId();
				if (auftragpositionDto != null) {
					zeitdatenDto.setIBelegartpositionid(auftragpositionDto
							.getIId());
					if (auftragpositionDto.getArtikelIId() != null
							&& auftragpositionDto.getCBez() == null) {
						wtfPosition.setText(DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikelFindByPrimaryKey(
										auftragpositionDto.getArtikelIId())
								.formatArtikelbezeichnung());
					}
				}
				wtfPosition.setMandatoryField(true);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wcoSonderTaetigkeit.setActivatable(false);
				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);

				wcoBeleg.setActivatable(true);
				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_LOS)) {

				if (zeitdatenDto.getIBelegartpositionid() != null) {
					LossollarbeitsplanDto lospositionDto = null;
					try {
						lospositionDto = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.lossollarbeitsplanFindByPrimaryKey(
										zeitdatenDto.getIBelegartpositionid());
						wtfPosition.setText(lospositionDto.getIId() + "");
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.lospositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (lospositionDto != null) {
						zeitdatenDto.setIBelegartpositionid(lospositionDto
								.getIId());
						if (lospositionDto.getArtikelIIdTaetigkeit() != null) {
							wtfPosition.setText(DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artikelFindByPrimaryKey(
											lospositionDto
													.getArtikelIIdTaetigkeit())
									.getCNr());
						}

					}
					erfuellungsgradBerechnen(lospositionDto, null);
				} else {
					erfuellungsgradBerechnen(null, null);
				}
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (losDto.getCProjekt() != null) {
					sProjBez = ", " + losDto.getCProjekt();
				}
				wtfBeleg.setText(losDto.getCNr() + sProjBez);
				selectedBeleg = losDto.getIId();

				if (!hatModulStueckrueckmeldung) {
					wtfPosition.setMandatoryField(false);
				} else {
					if (bLosbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}
				}
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wcoSonderTaetigkeit.setActivatable(false);
				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);
				wcoBeleg.setActivatable(true);
				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_PROJEKT)) {

				ProjektDto projektDto = DelegateFactory.getInstance()
						.getProjektDelegate()
						.projektFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (projektDto.getCTitel() != null) {
					sProjBez = ", " + projektDto.getCTitel();
				}
				wtfBeleg.setText(projektDto.getCNr() + sProjBez);
				selectedBeleg = projektDto.getIId();

				wtfPosition.setMandatoryField(false);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wcoSonderTaetigkeit.setActivatable(false);
				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);
				wcoBeleg.setActivatable(true);
				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			} else if (zeitdatenDto.getCBelegartnr().equals(
					LocaleFac.BELEGART_ANGEBOT)) {

				if (zeitdatenDto.getIBelegartpositionid() != null) {
					AngebotpositionDto angebotpositionDto = null;
					try {
						angebotpositionDto = DelegateFactory
								.getInstance()
								.getAngebotpositionDelegate()
								.angebotpositionFindByPrimaryKey(
										zeitdatenDto.getIBelegartpositionid());

					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.angebotpositiongeloescht"));
						} else {
							handleException(ex, false);
						}
					}
					if (angebotpositionDto != null) {
						zeitdatenDto.setIBelegartpositionid(angebotpositionDto
								.getIId());
						if (angebotpositionDto.getArtikelIId() != null) {
							wtfPosition.setText(angebotpositionDto.getCBez());
							if (angebotpositionDto.getArtikelIId() != null
									&& angebotpositionDto.getCBez() == null) {
								wtfPosition.setText(DelegateFactory
										.getInstance()
										.getArtikelDelegate()
										.artikelFindByPrimaryKey(
												angebotpositionDto
														.getArtikelIId())
										.formatArtikelbezeichnung());
							}

						}
					}

				}
				AngebotDto angebotDto = DelegateFactory.getInstance()
						.getAngebotDelegate()
						.angebotFindByPrimaryKey(zeitdatenDto.getIBelegartid());

				String sProjBez = "";
				if (angebotDto.getCBez() != null) {
					sProjBez = ", " + angebotDto.getCBez();
				}
				wtfBeleg.setText(angebotDto.getCNr() + sProjBez);
				selectedBeleg = angebotDto.getIId();

				wtfPosition.setMandatoryField(false);
				wtfBeleg.setMandatoryField(true);
				wtfTaetigkeit.setMandatoryField(true);
				wcoSonderTaetigkeit.setActivatable(false);
				wcbRelativ.setActivatable(true);

				wbuBeleg.setActivatable(true);
				wcoBeleg.setActivatable(true);
				wbuPosition.setActivatable(true);
				wbuTaetigkeit.setActivatable(true);

			}

		}

		this.setStatusbarPersonalIIdAendern(zeitdatenDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(zeitdatenDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(zeitdatenDto.getTAnlegen());
		this.setStatusbarTAendern(zeitdatenDto.getTAendern());
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
			panelQueryFLRArtikel = new PanelQueryFLR(null,
					PersonalFilterFactory.getInstance().createFKPersonal(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId()),
					QueryParameters.UC_ID_PERSONALVERFUEGBARKEIT,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("title.artikelauswahlliste"));
			new DialogQuery(panelQueryFLRArtikel);

		} else {
			panelQueryFLRArtikel = new PanelQueryFLR(null, ArtikelFilterFactory
					.getInstance().createFKArtikellisteNurArbeitszeit(),
					QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
					internalFrameZeiterfassung,
					LPMain.getTextRespectUISPr("title.artikelauswahlliste"));

			FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory
					.getInstance().createFKDArtikelnummer(getInternalFrame());
			FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory
					.getInstance().createFKDVolltextsuche();
			panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(fkDirekt1,
					fkDirekt2);

			panelQueryFLRArtikel.setSelectedId(zeitdatenDto.getArtikelIId());

			new DialogQuery(panelQueryFLRArtikel);
		}
	}

	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN, PanelBasis.ACTION_FILTER };

		FilterKriterium[] kriterien = null;

		if (bZeitdatenAufErledigteBuchbar) {
			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
							+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);
			kriterien[0] = krit1;
			kriterien[1] = krit2;
		} else {

			kriterien = new FilterKriterium[2];
			FilterKriterium krit1 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
							+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
							+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','"
							+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			kriterien[0] = krit1;
			kriterien[1] = krit2;
		}
		panelQueryFLRAuftrag = new PanelQueryFLR(AuftragFilterFactory
				.getInstance().createQTPanelAuftragAuswahl(), kriterien,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));

		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		panelQueryFLRAuftrag.addDirektFilter(AuftragFilterFactory.getInstance()
				.createFKDProjekt());
		panelQueryFLRAuftrag.setSelectedId(zeitdatenDto.getIBelegartid());

		new DialogQuery(panelQueryFLRAuftrag);

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LOSAUSWAHL_TECHNIKERFILTER,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null
				&& ((Boolean) parameter.getCWertAsObject()) == true) {

			// Dialog'Wiedervorlage erstellen
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = new DialogLoseEinesTechnikers(
					getInternalFrame(), bZeitdatenAufErledigteBuchbar,
					zeitdatenDto.getIBelegartid(),
					getInternalFrameZeiterfassung().getPersonalDto().getIId());

			LPMain.getInstance()
					.getDesktop()
					.platziereDialogInDerMitteDesFensters(
							getInternalFrameZeiterfassung().dialogLoseEinesTechnikers);
			getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
					.setVisible(true);

		} else {
			panelQueryFLRLos = FertigungFilterFactory.getInstance()
					.createPanelFLRBebuchbareLose(getInternalFrame(),
							bZeitdatenAufErledigteBuchbar, true, bZeitdatenAufAngelegteLoseBuchbar,
							zeitdatenDto.getIBelegartid(), false);
			// PJ17681

			Map<?, ?> mEingeschraenkteFertigungsgruppen = DelegateFactory
					.getInstance().getStuecklisteDelegate()
					.getEingeschraenkteFertigungsgruppen();

			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryFLRLos.setFilterComboBox(
						mEingeschraenkteFertigungsgruppen, new FilterKriterium(
								"flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false), true);
				panelQueryFLRLos.eventActionRefresh(null, true);
			} else {

				panelQueryFLRLos.setFilterComboBox(DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.getAllFertigungsgrupe(), new FilterKriterium(
						"flrlos.fertigungsgruppe_i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false, LPMain
						.getTextRespectUISPr("lp.alle"));
			}

		}

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(),
						zeitdatenDto.getIBelegartid(), false);
		new DialogQuery(panelQueryFLRProjekt);

	}

	void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRAngebot = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebot(
						getInternalFrame(),
						false,
						false,
						AngebotFilterFactory.getInstance()
								.createFKAngebotOffene(),
						zeitdatenDto.getIBelegartid());
		new DialogQuery(panelQueryFLRAngebot);

	}

	void dialogQueryZeitmodellFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRZeitmodell = PersonalFilterFactory.getInstance()
				.createPanelFLRZeitmodell(getInternalFrame(), null, false);
		new DialogQuery(panelQueryFLRZeitmodell);
	}

	void dialogQueryAuftragpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		QueryType[] querytypes = null;
		panelQueryFLRAuftragposition = new PanelQueryFLR(querytypes,
				AuftragFilterFactory.getInstance()
						.createFKUmsatzrelevantePositionen(selectedBeleg),
				QueryParameters.UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAuftragposition);

	}

	void dialogQueryAngebotpositionFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] aFilterKrit = null;

		if (selectedBeleg != null) {
			aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRANGEBOT + ".i_id",
					true, selectedBeleg.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;

			FilterKriterium krit2 = new FilterKriterium(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_POSITIONART_C_NR,
					true, "'" + AngebotServiceFac.ANGEBOTPOSITIONART_IDENT
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[1] = krit2;

		}
		QueryType[] querytypes = null;
		panelQueryFLRAngebotposition = new PanelQueryFLR(querytypes,
				aFilterKrit, QueryParameters.UC_ID_ANGEBOTPOSITION,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("auft.title.panel.positionen"));

		new DialogQuery(panelQueryFLRAngebotposition);

	}

	private void dialogQueryLospositionFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRLosposition = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollarbeitsplan(getInternalFrame(),
						selectedBeleg, null);
		new DialogQuery(panelQueryFLRLosposition);
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wcbRelativ.setShort(Helper.boolean2Short(false));
		wcbRelativ
				.addActionListener(new PanelZeitdaten_wcbRelativ_actionAdapter(
						this));
		wcbRelativ
				.addChangeListener(new PanelZeitdaten_wcbRelativ_changeAdapter(
						this));
		wcbRelativ
				.addPropertyChangeListener(new PanelZeitdaten_wcbRelativ_propertyChangeAdapter(
						this));
		wcbRelativ.setVisible(false);
		wlaZeit.setText(LPMain.getTextRespectUISPr("lp.zeit"));
		wcoSonderTaetigkeit.setMandatoryField(true);
		wcoBeleg.setMandatoryField(true);
		wcoBeleg.addActionListener(new PanelZeitdaten_wcoBeleg_actionAdapter(
				this));
		wcoBeleg.setVisible(false);
		wcoSonderTaetigkeit
				.addActionListener(new PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter(
						this));
		wdfDatum.setMandatoryField(true);
		wdfDatum.setActivatable(false);

		wtfTaetigkeit.setActivatable(false);
		wtfZeitmodell.setActivatable(false);
		wtfTaetigkeit.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfZeitmodell.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfPosition.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		java.sql.Timestamp t = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(
						internalFrameZeiterfassung.getPersonalDto().getIId());
		if (t != null) {
			wdfDatum.setTimestamp(t);
		} else {
			wdfDatum.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));
		}

		wdfDatum.getDisplay().addPropertyChangeListener(this);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wbuTagZurueck.setText("<");
		wbuTagZurueck
				.addActionListener(new PanelZeitdaten_wbuTagZurueck_actionAdapter(
						this));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bArbeitszeitartikelauspersonalverfuegbarkeit = true;
		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ERLEDIGTE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufErledigteBuchbar = true;
		}
		
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ZEITBUCHUNG_AUF_ANGELEGTE_LOSE_MOEGLICH,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().equals("1")) {
			bZeitdatenAufAngelegteLoseBuchbar = true;
		}
		

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SOLLZEITPRUEFUNG,
						ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getTheClient().getMandant());

		iOptionSollzeitpruefeung = (Integer) parameter.getCWertAsObject();

		wbuNaechsterTag.setText(">");
		wbuNaechsterTag
				.addActionListener(new PanelZeitdaten_wbuNaechsterTag_actionAdapter(
						this));

		wlaKalenderwochewochentag.setText(LPMain
				.getTextRespectUISPr("lp.kalenderwoche_kurz"));
		wlaKalenderwochewochentag.setHorizontalAlignment(SwingConstants.LEFT);

		wlaBetriebskalender.setHorizontalAlignment(SwingConstants.LEFT);
		wlaTagesarbeitszeit
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.zeitdaten.tagesarbeitszeit"));

		wbuBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg") + "...");
		wbuBeleg.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_BELEG_FROM_LISTE);
		wbuBeleg.addActionListener(this);
		wbuPosition.setText(LPMain.getTextRespectUISPr("lp.position") + "...");
		wbuPosition
				.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_POSITION_FROM_LISTE);
		wbuPosition.addActionListener(this);
		wtfBeleg.setActivatable(false);
		wtfBeleg.setText("");
		wtfBeleg.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfPosition.setActivatable(false);
		wtfPosition.setText("");
		wtfBemerkung.setColumnsMax(ZeiterfassungFac.MAX_ZEITDATEN_BEMERKUNG);
		wtfZeit.setMandatoryField(true);
		wrbSondertaetigkeit
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.zeitdaten.sondertaetigkeit"));
		wcbRelativ.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.relativezeitbuchung"));
		wrbSondertaetigkeit
				.addActionListener(new PanelZeitdaten_wrbSondertaetigkeit_actionAdapter(
						this));
		wrbAuftragszeit.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.zeitdaten.auftragszeit"));
		wrbAuftragszeit
				.addActionListener(new PanelZeitdaten_wrbAuftragszeit_actionAdapter(
						this));
		wbuTaetigkeit.setText(LPMain.getTextRespectUISPr("lp.taetigkeit")
				+ "...");
		wbuTaetigkeit
				.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);

		wrbSondertaetigkeit.setSelected(true);
		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wbuZeitmodell.setText(LPMain.getTextRespectUISPr("lp.zeitmodell")
				+ "...");
		wbuZeitmodell
				.setActionCommand(PanelZeitdaten.ACTION_SPECIAL_ZEITMODELL_FROM_LISTE);
		wbuZeitmodell.addActionListener(this);


		String[] aWhichButtonIUse = null;

		if (bRechtNurBuchen) {
			aWhichButtonIUse = new String[] { ACTION_SAVE, ACTION_DISCARD, };
			wtfZeit.setActivatable(false);
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, };

		}

		enableToolsPanelButtons(aWhichButtonIUse);
		buttonGroup1.add(wrbSondertaetigkeit);
		buttonGroup1.add(wrbAuftragszeit);
		
		jpaWorkingOn = new JPanel(new MigLayout("wrap 7, hidemode 3", "[10%,fill|50!,fill|fill|50!,fill|30%,fill|20%,fill|30%,fill]"));
		
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jpaWorkingOn.add(wlaBetriebskalender, "skip 4");
		jpaWorkingOn.add(wlaSonderzeiten, "span");

		jpaWorkingOn.add(wlaDatum);
		jpaWorkingOn.add(wbuTagZurueck);
		jpaWorkingOn.add(wdfDatum, "grow, center");
		jpaWorkingOn.add(wbuNaechsterTag);
		jpaWorkingOn.add(wlaKalenderwochewochentag);
		jpaWorkingOn.add(wlaErfuellungsgrad, "span");
		
		jpaWorkingOn.add(wlaZeit);
		jpaWorkingOn.add(wtfZeit, "skip");
		jpaWorkingOn.add(wcbRelativ, "hidemode 0, span 2");
		jpaWorkingOn.add(wlaTagesarbeitszeit, "span");

		jpaWorkingOn.add(wrbSondertaetigkeit, "span 2");
		jpaWorkingOn.add(wrbAuftragszeit, "span 2");
		jpaWorkingOn.add(wcoSonderTaetigkeit);
		jpaWorkingOn.add(wcoBeleg);

		JComponent jcZeitmodell;
		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_PERS_ZEITERFASSUNG_ZEITMODELL_TAGEWEISE_AENDERN)) {
			jcZeitmodell = wbuZeitmodell;
		} else {
			jcZeitmodell = new WrapperLabel(
					LPMain.getTextRespectUISPr("lp.zeitmodell"));
		}
		jpaWorkingOn.add(jcZeitmodell);
		jpaWorkingOn.add(wtfZeitmodell, "span");

		jpaWorkingOn.add(wbuBeleg, "span 2");
		jpaWorkingOn.add(wtfBeleg, "span 3");
		jpaWorkingOn.add(wefKommentar, "grow, span 3 5");

		jpaWorkingOn.add(wbuPosition, "span 2");
		jpaWorkingOn.add(wtfPosition, "span 3");

		jpaWorkingOn.add(wbuTaetigkeit, "span 2");
		jpaWorkingOn.add(wtfTaetigkeit, "span 3");

		jpaWorkingOn.add(wlaBemerkung, "span 2, top");
		jpaWorkingOn.add(wtfBemerkung, "span 3, top");

		wlaFehlerInZeitdaten.setForeground(Color.RED);
		wlaFehlerInZeitdaten.setMinimumSize(new Dimension(200, HelperClient
				.getToolsPanelButtonDimension().height));
		wlaFehlerInZeitdaten.setPreferredSize(new Dimension(200,
				HelperClient.getToolsPanelButtonDimension().height));
		jpaButtonAction.add(wlaFehlerInZeitdaten);
		wlaOffeneZeitverteilung.setMinimumSize(new Dimension(200, HelperClient
				.getToolsPanelButtonDimension().height));
		wlaOffeneZeitverteilung.setPreferredSize(new Dimension(200,
				HelperClient.getToolsPanelButtonDimension().height));
		wlaOffeneZeitverteilung
				.setText(LPMain
						.getTextRespectUISPr("pers.zeiterfassung.zeitverteilungvorhanden"));
		wlaOffeneZeitverteilung.setForeground(Color.RED);
		jpaButtonAction.add(wlaOffeneZeitverteilung);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		leereAlleFelder(this);
		wdfDatum.setTimestamp(ts);

		// super.eventActionNew(eventObject, true, false);
		zeitdatenDto = new ZeitdatenDto();
		selectedBeleg = null;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));
		wcbRelativ.setVisible(true);

		wbuNaechsterTag.setEnabled(false);
		wbuTagZurueck.setEnabled(false);
		wbuZeitmodell.setEnabled(false);
		wtfBemerkung.setText(null);
		wdfDatum.setEnabled(false);
		Object key = wcoSonderTaetigkeit.getKeyOfSelectedItem();
		wcoSonderTaetigkeit.setMap(sondertaetigkeitenOhneVersteckt);
		wcoSonderTaetigkeit.setKeyOfSelectedItem(key);
		if (wrbAuftragszeit.isSelected()) {
			wbuBeleg.setEnabled(true);
			wcoBeleg.setEnabled(true);
			wbuPosition.setEnabled(true);
			wbuTaetigkeit.setEnabled(true);

			wcbRelativ.setEnabled(true);

			wtfBeleg.setMandatoryField(true);
			wtfPosition.setMandatoryField(true);
			wtfTaetigkeit.setMandatoryField(true);
			wcoSonderTaetigkeit.setEnabled(false);

			wcoSonderTaetigkeit.setVisible(false);
			wcoBeleg.setVisible(true);
			wcoBeleg.setMandatoryField(true);

			wcoBeleg.setFocusable(true);
			jpaWorkingOn.repaint();
			// LPMain.getInstance().getDesktop().repaint();

		}
		if (wrbSondertaetigkeit.isSelected()) {
			wbuBeleg.setActivatable(false);
			wcoBeleg.setActivatable(false);

			wbuPosition.setActivatable(false);
			wbuTaetigkeit.setActivatable(false);
			wcbRelativ.setEnabled(false);
			wcbRelativ.setSelected(false);

			wtfBeleg.setMandatoryField(false);
			wtfPosition.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);
			wcoSonderTaetigkeit.setActivatable(true);
			wcoSonderTaetigkeit.setEnabled(true);

			wcoBeleg.setVisible(false);
			wcoBeleg.setMandatoryField(false);

			wcoSonderTaetigkeit.setVisible(true);
			jpaWorkingOn.repaint();

		}

		if (internalFrameZeiterfassung.getTabbedPaneZeiterfassung()
				.getPanelQueryZeitdaten().getSelectedId() == null) {
			wcoSonderTaetigkeit.setKeyOfSelectedItem(DelegateFactory
					.getInstance().getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT)
					.getIId());
		}

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wbuNaechsterTag.setEnabled(true);
		wbuTagZurueck.setEnabled(true);
		wbuZeitmodell.setEnabled(true);
		wdfDatum.setEnabled(true);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BELEG_FROM_LISTE)) {
			if (wcoBeleg.getKeyOfSelectedItem().equals(
					LocaleFac.BELEGART_AUFTRAG)) {
				dialogQueryAuftragFromListe(e);
			} else if (wcoBeleg.getKeyOfSelectedItem().equals(
					LocaleFac.BELEGART_LOS)) {
				dialogQueryLosFromListe(e);
			} else if (wcoBeleg.getKeyOfSelectedItem().equals(
					LocaleFac.BELEGART_ANGEBOT)) {
				dialogQueryAngebotFromListe(e);
			} else if (wcoBeleg.getKeyOfSelectedItem().equals(
					LocaleFac.BELEGART_PROJEKT)) {
				dialogQueryProjektFromListe(e);
			}
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ZEITMODELL_FROM_LISTE)) {
			dialogQueryZeitmodellFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_POSITION_FROM_LISTE)) {

			if (selectedBeleg != null) {
				if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_AUFTRAG)) {

					dialogQueryAuftragpositionFromListe(e);
				} else if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_LOS)) {
					dialogQueryLospositionFromListe(e);
				} else if (wcoBeleg.getKeyOfSelectedItem().equals(
						LocaleFac.BELEGART_ANGEBOT)) {
					dialogQueryAngebotpositionFromListe(e);
				}
			} else {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("zeiterfassung.belegauswaehlen"));
			}
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITDATEN;
	}

	protected void setDefaults() throws Throwable {

		if (bRechtNurBuchen == true) {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbar();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenNurBDEBuchbarOhneVersteckt();
		} else {
			sondertaetigkeitenMitVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate().getAllSprSondertaetigkeiten();
			sondertaetigkeitenOhneVersteckt = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.getAllSprSondertaetigkeitenOhneVersteckt();
		}
		wcoSonderTaetigkeit.setMap(sondertaetigkeitenMitVersteckt);

		boolean bHatAngebotszeiterfassung = false;
		boolean bHatProjektzeiterfassung = false;

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			bHatProjektzeiterfassung = true;
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
			bHatAngebotszeiterfassung = true;
		}

		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)
				&& !LPMain.getInstance().getDesktop()
						.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)
				&& !bHatAngebotszeiterfassung && !bHatProjektzeiterfassung) {
			wrbAuftragszeit.setVisible(false);

			wbuBeleg.setVisible(false);
			wtfBeleg.setVisible(false);
			wbuPosition.setVisible(false);
			wtfPosition.setVisible(false);
			wbuTaetigkeit.setVisible(false);
			wtfTaetigkeit.setVisible(false);

		}

		wcoBeleg.setMap(DelegateFactory.getInstance()
				.getZeiterfassungDelegate().getBebuchbareBelegarten());

		wefKommentar.setText(null);
	}

	protected void components2Dto() throws Exception {
		zeitdatenDto.setPersonalIId(internalFrameZeiterfassung.getPersonalDto()
				.getIId());
		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		ts = Helper.cutTimestamp(ts);

		Calendar cDatum = Calendar.getInstance();
		cDatum.setTimeInMillis(ts.getTime());

		Calendar cZeit = Calendar.getInstance();
		cZeit.setTimeInMillis(wtfZeit.getTime().getTime());

		cDatum.set(Calendar.HOUR_OF_DAY, cZeit.get(Calendar.HOUR_OF_DAY));
		cDatum.set(Calendar.MINUTE, cZeit.get(Calendar.MINUTE));
		cDatum.set(Calendar.SECOND, cZeit.get(Calendar.SECOND));
		cDatum.set(Calendar.MILLISECOND, cZeit.get(Calendar.MILLISECOND));
		ts.setTime(cDatum.getTimeInMillis());
		zeitdatenDto.setTZeit(ts);
		zeitdatenDto.setCBemerkungZuBelegart(wtfBemerkung.getText());

		zeitdatenDto.setCWowurdegebucht("Client: " + Helper.getPCName());

		zeitdatenDto.setXKommentar(wefKommentar.getText());

		if (wrbSondertaetigkeit.isSelected()) {
			zeitdatenDto.setCBelegartnr(null);
			zeitdatenDto.setIBelegartpositionid(null);
			zeitdatenDto.setTaetigkeitIId((Integer) wcoSonderTaetigkeit
					.getKeyOfSelectedItem());
		} else {
			zeitdatenDto.setTaetigkeitIId(null);

		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		wbuTagZurueck.setEnabled(false);
		wbuNaechsterTag.setEnabled(false);
		wbuZeitmodell.setEnabled(false);

		if (wrbSondertaetigkeit.isSelected()
				&& wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
			Integer telefonIId = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON)
					.getIId();
			if (telefonIId.equals(wcoSonderTaetigkeit.getKeyOfSelectedItem())) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
				return;
			}
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (pruefeObBuchungMoeglich()) {

			if (wrbSondertaetigkeit.isSelected()
					&& wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
				Integer telefonIId = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByCNr(
								ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();
				if (telefonIId.equals(wcoSonderTaetigkeit
						.getKeyOfSelectedItem())) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
					return;
				}
			}

			DelegateFactory.getInstance().getZeiterfassungDelegate()
					.removeZeitdaten(zeitdatenDto);
			aktualisiereDaten();
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, true, true);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			//
			if (pruefeObBuchungMoeglich()) {

				//
				try {

					if (wrbSondertaetigkeit.isSelected()
							&& wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {
						Integer telefonIId = DelegateFactory
								.getInstance()
								.getZeiterfassungDelegate()
								.taetigkeitFindByCNr(
										ZeiterfassungFac.TAETIGKEIT_TELEFON)
								.getIId();
						if (telefonIId.equals(wcoSonderTaetigkeit
								.getKeyOfSelectedItem())) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr("zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden"));
							return;
						}
					}

					if (zeitdatenDto.getIId() == null) {

						if (Helper.short2boolean(wcbRelativ.getShort())) {
							try {
								zeitdatenDto.setIId(DelegateFactory
										.getInstance()
										.getZeiterfassungDelegate()
										.bucheZeitRelativ(zeitdatenDto, false));
								setKeyWhenDetailPanel(zeitdatenDto.getIId());
							} catch (ExceptionLP ex1) {
								if (ex1.getICode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
									zeitdatenDto.setIId(DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.bucheZeitRelativ(zeitdatenDto,
													true));
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.warning"),
													LPMain.getTextRespectUISPr("lp.error.gehtvorende"));
								} else if (ex1.getICode() == EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_ENDE_FEHLT) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.warning"),
													LPMain.getTextRespectUISPr("lp.error.relativletzterauftragnichtbeendet"));
									return;
								} else if (ex1.getICode() == EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_GESAMTE_ZEIT_VERBUCHT) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.warning"),
													LPMain.getTextRespectUISPr("lp.error.relativegesamtezeitverbucht"));
									return;
								} else {
									handleException(ex1, false);
									return;
								}
							}
						} else {

							boolean bGespreichert = false;
							while (bGespreichert == false) {
								try {
									zeitdatenDto.setIId(DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.createZeitdaten(zeitdatenDto));
									bGespreichert = true;
								} catch (ExceptionLP ex) {
									if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {

										Object[] options = {
												LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
												LPMain.getTextRespectUISPr("pers.zeitdaten.nachher"),
												LPMain.getTextRespectUISPr("lp.abbrechen") };

										int iOption = DialogFactory
												.showModalDialog(
														getInternalFrame(),
														LPMain.getTextRespectUISPr("pers.warning.zeitbuchungbereitsvorhanden"),
														"", options, options[1]);
										if (iOption == JOptionPane.YES_OPTION) {
											zeitdatenDto
													.setTZeit(new java.sql.Timestamp(
															zeitdatenDto
																	.getTZeit()
																	.getTime() - 10));
										} else if (iOption == JOptionPane.NO_OPTION) {
											zeitdatenDto
													.setTZeit(new java.sql.Timestamp(
															zeitdatenDto
																	.getTZeit()
																	.getTime() + 10));
										} else if (iOption == JOptionPane.CANCEL_OPTION) {
											return;
										}
									} else {
										handleException(ex, false);
										return;
									}
								}
							}
							setKeyWhenDetailPanel(zeitdatenDto.getIId());
						}
					} else {
						boolean bGespreichert = false;
						while (bGespreichert == false) {
							try {
								if (Helper.short2boolean(wcbRelativ.getShort())
										&& zeitdatenDto.getCBelegartnr() != null) {
									bGespreichert = true;
									DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.aendereZeitRelativ(zeitdatenDto,
													wtfZeit.getTime());

								} else {
									DelegateFactory.getInstance()
											.getZeiterfassungDelegate()
											.updateZeitdaten(zeitdatenDto);
									bGespreichert = true;

								}
							} catch (ExceptionLP ex) {
								if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {

									Object[] options = {
											LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
											LPMain.getTextRespectUISPr("pers.zeitdaten.nachher") };

									int iOption = DialogFactory
											.showModalDialog(
													getInternalFrame(),
													LPMain.getTextRespectUISPr("pers.warning.zeitbuchungbereitsvorhanden"),
													"", options, options[1]);
									if (iOption == JOptionPane.YES_OPTION) {
										zeitdatenDto
												.setTZeit(new java.sql.Timestamp(
														zeitdatenDto.getTZeit()
																.getTime() - 10));
									} else if (iOption == JOptionPane.NO_OPTION) {
										zeitdatenDto
												.setTZeit(new java.sql.Timestamp(
														zeitdatenDto.getTZeit()
																.getTime() + 10));
									}
								} else if (ex.getICode() == EJBExceptionLP.FEHLER_RELATIVES_AENDERN_MIT_SONDERTAETIGKEITEN_NICHT_MOEGLICH) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.error"),
													LPMain.getTextRespectUISPr("zeiterfassung.error.realtivesaendernsondertaetigkeiten"));
								} else if (ex.getICode() == EJBExceptionLP.FEHLER_RELATIVES_AENDERN_ZUWENIG_ZEIT) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.error"),
													LPMain.getTextRespectUISPr("zeiterfassung.error.realtivesaendernzuwenigzeit"));

								} else {
									// Fallback fuer nicht explizit aufgefuehrte
									// Exceptions
									handleException(ex, false);
									return;
								}
							}
						}
					}

					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(
								zeitdatenDto.getIId().toString());
					}
					zeitdatenDto = DelegateFactory.getInstance()
							.getZeiterfassungDelegate()
							.zeitdatenFindByPrimaryKey(zeitdatenDto.getIId());
					aktualisiereDaten();
					eventYouAreSelected(false);
					wbuNaechsterTag.setEnabled(true);
					wbuTagZurueck.setEnabled(true);
					wbuZeitmodell.setEnabled(true);
				} catch (ExceptionLP ex) {
					if (ex.getICode() == EJBExceptionLP.FEHLER_GEHT_VOR_ENDE) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.gehtbuchungvorende"));

					} else if (ex.getICode() == EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.sondertaetigkeitbeenden"));
					} else {
						throw ex;
					}
				}
			}
		}
	}

	private boolean pruefeObBuchungMoeglich() throws ExceptionLP, Throwable {
		boolean bRechtChefbuchhalter = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		int iTag = (Integer) parameter.getCWertAsObject();

		Calendar cAktuelleZeit = Calendar.getInstance();
		cAktuelleZeit.setTimeInMillis(DelegateFactory.getInstance()
				.getSystemDelegate().getServerTimestamp().getTime());

		Calendar cBisDahinDarfGeaendertWerden = Calendar.getInstance();
		cBisDahinDarfGeaendertWerden.setTimeInMillis(Helper.cutTimestamp(
				DelegateFactory.getInstance().getSystemDelegate()
						.getServerTimestamp()).getTime());

		// Im aktuelle Monat darf geaendert werden
		cBisDahinDarfGeaendertWerden.set(Calendar.DAY_OF_MONTH, 1);

		if (cAktuelleZeit.get(Calendar.DAY_OF_MONTH) <= iTag) {
			// Im Vormonat darf geaendert werden
			cBisDahinDarfGeaendertWerden.set(Calendar.MONTH,
					cBisDahinDarfGeaendertWerden.get(Calendar.MONTH) - 1);
		}

		if (cBisDahinDarfGeaendertWerden.getTimeInMillis() > zeitdatenDto
				.getTZeit().getTime()) {

			if (bRechtChefbuchhalter) {
				// Warnung anzeigen
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden.trotzdem"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { Helper.formatDatum(
						cBisDahinDarfGeaendertWerden.getTime(), LPMain
								.getTheClient().getLocUi()) };
				String sMsg = mf.format(pattern);

				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(), sMsg,
						LPMain.getTextRespectUISPr("lp.warning"));
				if (b == false) {
					return false;
				}

			} else {
				// Fehler anzeigen
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden"));

				try {
					mf.setLocale(LPMain.getTheClient().getLocUi());
				} catch (Throwable ex) {
				}

				Object pattern[] = { Helper.formatDatum(
						cBisDahinDarfGeaendertWerden.getTime(), LPMain
								.getTheClient().getLocUi()) };

				String sMsg = mf.format(pattern);

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"), sMsg);

				return false;
			}

		}

		return true;
	}

	private void setZeitdatenDtoBelegart(String belegart, Integer belegIId) {
		zeitdatenDto.setCBelegartnr(belegart);
		zeitdatenDto.setIBelegartid(belegIId);
		zeitdatenDto.setArtikelIId(null);
		zeitdatenDto.setIBelegartpositionid(null);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				com.lp.server.artikel.service.ArtikelDto artikelDto = null;
				if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
					PersonalverfuegbarkeitDto dto = DelegateFactory
							.getInstance().getPersonalDelegate()
							.personalverfuegbarkeitFindByPrimaryKey(key);
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(dto.getArtikelIId());
				} else {
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(key);
				}
				zeitdatenDto.setArtikelIId(artikelDto.getIId());
				wtfTaetigkeit.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					AuftragDto auftragDto = null;
					auftragDto = DelegateFactory.getInstance()
							.getAuftragDelegate().auftragFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_AUFTRAG,
							auftragDto.getIId());
					// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
					// zeitdatenDto.setIBelegartid(auftragDto.getIId());
					wtfTaetigkeit.setText(null);
					wtfPosition.setMandatoryField(true);
					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = ", "
							+ auftragDto.getCBezProjektbezeichnung();

					String kunde = DelegateFactory
							.getInstance()
							.getKundeDelegate()
							.kundeFindByPrimaryKey(
									auftragDto.getKundeIIdAuftragsadresse())
							.getPartnerDto().formatTitelAnrede();

					wtfBeleg.setText(auftragDto.getCNr() + projBez + ", "
							+ kunde);
					selectedBeleg = key;

					com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory
							.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(auftragDto.getIId());

					if (auftragpositionDtos != null
							&& auftragpositionDtos.length > 0) {
						for (int i = 0; i < auftragpositionDtos.length; i++) {
							AuftragpositionDto dto = auftragpositionDtos[i];

							if (dto.getAuftragpositionstatusCNr() != null) {
								zeitdatenDto
										.setIBelegartpositionid(auftragpositionDtos[0]
												.getIId());
								wtfPosition.setText(auftragpositionDtos[0]
										.getCBez());
								if (auftragpositionDtos[0].getArtikelIId() != null
										&& auftragpositionDtos[0].getCBez() == null) {
									wtfPosition.setText(DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													auftragpositionDtos[0]
															.getArtikelIId())
											.formatArtikelbezeichnung());
									break;
								}

							}
						}

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
					}
					if (zeitdatenDto.getArtikelIId() == null) {
						if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
							Integer artikelIId = DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
											internalFrameZeiterfassung
													.getPersonalDto().getIId());
							if (artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIId);
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							}
						} else {
							// DEFAULT-AZ-Artikel
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
											ParameterFac.KATEGORIE_ALLGEMEIN,
											LPMain.getTheClient().getMandant());

							if (parameter.getCWert() != null
									&& !parameter.getCWertAsObject().equals("")) {
								try {
									ArtikelDto artikelDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByCNr(
													parameter.getCWert());
									wtfTaetigkeit.setText(artikelDto
											.formatArtikelbezeichnung());
									zeitdatenDto.setArtikelIId(artikelDto
											.getIId());
								} catch (Throwable ex) {
									throw new ExceptionLP(
											EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
											new Exception(
													"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
								}

							}
						}
					}
					erfuellungsgradBerechnen(null, null);
				}
			} else if (e.getSource() == panelQueryFLRLos
					|| (e.getSource() instanceof PanelQuery && ((PanelQuery) eI
							.getSource()).getIdUsecase() == QueryParameters.UC_ID_LOS)) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					LosDto losDto = null;
					losDto = DelegateFactory.getInstance()
							.getFertigungDelegate().losFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_LOS,
							losDto.getIId());
					// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
					// zeitdatenDto.setIBelegartid(losDto.getIId());
					// zeitdatenDto.setArtikelIId(null) ;

					if (!hatModulStueckrueckmeldung) {
						wtfPosition.setMandatoryField(false);
					} else {
						if (bLosbuchungOhnePositionbezug == true) {
							wtfPosition.setMandatoryField(false);
						}
					}
					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = losDto.getCProjekt();
					if (projBez == null) {
						projBez = "";
					}

					wtfBeleg.setText(losDto.getCNr() + ", " + projBez);
					selectedBeleg = key;

					if (zeitdatenDto.getArtikelIId() == null) {
						if (bArbeitszeitartikelauspersonalverfuegbarkeit) {
							Integer artikelIId = DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
											internalFrameZeiterfassung
													.getPersonalDto().getIId());
							if (artikelIId != null) {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIId);
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							}
						}

						if (zeitdatenDto.getArtikelIId() == null) {
							// DEFAULT-AZ-Artikel
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
									.getInstance()
									.getParameterDelegate()
									.getParametermandant(
											ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
											ParameterFac.KATEGORIE_ALLGEMEIN,
											LPMain.getTheClient().getMandant());

							if (parameter.getCWert() != null
									&& !parameter.getCWertAsObject().equals("")) {
								try {
									ArtikelDto artikelDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByCNr(
													parameter.getCWert());
									wtfTaetigkeit.setText(artikelDto
											.formatArtikelbezeichnung());
									zeitdatenDto.setArtikelIId(artikelDto
											.getIId());
								} catch (Throwable ex) {
									// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
								}
							}
						}
					}
					erfuellungsgradBerechnen(null, null);
				}

				if (getInternalFrameZeiterfassung().dialogLoseEinesTechnikers != null) {
					getInternalFrameZeiterfassung().dialogLoseEinesTechnikers
							.dispose();
					getInternalFrameZeiterfassung().dialogLoseEinesTechnikers = null;
				}
			} else if (e.getSource() == panelQueryFLRAngebot) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					AngebotDto angebotDto = null;
					angebotDto = DelegateFactory.getInstance()
							.getAngebotDelegate().angebotFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_ANGEBOT,
							angebotDto.getIId());
					// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_ANGEBOT);
					// zeitdatenDto.setIBelegartid(angebotDto.getIId());

					wtfPosition.setMandatoryField(false);
					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = angebotDto.getCBez();
					if (projBez == null) {
						projBez = "";
					}

					wtfBeleg.setText(angebotDto.getCNr() + ", " + projBez);
					selectedBeleg = key;

					if (zeitdatenDto.getArtikelIId() == null) {
						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null
								&& !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
							}
						}
					}
				}
			}

			else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					ProjektDto projektDto = null;
					projektDto = DelegateFactory.getInstance()
							.getProjektDelegate().projektFindByPrimaryKey(key);
					setZeitdatenDtoBelegart(LocaleFac.BELEGART_PROJEKT,
							projektDto.getIId());
					// zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_PROJEKT);
					// zeitdatenDto.setIBelegartid(projektDto.getIId());

					wtfPosition.setMandatoryField(false);
					wtfPosition.setText(null);
					// zeitdatenDto.setIBelegartpositionid(null);

					String projBez = projektDto.getCTitel();
					if (projBez == null) {
						projBez = "";
					}

					wtfBeleg.setText(projektDto.getCNr() + ", " + projBez);
					selectedBeleg = key;

					if (zeitdatenDto.getArtikelIId() == null) {
						// DEFAULT-AZ-Artikel
						ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
								.getInstance()
								.getParameterDelegate()
								.getParametermandant(
										ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										LPMain.getTheClient().getMandant());

						if (parameter.getCWert() != null
								&& !parameter.getCWertAsObject().equals("")) {
							try {
								ArtikelDto artikelDto = DelegateFactory
										.getInstance().getArtikelDelegate()
										.artikelFindByCNr(parameter.getCWert());
								wtfTaetigkeit.setText(artikelDto
										.formatArtikelbezeichnung());
								zeitdatenDto.setArtikelIId(artikelDto.getIId());
							} catch (Throwable ex) {
								// DEFAULT-AZ-ARTIKEL NICHT VORHANDEN
							}
						}
					}
				}
			}

			else if (e.getSource() == panelQueryFLRAuftragposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragpositionDto auftragpositionDto = null;
				auftragpositionDto = DelegateFactory.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey(key);
				zeitdatenDto
						.setIBelegartpositionid(auftragpositionDto.getIId());
				wtfPosition.setText(auftragpositionDto.getCBez());
				if (auftragpositionDto.getArtikelIId() != null
						&& auftragpositionDto.getCBez() == null) {
					wtfPosition.setText(DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									auftragpositionDto.getArtikelIId())
							.formatArtikelbezeichnung());
				}

				erfuellungsgradBerechnen(null, auftragpositionDto);

			} else if (e.getSource() == panelQueryFLRLosposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				LossollarbeitsplanDto auftragpositionDto = null;
				auftragpositionDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey(key);
				zeitdatenDto
						.setIBelegartpositionid(auftragpositionDto.getIId());
				wtfPosition.setText(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								auftragpositionDto.getArtikelIIdTaetigkeit())
						.formatArtikelbezeichnung());

				erfuellungsgradBerechnen(auftragpositionDto, null);

			} else if (e.getSource() == panelQueryFLRAngebotposition) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				AngebotpositionDto angebotpositionDto = null;
				angebotpositionDto = DelegateFactory.getInstance()
						.getAngebotpositionDelegate()
						.angebotpositionFindByPrimaryKey(key);
				zeitdatenDto
						.setIBelegartpositionid(angebotpositionDto.getIId());
				wtfPosition.setText(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								angebotpositionDto.getArtikelIId())
						.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRZeitmodell) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ZeitmodellDto zeitmodellDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.zeitmodellFindByPrimaryKey((Integer) key);
				wtfZeitmodell.setText(zeitmodellDto.getBezeichnung());
				zeitmodellIId = zeitmodellDto.getIId();

				// Zetimodell aendern, wenn noetig
				DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.uebersteuereZeitmodellFuerEinenTag(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId(), zeitmodellIId,
								wdfDatum.getDate());

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				wtfBeleg.setText(null);
				wtfPosition.setText(null);
				zeitdatenDto.setCBelegartnr(null);
				zeitdatenDto.setIBelegartpositionid(null);
				zeitdatenDto.setArtikelIId(null);
				if (!hatModulStueckrueckmeldung) {
					wtfPosition.setMandatoryField(false);
				} else {
					if (bLosbuchungOhnePositionbezug == true) {
						wtfPosition.setMandatoryField(false);
					} else {
						wtfPosition.setMandatoryField(true);
					}
				}

			}
		}
	}

	private void erfuellungsgradBerechnen(
			LossollarbeitsplanDto lossollarbeitsplanDto,
			AuftragpositionDto auftragpositionDto) throws ExceptionLP,
			Throwable {

		if (iOptionSollzeitpruefeung != 0) {

			if (zeitdatenDto.getCBelegartnr() != null) {
				boolean bZuvieleZeitbuchungen = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.sindZuvieleZeitdatenEinesBelegesVorhanden(
								zeitdatenDto.getCBelegartnr(),
								zeitdatenDto.getIBelegartid());
				String s = "";

				if (zeitdatenDto.getCBelegartnr()
						.equals(LocaleFac.BELEGART_LOS)) {
					s = "Soll: ";
					if (lossollarbeitsplanDto != null) {
						s += Helper.formatZahl(
								lossollarbeitsplanDto.getNGesamtzeit(), 1,
								LPMain.getTheClient().getLocUi())
								+ " Std., Ist: ";
					} else {
						// Sollzeit aller Positionen
						BigDecimal bdGesamt = new BigDecimal(0);

						LossollarbeitsplanDto[] sollarbeitsplanDtos = DelegateFactory
								.getInstance()
								.getFertigungDelegate()
								.lossollarbeitsplanFindByLosIId(
										zeitdatenDto.getIBelegartid());
						for (int i = 0; i < sollarbeitsplanDtos.length; i++) {
							bdGesamt = bdGesamt.add(sollarbeitsplanDtos[i]
									.getNGesamtzeit());
						}
						s += Helper.formatZahl(bdGesamt, 1, LPMain
								.getTheClient().getLocUi())
								+ " Std., Ist: ";
					}

					if (bZuvieleZeitbuchungen == false) {
						Integer lossollarbeitsplanIId = null;
						if (lossollarbeitsplanDto != null) {
							lossollarbeitsplanIId = lossollarbeitsplanDto
									.getIId();
						}

						try {
							Double d = DelegateFactory
									.getInstance()
									.getZeiterfassungDelegate()
									.getSummeZeitenEinesBeleges(
											LocaleFac.BELEGART_LOS,
											zeitdatenDto.getIBelegartid(),
											lossollarbeitsplanIId, null, null,
											null);
							s += Helper.formatZahl(d, 1, LPMain.getTheClient()
									.getLocUi())
									+ " Std.";
						} catch (ExceptionLP e) {
							if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
								DialogFactory.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getInstance().getMsg(e));
							} else {
								handleException(e, false);
							}
						}
					} else {
						s += "? Std.";
					}
				} else if (zeitdatenDto.getCBelegartnr().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					s = "Soll: ";
					if (iOptionSollzeitpruefeung > 1) {
						if (auftragpositionDto != null
								&& iOptionSollzeitpruefeung == 2) {
							s += Helper.formatZahl(auftragpositionDto
									.getNMenge(), 1, LPMain.getTheClient()
									.getLocUi())
									+ " Std., Ist: ";
						} else {
							// Sollzeit aller Positionen
							BigDecimal bdGesamt = new BigDecimal(0);

							AuftragpositionDto[] sollarbeitsplanDtos = DelegateFactory
									.getInstance()
									.getAuftragpositionDelegate()
									.auftragpositionFindByAuftrag(
											zeitdatenDto.getIBelegartid());
							for (int i = 0; i < sollarbeitsplanDtos.length; i++) {

								if (sollarbeitsplanDtos[i].getArtikelIId() != null) {
									ArtikelDto aDto = DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													sollarbeitsplanDtos[i]
															.getArtikelIId());
									if (aDto.getArtikelartCNr().equals(
											ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
										bdGesamt = bdGesamt
												.add(sollarbeitsplanDtos[i]
														.getNMenge());
									}
								}
							}
							s += Helper.formatZahl(bdGesamt, 1, LPMain
									.getTheClient().getLocUi())
									+ " Std., Ist: ";
						}

						if (bZuvieleZeitbuchungen == false) {
							Integer auftragpositionIId = null;
							if (auftragpositionDto != null
									&& iOptionSollzeitpruefeung == 2) {
								auftragpositionIId = auftragpositionDto
										.getIId();
							}

							try {
								Double d = DelegateFactory
										.getInstance()
										.getZeiterfassungDelegate()
										.getSummeZeitenEinesBeleges(
												LocaleFac.BELEGART_AUFTRAG,
												zeitdatenDto.getIBelegartid(),
												auftragpositionIId, null, null,
												null);
								s += Helper.formatZahl(d, 1, LPMain
										.getTheClient().getLocUi())
										+ " Std.";
							} catch (ExceptionLP e) {
								if (e.getICode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
									DialogFactory.showModalDialog(LPMain
											.getTextRespectUISPr("lp.error"),
											LPMain.getInstance().getMsg(e));
								} else {
									handleException(e, false);
								}
							}
						} else {
							s += "? Std.";
						}
					}
				}
				wlaErfuellungsgrad.setText(s);
			}
		}
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	public void setzteKalenderWochewochentag() throws Throwable {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(wdfDatum.getDate());
		int kw = cal.get(java.util.Calendar.WEEK_OF_YEAR);

		// Kurze Tagebezeichnungen holen
		String[] kurzeWochentage = new DateFormatSymbols(LPMain.getTheClient()
				.getLocUi()).getWeekdays();

		wlaKalenderwochewochentag.setText(kurzeWochentage[cal
				.get(Calendar.DAY_OF_WEEK)]
				+ ", "
				+ LPMain.getTextRespectUISPr("lp.kalenderwoche_kurz")
				+ " "
				+ kw);

	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	public void aktualisiereDaten() throws Throwable {
		wlaFehlerInZeitdaten.setText(null);
		java.sql.Date dDate = wdfDatum.getDate();
		java.sql.Timestamp tTimestamp = new java.sql.Timestamp(dDate.getTime());
		if (getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung() != null) {
			FilterKriterium[] fk = ZeiterfassungFilterFactory.getInstance()
					.createFKZeitdatenZuPersonalUndDatum(
							getInternalFrameZeiterfassung().getPersonalDto()
									.getIId(), dDate);
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.getPanelQueryZeitdaten().setDefaultFilter(fk);
		}
		setzteKalenderWochewochentag();

		if (bRechtNurBuchen == true
				|| (bRechtNurBuchen == false && bDarfKommtGehtAendern == false)) {
			LPButtonAction o = (LPButtonAction) getInternalFrameZeiterfassung()
					.getTabbedPaneZeiterfassung().getPanelQueryZeitdaten()
					.getHmOfButtons().get(PanelBasis.ACTION_NEW);
			if (Helper.cutTimestamp(
					new java.sql.Timestamp(System.currentTimeMillis())).equals(
					Helper.cutTimestamp(tTimestamp))) {
				o.getButton().setVisible(true);
			} else {
				o.getButton().setVisible(false);
			}
		}

		try {
			BetriebskalenderDto dto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.betriebskalenderFindByMandantCNrDDatum(tTimestamp);
			if (dto != null) {
				wlaBetriebskalender.setText(dto.getCBez());
			} else {
				wlaBetriebskalender.setText("");
			}
		} catch (ExceptionLP ex1) {
			wlaBetriebskalender.setText("");
			// Kein Betriebskalender-Eintrag
		}

		try {
			SonderzeitenDto[] dtos = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.sonderzeitenFindByPersonalIIdDDatum(
							getInternalFrameZeiterfassung().getPersonalDto()
									.getIId(), Helper.cutTimestamp(tTimestamp));
			if (dtos != null && dtos.length > 0) {
				String sSonder = LPMain
						.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten")
						+ ":";
				sSonder += " "
						+ DelegateFactory
								.getInstance()
								.getZeiterfassungDelegate()
								.taetigkeitFindByPrimaryKey(
										dtos[0].getTaetigkeitIId())
								.getBezeichnung();
				if (Helper.short2boolean(dtos[0].getBTag()) == true) {
					sSonder += " "
							+ LPMain.getTextRespectUISPr("zeiterfassung.ganztags");
				} else if (Helper.short2boolean(dtos[0].getBHalbtag()) == true) {
					sSonder += " "
							+ LPMain.getTextRespectUISPr("zeiterfassung.halbtags");
				} else {
					sSonder += " "
							+ dtos[0].getUStunden().toString().substring(0, 5)
							+ " " + LPMain.getTextRespectUISPr("lp.stunden");

				}

				wlaSonderzeiten.setText(sSonder);
			} else {
				wlaSonderzeiten.setText("");
			}
		} catch (ExceptionLP ex1) {
			wlaBetriebskalender.setText("");
			// Kein Betriebskalender-Eintrag
		}

		PersonalzeitmodellDto dto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalzeitmodellFindZeitmodellZuDatum(
						internalFrameZeiterfassung.getPersonalDto().getIId(),
						tTimestamp);
		if (dto != null) {
			wtfZeitmodell.setText(dto.getZeitmodellDto().getBezeichnung());
			zeitmodellIId = dto.getZeitmodellIId();
		} else {
			wtfZeitmodell
					.setText(LPMain
							.getTextRespectUISPr("zeiterfassung.keinzeitmodellzugeordnet"));
		}
		berechneTageszeit();
	}

	protected void berechneTageszeit() throws Throwable {
		java.sql.Date dDate = wdfDatum.getDate();
		String sZeit = null;
		try {

			Double d = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.berechneTagesArbeitszeit(
							internalFrameZeiterfassung.getPersonalDto()
									.getIId(), dDate);
			java.math.BigDecimal bd = new java.math.BigDecimal(d.doubleValue());
			bd = Helper.rundeKaufmaennisch(bd, 2);

			sZeit = bd.doubleValue() + "";
		} catch (ExceptionLP ex) {
			// if (ex.getICode() !=
			// EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_FEHLT) {
			wlaFehlerInZeitdaten.setText(LPMain
					.getTextRespectUISPr("zeiterfassung.error.fehler"));
			// }
		}
		if (sZeit != null) {
			wlaTagesarbeitszeit
					.setText(LPMain
							.getTextRespectUISPr("zeiterfassung.zeitdaten.tagesarbeitszeit")
							+ " " + sZeit);
		} else {
			wlaTagesarbeitszeit
					.setText(LPMain
							.getTextRespectUISPr("zeiterfassung.zeitdaten.tagesarbeitszeit"));
		}
		ZeitverteilungDto[] zv = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.zeitverteilungFindByPersonalIId(
						internalFrameZeiterfassung.getPersonalDto().getIId());

		wlaOffeneZeitverteilung.setVisible(zv != null && zv.length > 0);
	}

	public void wrbSondertaetigkeit_actionPerformed(ActionEvent e) {
		if (wrbSondertaetigkeit.isSelected()) {
			wbuBeleg.setEnabled(false);
			wcoBeleg.setEnabled(false);
			wbuPosition.setEnabled(false);
			wbuTaetigkeit.setEnabled(false);

			wcbRelativ.setEnabled(false);
			wcbRelativ.setSelected(false);

			wtfBeleg.setMandatoryField(false);
			wtfPosition.setMandatoryField(false);
			wtfTaetigkeit.setMandatoryField(false);
			wcoSonderTaetigkeit.setEnabled(true);

			wcoBeleg.setVisible(false);
			wcoBeleg.setMandatoryField(false);
			wcoSonderTaetigkeit.setVisible(true);
			jpaWorkingOn.repaint();

			int iLoc = getInternalFrameZeiterfassung()
					.getTabbedPaneZeiterfassung().getPanelSplitZeitdaten()
					.getPanelSplit().getDividerLocation();

			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.getPanelSplitZeitdaten().getPanelSplit()
					.setDividerLocation(iLoc + 1);
			getInternalFrameZeiterfassung().getTabbedPaneZeiterfassung()
					.getPanelSplitZeitdaten().getPanelSplit()
					.setDividerLocation(iLoc);

			if (bDarfKommtGehtAendern == false) {

				if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

					try {

						if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
								taetigkeitIIdGeht)
								|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
										.equals(taetigkeitIIdKommt)
								|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
										.equals(taetigkeitIIdUnter)) {
							wtfZeit.setEnabled(false);
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(System.currentTimeMillis());
							c.set(Calendar.MILLISECOND, 0);
							c.set(Calendar.SECOND, 0);
							wtfZeit.setTime(new java.sql.Time(c
									.getTimeInMillis()));
						} else {
							wtfZeit.setEnabled(true);
						}
					} catch (Throwable e1) {
						handleException(e1, true);
					}

				}

			}

		}
	}

	public void wrbAuftragszeit_actionPerformed(ActionEvent e) {
		if (wrbAuftragszeit.isSelected()) {
			wbuBeleg.setEnabled(true);
			wcoBeleg.setEnabled(true);
			wbuPosition.setEnabled(true);
			wbuTaetigkeit.setEnabled(true);
			wcbRelativ.setEnabled(true);

			wtfBeleg.setMandatoryField(true);
			wtfPosition.setMandatoryField(true);
			wtfTaetigkeit.setMandatoryField(true);
			if (bRechtNurBuchen == false) {
				wtfZeit.setEnabled(true);
			}
			wcoSonderTaetigkeit.setEnabled(false);

			wcoSonderTaetigkeit.setVisible(false);
			wcoBeleg.setVisible(true);
			wcoBeleg.setMandatoryField(true);

			wcoBeleg.setFocusable(true);
			jpaWorkingOn.repaint();
			LPMain.getInstance().getDesktop().repaint();

		}

	}

	public void wcbRelativ_propertyChange(PropertyChangeEvent evt) {

	}

	public void wcbRelativ_stateChanged(ChangeEvent e) {
		// wtfZeit.requestFocus();

	}

	public void wcbRelativ_actionPerformed(ActionEvent e) {
		if (wcbRelativ.isVisible()) {
			if (wcbRelativ.isSelected()) {
				try {
					java.sql.Time zeit = DelegateFactory
							.getInstance()
							.getZeiterfassungDelegate()
							.getRelativeZeitFuerRelativesAendernAmClient(
									zeitdatenDto.getPersonalIId(),
									zeitdatenDto.getTZeit());
					wtfZeit.setTime(zeit);
				} catch (Throwable ex) {
					// was tun???

				}
			} else {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.MILLISECOND, 0);
				c.set(Calendar.SECOND, 0);
				wtfZeit.setTime(new java.sql.Time(c.getTimeInMillis()));

			}
		}
		wtfZeit.requestFocus();
	}

	public void wcoBeleg_actionPerformed(ActionEvent e) {
		wtfBeleg.setText(null);
		wtfPosition.setText(null);
		wtfTaetigkeit.setText(null);
		selectedBeleg = null;

		if (wcoBeleg.getKeyOfSelectedItem().equals(LocaleFac.BELEGART_ANGEBOT)) {
			wbuBeleg.setWhereToGo(WrapperGotoButton.GOTO_ANGEBOT_AUSWAHL);
		} else if (wcoBeleg.getKeyOfSelectedItem().equals(
				LocaleFac.BELEGART_LOS)) {
			wbuBeleg.setWhereToGo(WrapperGotoButton.GOTO_FERTIGUNG_AUSWAHL);
		} else if (wcoBeleg.getKeyOfSelectedItem().equals(
				LocaleFac.BELEGART_AUFTRAG)) {
			wbuBeleg.setWhereToGo(WrapperGotoButton.GOTO_AUFTRAG_AUSWAHL);
		} else if (wcoBeleg.getKeyOfSelectedItem().equals(
				LocaleFac.BELEGART_PROJEKT)) {
			wbuBeleg.setWhereToGo(WrapperGotoButton.GOTO_PROJEKT_AUSWAHL);
		}

	}

	public void wcoSonderTaetigkeit_actionPerformed(ActionEvent e) {

		if (bDarfKommtGehtAendern == false) {

			if (wcoSonderTaetigkeit.getKeyOfSelectedItem() != null) {

				try {

					if (wcoSonderTaetigkeit.getKeyOfSelectedItem().equals(
							taetigkeitIIdGeht)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
									.equals(taetigkeitIIdKommt)
							|| wcoSonderTaetigkeit.getKeyOfSelectedItem()
									.equals(taetigkeitIIdUnter)) {
						wtfZeit.setEnabled(false);
					} else {
						wtfZeit.setEnabled(true);
					}
				} catch (Throwable e1) {
					handleException(e1, true);
				}

			}

		}
	}

}

class PanelZeitdaten_wcoBeleg_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcoBeleg_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoBeleg_actionPerformed(e);
	}
}

class PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter implements
		ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcoSonderTaetigkeit_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoSonderTaetigkeit_actionPerformed(e);
	}
}

class PanelZeitdaten_wcbRelativ_changeAdapter implements ChangeListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_changeAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void stateChanged(ChangeEvent e) {
		adaptee.wcbRelativ_stateChanged(e);
	}
}

class PanelZeitdaten_wcbRelativ_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbRelativ_actionPerformed(e);
	}
}

class PanelZeitdaten_wcbRelativ_propertyChangeAdapter implements
		PropertyChangeListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wcbRelativ_propertyChangeAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		adaptee.wcbRelativ_propertyChange(evt);
	}
}

class PanelZeitdaten_wbuNaechsterTag_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wbuNaechsterTag_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuNaechsterTag_actionPerformed(e);
	}
}

class PanelZeitdaten_wbuTagZurueck_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wbuTagZurueck_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuTagZurueck_actionPerformed(e);
	}
}

class PanelZeitdaten_wrbAuftragszeit_actionAdapter implements ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wrbAuftragszeit_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrbAuftragszeit_actionPerformed(e);
	}
}

class PanelZeitdaten_wrbSondertaetigkeit_actionAdapter implements
		ActionListener {
	private PanelZeitdaten adaptee;

	PanelZeitdaten_wrbSondertaetigkeit_actionAdapter(PanelZeitdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrbSondertaetigkeit_actionPerformed(e);
	}
}
