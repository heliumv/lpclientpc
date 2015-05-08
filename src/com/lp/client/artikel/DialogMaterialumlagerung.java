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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogMaterialumlagerung extends JDialog implements
		ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	private WrapperRadioButton wrbLos = new WrapperRadioButton();
	private WrapperRadioButton wrbLager = new WrapperRadioButton();

	private ButtonGroup bgOption = new ButtonGroup();

	private LosDto losDtoUmzubuchen = null;

	private WrapperButton wbuLager = new WrapperButton();

	WrapperSnrChnrField wtfSnrChnr = null;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaMaterialInsLosZubuchen = new WrapperLabel();
	private WrapperLabel wlaMaterialVonLosAbbuchen = new WrapperLabel();

	private WrapperLabel wlaVonLos = new WrapperLabel();
	private WrapperLabel wlaOder = new WrapperLabel();

	private WrapperSelectField wsfArtikel = null;
	private WrapperTextField wtfArtikelZusatzbezeichnung = new WrapperTextField();

	private WrapperSelectField wsfLagerRueckbuchung = null;

	private WrapperTextField wtfLager = new WrapperTextField();
	private LossollmaterialDto lossollmaterialDto = null;

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	private JPanel panelMaterialAufLos = new JPanel();
	private JPanel panelMaterialVonLos = new JPanel();

	private BigDecimal diff = null;

	private Integer lagerIIdAbbuchung = null;

	private Integer lossollmaterialIIdBucheNach = null;

	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRLossollmaterial = null;

	private PanelQueryFLR panelQueryFLRLos = null;
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";
	private Integer lagerIId = null;
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";

	private LosDto losDto = null;
	private InternalFrameArtikel internalFrame = null;
	private ArtikelDto artikelDto = null;

	private Integer lagerIId_Wareneingang = null;
	boolean bZuviel = true;

	public DialogMaterialumlagerung(Integer losollmaterialIId,
			InternalFrameArtikel internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ww.lagercockpit.material.umbuchen"), true);
		this.internalFrame = internalFrame;
		lossollmaterialDto = DelegateFactory.getInstance()
				.getFertigungDelegate()
				.lossollmaterialFindByPrimaryKey(losollmaterialIId);

		diff = lossollmaterialDto.getNMenge().subtract(
				DelegateFactory.getInstance().getFertigungDelegate()
						.getAusgegebeneMenge(lossollmaterialDto.getIId()));
		LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
				.losFindByPrimaryKey(lossollmaterialDto.getLosIId());
		
		
		this.artikelDto =DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(lossollmaterialDto.getArtikelIId());
		
		this.losDto = losDto;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();

		if (diff != null) {
			wnfMenge.setBigDecimal(diff.abs());

			if (diff.doubleValue() > 0) {
				// Zuwenig -> Lager an Los
				// Wenn Material nur auf einem Lager, dann soll dies
				// vorgeschlagen werden
				LagerDto lagerDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(
								lossollmaterialDto.getArtikelIId());
				if (lagerDto != null) {
					wtfLager.setText(lagerDto.getCNr());
					lagerIIdAbbuchung = lagerDto.getIId();
					BigDecimal lagerstand = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(lossollmaterialDto.getArtikelIId(),
									lagerIIdAbbuchung);

					
					if (artikelDto.istArtikelSnrOderchargentragend()) {
						wtfSnrChnr.setArtikelIdLagerId(artikelDto, lagerIIdAbbuchung);
					}
					
					if (lagerstand.doubleValue() < diff.doubleValue()) {
						wnfMenge.setBigDecimal(lagerstand);
					}
				}

			} else {

				LagerDto lagerDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerDesErstenArtikellagerplatzes(
								lossollmaterialDto.getArtikelIId());
				if (lagerDto != null) {
					wsfLagerRueckbuchung.setKey(lagerDto.getIId());

				}
			}

		}

		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(500, 300);

	}

	void dialogQueryLagerFromListe() throws Throwable {

		if (lossollmaterialDto.getArtikelIId() != null) {
			panelQueryFLRLager = new PanelQueryFLR(null, ArtikelFilterFactory
					.getInstance().createFKArtikellager(
							lossollmaterialDto.getArtikelIId()),
					QueryParameters.UC_ID_ARTIKELLAGER, null, internalFrame,
					LPMain.getInstance().getTextRespectUISPr("label.lager"));
			panelQueryFLRLager.befuellePanelFilterkriterienDirektUndVersteckte(
					null, null, ArtikelFilterFactory.getInstance()
							.createFKVLager());
			if (lossollmaterialDto.getArtikelIId() != null && lagerIId != null) {
				panelQueryFLRLager.setSelectedId(new WwArtikellagerPK(
						lossollmaterialDto.getArtikelIId(), lagerIId));
			}
			new DialogQuery(panelQueryFLRLager);

		}
	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRLager) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					// PJ 17798
					if (key instanceof WwArtikellagerPK) {
						key = ((WwArtikellagerPK) key).getLager_i_id();
					}
					LagerDto lagerDto = DelegateFactory.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey((Integer) key);
					wtfLager.setText(lagerDto.getCNr());
					lagerIIdAbbuchung = lagerDto.getIId();

					BigDecimal lagerstand = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(lossollmaterialDto.getArtikelIId(),
									lagerIIdAbbuchung);

					if (artikelDto.istArtikelSnrOderchargentragend()) {
						wtfSnrChnr.setSeriennummern(null, artikelDto, lagerIIdAbbuchung);
					}
					
					if (diff != null) {
						if (diff.abs().doubleValue() > lagerstand.doubleValue()) {
							wnfMenge.setBigDecimal(lagerstand);
						}
					}

				} else if (e.getSource() == panelQueryFLRLos) {
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					losDtoUmzubuchen = DelegateFactory.getInstance()
							.getFertigungDelegate().losFindByPrimaryKey(key);
					String losProjekt = losDtoUmzubuchen.getCNr();
					if (losDtoUmzubuchen.getCProjekt() != null) {
						losProjekt += " " + losDtoUmzubuchen.getCProjekt();
					}

					wtfLos.setText(losProjekt);

				} else if (wsfLagerRueckbuchung != null
						&& e.getSource() == wsfLagerRueckbuchung
								.getPanelQueryFLR()) {

					wtfLos.setText(null);
					lossollmaterialIIdBucheNach = null;

					wsfLagerRueckbuchung.setMandatoryField(true);
					wtfLos.setMandatoryField(false);

				} else if (e.getSource() == panelQueryFLRLossollmaterial) {

					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();
					LossollmaterialDto sollmaterialDto = DelegateFactory
							.getInstance().getFertigungDelegate()
							.lossollmaterialFindByPrimaryKey(key);

					if (!sollmaterialDto.getArtikelIId().equals(
							lossollmaterialDto.getArtikelIId())) {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"ww.lagercockpit.materialumbuchung.error.gleichartikel"));
						return;
					}

					LosDto losDto = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.losFindByPrimaryKey(sollmaterialDto.getLosIId());
					String losProjekt = losDto.getCNr();
					if (losDto.getCProjekt() != null) {
						losProjekt += " " + losDto.getCProjekt();
					}

					wtfLos.setText(losProjekt);
					lossollmaterialIIdBucheNach = key;
					wsfLagerRueckbuchung.setKey(null);

					wsfLagerRueckbuchung.setMandatoryField(false);
					wtfLos.setMandatoryField(true);

				}
			}
		} catch (Throwable e) {
			internalFrame.handleException(e, false);
		}
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRBebuchbareLose(internalFrame, false, true,
						false, null, false);
		new DialogQuery(panelQueryFLRLos);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAbbrechen)) {

			this.setVisible(false);
			internalFrame.removeItemChangedListener(this);
		} else if (e.getSource().equals(wrbLager)) {
			wbuLager.setEnabled(true);
			wbuLager.setMandatoryField(true);
			wtfLos.setMandatoryField(false);
			wsfLagerRueckbuchung.setMandatoryField(true);
			wbuLos.setEnabled(false);

			try {
				LagerDto lDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerDesErstenArtikellagerplatzes(
								lossollmaterialDto.getArtikelIId());

				if (lDto != null) {
					wbuLager.setText(lDto.getCNr());
				}

			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}

		} else if (e.getSource().equals(wrbLos)) {
			wsfLagerRueckbuchung.setEnabled(false);
			wsfLagerRueckbuchung.setMandatoryField(false);
			wtfLos.setMandatoryField(true);
			wbuLos.setEnabled(true);
		} else if (e.getSource().equals(wbuLager)) {
			try {
				dialogQueryLagerFromListe();
			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}

		} else if (e.getSource().equals(wbuLos)) {
			try {
				dialogQueryLosFromListe(e);
			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}

		} else if (e.getSource().equals(btnOK)) {
			try {

				if (artikelDto.istArtikelSnrOderchargentragend() && diff.doubleValue() > 0 ) {
					if (wnfMenge.getBigDecimal() == null || wtfSnrChnr.getText() == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				} else {
					if (wnfMenge.getBigDecimal() == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				}
				
				

				if (wnfMenge.getBigDecimal().doubleValue() > 0) {

					if (diff.doubleValue() > 0) {
						// Zuwenig -> Lager an Los

						LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
						losistmaterialDto
								.setLossollmaterialIId(lossollmaterialDto
										.getIId());
						losistmaterialDto.setLagerIId(lagerIIdAbbuchung);
						losistmaterialDto
								.setBAbgang(Helper.boolean2Short(true));
						losistmaterialDto.setNMenge(wnfMenge.getBigDecimal());
						
						if (artikelDto.istArtikelSnrOderchargentragend()) {
							DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.gebeMaterialNachtraeglichAus(
									lossollmaterialDto, losistmaterialDto,
									wtfSnrChnr.getSeriennummern(), true);

						} else {
							DelegateFactory
							.getInstance()
							.getFertigungDelegate()
							.gebeMaterialNachtraeglichAus(
									lossollmaterialDto, losistmaterialDto,
									null, true);

						}
						
						
					} else {

						// Zuviel -> Los an Lager oder Los an Los
						if (wrbLager.isSelected()) {
							// Ans Lager zurueckbuchen
							DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.vonSollpositionMengeZuruecknehmen(
											lossollmaterialDto.getIId(),
											wnfMenge.getBigDecimal(), null,
											wsfLagerRueckbuchung.getIKey());
						} else if (wrbLos.isSelected()) {
							if (wtfLos.getText() == null
									|| losDtoUmzubuchen == null) {
								DialogFactory
										.showModalDialog(
												LPMain.getTextRespectUISPr("lp.error"),
												LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
								return;
							}
							// Auf ein anderes Los buchen
							DelegateFactory
									.getInstance()
									.getFertigungDelegate()
									.vonSollpositionMengeZuruecknehmen(
											lossollmaterialDto.getIId(),
											wnfMenge.getBigDecimal(),
											losDtoUmzubuchen.getIId(), null);
						}
					}
				} else {

					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"lp.error.mengezuklein"));
					return;
				}

				List<SeriennrChargennrMitMengeDto> l = null;
				if (artikelDto.istArtikelSnrOderchargentragend()) {
					l = wtfSnrChnr.getSeriennummern();
				}

			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}
			this.setVisible(false);
			internalFrame.removeItemChangedListener(this);
			this.dispose();
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		// das Aussenpanel hat immer das Gridbaglayout.
		this.getContentPane().setLayout(new GridBagLayout());

		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		bgOption.add(wrbLos);
		bgOption.add(wrbLager);

		wrbLos.addActionListener(this);
		wrbLager.addActionListener(this);
		wrbLager.setSelected(true);

		wnfMenge.setMandatoryField(true);
		wbuLager = new WrapperButton();
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wlaOder.setText(LPMain.getInstance().getTextRespectUISPr(
				"ww.lagercockpit.materialumbuchung.oder"));
		internalFrame.addItemChangedListener(this);

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));

		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wtfLos.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title")
				+ "...");
		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wtfArtikelZusatzbezeichnung.setActivatable(false);
		wtfArtikelZusatzbezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);

		wsfLagerRueckbuchung = new WrapperSelectField(WrapperSelectField.LAGER,
				internalFrame, false);
		wsfLagerRueckbuchung.setMandatoryField(true);
		wbuLos.setEnabled(false);
		wtfLos.setActivatable(false);
		wsfLagerRueckbuchung.setText("Lager...");
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wtfSnrChnr = new WrapperSnrChnrField(internalFrame,false);
		wtfSnrChnr.setWnfBelegMenge(wnfMenge);
		
		wsfArtikel = new WrapperSelectField(
				WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, internalFrame,
				false);
		wsfArtikel.getWrapperGotoButton().setActivatable(false);
		wsfArtikel.setKey(lossollmaterialDto.getArtikelIId());
		int iZeile = 0;
		// Panel Material von Los
		GridBagLayout gbl = new GridBagLayout();
		panelMaterialVonLos.setLayout(gbl);
		GridBagLayout gbl2 = new GridBagLayout();
		panelMaterialAufLos.setLayout(gbl2);

		wlaMaterialInsLosZubuchen.setText(LPMain.getInstance()
				.getTextRespectUISPr("ww.lagercockpit.materialanslos")
				+ " ("
				+ losDto.getCNr() + ")");
		wlaMaterialVonLosAbbuchen.setText(LPMain.getInstance()
				.getTextRespectUISPr("ww.lagercockpit.materialablos")+ " ("
				+ losDto.getCNr() + ")");

		// NACHLOS
		int iZeileLosZubuchen = 0;
		wlaMaterialInsLosZubuchen.setHorizontalAlignment(SwingConstants.LEFT);
		panelMaterialAufLos.add(wlaMaterialInsLosZubuchen,
				new GridBagConstraints(0, iZeileLosZubuchen, 2, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeileLosZubuchen++;
		panelMaterialAufLos.add(wbuLager, new GridBagConstraints(0,
				iZeileLosZubuchen, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelMaterialAufLos.add(wtfLager, new GridBagConstraints(1,
				iZeileLosZubuchen, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeileLosZubuchen++;

		// VONLOS
		int iZeileLosAbbuchen = 0;
		wlaMaterialVonLosAbbuchen.setHorizontalAlignment(SwingConstants.LEFT);
		panelMaterialVonLos.add(wlaMaterialVonLosAbbuchen,
				new GridBagConstraints(0, iZeileLosAbbuchen, 3, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeileLosAbbuchen++;
		wlaVonLos.setHorizontalAlignment(SwingConstants.LEFT);
		panelMaterialVonLos.add(wlaVonLos, new GridBagConstraints(0,
				iZeileLosAbbuchen, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeileLosAbbuchen++;
		panelMaterialVonLos.add(wrbLager, new GridBagConstraints(0,
				iZeileLosAbbuchen, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelMaterialVonLos.add(wsfLagerRueckbuchung.getWrapperButton(),
				new GridBagConstraints(1, iZeileLosAbbuchen, 1, 1, 0, 0.1,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		panelMaterialVonLos.add(wsfLagerRueckbuchung.getWrapperTextField(),
				new GridBagConstraints(2, iZeileLosAbbuchen, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeileLosAbbuchen++;
		wlaOder.setHorizontalAlignment(SwingConstants.LEFT);
		panelMaterialVonLos.add(wlaOder, new GridBagConstraints(0,
				iZeileLosAbbuchen, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeileLosAbbuchen++;
		panelMaterialVonLos.add(wrbLos, new GridBagConstraints(0,
				iZeileLosAbbuchen, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelMaterialVonLos.add(wbuLos, new GridBagConstraints(1,
				iZeileLosAbbuchen, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelMaterialVonLos.add(wtfLos, new GridBagConstraints(2,
				iZeileLosAbbuchen, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeileLosAbbuchen++;

		// jetzt meine felder

		this.getContentPane().add(
				wsfArtikel.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						70, 0));
		this.getContentPane().add(
				wsfArtikel.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));
		this.getContentPane().add(
				wtfArtikelZusatzbezeichnung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		this.getContentPane().add(
				wlaMenge,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						70, 0));
		this.getContentPane().add(
				wnfMenge,
				new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 0, 0));
		
		if (artikelDto.istArtikelSnrOderchargentragend() && diff.doubleValue() > 0) {
			wnfMenge.setActivatable(false);
			wtfSnrChnr.setMandatoryField(true);
			wtfSnrChnr.setArtikelIdLagerId(artikelDto, lagerIIdAbbuchung);
			iZeile++;
			this.getContentPane().add(
					wtfSnrChnr.getButtonSnrAuswahl(),
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			this.getContentPane().add(
					wtfSnrChnr,
					new GridBagConstraints(1, iZeile, 2, 1, 0, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
		
		iZeile++;

		if (diff.doubleValue() < 0) {
			this.getContentPane().add(
					panelMaterialVonLos,
					new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		} else {
			this.getContentPane().add(
					panelMaterialAufLos,
					new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;

		this.getContentPane().add(
				btnOK,
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 100, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAbbrechen,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
