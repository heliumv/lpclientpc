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
package com.lp.client.inserat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.system.service.LocaleFac;

public class PanelInseratArtikel extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameInserat internalFrameInserat = null;
	private InseratartikelDto inseratartikelDto = null;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaPreisEK = new WrapperLabel();
	private WrapperNumberField wnfPreisEK = new WrapperNumberField();

	private WrapperLabel wlaPreisVK = new WrapperLabel();
	private WrapperNumberField wnfPreisVK = new WrapperNumberField();
	private WrapperIdentField wifArtikel = null;

	public PanelInseratArtikel(InternalFrameInserat internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.internalFrameInserat = internalFrame;

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected void setDefaults() {

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
		} else {
			inseratartikelDto = DelegateFactory.getInstance()
					.getInseratDelegate()
					.inseratartikelFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
				.getIId() != null) {
			if (internalFrameInserat.getTabbedPaneInserat().getInseratDto()
					.getStatusCNr().equals(LocaleFac.STATUS_BESTELLT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDUPDATE_ONLY);
			} else if (internalFrameInserat.getTabbedPaneInserat()
					.getInseratDto().getStatusCNr()
					.equals(LocaleFac.STATUS_VERRECHNET)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_TEILBEZAHLT)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_BEZAHLT)
					|| internalFrameInserat.getTabbedPaneInserat()
							.getInseratDto().getStatusCNr()
							.equals(LocaleFac.STATUS_ERLEDIGT)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (internalFrameInserat.getTabbedPaneInserat()
				.darfInseratGeaendertWerden()) {
			super.eventActionNew(eventObject, true, false);
			leereAlleFelder(this);
			inseratartikelDto = new InseratartikelDto();
		}
	}

	protected void dto2Components() throws Throwable {

		ArtikelDto artikelDto = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(inseratartikelDto.getArtikelIId());
		wifArtikel.setArtikelDto(artikelDto);

		wnfMenge.setBigDecimal(inseratartikelDto.getNMenge());
		wnfPreisEK.setBigDecimal(inseratartikelDto.getNNettoeinzelpreisEk());
		wnfPreisVK.setBigDecimal(inseratartikelDto.getNNettoeinzelpreisVk());

	}

	protected void components2Dto() throws Throwable {
		inseratartikelDto.setInseratIId(internalFrameInserat
				.getTabbedPaneInserat().getInseratDto().getIId());

		inseratartikelDto.setNMenge(wnfMenge.getBigDecimal());
		inseratartikelDto.setNNettoeinzelpreisEk(wnfPreisEK.getBigDecimal());
		inseratartikelDto.setNNettoeinzelpreisVk(wnfPreisVK.getBigDecimal());
		inseratartikelDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);
		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaPreisEK.setText(LPMain
				.getTextRespectUISPr("iv.inseratartikel.preisek"));
		wlaPreisVK.setText(LPMain
				.getTextRespectUISPr("iv.inseratartikel.preisvk"));
		wnfPreisEK.setMandatoryField(true);
		wnfPreisVK.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);
		
		wnfPreisEK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfPreisVK.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		iZeile++;
		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 1, 1, 2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPreisEK, new GridBagConstraints(0, iZeile, 1, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfPreisEK, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung()), new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPreisVK, new GridBagConstraints(0, iZeile, 1, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfPreisVK, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTheClient()
				.getSMandantenwaehrung()), new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INSERAT;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {

		DelegateFactory.getInstance().getInseratDelegate()
				.removeInseratartikel(inseratartikelDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (inseratartikelDto.getIId() == null) {

				inseratartikelDto.setIId(DelegateFactory.getInstance()
						.getInseratDelegate()
						.createInseratartikel(inseratartikelDto));

				setKeyWhenDetailPanel(inseratartikelDto.getIId());
			} else {
				DelegateFactory.getInstance().getInseratDelegate()
						.updateInseratartikel(inseratartikelDto);

			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						inseratartikelDto.getInseratIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

}
