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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JComponent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Interne Bestellung.</I></p>
 * <p>Dieser Dialog wird aus den folgenden Modulen aufgerufen:</p>
 * <ul>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22.11.05</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelDialogKriterienInternebestellung extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVorlaufZeit = null;
	private WrapperNumberField wnfVorlaufZeit = null;

	private WrapperLabel wlaToleranz = null;
	private WrapperNumberField wnfToleranz = null;

	private WrapperLabel wlaLieferterminfuerArtikelOhneReservierung = null;
	private WrapperDateField wdfLieferterminfuerArtikelOhneReservierung = null;

	private WrapperCheckBox wcbVorhandeneInterneBestellungEintrageLoeschen = null;
	private WrapperCheckBox wcbVerdichten = null;

	private WrapperLabel wlaVerdichtungstage = null;
	private WrapperNumberField wnfVerdichtungstage = null;

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();
	private Integer losIId = null;

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	private PanelQueryFLR panelQueryFLRLos = null;

	public PanelDialogKriterienInternebestellung(InternalFrame oInternalFrameI,
			String title) throws HeadlessException, Throwable {
		super(oInternalFrameI, title);
		jbInitPanel();
		setDefaults();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		wlaVorlaufZeit = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.vorlaufzeiten"));
		wlaToleranz = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("bes.toleranz"));
		wnfVorlaufZeit = new WrapperNumberField();
		wnfVorlaufZeit.setMandatoryField(true);
		wnfVorlaufZeit.setMaximumIntegerDigits(3);
		wnfVorlaufZeit.setFractionDigits(0);
		wnfVorlaufZeit.setMinimumValue(1);

		wlaVerdichtungstage = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.tage"));
		wnfVerdichtungstage = new WrapperNumberField();
		wnfVerdichtungstage.setMaximumIntegerDigits(3);
		wnfVerdichtungstage.setFractionDigits(0);
		wnfVerdichtungstage.setMinimumValue(1);

		wnfToleranz = new WrapperNumberField();
		wnfToleranz.setMandatoryField(true);
		wnfToleranz.setMaximumIntegerDigits(3);
		wnfToleranz.setFractionDigits(0);
		wnfToleranz.setMinimumValue(0);
		HelperClient.setDefaultsToComponent(wnfToleranz, 120);

		HelperClient.setDefaultsToComponent(wnfVorlaufZeit, 120);

		wlaLieferterminfuerArtikelOhneReservierung = new WrapperLabel();
		wlaLieferterminfuerArtikelOhneReservierung
				.setText(LPMain
						.getTextRespectUISPr("bes.lieferdatumfuerartikelohnereservierung"));
		wdfLieferterminfuerArtikelOhneReservierung = new WrapperDateField();
		wdfLieferterminfuerArtikelOhneReservierung.setMandatoryField(true);

		wcbVorhandeneInterneBestellungEintrageLoeschen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fert.internebestellungloeschen"));

		wcbVerdichten = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.verdichten"));

		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr(
				"fert.tab.unten.los.title")
				+ "...");

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		// wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfLos.setActivatable(false);

		iZeile++;
		jpaWorkingOn.add(wlaVorlaufZeit, new GridBagConstraints(0, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfVorlaufZeit, new GridBagConstraints(1, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaToleranz, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfToleranz, new GridBagConstraints(1, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferterminfuerArtikelOhneReservierung,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLieferterminfuerArtikelOhneReservierung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVorhandeneInterneBestellungEintrageLoeschen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVerdichten, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVerdichtungstage, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerdichtungstage, new GridBagConstraints(1, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile,
				1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRBebuchbareLose(getInternalFrame(), false, true,
						true, null, true);
		
		new DialogQuery(panelQueryFLRLos);

	}

	private void setDefaults() throws Throwable {
		// den Vorschlagswert fuer die Auftragsvorlaufdauer bestimmen
		ParametermandantDto parameterVorlaufzeit = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG);
		wnfVorlaufZeit.setInteger((Integer) parameterVorlaufzeit
				.getCWertAsObject());
		wdfLieferterminfuerArtikelOhneReservierung.setDate(new Date(System
				.currentTimeMillis()));
		wcbVorhandeneInterneBestellungEintrageLoeschen.setSelected(false);

		// den Vorschlagswert fuer die Verdichtungstage bestimmen
		ParametermandantDto parameterVerdichtungstage = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM);
		wnfVerdichtungstage.setInteger((Integer) parameterVerdichtungstage
				.getCWertAsObject());
		ParametermandantDto parameterToleranz = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.TOLERANZFRIST_INTERNE_BESTELLUNG);
		wnfToleranz.setInteger((Integer) parameterToleranz.getCWertAsObject());

	}

	public Integer getAuftragsvorlaufzeit() throws Throwable {
		return wnfVorlaufZeit.getInteger();
	}

	public Integer getVerdichtungstage() throws Throwable {
		return wnfVerdichtungstage.getInteger();
	}

	public Date getLieferterminFuerArtikelOhneReservierung() {
		return wdfLieferterminfuerArtikelOhneReservierung.getDate();
	}

	public boolean getVorhandeneLoeschen() {
		return wcbVorhandeneInterneBestellungEintrageLoeschen.isSelected();
	}

	public boolean getBVerdichten() {
		return wcbVerdichten.isSelected();
	}

	public Integer getToleranz() throws ExceptionLP {
		return wnfToleranz.getInteger();
	}
	public Integer getLosIId(){
		return losIId;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfVorlaufZeit;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (!allMandatoryFieldsSetDlg()) {
				return;
			}
		}
		// den allgemeine Behandlung.
		super.eventActionSpecial(e);
	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object o = panelQueryFLRLos.getSelectedId();

				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey((Integer) o);

				losIId = losDto.getIId();

				wtfLos.setText(losDto.getCNr());
			}
		}else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLos) {
				losIId=null;
				wtfLos.setText(null);
			}
		}
			
	}

}
