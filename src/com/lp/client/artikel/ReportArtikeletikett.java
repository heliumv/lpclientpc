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
package com.lp.client.artikel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportArtikeletikett extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaArtikel = new WrapperLabel();
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private Integer artikelIId = null;
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperButton wbuSnrChnr = new WrapperButton();
	private WrapperTextField wtfSnrChnr = new WrapperTextField();
	static final public String ACTION_SPECIAL_SNRCHNR_FROM_LISTE = "ACTION_SPECIAL_SNRCHNR_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRSnrChnrAuswahl = null;

	private WrapperLabel wlaExemplare = null;
	protected WrapperNumberField wnfExemplare = null;
	private String[] oSelectedSnrs = null;

	protected JPanel jpaWorkingOn = new JPanel();
	private InternalFrameArtikel internalFrame = null;

	public ReportArtikeletikett(InternalFrameArtikel internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		this.internalFrame = internalFrame;
		jbInit();
		initComponents();

		if (internalFrame.getArtikelDto() != null) {
			wtfArtikel.setText(internalFrame.getArtikelDto()
					.formatArtikelbezeichnung());
			artikelIId = internalFrame.getArtikelDto().getIId();
		}
	}

	void dialogQuerySnrChnrFromListe() throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
				.createButtonArray(false, true);

		FilterKriterium[] filtersI = new FilterKriterium[2];

		filtersI[0] = new FilterKriterium(""
				+ LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID, true, artikelIId
				+ "", FilterKriterium.OPERATOR_EQUAL, false);
		filtersI[1] = new FilterKriterium(""
				+ LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG, true, "0",
				FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryFLRSnrChnrAuswahl = new PanelQueryFLR(null, filtersI,
				QueryParameters.UC_ID_ALLESNRCHNR, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"rekla.snchrnauswahl"));
		panelQueryFLRSnrChnrAuswahl.setMultipleRowSelectionEnabled(true);

		FilterKriteriumDirekt fkdSeriennummer = ArtikelFilterFactory
				.getInstance().createFKDSnrChnrReklamation();

		panelQueryFLRSnrChnrAuswahl.befuellePanelFilterkriterienDirekt(
				fkdSeriennummer, null);

		if (fkdSeriennummer.value != null
				&& !fkdSeriennummer.value.trim().equals("")) {
			panelQueryFLRSnrChnrAuswahl.eventActionRefresh(null, false);
		}

		panelQueryFLRSnrChnrAuswahl.setSize(800,
				panelQueryFLRSnrChnrAuswahl.getHeight());

		new DialogQuery(panelQueryFLRSnrChnrAuswahl);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_SNRCHNR_FROM_LISTE)) {
			dialogQuerySnrChnrFromListe();
		}
	}

	private void jbInit() throws Exception {

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

		wlaArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikelbestellt.selektierterartikel")
				+ ": ");
		wtfArtikel.setActivatable(false);
		wtfArtikel.setMandatoryField(true);
		wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtikel.setSaveReportInformation(false);
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikeletikett.Kommentar")
				+ ": ");
		wtfKommentar.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikeletikett.Menge")
				+ ": ");

		wnfMenge.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wnfMenge.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));

		wbuSnrChnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.snchrnauswahl"));
		wbuSnrChnr.setActionCommand(ACTION_SPECIAL_SNRCHNR_FROM_LISTE);
		wbuSnrChnr.addActionListener(this);
		wtfSnrChnr.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfSnrChnr.setActivatable(false);
		wnfExemplare = new WrapperNumberField();
		wnfExemplare.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfExemplare.setFractionDigits(0);
		wnfExemplare.setMaximumIntegerDigits(2);
		wnfExemplare.setMandatoryField(true);
		wnfExemplare.setInteger(1);

		jpaWorkingOn.add(wlaExemplare, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfExemplare, new GridBagConstraints(1, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 300, 0));
		iZeile++;

		jpaWorkingOn.add(wlaArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 2, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 2, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		if (internalFrame.getArtikelDto() != null
				&& (Helper.short2Boolean(internalFrame.getArtikelDto()
						.getBChargennrtragend()) || Helper
						.short2Boolean(internalFrame.getArtikelDto()
								.getBSeriennrtragend()))) {
			jpaWorkingOn.add(wbuSnrChnr,
					new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wtfSnrChnr,
					new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public String getKommentar() {
		return wtfKommentar.getText();
	}

	public BigDecimal getMenge() throws ExceptionLP {
		return wnfMenge.getBigDecimal();
	}

	public String getModul() {
		return ArtikelReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ArtikelReportFac.REPORT_ARTIKELETIKETT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getArtikelReportDelegate()
				.printArtikeletikett(artikelIId, wtfKommentar.getText(),
						wnfMenge.getBigDecimal(), wnfExemplare.getInteger(),
						oSelectedSnrs);
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	@Override
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRSnrChnrAuswahl) {
				Object[] o = panelQueryFLRSnrChnrAuswahl.getSelectedIds();
				oSelectedSnrs = new String[o.length];
				if (o.length > 0) {

					String s = "";
					for (int i = 0; i < o.length; i++) {
						oSelectedSnrs[i] = (String) o[i];
						s += oSelectedSnrs[i] + ", ";
					}

					wtfSnrChnr.setText(s);
				}
			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

			if (e.getSource() == panelQueryFLRSnrChnrAuswahl) {
				oSelectedSnrs = null;
				wtfSnrChnr.setText(null);
			}
		}
	}
}
