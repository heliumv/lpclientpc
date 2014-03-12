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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Ueberleitung des Bestellvorschlags in Belege.</I></p>
 * <p>Dieser Dialog wird aus den folgenden Modulen aufgerufen:</p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22.11.05</I></p>
 * <p> </p>
 * @version $Revision: 1.6 $
 */
public class PanelDialogKriterienBestellvorschlagUeberleitung2 extends
		PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ButtonGroup jbgKriterien = null;

	private WrapperRadioButton wrbBelegprolieferantprotermin = null;
	private WrapperRadioButton wrbBelegprolieferant = null;
	private WrapperRadioButton wrbBelegeinlieferanteintermin = null;
	private WrapperRadioButton wrbBelegeinlieferant = null;
	private WrapperRadioButton wrbAbrufeZuRahmen = null;

	private WrapperLabel wlaEmpty = null; // fuer die Formatierung
	private WrapperButton wbuBelegeinlieferanteinterminLieferant = null;
	private WrapperTextField wtfBelegeinlieferanteinterminLieferant = null;
	private LieferantDto belegeinlieferanteinterminLieferantDto = null;

	private WrapperButton wbuBelegeinlieferanteinterminTermin = null;
	private WrapperTextField wtfBelegeinlieferanteinterminTermin = null;
	private java.sql.Date tBestelltermin = null;

	private WrapperButton wbuBelegeinlieferant = null;
	private WrapperTextField wtfBelegeinlieferant = null;
	private LieferantDto belegeinlieferantLieferantDto = null;

	private WrapperButton wbuKostenstelle = null;
	private WrapperTextField wtfKostenstelle = null;
	private KostenstelleDto kostenstelleDto = null;

	private PanelQueryFLR panelQueryBelegeinlieferanteinterminLieferant = null;
	private PanelQueryFLR panelQueryBelegeinlieferanteinterminTermin = null;
	private PanelQueryFLR panelQueryBelegeinlieferant = null;
	private PanelQueryFLR panelQueryKostenstelle = null;

	private WrapperCheckBox wcbRichtpreisUebernehmen = null;

	private WrapperCheckBox wcbProjektklammerBeruecksichtigen = null;

	private boolean bAnfragevorschlag;

	private static final String ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINLIEFERANT = "action_special_belegeinlieferanteinterminlieferant";
	private static final String ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINTERMIN = "action_special_belegeinlieferanteintermintermin";
	private static final String ACTION_SPECIAL_FLR_BELEGEINLIEFERANT = "action_special_belegeinlieferant";
	private static final String ACTION_SPECIAL_FLR_KOSTENSTELLE = "action_special_flr_kostenstelle";

	static final private String ACTION_RADIOBUTTON_BELEGPROLIEFERANTPROTERMIN = "action_radiobutton_prolieferantprotermin";
	static final private String ACTION_RADIOBUTTON_BELEGPROLIEFERANT = "action_radiobutton_belegprolieferant";
	static final private String ACTION_RADIOBUTTON_BELEGEINLIEFERANTEINTERMIN = "action_radiobutton_belegeinlieferanteintermin";
	static final private String ACTION_RADIOBUTTON_BELEGEINLIEFERANT = "action_radiobutton_belegeinlieferant";
	static final private String ACTION_RADIOBUTTON_ABRUFEZURAHMEN = "action_radiobutton_abrufzurahmen";

	private BestellvorschlagUeberleitungKriterienDto kritDto = null;

	PersonalDto personalDto = null;

	public PanelDialogKriterienBestellvorschlagUeberleitung2(
			boolean bAnfragevorschlag, InternalFrame oInternalFrameI,
			String title) throws Throwable {
		super(oInternalFrameI, title);
		this.bAnfragevorschlag = bAnfragevorschlag;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);
		personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
		jbgKriterien = new ButtonGroup();

		if (bAnfragevorschlag) {
			wrbBelegprolieferantprotermin = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr("anf.radiobutton1"));
		} else {
			wrbBelegprolieferantprotermin = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr("bes.radiobutton1"));
		}
		wrbBelegprolieferantprotermin
				.setActionCommand(ACTION_RADIOBUTTON_BELEGPROLIEFERANTPROTERMIN);
		wrbBelegprolieferantprotermin.addActionListener(this);
		if (bAnfragevorschlag) {
			wrbBelegprolieferant = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("anf.radiobutton2"));
		} else {
			wrbBelegprolieferant = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("bes.radiobutton2"));
		}
		wrbBelegprolieferant
				.setActionCommand(ACTION_RADIOBUTTON_BELEGPROLIEFERANT);
		wrbBelegprolieferant.addActionListener(this);
		if (bAnfragevorschlag) {
			wrbBelegeinlieferanteintermin = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr("anf.radiobutton3"));
		} else {
			wrbBelegeinlieferanteintermin = new WrapperRadioButton(LPMain
					.getInstance().getTextRespectUISPr("bes.radiobutton3"));
		}
		wrbBelegeinlieferanteintermin
				.setActionCommand(ACTION_RADIOBUTTON_BELEGEINLIEFERANTEINTERMIN);
		wrbBelegeinlieferanteintermin.addActionListener(this);
		wrbAbrufeZuRahmen = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr("bes.radiobutton5"));
		wrbAbrufeZuRahmen.setActionCommand(ACTION_RADIOBUTTON_ABRUFEZURAHMEN);
		wrbAbrufeZuRahmen.addActionListener(this);

		wcbRichtpreisUebernehmen = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("anf.richtpreisuebernehmen"));
		wcbRichtpreisUebernehmen.setSelected(true);

		wcbProjektklammerBeruecksichtigen = new WrapperCheckBox(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellvorschlag.projektklammerberuecksichtigen"));

		wlaEmpty = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaEmpty, 15);
		wbuBelegeinlieferanteinterminLieferant = new WrapperButton(LPMain
				.getInstance().getTextRespectUISPr("label.lieferant"));
		wbuBelegeinlieferanteinterminLieferant
				.setActionCommand(ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINLIEFERANT);
		wbuBelegeinlieferanteinterminLieferant.addActionListener(this);
		wtfBelegeinlieferanteinterminLieferant = new WrapperTextField();
		wtfBelegeinlieferanteinterminLieferant.setEditable(false);
		wtfBelegeinlieferanteinterminLieferant
				.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuBelegeinlieferanteinterminTermin = new WrapperButton(LPMain
				.getInstance().getTextRespectUISPr("label.termin"));
		wbuBelegeinlieferanteinterminTermin
				.setActionCommand(ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINTERMIN);
		wbuBelegeinlieferanteinterminTermin.addActionListener(this);
		wtfBelegeinlieferanteinterminTermin = new WrapperTextField();
		wtfBelegeinlieferanteinterminTermin.setEditable(false);
		if (bAnfragevorschlag) {
			wrbBelegeinlieferant = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("anf.radiobutton4"));
		} else {
			wrbBelegeinlieferant = new WrapperRadioButton(LPMain.getInstance()
					.getTextRespectUISPr("bes.radiobutton4"));
		}
		wrbBelegeinlieferant
				.setActionCommand(ACTION_RADIOBUTTON_BELEGEINLIEFERANT);
		wrbBelegeinlieferant.addActionListener(this);

		wbuBelegeinlieferant = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferant"));
		wbuBelegeinlieferant
				.setActionCommand(ACTION_SPECIAL_FLR_BELEGEINLIEFERANT);
		wbuBelegeinlieferant.addActionListener(this);
		wtfBelegeinlieferant = new WrapperTextField();
		wtfBelegeinlieferant.setEditable(false);
		wtfBelegeinlieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuKostenstelle = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("label.kostenstelle"));
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_FLR_KOSTENSTELLE);
		wbuKostenstelle.addActionListener(this);
		HelperClient.setDefaultsToComponent(wbuKostenstelle, 120);
		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setMandatoryField(false);
		wtfKostenstelle.setEditable(false);

		jbgKriterien.add(wrbBelegprolieferantprotermin);
		jbgKriterien.add(wrbBelegprolieferant);
		jbgKriterien.add(wrbBelegeinlieferanteintermin);
		jbgKriterien.add(wrbBelegeinlieferant);
		jbgKriterien.add(wrbAbrufeZuRahmen);

		jpaWorkingOn.add(wrbBelegprolieferantprotermin, new GridBagConstraints(
				0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbBelegprolieferant, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbBelegeinlieferanteintermin, new GridBagConstraints(
				0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaEmpty, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuBelegeinlieferanteinterminLieferant,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBelegeinlieferanteinterminLieferant,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuBelegeinlieferanteinterminTermin,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBelegeinlieferanteinterminTermin,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbBelegeinlieferant, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuBelegeinlieferant, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBelegeinlieferant, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(2, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		if (!bAnfragevorschlag) {
			jpaWorkingOn.add(wrbAbrufeZuRahmen, new GridBagConstraints(0,
					iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		} else {
			jpaWorkingOn.add(wcbRichtpreisUebernehmen, new GridBagConstraints(
					0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		}
		iZeile++;
		boolean bProjektklammer = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER);
		if (bProjektklammer) {
			wcbProjektklammerBeruecksichtigen.setSelected(true);
			jpaWorkingOn.add(wcbProjektklammerBeruecksichtigen,
					new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(10, 2, 2, 2), 0, 0));
		}

	}

	private void setDefaults() throws Throwable {
		// per default wird 1 Beleg pro Lieferant mit all seinen Positionen
		// angelegt
		wrbBelegeinlieferant.setSelected(true);
		wtfBelegeinlieferant.setMandatoryField(true);

		wbuBelegeinlieferanteinterminLieferant.setEnabled(false);
		wbuBelegeinlieferanteinterminTermin.setEnabled(false);

		// default ist die Kostenstelle des angemeldeten Benutzers
		kostenstelleDto = personalDto.getKostenstelleDto_Stamm();
		wtfKostenstelle.setText(kostenstelleDto.getCBez());

		belegeinlieferanteinterminLieferantDto = new LieferantDto();
		belegeinlieferantLieferantDto = new LieferantDto();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINLIEFERANT)) {
			panelQueryBelegeinlieferanteinterminLieferant = BestellungFilterFactory
					.getInstance()
					.createPanelFLRBestellvorschlagAlleLieferanten(
							getInternalFrame());
			new DialogQuery(panelQueryBelegeinlieferanteinterminLieferant);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_BELEGEINLIEFERANT)) {
			panelQueryBelegeinlieferant = BestellungFilterFactory.getInstance()
					.createPanelFLRBestellvorschlagAlleLieferanten(
							getInternalFrame());
			new DialogQuery(panelQueryBelegeinlieferant);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FLR_BELEGEINLIEFERANTEINTERMINTERMIN)) {
			panelQueryBelegeinlieferanteinterminTermin = BestellungFilterFactory
					.getInstance()
					.createPanelFLRBestellvorschlagBelegeInLieferant(
							getInternalFrame());
			new DialogQuery(panelQueryBelegeinlieferanteinterminTermin);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KOSTENSTELLE)) {
			panelQueryKostenstelle = SystemFilterFactory.getInstance()
					.createPanelFLRKostenstelle(getInternalFrame(), false,
							true, kostenstelleDto.getIId());
			new DialogQuery(panelQueryKostenstelle);
		} else if (e.getActionCommand().equals(
				ACTION_RADIOBUTTON_BELEGPROLIEFERANTPROTERMIN)
				|| e.getActionCommand().equals(
						ACTION_RADIOBUTTON_BELEGPROLIEFERANT)) {
			wbuBelegeinlieferanteinterminLieferant.setEnabled(false);
			wtfBelegeinlieferanteinterminLieferant.setMandatoryField(false);
			wtfBelegeinlieferanteinterminLieferant.setText("");
			belegeinlieferanteinterminLieferantDto = new LieferantDto();

			wbuBelegeinlieferanteinterminTermin.setEnabled(false);
			wtfBelegeinlieferanteinterminTermin.setMandatoryField(false);
			wtfBelegeinlieferanteinterminTermin.setText("");
			tBestelltermin = null;

			wbuBelegeinlieferant.setEnabled(false);
			wtfBelegeinlieferant.setMandatoryField(false);
			wtfBelegeinlieferant.setText("");
			belegeinlieferantLieferantDto = new LieferantDto();

			if (kostenstelleDto.getIId() == null) {
				kostenstelleDto = personalDto.getKostenstelleDto_Stamm();
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
			}
		} else if (e.getActionCommand().equals(
				ACTION_RADIOBUTTON_BELEGEINLIEFERANTEINTERMIN)) {
			wbuBelegeinlieferanteinterminLieferant.setEnabled(true);
			wtfBelegeinlieferanteinterminLieferant.setMandatoryField(true);
			wtfBelegeinlieferanteinterminLieferant.setText("");
			belegeinlieferanteinterminLieferantDto = new LieferantDto();

			wbuBelegeinlieferanteinterminTermin.setEnabled(true);
			wtfBelegeinlieferanteinterminTermin.setMandatoryField(true);
			wtfBelegeinlieferanteinterminTermin.setText("");
			tBestelltermin = null;

			wbuBelegeinlieferant.setEnabled(false);
			wtfBelegeinlieferant.setMandatoryField(false);
			wtfBelegeinlieferant.setText("");
			belegeinlieferantLieferantDto = new LieferantDto();

			if (kostenstelleDto.getIId() == null) {
				kostenstelleDto = personalDto.getKostenstelleDto_Stamm();
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
			}
		} else if (e.getActionCommand().equals(
				ACTION_RADIOBUTTON_BELEGEINLIEFERANT)) {
			wbuBelegeinlieferanteinterminLieferant.setEnabled(false);
			wtfBelegeinlieferanteinterminLieferant.setMandatoryField(false);
			wtfBelegeinlieferanteinterminLieferant.setText("");
			belegeinlieferanteinterminLieferantDto = new LieferantDto();

			wbuBelegeinlieferanteinterminTermin.setEnabled(false);
			wtfBelegeinlieferanteinterminTermin.setMandatoryField(false);
			wtfBelegeinlieferanteinterminTermin.setText("");
			tBestelltermin = null;

			wbuBelegeinlieferant.setEnabled(true);
			wtfBelegeinlieferant.setMandatoryField(true);
			wtfBelegeinlieferant.setText("");
			belegeinlieferantLieferantDto = new LieferantDto();

			wbuKostenstelle.setEnabled(true);
			if (kostenstelleDto.getIId() == null) {
				kostenstelleDto = personalDto.getKostenstelleDto_Stamm();
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
			}
		} else if (e.getActionCommand().equals(
				ACTION_RADIOBUTTON_ABRUFEZURAHMEN)) {
			wbuBelegeinlieferanteinterminLieferant.setEnabled(false);
			wtfBelegeinlieferanteinterminLieferant.setMandatoryField(false);
			wtfBelegeinlieferanteinterminLieferant.setText("");
			belegeinlieferanteinterminLieferantDto = new LieferantDto();

			wbuBelegeinlieferanteinterminTermin.setEnabled(false);
			wtfBelegeinlieferanteinterminTermin.setMandatoryField(false);
			wtfBelegeinlieferanteinterminTermin.setText("");
			tBestelltermin = null;

			wbuBelegeinlieferant.setEnabled(false);
			wtfBelegeinlieferant.setMandatoryField(false);
			wtfBelegeinlieferant.setText("");
			belegeinlieferantLieferantDto = new LieferantDto();

			wbuKostenstelle.setEnabled(false);
			wtfKostenstelle.setText("");
			kostenstelleDto = new KostenstelleDto();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (allMandatoryFieldsSetDlg()) {
				buildBenutzerKriterien();

				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			super.eventActionSpecial(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryBelegeinlieferanteinterminTermin) {
				tBestelltermin = (java.sql.Date) ((ISourceEvent) e.getSource())
						.getIdSelected();
				wtfBelegeinlieferanteinterminTermin.setText(Helper.formatDatum(
						tBestelltermin, LPMain.getInstance().getTheClient()
								.getLocUi()));
			} else if (e.getSource() == panelQueryBelegeinlieferanteinterminLieferant) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				belegeinlieferanteinterminLieferantDto = DelegateFactory
						.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(iIdLieferant);
				wtfBelegeinlieferanteinterminLieferant
						.setText(belegeinlieferanteinterminLieferantDto
								.getPartnerDto().formatAnrede());
			} else if (e.getSource() == panelQueryBelegeinlieferant) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				belegeinlieferantLieferantDto = DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(iIdLieferant);
				wtfBelegeinlieferant.setText(belegeinlieferantLieferantDto
						.getPartnerDto().formatAnrede());
				// PJ14880
				DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.pruefeMindestbestellwert(iIdLieferant);
			} else if (e.getSource() == panelQueryKostenstelle) {
				Integer iIdKostenstelle = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey(iIdKostenstelle);
				wtfKostenstelle.setText(kostenstelleDto.getCBez());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryKostenstelle) {
				kostenstelleDto = new KostenstelleDto();
				wtfKostenstelle.setText("");
			}
		}
	}

	private BestellvorschlagUeberleitungKriterienDto buildBenutzerKriterien() {
		kritDto = new BestellvorschlagUeberleitungKriterienDto();

		kritDto.setBBelegprolieferantprotermin(wrbBelegprolieferantprotermin
				.isSelected());
		kritDto.setBBelegprolieferant(wrbBelegprolieferant.isSelected());
		kritDto.setBBelegeinlieferanteintermin(wrbBelegeinlieferanteintermin
				.isSelected());
		kritDto.setBBelegeinlieferant(wrbBelegeinlieferant.isSelected());
		kritDto.setBelegeinlieferanteinterminLieferantIId(belegeinlieferanteinterminLieferantDto
				.getIId());
		kritDto.setBelegeinlieferantLieferantIId(belegeinlieferantLieferantDto
				.getIId());
		kritDto.setTBelegeinlieferanteinterminTermin(tBestelltermin);
		kritDto.setKostenstelleIId(kostenstelleDto.getIId());
		kritDto.setBAbrufeZuRahmen(wrbAbrufeZuRahmen.isSelected());
		kritDto.setBRichtpreisUebernehmen(wcbRichtpreisUebernehmen.isSelected());
		kritDto.setBBeruecksichtigeProjektklammer(wcbProjektklammerBeruecksichtigen
				.isSelected());
		return kritDto;
	}

	public BestellvorschlagUeberleitungKriterienDto getBestellvorschlagUeberleitungKriterienDto() {
		return kritDto;
	}
}
