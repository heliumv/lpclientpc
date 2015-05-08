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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um das Panel zur Erfassung von Preislisten. <br>
 * Preislisten sind mandantenabhaengig. <br>
 * Es koennen maximal 10 Preislisten pro Mandant erfasst werden.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>29. 09. 04</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.8 $ Date $Date: 2012/10/09 08:01:39 $
 */
public class PanelKundenartikelnummern extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InternalFrameArtikel intFrame = null;

	/** Wenn true, dann die neue Position vor der aktuell markierten einfuegen. */
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;
	
	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);
	private WrapperTextField wtfArtikel = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	
	private WrapperSelectField wsfKunde = new WrapperSelectField(
			WrapperSelectField.KUNDE, getInternalFrame(), false);

	public PanelKundenartikelnummern(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameArtikel) internalFrame;

		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		setBorder(innerBorder);
		wbuArtikel.setText(LPMain.getTextRespectUISPr("button.artikel"));
		wtfArtikel.setActivatable(false);
		
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = {};
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Zeile
		jPanelWorkingOn.add(wbuArtikel,
				new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfArtikel,
				new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wsfKunde.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wsfKunde.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}


	
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		setDefaults();

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		setDefaults();

		// Key neu einlesen, Ausloeser war ev. ein Refresh oder Discard
		Object oKey = getKeyWhenDetailPanel();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {

			KundesokoDto kdsokoDto = DelegateFactory.getInstance()
					.getKundesokoDelegate()
					.kundesokoFindByPrimaryKey((Integer) oKey);

			
			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(kdsokoDto.getArtikelIId());
			
			wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
			wbuArtikel.setOKey(artikelDto.getIId());
			wsfKunde.setKey(kdsokoDto.getKundeIId());

		}

	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VKPF_PREISLISTENNAME;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
