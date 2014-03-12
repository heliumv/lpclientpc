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
import java.math.BigDecimal;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;

@SuppressWarnings("static-access")
public class PanelFinanzMahnungKopfdaten extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneMahnwesen tpMahnwesen = null;
	private MahnungDto mahnungDto = null;

	private Border border1 = null;
	private WrapperLabel wlaDatum = null;
	private WrapperLabel wlaMahnstufe = null;
	private WrapperLabel wlaLetztesMahndatum = null;
	private WrapperLabel wlaLetzteMahnstufe = null;
	private WrapperDateField wdfDatum = null;
	private WrapperDateField wdfLetztesMahndatum = null;
	private WrapperNumberField wnfLetzteMahnstufe = null;
	private GridBagLayout gridBagLayout1 = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayout3 = null;
	private WrapperComboBox wcoMahnstufe = null;
	private WrapperLabel wlaZieldatum = null;
	private WrapperDateField wdfZieldatum = null;
	private WrapperLabel wlaOffen = null;
	private WrapperNumberField wnfOffen = null;
	private WrapperLabel wlaWaehrung = null;
	private WrapperLabel wlaWaehrung2 = null;
	private WrapperNumberField wnfOffenKunde = null;
	private WrapperLabel wlaKunde = null;

	private final static String ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN = "action_special_mahnung_zuruecknehmen";

	public PanelFinanzMahnungKopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object key, TabbedPaneMahnwesen tpMahnwesen)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tpMahnwesen = tpMahnwesen;
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
		wcoMahnstufe.setMap(DelegateFactory.getInstance()
				.getMahnwesenDelegate().getAllMahnstufe());
		wlaWaehrung.setText(LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung());
		wlaWaehrung2.setText(LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung());
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		wlaDatum = new WrapperLabel();
		wlaMahnstufe = new WrapperLabel();
		wlaLetztesMahndatum = new WrapperLabel();
		wlaLetzteMahnstufe = new WrapperLabel();
		wdfDatum = new WrapperDateField();
		wdfLetztesMahndatum = new WrapperDateField();
		wnfLetzteMahnstufe = new WrapperNumberField();
		gridBagLayout1 = new GridBagLayout();
		jPanelWorkingOn = new JPanel();
		gridBagLayout3 = new GridBagLayout();
		wcoMahnstufe = new WrapperComboBox();
		wlaZieldatum = new WrapperLabel();
		wdfZieldatum = new WrapperDateField();
		wlaOffen = new WrapperLabel();
		wnfOffen = new WrapperNumberField();
		wlaWaehrung = new WrapperLabel();
		wlaWaehrung2 = new WrapperLabel();
		wnfOffenKunde = new WrapperNumberField();
		wlaKunde = new WrapperLabel();

		this.setLayout(gridBagLayout1);
		this.setBorder(BorderFactory.createEtchedBorder());
		JPanel panelButtonAction = getToolsPanel();
		createAndSaveAndShowButton("/com/lp/client/res/leeren.png", LPMain
				.getTextRespectUISPr("finanz.tooltip.mahnungzuruecknehmen"),
				ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN,
				RechteFac.RECHT_FB_FINANZ_CUD);
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD,
				ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN };
		enableToolsPanelButtons(aWhichButtonIUse);
		// ... bis hier ist's immer gleich
		// wegen dialogFLR
		getInternalFrame().addItemChangedListener(this);

		jPanelWorkingOn.setBorder(border1);
		jPanelWorkingOn.setOpaque(true);
		jPanelWorkingOn.setLayout(gridBagLayout3);
		wlaDatum.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaDatum.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaLetztesMahndatum.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaLetztesMahndatum.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaWaehrung.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaWaehrung.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));

		wlaWaehrung.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaOffen.setText(LPMain.getInstance().getTextRespectUISPr(
				"rech.mahnung.bruttooffen"));
		wlaZieldatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.faelligam"));

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wlaLetztesMahndatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.letztesmahndatum"));
		wlaMahnstufe.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mahnstufe"));
		wlaLetzteMahnstufe.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.letztemahnstufe"));
		wcoMahnstufe.setMandatoryFieldDB(true);
		wdfDatum.setMandatoryFieldDB(true);
		wnfLetzteMahnstufe.setActivatable(false);
		wnfLetzteMahnstufe.setFractionDigits(0);
		wdfLetztesMahndatum.setActivatable(false);
		wnfOffen.setActivatable(false);
		wnfOffenKunde.setActivatable(false);
		wdfZieldatum.setActivatable(false);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLetztesMahndatum, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfLetztesMahndatum, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMahnstufe, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoMahnstufe, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLetzteMahnstufe, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfLetzteMahnstufe, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaZieldatum, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfZieldatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaOffen, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfOffen, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKunde, new GridBagConstraints(0, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 400, 0));
		jPanelWorkingOn.add(wnfOffenKunde, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrung2, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_MAHNLAUF;
	}

	private void dto2Components() throws Throwable {
		if (mahnungDto != null) {
			wdfDatum.setDate(mahnungDto.getTMahndatum());
			wcoMahnstufe.setKeyOfSelectedItem(mahnungDto.getMahnstufeIId());
			RechnungDto rechnungDto = DelegateFactory.getInstance()
					.getRechnungDelegate().rechnungFindByPrimaryKey(
							mahnungDto.getRechnungIId());
			wdfLetztesMahndatum.setDate(mahnungDto.getTLetztesmahndatum());
			wnfLetzteMahnstufe.setInteger(mahnungDto
					.getMahnstufeIIdLetztemahnstufe());

			wdfZieldatum.setDate(null);
			if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
					|| rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_VERBUCHT)
					|| rechnungDto.getStatusCNr().equals(
							RechnungFac.STATUS_TEILBEZAHLT)) {

				BigDecimal bdOffenFw = rechnungDto
						.getNWertfw()
						.add(rechnungDto.getNWertustfw())
						.subtract(
								DelegateFactory
										.getInstance()
										.getRechnungDelegate()
										.getBereitsBezahltWertVonRechnungFw(
												rechnungDto.getIId(), null)
										.add(
												DelegateFactory
														.getInstance()
														.getRechnungDelegate()
														.getBereitsBezahltWertVonRechnungUstFw(
																rechnungDto
																		.getIId(),
																null)));

				BigDecimal bdOffen = DelegateFactory.getInstance()
						.getLocaleDelegate().rechneUmInAndereWaehrung(
								bdOffenFw,
								rechnungDto.getWaehrungCNr(),
								LPMain.getInstance().getTheClient()
										.getSMandantenwaehrung());
				wnfOffen.setBigDecimal(bdOffen);
			} else {
				if (rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_STORNIERT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain
											.getTextRespectUISPr("rechnung.zahlung.rechnungiststorniert1"));
				} else if (rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_ANGELEGT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain
											.getTextRespectUISPr("rechnung.zahlung.rechnungistnochnichtaktiviert1"));
				} else if (rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_BEZAHLT)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain
											.getTextRespectUISPr("rechnung.zahlung.rechnungistbereitsbezahlt1"));
				}
			}
			Date dZieldatum = null;
			if (rechnungDto.getZahlungszielIId() != null) {
				dZieldatum = DelegateFactory.getInstance().getMandantDelegate()
						.berechneZielDatumFuerBelegdatum(
								new java.sql.Date(rechnungDto.getTBelegdatum()
										.getTime()),
								rechnungDto.getZahlungszielIId());
			}
			wdfZieldatum.setDate(dZieldatum);
			// Kunde und dessen summe
			KundeDto kundeDto = DelegateFactory.getInstance()
					.getKundeDelegate().kundeFindByPrimaryKey(
							rechnungDto.getKundeIId());
			wlaKunde.setText(LPMain.getInstance().getTextRespectUISPr(
					"label.kunde")
					+ ": "
					+ kundeDto.getPartnerDto().formatFixTitelName1Name2()
					+ ": "
					+ LPMain.getInstance().getTextRespectUISPr(
							"rech.mahnung.kundegesamtoffen"));
			wnfOffenKunde.setBigDecimal(DelegateFactory.getInstance()
					.getMahnwesenDelegate().getSummeEinesKundenImMahnlauf(
							mahnungDto.getMahnlaufIId(), kundeDto.getIId()));
		} else {
			wdfDatum.setDate(null);
			wdfZieldatum.setDate(null);
			wnfOffen.setBigDecimal(null);
			wnfOffenKunde.setBigDecimal(null);
			wlaKunde.setText("");
		}
	}

	private void components2Dto() {
		mahnungDto.setTMahndatum(wdfDatum.getDate());
		mahnungDto.setMahnstufeIId((Integer) wcoMahnstufe
				.getKeyOfSelectedItem());
	}

	/**
	 * Loeschen einer Zahlung
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

		Object[] o = tpMahnwesen.getPanelQueryMahnung().getSelectedIds();

		if (o != null) {

			for (int i = 0; i < o.length; i++) {
				MahnungDto mDto = DelegateFactory.getInstance()
						.getMahnwesenDelegate().mahnungFindByPrimaryKey(
								(Integer) o[i]);

				if (!isLockedDlg()) {

					if (mDto.getTGedruckt() != null) {
						boolean b=DelegateFactory.getInstance().getRechnungDelegate()
								.setzeMahnstufeZurueck(mDto.getRechnungIId());
						
						if(b==false){
							DelegateFactory.getInstance().getMahnwesenDelegate()
							.removeMahnung(mDto);
							DialogFactory.showModalDialog(LPMain
									.getTextRespectUISPr("lp.hint"), LPMain
									.getTextRespectUISPr("fb.mahnung.error.zuruecksetzen"));
						}
						
					} else {
						DelegateFactory.getInstance().getMahnwesenDelegate()
								.removeMahnung(mDto);

					}

				}

			}
		}
		this.setMahnungDto(null);
		this.leereAlleFelder(this);
		super.eventActionDelete(e, false, false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.
			leereAlleFelder(this);
			dto2Components();
			clearStatusbar();
		} else {
			this.setMahnungDto((DelegateFactory.getInstance()
					.getMahnwesenDelegate()
					.mahnungFindByPrimaryKey((Integer) key)));
			dto2Components();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (mahnungDto != null) {
				if (mahnungDto.getMahnstufeIIdLetztemahnstufe() != null
						&& mahnungDto.getMahnstufeIIdLetztemahnstufe() >= mahnungDto
								.getMahnstufeIId()) {
					DialogFactory.showModalDialog(LPMain
							.getTextRespectUISPr("lp.hint"),
							"R\u00FCcksetzen der Mahnstufe nicht erlaubt");
				} else {
					MahnungDto savedDto = DelegateFactory.getInstance()
							.getMahnwesenDelegate().updateMahnung(mahnungDto);
					setMahnungDto(savedDto);
					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(
								tpMahnwesen.getMahnlaufDto().getIId()
										.toString());
					}
					eventYouAreSelected(false);
				}
			}
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	private void setMahnungDto(MahnungDto mahnungDto) throws Throwable {
		this.mahnungDto = mahnungDto;
		Integer key = null;
		if (mahnungDto != null) {
			key = mahnungDto.getIId();
		}
		setKeyWhenDetailPanel(key);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MAHNUNG_ZURUECKNEHMEN)) {
			Object[] o = tpMahnwesen.getPanelQueryMahnung().getSelectedIds();
			if (o.length > 1) {
				for (int i = 0; i < o.length; i++) {
					DelegateFactory.getInstance().getMahnwesenDelegate()
							.mahneMahnungRueckgaengig((Integer) o[i]);
				}
			} else {
				DelegateFactory.getInstance().getMahnwesenDelegate()
						.mahneMahnungRueckgaengig(mahnungDto.getIId());
			}

			// wirkt fuer diesen Filter wie Loeschen -> QP informieren
			ItemChangedEvent it = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
			this.tpMahnwesen.lPEventItemChanged(it);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}
}
