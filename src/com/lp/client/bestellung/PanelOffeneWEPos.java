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
package com.lp.client.bestellung;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperCHNRField;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelOffeneWEPos extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;

	private Integer bestellpositionIId = null;

	private BestellpositionDto besposDto = null;

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperLabel wlaWareneingang = new WrapperLabel();
	private WrapperComboBox wcoWareneingang = new WrapperComboBox();

	private WrapperLabel wlaLieferscheinbeschreibung = new WrapperLabel();
	private WrapperTextField wtfLieferscheinbeschreibung = new WrapperTextField();

	private WrapperLabel wlaLieferscheinDatum = new WrapperLabel();
	private WrapperDateField wdfLieferscheinDatum = new WrapperDateField();

	private WrapperGotoButton wbtBestellung = new WrapperGotoButton(LPMain
			.getInstance().getTextRespectUISPr("button.bestellung"),
			WrapperGotoButton.GOTO_BESTELLUNG_AUSWAHL);
	private WrapperTextField wtfBestellung = new WrapperTextField();

	private WrapperLabel wlaChargennr = new WrapperLabel();
	private WrapperCHNRField wtfChargennr = new WrapperCHNRField();

	WrapperLabel wlaArtikel = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.artikel"));
	WrapperTextField wtfArtikel = new WrapperTextField();

	WrapperLabel wlaLager = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("label.lager"));
	WrapperTextField wtfLager = new WrapperTextField();

	WrapperLabel wlaZubuchungsmenge = new WrapperLabel("Menge");

	WrapperNumberField wnfZubuchungsmenge = new WrapperNumberField();

	PanelQuery panelQuery = null;

	Dialog dialog = null;

	public PanelOffeneWEPos(InternalFrame internalFrame,
			Integer bestellpositionIId, PanelQuery panelQuery, Dialog dialog)
			throws Throwable {
		super(internalFrame, "");
		this.bestellpositionIId = bestellpositionIId;

		this.panelQuery = panelQuery;
		this.dialog = dialog;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	protected void dto2Components() throws ExceptionLP {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() instanceof PanelQuery
					&& ((PanelQuery) e.getSource()).getIdUsecase() == QueryParameters.UC_ID_OFFENEWEPOS) {
				Integer key = (Integer) ((PanelQuery) e.getSource())
						.getSelectedId();
				if (key != null) {

					wbtBestellung.setOKey(besposDto.getBestellungIId());

					besposDto = DelegateFactory.getInstance()
							.getBestellungDelegate()
							.bestellpositionFindByPrimaryKey(key);

					BestellungDto besDto = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(
									besposDto.getBestellungIId());

					wbtBestellung.setOKey(besposDto.getBestellungIId());

					wtfBestellung.setText(besDto.getCNr());

					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(besposDto.getArtikelIId());

					wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());

					wtfChargennr.setChagennummer(null);

					if (besposDto.getNOffeneMenge() != null) {
						wnfZubuchungsmenge.setBigDecimal(besposDto
								.getNOffeneMenge());
					} else {
						wnfZubuchungsmenge.setBigDecimal(besposDto.getNMenge());
					}

					WareneingangDto[] weDtos = DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.wareneingangFindByBestellungIId(
									besposDto.getBestellungIId());
					TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
					tmArten.put(new Integer(-999),
							LPMain.getTextRespectUISPr("bes.offenewepos.neu"));
					for (int i = 0; i < weDtos.length; i++) {

						tmArten.put(weDtos[i].getIId(),
								weDtos[i].getCLieferscheinnr());
					}
					wcoWareneingang.setMap(tmArten);

					if (weDtos.length > 0) {
						wcoWareneingang
								.setKeyOfSelectedItem(weDtos[0].getIId());
						wtfLieferscheinbeschreibung.setText(weDtos[0]
								.getCLieferscheinnr());
						wdfLieferscheinDatum.setTimestamp(weDtos[0]
								.getTLieferscheindatum());
					} else {
						wcoWareneingang.setKeyOfSelectedItem(-999);
						wtfLieferscheinbeschreibung.setText(null);
						wdfLieferscheinDatum.setTimestamp(Helper
								.cutTimestamp(new Timestamp(System
										.currentTimeMillis())));
					}

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
						jPanelWorkingOn.add(wlaChargennr,
								new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER,
										GridBagConstraints.HORIZONTAL,
										new Insets(2, 2, 2, 2), 0, 0));
						jPanelWorkingOn.add(wtfChargennr,
								new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER,
										GridBagConstraints.HORIZONTAL,
										new Insets(2, 2, 2, 2), 0, 0));
					} else {
						jPanelWorkingOn.remove(wtfChargennr);
						jPanelWorkingOn.remove(wlaChargennr);
					}
					jPanelWorkingOn.repaint();
					jPanelWorkingOn.revalidate();

					enableAllComponents(this, false);
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, false);
					setzeButton(PanelBasis.ACTION_UPDATE, true, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, true);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				} else {
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, false, false);
					setzeButton(PanelBasis.ACTION_UPDATE, false, false);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);
					wcoWareneingang.setMap(null);
				}

			}
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wtfArtikel.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wnfZubuchungsmenge.setMandatoryField(true);
		wnfZubuchungsmenge.setFractionDigits(3);
		wtfArtikel.setActivatable(false);
		wnfZubuchungsmenge.setMinimumValue(0);

		wbtBestellung.setActivatable(false);
		wbtBestellung.closeDialogOnGoto(dialog);

		wcoWareneingang.setMandatoryField(true);
		wcoWareneingang.addActionListener(this);

		wlaLieferscheinDatum.setText(LPMain
				.getTextRespectUISPr("bes.lieferscheindatum"));
		wlaLieferscheinbeschreibung.setText(LPMain
				.getTextRespectUISPr("bes.lieferscheinnummer"));
		wlaWareneingang.setText(LPMain
				.getTextRespectUISPr("bes.title.panel.wareneingang"));
		wlaChargennr.setText(LPMain
				.getTextRespectUISPr("lp.chargennummer_lang"));
		wtfChargennr.setMandatoryField(true);
		wtfLieferscheinbeschreibung.setMandatoryField(true);
		wdfLieferscheinDatum.setMandatoryField(true);

		wtfLager.setActivatable(false);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaArtikel, new GridBagConstraints(0, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWareneingang, new GridBagConstraints(0, 1, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoWareneingang, new GridBagConstraints(1, 1, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLieferscheinbeschreibung,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jPanelWorkingOn.add(wtfLieferscheinbeschreibung,
				new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jPanelWorkingOn.add(wlaLieferscheinDatum, new GridBagConstraints(0, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfLieferscheinDatum, new GridBagConstraints(1, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaZubuchungsmenge, new GridBagConstraints(0, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfZubuchungsmenge, new GridBagConstraints(1, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wbtBestellung, new GridBagConstraints(0, 6, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBestellung, new GridBagConstraints(1, 6, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD,
		// ACTION_PRINT,
		};

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wcoWareneingang)) {

			if (wcoWareneingang.getKeyOfSelectedItem() == null
					|| wcoWareneingang.getKeyOfSelectedItem().equals(-999)) {
				wtfLieferscheinbeschreibung.setText(null);
				wdfLieferscheinDatum.setTimestamp(new Timestamp(System
						.currentTimeMillis()));
				if (!((LPButtonAction) (getHmOfButtons().get(ACTION_REFRESH)))
						.getButton().isEnabled()) {
					wtfLieferscheinbeschreibung.setEditable(true);
					wdfLieferscheinDatum.setEnabled(true);
				}

				wtfLieferscheinbeschreibung.setActivatable(true);
				wdfLieferscheinDatum.setActivatable(true);
			} else {
				WareneingangDto weDto = DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.wareneingangFindByPrimaryKey(
								(Integer) wcoWareneingang
										.getKeyOfSelectedItem());

				wtfLieferscheinbeschreibung.setText(weDto.getCLieferscheinnr());
				wdfLieferscheinDatum
						.setTimestamp(weDto.getTLieferscheindatum());

				wtfLieferscheinbeschreibung.setActivatable(false);
				wdfLieferscheinDatum.setActivatable(false);
				wdfLieferscheinDatum.setEnabled(false);
			}

		}
	}

	protected void setDefaults() throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		super.eventActionDelete(e, false, false);
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void components2Dto() throws ExceptionLP {

	}

	private void setzeButton(String button, boolean bEnabled,
			boolean bPanelQuery) {
		Collection<?> buttons = getHmOfButtons().values();
		if (bPanelQuery == true) {
			buttons = panelQuery.getHmOfButtons().values();
		}
		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();
			if (item.getButton().getActionCommand().equals(button)) {
				item.getButton().setEnabled(bEnabled);
			}

		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		eventYouAreSelected(false);
		enableAllComponents(this, false);

		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		// setzeButton(PanelBasis.ACTION_PRINT, true, true);

	}

	protected void eventActionUpdate(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (besposDto != null && besposDto.getArtikelIId() != null) {
			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(besposDto.getArtikelIId());

			if (Helper.short2boolean(aDto.getBSeriennrtragend()) == true) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hinweis"),
						LPMain.getInstance().getTextRespectUISPr(
								"bes.uebersichtoffene.error"));
				return;
			}
		}

		super.eventActionUpdate(e, false);

		if (besposDto != null && besposDto.getArtikelIId() != null) {

			String[] hinweise = DelegateFactory
					.getInstance()
					.getArtikelkommentarDelegate()
					.getArtikelhinweise(besposDto.getArtikelIId(),
							LocaleFac.BELEGART_WARENEINGANG);

			if (hinweise != null) {
				for (int i = 0; i < hinweise.length; i++) {
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr("lp.hinweis"), Helper
							.strippHTML(hinweise[i]));
				}
			}
			ArrayList<byte[]> bilder = DelegateFactory
					.getInstance()
					.getArtikelkommentarDelegate()
					.getArtikelhinweiseBild(besposDto.getArtikelIId(),
							LocaleFac.BELEGART_WARENEINGANG);

			if (bilder != null && bilder.size() > 0) {
				DialogFactory
						.showArtikelHinweisBild(bilder, getInternalFrame());
			}

		}
		if (panelQuery.getSelectedId() != null) {
			enableAllComponents(this, true);
			setzeButton(PanelBasis.ACTION_SAVE, true, false);
			setzeButton(PanelBasis.ACTION_DISCARD, true, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, true);
			// setzeButton(PanelBasis.ACTION_PRINT, false, true);
		} else {
			enableAllComponents(this, false);
			setzeButton(PanelBasis.ACTION_SAVE, false, false);
			setzeButton(PanelBasis.ACTION_DISCARD, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			// setzeButton(PanelBasis.ACTION_PRINT, true, true);

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			if (besposDto != null) {

				Integer wareneingangIId = null;

				// Neuen anlegen
				if (wcoWareneingang.getKeyOfSelectedItem() == null
						|| wcoWareneingang.getKeyOfSelectedItem().equals(-999)) {

					WareneingangDto weDto = new WareneingangDto();
					weDto.setBestellungIId(besposDto.getBestellungIId());

					LagerDto lagerDto = DelegateFactory.getInstance()
							.getLagerDelegate().getHauptlagerDesMandanten();
					weDto.setLagerIId(lagerDto.getIId());
					weDto.setTWareneingangsdatum(Helper
							.cutTimestamp(new Timestamp(System
									.currentTimeMillis())));

					ParametermandantDto pm = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getMandantparameter(
									LPMain.getTheClient().getMandant(),
									ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR,
									weDto.getTWareneingangsdatum());

					Double gkdouble = new Double(pm.getCWert());
					weDto.setCLieferscheinnr(wtfLieferscheinbeschreibung
							.getText());
					weDto.setTLieferscheindatum(wdfLieferscheinDatum
							.getTimestamp());

					String sMandantWaehrung = LPMain.getTheClient()
							.getSMandantenwaehrung();
					BestellungDto besDto = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(
									besposDto.getBestellungIId());

					if (besDto.getWaehrungCNr().equals(sMandantWaehrung)) {
						// gleich wie Mandantenwaehrung -> Kurs = 1.
						weDto.setNWechselkurs(new BigDecimal(1));
					} else {
						// aktuellen Kurs zur Mandantenwaehrung holen.
						WechselkursDto wechselkursDto = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.getKursZuDatum(sMandantWaehrung,
										besDto.getWaehrungCNr(),
										new Date(System.currentTimeMillis()));
						weDto.setNWechselkurs(wechselkursDto.getNKurs());
					}

					weDto.setNTransportkosten(new BigDecimal(0));
					weDto.setDGemeinkostenfaktor(gkdouble);
					weDto.setFRabattsatz(0.);

					wareneingangIId = DelegateFactory.getInstance()
							.getWareneingangDelegate()
							.createWareneingang(weDto);

				} else {
					wareneingangIId = (Integer) wcoWareneingang
							.getKeyOfSelectedItem();
				}
				// Nur WEPOs anlegen

				WareneingangspositionDto wePosDto = new WareneingangspositionDto();
				wePosDto.setWareneingangIId(wareneingangIId);
				wePosDto.setBestellpositionIId(besposDto.getIId());
				wePosDto.setNGeliefertemenge(wnfZubuchungsmenge.getBigDecimal());
				wePosDto.setNGelieferterpreis(besposDto.getNNettogesamtpreis());

				if (besposDto.getPositionsartCNr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)) {

					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(besposDto.getArtikelIId());

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())
							|| Helper.short2boolean(artikelDto
									.getBSeriennrtragend())) {

						wePosDto.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
								.erstelleDtoAusEinerChargennummer(
										wtfChargennr.getChargennummer(),
										wnfZubuchungsmenge.getBigDecimal()));
					}
				}

				wePosDto.setBPreiseErfasst(false);
				DelegateFactory.getInstance().getWareneingangDelegate()
						.createWareneingangsposition(wePosDto);
			}
			panelQuery.eventYouAreSelected(false);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		} else {
			return;
		}

		enableAllComponents(this, false);
		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		setzeButton(PanelBasis.ACTION_PRINT, true, true);

		wnfZubuchungsmenge.setText("");
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = panelQuery.getSelectedId();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();

		} else {
			besposDto = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellpositionFindByPrimaryKey((Integer) key);

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(besposDto.getArtikelIId());

			wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());

		}
	}
}