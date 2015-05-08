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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.SchwereDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelReklamationAnalyse extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReklamationDto reklamationDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaRuecksprache = new WrapperLabel();
	private WrapperDateField wdfRuecksprache = new WrapperDateField();

	private WrapperButton wbuPersonalRueckspreche = new WrapperButton();
	private WrapperButton wbuSchwere = new WrapperButton();

	private WrapperTextField wtfPersonalRueckspreche = new WrapperTextField();

	private WrapperLabel wlaKostenMaterial = new WrapperLabel();
	private WrapperLabel wlaKostenArbeitszeit = new WrapperLabel();

	private WrapperNumberField wnfKostenMaterial = new WrapperNumberField();
	private WrapperNumberField wnfKostenArbeitszeit = new WrapperNumberField();

	private WrapperLabel wlaRueckspracheMit = new WrapperLabel();
	private WrapperTextField wtfRueckspracheMit = new WrapperTextField();

	private WrapperTextField wtfSchwere = new WrapperTextField();

	private WrapperCheckBox wcbBerechtigt = new WrapperCheckBox();

	private WrapperEditorField wefBemerkung = null;

	private WrapperButton wbuFehler = new WrapperButton();

	private WrapperTextField wtfFehler = new WrapperTextField();

	private InternalFrameReklamation internalFrameReklamation = null;

	private PanelQueryFLR panelQueryFLRFehler = null;
	private PanelQueryFLR panelQueryFLRPersonalRuecksprache = null;
	private PanelQueryFLR panelQueryFLRSchwere = null;

	static final public String ACTION_SPECIAL_FEHLER_FROM_LISTE = "ACTION_SPECIAL_FEHLER_FROM_LISTE";
	static final public String ACTION_SPECIAL_PERSONAL_RUECKSPRACHE_FROM_LISTE = "ACTION_SPECIAL_PERSONAL_RUECKSPRACHE_FROM_LISTE";

	static final public String ACTION_SPECIAL_SCHWERE_FROM_LISTE = "ACTION_SPECIAL_SCHWERE_FROM_LISTE";

	public PanelReklamationAnalyse(InternalFrameReklamation internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameReklamation = internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_REKLAMATION;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfRuecksprache;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		reklamationDto = DelegateFactory
				.getInstance()
				.getReklamationDelegate()
				.reklamationFindByPrimaryKey(
						((InternalFrameReklamation) getInternalFrame())
								.getReklamationDto().getIId());
		dto2Components();
	}

	void dialogQueryPersonalRueckspracheFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRPersonalRuecksprache = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, true,
						reklamationDto.getPersonalIIdEingefuehrtkurz());
		new DialogQuery(panelQueryFLRPersonalRuecksprache);
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

		wbuSchwere.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.schwere")
				+ "...");
		wbuSchwere.setActionCommand(ACTION_SPECIAL_SCHWERE_FROM_LISTE);
		wbuSchwere.addActionListener(this);

		wbuFehler.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.fehler")
				+ "...");
		wbuFehler.setActionCommand(ACTION_SPECIAL_FEHLER_FROM_LISTE);
		wbuFehler.addActionListener(this);
		wtfFehler.setActivatable(false);

		wtfRueckspracheMit.setColumnsMax(80);

		wbuPersonalRueckspreche.setText(LPMain.getInstance()
				.getTextRespectUISPr("rekla.ruecksprachedurch"));
		wbuPersonalRueckspreche
				.setActionCommand(ACTION_SPECIAL_PERSONAL_RUECKSPRACHE_FROM_LISTE);
		wbuPersonalRueckspreche.addActionListener(this);
		wtfPersonalRueckspreche.setActivatable(false);

		wcbBerechtigt.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.berechtigt"));
		wlaRuecksprache.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.am"));
		wtfSchwere.setActivatable(false);

		wefBemerkung = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("rekla.analyse"));

		wlaKostenMaterial.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.kostenmaterial"));
		wlaKostenArbeitszeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.kostenarbeitszeit"));

		wlaRueckspracheMit.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.ruecksprachemit"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wefBemerkung, new GridBagConstraints(0, iZeile, 4, 1,
				0, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 180));
		iZeile++;
		jpaWorkingOn.add(wbuSchwere, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wtfSchwere, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));

		jpaWorkingOn.add(wcbBerechtigt, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuFehler, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFehler, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuPersonalRueckspreche, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalRueckspreche, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaRuecksprache, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfRuecksprache, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaRueckspracheMit, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wtfRueckspracheMit, new GridBagConstraints(1, iZeile,
				3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 10, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKostenMaterial, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKostenMaterial, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKostenArbeitszeit, new GridBagConstraints(2,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKostenArbeitszeit, new GridBagConstraints(3,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() throws Throwable {
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_RUECKSPRACHE_FROM_LISTE)) {
			dialogQueryPersonalRueckspracheFromListe(e);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FEHLER_FROM_LISTE)) {
			dialogQueryFehlerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SCHWERE_FROM_LISTE)) {
			dialogQuerySchwereFromListe(e);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		boolean b = internalFrameReklamation.getTabbedPaneReklamation()
				.pruefeObReklamationAenderbar();
		if (b == true) {
			super.eventActionUpdate(aE, false); // Buttons schalten
		} else {
			return;
		}
	}

	void dialogQueryFehlerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRFehler = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_FEHLER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"rekla.fehler"));
		panelQueryFLRFehler.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance()
						.createFKDBezeichnungMitAlias("fehler"), null);
		panelQueryFLRFehler.setSelectedId(reklamationDto.getFehlerIId());

		new DialogQuery(panelQueryFLRFehler);
	}

	void dialogQuerySchwereFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRSchwere = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_SCHWERE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"rekla.schwere"));
		panelQueryFLRSchwere.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null);
		panelQueryFLRSchwere.setSelectedId(reklamationDto.getSchwereIId());

		new DialogQuery(panelQueryFLRSchwere);
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wefBemerkung.setText(reklamationDto.getXAnalyse());

		wdfRuecksprache.setTimestamp(reklamationDto.getTRuecksprache());

		wtfRueckspracheMit.setText(reklamationDto.getCRuecksprachemit());

		wnfKostenArbeitszeit.setBigDecimal(reklamationDto
				.getNKostenarbeitszeit());
		wnfKostenMaterial.setBigDecimal(reklamationDto.getNKostenmaterial());

		wcbBerechtigt.setShort(reklamationDto.getBBerechtigt());
		if (reklamationDto.getSchwereIId() != null) {
			wtfSchwere.setText(DelegateFactory.getInstance()
					.getReklamationDelegate()
					.schwereFindByPrimaryKey(reklamationDto.getSchwereIId())
					.formatBezeichnung());
		}

		if (reklamationDto.getPersonalIIdRuecksprache() != null) {
			PersonalDto personalDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							reklamationDto.getPersonalIIdRuecksprache());
			wtfPersonalRueckspreche.setText(personalDto.formatAnrede());
		} else {
			wtfPersonalRueckspreche.setText(null);
		}

		if (reklamationDto.getFehlerIId() != null) {
			FehlerDto fehlerDto = DelegateFactory.getInstance()
					.getReklamationDelegate()
					.fehlerFindByPrimaryKey(reklamationDto.getFehlerIId());
			wtfFehler.setText(fehlerDto.getCBez());
		} else {
			wtfFehler.setText(null);
		}

		this.setStatusbarPersonalIIdAendern(reklamationDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(reklamationDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(reklamationDto.getTAnlegen());
		this.setStatusbarTAendern(reklamationDto.getTAendern());
	}

	protected void components2Dto() throws Throwable {
		reklamationDto.setNKostenarbeitszeit(wnfKostenArbeitszeit
				.getBigDecimal());
		reklamationDto.setNKostenmaterial(wnfKostenMaterial.getBigDecimal());
		reklamationDto.setBBerechtigt(wcbBerechtigt.getShort());
		reklamationDto.setCRuecksprachemit(wtfRueckspracheMit.getText());
		reklamationDto.setXAnalyse(wefBemerkung.getText());

		reklamationDto.setTRuecksprache(wdfRuecksprache.getTimestamp());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFehler) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				FehlerDto fehlerDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.fehlerFindByPrimaryKey((Integer) key);
				wtfFehler.setText(fehlerDto.getCBez());
				reklamationDto.setFehlerIId((Integer) key);

			} else if (e.getSource() == panelQueryFLRSchwere) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				SchwereDto schwereDto = DelegateFactory.getInstance()
						.getReklamationDelegate()
						.schwereFindByPrimaryKey((Integer) key);
				wtfSchwere.setText(schwereDto.formatBezeichnung());
				reklamationDto.setSchwereIId((Integer) key);

			}

			else if (e.getSource() == panelQueryFLRPersonalRuecksprache) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonalRueckspreche.setText(personalDto.formatAnrede());
					reklamationDto.setPersonalIIdRuecksprache((Integer) key);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPersonalRuecksprache) {
				wtfPersonalRueckspreche.setText(null);
				reklamationDto.setPersonalIIdRuecksprache(null);
			} else if (e.getSource() == panelQueryFLRSchwere) {
				wtfSchwere.setText(null);
				reklamationDto.setSchwereIId(null);
			} else if (e.getSource() == panelQueryFLRFehler) {
				wtfFehler.setText(null);
				reklamationDto.setFehlerIId(null);
			}

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getReklamationDelegate()
					.updateReklamation(reklamationDto);
		}
		super.eventActionSave(e, true);
	}

}
