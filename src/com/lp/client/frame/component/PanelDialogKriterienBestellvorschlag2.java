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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer den Bestellvorschlag.</I></p>
 * <p>Dieser Dialog wird aus den folgenden Modulen aufgerufen:</p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22.11.05</I></p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelDialogKriterienBestellvorschlag2 extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVorlaufZeit = null;
	private WrapperNumberField wnfVorlaufZeit = null;

	private WrapperLabel wlaToleranz = null;
	private WrapperNumberField wnfToleranz = null;

	public boolean isbMitNichtlagerbeweirtschaftetenArtikeln() {
		return wcbMitNichtLagerwewirtschafteten.isSelected();
	}

	public boolean isbVormerklisteLoeschen() {
		return wcbVormerklisteLoeschen.isSelected();
	}

	public boolean isbNurBetroffeneLospositionen() {
		return wcbNurBetroffeneLospositionen.isSelected();
	}

	private WrapperLabel wlaLieferterminfuerArtikelOhneReservierung = null;
	private WrapperDateField wdfLieferterminfuerArtikelOhneReservierung = null;

	private WrapperLabel wlaVorhandeneBestellvorschlagEintrageLoeschen = null;
	private WrapperCheckBox wcbVorhandeneBestellvorschlagEintrageLoeschen = null;
	private WrapperCheckBox wcbMitNichtLagerwewirtschafteten = null;
	private WrapperCheckBox wcbNurBetroffeneLospositionen = null;
	private WrapperCheckBox wcbVormerklisteLoeschen = null;

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();

	private static final String ACTION_SPECIAL_LEEREN = "action_special_leeren";
	private ImageIcon imageIconLeeren = null;

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_auftrag_liste";

	private ArrayList<Integer> losIId = new ArrayList<Integer>();
	private ArrayList<Integer> auftragIId = new ArrayList<Integer>();

	public ArrayList<Integer> getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(ArrayList<Integer> auftragIId) {
		this.auftragIId = auftragIId;
	}

	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	public PanelDialogKriterienBestellvorschlag2(InternalFrame oInternalFrameI,
			String title) throws HeadlessException, Throwable {
		super(oInternalFrameI, title);

		try {
			jbInit();
			setDefaults();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {
		wlaVorlaufZeit = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("bes.vorlaufzeiten"));
		wlaToleranz = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("bes.toleranz"));

		wnfVorlaufZeit = new WrapperNumberField();
		wnfVorlaufZeit.setMandatoryField(true);
		wnfVorlaufZeit.setMaximumIntegerDigits(3);
		wnfVorlaufZeit.setFractionDigits(0);
		wnfVorlaufZeit.setMinimumValue(1);
		HelperClient.setDefaultsToComponent(wnfVorlaufZeit, 120);

		wnfToleranz = new WrapperNumberField();
		wnfToleranz.setMandatoryField(true);
		wnfToleranz.setMaximumIntegerDigits(3);
		wnfToleranz.setFractionDigits(0);
		wnfToleranz.setMinimumValue(0);
		HelperClient.setDefaultsToComponent(wnfToleranz, 120);

		wlaLieferterminfuerArtikelOhneReservierung = new WrapperLabel();
		wlaLieferterminfuerArtikelOhneReservierung.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"bes.lieferdatumfuerartikelohnereservierung"));
		wdfLieferterminfuerArtikelOhneReservierung = new WrapperDateField();

		wlaVorhandeneBestellvorschlagEintrageLoeschen = new WrapperLabel();
		wlaVorhandeneBestellvorschlagEintrageLoeschen.setText(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellvorschlagloeschen"));
		wcbVorhandeneBestellvorschlagEintrageLoeschen = new WrapperCheckBox();
		wcbVorhandeneBestellvorschlagEintrageLoeschen.setActivatable(false);
		wcbVorhandeneBestellvorschlagEintrageLoeschen.setEnabled(false);
		wcbVorhandeneBestellvorschlagEintrageLoeschen.setSelected(true);

		wcbMitNichtLagerwewirtschafteten = new WrapperCheckBox(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellvorschlag.mitnichtlagerbewirtschafteten"));

		wcbVormerklisteLoeschen = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("bes.vormerklisteLoeschen"));

		wcbNurBetroffeneLospositionen = new WrapperCheckBox(LPMain
				.getInstance().getTextRespectUISPr(
						"bes.bestellvorschlag.nurbetroffenelospositionen"));

		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.lose")
				+ "...");

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.modulname.tooltip")
				+ "...");

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		// wtfLos.setActivatable(false);
		wtfAuftrag.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

		JButton jbuSetNull = new JButton();
		jbuSetNull.setActionCommand(ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);
		jbuSetNull.setIcon(getImageIconLeeren());
		jbuSetNull
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		jbuSetNull
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		iZeile++;
		jpaWorkingOn.add(wlaVorlaufZeit, new GridBagConstraints(1, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfVorlaufZeit, new GridBagConstraints(2, iZeile, 1,
				1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaToleranz, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfToleranz, new GridBagConstraints(2, iZeile, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferterminfuerArtikelOhneReservierung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfLieferterminfuerArtikelOhneReservierung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaVorhandeneBestellvorschlagEintrageLoeschen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbVorhandeneBestellvorschlagEintrageLoeschen,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(jbuSetNull, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbNurBetroffeneLospositionen, new GridBagConstraints(
				1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitNichtLagerwewirtschafteten,
				new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVormerklisteLoeschen, new GridBagConstraints(1,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {
		// den Vorschlagswert fuer die Auftragsvorlaufdauer bestimmen
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_AUFTRAG);
		wnfVorlaufZeit.setInteger((Integer) parametermandantDto
				.getCWertAsObject());
		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.TOLERANZFRIST_BESTELLVORSCHLAG);
		wnfToleranz
				.setInteger((Integer) parametermandantDto.getCWertAsObject());
		wdfLieferterminfuerArtikelOhneReservierung.setDate(new Date(System
				.currentTimeMillis()));
		wcbVorhandeneBestellvorschlagEintrageLoeschen.setSelected(true);
	}

	public Integer getAuftragsvorlaufzeit() throws Throwable {
		return wnfVorlaufZeit.getInteger();
	}

	public Integer getToleranz() throws ExceptionLP {
		return wnfToleranz.getInteger();
	}

	public Date getLieferterminFuerArtikelOhneReservierung() {
		return wdfLieferterminfuerArtikelOhneReservierung.getDate();
	}

	public boolean getVorhandeneBestellvorschlagEintrageLoeschen() {
		return wcbVorhandeneBestellvorschlagEintrageLoeschen.isSelected();
	}

	private ImageIcon getImageIconLeeren() {
		if (imageIconLeeren == null) {
			imageIconLeeren = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/leeren.png"));
		}
		return imageIconLeeren;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
					.createFKPanelQueryFLRAuftragAuswahl(false);
			panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
					.createPanelFLRAuftrag(getInternalFrame(), true, true, fk);
			panelQueryFLRAuftrag.setMultipleRowSelectionEnabled(true);
			new DialogQuery(panelQueryFLRAuftrag);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LEEREN)) {
			wtfLos.setText(null);
			losIId = new ArrayList<Integer>();
		} else {
			super.eventActionSpecial(e);
		}

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRBebuchbareLose(getInternalFrame(), false, true,
						true, null, false);
		panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
		new DialogQuery(panelQueryFLRLos);

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object[] o = panelQueryFLRLos.getSelectedIds();

				String lose = "";

				if (wtfLos.getText() != null) {
					lose = wtfLos.getText();
				}
				for (int i = 0; i < o.length; i++) {
					LosDto losDto = DelegateFactory.getInstance()
							.getFertigungDelegate()
							.losFindByPrimaryKey((Integer) o[i]);
					lose += losDto.getCNr() + ", ";

					losIId.add(losDto.getIId());

				}
				wtfLos.setText(lose);
				if (panelQueryFLRLos.dialog != null) {
					panelQueryFLRLos.dialog.setVisible(false);
				}
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object[] o = panelQueryFLRAuftrag.getSelectedIds();

				String auftraege = "";
				
				if (wtfAuftrag.getText() != null) {
					auftraege = wtfAuftrag.getText();
				}
				for (int i = 0; i < o.length; i++) {
					AuftragDto losDto = DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey((Integer) o[i]);
					auftraege += losDto.getCNr() + ", ";

					auftragIId.add(losDto.getIId());

				}
				wtfAuftrag.setText(auftraege);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = new ArrayList<Integer>();
				wtfAuftrag.setText("");
			}
		}
	}

	public ArrayList<Integer> getLosIId() {
		return this.losIId;
	}
}
