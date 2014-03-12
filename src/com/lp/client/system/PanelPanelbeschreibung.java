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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;

@SuppressWarnings("static-access")
public class PanelPanelbeschreibung extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameSystem internalFramePersonal = null;
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;
	WrapperLabel wlaName = new WrapperLabel();
	WrapperTextField wtfName = new WrapperTextField();

	WrapperLabel wlaTyp = new WrapperLabel();
	WrapperComboBox wcoTyp = new WrapperComboBox();

	WrapperLabel wlaText = new WrapperLabel();
	WrapperTextField wtfText = new WrapperTextField();

	WrapperLabel wlaFill = new WrapperLabel();
	WrapperComboBox wcoFill = new WrapperComboBox();

	WrapperLabel wlaAnchor = new WrapperLabel();
	WrapperComboBox wcoAnchor = new WrapperComboBox();

	WrapperCheckBox wcbMandatory = new WrapperCheckBox();

	WrapperLabel wlaGridx = new WrapperLabel();
	WrapperNumberField wnfGridx = new WrapperNumberField();

	WrapperLabel wlaGridy = new WrapperLabel();
	WrapperNumberField wnfGridy = new WrapperNumberField();

	WrapperLabel wlaIpadx = new WrapperLabel();
	WrapperNumberField wnfIpadx = new WrapperNumberField();

	WrapperLabel wlaIpady = new WrapperLabel();
	WrapperNumberField wnfIpady = new WrapperNumberField();

	WrapperLabel wlaWeightX = new WrapperLabel();
	WrapperNumberField wnfWeightX = new WrapperNumberField();

	WrapperLabel wlaWeightY = new WrapperLabel();
	WrapperNumberField wnfWeightY = new WrapperNumberField();

	WrapperLabel wlaGridwidth = new WrapperLabel();
	WrapperNumberField wnfGridwidth = new WrapperNumberField();

	WrapperLabel wlaGridheight = new WrapperLabel();
	WrapperNumberField wnfGridheight = new WrapperNumberField();

	WrapperLabel wlaInsetsLeft = new WrapperLabel();
	WrapperNumberField wnfInsetsLeft = new WrapperNumberField();

	WrapperLabel wlaInsetsRight = new WrapperLabel();
	WrapperNumberField wnfInsetsRightt = new WrapperNumberField();

	WrapperLabel wlaInsetsTop = new WrapperLabel();
	WrapperNumberField wnfInsetsTop = new WrapperNumberField();

	WrapperSelectField wsfArtgru = null;
	WrapperSelectField wsfPartnerklasse = null;

	WrapperLabel wlaInsetsBottom = new WrapperLabel();
	WrapperNumberField wnfInsetsBottom = new WrapperNumberField();

	WrapperLabel wlaDruckname = new WrapperLabel();
	WrapperTextField wtfDruckname = new WrapperTextField();

	WrapperLabel wlaDummy = new WrapperLabel();

	PanelbeschreibungDto panelbeschreibungDto = null;

	private String letzterTyp = null;

	public PanelPanelbeschreibung(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameSystem) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	private void setDefaults() {
		Map<String, String> fill = new TreeMap<String, String>();
		fill.put(PanelFac.FILL_BOTH, PanelFac.FILL_BOTH.trim());
		fill.put(PanelFac.FILL_HORIZONTAL, PanelFac.FILL_HORIZONTAL.trim());
		fill.put(PanelFac.FILL_NONE, PanelFac.FILL_NONE.trim());
		fill.put(PanelFac.FILL_VERTICAL, PanelFac.FILL_VERTICAL.trim());
		wcoFill.setMap(fill);

		Map<String, String> typ = new TreeMap<String, String>();
		typ.put(PanelFac.TYP_WRAPPERCHECKBOX,
				PanelFac.TYP_WRAPPERCHECKBOX.trim());
		typ.put(PanelFac.TYP_WRAPPEREDITOR, PanelFac.TYP_WRAPPEREDITOR.trim());
		typ.put(PanelFac.TYP_WRAPPERLABEL, PanelFac.TYP_WRAPPERLABEL.trim());
		typ.put(PanelFac.TYP_WRAPPERTEXTFIELD,
				PanelFac.TYP_WRAPPERTEXTFIELD.trim());
		typ.put(PanelFac.TYP_WRAPPERPRINTBUTTON,
				PanelFac.TYP_WRAPPERPRINTBUTTON.trim());
		typ.put(PanelFac.TYP_WRAPPEREXECBUTTON,
				PanelFac.TYP_WRAPPEREXECBUTTON.trim());
		typ.put(PanelFac.TYP_WRAPPERCOMBOBOX,
				PanelFac.TYP_WRAPPERCOMBOBOX.trim());
		wcoTyp.setMap(typ);

		Map<String, String> anchor = new TreeMap<String, String>();
		anchor.put(PanelFac.ANCHOR_CENTER, PanelFac.ANCHOR_CENTER.trim());
		anchor.put(PanelFac.ANCHOR_EAST, PanelFac.ANCHOR_EAST.trim());
		anchor.put(PanelFac.ANCHOR_NORTH, PanelFac.ANCHOR_NORTH.trim());
		anchor.put(PanelFac.ANCHOR_NORTHEAST, PanelFac.ANCHOR_NORTHEAST.trim());
		anchor.put(PanelFac.ANCHOR_NORTHWEST, PanelFac.ANCHOR_NORTHWEST.trim());
		anchor.put(PanelFac.ANCHOR_SOUTH, PanelFac.ANCHOR_SOUTH.trim());
		anchor.put(PanelFac.ANCHOR_SOUTHEAST, PanelFac.ANCHOR_SOUTHEAST.trim());
		anchor.put(PanelFac.ANCHOR_SOUTHWEST, PanelFac.ANCHOR_SOUTHWEST.trim());
		anchor.put(PanelFac.ANCHOR_WEST, PanelFac.ANCHOR_WEST.trim());
		wcoAnchor.setMap(anchor);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfName;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		panelbeschreibungDto = new PanelbeschreibungDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
		wsfArtgru.setKey(null);
		wsfPartnerklasse.setKey(null);

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPanelDelegate()
				.removePanelbeschreibung(panelbeschreibungDto.getIId());
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		panelbeschreibungDto.setPanelCNr(internalFramePersonal.getPanelDto()
				.getCNr());
		panelbeschreibungDto.setBMandatory(wcbMandatory.getShort());
		panelbeschreibungDto.setCAnchor((String) wcoAnchor
				.getKeyOfSelectedItem());
		panelbeschreibungDto.setCFill((String) wcoFill.getKeyOfSelectedItem());
		panelbeschreibungDto.setCName(wtfName.getText());
		panelbeschreibungDto.setCTokeninresourcebundle(wtfText.getText());
		panelbeschreibungDto.setCDruckname(wtfDruckname.getText());
		panelbeschreibungDto.setCTyp((String) wcoTyp.getKeyOfSelectedItem());
		panelbeschreibungDto.setFWeightx(wnfWeightX.getDouble());
		panelbeschreibungDto.setFWeighty(wnfWeightY.getDouble());
		panelbeschreibungDto.setIIpadx(wnfIpadx.getInteger());
		panelbeschreibungDto.setIIpady(wnfIpady.getInteger());
		panelbeschreibungDto.setIGridx(wnfGridx.getInteger());
		panelbeschreibungDto.setIGridy(wnfGridy.getInteger());

		panelbeschreibungDto.setArtgruIId(null);
		panelbeschreibungDto.setPartnerklasseIId(null);
		
		if (internalFramePersonal.getPanelDto().getCNr()
				.equals(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {

			panelbeschreibungDto.setArtgruIId((Integer) wsfArtgru.getOKey());
		
		}
		if (internalFramePersonal.getPanelDto().getCNr()
				.equals(PanelFac.PANEL_KUNDENEIGENSCHAFTEN)) {
			panelbeschreibungDto.setPartnerklasseIId((Integer) wsfPartnerklasse
					.getOKey());
			
		}
	

		panelbeschreibungDto.setIInsetsbottom(wnfInsetsBottom.getInteger());
		panelbeschreibungDto.setIInsetsleft(wnfInsetsLeft.getInteger());
		panelbeschreibungDto.setIInsetsright(wnfInsetsRightt.getInteger());
		panelbeschreibungDto.setIInsetstop(wnfInsetsTop.getInteger());

		panelbeschreibungDto.setIGridheigth(wnfGridheight.getInteger());
		panelbeschreibungDto.setIGridwidth(wnfGridwidth.getInteger());

		panelbeschreibungDto.setMandantCNr(LPMain.getInstance().getTheClient()
				.getMandant());

	}

	protected void dto2Components() throws Throwable {

		wcbMandatory.setShort(panelbeschreibungDto.getBMandatory());
		wcoAnchor.setKeyOfSelectedItem(panelbeschreibungDto.getCAnchor());
		wcoFill.setKeyOfSelectedItem(panelbeschreibungDto.getCFill());
		wcoTyp.setKeyOfSelectedItem(panelbeschreibungDto.getCTyp());
		wtfName.setText(panelbeschreibungDto.getCName());
		wtfText.setText(panelbeschreibungDto.getCTokeninresourcebundle());
		wtfDruckname.setText(panelbeschreibungDto.getCDruckname());
		wnfWeightX.setDouble(panelbeschreibungDto.getFWeightx());
		wnfWeightY.setDouble(panelbeschreibungDto.getFWeighty());
		wnfIpadx.setInteger(panelbeschreibungDto.getIIpadx());
		wnfIpady.setInteger(panelbeschreibungDto.getIIpady());
		wnfGridx.setInteger(panelbeschreibungDto.getIGridx());
		wnfGridy.setInteger(panelbeschreibungDto.getIGridy());

		wsfArtgru.setKey(panelbeschreibungDto.getArtgruIId());
		wsfPartnerklasse.setKey(panelbeschreibungDto.getPartnerklasseIId());
		wnfInsetsBottom.setInteger(panelbeschreibungDto.getIInsetsbottom());
		wnfInsetsLeft.setInteger(panelbeschreibungDto.getIInsetsleft());
		wnfInsetsRightt.setInteger(panelbeschreibungDto.getIInsetsright());
		wnfInsetsTop.setInteger(panelbeschreibungDto.getIInsetstop());

		wnfGridheight.setInteger(panelbeschreibungDto.getIGridheigth());
		wnfGridwidth.setInteger(panelbeschreibungDto.getIGridwidth());

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		try {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				letzterTyp = panelbeschreibungDto.getCTyp();
				if (panelbeschreibungDto.getIId() == null) {
					components2Dto();
					panelbeschreibungDto.setIId(DelegateFactory
							.getInstance()
							.getPanelDelegate()
							.createPanelbeschreibung(panelbeschreibungDto,
									bFuegeNeuePositionVorDerSelektiertenEin));
					setKeyWhenDetailPanel(panelbeschreibungDto.getIId());
				} else {
					DelegateFactory.getInstance().getPanelDelegate()
							.updatePanelbeschreibung(panelbeschreibungDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFramePersonal.getPanelDto().getCNr());
				}
				eventYouAreSelected(false);
			}
		} finally {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
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
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaName.setText(LPMain.getInstance().getTextRespectUISPr("lp.feldname"));
		wlaAnchor.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.feldausrichtung"));
		wlaFill.setText(LPMain.getInstance().getTextRespectUISPr("lp.fill"));
		wlaGridheight.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.feldhoehe"));
		wlaGridwidth.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.feldbreite"));
		wlaGridx.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.feldxposition"));
		wlaGridy.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.feldyposition"));
		wlaIpadx.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mindestgroessex"));
		wlaIpady.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mindestgroessey"));
		wlaText.setText(LPMain.getInstance().getTextRespectUISPr("lp.feldtext"));
		wlaDruckname.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.druckname"));
		wlaTyp.setText(LPMain.getInstance().getTextRespectUISPr("lp.feldtyp"));
		wlaWeightX.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.horizontalegewichtung"));
		wlaWeightY.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.vertikalegewichtung"));
		wcbMandatory.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.pflichtfeld"));

		wlaInsetsBottom.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abstandunten"));
		wlaInsetsLeft.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abstandlinks"));
		wlaInsetsRight.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abstandrechts"));
		wlaInsetsTop.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abstandoben"));

		wtfText.setColumnsMax(3000);
		wtfDruckname.setColumnsMax(50);
		wnfGridheight.setMandatoryField(true);
		wnfGridheight.setFractionDigits(0);
		wnfGridwidth.setMandatoryField(true);
		wnfGridwidth.setFractionDigits(0);
		wnfGridx.setMandatoryField(true);
		wnfGridx.setFractionDigits(0);
		wnfGridy.setMandatoryField(true);
		wnfGridy.setFractionDigits(0);
		wnfIpadx.setMandatoryField(true);
		wnfIpadx.setFractionDigits(0);
		wnfIpady.setMandatoryField(true);
		wnfIpady.setFractionDigits(0);
		wnfWeightX.setMandatoryField(true);
		wnfWeightY.setMandatoryField(true);

		wnfInsetsBottom.setMandatoryField(true);
		wnfInsetsBottom.setFractionDigits(0);
		wnfInsetsLeft.setFractionDigits(0);
		wnfInsetsRightt.setFractionDigits(0);
		wnfInsetsTop.setFractionDigits(0);
		wnfInsetsLeft.setMandatoryField(true);
		wnfInsetsRightt.setMandatoryField(true);
		wnfInsetsTop.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);
		wcoAnchor.setMandatoryField(true);
		wcoFill.setMandatoryField(true);
		wcoTyp.setMandatoryField(true);
		wcoTyp.addActionListener(new PanelPanelbeschreibung_wcoTyp_actionAdapter(
				this));

		wtfName.setColumnsMax(PanelFac.MAX_PANELBESCHREIBUNG_NAME);
		wtfName.setMandatoryField(true);

		wsfArtgru = new WrapperSelectField(WrapperSelectField.ARTIKELGRUPPE,
				getInternalFrame(), true);
		wsfPartnerklasse = new WrapperSelectField(
				WrapperSelectField.PARTNERKLASSE, getInternalFrame(), true);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaName, new GridBagConstraints(0, 0, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfName, new GridBagConstraints(1, 0, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaDummy, new GridBagConstraints(2, 0, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wsfArtgru.getWrapperButton(),
				new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jPanelWorkingOn.add(wsfArtgru.getWrapperTextField(),
				new GridBagConstraints(4, 0, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jPanelWorkingOn.add(wlaGridx, new GridBagConstraints(0, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfGridx, new GridBagConstraints(1, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaGridy, new GridBagConstraints(3, 2, 1, 1, 0.15,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfGridy, new GridBagConstraints(4, 2, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaAnchor, new GridBagConstraints(0, 3, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcoAnchor, new GridBagConstraints(1, 3, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaFill, new GridBagConstraints(3, 3, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcoFill, new GridBagConstraints(4, 3, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaGridwidth, new GridBagConstraints(0, 4, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfGridwidth, new GridBagConstraints(1, 4, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaGridheight, new GridBagConstraints(3, 4, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfGridheight, new GridBagConstraints(4, 4, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaWeightX, new GridBagConstraints(0, 5, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfWeightX, new GridBagConstraints(1, 5, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWeightY, new GridBagConstraints(3, 5, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfWeightY, new GridBagConstraints(4, 5, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaIpadx, new GridBagConstraints(0, 6, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfIpadx, new GridBagConstraints(1, 6, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaIpady, new GridBagConstraints(3, 6, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfIpady, new GridBagConstraints(4, 6, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaInsetsTop, new GridBagConstraints(0, 7, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfInsetsTop, new GridBagConstraints(1, 7, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaInsetsBottom, new GridBagConstraints(3, 7, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfInsetsBottom, new GridBagConstraints(4, 7, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaInsetsLeft, new GridBagConstraints(0, 8, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfInsetsLeft, new GridBagConstraints(1, 8, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaInsetsRight, new GridBagConstraints(3, 8, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfInsetsRightt, new GridBagConstraints(4, 8, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaTyp, new GridBagConstraints(0, 9, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcoTyp, new GridBagConstraints(1, 9, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcbMandatory, new GridBagConstraints(2, 9, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaDruckname, new GridBagConstraints(3, 9, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfDruckname, new GridBagConstraints(4, 9, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaText, new GridBagConstraints(0, 10, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfText, new GridBagConstraints(1, 10, 4, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PANELBESCHREIBUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		
		
		jPanelWorkingOn.remove(wsfArtgru.getWrapperButton());
		jPanelWorkingOn.remove(wsfArtgru.getWrapperTextField());
		jPanelWorkingOn.remove(wsfPartnerklasse.getWrapperButton());
		jPanelWorkingOn.remove(wsfPartnerklasse.getWrapperTextField());

		if (internalFramePersonal.getPanelDto().getCNr()
				.equals(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {

			jPanelWorkingOn.add(wsfArtgru.getWrapperButton(),
					new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jPanelWorkingOn.add(wsfArtgru.getWrapperTextField(),
					new GridBagConstraints(4, 0, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else if (internalFramePersonal.getPanelDto().getCNr()
				.equals(PanelFac.PANEL_KUNDENEIGENSCHAFTEN)) {
			jPanelWorkingOn.add(wsfPartnerklasse.getWrapperButton(),
					new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jPanelWorkingOn.add(wsfPartnerklasse.getWrapperTextField(),
					new GridBagConstraints(4, 0, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

			wnfInsetsBottom.setDouble(new Double(2));
			wnfInsetsLeft.setDouble(new Double(2));
			wnfInsetsRightt.setDouble(new Double(2));
			wnfInsetsTop.setDouble(new Double(2));

			wnfIpadx.setDouble(new Double(0));
			wnfIpady.setDouble(new Double(0));

			wnfGridheight.setDouble(new Double(1));
			wnfGridwidth.setDouble(new Double(1));

			wnfWeightX.setDouble(new Double(0));
			wnfWeightY.setDouble(new Double(0));

			wcoFill.setSelectedItem(PanelFac.FILL_HORIZONTAL);
			wcoAnchor.setSelectedItem(PanelFac.ANCHOR_CENTER);

			if (key != null && key.equals(LPMain.getLockMeForNew())
					&& letzterTyp != null) {
				wcoTyp.setKeyOfSelectedItem(letzterTyp);
			}

		} else {
			panelbeschreibungDto = DelegateFactory.getInstance()
					.getPanelDelegate()
					.panelbeschreibungFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

	public void wcoTyp_actionPerformed(ActionEvent e) {
		if (wcoTyp.getKeyOfSelectedItem().equals(PanelFac.TYP_WRAPPERTEXTFIELD)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPERCOMBOBOX)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPERCHECKBOX)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPEREDITOR)) {
			wlaDruckname.setVisible(true);
			wtfDruckname.setVisible(true);

		} else {

			wlaDruckname.setVisible(false);
			wtfDruckname.setVisible(false);

		}
		if (wcoTyp.getKeyOfSelectedItem().equals(PanelFac.TYP_WRAPPERLABEL)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPERCHECKBOX)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPERCOMBOBOX)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPEREXECBUTTON)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPERPRINTBUTTON)) {
			wcbMandatory.setVisible(false);
			wlaText.setVisible(true);
			wtfText.setVisible(true);
			wtfText.setMandatoryField(true);
			wlaName.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.feldname"));
			if (wcoTyp.getKeyOfSelectedItem().equals(
					PanelFac.TYP_WRAPPERPRINTBUTTON)) {
				wlaName.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.report"));
			} else if (wcoTyp.getKeyOfSelectedItem().equals(
					PanelFac.TYP_WRAPPEREXECBUTTON)) {
				wlaName.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.befehlsstring"));
			}

		} else if (wcoTyp.getKeyOfSelectedItem().equals(
				PanelFac.TYP_WRAPPERTEXTFIELD)
				|| wcoTyp.getKeyOfSelectedItem().equals(
						PanelFac.TYP_WRAPPEREDITOR)) {
			wcbMandatory.setVisible(true);
			wlaText.setVisible(false);
			wtfText.setVisible(false);
			wtfText.setMandatoryField(false);
			wlaName.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.feldname"));

		}

		if (wcoTyp.getKeyOfSelectedItem().equals(PanelFac.TYP_WRAPPERCOMBOBOX)) {
			wcbMandatory.setVisible(true);
		}

	}

}

class PanelPanelbeschreibung_wcoTyp_actionAdapter implements ActionListener {
	private PanelPanelbeschreibung adaptee;

	PanelPanelbeschreibung_wcoTyp_actionAdapter(PanelPanelbeschreibung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoTyp_actionPerformed(e);
	}
}
