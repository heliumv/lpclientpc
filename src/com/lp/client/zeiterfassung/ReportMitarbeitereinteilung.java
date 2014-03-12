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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PanelPersonal;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportMitarbeitereinteilung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperLabel wbuPersonal = new WrapperLabel();
	private WrapperTextField wtfPersonal = new WrapperTextField();
	private WrapperCheckBox wcbNurSelektiertePerson = new WrapperCheckBox();

	private WrapperButton wbuPersonalgruppe = new WrapperButton();
	private WrapperTextField wtfPersonalgruppe = new WrapperTextField();
	private WrapperLabel wlaSortierung = new WrapperLabel();
	static final public String ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE = "action_personalgruppe_from_liste";

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private JRadioButton wrbSortPersonalnummer = new JRadioButton();
	private JRadioButton wrbSortNameVorname = new JRadioButton();
	private JRadioButton wrbSortAbteilungNameVorname = new JRadioButton();

	private JRadioButton wrbSortKostenstelleNameVorname = new JRadioButton();
	private JRadioButton wrbSortAbteilungKostenstelleNameVorname = new JRadioButton();

	private PanelQueryFLR panelQueryFLRPersonalgruppe = null;

	private Integer personalgruppeIId = null;

	protected JPanel jpaWorkingOn = new JPanel();

	public ReportMitarbeitereinteilung(
			InternalFrameZeiterfassung internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();

		if (internalFrame.getPersonalDto() != null) {
			wtfPersonal.setText(internalFrame.getPersonalDto().formatAnrede());

		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdfVon.setTimestamp(Helper.cutTimestamp(new Timestamp(System
				.currentTimeMillis())));
		wdfVon.setActivatable(false);
		wdfVon.setEditable(false);
		wdfVon.setEnabled(false);

		long l = System.currentTimeMillis();
		l = l + (24 * 3600000 * 20);
		l = l + (24 * 3600000 * 20);
		l = l + (24 * 3600000 * 16);

		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(l);
		wdfBis.setTimestamp(wdfBisTemp);

		wbuPersonal
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson")
						+ ": ");

		wbuPersonalgruppe = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("pers.personalgruppe") + "...");

		wbuPersonalgruppe
				.setActionCommand(PanelPersonal.ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE);
		wbuPersonalgruppe.addActionListener(this);
		wtfPersonalgruppe = new WrapperTextField();
		wtfPersonalgruppe.setActivatable(false);

		wcbNurSelektiertePerson
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setEditable(false);
		wtfPersonal.setSaveReportInformation(false);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbSortAbteilungNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.abteilungnamevorname"));
		wrbSortNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.namevorname"));
		wrbSortPersonalnummer.setText(LPMain
				.getTextRespectUISPr("pers.personal.personalnummer"));

		wrbSortKostenstelleNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.kostenstellenamevorname"));
		wrbSortAbteilungKostenstelleNameVorname
				.setText(LPMain
						.getTextRespectUISPr("pers.monatsabrechnung.sortierung.abteilungkostenstellenamevorname"));

		buttonGroupSortierung.add(wrbSortAbteilungNameVorname);
		buttonGroupSortierung.add(wrbSortNameVorname);
		buttonGroupSortierung.add(wrbSortPersonalnummer);
		buttonGroupSortierung.add(wrbSortKostenstelleNameVorname);
		buttonGroupSortierung.add(wrbSortAbteilungKostenstelleNameVorname);
		wrbSortPersonalnummer.setSelected(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, iZeile, 2, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbNurSelektiertePerson, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuPersonalgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalgruppe, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortPersonalnummer, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortNameVorname, new GridBagConstraints(3, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortAbteilungNameVorname, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortKostenstelleNameVorname,
				new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortAbteilungKostenstelleNameVorname,
				new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONALGRUPPE_FROM_LISTE)) {
			dialogQueryPersonalgruppeFromListe(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonalgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				personalgruppeIId = key;

				PersonalgruppeDto personalgruppeDto = null;
				personalgruppeDto = DelegateFactory.getInstance()
						.getPersonalDelegate()
						.personalgruppeFindByPrimaryKey(key);
				wtfPersonalgruppe.setText(personalgruppeDto.getCBez());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPersonalgruppe) {
				wtfPersonalgruppe.setText(null);
				personalgruppeIId = null;
			}
		}
	}

	public String getModul() {
		return ZeiterfassungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_MASCHINENBELEGUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(wdfBis
				.getTimestamp().getTime() + 24 * 3600000);

		Integer personalIId = null;

		String sortierung = "";
		Integer iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER;
		if (wrbSortAbteilungNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_NAME_VORNAME;
			sortierung = wrbSortAbteilungNameVorname.getText();
		} else if (wrbSortNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_NAME_VORNAME;
			sortierung = wrbSortNameVorname.getText();
		} else if (wrbSortKostenstelleNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_KOSTENSTELLE_NAME_VORNAME;
			sortierung = wrbSortKostenstelleNameVorname.getText();
		} else if (wrbSortAbteilungKostenstelleNameVorname.isSelected()) {
			iSortierung = ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_ABTEILUNG_KOSTENSTELLE_NAME_VORNAME;
			sortierung = wrbSortAbteilungKostenstelleNameVorname.getText();
		}

		if (wcbNurSelektiertePerson.isSelected() == true) {
			personalIId = ((InternalFrameZeiterfassung) getInternalFrame())
					.getPersonalDto().getIId();
			return DelegateFactory
					.getInstance()
					.getZeiterfassungReportDelegate()
					.printMitarbeitereinteilung(personalIId, null, wdfBisTemp,
							iSortierung, sortierung);
		} else {
			return DelegateFactory
					.getInstance()
					.getZeiterfassungReportDelegate()
					.printMitarbeitereinteilung(null, personalgruppeIId,
							wdfBisTemp, iSortierung, sortierung);
		}

	}

	void dialogQueryPersonalgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPersonalgruppe = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonalgruppe(getInternalFrame(),
						personalgruppeIId, true);
		new DialogQuery(panelQueryFLRPersonalgruppe);

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
