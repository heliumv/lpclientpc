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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

import javax.persistence.NoResultException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoBestellvorschlagDto;
import com.lp.server.system.service.AutoFehlmengendruckDto;
import com.lp.server.system.service.AutoMahnenDto;
import com.lp.server.system.service.AutoMahnungsversandDto;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckDto;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatiktimerFac;

@SuppressWarnings("static-access")
public class PanelAutomatikDetails extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private String sPropertyType = null;

	private WrapperLabel wlaName = null;
	private WrapperTextField wtfName = null;
	private WrapperCheckBox wcbActive = null;
	private WrapperCheckBox wcbNonWorkingDay = null;
	private WrapperCheckBox wcbMonthJob = null;
	private WrapperLabel wlaIntervall = null;
	private WrapperTextField wtfIntervall = null;
	private WrapperLabel wlaBeschreibung = null;
	private WrapperTextField wtfBeschreibung = null;

	private WrapperLabel wlaAutoFehlmengen = null;
	private WrapperComboBox wcbAutoFehlmengen = null;

	private WrapperLabel wlaAutoBestDrucker = null;
	private WrapperComboBox wcbAutoBestDrucker = null;
	
	private WrapperLabel wlaAutoRahmendetrailbedarfDrucker = null;
	private WrapperLabel wlaAutoRahmendetailbedarfSortierung = null;
	private WrapperComboBox wcbAutoRahmendetrailbedarfDrucker = null;
	private WrapperRadioButton wrbAutoRahmendetailbedarfdruckSortArtikel = null;
	private WrapperRadioButton wrbAutoRahmendetailbedarfdruckSortLieferant = null;
	private ButtonGroup bgAutoRahmendetailbedarfdruckSort = null;

	private WrapperLabel wlaAutoMahnDrucker = null;
	private WrapperComboBox wcbAutoMahnDrucker = null;
	private WrapperRadioButton wrbAutoMahnVersandKein = null;
	private WrapperRadioButton wrbAutoMahnVersandMail = null;
	private WrapperRadioButton wrbAutoMahnVersandFax = null;
	private ButtonGroup bgMahnVersandAuswahl = null;
	
	private WrapperRadioButton wrbAutoMahnAB = null;
	private WrapperRadioButton wrbAutoMahnLiefer = null;
	private WrapperRadioButton wrbAutoMahnABundLiefer = null;
	private ButtonGroup bgAutoMahnArt = null;

	private static final String VERSANDART_E_MAIL = "EMAIL";
	private static final String VERSANDART_FAX = "FAX";
	private static final String VERSANDART_KEIN_VERSAND = "KEIN";
	private static final String NICHT_DRUCKEN = "Nicht Drucken";

	TabbedPaneAutomatik tpAutomatik = null;

	private AutomatikjobDto automatikjobDto = null;
	private AutoBestellvorschlagDto autoBestellvorschlagDto = null;
	private AutoMahnungsversandDto autoMahnungsversandDto = null;
	private AutoFehlmengendruckDto autoFehlmengendruckDto = null;
	private AutoMahnenDto autoMahnenDto = null;
	private AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto = null;

	
	
	
	public PanelAutomatikDetails(InternalFrame internalFrameI, String addTitleI)
			throws Throwable {
		super(internalFrameI, addTitleI);
	}

	public PanelAutomatikDetails(InternalFrame internalFrameI,
			String addTitleI, Object keyWhenDetailPanelI,
			TabbedPaneAutomatik tabbedPaneAutomatik, String sPropertyType)
			throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
		this.tpAutomatik = tabbedPaneAutomatik;
		this.sPropertyType = sPropertyType;
		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaName = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.system.automatik.details.name"));
		wtfName = new WrapperTextField();
		wtfName.setEditable(false);
		wtfName.setActivatable(false);
		wcbActive = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.system.automatik.details.aktiv"));
		wcbNonWorkingDay = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr(
						"lp.system.automatik.details.vorfeiertagenausfuehren"));
		wcbMonthJob = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr(
						"lp.system.automatik.details.monatsendeausfuehren"));
		wlaIntervall = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr(
						"lp.system.automatik.details.intervalltage"));
		wtfIntervall = new WrapperTextField();
		wlaBeschreibung = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr(
						"lp.system.automatik.details.beschreibung"));
		wtfBeschreibung = new WrapperTextField();
		wtfBeschreibung.setEditable(false);
		wtfBeschreibung.setActivatable(false);
		wtfBeschreibung.setColumnsMax(1000);
		jPanelWorkingOn.add(wlaName, new GridBagConstraints(0, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfName, new GridBagConstraints(2, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaIntervall, new GridBagConstraints(4, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfIntervall, new GridBagConstraints(6, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBeschreibung, new GridBagConstraints(0, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBeschreibung, new GridBagConstraints(2, iZeile,
				6, 1, 2.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wcbActive, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbNonWorkingDay, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbMonthJob, new GridBagConstraints(6, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		// Add jobtype specific properties
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_PATERNOSTERABFRAGE_TYPE)) {
			wlaIntervall.setText(LPMain.getInstance().getTextRespectUISPr(
						"lp.system.automatik.details.intervallminuten"));
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
			wlaAutoFehlmengen = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.drucker"));
			wcbAutoFehlmengen = new WrapperComboBox();
			jPanelWorkingOn.add(wlaAutoFehlmengen,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wcbAutoFehlmengen,
					new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		}

		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
			wlaAutoMahnDrucker = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("lp.system.automatik.details.drucker"));
			wcbAutoMahnDrucker = new WrapperComboBox();
			wrbAutoMahnVersandKein = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr(
							"lp.system.automatik.details.nichtsenden"));
			wrbAutoMahnVersandMail = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr(
							"lp.system.automatik.details.emailsenden"));
			wrbAutoMahnVersandFax = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr(
							"lp.system.automatik.details.faxsenden"));
			bgMahnVersandAuswahl = new ButtonGroup();
			bgMahnVersandAuswahl.add(wrbAutoMahnVersandKein);
			bgMahnVersandAuswahl.add(wrbAutoMahnVersandMail);
			bgMahnVersandAuswahl.add(wrbAutoMahnVersandFax);
			jPanelWorkingOn.add(wlaAutoMahnDrucker,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wcbAutoMahnDrucker,
					new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			iZeile++;
			jPanelWorkingOn.add(wrbAutoMahnVersandKein,
					new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wrbAutoMahnVersandMail,
					new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wrbAutoMahnVersandFax,
					new GridBagConstraints(5, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {
			wlaAutoBestDrucker = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.drucker"));
			wcbAutoBestDrucker = new WrapperComboBox();
			jPanelWorkingOn.add(wlaAutoBestDrucker,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wcbAutoBestDrucker,
					new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
			wlaAutoRahmendetrailbedarfDrucker = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("label.drucker"));
			wlaAutoRahmendetailbedarfSortierung = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.sortierung"));
			wcbAutoRahmendetrailbedarfDrucker = new WrapperComboBox();
			wrbAutoRahmendetailbedarfdruckSortArtikel = new WrapperRadioButton(LPMain.getInstance().getTextRespectUISPr(
	        "artikel.artikelnummer"));
			wrbAutoRahmendetailbedarfdruckSortLieferant = new WrapperRadioButton(LPMain.getInstance().getTextRespectUISPr(
	        "label.lieferant"));
			bgAutoRahmendetailbedarfdruckSort = new ButtonGroup();
			bgAutoRahmendetailbedarfdruckSort.add(wrbAutoRahmendetailbedarfdruckSortArtikel);
			bgAutoRahmendetailbedarfdruckSort.add(wrbAutoRahmendetailbedarfdruckSortLieferant);
			jPanelWorkingOn.add(wlaAutoRahmendetrailbedarfDrucker,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wcbAutoRahmendetrailbedarfDrucker,
					new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			iZeile++;
			jPanelWorkingOn.add(wlaAutoRahmendetailbedarfSortierung,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			iZeile++;
			jPanelWorkingOn.add(wrbAutoRahmendetailbedarfdruckSortArtikel,
					new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wrbAutoRahmendetailbedarfdruckSortLieferant,
					new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
		
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE)){
			wrbAutoMahnAB = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("bes.mahnwesen.radiobutton2"));
			wrbAutoMahnLiefer = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("bes.mahnwesen.radiobutton1"));
			wrbAutoMahnABundLiefer = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("bes.mahnwesen.radiobutton3"));
			bgAutoMahnArt = new ButtonGroup();
			bgAutoMahnArt.add(wrbAutoMahnLiefer);
			bgAutoMahnArt.add(wrbAutoMahnAB);
			bgAutoMahnArt.add(wrbAutoMahnABundLiefer);
			jPanelWorkingOn.add(wrbAutoMahnLiefer,
					new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wrbAutoMahnAB,
					new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelWorkingOn.add(wrbAutoMahnABundLiefer,
					new GridBagConstraints(5, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUTOMATIK;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			dto2Components();
			if (automatikjobDto != null) {
				automatikjobDto.setIId(null);
			}
			wtfName.setText("");
			wtfIntervall.setText("");
		} else {
			String sMandant = LPMain.getInstance().getTheClient().getMandant();
			automatikjobDto = DelegateFactory.getInstance()
					.getAutomatikDelegate().automatikjobFindByPrimaryKey(
							(Integer) key);
			if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
				try {
					autoFehlmengendruckDto = DelegateFactory.getInstance()
							.getAutoFehlmengenDruckDelegate()
							.autoFehlmengendruckFindByMandantCNr(sMandant);
				} catch (Exception ex) {
					if(ex.getCause() instanceof NoResultException){
						autoFehlmengendruckDto = new AutoFehlmengendruckDto();
						autoFehlmengendruckDto.setMandantCNr(sMandant);
						DelegateFactory.getInstance()
								.getAutoFehlmengenDruckDelegate()
								.createAutoFehlmengendruck(autoFehlmengendruckDto);
					} else {
						throw ex;
					}
					// Fuer diesen Mandanten noch keine properties vorhanden
					
				}
			}
			if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {

				try {
					autoBestellvorschlagDto = DelegateFactory.getInstance()
							.getautoBestellvorschlagDelegate()
							.autoBestellvorschlagFindByMandantCNr(sMandant);
				} catch (Exception ex) {
					if(ex.getCause() instanceof NoResultException){
					// Fuer diesen Mandanten noch keine properties vorhanden
					autoBestellvorschlagDto = new AutoBestellvorschlagDto();
					autoBestellvorschlagDto.setMandantCNr(sMandant);
					DelegateFactory
							.getInstance()
							.getautoBestellvorschlagDelegate()
							.createAutoBestellvorschlag(autoBestellvorschlagDto);
					} else {
						throw ex;
					}
				}
			}
			if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
				try{
					autoRahmendetailbedarfdruckDto = DelegateFactory.getInstance()
					.getautoRahmendetailbedarfdruckDelegate().autoAutoRahmendetailbedarfdruckFindByMandantCNr(sMandant);
				} catch (Exception e){
					if(e.getCause() instanceof NoResultException){
						autoRahmendetailbedarfdruckDto = new AutoRahmendetailbedarfdruckDto();
						autoRahmendetailbedarfdruckDto.setMandantCNr(sMandant);
						DelegateFactory.getInstance().getautoRahmendetailbedarfdruckDelegate().createAutoRahmendetailbedarfdruck(autoRahmendetailbedarfdruckDto);
					} else {
						throw e;
					}
				}
			}
			
			
			if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
				try {
					autoMahnungsversandDto = DelegateFactory.getInstance()
							.getAutoMahnungsversandDelegate()
							.autoMahnungsversandFindByMandantCNr(sMandant);
				} catch (Exception ex) {
					if(ex.getCause() instanceof NoResultException){
					// Fuer diesen Mandanten noch keine properties vorhanden
					autoMahnungsversandDto = new AutoMahnungsversandDto();
					autoMahnungsversandDto.setMandantCNr(sMandant);
					DelegateFactory.getInstance()
							.getAutoMahnungsversandDelegate()
							.createAutoMahnungsversand(autoMahnungsversandDto);
					} else {
						throw ex;
					}
				}
			}
			if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE)){
				try{
					autoMahnenDto = DelegateFactory.getInstance().getAutoMahnenDelegate().autoMahnenFindByMandantCNr(sMandant);
				} catch (Exception ex){
					if(ex.getCause() instanceof NoResultException){
						autoMahnenDto = new AutoMahnenDto();
						autoMahnenDto.setMandantCNr(sMandant);
						DelegateFactory.getInstance().getAutoMahnenDelegate().createAutoMahnen(autoMahnenDto);
					} else {
						throw ex;
					}
				}
			}
			dto2Components();
		}
	}

	protected void dto2Components() throws ExceptionLP, RemoteException {
		wtfName.setText(automatikjobDto.getCName());
		wtfIntervall.setText(automatikjobDto.getIIntervall().toString());
		wtfBeschreibung.setText(automatikjobDto.getCBeschreibung());
		if (automatikjobDto.getBActive() == 1) {
			wcbActive.setSelected(true);
		} else {
			wcbActive.setSelected(false);
		}
		if (automatikjobDto.getBMonthjob() == 1) {
			wcbMonthJob.setSelected(true);
		} else {
			wcbMonthJob.setSelected(false);
		}
		if (automatikjobDto.getBPerformOnNonWOrkingDays() == 1) {
			wcbNonWorkingDay.setSelected(true);
		} else {
			wcbNonWorkingDay.setSelected(false);
		}
		// Fill specific Components
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
			String[] printers = DelegateFactory.getInstance()
					.getServerDruckerDelegate().getServerDrucker();
			wcbAutoMahnDrucker.removeAllItems();
			for (int i = 0; i < printers.length; i++) {
				wcbAutoMahnDrucker.addItem(printers[i]);
			}
			wcbAutoMahnDrucker.addItem(NICHT_DRUCKEN);
			if (autoMahnungsversandDto.getCDrucker() == null) {
				wcbAutoMahnDrucker.setSelectedItem(DelegateFactory
						.getInstance().getServerDruckerDelegate()
						.getServerStandarddrucker());
			} else {
				wcbAutoMahnDrucker.setSelectedItem(autoMahnungsversandDto
						.getCDrucker());
			}
			String sVersandart = autoMahnungsversandDto.getCVersandart();
			if (sVersandart == null) {
				wrbAutoMahnVersandMail.setSelected(false);
				wrbAutoMahnVersandFax.setSelected(false);
				wrbAutoMahnVersandKein.setSelected(false);
			} else {
				if (sVersandart.equals(VERSANDART_KEIN_VERSAND)) {
					wrbAutoMahnVersandKein.setSelected(true);
				}
				if (sVersandart.equals(VERSANDART_E_MAIL)) {
					wrbAutoMahnVersandMail.setSelected(true);
				}
				if (sVersandart.equals(VERSANDART_FAX)) {
					wrbAutoMahnVersandFax.setSelected(true);
				}
			}
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {
			String[] printers = DelegateFactory.getInstance()
					.getServerDruckerDelegate().getServerDrucker();
			wcbAutoBestDrucker.removeAllItems();
			for (int i = 0; i < printers.length; i++) {
				wcbAutoBestDrucker.addItem(printers[i]);
			}
			wcbAutoBestDrucker.addItem(NICHT_DRUCKEN);
			if (autoBestellvorschlagDto.getCDrucker() == null) {
				wcbAutoBestDrucker.setSelectedItem(DelegateFactory
						.getInstance().getServerDruckerDelegate()
						.getServerStandarddrucker());
			} else {
				wcbAutoBestDrucker.setSelectedItem(autoBestellvorschlagDto
						.getCDrucker());
			}
		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
			String[] printers = DelegateFactory.getInstance().getServerDruckerDelegate().getServerDrucker();
			wcbAutoRahmendetrailbedarfDrucker.removeAllItems();
			for(int i=0;i<printers.length;i++){
				wcbAutoRahmendetrailbedarfDrucker.addItem(printers[i]);
			}
			wcbAutoRahmendetrailbedarfDrucker.addItem(NICHT_DRUCKEN);
			if(autoRahmendetailbedarfdruckDto.getCDrucker()==null){
				wcbAutoRahmendetrailbedarfDrucker.setSelectedItem(DelegateFactory.getInstance().getServerDruckerDelegate().getServerStandarddrucker());
			} else {
				wcbAutoRahmendetrailbedarfDrucker.setSelectedItem(autoRahmendetailbedarfdruckDto.getCDrucker());
			}
			if(autoRahmendetailbedarfdruckDto.getBSortiertnachArtikel()==null){
				autoRahmendetailbedarfdruckDto.setBSortiertnachArtikel(true);
			}
			if(autoRahmendetailbedarfdruckDto.getBSortiertnachArtikel()){
				wrbAutoRahmendetailbedarfdruckSortArtikel.setSelected(true);
			} else {
				wrbAutoRahmendetailbedarfdruckSortLieferant.setSelected(true);
			}
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
			String[] printers = DelegateFactory.getInstance()
					.getServerDruckerDelegate().getServerDrucker();
			wcbAutoFehlmengen.removeAllItems();
			for (int i = 0; i < printers.length; i++) {
				wcbAutoFehlmengen.addItem(printers[i]);
			}
			wcbAutoFehlmengen.addItem(NICHT_DRUCKEN);
			if (autoFehlmengendruckDto.getCDrucker() == null) {
				wcbAutoFehlmengen.setSelectedItem(DelegateFactory.getInstance()
						.getServerDruckerDelegate().getServerStandarddrucker());
			} else {
				wcbAutoFehlmengen.setSelectedItem(autoFehlmengendruckDto
						.getCDrucker());
			}

		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE)){
			if(autoMahnenDto.getBAbMahnen()==null){
				autoMahnenDto.setBAbMahnen(false);
			}
			if(autoMahnenDto.getBLieferMahnen()==null){
				autoMahnenDto.setBLieferMahnen(false);
			}
			if(autoMahnenDto.getBAbMahnen()){
				if(autoMahnenDto.getBLieferMahnen()){
					wrbAutoMahnABundLiefer.setSelected(true);
				} else {
					wrbAutoMahnAB.setSelected(true);
				}
			} else {
				if(autoMahnenDto.getBLieferMahnen()){
					wrbAutoMahnLiefer.setSelected(true);
				}
			}
		}

	}

	protected void components2Dto() {
		automatikjobDto.setIIntervall(Integer.parseInt(wtfIntervall.getText()));
		if (wcbActive.isSelected()) {
			automatikjobDto.setBActive(1);
		} else {
			automatikjobDto.setBActive(0);
		}
		if (wcbMonthJob.isSelected()) {
			automatikjobDto.setBMonthjob(1);
		} else {
			automatikjobDto.setBMonthjob(0);
		}
		if (wcbNonWorkingDay.isSelected()) {
			automatikjobDto.setBPerformOnNonWOrkingDays(1);
		} else {
			automatikjobDto.setBPerformOnNonWOrkingDays(0);
		}
		// Write specific Components
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
			autoMahnungsversandDto.setCDrucker((String) wcbAutoMahnDrucker
					.getSelectedItem());
			if (wrbAutoMahnVersandKein.isSelected()) {
				autoMahnungsversandDto.setCVersandart(VERSANDART_KEIN_VERSAND);
			}
			if (wrbAutoMahnVersandMail.isSelected()) {
				autoMahnungsversandDto.setCVersandart(VERSANDART_E_MAIL);
			}
			if (wrbAutoMahnVersandFax.isSelected()) {
				autoMahnungsversandDto.setCVersandart(VERSANDART_FAX);
			}
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {
			autoBestellvorschlagDto.setCDrucker((String) wcbAutoBestDrucker
					.getSelectedItem());
		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
			autoRahmendetailbedarfdruckDto.setCDrucker((String) wcbAutoRahmendetrailbedarfDrucker.getSelectedItem());
			if(wrbAutoRahmendetailbedarfdruckSortArtikel.isSelected()){
				autoRahmendetailbedarfdruckDto.setBSortiertnachArtikel(true);
			} else {
				autoRahmendetailbedarfdruckDto.setBSortiertnachArtikel(false);
			}
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
			autoFehlmengendruckDto.setCDrucker((String) wcbAutoFehlmengen
					.getSelectedItem());
		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE)){
			if(wrbAutoMahnAB.isSelected()){
				autoMahnenDto.setBAbMahnen(true);
				autoMahnenDto.setBLieferMahnen(false);
			}
			else if(wrbAutoMahnABundLiefer.isSelected()){
				autoMahnenDto.setBAbMahnen(true);
				autoMahnenDto.setBLieferMahnen(true);
			}
			else if(wrbAutoMahnLiefer.isSelected()){
				autoMahnenDto.setBAbMahnen(false);
				autoMahnenDto.setBLieferMahnen(true);
			}
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		components2Dto();
		DelegateFactory.getInstance().getAutomatikDelegate()
				.updateAutomatikjob(automatikjobDto);
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
			DelegateFactory.getInstance().getAutoFehlmengenDruckDelegate()
					.updateAutoFehlmengendruck(autoFehlmengendruckDto);
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
			DelegateFactory.getInstance().getAutoMahnungsversandDelegate()
					.updateAutoMahnungsversand(autoMahnungsversandDto);
		}
		if (sPropertyType.equals(AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {
			DelegateFactory.getInstance().getautoBestellvorschlagDelegate()
					.updateAutoBestellvorschlag(autoBestellvorschlagDto);
		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
			DelegateFactory.getInstance().getautoRahmendetailbedarfdruckDelegate()
					.updateAutoRahmendetailbedarfdruck(autoRahmendetailbedarfdruckDto);
		}
		if(sPropertyType.equals(AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE)){
			DelegateFactory.getInstance().getAutoMahnenDelegate()
					.updateAutoMahnen(autoMahnenDto);
		}

		super.eventActionSave(e, true);
		eventYouAreSelected(false);
	}

	public PanelAutomatikDetails() {
		super();
	}

}
