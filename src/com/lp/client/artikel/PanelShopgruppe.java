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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.ShopgruppesprDto;

@SuppressWarnings("static-access")
public class PanelShopgruppe extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;

	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private InternalFrameArtikel internalFrameArtikel = null;

	private WrapperButton wbuArtikel = new WrapperButton();
	private WrapperTextField wtfArtikel = new WrapperTextField();

	private WrapperSelectField wsfShopgruppe = new WrapperSelectField(
			WrapperSelectField.SHOPGRUPPE, getInternalFrame(), true);
	
	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private boolean bFuegeNeuePositionVorDerSelektiertenEin = false ;
	
	public PanelShopgruppe(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	/*
		panelQueryShopgruppe = new PanelQueryFLRArtikelShopgruppe(null,ArtikelFilterFactory.getInstance().createFKShopgruppeMandantCNr(null),
				QueryParameters.UC_ID_SHOPGRUPPE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"lp.shopgruppe"), true);
	 * 
	 * (non-Javadoc)
	 * @see com.lp.client.frame.component.PanelBasis#eventActionNew(java.util.EventObject, boolean, boolean)
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);		
		internalFrameArtikel.getTabbedPaneShopgruppe().setShopgruppeDto(
				new ShopgruppeDto());
		leereAlleFelder(this);
		setDefaults() ;
		
		// die neue Position soll vor der aktuell selektierten eingefuegt
		// werden
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}		
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.removeShopgruppe(
						internalFrameArtikel.getTabbedPaneShopgruppe()
								.getShopgruppeDto().getIId());
		super.eventActionDelete(e, false, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto() != null) {
			key = internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getIId();
		}

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			internalFrameArtikel.getTabbedPaneShopgruppe().setShopgruppeDto(
					DelegateFactory.getInstance().getArtikelDelegate()
							.shopgruppeFindByPrimaryKey((Integer) key));
			wtfKennung.setText(internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getCNr());

			wsfShopgruppe.setKey(internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getShopgruppeIId());

			if (internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getArtikelIId() != null) {
				ArtikelDto temp = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								internalFrameArtikel.getTabbedPaneShopgruppe()
										.getShopgruppeDto().getArtikelIId());
				wtfArtikel.setText(temp.getCNr());
			} else {
				wtfArtikel.setText(null);
			}

			if (internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getShopgruppesprDto() != null) {
				wtfBezeichnung.setText(internalFrameArtikel
						.getTabbedPaneShopgruppe().getShopgruppeDto()
						.getShopgruppesprDto().getCBez());
			}

			setStatusbarModification(internalFrameArtikel
					.getTabbedPaneShopgruppe().getShopgruppeDto());

			String sBezeichnung = "";
			if (internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getShopgruppesprDto() != null) {
				sBezeichnung = internalFrameArtikel.getTabbedPaneShopgruppe()
						.getShopgruppeDto().getShopgruppesprDto().getCBez()
						+ "";
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr("lp.shopgruppe")
							+ ": "
							+ internalFrameArtikel.getTabbedPaneShopgruppe()
									.getShopgruppeDto().getCNr()
							+ ", "
							+ LPMain.getInstance().getTextRespectUISPr(
									"label.bezeichnung") + ": " + sBezeichnung);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto temp = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfArtikel.setText(temp.getCNr());
				internalFrameArtikel.getTabbedPaneShopgruppe()
						.getShopgruppeDto().setArtikelIId(temp.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {
				wtfArtikel.setText(null);
				internalFrameArtikel.getTabbedPaneShopgruppe()
						.getShopgruppeDto().setArtikelIId(null);
			}
		}
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {

		if (internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto() != null
				&& internalFrameArtikel.getTabbedPaneShopgruppe()
						.getShopgruppeDto().getIId() != null) {
			panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelEinerShopgruppe(
							getInternalFrame(),
							internalFrameArtikel.getTabbedPaneShopgruppe()
									.getShopgruppeDto().getIId(),
							internalFrameArtikel.getTabbedPaneShopgruppe()
									.getShopgruppeDto().getArtikelIId(), true);
		}

		new DialogQuery(panelQueryFLRArtikel);
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.shopgruppe"));
		wtfKennung.setColumnsMax(ArtikelFac.MAX_SHOPGRUPPE_NAME);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);

		wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.shopgruppe.referenzartikel")
				+ "...");
		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuArtikel.addActionListener(this);
		wtfArtikel.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		wsfShopgruppe.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr("lp.vatershopgruppe")
						+ "...");

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));

		wtfBezeichnung.setText("");
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
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(3, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wsfShopgruppe.getWrapperButton(),
				new GridBagConstraints(0, 1, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfShopgruppe.getWrapperTextField(),
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, 2, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 2, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SHOPGRUPPE;
	}

	protected void components2Dto() {
		internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto()
				.setCNr(wtfKennung.getText());
		if (internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto()
				.getShopgruppesprDto() == null) {
			internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto()
					.setShopgruppesprDto(new ShopgruppesprDto());
		}
		internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto()
				.getShopgruppesprDto().setCBez(wtfBezeichnung.getText());
		internalFrameArtikel.getTabbedPaneShopgruppe().getShopgruppeDto()
				.setShopgruppeIId(wsfShopgruppe.getIKey());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (internalFrameArtikel.getTabbedPaneShopgruppe()
					.getShopgruppeDto().getIId() == null) {
				
				Integer previousId = null ;
				if(bFuegeNeuePositionVorDerSelektiertenEin) {
					PanelQuery queryShopgruppe = internalFrameArtikel.getTabbedPaneShopgruppe().getPanelQueryShopgruppe() ;
					int iPos = queryShopgruppe.getTable().getSelectedRow() ;
					previousId = (Integer) queryShopgruppe.getSelectedId() ;
				}

				// Create
				internalFrameArtikel
						.getTabbedPaneShopgruppe()
						.getShopgruppeDto()
						.setIId(DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.createShopgruppeVor(
										internalFrameArtikel
												.getTabbedPaneShopgruppe()
												.getShopgruppeDto(), previousId));
				
				bFuegeNeuePositionVorDerSelektiertenEin = false ;
				setKeyWhenDetailPanel(internalFrameArtikel
						.getTabbedPaneShopgruppe().getShopgruppeDto().getIId());
			} else {
				DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.updateShopgruppe(
								internalFrameArtikel.getTabbedPaneShopgruppe()
										.getShopgruppeDto());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getTabbedPaneShopgruppe()
								.getShopgruppeDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

}
