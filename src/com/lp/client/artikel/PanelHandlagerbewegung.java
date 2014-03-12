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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.DialogSnrChnrauswahl;
import com.lp.client.frame.component.FehlmengenAufloesen;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCHNRField;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerumbuchungDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelHandlagerbewegung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Border border1;
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();

	private WrapperButton wbuLagerZu = new WrapperButton();
	private WrapperTextField wtfLagerZu = new WrapperTextField();

	private WrapperRadioButton wrbZugang = new WrapperRadioButton();
	private WrapperRadioButton wrbAbgang = new WrapperRadioButton();
	private WrapperRadioButton wrbUmbuchung = new WrapperRadioButton();
	private WrapperSnrChnrField wtfSeriennr = new WrapperSnrChnrField(
			getInternalFrame());
	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private HandlagerbewegungDto handlagerbewegungDto = null;
	private PanelQueryFLR panelQueryFLRLager = null;
	private PanelQueryFLR panelQueryFLRLagerZu = null;
	private PanelQueryFLR panelQueryFLRLagerplatz = null;

	private WrapperSelectField wsfHersteller = null;
	private WrapperSelectField wsfLand = null;

	private Integer letzteLagerIId = null;
	boolean hatRechtCUD = false;
	int iDefaultHandbuchungsart = 0;
	private Integer zielLager = null;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn;
	private GridBagLayout gridBagLayoutWorkingOn = new GridBagLayout();
	// ... bis hier ist's immer gleich
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	static final public String ACTION_SPECIAL_LAGER_ZU_FROM_LISTE = "action_lager_zu_from_liste";
	static final public String ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE = "action_lagerplatz_from_liste";

	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaLagerstand = new WrapperLabel();
	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperNumberField wnfPreis = new WrapperNumberField();
	private WrapperIdentField wifArtikel = new WrapperIdentField();
	private WrapperLabel wlaMengeneinheit = new WrapperLabel();
	private WrapperTextField wtfLagerplatz = new WrapperTextField();
	private WrapperButton wbuLagerplatz = new WrapperButton();

	public PanelHandlagerbewegung(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;

		hatRechtCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_WW_HANDLAGERBEWEGUNG_CUD);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMenge;
	}

	protected void setDefaults() {
		wrbZugang.setSelected(true);
	}

	private void aktualisiereFelderSnrChnr(ArtikelDto artikelDto,
			boolean bEnableField,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			Integer lagerIId) throws Throwable {
		if (Helper.short2boolean(artikelDto.getBChargennrtragend())
				|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
			wnfMenge.setActivatable(false);
			wnfMenge.setEditable(bEnableField);
			wtfSeriennr.setSeriennummern(alSeriennrChargennrMitMenge,
					artikelDto, lagerIId);
			wtfSeriennr.setVisible(true);
			wtfSeriennr.setMandatoryField(true);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(true);

			jpaWorkingOn.repaint();

		} else {
			wnfMenge.setActivatable(true);
			wnfMenge.setEditable(bEnableField);
			wtfSeriennr.setVisible(false);
			wtfSeriennr.setMandatoryField(false);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
			jpaWorkingOn.repaint();

		}
		if (bEnableField) {
			wtfSeriennr.getButtonSnrAuswahl().setEnabled(bEnableField);
		}

	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		// Actionpanel
		JPanel jpaButtonAction = getToolsPanel();

		wlaLagerstand.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLagerstand.setText("");
		wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gestehungspreis"));
		wnfPreis.setMandatoryField(true);

		int iNachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseAllgemein();
		wnfPreis.setFractionDigits(iNachkommastellen);

		wsfHersteller = new WrapperSelectField(WrapperSelectField.HERSTELLER,
				getInternalFrame(), true);
		wsfLand = new WrapperSelectField(WrapperSelectField.LAND,
				getInternalFrame(), true);
		wsfLand.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.ursprungsland")
				+ "...");

		wtfLager.setActivatable(false);
		wtfLager.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);

		int iNachkommastellenMenge = Defaults.getInstance()
				.getIUINachkommastellenMenge();
		wnfMenge.setFractionDigits(iNachkommastellenMenge);
		wtfKommentar.setMandatoryField(true);

		wrbZugang
				.addActionListener(new PanelHandlagerbewegung_wrapperRadioButtonZugang_actionAdapter(
						this));
		wrbAbgang
				.addActionListener(new PanelHandlagerbewegung_wrapperRadioButtonAbgang_actionAdapter(
						this));
		wrbUmbuchung
				.addActionListener(new PanelHandlagerbewegung_wrbUmbuchung_actionAdapter(
						this));
		wlaMengeneinheit.setRequestFocusEnabled(true);
		wlaMengeneinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMengeneinheit.setText("");
		wtfLagerplatz.setText("");
		wtfLagerplatz.setActivatable(false);
		wbuLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.lagerplatz"));

		wbuLagerplatz
				.setActionCommand(PanelHandlagerbewegung.ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		wbuLagerplatz.addActionListener(this);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LAGERBEWEGUNG_MIT_URSPRUNG,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bHerstellerUrsprungsland = ((Boolean) parameter
				.getCWertAsObject());

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DEFAULT_HANDBUCHUNGSART,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		iDefaultHandbuchungsart = ((Integer) parameter.getCWertAsObject());


		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wtfLagerZu.setMandatoryField(true);
		wtfLagerZu.setActivatable(false);

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));
		wtfKommentar.setText("");
		wtfKommentar.setColumnsMax(ArtikelFac.MAX_HANDLAGERBEWEGUNG_KOMMENTAR);
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLagerZu.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.nachlager"));

		wbuLager.setActionCommand(PanelHandlagerbewegung.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLagerZu
				.setActionCommand(PanelHandlagerbewegung.ACTION_SPECIAL_LAGER_ZU_FROM_LISTE);
		wbuLager.addActionListener(this);
		wbuLagerZu.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);

		wtfLager.setText("");
		wrbZugang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.lagerzugang"));
		wrbAbgang.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.lagerabgang"));
		wrbUmbuchung.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.lagerumbuchung"));
		wtfSeriennr.setText("");
		wtfSeriennr.setMandatoryField(true);
		wtfSeriennr.setActivatable(false);
		wtfSeriennr.setWnfBelegMenge(wnfMenge);
		
		if (hatRechtCUD == false) {
			wrbZugang.setActivatable(false);
			wrbAbgang.setActivatable(false);
		}
		
		buttonGroup1.add(wrbZugang);
		buttonGroup1.add(wrbAbgang);
		buttonGroup1.add(wrbUmbuchung);
		jpaWorkingOn = new JPanel(new GridBagLayout());
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		int iZeile = 0;		
		jpaWorkingOn.add(wrbZugang, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAbgang, new GridBagConstraints(3, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbUmbuchung, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bHerstellerUrsprungsland == true) {
			jpaWorkingOn.add(wsfHersteller.getWrapperButton(), new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wsfHersteller.getWrapperTextField(), new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wtfSeriennr.getButtonSnrAuswahl(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 140, 0));
		jpaWorkingOn.add(wtfSeriennr, new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bHerstellerUrsprungsland == true) {
			jpaWorkingOn.add(wsfLand.getWrapperButton(), new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wsfLand.getWrapperTextField(), new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 100, 0));
		jpaWorkingOn.add(wlaLagerstand, new GridBagConstraints(6, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLagerZu, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerZu, new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLagerplatz, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLagerplatz, new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMengeneinheit, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), -3, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaPreis, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPreis, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		

		String[] aWhichButtonIUse = null;

		if (hatRechtCUD == true) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, PanelBasis.ACTION_PRINT, };
		} else {
			aWhichButtonIUse = new String[] { ACTION_SAVE, ACTION_DISCARD,
					PanelBasis.ACTION_PRINT, };

		}

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		wifArtikel.getWbuArtikel().setEnabled(false);
		wifArtikel.getWtfIdent().setEnabled(false);
		wbuLager.setEnabled(false);
		wbuLagerplatz.setEnabled(false);
		wbuLagerZu.setEnabled(false);
		wbuLagerZu.setEnabled(false);
		wrbAbgang.setEnabled(false);
		wrbZugang.setEnabled(false);
		wrbUmbuchung.setEnabled(false);

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		// PJ17961
		HandlagerbewegungDto zugehDto = DelegateFactory.getInstance()
				.getLagerDelegate()
				.getZugehoerigeUmbuchung(handlagerbewegungDto.getIId());
		if (zugehDto != null) {

			// Zuerst nachfragen
			int i = DialogFactory.showModalJaNeinAbbrechenDialog(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.umbuchung.zugehoerige.loeschen"), LPMain
							.getInstance().getTextRespectUISPr("lp.frage"));

			if (i == JOptionPane.YES_OPTION) {
				DelegateFactory.getInstance().getLagerDelegate()
						.removeHandlagerbewegung(handlagerbewegungDto);
				DelegateFactory.getInstance().getLagerDelegate()
						.removeHandlagerbewegung(zugehDto);
			} else if (i == JOptionPane.NO_OPTION) {
				DelegateFactory.getInstance().getLagerDelegate()
						.removeHandlagerbewegung(handlagerbewegungDto);
			} else if (i == JOptionPane.CANCEL_OPTION) {
				return;
			}

		} else {
			DelegateFactory.getInstance().getLagerDelegate()
					.removeHandlagerbewegung(handlagerbewegungDto);
		}

		super.eventActionDelete(e, false, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_HANDLAGERBEWEGUNG;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LAGER_ZU_FROM_LISTE)) {
			dialogQueryLagerZuFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE)) {
			dialogQueryLagerplatzFromListe(e);
		}

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

		LagerbewegungDto[] dtos = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerbewegungFindByBelegartCNrBelegartPositionIId(
						LocaleFac.BELEGART_HAND, handlagerbewegungDto.getIId());

		Integer iIdBuchung = dtos[0].getIIdBuchung();

		if (Helper.short2boolean(dtos[0].getBAbgang()) == true) {

			LagerumbuchungDto[] umbuchungen = DelegateFactory.getInstance()
					.getLagerDelegate()
					.lagerumbuchungFindByIdAbbuchung(iIdBuchung);

			if (umbuchungen != null && umbuchungen.length > 0) {
				// UMBUCHUNGSBELEG
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"artikel.handlagerbewegung.lagerumbuchung");
				getInternalFrame().showReportKriterien(
						new ReportUmbuchungsbeleg(getInternalFrame(),
								umbuchungen[0].getILagerbewegungidzubuchung(),
								umbuchungen[0].getILagerbewegungidabbuchung(),
								add2Title));

			} else {
				// NORMALER BELEG
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"artikel.handlagerbewegung.lagerbuchungsbeleg");
				getInternalFrame().showReportKriterien(
						new ReportLagerbuchungsbeleg(getInternalFrame(),
								iIdBuchung, add2Title));
			}

		} else {
			LagerumbuchungDto[] umbuchungen = DelegateFactory.getInstance()
					.getLagerDelegate()
					.lagerumbuchungFindByIdZubuchung(iIdBuchung);
			if (umbuchungen != null && umbuchungen.length > 0) {
				// UMBUCHUNGSBELEG
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"artikel.handlagerbewegung.lagerumbuchung");
				getInternalFrame().showReportKriterien(
						new ReportUmbuchungsbeleg(getInternalFrame(),
								umbuchungen[0].getILagerbewegungidzubuchung(),
								umbuchungen[0].getILagerbewegungidabbuchung(),
								add2Title));

			} else {
				// NORMALER BELEG
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"artikel.handlagerbewegung.lagerbuchungsbeleg");
				getInternalFrame().showReportKriterien(
						new ReportLagerbuchungsbeleg(getInternalFrame(),
								iIdBuchung, add2Title));
			}

		}

	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {

		if (handlagerbewegungDto.getArtikelIId() != null
				&& (wrbUmbuchung.isSelected() || wrbAbgang.isSelected())) {
			panelQueryFLRLager = new PanelQueryFLR(null, ArtikelFilterFactory
					.getInstance().createFKArtikellager(
							handlagerbewegungDto.getArtikelIId()),
					QueryParameters.UC_ID_ARTIKELLAGER, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("label.lager"));
			panelQueryFLRLager.befuellePanelFilterkriterienDirektUndVersteckte(
					null, null, ArtikelFilterFactory.getInstance()
							.createFKVLager());
			if (handlagerbewegungDto.getArtikelIId() != null
					&& handlagerbewegungDto.getLagerIId() != null) {
				panelQueryFLRLager.setSelectedId(new WwArtikellagerPK(
						handlagerbewegungDto.getArtikelIId(),
						handlagerbewegungDto.getLagerIId()));
			}
			new DialogQuery(panelQueryFLRLager);

		} else {
			// Wenn noch kein Artikel Ausgewaehlt ist, dann normale Lagerliste
			// anzeigen
			panelQueryFLRLager = ArtikelFilterFactory.getInstance()
					.createPanelFLRLager(getInternalFrame(),
							handlagerbewegungDto.getLagerIId());

			new DialogQuery(panelQueryFLRLager);
		}
	}

	void dialogQueryLagerZuFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerZu = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(),
						handlagerbewegungDto.getLagerIId());

		new DialogQuery(panelQueryFLRLagerZu);
	}

	void dialogQueryLagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRLagerplatz(getInternalFrame(), null, true);

		new DialogQuery(panelQueryFLRLagerplatz);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		handlagerbewegungDto = new HandlagerbewegungDto();
		handlagerbewegungDto.setBAendereLagerplatz(true);
		wlaLagerstand.setText("");

		if (letzteLagerIId == null) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_DEFAULT_LAGER);

			letzteLagerIId = new Integer(parameter.getCWert());
		}

		try {
			LagerDto lagerDto = DelegateFactory.getInstance()
					.getLagerDelegate().lagerFindByPrimaryKey(letzteLagerIId);
			if (lagerDto != null
					&& lagerDto.getMandantCNr().equals(
							LPMain.getInstance().getTheClient().getMandant())) {
				wtfLager.setText(lagerDto.getCNr());
				handlagerbewegungDto.setLagerIId(lagerDto.getIId());
				letzteLagerIId = lagerDto.getIId();
			}
		} catch (ExceptionLP e) {
			if (e.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.error.parameterdefaultlager"));

			} else {
				throw e;
			}
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wlaLagerstand.setText("");
		if (allMandatoryFieldsSetDlg()) {

			if (hatRechtCUD == false) {
				if (wrbUmbuchung.isSelected() == false) {
					// Meldung
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"artikel.handlagerbewegung.nurumbuchen"));
					return;
				}
			}

			boolean bAbbuchen = true;
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_HANDLAGERBUCHUNG_WERT_OBERGRENZE);
			BigDecimal wert_param = (BigDecimal) parameter.getCWertAsObject();

			BigDecimal wert_akt = wnfMenge.getBigDecimal().multiply(
					wnfPreis.getBigDecimal());

			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_HANDLAGERBUCHUNG_MENGE_OBERGRENZE);

			BigDecimal menge_param = (BigDecimal) parameter.getCWertAsObject();
			if (wert_param.doubleValue() < wert_akt.doubleValue()) {
				bAbbuchen = (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.warning.wertzugross")
								+ " "
								+ wert_param.doubleValue()
								+ ". "
								+ LPMain.getInstance().getTextRespectUISPr(
										"artikel.warning.trotzdembuchen"),
						LPMain.getInstance().getTextRespectUISPr("lp.warning"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			}
			if (menge_param.doubleValue() < wnfMenge.getBigDecimal()
					.doubleValue() && bAbbuchen == true) {
				bAbbuchen = (DialogFactory.showMeldung(
						LPMain.getInstance().getTextRespectUISPr(
								"artikel.warning.mengezugross")
								+ " "
								+ menge_param.doubleValue()
								+ " "
								+ wlaMengeneinheit.getText()
								+ " "
								+ LPMain.getInstance().getTextRespectUISPr(
										"artikel.warning.trotzdembuchen"),
						LPMain.getInstance().getTextRespectUISPr("lp.warning"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			}
			if (bAbbuchen == true) {
				if (wrbAbgang.isSelected()) {
					handlagerbewegungDto.setBAbgang(new Short((short) 1));
					handlagerbewegungDto.setNVerkaufspreis(wnfPreis
							.getBigDecimal());
					handlagerbewegungDto.setNEinstandspreis(null);
					handlagerbewegungDto.setNGestehungspreis(null);
				} else {
					handlagerbewegungDto.setBAbgang(new Short((short) 0));
					handlagerbewegungDto.setNVerkaufspreis(null);
					handlagerbewegungDto.setNEinstandspreis(wnfPreis
							.getBigDecimal());

				}

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								handlagerbewegungDto.getArtikelIId());

				handlagerbewegungDto.setHerstellerIId(wsfHersteller.getIKey());
				handlagerbewegungDto.setLandIId(wsfLand.getIKey());

				letzteLagerIId = handlagerbewegungDto.getLagerIId();

				handlagerbewegungDto.setCKommentar(wtfKommentar.getText());

				handlagerbewegungDto.setNMenge(wnfMenge.getBigDecimal());

				if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())
						|| Helper.short2Boolean(artikelDto
								.getBChargennrtragend())) {
					handlagerbewegungDto
							.setSeriennrChargennrMitMenge(wtfSeriennr
									.getSeriennummern());
				} else {
					handlagerbewegungDto.setSeriennrChargennrMitMenge(null);
				}

				if (handlagerbewegungDto.getIId() == null) {
					if (wrbUmbuchung.isSelected()) {

						if (handlagerbewegungDto.getLagerIId()
								.equals(zielLager)) {
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.error"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"artikel.handlagerbewegung.error.umbuchung.selbeslager"));

							return;
						}

						DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.bucheUm(
										handlagerbewegungDto.getArtikelIId(),
										handlagerbewegungDto.getLagerIId(),
										handlagerbewegungDto.getArtikelIId(),
										zielLager,
										handlagerbewegungDto.getNMenge(),
										handlagerbewegungDto
												.getSeriennrChargennrMitMenge(),
										handlagerbewegungDto.getCKommentar(),
										handlagerbewegungDto
												.getNEinstandspreis());
					} else {
						handlagerbewegungDto.setIId(DelegateFactory
								.getInstance().getLagerDelegate()
								.createHandlagerbewegung(handlagerbewegungDto));

					}
				} else {
					DelegateFactory.getInstance().getLagerDelegate()
							.updateHandlagerbewegung(handlagerbewegungDto);

					// PJ17961
					HandlagerbewegungDto zugehDto = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getZugehoerigeUmbuchung(
									handlagerbewegungDto.getIId());

					if (zugehDto != null) {
						// Zuerst nachfragen
						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"artikel.umbuchung.zugehoerige.aendern"));
						if (b == true) {
							zugehDto.setNMenge(handlagerbewegungDto.getNMenge());
							zugehDto.setSeriennrChargennrMitMenge(handlagerbewegungDto.getSeriennrChargennrMitMenge());
							DelegateFactory.getInstance().getLagerDelegate()
									.updateHandlagerbewegung(zugehDto);
						}
					}

				}

				setKeyWhenDetailPanel(handlagerbewegungDto.getIId());
				wlaLagerstand.setText("");

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							handlagerbewegungDto.getIId().toString());
				}
				if (Helper.short2boolean(handlagerbewegungDto.getBAbgang()) == false) {
					FehlmengenAufloesen
							.fehlmengenAufloesen(getInternalFrame(),
									handlagerbewegungDto.getArtikelIId(),
									handlagerbewegungDto.getLagerIId(),
									handlagerbewegungDto
											.getSeriennrChargennrMitMenge(),
									handlagerbewegungDto.getNMenge());

				}
				eventYouAreSelected(false);
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			clearStatusbar();
			// Den Artikel aus der Artikelauswahllliste des Moduls vorbesetzen
			if (internalFrameArtikel.getArtikelDto() != null) {

				if (handlagerbewegungDto != null) {

					wifArtikel.setArtikelDto(internalFrameArtikel
							.getArtikelDto());
					EventObject e = new ItemChangedEvent(wifArtikel,
							ItemChangedEvent.GOTO_DETAIL_PANEL);
					eventItemchanged(e);

					wsfHersteller.setKey(internalFrameArtikel.getArtikelDto()
							.getHerstellerIId());
					wsfLand.setKey(internalFrameArtikel.getArtikelDto()
							.getLandIIdUrsprungsland());

				}

			}
			if (hatRechtCUD == false) {
				wrbUmbuchung.setSelected(true);
				wrbUmbuchung_actionPerformed(null);
			} else {

				if (iDefaultHandbuchungsart == 0) {
					wrbZugang.setSelected(true);
					wrapperRadioButtonZugang_actionPerformed(null);
				} else if (iDefaultHandbuchungsart == 1) {
					wrbAbgang.setSelected(true);
					wrapperRadioButtonAbgang_actionPerformed(null);
				} else if (iDefaultHandbuchungsart == 2) {
					wrbUmbuchung.setSelected(true);
					wrbUmbuchung_actionPerformed(null);
				}

			}

		} else {
			handlagerbewegungDto = DelegateFactory.getInstance()
					.getLagerDelegate()
					.handlagerbewegungFindByPrimaryKey((Integer) key);

			try {
				ArtikellagerplaetzeDto dto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.artikellagerplaetzeFindByArtikelIIdLagerIId(
								handlagerbewegungDto.getArtikelIId(),
								handlagerbewegungDto.getLagerIId());
				wtfLagerplatz.setText(dto.getLagerplatzDto().getCLagerplatz());
			} catch (Throwable ex1) {
				wtfLagerplatz.setText(null);
			}

			wnfMenge.setBigDecimal(handlagerbewegungDto.getNMenge());
			if (Helper.short2boolean(handlagerbewegungDto.getBAbgang())) {
				wrbAbgang.setSelected(true);
				wnfPreis.setBigDecimal(handlagerbewegungDto.getNVerkaufspreis());
			} else {
				wrbZugang.setSelected(true);
				wnfPreis.setBigDecimal(handlagerbewegungDto
						.getNEinstandspreis());
			}
			wrapperRadioButtonAbgang_actionPerformed(new ActionEvent(this, 0,
					""));
			wtfKommentar.setText(handlagerbewegungDto.getCKommentar());
			wtfLager.setText(handlagerbewegungDto.getLagerDto().getCNr());

			wifArtikel.setArtikelDto(handlagerbewegungDto.getArtikelDto());

			wsfHersteller.setKey(handlagerbewegungDto.getHerstellerIId());

			wsfLand.setKey(handlagerbewegungDto.getLandIId());

			aktualisiereFelderSnrChnr(handlagerbewegungDto.getArtikelDto(),
					false, handlagerbewegungDto.getSeriennrChargennrMitMenge(),
					handlagerbewegungDto.getLagerIId());

			wlaLagerstand.setText("");

			this.setStatusbarPersonalIIdAendern(handlagerbewegungDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(handlagerbewegungDto.getTBuchungszeit());

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikel) {
				ArtikelDto artikelDto = wifArtikel.getArtikelDto();
				wlaMengeneinheit.setText(artikelDto.getEinheitCNr());
				handlagerbewegungDto.setArtikelIId(artikelDto.getIId());
				handlagerbewegungDto.setArtikelDto(artikelDto);

				if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {

				} else {
					LagerDto dto = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerFindByCNrByMandantCNr(
									LagerFac.LAGER_KEINLAGER);
					if (dto != null) {
						handlagerbewegungDto.setLagerIId(dto.getIId());
						wtfLager.setText(dto.getCNr());
					}

				}

				aktualisiereFelderSnrChnr(artikelDto, true,
						handlagerbewegungDto.getSeriennrChargennrMitMenge(),
						handlagerbewegungDto.getLagerIId());

				if (handlagerbewegungDto.getLagerIId() != null) {
					LagerDto lagerDto = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey(
									handlagerbewegungDto.getLagerIId());
					wtfLager.setText(lagerDto.getCNr());
					handlagerbewegungDto.setLagerIId(lagerDto.getIId());

					if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
							|| Helper.short2boolean(artikelDto
									.getBChargennrtragend())) {
						handlagerbewegungDto
								.setSeriennrChargennrMitMenge(wtfSeriennr
										.getSeriennummern());
					} else {
						handlagerbewegungDto.setSeriennrChargennrMitMenge(null);
					}

					wnfPreis.setBigDecimal(DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getGemittelterGestehungspreisEinesLagers(
									artikelDto.getIId(),
									handlagerbewegungDto.getLagerIId()));
					BigDecimal lagerstand = new BigDecimal(0);

					lagerstand = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(
									handlagerbewegungDto.getArtikelIId(),
									handlagerbewegungDto.getLagerIId());

					wlaLagerstand.setText(Helper.formatZahl(lagerstand,
							Defaults.getInstance()
									.getIUINachkommastellenMenge(), LPMain
									.getInstance().getTheClient().getLocUi())
							+ " "
							+ LPMain.getInstance().getTextRespectUISPr(
									"artikel.handlagerbewegung.auflager"));

					if (handlagerbewegungDto.getArtikelIId() != null
							&& lagerDto.getIId() != null) {
						ArtikellagerplaetzeDto dto = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.artikellagerplaetzeFindByArtikelIIdLagerIId(
										handlagerbewegungDto.getArtikelIId(),
										lagerDto.getIId());
						if (dto != null) {
							wtfLagerplatz.setText(dto.getLagerplatzDto()
									.getCLagerplatz());
							handlagerbewegungDto.setLagerplatzIId(dto
									.getLagerplatzIId());
						} else {
							wtfLagerplatz.setText(null);
							handlagerbewegungDto.setLagerplatzIId(null);
						}
					}

				}

			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				// PJ 17798
				if (key instanceof WwArtikellagerPK) {
					key = ((WwArtikellagerPK) key).getLager_i_id();
				}

				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				handlagerbewegungDto.setLagerIId(lagerDto.getIId());

				if (handlagerbewegungDto.getArtikelDto() != null) {

					if (Helper.short2boolean(handlagerbewegungDto
							.getArtikelDto().getBChargennrtragend()) == true
							|| Helper.short2boolean(handlagerbewegungDto
									.getArtikelDto().getBSeriennrtragend()) == true) {
						handlagerbewegungDto
								.setSeriennrChargennrMitMenge(wtfSeriennr
										.getSeriennummern());
						aktualisiereFelderSnrChnr(
								handlagerbewegungDto.getArtikelDto(), true,
								handlagerbewegungDto
										.getSeriennrChargennrMitMenge(),
								handlagerbewegungDto.getLagerIId());

					} else {
						handlagerbewegungDto.setSeriennrChargennrMitMenge(null);
					}
				}

				if (handlagerbewegungDto.getArtikelIId() != null) {

					wnfPreis.setBigDecimal(DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getGemittelterGestehungspreisEinesLagers(
									handlagerbewegungDto.getArtikelIId(),
									handlagerbewegungDto.getLagerIId()));

					BigDecimal lagerstand = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(
									handlagerbewegungDto.getArtikelIId(),
									handlagerbewegungDto.getLagerIId());
					wlaLagerstand.setText(Helper.formatZahl(lagerstand,
							Defaults.getInstance()
									.getIUINachkommastellenMenge(), LPMain
									.getInstance().getTheClient().getLocUi())
							+ " "
							+ LPMain.getInstance().getTextRespectUISPr(
									"artikel.handlagerbewegung.auflager"));

				}

				if (handlagerbewegungDto.getArtikelIId() != null
						&& lagerDto.getIId() != null) {
					ArtikellagerplaetzeDto dto = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.artikellagerplaetzeFindByArtikelIIdLagerIId(
									handlagerbewegungDto.getArtikelIId(),
									lagerDto.getIId());

					if (dto != null) {
						wtfLagerplatz.setText(dto.getLagerplatzDto()
								.getCLagerplatz());
						handlagerbewegungDto.setLagerplatzIId(dto
								.getLagerplatzIId());
					} else {
						wtfLagerplatz.setText(null);
						handlagerbewegungDto.setLagerplatzIId(null);
					}
				}

			} else if (e.getSource() == panelQueryFLRLagerZu) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLagerZu.setText(lagerDto.getCNr());
				zielLager = lagerDto.getIId();
			} else if (e.getSource() == panelQueryFLRLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				LagerplatzDto lagerplatzDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerplatzFindByPrimaryKey((Integer) key);
				wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
				handlagerbewegungDto.setLagerplatzIId(lagerplatzDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLagerplatz) {
				wtfLagerplatz.setText(null);
				handlagerbewegungDto.setLagerplatzIId(null);
			}
		}
	}

	void wrapperRadioButtonZugang_actionPerformed(ActionEvent e) {
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLagerZu.setVisible(false);
		wtfLagerZu.setVisible(false);
		wtfLagerZu.setMandatoryField(false);
		wbuLagerplatz.setVisible(true);
		wtfLagerplatz.setVisible(true);
		zielLager = null;
		wtfLagerZu.setText(null);
		jpaWorkingOn.repaint();
		if (wrbAbgang.isSelected()) {
			wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
					"artikel.handlagerbewegung.verkaufspreis"));
		} else {
			wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.gestehungspreis"));

		}

	}

	void wrapperRadioButtonAbgang_actionPerformed(ActionEvent e) {
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLagerZu.setVisible(false);
		wtfLagerZu.setVisible(false);
		wtfLagerZu.setMandatoryField(false);
		wbuLagerplatz.setVisible(true);
		wtfLagerplatz.setVisible(true);
		
		zielLager = null;
		wtfLagerZu.setText(null);
		jpaWorkingOn.repaint();
		if (wrbAbgang.isSelected()) {
			wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
					"artikel.handlagerbewegung.verkaufspreis"));
		} else {
			wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.gestehungspreis"));

		}

	}

	public void wrbUmbuchung_actionPerformed(ActionEvent e) {
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.handlagerbewegung.vonlager"));
		wbuLagerZu.setEnabled(true);
		wbuLagerplatz.setVisible(false);
		wtfLagerplatz.setVisible(false);
		wbuLagerZu.setVisible(true);
		wtfLagerZu.setVisible(true);
		wtfLagerZu.setMandatoryField(true);

		jpaWorkingOn.repaint();

	}

}

class PanelHandlagerbewegung_wrbUmbuchung_actionAdapter implements
		ActionListener {
	private PanelHandlagerbewegung adaptee;

	PanelHandlagerbewegung_wrbUmbuchung_actionAdapter(
			PanelHandlagerbewegung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrbUmbuchung_actionPerformed(e);
	}
}

class PanelHandlagerbewegung_wrapperRadioButtonZugang_actionAdapter implements
		java.awt.event.ActionListener {
	PanelHandlagerbewegung adaptee;

	PanelHandlagerbewegung_wrapperRadioButtonZugang_actionAdapter(
			PanelHandlagerbewegung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrapperRadioButtonZugang_actionPerformed(e);
	}
}

class PanelHandlagerbewegung_wrapperRadioButtonAbgang_actionAdapter implements
		java.awt.event.ActionListener {
	PanelHandlagerbewegung adaptee;

	PanelHandlagerbewegung_wrapperRadioButtonAbgang_actionAdapter(
			PanelHandlagerbewegung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wrapperRadioButtonAbgang_actionPerformed(e);
	}
}
