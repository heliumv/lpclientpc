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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungDto;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster werden die Details der Lieferscheinposition zu einer
 * Auftragposition angezeigt.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch, 14. 04. 2005</p>
 *
 * <p>@author Uli Walch</p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2011/07/14 08:29:34 $
 */
public class PanelAuftragpositionInLieferschein extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private LieferscheinpositionDto oLieferscheinpositionDto = null;

	private WrapperGotoButton wbuRechnung = null;
	private WrapperTextField wtfRechnungcnr = null;
	private WrapperLabel wlaStatus = null;
	private WrapperLabel wlaStatusRechnung = null;
	
	private WrapperGotoButton wbuLieferschein = null;
	private WrapperTextField wtfLieferscheincnr = null;
	private WrapperLabel wlaStatus2 = null;
	private WrapperLabel wlaStatusLieferschein = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelAuftragpositionInLieferschein(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wbuRechnung = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("rechnung.modulname"),
				WrapperGotoButton.GOTO_RECHNUNG_AUSWAHL);
		wbuRechnung.setMaximumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wbuRechnung.setPreferredSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wbuRechnung.setMinimumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wtfRechnungcnr = new WrapperTextField();
		wtfRechnungcnr.setActivatable(false);
		wlaStatus = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.status"));
		wlaStatus.setMaximumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatus.setPreferredSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatus.setMinimumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatusRechnung = new WrapperLabel();
		wlaStatusRechnung.setHorizontalAlignment(SwingConstants.LEADING);
		
		
		wbuLieferschein = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("ls.modulname"),
				WrapperGotoButton.GOTO_LIEFERSCHEIN_AUSWAHL);
		wbuLieferschein.setMaximumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wbuLieferschein.setPreferredSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wbuLieferschein.setMinimumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wtfLieferscheincnr = new WrapperTextField();
		wtfLieferscheincnr.setActivatable(false);
		wlaStatus2 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.status"));
		wlaStatus2.setMaximumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatus2.setPreferredSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatus2.setMinimumSize(new Dimension(90, Defaults.getInstance()
				.getControlHeight()));
		wlaStatusLieferschein = new WrapperLabel();
		wlaStatusLieferschein.setHorizontalAlignment(SwingConstants.LEADING);
		
		

		// Zeile
		jPanelWorkingOn.add(wbuRechnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wtfRechnungcnr, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaStatus, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaStatusRechnung, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuLieferschein, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wtfLieferscheincnr, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaStatus2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaStatusLieferschein, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
	}

	private void dto2Components() throws Throwable {
		LieferscheinDto oLieferscheinDto = DelegateFactory
				.getInstance()
				.getLsDelegate()
				.lieferscheinFindByPrimaryKey(
						oLieferscheinpositionDto.getLieferscheinIId());

		if (oLieferscheinDto.getRechnungIId() != null) {
			RechnungDto oRechnungDto = DelegateFactory
					.getInstance()
					.getRechnungDelegate()
					.rechnungFindByPrimaryKey(oLieferscheinDto.getRechnungIId());
			wbuRechnung.setOKey(oLieferscheinDto.getRechnungIId());
			wtfRechnungcnr.setText(oRechnungDto.getCNr());
			wlaStatusRechnung.setText(oRechnungDto.getStatusCNr());
		} else {
			wbuRechnung.setOKey(null);
			resetPanel();
		}
		
		
			
			wbuLieferschein.setOKey(oLieferscheinDto.getIId());
			wtfLieferscheincnr.setText(oLieferscheinDto.getCNr());
			wlaStatusLieferschein.setText(oLieferscheinDto.getStatusCNr());
		
		
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// die Position neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel(); // key der Lieferscheinposition!

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			oLieferscheinpositionDto = DelegateFactory.getInstance()
					.getLieferscheinpositionDelegate()
					.lieferscheinpositionFindByPrimaryKey((Integer) oKey);

			dto2Components();
		}
		tpAuftrag.enablePanelsNachBitmuster();
		aktualisiereStatusbar();
		tpAuftrag.setTitleAuftrag(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auftragpositioninlieferschein"));
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	private void resetPanel() throws Throwable {
		leereAlleFelder(this);
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		tpAuftrag.enablePanelsNachBitmuster();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		tpAuftrag.enablePanelsNachBitmuster();
	}
}
