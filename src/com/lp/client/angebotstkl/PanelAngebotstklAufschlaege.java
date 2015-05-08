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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.Helper;

public class PanelAngebotstklAufschlaege extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgstklDto agstklDto = null;

	private InternalFrameAngebotstkl internalFrameAngebotstkl = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	WrapperNumberField wnfNettosummeMaterial = new WrapperNumberField();
	WrapperNumberField wnfVKPreisMaterial = new WrapperNumberField();
	WrapperNumberField wnfNettosummeArbeitszeit = new WrapperNumberField();
	WrapperNumberField wnfVKPReisGesamt = new WrapperNumberField();

	AufschlagDto[] aufschlagMaterialDto = null;
	AufschlagDto[] aufschlagArbeitszeitDto = null;

	private HashMap<AufschlagDto, WrapperNumberField[]> hmAufschlaege = new HashMap<AufschlagDto, WrapperNumberField[]>();

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return internalFrameAngebotstkl;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public PanelAngebotstklAufschlaege(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameAngebotstkl = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			agstklDto = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.agstklFindByPrimaryKey(
							getInternalFrameAngebotstkl().getAgstklDto()
									.getIId());

			dto2Components();

			String cBez = "";
			
			if (getInternalFrameAngebotstkl().getAgstklDto().getKundeIId() != null) {

				KundeDto kundeDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(
								getInternalFrameAngebotstkl().getAgstklDto()
										.getKundeIId());

				cBez = kundeDto.getPartnerDto()
						.formatFixTitelName1Name2();
			}
			
			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				if (getInternalFrameAngebotstkl().getAgstklDto().getCBez() != null) {
					cBez +=" "+ getInternalFrameAngebotstkl().getAgstklDto()
							.getCBez();
				}
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getAgstklDto().getCNr()
							+ ", " + cBez);

		} else {
			leereAlleFelder(this);

		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		this.setStatusbarPersonalIIdAendern(agstklDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(agstklDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(agstklDto.getTAnlegen());
		this.setStatusbarTAendern(agstklDto.getTAendern());

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
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

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// Zeile Material
		jpaWorkingOn.add(new WrapperLabel("Nettosumme Material"),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						200, 0));

		wnfNettosummeMaterial.setActivatable(false);
		jpaWorkingOn.add(wnfNettosummeMaterial, new GridBagConstraints(3,
				iZeile, 1, 1, 0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		aufschlagMaterialDto = DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.aufschlagFindByBMaterial(
						getInternalFrameAngebotstkl().getAgstklDto().getIId(),
						Helper.boolean2Short(true));
		aufschlagArbeitszeitDto = DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.aufschlagFindByBMaterial(
						getInternalFrameAngebotstkl().getAgstklDto().getIId(),
						Helper.boolean2Short(false));

		iZeile++;

		for (int i = 0; i < aufschlagMaterialDto.length; i++) {
			jpaWorkingOn.add(
					new WrapperLabel(aufschlagMaterialDto[i].getCBez()),
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			WrapperNumberField wnfAufschlagProzent = new WrapperNumberField();
			wnfAufschlagProzent.setDouble(aufschlagMaterialDto[i]
					.getAgstklaufschlagDto().getFAufschlag());
			wnfAufschlagProzent.addKeyListener(this);
			jpaWorkingOn.add(wnfAufschlagProzent,
					new GridBagConstraints(1, iZeile, 1, 1, 0.35, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(new WrapperLabel("% ="), new GridBagConstraints(2,
					iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20,
					0));

			WrapperNumberField wnfAufschlagWert = new WrapperNumberField();

			wnfAufschlagWert.setActivatable(false);
			jpaWorkingOn.add(wnfAufschlagWert,
					new GridBagConstraints(3, iZeile, 1, 1, 0.35, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			WrapperNumberField[] wnfs = new WrapperNumberField[2];

			wnfs[0] = wnfAufschlagProzent;
			wnfs[1] = wnfAufschlagWert;

			hmAufschlaege.put(aufschlagMaterialDto[i], wnfs);

			iZeile++;
		}
		iZeile++;
		jpaWorkingOn
				.add(new WrapperLabel(
						"------------------------------------------------------------------------"),
						new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 200, 0));

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("Materialverkaufspreis"),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						200, 0));

		wnfVKPreisMaterial.setActivatable(false);
		jpaWorkingOn.add(wnfVKPreisMaterial, new GridBagConstraints(3, iZeile,
				1, 1, 0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("Arbeitszeit"),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						200, 0));

		wnfNettosummeArbeitszeit.setActivatable(false);

		jpaWorkingOn.add(wnfNettosummeArbeitszeit, new GridBagConstraints(3,
				iZeile, 1, 1, 0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		for (int i = 0; i < aufschlagArbeitszeitDto.length; i++) {
			jpaWorkingOn.add(
					new WrapperLabel(aufschlagArbeitszeitDto[i].getCBez()),
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			WrapperNumberField wnfAufschlagProzent = new WrapperNumberField();
			wnfAufschlagProzent.setDouble(aufschlagArbeitszeitDto[i]
					.getAgstklaufschlagDto().getFAufschlag());
			wnfAufschlagProzent.addKeyListener(this);

			jpaWorkingOn.add(wnfAufschlagProzent,
					new GridBagConstraints(1, iZeile, 1, 1, 0.35, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(new WrapperLabel("% ="), new GridBagConstraints(2,
					iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20,
					0));

			WrapperNumberField wnfAufschlagWert = new WrapperNumberField();

			wnfAufschlagWert.setActivatable(false);
			jpaWorkingOn.add(wnfAufschlagWert,
					new GridBagConstraints(3, iZeile, 1, 1, 0.35, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			WrapperNumberField[] wnfs = new WrapperNumberField[2];

			wnfs[0] = wnfAufschlagProzent;
			wnfs[1] = wnfAufschlagWert;

			hmAufschlaege.put(aufschlagArbeitszeitDto[i], wnfs);

			iZeile++;
		}

		jpaWorkingOn
				.add(new WrapperLabel(
						"------------------------------------------------------------------------"),
						new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 200, 0));
		iZeile++;
		jpaWorkingOn.add(new WrapperLabel("Verkaufspreis Gesamt"),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						200, 0));

		wnfVKPReisGesamt.setActivatable(false);
		jpaWorkingOn.add(wnfVKPReisGesamt, new GridBagConstraints(3, iZeile, 1,
				1, 0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		prozentGeaendertPreiseNeuRechnen();

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		agstklDto = new AgstklDto();

		getInternalFrameAngebotstkl().setAgstklDto(agstklDto);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		int z = 0;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AGSTKL;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws ExceptionLP {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate()
				.removeAgstkl(agstklDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.updateAgstklaufschlag(agstklDto.getIId(),
							aufschlagMaterialDto);
			DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.updateAgstklaufschlag(agstklDto.getIId(),
							aufschlagArbeitszeitDto);
			setKeyWhenDetailPanel(agstklDto.getIId());

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						agstklDto.getIId().toString());
			}

			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	protected void eventKeyPressed(KeyEvent e) throws Throwable {

	}

	protected void eventKeyTyped(KeyEvent e) throws Throwable {

	}

	protected void eventKeyReleased(KeyEvent e) throws Throwable {
		prozentGeaendertPreiseNeuRechnen();
	}

	private void prozentGeaendertPreiseNeuRechnen() throws Throwable {

		BigDecimal[] nWerte = DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.berechneAgstklMaterialwertUndArbeitszeitwert(
						internalFrameAngebotstkl.getAgstklDto().getIId());
		wnfNettosummeMaterial.setBigDecimal(nWerte[0]);
		// Material
		BigDecimal bdMaterialnetto = wnfNettosummeMaterial.getBigDecimal();

		BigDecimal aufschlaegeMaterial = new BigDecimal(0);
		for (int i = 0; i < aufschlagMaterialDto.length; i++) {
			if (hmAufschlaege.containsKey(aufschlagMaterialDto[i])) {
				WrapperNumberField[] wnfs = hmAufschlaege
						.get(aufschlagMaterialDto[i]);

				BigDecimal bdAufschlag = wnfs[0].getBigDecimal();

				if (bdAufschlag != null) {
					aufschlagMaterialDto[i].getAgstklaufschlagDto()
							.setFAufschlag(bdAufschlag.doubleValue());
					wnfs[1].setBigDecimal(Helper.getProzentWert(
							bdMaterialnetto, bdAufschlag, 2));

					aufschlaegeMaterial = aufschlaegeMaterial.add(Helper
							.getProzentWert(bdMaterialnetto, bdAufschlag, 2));
				} else {
					wnfs[1].setBigDecimal(new BigDecimal(0));
				}

			}
		}

		wnfVKPreisMaterial.setBigDecimal(bdMaterialnetto
				.add(aufschlaegeMaterial));

		// AZ
		wnfNettosummeArbeitszeit.setBigDecimal(nWerte[1]);
		BigDecimal nettosummeArbeitszeit = wnfNettosummeArbeitszeit
				.getBigDecimal();
		BigDecimal aufschlaegeAZ = new BigDecimal(0);
		for (int i = 0; i < aufschlagArbeitszeitDto.length; i++) {
			if (hmAufschlaege.containsKey(aufschlagArbeitszeitDto[i])) {
				WrapperNumberField[] wnfs = hmAufschlaege
						.get(aufschlagArbeitszeitDto[i]);

				BigDecimal bdAufschlag = wnfs[0].getBigDecimal();

				if (bdAufschlag != null) {
					aufschlagArbeitszeitDto[i].getAgstklaufschlagDto()
							.setFAufschlag(bdAufschlag.doubleValue());
					wnfs[1].setBigDecimal(Helper.getProzentWert(
							nettosummeArbeitszeit, bdAufschlag, 2));

					aufschlaegeAZ = aufschlaegeAZ.add(Helper.getProzentWert(
							nettosummeArbeitszeit, bdAufschlag, 2));
				} else {
					wnfs[1].setBigDecimal(new BigDecimal(0));
				}

			}
		}

		wnfVKPReisGesamt.setBigDecimal(wnfVKPreisMaterial.getBigDecimal().add(
				nettosummeArbeitszeit.add(aufschlaegeAZ)));

	}

}
