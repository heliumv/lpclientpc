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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperAttachmentViewer;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Waehrungen
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 22.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/11/12 09:36:57 $
 */
public class PanelVersandauftrag extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int I_ASPECT_POSTAUSGANG = 0;
	public final static int I_ASPECT_GESENDET = 1;
	public final static int I_ASPECT_PAPIERKORB = 2;
	public final static int I_ASPECT_FEHLGESCHLAGEN = 3;

	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private TabbedPaneVersandauftrag tpVersandauftrag = null;
	private VersandauftragDto versandauftragDto = null;
	private VersandanhangDto[] versandanhangDto = null;

	private WrapperLabel wlaEmpfaenger = null;
	private WrapperTextField wtfEmpfaenger = null;
	private WrapperLabel wlaCCEmpfaenger = null;
	private WrapperTextField wtfCCEmpfaenger = null;
	private WrapperLabel wlaBetreff = null;
	private WrapperTextField wtfBetreff = null;
	private WrapperLabel wlaAbsender = null;
	private WrapperTextField wtfAbsender = null;
	private WrapperLabel wlaSendezeitpunktwunsch = null;
	private WrapperTimestampField wtsfSendezeitpunktWunsch = null;
	private WrapperLabel wlaText = null;
	private WrapperEditorField wefText = null;
	private WrapperCheckBox wcbEmpfangsbestaetigung = null;
	private WrapperAttachmentViewer wrapperAttachmentViewer = null;
	private WrapperLabel wlaStatus = new WrapperLabel();

	private final static String ACTION_SPECIAL_ERNEUT_VERSENDEN = "action_special_erneut_versenden";
	private final static String ACTION_SPECIAL_SOFORT_VERSENDEN = "action_special_sofort_versenden";

	private int iAspect;

	public PanelVersandauftrag(InternalFrame internalFrame, String add2TitleI,
			Object pk, TabbedPaneVersandauftrag tpVersandauftrag, int iAspect)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tpVersandauftrag = tpVersandauftrag;
		this.iAspect = iAspect;
		jbInit();
		initComponents();
	}

	public VersandauftragDto getVersandauftragDto() {
		return versandauftragDto;
	}

	protected void dto2Components() throws FileNotFoundException, IOException {
		if (versandauftragDto != null) {
			try {
				versandauftragDto = DelegateFactory.getInstance()
						.getJCRDocDelegate()
						.getDataForVersandauftragFromJCR(versandauftragDto);
			} catch (Throwable e) {
			}
			wtfAbsender.setText(versandauftragDto.getCAbsenderadresse());
			wtfBetreff.setText(versandauftragDto.getCBetreff());
			wtfCCEmpfaenger.setText(versandauftragDto.getCCcempfaenger());
			wefText.setText(versandauftragDto.getCText());
			wtfEmpfaenger.setText(versandauftragDto.getCEmpfaenger());
			wtsfSendezeitpunktWunsch.setTimestamp(versandauftragDto
					.getTSendezeitpunktwunsch());
			wlaStatus.setText(versandauftragDto.getCStatustext());
			wcbEmpfangsbestaetigung
					.setSelected(Helper.short2Boolean(versandauftragDto
							.getBEmpfangsbestaetigung()));
			List<File> mailFiles = new ArrayList<File>();
			File f;
			if (versandauftragDto.getOInhalt() != null
					&& versandauftragDto.getOInhalt().length > 0) {
				if (versandauftragDto.getCDateiname() == null) {
					f = File.createTempFile("Anhang", ".pdf");
				} else {
					String fileName = versandauftragDto.getCDateiname();
					int posOfDot = fileName.lastIndexOf(".");
					// String sPrefix = fileName.substring(0, posOfDot);
					// while (sPrefix.length() < 3) {
					// sPrefix = sPrefix + "_";
					// }
					// f = File.createTempFile(sPrefix,
					// fileName.substring(posOfDot));
					String sPrefix = fileName;
					String sSuffix = "";
					if (posOfDot != -1) {
						sPrefix = fileName.substring(0, posOfDot);
						while (sPrefix.length() < 3) {
							sPrefix = sPrefix + "_";
						}
						sSuffix = fileName.substring(posOfDot);
					}
					f = File.createTempFile(sPrefix, sSuffix);
				}
				FileOutputStream foStream = new FileOutputStream(f);
				foStream.write(versandauftragDto.getOInhalt());
				foStream.close();
				mailFiles.add(f);
				f.deleteOnExit();
			}
			for (int i = 0; i < versandanhangDto.length; i++) {
				try {
					versandanhangDto[i] = DelegateFactory
							.getInstance()
							.getJCRDocDelegate()
							.getDataForVersandanhangFromJCR(versandanhangDto[i]);
				} catch (Throwable e) {
				}
				String fileName = versandanhangDto[i].getCDateiname();
				byte[] oinhalt = versandanhangDto[i].getOInhalt();
				if (oinhalt == null || oinhalt.length == 0) {
					myLogger.warn("Der Versandanhang mit der IId "
							+ versandanhangDto[i].getIId()
							+ " hat keinen Inhalt! (" + fileName + ")");
					continue;
				}

				int posOfDot = fileName.lastIndexOf(".");
				String sPrefix = fileName.substring(0, posOfDot);
				while (sPrefix.length() < 3) {
					sPrefix = sPrefix + "_";
				}
				File tempFile = File.createTempFile(sPrefix,
						fileName.substring(posOfDot));
				tempFile.deleteOnExit();
				FileOutputStream fosFileGetter = new FileOutputStream(tempFile);
				fosFileGetter.write(oinhalt);
				fosFileGetter.close();
				mailFiles.add(tempFile);
			}
			wrapperAttachmentViewer.setFileList(mailFiles.toArray(new File[0]));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			// mit den Daten des bestehenden Versandauftrags fuellen
			dto2Components();
			if (versandauftragDto != null) {
				versandauftragDto.setIId(null);
			}
			wtsfSendezeitpunktWunsch.setTimestamp(null);
			wtfEmpfaenger.setText(null);
			wtfCCEmpfaenger.setText(null);
		} else {
			versandauftragDto = DelegateFactory.getInstance()
					.getVersandDelegate()
					.versandauftragFindByPrimaryKey((Integer) key);
			versandanhangDto = DelegateFactory
					.getInstance()
					.getVersandDelegate()
					.VersandanhangFindByVersandauftragIID(
							versandauftragDto.getIId());
			dto2Components();
		}
	}

	protected void components2Dto() throws Throwable {
		versandauftragDto.setCAbsenderadresse(wtfAbsender.getText());
		versandauftragDto.setCBetreff(wtfBetreff.getText());
		versandauftragDto.setCCcempfaenger(wtfCCEmpfaenger.getText());
		versandauftragDto.setCEmpfaenger(wtfEmpfaenger.getText());
		versandauftragDto.setCText(wefText.getText());
		versandauftragDto.setTSendezeitpunktwunsch(wtsfSendezeitpunktWunsch
				.getTimestamp());
		versandauftragDto.setBEmpfangsbestaetigung(Helper
				.boolean2Short(wcbEmpfangsbestaetigung.isSelected()));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			versandauftragDto = DelegateFactory.getInstance()
					.getVersandDelegate()
					.updateVersandauftrag(versandauftragDto, false);
			setKeyWhenDetailPanel(versandauftragDto.getIId());
			for (int i = 0; i < versandanhangDto.length; i++) {
				versandanhangDto[i].setIId(null);
				versandanhangDto[i].setVersandauftragIId(versandauftragDto
						.getIId());
				versandanhangDto[i] = DelegateFactory.getInstance()
						.getVersandDelegate()
						.createVersandanhang(versandanhangDto[i]);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						versandauftragDto.getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		boolean bDarfAndereVeraendern = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD);

		if (!LPMain.getTheClient().getIDPersonal()
				.equals(versandauftragDto.getPersonalIId())
				&& bDarfAndereVeraendern == false) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
			return;
		} else {
			if (iAspect == I_ASPECT_FEHLGESCHLAGEN
					|| iAspect == I_ASPECT_PAPIERKORB) {

				Object[] ids = null;
				if (iAspect == I_ASPECT_FEHLGESCHLAGEN) {
					ids = tpVersandauftrag.getPanelQueryFehlgeschlagen(true)
							.getSelectedIds();
				} else {
					ids = tpVersandauftrag.getPanelQueryPapierkorb(true)
							.getSelectedIds();
				}
				for (int i = 0; i < ids.length; i++) {
					DelegateFactory.getInstance().getVersandDelegate()
							.removeVersandauftrag((Integer) ids[i]);
				}
			} else {

				if (iAspect == I_ASPECT_POSTAUSGANG) {

					versandauftragDto = DelegateFactory
							.getInstance()
							.getVersandDelegate()
							.versandauftragFindByPrimaryKey(
									versandauftragDto.getIId());

					if (versandauftragDto.getStatusCNr() != null) {
						// SP 275 Wahrscheinlich wurde der auftrag nun versendet
						// und ist
						// nicht mehr da
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.versandauftrag.error.loeschen"));
						return;
					}
				}
				DelegateFactory.getInstance().getVersandDelegate()
						.storniereVersandauftrag(versandauftragDto.getIId());
			}

			super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		JButton btn = getToolBar().getButton(ACTION_SAVE);
		if (btn != null) {
			btn.setIcon(HelperClient.createImageIcon("disk_blue.png"));
		}
		boolean bDarfAndereVeraendern = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD);

		if (!LPMain.getTheClient().getIDPersonal()
				.equals(versandauftragDto.getPersonalIId())
				&& bDarfAndereVeraendern == false) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
			return;
		} else {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
		}

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		wrapperAttachmentViewer = new WrapperAttachmentViewer(
				getInternalFrame(), "");
		// wrapperPdfField.getLabelDatei().setVisible(false);
		// wrapperPdfField.getButtonDatei().setVisible(false);

		wlaEmpfaenger = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.empfaenger"));
		wtfEmpfaenger = new WrapperTextField();
		wlaCCEmpfaenger = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.cc"));
		wtfCCEmpfaenger = new WrapperTextField();
		wlaBetreff = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.betreff"));
		wtfBetreff = new WrapperTextField();
		wlaAbsender = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.absender"));
		wtfAbsender = new WrapperTextField();
		wlaSendezeitpunktwunsch = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.sendezeitpunkt"));
		wtsfSendezeitpunktWunsch = new WrapperTimestampField();
		wlaText = new WrapperLabel(LPMain.getTextRespectUISPr("label.text"));
		wefText = new WrapperEditorField(getInternalFrame(), "");

		wlaStatus.setHorizontalAlignment(SwingConstants.LEFT);

		wtfEmpfaenger.setColumnsMax(VersandFac.MAX_EMPFAENGER);
		wtfCCEmpfaenger.setColumnsMax(VersandFac.MAX_CCEMPFAENGER);
		wtfBetreff.setColumnsMax(VersandFac.MAX_BETREFF);
		wtfAbsender.setColumnsMax(VersandFac.MAX_ABSENDER);

		wtfEmpfaenger.setMandatoryFieldDB(true);
		// wtfBetreff.setMandatoryField(true);
		// wtfAbsender.setMandatoryField(true);
		// wtsfSendezitpunktWunsch.setMandatoryField(true);

		wlaEmpfaenger.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaEmpfaenger.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtsfSendezeitpunktWunsch.setMinimumSize(new Dimension(200, Defaults
				.getInstance().getControlHeight()));
		wtsfSendezeitpunktWunsch.setPreferredSize(new Dimension(200, Defaults
				.getInstance().getControlHeight()));
		wcbEmpfangsbestaetigung = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.empfangsbestaetigung"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaEmpfaenger, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfEmpfaenger, new GridBagConstraints(1, iZeile, 5,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaCCEmpfaenger, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfCCEmpfaenger, new GridBagConstraints(1, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBetreff, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAbsender, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfAbsender, new GridBagConstraints(1, iZeile, 5,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaSendezeitpunktwunsch, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtsfSendezeitpunktWunsch, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbEmpfangsbestaetigung, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wrapperAttachmentViewer, new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefText, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn
				.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.status")
						+ ":"), new GridBagConstraints(0, iZeile, 1, 1, 0.0,
						0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jPanelWorkingOn.add(wlaStatus, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = new String[0];
		switch (iAspect) {
		case I_ASPECT_POSTAUSGANG: {
			createAndSaveAndShowButton("/com/lp/client/res/check2.png",
					LPMain.getTextRespectUISPr("lp.sofortversenden"),
					ACTION_SPECIAL_SOFORT_VERSENDEN,
					RechteFac.RECHT_LP_SYSTEM_CUD);
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD,
					ACTION_SPECIAL_SOFORT_VERSENDEN, ACTION_PRINT };
		}
			break;

		case I_ASPECT_GESENDET: {
			aWhichButtonIUse = new String[] { ACTION_SAVE, ACTION_DISCARD,
					ACTION_PRINT };
		}
			break;

		case I_ASPECT_PAPIERKORB: {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };
		}
			break;

		case I_ASPECT_FEHLGESCHLAGEN: {
			/*
			 * createAndSaveAndShowButton("/com/lp/client/res/mail_new.png",
			 * LPMain.getInstance().getTextRespectUISPr( "lp.erneutversenden"),
			 * ACTION_SPECIAL_ERNEUT_VERSENDEN);
			 */
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };
		}
			break;
		}

		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VERSANDAUFTRAG;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ERNEUT_VERSENDEN)) {
			DelegateFactory
					.getInstance()
					.getVersandDelegate()
					.sendeVersandauftragErneut(versandauftragDto.getIId(), null);
			// wirkt fuer diesen Filter wie Loeschen -> QP informieren
			ItemChangedEvent it = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
			this.tpVersandauftrag.lPEventItemChanged(it);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SOFORT_VERSENDEN)) {
			boolean bDarfAndereVeraendern = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD);

			if (!LPMain.getTheClient().getIDPersonal()
					.equals(versandauftragDto.getPersonalIId())
					&& bDarfAndereVeraendern == false) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("system.error.versandauftrag.bearbeiten"));
				return;
			}

			DelegateFactory.getInstance().getVersandDelegate()
					.sendeVersandauftragSofort(versandauftragDto.getIId());
			// wirkt fuer diesen Filter wie Loeschen -> QP informieren
			ItemChangedEvent it = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
			this.tpVersandauftrag.lPEventItemChanged(it);
		}
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(e, bChangeKeyLockMeI, bNeedNoNewI);
		JButton btn = getToolBar().getButton(ACTION_SAVE);
		if (btn != null) {
			btn.setIcon(HelperClient.createImageIcon("mail_forward.png"));
		}
		clearStatusbar();
		if (versandauftragDto != null) {
			versandauftragDto.setIId(null);
		}
		wtsfSendezeitpunktWunsch.setTimestamp(null);
		wtfEmpfaenger.setText(null);
		wtfCCEmpfaenger.setText(null);
		// Der Absender ist der Benutzer
		Integer personalIId = LPMain.getTheClient().getIDPersonal();
		PersonalDto personalDto = DelegateFactory.getInstance()
				.getPersonalDelegate().personalFindByPrimaryKey(personalIId);
		PartnerDto partnerDtoAbsender = DelegateFactory.getInstance()
				.getPartnerDelegate()
				.partnerFindByPrimaryKey(personalDto.getPartnerIId());
		PartnerkommunikationDto[] komm = DelegateFactory
				.getInstance()
				.getPartnerDelegate()
				.partnerkommFindByPartnerIIdKommunikationsartPAiIdKommArtMandant(
						partnerDtoAbsender.getIId(),
						PartnerFac.KOMMUNIKATIONSART_EMAIL,
						LPMain.getTheClient().getMandant());
		if (komm != null && komm.length > 0) {
			wtfAbsender.setText(komm[0].getCInhalt());
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfEmpfaenger;
	}

	/**
	 * Drucke Versandauftrag.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		// PartnerDto empfaengerPartnerDto =
		// DelegateFactory.getInstance().getPartnerDelegate
		// ().partnerFindByPrimaryKey
		// (versandauftragDto.getPartnerIIdEmpfaenger());
		// DelegateFactory.getInstance().getPartnerDelegate()

		getInternalFrame().showReportKriterien(
				new ReportVersandauftrag(getInternalFrame(), versandauftragDto,
						null), null, null, false, false, false);

		/*
		 * getInternalFrame().showReportKriterien(new
		 * ReportVersandauftrag(getInternalFrame(), versandauftragDto,
		 * "my title"), empfaengerPartnerDto, empfaengerPartnerDto.getIId(),
		 * false);
		 */

		eventYouAreSelected(false);
	}

}
