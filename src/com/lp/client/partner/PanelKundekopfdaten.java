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
package com.lp.client.partner;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
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
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um den Partnerteil des Kunden.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; xx.xx.04</p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.20 $ Date $Date: 2012/11/09 08:05:24 $
 */
public class PanelKundekopfdaten extends PanelPartnerDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final private String ACTION_SPECIAL_FLR_PREISLISTEN = "action_special_flr_preislisten";

	private PanelQueryFLR panelQueryFLRPreislisten = null;
	private WrapperLabel wlaLeer = null;
	private WrapperButton wbtPreislisteFLR = null;
	private WrapperTextField wtfPreisliste = null;
	private WrapperLabel wlaMwst = null;
	private WrapperLabel wlaAdressart = null;
	private WrapperComboBox wcoMwst = null;
	private WrapperComboBox wcoAdressart = new WrapperComboBox();
	private WrapperLabel wlaEinheitProzentMwst = null;
	private WrapperCheckBox wcbInteressent = null;
	private Map<?, ?> tmMwst = null;
	private LockMeDto lockMePartner = null;
	private WrapperKeyValueField wkvUmsatzLfdJahr = null;
	private WrapperKeyValueField wkvUmsatzVorjahr = null;
	private WrapperKeyValueField wkvGelegteRechnungenLfdJahr = null;
	private WrapperKeyValueField wkvGelegteRechnungenVorjahr = null;

	private WrapperKeyValueField wkvOffenerRechnungswert = null;
	private WrapperKeyValueField wkvOffenerLSwert = null;

	private WrapperKeyValueField wkvZahlungsmoral = null;

	private ButtonGroup buttonGroupUmsatz = new ButtonGroup();
	private WrapperRadioButton wrbKundenadresse = new WrapperRadioButton();
	private WrapperRadioButton wrbStatistikadresse = new WrapperRadioButton();

	private boolean bFibuInstalliert = false;
	
	private static final int OPTION_JA = 0;
	private static final int OPTION_UEBERSCHREIBEN = 1;
	private static final int OPTION_VERWERFEN = 2;

	public PanelKundekopfdaten(InternalFrame internalFrame, String add2TitleI,
			Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		lockMePartner = null;
		Object key = getInternalFrameKunde().getKundeDto().getIId();

		if (key == null) {

			super.eventYouAreSelected(true);

			leereAlleFelder(this);
			setDefaults();

			if (getKundeDto().getPartnerIId() != null) {
				// Neu aus Partner;

				setBNeuAusPartner(true);

				getKundeDto()
						.setPartnerDto(
								DelegateFactory
										.getInstance()
										.getPartnerDelegate()
										.partnerFindByPrimaryKey(
												(Integer) getKundeDto()
														.getPartnerIId()));

				lockMePartner = new LockMeDto(HelperClient.LOCKME_PARTNER,
						getKundeDto().getPartnerIId() + "", LPMain
								.getInstance().getCNrUser());
				try {
					// versuche zu locken
					eventActionLock(null);
				} catch (ExceptionLP elp) {
					// lock fehlgeschlagen: Code korr. fuer Meldung
					elp.setICode(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED);
					// handleex: Der Aufruf.
					handleException(elp, false);
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING, null);
				}
			}

			dto2Components();
			wcbInteressent.setSelected(true);
			clearStatusbar();
		} else {
			// Bestehender Kunde.
			getInternalFrameKunde().setKundeDto(
					DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey((Integer) key));
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameKunde().getKundeDto().getPartnerDto()
							.formatFixTitelName1Name2());
			lockMePartner = new LockMeDto(HelperClient.LOCKME_PARTNER,
					getKundeDto().getPartnerIId() + "", LPMain.getInstance()
							.getCNrUser());

			super.eventYouAreSelected(true);

			updateComponents();
			dto2Components();
			setStatusbar();
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED
				&& lockMePartner != null) {
			int iLockstate = getLockedByWerWas(lockMePartner);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat
				// gelock zB Partner
			}
			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}

	private void jbInit() throws Throwable {
		// buttons.
		resetToolsPanel();
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		bFibuInstalliert = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG);

		// Partnerfelder von der Oberklasse, es folgen die Kundenfelder.

		wlaLeer = new WrapperLabel();
		wlaLeer.setText("");

		wlaEinheitProzentMwst = new WrapperLabel();
		wlaEinheitProzentMwst.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.prozent"));
		wlaEinheitProzentMwst.setHorizontalAlignment(SwingConstants.LEFT);

		wbtPreislisteFLR = new WrapperButton();
		wbtPreislisteFLR.setText(LPMain.getInstance().getTextRespectUISPr(
				"vkpf.button.preislisten"));
		wbtPreislisteFLR.setMandatoryField(true);
		wbtPreislisteFLR.setActionCommand(ACTION_SPECIAL_FLR_PREISLISTEN);
		wbtPreislisteFLR.addActionListener(this);

		wtfPreisliste = new WrapperTextField(VkPreisfindungFac.MAX_CNR);
		wtfPreisliste.setMandatoryFieldDB(true);
		wtfPreisliste.setActivatable(false);

		wlaMwst = new WrapperLabel();
		wlaMwst.setText(LPMain.getInstance().getTextRespectUISPr("lp.mwst"));

		wlaAdressart = new WrapperLabel();
		wlaAdressart.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.adressart"));

		wcoMwst = new WrapperComboBox();
		wcoMwst.setMandatoryFieldDB(true);

		Map<String, String> m = new TreeMap<String, String>();
		m.put(PartnerFac.ADRESSART_LIEFERADRESSE, LPMain.getInstance()
				.getTextRespectUISPr("part.adressart.lieferadresse"));
		m.put(PartnerFac.ADRESSART_FILIALADRESSE, LPMain.getInstance()
				.getTextRespectUISPr("part.adressart.filialadresse"));
		wcoAdressart.setMap(m);

		wcbInteressent = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.interessent"));
		final int iBreite = Defaults.getInstance().bySizeFactor(100);
		wkvUmsatzLfdJahr = new WrapperKeyValueField(iBreite);
		wkvUmsatzLfdJahr.setKey(LPMain.getInstance().getTextRespectUISPr(
				"part.umsatz.lfd_jahr"));

		wkvUmsatzVorjahr = new WrapperKeyValueField(iBreite);

		wkvGelegteRechnungenLfdJahr = new WrapperKeyValueField(iBreite);
		wkvGelegteRechnungenLfdJahr.setKey(LPMain.getInstance()
				.getTextRespectUISPr("part.gelegterechnung.heuer"));

		wkvUmsatzVorjahr.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorjahr"));
		wkvGelegteRechnungenVorjahr = new WrapperKeyValueField(iBreite);

		wkvOffenerRechnungswert = new WrapperKeyValueField(iBreite);
		wkvOffenerRechnungswert.setKey(LPMain.getInstance()
				.getTextRespectUISPr("kund.offenre"));
		wkvOffenerLSwert = new WrapperKeyValueField(iBreite);
		wkvOffenerLSwert.setKey(LPMain.getInstance().getTextRespectUISPr(
				"kund.offenls"));

		// Zahlungsmoral
		wkvZahlungsmoral = new WrapperKeyValueField(Defaults.getInstance()
				.bySizeFactor(200));
		wkvZahlungsmoral.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.zahlungsmoral"));

		// Statistikadresse
		wrbKundenadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kunde.kundenliste.kundenadresse"));
		wrbStatistikadresse.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kunde.kundenliste.statistikadresse"));
		wrbKundenadresse.setSelected(true);

		wrbKundenadresse.addActionListener(this);
		wrbStatistikadresse.addActionListener(this);

		buttonGroupUmsatz.add(wrbKundenadresse);
		buttonGroupUmsatz.add(wrbStatistikadresse);

		// ab hier einhaengen.
		// CK: mit WH besprochen: Interessent (obwohl Kundeneigenschaft)
		// einfuegen:

		jpaWorkingOn.add(wcbInteressent, new GridBagConstraints(2, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAdressart, new GridBagConstraints(5, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoAdressart, new GridBagConstraints(6, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMwst, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitProzentMwst, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbtPreislisteFLR, new GridBagConstraints(4, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPreisliste, new GridBagConstraints(6, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wkvUmsatzLfdJahr, new GridBagConstraints(0, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvUmsatzVorjahr, new GridBagConstraints(2, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvZahlungsmoral, new GridBagConstraints(5, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 180, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wkvGelegteRechnungenLfdJahr, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvGelegteRechnungenVorjahr, new GridBagConstraints(2,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKundenadresse, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbStatistikadresse, new GridBagConstraints(6, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wkvOffenerRechnungswert, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvOffenerLSwert, new GridBagConstraints(2, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung()), new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 25, 0));

		createAndSaveAndShowButton("/com/lp/client/res/book_open2.png",
				LPMain.getTextRespectUISPr("part.partner.export.vcard"),
				ACTION_SPECIAL_VCARD_EXPORT, null);

	}

	protected void components2Dto() throws Throwable {

		super.components2Dto();

		getKundeDto().setMwstsatzbezIId(
				(Integer) wcoMwst.getKeyOfSelectedItem());

		getKundeDto().setbIstinteressent(wcbInteressent.getShort());
		// damit die Debitorenkto. nicht anschlaegt.
		getKundeDto().setUpdateModeDebitorenkonto(
				KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

		getPartnerDto().setCAdressart(
				(String) wcoAdressart.getKeyOfSelectedItem());
	}

	/**
	 * Behandle Ereignis Itemchanged.
	 * 
	 * @param eI
	 *            Ereignis.
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {

		// immer
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;
		// Ort wurde ausgewaehlt
		if (e.getSource() == panelQueryFLROrt) {
			// Abfrage ob Kunde angelegt oder editiert wird (IID=null =>
			// anlegen)
			if (getKundeDto().getIId() == null
					&& getPartnerDto().getLandplzortDto() != null
					&& getPartnerDto().getLandplzortDto().getLandDto() != null) {
				setDefaultMWSTforLand(getPartnerDto().getLandplzortDto()
						.getLandDto());
			}
		}
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// koennte mein lokaler FLR
			if (e.getSource() == panelQueryFLRPreislisten) {
				// JA ist mein lokaler FLR
				// hol jetzt den preislitenkey
				Integer keyPreisliste = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				getKundeDto().setVkpfArtikelpreislisteIIdStdpreisliste(
						keyPreisliste);

				setWtfPreisliste(keyPreisliste);
			}
		}
	}

	public void setDefaultMWSTforLand(LandDto landDto) {
		try {
			MandantDto mandantDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(
							LPMain.getInstance().getTheClient().getMandant());
			String sLKZMandant = mandantDto.getPartnerDto().getLandplzortDto()
					.getLandDto().getCLkz();
			// Land != Mandantenland => ausland
			if (!landDto.getCLkz().equals(sLKZMandant)) {

				if (landDto.getEUMitglied() == null
						|| landDto.getEUMitglied().after(
								new java.sql.Date(System.currentTimeMillis()))) {
					// Fuer Drittland soll MWST-Satz wie in Mandant definiert
					// vorbesetzt werden
					Integer mwstSatzIId = mandantDto
							.getMwstsatzbezIIdStandarddrittlandmwstsatz();
					if (mwstSatzIId != null) {
						wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
					}
				} else {
					// Fuer EU-Ausland soll MWST-Satz wie in Mandant definiert
					// vorbesetzt werden
					Integer mwstSatzIId = mandantDto
							.getMwstsatzbezIIdStandardauslandmwstsatz();
					if (mwstSatzIId != null) {
						wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
					}
				}

			} else {
				Integer mwstSatzIId = mandantDto
						.getMwstsatzbezIIdStandardinlandmwstsatz();
				if (mwstSatzIId != null) {
					wcoMwst.setKeyOfSelectedItem(mwstSatzIId);
				}
			}
		} catch (Throwable e) {
			// Sollte nicht vorkommen... und wenn doch dann gibts keinen MWST
			// vorbesetzt
		}

	}

	private void setWtfPreisliste(Integer keyPreisliste) throws ExceptionLP,
			Throwable {
		VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;
		vkpfartikelpreislisteDto = DelegateFactory.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfartikelpreislisteFindByPrimaryKey(keyPreisliste);
		wtfPreisliste.setText(vkpfartikelpreislisteDto.getCNr());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		// immer
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_PREISLISTEN)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
			QueryType[] querytypes = null;
			// FilterKriterium[] filters = PartnerFilterFactory.getInstance().
			// createFKMandantFromLPMain();
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKAktivepreislisten();

			panelQueryFLRPreislisten = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_PREISLISTENNAME, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("vkpf.preislisten.title.tab"));
			// vorbesetzen
			if (getKundeDto().getVkpfArtikelpreislisteIIdStdpreisliste() != null) {
				panelQueryFLRPreislisten.setSelectedId(getKundeDto()
						.getVkpfArtikelpreislisteIIdStdpreisliste());
			}
			new DialogQuery(panelQueryFLRPreislisten);
		} else if (e.getSource().equals(wrbKundenadresse)
				|| e.getSource().equals(wrbStatistikadresse)) {
			if (wrbKundenadresse.isSelected()) {

				wkvUmsatzLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenHeuer(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenVorjahr(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenVorjahr(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));

				wkvZahlungsmoral.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getZahlungsmoraleinesKunden(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));
				wkvOffenerRechnungswert.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.berechneSummeOffenNetto(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));
			} else {

				wkvUmsatzLfdJahr.setValue(Helper.formatZahl(DelegateFactory
						.getInstance().getRechnungDelegate()
						.getUmsatzVomKundenHeuer(getKundeDto().getIId(), true),
						2, LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenVorjahr(
										getKundeDto().getIId(), true), 2,
						LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenVorjahr(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));
				wkvZahlungsmoral.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getZahlungsmoraleinesKunden(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));
				wkvOffenerRechnungswert.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.berechneSummeOffenNetto(
										getKundeDto().getIId(), true), 2,
						LPMain.getTheClient().getLocUi()));
			}
			hintergrundAnhandKreditlimitAendern();

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			checkLockedDlg();
			if (lockMePartner != null) {
				checkLockedDlg(lockMePartner);
			}

			boolean uidGeaendert = false;
			if (wtfUID.getText() == null && getPartnerDto().getCUid() != null)
				uidGeaendert = true;
			else if (getPartnerDto().getCUid() == null
					&& wtfUID.getText() != null)
				uidGeaendert = true;
			else if (getPartnerDto().getCUid() == null
					&& wtfUID.getText() == null)
				uidGeaendert = false;
			else if (getPartnerDto().getCUid().compareTo(wtfUID.getText()) != 0)
				uidGeaendert = true;

			components2Dto();
			
			if(isBNeuAusPartner() && getPartnerDto().getIId() != null) {
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByiIdPartnercNrMandantOhneExc(getPartnerDto().getIId(), 
							LPMain.getInstance().getTheClient().getMandant());
				//SP3231 Kunde aus Partner anlegen: Kunde besteht bereits, soll er kopiert werden?
				if(kundeDto != null) {
					int dialogReturnValue = showKundeSaveOptionsDialog();
					if(dialogReturnValue == OPTION_JA) {
						getPartnerDto().setIId(null);
					} else if(dialogReturnValue == OPTION_VERWERFEN) {
						doActionDiscard();
						return;
					}
				}
			}
			
			if (getKundeDto().getIId() == null) {
				// create
				Integer key = DelegateFactory.getInstance().getKundeDelegate()
						.createKunde(getKundeDto());
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(key);
				// dem kundentto den key setzen.
				getInternalFrameKunde().getKundeDto().setIId(key);
			} else {
				// update
				DelegateFactory.getInstance().getKundeDelegate()
						.updateKunde(getKundeDto());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				// der erste eintrag wurde angelegt
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");
			}
			eventYouAreSelected(false);
			getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");
			setStatusbar();
			dto2Components();
			if (uidGeaendert)
				pruefeSteuerkategorie();
		}
	}

	/**
	 * Zeigt ein Dialogfenster &uuml;ber die Speicherm&ouml;glichkeiten des
	 * Kunden bei Neu aus Partner. Ja: Kunde soll kopiert werden.
	 * &Uuml;berschreiben: Kunde soll &uuml;berschrieben werden. Nein, 
	 * &Auml;nderungen sollen verworfen werden.
	 * 
	 * @return 0, wenn Ja; 1, wenn &Uuml;berschreiben; 2, wenn Verworfen;
	 */
	private int showKundeSaveOptionsDialog() {
		String[] options = {LPMain.getTextRespectUISPr("lp.ja"), 
				LPMain.getTextRespectUISPr("lp.ueberschreiben"), 
				LPMain.getTextRespectUISPr("lp.verwerfen_ohne_frage")};
		int iOption = DialogFactory.showModalDialog(getInternalFrame(), 
				LPMain.getInstance().getTextRespectUISPr("part.kunde.warning.kundebestehtschon"), 
				LPMain.getInstance().getTextRespectUISPr("lp.frage"), 
				options, 2);
		if(iOption != OPTION_JA && iOption != OPTION_UEBERSCHREIBEN) {
			iOption = OPTION_VERWERFEN;
		}		
		
		return iOption;
	}

	private void pruefeSteuerkategorie() throws Throwable {

		if (bFibuInstalliert) {
			if (getKundeDto().getIidDebitorenkonto() != null) {
				KontoDto kontoDto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								getKundeDto().getIidDebitorenkonto());
				SteuerkategorieDto steuerkategorieDto = DelegateFactory
						.getInstance()
						.getFinanzServiceDelegate()
						.steuerkategorieFindByPrimaryKey(
								kontoDto.getSteuerkategorieIId());

				if (getPartnerDto().getCUid() == null) {
					// Steuerkategorie muss ohne UID sein
					if (steuerkategorieDto.getCNr().compareTo(
							FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID) == 0)
						zeigeSteuerMeldung(LPMain
								.getTextRespectUISPr("kunde.meldung.steuerkategorie.ohneuid"));
				} else {
					// Steuerkategorie muss mit UID sein
					if ((steuerkategorieDto.getCNr().compareTo(
							FinanzServiceFac.STEUERKATEGORIE_AUSLAND) == 0)
							|| (steuerkategorieDto.getCNr().compareTo(
									FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU) == 0))
						zeigeSteuerMeldung(LPMain
								.getTextRespectUISPr("kunde.meldung.steuerkategorie.mituid"));
				}
			}
		}
	}

	private void zeigeSteuerMeldung(String meldung) {
		String titel = LPMain
				.getTextRespectUISPr("kunde.titel.steuerkategorie.pruefen");
		DialogFactory.showMeldung(meldung, titel, JOptionPane.DEFAULT_OPTION);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == PanelBasis.LOCK_IS_NOT_LOCKED) {
			DelegateFactory.getInstance().getKundeDelegate()
					.removeKunde(getKundeDto());

			getInternalFrameKunde().setKundeDto(new KundeDto());

			super.eventActionDelete(e, true, true);
		}
	}

	protected void hintergrundAnhandKreditlimitAendern() throws Throwable {
		wkvOffenerRechnungswert.getWlaValue().setBackground(
				new Color(240, 240, 240));
		wkvOffenerLSwert.getWlaValue().setBackground(new Color(240, 240, 240));
		if (getKundeDto().getNKreditlimit() != null) {

			BigDecimal bdLs = DelegateFactory.getInstance().getLsDelegate()
					.berechneOffenenLieferscheinwert(getKundeDto().getIId());

			BigDecimal bdRE = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.berechneSummeOffenNetto(getKundeDto().getIId(),
							!wrbKundenadresse.isSelected());

			BigDecimal offen = bdRE.add(bdLs);

			BigDecimal wert80Prozent = BigDecimal.ZERO;

			if (getKundeDto().getNKreditlimit().doubleValue() != 0) {
				wert80Prozent = getKundeDto()
						.getNKreditlimit()
						.divide(new BigDecimal(100), 4,
								BigDecimal.ROUND_HALF_EVEN)
						.multiply(new BigDecimal(80));
			}

			if (offen.doubleValue() > 0) {
				if (offen.doubleValue() >= getKundeDto().getNKreditlimit()
						.doubleValue()) {
					wkvOffenerRechnungswert.getWlaValue().setBackground(
							Color.RED);
					wkvOffenerLSwert.getWlaValue().setBackground(Color.RED);
				} else if (offen.doubleValue() >= wert80Prozent.doubleValue()) {
					wkvOffenerRechnungswert.getWlaValue().setBackground(
							Color.YELLOW);
					wkvOffenerLSwert.getWlaValue().setBackground(Color.YELLOW);
				}
			}
		}

	}

	protected void dto2Components() throws Throwable {

		super.dto2Components();

		setWtfPreisliste(getKundeDto()
				.getVkpfArtikelpreislisteIIdStdpreisliste());

		wcoMwst.setKeyOfSelectedItem(getKundeDto().getMwstsatzbezIId());

		wcbInteressent.setShort(getKundeDto().getbIstinteressent());

		wcoAdressart.setKeyOfSelectedItem(getPartnerDto().getCAdressart());

		if (getKundeDto().getIId() != null) {

			wkvOffenerLSwert.setValue(Helper.formatZahl(DelegateFactory
					.getInstance().getLsDelegate()
					.berechneOffenenLieferscheinwert(getKundeDto().getIId()),
					2, LPMain.getTheClient().getLocUi()));

			if (wrbKundenadresse.isSelected()) {

				wkvUmsatzLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenHeuer(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenVorjahr(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenVorjahr(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));
				wkvZahlungsmoral.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getZahlungsmoraleinesKunden(
										getKundeDto().getIId(), false), 0,
						LPMain.getTheClient().getLocUi()));

				wkvOffenerRechnungswert.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.berechneSummeOffenNetto(
										getKundeDto().getIId(), false), 2,
						LPMain.getTheClient().getLocUi()));

			} else {

				wkvUmsatzLfdJahr.setValue(Helper.formatZahl(DelegateFactory
						.getInstance().getRechnungDelegate()
						.getUmsatzVomKundenHeuer(getKundeDto().getIId(), true),
						2, LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getUmsatzVomKundenVorjahr(
										getKundeDto().getIId(), true), 2,
						LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenVorjahr(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));
				wkvZahlungsmoral.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.getZahlungsmoraleinesKunden(
										getKundeDto().getIId(), true), 0,
						LPMain.getTheClient().getLocUi()));

				wkvOffenerRechnungswert.setValue(Helper.formatZahl(
						DelegateFactory
								.getInstance()
								.getRechnungDelegate()
								.berechneSummeOffenNetto(
										getKundeDto().getIId(), true), 2,
						LPMain.getTheClient().getLocUi()));

			}

			hintergrundAnhandKreditlimitAendern();

		}
	}

	private void initPanel() throws Throwable {

		tmMwst = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getAllMwstsatzbez(
						LPMain.getInstance().getTheClient().getMandant());
		wcoMwst.setMap(tmMwst);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		lockMePartner = null;
		super.eventActionNew(eventObject, true, true);
	}

	public boolean handleOwnException(ExceptionLP exfc) {

		boolean bErrorBekannt = true;
		String sMsg = null;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_PARTNER_LIEFERART: {
			sMsg = LPMain.getInstance().getTextRespectUISPr(
					"part.mandant.lieferart_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_SPEDITEUR: {
			sMsg = LPMain.getInstance().getTextRespectUISPr(
					"part.mandant.spediteur_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME: {
			sMsg = LPMain.getInstance().getTextRespectUISPr(
					"part.mandant.vkpreislistenname_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_ZAHLUNGSZIEL: {
			sMsg = LPMain.getInstance().getTextRespectUISPr(
					"part.mandant.zahlungsziel_fehlt");
			break;
		}
		case EJBExceptionLP.FEHLER_PARTNER_MWST: {
			sMsg = LPMain.getInstance().getTextRespectUISPr(
					"part.mandant.mwst_fehlt");
			break;
		}
		default:
			bErrorBekannt = false;
			break;
		}

		if (bErrorBekannt) {
			JOptionPane pane = getInternalFrame().getNarrowOptionPane(
					com.lp.client.pc.Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
			pane.setMessage(sMsg);
			pane.setMessageType(JOptionPane.ERROR_MESSAGE);
			JDialog dialog = pane.createDialog(this, "");
			dialog.setVisible(true);
		}
		return bErrorBekannt;
	}

	/**
	 * Setze alle Defaultwerte.
	 * 
	 * @throws Throwable
	 */
	protected void setDefaults() throws Throwable {

		DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getIDPersonal());
		MandantDto mandantDto = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getInstance().getTheClient().getMandant());
		getKundeDto().setKostenstelleIId(mandantDto.getIIdKostenstelle());
		// getKundeDto().setKostenstelleIId(personalDto.getKostenstelleIIdStamm()
		// );
		getKundeDto().setMandantCNr(mandantDto.getCNr());

		defaultFieldIsNullDlg();

		super.setDefaults();

		String sKreditlimit = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KREDITLIMIT).getCWert();

		if (sKreditlimit != null && sKreditlimit.trim().length() > 0) {
			int iKreditlimit = Integer.parseInt(sKreditlimit);

			if (iKreditlimit >= 0) {

				getKundeDto().setNKreditlimit(new BigDecimal(iKreditlimit));
			}
		}

		// Default 'Akzeptiert Teillieferung'
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getInstance().getTheClient().getMandant());

		getKundeDto().setBAkzeptiertteillieferung(
				new Short(parameter.getCWert()));

		getKundeDto().setBDistributor(Helper.boolean2Short(false));

		getKundeDto().setBIstreempfaenger(Helper.boolean2Short(false));

		getKundeDto().setbIstinteressent(Helper.boolean2Short(false));

		getKundeDto().setBLsgewichtangeben(Helper.boolean2Short(false));

		getKundeDto().setBMindermengenzuschlag(Helper.boolean2Short(false));

		getKundeDto().setBMonatsrechnung(Helper.boolean2Short(false));

		getKundeDto().setBPreiseanlsandrucken(Helper.boolean2Short(false));

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_BELEGDRUCK_MIT_RABATT,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getInstance().getTheClient().getMandant());

		getKundeDto().setBRechnungsdruckmitrabatt(
				Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

		getKundeDto().setBSammelrechnung(Helper.boolean2Short(false));

		getKundeDto().setMwstsatzbezIId(
				mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());

		getKundeDto().setVkpfArtikelpreislisteIIdStdpreisliste(
				mandantDto.getVkpfArtikelpreislisteIId());

		setWtfPreisliste(mandantDto.getVkpfArtikelpreislisteIId());

		getKundeDto().setLieferartIId(mandantDto.getLieferartIIdKunde());

		getKundeDto().setSpediteurIId(mandantDto.getSpediteurIIdKunde());

		getKundeDto().setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());

		getKundeDto().setCWaehrung(mandantDto.getWaehrungCNr());

		getKundeDto().setPersonaliIdProvisionsempfaenger(
				LPMain.getInstance().getTheClient().getIDPersonal());
	}

	private void defaultFieldIsNullDlg() throws Throwable {
		MandantDto mandantDto = DelegateFactory.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(getKundeDto().getMandantCNr());

		if (mandantDto.getLieferartIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_LIEFERART, null);
		} else if (mandantDto.getVkpfArtikelpreislisteIId() == null) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_PARTNER_VKPREISFINDUNGPREISLISTENNAME,
					null);
		} else if (mandantDto.getSpediteurIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_SPEDITEUR, null);
		} else if (mandantDto.getZahlungszielIIdKunde() == null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_ZAHLUNGSZIEL,
					null);
		} else if (tmMwst == null || (tmMwst != null && tmMwst.size() < 1)) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_PARTNER_MWST, null);
		}

	}

	private InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

	protected KundeDto getKundeDto() {
		return getInternalFrameKunde().getKundeDto();
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameKunde().getKundeDto().getPartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameKunde().getKundeDto().setPartnerDto(partnerDto);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KUNDE;
	}

	/**
	 * Update alle Components.
	 * 
	 * @throws Throwable
	 */
	private void updateComponents() throws Throwable {

		wlaGebDatumAnsprechpartner.setVisible(false);
		wdfGebDatumAnsprechpartner.setVisible(false);
		wdfGebDatumAnsprechpartner.setDate(null);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

		// Kunden locken.
		super.eventActionLock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner locken.
			super.lock(lockMePartner);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

		// Kunde unlocken.
		super.eventActionUnlock(e);

		if (lockMePartner != null) {
			// Zugehoerigen Partner unlocken.
			super.unlock(lockMePartner);
		}
	}
}
