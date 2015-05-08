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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.SystemFac;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

//public class PanelPositionenIntelligenteZwischensumme extends PanelBasis implements FocusListener {
public class PanelPositionenIntelligenteZwischensumme extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5622174784683282842L;

	private static int BREITE_SPALTE_1 = 100;

	public WrapperLabel wlaBezeichnung = null;
	public WrapperTextField wtfBezeichnung = null;

	private WrapperLabel wlaRabattsatz = null;
	public WrapperNumberField wnfRabattsatz = null;

	private WrapperLabel wlaProzent1 = null;

	private WrapperLabel wlaVonPosition = null;
	public WrapperTextNumberField wnfVonPosition = null;

	private WrapperLabel wlaBisPosition = null;
	public WrapperTextNumberField wnfBisPosition = null;

	public WrapperNumberField wnfRabattbetrag = null;

	public WrapperNumberField wnfZwsBetrag = null;

	public WrapperCheckBox wcbPositionspreisZeigen = null ;
	
	protected IntelligenteZwischensummeController zwController;
	private InternalFrame internalFrame;

	public PanelPositionenIntelligenteZwischensumme(
			InternalFrame internalFrame, String add2TitleI, Object key,
			IntelligenteZwischensummeController controller) throws Throwable {

		super(internalFrame, add2TitleI, key);
		zwController = controller;
		this.internalFrame = internalFrame;
		jbInit();
		initComponents();
	}

	public void setDefaults() throws Throwable {
		if (wnfVonPosition != null)
			wnfVonPosition.setText("");
		if (wnfBisPosition != null)
			wnfBisPosition.setText("");
		if (wnfRabattsatz != null)
			wnfRabattsatz.setBigDecimal(BigDecimal.ZERO);
		if(wcbPositionspreisZeigen != null) {
			wcbPositionspreisZeigen.setSelected(true); 
		}
	}

	private void jbInit() throws Throwable {
		setLayout(new GridBagLayout());

		wlaBezeichnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.bezeichnung"));
		HelperClient.setDefaultsToComponent(wlaBezeichnung, BREITE_SPALTE_1);

		wtfBezeichnung = new WrapperTextField();

		wlaRabattsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.rabatt"));
		HelperClient.setDefaultsToComponent(wlaRabattsatz, BREITE_SPALTE_1);

		wnfRabattsatz = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfRabattsatz.setMandatoryField(true);

		wlaProzent1 = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent1.setHorizontalTextPosition(SwingConstants.LEFT);
		wlaProzent1.setMaximumSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));
		wlaProzent1.setMinimumSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));
		wlaProzent1.setPreferredSize(new Dimension(12, Defaults.getInstance()
				.getControlHeight()));

		wlaVonPosition = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.zws.vonposition"));
		HelperClient.setDefaultsToComponent(wlaVonPosition, BREITE_SPALTE_1);

		wnfVonPosition = new WrapperTextNumberField();
		wnfVonPosition.setMaximumDigits(3);
		wnfVonPosition.setMandatoryField(true);
		wnfVonPosition
				.addFocusListener(new PanelPositionenIntelligenteZwischensumme_wnfVonPosition_focusAdapter(
						this));

		wlaBisPosition = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.zws.bisposition"));
		HelperClient.setDefaultsToComponent(wlaBisPosition, BREITE_SPALTE_1);
		wnfBisPosition = new WrapperTextNumberField();
		wnfBisPosition.setMaximumDigits(3);
		wnfBisPosition.setMandatoryField(true);
		wnfBisPosition
				.addFocusListener(new PanelPositionenIntelligenteZwischensumme_wnfBisPosition_focusAdapter(
						this));

		wnfRabattbetrag = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfRabattbetrag.setMandatoryField(false);
		wnfRabattbetrag.setActivatable(false);
		wnfRabattbetrag.setDependenceField(true);
		HelperClient.setDefaultsToComponent(wnfRabattbetrag, BREITE_SPALTE_1);

		wnfZwsBetrag = new WrapperNumberField(new BigDecimal(
				SystemFac.MIN_N_NUMBER), new BigDecimal(SystemFac.MAX_N_NUMBER));
		wnfZwsBetrag.setMandatoryField(false);
		wnfZwsBetrag.setActivatable(false);
		wnfZwsBetrag.setDependenceField(true);
		HelperClient.setDefaultsToComponent(wnfZwsBetrag, BREITE_SPALTE_1);

		wcbPositionspreisZeigen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.zws.preiszeigen")) ;
		HelperClient.setDefaultsToComponent(wcbPositionspreisZeigen, BREITE_SPALTE_1);
		
		if (!internalFrame.bRechtDarfPreiseSehenVerkauf) {
			wnfRabattbetrag.setVisible(false);
			wnfZwsBetrag.setVisible(false);
		}

		int zeile = 0;
		add(wlaBezeichnung, new GridBagConstraints(0, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wtfBezeichnung, new GridBagConstraints(1, zeile, 7 /*
																 * GridBagConstraints
																 * .REMAINDER
																 */, 1, 1.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		++zeile;
		add(wlaRabattsatz, new GridBagConstraints(0, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wnfRabattsatz, new GridBagConstraints(1, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wlaProzent1, new GridBagConstraints(2, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wnfRabattbetrag, new GridBagConstraints(3, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wnfZwsBetrag, new GridBagConstraints(4, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		++zeile;
		add(wlaVonPosition, new GridBagConstraints(0, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wnfVonPosition, new GridBagConstraints(1, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		++zeile;
		add(wlaBisPosition, new GridBagConstraints(0, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		add(wnfBisPosition, new GridBagConstraints(1, zeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		++zeile ;
		add(wcbPositionspreisZeigen, new GridBagConstraints(1, zeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0)) ;
		
		setDefaults();

		getInternalFrame().addItemChangedListener(this);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			zwController.setChanged(true);
		}
	}

	public void calculateFields() throws Throwable, ExceptionLP {
		Integer von = wnfVonPosition.getInteger();
		if (von == null || von < 1)
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_EINS,
					"Von '" + wnfVonPosition.getText() + "' muss >= 1 sein",
					new ArrayList<String>() {
						private static final long serialVersionUID = -1834810833095924275L;

						{
							add(wnfVonPosition.getText());
						}
					}, new IllegalArgumentException(wnfVonPosition.getText()));

		Integer bis = wnfBisPosition.getInteger();
		if (bis == null || bis < von)
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_BIS_KLEINER_VON,
					"Bis '" + wnfBisPosition.getText()
							+ "' muss gr\u00F6\u00DFer/gleich '" + von + "' sein",
					new ArrayList<String>() {
						private static final long serialVersionUID = 906098343651024896L;

						{
							add(wnfBisPosition.getText());
						}
					}, new IllegalArgumentException(wnfBisPosition.getText()));

		if (!zwController.isValidPositionNumber(von, bis))
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_POSITIONSNUMMER,
					"Von/Bis muss kleiner als Zwischensummenposition sein",
					new ArrayList<String>() {
						private static final long serialVersionUID = -2431621337549035774L;

						{
							add(wnfVonPosition.getText());
							add(wnfBisPosition.getText());
						}
					}, new IllegalArgumentException());

		if (!zwController.isValidMwstSatz(von, bis)) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH,
					"Mehrwertsteuersatz ist f\u00FCr die Zwischensummenpositionen unterschiedlich",
					new ArrayList<String>() {

						private static final long serialVersionUID = -1007164583320766068L;

						{
							add(wnfVonPosition.getText());
							add(wnfBisPosition.getText());
						}
					}, new IllegalArgumentException());
		}
	}

	public void dto2Components(BelegpositionDto positionDto) throws Throwable,
			ExceptionLP {
		if (positionDto instanceof BelegpositionVerkaufDto) {
			
			BelegpositionVerkaufDto positionDtoVK = (BelegpositionVerkaufDto) positionDto;

			wtfBezeichnung.setText(positionDto.getCBez());
			wnfVonPosition.setInteger(null) ;
			if(positionDtoVK.getZwsVonPosition() != null) {
				Integer positionsnummer = zwController
					.getPositionNummer(positionDtoVK.getZwsVonPosition());
				wnfVonPosition.setInteger(positionsnummer);
			}
			
			wnfBisPosition.setInteger(null) ;
			if(positionDtoVK.getZwsBisPosition() != null) {
				Integer positionsnummer = zwController.getPositionNummer(positionDtoVK
					.getZwsBisPosition());
				wnfBisPosition.setInteger(positionsnummer);
			}
			
			wnfRabattsatz.setDouble(positionDtoVK.getFRabattsatz());

			wnfRabattbetrag.setBigDecimal(positionDtoVK
					.getNEinzelpreisplusversteckteraufschlag());
			wnfZwsBetrag.setBigDecimal(positionDtoVK.getZwsNettoSumme());
			wcbPositionspreisZeigen.setShort(positionDtoVK.getBZwsPositionspreisZeigen());
			
			if (zwController.getChanged()) {
				wnfRabattbetrag.setBackground(Color.ORANGE);
				wnfZwsBetrag.setBackground(Color.ORANGE);
			} else {
				Color backgroundColor = HelperClient
						.getDependenceFieldBackgroundColor();
				if (null == backgroundColor) {
					backgroundColor = Color.LIGHT_GRAY;
				}
				wnfRabattbetrag.setBackground(backgroundColor);
				wnfZwsBetrag.setBackground(backgroundColor);
			}
		}
	}

	public Integer components2Dto(BelegVerkaufDto rechnungDto,
			BelegpositionVerkaufDto positionDto) throws Throwable, ExceptionLP {
		Integer zwsPositionIId = zwController.getPositionIIdFromPositionNummer(
				rechnungDto.getIId(), wnfVonPosition.getInteger());
		positionDto.setZwsVonPosition(zwsPositionIId);

		zwsPositionIId = zwController.getPositionIIdFromPositionNummer(
				rechnungDto.getIId(), wnfBisPosition.getInteger());
		positionDto.setZwsBisPosition(zwsPositionIId);
		positionDto.setFRabattsatz(wnfRabattsatz.getDouble());

		positionDto.setCBez(wtfBezeichnung.getText());
		positionDto.setBZwsPositionspreisDrucken(wcbPositionspreisZeigen.getShort()) ;

		zwController.setBelegDto(rechnungDto);
		return zwsPositionIId;
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		/** @todo JO->UW PJ 5029 */
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		case EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING:

			// die laufende Aktion wurde abgebrochen
			break;

		default:
			bErrorErkannt = false;
		}
		return bErrorErkannt;
	}

	public void wnfVonPosition_focusLost(FocusEvent e) {
		// System.out.println("von focuslost") ;
	}

	public void wnfBisPosition_focusLost(FocusEvent e) {
		// System.out.println("bis focuslost") ;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

}

class PanelPositionenIntelligenteZwischensumme_wnfVonPosition_focusAdapter
		extends java.awt.event.FocusAdapter {
	private PanelPositionenIntelligenteZwischensumme adaptee;

	PanelPositionenIntelligenteZwischensumme_wnfVonPosition_focusAdapter(
			PanelPositionenIntelligenteZwischensumme adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfVonPosition_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}

class PanelPositionenIntelligenteZwischensumme_wnfBisPosition_focusAdapter
		extends java.awt.event.FocusAdapter {
	private PanelPositionenIntelligenteZwischensumme adaptee;

	PanelPositionenIntelligenteZwischensumme_wnfBisPosition_focusAdapter(
			PanelPositionenIntelligenteZwischensumme adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBisPosition_focusLost(e);
		} catch (Throwable t) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}
