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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelTelefonzeiten extends PanelBasis {

	private static final long serialVersionUID = -7074583086617801145L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private TelefonzeitenDto telefonzeitenDto = null;

	private final static String ACTION_SPECIAL_PROJEKT = "action_special_projekt";
	private WrapperTextField wtfProjekt = new WrapperTextField();
	private WrapperButton wbuProjekt = null;

	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;

	private WrapperGotoButton wbuPartner = new WrapperGotoButton(
			WrapperGotoButton.GOTO_PARTNER_AUSWAHL);
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperLabel wlaBis = new WrapperLabel();

	private PanelQueryFLR panelQueryFLRProjekt = null;

	private WrapperLabel wlaLfdUhrzeit = new WrapperLabel();

	private WrapperDateField wdfDate = new WrapperDateField();

	private WrapperTimeField wtfVon = new WrapperTimeField();
	private WrapperTimeField wtfBis = new WrapperTimeField();

	private WrapperButton wbuJetzt = new WrapperButton(new ImageIcon(getClass()
			.getResource("/com/lp/client/res/clock16x16.png")));

	private WrapperLabel wlaKommentarIntern = new WrapperLabel();
	private WrapperLabel wlaKommentarExtern = new WrapperLabel();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();
	private WrapperTextField wtfPartner = new WrapperTextField();
	private WrapperEditorField wefKommentarIntern = new WrapperEditorFieldKommentar(
			getInternalFrame(), "");
	private WrapperEditorField wefKommentarExtern = new WrapperEditorFieldKommentar(
			getInternalFrame(), "");
	java.util.Timer timer = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;

	private static final String ACTION_SPECIAL_PARTNER = "ACTION_SPECIAL_PARTNER";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "ACTION_SPECIAL_ANSPRECHPARTNER";

	public PanelTelefonzeiten(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfVon;
	}

	protected void setDefaults() {
		long now = System.currentTimeMillis();
		wdfDate.setDatumHeute();
		wtfVon.setTime(new Time(now));
		wtfBis.setTime(new Time(now));
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		telefonzeitenDto = new TelefonzeitenDto();
		leereAlleFelder(this);

		setDefaults();
	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(), null, false);

		new DialogQuery(panelQueryFLRProjekt);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuJetzt)) {
			startUhrzeitAktualisieren();
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
			if (telefonzeitenDto.getPartnerIId() != null) {
				dialogQueryAnsprechpartner(e);
			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("rechnung.kundewaehlen"));
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
			dialogQueryPartner(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROJEKT)) {
			dialogQueryProjektFromListe(e);
		}
	}

	/**
	 * Dialogfenster zur Ansprechpartnerauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		Integer ansprechpartnerIId = null;
		if (ansprechpartnerDto != null) {
			ansprechpartnerIId = ansprechpartnerDto.getIId();
		}

		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
				.createPanelFLRAnsprechpartner(getInternalFrame(),
						telefonzeitenDto.getPartnerIId(), ansprechpartnerIId,
						true, true);

		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	private void dialogQueryPartner(ActionEvent e) throws Throwable {
		panelQueryFLRPartner = PartnerFilterFactory.getInstance()
				.createPanelFLRPartner(getInternalFrame(),
						telefonzeitenDto.getPartnerIId(), true);
		new DialogQuery(panelQueryFLRPartner);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (pruefeObBuchungMoeglich()) {

			DelegateFactory.getInstance().getZeiterfassungDelegate()
					.removeTelefonzeiten(telefonzeitenDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (timer != null) {
			timer.cancel();
			wlaLfdUhrzeit.setText("");
		}
		super.eventActionUnlock(e);
	}

	protected void components2Dto() throws Throwable {

		Calendar date = Calendar.getInstance();
		Calendar temp = Calendar.getInstance();
		date.setTime(wdfDate.getDate());

		temp.setTimeInMillis(wtfVon.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DATE));
		telefonzeitenDto.setTVon(new Timestamp(temp.getTimeInMillis()
				- temp.getTimeInMillis() % 1000));

		temp.setTimeInMillis(wtfBis.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DATE));
		telefonzeitenDto.setTBis(new Timestamp(temp.getTimeInMillis()
				- temp.getTimeInMillis() % 1000));

		telefonzeitenDto.setPersonalIId(internalFrameZeiterfassung
				.getPersonalDto().getIId());
		telefonzeitenDto.setXKommentarext(wefKommentarExtern.getText());
		telefonzeitenDto.setXKommentarint(wefKommentarIntern.getText());
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wbuPartner.setOKey(telefonzeitenDto.getPartnerIId());
		wtfVon.setTime(new Time(telefonzeitenDto.getTVon().getTime()));
		wtfBis.setTime(new Time(telefonzeitenDto.getTBis().getTime()));
		wdfDate.setDate(new Date(telefonzeitenDto.getTVon().getTime()));

		wefKommentarExtern.setText(telefonzeitenDto.getXKommentarext());
		wefKommentarIntern.setText(telefonzeitenDto.getXKommentarint());

		if (telefonzeitenDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							telefonzeitenDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatFixTitelName1Name2());
		} else {
			wtfAnsprechpartner.setText(null);
		}
		if (telefonzeitenDto.getPartnerIId() != null) {
			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(telefonzeitenDto.getPartnerIId());
			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
		} else {
			wtfPartner.setText(null);
		}

		if (telefonzeitenDto.getProjektIId() != null) {
			ProjektDto pDto = DelegateFactory.getInstance()
					.getProjektDelegate()
					.projektFindByPrimaryKey(telefonzeitenDto.getProjektIId());
			wtfProjekt.setText(pDto.getCNr() + " " + pDto.getCTitel());
		} else {
			wtfProjekt.setText(null);
		}

		if (wtfBis.getTime() != null) {
			berechneDauer();
		} else {
			wlaLfdUhrzeit.setText("");
		}

	}

	private boolean pruefeObBuchungMoeglich() throws ExceptionLP, Throwable {

		Calendar date = Calendar.getInstance();
		Calendar temp = Calendar.getInstance();
		date.setTime(wdfDate.getDate());

		temp.setTimeInMillis(wtfVon.getTime().getTime());
		temp.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DATE));

		Timestamp tVon = new Timestamp(temp.getTimeInMillis()
				- temp.getTimeInMillis() % 1000);

		if (tVon != null) {

			// SP3285
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN)) {

				java.sql.Timestamp t = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.gibtEsBereitseinenZeitabschlussBisZurKW(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId(), tVon);

				if (t != null) {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis"));
					mf.setLocale(LPMain.getTheClient().getLocUi());

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(t.getTime());
					c.get(Calendar.WEEK_OF_YEAR);
					Object pattern[] = { c.get(Calendar.WEEK_OF_YEAR) };

					String sMsg = mf.format(pattern);

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"), sMsg);
					return false;
				}

			}

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

			if (cBisDahinDarfGeaendertWerden.getTimeInMillis() > tVon.getTime()) {

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
		}

		return true;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			if (pruefeObBuchungMoeglich()) {
				components2Dto();
				stopUhrzeitAktualisieren();
				boolean gespeichert = false;
				while (!gespeichert) {
					try {
						if (telefonzeitenDto.getIId() == null) {
							telefonzeitenDto.setIId(DelegateFactory
									.getInstance().getZeiterfassungDelegate()
									.createTelefonzeiten(telefonzeitenDto));

							setKeyWhenDetailPanel(telefonzeitenDto.getIId());
						} else {
							DelegateFactory.getInstance()
									.getZeiterfassungDelegate()
									.updateTelefonzeiten(telefonzeitenDto);
						}
						gespeichert = true;
					} catch (ExceptionLP ex) {
						if (ex.getICode() == EJBExceptionLP.FEHLER_ZEITBUCHUNGEN_VORHANDEN) {

							Object[] options = {
									LPMain.getTextRespectUISPr("pers.zeitdaten.vorher"),
									LPMain.getTextRespectUISPr("pers.zeitdaten.nachher"),
									LPMain.getTextRespectUISPr("lp.abbrechen") };

							int iOption = DialogFactory
									.showModalDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("pers.error.zeitdatenvorhandenum")
													+ " "
													+ Helper.formatTimestamp(
															(Timestamp) ex
																	.getAlInfoForTheClient()
																	.get(0),
															LPMain.getTheClient()
																	.getLocUi()),
											"", options, options[1]);
							if (iOption == JOptionPane.YES_OPTION) {
								telefonzeitenDto
										.setTVon(new java.sql.Timestamp(
												telefonzeitenDto.getTVon()
														.getTime() - 10));
								telefonzeitenDto
										.setTBis(new java.sql.Timestamp(
												telefonzeitenDto.getTBis()
														.getTime() - 10));
							} else if (iOption == JOptionPane.NO_OPTION) {
								telefonzeitenDto
										.setTVon(new java.sql.Timestamp(
												telefonzeitenDto.getTVon()
														.getTime() + 10));
								telefonzeitenDto
										.setTBis(new java.sql.Timestamp(
												telefonzeitenDto.getTBis()
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
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							telefonzeitenDto.getIId() + "");
				}
				eventYouAreSelected(false);
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);
				telefonzeitenDto.setAnsprechpartnerIId(ansprechpartnerDto
						.getIId());
				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
						.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRPartner) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(key);
				telefonzeitenDto.setPartnerIId(partnerDto.getIId());
				wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) panelQueryFLRProjekt.getSelectedId();
				if (key != null) {

					ProjektDto pDto = DelegateFactory.getInstance()
							.getProjektDelegate().projektFindByPrimaryKey(key);

					telefonzeitenDto.setProjektIId(key);
					wtfProjekt.setText(pDto.getCNr() + " " + pDto.getCTitel());

					// + Partner holen
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey(pDto.getPartnerIId());
					telefonzeitenDto.setPartnerIId(partnerDto.getIId());
					wtfPartner.setText(partnerDto.formatFixTitelName1Name2());

					if (pDto.getAnsprechpartnerIId() != null) {
						AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
								.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindByPrimaryKey(
										pDto.getAnsprechpartnerIId());
						telefonzeitenDto
								.setAnsprechpartnerIId(ansprechpartnerDto
										.getIId());
						wtfAnsprechpartner.setText(ansprechpartnerDto
								.getPartnerDto().formatFixTitelName1Name2());
					} else {
						telefonzeitenDto.setAnsprechpartnerIId(null);
						wtfAnsprechpartner.setText(null);
					}

				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPartner) {
				telefonzeitenDto.setPartnerIId(null);
				wtfPartner.setText(null);
			} else if (e.getSource() == panelQueryFLRProjekt) {
				telefonzeitenDto.setProjektIId(null);
				wtfProjekt.setText(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wdfDate.setMandatoryField(true);
		wtfVon.setMandatoryField(true);
		wtfBis.setMandatoryField(true);
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wtfVon.setShowSeconds(true);

		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wtfBis.setShowSeconds(true);
		FocusListener focusListener = new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				stopUhrzeitAktualisieren();
			}
		};

		wbuProjekt = new WrapperButton(
				LPMain.getTextRespectUISPr("auft.report.projekte"));
		wbuProjekt.setActionCommand(ACTION_SPECIAL_PROJEKT);
		wbuProjekt.addActionListener(this);
		wbuProjekt.setMnemonic(KeyEvent.VK_R);
		wtfProjekt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfProjekt.setActivatable(false);

		wtfBis.installFocusListener(focusListener);

		wbuJetzt.setToolTipText("Aktuelle Zeit einstellen");
		wbuJetzt.addActionListener(this);
		// wbuJetzt.setEnabled(false);

		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);
		wbuPartner.setMnemonic(KeyEvent.VK_P);

		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);
		wbuAnsprechpartner.setMnemonic(KeyEvent.VK_A);

		wlaKommentarExtern.setText(LPMain
				.getTextRespectUISPr("lp.externerkommentar"));
		wlaKommentarIntern.setText(LPMain
				.getTextRespectUISPr("lp.internerkommentar"));
		wefKommentarExtern.setMandatoryField(true);
		wefKommentarExtern.setEditButtonMnemonic(KeyEvent.VK_E);
		wefKommentarExtern.setDefaultButtonMnemonic(KeyEvent.VK_Z);

		wefKommentarIntern.setEditButtonMnemonic(KeyEvent.VK_I);
		wefKommentarIntern.setDefaultButtonMnemonic(KeyEvent.VK_Y);

		wtfAnsprechpartner.setActivatable(false);
		wtfPartner.setActivatable(false);
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfVon.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wdfDate, new GridBagConstraints(1, iZeile, 1, 1, 0.2,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wtfVon, new GridBagConstraints(3, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaBis, new GridBagConstraints(4, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wtfBis, new GridBagConstraints(5, iZeile, 1, 1, 0.2,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wbuJetzt, new GridBagConstraints(6, iZeile, 1, 1, 0,
				0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 10, 0));

		iZeile++;

		jpaWorkingOn.add(wbuProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 4, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuPartner, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfPartner, new GridBagConstraints(1, iZeile, 4, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				4, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLfdUhrzeit, new GridBagConstraints(5, iZeile, 2, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommentarExtern, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentarExtern, new GridBagConstraints(1, iZeile,
				6, 1, 0, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentarIntern, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentarIntern, new GridBagConstraints(1, iZeile,
				6, 1, 0, 0.05, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_TELEFONZEITEN;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			clearStatusbar();
			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				startUhrzeitAktualisieren();
			}
		} else {
			telefonzeitenDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.telefonzeitenFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	private void stopUhrzeitAktualisieren() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	private void startUhrzeitAktualisieren() {
		stopUhrzeitAktualisieren();
		wtfBis.setTime(new Time(System.currentTimeMillis()));
		timer = new java.util.Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					wtfBis.setTime(new Time(System.currentTimeMillis()));
					berechneDauer();
				} catch (ExceptionLP e) {
				}
			}
		}, 0, 500);
	}

	public void setzeAusWrapperTelefonField(Integer partnerIId,
			Integer ansprechpartnerIId, Integer projektIId) throws Throwable {

		telefonzeitenDto.setPartnerIId(partnerIId);
		telefonzeitenDto.setAnsprechpartnerIId(ansprechpartnerIId);

		telefonzeitenDto.setProjektIId(projektIId);

		if (ansprechpartnerIId != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId);
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto()
					.formatFixTitelName1Name2());
		} else {
			wtfAnsprechpartner.setText(null);
		}
		if (partnerIId != null) {
			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate().partnerFindByPrimaryKey(partnerIId);
			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
		} else {
			wtfPartner.setText(null);
		}
		if (projektIId != null) {
			ProjektDto pDto = DelegateFactory.getInstance()
					.getProjektDelegate().projektFindByPrimaryKey(projektIId);

			wtfProjekt.setText(pDto.getCNr() + " " + pDto.getCTitel());
		}

		// Heutiges Datum und Zeit in richtigem Locale, und Bearbeiter
		// Kurzzeichen einfuegen
		Locale editorLocale = LPMain.getInstance().getUISprLocale();
		GregorianCalendar cal = new GregorianCalendar(editorLocale);
		// Datum
		SimpleDateFormat sdDateFormat = new SimpleDateFormat("dd.MM.yyyy",
				editorLocale);
		String content = sdDateFormat.format(cal.getTime());
		// Uhrzeit
		SimpleDateFormat sdTimeFormat = new SimpleDateFormat("HH:mm",
				editorLocale);
		content += " " + sdTimeFormat.format(cal.getTime());
		// Personal Kurzzeichen des Bearbeiters
		try {
			TheClientDto clientDto = LPMain.getInstance().getTheClient();
			Integer iPersonalID = clientDto.getIDPersonal();
			PersonalDelegate persDelegate = DelegateFactory.getInstance()
					.getPersonalDelegate();
			PersonalDto persDto = persDelegate
					.personalFindByPrimaryKey(iPersonalID);
			String sPersKurzzeichen = persDto.getCKurzzeichen();
			content += " " + sPersKurzzeichen;
		} catch (Throwable ex) {
		}

		wefKommentarExtern.setText(content);

	}

	private void berechneDauer() throws ExceptionLP {

		if (wtfBis.getTime() != null && wtfVon.getTime() != null) {
			long time = (wtfBis.getTime().getTime() - wtfVon.getTime()
					.getTime());
			wlaLfdUhrzeit.setText(LPMain.getTextRespectUISPr("lp.dauer")
					+ String.format(": %02d:%02d:%02d",
							TimeUnit.MILLISECONDS.toHours(time),
							TimeUnit.MILLISECONDS.toMinutes(time) % 60,
							TimeUnit.MILLISECONDS.toSeconds(time) % 60));
		}
	}
}
