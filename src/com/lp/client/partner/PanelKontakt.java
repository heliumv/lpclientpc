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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KontaktDto;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um den Ansprechpartner</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum xx.12.04</p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.8 $ Date $Date: 2012/08/29 14:29:30 $
 */
abstract public class PanelKontakt extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;
	private Border border = null;
	private JPanel panelButtonAction = null;
	private WrapperLabel wlaKontaktVon = null;
	private WrapperTimestampField wtfKontaktVon = null;
	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;

	protected KontaktDto kontaktDto = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerAuswahl = null;
	private PanelQueryFLR panelQueryFLRKontaktart = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;

	private GridBagLayout gridBagLayout = null;
	private GridBagLayout gridBagLayoutAll = null;

	private WrapperLabel wlaTitel = new WrapperLabel();
	private WrapperTextField wtfTitel = new WrapperTextField();

	private WrapperLabel wlaKontaktBis = null;
	private WrapperTimestampField wtfKontaktBis = new WrapperTimestampField();

	private WrapperButton wbuKontaktart = new WrapperButton();
	private WrapperTextField wtfKontaktart = new WrapperTextField();

	private WrapperButton wbuZugewiesener = new WrapperButton();
	private WrapperTextField wtfZugewiesener = new WrapperTextField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wefKommentar = null;

	private WrapperTelefonField wtfTelefon = null;

	private WrapperLabel wlaWiedervorlage = new WrapperLabel();
	private WrapperTimestampField wtfWiedervorlage = new WrapperTimestampField();

	private WrapperButton wbuJetzt = new WrapperButton(new ImageIcon(getClass()
			.getResource("/com/lp/client/res/clock16x16.png")));

	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER = "action_special_flr_ansprechpartner";
	static final public String ACTION_SPECIAL_FLR_KONTAKTART = "action_special_flr_kontaktart";
	static final public String ACTION_SPECIAL_FLR_PERSON = "action_special_flr_person";

	static final private String ACTION_SPECIAL_POSITIONERLEDIGEN = "action_special_positionerledigen";

	public PanelKontakt(InternalFrame internalFrame, String add2TitleI,
			Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// Buttons.
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		this.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("bes.tooltip.manuellerledigen"),
				ACTION_SPECIAL_POSITIONERLEDIGEN, null);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// Das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und einhaengen.
		panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel();
		gridBagLayout = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayout);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier Ansprechpartnerfelder.
		wlaKontaktVon = new WrapperLabel();
		wlaKontaktVon.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kontaktvon"));
		wtfKontaktVon = new WrapperTimestampField();
		wtfKontaktVon.setMandatoryField(true);

		wlaKontaktBis = new WrapperLabel();
		wlaKontaktBis.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kontaktbis"));

		wtfTitel.setMandatoryField(true);

		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel"));

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ansprechpartner.long"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wbuKontaktart = new WrapperButton();
		wbuKontaktart.setText(LPMain.getInstance().getTextRespectUISPr(
				"proj.projekt.label.kontaktart")
				+ "...");
		wbuKontaktart.setActionCommand(ACTION_SPECIAL_FLR_KONTAKTART);
		wbuKontaktart.addActionListener(this);

		wbuJetzt.setToolTipText("Aktuelle Zeit einstellen");
		wbuJetzt.addActionListener(this);

		wbuZugewiesener.setText(LPMain.getInstance().getTextRespectUISPr(
				"proj.personal.zugewiesener")
				+ "...");

		wtfTelefon = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfTelefon.setActivatable(false);

		wbuZugewiesener.setActionCommand(ACTION_SPECIAL_FLR_PERSON);
		wbuZugewiesener.addActionListener(this);

		wtfZugewiesener.setMandatoryField(true);

		wtfKontaktart.setMandatoryField(true);

		wtfAnsprechpartner = new WrapperTextField();

		wtfZugewiesener.setActivatable(false);
		wtfKontaktart.setActivatable(false);
		wtfAnsprechpartner.setActivatable(false);

		wlaWiedervorlage.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.wiedervorlage"));
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));

		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.kommentar"));

		// Ab hier einhaengen.
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaTitel, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfTitel, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTelefon, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKontaktVon, new GridBagConstraints(0, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wtfKontaktVon, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wlaKontaktBis, new GridBagConstraints(2, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wtfKontaktBis, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wbuJetzt, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKontaktart, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfKontaktart, new GridBagConstraints(1, iZeile, 3, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZugewiesener, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfZugewiesener, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaWiedervorlage, new GridBagConstraints(0, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWiedervorlage, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 3, 1,
				0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	protected abstract String getLockMeWer() throws Exception;

	/**
	 * Behandle Ereignis Neu.
	 * 
	 * @param eventObject
	 *            Ereignis.
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	final public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		kontaktDto = new KontaktDto();

		if (!bNeedNoNewI) {

			setDefaults();
		}
	}

	final public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(true);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();

			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

				leereAlleFelder(this);

				if (key != null && key.equals(LPMain.getLockMeForNew())) {

					wtfKontaktVon.setTimestamp(new java.sql.Timestamp(System
							.currentTimeMillis()));

					PersonalDto personalDto = DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									LPMain.getTheClient().getIDPersonal());
					wtfZugewiesener.setText(personalDto.formatAnrede());
					kontaktDto.setPersonalIIdZugewiesener(personalDto.getIId());
				}

				clearStatusbar();

			} else {

				kontaktDto = DelegateFactory.getInstance().getPartnerDelegate()
						.kontaktFindByPrimaryKey((Integer) key);

				setStatusbar();

				dto2Components();
			}

		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				getSelectedPartnerTitelAnrede());

	}

	protected String getSelectedPartnerTitelAnrede() {
		String t1 = "";
		if (getPartnerDto() != null) {
			t1 = getPartnerDto().formatFixTitelName1Name2();
		}
		return t1;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (kontaktDto.getTErledigt() != null) {

			boolean b = DialogFactory
					.showModalJaNeinDialog(getInternalFrame(),
							"Der Kontakt ist bereits erledigt. Wollen sie die Erledigung zur\u00FCcknehmen?");

			if (b == true) {
				kontaktDto.setTErledigt(null);

				DelegateFactory.getInstance().getPartnerDelegate()
						.updateKontakt(kontaktDto);
			} else {
				return;
			}

		}

		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	final protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getKontaktDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(getKontaktDto().getTAnlegen());
	}

	protected void setDefaults() throws Throwable {

	}

	abstract protected PartnerDto getPartnerDto();

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER)) {

			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_LEEREN };

			QueryType[] querytypes = null;
			panelQueryFLRAnsprechpartnerAuswahl = new PanelQueryFLR(querytypes,
					PartnerFilterFactory.getInstance().createFKAnsprechpartner(
							getPartnerDto().getIId()),
					QueryParameters.UC_ID_ANSPRECHPARTNER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("button.ansprechpartner.long"));

			panelQueryFLRAnsprechpartnerAuswahl
					.befuellePanelFilterkriterienDirekt(PartnerFilterFactory
							.getInstance()
							.createFKDAnsprechpartnerPartnerName(),
							PartnerFilterFactory.getInstance()
									.createFKDPartnerLandPLZOrt());

			new DialogQuery(panelQueryFLRAnsprechpartnerAuswahl);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KONTAKTART)) {
			String[] aWhichButtonIUse = null;

			QueryType[] querytypes = null;
			panelQueryFLRKontaktart = new PanelQueryFLR(querytypes, null,
					QueryParameters.UC_ID_KONTAKTART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"proj.projekt.label.kontaktart"));

			new DialogQuery(panelQueryFLRKontaktart);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_PERSON)) {
			panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
					.createPanelFLRPersonal(getInternalFrame(), true, false,
							getKontaktDto().getPersonalIIdZugewiesener());
			new DialogQuery(panelQueryFLRPersonal);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_POSITIONERLEDIGEN)) {
			if (kontaktDto != null) {

				if (kontaktDto.getTErledigt() == null) {

					kontaktDto.setTErledigt(new Timestamp(System
							.currentTimeMillis()));

					DelegateFactory.getInstance().getPartnerDelegate()
							.updateKontakt(kontaktDto);
				} else {
					kontaktDto.setTErledigt(null);

					DelegateFactory.getInstance().getPartnerDelegate()
							.updateKontakt(kontaktDto);
				}
				if (getInternalFrame() instanceof InternalFramePartner) {
					((InternalFramePartner) getInternalFrame()).getTpPartner().panelQueryKontakt
							.eventYouAreSelected(true);
				} else if (getInternalFrame() instanceof InternalFrameKunde) {
					((InternalFrameKunde) getInternalFrame()).getTpKunde().panelQueryKontakt
							.eventYouAreSelected(true);
				}

			}

		} else if (e.getSource().equals(wbuJetzt)) {
			wtfKontaktBis
					.setTimestamp(new Timestamp(System.currentTimeMillis()));
		}

	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getKontaktDto().setAnsprechpartnerIId(key);
					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
							.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(key);
					wtfAnsprechpartner.setText(ansprechpartnerDto
							.getPartnerDto().formatTitelAnrede());

				}
			} else if (e.getSource() == panelQueryFLRKontaktart) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getKontaktDto().setKontaktartIId(key);
					KontaktartDto kontaktartDto = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.kontaktartFindByPrimaryKey(key);
					wtfKontaktart.setText(kontaktartDto.getCBez());

				}
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfZugewiesener.setText(personalDto.formatAnrede());
					kontaktDto.setPersonalIIdZugewiesener((Integer) key);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartnerAuswahl) {
				wtfAnsprechpartner.setText(null);
				getKontaktDto().setAnsprechpartnerIId(null);
			}
		}
	}

	protected void dto2Components() throws Throwable {

		if (getKontaktDto().getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory
					.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(
							getKontaktDto().getAnsprechpartnerIId());

			PartnerDto partnerDto = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(
							ansprechpartnerDto.getPartnerIIdAnsprechpartner());

			PartnerDto partnerDtoZugehoerig = DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIId());

			wtfAnsprechpartner.setText(partnerDto.formatTitelAnrede());

			if (ansprechpartnerDto.getCTelefon() == null) {
				wtfTelefon.setPartnerKommunikationDto(partnerDtoZugehoerig,
						ansprechpartnerDto.getCTelefon());
			} else {

				String cTelefon = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.enrichNumber(
								ansprechpartnerDto.getPartnerIId(),
								PartnerFac.KOMMUNIKATIONSART_TELEFON,
								ansprechpartnerDto
										.getCTelefon(),
								true);

				ansprechpartnerDto
						.setCTelefon(cTelefon
										+ " "
										+ ansprechpartnerDto
												.getCTelefon());

				wtfTelefon.setPartnerKommunikationDto(getPartnerDto(),
						ansprechpartnerDto.getCTelefon());
			}

		} else {
			wtfAnsprechpartner.setText(null);

			if (getPartnerDto().getCTelefon() != null) {
				wtfTelefon.setPartnerKommunikationDto(getPartnerDto(),getPartnerDto().getCTelefon());
			} else {
				wtfTelefon.setPartnerKommunikationDto(null, null);
			}

		}

		KontaktartDto kontaktartDto = DelegateFactory.getInstance()
				.getPartnerServicesDelegate()
				.kontaktartFindByPrimaryKey(getKontaktDto().getKontaktartIId());
		wtfKontaktart.setText(kontaktartDto.getCBez());

		wtfKontaktVon.setTimestamp(getKontaktDto().getTKontakt());
		wtfKontaktBis.setTimestamp(getKontaktDto().getTKontaktbis());
		wtfTitel.setText(getKontaktDto().getCTitel());

		PersonalDto personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						getKontaktDto().getPersonalIIdZugewiesener());
		wtfZugewiesener.setText(personalDto.formatAnrede());

		wefKommentar.setText(getKontaktDto().getXKommentar());
		wtfWiedervorlage.setTimestamp(getKontaktDto().getTWiedervorlage());

	}

	abstract protected KontaktDto getKontaktDto();

	protected void components2Dto() throws Throwable {

		getKontaktDto().setCTitel(wtfTitel.getText());
		getKontaktDto().setTKontakt(wtfKontaktVon.getTimestamp());
		getKontaktDto().setPartnerIId(getPartnerDto().getIId());
		getKontaktDto().setTKontaktbis(wtfKontaktBis.getTimestamp());
		getKontaktDto().setXKommentar(wefKommentar.getText());
		getKontaktDto().setTWiedervorlage(wtfWiedervorlage.getTimestamp());

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuAnsprechpartner;
	}

}
