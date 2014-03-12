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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
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
 * @version $Revision: 1.9 $
 */
public class ReportWepEtikett extends PanelBasis implements PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final public String ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE = "ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE";
	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	static final public String ACTION_SPECIAL_UPDATE = "ACTION_SPECIAL_UPDATE";
	static final public String ACTION_SPECIAL_SAVE = "ACTION_SPECIAL_SAVE";
	static final public String ACTION_SPECIAL_DISCARD = "ACTION_SPECIAL_DISCARD";

	private WareneingangDto wareneingangDto = null;
	private WareneingangspositionDto weposDto = null;
	private BestellpositionDto bestellpositionDto = null;
	private ArtikelDto artikelDto = null;
	private ArtikellieferantDto artikellieferantDto = null;
	private ArtikellagerplaetzeDto artikellagerplaetzeDto = null;
	private LandDto landDto = null;
	private LagerplatzDto lagerplatzDto = null;
	private WrapperLabel wlaVerpackungseinheit = null;
	private WrapperNumberField wnfVerpackungseinheit = null;
	private PanelQueryFLR panelQueryFLRWarenverkehrsnummer = null;
	private PanelQueryFLR panelQueryFLRLand = null;
	private WrapperButton wbuWarenverkehrsnummer = null;
	private WrapperTextField wtfWarenverkehrsnummer = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperLabel wlaLagerort = null;
	private WrapperTextField wtfLagerort = null;
	private WrapperButton wbuUrsprungsland = null;
	private WrapperTextField wtfUrsprungsland = null;
	private WrapperLabel wlaKommentar = null;
	private WrapperTextField wtfKommentar = null;

	protected JPanel jpaWorkingOn = new JPanel();
	private WrapperLabel wlaExemplare = null;
	protected WrapperNumberField wnfExemplare = null;

	public ReportWepEtikett(InternalFrame internalFrame,
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

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		jpaWorkingOn.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		wlaExemplare = new WrapperLabel();
		wlaExemplare.setText(LPMain.getInstance().getTextRespectUISPr(
				"report.exemplare"));
		wlaExemplare.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaExemplare.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare = new WrapperNumberField();
		wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setFractionDigits(0);
		wnfExemplare.setMaximumIntegerDigits(2);

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

		wtfWarenverkehrsnummer = new WrapperTextField();
		wlaGewicht = new WrapperLabel();
		wlaGewicht.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel();
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheit.setHorizontalTextPosition(SwingConstants.TRAILING);
		wlaEinheit.setText("kg");

		wlaLagerort = new WrapperLabel();
		wlaLagerort.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.lagerort"));
		wtfLagerort = new WrapperTextField();

		wlaKommentar = new WrapperLabel();
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));
		wtfKommentar = new WrapperTextField();

		wbuUrsprungsland = new WrapperButton();
		wbuUrsprungsland.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.ursprungsland")
				+ "...");
		wbuUrsprungsland.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuUrsprungsland.addActionListener(this);
		wtfUrsprungsland = new WrapperTextField();

		this.setLayout(new GridBagLayout());
		jpaWorkingOn.add(wlaExemplare, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfExemplare, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVerpackungseinheit, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerpackungseinheit, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuWarenverkehrsnummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWarenverkehrsnummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLagerort, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerort, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance().getParameterDelegate().getParametermandant(
						ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bHerstellerUrsprungsland = ((Boolean) parameter
				.getCWertAsObject());
		if (bHerstellerUrsprungsland == false) {
			iZeile++;
			jpaWorkingOn.add(wbuUrsprungsland, new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfUrsprungsland, new GridBagConstraints(1,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1, 1,
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
		artikelDto.setCWarenverkehrsnummer(wtfWarenverkehrsnummer.getText());
		artikellieferantDto.setNVerpackungseinheit(wnfVerpackungseinheit
				.getBigDecimal());
		if (wtfLagerort.getText() != null) {
			artikellagerplaetzeDto.getLagerplatzDto().setCLagerplatz(
					wtfLagerort.getText());
		}
	}

	protected void dto2Components() throws Throwable {
		try {
			artikellagerplaetzeDto = DelegateFactory.getInstance()
					.getLagerDelegate()
					.artikellagerplaetzeFindByArtikelIIdLagerIId(
							artikelDto.getIId(), wareneingangDto.getLagerIId());
		} catch (Exception ex) {
			artikellagerplaetzeDto = new ArtikellagerplaetzeDto();
			lagerplatzDto = new LagerplatzDto();
			lagerplatzDto.setLagerIId(wareneingangDto.getLagerIId());
			artikellagerplaetzeDto.setLagerplatzDto(lagerplatzDto);
			artikellagerplaetzeDto.setArtikelIId(artikelDto.getIId());
		}
		if (artikelDto.getLandIIdUrsprungsland() != null) {
			landDto = DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
			wtfUrsprungsland.setText(landDto.getCName());
		}
		wnfGewicht.setDouble(artikelDto.getFGewichtkg());
		wtfWarenverkehrsnummer.setText(artikelDto.getCWarenverkehrsnummer());
		wnfVerpackungseinheit.setBigDecimal(artikellieferantDto
				.getNVerpackungseinheit());
		if (artikellagerplaetzeDto.getLagerplatzDto() != null)
			wtfLagerort.setText(artikellagerplaetzeDto.getLagerplatzDto()
					.getCLagerplatz());
	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);
	}

	private void setEditable(Boolean flag) {
		wnfGewicht.setEditable(flag);
		wnfGewicht.setEditable(flag);
		wnfVerpackungseinheit.setEditable(flag);
		wbuWarenverkehrsnummer.setEnabled(flag);
		wtfWarenverkehrsnummer.setEditable(flag);
		wtfKommentar.setEditable(flag);
		wtfLagerort.setEditable(flag);
		wbuUrsprungsland.setEnabled(flag);
		wtfUrsprungsland.setEditable(flag);
	}

	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_WEP_ETIKETT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getBestellungDelegate()
				.printWepEtikett(weposDto.getIId(),
						bestellpositionDto.getIId(),
						wareneingangDto.getLagerIId(),
						wnfExemplare.getInteger(),
						wnfVerpackungseinheit.getInteger(),
						wnfGewicht.getDouble(),
						wtfWarenverkehrsnummer.getText(),
						wtfLagerort.getText(), wtfUrsprungsland.getText(),
						wtfKommentar.getText(), null, null);
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
		wnfExemplare.setInteger(1);
		wnfExemplare.setEditable(true);
		wnfExemplare.setMandatoryField(true);
		dto2Components();

		LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
				PanelBasis.ACTION_UPDATE);
		item.getButton().setEnabled(true);
		item.getButton().setActionCommand(ACTION_SPECIAL_UPDATE);
		item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_SAVE);
		item.getButton().setEnabled(true);
		item.getButton().setActionCommand(ACTION_SPECIAL_SAVE);
		item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_DISCARD);
		item.getButton().setEnabled(true);
		item.getButton().setActionCommand(ACTION_SPECIAL_DISCARD);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE)) {
			dialogQueryWarenverkehrsnummerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_UPDATE)) {
			setEditable(true);
			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);
			item.getButton().setEnabled(false);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			components2Dto();
			if (wtfLagerort.getText() != null) {
				if (artikellagerplaetzeDto.getIId() != null) {
					DelegateFactory.getInstance().getLagerDelegate()
							.updateArtikellagerplaetze(artikellagerplaetzeDto);
				} else {
					Integer alpIId = DelegateFactory.getInstance()
							.getLagerDelegate().createArtikellagerplaetze(
									artikellagerplaetzeDto);
					artikellagerplaetzeDto = DelegateFactory.getInstance()
							.getLagerDelegate()
							.artikellagerplaetzeFindByPrimaryKey(alpIId);
					lagerplatzDto = artikellagerplaetzeDto.getLagerplatzDto();
				}
			}
			DelegateFactory.getInstance().getArtikelDelegate()
					.updateArtikellieferant(artikellieferantDto);
			DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(
					artikelDto);
			setEditable(false);
			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);
			item.getButton().setEnabled(true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DISCARD)) {
			setEditable(false);
			dto2Components();
			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);
			item.getButton().setEnabled(true);
		}

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
			}
		}
	}

	void dialogQueryWarenverkehrsnummerFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRWarenverkehrsnummer = FinanzFilterFactory.getInstance()
				.createPanelFLRWarenverkehrsnummer(getInternalFrame(),
						wtfWarenverkehrsnummer.getText());
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
