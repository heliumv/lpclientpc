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
package com.lp.client.rechnung;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version unbekannt Date $Date: 2010/05/19 15:12:54 $
 */
public class ReportWarenausgangsjournal
extends PanelReportJournalVerkauf implements PanelReportIfJRDS
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperCheckBox wcbGruppierungNachStatistikAdresse;
	private WrapperCheckBox wcbGutschriftenDrucken;
	
	private final static String ACTION_SPECIAL_SORTIERUNG_VERTRETER =
		"action_special_sortierung_vertreter";
	private final static String ACTION_SPECIAL_VERTRETER_AUSWAHL =
		"action_special_vertreter_auswahl";
	private final static String ACTION_SPECIAL_VERTRETER_ALLE =
		"action_special_vertreter_alle";
	private final static String ACTION_SPECIAL_VERTRETER_EINER =
		"action_special_vertreter_einer";
	
	private WrapperRadioButton wrbSortierungVertreter = new WrapperRadioButton();
	private WrapperButton wbuVertreter = null;
	private WrapperTextField wtfVertreter = null;
	private ButtonGroup jbgVertreter = new ButtonGroup();
	private WrapperRadioButton wrbVertreterAlle = new WrapperRadioButton();
	private WrapperRadioButton wrbVertreterEiner = new WrapperRadioButton();
	private WrapperCheckBox wcbMitTexeingaben; 
	private WrapperRadioButton wrbSortierungArtikel = new WrapperRadioButton();
	
	
	private JPanel jPanelVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;
	private PersonalDto vertreterDto = null;


	public ReportWarenausgangsjournal(InternalFrame internalFrame, String add2Title)
	throws Throwable {
		super(internalFrame, add2Title);
		wcbGruppierungNachStatistikAdresse = new WrapperCheckBox();
		wcbGutschriftenDrucken = new WrapperCheckBox();
		wcbMitTexeingaben = new WrapperCheckBox();
		jbInit();
		setDefaults();
	    initComponents();
	}


	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}
	
	private void jbInit()
    throws Exception {
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wtfVon.setMandatoryField(false);
		wtfBis.setMandatoryField(false);
		wrbSortierungVertreter = new WrapperRadioButton(LPMain.
				getTextRespectUISPr("label.vertreter"));
		wrbSortierungVertreter.setActionCommand(ACTION_SPECIAL_SORTIERUNG_VERTRETER);
		wrbSortierungVertreter.addActionListener(this);
		buttonGroupSortierung.add(wrbSortierungVertreter);

		wrbVertreterAlle.setText(LPMain.getTextRespectUISPr("label.alle"));
		wrbVertreterAlle.setActionCommand(ACTION_SPECIAL_VERTRETER_ALLE);
		wrbVertreterAlle.addActionListener(this);

		wrbVertreterEiner.setText(LPMain.getTextRespectUISPr("label.einer"));
		wrbVertreterEiner.setActionCommand(ACTION_SPECIAL_VERTRETER_EINER);
		wrbVertreterEiner.addActionListener(this);
		
		wrbSortierungArtikel.setText(LPMain.getTextRespectUISPr("lp.artikel"));
		buttonGroupSortierung.add(wrbSortierungArtikel);


		jbgVertreter = new ButtonGroup();
		jbgVertreter.add(wrbVertreterAlle);
		jbgVertreter.add(wrbVertreterEiner);

		wbuVertreter = new WrapperButton(LPMain.getTextRespectUISPr(
				"button.vertreter"));
		wbuVertreter.setToolTipText(LPMain.getTextRespectUISPr(
		"button.vertreter.tooltip"));
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_AUSWAHL);
		wbuVertreter.addActionListener(this);

		wbuVertreter.setMinimumSize(new Dimension(BREITE_BUTTONS,
				Defaults.getInstance().getControlHeight()));
		wbuVertreter.setPreferredSize(new Dimension(BREITE_BUTTONS,
				Defaults.getInstance().getControlHeight()));
		wtfVertreter = new WrapperTextField();
		wtfVertreter.setEditable(false);
		wtfVertreter.setMinimumSize(new Dimension(50,
				Defaults.getInstance().getControlHeight()));
		wtfVertreter.setPreferredSize(new Dimension(50,
				Defaults.getInstance().getControlHeight()));

		iZeile++;
		jpaWorkingOn.add(wrbSortierungVertreter,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wrbVertreterAlle,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbVertreterEiner,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(getPanelVertreter(),
				new GridBagConstraints(2, iZeile, 4, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortierungArtikel,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		
		wcbGruppierungNachStatistikAdresse.setText(LPMain.getTextRespectUISPr(
        "warenausgang.gruppierung.nachstatistikadresse"));
		wcbGruppierungNachStatistikAdresse.setEnabled(true);
		iZeile++;
		jpaWorkingOn.add(wcbGruppierungNachStatistikAdresse,
                new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                                       GridBagConstraints.CENTER,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		wcbGutschriftenDrucken.setText(LPMain.getTextRespectUISPr(
        "warenausgang.gutschriften.drucken"));
		wcbGutschriftenDrucken.setEnabled(true);
		wcbGutschriftenDrucken.setSelected(true);
		jpaWorkingOn.add(wcbGutschriftenDrucken,
				new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		wcbMitTexeingaben.setText(LPMain.getTextRespectUISPr(
        "part.kunde.lieferstatistik.mittexteingaben"));
		wcbMitTexeingaben.setEnabled(true);
		jpaWorkingOn.add(wcbMitTexeingaben,
				new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

	}


	public String getReportname() {
	    return RechnungReportFac.REPORT_WARENAUSGANGSJOURNAL;
	}


	public boolean getBErstelleReportSofort() {
		return false;
	}


	public MailtextDto getMailtextDto()
	throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}


	protected ReportRechnungJournalKriterienDto getKriterien() {
		ReportRechnungJournalKriterienDto krit = new ReportRechnungJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbVertreterEiner.isSelected()) {
			krit.vertreterIId = vertreterDto.getIId();
		}
		if (wrbSortierungVertreter.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER;
		}
		if (wrbSortierungArtikel.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_IDENT;
		}
		krit.setBVerwendeStatistikAdresse(wcbGruppierungNachStatistikAdresse.isSelected());
		krit.setBGutschriftenBeruecksichtigen(wcbGutschriftenDrucken.isSelected());
		krit.setBMitTexteingaben(wcbMitTexeingaben.isSelected());
		return krit;
	}
	

	private void setDefaults() {
		vertreterDto = new PersonalDto();

		// alle Vertreter
		wrbVertreterAlle.setSelected(true);
		wbuVertreter.setVisible(false);
		wtfVertreter.setVisible(false);
		wcbGutschriftenDrucken.setSelected(true);
	}
	
	protected void eventActionSpecial(ActionEvent e)
	throws Throwable {
		super.eventActionSpecial(e);
		if(wdfVon.isVisible() && wdfBis.isVisible()){
			wdfVon.setMandatoryField(true);
			wdfBis.setMandatoryField(true);
		} else {
			wdfVon.setMandatoryField(false);
			wdfBis.setMandatoryField(false);
		}
		if(wtfVon.isVisible() &&wtfBis.isVisible()){
			wtfVon.setMandatoryField(true);
			wtfBis.setMandatoryField(true);
		} else {
			wtfVon.setMandatoryField(false);
			wtfBis.setMandatoryField(false);
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_EINER)) {
			// wenn noch keiner gewaehlt ist, dann geht der Dialog auf
			if (vertreterDto.getIId() == null) {
				wbuVertreter.doClick();
			}

			wbuVertreter.setVisible(true);
			wtfVertreter.setVisible(true);
			wtfVertreter.setMandatoryField(true);
		}
		else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_AUSWAHL)) {
			dialogQueryVertreter(e);
		}
		else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_ALLE)) {
			wbuVertreter.setVisible(false);
			wtfVertreter.setVisible(false);
			wtfVertreter.setMandatoryField(false);

			vertreterDto = new PersonalDto();
		}
	}
	
	protected void eventItemchanged(EventObject eI)
	throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRVertreter) {

				Object key = ( (ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().
					personalFindByPrimaryKey( (Integer) key);
					getKriterien().vertreterIId = (Integer) key;

					wtfVertreter.setText(vertreterDto.getPartnerDto().formatFixName2Name1());
				}
			}
		}
	}
	
	private void dialogQueryVertreter(ActionEvent e)
	throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
				getInternalFrame(),
				true,
				false,
				vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}


	public JasperPrintLP getReport(String sDrucktype)
	throws Throwable {
		return DelegateFactory.getInstance().getRechnungDelegate().printWarenausgangsjournal(getKriterien());
	}
	
	private JPanel getPanelVertreter() {
		if (jPanelVertreter == null) {
			jPanelVertreter = new JPanel(new GridBagLayout());

			jPanelVertreter.add(wbuVertreter,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.WEST,
							GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2),
							0, 0));
			jPanelVertreter.add(wtfVertreter,
					new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
							GridBagConstraints.WEST,
							GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2),
							0, 0));
		}

		return jPanelVertreter;
	}
}
