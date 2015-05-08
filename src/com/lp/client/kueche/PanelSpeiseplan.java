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
package com.lp.client.kueche;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.zeiterfassung.PanelZeitdaten;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelSpeiseplan extends PanelBasis implements
		PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpeiseplanDto speiseplanDto = null;
	private InternalFrameKueche internalFrameKueche = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private JButton wbuTagZurueck = new JButton();
	private JButton wbuNaechsterTag = new JButton();

	private boolean bChefbuchhalter = false;

	private Integer letzteFertigunsgruppe = null;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaDatum = new WrapperLabel();
	public WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperSelectField wsfStueckliste = new WrapperSelectField(
			WrapperSelectField.STUECKLISTE, getInternalFrame(), false);
	private WrapperSelectField wsfSpeisekassa = new WrapperSelectField(
			WrapperSelectField.SPEISEKASSA, getInternalFrame(), false);

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	private WrapperButton wbuFertigungsgruppe = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";

	public InternalFrameKueche getInternalFrameKueche() {
		return internalFrameKueche;
	}

	public PanelSpeiseplan(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameKueche = (InternalFrameKueche) internalFrame;

		bChefbuchhalter = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		// Object key = getInternalFrameReklamation().getTabbedPaneKueche().
		// getPanelQuerySpeiseplan().getSelectedId();
		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {

			clearStatusbar();
			wnfMenge.setText("");
			wtfFertigungsgruppe.setText("");
			wsfSpeisekassa.setKey(null);
			wsfStueckliste.setKey(null);

			if (key == null) {
				wdfDatum.setEnabled(true);
			} else {

				String fertiguggsgruppeIId = getInternalFrameKueche()
						.getTabbedPaneKueche()
						.getSelektierteFertigungsgruppeIId();

				if (fertiguggsgruppeIId != null
						&& fertiguggsgruppeIId.length() > 0) {
					FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
							.getInstance().getStuecklisteDelegate()
							.fertigungsgruppeFindByPrimaryKey(
									new Integer(fertiguggsgruppeIId));
					speiseplanDto.setFertigungsgruppeIId(fertigungsgruppeDto
							.getIId());
					wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
				} else {
					if (letzteFertigunsgruppe != null) {
						FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
								.getInstance().getStuecklisteDelegate()
								.fertigungsgruppeFindByPrimaryKey(letzteFertigunsgruppe);
						speiseplanDto
								.setFertigungsgruppeIId(fertigungsgruppeDto
										.getIId());
						wtfFertigungsgruppe.setText(fertigungsgruppeDto
								.getCBez());
					}

				}

			}

		} else {
			speiseplanDto = DelegateFactory.getInstance().getKuecheDelegate()
					.speiseplanFindByPrimaryKey((Integer) key);

			dto2Components();
			wdfDatum.setEnabled(true);
		}

	}

	public void propertyChange(PropertyChangeEvent e) {
		System.out.println(e.getPropertyName());
		if (e.getSource() == wdfDatum.getDisplay()
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfDatum.setDate((Date) e.getNewValue());
			try {

				java.sql.Date dDate = wdfDatum.getDate();

				if (getInternalFrameKueche().getTabbedPaneKueche() != null) {
					FilterKriterium[] fk = KuecheFilterFactory
							.getInstance()
							.createFKSpeiseplanZuDatum(
									dDate,
									getInternalFrameKueche()
											.getTabbedPaneKueche().selektierteFertigungsgruppeIId);
					getInternalFrameKueche().getTabbedPaneKueche()
							.getPanelQuerySpeiseplan().setDefaultFilter(fk);
				}

				Object sKey = getInternalFrame().getKeyWasForLockMe();

				getInternalFrameKueche().getTabbedPaneKueche()
						.refreshQPSpeisplan(wdfDatum.getDate(),
								LPMain.getLockMeForNew().equals(sKey));

			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
			}
		}
	}

	protected void dto2Components() throws Throwable {

		wdfDatum.setTimestamp(speiseplanDto.getTDatum());

		if (bChefbuchhalter == false) {

			LPButtonAction oDelete = (LPButtonAction) internalFrameKueche
					.getTabbedPaneKueche().getPanelDetailSpeiseplan()
					.getHmOfButtons().get(PanelBasis.ACTION_DELETE);
			LPButtonAction oUpdate = (LPButtonAction) internalFrameKueche
					.getTabbedPaneKueche().getPanelDetailSpeiseplan()
					.getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
			if (Helper.cutTimestamp(
					new java.sql.Timestamp(System.currentTimeMillis())).after(
					Helper.cutTimestamp(speiseplanDto.getTDatum()))) {
				oDelete.getButton().setVisible(false);
				oUpdate.getButton().setVisible(false);
			} else {
				oDelete.getButton().setVisible(true);
				oUpdate.getButton().setVisible(true);
			}
		}

		wnfMenge.setBigDecimal(speiseplanDto.getNMenge());

		wsfSpeisekassa.setKey(speiseplanDto.getKassaartikelIId());
		wsfStueckliste.setKey(speiseplanDto.getStuecklisteIId());

		FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance()
				.getStuecklisteDelegate().fertigungsgruppeFindByPrimaryKey(
						speiseplanDto.getFertigungsgruppeIId());
		wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 0, 10);
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
		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));

		wdfDatum.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		wsfStueckliste.setMandatoryField(true);
		wsfSpeisekassa.setMandatoryField(true);

		wbuFertigungsgruppe.setText(LPMain
				.getTextRespectUISPr("stkl.fertigungsgruppe")
				+ "...");
		wbuFertigungsgruppe
				.setActionCommand(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe.setActivatable(false);
		wtfFertigungsgruppe.setMandatoryField(true);

		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wdfDatum
				.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
		wdfDatum.setActivatable(false);

		wlaDatum.setHorizontalAlignment(SwingConstants.LEFT);

		wdfDatum.getDisplay().addPropertyChangeListener(this);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wbuTagZurueck.setText("<");
		wbuTagZurueck
				.addActionListener(new PanelSpeiseplan_wbuTagZurueck_actionAdapter(
						this));
		wbuTagZurueck.setMaximumSize(new Dimension(25, 21));
		wbuTagZurueck.setMinimumSize(new Dimension(25, 21));
		wbuTagZurueck.setPreferredSize(new Dimension(25, 21));

		wbuNaechsterTag.setText(">");
		wbuNaechsterTag
				.addActionListener(new PanelSpeiseplan_wbuNaechsterTag_actionAdapter(
						this));
		wbuNaechsterTag.setMaximumSize(new Dimension(25, 21));
		wbuNaechsterTag.setMinimumSize(new Dimension(25, 21));
		wbuNaechsterTag.setPreferredSize(new Dimension(25, 21));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));

		jpaWorkingOn.add(wbuTagZurueck, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuNaechsterTag, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 20, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(3, iZeile,
				1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 400, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfStueckliste, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfStueckliste.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfSpeisekassa, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfSpeisekassa.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		java.sql.Timestamp ts = wdfDatum.getTimestamp();
		leereAlleFelder(this);
		wdfDatum.setTimestamp(ts);
		speiseplanDto = new SpeiseplanDto();
		wdfDatum.setEnabled(false);
		wbuNaechsterTag.setEnabled(false);
		wbuTagZurueck.setEnabled(false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		}
	}

	private void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory
				.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(), null, false);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SPEISEPLAN;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		speiseplanDto.setTDatum(Helper.cutTimestamp(wdfDatum.getTimestamp()));

		speiseplanDto.setNMenge(wnfMenge.getBigDecimal());

		speiseplanDto.setKassaartikelIId(wsfSpeisekassa.getIKey());
		speiseplanDto.setStuecklisteIId(wsfStueckliste.getIKey());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getKuecheDelegate().removeSpeiseplan(
				speiseplanDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			int i = DelegateFactory.getInstance().getKuecheDelegate()
					.getAnzahlKassaimportZuSpeiseplan(speiseplanDto.getIId());

			if (i > 0) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr(
								"kue.error.kassaimportdatenvorhanden"));
				return;
			}

			components2Dto();

			if (Helper.cutTimestamp(
					new java.sql.Timestamp(System.currentTimeMillis())).after(
					Helper.cutTimestamp(speiseplanDto.getTDatum()))
					&& bChefbuchhalter == false) {

				// Meldung
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr(
								"kue.error.keinealtenspeiseplaeneaendern"));
				return;

			}

			if (speiseplanDto.getIId() == null) {
				speiseplanDto.setIId(DelegateFactory.getInstance()
						.getKuecheDelegate().createSpeiseplan(speiseplanDto));
				setKeyWhenDetailPanel(speiseplanDto.getIId());

				speiseplanDto = DelegateFactory.getInstance()
						.getKuecheDelegate().speiseplanFindByPrimaryKey(
								speiseplanDto.getIId());

			} else {

				DelegateFactory.getInstance().getKuecheDelegate()
						.updateSpeiseplan(speiseplanDto);

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						speiseplanDto.getIId().toString());
			}
			eventYouAreSelected(false);
			wbuNaechsterTag.setEnabled(true);
			wbuTagZurueck.setEnabled(true);

			speiseplanDto = DelegateFactory.getInstance().getKuecheDelegate()
					.speiseplanFindByPrimaryKey(speiseplanDto.getIId());
		}
	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfDatum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfDatum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		wbuTagZurueck.setEnabled(false);
		wbuNaechsterTag.setEnabled(false);
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		wbuNaechsterTag.setEnabled(true);
		wbuTagZurueck.setEnabled(true);
		wdfDatum.setEnabled(true);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey(key);
				speiseplanDto.setFertigungsgruppeIId(key);
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
				letzteFertigunsgruppe=key;
			}
		}
	}

	class PanelSpeiseplan_wbuNaechsterTag_actionAdapter implements
			ActionListener {
		private PanelSpeiseplan adaptee;

		PanelSpeiseplan_wbuNaechsterTag_actionAdapter(PanelSpeiseplan adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wbuNaechsterTag_actionPerformed(e);
		}
	}

	class PanelSpeiseplan_wbuTagZurueck_actionAdapter implements ActionListener {
		private PanelSpeiseplan adaptee;

		PanelSpeiseplan_wbuTagZurueck_actionAdapter(PanelSpeiseplan adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wbuTagZurueck_actionPerformed(e);
		}
	}
}
