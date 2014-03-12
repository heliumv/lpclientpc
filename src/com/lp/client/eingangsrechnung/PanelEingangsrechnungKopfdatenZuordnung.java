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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
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
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Kopfdaten der Eingangsrechnung</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.3 $
 */
public class PanelEingangsrechnungKopfdatenZuordnung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MEHRFACH = "mehrfach";
	private KostenstelleDto kostenstelleDto = null;
	private KontoDto kontoDto = null;
	private WrapperLabel wlaAbstand1 = new WrapperLabel();
	private WrapperLabel wlaAbstand2 = new WrapperLabel();
	private WrapperLabel wlaAbstand3 = new WrapperLabel();
	private WrapperLabel wlaAbstand4 = new WrapperLabel();

	private boolean bIstModulKostenstelleInstalliert = false;

	public final static String ACTION_SPECIAL_KONTO = "action_special_er_konto";
	public final static String ACTION_SPECIAL_KOSTENSTELLE = "action_special_er_kostenstelle";
	public final static String ACTION_SPECIAL_MEHRFACH = "action_special_er_mehrfach";

	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKonto = null;

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private Border border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbMehrfachkontierung = new WrapperCheckBox();

	private WrapperLabel wlaNochNichtKontiert = new WrapperLabel();

	public PanelEingangsrechnungKopfdatenZuordnung(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initPanel();
		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout2);
	
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
	
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto.tooltip"));
		
		wlaNochNichtKontiert.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.nochnichtvollstaendigkontiert"));
		wlaNochNichtKontiert.setForeground(Color.RED);
	
		wcbMehrfachkontierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.checkbox.mehrfach"));
		wcbMehrfachkontierung.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("er.checkbox.mehrfach.tooltip"));
	
	
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKontoBezeichnung.setActivatable(false);
		wtfKontoNummer.setActivatable(false);
		

	
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKontoNummer.setMandatoryField(true);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wcbMehrfachkontierung.setActionCommand(ACTION_SPECIAL_MEHRFACH);
		// max
		wbuKostenstelle.addActionListener(this);
		wbuKonto.addActionListener(this);
		wcbMehrfachkontierung.addActionListener(this);
		
		// sizes
		wlaAbstand1.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand1.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand4.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand4.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		

		// Actionpanel
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	iZeile++;
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMehrfachkontierung, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaNochNichtKontiert, new GridBagConstraints(2,
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
		jpaWorkingOn.add(wlaAbstand1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstand2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstand3, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstand4, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	
	
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	/**
	 * Neue ER.
	 * 
	 * @param eventObject
	 *            EventObject
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
		getTabbedPane().setEingangsrechnungDto(null);
		getTabbedPane().setLieferantDto(null);
		this.kostenstelleDto = null;
		this.kontoDto = null;
		this.leereAlleFelder(this);
		setDefaults();
		updateMehrfach();
		// noch nicht vollstaendig kontiert ausblenden
		wlaNochNichtKontiert.setVisible(false);
		this.clearStatusbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			EingangsrechnungDto erDto = getTabbedPane()
					.getEingangsrechnungDto();
			if (erDto != null) {
				getTabbedPane().setEingangsrechnungDto(
						DelegateFactory.getInstance()
								.getEingangsrechnungDelegate()
								.eingangsrechnungFindByPrimaryKey(
										erDto.getIId()));
				dto2Components();
				getTabbedPane().enablePanels();
			}
		}
	}

	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		 if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MEHRFACH)) {
			updateMehrfach();
			// falls NICHT mehrfach, dann kst des lieferanten vorbesetzen
			if (wcbMehrfachkontierung.isSelected() == false) {
				// falls er eine hat
				if (getTabbedPane().getLieferantDto().getIIdKostenstelle() != null) {
					holeKostenstelle(getTabbedPane().getLieferantDto()
							.getIIdKostenstelle());
				}
			}
		}  else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		}
	}

	/**
	 * updateMehrfach.
	 * 
	 * @throws Throwable
	 */
	private void updateMehrfach() throws Throwable {
		if (wcbMehrfachkontierung.isSelected()) {
			wbuKostenstelle.setEnabled(false);
			wtfKostenstelleNummer.setMandatoryField(false);
			wbuKonto.setEnabled(false);
			wtfKontoNummer.setMandatoryField(false);
			kostenstelleDto = null;
			kontoDto = null;
			dto2ComponentsKostenstelle();
			dto2ComponentsKonto();
		
		} else {
			// buttons nur aktiveren wenn die anderen auch aktiviert sind
			wbuKostenstelle.setEnabled(true);
			wbuKonto.setEnabled(true);
			wtfKostenstelleNummer
					.setMandatoryField(true && bIstModulKostenstelleInstalliert);
			wtfKontoNummer.setMandatoryField(true);
			
		}
	}

	/**
	 * Speichere ER.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		
			if (allMandatoryFieldsSetDlg()) {
					components2Dto();
				
					EingangsrechnungDto erDto = DelegateFactory.getInstance()
						.getEingangsrechnungDelegate().updateEingangsrechnung(
								getTabbedPane().getEingangsrechnungDto());
				this.setKeyWhenDetailPanel(erDto.getIId());
				getTabbedPane().setEingangsrechnungDto(erDto);

				super.eventActionSave(e, true);
				eventYouAreSelected(false);
				}
		
	}

	/**
	 * Stornieren einer ER.
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
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits storniert");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits erledigt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits teilweise bezahlt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)) {
			boolean answer = (DialogFactory.showMeldung("Eingangsrechnung "
					+ erDto.getCNr() + " stornieren?", LPMain.getInstance()
					.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			} else {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.storniereEingangsrechnung(
								getTabbedPane().getEingangsrechnungDto()
										.getIId());
			}
		}
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			 if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle((Integer) key);
			}else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKonto((Integer) key);
			}
		}
	}


	private void holeKostenstelle(Integer key) throws Throwable {
		if (key != null) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(key);
		} else {
			kostenstelleDto = null;
		}
		dto2ComponentsKostenstelle();
	}


	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}


	/**
	 * initPanel
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		
	
		// rechte
		bIstModulKostenstelleInstalliert = true;
		wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer
				.setMandatoryField(bIstModulKostenstelleInstalliert);
	}

	private void setDefaults() throws Throwable {
		wcbMehrfachkontierung.setSelected(false);
		
	}

	private void components2Dto() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		
		if (kostenstelleDto != null) {
			erDto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			erDto.setKostenstelleIId(null);
		}
		if (getTabbedPane().getLieferantDto() != null) {
			erDto.setLieferantIId(getTabbedPane().getLieferantDto().getIId());
		} else {
			erDto.setLieferantIId(null);
		}
		if (kontoDto != null) {
			erDto.setKontoIId(kontoDto.getIId());
		} else {
			erDto.setKontoIId(null);
		}
		String sMandantWaehrung = DelegateFactory.getInstance()
				.getMandantDelegate().mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant())
				.getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	
		getTabbedPane().setEingangsrechnungDto(erDto);
	}

	private void dto2Components() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		holeKostenstelle(erDto.getKostenstelleIId());
		holeKonto(erDto.getKontoIId());

		boolean bMehrfach = erDto.getKontoIId() == null
				|| erDto.getKostenstelleIId() == null;
		wcbMehrfachkontierung.setSelected(bMehrfach);
		updateMehrfach();
		// vollstaendig kontiert?
		if (bMehrfach) {
			BigDecimal bdNochNichtKontiert = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate().getWertNochNichtKontiert(
							erDto.getIId());
			// falls noch was offen ist
			if (bdNochNichtKontiert.compareTo(new BigDecimal(0)) != 0) {
				wlaNochNichtKontiert.setVisible(true);
			} else {
				wlaNochNichtKontiert.setVisible(false);
			}
		} else {
			wlaNochNichtKontiert.setVisible(false);
		}

	
		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(erDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(erDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(erDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(erDto.getTAendern());
		this.setStatusbarStatusCNr(erDto.getStatusCNr());
		String status = DelegateFactory.getInstance().getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						erDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelleNummer.setText(null);
			wtfKostenstelleBezeichnung.setText(null);
		}
	}

	private void holeKonto(Integer key) throws Throwable {
		if (key != null) {
			this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(key);
		} else {
			kontoDto = null;
		}
		dto2ComponentsKonto();
	}

	/**
	 * dto2Components
	 */
	private void dto2ComponentsKonto() {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getCNr());
			wtfKontoBezeichnung.setText(kontoDto.getCBez());
		} else {
			wtfKontoNummer.setText(null);
			wtfKontoBezeichnung.setText(null);
		}
	}

	private void dialogQueryKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
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
		if (kontoDto != null) {
			panelQueryFLRKonto.setSelectedId(kontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRKonto);
	}

	public Map getMapSieheKontierung() {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		tm.put(MEHRFACH, LPMain.getInstance().getTextRespectUISPr(
				"er.siehekontierung"));
		return tm;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKonto;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
		getTabbedPane().setWareneingangDto(null);
		getTabbedPane().setInseratIIds(null);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getTabbedPane().print();
		eventYouAreSelected(false);
	}

	public void setMyComponents(EingangsrechnungDto eingangsrechnungDto)
			throws Throwable {
		holeKostenstelle(eingangsrechnungDto.getKostenstelleIId());
		holeKonto(eingangsrechnungDto.getKontoIId());


		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(eingangsrechnungDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(eingangsrechnungDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(eingangsrechnungDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(eingangsrechnungDto.getTAendern());
		this.setStatusbarStatusCNr(eingangsrechnungDto.getStatusCNr());
		String status = DelegateFactory.getInstance().getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						eingangsrechnungDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}
}
