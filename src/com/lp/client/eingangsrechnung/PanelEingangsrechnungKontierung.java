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
package com.lp.client.eingangsrechnung;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.HvValueHolder;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>Panel zum Bearbeiten der Kontierungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>15. 03. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.16 $
 */
public class PanelEingangsrechnungKontierung extends PanelBasis implements IHvValueHolderListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private EingangsrechnungKontierungDto kontierungDto = null;
	private KontoDto kontoDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private final static String ACTION_SPECIAL_KOSTENSTELLE = "action_special_erkontierung_kostenstelle";
	private final static String ACTION_SPECIAL_KONTO = "action_special_erkontierung_konto";
	private final static String ACTION_SPECIAL_REST = "action_special_erkontierung_rest";
	private final static String ACTION_SPECIAL_UST = "action_special_erkontierung_ust";
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKonto = null;

	private boolean bIstModulKostenstelleInstalliert = false;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperComboBox wcoMwst = new WrapperComboBox();

	private WrapperRadioButton wrbBetragBrutto = new WrapperRadioButton();
	private WrapperRadioButton wrbBetragNetto = new WrapperRadioButton();
	private WrapperNumberField wnfBetragBrutto = new WrapperNumberField();
	private WrapperNumberField wnfBetragNetto = new WrapperNumberField();
	private WrapperNumberField wnfBetragUst = new WrapperNumberField();
	private WrapperNumberField wnfBetragOffen = new WrapperNumberField();
	private WrapperLabel wlaBetragUst = new WrapperLabel();
	private WrapperLabel wlaBetragOffen = new WrapperLabel();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperLabel wlaWaehrung4 = new WrapperLabel();
	private WrapperLabel wlaWaehrung5 = new WrapperLabel();
	private WrapperLabel wlaWaehrung6 = new WrapperLabel();
	private WrapperButton wbuRest = new WrapperButton();
	private WrapperLabel wlaRechnungsbetrag = new WrapperLabel();
	private WrapperNumberField wnfRechnungsbetrag = new WrapperNumberField();
	private WrapperLabel wlaBisherKontiert = new WrapperLabel();
	private WrapperNumberField wnfBisherKontiert = new WrapperNumberField();
	private boolean bMapSetAktiv = false;

	private MwstsatzDto mwst;
	private MwstsatzbezDto mwstBez;

	private HvValueHolder<BigDecimal> holderBrutto;
	private HvValueHolder<BigDecimal> holderNetto;
	private HvValueHolder<BigDecimal> holderUst;
	
	public PanelEingangsrechnungKontierung(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initPanel();
		initComponents();
	}

	/**
	 * initPanel
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		// // am anfang die Werte der ER setzen
		// wnfRechnungsbetrag.setBigDecimal(getTabbedPaneEingangsrechnung().getEingangsrechnungDto().getNBetragfw());
		// wnfBetragOffen.setBigDecimal(getInternalFrame().getEingangsrechnungDelegate().
		// getWertNochNichtKontiert(getTabbedPaneEingangsrechnung().
		// getEingangsrechnungDto().getIId()));
		//
		java.sql.Timestamp tDatum = new java.sql.Timestamp(getTabbedPane()
				.getEingangsrechnungDto().getDBelegdatum().getTime());

		bMapSetAktiv = true;
		wcoMwst.setMap(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getAllMwstsatz(
						LPMain.getTheClient().getMandant(),
						tDatum, true));
		bMapSetAktiv = false;
		// erst jetzt den Listener installieren
		wcoMwst.addActionListener(this);
		// rechte
		// kst: ausblenden in initPanel()
		bIstModulKostenstelleInstalliert = true;
		wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer
				.setMandatoryField(bIstModulKostenstelleInstalliert);
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);
		
		this.setLayout(gridBagLayout1);
		wtfKostenstelleNummer.setMandatoryField(true);
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getTextRespectUISPr(
				"button.kostenstelle.tooltip"));
		wbuKonto.setText(LPMain.getTextRespectUISPr(
				"button.sachkonto"));
		wbuKonto.setToolTipText(LPMain.getTextRespectUISPr(
				"button.sachkonto.tooltip"));
		wbuRest.setText(LPMain.getTextRespectUISPr(
				"er.button.rest"));
		wbuRest.setToolTipText(LPMain.getTextRespectUISPr(
				"er.button.rest.tooltip"));
		wlaRechnungsbetrag.setText(LPMain.getTextRespectUISPr(
				"er.label.rechnungsbetrag"));
		wlaBisherKontiert.setText(LPMain.getTextRespectUISPr(
				"er.label.bisherkontiert"));
		wnfBetragBrutto.setMandatoryField(true);
		wnfBetragOffen.setActivatable(false);
		
		wnfBetragNetto.setMandatoryField(true);

		wrbBetragBrutto.setText(LPMain.getTextRespectUISPr(
				"label.bruttobetrag"));
		wrbBetragBrutto.setSelected(true);
		wrbBetragBrutto.setHorizontalAlignment(SwingConstants.RIGHT);
		wrbBetragNetto.setText(LPMain.getTextRespectUISPr(
				"label.nettobetrag"));
		wrbBetragNetto.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaBetragUst.setText(LPMain.getTextRespectUISPr(
				"label.mwst"));
		wlaBetragOffen.setText(LPMain.getTextRespectUISPr(
				"label.offen"));

		ButtonGroup bg = new ButtonGroup();
		bg.add(wrbBetragBrutto);
		bg.add(wrbBetragNetto);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKontoBezeichnung.setActivatable(false);
		wtfKontoNummer.setActivatable(false);
		wnfBetragOffen.setActivatable(false);
		wnfBetragUst.setMandatoryField(true);
		wnfRechnungsbetrag.setActivatable(false);
		wnfBisherKontiert.setActivatable(false);

		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung6.setHorizontalAlignment(SwingConstants.LEFT);

		wcoMwst.setMandatoryFieldDB(true);
		wtfKontoNummer.setMandatoryField(true);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuRest.setActionCommand(ACTION_SPECIAL_REST);
		wcoMwst.setActionCommand(ACTION_SPECIAL_UST);
		wbuKostenstelle.addActionListener(this);
		wbuKonto.addActionListener(this);
		wbuRest.addActionListener(this);
		
		holderBrutto = new HvValueHolder<BigDecimal>(wnfBetragBrutto, null);
		holderNetto = new HvValueHolder<BigDecimal>(wnfBetragNetto, null);
		holderUst = new HvValueHolder<BigDecimal>(wnfBetragUst, null);

		holderBrutto.addListener(this);
		holderNetto.addListener(this);
		holderUst.addListener(this);
		
		JPanel jpaWorkingOn = new JPanel(new MigLayout("wrap 7", "[fill,20%|fill,15%|fill,5%|fill,20%|fill,15%|fill,5%|fill,20%]"));
		
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		jpaWorkingOn.add(wbuKostenstelle);
		jpaWorkingOn.add(wtfKostenstelleNummer);
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, "span");
		
		jpaWorkingOn.add(wbuKonto);
		jpaWorkingOn.add(wtfKontoNummer);
		jpaWorkingOn.add(wtfKontoBezeichnung, "span");
		
		jpaWorkingOn.add(wlaRechnungsbetrag);
		jpaWorkingOn.add(wnfRechnungsbetrag);
		jpaWorkingOn.add(wlaWaehrung4, "wrap");
		
		jpaWorkingOn.add(wlaBisherKontiert);
		jpaWorkingOn.add(wnfBisherKontiert);
		jpaWorkingOn.add(wlaWaehrung5, "wrap");

		jpaWorkingOn.add(wrbBetragBrutto);
		jpaWorkingOn.add(wnfBetragBrutto);
		jpaWorkingOn.add(wlaWaehrung1);
		jpaWorkingOn.add(wrbBetragNetto);
		jpaWorkingOn.add(wnfBetragNetto);
		jpaWorkingOn.add(wlaWaehrung6);
		jpaWorkingOn.add(wbuRest);
		
		jpaWorkingOn.add(wlaBetragOffen);
		jpaWorkingOn.add(wnfBetragOffen);
		jpaWorkingOn.add(wlaWaehrung3, "wrap");
		
		jpaWorkingOn.add(wcoMwst, "skip 1, span 3, wrap");
		
		jpaWorkingOn.add(wlaBetragUst);
		jpaWorkingOn.add(wnfBetragUst);
		jpaWorkingOn.add(wlaWaehrung2);
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {
			boolean answer = (DialogFactory
					.showMeldung(
							"Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
							"Frage", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			}
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.storniereEingangsrechnungRueckgaengig(erDto.getIId());
			this.eventYouAreSelected(false);
		}
		super.eventActionNew(e, true, bNeedNoNewI);
		clearStatusbar();
		kontierungDto = null;
		kontoDto = null;
		kostenstelleDto = null;
		leereAlleFelder(this);
		setDefaults();
		
		// mit offenem betrag vorbesetzen
		BigDecimal bdOffen = DelegateFactory
			.getInstance()
			.getEingangsrechnungDelegate()
			.getWertNochNichtKontiert(
					getTabbedPane().getEingangsrechnungDto().getIId());
		setMaximumBruttoCalcNetto(bdOffen);
		wbuRest.doClick();
	}

	private void setDefaults() throws Throwable {
		LieferantDto lieferantDto = getTabbedPane().getLieferantDto();
		if (lieferantDto.getMwstsatzbezIId() != null) {
			// Auf den aktuellen MWST-Satz uebersetzen.
			MwstsatzDto mwstsatzDtoAktuell = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							lieferantDto.getMwstsatzbezIId());
			wcoMwst.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
		}
	}

	/**
	 * Loeschen einer Rechnungsposition
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.kontierungDto != null) {
			if (kontierungDto.getIId() != null) {
				if (!isLockedDlg()) {
					EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
					if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {
						boolean answer = (DialogFactory
								.showMeldung(
										"Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
										"Frage", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (!answer) {
							return;
						}
						DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.storniereEingangsrechnungRueckgaengig(erDto.getIId());
						this.eventYouAreSelected(false);
					} else if (erDto.getStatusCNr().equals(
							EingangsrechnungFac.STATUS_TEILBEZAHLT)
							|| erDto.getStatusCNr().equals(
									EingangsrechnungFac.STATUS_ERLEDIGT)) {
						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr(
														"er.eingangsrechnungistbereitserledigt.wirklichaendern"));

						if (b == false) {
							return;
						}
					}

					DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.removeEingangsrechnungKontierung(kontierungDto);
					this.kontierungDto = null;
					this.setKeyWhenDetailPanel(null);
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {
			boolean answer = (DialogFactory
					.showMeldung(
							"Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
							"Frage", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			}
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.storniereEingangsrechnungRueckgaengig(erDto.getIId());
			this.eventYouAreSelected(false);
		} else if (erDto.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_TEILBEZAHLT)
				|| erDto.getStatusCNr().equals(
						EingangsrechnungFac.STATUS_ERLEDIGT)) {
			boolean b = DialogFactory
					.showModalJaNeinDialog(
							getInternalFrame(),
							LPMain.getTextRespectUISPr(
											"er.eingangsrechnungistbereitserledigt.wirklichaendern"));

			if (b == false) {
				return;
			}
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		// Das Betragsfeld erhaelt einen neuen maximalwert
		// und zwar den wert, den es schon hat plus den offenen Betrag
		wnfBisherKontiert.setBigDecimal(wnfBisherKontiert.getBigDecimal().subtract(kontierungDto.getNBetrag()));
		setMaximumBruttoCalcNetto(wnfBetragOffen.getBigDecimal().add(
				kontierungDto.getNBetrag()));
		berechneMwst();

	}
	
	/**
	 * Ein positiver max-Wert wird als Maximum gesetzt, das Minimum ist dann 0.
	 * Ein negativer max-Wert wird als Minimum interpretiert, wobei das Maximum dann 0 ist.
	 * Die Min/Max-Werte des Nettofeldes werden auch gleich berechnet und gesetzt.
	 * @param max
	 * @throws Throwable 
	 * @throws ExceptionLP 
	 */
	private void setMaximumBruttoCalcNetto(BigDecimal max) throws ExceptionLP, Throwable {
		setMaximum(wnfBetragBrutto, max);
		updateNettoMaximum();
	}
	
	private void setMaximum(WrapperNumberField field, BigDecimal max) {
		BigDecimal bruttoMax = null;
		BigDecimal bruttoMin = null;
		if(max != null) {
			if(max.signum() < 0) {
				bruttoMin = max;
				bruttoMax = BigDecimal.ZERO;
			} else {
				bruttoMin = BigDecimal.ZERO;
				bruttoMax = max;
			}
		}
		field.setMaximumValue(bruttoMax);
		field.setMinimumValue(bruttoMin);
	}
	
	/**
	 * Berechnet anhand der Min/Max-Werte des Bruttofeldes die des Nettofeldes und setzt diese auch gleich.
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void updateNettoMaximum() throws ExceptionLP, Throwable {
		BigDecimal maxBrutto = wnfBetragBrutto.getMaximumValue();
		BigDecimal minBrutto = wnfBetragBrutto.getMinimumValue();

		BigDecimal maxNetto = null;
		BigDecimal minNetto = null;
		
		MwstsatzDto mwst = getSelectedMwstsatz(); //hier schon holen, damit isHandeingabeSatz() die gewaehlte Mwst nimmt
		if(isHandeingabeSatz()) {
			maxNetto = maxBrutto;
			minNetto = minBrutto;
		} else if(maxBrutto != null){
			BigDecimal maxUst = Helper.getMehrwertsteuerBetrag(maxBrutto, mwst.getFMwstsatz().doubleValue());
			maxNetto = maxBrutto.subtract(maxUst);
			BigDecimal minUst = Helper.getMehrwertsteuerBetrag(minBrutto, mwst.getFMwstsatz().doubleValue());
			minNetto = minBrutto.subtract(minUst);
		}
		wnfBetragNetto.setMaximumValue(maxNetto);
		wnfBetragNetto.setMinimumValue(minNetto);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (kontierungDto != null) {
				EingangsrechnungKontierungDto savedDto = DelegateFactory
						.getInstance().getEingangsrechnungDelegate()
						.updateEingangsrechnungKontierung(kontierungDto);
				this.kontierungDto = savedDto;
				setKeyWhenDetailPanel(kontierungDto.getIId());

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							getTabbedPane().getEingangsrechnungDto().getIId()
									.toString());
				}
				// jetz den anzeigen
				eventYouAreSelected(false);
				dto2Components();
			}
		}
	}

	private void components2Dto() throws Throwable {
		if (kontierungDto == null) {
			kontierungDto = new EingangsrechnungKontierungDto();
			kontierungDto.setEingangsrechnungIId(getTabbedPane()
					.getEingangsrechnungDto().getIId());
		}
		if (kontoDto != null) {
			kontierungDto.setKontoIId(kontoDto.getIId());
		} else {
			kontierungDto.setKontoIId(null);
		}
		if (kostenstelleDto != null) {
			kontierungDto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			kontierungDto.setKostenstelleIId(null);
		}
		kontierungDto.setNBetrag(wnfBetragBrutto.getBigDecimal());
		kontierungDto.setNBetragUst(wnfBetragUst.getBigDecimal());
		kontierungDto.setMwstsatzIId((Integer) wcoMwst.getKeyOfSelectedItem());
	}

	private void dto2Components() throws Throwable {
		if (getKeyWhenDetailPanel() != null) {
			kontierungDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungKontierungFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());
		}

		if (kontierungDto != null) {
			holeKostenstelle(kontierungDto.getKostenstelleIId());
			holeKonto(kontierungDto.getKontoIId());
			
			wnfBetragBrutto.setBigDecimal(kontierungDto.getNBetrag());
			wnfBetragUst.setBigDecimal(kontierungDto.getNBetragUst());
			wnfBetragNetto.setBigDecimal(kontierungDto.getNBetrag().subtract(kontierungDto.getNBetragUst()));
			wcoMwst.setKeyOfSelectedItem(kontierungDto.getMwstsatzIId());
			
			this.setStatusbarPersonalIIdAnlegen(kontierungDto
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(kontierungDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(kontierungDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(kontierungDto.getTAendern());
		}
		dto2ComponentsKonto();
		dto2ComponentsKostenstelle();
		wrbBetragBrutto.setSelected(true);
		wnfBetragNetto.setBigDecimal(kontierungDto.getNBetrag().subtract(kontierungDto.getNBetragUst()));
	}

	/**
	 * dto2Components
	 */
	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelleNummer.setText(null);
			wtfKostenstelleBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsKonto() {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getCNr());
			wtfKontoBezeichnung.setText(kontoDto.getCBez());
		} else {
			wtfKontoNummer.setText(null);
			wtfKontoBezeichnung.setText(null);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_REST)) {
			BigDecimal bdRechnungsbetrag = getTabbedPane().getEingangsrechnungDto()
			.getNBetragfw();
			
			if(bdRechnungsbetrag.doubleValue()<0){
				wnfBetragBrutto.setBigDecimal(wnfBetragBrutto.getMinimumValue());
			} else {
				wnfBetragBrutto.setBigDecimal(wnfBetragBrutto.getMaximumValue());
			}
			wrbBetragBrutto.setSelected(true);
			berechneBetraege();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_UST)) {
			updateNettoMaximum();
			berechneBetraege();
			if (wcoMwst.isEnabled() && !bMapSetAktiv) {
				if (kontoDto != null && Helper.short2boolean(kontoDto.getBOhneUst())) {
					if (wcoMwst.getSelectedItem() != null) {
						MwstsatzDto mwst = getSelectedMwstsatz();
						if (mwst != null && mwst.getFMwstsatz() != 0.0) {
								DialogFactory.showModalDialog(
										LPMain.getTextRespectUISPr("lp.hint"),
										LPMain.getTextRespectUISPr("er.hint.keinevst"));
						}
					}
				}
			}
		}

	}

	private void dialogQueryKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKSachkonten();

		panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr(
						"finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
		.createFKVKonto();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1,
				fkDirekt2, fkVersteckt);
		new DialogQuery(panelQueryFLRKonto);
	}

	void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle((Integer) key);
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKonto((Integer) key);
			}
		}
	}

	/**
	 * holeKostenstelle.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeKostenstelle(Integer key) throws Throwable {
		if (key != null) {
			this.kostenstelleDto = DelegateFactory.getInstance()
					.getSystemDelegate().kostenstelleFindByPrimaryKey(key);
			dto2ComponentsKostenstelle();
		}
	}

	/**
	 * holeKonto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeKonto(Integer key) throws Throwable {
		if (key != null) {
			this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(key);
			dto2ComponentsKonto();
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setMaximumBruttoCalcNetto(null);
		Object key = getKeyWhenDetailPanel();
		if (key == null
				|| key.equals(LPMain.getLockMeForNew())) {
			kontierungDto = null;
			kontoDto = null;
			kostenstelleDto = null;
			leereAlleFelder(this);
			// Kostenstelle vorbesetzen
			LieferantDto lieferantDto = getTabbedPane().getLieferantDto();
			MandantDto mandant = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
			holeKostenstelle(lieferantDto.getIIdKostenstelle() == null ? mandant.getIIdKostenstelle() : lieferantDto.getIIdKostenstelle());
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.
			initERKopfdaten();
			// mit offenem betrag vorbesetzen
			BigDecimal bdOffen = DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.getWertNochNichtKontiert(
						getTabbedPane().getEingangsrechnungDto().getIId());
			setMaximumBruttoCalcNetto(bdOffen);
			wbuRest.doClick();
		} else {
			// einen alten Eintrag laden.
			initERKopfdaten();
			kontierungDto = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungKontierungFindByPrimaryKey((Integer) key);
			dto2Components();
			getTabbedPane().enablePanels();
		}
	}

	private void initERKopfdaten() throws Throwable {
		BigDecimal bdRechnungsbetrag = getTabbedPane().getEingangsrechnungDto()
				.getNBetragfw();
		wnfRechnungsbetrag.setBigDecimal(bdRechnungsbetrag);
		BigDecimal bdOffen = DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.getWertNochNichtKontiert(
						getTabbedPane().getEingangsrechnungDto().getIId());
		wnfBetragOffen.setBigDecimal(bdOffen);
		wnfBisherKontiert.setBigDecimal(bdRechnungsbetrag.subtract(bdOffen));
		
		String sWaehrungCNr = getTabbedPane().getEingangsrechnungDto()
				.getWaehrungCNr();
		wlaWaehrung1.setText(sWaehrungCNr);
		wlaWaehrung2.setText(sWaehrungCNr);
		wlaWaehrung3.setText(sWaehrungCNr);
		wlaWaehrung4.setText(sWaehrungCNr);
		wlaWaehrung5.setText(sWaehrungCNr);
		wlaWaehrung6.setText(sWaehrungCNr);
		updateIGErwerbReverseCharge();
	}

	/**
	 * wnfBetrag_focusLost
	 * 
	 * @throws Throwable
	 */
	private void berechneBetraege() throws Throwable {
		getSelectedMwstsatz();
		berechneMwst();
		berechneOffen();
	}

	private void berechneOffen() throws ExceptionLP, Throwable {
		if (wnfBetragBrutto.isEditable()) {
			// im berabeiten-modus: abziehen
			wnfBetragOffen.setBigDecimal(wnfRechnungsbetrag.getBigDecimal()
					.subtract(wnfBisherKontiert.getBigDecimal()).subtract(wnfBetragBrutto.getBigDecimal()));
		} else {
			// nur schauen
			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtKontiert(
							getTabbedPane().getEingangsrechnungDto().getIId()));
		}
	}
	
	private void berechneMwst() throws Throwable {
		BigDecimal brutto = wnfBetragBrutto.getBigDecimal();
		BigDecimal netto = wnfBetragNetto.getBigDecimal();
		BigDecimal ust = wnfBetragUst.getBigDecimal();

		wrbBetragBrutto.setEnabled(!isHandeingabeSatz());
		wrbBetragNetto.setEnabled(!isHandeingabeSatz());
		wnfBetragNetto.setEditable(!isHandeingabeSatz() && wnfBetragBrutto.isEditable());
		wnfBetragUst.setEditable(isHandeingabeSatz() && wnfBetragBrutto.isEditable());
		
		if(isHandeingabeSatz()) {
			if(brutto == null || ust == null)
				netto = null;
			if(brutto != null && ust != null) {
				if(ust.abs().compareTo(brutto.abs()) > 0) {
					ust = brutto;
				}
				netto = brutto.subtract(ust);
			}
		} else if(isIGErwerbOrReverseCharge()) {
			// das brutto Feld wird als netto verwendet
			// das netto Feld ist egal, da nicht visible
			if(brutto != null)
				ust = Helper.getProzentWert(brutto, 
					new BigDecimal(mwst.getFMwstsatz()), FinanzFac.NACHKOMMASTELLEN);
			
		} else if(wrbBetragBrutto.isSelected()) {
			if(brutto == null) {
				netto = null;
				ust = null;
			} else {
				ust = Helper.getMehrwertsteuerBetrag(wnfBetragBrutto
						.getBigDecimal(), mwst.getFMwstsatz().doubleValue());
				netto = brutto.subtract(ust);
			}
		} else if(wrbBetragNetto.isSelected()) {
			if(netto == null) {
				brutto = null;
				ust = null;
			} else {
				ust = Helper.getProzentWert(netto, new BigDecimal(mwst.getFMwstsatz()), FinanzFac.NACHKOMMASTELLEN);
				brutto = netto.add(ust);
			}
		}
		wnfBetragBrutto.setBigDecimal(brutto);
		wnfBetragNetto.setBigDecimal(netto);
		wnfBetragUst.setBigDecimal(ust);
		
		holderBrutto.updateValueWithoutEvent();
		holderNetto.updateValueWithoutEvent();
		holderUst.updateValueWithoutEvent();
	}
	
	private boolean isHandeingabeSatz() throws Throwable {
		return mwstBez.getBHandeingabe();
	}

	private MwstsatzDto getSelectedMwstsatz() throws ExceptionLP, Throwable {
		if (mwst == null || !mwst.getIId().equals(wcoMwst.getKeyOfSelectedItem())) {
			mwst = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByPrimaryKey(
							(Integer) wcoMwst.getKeyOfSelectedItem());
			mwstBez = DelegateFactory.getInstance().getMandantDelegate()
					.mwstsatzbezFindByPrimaryKey(mwst.getIIMwstsatzbezId());
		}
		return mwst;
	}
	
	private boolean isIGErwerbOrReverseCharge() {
		return Helper.short2boolean(getTabbedPane().getEingangsrechnungDto().getBIgErwerb()) 
			|| Helper.short2boolean(getTabbedPane().getEingangsrechnungDto().getBReversecharge());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKostenstelle;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
	}

	private void updateIGErwerbReverseCharge() throws ExceptionLP, Throwable {
		boolean igeRc = isIGErwerbOrReverseCharge();
		wrbBetragBrutto.setText(LPMain.getTextRespectUISPr("label.bruttobetrag" + (igeRc ? ".ig" : "")));
		wlaBetragUst.setText(LPMain.getTextRespectUISPr("label.mwst" + ( igeRc ? ".ig" : "")));
//		if (igeRc) {
//			wrbBetragBrutto.setText(LPMain.getTextRespectUISPr("label.bruttobetrag.ig"));
//			wlaBetragUst.setText(LPMain.getTextRespectUISPr("label.mwst.ig"));
//		} else {
//			wrbBetragBrutto.setText(LPMain.getTextRespectUISPr("label.bruttobetrag"));
//			wlaBetragUst.setText(LPMain.getTextRespectUISPr("label.mwst"));
//		}
		wrbBetragNetto.setVisible(!igeRc);
		wnfBetragNetto.setVisible(!igeRc);
		wrbBetragNetto.setMandatoryField(!igeRc) ;
		wnfBetragNetto.setMandatoryField(!igeRc) ;
	}
	
	@Override
	public void valueChanged(Component reference, Object oldValue,
			Object newValue) {
		try {
			if(reference == wnfBetragBrutto)
				wrbBetragBrutto.setSelected(true);
			if(reference == wnfBetragNetto)
				wrbBetragNetto.setSelected(true);

			berechneBetraege();
		} catch(Throwable t) {
			handleException(t, false);
		}
	}

}
