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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelStueckliste extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StuecklisteDto stuecklisteDto = null;
	private InternalFrameStueckliste internalFrameStueckliste = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	PanelQueryFLR panelQueryFLRStueckliste = null;

	private WrapperButton buttonGoto = null;
	private static final String ACTION_GOTO = "ACTION_GOTO";
	private WrapperSelectField wsfLager = null;

	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";

	public final static String MY_OWN_NEW_GOTO_UEBERGEORDNET = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_GOTO_UEBERGEORDNET";

	public final static String MY_OWN_NEW_TOGGLE_FREIGABE = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_FREIGABE";

	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private WrapperButton wbuFertigungsgruppe = new WrapperButton();

	private WrapperLabel wlaLetzteAenderungArbeitsplan = new WrapperLabel();
	private WrapperLabel wlaLetzteAenderungPosition = new WrapperLabel();

	private WrapperLabel wlaDefaultdurchlaufzeitInTagen = new WrapperLabel();

	private WrapperSelectField wsfStueckliste = new WrapperSelectField(
			WrapperSelectField.STUECKLISTE, getInternalFrame(), true);

	private WrapperLabel wlaTage = new WrapperLabel();

	private WrapperNumberField wnfDefaultdurchlaufzeitInTagen = new WrapperNumberField();

	private JLabel wlaFreigabe = new JLabel();

	private WrapperLabel wlaErfassungsfaktor = new WrapperLabel();
	private WrapperNumberField wnfErfassungsfaktor = new WrapperNumberField();
	private WrapperLabel wlaErfassungsfaktorEinheit = new WrapperLabel();

	private WrapperComboBox wcoStuecklisteart = new WrapperComboBox();

	private WrapperButton wbuLeitauftrag = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();
	private WrapperTextField wtfLeitauftrag = new WrapperTextField();
	private WrapperCheckBox wcbFremdfertigung = new WrapperCheckBox();
	private WrapperCheckBox wcbMaterialbuchungBeiAblieferung = new WrapperCheckBox();
	private WrapperCheckBox wcbUnterstuecklistenAusgeben = new WrapperCheckBox();
	private WrapperCheckBox wcbUeberlieferbar = new WrapperCheckBox();
	private WrapperCheckBox wcbDruckeInLagerstandsdetailauswertung = new WrapperCheckBox();
	private WrapperCheckBox wcbKeineAutomatischeMaterialbuchung = new WrapperCheckBox();

	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameStueckliste.getTabbedPaneStueckliste().bStuecklistenfreigabe == true) {
			// Wenn Fregegeben, dann nicht mehr aenderbar
			if (stuecklisteDto != null && stuecklisteDto.getTFreigabe() != null) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	public InternalFrameStueckliste getInternalFramePersonal() {
		return internalFrameStueckliste;
	}

	public PanelStueckliste(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameStueckliste) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuArtikel;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getInternalFramePersonal().getStuecklisteDto().getIId();
		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			stuecklisteDto = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(
							getInternalFramePersonal().getStuecklisteDto()
									.getIId());

			dto2Components();

			String sLosgroesse = "0";
			if (getInternalFramePersonal().getStuecklisteDto().getNLosgroesse() != null) {
				sLosgroesse = getInternalFramePersonal().getStuecklisteDto()
						.getNLosgroesse().intValue()
						+ "";
			}

			getInternalFramePersonal().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFramePersonal().getStuecklisteDto()
							.getArtikelDto().formatArtikelbezeichnung()
							+ "; LGR: " + sLosgroesse);
			buttonGoto.setEnabled(true);

			LPButtonAction toggleFreigabe = getHmOfButtons().get(
					MY_OWN_NEW_TOGGLE_FREIGABE);
			// Wenn vorhanden
			if (toggleFreigabe != null) {
				toggleFreigabe.getButton().setEnabled(true);
			}

		} else {
			leereAlleFelder(this);
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getInstance().getTheClient().getMandant());

			if (parameter.getCWertAsObject() != null) {
				wnfDefaultdurchlaufzeitInTagen.setInteger((Integer) parameter
						.getCWertAsObject());
			}

			parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN,
							ParameterFac.KATEGORIE_STUECKLISTE,
							LPMain.getInstance().getTheClient().getMandant());
			wcbUnterstuecklistenAusgeben.setShort(Helper
					.boolean2Short((Boolean) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG,
							ParameterFac.KATEGORIE_FERTIGUNG,
							LPMain.getInstance().getTheClient().getMandant());
			wcbKeineAutomatischeMaterialbuchung.setShort(Helper
					.boolean2Short((Boolean) parameter.getCWertAsObject()));

			wsfLager.setKey(DelegateFactory.getInstance().getLagerDelegate()
					.getHauptlagerDesMandanten().getIId());

			MandantDto mDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

			if (mDto.getKundeIIdStueckliste() != null) {

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(mDto.getKundeIIdStueckliste());

				PartnerDto partnerDto = DelegateFactory.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey(kundeDto.getPartnerIId());
				stuecklisteDto.setPartnerIId(kundeDto.getPartnerIId());
				wtfKunde.setText(partnerDto.formatAnrede());

			}

			wcoStuecklisteart
					.setKeyOfSelectedItem(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

			parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG,
							ParameterFac.KATEGORIE_STUECKLISTE,
							LPMain.getInstance().getTheClient().getMandant());
			wcbMaterialbuchungBeiAblieferung.setShort(Helper
					.boolean2Short((Boolean) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_UEBERLIEFERBAR,
							ParameterFac.KATEGORIE_STUECKLISTE,
							LPMain.getInstance().getTheClient().getMandant());
			wcbUeberlieferbar.setShort(Helper.boolean2Short((Boolean) parameter
					.getCWertAsObject()));

			wnfErfassungsfaktor.setInteger(1);

			if (stuecklisteDto != null) {

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_FERTIGUNGSGRUPPE_VORBESETZEN,
								ParameterFac.KATEGORIE_STUECKLISTE,
								LPMain.getInstance().getTheClient()
										.getMandant());

				if ((Boolean) parameter.getCWertAsObject()) {

					FertigungsgruppeDto[] fertigungsgruppeDtos = DelegateFactory
							.getInstance().getStuecklisteDelegate()
							.fertigungsgruppeFindByMandantCNr();

					if (fertigungsgruppeDtos.length > 0) {
						wtfFertigungsgruppe.setText(fertigungsgruppeDtos[0]
								.getCBez());
						stuecklisteDto
								.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
										.getIId());
					}
				}
			}
			buttonGoto.setEnabled(false);

		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		buttonGoto.setEnabled(false);
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wnfDefaultdurchlaufzeitInTagen.setBigDecimal(stuecklisteDto
				.getNDefaultdurchlaufzeit());
		wnfErfassungsfaktor.setInteger(stuecklisteDto.getIErfassungsfaktor());
		wtfArtikel.setText(stuecklisteDto.getArtikelDto()
				.formatArtikelbezeichnung());
		wbuArtikel.setOKey(stuecklisteDto.getArtikelDto().getIId());

		wlaErfassungsfaktorEinheit.setText(stuecklisteDto.getArtikelDto()
				.getEinheitCNr().trim());

		if (stuecklisteDto.getAuftragIIdLeitauftrag() != null) {
			wtfLeitauftrag.setText(stuecklisteDto.getAuftragDto().getCNr());
		} else {
			wtfLeitauftrag.setText(null);
		}
		wcbFremdfertigung.setShort(stuecklisteDto.getBFremdfertigung());
		wcbMaterialbuchungBeiAblieferung.setShort(stuecklisteDto
				.getBMaterialbuchungbeiablieferung());
		wcbUeberlieferbar.setShort(stuecklisteDto.getBUeberlieferbar());
		wcbKeineAutomatischeMaterialbuchung.setShort(stuecklisteDto
				.getBKeineAutomatischeMaterialbuchung());
		wcbDruckeInLagerstandsdetailauswertung.setShort(stuecklisteDto
				.getBDruckeinlagerstandsdetail());
		wcbUnterstuecklistenAusgeben.setShort(stuecklisteDto
				.getBAusgabeunterstueckliste());
		wcbMaterialbuchungBeiAblieferung.setShort(stuecklisteDto
				.getBMaterialbuchungbeiablieferung());
		wcbUnterstuecklistenAusgeben.setShort(stuecklisteDto
				.getBAusgabeunterstueckliste());

		wsfLager.setKey(stuecklisteDto.getLagerIIdZiellager());
		wsfStueckliste.setKey(stuecklisteDto.getStuecklisteIIdEk());

		wcoStuecklisteart.setKeyOfSelectedItem(stuecklisteDto
				.getStuecklisteartCNr());

		if (stuecklisteDto.getPartnerIId() != null) {
			PartnerDto partnerDto = DelegateFactory.getInstance()
					.getPartnerDelegate()
					.partnerFindByPrimaryKey(stuecklisteDto.getPartnerIId());
			wtfKunde.setText(partnerDto.formatAnrede());
		} else {
			wtfKunde.setText(null);
		}

		if (stuecklisteDto.getFertigungsgruppeIId() != null) {
			FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.fertigungsgruppeFindByPrimaryKey(
							stuecklisteDto.getFertigungsgruppeIId());
			wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
		} else {
			wtfFertigungsgruppe.setText(null);
		}

		String personalArbeitsplan = "";
		PersonalDto personalDtoArbeitsplan = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						stuecklisteDto.getPersonalIIdAendernarbeitsplan());
		if (personalDtoArbeitsplan.getCKurzzeichen() != null) {
			personalArbeitsplan = personalDtoArbeitsplan.getCKurzzeichen();
		}

		wlaLetzteAenderungArbeitsplan.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.letzteaenderungimarbeitsplan")
				+ ": "
				+ Helper.formatTimestamp(
						stuecklisteDto.getTAendernarbeitsplan(), LPMain
								.getInstance().getTheClient().getLocUi())
				+ " "
				+ personalArbeitsplan);

		String personalPosition = "";
		PersonalDto personalDtoPosition = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						stuecklisteDto.getPersonalIIdAendernposition());
		if (personalDtoPosition.getCKurzzeichen() != null) {
			personalPosition = personalDtoPosition.getCKurzzeichen();
		}

		wlaLetzteAenderungPosition.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.letzteaenderungindenpositionen")
				+ ": "
				+ Helper.formatTimestamp(stuecklisteDto.getTAendernposition(),
						LPMain.getInstance().getTheClient().getLocUi())
				+ " "
				+ personalPosition);

		String text = "";

		if (stuecklisteDto.getTFreigabe() != null) {
			text = LPMain.getTextRespectUISPr("stkl.freigegebenam")
					+ " "
					+ Helper.formatDatumZeit(stuecklisteDto.getTFreigabe(),
							LPMain.getTheClient().getLocUi());
		}
		if (stuecklisteDto.getPersonalIIdFreigabe() != null) {
			text += "("
					+ DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									stuecklisteDto.getPersonalIIdFreigabe())
							.getCKurzzeichen() + ")";
		}

		wlaFreigabe.setText(text);

		this.setStatusbarPersonalIIdAendern(stuecklisteDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(stuecklisteDto.getTAendern());
		this.setStatusbarPersonalIIdAnlegen(stuecklisteDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(stuecklisteDto.getTAnlegen());

	}

	private void jbInit() throws Throwable {
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

		wbuLeitauftrag.setActionCommand(this.ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuLeitauftrag.addActionListener(this);
		wbuArtikel.setActionCommand(this.ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuArtikel.addActionListener(this);

		wsfLager = new WrapperSelectField(WrapperSelectField.LAGER,
				getInternalFrame(), true);

		wsfLager.setMandatoryField(true);
		wsfLager.setText(LPMain.getTextRespectUISPr("button.ziellager"));

		buttonGoto = new WrapperButton();
		buttonGoto.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/data_into.png")));
		buttonGoto.setActionCommand(ACTION_GOTO);
		buttonGoto.addActionListener(this);

		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		wtfArtikel.setMandatoryField(true);
		wtfFertigungsgruppe.setMandatoryField(true);
		wtfArtikel.setActivatable(false);
		wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wlaLetzteAenderungArbeitsplan
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLetzteAenderungPosition.setHorizontalAlignment(SwingConstants.LEFT);

		wcoStuecklisteart.setMandatoryField(true);

		wtfLeitauftrag.setActivatable(false);
		wtfKunde.setActivatable(false);
		wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikel"));

		wcbMaterialbuchungBeiAblieferung.setText(LPMain.getInstance()
				.getTextRespectUISPr("stk.materialbuchungbeiablieferung"));
		wcbUeberlieferbar.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.los.ueberlieferbar"));
		wcbDruckeInLagerstandsdetailauswertung
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"stk.stueckliste.druckeindetailsuwertung"));
		wcbUnterstuecklistenAusgeben.setText(LPMain.getInstance()
				.getTextRespectUISPr("stk.enthaltenestuecklistenausgeben"));

		wcbKeineAutomatischeMaterialbuchung.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.keineautomatischematerialbuchung"));

		wlaErfassungsfaktor.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.erfassungsfaktor"));

		wnfErfassungsfaktor.setMandatoryField(true);

		wlaDefaultdurchlaufzeitInTagen.setText(LPMain.getInstance()
				.getTextRespectUISPr("stkl.defaultdurchlaufzeit"));
		wnfDefaultdurchlaufzeitInTagen.setFractionDigits(0);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_DURCHLAUFZEIT_IST_PFLICHTFELD,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getInstance().getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			wnfDefaultdurchlaufzeitInTagen.setMandatoryField(true);
			wnfDefaultdurchlaufzeitInTagen.setMinimumValue(1);
		}

		wnfErfassungsfaktor.setFractionDigits(0);
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuLeitauftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.auftrag"));
		wcbFremdfertigung.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fremdfertigung"));
		wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fertigungsgruppe")
				+ "...");
		wbuFertigungsgruppe
				.setActionCommand(this.ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe.setActivatable(false);
		wlaTage.setText(LPMain.getInstance().getTextRespectUISPr("lp.tage"));
		wlaTage.setHorizontalAlignment(SwingConstants.LEFT);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.20, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 5, 1,
				0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLeitauftrag, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLeitauftrag, new GridBagConstraints(1, iZeile, 5,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 22), 0, 0));

		jpaWorkingOn.add(buttonGoto, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 10, 0));

		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 5, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaDefaultdurchlaufzeitInTagen,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wnfDefaultdurchlaufzeitInTagen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 50, 0));
		wlaTage.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaTage, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 80, 0));

		jpaWorkingOn.add(wlaErfassungsfaktor, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wnfErfassungsfaktor, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 50, 0));
		wlaErfassungsfaktorEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaErfassungsfaktorEinheit, new GridBagConstraints(5,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		iZeile++;

		jpaWorkingOn.add(wcoStuecklisteart, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wcbFremdfertigung, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wcbMaterialbuchungBeiAblieferung,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbUnterstuecklistenAusgeben, new GridBagConstraints(
				1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbUeberlieferbar, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbDruckeInLagerstandsdetailauswertung,
				new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbKeineAutomatischeMaterialbuchung,
				new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;

		jpaWorkingOn.add(wsfLager.getWrapperButton(), new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfLager.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));

		if (getInternalFramePersonal().getTabbedPaneStueckliste().iStrukturierterStklImport == 2) {
			iZeile++;

			wsfStueckliste.getWrapperButton().setText(
					LPMain.getInstance()
							.getTextRespectUISPr("stkl.ekvorschlag"));
			jpaWorkingOn.add(wsfStueckliste.getWrapperButton(),
					new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(wsfStueckliste.getWrapperTextField(),
					new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		iZeile++;

		jpaWorkingOn.add(wlaLetzteAenderungArbeitsplan, new GridBagConstraints(
				0, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaLetzteAenderungPosition, new GridBagConstraints(0,
				iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		getToolBar().addButtonLeft("/com/lp/client/res/data_out.png",
				LPMain.getTextRespectUISPr("stkl.kopfdaten.gotouebergeordnet"),
				MY_OWN_NEW_GOTO_UEBERGEORDNET, null, null);

		if (internalFrameStueckliste.getTabbedPaneStueckliste().bStuecklistenfreigabe == true) {

			boolean hatRecht = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_STK_FREIGABE_CUD);

			if (hatRecht) {

				getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("stkl.freigeben"),
						MY_OWN_NEW_TOGGLE_FREIGABE, null,
						RechteFac.RECHT_STK_FREIGABE_CUD);
			}
			getToolBar().getToolsPanelCenter().add(wlaFreigabe);
		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		stuecklisteDto = new StuecklisteDto();
		internalFrameStueckliste.setStuecklisteDto(stuecklisteDto);
		leereAlleFelder(this);
		wlaErfassungsfaktorEinheit.setText("");

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftragFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_GOTO)) {
			if (stuecklisteDto.getPartnerIId() != null
					&& LPMain
							.getInstance()
							.getDesktop()
							.darfAnwenderAufModulZugreifen(
									LocaleFac.BELEGART_KUNDE)) {

				KundeDto kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByiIdPartnercNrMandantOhneExc(
								stuecklisteDto.getPartnerIId(),
								LPMain.getTheClient().getMandant());
				if (kundeDto != null) {
					InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
							.getInstance().getDesktop()
							.holeModul(LocaleFac.BELEGART_KUNDE);
					ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
							TabbedPaneKunde.IDX_PANE_KUNDE, kundeDto.getIId(),
							null, PartnerFilterFactory.getInstance()
									.createFKPartnerKey(kundeDto.getIId()));
				}
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_GOTO_UEBERGEORDNET)) {
			HashMap hm = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.getAlleStuecklistenIIdsFuerVerwendungsnachweis(
							stuecklisteDto.getArtikelIId());

			if (hm.size() == 0) {
				// Meldung ausgeben
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("stkl.kopfdaten.gotouebergeordnet.keineuebergeordnetevorhanden"));
			} else if (hm.size() == 1) {
				// Direkt GOTO
				Integer stklIId = (Integer) hm.keySet().iterator().next();

				internalFrameStueckliste.geheZu(
						InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
						TabbedPaneStueckliste.IDX_PANEL_AUSWAHL, stklIId, null,
						StuecklisteFilterFactory.getInstance()
								.createFKStuecklisteKey((Integer) stklIId));

			} else {
				// Liste anzeigen

				String in = "(";

				Iterator it = hm.keySet().iterator();

				while (it.hasNext()) {
					Integer key = (Integer) it.next();

					in += key;

					if (it.hasNext()) {
						in += ",";
					}

				}

				in += ")";

				FilterKriterium[] kriterien = new FilterKriterium[1];
				kriterien[0] = new FilterKriterium("stueckliste.i_id", true,
						in, FilterKriterium.OPERATOR_IN, false);

				panelQueryFLRStueckliste = new PanelQueryFLR(
						null,
						kriterien,
						QueryParameters.UC_ID_STUECKLISTE,
						null,
						internalFrameStueckliste,
						LPMain.getTextRespectUISPr("title.stuecklisteauswahlliste"));

				panelQueryFLRStueckliste
						.befuellePanelFilterkriterienDirektUndVersteckte(
								StuecklisteFilterFactory.getInstance()
										.createFKDArtikelnummer(),
								ArtikelFilterFactory.getInstance()
										.createFKDVolltextsuche(),
								StuecklisteFilterFactory.getInstance()
										.createFKVStuecklisteArtikel());
				panelQueryFLRStueckliste
						.addDirektFilter(StuecklisteFilterFactory.getInstance()
								.createFKDErweiterteTextsuche());
				new DialogQuery(panelQueryFLRStueckliste);

			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_FREIGABE)) {
			// PJ 17558
			if (stuecklisteDto.getIId() != null) {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.toggleFreigabe(stuecklisteDto.getIId());
				stuecklisteDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(stuecklisteDto.getIId());
				internalFrameStueckliste.setStuecklisteDto(stuecklisteDto);
				eventYouAreSelected(false);
			}
		}

	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelOhneArbeitszeit(getInternalFrame(),
						stuecklisteDto.getArtikelIId(), false);
		new DialogQuery(panelQueryFLRArtikel);
	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, true);
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryFertigungsgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(),
						stuecklisteDto.getFertigungsgruppeIId(), false);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, true, "'"
						+ LPMain.getInstance().getTheClient().getMandant()
						+ "'", FilterKriterium.OPERATOR_EQUAL, false);

		FilterKriterium krit2 = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, true, "('"
						+ AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT + "','"
						+ AuftragServiceFac.AUFTRAGSTATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;

		QueryType[] querytypes = null;
		panelQueryFLRAuftrag = new PanelQueryFLR(querytypes, kriterien,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.auftragauswahlliste"));
		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());

		new DialogQuery(panelQueryFLRAuftrag);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	protected void setDefaults() throws Throwable {
		Map<?, ?> m = DelegateFactory.getInstance().getStuecklisteDelegate()
				.getAllStuecklisteart();
		wcoStuecklisteart.setMap(m);
	}

	protected void components2Dto() throws Throwable {
		stuecklisteDto.setBFremdfertigung(wcbFremdfertigung.getShort());
		stuecklisteDto
				.setBMaterialbuchungbeiablieferung(wcbMaterialbuchungBeiAblieferung
						.getShort());
		stuecklisteDto.setBUeberlieferbar(wcbUeberlieferbar.getShort());
		stuecklisteDto
				.setBDruckeinlagerstandsdetail(wcbDruckeInLagerstandsdetailauswertung
						.getShort());
		stuecklisteDto.setBAusgabeunterstueckliste(wcbUnterstuecklistenAusgeben
				.getShort());
		stuecklisteDto
				.setBKeineAutomatischeMaterialbuchung(wcbKeineAutomatischeMaterialbuchung
						.getShort());
		stuecklisteDto.setNDefaultdurchlaufzeit(wnfDefaultdurchlaufzeitInTagen
				.getBigDecimal());
		stuecklisteDto.setIErfassungsfaktor(wnfErfassungsfaktor.getInteger());
		stuecklisteDto.setStuecklisteartCNr((String) wcoStuecklisteart
				.getKeyOfSelectedItem());
		stuecklisteDto.setLagerIIdZiellager(wsfLager.getIKey());
		stuecklisteDto.setStuecklisteIIdEk(wsfStueckliste.getIKey());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeStueckliste(stuecklisteDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (stuecklisteDto.getIId() == null) {
				stuecklisteDto.setIId(DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.createStueckliste(stuecklisteDto));
				setKeyWhenDetailPanel(stuecklisteDto.getIId());
				stuecklisteDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(stuecklisteDto.getIId());

			} else {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.updateStueckliste(stuecklisteDto);
			}
			internalFrameStueckliste.setStuecklisteDto(stuecklisteDto);
			if (stuecklisteDto.getStuecklisteartCNr().equals(
					StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {

				ArtikelDto artikelTempDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId());

				if (Helper.short2boolean(artikelTempDto
						.getBLagerbewirtschaftet()) == true) {

					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"stkl.error.artikelistlagerbewirtschaftet"));
					if (b == true) {
						artikelTempDto.setBLagerbewirtschaftet(Helper
								.boolean2Short(false));
						artikelTempDto.setBSeriennrtragend(Helper
								.boolean2Short(false));
						artikelTempDto.setBChargennrtragend(Helper
								.boolean2Short(false));
						DelegateFactory.getInstance().getArtikelDelegate()
								.updateArtikel(artikelTempDto);
					}

				}

			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameStueckliste.getStuecklisteDto().getIId()
								.toString());
			}

			eventYouAreSelected(false);

			stuecklisteDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(stuecklisteDto.getIId());

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);

				artikelTempDto = DelegateFactory
						.getInstance()
						.getArtikelkommentarDelegate()
						.pruefeArtikel(artikelTempDto,
								LocaleFac.BELEGART_STUECKLISTE,
								getInternalFrame());
				if (artikelTempDto != null) {
					wtfArtikel.setText(artikelTempDto
							.formatArtikelbezeichnung());

					stuecklisteDto.setArtikelIId(artikelTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				AuftragDto auftragDto = null;
				auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(key);
				stuecklisteDto.setAuftragIIdLeitauftrag(auftragDto.getIId());

				String projBez = auftragDto.getCBezProjektbezeichnung();
				if (projBez == null) {
					projBez = "";
				}

				wtfLeitauftrag.setText(auftragDto.getCNr() + ", " + projBez);
			} else if (e.getSource() == panelQueryFLRStueckliste) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				internalFrameStueckliste.geheZu(
						InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
						TabbedPaneStueckliste.IDX_PANEL_AUSWAHL, key, null,
						StuecklisteFilterFactory.getInstance()
								.createFKStuecklisteKey((Integer) key));
				// ->GOTO
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(key);
				stuecklisteDto.setPartnerIId(kundeDto.getPartnerIId());
				wtfKunde.setText(kundeDto.getPartnerDto().formatAnrede());
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey(key);
				stuecklisteDto.setFertigungsgruppeIId(fertigungsgruppeDto
						.getIId());
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				wtfLeitauftrag.setText(null);
				stuecklisteDto.setAuftragIIdLeitauftrag(null);
			} else if (e.getSource() == panelQueryFLRKunde) {
				wtfKunde.setText(null);
				stuecklisteDto.setPartnerIId(null);
			}
		}

	}

}
