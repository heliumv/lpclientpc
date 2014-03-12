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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

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
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Panel zum Bearbeiten der Kopfdaten einer Ergebnisgruppe</p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>20. 01.
 * 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.7 $
 */
public class PanelFinanzErgebnisgruppeKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneErgebnisgruppen tabbedPaneErgebnisgruppen = null;
	private ErgebnisgruppeDto ergebnisgruppeDtoSumme = null;
	private static final String ACTION_SPECIAL_ERGEBNISGRUPPE = "action_special_ergebnisgruppe";
	private static final String ACTION_SPECIAL_ERGEBNISGRUPPETYP = "action_special_ergebnisgruppetyp";

	private PanelQueryFLR panelQueryFLRErgebnisgruppe = null;

	private WrapperTextField wtfBezeichnung = null;
	private WrapperLabel wlaBezeichnung = null;
	private JPanel jpaWorkingOn = null;
	private WrapperButton wbuErgebnisgruppe = null;
	private WrapperTextField wtfErgebnisgruppe = null;
	private WrapperCheckBox wcbInvertiert = null;
	private WrapperCheckBox wcbSummeNegativ = null;
	private WrapperCheckBox wcbProzentbasis = null;
	private WrapperComboBox wcoArt = null;
	boolean bBilanzgruppe = false;

	public PanelFinanzErgebnisgruppeKopfdaten(InternalFrame internalFrame,
			String add2TitleI,
			TabbedPaneErgebnisgruppen tabbedPaneErgebnisgruppen)
			throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPaneErgebnisgruppen = tabbedPaneErgebnisgruppen;
		bBilanzgruppe = tabbedPaneErgebnisgruppen.bBilanzgruppe;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() {
		LinkedHashMap<Integer, String> mapArten = new LinkedHashMap<Integer, String>();

		if (bBilanzgruppe) {
			mapArten.put(new Integer(
					FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE), LPMain
					.getInstance().getTextRespectUISPr("fb.egart.bilanzgruppe"));
		} else {
			mapArten.put(
					new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE),
					LPMain.getInstance().getTextRespectUISPr(
							"fb.egart.ergebnisgruppe"));
		}

		mapArten.put(new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_LEEREZEILE),
				LPMain.getInstance().getTextRespectUISPr("fb.egart.leerzeile"));
		mapArten.put(new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_LINIE), LPMain
				.getInstance().getTextRespectUISPr("fb.egart.linie"));
		mapArten.put(
				new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_SEITENUMBRUCH),
				LPMain.getInstance().getTextRespectUISPr(
						"fb.egart.seitenumbruch"));

		if (bBilanzgruppe) {
			mapArten.put(
					new Integer(
							FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV),
					LPMain.getInstance().getTextRespectUISPr(
							"fb.egart.bilanzgruppepositiv"));
			mapArten.put(
					new Integer(
							FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV),
					LPMain.getInstance().getTextRespectUISPr(
							"fb.egart.bilanzgruppenegativ"));
		}

		wcoArt.setMap(mapArten);
	}

	private TabbedPaneErgebnisgruppen getTabbedPaneErgebnisgruppen() {
		return tabbedPaneErgebnisgruppen;
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
		this.setLayout(new GridBagLayout());

		wtfBezeichnung = new WrapperTextField();
		wlaBezeichnung = new WrapperLabel();
		jpaWorkingOn = new JPanel();
		wbuErgebnisgruppe = new WrapperButton();
		wtfErgebnisgruppe = new WrapperTextField();
		wcbInvertiert = new WrapperCheckBox();
		wcbSummeNegativ = new WrapperCheckBox();
		wcbProzentbasis = new WrapperCheckBox();
		wcoArt = new WrapperComboBox();
		wcoArt.addActionListener(this);
		wtfBezeichnung.setMandatoryFieldDB(true);
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wbuErgebnisgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"finanz.summengruppe"));
		wcbInvertiert.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.invertiert"));
		wcbSummeNegativ.setText(LPMain.getInstance().getTextRespectUISPr(
				"finanz.summenegativ"));
		wcbProzentbasis.setText(LPMain.getInstance().getTextRespectUISPr(
				"fb.prozentbasis"));
		wcoArt.setMandatoryFieldDB(true);

		wlaBezeichnung.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaBezeichnung.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));

		jpaWorkingOn.setLayout(new GridBagLayout());
		jpaWorkingOn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		wtfErgebnisgruppe.setActivatable(false);
		wbuErgebnisgruppe.setActionCommand(ACTION_SPECIAL_ERGEBNISGRUPPE);
		wbuErgebnisgruppe.addActionListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuErgebnisgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfErgebnisgruppe, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbSummeNegativ, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbInvertiert, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbProzentbasis, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_ERGEBNISGRUPPE;
	}

	/**
	 * Neu.
	 * 
	 * @param eventObject
	 *            ActionEvent
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getTabbedPaneErgebnisgruppen().setErgebnisgruppeDto(null);
		this.ergebnisgruppeDtoSumme = null;
		this.leereAlleFelder(this);
		wcoArt.setKeyOfSelectedItem(new Integer(
				FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			ErgebnisgruppeDto ergebnisgruppeDto = getTabbedPaneErgebnisgruppen()
					.getErgebnisgruppeDto();
			if (ergebnisgruppeDto != null) {
				Object key = ergebnisgruppeDto.getIId();
				getTabbedPaneErgebnisgruppen().setErgebnisgruppeDto(
						DelegateFactory.getInstance().getFinanzDelegate()
								.ergebnisgruppeFindByPrimaryKey((Integer) key));
				dto2Components();
			}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		ErgebnisgruppeDto ergebnisgruppeDto = getTabbedPaneErgebnisgruppen()
				.getErgebnisgruppeDto();
		if (ergebnisgruppeDto != null) {
			DelegateFactory.getInstance().getFinanzDelegate()
					.removeErgebnisgruppe(ergebnisgruppeDto);
			super.eventActionDelete(e, true, true);
		}
	}

	/**
	 * Speichere Ergebnisgruppe.
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
			ErgebnisgruppeDto ergebnisgruppeDto = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.updateErgebnisgruppe(
							getTabbedPaneErgebnisgruppen()
									.getErgebnisgruppeDto());
			this.setKeyWhenDetailPanel(ergebnisgruppeDto.getIId());
			getTabbedPaneErgebnisgruppen().setErgebnisgruppeDto(
					ergebnisgruppeDto);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	/**
	 * eventActionSpecial
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ERGEBNISGRUPPE)) {
			dialogQueryErgebnisgruppe(e);
		}

		wtfBezeichnung.setActivatable(true);
		if (super.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {
			wtfBezeichnung.setEditable(true);
		}

		if (e.getSource().equals(wcoArt)) {
			if (wcoArt.getKeyOfSelectedItem().equals(
					new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE))||wcoArt.getKeyOfSelectedItem().equals(
							new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_NEGATIV))||wcoArt.getKeyOfSelectedItem().equals(
									new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV))) {
				wbuErgebnisgruppe.setVisible(true);
				wtfErgebnisgruppe.setVisible(true);

			} else {

				wbuErgebnisgruppe.setVisible(false);
				wtfErgebnisgruppe.setVisible(false);
				ergebnisgruppeDtoSumme = null;
				dto2ComponentsErgebnisgruppe();
			}

			if (wcoArt.getKeyOfSelectedItem().equals(
					new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_LEEREZEILE))) {
				wtfBezeichnung.setActivatable(false);
				wtfBezeichnung.setText("leer    leer    leer");
			}
			if (wcoArt.getKeyOfSelectedItem().equals(
					new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_LINIE))) {
				wtfBezeichnung.setActivatable(false);
				wtfBezeichnung
						.setText("----------------------------------------");
			}
			if (wcoArt.getKeyOfSelectedItem().equals(
					new Integer(FinanzFac.ERGEBNISGRUPPE_TYP_SEITENUMBRUCH))) {
				wtfBezeichnung.setActivatable(false);
				wtfBezeichnung.setText("*** Seitenumbruch ***");
			}

		}

	}

	private void dialogQueryErgebnisgruppe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten

		Integer ergebnisgruppeIIdSumme = null;
		if (ergebnisgruppeDtoSumme != null) {
			ergebnisgruppeIIdSumme = ergebnisgruppeDtoSumme.getIId();
		}

		FilterKriterium[] filters = null;

		if (getTabbedPaneErgebnisgruppen().getErgebnisgruppeDto() != null) {
			filters = FinanzFilterFactory.getInstance().createFKSummengruppe(
					getTabbedPaneErgebnisgruppen().getErgebnisgruppeDto()
							.getIId(), bBilanzgruppe);
		} else {
			filters = FinanzFilterFactory.getInstance().createFKSummengruppe(
					null, bBilanzgruppe);
		}
		String title = null;
		if (bBilanzgruppe == true) {
			title = LPMain.getInstance().getTextRespectUISPr(
					"finanz.liste.bilanzgruppen");
		} else {
			title = LPMain.getInstance().getTextRespectUISPr(
					"finanz.liste.ergebnisgruppen");
		}

		panelQueryFLRErgebnisgruppe = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_ERGEBNISGRUPPE, aWhichButtonIUse,
				getInternalFrame(), title, ergebnisgruppeIIdSumme);
		new DialogQuery(panelQueryFLRErgebnisgruppe);
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionLP
	 */
	protected void eventItemchanged(EventObject eI) throws ExceptionLP {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRErgebnisgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					ergebnisgruppeDtoSumme = DelegateFactory.getInstance()
							.getFinanzDelegate()
							.ergebnisgruppeFindByPrimaryKey((Integer) key);

					if (!ergebnisgruppeDtoSumme
							.getITyp()
							.equals(new Integer(
									FinanzFac.ERGEBNISGRUPPE_TYP_ERGEBNISGRUPPE))) {
						ergebnisgruppeDtoSumme = null;

						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.info"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"fb.ergebnisgruppe.summengruppe.error"));
					}

					this.dto2ComponentsErgebnisgruppe();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRErgebnisgruppe) {
				ergebnisgruppeDtoSumme = null;
				this.dto2ComponentsErgebnisgruppe();
			}
		}
	}

	/**
	 * dto2ComponentsErgebnisgruppe
	 */
	private void dto2ComponentsErgebnisgruppe() {
		if (ergebnisgruppeDtoSumme != null) {
			wtfErgebnisgruppe.setText(ergebnisgruppeDtoSumme.getCBez());
		} else {
			wtfErgebnisgruppe.setText(null);
		}

	}

	private void dto2Components() throws Throwable {
		ErgebnisgruppeDto ergebnisgruppeDto = getTabbedPaneErgebnisgruppen()
				.getErgebnisgruppeDto();
		if (ergebnisgruppeDto.getErgebnisgruppeIIdSumme() != null) {
			ergebnisgruppeDtoSumme = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.ergebnisgruppeFindByPrimaryKey(
							ergebnisgruppeDto.getErgebnisgruppeIIdSumme());
		} else {
			ergebnisgruppeDtoSumme = null;
		}
		dto2ComponentsErgebnisgruppe();
		wtfBezeichnung.setText(ergebnisgruppeDto.getCBez());
		wcoArt.setKeyOfSelectedItem(ergebnisgruppeDto.getITyp());
		wcbInvertiert.setSelected(Helper.short2boolean(ergebnisgruppeDto
				.getBInvertiert()));
		wcbSummeNegativ.setSelected(Helper.short2boolean(ergebnisgruppeDto
				.getBSummeNegativ()));
		wcbProzentbasis.setSelected(Helper.short2boolean(ergebnisgruppeDto
				.getBProzentbasis()));
		this.setStatusbarPersonalIIdAnlegen(ergebnisgruppeDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(ergebnisgruppeDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(ergebnisgruppeDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(ergebnisgruppeDto.getTAendern());
	}

	private void components2Dto() throws Throwable {
		ErgebnisgruppeDto ergebnisgruppeDto = getTabbedPaneErgebnisgruppen()
				.getErgebnisgruppeDto();
		if (ergebnisgruppeDto == null) {
			ergebnisgruppeDto = new ErgebnisgruppeDto();
			ergebnisgruppeDto.setMandantCNr(LPMain.getInstance().getTheClient()
					.getMandant());
			ergebnisgruppeDto.setBBilanzgruppe(Helper
					.boolean2Short(bBilanzgruppe));
		}
		ergebnisgruppeDto.setCBez(wtfBezeichnung.getText());
		if (ergebnisgruppeDtoSumme != null) {
			ergebnisgruppeDto.setErgebnisgruppeIIdSumme(ergebnisgruppeDtoSumme
					.getIId());
		} else {
			ergebnisgruppeDto.setErgebnisgruppeIIdSumme(null);
		}
		ergebnisgruppeDto.setBInvertiert(Helper.boolean2Short(wcbInvertiert
				.isSelected()));
		ergebnisgruppeDto.setBSummeNegativ(Helper.boolean2Short(wcbSummeNegativ
				.isSelected()));
		ergebnisgruppeDto.setBProzentbasis(Helper.boolean2Short(wcbProzentbasis
				.isSelected()));
		ergebnisgruppeDto.setITyp((Integer) wcoArt.getKeyOfSelectedItem());
		getTabbedPaneErgebnisgruppen().setErgebnisgruppeDto(ergebnisgruppeDto);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}
}
