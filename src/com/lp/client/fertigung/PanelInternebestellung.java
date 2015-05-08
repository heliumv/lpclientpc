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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * Panel zum Bearbeiten der Internen Bestellung
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>03. 12. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public class PanelInternebestellung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneInternebestellung tabbedPaneInternebestellung = null;

	private AuftragDto auftragDto = null;
	private AuftragpositionDto auftragpositionDto = null;

	private static final String ACTION_SPECIAL_AUFTRAG = "action_special_los_auftrag";
	private static final String ACTION_SPECIAL_AUFTRAGPOSITION = "action_special_los_auftragposition";
	private static final String ACTION_SPECIAL_STUECKLISTE = "action_special_los_stueckliste";

	private PanelQueryFLR panelQueryFLRAuftrag = null;
	private PanelQueryFLR panelQueryFLRAuftragposition = null;
	private PanelQueryFLR panelQueryFLRStueckliste = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanelWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private WrapperTextField wtfAuftragNummer = new WrapperTextField();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftragBezeichnung = new WrapperTextField();
	private WrapperButton wbuAuftragposition = new WrapperButton();
	private WrapperNumberField wtfAuftragpositionNummer = new WrapperNumberField();
	private WrapperTextField wtfAuftragpositionBezeichnung = new WrapperTextField();

	private WrapperLabel wlaKunde = new WrapperLabel();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfAdresse = new WrapperTextField();
	private WrapperLabel wlaAbteilung = new WrapperLabel();
	private WrapperTextField wtfAbteilung = new WrapperTextField();

	private WrapperGotoButton wbuStueckliste = new WrapperGotoButton(
			WrapperGotoButton.GOTO_STUECKLISTE_DETAIL);
	private WrapperTextField wtfStuecklisteNummer = new WrapperTextField();
	private WrapperTextField wtfStuecklisteBezeichnung = new WrapperTextField();

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	private WrapperNumberField wnfLosgroesse = null;
	private WrapperLabel wlaEinheit = new WrapperLabel();

	private WrapperLabel wlaProduktionsbeginn = new WrapperLabel();
	private WrapperDateField wdfProduktionsbeginn = new WrapperDateField();

	private WrapperLabel wlaProduktionsende = new WrapperLabel();
	private WrapperDateField wdfProduktionsende = new WrapperDateField();

	private WrapperLabel wlaDauer = new WrapperLabel();
	private WrapperNumberField wnfDauer = new WrapperNumberField();
	private WrapperLabel wlaTage = new WrapperLabel();

	public PanelInternebestellung(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneInternebestellung tabbedPaneInternebestellung)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneInternebestellung = tabbedPaneInternebestellung;
		jbInit();
		initComponents();
	}

	private TabbedPaneInternebestellung getTabbedPaneInternebestellung() {
		return tabbedPaneInternebestellung;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		this.setLayout(gridBagLayout1);
		wlaLosgroesse.setText(LPMain.getTextRespectUISPr("label.losgroesse"));
		wlaProduktionsbeginn.setText(LPMain.getTextRespectUISPr("lp.beginn"));
		wlaProduktionsende.setText(LPMain.getTextRespectUISPr("lp.ende"));
		wlaDauer.setText(LPMain.getTextRespectUISPr("lp.dauer"));
		wlaTage.setText(LPMain.getTextRespectUISPr("lp.tage"));
		wlaTage.setHorizontalAlignment(SwingConstants.LEFT);
		wlaAbteilung.setText(LPMain.getTextRespectUISPr("lp.abteilung"));
		wtfAdresse.setActivatable(false);
		wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAuftragpositionNummer.setFractionDigits(0);
		wtfAuftragpositionNummer.setMaximumIntegerDigits(4);
		wtfAuftragpositionNummer.setActivatable(false);
		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain
				.getTextRespectUISPr("button.auftrag.tooltip"));
		wbuAuftragposition.setText(LPMain.getTextRespectUISPr("lp.position"));
		wbuAuftragposition.setToolTipText(LPMain
				.getTextRespectUISPr("lp.position"));
		wlaKunde.setText(LPMain.getTextRespectUISPr("label.kunde"));
		wbuStueckliste
				.setText(LPMain.getTextRespectUISPr("button.stueckliste"));
		wbuStueckliste.setToolTipText(LPMain
				.getTextRespectUISPr("button.stueckliste.tooltip"));
		jPanelWorkingOn.setBorder(border1);
		jPanelWorkingOn.setLayout(gridBagLayout3);

		wtfKunde.setActivatable(false);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wtfAuftragNummer.setActivatable(false);
		wtfAuftragBezeichnung.setActivatable(false);
		wtfAuftragBezeichnung
				.setColumnsMax(AuftragFac.MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG);
		wtfAuftragpositionBezeichnung.setActivatable(false);
		wtfStuecklisteBezeichnung.setActivatable(false);
		wtfStuecklisteNummer.setActivatable(false);

		wbuAuftrag.addActionListener(this);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
		wbuAuftragposition.addActionListener(this);
		wbuAuftragposition.setActionCommand(ACTION_SPECIAL_AUFTRAGPOSITION);
		wbuStueckliste.addActionListener(this);
		wbuStueckliste.setActionCommand(ACTION_SPECIAL_STUECKLISTE);

		wdfProduktionsbeginn.getDisplay().addFocusListener(
				new PanelInternebestellung_focusAdapter(this));
		wdfProduktionsende.getDisplay().addFocusListener(
				new PanelInternebestellung_focusAdapter(this));
		wnfDauer.addFocusListener(new PanelInternebestellung_focusAdapter(this));

		wnfLosgroesse = new WrapperNumberField();
		wnfLosgroesse.setMinimumValue(1);
		wnfLosgroesse.setFractionDigits(0);
		wnfLosgroesse.setMandatoryFieldDB(true);
		wtfAbteilung.setActivatable(false);

		wtfStuecklisteNummer.setMandatoryField(true);
		wdfProduktionsbeginn.setMandatoryField(true);
		wdfProduktionsende.setMandatoryField(true);
		//wdfProduktionsbeginn.setActivatable(false);
		wnfDauer.setMandatoryField(true);
		wnfDauer.setMinimumValue(0);
		wnfDauer.setFractionDigits(0);
		wnfDauer.setMaximumIntegerDigits(3);
		//wnfDauer.setActivatable(false);

		wlaLosgroesse.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wlaLosgroesse.setPreferredSize(new Dimension(120, Defaults
				.getInstance().getControlHeight()));
		wnfLosgroesse.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfLosgroesse.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit.setMinimumSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit.setPreferredSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));

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
		jPanelWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAuftragNummer, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAuftragBezeichnung, new GridBagConstraints(2,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuAuftragposition, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAuftragpositionNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAuftragpositionBezeichnung,
				new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKunde, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wtfAdresse, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAbteilung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAbteilung, new GridBagConstraints(1, iZeile, 4,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuStueckliste, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfStuecklisteNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfStuecklisteBezeichnung, new GridBagConstraints(
				2, iZeile, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfLosgroesse, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaProduktionsbeginn, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfProduktionsbeginn, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaDauer, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfDauer, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 20, 0));
		jPanelWorkingOn.add(wlaTage, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaProduktionsende, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfProduktionsende, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			InternebestellungDto ibDto = getTabbedPaneInternebestellung()
					.getInternebestellungDto();
			// wenn alles passt, wird gespeichert
			if (ibDto != null) {
				// speichern
				InternebestellungDto savedDto = DelegateFactory.getInstance()
						.getFertigungDelegate().updateInternebestellung(ibDto);
				// falls neue Rechnung, titel setzen
				if (ibDto.getIId() == null) {
					this.setKeyWhenDetailPanel(savedDto.getIId());
				}
				getTabbedPaneInternebestellung().setInternebestellungDto(
						savedDto);
				// das Panel aktualisieren
				dto2Components();
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// den bestehenden Dto verwenden
		InternebestellungDto dto = getTabbedPaneInternebestellung()
				.getInternebestellungDto();
		if (dto == null) {
			dto = new InternebestellungDto();
			dto.setMandantCNr(LPMain.getTheClient().getMandant());
		}
		// Auftragszuordnung
		if (auftragDto != null) {
			dto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
			dto.setIBelegiid(auftragDto.getIId());
			if (auftragpositionDto != null) {
				dto.setIBelegpositionIId(auftragpositionDto.getIId());
			} else {
				dto.setIBelegpositionIId(null);
			}
		} else {
			dto.setBelegartCNr(null);
			dto.setIBelegiid(null);
			dto.setIBelegpositionIId(null);
		}

		dto.setNMenge(wnfLosgroesse.getBigDecimal());
		if (getTabbedPaneInternebestellung().getStuecklisteDto() != null) {
			dto.setStuecklisteIId(getTabbedPaneInternebestellung()
					.getStuecklisteDto().getIId());
		} else {
			dto.setStuecklisteIId(null);
		}
		dto.setTLiefertermin(wdfProduktionsende.getDate());
		dto.setTProduktionsbeginn(wdfProduktionsbeginn.getDate());

		getTabbedPaneInternebestellung().setInternebestellungDto(dto);
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		InternebestellungDto ibDto = getTabbedPaneInternebestellung()
				.getInternebestellungDto();
		if (ibDto.getBelegartCNr() != null
				&& ibDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
			holeAuftrag(ibDto.getIBelegiid());
			holeAuftragposition(ibDto.getIBelegpositionIId());
		} else {
			holeAuftrag(null);
			holeAuftragposition(null);
		}
		holeStueckliste(ibDto.getStuecklisteIId());
		wnfLosgroesse.setBigDecimal(ibDto.getNMenge());
		wdfProduktionsende.setDate(ibDto.getTLiefertermin());
		wdfProduktionsbeginn.setDate(ibDto.getTProduktionsbeginn());

		wnfDauer.setInteger(Helper.getDifferenzInTagen(
				ibDto.getTProduktionsbeginn(), ibDto.getTLiefertermin()));

		this.setStatusbarPersonalIIdAendern(ibDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(ibDto.getTAendern());
	}

	private void dto2ComponentsAuftrag() throws Throwable {
		if (auftragDto != null) {
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragBezeichnung.setText(auftragDto
					.getCBezProjektbezeichnung());
			KundeDto kundeDto = DelegateFactory
					.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(
							auftragDto.getKundeIIdAuftragsadresse());
			this.wtfKunde.setText(kundeDto.getPartnerDto()
					.formatFixTitelName1Name2());
			if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
				this.wtfAdresse.setText(kundeDto.getPartnerDto()
						.getLandplzortDto().formatLandPlzOrt());
			} else {
				this.wtfAdresse.setText(null);
			}
			this.wtfAbteilung.setText(kundeDto.getPartnerDto()
					.getCName3vorname2abteilung());
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragBezeichnung.setText(null);
			wtfKunde.setText(null);
			wtfAdresse.setText(null);
			wtfAbteilung.setText(null);
		}
	}

	private void dto2ComponentsAuftragposition() {
		if (auftragpositionDto != null) {
			wtfAuftragpositionNummer.setInteger(auftragpositionDto.getISort());
			wtfAuftragpositionBezeichnung.setText(auftragpositionDto.getCBez());
		} else {
			wtfAuftragpositionNummer.setInteger(null);
			wtfAuftragpositionBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsStueckliste() throws Throwable {
		if (getTabbedPaneInternebestellung().getStuecklisteDto() != null) {
			StuecklisteDto stuecklisteDto = getTabbedPaneInternebestellung()
					.getStuecklisteDto();
			wbuStueckliste.setOKey(stuecklisteDto.getIId());
			wtfStuecklisteNummer.setText(stuecklisteDto.getArtikelDto()
					.getCNr());
			wtfStuecklisteBezeichnung.setText(stuecklisteDto.getArtikelDto()
					.getArtikelsprDto().getCBez());
			wlaEinheit.setText(stuecklisteDto.getArtikelDto().getEinheitCNr());
			// Durchlaufzeit aus der Stueckliste
			if (stuecklisteDto.getNDefaultdurchlaufzeit() != null) {
				wnfDauer.setInteger(stuecklisteDto.getNDefaultdurchlaufzeit()
						.intValue());
			} else {
				// Allgemeine standarddurchlaufzeit
				ParametermandantDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient().getMandant(),
								ParameterFac.KATEGORIE_FERTIGUNG,
								ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
				int iStandarddurchlaufzeit = ((Integer) parameter
						.getCWertAsObject()).intValue();
				wnfDauer.setInteger(iStandarddurchlaufzeit);
			}
		} else {
			wbuStueckliste.setOKey(null);
			wtfStuecklisteNummer.setText(null);
			wtfStuecklisteBezeichnung.setText(null);
			wlaEinheit.setText("");
			wnfDauer.setBigDecimal(null);
		}
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getTabbedPaneInternebestellung().setInternebestellungDto(null);
		this.auftragDto = null;
		this.leereAlleFelder(this);
		wlaEinheit.setText("");
		wnfLosgroesse.setInteger(new Integer(1));
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
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
		InternebestellungDto ibDto = getTabbedPaneInternebestellung()
				.getInternebestellungDto();
		if (ibDto != null) {
			if (ibDto.getIId() != null) {
				if (!isLockedDlg()) {
					Object[] o = getTabbedPaneInternebestellung()
							.getPanelQueryInternebestellung(true)
							.getSelectedIds();
					if (o != null) {
						for (int i = 0; i < o.length; i++) {
							InternebestellungDto toRemove = new InternebestellungDto();
							toRemove.setIId((Integer) o[i]);

							DelegateFactory.getInstance()
									.getFertigungDelegate()
									.removeInternebestellung(toRemove);
						}
					}
					getTabbedPaneInternebestellung().setInternebestellungDto(
							null);
					this.leereAlleFelder(this);
					this.setKeyWhenDetailPanel(null);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_INTERNEBESTELLUNG;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAGPOSITION)) {
			if (auftragDto == null) {
				// zuerst den auftrag
				dialogQueryAuftrag(e);
			} else {
				dialogQueryAuftragposition(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STUECKLISTE)) {
			dialogQueryStueckliste(e);
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
			if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftrag(key);
			} else if (e.getSource() == panelQueryFLRAuftragposition) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeAuftragposition(key);
			} else if (e.getSource() == panelQueryFLRStueckliste) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeStueckliste(key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				holeAuftrag(null);
				holeAuftragposition(null);
			} else if (e.getSource() == panelQueryFLRAuftragposition) {
				holeAuftragposition(null);
			}
		}
	}

	private void holeAuftrag(Object key) throws Throwable {
		if (key != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey((Integer) key);
			// falls die gewaehlte Auftragsposition nicht zu diesem Auftrag
			// gehoert - loeschen
			if (auftragpositionDto != null
					&& !auftragpositionDto.getBelegIId().equals(
							auftragDto.getIId())) {
				holeAuftragposition(null);
			}
		} else {
			auftragDto = null;
			// keine Position ohne Auftrag
			holeAuftragposition(null);
		}
		dto2ComponentsAuftrag();
	}

	private void holeAuftragposition(Object key) throws Throwable {
		if (key != null) {
			auftragpositionDto = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey((Integer) key);
		} else {
			auftragpositionDto = null;
		}
		dto2ComponentsAuftragposition();
	}

	private void holeStueckliste(Object key) throws Throwable {
		if (key != null) {
			if (getTabbedPaneInternebestellung().getStuecklisteDto() == null
					|| !getTabbedPaneInternebestellung().getStuecklisteDto()
							.getIId().equals(key)) {
				getTabbedPaneInternebestellung().setStuecklisteDto(
						DelegateFactory.getInstance().getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey((Integer) key));
			}
		} else {
			getTabbedPaneInternebestellung().setStuecklisteDto(null);
		}
		dto2ComponentsStueckliste();
	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAuftrag(ActionEvent e) throws Throwable {
		// FilterKriterium[] fk =
		// SystemFilterFactory.getInstance().createFKMandantcnr();
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, true, null);
		if (auftragDto != null) {
			panelQueryFLRAuftrag.setSelectedId(auftragDto.getIId());
		}
		new DialogQuery(panelQueryFLRAuftrag);
	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryAuftragposition(ActionEvent e) throws Throwable {
		FilterKriterium[] filtersPositionen = AuftragFilterFactory
				.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };

		panelQueryFLRAuftragposition = new PanelQueryFLR(null,
				filtersPositionen, QueryParameters.UC_ID_AUFTRAGPOSITION,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("title.auftragauswahlliste"));
		if (auftragpositionDto != null) {
			panelQueryFLRAuftragposition.setSelectedId(auftragpositionDto
					.getIId());
		}
		new DialogQuery(panelQueryFLRAuftragposition);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			InternebestellungDto ibDto = getTabbedPaneInternebestellung()
					.getInternebestellungDto();
			if (ibDto != null) {
				// neu Laden.
				ibDto = DelegateFactory.getInstance().getFertigungDelegate()
						.internebestellungFindByPrimaryKey(ibDto.getIId());
				getTabbedPaneInternebestellung().setInternebestellungDto(ibDto);
				dto2Components();
			} else {
				this.leereAlleFelder(this);
			}
		}
	}

	public void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuStueckliste;
	}

	private void dialogQueryStueckliste(ActionEvent e) throws Throwable {
		Integer selectedId = null;
		if (getTabbedPaneInternebestellung().getStuecklisteDto() != null) {
			selectedId = getTabbedPaneInternebestellung().getStuecklisteDto()
					.getIId();
		}
		panelQueryFLRStueckliste = StuecklisteFilterFactory.getInstance()
				.createPanelFLRStueckliste(getInternalFrame(), selectedId,
						false);
		new DialogQuery(panelQueryFLRStueckliste);
	}

	protected void focusLostPI(FocusEvent e) throws Throwable {
		if (e.getSource() == wnfDauer
				|| e.getSource() == wdfProduktionsbeginn.getDisplay()) {
			Date dEnde = wdfProduktionsende.getDate();
			if (wdfProduktionsbeginn.getDate() != null
					&& wnfDauer.getInteger() != null) {
				dEnde = Helper.addiereTageZuDatum(wdfProduktionsbeginn
						.getDate(), wnfDauer.getInteger().intValue());
				wdfProduktionsende.setMinimumValue(wdfProduktionsbeginn
						.getDate());
			} else {
				wdfProduktionsende.setMinimumValue(null);
			}
			wdfProduktionsende.setDate(dEnde);
		} else if (e.getSource() == wdfProduktionsende.getDisplay()) {
			Integer iDauer = wnfDauer.getInteger();
			if (wdfProduktionsende.getDate() != null
					&& wnfDauer.getInteger() != null) {
				Date dBeginn = Helper.addiereTageZuDatum(
						wdfProduktionsende.getDate(), -wnfDauer.getInteger());
				wdfProduktionsbeginn.setMaximumValue(wdfProduktionsende
						.getDate());
				wdfProduktionsbeginn.setDate(dBeginn);
			} else {
				wdfProduktionsbeginn.setMaximumValue(null);
			}
			wnfDauer.setInteger(iDauer);
		}
	}
}

class PanelInternebestellung_focusAdapter implements
		java.awt.event.FocusListener {
	private PanelInternebestellung adaptee;

	PanelInternebestellung_focusAdapter(PanelInternebestellung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.focusLostPI(e);
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
