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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Fenster werden Anfragepositionlieferdaten erfasst. <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 17.06.05</p>
 * <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.14 $
 */
public class PanelAnfragepositionlieferdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;

	private AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;
	private AnfragepositionDto anfragepositionDto = null;

	private JPanel jPanelWorkingOn = null;
	private Border innerBorder = null;

	private WrapperGotoButton wlaIdent = null;
	private WrapperLabel wlaArtikelbezeichnung = null;
	private WrapperLabel wlaArtikelzusatzbez = null;
	private WrapperLabel wlaArtikelbezeichnungbeimlieferanten = null;

	private WrapperLabel wlaAnlieferzeitinwochen = null;
	private WrapperLabel wlaWunschtermin = null;

	private WrapperLabel wlaAnliefermenge = null;
	private WrapperLabel wlaWunschmenge = null;
	private WrapperLabel wlaEinheitAnliefermenge = null;
	private WrapperLabel wlaEinheitWunschmenge = null;

	private WrapperLabel wlaAngeboteinzelpreis = null;
	private WrapperLabel wlaRichtpreis = null;
	private WrapperLabel wlaWaehrungAngeboteinzelpreis = null;
	private WrapperLabel wlaWaehrungRichtpreis = null;
	private WrapperLabel wlaArtikelnummerbeimlieferanten = null;

	private WrapperTextField wtfIdent = null;
	private WrapperTextField wtfArtikelbezeichnung = null;
	private WrapperTextField wtfArtikelzusatzbez = null;
	private WrapperTextField wtfArtikelbezeichnungbeimlieferanten = null;
	private WrapperTextField wtfArtikelnummerbeimlieferanten = null;

	private WrapperNumberField wnfAnlieferzeit = null;
	private WrapperDateField wdfWunschtermin = null;

	private WrapperNumberField wnfAnliefermenge = null;
	private WrapperNumberField wnfWunschmenge = null;

	private WrapperNumberField wnfAngeboteinzelpreis = null;
	private WrapperNumberField wnfRichtpreis = null;

	private WrapperLabel wlaStandardmenge = null;
	private WrapperLabel wlaMindestbestellmenge = null;
	private WrapperLabel wlaVerpackungseinheit = null;
	private WrapperNumberField wnfStandardmenge = null;
	private WrapperNumberField wnfMindestbestellmenge = null;
	private WrapperNumberField wnfVerpackungseinheit = null;

	private WrapperLabel wlaEinheitVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaEinheitMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitStandardmenge = new WrapperLabel();
	private WrapperSelectField wsfZertifikatart = null;

	private WrapperLabel wlaAnlieferzeitEinheit = new WrapperLabel();
	
	public PanelAnfragepositionlieferdaten(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wsfZertifikatart = new WrapperSelectField(
				WrapperSelectField.ZERTIFIKATART, getInternalFrame(), true);

		wlaIdent = new WrapperGotoButton(LPMain.getInstance()
				.getTextRespectUISPr("label.identnummer"),
				WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);
		wlaIdent.setActivatable(false);

		wlaArtikelbezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bezeichnung"));
		wlaArtikelzusatzbez = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaArtikelbezeichnungbeimlieferanten = new WrapperLabel(LPMain
				.getInstance().getTextRespectUISPr(
						"anf.artikelbezeichnungbeimlieferanten"));
		wlaArtikelnummerbeimlieferanten = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anfr.artikelnrlieferant"));

		wlaAnlieferzeitEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance().getParameterDelegate().getParametermandant(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wlaAnlieferzeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.kw"));
			} else if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wlaAnlieferzeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.tage"));
			} else {
				wlaAnlieferzeitEinheit.setText("?");
			}
		}

		wlaAnlieferzeitinwochen = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("artikel.artikellieferant.wiederbeschaffungszeit"));
		wlaWunschtermin = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.anliefertermin"));

		wlaStandardmenge = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("artikel.standardmenge"));
		wlaVerpackungseinheit = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("artikel.verpackungseinheit"));
		wlaMindestbestellmenge = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("artikel.mindestbestellmenge"));

		wtfArtikelnummerbeimlieferanten = new WrapperTextField();

		wnfStandardmenge = new WrapperNumberField();
		wnfVerpackungseinheit = new WrapperNumberField();
		wnfMindestbestellmenge = new WrapperNumberField();

		wlaAnliefermenge = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.anliefermenge"));
		HelperClient.setDefaultsToComponent(wlaAnliefermenge, 90);
		wlaWunschmenge = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.menge"));
		HelperClient.setDefaultsToComponent(wlaWunschmenge, 90);
		wlaAngeboteinzelpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.anlieferpreis"));
		wlaRichtpreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("anf.richtpreis"));

		wlaEinheitAnliefermenge = new WrapperLabel();
		wlaEinheitAnliefermenge.setHorizontalAlignment(SwingConstants.LEADING);
		HelperClient.setDefaultsToComponent(wlaEinheitAnliefermenge, 30);
		wlaEinheitWunschmenge = new WrapperLabel();
		wlaEinheitWunschmenge.setHorizontalAlignment(SwingConstants.LEADING);
		HelperClient.setDefaultsToComponent(wlaEinheitWunschmenge, 30);

		wlaEinheitStandardmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMindestbestellmenge
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitVerpackungseinheit
				.setHorizontalAlignment(SwingConstants.LEFT);

		wlaWaehrungAngeboteinzelpreis = new WrapperLabel();
		wlaWaehrungAngeboteinzelpreis
				.setHorizontalAlignment(SwingConstants.LEADING);
		wlaWaehrungRichtpreis = new WrapperLabel();
		wlaWaehrungRichtpreis.setHorizontalAlignment(SwingConstants.LEADING);

		wtfIdent = new WrapperTextField();
		wtfIdent.setActivatable(false);
		wtfArtikelbezeichnung = new WrapperTextField();
		wtfArtikelbezeichnung.setActivatable(false);
		wtfArtikelbezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtikelzusatzbez = new WrapperTextField();
		wtfArtikelzusatzbez.setActivatable(false);
		wtfArtikelbezeichnungbeimlieferanten = new WrapperTextField();

		wnfAnlieferzeit = new WrapperNumberField();
		wnfAnlieferzeit = new WrapperNumberField(0, 9999);
		wnfAnlieferzeit.setFractionDigits(0);

		wdfWunschtermin = new WrapperDateField();
		wdfWunschtermin.setActivatable(false);

		wnfAnliefermenge = new WrapperNumberField();
		wnfAnliefermenge.setMandatoryField(true);
		wnfWunschmenge = new WrapperNumberField();
		wnfWunschmenge.setActivatable(false);
		wnfAngeboteinzelpreis = new WrapperNumberField();
		wnfAngeboteinzelpreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfAngeboteinzelpreis.setMandatoryField(true);
		wnfRichtpreis = new WrapperNumberField();
		wnfRichtpreis.setActivatable(false);
		wnfRichtpreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		
		jPanelWorkingOn = new JPanel(new MigLayout("wrap 6", "[40%][15%][5%][20%][15%][5%]"));
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanelWorkingOn.add(wlaIdent,"growx");
		jPanelWorkingOn.add(wtfIdent, "growx, span 2, wrap");

		jPanelWorkingOn.add(wlaArtikelbezeichnung, "growx");
		jPanelWorkingOn.add(wtfArtikelbezeichnung, "growx, span 2");
		jPanelWorkingOn.add(wlaStandardmenge, "growx");
		jPanelWorkingOn.add(wnfStandardmenge, "growx");
		jPanelWorkingOn.add(wlaEinheitStandardmenge, "growx, wrap");
		
		jPanelWorkingOn.add(wlaArtikelzusatzbez, "growx");
		jPanelWorkingOn.add(wtfArtikelzusatzbez, "growx, span 2");
		jPanelWorkingOn.add(wlaMindestbestellmenge, "growx");
		jPanelWorkingOn.add(wnfMindestbestellmenge, "growx");
		jPanelWorkingOn.add(wlaEinheitMindestbestellmenge, "growx, wrap");
		
		jPanelWorkingOn.add(wlaArtikelnummerbeimlieferanten, "growx");
		jPanelWorkingOn.add(wtfArtikelnummerbeimlieferanten, "growx, span 2");
		jPanelWorkingOn.add(wlaVerpackungseinheit, "growx");
		jPanelWorkingOn.add(wnfVerpackungseinheit, "growx");
		jPanelWorkingOn.add(wlaEinheitVerpackungseinheit, "growx, wrap");
		
		jPanelWorkingOn.add(wlaArtikelbezeichnungbeimlieferanten, "growx");
		jPanelWorkingOn.add(wtfArtikelbezeichnungbeimlieferanten, "growx, span 2, wrap 20");
		
		jPanelWorkingOn.add(wlaAnlieferzeitinwochen, "growx");
		jPanelWorkingOn.add(wnfAnlieferzeit, "growx");
		jPanelWorkingOn.add(wlaAnlieferzeitEinheit, "growx");
		jPanelWorkingOn.add(wlaWunschtermin, "growx");
		jPanelWorkingOn.add(wdfWunschtermin, "growx, span 2, wrap");
		
		jPanelWorkingOn.add(wlaAnliefermenge, "right, growx");
		jPanelWorkingOn.add(wnfAnliefermenge, "growx");
		jPanelWorkingOn.add(wlaEinheitAnliefermenge, "growx");
		jPanelWorkingOn.add(wlaWunschmenge, "right, growx");
		jPanelWorkingOn.add(wnfWunschmenge, "growx");
		jPanelWorkingOn.add(wlaEinheitWunschmenge, "growx, wrap");
		
		jPanelWorkingOn.add(wlaAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wnfAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wlaWaehrungAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wlaRichtpreis, "growx");
		jPanelWorkingOn.add(wnfRichtpreis, "growx");
		jPanelWorkingOn.add(wlaWaehrungRichtpreis, "growx, wrap 20");

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZERTIFIKATART)) {
			iZeile++;

			jPanelWorkingOn.add(wsfZertifikatart.getWrapperButton(), "growx");
			jPanelWorkingOn.add(wsfZertifikatart.getWrapperTextField(), "growx, span 2");
		}

	}

	private void setDefaults() throws Throwable {
		wlaWaehrungAngeboteinzelpreis.setText(tpAnfrage.getAnfrageDto()
				.getWaehrungCNr());
		wlaWaehrungRichtpreis.setText(tpAnfrage.getAnfrageDto()
				.getWaehrungCNr());
		wdfWunschtermin.setTimestamp(tpAnfrage.getAnfrageDto()
				.getTAnliefertermin());
	}

	private void setIdentVisible(boolean bVisible) {
		wlaIdent.setVisible(bVisible);
		wtfIdent.setVisible(bVisible);
	}

	private void setArtikelbezeichnungbeimlieferantenVisible(boolean bVisible) {
		wlaArtikelbezeichnungbeimlieferanten.setVisible(bVisible);
		wtfArtikelbezeichnungbeimlieferanten.setVisible(bVisible);
		wlaArtikelnummerbeimlieferanten.setVisible(bVisible);
		wtfArtikelnummerbeimlieferanten.setVisible(bVisible);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getAnfragepositionDelegate()
					.updateAnfragepositionlieferdaten(
							anfragepositionlieferdatenDto);

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAnfrage.pruefeAktuelleAnfrage()) {
			if (tpAnfrage.istAktualisierenLieferdatenErlaubt()) {

				super.eventActionUpdate(aE, false);
				// Vorschlagswerte setzen, wenn die Position noch nicht erfasst
				// wurde
				if (!Helper.short2boolean(anfragepositionlieferdatenDto
						.getBErfasst())) {
					wnfAnliefermenge.setBigDecimal(anfragepositionDto
							.getNMenge());
					wnfAngeboteinzelpreis.setBigDecimal(anfragepositionDto
							.getNRichtpreis());

				}

				if (!Helper.short2boolean(anfragepositionlieferdatenDto
						.getBErfasst())) {
					if (anfragepositionDto.getArtikelIId() != null) {

						ArtikellieferantDto alDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
										anfragepositionDto.getArtikelIId(),
										tpAnfrage
												.getAnfrageDto()
												.getLieferantIIdAnfrageadresse());

						if (alDto != null) {
							wnfStandardmenge.setDouble(alDto
									.getFStandardmenge());
							wnfMindestbestellmenge.setDouble(alDto
									.getFMindestbestelmenge());
							wnfVerpackungseinheit.setBigDecimal(alDto
									.getNVerpackungseinheit());
							wtfArtikelnummerbeimlieferanten.setText(alDto
									.getCArtikelnrlieferant());
							wtfArtikelbezeichnungbeimlieferanten.setText(alDto
									.getCBezbeilieferant());
						}

					}

				}

			}
		}
	}

	private void resetPanel() throws Throwable {
		anfragepositionlieferdatenDto = new AnfragepositionlieferdatenDto();
		anfragepositionDto = new AnfragepositionDto();

		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {
		String cArtikelbezeichnung = null;

		if (anfragepositionDto.getPositionsartCNr().equals(
				AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(
							anfragepositionDto.getArtikelIId());

			wlaEinheitMindestbestellmenge.setText(artikelDto.getEinheitCNr()
					.trim());
			wlaEinheitStandardmenge.setText(artikelDto.getEinheitCNr().trim());
			wlaEinheitVerpackungseinheit.setText(artikelDto.getEinheitCNr()
					.trim());

			wnfMindestbestellmenge.setBigDecimal(anfragepositionlieferdatenDto
					.getNMindestbestellmenge());
			wnfVerpackungseinheit.setBigDecimal(anfragepositionlieferdatenDto
					.getNVerpackungseinheit());
			wnfStandardmenge.setBigDecimal(anfragepositionlieferdatenDto
					.getNStandardmenge());

			setIdentVisible(true);
			wtfIdent.setText(artikelDto.getCNr());

			wlaIdent.setOKey(artikelDto.getIId());

			wtfArtikelbezeichnungbeimlieferanten
					.setText(anfragepositionlieferdatenDto
							.getCBezbeilieferant());
			wtfArtikelnummerbeimlieferanten
					.setText(anfragepositionlieferdatenDto
							.getCArtikelnrlieferant());

			// die sprachabhaengig Artikelbezeichnung anzeigen
			cArtikelbezeichnung = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.formatArtikelbezeichnungEinzeiligOhneExc(
							anfragepositionDto.getArtikelIId());

			// die Artikelzusatzbezeichnung anzeigen
			ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
			if (artikelsprDto != null) {
				if (artikelsprDto.getCZbez() != null) {
					wtfArtikelzusatzbez.setText(artikelsprDto.getCZbez());
				}
			}

			setArtikelbezeichnungbeimlieferantenVisible(true);
		} else {
			setIdentVisible(false);

			if (anfragepositionDto.getCBez() != null) {
				cArtikelbezeichnung = anfragepositionDto.getCBez();
			}

			setArtikelbezeichnungbeimlieferantenVisible(false);
		}

		wtfArtikelbezeichnung.setText(cArtikelbezeichnung);
		wtfArtikelzusatzbez.setText(anfragepositionDto.getCZusatzbez());

		wnfAnlieferzeit.setInteger(anfragepositionlieferdatenDto.getIAnlieferzeit());

		wsfZertifikatart.setKey(anfragepositionlieferdatenDto
				.getZertifikatartIId());

		if (Helper.short2boolean(anfragepositionlieferdatenDto.getBErfasst())) {
			wnfAnliefermenge.setBigDecimal(anfragepositionlieferdatenDto
					.getNAnliefermenge());
			wnfAngeboteinzelpreis.setBigDecimal(anfragepositionlieferdatenDto
					.getNNettogesamtpreis());
		} else {

			wnfAnliefermenge.setText("");
			wnfAngeboteinzelpreis.setText("");
		}

		wlaEinheitAnliefermenge.setText(anfragepositionDto.getEinheitCNr()
				.trim());

		wnfWunschmenge.setBigDecimal(anfragepositionDto.getNMenge());
		wlaEinheitWunschmenge
				.setText(anfragepositionDto.getEinheitCNr().trim());
		wnfRichtpreis.setBigDecimal(anfragepositionDto.getNRichtpreis());
	}

	private void components2Dto() throws Throwable {
		anfragepositionlieferdatenDto
				.setCBezbeilieferant(wtfArtikelbezeichnungbeimlieferanten
						.getText());
		anfragepositionlieferdatenDto
				.setCArtikelnrlieferant(wtfArtikelnummerbeimlieferanten
						.getText());

		anfragepositionlieferdatenDto
				.setNMindestbestellmenge(wnfMindestbestellmenge.getBigDecimal());
		anfragepositionlieferdatenDto
				.setNVerpackungseinheit(wnfVerpackungseinheit.getBigDecimal());
		anfragepositionlieferdatenDto.setNStandardmenge(wnfStandardmenge
				.getBigDecimal());

		anfragepositionlieferdatenDto
				.setIAnlieferzeit(wnfAnlieferzeit.getInteger());
		anfragepositionlieferdatenDto.setNAnliefermenge(wnfAnliefermenge
				.getBigDecimal());
		anfragepositionlieferdatenDto
				.setNNettogesamtpreis(wnfAngeboteinzelpreis.getBigDecimal());
		anfragepositionlieferdatenDto.setBErfasst(new Short((short) 0));

		anfragepositionlieferdatenDto.setZertifikatartIId(wsfZertifikatart
				.getIKey());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		wlaIdent.getWrapperButtonGoTo().setEnabled(true);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			anfragepositionlieferdatenDto = DelegateFactory.getInstance()
					.getAnfragepositionDelegate()
					.anfragepositionlieferdatenFindByPrimaryKey((Integer) oKey);

			anfragepositionDto = DelegateFactory.getInstance()
					.getAnfragepositionDelegate()
					.anfragepositionFindByPrimaryKey(
							anfragepositionlieferdatenDto
									.getAnfragepositionIId());

			dto2Components();

		}

		tpAnfrage.setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr(
				"anf.panel.positionlieferdaten"));

		// die Anfrage fuer die Statusbar neu einlesen
		if (anfragepositionDto != null) {
			tpAnfrage.setAnfrageDto(DelegateFactory.getInstance()
					.getAnfrageDelegate().anfrageFindByPrimaryKey(
							anfragepositionDto.getBelegIId()));
		}

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenLieferdatenErlaubt()) {
			DelegateFactory.getInstance().getAnfragepositionDelegate()
					.resetAnfragepositionlieferdaten(
							anfragepositionlieferdatenDto);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
			// ueberschreiben
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(
							AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(
							AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}
}
