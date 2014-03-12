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
package com.lp.client.eingangsrechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
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
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Kontierungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>15. 03. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.16 $
 */
public class PanelEingangsrechnungKontierung extends PanelBasis {
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

	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperNumberField wnfBetragUst = new WrapperNumberField();
	private WrapperNumberField wnfBetragOffen = new WrapperNumberField();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperLabel wlaBetragUst = new WrapperLabel();
	private WrapperLabel wlaBetragOffen = new WrapperLabel();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperLabel wlaWaehrung4 = new WrapperLabel();
	private WrapperLabel wlaWaehrung5 = new WrapperLabel();
	private WrapperButton wbuRest = new WrapperButton();
	private Border border1;
	private WrapperLabel wlaAbstand1 = new WrapperLabel();
	private WrapperLabel wlaAbstand2 = new WrapperLabel();
	private WrapperLabel wlaAbstand3 = new WrapperLabel();
	private WrapperLabel wlaRechnungsbetrag = new WrapperLabel();
	private WrapperNumberField wnfRechnungsbetrag = new WrapperNumberField();
	private WrapperLabel wlaBisherKontiert = new WrapperLabel();
	private WrapperNumberField wnfBisherKontiert = new WrapperNumberField();
	private boolean bMapSetAktiv = false;
	
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
	 * @throws ExceptionForLPClients
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
						LPMain.getInstance().getTheClient().getMandant(),
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

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);
		wtfKostenstelleNummer.setMandatoryField(true);
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.sachkonto"));
		wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.sachkonto.tooltip"));
		wbuRest.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.button.rest"));
		wbuRest.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"er.button.rest.tooltip"));
		wlaRechnungsbetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.label.rechnungsbetrag"));
		wlaBisherKontiert.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.label.bisherkontiert"));
		wnfBetrag.setMandatoryField(true);
		wnfBetragOffen.setActivatable(false);

		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bruttobetrag"));
		wlaBetragUst.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.mwst"));
		wlaBetragOffen.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.offen"));

		jpaWorkingOn.setLayout(gridBagLayout3);
		wlaAbstand1.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand1.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setMinimumSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setPreferredSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		jpaWorkingOn.setBorder(border1);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKontoBezeichnung.setActivatable(false);
		wtfKontoNummer.setActivatable(false);
		wnfBetragOffen.setActivatable(false);
		wnfBetragUst.setActivatable(false);
		wnfBetragUst.setMandatoryFieldDB(true);
		wnfRechnungsbetrag.setActivatable(false);
		wnfBisherKontiert.setActivatable(false);

		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung5.setHorizontalAlignment(SwingConstants.LEFT);
		wnfBetrag.setMinimumValue(new BigDecimal(0));

		wcoMwst.setMandatoryFieldDB(true);
		wtfKontoNummer.setMandatoryField(true);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuRest.setActionCommand(ACTION_SPECIAL_REST);
		wcoMwst.setActionCommand(ACTION_SPECIAL_UST);
		wbuKostenstelle.addActionListener(this);
		wbuKonto.addActionListener(this);
		wbuRest.addActionListener(this);
		wnfBetrag
				.addFocusListener(new PanelEingangsrechnungKontierung_wnfBetrag_focusAdapter(
						this));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoBezeichnung, new GridBagConstraints(2, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaRechnungsbetrag, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfRechnungsbetrag, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung4, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBisherKontiert, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBisherKontiert, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung5, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuRest, new GridBagConstraints(3, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragOffen, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragOffen, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung3, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoMwst, new GridBagConstraints(1, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragUst, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragUst, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		// Labels zur Kontrolle der Spaltenbreiten
		jpaWorkingOn.add(wlaAbstand1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaAbstand2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaAbstand3, new GridBagConstraints(2, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
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
		// mit offfenem betrag vorbesetzen
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
		// Kostenstelle vorbesetzen
		holeKostenstelle(lieferantDto.getIIdKostenstelle());
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
										LPMain.getInstance()
												.getTextRespectUISPr(
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
							LPMain.getInstance()
									.getTextRespectUISPr(
											"er.eingangsrechnungistbereitserledigt.wirklichaendern"));

			if (b == false) {
				return;
			}
		}

		// Das Betragsfeld erhaelt einen neuen maximalwert
		// und zwar den wert, den es schon hat plus den offenen Betrag
		wnfBetrag.setMaximumValue(wnfBetragOffen.getBigDecimal().add(
				kontierungDto.getNBetrag()));

		super.eventActionUpdate(aE, bNeedNoUpdateI);
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

				// muss den hoechstwert fuer den betrag wieder wegtun
				wnfBetrag.setMaximumValue(getTabbedPane()
						.getEingangsrechnungDto().getNBetragfw());

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							getTabbedPane().getEingangsrechnungDto().getIId()
									.toString());
				}
				// jetz den anzeigen
				eventYouAreSelected(false);
				// den Maximalwert wieder auf den offenen Betrag beschraenken
				wnfBetrag.setMaximumValue(wnfBetragOffen.getBigDecimal());
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
		kontierungDto.setNBetrag(wnfBetrag.getBigDecimal());
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
			// den maximalwert setzen, denn der ist ja 0, wenn alles zugeordnet
			// ist
			wnfBetrag.setMinimumValue(null);
			wnfBetrag.setMaximumValue(null);
			if(kontierungDto.getNBetrag().doubleValue()<0){
				wnfBetrag.setMinimumValue(kontierungDto.getNBetrag().add(wnfBetragOffen.getBigDecimal()));	
			} else {
				wnfBetrag.setMaximumValue(kontierungDto.getNBetrag().add(wnfBetragOffen.getBigDecimal()));
			}
			
			wnfBetrag.setBigDecimal(kontierungDto.getNBetrag());
			wnfBetragUst.setBigDecimal(kontierungDto.getNBetragUst());
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
		//setIGErwerbReverseCharge();
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
				wnfBetrag.setBigDecimal(wnfBetrag.getMinimumValue());
			} else {
				wnfBetrag.setBigDecimal(wnfBetrag.getMaximumValue());
			}
			
			
			wnfBetrag_focusLost();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_UST)) {
			wnfBetrag_focusLost();
			if (wcoMwst.isEnabled() && !bMapSetAktiv) {
				if (kontoDto != null && Helper.short2boolean(kontoDto.getBOhneUst())) {
					if (wcoMwst.getSelectedItem() != null) {
						MwstsatzDto mwst = getMwstsatzForSelected();
						if (mwst != null && mwst.getFMwstsatz() != 0.0) {
								DialogFactory.showModalDialog(
										LPMain.getInstance().getTextRespectUISPr("lp.hint"),
										LPMain.getInstance().getTextRespectUISPr("er.hint.keinevst"));
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
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
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
		Object key = getKeyWhenDetailPanel();
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			kontierungDto = null;
			kontoDto = null;
			kostenstelleDto = null;
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.
			initERKopfdaten();
			// dto2Components();
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
		
		
		if(bdRechnungsbetrag.doubleValue()<0){
			wnfBetrag.setMinimumValue(bdOffen);
			wnfBetrag.setMaximumValue(0);
		} else {
			wnfBetrag.setMaximumValue(bdOffen);
			wnfBetrag.setMinimumValue(0);
		}
		
		
		String sWaehrungCNr = getTabbedPane().getEingangsrechnungDto()
				.getWaehrungCNr();
		wlaWaehrung1.setText(sWaehrungCNr);
		wlaWaehrung2.setText(sWaehrungCNr);
		wlaWaehrung3.setText(sWaehrungCNr);
		wlaWaehrung4.setText(sWaehrungCNr);
		wlaWaehrung5.setText(sWaehrungCNr);
		setIGErwerbReverseCharge();
	}

	/**
	 * wnfBetrag_focusLost
	 * 
	 * @throws Throwable
	 */
	void wnfBetrag_focusLost() throws Throwable {
		updateMwst();

		if (wnfBetrag.isEditable()) {
			// im berabeiten-modus: abziehen
			wnfBetragOffen.setBigDecimal(wnfRechnungsbetrag.getBigDecimal()
					.subtract(wnfBisherKontiert.getBigDecimal()));
		} else {
			// nur schauen
			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtKontiert(
							getTabbedPane().getEingangsrechnungDto().getIId()));
		}
	}

	private void updateMwst() throws Throwable {
		if (wnfBetrag.getBigDecimal() == null) {
			wnfBetragUst.setBigDecimal(null);
		} else {
			MwstsatzDto mwst = null;
			mwst = getMwstsatzForSelected();
			MwstsatzbezDto mwstBezDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.mwstsatzbezFindByPrimaryKey(mwst.getIIMwstsatzbezId());
			if (mwstBezDto.getBHandeingabe()) {
				wnfBetragUst.setActivatable(true);
				wnfBetragUst.setEditable(true);
			} else {
				if (isIGErwerbOrReverseCharge()) {
					// IG Erwerb und Reverse Charge rechnen netto
					wnfBetragUst.setBigDecimal(Helper.getProzentWert(wnfBetrag.getBigDecimal(), 
							new BigDecimal(mwst.getFMwstsatz()), FinanzFac.NACHKOMMASTELLEN));
				} else {
					wnfBetragUst.setBigDecimal(Helper.getMehrwertsteuerBetrag(wnfBetrag
						.getBigDecimal(), mwst.getFMwstsatz().doubleValue()));
				}
				wnfBetragUst.setEditable(false);
				wnfBetragUst.setActivatable(false);
			}
		}
	}

	private MwstsatzDto getMwstsatzForSelected() throws ExceptionLP, Throwable {
		MwstsatzDto mwst;
		mwst = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mwstsatzFindByPrimaryKey(
						(Integer) wcoMwst.getKeyOfSelectedItem());
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

	private void setIGErwerbReverseCharge() throws ExceptionLP, Throwable {
		if (isIGErwerbOrReverseCharge()) {
			wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr("label.bruttobetrag.ig"));
			wlaBetragUst.setText(LPMain.getInstance().getTextRespectUISPr("label.mwst.ig"));
		} else {
			wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr("label.bruttobetrag"));
			wlaBetragUst.setText(LPMain.getInstance().getTextRespectUISPr("label.mwst"));
		}
		updateMwst();
	}

}

class PanelEingangsrechnungKontierung_wnfBetrag_focusAdapter implements
		java.awt.event.FocusListener {
	PanelEingangsrechnungKontierung adaptee;

	PanelEingangsrechnungKontierung_wnfBetrag_focusAdapter(
			PanelEingangsrechnungKontierung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBetrag_focusLost();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
