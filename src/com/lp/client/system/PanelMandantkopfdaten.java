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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.Currency;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PanelPartnerDetail;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um das Mandantkopfdaten-CRUD.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>08.03.05</I></p>
 *
 * @author $Author: sebastian $
 * @version $Revision: 1.4 $
 */
public class PanelMandantkopfdaten extends PanelPartnerDetail {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaMandant = null;
	private WrapperTextField wtfMandant = null;
	private WrapperComboBox wcbWaehrung = null;
	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private Map<?, ?> tmWaehrungen = null;
	private PanelQueryFLR panelQueryFLRPartner = null;

	public PanelMandantkopfdaten(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
		initPanel();
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);
		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance()
				.getTheClient().getMandant(), getMandantDto().getCNr());

		String cNr = getInternalFrameSystem().getMandantDto().getCNr();

		if (cNr == null) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			getInternalFrameSystem().setMandantDto(
					DelegateFactory.getInstance().getMandantDelegate()
							.mandantFindByPrimaryKey(cNr));

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameSystem().getMandantDto().getCNr());

			setStatusbar();
		}
		dto2Components();
	}

	private void jbInit() throws Exception {
		resetToolsPanel();
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// Partnerfelder von der Oberklasse, es folgen die Mandantfelder.

		wlaMandant = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant"));
		wtfMandant = new WrapperTextField(MandantFac.MAX_MANDANT);
		wtfMandant.setMandatoryFieldDB(true);

		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.waehrung"));
		wcbWaehrung = new WrapperComboBox();
		wcbWaehrung.setMandatoryFieldDB(true);

		wtfLandPLZOrt.setMandatoryField(true);

		// ab hier einhaengen.

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaMandant, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wtfMandant, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wcbWaehrung, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void components2Dto() throws Throwable {

		super.components2Dto();

		getMandantDto().getAnwenderDto().setIId(
				new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

		getMandantDto().setWaehrungCNr(wcbWaehrung.getKeyOfSelectedItem() + "");

		// Damit man gleich speichern kann.
		String sKBez = getMandantDto().getCKbez() != null ? getMandantDto()
				.getCKbez() : wtfMandant.getText();

		getMandantDto().setCKbez(sKBez);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			try {
				if (getMandantDto().getCNr() == null) {
					// Achtung: Der PK wird vom Benutzer vergeben; deshalb hier
					// ...
					getMandantDto().setCNr(wtfMandant.getText());
					getMandantDto().setPartnerIIdLieferadresse(
							getMandantDto().getPartnerIId());
					try {
						String cNr = DelegateFactory.getInstance()
								.getMandantDelegate()
								.createMandant(getMandantDto());

						setKeyWhenDetailPanel(cNr);
						getInternalFrameSystem().getMandantDto().setCNr(cNr);

						if (LPMain.getInstance().isLPAdmin()) {
							DialogFactory
									.showModalDialog(
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.info"),
											LPMain.getInstance()
													.getTextRespectUISPr(
															"lp.mandant.neuanlage.updatemanager"));
						}

					} catch (Throwable t) {
						getMandantDto().setCNr(null);

						if (((ExceptionLP) t).getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
							DialogFactory.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.error"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.mandantschonangelegt"));
						}

						throw t;
					}
				} else {
					// update
					DelegateFactory.getInstance().getMandantDelegate()
							.updateMandant(getMandantDto());
				}

				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					// der erste eintrag wurde angelegt
					getInternalFrame().setKeyWasForLockMe(
							getKeyWhenDetailPanel() + "");
				}

				eventYouAreSelected(false);

				// dem internalframe den zu lockenden setzen.
				getInternalFrame().setKeyWasForLockMe(
						getKeyWhenDetailPanel() + "");

				setStatusbar();

				dto2Components();
			} catch (ExceptionLP t) {
				if (t.getICode() == EJBExceptionLP.FEHLER_SYSTEM_MWSTSATZ_IST_NULL) {
					DialogFactory.showModalDialog(
							LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr(
									"mandant.mwstsatzistnull"));
				} else {
					throw t;
				}
			}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (getLockedstateDetailMainKey().getIState() == LOCK_IS_NOT_LOCKED) {
			DelegateFactory.getInstance().getMandantDelegate()
					.removeMandant(getMandantDto());

			getInternalFrameSystem().setMandantDto(new MandantDto());

			super.eventActionDelete(e, true, true);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		super.eventActionUpdate(aE, bNeedNoUpdateI);

		this.wtfMandant.setActivatable(false);
		this.wtfName1.setActivatable(false);
	}

	protected void dto2Components() throws Throwable {

		super.dto2Components();

		wtfMandant.setText(getMandantDto().getCNr());
		wcbWaehrung.setKeyOfSelectedItem(getMandantDto().getWaehrungCNr());
	}

	private void initPanel() throws Throwable {

		// Waehrung mit EUR default vorbesetzen
		tmWaehrungen = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen();
		wcbWaehrung.setMap(tmWaehrungen);

		Iterator<?> it = tmWaehrungen.keySet().iterator();
		int count = 0;
		while (it.hasNext()) {
			if (it.next()
					.toString()
					.equals(Currency.getInstance(
							Helper.string2Locale(LocaleFac.LP_LOCALE_DE_AT))
							.getCurrencyCode())) {
				break;
			}
			count++;
		}
		wcbWaehrung.setSelectedIndex(count);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			getInternalFrameSystem().setMandantDto(new MandantDto());
			setDefaults();
		}
		wtfMandant.setActivatable(true);
		wcoPartnerart.setActivatable(true);
	}

	protected void setDefaults() throws Throwable {

		super.setDefaults();

		wcoPartnerart.setActivatable(false);
		wcoPartnerart.setEnabled(false);
		wcoPartnerart.setKeyOfSelectedItem(PartnerFac.PARTNERART_ADRESSE);

		getPartnerDto().setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);

		// Eigentlich wird dies beiden Werte am Server gesetzt!
		// Grund: Tabellen zyklische abh.
		getInternalFrameSystem().getMandantDto().setIAendern(
				LPMain.getInstance().getTheClient().getIDPersonal());

		getInternalFrameSystem().getMandantDto().setIAnlegen(
				LPMain.getInstance().getTheClient().getIDPersonal());

		// getInternalFrame().getSystemDelegate().
		// getInternalFrameSystem().getMandantDto().setIIdKostenstelle(
		// LPMain.getInstance().getTheClient().getMandant());

	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	protected MandantDto getMandantDto() {
		return getInternalFrameSystem().getMandantDto();
	}

	protected PartnerDto getPartnerDto() {
		return getInternalFrameSystem().getMandantDto().getPartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		getInternalFrameSystem().getMandantDto().setPartnerDto(partnerDto);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	/**
	 * 
	 * @param loggedinMandant
	 *            String
	 * @param selectedMandant
	 *            String
	 * @throws Throwable
	 */
	private void checkMandantLoggedInEqualsMandantSelected(
			String loggedinMandant, String selectedMandant) throws Throwable {

		if (!loggedinMandant.equals(selectedMandant)) {

			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(
					PanelBasis.ACTION_UPDATE);

			item.getButton().setEnabled(false);
			getPanelStatusbar().setLockField(
					LPMain.getInstance().getTextRespectUISPr(
							"system.nurleserecht"));
		}
	}

	protected void wtfName1FocusLost(FocusEvent e) {

		boolean bExit = false;

		try {

			if (getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW
					|| getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {

				if (getPartnerDto().getIId() == null // neu
						|| (wtfName1.getText() != null && !wtfName1.getText()
								.equals(getPartnerDto()
										.getCName1nachnamefirmazeile1())) // name1
																			// geaendert
						|| isBNeuAusPartner()) { // neu aus

					if (wtfName1.getText() != null
							&& !wtfName1.getText().equals("")) {
						PartnerDto[] p = DelegateFactory.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryName1(wtfName1.getText());

						if (p.length > 1) {
							Object[] options = {
									LPMain.getInstance().getTextRespectUISPr(
											"lp.ja"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.nein") };
							if ((JOptionPane.showOptionDialog(
									null,
									LPMain.getInstance().getTextRespectUISPr(
											"part.existiert_mehrere"), "",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0])) == JOptionPane.YES_OPTION) {
								String[] aWhichButtonIUse = null;
								QueryType[] querytypes = null;
								FilterKriterium[] filters = createFKPartnerName1(wtfName1
										.getText());

								panelQueryFLRPartner = new PanelQueryFLR(
										querytypes, filters,
										QueryParameters.UC_ID_PARTNER,
										aWhichButtonIUse, getInternalFrame(),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"part.partner"));

								panelQueryFLRPartner
										.befuellePanelFilterkriterienDirekt(
												SystemFilterFactory
														.getInstance()
														.createFKDLandPlzOrtOrt(),
												null);
								// vorbesetzen
								if (getPartnerDto().getIId() != null) {
									panelQueryFLRPartner
											.setSelectedId(getPartnerDto()
													.getIId());
								}
								new DialogQuery(panelQueryFLRPartner);
							}
						} else if (p.length == 1)
						// Existiert nur ein Partner mit demselben Namen
						{
							Object[] options = {
									LPMain.getInstance().getTextRespectUISPr(
											"lp.ja"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.nein") };
							if ((JOptionPane.showOptionDialog(
									null,
									LPMain.getInstance().getTextRespectUISPr(
											"part.existiert_ein"), "",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0])) == JOptionPane.YES_OPTION) {
								setPartnerDto(p[0]);
								dto2Components();
							}
						}
					}

				}
			}
		} catch (Throwable ex1) {
			// nothing here
		}

		if (!bExit) {
			// Kurzbezeichnung
			int iLK = 0;
			if (wtfKurzbezeichnung != null) {
				iLK = wtfKurzbezeichnung.getText() == null ? 0
						: wtfKurzbezeichnung.getText().length();
			}

			if (iLK == 0) {
				String sN1 = " ";
				if (wtfName1 != null) {
					sN1 = wtfName1.getText() == null ? " " : wtfName1.getText()
							+ " ";
				}

				int iE = sN1.indexOf(" ");
				if (iE > PartnerFac.MAX_KBEZ / 2) {
					iE = PartnerFac.MAX_KBEZ / 2;
				}
				wtfKurzbezeichnung.setText(sN1.substring(0, iE));
			}
		}

	}

	/**
	 * Default Filterkriterium fuer Filter nach Partner Name 1. <br>
	 * Bedingung: Attributname im FLR ist
	 * PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1.
	 * 
	 * @param sPartnerName1
	 *            Integer
	 * @return FilterKriterium[]
	 */
	public FilterKriterium[] createFKPartnerName1(String sPartnerName1) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(
				PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1, true, "'"
						+ sPartnerName1 + "'", FilterKriterium.OPERATOR_EQUAL,
				true);
		kriterien[0] = krit1;
		return kriterien;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRPartner) {
				Integer keyPartner = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (keyPartner != null) {
					PartnerDto partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) keyPartner);

					setPartnerDto(partnerDto);
					dto2Components();

				}
			}
			if (e.getSource() == panelQueryFLROrt) {
				Integer keyLandPLZOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (keyLandPLZOrt != null) {
					LandplzortDto landPLZOrtDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);
					getPartnerDto().setLandplzortDto(landPLZOrtDto);
					getPartnerDto().setLandplzortIId(keyLandPLZOrt);
					wtfLandPLZOrt.setText(landPLZOrtDto.formatLandPlzOrt());
				}
			}

			else if (e.getSource() == panelQueryFLRPostfach) {
				Integer keyLandPLZOrt = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (keyLandPLZOrt != null) {
					LandplzortDto landPLZOrtDto = DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.landplzortFindByPrimaryKey((Integer) keyLandPLZOrt);
					getPartnerDto().setLandplzortDto_Postfach(landPLZOrtDto);
					getPartnerDto().setLandplzortIIdPostfach(keyLandPLZOrt);
					wtfOrtPostfach.setText(landPLZOrtDto.formatLandPlzOrt());
				}
			}

			else if (e.getSource() == panelQueryFLRBranche) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					BrancheDto brancheDto = DelegateFactory.getInstance()
							.getPartnerDelegate().brancheFindByPrimaryKey(key);
					getPartnerDto().setBrancheIId(key);
					wtfBranche.setText(brancheDto.getCNr());
				}
			}

			else if (e.getSource() == panelQueryFLRPartnerklasse) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					PartnerklasseDto partnerklasseDto = DelegateFactory
							.getInstance().getPartnerDelegate()
							.partnerklasseFindByPrimaryKey(key);
					getPartnerDto().setPartnerklasseIId(key);
					wtfPartnerklasse.setText(partnerklasseDto.getCNr());
				}
			}
		}
	}
}
