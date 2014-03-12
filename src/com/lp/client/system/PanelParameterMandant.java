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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.fastlanereader.generated.service.FLRParametermandantPK;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden Parameter fuer den Mandanten erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-09-29</p>
 * <p> </p>
 * @author uli walch
 * @version $Revision: 1.6 $
 */
public class PanelParameterMandant extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private InternalFrameSystem intFrame = null;

	private ParametermandantDto parametermandantDto = null;

	private WrapperLabel wlaBezeichnung = null;
	private WrapperLabel wlaKategorie = null;
	private WrapperLabel wlaWert = null;
	private WrapperLabel wlaBemerkungsmall = null;
	private WrapperLabel wlaBemerkunglarge = null;
	private WrapperLabel wlaDatentyp = null;

	private WrapperTextField wtfBezeichnung = null;
	private WrapperTextField wtfKategorie = null;
	private WrapperTextField wtfWert = null;
	private WrapperPasswordField wtfPassword = null;
	private WrapperTextField wtfBemerkungsmall = null;
	private WrapperEditorField wefBemerkunglarge = null;
	private WrapperTextField wtfDatentyp = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private int iZeileWert = -1;

	public PanelParameterMandant(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameSystem) internalFrame;

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bezeichnung"));
		HelperClient.setDefaultsToComponent(wlaBezeichnung, 90);

		wlaKategorie = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kategorie"));
		wlaWert = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.wert"));
		wlaBemerkungsmall = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkunglarge = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bemerkunglang"));
		wlaDatentyp = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.datentyp"));

		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setColumnsMax(ParameterFac.MAX_PARAMETER_KENNUNG);
		wtfBezeichnung.setActivatable(false);

		wtfKategorie = new WrapperTextField();
		wtfKategorie.setActivatable(false);

		wtfPassword = new WrapperPasswordField();
		wtfPassword.setMandatoryField(true);
		wtfPassword.setColumnsMax(ParameterFac.MAX_PARAMETER_WERT);

		wtfWert = new WrapperTextField();
		wtfWert.setMandatoryField(true);
		wtfWert.setColumnsMax(ParameterFac.MAX_PARAMETER_WERT);

		wtfBemerkungsmall = new WrapperTextField();
		wtfBemerkungsmall.setMandatoryField(true);
		wtfBemerkungsmall
				.setColumnsMax(ParameterFac.MAX_PARAMETER_BEMERKUNGSMALL);

		wefBemerkunglarge = new WrapperEditorField(intFrame, LPMain
				.getInstance().getTextRespectUISPr("lp.bemerkunglang"));
		// wefBermerkunglarge.setActionMap() auf 3000 beschraenken

		wtfDatentyp = new WrapperTextField();
		wtfDatentyp.setColumnsMax(ParameterFac.MAX_PARAMETER_DATENTYP);

		// Zeile
		jPanelWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile,
				1, 1, 0.05, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaKategorie, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKategorie, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaWert, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeileWert = iZeile;

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaBemerkungsmall, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBemerkungsmall, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaBemerkunglarge, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefBemerkunglarge, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaDatentyp, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfDatentyp, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			boolean bSpeichern = false;

			if (Helper.istKennwortParameter(parametermandantDto.getCNr())) {
				if (Helper.istWertVomTyp(new String(wtfPassword.getPassword()),
						wtfDatentyp.getText())) {
					bSpeichern = true;
				}
			} else {
				if (Helper.istWertVomTyp(wtfWert.getText(), wtfDatentyp.getText())) {
					bSpeichern=true;
				}
			}

			if (bSpeichern) {
				components2Dto();

				if (parametermandantDto.getCNr() == null) {
					ParametermandantPK pkParametermandant = DelegateFactory
							.getInstance().getParameterDelegate()
							.createParametermandant(parametermandantDto);
					parametermandantDto = DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.parametermandantFindByPrimaryKey(
									pkParametermandant);
					setKeyWhenDetailPanel(pkParametermandant);
				} else {
					DelegateFactory.getInstance().getParameterDelegate()
							.updateParametermandant(parametermandantDto);
				}

				// buttons schalten
				super.eventActionSave(e, true);

				eventYouAreSelected(false);
			} else {
				if (wtfDatentyp.getText() != null) {
					if (wtfDatentyp.getText().equals("java.lang.Boolean")) {
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.nurbooleanwerte"));
					} else if (wtfDatentyp.getText()
							.equals("java.lang.Integer")) {
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.nurganzzahligewerte"));
					} else if (wtfDatentyp.getText().equals("java.lang.Double")) {
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.nurzahlenewerte"));
					} else if (wtfDatentyp.getText().equals(
							"java.math.BigDecimal")) {
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.nurzahlenewerte"));
					} else {
						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.wertnichtunterstuetzt"));
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"system.wertnichtunterstuetzt"));
				}
			}
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		resetPanel();
	}

	private void resetPanel() throws Throwable {
		parametermandantDto = new ParametermandantDto();
		leereAlleFelder(this);
	}

	private void dto2Components() throws Throwable {
		wtfDatentyp.setText(parametermandantDto.getCDatentyp());
		wtfBezeichnung.setText(parametermandantDto.getCNr());
		wtfKategorie.setText(parametermandantDto.getCKategorie());
		
		wtfBemerkungsmall.setText(parametermandantDto.getCBemerkungsmall());
		wefBemerkunglarge.setText(parametermandantDto.getCBemerkunglarge());

		if (Helper.istKennwortParameter(parametermandantDto.getCNr())) {
			jPanelWorkingOn.remove(wtfWert);

			jPanelWorkingOn.add(wtfPassword, new GridBagConstraints(1,
					iZeileWert, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			wtfPassword.setText(parametermandantDto.getCWert());
		} else {
			jPanelWorkingOn.remove(wtfPassword);
			jPanelWorkingOn.add(wtfWert, new GridBagConstraints(1, iZeileWert,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			wtfWert.setText(parametermandantDto.getCWert());
		}
		jPanelWorkingOn.repaint();
	}

	
	private void components2Dto() throws Throwable {
		// es werden keine neuen Parameter angelegt

		if (Helper.istKennwortParameter(parametermandantDto.getCNr())) {
			parametermandantDto.setCWert(new String(wtfPassword.getPassword()));
		} else {
			parametermandantDto.setCWert(wtfWert.getText());
		}

		parametermandantDto.setCBemerkungsmall(wtfBemerkungsmall.getText());
		parametermandantDto.setCBemerkunglarge(wefBemerkunglarge.getText());
		parametermandantDto.setCDatentyp(wtfDatentyp.getText());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		// setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			ParametermandantPK pkParametermandant = new ParametermandantPK();
			pkParametermandant.setCNr(((FLRParametermandantPK) oKey).getC_nr());
			pkParametermandant.setCKategorie(((FLRParametermandantPK) oKey)
					.getC_kategorie());

			parametermandantDto = DelegateFactory.getInstance()
					.getParameterDelegate()
					.parametermandantFindByPrimaryKey(pkParametermandant);
			dto2Components();
		}

		getInternalFrame()
				.setLpTitle(
						InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
						LPMain.getInstance().getTextRespectUISPr(
								"lp.parametermandant"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				LPMain.getInstance().getTheClient().getMandant());

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		if (parametermandantDto != null && parametermandantDto.getCNr() != null) {
			setStatusbarPersonalIIdAendern(parametermandantDto
					.getPersonalIIdAendern());
			setStatusbarTAendern(parametermandantDto.getTAendern());
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARAMETERMANDANT;
	}
}
