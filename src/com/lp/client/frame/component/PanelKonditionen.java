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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.ZahlungszielDto;

@SuppressWarnings("static-access")
/**
 * <p><I>Basispanel fuer alle Konditionenpanels</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>21.03.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public abstract class PanelKonditionen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected LieferartDto lieferartDto = null;
	protected ZahlungszielDto zahlungszielDto = null;
	protected SpediteurDto spediteurDto = null;

	public final static String ACTION_SPECIAL_ZAHLUNGSZIEL = "action_special_re_konditionen_zahlungsziel";
	public final static String ACTION_SPECIAL_LIEFERART = "action_special_re_konditionen_lieferart";
	public final static String ACTION_SPECIAL_SPEDITION = "action_special_re_konditionen_spedition";

	private PanelQueryFLR panelQueryFLRLieferart = null;
	private PanelQueryFLR panelQueryFLRSpediteur = null;
	private PanelQueryFLR panelQueryFLRZahlungsziel = null;

	protected boolean isPositionskontierung = false;
	protected GridBagLayout gridBagLayout1 = new GridBagLayout();
	protected JPanel jPanelWorkingOn = new JPanel();
	protected GridBagLayout gridBagLayoutWorkingOn = new GridBagLayout();
	protected WrapperLabel wlaAllgemeinerRabatt = new WrapperLabel();
	protected WrapperNumberField wnfAllgemeinerRabatt = null;
	protected WrapperButton wbuLieferart = new WrapperButton();
	protected WrapperButton wbuZahlungsziel = new WrapperButton();
	protected WrapperLabel wlaProzent2 = new WrapperLabel();
	protected Border border1;
	protected Border border2;
	protected WrapperComboBox wcoMehrwertsteuer = new WrapperComboBox();
	protected WrapperLabel wlaMehrwertsteuer = new WrapperLabel();
	protected WrapperLabel wlaKopftext = new WrapperLabel();
	protected WrapperLabel wlaFusstext = new WrapperLabel();
	// wreditf: 1 deklaration mit null (wegen Designer)
	protected WrapperEditorField wefKopftext = null;
	protected WrapperEditorField wefFusstext = null;
	protected WrapperButton wbuSpedition = new WrapperButton();
	protected WrapperTextField wtfSpedition = new WrapperTextField();
	protected WrapperTextField wtfLieferart = new WrapperTextField();
	protected WrapperTextField wtfZahlungsziel = new WrapperTextField();
	protected WrapperLabel wlaAllgemeinerRabattText = new WrapperLabel();
	protected WrapperLabel wlaLieferartort = new WrapperLabel();
	protected WrapperTextField wtfLieferartort = new WrapperTextField();

	public PanelKonditionen(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		jbInit();
		setDefaults();
		setDefaultsForThisPanel();
		// MB 09.05.06 nur in der subklasse ausfuehren, da hier die zusatzfelder
		// noch null sind
		// initComponents();
	}

	private void jbInit() throws Throwable {
		wnfAllgemeinerRabatt = new WrapperNumberField();
		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		border2 = BorderFactory.createEmptyBorder();
		this.setLayout(gridBagLayout1);
		this.setBorder(border2);
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		jPanelWorkingOn.setBorder(border1);

		// wreditf: 2 instantiierung (braucht internalFrame und Titel)
		wefFusstext = new WrapperEditorFieldTextmodul(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("label.fusstext"));
		wefKopftext = new WrapperEditorFieldTextmodul(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("label.kopftext"));
		wlaKopftext.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kopftext"));
		HelperClient.setDefaultsToComponent(wlaKopftext, 110);
		wlaFusstext.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.fusstext"));
		wbuLieferart.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart"));
		wbuLieferart.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart.tooltip"));
		wbuZahlungsziel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbuZahlungsziel.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.zahlungsziel.tooltip"));
		wbuSpedition.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur"));
		wbuSpedition.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur.tooltip"));
		wlaAllgemeinerRabatt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.allgemeinerrabatt"));
		wlaMehrwertsteuer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.mwst"));
		wlaProzent2.setText("%");
		wlaLieferartort.setText(LPMain.getInstance().getTextRespectUISPr(
		"lp.lieferort"));
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
		// activatable
		wtfZahlungsziel.setActivatable(true);
		wnfAllgemeinerRabatt.setActivatable(true);
		wnfAllgemeinerRabatt.setMaximumValue(100);
		wcoMehrwertsteuer.setActivatable(true);
		wtfLieferart.setActivatable(false);
		wtfSpedition.setActivatable(false);
		wtfZahlungsziel.setActivatable(false);
		// sizes
		wnfAllgemeinerRabatt.setMaximumSize(new Dimension(10000, Defaults
				.getInstance().getControlHeight()));
		wnfAllgemeinerRabatt.setMinimumSize(new Dimension(70, Defaults
				.getInstance().getControlHeight()));
		wnfAllgemeinerRabatt.setPreferredSize(new Dimension(70, Defaults
				.getInstance().getControlHeight()));

		// Actionpanel
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wbuLieferart.setActionCommand(ACTION_SPECIAL_LIEFERART);
		wbuSpedition.setActionCommand(ACTION_SPECIAL_SPEDITION);
		wbuZahlungsziel.setActionCommand(ACTION_SPECIAL_ZAHLUNGSZIEL);
		wbuLieferart.addActionListener(this);
		wbuSpedition.addActionListener(this);
		wbuZahlungsziel.addActionListener(this);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wbuLieferart, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLieferart, new GridBagConstraints(1, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaLieferartort, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wtfLieferartort, new GridBagConstraints(3, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuZahlungsziel, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfZahlungsziel, new GridBagConstraints(1, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuSpedition, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfSpedition, new GridBagConstraints(1, iZeile, 5,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAllgemeinerRabatt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAllgemeinerRabatt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAllgemeinerRabattText, new GridBagConstraints(3,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaMehrwertsteuer, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoMehrwertsteuer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaKopftext, new GridBagConstraints(0, 100, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefKopftext, new GridBagConstraints(1, 100, 5, 1,
				0.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn.add(wlaFusstext, new GridBagConstraints(0, 101, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefFusstext, new GridBagConstraints(1, 101, 5, 1,
				0.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Comboboxen befuellen.
	 * 
	 * @throws Throwable
	 */
	private void fuelleComboboxen() throws Throwable {
		wcoMehrwertsteuer.setMap(DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getAllMwstsatzbez(
						LPMain.getInstance().getTheClient().getMandant()));
	}

	private void setDefaultsForThisPanel() throws Throwable {
		fuelleComboboxen();
		this.wcoMehrwertsteuer
				.setKeyOfSelectedItem(MandantFac.MWST_NORMALSTEUERSATZ);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferart) {
				Integer key = (Integer) panelQueryFLRLieferart.getSelectedId();
				holeLieferart(key);
			} else if (e.getSource() == panelQueryFLRSpediteur) {
				Integer key = (Integer) panelQueryFLRSpediteur.getSelectedId();
				holeSpediteur(key);
			} else if (e.getSource() == panelQueryFLRZahlungsziel) {
				Integer key = (Integer) panelQueryFLRZahlungsziel
						.getSelectedId();
				holeZahlungsziel(key);
			}
		}
	}

	private void setDefaults() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);
		// Mandantenparameter fuer Positionskontierung bestimmen
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG,
						ParameterFac.KATEGORIE_KUNDEN,
						LPMain.getInstance().getTheClient().getMandant());

		isPositionskontierung = ((Boolean) parameter.getCWertAsObject())
				.booleanValue();
		isPositionskontierung = true;
		if (isPositionskontierung) {
			wcoMehrwertsteuer.setVisible(false);
			wlaMehrwertsteuer.setVisible(false);
		} else {
			wcoMehrwertsteuer.setEnabled(true);
			wcoMehrwertsteuer.setActivatable(true);
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_RECHNUNG;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERART)) {
			dialogQueryLieferart(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SPEDITION)) {
			dialogQuerySpediteur(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZAHLUNGSZIEL)) {
			dialogQueryZahlungsziel(e);
		}
	}

	protected final void holeZahlungsziel(Integer key) throws Throwable {
		if (key != null) {
			zahlungszielDto = DelegateFactory.getInstance()
					.getMandantDelegate().zahlungszielFindByPrimaryKey(key);
		} else {
			zahlungszielDto = null;
		}
		dto2ComponentsZahlungsziel();
	}

	protected final void holeLieferart(Integer key) throws Throwable {
		if (key != null) {
			lieferartDto = DelegateFactory.getInstance().getLocaleDelegate()
					.lieferartFindByPrimaryKey(key);
		} else {
			lieferartDto = null;
		}
		dto2ComponentLieferart();
	}

	protected final void holeSpediteur(Integer key) throws Throwable {
		if (key != null) {
			spediteurDto = DelegateFactory.getInstance().getMandantDelegate()
					.spediteurFindByPrimaryKey(key);
		} else {
			spediteurDto = null;
		}
		dto2ComponentsSpediteur();
	}

	private void dto2ComponentsZahlungsziel() {
		if (zahlungszielDto != null) {
			if (zahlungszielDto.getZahlungszielsprDto() != null) {
				wtfZahlungsziel.setText(zahlungszielDto.getZahlungszielsprDto()
						.getCBezeichnung());
			} else {
				wtfZahlungsziel.setText(zahlungszielDto.getCBez());
			}
		} else {
			wtfZahlungsziel.setText(null);
		}
	}

	private void dto2ComponentLieferart() {
		if (lieferartDto != null) {
			// die Lieferart ist sprachabhaengig
			String cBezLieferart = lieferartDto.getCNr();

			if (lieferartDto.getLieferartsprDto() != null) {
				cBezLieferart = lieferartDto.getLieferartsprDto()
						.getCBezeichnung();
			}

			wtfLieferart.setText(cBezLieferart == null ? lieferartDto.getCNr() : cBezLieferart);
		} else {
			wtfLieferart.setText(null);
		}
	}

	private void dto2ComponentsSpediteur() {
		if (spediteurDto != null) {
			wtfSpedition.setText(spediteurDto.getCNamedesspediteurs());
		} else {
			wtfSpedition.setText(null);
		}
	}

	/**
	 * Dialogfenster zur Zahlungszieleauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryZahlungsziel(ActionEvent e) throws Throwable {
		Integer zahlungszielIId = null;
		if (zahlungszielDto != null) {
			zahlungszielIId = zahlungszielDto.getIId();
		}
		panelQueryFLRZahlungsziel = SystemFilterFactory
				.getInstance()
				.createPanelFLRZahlungsziel(getInternalFrame(), zahlungszielIId);
		new DialogQuery(panelQueryFLRZahlungsziel);
	}

	/**
	 * Dialogfenster zur Lieferartenauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryLieferart(ActionEvent e) throws Throwable {
		Integer lieferartIId = null;
		if (lieferartDto != null) {
			lieferartIId = lieferartDto.getIId();
		}
		panelQueryFLRLieferart = SystemFilterFactory.getInstance()
				.createPanelFLRLieferart(getInternalFrame(), lieferartIId);
		new DialogQuery(panelQueryFLRLieferart);
	}

	/**
	 * Dialogfenster zur Spediteurauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQuerySpediteur(ActionEvent e) throws Throwable {
		Integer spediteurIId = null;
		if (spediteurDto != null) {
			spediteurIId = spediteurDto.getIId();
		}
		panelQueryFLRSpediteur = SystemFilterFactory.getInstance()
				.createPanelFLRSpediteur(getInternalFrame(), spediteurIId);
		new DialogQuery(panelQueryFLRSpediteur);
	}

	protected void setVisibleLieferart(boolean bVisible) {
		wbuLieferart.setVisible(bVisible);
		wtfLieferart.setVisible(bVisible);
	}

	protected void setVisibleSpediteur(boolean bVisible) {
		wbuSpedition.setVisible(bVisible);
		wtfSpedition.setVisible(bVisible);
	}

	protected void setVisibleZahlungsziel(boolean bVisible) {
		wbuZahlungsziel.setVisible(bVisible);
		wtfZahlungsziel.setVisible(bVisible);
	}

	protected void setVisibleAllgemeinerRabatt(boolean bVisible) {
		wlaAllgemeinerRabatt.setVisible(bVisible);
		wnfAllgemeinerRabatt.setVisible(bVisible);
		wlaProzent2.setVisible(bVisible);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferart;
	}
}
