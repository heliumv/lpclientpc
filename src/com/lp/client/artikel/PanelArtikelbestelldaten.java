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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.MandantFac;

@SuppressWarnings("static-access")
public class PanelArtikelbestelldaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaButtonAction = null;
	private WrapperLabel wlaLetzterzugang = new WrapperLabel();
	private WrapperLabel wlaVerschnitt = new WrapperLabel();
	private WrapperLabel wla8 = new WrapperLabel();
	private WrapperLabel wlaLetzterabgang = new WrapperLabel();
	private WrapperDateField wdfLetzterabgang = new WrapperDateField();
	private WrapperDateField wdfLetzterzugang = new WrapperDateField();
	private WrapperNumberField wnfVerschnitt = new WrapperNumberField();
	private WrapperLabel wlaverschnittfaktor = new WrapperLabel();
	private WrapperNumberField wnfVerschnittfaktor = new WrapperNumberField();
	private WrapperLabel wlaJahresmenge = new WrapperLabel();
	private WrapperLabel wlaLagersollstand = new WrapperLabel();
	private WrapperLabel wlaLagermindeststand = new WrapperLabel();
	private WrapperLabel wlaFertigungssatzgrosse = new WrapperLabel();
	private WrapperNumberField wnfJahresmenge = new WrapperNumberField();
	private WrapperNumberField wnfLagermindeststand = new WrapperNumberField();
	private WrapperNumberField wnfLagersollstand = new WrapperNumberField();
	private WrapperNumberField wnfFertigungssatzgroesse = new WrapperNumberField();

	private WrapperSelectField wsfHersteller = new WrapperSelectField(
			WrapperSelectField.HERSTELLER, getInternalFrame(), true);

	private WrapperLabel wlaDummy = new WrapperLabel();
	private WrapperLabel wlaArtikelBez = new WrapperLabel();
	private WrapperTextField wtfArtikelBez = new WrapperTextField();
	private WrapperLabel wlaArtikelZBez = new WrapperLabel();
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperTextField wtfArtikelZBez = new WrapperTextField();
	private WrapperLabel wlaZBez2 = new WrapperLabel();
	private WrapperTextField wtfArtikelZBez2 = new WrapperTextField();
	private Border border1;
	private WrapperLabel wlaVerschnittProzent = new WrapperLabel();
	private WrapperLabel wlaVerschnittMengeneinheit = new WrapperLabel();
	private WrapperLabel wlaLagersollMengeneinheit = new WrapperLabel();
	private WrapperLabel wlaLagermindestMengeneinheit = new WrapperLabel();
	private WrapperLabel wlaFertigungssatzgroesseMengeneinheit = new WrapperLabel();
	private WrapperLabel wlaJahresmengeprozent = new WrapperLabel();
	private WrapperLabel wlaFertigungsVPE = new WrapperLabel();
	private WrapperNumberField wnfFertigungsVPE = new WrapperNumberField();

	private WrapperLabel wlaDetailprozentmindeststand = new WrapperLabel();
	private WrapperNumberField wnfDetailprozentmindeststand = new WrapperNumberField();

	private WrapperLabel wlaHerstellerNr = new WrapperLabel();
	private WrapperTextField wtfHerstellerNr = new WrapperTextField();
	private WrapperLabel wlaHerstellerBez = new WrapperLabel();
	private WrapperTextField wtfHerstellerBez = new WrapperTextField();

	private WrapperLabel wlaUeberproduktion = new WrapperLabel();
	private WrapperNumberField wnfUeberproduktion = new WrapperNumberField();

	public PanelArtikelbestelldaten(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						((InternalFrameArtikel) getInternalFrame())
								.getArtikelDto().getIId());
		dto2Components();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfVerschnitt;
	}

	private void jbInit() throws Throwable {
		border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				165, 163, 151));
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wlaLetzterzugang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.letzterzugang"));
		wlaVerschnitt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.verschnittbasis"));
		wla8.setToolTipText("");
		wla8.setText("");
		wlaLetzterabgang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.letzterabgang"));
		wlaverschnittfaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.verschnittfaktor"));
		wlaUeberproduktion.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.ueberproduktion"));
		wlaFertigungsVPE.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.fertigungsvpe"));

		wlaJahresmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.jahresmenge"));
		wlaLagersollstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.lagersollstand"));
		wlaLagermindeststand.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.lagermindeststand"));
		wlaFertigungssatzgrosse.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.fertigungssatzgroesse"));

		wlaDetailprozentmindeststand.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.detailprozentmindeststand"));

		wdfLetzterzugang.setActivatable(false);
		wdfLetzterzugang.setEnabled(false);
		wdfLetzterabgang.setActivatable(false);
		wdfLetzterabgang.setEnabled(false);
		wlaArtikelBez.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfArtikelBez.setText("");
		wtfArtikelBez.setActivatable(false);
		wlaArtikelZBez.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zusatzbezeichnung"));
		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));

		wlaHerstellerNr.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.herstellernr"));
		wlaHerstellerBez.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.herstellerbez"));

		wtfArtikelnummer.setText("");
		wtfArtikelnummer.setActivatable(false);
		wtfArtikelZBez.setText("");
		wtfArtikelZBez.setActivatable(false);
		wlaZBez2.setRequestFocusEnabled(true);
		wlaZBez2.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zusatzbez2"));
		wtfArtikelZBez2.setText("");
		wtfArtikelZBez2.setActivatable(false);
		wlaVerschnittProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaVerschnittProzent.setText("%");
		wlaVerschnittMengeneinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLagermindestMengeneinheit
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLagersollMengeneinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaFertigungssatzgroesseMengeneinheit
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaVerschnittMengeneinheit.setText("EHT");
		wlaJahresmengeprozent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaJahresmengeprozent.setText("EHT");
		wnfVerschnittfaktor.setMinimumValue(new BigDecimal(0));
		wnfUeberproduktion.setMinimumValue(new BigDecimal(0));
		
		wnfFertigungsVPE.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		wnfVerschnitt.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		

		JPanel jpaWorkingOn = new JPanel(new MigLayout("wrap 6",
				"[20%][15%][5%][30%][15%][5%]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaArtikelnummer, "growx");
		jpaWorkingOn.add(wtfArtikelnummer, "growx, span 3, wrap");

		jpaWorkingOn.add(wlaArtikelBez, "growx");
		jpaWorkingOn.add(wtfArtikelBez, "growx, span");

		jpaWorkingOn.add(wlaArtikelZBez, "growx");
		jpaWorkingOn.add(wtfArtikelZBez, "growx, span");

		jpaWorkingOn.add(wlaZBez2, "growx");
		jpaWorkingOn.add(wtfArtikelZBez2, "growx, span, wrap 30");

		jpaWorkingOn.add(wsfHersteller.getWrapperButton(), "growx");
		jpaWorkingOn.add(wsfHersteller.getWrapperTextField(),
				"growx,span 3, wrap");

		jpaWorkingOn.add(wlaHerstellerNr, "growx");
		jpaWorkingOn.add(wtfHerstellerNr, "growx,span 3, wrap");

		jpaWorkingOn.add(wlaHerstellerBez, "growx");
		jpaWorkingOn.add(wtfHerstellerBez, "growx, span 3,wrap 30");

		jpaWorkingOn.add(wlaLetzterzugang, "growx");
		jpaWorkingOn.add(wdfLetzterzugang, "growx");
		jpaWorkingOn.add(wla8, "growx");
		jpaWorkingOn.add(wlaLetzterabgang, "growx");
		jpaWorkingOn.add(wdfLetzterabgang, "wrap");

		jpaWorkingOn.add(wlaVerschnitt, "growx");
		jpaWorkingOn.add(wnfVerschnitt, "growx");
		jpaWorkingOn.add(wlaVerschnittMengeneinheit, "growx");
		jpaWorkingOn.add(wlaverschnittfaktor, "growx");
		jpaWorkingOn.add(wnfVerschnittfaktor, "growx");
		jpaWorkingOn.add(wlaVerschnittProzent, "growx");

		jpaWorkingOn.add(wlaJahresmenge, "growx");
		jpaWorkingOn.add(wnfJahresmenge, "growx");
		jpaWorkingOn.add(wlaJahresmengeprozent, "growx");
		jpaWorkingOn.add(wlaUeberproduktion, "growx");
		jpaWorkingOn.add(wnfUeberproduktion, "growx");

		WrapperLabel wlaUeberproduktionProzent = new WrapperLabel("%");
		wlaUeberproduktionProzent.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaUeberproduktionProzent, "growx");

		jpaWorkingOn.add(wlaLagermindeststand, "growx");
		jpaWorkingOn.add(wnfLagermindeststand, "growx");
		jpaWorkingOn.add(wlaLagermindestMengeneinheit, "growx");
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {
			jpaWorkingOn.add(wlaDetailprozentmindeststand, "growx");
			jpaWorkingOn.add(wnfDetailprozentmindeststand, "growx");

			WrapperLabel prozent = new WrapperLabel("%");
			prozent.setHorizontalAlignment(SwingConstants.LEFT);
			jpaWorkingOn.add(prozent, "left, growx, wrap");
		} else {
			jpaWorkingOn.add(new WrapperLabel(), "wrap");
		}

		jpaWorkingOn.add(wlaLagersollstand, "growx");
		jpaWorkingOn.add(wnfLagersollstand, "growx");
		jpaWorkingOn.add(wlaLagersollMengeneinheit, "growx, wrap");

		jpaWorkingOn.add(wlaFertigungssatzgrosse, "growx");
		jpaWorkingOn.add(wnfFertigungssatzgroesse, "growx");
		jpaWorkingOn.add(new WrapperLabel(""), "growx");
		jpaWorkingOn.add(wlaFertigungsVPE, "growx");
		jpaWorkingOn.add(wnfFertigungsVPE, "growx, wrap");

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void dto2Components() throws Throwable {
		wnfJahresmenge.setDouble(artikelDto.getFJahresmenge());
		wlaJahresmengeprozent.setText(artikelDto.getEinheitCNr().trim());

		wnfLagersollstand.setDouble(artikelDto.getFLagersoll());
		wnfLagermindeststand.setDouble(artikelDto.getFLagermindest());
		wnfFertigungssatzgroesse.setDouble(artikelDto
				.getFFertigungssatzgroesse());

		wnfVerschnitt.setDouble(artikelDto.getFVerschnittbasis());
		wnfVerschnittfaktor.setDouble(artikelDto.getFVerschnittfaktor());
		wnfUeberproduktion.setDouble(artikelDto.getFUeberproduktion());
		wnfFertigungsVPE.setDouble(artikelDto.getFFertigungsVpe());
		wlaVerschnittMengeneinheit.setText(artikelDto.getEinheitCNr().trim());
		wlaLagermindestMengeneinheit.setText(artikelDto.getEinheitCNr().trim());
		wlaLagersollMengeneinheit.setText(artikelDto.getEinheitCNr().trim());
		wlaFertigungssatzgroesseMengeneinheit.setText(artikelDto
				.getEinheitCNr().trim());
		wnfDetailprozentmindeststand.setDouble(artikelDto
				.getFDetailprozentmindeststand());

		wsfHersteller.setKey(artikelDto.getHerstellerIId());

		wtfArtikelnummer.setText(artikelDto.getCNr());

		wtfHerstellerBez.setText(artikelDto.getCArtikelbezhersteller());
		wtfHerstellerNr.setText(artikelDto.getCArtikelnrhersteller());

		if (artikelDto.getArtikelsprDto() != null) {
			wtfArtikelBez.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfArtikelZBez.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfArtikelZBez2.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}
		wdfLetzterzugang.setTimestamp(DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.getDatumLetzterZugangsOderAbgangsbuchung(artikelDto.getIId(),
						false));
		wdfLetzterabgang.setTimestamp(DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.getDatumLetzterZugangsOderAbgangsbuchung(artikelDto.getIId(),
						true));
		this.setStatusbarPersonalIIdAendern(artikelDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(artikelDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artikelDto.getTAnlegen());
		this.setStatusbarTAendern(artikelDto.getTAendern());

	}

	protected void components2Dto() throws ExceptionLP {
		artikelDto.setFVerschnittbasis(wnfVerschnitt.getDouble());
		artikelDto.setFVerschnittfaktor(wnfVerschnittfaktor.getDouble());
		artikelDto.setFUeberproduktion(wnfUeberproduktion.getDouble());
		artikelDto.setFJahresmenge(wnfJahresmenge.getDouble());
		artikelDto.setFFertigungsVpe(wnfFertigungsVPE.getDouble());
		artikelDto.setFLagersoll(wnfLagersollstand.getDouble());
		artikelDto.setFLagermindest(wnfLagermindeststand.getDouble());
		artikelDto.setFFertigungssatzgroesse(wnfFertigungssatzgroesse
				.getDouble());
		artikelDto.setFDetailprozentmindeststand(wnfDetailprozentmindeststand
				.getDouble());
		artikelDto.setCArtikelbezhersteller(wtfHerstellerBez.getText());
		artikelDto.setCArtikelnrhersteller(wtfHerstellerNr.getText());

		artikelDto.setHerstellerIId(wsfHersteller.getIKey());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getArtikelDelegate()
					.updateArtikel(artikelDto);
		}
		super.eventActionSave(e, true);
	}
}
