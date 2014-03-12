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
package com.lp.client.angebot;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.angebot.service.ReportAngebotJournalKriterienDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAngebotAlle extends PanelReportJournalVerkauf implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @todo den Vertreter in die Superklasse. warum bauen wir immer alles 17
	 *       mal????
	 */
	private ButtonGroup jbgVertreter = new ButtonGroup();
	protected WrapperRadioButton wrbVertreterAlle = new WrapperRadioButton();
	protected WrapperRadioButton wrbVertreterEiner = new WrapperRadioButton();
	protected PersonalDto vertreterDto = null;

	private WrapperRadioButton wrbSortierungVertreter = new WrapperRadioButton();
	private WrapperButton wbuVertreter = null;
	private WrapperTextField wtfVertreter = null;
	private JPanel jPanelVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;

	private final static String ACTION_SPECIAL_SORTIERUNG_VERTRETER = "action_special_sortierung_vertreter";
	private final static String ACTION_SPECIAL_VERTRETER_AUSWAHL = "action_special_vertreter_auswahl";
	private final static String ACTION_SPECIAL_VERTRETER_ALLE = "action_special_vertreter_alle";
	private final static String ACTION_SPECIAL_VERTRETER_EINER = "action_special_vertreter_einer";

	private final static String ACTION_SPECIAL_ANGEBOTERLEDIGUNGSGRUND = "action_special_angeboterledigungsgrund";

	private PanelQueryFLR panelQueryFLRAngeboterledigungsgrund = null;
	private WrapperButton wbuAngeboterledigungsgrund = null;
	private WrapperTextField wtfAngeboterledigungsgrund = null;
	private String erledigungsgrundCNr = null;
	private WrapperCheckBox wcbNurErledigteAngebote = null;
	private WrapperCheckBox wcbMitDetails = null;

	public ReportAngebotAlle(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);

		jbInit();
		setDefaults();
		initComponents();
	}

	protected void jbInit() throws Exception {
		wrbSortierungVertreter = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr("label.vertreter"));
		wrbSortierungVertreter
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_VERTRETER);
		wrbSortierungVertreter.addActionListener(this);
		buttonGroupSortierung.add(wrbSortierungVertreter);

		wrbVertreterAlle.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.alle"));
		wrbVertreterAlle.setActionCommand(ACTION_SPECIAL_VERTRETER_ALLE);
		wrbVertreterAlle.addActionListener(this);

		wrbVertreterEiner.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.einer"));
		wrbVertreterEiner.setActionCommand(ACTION_SPECIAL_VERTRETER_EINER);
		wrbVertreterEiner.addActionListener(this);

		jbgVertreter = new ButtonGroup();
		jbgVertreter.add(wrbVertreterAlle);
		jbgVertreter.add(wrbVertreterEiner);
		wbuVertreter = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.vertreter"));
		wbuVertreter.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.vertreter.tooltip"));
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_AUSWAHL);
		wbuVertreter.addActionListener(this);
		wbuVertreter.setMinimumSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));
		wbuVertreter.setPreferredSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));

		wbuAngeboterledigungsgrund = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("angb.angeboterledigungsgrund"));
		wbuAngeboterledigungsgrund.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("angb.angeboterledigungsgrund"));
		wbuAngeboterledigungsgrund
				.setActionCommand(ACTION_SPECIAL_ANGEBOTERLEDIGUNGSGRUND);
		wbuAngeboterledigungsgrund.addActionListener(this);

		wtfAngeboterledigungsgrund = new WrapperTextField();
		wtfAngeboterledigungsgrund.setEditable(false);

		wcbNurErledigteAngebote = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("angb.nurerledigte"));
		wcbMitDetails = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("angb.mitdetails"));

		wtfVertreter = new WrapperTextField();
		wtfVertreter.setEditable(false);
		wtfVertreter.setMinimumSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));
		wtfVertreter.setPreferredSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));
		// neben alle Kunden
		jpaWorkingOn.add(wcbNurErledigteAngebote, new GridBagConstraints(2, 5,
				2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuAngeboterledigungsgrund, new GridBagConstraints(5,
				5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAngeboterledigungsgrund, new GridBagConstraints(6,
				5, 4, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbSortierungVertreter, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbVertreterAlle, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbVertreterEiner, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(getPanelVertreter(), new GridBagConstraints(2, iZeile,
				4, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitDetails, new GridBagConstraints(0, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// this.setEinschraenkungDatumBelegnummerSichtbar(true);
	}

	private void setDefaults() {
		vertreterDto = new PersonalDto();
		// alle Vertreter
		wrbVertreterAlle.setSelected(true);
		wbuVertreter.setVisible(false);
		wtfVertreter.setVisible(false);
	}

	public String getModul() {
		return AngebotReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AngebotReportFac.REPORT_ANGEBOT_JOURNAL;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		ReportAngebotJournalKriterienDto kritDto = new ReportAngebotJournalKriterienDto();

		kritDto.befuelleBasiskrit(getKriterien());

		if (wrbVertreterAlle.isSelected()) {
			kritDto.vertreterIId = null;
		} else {
			kritDto.vertreterIId = vertreterDto.getIId();
		}

		if (wcbNurErledigteAngebote.isSelected()) {
			kritDto.setBNurErledigteAngebote(true);
		} else {
			kritDto.setBNurErledigteAngebote(false);
		}

		if (wcbMitDetails.isSelected()) {
			kritDto.setBMitDetails(true);
		} else {
			kritDto.setBMitDetails(false);
		}

		return DelegateFactory.getInstance().getAngebotReportDelegate()
				.printAngebotAlle(kritDto, erledigungsgrundCNr);
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	private JPanel getPanelVertreter() {
		if (jPanelVertreter == null) {
			jPanelVertreter = new JPanel(new GridBagLayout());

			jPanelVertreter.add(wbuVertreter, new GridBagConstraints(0, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanelVertreter.add(wtfVertreter, new GridBagConstraints(1, 0, 1,
					1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		return jPanelVertreter;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_EINER)) {
			// wenn noch keiner gewaehlt ist, dann geht der Dialog auf
			if (vertreterDto.getIId() == null) {
				wbuVertreter.doClick();
			}

			wbuVertreter.setVisible(true);
			wtfVertreter.setVisible(true);
			wtfVertreter.setMandatoryField(true);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_VERTRETER_AUSWAHL)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ANGEBOTERLEDIGUNGSGRUND)) {
			dialogQueryAngeboterledigungsgrund(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_ALLE)) {
			wbuVertreter.setVisible(false);
			wtfVertreter.setVisible(false);
			wtfVertreter.setMandatoryField(false);

			vertreterDto = new PersonalDto();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRVertreter) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					vertreterDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);

					wtfVertreter.setText(vertreterDto.getPartnerDto()
							.formatFixName2Name1());
				}
			} else if (e.getSource() == panelQueryFLRAngeboterledigungsgrund) {
				erledigungsgrundCNr = (String) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AngeboterledigungsgrundDto grundDto = DelegateFactory
						.getInstance()
						.getAngebotServiceDelegate()
						.angeboterledigungsgrundFindByPrimaryKey(
								erledigungsgrundCNr);
				wtfAngeboterledigungsgrund.setText(grundDto.getBezeichnung());
			}

		}
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, false,
						vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}

	public void dialogQueryAngeboterledigungsgrund(ActionEvent e)
			throws Throwable {
		panelQueryFLRAngeboterledigungsgrund = AngebotFilterFactory
				.getInstance().createPanelFLRAngeboterledigungsgrund(
						getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRAngeboterledigungsgrund);
	}

	protected ReportJournalKriterienDto getKriterien() {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbSortierungVertreter.isSelected()) {
			krit.iSortierung = ReportAngebotJournalKriterienDto.KRIT_SORT_NACH_VERTRETER;
		}
		return krit;
	}
}
