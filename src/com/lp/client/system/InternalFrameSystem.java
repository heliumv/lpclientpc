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
package com.lp.client.system;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.Command;
import com.lp.client.frame.CommandGoto;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.EinheitKonvertierungDto;
import com.lp.server.system.service.FunktionDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaartDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.PanelDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.PositionsartDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.StatusDto;
import com.lp.server.system.service.ZahlungszielDto;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich alle Systemtabellen wie Locale, Rechte,
 * Versand, Mandant, System, ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; dd.mm.04</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version $Revision: 1.12 $ Date $Date: 2011/03/24 07:39:17 $
 */
public class InternalFrameSystem extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneGesperrt tpRechte = null;
	private TabbedPaneSystem tpSystem = null;
	private TabbedPaneMandant tpMandant = null;
	private TabbedPaneLocale tpLocale = null;
	private TabbedPaneMediastandard tpMedia = null;
	private TabbedPaneParameter tpParameter = null;
	private TabbedPaneArbeitsplatzparameter tpArbeitsplatzparameter = null;
	private TabbedPaneVersandauftrag tpVersandauftrag = null;
	private TabbedPaneExtraliste tpExtraliste = null;
	private TabbedPaneAutomatik tpAutomatik = null;
	private TabbedPanePanel tpPanel = null;
	private TabbedPaneDokumente tpDokumente = null;
	private TabbedPanePflege tpPflege = null;
	private TabbedPaneNachrichtarchiv tpNachrichtenarchiv = null;

	private MandantDto mandantDto = new MandantDto();
	private EinheitDto einheitDto = new EinheitDto();
	private BelegartDto belegartDto = new BelegartDto();
	private LieferartDto lieferart2Dto = new LieferartDto();
	private StatusDto statusDto = new StatusDto();
	private FunktionDto funktionDto = new FunktionDto();
	private PositionsartDto positionsartDto = new PositionsartDto();
	private SpediteurDto spediteurDto = new SpediteurDto();
	private KostenstelleDto kostenstelleDto = new KostenstelleDto();
	private BestellungtextDto bestellungtextDto = new BestellungtextDto();
	private EinheitKonvertierungDto einheitKonvertierungDto = new EinheitKonvertierungDto();
	private PanelDto panelDto;
	private ArbeitsplatzDto arbeitsplatzDto;
	public String sRechtModulweit = null;

	// stdcrud: hier stehe die DTOs; muss leer angelegt werden.
	private ZahlungszielDto zahlungszielDto = new ZahlungszielDto();
	private MwstsatzDto mwstsatzDto = new MwstsatzDto();
	private MwstsatzbezDto mwstsatzbezDto = new MwstsatzbezDto();
	private MediaartDto mediaartDto = new MediaartDto();

	public static final int IDX_TABBED_PANE_SYSTEM = 0;
	public static final int IDX_TABBED_PANE_RECHT = 1;
	public static final int IDX_PANE_MANDANT = 2;
	public static final int IDX_PANE_LOCALE = 3;
	public static final int IDX_PANE_MEDIA = 4;
	public static final int IDX_PANE_PARAMETER = 5;
	public static final int IDX_PANE_ARBEITSPLATZPARAMETER = 6;
	public static final int IDX_PANE_VERSANDAUFTRAG = 7;
	public static final int IDX_PANE_NACHRICHTENARCHIV = 8;
	public static final int IDX_PANE_EXTRALISTE = 9;
	public static final int IDX_PANE_AUTOMATIK = 10;
	public static final int IDX_PANE_DOKUMENTE = 11;
	public static final int IDX_PANE_PFLEGE = 12;
	public static final int IDX_PANE_EIGENSCHAFTENDEFINITION = 13;

	
	
	public InternalFrameSystem(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		sRechtModulweit = sRechtModulweitI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// 1 tab unten; System; lazy loading
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.system.modulname"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.system.tooltip"),
				IDX_TABBED_PANE_SYSTEM);

		// 2 tab unten; Rechte; lazy loading
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.gesperrt"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.system.tooltip"),
				IDX_TABBED_PANE_RECHT);

		// 2 unteres tab: Mandant; lazy loading.
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.mandant"), IDX_PANE_MANDANT);

		// 4. Tab unten: Locale
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.locale"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.locale"), IDX_PANE_LOCALE);

		// 5. Tab unten: Media
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.medien"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.medien"), IDX_PANE_MEDIA);

		// 6. Tab unten: Parameter
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.parameter"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.parameter"), IDX_PANE_PARAMETER);
		// 6. Tab unten: Parameter
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.arbeitsplatzparameter"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.arbeitsplatzparameter"),
				IDX_PANE_ARBEITSPLATZPARAMETER);
		// 7. Tab unten: Versandauftraege
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.versandauftrag"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.versandauftrag"),
				IDX_PANE_VERSANDAUFTRAG);
		// 8. Tab unten: Nachrichtenarchiv
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"ben.nachrichtenarchiv"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("ben.nachrichtenarchiv"), IDX_PANE_NACHRICHTENARCHIV);
		// 8. Tab unten: Extraliste
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"system.extraliste"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("system.extraliste"), IDX_PANE_EXTRALISTE);

		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"system.automatik"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("system.automatik"), IDX_PANE_AUTOMATIK);

		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
				"lp.dokumente"), null, null, LPMain.getInstance()
				.getTextRespectUISPr("lp.dokumente"), IDX_PANE_DOKUMENTE);
		tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
		"lp.pflege"), null, null, LPMain.getInstance()
		.getTextRespectUISPr("lp.pflege"), IDX_PANE_PFLEGE);

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(
				RechteFac.RECHT_LP_EIGENSCHAFTSPANELS_BEARBEITEN)) {
			// 10. Tab unten: Eigenschaftsdefinition
			tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr(
					"lp.eigenschaftsdefinition"), null, null, LPMain
					.getInstance().getTextRespectUISPr(
							"lp.eigenschaftsdefinition"),
					IDX_PANE_EIGENSCHAFTENDEFINITION);
		}

		refreshTPSystem();
		tpSystem.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpSystem);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();
		// das icon setzen.
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/toolbox16x16.png"));
		setFrameIcon(iicon);
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();
		setRechtModulweit(sRechtModulweit);
		if (selectedCur == IDX_TABBED_PANE_RECHT) {
			refreshTPRechte();
			tabbedPaneRoot.setSelectedComponent(tpRechte);
			// Info an Tabbedpane, bist selektiert worden.
			tpRechte.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_TABBED_PANE_SYSTEM) {
			refreshTPSystem();
			tabbedPaneRoot.setSelectedComponent(tpSystem);
			// Info an Tabbedpane, bist selektiert worden.
			tpSystem.lPEventObjectChanged(null);
			// tpSystem.changed(new ItemChangedEvent(this,
			// ItemChangedEvent.ACTION_YOU_ARE_SELECTED));
		}

		else if (selectedCur == IDX_PANE_MANDANT) {
			refreshTPMandant();
			tabbedPaneRoot.setSelectedComponent(tpMandant);
			tpMandant.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_PANE_MEDIA) {
			refreshTPMedia();
			tabbedPaneRoot.setSelectedComponent(tpMedia);
			tpMedia.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_PANE_LOCALE) {
			refreshTPLocale();
			tabbedPaneRoot.setSelectedComponent(tpLocale);
			tpLocale.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_PANE_VERSANDAUFTRAG) {
			//PJ 14300: In Versandauftrag darf man seine eigenen immer bearbeiten
			setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			
			refreshTPVersandauftrag();
			tabbedPaneRoot.setSelectedComponent(tpVersandauftrag);
			tpVersandauftrag.lPEventObjectChanged(null);
		}

		else if (selectedCur == IDX_PANE_PARAMETER) {
			refreshTPParameter();
			tabbedPaneRoot.setSelectedComponent(tpParameter);
			tpParameter.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_ARBEITSPLATZPARAMETER) {
			refreshTPArbeitsplatzparameter();
			tabbedPaneRoot.setSelectedComponent(tpArbeitsplatzparameter);
			tpArbeitsplatzparameter.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_EIGENSCHAFTENDEFINITION) {
			refreshTPPanel();
			tabbedPaneRoot.setSelectedComponent(tpPanel);
			tpPanel.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_EXTRALISTE) {
			refreshTPExtraliste();
			tabbedPaneRoot.setSelectedComponent(tpExtraliste);
			tpExtraliste.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_NACHRICHTENARCHIV) {
			setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			refreshTPNachrichtenarchiv();
			tabbedPaneRoot.setSelectedComponent(tpNachrichtenarchiv);
			tpNachrichtenarchiv.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_AUTOMATIK) {
			refreshTPAutomatik();
			tabbedPaneRoot.setSelectedComponent(tpAutomatik);
			tpAutomatik.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_PFLEGE) {
			refreshTPPflege();
			tabbedPaneRoot.setSelectedComponent(tpPflege);
			tpPflege.lPEventObjectChanged(null);
		} else if (selectedCur == IDX_PANE_DOKUMENTE) {
			refreshTPDokumente();
			tabbedPaneRoot.setSelectedComponent(tpDokumente);
			tpDokumente.lPEventObjectChanged(null);
		}
	}

	private void refreshTPRechte() throws Throwable {

		if (tpRechte == null) {
			// lazy loading
			tpRechte = new TabbedPaneGesperrt(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_RECHT, tpRechte);
			initComponents();
		}
	}

	private void refreshTPSystem() throws Throwable {
		if (tpSystem == null) {
			// lazy loading
			tpSystem = new TabbedPaneSystem(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_SYSTEM, tpSystem);
			initComponents();
		}
	}

	private void refreshTPMedia() throws Throwable {

		if (tpMedia == null) {
			// lazy loading
			tpMedia = new TabbedPaneMediastandard(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_MEDIA, tpMedia);
			initComponents();
		}
	}

	private void refreshTPParameter() throws Throwable {

		if (tpParameter == null) {
			// lazy loading
			tpParameter = new TabbedPaneParameter(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_PARAMETER, tpParameter);
			initComponents();
		}
	}

	private void refreshTPArbeitsplatzparameter() throws Throwable {

		if (tpArbeitsplatzparameter == null) {
			// lazy loading
			tpArbeitsplatzparameter = new TabbedPaneArbeitsplatzparameter(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_ARBEITSPLATZPARAMETER,
					tpArbeitsplatzparameter);
			initComponents();
		}
	}

	protected void lPEventItemChanged(ItemChangedEvent eI) {
		// nothing here.
	}

	private void refreshTPMandant() throws Throwable {
		if (tpMandant == null) {
			tpMandant = new TabbedPaneMandant(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_MANDANT, tpMandant);
			initComponents();
		}
	}

	private void refreshTPLocale() throws Throwable {
		if (tpLocale == null) {
			tpLocale = new TabbedPaneLocale(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_LOCALE, tpLocale);
			initComponents();
		}
	}

	private void refreshTPVersandauftrag() throws Throwable {
		if (tpVersandauftrag == null) {
			tpVersandauftrag = new TabbedPaneVersandauftrag(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_VERSANDAUFTRAG,
					tpVersandauftrag);
			initComponents();
		}
	}

	private void refreshTPPanel() throws Throwable {
		if (tpPanel == null) {
			tpPanel = new TabbedPanePanel(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_EIGENSCHAFTENDEFINITION,
					tpPanel);
			initComponents();
		}
	}

	private void refreshTPExtraliste() throws Throwable {
		if (tpExtraliste == null) {
			tpExtraliste = new TabbedPaneExtraliste(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_EXTRALISTE, tpExtraliste);
			initComponents();
		}
	}

	private void refreshTPNachrichtenarchiv() throws Throwable {
		if (tpNachrichtenarchiv == null) {
			tpNachrichtenarchiv = new TabbedPaneNachrichtarchiv(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_NACHRICHTENARCHIV, tpNachrichtenarchiv);
			initComponents();
		}
	}

	private void refreshTPAutomatik() throws Throwable {
		if (tpAutomatik == null) {
			tpAutomatik = new TabbedPaneAutomatik(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_AUTOMATIK, tpAutomatik);
			initComponents();
		}
	}
	private void refreshTPPflege() throws Throwable {
		if (tpPflege == null) {
			tpPflege = new TabbedPanePflege(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_PFLEGE, tpPflege);
			initComponents();
		}
	}

	private void refreshTPDokumente() throws Throwable {
		if (tpDokumente == null) {
			tpDokumente = new TabbedPaneDokumente(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_DOKUMENTE, tpDokumente);
			initComponents();
		}
	}

	public MandantDto getMandantDto() {
		return mandantDto;
	}

	public void setMandantDto(MandantDto mandantDto) {
		this.mandantDto = mandantDto;
	}

	public EinheitDto getEinheitDto() {
		return einheitDto;
	}

	public void setEinheitDto(EinheitDto einheitDto) {
		this.einheitDto = einheitDto;
	}

	public void setEinheitKonvertierungDto(
			EinheitKonvertierungDto einheitKonvertierungDto) {
		this.einheitKonvertierungDto = einheitKonvertierungDto;
	}

	public EinheitKonvertierungDto getEinheitKonvertierungDto() {
		return einheitKonvertierungDto;
	}

	// stdcrud: 3 dtoZugriffsKlasse -> setDto
	public void setBelegartDto(BelegartDto belegartDto) {
		this.belegartDto = belegartDto;
	}

	public void setLieferartDto(LieferartDto lieferartDto) {
		this.lieferart2Dto = lieferartDto;
	}

	public void setStatusDto(StatusDto statusDto) {
		this.statusDto = statusDto;
	}

	// stdcrud: 4 dtoZugriffsKlasse -> getDto
	public BelegartDto getBelegartDto() {
		return belegartDto;
	}

	public LieferartDto getLieferartDto() {
		return lieferart2Dto;
	}

	public StatusDto getStatusDto() {
		return statusDto;
	}

	public BestellungtextDto getBestellungtextDto() {
		return bestellungtextDto;
	}

	public FunktionDto getFunktionDto() {
		return funktionDto;
	}

	public PositionsartDto getPositionsartDto() {
		return positionsartDto;
	}

	public SpediteurDto getSpediteurDto() {
		return spediteurDto;
	}

	public ZahlungszielDto getZahlungszielDto() {
		return zahlungszielDto;
	}

	public MediaartDto getMediaartDto() {
		return mediaartDto;
	}

	public MwstsatzDto getMwstsatzDto() {
		return mwstsatzDto;
	}

	public void setBestellungtextDto(BestellungtextDto bestellungtextDtoI) {
		this.bestellungtextDto = bestellungtextDtoI;
	}

	public void setFunktionDto(FunktionDto funktionDto) {
		this.funktionDto = funktionDto;
	}

	public void setPositionsartDto(PositionsartDto positionsartDto) {
		this.positionsartDto = positionsartDto;
	}

	public void setSpediteurDto(SpediteurDto spediteurDto) {
		this.spediteurDto = spediteurDto;
	}

	public void setZahlungszielDto(ZahlungszielDto zahlungszielDto) {
		this.zahlungszielDto = zahlungszielDto;
	}

	public void setMwstsatzDto(MwstsatzDto mwstsatzDto) {
		this.mwstsatzDto = mwstsatzDto;
	}

	public void setMediaartDto(MediaartDto mediaartDto) {
		this.mediaartDto = mediaartDto;
	}

	public KostenstelleDto getKostenstelleDto() {
		return kostenstelleDto;
	}

	public PanelDto getPanelDto() {
		return panelDto;
	}

	public MwstsatzbezDto getMwstsatzbezDto() {
		return mwstsatzbezDto;
	}

	public ArbeitsplatzDto getArbeitsplatzDto() {
		return arbeitsplatzDto;
	}

	public void setKostenstelleDto(KostenstelleDto kostenstelleDto) {
		this.kostenstelleDto = kostenstelleDto;
	}

	public void setPanelDto(PanelDto panelDto) {
		this.panelDto = panelDto;
	}

	public void setMwstsatzbezDto(MwstsatzbezDto mwstsatzbezDto) {
		this.mwstsatzbezDto = mwstsatzbezDto;
	}

	public void setArbeitsplatzDto(ArbeitsplatzDto arbeitsplatzDto) {
		this.arbeitsplatzDto = arbeitsplatzDto;
	}

	public TabbedPaneParameter getTabbedPaneParameter() {
		return tpParameter;
	}

	public TabbedPaneNachrichtarchiv getTabbedPaneNachrichtarchiv() {
		return tpNachrichtenarchiv;
	}

	public int execute(Command commandI) throws Throwable {

		int iRetCmd = ICommand.COMMAND_DONE;

		if (commandI instanceof CommandGoto) {
			CommandGoto gotoCommand = (CommandGoto) commandI;

			if (gotoCommand.getsInternalFrame() == Command.S_INTERNALFRAME_SYSTEM) {
				if (gotoCommand.getTabbedPaneAsClass() == Command.CLASS_TABBED_PANE_SYSTEM) {
					if (gotoCommand.getsPanel().equals(
							Command.PANEL_LAND_PLZ_ORT)) {
						tpSystem
								.setSelectedIndex(TabbedPaneSystem.IDX_PANEL_LANDPLZORT);
						if (gotoCommand.getSAction().equals(
								PanelBasis.ACTION_UPDATE)) {
							tpSystem.getPanelLandPlzOrtBottomD3()
									.eventActionNew(null, true, false);
							tpSystem.getPanelLandPlzOrtBottomD3()
									.eventYouAreSelected(false);
						}
					}
				}
			}
		} else {
			iRetCmd = ICommand.COMMAND_NOT_DONE;
		}

		return iRetCmd;

	}
}
