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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperFormattedTextField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.SollverkaufDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelArtikelsonstiges extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaMaxvertreterprovision = new WrapperLabel();
	private WrapperNumberField wnfMaxvertreterprovision = new WrapperNumberField();
	private WrapperLabel wla2 = new WrapperLabel();
	private WrapperLabel wlaGarantiezeit = new WrapperLabel();
	private WrapperNumberField wnfGarantiezeit = new WrapperNumberField();
	private WrapperLabel wlaInMonaten = new WrapperLabel();
	private WrapperLabel wlaEinheitVerpackungsmenge = new WrapperLabel();
	private WrapperLabel wlaMinutenfaktor1 = new WrapperLabel();
	private WrapperNumberField wrapperNumberFieldMinutenfaktor1 = new WrapperNumberField();
	private WrapperLabel wlaMinutenfaktor2 = new WrapperLabel();
	private WrapperNumberField wnfMinutenfaktor2 = new WrapperNumberField();
	private WrapperLabel wlaVerpackungsmenge = new WrapperLabel();
	private WrapperNumberField wnfVerpackungsmenge = new WrapperNumberField();
	private WrapperNumberField wnfMindestdeckungsbeitrag = new WrapperNumberField();
	private WrapperLabel wlaMindestdeckungsbeitrag = new WrapperLabel();
	private WrapperLabel wla8 = new WrapperLabel();
	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperNumberField wnfAufschlag = new WrapperNumberField();
	private WrapperLabel wlaSoll = new WrapperLabel();
	private WrapperNumberField wnfSoll = new WrapperNumberField();
	private WrapperLabel wlaVerkaufsean = new WrapperLabel();
	private WrapperTextField wtfVerkaufsean = new WrapperTextField();
	private WrapperLabel wlaVerpackungsean = new WrapperLabel();
	private WrapperTextField wtfVerpackungsean = new WrapperTextField();
	private WrapperButton wbuWarenverkehrsnummer = new WrapperButton();
	private WrapperFormattedTextField wtfWarenverkehrsnummer = new WrapperFormattedTextField();
	private WrapperCheckBox wcoSeriennummer = new WrapperCheckBox();
	private WrapperCheckBox wcoLagerbewertet = new WrapperCheckBox();
	private WrapperCheckBox wcoChargennummer = new WrapperCheckBox();
	private WrapperCheckBox wcoRabattierbar = new WrapperCheckBox();
	private WrapperCheckBox wcoDokumentenpflicht = new WrapperCheckBox();
	private WrapperCheckBox wcoLagerbewirtschaftet = new WrapperCheckBox();
	private WrapperCheckBox wcoVerleih = new WrapperCheckBox();
	private WrapperCheckBox wcoWerbeabgabepflichtig = new WrapperCheckBox();

	private WrapperLabel wlaLetzteWartung = new WrapperLabel();
	private WrapperLabel wlaLetzteWartungPersonal = new WrapperLabel();
	private WrapperDateField wdfLetzteWartung = new WrapperDateField();

	private WrapperLabel wlaWartungsintervall = new WrapperLabel();
	private WrapperNumberField wnfWartungsintervall = new WrapperNumberField();
	private WrapperLabel wlaSofortverbrauch = new WrapperLabel();
	private WrapperNumberField wnfSofortverbrauch = new WrapperNumberField();

	private WrapperGotoButton wbuZugehoerigerArtikel = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);

	private WrapperTextField wtfZugehoerigerArtikel = new WrapperTextField();

	private WrapperButton wbuUrsprungsland = new WrapperButton();
	private WrapperTextField wtfUrsprungsland = new WrapperTextField();

	private WrapperGotoButton wbuErsatzArtikel = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);

	private WrapperTextField wtfErsatzArtikel = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRErsatzArtikel = null;
	private PanelQueryFLR panelQueryFLRWarenverkehrsnummer = null;
	private PanelQueryFLR panelQueryFLRLand = null;
	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE = "ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE";
	static final public String ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE = "ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE";
	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	private WrapperLabel wlaDummy = new WrapperLabel();
	private WrapperLabel wlaZBez2 = new WrapperLabel();
	private WrapperLabel wlaZbez = new WrapperLabel();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperTextField wftBezeichnungStd = new WrapperTextField();
	private WrapperTextField wtfZBezStd = new WrapperTextField();
	private WrapperTextField wtfZBez2Std = new WrapperTextField();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private Border border1;
	private WrapperLabel wla1 = new WrapperLabel();
	private WrapperLabel wla3 = new WrapperLabel();

	public PanelArtikelsonstiges(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMaxvertreterprovision;
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_LEEREN };
		panelQueryFLRArtikel = new PanelQueryFLR(
				null,
				ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE,
				aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
						.getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel());
		panelQueryFLRArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false,
				LPMain.getTextRespectUISPr("lp.alle"));
		panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(
						internalFrameArtikel), ArtikelFilterFactory
						.getInstance().createFKDVolltextsuche());
		panelQueryFLRArtikel
				.setSelectedId(artikelDto.getArtikelIIdZugehoerig());
		new DialogQuery(panelQueryFLRArtikel);
	}

	void dialogQueryErsatzArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_LEEREN };
		panelQueryFLRErsatzArtikel = new PanelQueryFLR(
				null,
				ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE,
				aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
						.getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel());
		panelQueryFLRErsatzArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "",
						FilterKriterium.OPERATOR_EQUAL, false), false,
				LPMain.getTextRespectUISPr("lp.alle"));
		panelQueryFLRErsatzArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(
						internalFrameArtikel), ArtikelFilterFactory
						.getInstance().createFKDVolltextsuche());
		panelQueryFLRErsatzArtikel.setSelectedId(artikelDto
				.getArtikelIIdErsatz());
		new DialogQuery(panelQueryFLRErsatzArtikel);
	}

	void dialogQueryWarenverkehrsnummerFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRWarenverkehrsnummer = FinanzFilterFactory.getInstance()
				.createPanelFLRWarenverkehrsnummer(getInternalFrame(),
						wtfWarenverkehrsnummer.getFormattedText());
		new DialogQuery(panelQueryFLRWarenverkehrsnummer);

	}

	void dialogQueryLandFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRLand = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_LAND, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.landauswahlliste"));

		panelQueryFLRLand.setSelectedId(artikelDto.getLandIIdUrsprungsland());

		new DialogQuery(panelQueryFLRLand);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE)) {
			dialogQueryErsatzArtikelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE)) {
			dialogQueryWarenverkehrsnummerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			ArtikelDto artikelTempDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getIId());

			if (Helper.short2boolean(artikelTempDto.getBLagerbewirtschaftet()) == true
					&& Helper.short2boolean(artikelDto
							.getBLagerbewirtschaftet()) == false) {

				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.error.wechsel.lagerbewirtschaftet"),
						LPMain.getTextRespectUISPr("lp.warning"),
						JOptionPane.WARNING_MESSAGE, JOptionPane.NO_OPTION);

				if (b == false) {
					return;
				}

				Object[] aOptionen = new Object[2];
				aOptionen[0] = LPMain
						.getInstance()
						.getTextRespectUISPr(
								"artikel.error.wechsel.lagerbewirtschaftet.nichtdurchfuehren");
				aOptionen[1] = LPMain.getInstance().getTextRespectUISPr(
						"artikel.error.wechsel.lagerbewirtschaftet.jasicher");

				int iAuswahl = DialogFactory.showModalDialog(
						getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.error.wechsel.lagerbewirtschaftet2"),
						LPMain.getInstance().getTextRespectUISPr("lp.warning"),
						aOptionen, aOptionen[0]);

				if (iAuswahl != 1) {
					return;
				}

			}

			// PJ16141
			if (Helper.short2boolean(artikelTempDto.getBChargennrtragend()) != Helper
					.short2boolean(artikelDto.getBChargennrtragend())
					&& DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.sindBereitsLagerbewegungenVorhanden(
									artikelDto.getIId())) {
				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.error.wechsel.chnrbehaftet"));
				if (b == true) {
					DelegateFactory.getInstance().getArtikelDelegate()
							.updateArtikel(artikelDto);
				}

			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateArtikel(artikelDto);
			}

			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getIId());
			((InternalFrameArtikel) getInternalFrame())
					.setArtikelDto(artikelDto);
			super.eventActionSave(e, true);
		}
		eventYouAreSelected(false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				if (artikelTempDto.getIId().equals(
						internalFrameArtikel.getArtikelDto().getIId())) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"artikel.error.kannnichtselbstzugeordnetwerden"));
				} else {
					wtfZugehoerigerArtikel.setText(artikelTempDto
							.formatArtikelbezeichnung());
					artikelDto.setArtikelIIdZugehoerig(artikelTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRErsatzArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				if (artikelTempDto.getIId().equals(
						internalFrameArtikel.getArtikelDto().getIId())) {
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"artikel.error.kannnichtselbstzugeordnetwerden"));
				} else {
					wtfErsatzArtikel.setText(artikelTempDto
							.formatArtikelbezeichnung());
					artikelDto.setArtikelIIdErsatz(artikelTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRLand) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landFindByPrimaryKey((Integer) key);
				wtfUrsprungsland.setText(landDto.getCLkz());
				artikelDto.setLandIIdUrsprungsland(landDto.getIID());
			}

			else if (e.getSource() == panelQueryFLRWarenverkehrsnummer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfWarenverkehrsnummer.setText((String) key);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {

				wtfZugehoerigerArtikel.setText(null);
				artikelDto.setArtikelIIdZugehoerig(null);
			} else if (e.getSource() == panelQueryFLRErsatzArtikel) {

				wtfErsatzArtikel.setText(null);
				artikelDto.setArtikelIIdErsatz(null);
			} else if (e.getSource() == panelQueryFLRLand) {
				wtfUrsprungsland.setText(null);
				artikelDto.setLandIIdUrsprungsland(null);
			}

		}

	}

	protected void dto2Components() throws Throwable {
		wnfMaxvertreterprovision.setDouble(artikelDto
				.getFVertreterprovisionmax());
		wnfMindestdeckungsbeitrag.setDouble(artikelDto
				.getFMindestdeckungsbeitrag());
		wrapperNumberFieldMinutenfaktor1.setDouble(artikelDto
				.getFMinutenfaktor1());
		wnfMinutenfaktor2.setDouble(artikelDto.getFMinutenfaktor2());
		wnfVerpackungsmenge.setDouble(artikelDto.getFVerpackungsmenge());

		wlaEinheitVerpackungsmenge.setText(artikelDto.getEinheitCNr());

		wtfVerkaufsean.setText(artikelDto.getCVerkaufseannr());
		wtfVerpackungsean.setText(artikelDto.getCVerpackungseannr());
		wtfWarenverkehrsnummer.setText(artikelDto.getCWarenverkehrsnummer());

		wnfWartungsintervall.setInteger(artikelDto.getIWartungsintervall());
		wnfSofortverbrauch.setInteger(artikelDto.getISofortverbrauch());

		wdfLetzteWartung.setTimestamp(artikelDto.getTLetztewartung());

		if (artikelDto.getPersonalIIdLetztewartung() != null) {
			wlaLetzteWartungPersonal.setText(DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							artikelDto.getPersonalIIdLetztewartung())
					.getCKurzzeichen());
		} else {
			wlaLetzteWartungPersonal.setText(null);
		}

		if (artikelDto.getCWarenverkehrsnummer() == null
				|| Helper.checkWarenverkehrsnummer(artikelDto
						.getCWarenverkehrsnummer().trim())
				|| artikelDto.getCWarenverkehrsnummer().length() == 0) {
			wtfWarenverkehrsnummer.setForeground(new WrapperTextField()
					.getForeground());
		} else {
			wtfWarenverkehrsnummer.setForeground(Color.red);
		}

		if (artikelDto.getLandIIdUrsprungsland() != null) {
			LandDto landDto = DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
			wtfUrsprungsland.setText(landDto.getCLkz());
		} else {
			wtfUrsprungsland.setText(null);
		}

		wcoSeriennummer.setShort(artikelDto.getBSeriennrtragend());
		wcoChargennummer.setShort(artikelDto.getBChargennrtragend());
		wcoRabattierbar.setShort(artikelDto.getBRabattierbar());
		wcoDokumentenpflicht.setShort(artikelDto.getBDokumentenpflicht());
		wcoWerbeabgabepflichtig.setShort(artikelDto.getBWerbeabgabepflichtig());
		wcoLagerbewertet.setShort(artikelDto.getBLagerbewertet());
		wcoVerleih.setShort(artikelDto.getBVerleih());
		wcoLagerbewirtschaftet.setShort(artikelDto.getBLagerbewirtschaftet());
		wnfGarantiezeit.setInteger(artikelDto.getIGarantiezeit());
		if (artikelDto.getSollverkaufDto() != null) {
			wnfAufschlag.setDouble(artikelDto.getSollverkaufDto()
					.getFAufschlag());
			wnfSoll.setDouble(artikelDto.getSollverkaufDto().getFSollverkauf());
		}
		if (artikelDto.getArtikelIIdZugehoerig() != null) {
			ArtikelDto artikelDtoTemp = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							artikelDto.getArtikelIIdZugehoerig());
			wtfZugehoerigerArtikel.setText(artikelDtoTemp
					.formatArtikelbezeichnung());
		} else {
			wtfZugehoerigerArtikel.setText(null);
		}

		wbuZugehoerigerArtikel.setOKey(artikelDto.getArtikelIIdZugehoerig());

		if (artikelDto.getArtikelIIdErsatz() != null) {
			ArtikelDto artikelDtoTemp = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getArtikelIIdErsatz());
			wtfErsatzArtikel.setText(artikelDtoTemp.formatArtikelbezeichnung());
		} else {
			wtfErsatzArtikel.setText(null);
		}

		wbuErsatzArtikel.setOKey(artikelDto.getArtikelIIdErsatz());

		wtfArtikelnummer.setText(artikelDto.getCNr());
		if (artikelDto.getArtikelsprDto() != null) {
			wftBezeichnungStd.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfZBezStd.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfZBez2Std.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}
		this.setStatusbarPersonalIIdAendern(artikelDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(artikelDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artikelDto.getTAnlegen());
		this.setStatusbarTAendern(artikelDto.getTAendern());

	}

	protected void components2Dto() throws Throwable {
		artikelDto.setFVertreterprovisionmax(wnfMaxvertreterprovision
				.getDouble());
		artikelDto.setFMindestdeckungsbeitrag(wnfMindestdeckungsbeitrag
				.getDouble());
		artikelDto.setFMinutenfaktor1(wrapperNumberFieldMinutenfaktor1
				.getDouble());
		artikelDto.setFMinutenfaktor2(wnfMinutenfaktor2.getDouble());
		artikelDto.setIGarantiezeit(wnfGarantiezeit.getInteger());
		artikelDto.setBRabattierbar(wcoRabattierbar.getShort());
		artikelDto.setBDokumentenpflicht(wcoDokumentenpflicht.getShort());
		artikelDto.setBWerbeabgabepflichtig(wcoWerbeabgabepflichtig.getShort());
		artikelDto.setBSeriennrtragend(wcoSeriennummer.getShort());
		artikelDto.setBChargennrtragend(wcoChargennummer.getShort());
		artikelDto.setBLagerbewertet(wcoLagerbewertet.getShort());
		artikelDto.setBVerleih(wcoVerleih.getShort());
		artikelDto.setBLagerbewirtschaftet(wcoLagerbewirtschaftet.getShort());

		artikelDto.setIWartungsintervall(wnfWartungsintervall.getInteger());
		artikelDto.setTLetztewartung(wdfLetzteWartung.getTimestamp());
		artikelDto.setISofortverbrauch(wnfSofortverbrauch.getInteger());

		String sWVN = wtfWarenverkehrsnummer.getFormattedText();
		if (sWVN != null && !sWVN.equals("")) {
			// MB: Format pruefen.
			if (Helper.checkWarenverkehrsnummer(wtfWarenverkehrsnummer
					.getFormattedText().trim())) {
				WarenverkehrsnummerDto dto = DelegateFactory
						.getInstance()
						.getFinanzServiceDelegate()
						.warenverkehrsnummerFindByPrimaryKeyOhneExc(
								wtfWarenverkehrsnummer.getFormattedText()
										.trim());
				if (dto == null) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("artikel.error.warenverkehrsnummer"));
				}

				artikelDto.setCWarenverkehrsnummer(wtfWarenverkehrsnummer
						.getFormattedText());

			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getTextRespectUISPr("artikel.error.warenverkehrsnummer.format"));
			}
		} else {
			artikelDto.setCWarenverkehrsnummer(null);
		}

		artikelDto.setCVerkaufseannr(wtfVerkaufsean.getText());
		artikelDto.setCVerpackungseannr(wtfVerpackungsean.getText());

		artikelDto.setSollverkaufDto(new SollverkaufDto());
		artikelDto.getSollverkaufDto().setFAufschlag(wnfAufschlag.getDouble());
		artikelDto.getSollverkaufDto().setFSollverkauf(wnfSoll.getDouble());
		artikelDto.setFVerpackungsmenge(wnfVerpackungsmenge.getDouble());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		leereAlleFelder(this);
		artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						((InternalFrameArtikel) getInternalFrame())
								.getArtikelDto().getIId());
		dto2Components();
	}

	private void jbInit() throws Throwable {
		// border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				165, 163, 151));
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
		wlaMaxvertreterprovision
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.maxvertreterprovision"));
		wlaGarantiezeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.garantiezeit"));
		wla2.setHorizontalAlignment(SwingConstants.LEFT);
		wla2.setText("%");
		wlaInMonaten.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.inmonaten"));
		wlaMinutenfaktor1.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.minutenfaktor")
				+ " 1");
		wlaMinutenfaktor2.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.minutenfaktor")
				+ " 2");

		wlaVerpackungsmenge.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verpackungsmenge"));
		wlaMindestdeckungsbeitrag
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.mindestdeckungsbeitrag"));
		wlaLetzteWartung.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.letztewartung"));
		wlaLetzteWartungPersonal.setHorizontalAlignment(SwingConstants.LEFT);
		wla8.setHorizontalAlignment(SwingConstants.LEFT);
		wla8.setText("%");
		wlaAufschlag.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.aufschlag"));
		wlaSoll.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.soll"));
		wlaVerkaufsean.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verkaufsean"));
		wlaVerpackungsean.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verpackungsean"));

		wlaWartungsintervall.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.wartungsintervall"));
		wlaSofortverbrauch.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.sofortverbrauch"));
		wnfWartungsintervall.setFractionDigits(0);
		wnfSofortverbrauch.setFractionDigits(0);

		wtfVerkaufsean.setColumnsMax(ArtikelFac.MAX_ARTIKEL_VERKAUFEANNR);
		wtfVerpackungsean.setColumnsMax(ArtikelFac.MAX_ARTIKEL_VERKAUFEANNR);
		wtfVerkaufsean.setText("");
		wbuWarenverkehrsnummer.setToolTipText("");
		wbuWarenverkehrsnummer.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.warenverkehrsnummer")
				+ "...");
		wtfWarenverkehrsnummer
				.setColumnsMax(ArtikelFac.MAX_ARTIKEL_WARENVERKEHRSNUMMER);
		wtfWarenverkehrsnummer
				.setFormat(ArtikelFac.PATTERN_WARENVERKEHRSNUMMER);
		wtfWarenverkehrsnummer.removeContent();
		wcoSeriennummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.seriennummernbehaftet"));
		wcoSeriennummer
				.addActionListener(new PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter(
						this));
		wcoChargennummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.chargennummernbehaftet"));
		wcoChargennummer
				.addActionListener(new PanelArtikelsonstiges_wrapperCheckBoxChargennummer_actionAdapter(
						this));
		wcoRabattierbar.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.rabattierbar"));

		wcoDokumentenpflicht.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.dokumentenpflicht"));
		wcoWerbeabgabepflichtig.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.werbeabgabepflichtig"));

		wcoLagerbewirtschaftet.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.lagerbewirtschaftet"));
		wcoLagerbewertet.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.lagerbewertet"));
		wcoVerleih.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.verleih"));
		wcoVerleih
				.addActionListener(new PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter(
						this));
		wbuZugehoerigerArtikel.setText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.sonstiges.zugehoerigerartikel")
				+ "...");
		wbuZugehoerigerArtikel
				.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuZugehoerigerArtikel.addActionListener(this);
		wbuWarenverkehrsnummer
				.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE);
		wbuWarenverkehrsnummer.addActionListener(this);

		wbuUrsprungsland.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.ursprungsland")
				+ "...");
		wbuUrsprungsland.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuUrsprungsland.addActionListener(this);

		wbuErsatzArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.sonstiges.ersatzartikel")
				+ "...");

		wbuErsatzArtikel
				.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE);
		wbuErsatzArtikel.addActionListener(this);
		wtfErsatzArtikel.setActivatable(false);
		wtfErsatzArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfZugehoerigerArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfZugehoerigerArtikel.setText("");
		wtfZugehoerigerArtikel.setActivatable(false);
		wlaDummy.setBorder(border1);
		wlaDummy.setMinimumSize(new Dimension(2, 4));
		wlaDummy.setPreferredSize(new Dimension(2, 4));
		wlaDummy.setText("");
		wlaZBez2.setRequestFocusEnabled(true);
		wlaZBez2.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zusatzbezeichnung")
				+ " 2");
		wlaZbez.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.zusatzbezeichnung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wftBezeichnungStd.setActivatable(false);
		wftBezeichnungStd.setText("");
		wtfZBezStd.setActivatable(false);
		wtfZBezStd.setText("");
		wtfZBez2Std.setActivatable(false);
		wtfZBez2Std.setText("");
		wtfArtikelnummer.setBackground(Color.white);
		wtfArtikelnummer.setActivatable(false);
		wtfArtikelnummer.setText("");
		wnfGarantiezeit.setFractionDigits(0);
		wnfAufschlag.setMinimumValue(new BigDecimal(0));
		wnfSoll.setMinimumValue(new BigDecimal(0));
		wnfMindestdeckungsbeitrag.setMinimumValue(new BigDecimal(0));
		wnfMindestdeckungsbeitrag.setMandatoryField(true);
		wnfMaxvertreterprovision.setMinimumValue(new BigDecimal(0));

		wla1.setRequestFocusEnabled(true);
		wla1.setHorizontalAlignment(SwingConstants.LEFT);
		wla1.setText("%");
		wla3.setRequestFocusEnabled(true);
		wla3.setHorizontalAlignment(SwingConstants.LEFT);
		wla3.setText("%");
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaArtikelnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfArtikelnummer, new GridBagConstraints(1, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wftBezeichnungStd, new GridBagConstraints(1, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaZbez, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfZBezStd, new GridBagConstraints(1, iZeile++, 5, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaZBez2, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfZBez2Std, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaDummy, new GridBagConstraints(0, iZeile, 6, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 0, 1, 0), 0, -10));

		iZeile++;

		jpaWorkingOn.add(wlaMaxvertreterprovision, new GridBagConstraints(0,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMaxvertreterprovision, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -40, 0));
		jpaWorkingOn.add(wla2, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wlaGarantiezeit, new GridBagConstraints(3, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGarantiezeit, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(wlaInMonaten, new GridBagConstraints(5, iZeile, 1, 1,
				0.07, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wnfMindestdeckungsbeitrag, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -40, 0));
		jpaWorkingOn.add(wlaMindestdeckungsbeitrag, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wla8, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSofortverbrauch, new GridBagConstraints(3, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSofortverbrauch, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.sofortverbrauch.intagen")),
				new GridBagConstraints(5, iZeile, 1, 1, 0.07, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(wdfLetzteWartung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLetzteWartung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLetzteWartungPersonal, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWartungsintervall, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWartungsintervall, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"artikel.sonstiges.wartungsintervall.inmonaten")),
				new GridBagConstraints(5, iZeile, 1, 1, 0.07, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaAufschlag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAufschlag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -40, 0));
		jpaWorkingOn.add(wla1, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaMinutenfaktor1, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrapperNumberFieldMinutenfaktor1,
				new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						-40, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSoll, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSoll, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -40, 0));
		jpaWorkingOn.add(wla3, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaMinutenfaktor2, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMinutenfaktor2, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		iZeile++;

		jpaWorkingOn.add(wlaVerkaufsean, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVerkaufsean, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVerpackungsmenge, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerpackungsmenge, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));
		jpaWorkingOn.add(wlaEinheitVerpackungsmenge, new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaVerpackungsean, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVerpackungsean, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -50, 0));

		iZeile++;

		jpaWorkingOn.add(wbuWarenverkehrsnummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWarenverkehrsnummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuUrsprungsland, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUrsprungsland, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

			jpaWorkingOn.add(wcoSeriennummer,
					new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)) {

			jpaWorkingOn.add(wcoChargennummer,
					new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 2, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wcoLagerbewirtschaftet, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoLagerbewertet, new GridBagConstraints(1, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_VERLEIH)) {

			jpaWorkingOn.add(wcoVerleih,
					new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wcoRabattierbar, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoDokumentenpflicht, new GridBagConstraints(1,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWerbeabgabepflichtig, new GridBagConstraints(4,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 2, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZugehoerigerArtikel, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZugehoerigerArtikel, new GridBagConstraints(2,
				iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuErsatzArtikel, new GridBagConstraints(0, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfErsatzArtikel, new GridBagConstraints(2, iZeile, 4,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() {
		wcoLagerbewirtschaftet.setSelected(true);
		wcoLagerbewertet.setSelected(true);

	}

	void wcoSeriennummer_actionPerformed(ActionEvent e) {
		if (wcoSeriennummer.isSelected()) {
			wcoChargennummer.setSelected(false);
			wcoLagerbewirtschaftet.setSelected(true);
		}
	}

	void wcoChargennummer_actionPerformed(ActionEvent e) {
		if (wcoChargennummer.isSelected()) {
			wcoSeriennummer.setSelected(false);
			wcoLagerbewirtschaftet.setSelected(true);
		}

	}

	void wcoVerleih_actionPerformed(ActionEvent e) {
		if (wcoVerleih.isSelected()) {
			wcoLagerbewirtschaftet.setSelected(true);
		}

	}

}

class PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter implements
		java.awt.event.ActionListener {
	PanelArtikelsonstiges adaptee;

	PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter(
			PanelArtikelsonstiges adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoSeriennummer_actionPerformed(e);
	}
}

class PanelArtikelsonstiges_wrapperCheckBoxChargennummer_actionAdapter
		implements java.awt.event.ActionListener {
	PanelArtikelsonstiges adaptee;

	PanelArtikelsonstiges_wrapperCheckBoxChargennummer_actionAdapter(
			PanelArtikelsonstiges adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoChargennummer_actionPerformed(e);
	}
}

class PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter implements
		java.awt.event.ActionListener {
	PanelArtikelsonstiges adaptee;

	PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter(
			PanelArtikelsonstiges adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVerleih_actionPerformed(e);
	}
}
