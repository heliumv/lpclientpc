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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
public class PanelWartungsschritte extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameInstandhaltung internalFrameInstandhaltung = null;
	private WrapperLabel wlaStueckzeit = new WrapperLabel();
	private WrapperSpinner wspStueckzeitStunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(50000), new Integer(1));
	private WrapperSpinner wspStueckzeitMinuten = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperSpinner wspStueckzeitSekunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));

	private WrapperLabel wlaStueckzeitUmgewandelt = new WrapperLabel();

	private WrapperLabel wlaWiederholungsintervall = null;
	private WrapperComboBox wcoWiederholungsintervall = null;

	private WrapperLabel wlaAbDatum = new WrapperLabel();
	private WrapperDateField wdfAbDatum = new WrapperDateField();
	
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();
	private WrapperLabel wlaBemerkungLieferant = new WrapperLabel();
	private WrapperTextField wtfBemerkungLieferant = new WrapperTextField();


	private WartungsschritteDto wartungsschritteDto = null;

	private WrapperSelectField wsfTagesart = new WrapperSelectField(
			WrapperSelectField.TAGESART, getInternalFrame(), true);
	private WrapperSelectField wsfLieferant = new WrapperSelectField(
			WrapperSelectField.LIEFERANT, getInternalFrame(), true);

	private WrapperSelectField wsfPersonalgruppe = new WrapperSelectField(
			WrapperSelectField.PERSONALGRUPPE, getInternalFrame(), true);

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaUeberschriftZeit = new WrapperLabel();

	public PanelWartungsschritte(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameInstandhaltung = (InternalFrameInstandhaltung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	private void setDefaults() throws Throwable {
		wcoWiederholungsintervall.setMap(DelegateFactory
				.getInstance()
				.getAuftragServiceDelegate()
				.getAuftragwiederholungsintervall(
						LPMain.getInstance().getUISprLocale()));
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wifArtikel.getWtfIdent();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		wartungsschritteDto = new WartungsschritteDto();
		leereAlleFelder(this);
		wifArtikel.setArtikelDto(null);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getInstandhaltungDelegate()
				.removeWartungsschritte(wartungsschritteDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		wartungsschritteDto.setGeraetIId(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getIId());

		long sStunden = wspStueckzeitStunden.getInteger().longValue() * 3600000;
		long sMinuten = wspStueckzeitMinuten.getInteger().longValue() * 60000;
		long sSekunden = wspStueckzeitSekunden.getInteger().longValue() * 1000;
		long lStueckzeit = sStunden + sMinuten + sSekunden;

		wartungsschritteDto.setLDauer(new Long(lStueckzeit));

		wartungsschritteDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());

		wartungsschritteDto
				.setAuftragwiederholungsintervallCNr((String) wcoWiederholungsintervall
						.getKeyOfSelectedItem());
		wartungsschritteDto.setTAbdurchfuehren(wdfAbDatum.getTimestamp());

		wartungsschritteDto.setTagesartIId(wsfTagesart.getIKey());
		wartungsschritteDto.setLieferantIId(wsfLieferant.getIKey());
		wartungsschritteDto.setPersonalgruppeIId(wsfPersonalgruppe.getIKey());
		wartungsschritteDto.setCBemerkung(wtfBemerkung.getText());
		wartungsschritteDto.setCBemerkunglieferant(wtfBemerkungLieferant.getText());
	}

	protected void dto2Components() throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getInstance().getTheClient().getMandant());
		String sEinheit = parameter.getCWert().trim();

		double lStueckzeit = wartungsschritteDto.getLDauer().longValue();

		double dStueckzeit = 0;

		if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
			dStueckzeit = lStueckzeit / 3600000;

		} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
			dStueckzeit = lStueckzeit / 60000;

		} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
			dStueckzeit = lStueckzeit / 100;

		}
		dStueckzeit = (wartungsschritteDto.getLDauer().longValue());
		if (dStueckzeit > 0) {
			double sStunden = dStueckzeit / 3600000;
			if (sStunden != 0) {
				dStueckzeit = dStueckzeit % 3600000;
			}
			double sMinuten = dStueckzeit / 60000;
			if (sMinuten != 0) {
				dStueckzeit = dStueckzeit % 60000;
			}
			double sSekunden = dStueckzeit / 1000;
			if (sSekunden != 0) {
				dStueckzeit = dStueckzeit % 1000;
			}
			
			wspStueckzeitStunden.setInteger(new Integer((int) sStunden));
			wspStueckzeitMinuten.setInteger(new Integer((int) sMinuten));
			wspStueckzeitSekunden.setInteger(new Integer((int) sSekunden));

		} else {
			wspStueckzeitStunden.setInteger(new Integer(0));
			wspStueckzeitMinuten.setInteger(new Integer(0));
			wspStueckzeitSekunden.setInteger(new Integer(0));

		}
		wcoWiederholungsintervall.setKeyOfSelectedItem(wartungsschritteDto
				.getAuftragwiederholungsintervallCNr());

		wtfBemerkung.setText(wartungsschritteDto.getCBemerkung());
		wdfAbDatum.setTimestamp(wartungsschritteDto.getTAbdurchfuehren());
		wsfTagesart.setKey(wartungsschritteDto.getTagesartIId());
		wsfPersonalgruppe.setKey(wartungsschritteDto.getPersonalgruppeIId());
		wsfLieferant.setKey(wartungsschritteDto.getLieferantIId());
		wtfBemerkungLieferant.setText(wartungsschritteDto.getCBemerkunglieferant());

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(wartungsschritteDto.getArtikelIId());

		wifArtikel.setArtikelDto(aDto);

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (wartungsschritteDto.getIId() == null) {
				wartungsschritteDto.setIId(DelegateFactory.getInstance()
						.getInstandhaltungDelegate()
						.createWartungsschritte(wartungsschritteDto));
				setKeyWhenDetailPanel(wartungsschritteDto.getIId());
			} else {
				DelegateFactory.getInstance().getInstandhaltungDelegate()
						.updateWartungsschritte(wartungsschritteDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getGeraetDto()
								.getIId()
								+ "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.setDefaultFilter(ArtikelFilterFactory.getInstance()
				.createFKArtikellisteNurArbeitszeit());

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaWiederholungsintervall = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.wiederholungsintervall"));
		wcoWiederholungsintervall = new WrapperComboBox();
		wcoWiederholungsintervall.setMandatoryFieldDB(true);

		wlaStueckzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dauer"));
		wlaAbDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"is.geraetestueckliste.abdatum"));
		wlaBemerkung.setText(LPMain.getInstance().getTextRespectUISPr(
		"lp.bemerkung"));
		wlaBemerkungLieferant.setText(LPMain.getInstance().getTextRespectUISPr(
		"lp.bemerkung"));

		wtfBemerkung.setColumnsMax(80);

		wdfAbDatum.setMandatoryField(true);

		wspStueckzeitStunden.setMandatoryField(true);
		wspStueckzeitMinuten.setMandatoryField(true);
		wspStueckzeitSekunden.setMandatoryField(true);

		wsfPersonalgruppe.setMandatoryField(true);

		wlaStueckzeitUmgewandelt.setHorizontalAlignment(SwingConstants.LEFT);

		wlaUeberschriftZeit
				.setText("Std.                 Min.                 Sek.");

		// wlaUeberschriftZeit.setHorizontalAlignment(wlaUeberschriftZeit.LEFT);

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
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		wifArtikel.getWtfIdent().setUppercaseField(true);

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, 0,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(3, 0, 2, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		
		jpaWorkingOn.add(wlaUeberschriftZeit, new GridBagConstraints(1, 1, 1,
				1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaStueckzeit, new GridBagConstraints(0, 2, 1, 1,
				0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		JPanel jpStueckzeit = new JPanel();
		jpStueckzeit.setLayout(new java.awt.GridBagLayout());
		jpStueckzeit.setSize(110, Defaults.getInstance().getControlHeight());
		jpStueckzeit.add(wspStueckzeitStunden, new GridBagConstraints(0, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 30, 0));
		jpStueckzeit.add(wspStueckzeitMinuten, new GridBagConstraints(1, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 30, 0));
		jpStueckzeit.add(wspStueckzeitSekunden, new GridBagConstraints(2, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 4, 0, 0), 30, 0));

		jpaWorkingOn.add(jpStueckzeit, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 80, 0));

		jpaWorkingOn.add(wlaWiederholungsintervall, new GridBagConstraints(0,
				5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWiederholungsintervall, new GridBagConstraints(1,
				5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperButton(), new GridBagConstraints(3,
				5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(), new GridBagConstraints(4,
				5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaAbDatum, new GridBagConstraints(0, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAbDatum, new GridBagConstraints(1, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		
		jpaWorkingOn.add(wlaBemerkungLieferant, new GridBagConstraints(3,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBemerkungLieferant, new GridBagConstraints(4,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),0, 0));
		
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperButton(),
				new GridBagConstraints(0, 7, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperTextField(),
				new GridBagConstraints(1, 7, 2, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wsfTagesart.getWrapperButton(),
				new GridBagConstraints(0, 8, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));
		jpaWorkingOn.add(wsfTagesart.getWrapperTextField(),
				new GridBagConstraints(1, 8, 2, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaBemerkung,
				new GridBagConstraints(0, 9, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));
		jpaWorkingOn.add(wtfBemerkung,
				new GridBagConstraints(1, 9, 4, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSTANDHALTUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

		} else {
			wartungsschritteDto = DelegateFactory.getInstance()
					.getInstandhaltungDelegate()
					.wartungsschritteFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
