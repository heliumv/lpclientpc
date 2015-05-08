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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelDialogChargennummer;
import com.lp.client.artikel.PanelDialogSeriennummer;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCHNRField;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.14 $
 */
public class PanelDialogNachtraeglicheEntnahme extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelDialogSeriennummer pdSeriennummer = null;

	private ArtikelDto artikelDto = null;
	private LagerDto lagerDto = null;
	private MontageartDto montageartDto = null;

	private Integer losIId = null;
	private LossollmaterialDto lossollmaterialDto = null;
	private PanelQuery panelQueryLossollmaterial = null;

	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private WrapperRadioButton wrbZugang = new WrapperRadioButton();
	private WrapperRadioButton wrbAbgang = new WrapperRadioButton();

	private WrapperButton wbuArtikel = null;
	private WrapperButton wbuLager = null;
	private WrapperButton wbuMontageart = null;
	private WrapperButton wbuLossollmaterial = null;
	private WrapperButton wbuEntnahme = null;

	private WrapperTextField wtfArtikelnummer = null;
	private WrapperTextField wtfArtikelbezeichnung = null;
	private WrapperLabel wlaMenge = null;
	private WrapperNumberField wnfMenge = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperTextField wtfLager = null;
	private WrapperTextField wtfMontageart = null;
	private WrapperNumberField wnfSollmaterialmenge = null;
	private WrapperTextField wtfSollmaterialArtikelnummer = null;
	private WrapperTextField wtfSollmaterialArtikelbezeichnung = null;
	private WrapperLabel wlaSollmaterialMenge = null;
	private WrapperLabel wlaSollmaterialEinheit = null;
	private WrapperLabel wlaFehlmenge = null;
	private WrapperNumberField wnfFehlmenge = null;
	private WrapperLabel wlaFehlmengeEinheit = null;

	private WrapperSnrChnrField wtfSeriennr = new WrapperSnrChnrField(
			getInternalFrame(),false);
	private WrapperCheckBox wcbReduziereFehlmenge = null;

	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRArtikellager = null;
	private PanelQueryFLR panelQueryFLRMontageart = null;
	private PanelQueryFLR panelQueryFLRLossollmaterial = null;

	private WrapperLabel wlaLagerstand = null;

	private final static String ACTION_SPECIAL_ENTNAHME = "action_special_"
			+ ALWAYSENABLED + "gebe_nachtraeglich_material_aus";
	private static final String ACTION_SPECIAL_ARTIKEL = "action_special_los_artikel";
	private static final String ACTION_SPECIAL_LAGER = "action_special_los_lager";
	private static final String ACTION_SPECIAL_MONTAGEART = "action_special_los_montageart";
	private static final String ACTION_SPECIAL_SOLLMATERIAL = "action_special_los_sollmaterial";

	public PanelDialogNachtraeglicheEntnahme(InternalFrame internalFrame,
			PanelQuery panelQueryLossollmaterial, String title, Integer losIId,
			LossollmaterialDto lossollmaterialDto) throws Throwable {
		super(internalFrame, title);
		this.losIId = losIId;
		this.lossollmaterialDto = lossollmaterialDto;
		this.panelQueryLossollmaterial = panelQueryLossollmaterial;
		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		wbuArtikel = new WrapperButton();
		wbuLager = new WrapperButton();
		wbuMontageart = new WrapperButton();
		wbuEntnahme = new WrapperButton();

		wtfArtikelnummer = new WrapperTextField();
		wtfArtikelbezeichnung = new WrapperTextField();
		wlaMenge = new WrapperLabel();
		wnfMenge = new WrapperNumberField();
		wlaEinheit = new WrapperLabel();
		wtfLager = new WrapperTextField();
		wtfMontageart = new WrapperTextField();
		wbuLossollmaterial = new WrapperButton();
		wnfSollmaterialmenge = new WrapperNumberField();
		wtfSollmaterialArtikelnummer = new WrapperTextField();
		wtfSollmaterialArtikelbezeichnung = new WrapperTextField();
		wlaSollmaterialMenge = new WrapperLabel();
		wlaSollmaterialEinheit = new WrapperLabel();
		wlaFehlmengeEinheit = new WrapperLabel();

		wcbReduziereFehlmenge = new WrapperCheckBox();
		wlaLagerstand = new WrapperLabel();
		wlaFehlmenge = new WrapperLabel();
		wnfFehlmenge = new WrapperNumberField();

		wrbZugang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.lagerzugang"));
		wrbAbgang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.lagerabgang"));
		buttonGroup1.add(wrbZugang);
		buttonGroup1.add(wrbAbgang);
		wrbAbgang.setSelected(true);
		wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikel"));
		wbuArtikel.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikel.tooltip"));
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLager.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager.tooltip"));
		wbuMontageart.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.montageart"));
		wbuMontageart.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.montageart.tooltip"));
		wbuLossollmaterial.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.sollposition"));
		wbuLossollmaterial.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.sollposition.tooltip"));
		wbuEntnahme.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.button.entnahme"));
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		wlaSollmaterialMenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.menge"));
		wcbReduziereFehlmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.fehlmengereduzieren"));
		wlaFehlmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.fehlmenge"));

		wbuArtikel.addActionListener(this);
		wbuEntnahme.addActionListener(this);
		wbuLager.addActionListener(this);
		wbuMontageart.addActionListener(this);
		wbuLossollmaterial.addActionListener(this);
		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL);
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER);
		wbuMontageart.setActionCommand(ACTION_SPECIAL_MONTAGEART);
		wbuLossollmaterial.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL);

		wbuEntnahme.setActionCommand(ACTION_SPECIAL_ENTNAHME);

		wtfArtikelbezeichnung.setActivatable(false);
		wtfArtikelnummer.setActivatable(false);
		wtfLager.setActivatable(false);
		wtfMontageart.setActivatable(false);
		wtfSollmaterialArtikelbezeichnung.setActivatable(false);
		wtfSollmaterialArtikelnummer.setActivatable(false);
		wnfSollmaterialmenge.setActivatable(false);
		wnfFehlmenge.setActivatable(false);

		wtfArtikelnummer.setMandatoryFieldDB(true);
		wtfLager.setMandatoryFieldDB(true);
		wtfMontageart.setMandatoryFieldDB(true);
		wnfMenge.setMandatoryFieldDB(true);
		wnfMenge.setFractionDigits(3);

		wtfSeriennr.setWnfBelegMenge(wnfMenge);

		wnfSollmaterialmenge.setFractionDigits(3);
		wnfFehlmenge.setFractionDigits(3);
		wnfMenge.setMaximumIntegerDigits(8);
		wnfSollmaterialmenge.setMaximumIntegerDigits(8);
		wnfFehlmenge.setMaximumIntegerDigits(8);

		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSollmaterialEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaFehlmengeEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLagerstand.setHorizontalAlignment(SwingConstants.LEFT);

		wbuArtikel.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wbuArtikel.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wtfArtikelnummer.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfArtikelnummer.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaEinheit.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wlaEinheit.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));

		getInternalFrame().addItemChangedListener(this);

		boolean hatRecht = DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						RechteFac.RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN);

		if (((InternalFrameFertigung) getInternalFrame()).getTabbedPaneLos()
				.getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_READ)
				&& hatRecht == true) {

			wbuLossollmaterial.setEnabled(false);
			wbuArtikel.setEnabled(false);
			wnfFehlmenge.setEditable(false);
			wnfSollmaterialmenge.setEditable(false);

		}

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuLossollmaterial, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSollmaterialArtikelnummer, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSollmaterialArtikelbezeichnung,
				new GridBagConstraints(2, iZeile, 2, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSollmaterialMenge, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSollmaterialmenge, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSollmaterialEinheit, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFehlmenge, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFehlmenge, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFehlmengeEinheit, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelbezeichnung, new GridBagConstraints(2,
				iZeile, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAbgang, new GridBagConstraints(3, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wrbZugang, new GridBagConstraints(3, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 150, 2, 2), 50, 0));
		iZeile++;
		jpaWorkingOn.add(wcbReduziereFehlmenge, new GridBagConstraints(3,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLagerstand, new GridBagConstraints(2, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuMontageart, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMontageart, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuEntnahme, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfSeriennr.getButtonSnrAuswahl(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfSeriennr, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
		wtfSeriennr.setVisible(false);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_ENTNAHME)) {
			if (allMandatoryFieldsSetDlg()) {
				if (lossollmaterialDto == null) {
					lossollmaterialDto = new LossollmaterialDto();

					lossollmaterialDto.setBNachtraeglich(Helper
							.boolean2Short(true));
					lossollmaterialDto
							.setEinheitCNr(artikelDto.getEinheitCNr());
					lossollmaterialDto.setLosIId(losIId);
					lossollmaterialDto.setMontageartIId(montageartDto.getIId());

					BigDecimal bdSollpreis = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getGemittelterGestehungspreisEinesLagers(
									artikelDto.getIId(), lagerDto.getIId());
					lossollmaterialDto.setNSollpreis(bdSollpreis);
					lossollmaterialDto.setNMenge(new BigDecimal(0));
				} else {
					lossollmaterialDto.setNMenge(new BigDecimal(0));
				}
				lossollmaterialDto.setArtikelIId(artikelDto.getIId());

				LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
				losistmaterialDto.setLagerIId(lagerDto.getIId());
				losistmaterialDto.setBAbgang(wrbAbgang.getShort());
				losistmaterialDto.setNMenge(wnfMenge.getBigDecimal());

				List<SeriennrChargennrMitMengeDto> listSnrChnr = null;

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())
						|| Helper.short2boolean(artikelDto
								.getBSeriennrtragend())) {
					listSnrChnr = wtfSeriennr.getSeriennummern();
				}

				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.gebeMaterialNachtraeglichAus(lossollmaterialDto,
								losistmaterialDto, listSnrChnr,
								wcbReduziereFehlmenge.isSelected());
				// refresh auf die liste, damit mans gleich aktuell sieht
				panelQueryLossollmaterial.eventYouAreSelected(false);
				// Dialog schliessen
				getInternalFrame().closePanelDialog();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL)) {
			if (lossollmaterialDto != null
					&& lossollmaterialDto.getIId() != null) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						LPMain.getInstance().getTextRespectUISPr(
								"fert.hint.artikelbeziehtsichaufsollposition"));
			} else {
				dialogQueryArtikel(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER)) {
			dialogQueryLager(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MONTAGEART)) {
			dialogQueryMontageart(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SOLLMATERIAL)) {
			dialogQueryLossollmaterial(e);
		}/*
		 * else if (e.getActionCommand().equals(
		 * ACTION_SPECIAL_SERIENCHARGENNUMMER)) { if (artikelDto != null &&
		 * lagerDto != null) { if (wnfMenge.getBigDecimal() != null) { if
		 * (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
		 * pdSeriennummer = new PanelDialogSeriennummer( getInternalFrame(),
		 * LPMain.getInstance().getTextRespectUISPr( "artikel.seriennummer"),
		 * getTableSerienchargennummer(),
		 * Helper.erzeugeStringArrayAusString(wtfSeriennummer .getText()),
		 * artikelDto.getIId(), lagerDto.getIId(), wnfMenge.getDouble(), true);
		 * getInternalFrame().showPanelDialog(pdSeriennummer); } else if
		 * (Helper.short2boolean(artikelDto .getBChargennrtragend())) { //
		 * zusaetzliche Menge Double ddZusaetzlicheMenge = new Double(wnfMenge
		 * .getDouble().doubleValue()); pdSeriennummer = new
		 * PanelDialogChargennummer( getInternalFrame(), LPMain.getInstance()
		 * .getTextRespectUISPr( "artikel.chargennummer"),
		 * getTableSerienchargennummer(), new String[0], artikelDto.getIId(),
		 * lagerDto.getIId(), ddZusaetzlicheMenge, new Double(0), true);
		 * getInternalFrame().showPanelDialog(pdSeriennummer); } } else {
		 * DialogFactory .showModalDialog(
		 * LPMain.getInstance().getTextRespectUISPr( "lp.hint"),
		 * LPMain.getInstance().getTextRespectUISPr(
		 * "fert.hint.mengeeingeben")); } } else {
		 * DialogFactory.showModalDialog(
		 * LPMain.getInstance().getTextRespectUISPr("lp.hint"),
		 * LPMain.getInstance().getTextRespectUISPr(
		 * "fert.hint.zuerstartikelundlagerwaehlen")); } }
		 */
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
			if (e.getSource() == panelQueryFLRArtikellager) {
				WwArtikellagerPK key = (WwArtikellagerPK) ((ISourceEvent) e
						.getSource()).getIdSelected();
				holeLager(key.getLager_i_id());
			} else if (e.getSource() == panelQueryFLRArtikel) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				holeArtikel(key);
			} else if (e.getSource() == panelQueryFLRMontageart) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				holeMontageart(key);
			} else if (e.getSource() == panelQueryFLRLossollmaterial) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeLossollmaterial((Integer) key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {
				holeArtikel(null);
			} else if (e.getSource() == panelQueryFLRLossollmaterial) {
				holeLossollmaterial(null);
				// lt. wh dann mi 0 vorbesetzen
				wnfMenge.setBigDecimal(new BigDecimal(0));
			}
		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryArtikel(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(),
						(artikelDto != null) ? artikelDto.getIId() : null,
						false);
		new DialogQuery(panelQueryFLRArtikel);
	}

	private void dialogQueryLager(ActionEvent e) throws Throwable {
		panelQueryFLRArtikellager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						(lagerDto != null) ? lagerDto.getIId() : null);

		Integer artikelIId = null;
		if (artikelDto != null) {
			artikelIId = artikelDto.getIId();
		}

		panelQueryFLRArtikellager = new PanelQueryFLR(null,
				ArtikelFilterFactory.getInstance().createFKArtikellager(
						artikelIId), QueryParameters.UC_ID_ARTIKELLAGER, null,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"label.lager"));
		panelQueryFLRArtikellager
				.befuellePanelFilterkriterienDirektUndVersteckte(null, null,
						ArtikelFilterFactory.getInstance().createFKVLager());
		if (artikelDto != null && lagerDto != null) {
			panelQueryFLRArtikellager.setSelectedId(new WwArtikellagerPK(
					artikelDto.getIId(), lagerDto.getIId()));
		}
		new DialogQuery(panelQueryFLRArtikellager);
	}

	void dialogQueryMontageart(ActionEvent e) throws Throwable {
		panelQueryFLRMontageart = StuecklisteFilterFactory
				.getInstance()
				.createPanelFLRMontageart(getInternalFrame(),
						(montageartDto != null) ? montageartDto.getIId() : null);
		new DialogQuery(panelQueryFLRMontageart);
	}

	private void dialogQueryLossollmaterial(ActionEvent e) throws Throwable {
		panelQueryFLRLossollmaterial = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollmaterial(
						getInternalFrame(),
						losIId,
						(lossollmaterialDto != null) ? lossollmaterialDto
								.getIId() : null, true);
		new DialogQuery(panelQueryFLRLossollmaterial);
	}

	private void dto2ComponentsArtikel() throws Throwable {
		if (artikelDto != null) {
			wtfArtikelnummer.setText(artikelDto.getCNr());
			wtfArtikelbezeichnung.setText(artikelDto.getArtikelsprDto()
					.getCBez());
			wlaEinheit.setText(artikelDto.getEinheitCNr().trim());

			Integer lagerIId = null;
			if (lagerDto != null) {
				lagerIId = lagerDto.getIId();
			}

			wtfSeriennr.setArtikelIdLagerId(artikelDto, lagerIId);

			if (Helper.short2boolean(artikelDto.getBChargennrtragend())
					|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				wtfSeriennr.setVisible(true);
				wtfSeriennr.getButtonSnrAuswahl().setVisible(true);
				wtfSeriennr.setMandatoryFieldDB(true);
				wtfSeriennr.setMandatoryField(true);
				wnfMenge.setActivatable(false);
				wnfMenge.setActivatable(false);
				wnfMenge.setEditable(false);

			}
		} else {
			wtfArtikelnummer.setText(null);
			wtfArtikelbezeichnung.setText(null);
			wlaEinheit.setText("");
			wtfSeriennr.setVisible(false);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
			wtfSeriennr.setMandatoryFieldDB(false);
			wtfSeriennr.setMandatoryField(false);
			wnfMenge.setActivatable(true);
			wnfMenge.setEditable(true);
		}
		updateLagerstand();
	}

	private void dto2ComponentsLossollmaterial() throws Throwable {
		if (lossollmaterialDto != null) {
			wtfSollmaterialArtikelnummer.setText(artikelDto.getCNr());
			wtfSollmaterialArtikelbezeichnung.setText(artikelDto
					.getArtikelsprDto().getCBez());
			wlaSollmaterialEinheit.setText(artikelDto.getEinheitCNr().trim());
			wlaFehlmengeEinheit.setText(artikelDto.getEinheitCNr().trim());
			wnfSollmaterialmenge.setBigDecimal(lossollmaterialDto.getNMenge());
			ArtikelfehlmengeDto fmDto = DelegateFactory
					.getInstance()
					.getFehlmengeDelegate()
					.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
							LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId());
			if (fmDto != null) {
				wnfFehlmenge.setBigDecimal(fmDto.getNMenge());
				wcbReduziereFehlmenge.setSelected(true);
				wcbReduziereFehlmenge.setEnabled(true);
			} else {
				wnfFehlmenge.setBigDecimal(new BigDecimal(0));
				wcbReduziereFehlmenge.setSelected(false);
				wcbReduziereFehlmenge.setEnabled(false);
			}
		} else {
			wtfSollmaterialArtikelnummer.setText(null);
			wtfSollmaterialArtikelbezeichnung.setText(null);
			wlaSollmaterialEinheit.setText("");
			wlaFehlmengeEinheit.setText("");
			wnfSollmaterialmenge.setBigDecimal(null);
			wnfFehlmenge.setBigDecimal(null);
			wcbReduziereFehlmenge.setSelected(false);
			wcbReduziereFehlmenge.setEnabled(false);
		}
	}

	private void dto2ComponentsLager() throws Throwable {
		if (lagerDto != null) {
			wtfLager.setText(lagerDto.getCNr());
			if (artikelDto != null) {
				wtfSeriennr.setArtikelIdLagerId(artikelDto, lagerDto.getIId());
			}
		} else {
			wtfLager.setText(null);
		}
		updateLagerstand();
	}

	private void dto2ComponentsMontageart() {
		if (montageartDto != null) {
			wtfMontageart.setText(montageartDto.getCBez());
		} else {
			wtfMontageart.setText(null);
		}
	}

	private void holeLager(Integer key) throws Throwable {
		if (key != null) {
			lagerDto = DelegateFactory.getInstance().getLagerDelegate()
					.lagerFindByPrimaryKey((Integer) key);
		} else {
			lagerDto = null;
		}
		dto2ComponentsLager();
	}

	private void holeArtikel(Integer key) throws Throwable {
		if (key != null) {
			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey((Integer) key);
		} else {
			artikelDto = null;
		}
		dto2ComponentsArtikel();
	}

	private void holeMontageart(Integer key) throws Throwable {
		if (key != null) {
			montageartDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.montageartFindByPrimaryKey((Integer) key);
		} else {
			montageartDto = null;
		}
		dto2ComponentsMontageart();
	}

	private void holeLossollmaterial(Integer key) throws Throwable {
		if (key != null) {
			lossollmaterialDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(key);
			holeArtikel(lossollmaterialDto.getArtikelIId());
		} else {
			lossollmaterialDto = null;
		}
		dto2ComponentsLossollmaterial();
	}

	private void setDefaults() throws Throwable {
		if (lossollmaterialDto != null) {
			holeLossollmaterial(lossollmaterialDto.getIId());
			holeMontageart(lossollmaterialDto.getMontageartIId());
		} else {
			MontageartDto[] dtos = DelegateFactory.getInstance()
					.getStuecklisteDelegate().montageartFindByMandantCNr();

			if (dtos != null && dtos.length > 0) {
				holeMontageart(dtos[0].getIId());
			}
		}
		LoslagerentnahmeDto[] laeger = DelegateFactory.getInstance()
				.getFertigungDelegate().loslagerentnahmeFindByLosIId(losIId);
		if (laeger.length > 0) {
			holeLager(laeger[0].getLagerIId());

			if (artikelDto != null
					&& Helper.short2boolean(artikelDto
							.getBLagerbewirtschaftet())) {

				wtfSeriennr.setArtikelIdLagerId(artikelDto,
						laeger[0].getLagerIId());

				// PJ 15021
				BigDecimal bdLagerstand = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerstand(artikelDto.getIId(),
								laeger[0].getLagerIId());

				if (bdLagerstand.doubleValue() <= 0) {

					for (int i = 1; i < laeger.length; i++) {

						BigDecimal bdLs = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.getLagerstand(artikelDto.getIId(),
										laeger[i].getLagerIId());
						if (bdLs.doubleValue() > 0) {
							holeLager(laeger[i].getLagerIId());
							break;
						}

					}

				}
			}

		}

		// Wenn Ist-Material groeszer 1 und alle das selbe lager haben, dann
		// nimm
		// dieses
		if (lossollmaterialDto != null) {
			LosistmaterialDto[] dtos = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.losistmaterialFindByLossollmaterialIId(
							lossollmaterialDto.getIId());
			if (dtos.length > 0) {
				Integer lagerIId = null;
				for (int i = 0; i < dtos.length; i++) {

					if (lagerIId == null) {
						lagerIId = dtos[i].getLagerIId();
					} else {

						if (!lagerIId.equals(dtos[i].getLagerIId())) {
							lagerIId = null;
							break;
						}
					}
				}
				if (lagerIId != null) {
					holeLager(lagerIId);
				}
			}
		}

		// default auf fehlmenge setzen
		if (lossollmaterialDto != null) {
			ArtikelfehlmengeDto artikelFehlmengeDto = DelegateFactory
					.getInstance()
					.getFehlmengeDelegate()
					.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
							LocaleFac.BELEGART_LOS, lossollmaterialDto.getIId());
			if (artikelFehlmengeDto != null) {
				wcbReduziereFehlmenge.setSelected(true);
			}
		}
	}

	private void updateLagerstand() throws Throwable {
		if (lagerDto != null && artikelDto != null) {

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

				BigDecimal bdLagerstand = DelegateFactory.getInstance()
						.getLagerDelegate()
						.getLagerstand(artikelDto.getIId(), lagerDto.getIId());
				String sLagerstand = Helper.formatZahl(bdLagerstand, 3, LPMain
						.getInstance().getTheClient().getLocUi());
				wlaLagerstand.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.lagerstand")
						+ " " + sLagerstand + " " + artikelDto.getEinheitCNr());
				// entweder Lagerstand oder fehlmenge vorschlagen
				BigDecimal bdMenge;
				if (lossollmaterialDto != null
						&& lossollmaterialDto.getIId() != null) {
					ArtikelfehlmengeDto artikelFehlmengeDto = DelegateFactory
							.getInstance()
							.getFehlmengeDelegate()
							.artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
									LocaleFac.BELEGART_LOS,
									lossollmaterialDto.getIId());
					if (artikelFehlmengeDto != null) {
						BigDecimal bdFehlmenge = artikelFehlmengeDto
								.getNMenge();
						if (bdLagerstand.compareTo(bdFehlmenge) > 0) {
							// wenn genug auf Lager, dann die fehlmenge
							// vorschlagen
							bdMenge = bdFehlmenge;
						} else {
							// sonst das was auf lager ist
							bdMenge = bdLagerstand;
						}
					} else {
						bdMenge = new BigDecimal(0);
					}
					wnfMenge.setBigDecimal(bdMenge);
				}
				// ohne bezug zu einer sollposition wird menge 0 vorgeschlagen
				else {
					wnfMenge.setBigDecimal(new BigDecimal(0));
				}
			} else {
				wlaLagerstand.setText(LPMain.getInstance().getTextRespectUISPr(
						"artikel.error.artikelnichtlagerbewirtschaftet"));
			}
		} else {
			wlaLagerstand.setText("");
		}
	}

	/**
	 * Alle Serien- oder Chargennummern in einer Tabelle sammeln.
	 * 
	 * @deprecated MB mit UW eine gemeinsame methode bauen
	 * 
	 * @return WrapperTable die Tabelle
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	private WrapperTable getTableSerienchargennummer() throws Throwable {
		QueryType[] qt = null;
		FilterKriterium[] fk = ArtikelFilterFactory.getInstance()
				.createFKArtikelSNR(artikelDto.getIId(), lagerDto.getIId());

		int iIdUsecase = -1;
		String sTitle = null;

		if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
			iIdUsecase = QueryParameters.UC_ID_SERIENNUMMERNCHARGENNUMMERNAUFLAGER;
			sTitle = LPMain.getInstance().getTextRespectUISPr(
					"artikel.seriennummer");
		} else if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
			iIdUsecase = QueryParameters.UC_ID_CHARGENAUFLAGER;
			sTitle = LPMain.getInstance().getTextRespectUISPr(
					"artikel.chargennummer");
		}

		String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN };

		PanelQuery pqDummy = new PanelQuery(qt, fk, iIdUsecase,
				aWhichButtonIUse, getInternalFrame(), sTitle,
				artikelDto.getIId());

		return (WrapperTable) pqDummy.getTable();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLossollmaterial;
	}
}
