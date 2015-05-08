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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.artikel.PanelArtikelgruppen;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZahlungszielsprDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelZahlungsziel extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameSystem internalFrameSystem = null;
	private ZahlungszielDto zahlungszielDto = null;

	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaTageNetto = new WrapperLabel();
	private WrapperNumberField wnfTageNetto = new WrapperNumberField();
	private WrapperLabel wlaSkontoTage1 = new WrapperLabel();
	private WrapperNumberField wnfSkontoTage1 = new WrapperNumberField();
	private WrapperLabel wlaSkonto1 = new WrapperLabel();
	private WrapperNumberField wnfSkonto1 = new WrapperNumberField();
	private WrapperLabel wlaSkontoTage2 = new WrapperLabel();
	private WrapperNumberField wnfSkontoTage2 = new WrapperNumberField();
	private WrapperLabel wlaSkonto2 = new WrapperLabel();
	private WrapperNumberField wnfSkonto2 = new WrapperNumberField();

	private WrapperRadioButton wrbStichtag = new WrapperRadioButton();
	private WrapperRadioButton wrbLaufzeit = new WrapperRadioButton();

	private WrapperLabel wlaFaelligkeitstag = new WrapperLabel();
	private WrapperNumberField wnfFaelligkeitstag = new WrapperNumberField();
	private WrapperLabel wlaFaelligkeitsmonat = new WrapperLabel();
	private WrapperNumberField wnfFaelligkeitsmonat = new WrapperNumberField();
	private WrapperCheckBox wcbStichtagMonatsletzter = new WrapperCheckBox();

	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperCheckBox wcbInZahlungsvorschlagBeruecksichtigen = new WrapperCheckBox();

	public PanelZahlungsziel(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameSystem = (InternalFrameSystem) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	protected void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zahlungszielDto = new ZahlungszielDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	public void aktualisiereKomponenten() {
		wnfFaelligkeitstag.setMandatoryField(false);

		if (wrbLaufzeit.isSelected()) {
			wnfTageNetto.setVisible(true);
			wnfTageNetto.setMandatoryField(true);

			wnfFaelligkeitsmonat.setMandatoryField(false);
			wnfFaelligkeitsmonat.setVisible(false);
			wnfFaelligkeitstag.setEditable(false);
			wnfFaelligkeitstag.setVisible(false);
			wcbStichtagMonatsletzter.setVisible(false);
			wlaFaelligkeitsmonat.setVisible(false);
			wlaFaelligkeitstag.setVisible(false);

		} else {

			wlaFaelligkeitsmonat.setVisible(true);
			wlaFaelligkeitstag.setVisible(true);
			wnfFaelligkeitsmonat.setVisible(true);
			wnfFaelligkeitstag.setVisible(true);
			wcbStichtagMonatsletzter.setVisible(true);

			wnfTageNetto.setMandatoryField(false);
			wnfTageNetto.setVisible(false);
			wnfFaelligkeitsmonat.setMandatoryField(true);

			if (wcbStichtagMonatsletzter.isSelected() == false) {
				wnfFaelligkeitstag.setMandatoryField(true);
				wnfFaelligkeitstag.setEditable(true);
			} else {

			}

		}
		jpaWorkingOn.repaint();
		LPMain.getInstance().getDesktop().repaint();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wrbLaufzeit)
				|| e.getSource().equals(wrbStichtag)) {
			aktualisiereKomponenten();

		}
		if (e.getSource().equals(wcbStichtagMonatsletzter)) {
			if (wcbStichtagMonatsletzter.isSelected()) {
				wnfFaelligkeitstag.setInteger(null);

			}
			aktualisiereKomponenten();
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMandantDelegate()
				.removeZahlungsziel(zahlungszielDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {

		if (zahlungszielDto.getZahlungszielsprDto() == null) {
			zahlungszielDto.setZahlungszielsprDto(new ZahlungszielsprDto());
		}
		zahlungszielDto.getZahlungszielsprDto().setCBezeichnung(
				wtfBezeichnung.getText());
		zahlungszielDto.getZahlungszielsprDto().setLocaleCNr(
				LPMain.getTheClient().getLocUiAsString());

		zahlungszielDto.setAnzahlZieltageFuerNetto(wnfTageNetto.getInteger());
		zahlungszielDto
				.setBInzahlungsvorschlagberuecksichtigen(wcbInZahlungsvorschlagBeruecksichtigen
						.getShort());
		zahlungszielDto.setBVersteckt(wcbVersteckt.getShort());
		zahlungszielDto.setCBez(wtfKennung.getText());
		zahlungszielDto.setSkontoAnzahlTage1(wnfSkontoTage1.getInteger());
		zahlungszielDto.setSkontoAnzahlTage2(wnfSkontoTage2.getInteger());
		zahlungszielDto.setSkontoProzentsatz1(wnfSkonto1.getBigDecimal());
		zahlungszielDto.setSkontoProzentsatz2(wnfSkonto2.getBigDecimal());

		zahlungszielDto.setBStichtag(wrbStichtag.getShort());

		zahlungszielDto.setIStichtag(wnfFaelligkeitstag.getInteger());
		zahlungszielDto.setIFolgemonat(wnfFaelligkeitsmonat.getInteger());

		zahlungszielDto.setBStichtagMonatsletzter(wcbStichtagMonatsletzter
				.getShort());

		if (wcbStichtagMonatsletzter.isSelected()) {
			zahlungszielDto.setIStichtag(null);
		} else {
			zahlungszielDto.setIStichtag(wnfFaelligkeitstag.getInteger());
		}

	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(zahlungszielDto.getCBez());
		wnfTageNetto.setInteger(zahlungszielDto.getAnzahlZieltageFuerNetto());
		wcbInZahlungsvorschlagBeruecksichtigen.setShort(zahlungszielDto
				.getBInzahlungsvorschlagberuecksichtigen());
		wcbVersteckt.setShort(zahlungszielDto.getBVersteckt());

		if (zahlungszielDto.getZahlungszielsprDto() != null) {
			wtfBezeichnung.setText(zahlungszielDto.getZahlungszielsprDto()
					.getCBezeichnung());
		}

		wnfTageNetto.setInteger(zahlungszielDto.getAnzahlZieltageFuerNetto());
		wnfSkontoTage1.setInteger(zahlungszielDto.getSkontoAnzahlTage1());
		wnfSkontoTage2.setInteger(zahlungszielDto.getSkontoAnzahlTage2());
		wnfSkonto1.setBigDecimal(zahlungszielDto.getSkontoProzentsatz1());
		wnfSkonto2.setBigDecimal(zahlungszielDto.getSkontoProzentsatz2());

		if (Helper.short2boolean(zahlungszielDto.getBStichtag())) {
			wrbStichtag.setSelected(true);
		} else {
			wrbLaufzeit.setSelected(true);
		}

		wnfFaelligkeitsmonat.setInteger(zahlungszielDto.getIFolgemonat());

		wcbStichtagMonatsletzter.setShort(zahlungszielDto
				.getBStichtagMonatsletzter());
		if (Helper.short2boolean(zahlungszielDto.getBStichtagMonatsletzter())) {
			wnfFaelligkeitstag.setInteger(null);
		} else {
			wnfFaelligkeitstag.setInteger(zahlungszielDto.getIStichtag());
		}

		aktualisiereKomponenten();

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (zahlungszielDto.getIId() == null) {
				zahlungszielDto.setIId(DelegateFactory.getInstance()
						.getMandantDelegate()
						.createZahlungsziel(zahlungszielDto));
				setKeyWhenDetailPanel(zahlungszielDto.getIId());

			} else {
				DelegateFactory.getInstance().getMandantDelegate()
						.updateZahlungsziel(zahlungszielDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						zahlungszielDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.bezeichnung_lang"));
		wlaTageNetto.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.skonto.tagenetto"));
		wlaSkontoTage1.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.skonto.tage1"));
		wlaSkontoTage2.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.skonto.tage2"));
		wlaSkonto1.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.skonto.prozentsatz1"));
		wlaSkonto2.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.skonto.prozentsatz2"));

		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckt"));
		wcbInZahlungsvorschlagBeruecksichtigen.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"lp.zahlungsziel.inzahlungsvorschlagberuecksichtigen"));

		wlaFaelligkeitstag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zahlungsziel.faelligkeitstag"));
		wlaFaelligkeitsmonat.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zahlungsziel.faelligkeitsfolgemonat"));
		wcbStichtagMonatsletzter.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.zahlungsziel.monatsletzter"));
		wnfFaelligkeitstag.setFractionDigits(0);
		wnfFaelligkeitstag.setMaximumValue(28);

		wnfFaelligkeitsmonat.setFractionDigits(0);
		wnfFaelligkeitsmonat.setMaximumValue(99);

		wtfKennung.setMandatoryField(true);

		wtfBezeichnung.setColumnsMax(120);
		wnfTageNetto.setMandatoryField(true);
		wnfTageNetto.setMaximumValue(0);
		wnfTageNetto.setMaximumValue(999);

		wnfSkontoTage1.setMaximumValue(0);
		wnfSkontoTage1.setMaximumValue(999);
		wnfSkontoTage2.setMaximumValue(0);
		wnfSkontoTage2.setMaximumValue(999);

		wnfSkonto1.setMaximumValue(0);
		wnfSkonto1.setMaximumValue(99);
		wnfSkonto2.setMaximumValue(0);
		wnfSkonto2.setMaximumValue(99);

		wrbLaufzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zahlungsziel.art.laufzeit"));
		wrbStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zahlungsziel.art.stichtag"));
		wrbLaufzeit.setSelected(true);
		buttonGroup.add(wrbLaufzeit);
		buttonGroup.add(wrbStichtag);

		wrbLaufzeit.addActionListener(this);
		wrbStichtag.addActionListener(this);
		wcbStichtagMonatsletzter.addActionListener(this);

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

		iZeile++;

		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1,
				0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 5, 1,
				0.10, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 5,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbLaufzeit, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbStichtag, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));

		iZeile++;

		jpaWorkingOn.add(wlaTageNetto, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfTageNetto, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFaelligkeitstag, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));
		jpaWorkingOn.add(wnfFaelligkeitstag, new GridBagConstraints(5, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSkontoTage1, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSkontoTage1, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSkonto1, new GridBagConstraints(2, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfSkonto1, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -80, 0));

		jpaWorkingOn.add(wcbStichtagMonatsletzter, new GridBagConstraints(4,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSkontoTage2, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSkontoTage2, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSkonto2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSkonto2, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 80), -80, 0));

		jpaWorkingOn.add(wlaFaelligkeitsmonat, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaelligkeitsmonat, new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbInZahlungsvorschlagBeruecksichtigen,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
			wcbInZahlungsvorschlagBeruecksichtigen.setSelected(true);
		} else {
			zahlungszielDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
