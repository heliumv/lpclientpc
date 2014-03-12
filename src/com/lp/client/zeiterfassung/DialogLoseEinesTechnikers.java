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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JDialog;

import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class DialogLoseEinesTechnikers extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryLose = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private InternalFrame internalFrame = null;
	private boolean bAufErledigteBuchbar = false;
	private Integer selectedId = null;
	private Integer personalIIdTechniker = null;

	private String FILTER_TECHNIKER = "FILTER_TECHNIKER";
	private String FILTER_TECHNIKER_AUS_KOPFDATEN = "FILTER_TECHNIKER_AUS_KOPFDATEN";
	private String FILTER_ALLE_LOSE = "FILTER_ALLE_LOSE";

	private WrapperComboBox wcbTechnikerfilter = new WrapperComboBox();

	public DialogLoseEinesTechnikers(InternalFrame internalFrame,
			boolean bAufErledigteBuchbar, Integer selectedId,
			Integer personalIIdTechniker) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("lp.auswahl"), true);
		this.internalFrame = internalFrame;
		this.bAufErledigteBuchbar = bAufErledigteBuchbar;
		this.selectedId = selectedId;
		this.personalIIdTechniker = personalIIdTechniker;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();

		panelQueryLose.updateButtons(new LockStateValue(
				PanelBasis.LOCK_IS_NOT_LOCKED));
		this.setSize(800, 500);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		FilterKriterium[] kriterien = FertigungFilterFactory.getInstance()
				.createFKBebuchbareLose(bAufErledigteBuchbar, true, false);

		kriterien = addFilter(kriterien, new FilterKriterium(
				"technikerset.personal_i_id", true, personalIIdTechniker + "",
				FilterKriterium.OPERATOR_EQUAL, false));

		panelQueryLose = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_LOS, null, internalFrame,
				LPMain.getTextRespectUISPr("lp.auswahl"), true);

		FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance()
				.createFKDLosnummer();
		FilterKriteriumDirekt fkDirekt2 = null;

		ParametermandantDto parameterAuftragStattbezeichnung = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_AUFTRAG_STATT_ARTIKEL_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameterAuftragStattbezeichnung.getCWertAsObject()) {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDProjektDesLoses();
		} else {
			fkDirekt2 = FertigungFilterFactory.getInstance()
					.createFKDArtikelnummer();
		}
		panelQueryLose.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosAuftagsnummer());
		panelQueryLose.addDirektFilter(FertigungFilterFactory.getInstance()
				.createFKDLosKunde());

		Map m = new LinkedHashMap();
		m.put(FILTER_TECHNIKER, LPMain.getTextRespectUISPr("fert.lostechniker"));

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_TECHNIKERAUSWAHL)) {

			m.put(FILTER_TECHNIKER_AUS_KOPFDATEN,
					LPMain.getTextRespectUISPr("button.techniker"));
			m.put(FILTER_ALLE_LOSE,
					LPMain.getTextRespectUISPr("fert.los.allelose"));
		}

		wcbTechnikerfilter.setMandatoryField(true);

		wcbTechnikerfilter.setMap(m);

		wcbTechnikerfilter.addActionListener(this);

		panelQueryLose.eventYouAreSelected(false);
		panelQueryLose.setSelectedId(selectedId);

		panelQueryLose.getPanelButtons().add(
				wcbTechnikerfilter,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
						new Insets(4, 0, 0, 40), 200, 0));

		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelQueryLose,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

	}

	private FilterKriterium[] addFilter(FilterKriterium[] array,
			FilterKriterium kriterium) {

		ArrayList al = new ArrayList();

		for (int i = 0; i < array.length; i++) {
			al.add(array[i]);
		}
		al.add(kriterium);

		FilterKriterium[] filters = new FilterKriterium[al.size()];
		for (int i = 0; i < al.size(); i++) {
			filters[i] = (FilterKriterium) al.get(i);
		}

		return filters;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		FilterKriterium[] kriterien = null;
		try {
			kriterien = FertigungFilterFactory.getInstance()
					.createFKBebuchbareLose(bAufErledigteBuchbar, true, false);

			if (wcbTechnikerfilter.getKeyOfSelectedItem().equals(
					FILTER_TECHNIKER)) {

				kriterien = addFilter(kriterien, new FilterKriterium(
						"technikerset.personal_i_id", true,
						personalIIdTechniker + "",
						FilterKriterium.OPERATOR_EQUAL, false));

			} else if (wcbTechnikerfilter.getKeyOfSelectedItem().equals(
					FILTER_TECHNIKER_AUS_KOPFDATEN)) {

				kriterien = addFilter(kriterien, new FilterKriterium(""
						+ FertigungFac.FLR_LOS_PERSONAL_I_ID_TECHNIKER, true,
						personalIIdTechniker + "",
						FilterKriterium.OPERATOR_EQUAL, false));
			}

		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		panelQueryLose.setDefaultFilter(kriterien);
		try {
			panelQueryLose.eventYouAreSelected(false);
			panelQueryLose.getHmOfButtons()
					.get(PanelQuery.LEAVEALONE_DOKUMENTE).getButton()
					.setVisible(false);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

	}

}
