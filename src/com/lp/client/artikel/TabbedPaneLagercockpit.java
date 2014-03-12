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

import java.math.BigDecimal;

import javax.swing.JMenu;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.3 $
 */
public class TabbedPaneLagercockpit extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryArtikel = null;

	public PanelQuery getPanelQueryArtikel() {
		return panelQueryArtikel;
	}

	private PanelQuery panelQueryFehlmengen = null;
	private PanelQuery panelQueryNichtLagerbewirtschafteteArtikel = null;

	private PanelQuery panelQueryLossollmaterial = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_FEHLMENGEN = 1;
	private final static int IDX_PANEL_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL = 2;
	private final static int IDX_PANEL_LOSSOLLMATERIAL = 3;

	private final String BUTTON_MATERIAL_UMBUCHEN = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_MATERIAL_UMBUCHEN;

	private final String BUTTON_MATERIAL_VERRAEUMEN = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_MATERIAL_VERRAEUMEN;

	private static final String ACTION_SPECIAL_MATERIAL_UMBUCHEN = "action_special_MATERIAL_UMBUCHEN";
	private static final String ACTION_SPECIAL_MATERIAL_VERRAEUMEN = "action_special_MATERIAL_VERRAEUMEN";

	private final String BUTTON_UMLAGERUNG = PanelBasis.ALWAYSENABLED
			+ ACTION_SPECIAL_UMLAGERUNG;
	private static final String ACTION_SPECIAL_UMLAGERUNG = "action_special_UMLAGERUNG";

	private final String MENUE_JOURNAL_WE_LAGER_VERTEILUNGSVORSCHLAG = "MENUE_JOURNAL_WE_LAGER_VERTEILUNGSVORSCHLAG";
	private final String MENUE_JOURNAL_MATERIAL_VERTEILUNGSVORSCHLAG = "MENUE_JOURNAL_MATERIAL_VERTEILUNGSVORSCHLAG";

	private ArtikelDto artikelDto = null;

	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	public TabbedPaneLagercockpit(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"ww.lagercockpit"));

		jbInit();
		initComponents();

		LagerDto lagerDtoWareneingang = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.lagerFindByMandantCNrLagerartCNrOhneExc(
						LPMain.getTheClient().getMandant(),
						LagerFac.LAGERART_WARENEINGANG);

		if (lagerDtoWareneingang == null) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.info"),
							LPMain.getInstance()
									.getTextRespectUISPr(
											"ww.lagercockpit.wareneingangslager.nichtvorhanden"));

		}

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryArtikel == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = null;

			QueryType[] qtAuswahl = ArtikelFilterFactory.getInstance()
					.createQTArtikelauswahl();

			boolean bBenutzerIstInMandantensprechAngemeldet = false;
			if (LPMain
					.getInstance()
					.getTheClient()
					.getLocMandantAsString()
					.equals(LPMain.getInstance().getTheClient()
							.getLocUiAsString())) {
				bBenutzerIstInMandantensprechAngemeldet = true;
			}

			if (bBenutzerIstInMandantensprechAngemeldet) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			}

			panelQueryArtikel = new PanelQuery(qtAuswahl, ArtikelFilterFactory
					.getInstance().createFKLagercockpit(),
					QueryParameters.UC_ID_LAGERCOCKPIT_ARTIKEL,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true,
					ArtikelFilterFactory.getInstance()
							.createFKVArtikelLagercockpit());

			panelQueryArtikel.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance()
							.createFKDArtikelnummerLagercockpit(
									getInternalFrame()), null);

			panelQueryArtikel.addStatusBar();

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryArtikel);
		}
	}

	private void createFehlmengen(Integer artikelIId) throws Throwable {
		if (panelQueryFehlmengen == null) {

			QueryType[] qtAuswahl = ArtikelFilterFactory.getInstance()
					.createQTArtikelauswahl();

			panelQueryFehlmengen = new PanelQuery(qtAuswahl,
					ArtikelFilterFactory.getInstance()
							.createFKLagercockpitArtikel(artikelIId),
					QueryParameters.UC_ID_LAGERCOCKPIT_FEHLMENGE, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryFehlmengen.addStatusBar();

			panelQueryFehlmengen.createAndSaveAndShowButton(
					"/com/lp/client/res/data_next.png",
					LPMain.getInstance().getTextRespectUISPr(
							"ww.lagercockpit.material.auflos"),
					BUTTON_MATERIAL_UMBUCHEN, KeyStroke.getKeyStroke('B',
							java.awt.event.InputEvent.CTRL_MASK), null);

			setComponentAt(IDX_PANEL_FEHLMENGEN, panelQueryFehlmengen);
		}
		panelQueryFehlmengen.setDefaultFilter(ArtikelFilterFactory
				.getInstance().createFKLagercockpitArtikel(artikelIId));

	}

	private void createNichtlagerbewirtschafteteArtikel() throws Throwable {
		if (panelQueryNichtLagerbewirtschafteteArtikel == null) {

			panelQueryNichtLagerbewirtschafteteArtikel = new PanelQuery(
					null,
					ArtikelFilterFactory
							.getInstance()
							.createFKLagercockpitNichtlagerbewirtschafteteArtikel(),
					QueryParameters.UC_ID_LAGERCOCKPIT_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL,
					null, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryNichtLagerbewirtschafteteArtikel.addStatusBar();

			panelQueryNichtLagerbewirtschafteteArtikel
					.createAndSaveAndShowButton(
							"/com/lp/client/res/data_next.png",
							LPMain.getInstance().getTextRespectUISPr(
									"ww.lagercockpit.verraeumen"),
							BUTTON_MATERIAL_VERRAEUMEN, KeyStroke.getKeyStroke(
									'B', java.awt.event.InputEvent.CTRL_MASK),
							null);

			setComponentAt(IDX_PANEL_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL,
					panelQueryNichtLagerbewirtschafteteArtikel);
		}
		panelQueryNichtLagerbewirtschafteteArtikel
				.setDefaultFilter(ArtikelFilterFactory.getInstance()
						.createFKLagercockpitNichtlagerbewirtschafteteArtikel());

	}

	private void createLossollmaterial() throws Throwable {

		if (panelQueryLossollmaterial == null) {
			panelQueryLossollmaterial = new PanelQuery(null, null,
					QueryParameters.UC_ID_LAGERCOCKPIT_LOSSOLLMATERIAL, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryLossollmaterial
					.befuelleFilterkriteriumSchnellansicht(ArtikelFilterFactory
							.getInstance().createFKLagercockpitSchnellansicht());

			panelQueryLossollmaterial
					.befuellePanelFilterkriterienDirekt(
							ArtikelFilterFactory
									.getInstance()
									.createFKDArtikelnummerLagercockpitMaterialumlagerung(
											getInternalFrame()), null);

			panelQueryLossollmaterial.getCbSchnellansicht().setText(
					LPMain.getInstance().getTextRespectUISPr(
							"ww.lagercockpit.schnellansicht"));

			panelQueryLossollmaterial.createAndSaveAndShowButton(
					"/com/lp/client/res/data_next.png",
					LPMain.getInstance().getTextRespectUISPr(
							"ww.lagercockpit.material.auflos"),
					BUTTON_UMLAGERUNG, KeyStroke.getKeyStroke('B',
							java.awt.event.InputEvent.CTRL_MASK), null);

			setComponentAt(IDX_PANEL_LOSSOLLMATERIAL, panelQueryLossollmaterial);
		}

		// PJ18216

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_NICHT_LAGERBEWIRTSCHAFTETE_SOFORT_AUSGEBEN);
		boolean bNichtLagerbewAusblenden = (java.lang.Boolean) parameter
				.getCWertAsObject();

		if (bNichtLagerbewAusblenden == true) {
			panelQueryLossollmaterial
					.setDefaultFilter(ArtikelFilterFactory
							.getInstance()
							.createFKLagercockpitNichtlagerbewirtschafteteArtikelAusblenden());
		}

	}

	private void jbInit() throws Throwable {

		addTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				panelQueryArtikel);
		createAuswahl();
		panelQueryArtikel.eventYouAreSelected(false);

		if ((Integer) panelQueryArtikel.getSelectedId() != null) {
			setArtikelDto(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							(Integer) panelQueryArtikel.getSelectedId()));
		}

		addTab(LPMain.getInstance().getTextRespectUISPr(
				"ww.lagercockpit.buchungen"), panelQueryFehlmengen);
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"ww.lagercockpit.nlbartikel"),
				panelQueryNichtLagerbewirtschafteteArtikel);
		addTab(LPMain.getInstance().getTextRespectUISPr(
				"ww.lagercockpit.materialumlagerung"),
				panelQueryLossollmaterial);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryArtikel) {
				setSelectedComponent(panelQueryFehlmengen);
			} else if (e.getSource() == panelQueryFehlmengen) {
				dialogFehlmengenUmbuchen();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {

		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameArtikel().setKeyWasForLockMe(
							panelQueryArtikel.getSelectedId() + "");

					setArtikelDto(DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) key));

					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"ww.lagercockpit")
									+ ": "
									+ getArtikelDto()
											.formatArtikelbezeichnung());
				}
			} else if (e.getSource() == panelQueryLossollmaterial) {

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {

		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQueryFehlmengen) {

				if (sAspectInfo.endsWith(ACTION_SPECIAL_MATERIAL_UMBUCHEN)) {
					dialogFehlmengenUmbuchen();

				}
			}
			if (e.getSource() == panelQueryLossollmaterial) {

				if (sAspectInfo.endsWith(ACTION_SPECIAL_UMLAGERUNG)) {
					dialogMaterialumlagerung();

				}
			}
			if (e.getSource() == panelQueryNichtLagerbewirtschafteteArtikel) {

				if (sAspectInfo.endsWith(ACTION_SPECIAL_MATERIAL_VERRAEUMEN)) {
					if (panelQueryNichtLagerbewirtschafteteArtikel
							.getSelectedId() != null) {
						DelegateFactory
								.getInstance()
								.getWareneingangDelegate()
								.wareneingangspositionAlsVerraeumtKennzeichnen(
										(Integer) panelQueryNichtLagerbewirtschafteteArtikel
												.getSelectedId());
						panelQueryNichtLagerbewirtschafteteArtikel
								.eventYouAreSelected(false);
					}

				}
			}
		}
	}

	private void dialogFehlmengenUmbuchen() throws Throwable {
		Integer fehlmengeIId = null;
		if ((Integer) panelQueryFehlmengen.getSelectedId() != -1) {
			fehlmengeIId = (Integer) panelQueryFehlmengen.getSelectedId();
		}
		DialogLagercockpitUmbuchen dialog = new DialogLagercockpitUmbuchen(
				getArtikelDto().getIId(), fehlmengeIId,
				getInternalFrameArtikel());
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(dialog);
		dialog.setVisible(true);
		panelQueryFehlmengen.eventYouAreSelected(false);
	}

	private void dialogMaterialumlagerung() throws Throwable {
		Integer sollmaterialIId = (Integer) panelQueryLossollmaterial
				.getSelectedId();
		if (sollmaterialIId != null) {
			DialogMaterialumlagerung dialog = new DialogMaterialumlagerung(
					sollmaterialIId, getInternalFrameArtikel());
			LPMain.getInstance().getDesktop()
					.platziereDialogInDerMitteDesFensters(dialog);
			dialog.setVisible(true);
			panelQueryLossollmaterial.eventYouAreSelected(false);
		}
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryLossollmaterial.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		if (getArtikelDto() != null) {
			getInternalFrame()
					.setLpTitle(
							InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
							LPMain.getInstance().getTextRespectUISPr(
									"ww.lagercockpit"));
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					((PanelBasis) this.getSelectedComponent()).getAdd2Title());
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getArtikelDto().formatArtikelbezeichnung());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();
		setEnabledAt(IDX_PANEL_LOSSOLLMATERIAL, true);
		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryArtikel.eventYouAreSelected(false);
			panelQueryArtikel.updateButtons();
		} else if (selectedIndex == IDX_PANEL_FEHLMENGEN) {
			if (panelQueryArtikel.getSelectedId() != null) {
				createFehlmengen(getArtikelDto().getIId());
				panelQueryFehlmengen.eventYouAreSelected(false);
				panelQueryFehlmengen.updateButtons();
			}
		} else if (selectedIndex == IDX_PANEL_LOSSOLLMATERIAL) {
			createLossollmaterial();
			panelQueryLossollmaterial.eventYouAreSelected(false);
			panelQueryLossollmaterial.updateButtons();
		} else if (selectedIndex == IDX_PANEL_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL) {
			createNichtlagerbewirtschafteteArtikel();
			panelQueryNichtLagerbewirtschafteteArtikel
					.eventYouAreSelected(false);
			panelQueryNichtLagerbewirtschafteteArtikel.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				MENUE_JOURNAL_WE_LAGER_VERTEILUNGSVORSCHLAG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"ww.lagercockpit.report.welagerverteilungsvorschlag");
			getInternalFrame().showReportKriterien(
					new ReportLagercockpitWELagerVerteilungsvorschlag(
							getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(
				MENUE_JOURNAL_MATERIAL_VERTEILUNGSVORSCHLAG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"ww.lagercockpit.report.materialverteilungsvorschlag");
			getInternalFrame().showReportKriterien(
					new ReportLagercockpitMaterialVerteilungsvorschlag(
							getInternalFrameArtikel(), add2Title));

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);

			WrapperMenuItem menuItemWELagerVerteilungsvorschlag = new WrapperMenuItem(
					LPMain.getInstance()
							.getTextRespectUISPr(
									"ww.lagercockpit.report.welagerverteilungsvorschlag"),
					null);
			menuItemWELagerVerteilungsvorschlag.addActionListener(this);
			menuItemWELagerVerteilungsvorschlag
					.setActionCommand(MENUE_JOURNAL_WE_LAGER_VERTEILUNGSVORSCHLAG);
			journal.add(menuItemWELagerVerteilungsvorschlag);

			WrapperMenuItem menuItemMaterialVerteilungsvorschlag = new WrapperMenuItem(
					LPMain.getInstance()
							.getTextRespectUISPr(
									"ww.lagercockpit.report.materialverteilungsvorschlag"),
					null);
			menuItemMaterialVerteilungsvorschlag.addActionListener(this);
			menuItemMaterialVerteilungsvorschlag
					.setActionCommand(MENUE_JOURNAL_MATERIAL_VERTEILUNGSVORSCHLAG);
			journal.add(menuItemMaterialVerteilungsvorschlag);

		}
		return wrapperMenuBar;
	}

}
