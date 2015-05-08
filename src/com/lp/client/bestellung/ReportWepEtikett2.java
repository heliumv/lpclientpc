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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperFormattedTextField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p>Report ReportWepEtikett <p>Copyright Logistik Pur Software GmbH (c)
 * 2004-2008</p> <p>Erstellungsdatum 28.11.07</p> <p> </p>
 * 
 * @author Victor Finder
 * 
 * @version $Revision: 1.17 $
 */
public class ReportWepEtikett2 extends ReportEtikett implements
		PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final public String ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE = "ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE";
	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	static final public String ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE = "action_lagerplatz_from_liste";

	private WareneingangDto wareneingangDto = null;
	private WareneingangspositionDto weposDto = null;
	private BestellpositionDto bestellpositionDto = null;
	private ArtikelDto artikelDto = null;
	private ArtikellieferantDto artikellieferantDto = null;
	private LandDto landDto = null;
	private WrapperLabel wlaVerpackungseinheit = null;
	private WrapperNumberField wnfVerpackungseinheit = null;
	private PanelQueryFLR panelQueryFLRWarenverkehrsnummer = null;
	private PanelQueryFLR panelQueryFLRLand = null;
	private WrapperButton wbuWarenverkehrsnummer = null;
	private WrapperFormattedTextField wtfWarenverkehrsnummer = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperButton wbuLagerplatz = null;
	private WrapperTextField wtfLagerplatz = null;
	private WrapperButton wbuUrsprungsland = null;
	private WrapperTextField wtfUrsprungsland = null;
	private WrapperLabel wlaKommentar = null;
	private WrapperTextField wtfKommentar = null;
	private WrapperLabel wlaHandmenge = null;
	private WrapperNumberField wnfHandmenge = null;

	private PanelQueryFLR panelQueryFLRLagerplatz = null;
	private PanelQueryFLR panelQueryFLRArtikellagerplatz = null;
	private Integer lagerplatzIId = null;

	// protected JPanel jpaWorkingOn = new JPanel();
	// private WrapperLabel wlaExemplare = null;
	// protected WrapperNumberField wnfExemplare = null;

	public ReportWepEtikett2(InternalFrame internalFrame,
			WareneingangDto wareneingangDto, WareneingangspositionDto weposDto,
			BestellpositionDto bestellpositionDto, ArtikelDto artikelDto,
			ArtikellieferantDto artikellieferantDto, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		this.wareneingangDto = wareneingangDto;
		this.weposDto = weposDto;
		this.bestellpositionDto = bestellpositionDto;
		this.artikelDto = artikelDto;
		this.artikellieferantDto = artikellieferantDto;
		jbInit();
		setDefaults();
	}

	public void unlock() throws Throwable {

		try {
			eventActionUnlock(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	public ReportWepEtikett2(InternalFrame internalFrame, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		jbInit();
		setDefaults();
		wnfExemplare.setEditable(true);
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// jpaWorkingOn.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		/*
		 * wlaExemplare = new WrapperLabel();
		 * wlaExemplare.setText(LPMain.getInstance().getTextRespectUISPr(
		 * "report.exemplare")); wlaExemplare.setMinimumSize(new Dimension(100,
		 * Defaults.getInstance() .getControlHeight()));
		 * wlaExemplare.setPreferredSize(new Dimension(100,
		 * Defaults.getInstance() .getControlHeight())); wnfExemplare = new
		 * WrapperNumberField(); wnfExemplare.setMinimumSize(new Dimension(30,
		 * Defaults.getInstance() .getControlHeight()));
		 * wnfExemplare.setPreferredSize(new Dimension(30,
		 * Defaults.getInstance() .getControlHeight()));
		 * wnfExemplare.setFractionDigits(0);
		 * wnfExemplare.setMaximumIntegerDigits(2);
		 */

		wlaVerpackungseinheit = new WrapperLabel();
		wlaVerpackungseinheit.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.verpackungseinheit"));
		wlaVerpackungseinheit.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaVerpackungseinheit.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));

		wnfVerpackungseinheit = new WrapperNumberField();
		wnfVerpackungseinheit.setMinimumSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		wnfVerpackungseinheit.setPreferredSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		wbuWarenverkehrsnummer = new WrapperButton();
		wbuWarenverkehrsnummer.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.warenverkehrsnummer")
				+ "...");
		wbuWarenverkehrsnummer
				.setActionCommand(ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE);
		wbuWarenverkehrsnummer.addActionListener(this);

		wtfWarenverkehrsnummer = new WrapperFormattedTextField();
		wtfWarenverkehrsnummer
				.setFormat(ArtikelFac.PATTERN_WARENVERKEHRSNUMMER);
		wlaGewicht = new WrapperLabel();
		wlaGewicht.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel();
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit.setHorizontalTextPosition(SwingConstants.TRAILING);
		wlaEinheit.setText("kg");

		wbuLagerplatz = new WrapperButton();
		wbuLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"));
		wbuLagerplatz.setActionCommand(ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		wbuLagerplatz.addActionListener(this);

		wtfLagerplatz = new WrapperTextField();
		wtfLagerplatz.setActivatable(false);

		wlaKommentar = new WrapperLabel();
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));
		wtfKommentar = new WrapperTextField();

		wlaHandmenge = new WrapperLabel();
		wlaHandmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.handmenge"));
		wnfHandmenge = new WrapperNumberField();

		wbuUrsprungsland = new WrapperButton();
		wbuUrsprungsland.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.ursprungsland")
				+ "...");
		wbuUrsprungsland.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuUrsprungsland.addActionListener(this);
		wtfUrsprungsland = new WrapperTextField();

		// this.setLayout(super.getLayout());
		/*
		 * jpaWorkingOn.add(wlaExemplare, new GridBagConstraints(0, iZeile, 1,
		 * 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new
		 * Insets(2, 2, 2, 2), 0, 0)); jpaWorkingOn.add(wnfExemplare, new
		 * GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
		 * GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2,
		 * 2, 2), 0, 0));
		 */
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVerpackungseinheit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerpackungseinheit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuWarenverkehrsnummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWarenverkehrsnummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuLagerplatz, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerplatz, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bHerstellerUrsprungsland = ((Boolean) parameter
				.getCWertAsObject());
		if (bHerstellerUrsprungsland == false) {
			iZeile++;
			jpaWorkingOn.add(wbuUrsprungsland,
					new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wtfUrsprungsland,
					new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaHandmenge, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfHandmenge, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		this.add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	protected void components2Dto() throws Throwable {
		artikelDto.setFGewichtkg(wnfGewicht.getDouble());
		if (landDto != null) {
			artikelDto.setLandIIdUrsprungsland(landDto.getIID());
		}
		artikelDto.setCWarenverkehrsnummer(wtfWarenverkehrsnummer
				.getFormattedText());
		if (artikellieferantDto != null)
			artikellieferantDto.setNVerpackungseinheit(wnfVerpackungseinheit
					.getBigDecimal());

	}

	protected void dto2Components() throws Throwable {

		if (artikelDto.getLandIIdUrsprungsland() != null) {
			landDto = DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
			wtfUrsprungsland.setText(landDto.getCName());
		}
		wnfGewicht.setDouble(artikelDto.getFGewichtkg());
		wtfWarenverkehrsnummer.setText(artikelDto.getCWarenverkehrsnummer());
		if (artikellieferantDto != null)
			wnfVerpackungseinheit.setBigDecimal(artikellieferantDto
					.getNVerpackungseinheit());

		ArtikellagerplaetzeDto dto = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.artikellagerplaetzeFindByArtikelIIdLagerIId(
						artikelDto.getIId(), wareneingangDto.getLagerIId());
		if (dto != null) {
			wtfLagerplatz.setText(dto.getLagerplatzDto().getCLagerplatz());
			lagerplatzIId = dto.getLagerplatzIId();
		} else {
			wtfLagerplatz.setText(null);
			lagerplatzIId = null;
		}

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);
	}

	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_WEP_ETIKETT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getBestellungDelegate()
				.printWepEtikett(weposDto.getIId(),
						bestellpositionDto.getIId(),
						wareneingangDto.getLagerIId(),
						wnfExemplare.getInteger(),
						wnfVerpackungseinheit.getInteger(),
						wnfGewicht.getDouble(),
						wtfWarenverkehrsnummer.getFormattedText(),
						wtfLagerplatz.getText(), wtfUrsprungsland.getText(),
						wtfKommentar.getText(), wnfHandmenge.getBigDecimal(),
						null);
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setDefaults();
		dto2Components();
		wnfExemplare.setInteger(1);
		wnfExemplare.setEditable(true);
		wnfExemplare.setActivatable(true);
		wnfExemplare.setMandatoryField(true);
	}

	public void updateButtons() throws Throwable {
		super.updateButtons();
		wnfExemplare.setEditable(true);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(
				PanelDialog.ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			int z = 0;
		}

		if (e.getActionCommand().equals(
				ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE)) {
			dialogQueryWarenverkehrsnummerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_WARENEINGANG_LAGERPLATZ_NUR_DEFINIERTE,
							ParameterFac.KATEGORIE_BESTELLUNG,
							LPMain.getTheClient().getMandant());
			boolean bWareneingangNurDefinierte = ((Boolean) parameter
					.getCWertAsObject());
			if (bWareneingangNurDefinierte == true) {
				dialogQueryArtikellagerplatzFromListe(e);
			} else {
				dialogQueryLagerplatzFromListe(e);
			}

		}
	}

	void dialogQueryLagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRLagerplatz(getInternalFrame(), lagerplatzIId,
						true);

		new DialogQuery(panelQueryFLRLagerplatz);
	}

	void dialogQueryArtikellagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikellagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikellagerplaetze(getInternalFrame(),
						bestellpositionDto.getArtikelIId(), true);

		new DialogQuery(panelQueryFLRArtikellagerplatz);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRWarenverkehrsnummer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfWarenverkehrsnummer.setText((String) key);
			} else if (e.getSource() == panelQueryFLRLand) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				landDto = DelegateFactory.getInstance().getSystemDelegate()
						.landFindByPrimaryKey((Integer) key);
				wtfUrsprungsland.setText(landDto.getCName());
			} else if (e.getSource() == panelQueryFLRLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				LagerplatzDto lagerplatzDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerplatzFindByPrimaryKey((Integer) key);
				wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
				lagerplatzIId = lagerplatzDto.getIId();
			} else if (e.getSource() == panelQueryFLRArtikellagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				ArtikellagerplaetzeDto apDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.artikellagerplaetzeFindByPrimaryKey((Integer) key);

				LagerplatzDto lagerplatzDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerplatzFindByPrimaryKey(apDto.getLagerplatzIId());
				wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
				lagerplatzIId = lagerplatzDto.getIId();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLagerplatz) {
				wtfLagerplatz.setText(null);
				lagerplatzIId = null;
			} else if (e.getSource() == panelQueryFLRArtikellagerplatz) {
				wtfLagerplatz.setText(null);
				lagerplatzIId = null;
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		super.eventActionSave(e, true);
		components2Dto();

		if (artikellieferantDto != null)
			DelegateFactory.getInstance().getArtikelDelegate()
					.updateArtikellieferant(artikellieferantDto);
		DelegateFactory.getInstance().getArtikelDelegate()
				.updateArtikel(artikelDto);
		DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.artikellagerplatzCRUD(artikelDto.getIId(),
						wareneingangDto.getLagerIId(), lagerplatzIId);

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, false); // Buttons schalten
		if (artikellieferantDto == null)
			wnfVerpackungseinheit.setActivatable(false);

	}

	void dialogQueryWarenverkehrsnummerFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRWarenverkehrsnummer = FinanzFilterFactory.getInstance()
				.createPanelFLRWarenverkehrsnummer(getInternalFrame(),
						wtfWarenverkehrsnummer.getFormattedText());
		new DialogQuery(panelQueryFLRWarenverkehrsnummer);
	}

	void dialogQueryLandFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLand = SystemFilterFactory.getInstance()
				.createPanelFLRLand(getInternalFrame(), null, true);
		// vorbesetzen
		if (landDto != null) {
			panelQueryFLRLand.setSelectedId(landDto.getIID());
		}
		new DialogQuery(panelQueryFLRLand);
	}

	public Integer getAnzahlExemplare() throws Throwable {
		return wnfExemplare.getInteger();
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

}
