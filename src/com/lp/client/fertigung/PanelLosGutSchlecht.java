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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.reklamation.ReklamationFilterFactory;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Panel zum Bearbeiten der Ablieferungen eines Loses</p> <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>11. 10.
 * 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.11 $
 */
public class PanelLosGutSchlecht extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneLos tabbedPaneLos = null;

	private LosgutschlechtDto losgutschlechtDto = null;

	private JPanel jPanelWorkingOn = null;
	private Border border1;

	private PanelQueryFLR panelQueryFLRFehler = null;
	static final public String ACTION_SPECIAL_FEHLER_FROM_LISTE = "ACTION_SPECIAL_FEHLER_FROM_LISTE";
	private WrapperButton wbuFehler = new WrapperButton();

	private WrapperTextField wtfFehler = new WrapperTextField();

	static final public String ACTION_SPECIAL_ZEITDATEN_FROM_LISTE = "action_zeitdaten_from_liste";
	private PanelQueryFLR panelQueryFLRZeitdaten = null;
	private PanelQueryFLR panelQueryFLRMaschine = null;

	private WrapperButton wbuZeitdaten = new WrapperButton();
	private WrapperTimestampField wtfZeitdaten = new WrapperTimestampField();

	private WrapperLabel wlaGut = new WrapperLabel();
	private WrapperNumberField wnfGut = new WrapperNumberField();
	private WrapperLabel wlaSchlecht = new WrapperLabel();
	private WrapperNumberField wnfSchlecht = new WrapperNumberField();
	private WrapperLabel wlaInarbeit = new WrapperLabel();
	private WrapperNumberField wnfInarbeit = new WrapperNumberField();

	private WrapperLabel wlaAufspannung = new WrapperLabel();
	private WrapperLabel wlaPerson = new WrapperLabel();
	private WrapperLabel wlaArbeitsgang = new WrapperLabel();

	private WrapperLabel wlaGesamtGut = new WrapperLabel();
	private WrapperLabel wlaGesamtSchlecht = new WrapperLabel();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	
	private WrapperRadioButton wrbPersonal = new WrapperRadioButton();
	private WrapperRadioButton wrbMaschine = new WrapperRadioButton();
	private ButtonGroup buttonGroup1 = new ButtonGroup();

	public PanelLosGutSchlecht(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneLos tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneLos = tabbedPaneLos;
		jbInit();
		initComponents();

	}

	private TabbedPaneLos getTabbedPaneLos() {
		return tabbedPaneLos;
	}

	private void jbInit() throws Throwable {
		jPanelWorkingOn = new JPanel();

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(new GridBagLayout());

		jPanelWorkingOn.setBorder(border1);
		jPanelWorkingOn.setLayout(new GridBagLayout());
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wbuFehler.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.fehler")
				+ "...");
		wbuFehler.setActionCommand(ACTION_SPECIAL_FEHLER_FROM_LISTE);
		wbuFehler.addActionListener(this);
		wtfFehler.setActivatable(false);
		wtfFehler.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		// controls
		wlaGut.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.zeitdaten.mengegut"));
		wlaSchlecht.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.zeitdaten.mengeschlecht"));
		wbuZeitdaten.setText(LPMain
				.getTextRespectUISPr("pers.personalzeitbuchung") + "...");

		wrbPersonal.setText(LPMain
				.getTextRespectUISPr("pers.personalzeitbuchung"));
		wrbMaschine.setText(LPMain.getTextRespectUISPr("lp.maschine"));
		buttonGroup1.add(wrbPersonal);
		buttonGroup1.add(wrbMaschine);

		wrbPersonal.addActionListener(this);
		wrbMaschine.addActionListener(this);
		wrbPersonal.setSelected(true);
		wlaInarbeit.setText(LPMain.getTextRespectUISPr("fert.inarbeit"));

		wbuZeitdaten.setActionCommand(ACTION_SPECIAL_ZEITDATEN_FROM_LISTE);
		wbuZeitdaten.addActionListener(this);

		wlaAufspannung.setHorizontalAlignment(SwingConstants.LEFT);
		wlaPerson.setHorizontalAlignment(SwingConstants.LEFT);
		wlaArbeitsgang.setHorizontalAlignment(SwingConstants.LEFT);
		wlaGesamtGut.setHorizontalAlignment(SwingConstants.LEFT);
		wlaGesamtSchlecht.setHorizontalAlignment(SwingConstants.LEFT);

		wnfGut.setMandatoryField(true);
		wnfSchlecht.setMandatoryField(true);
		wnfInarbeit.setMandatoryField(true);
		wtfZeitdaten.setMandatoryField(true);
		wtfZeitdaten.setActivatable(false);
		
		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wtfKommentar.setColumnsMax(300);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wrbPersonal, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 140, 0));
		jPanelWorkingOn.add(wrbMaschine, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 180, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaArbeitsgang, new GridBagConstraints(1, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuZeitdaten, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 140, 0));
		jPanelWorkingOn.add(wtfZeitdaten, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 180, 0));
		jPanelWorkingOn.add(wlaPerson, new GridBagConstraints(3, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaGut, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGut, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaGesamtGut, new GridBagConstraints(2, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaSchlecht, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfSchlecht, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaGesamtSchlecht, new GridBagConstraints(2,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaInarbeit, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfInarbeit, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAufspannung, new GridBagConstraints(2, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuFehler, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfFehler, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));


	}

	void dialogQueryZeitdatenFromListe(ActionEvent e) throws Throwable {

		java.sql.Date dDatum = DialogFactory.showDatumseingabe(LPMain
				.getTextRespectUISPr("fert.datumderstueckmeldung"));

		if (dDatum != null) {
			panelQueryFLRZeitdaten = ZeiterfassungFilterFactory.getInstance()
					.createPanelFLRZeitdatenGutSchlecht(getInternalFrame(),
							dDatum, losgutschlechtDto.getZeitdatenIId());

			new DialogQuery(panelQueryFLRZeitdaten);
		}

	}

	void dialogQueryFehlerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRFehler = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_FEHLER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"rekla.fehler"));
		panelQueryFLRFehler.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance()
						.createFKDBezeichnungMitAlias("fehler"), null);
		panelQueryFLRFehler.setSelectedId(losgutschlechtDto.getFehlerIId());

		new DialogQuery(panelQueryFLRFehler);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (losgutschlechtDto.getIId() == null) {
				// Create
				losgutschlechtDto.setIId(DelegateFactory.getInstance()
						.getFertigungDelegate()
						.createLosgutschlecht(losgutschlechtDto));
				setKeyWhenDetailPanel(losgutschlechtDto.getIId());
			} else {
				DelegateFactory.getInstance().getFertigungDelegate()
						.updateLosgutschlecht(losgutschlechtDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						getTabbedPaneLos().getLosDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		losgutschlechtDto.setLossollarbeitsplanIId((Integer) getTabbedPaneLos()
				.getPanelQueryZeitdaten(true).getSelectedId());
		losgutschlechtDto.setNGut(wnfGut.getBigDecimal());
		losgutschlechtDto.setNSchlecht(wnfSchlecht.getBigDecimal());
		losgutschlechtDto.setNInarbeit(wnfInarbeit.getBigDecimal());
		losgutschlechtDto.setCKommentar(wtfKommentar.getText());
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wnfGut.setBigDecimal(losgutschlechtDto.getNGut());
		wnfSchlecht.setBigDecimal(losgutschlechtDto.getNSchlecht());
		wnfInarbeit.setBigDecimal(losgutschlechtDto.getNInarbeit());
		wtfKommentar.setText(losgutschlechtDto.getCKommentar());

		if (losgutschlechtDto.getZeitdatenIId() != null) {

			ZeitdatenDto dto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.zeitdatenFindByPrimaryKey(
							losgutschlechtDto.getZeitdatenIId());
			wtfZeitdaten.setTimestamp(dto.getTZeit());

			PersonalDto personalDto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(dto.getPersonalIId());

			wlaPerson.setText(personalDto.getPartnerDto().formatAnrede());
			wrbPersonal.setSelected(true);
		} else {
			wlaPerson.setText("");

			MaschinenzeitdatenDto maschinenzeitdatenDto = null;
			maschinenzeitdatenDto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.maschinenzeitdatenFindByPrimaryKey(
							losgutschlechtDto.getMaschinenzeitdatenIId());

			wtfZeitdaten.setTimestamp(maschinenzeitdatenDto.getTVon());

			MaschineDto maschineDto = null;
			maschineDto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.maschineFindByPrimaryKey(
							maschinenzeitdatenDto.getMaschineIId());

			wlaPerson.setText(maschineDto.getBezeichnung());
			wrbMaschine.setSelected(true);
		}

		if (losgutschlechtDto.getFehlerIId() != null) {
			FehlerDto fehlerDto = DelegateFactory.getInstance()
					.getReklamationDelegate()
					.fehlerFindByPrimaryKey(losgutschlechtDto.getFehlerIId());
			wtfFehler.setText(fehlerDto.getKennungUndBezeichnung());
		} else {
			wtfFehler.setText(null);
		}

		ActionEvent e = new ActionEvent(wrbMaschine, 0, "");
		eventActionSpecial(e);

		setStatusbarStatusCNr(getTabbedPaneLos().getLosDto().getStatusCNr());
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		losgutschlechtDto = new LosgutschlechtDto();
		this.leereAlleFelder(this);
		clearStatusbar();
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.losgutschlechtDto != null) {
			if (losgutschlechtDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getFertigungDelegate()
							.removeLosgutschlecht(losgutschlechtDto);
					this.losgutschlechtDto = null;
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LOS;
	}

	private void dialogQueryMaschineFromListe() throws Throwable {
		java.sql.Date dDatum = DialogFactory.showDatumseingabe(LPMain
				.getTextRespectUISPr("fert.datumderstueckmeldung"));

		if (dDatum != null) {
			panelQueryFLRMaschine = ZeiterfassungFilterFactory.getInstance()
					.createPanelFLRMaschinenZeitdatenGutSchlecht(
							getInternalFrame(), dDatum,
							losgutschlechtDto.getMaschinenzeitdatenIId());
			new DialogQuery(panelQueryFLRMaschine);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ZEITDATEN_FROM_LISTE)) {
			if (wrbPersonal.isSelected()) {
				dialogQueryZeitdatenFromListe(e);
			} else {
				dialogQueryMaschineFromListe();
			}

		} else if (e.getSource().equals(wrbMaschine)
				|| e.getSource().equals(wrbPersonal)) {

			if (wrbMaschine.isSelected()) {
				wbuZeitdaten.setText(LPMain.getTextRespectUISPr("lp.maschine")
						+ "...");
			}

			else if (wrbPersonal.isSelected()) {
				wbuZeitdaten.setText(LPMain
						.getTextRespectUISPr("pers.personalzeitbuchung")
						+ "...");
			}

		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FEHLER_FROM_LISTE)) {
			dialogQueryFehlerFromListe(e);
		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRZeitdaten) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					ZeitdatenDto dto = DelegateFactory.getInstance()
							.getZeiterfassungDelegate()
							.zeitdatenFindByPrimaryKey((Integer) key);
					losgutschlechtDto.setZeitdatenIId(dto.getIId());
					losgutschlechtDto.setMaschinenzeitdatenIId(null);
					wtfZeitdaten.setTimestamp(dto.getTZeit());
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(dto.getPersonalIId());

					wlaPerson.setText(personalDto.getPartnerDto()
							.formatAnrede());

				}
			} else if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				MaschinenzeitdatenDto maschinenzeitdatenDto = null;
				maschinenzeitdatenDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.maschinenzeitdatenFindByPrimaryKey(key);

				wtfZeitdaten.setTimestamp(maschinenzeitdatenDto.getTVon());

				MaschineDto maschineDto = null;
				maschineDto = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.maschineFindByPrimaryKey(
								maschinenzeitdatenDto.getMaschineIId());
				losgutschlechtDto
						.setMaschinenzeitdatenIId(maschinenzeitdatenDto
								.getIId());
				losgutschlechtDto.setZeitdatenIId(null);

				wlaPerson.setText(maschineDto.getBezeichnung());
			} else if (e.getSource() == panelQueryFLRFehler) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				FehlerDto fehlerDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.fehlerFindByPrimaryKey((Integer) key);
				wtfFehler.setText(fehlerDto.getKennungUndBezeichnung());
				losgutschlechtDto.setFehlerIId((Integer) key);

			}
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || key.equals(LPMain.getLockMeForNew())) {
				wnfGut.setBigDecimal(new BigDecimal(0));
				wnfSchlecht.setBigDecimal(new BigDecimal(0));
				wnfInarbeit.setBigDecimal(new BigDecimal(0));
			} else {
				// einen alten Eintrag laden.
				losgutschlechtDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losgutschlechtFindByPrimaryKey((Integer) key);
				dto2Components();
			}

		}

		if (getTabbedPaneLos().getPanelQueryZeitdaten(true).getSelectedId() != null) {

			LossollarbeitsplanDto sollDto = null;
			try {
				sollDto = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey(
								(Integer) getTabbedPaneLos()
										.getPanelQueryZeitdaten(true)
										.getSelectedId());
			} catch (Exception e) {
				// dann nicht mehr vorhanden
			}
			if (sollDto != null) {
				String aufspannung = "";
				if (sollDto.getIAufspannung() != null) {
					aufspannung = sollDto.getIAufspannung() + "";
				}
				wlaAufspannung.setText(LPMain
						.getTextRespectUISPr("stkl.aufspannung")
						+ ": "
						+ aufspannung);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								sollDto.getArtikelIIdTaetigkeit());
				String ag = "AG: " + sollDto.getIArbeitsgangnummer() + ", "
						+ artikelDto.formatArtikelbezeichnung();

				wlaArbeitsgang.setText(ag);
				wlaGesamtGut
						.setText(LPMain.getTextRespectUISPr("fert.gutgesamt")
								+ ": "
								+ Helper.formatZahl(
										DelegateFactory
												.getInstance()
												.getZeiterfassungDelegate()
												.getMengeGutSchlechtEinesLosSollarbeitsplanes(
														sollDto.getIId(), true),
										2, LPMain.getTheClient().getLocUi()));
				wlaGesamtSchlecht
						.setText(LPMain
								.getTextRespectUISPr("fert.schlechtgesamt")
								+ ": "
								+ Helper.formatZahl(
										DelegateFactory
												.getInstance()
												.getZeiterfassungDelegate()
												.getMengeGutSchlechtEinesLosSollarbeitsplanes(
														sollDto.getIId(), false),
										2, LPMain.getTheClient().getLocUi()));
			}
		}

	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		super.eventActionUnlock(e);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (getTabbedPaneLos().getLosDto().getStatusCNr()
				.equals(FertigungFac.STATUS_STORNIERT)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					new Exception(getTabbedPaneLos().getLosDto().getCNr()
							+ " ist storniert"));
		}
		if (getTabbedPaneLos().getLosDto().getStatusCNr()
				.equals(FertigungFac.STATUS_ERLEDIGT)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					new Exception(getTabbedPaneLos().getLosDto().getCNr()
							+ " ist erledigt"));
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		if (exfc.getICode() == EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN) {
			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getMsg(exfc));
			return true;
		} else {
			return false;
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfGut;
	}

}
