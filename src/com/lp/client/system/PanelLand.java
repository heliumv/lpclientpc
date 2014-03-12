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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>18.02.05</I></p>
 * 
 * <p> </p>
 * 
 * @author josef erlinger
 * 
 * @version $Revision: 1.8 $
 */
public class PanelLand extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LandDto landDto = new LandDto();

	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private WrapperLabel wlaVorwahl = new WrapperLabel();
	private WrapperTextField wtfVorwahl = new WrapperTextField();
	private WrapperLabel wlaLKZ = new WrapperLabel();
	private WrapperTextField wtfLKZ = new WrapperTextField();
	private WrapperLabel wlaLand = new WrapperLabel();
	private WrapperTextField wtfLand = new WrapperTextField();
	private WrapperComboBox wcbWaehrung = null;
	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private Map<?, ?> tmWaehrungen = null;
	private WrapperDateField wdfEUMitgliedSeit = null;
	private WrapperLabel wlaEUMitgliedSeit = null;
	private WrapperNumberField wnfUIDNummerPruefenAbBetrag = null;
	private WrapperLabel wlaUIDNummerPruefenAbBetrag = null;
	private WrapperLabel wlaMandantenwaehrung = null;
	private WrapperLabel wlaGmtVersatz = new WrapperLabel();
	private WrapperNumberField wnfGmtVersatz = new WrapperNumberField();

	private WrapperLabel wlaLaengeUIDNummer = new WrapperLabel();
	private WrapperNumberField wnfLaengeUIDNummer = new WrapperNumberField();
	private WrapperCheckBox wcbSepa = new WrapperCheckBox();

	private WrapperSelectField wsfGemeinsamesPostland = new WrapperSelectField(
			WrapperSelectField.LAND, getInternalFrame(), true);

	private WrapperLabel wlaRundungswerte = new WrapperLabel();
	private WrapperComboBox wcbRundungswerte = null;
	private Map<BigDecimal, String> tmRundungswerte = null;

	public PanelLand(InternalFrame internalFrame, String add2TitleI, Object pk)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);

		jbInit();
		initComponents();
		initPanel();
	}

	private void initRundungswerte() {
		tmRundungswerte = new HashMap<BigDecimal, String>() {
			{
				// put(null, "<keine Rundung>");
				put(new BigDecimal("0.05"), "0.05");
				put(new BigDecimal("0.10"), "0.10");
			}
		};
		wcbRundungswerte = new WrapperComboBox();
		wcbRundungswerte.setMap(tmRundungswerte);
		wlaRundungswerte.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.land.muenzrundung"));

	}

	private void initPanel() throws Throwable {
		tmWaehrungen = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen();
		wcbWaehrung.setMap(tmWaehrungen);
		// Mandantenwaehrung
		wlaMandantenwaehrung.setText(LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	private void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		// hier null setzen des Feldes lkz (update)
		setLandDto(new LandDto());

		leereAlleFelder(this);

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getSystemDelegate()
				.removeLand(getLandDto());
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		getLandDto().setCLkz(wtfLKZ.getText());
		getLandDto().setCName(wtfLand.getText());
		getLandDto().setCTelvorwahl(wtfVorwahl.getText());
		getLandDto()
				.setWaehrungCNr((String) wcbWaehrung.getKeyOfSelectedItem());
		getLandDto().setTEUMitglied(wdfEUMitgliedSeit.getDate());
		getLandDto().setNUidnummerpruefenabbetrag(
				wnfUIDNummerPruefenAbBetrag.getBigDecimal());
		getLandDto().setILaengeuidnummer(wnfLaengeUIDNummer.getInteger());
		getLandDto().setBSepa(wcbSepa.getShort());
		getLandDto().setFGmtversatz(wnfGmtVersatz.getDouble());
		getLandDto().setNMuenzRundung(
				(BigDecimal) wcbRundungswerte.getKeyOfSelectedItem());
		getLandDto().setLandIIdGemeinsamespostland(wsfGemeinsamesPostland.getIKey());
	}

	protected void dto2Components() throws Throwable {
		wtfVorwahl.setText(getLandDto().getCTelvorwahl());
		wtfLKZ.setText(getLandDto().getCLkz());
		wtfLand.setText(getLandDto().getCName());
		wcbWaehrung.setKeyOfSelectedItem(getLandDto().getWaehrungCNr());
		wdfEUMitgliedSeit.setDate(getLandDto().getEUMitglied());
		wnfUIDNummerPruefenAbBetrag.setBigDecimal(getLandDto()
				.getNUidnummerpruefenabbetrag());
		wnfLaengeUIDNummer.setInteger(getLandDto().getILaengeuidnummer());

		wcbSepa.setShort(getLandDto().getBSepa());
		wnfGmtVersatz.setDouble(getLandDto().getFGmtversatz());
		wcbRundungswerte.setKeyOfSelectedItem(getLandDto().getNMuenzRundung());
		wsfGemeinsamesPostland.setKey(getLandDto().getLandIIdGemeinsamespostland());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			Integer iId = null;

			components2Dto();
			if (getLandDto().getIID() == null) {
				// create
				iId = DelegateFactory.getInstance().getSystemDelegate()
						.createLand(getLandDto());
				// dem dto den key setzen.
				getLandDto().setIID(iId);
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(iId);
			} else {
				// update
				DelegateFactory.getInstance().getSystemDelegate()
						.updateLand(getLandDto());
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		initRundungswerte();

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaVorwahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorwahl"));
		
		
		wsfGemeinsamesPostland.getWrapperButton().setText(LPMain.getInstance().getTextRespectUISPr(
		"lp.land.gemeinsamespostland")+"...");

		wtfVorwahl.setColumnsMax(SystemFac.MAX_VORWAHL);
		wtfVorwahl.setMandatoryFieldDB(false);

		wlaLKZ.setText(LPMain.getInstance().getTextRespectUISPr("lp.lkz1"));

		wtfLKZ.setColumnsMax(SystemFac.MAX_LKZ);
		wtfLKZ.setMandatoryFieldDB(true);

		wlaLand.setText(LPMain.getInstance().getTextRespectUISPr("lp.land"));

		wtfLand.setColumnsMax(SystemFac.MAX_LAND);
		wtfLand.setMandatoryField(true);

		wdfEUMitgliedSeit = new WrapperDateField();
		wlaEUMitgliedSeit = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.eumitglied"));

		wcbSepa.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.land.sepa"));
		wlaGmtVersatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.land.gmtversatz"));

		wnfGmtVersatz.setFractionDigits(2);
		wlaLaengeUIDNummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.laengeuidnummer"));
		wnfLaengeUIDNummer.setFractionDigits(0);
		wnfLaengeUIDNummer.setMinimumValue(0);
		wnfLaengeUIDNummer.setMandatoryField(true);

		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.waehrung"));

		wcbWaehrung = new WrapperComboBox();
		// wcbWaehrung.setMandatoryFieldDB(true);
		wnfUIDNummerPruefenAbBetrag = new WrapperNumberField(0, 999999999);
		wlaUIDNummerPruefenAbBetrag = new WrapperLabel();
		wlaUIDNummerPruefenAbBetrag.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.uidnummerpruefenabbetrag"));
		wlaMandantenwaehrung = new WrapperLabel();
		wlaMandantenwaehrung.setMinimumSize(new Dimension(40, Defaults
				.getInstance().getControlHeight()));
		wlaMandantenwaehrung.setPreferredSize(new Dimension(40, Defaults
				.getInstance().getControlHeight()));
		wlaMandantenwaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		// jetzt meine felder
		jPanelWorkingOn = new JPanel(new GridBagLayout());
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaLKZ, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLKZ, new GridBagConstraints(1, iZeile, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaLand, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLand, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaVorwahl, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfVorwahl, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaGmtVersatz, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wnfGmtVersatz, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaWaehrung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcbWaehrung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaRundungswerte, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcbRundungswerte, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaLaengeUIDNummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfLaengeUIDNummer, new GridBagConstraints(1,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaEUMitgliedSeit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wdfEUMitgliedSeit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbSepa, new GridBagConstraints(2, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaUIDNummerPruefenAbBetrag,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));

		jPanelWorkingOn.add(wnfUIDNummerPruefenAbBetrag,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wsfGemeinsamesPostland.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jPanelWorkingOn.add(wsfGemeinsamesPostland.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LAND;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
			wnfLaengeUIDNummer.setInteger(10);
		} else {
			setLandDto(DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey((Integer) key));
			setDefaults();
			dto2Components();
		}
	}

	public LandDto getLandDto() {
		return landDto;
	}

	public void setLandDto(LandDto landDto) {
		this.landDto = landDto;
	}
}
