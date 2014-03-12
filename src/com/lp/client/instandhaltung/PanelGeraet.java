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
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.instandhaltung.service.AnlageDto;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.HalleDto;
import com.lp.server.instandhaltung.service.IsmaschineDto;
import com.lp.util.Helper;

public class PanelGeraet extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameInstandhaltung internalFrameInstandhaltung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperLabel wlaGeraeteposition = new WrapperLabel();
	private WrapperTextField wtfGeraeteposition = new WrapperTextField();

	private WrapperLabel wlaFabrikat = new WrapperLabel();
	private WrapperTextField wtfFabrikat = new WrapperTextField();

	private WrapperLabel wlaGeraetesnr = new WrapperLabel();
	private WrapperTextField wtfGeraetesnr = new WrapperTextField();

	private WrapperLabel wlaVersorgungskreis = new WrapperLabel();
	private WrapperTextField wtfVersorgungskreis = new WrapperTextField();

	private WrapperLabel wlaGrenzwertMax = new WrapperLabel();
	private WrapperNumberField wnfGrenzwertMax = new WrapperNumberField();

	private WrapperLabel wlaLeistung = new WrapperLabel();
	private WrapperTextField wtfLeistung = new WrapperTextField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperEditorField wtfBemerkung = new WrapperEditorFieldTexteingabe(
			getInternalFrame(), "");

	private WrapperLabel wlaGrenzwertMin = new WrapperLabel();
	private WrapperNumberField wnfGrenzwertMin = new WrapperNumberField();

	private WrapperLabel wlaAnzahl = new WrapperLabel();
	private WrapperNumberField wnfAnzahl = new WrapperNumberField();

	private WrapperLabel wlaGrenzwert = new WrapperLabel();
	private WrapperNumberField wnfGrenzwert = new WrapperNumberField();

	private WrapperLabel wlaWartung = new WrapperLabel();
	private WrapperRadioButton wrbNachAufwand = new WrapperRadioButton();
	private WrapperRadioButton wrbPauschal = new WrapperRadioButton();
	private ButtonGroup buttonGroup1 = new ButtonGroup();

	private WrapperRadioButton wrbGrenzwertAbsolut = new WrapperRadioButton();
	private WrapperRadioButton wrbGrenzwertKummulierend = new WrapperRadioButton();
	private ButtonGroup buttonGroupGrenzwert = new ButtonGroup();

	static final private String ACTION_SPECIAL_FLR_HALLE = "action_special_flr_halle";
	static final private String ACTION_SPECIAL_FLR_ANLAGE = "action_special_flr_anlage";
	static final private String ACTION_SPECIAL_FLR_MASCHINE = "action_special_flr_maschine";

	protected PanelQueryFLR panelQueryFLRHalle = null;
	protected PanelQueryFLR panelQueryFLRAnlage = null;
	protected PanelQueryFLR panelQueryFLRMaschine = null;

	protected WrapperButton wbuHalle = new WrapperButton();
	protected WrapperTextField wtfHalle = new WrapperTextField();

	protected WrapperButton wbuAnlage = new WrapperButton();
	protected WrapperTextField wtfAnlage = new WrapperTextField();

	protected WrapperButton wbuMaschine = new WrapperButton();
	protected WrapperTextField wtfMaschine = new WrapperTextField();

	private WrapperSelectField wsfGeraetetyp = new WrapperSelectField(
			WrapperSelectField.GERAETETYP, getInternalFrame(), false);
	private WrapperSelectField wsfGewerk = new WrapperSelectField(
			WrapperSelectField.GEWERK, getInternalFrame(), false);
	private WrapperSelectField wsfHersteller = new WrapperSelectField(
			WrapperSelectField.HERSTELLER, getInternalFrame(), false);

	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

	public InternalFrameInstandhaltung getInternalFrameInstandhaltung() {
		return internalFrameInstandhaltung;
	}

	public PanelGeraet(InternalFrame internalFrame, String add2TitleI, Object pk)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameInstandhaltung = (InternalFrameInstandhaltung) internalFrame;

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		// Object key = getInternalFrameReklamation().getTabbedPaneKueche().
		// getPanelQuerySpeiseplan().getSelectedId();
		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {

			clearStatusbar();

		} else {
			internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.setGeraetDto(
							DelegateFactory.getInstance()
									.getInstandhaltungDelegate()
									.geraetFindByPrimaryKey((Integer) key));

			dto2Components();

		}

	}

	protected void dto2Components() throws Throwable {

		if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().getHalleIId() != null) {
			HalleDto halleDto = DelegateFactory
					.getInstance()
					.getInstandhaltungDelegate()
					.halleFindByPrimaryKey(
							internalFrameInstandhaltung
									.getTabbedPaneInstandhaltung()
									.getGeraetDto().getHalleIId());

			wtfHalle.setText(halleDto.getCBez());
		} else {
			wtfHalle.setText("");
		}

		if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().getAnlageIId() != null) {
			AnlageDto anlageDto = DelegateFactory
					.getInstance()
					.getInstandhaltungDelegate()
					.anlageFindByPrimaryKey(
							internalFrameInstandhaltung
									.getTabbedPaneInstandhaltung()
									.getGeraetDto().getAnlageIId());
			wtfAnlage.setText(anlageDto.getCBez());
		} else {
			wtfAnlage.setText("");
		}
		if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().getIsmaschineIId() != null) {

			IsmaschineDto ismaschineDto = DelegateFactory
					.getInstance()
					.getInstandhaltungDelegate()
					.ismaschineFindByPrimaryKey(
							internalFrameInstandhaltung
									.getTabbedPaneInstandhaltung()
									.getGeraetDto().getIsmaschineIId());

			wtfMaschine.setText(ismaschineDto.getCBez());
		} else {
			wtfMaschine.setText(null);
		}
		wsfGeraetetyp.setKey(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getGeraetetypIId());
		
		wsfHersteller.setKey(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getHerstellerIId());
		wsfGewerk.setKey(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getGewerkIId());
		wtfBezeichnung.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getCBez());
		wtfBemerkung.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getXBemerkung());
		wtfGeraetesnr.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getCGeraetesnr());
		wtfGeraeteposition.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getCStandort());
		wtfFabrikat.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getCFabrikat());
		wtfLeistung.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getCLeistung());
		wtfVersorgungskreis.setText(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getCVersorgungskreis());
		wnfGrenzwert.setBigDecimal(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getNGrenzwert());
		wnfGrenzwertMin.setBigDecimal(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getNGrenzwertmin());
		wnfGrenzwertMax.setBigDecimal(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getNGrenzwertmax());
		wnfAnzahl.setInteger(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getIAnzahl());

		if (Helper.short2boolean(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto()
				.getBMesswertabsolut())) {
			wrbGrenzwertAbsolut.setSelected(true);
		} else {
			wrbGrenzwertKummulierend.setSelected(true);
		}

		wcbVersteckt.setShort(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getBVersteckt());

		if (Helper.short2boolean(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getGeraetDto().getBAufwand()) == true) {
			wrbNachAufwand.setSelected(true);
		} else {
			wrbPauschal.setSelected(true);
		}

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setBAufwand(Helper.boolean2Short(wrbNachAufwand.isSelected()));

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCGeraetesnr(wtfGeraetesnr.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCStandort(wtfGeraeteposition.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setCVersorgungskreis(wtfVersorgungskreis.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCBez(wtfBezeichnung.getText());

	}

	private void jbInit() throws Throwable {
		
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wlaGeraetesnr.setText(LPMain
				.getTextRespectUISPr("is.geraet.geraetesnr"));
		wlaGrenzwert.setText(LPMain.getTextRespectUISPr("is.geraet.grenzwert"));
		wlaGrenzwertMin.setText(LPMain
				.getTextRespectUISPr("is.geraet.grenzwertmin"));
		wlaGrenzwertMax.setText(LPMain
				.getTextRespectUISPr("is.geraet.grenzwertmax"));
		wlaWartung.setText(LPMain.getTextRespectUISPr("is.geraet.wartung"));
		wlaGeraeteposition.setText(LPMain
				.getTextRespectUISPr("is.geraet.position"));
		wlaVersorgungskreis.setText(LPMain
				.getTextRespectUISPr("is.geraet.versorgungskreis"));

		wrbNachAufwand.setText(LPMain
				.getTextRespectUISPr("is.geraet.wartung.nachaufwand"));
		wrbPauschal.setText(LPMain
				.getTextRespectUISPr("is.geraet.wartung.pauschal"));
		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));
		wrbGrenzwertAbsolut.setText(LPMain
				.getTextRespectUISPr("is.geraet.messwert.absolut"));
		wrbGrenzwertKummulierend.setText(LPMain
				.getTextRespectUISPr("is.geraet.messwert.kummulierend"));

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wlaFabrikat.setText(LPMain.getTextRespectUISPr("is.fabrikat"));

		wlaLeistung.setText(LPMain.getTextRespectUISPr("is.geraet.leistung"));
		wlaAnzahl.setText(LPMain.getTextRespectUISPr("is.geraet.anzahl"));
		
		wnfAnzahl.setFractionDigits(0);
		
		wrbNachAufwand.isSelected();
		buttonGroup1.add(wrbNachAufwand);
		buttonGroup1.add(wrbPauschal);

		buttonGroupGrenzwert.add(wrbGrenzwertAbsolut);
		buttonGroupGrenzwert.add(wrbGrenzwertKummulierend);

		wrbGrenzwertKummulierend.isSelected();

		wsfGeraetetyp.setMandatoryField(true);
		wtfHalle.setActivatable(false);

		wbuHalle.setText(LPMain.getTextRespectUISPr("is.halle") + "...");

		wbuHalle.setActionCommand(ACTION_SPECIAL_FLR_HALLE);

		wbuHalle.addActionListener(this);

		wbuAnlage.setText(LPMain.getTextRespectUISPr("is.anlage") + "...");

		wbuAnlage.setActionCommand(ACTION_SPECIAL_FLR_ANLAGE);

		wbuAnlage.addActionListener(this);
		wtfAnlage.setActivatable(false);

		wbuMaschine.setText(LPMain.getTextRespectUISPr("is.maschine") + "...");

		wbuMaschine.setActionCommand(ACTION_SPECIAL_FLR_MASCHINE);

		wbuMaschine.addActionListener(this);
		wtfMaschine.setActivatable(false);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wsfGewerk, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 100, 0));
		jpaWorkingOn.add(wsfGewerk.getWrapperTextField(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuHalle, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 50, 0));
		jpaWorkingOn.add(wtfHalle, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wsfGeraetetyp, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 100, 0));
		jpaWorkingOn.add(wsfGeraetetyp.getWrapperTextField(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2),
						0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wbuAnlage, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfAnlage, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wsfHersteller, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 100, 0));
		jpaWorkingOn.add(wsfHersteller.getWrapperTextField(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuMaschine, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfMaschine, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wlaFabrikat, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfFabrikat, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaGeraeteposition, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfGeraeteposition, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaGeraetesnr, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfGeraetesnr, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWartung, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wrbNachAufwand, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		
		jpaWorkingOn.add(wlaLeistung, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfLeistung, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbPauschal, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaVersorgungskreis, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfVersorgungskreis, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaAnzahl, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfAnzahl, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaGrenzwert, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfGrenzwert, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 1, 3,
				0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaGrenzwertMin, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfGrenzwertMin, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaGrenzwertMax, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfGrenzwertMax, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wrbGrenzwertAbsolut, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 0, 2), 50, 0));
		jpaWorkingOn.add(wrbGrenzwertKummulierend, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 120, 0, 2), 0, 0));
		iZeile++;
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung().setGeraetDto(
				new GeraetDto());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_HALLE)) {
			panelQueryFLRHalle = InstandhaltungFilterFactory.getInstance()
					.createPanelFLRHalleNeu(getInternalFrameInstandhaltung(),
							null, true);

			panelQueryFLRHalle
					.setSelectedId(internalFrameInstandhaltung
							.getTabbedPaneInstandhaltung().getGeraetDto()
							.getHalleIId());
			new DialogQuery(panelQueryFLRHalle);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANLAGE)) {

			if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.getGeraetDto().getHalleIId() != null) {

				panelQueryFLRAnlage = InstandhaltungFilterFactory.getInstance()
						.createPanelFLRAnlageNeu(
								getInternalFrameInstandhaltung(),
								null,
								true,
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getGeraetDto().getHalleIId());

				panelQueryFLRAnlage.setSelectedId(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getGeraetDto()
						.getAnlageIId());
				new DialogQuery(panelQueryFLRAnlage);
			} else {

				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.error"), LPMain
						.getTextRespectUISPr("is.error.zuersthalleauswaehlen"));
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_MASCHINE)) {
			if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.getGeraetDto().getAnlageIId() != null) {

				panelQueryFLRMaschine = InstandhaltungFilterFactory
						.getInstance().createPanelFLRMaschineNeu(
								getInternalFrameInstandhaltung(),
								null,
								true,
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getGeraetDto().getAnlageIId());

				panelQueryFLRMaschine.setSelectedId(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getGeraetDto()
						.getAnlageIId());
				new DialogQuery(panelQueryFLRMaschine);
			} else {

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("is.error.zuerstanlageauswaehlen"));
			}
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSTANDHALTUNG;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setStandortIId(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getStandortDto()
								.getIId());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setMandantCNr(LPMain.getTheClient().getMandant());

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setBMesswertabsolut(wrbGrenzwertAbsolut.getShort());

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setBAufwand(Helper.boolean2Short(wrbNachAufwand.isSelected()));
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setGeraetetypIId(wsfGeraetetyp.getIKey());

		
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
		.getGeraetDto().setHerstellerIId(wsfHersteller.getIKey());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
		.getGeraetDto().setGewerkIId(wsfGewerk.getIKey());

		
		
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setNGrenzwert(wnfGrenzwert.getBigDecimal());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setNGrenzwertmin(wnfGrenzwertMin.getBigDecimal());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setNGrenzwertmax(wnfGrenzwertMax.getBigDecimal());

		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCGeraetesnr(wtfGeraetesnr.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCStandort(wtfGeraeteposition.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setXBemerkung(wtfBemerkung.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCFabrikat(wtfFabrikat.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto()
				.setCVersorgungskreis(wtfVersorgungskreis.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCBez(wtfBezeichnung.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setCLeistung(wtfLeistung.getText());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
				.getGeraetDto().setBVersteckt(wcbVersteckt.getShort());
		internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
		.getGeraetDto().setIAnzahl(wnfAnzahl.getInteger());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory
				.getInstance()
				.getInstandhaltungDelegate()
				.removeGeraet(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getGeraetDto());
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
					.getGeraetDto().getIId() == null) {
				internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung()
						.getGeraetDto()
						.setIId(DelegateFactory
								.getInstance()
								.getInstandhaltungDelegate()
								.createGeraet(
										internalFrameInstandhaltung
												.getTabbedPaneInstandhaltung()
												.getGeraetDto()));
				setKeyWhenDetailPanel(internalFrameInstandhaltung
						.getTabbedPaneInstandhaltung().getGeraetDto().getIId());

			} else {

				DelegateFactory
						.getInstance()
						.getInstandhaltungDelegate()
						.updateGeraet(
								internalFrameInstandhaltung
										.getTabbedPaneInstandhaltung()
										.getGeraetDto());

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameInstandhaltung
								.getTabbedPaneInstandhaltung().getGeraetDto()
								.getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRHalle) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				HalleDto halleDto = DelegateFactory.getInstance()
						.getInstandhaltungDelegate().halleFindByPrimaryKey(key);

				wtfHalle.setText(halleDto.getCBez());

				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setHalleIId(key);

				wtfAnlage.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setAnlageIId(null);
				wtfMaschine.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(null);

			} else if (e.getSource() == panelQueryFLRAnlage) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AnlageDto anlageDto = DelegateFactory.getInstance()
						.getInstandhaltungDelegate()
						.anlageFindByPrimaryKey(key);
				wtfAnlage.setText(anlageDto.getCBez());
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setAnlageIId(key);
				wtfMaschine.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(null);

			} else if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				IsmaschineDto ismaschineDto = DelegateFactory.getInstance()
						.getInstandhaltungDelegate()
						.ismaschineFindByPrimaryKey(key);

				wtfMaschine.setText(ismaschineDto.getCBez());
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(key);

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRHalle) {
				wtfHalle.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setHalleIId(null);
				wtfAnlage.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setAnlageIId(null);
				wtfMaschine.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(null);
			} else if (e.getSource() == panelQueryFLRAnlage) {
				wtfAnlage.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setAnlageIId(null);
				wtfMaschine.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(null);
			} else if (e.getSource() == panelQueryFLRMaschine) {
				wtfMaschine.setText(null);
				internalFrameInstandhaltung.getTabbedPaneInstandhaltung()
						.getGeraetDto().setIsmaschineIId(null);
			}
		}
	}

}
