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
package com.lp.client.eingangsrechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.inserat.InseratFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Auftragszuordnungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>20. 02. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelEingangsrechnungInseratzuordnung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private InseraterDto inseraterDto = null;
	private InseratDto inseratDto = null;
	private final static String ACTION_SPECIAL_INSERAT = "action_special_er_inserat";
	private final static String ACTION_SPECIAL_REST = "action_special_er_rest";
	private PanelQueryFLR panelQueryFLRInserat = null;

	private WrapperCheckBox wcbWertaufteilen = new WrapperCheckBox();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperTextField wtfAuftragNummer = new WrapperTextField();
	private WrapperTextField wtfAuftragBezeichnung = new WrapperTextField();
	private WrapperGotoButton wbuInserat = new WrapperGotoButton(
			WrapperGotoButton.GOTO_INSERAT_AUSWAHL);
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperNumberField wnfBetragOffen = new WrapperNumberField();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private WrapperLabel wlaAbstand1 = new WrapperLabel();
	private WrapperLabel wlaAbstand2 = new WrapperLabel();
	private WrapperLabel wlaAbstand3 = new WrapperLabel();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperLabel wlaBetragOffen = new WrapperLabel();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperTextField wtfText = new WrapperTextField();

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperLabel wlaRabatt = new WrapperLabel();
	private WrapperNumberField wnfRabattKD = new WrapperNumberField();
	private WrapperLabel wlaZusatzRabatt = new WrapperLabel();
	private WrapperNumberField wnfZusatzRabattKD = new WrapperNumberField();
	private WrapperLabel wlaNachlass = new WrapperLabel();
	private WrapperNumberField wnfNachlassKD = new WrapperNumberField();
	private WrapperNumberField wnfRabattLF = new WrapperNumberField();
	private WrapperNumberField wnfZusatzRabattLF = new WrapperNumberField();
	private WrapperNumberField wnfNachlassLF = new WrapperNumberField();

	private WrapperNumberField wnfPreisEK = new WrapperNumberField();

	private WrapperNumberField wnfPreisVK = new WrapperNumberField();

	private WrapperLabel wlaSummeBereitsvorhanden = new WrapperLabel();

	JList list = null;

	private Border border1;

	public PanelEingangsrechnungInseratzuordnung(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);
		wtfAuftragNummer.setMandatoryField(true);
		wbuInserat.setText(LPMain.getInstance().getTextRespectUISPr(
				"iv.inserat.modulname")
				+ "...");
		wlaRabatt.setText(LPMain.getTextRespectUISPr("label.rabatt"));
		wlaZusatzRabatt.setText(LPMain.getTextRespectUISPr("iv.zusatzrabatt"));
		wlaNachlass.setText(LPMain.getTextRespectUISPr("iv.nachlass"));

		wnfMenge.setMandatoryField(true);
		wnfRabattKD.setMandatoryField(true);
		wnfZusatzRabattKD.setMandatoryField(true);
		wnfNachlassKD.setMandatoryField(true);
		wnfRabattLF.setMandatoryField(true);
		wnfZusatzRabattLF.setMandatoryField(true);
		wnfNachlassLF.setMandatoryField(true);
		wnfPreisEK.setMandatoryField(true);
		wnfPreisVK.setMandatoryField(true);

		wnfRabattKD.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfZusatzRabattKD.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfNachlassKD.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfRabattLF.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfZusatzRabattLF.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfNachlassLF.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfPreisEK.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfPreisVK.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());

		list = new JList();

		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);

		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wbuInserat.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"iv.inserat"));
		wnfBetrag.setMandatoryField(true);
		wnfBetrag.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());

		wnfBetragOffen.setActivatable(false);
		wnfBetragOffen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());

		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"iv.erinseratzuordnung.betragohnewerbeabgabe"));
		wlaBetragOffen.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.offen"));
		wlaText.setText(LPMain.getInstance().getTextRespectUISPr("label.text"));
		jpaWorkingOn.setLayout(gridBagLayout3);
		wlaAbstand1.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand1.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setMinimumSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setPreferredSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		jpaWorkingOn.setBorder(border1);
		wtfAuftragBezeichnung.setActivatable(false);
		wtfAuftragNummer.setActivatable(false);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wcbWertaufteilen.setText(LPMain
				.getTextRespectUISPr("iv.kopfdaten.wertaufteilen"));
		wcbWertaufteilen.addActionListener(this);
		wtfAuftragBezeichnung.setMandatoryField(false);
		wbuInserat.setActionCommand(ACTION_SPECIAL_INSERAT);
		wbuInserat.addActionListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuInserat, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wtfAuftragNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfAuftragBezeichnung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbWertaufteilen, new GridBagConstraints(3, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragOffen, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragOffen, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 20, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 20, 2), 0, 0));

		iZeile++;

		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("iv.eingangsrechnung.inseratzuordnung.rueckpflege")),
						new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										0, 2), 50, 0));

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 150, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 2), 50, 0));

		WrapperLabel wlaMenge = new WrapperLabel(
				SystemFac.EINHEIT_STUECK.trim());
		wlaMenge.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 0, 0), 50, 0));
		iZeile++;

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("label.lieferant")),
						new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										0, 2), 0, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("label.kunde")),
				new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2),
						0, 0));

		iZeile++;

		wlaSummeBereitsvorhanden
				.setText(LPMain
						.getTextRespectUISPr("iv.erinseratzuordnung.bereitszugeordnet"));
		wlaSummeBereitsvorhanden.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaSummeBereitsvorhanden, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(
				new WrapperLabel(LPMain
						.getTextRespectUISPr("iv.kopfdaten.preis")),
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2),
						0, 0));

		jpaWorkingOn.add(wnfPreisEK, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 70, 0));
		jpaWorkingOn.add(wnfPreisVK, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 70, 0));

		iZeile++;
		jpaWorkingOn.add(listScroller, new GridBagConstraints(0, iZeile, 3, 3,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 0, 80), 0, 0));
		jpaWorkingOn.add(wlaRabatt, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wnfRabattLF, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfRabattKD, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaZusatzRabatt, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wnfZusatzRabattLF, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfZusatzRabattKD, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaNachlass, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfNachlassLF, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfNachlassKD, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		// locknew: 1 hier muss dafuer gesorgt werden, dass der Framework lockt
		super.eventActionNew(e, true, false);
		clearStatusbar();
		inseraterDto = null;
		inseratDto = null;
		leereAlleFelder(this);

	}

	/**
	 * Loeschen einer Rechnungsposition
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.inseraterDto != null) {
			if (inseraterDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getInseratDelegate()
							.removeInserater(inseraterDto.getIId());
					this.inseraterDto = null;
					// selectafterdelete: key null setzen
					this.setKeyWhenDetailPanel(null);
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wbuInserat.setEnabled(false);
		if (!Helper.short2boolean(inseratDto.getBWertaufteilen())) {
			wnfBetrag.setEditable(false);
		}

		if (inseratDto.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)) {

			wnfMenge.setEditable(false);
			// wnfPreisEK.setEditable(false);
			wnfPreisVK.setEditable(false);
			wnfNachlassKD.setEditable(false);
			// wnfNachlassLF.setEditable(false);
			wnfRabattKD.setEditable(false);
			// wnfRabattLF.setEditable(false);

			// wnfZusatzRabattLF.setEditable(false);
			wnfZusatzRabattKD.setEditable(false);

			DelegateFactory.getInstance().getInseratDelegate()
					.updateInserat(inseratDto);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			// Preise rueckpflegen
			if (!inseratDto.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)) {

				inseratDto.setNMenge(wnfMenge.getBigDecimal());
				inseratDto.setNNettoeinzelpreisVk(wnfPreisVK.getBigDecimal());
				inseratDto.setFKdNachlass(wnfNachlassKD.getDouble());
				inseratDto.setFKdRabatt(wnfRabattKD.getDouble());
				inseratDto.setFKdZusatzrabatt(wnfZusatzRabattKD.getDouble());
			}

			inseratDto.setBWertaufteilen(wcbWertaufteilen.getShort());

			inseratDto.setNNettoeinzelpreisEk(wnfPreisEK.getBigDecimal());

			inseratDto.setFLfNachlass(wnfNachlassLF.getDouble());

			inseratDto.setFLFRabatt(wnfRabattLF.getDouble());
			inseratDto.setFLfZusatzrabatt(wnfZusatzRabattLF.getDouble());

			DelegateFactory.getInstance().getInseratDelegate()
					.updateInserat(inseratDto);

			if (inseraterDto.getIId() == null) {

				inseraterDto.setIId(DelegateFactory.getInstance()
						.getInseratDelegate().createInserater(inseraterDto));

				setKeyWhenDetailPanel(inseraterDto.getIId());
			} else {
				DelegateFactory.getInstance().getInseratDelegate()
						.updateInserater(inseraterDto);

			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						inseraterDto.getInseratIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	private void components2Dto() throws Throwable {
		if (inseraterDto == null) {
			inseraterDto = new InseraterDto();
			inseraterDto.setEingangsrechnungIId(getTabbedPane()
					.getEingangsrechnungDto().getIId());
		}
		if (inseratDto != null) {
			inseraterDto.setInseratIId(inseratDto.getIId());
		} else {
			inseraterDto.setInseratIId(null);
		}

		if (wcbWertaufteilen.isSelected()) {
			inseraterDto.setNBetrag(wnfBetrag.getBigDecimal());
		} else {
			// Betrag aus Inserat-Preisen berechnen
		}

		inseraterDto.setCText(wtfText.getText());
	}

	private void dto2Components() throws Throwable {
		if (getKeyWhenDetailPanel() != null) {
			inseraterDto = DelegateFactory
					.getInstance()
					.getInseratDelegate()
					.inseraterFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());
		}

		if (inseraterDto != null) {
			holeInserat(inseraterDto.getInseratIId());
			dto2ComponentsAuftrag();
			// den maximalwert setzen, denn der ist ja 0, wenn alles zugeordnet
			// ist

			wnfBetrag.setBigDecimal(inseraterDto.getNBetrag());
			wtfText.setText(inseraterDto.getCText());
			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtZuAuftraegenZugeordnet(
							getTabbedPane().getEingangsrechnungDto().getIId()));
			wbuInserat.setOKey(inseratDto.getIId());

			// Liste befuellen
			BigDecimal bdSummeZugeordnet = new BigDecimal(0);

			list.removeAll();

			InseraterDto[] inseraterDtos = DelegateFactory.getInstance()
					.getInseratDelegate()
					.inseraterFindByInseratIId(inseratDto.getIId());

			Object[] tempZeilen = new Object[inseraterDtos.length];

			for (int i = 0; i < inseraterDtos.length; i++) {

				EingangsrechnungDto erDto=DelegateFactory.getInstance().getEingangsrechnungDelegate().eingangsrechnungFindByPrimaryKey(inseraterDtos[i].getEingangsrechnungIId());
				
				bdSummeZugeordnet = bdSummeZugeordnet.add(inseraterDtos[i]
						.getNBetrag());
				tempZeilen[i] = "ER: "+erDto.getCNr()+" Inserat: "+ inseratDto.getCNr()
						+ " "
						+ Helper.fitString2LengthAlignRight(Helper.formatZahl(
								inseraterDtos[i].getNBetrag(), 2, LPMain
										.getTheClient().getLocUi()), 15, ' ')
						+ " "
						+ LPMain.getInstance().getTheClient()
								.getSMandantenwaehrung();
			}

			wlaSummeBereitsvorhanden
					.setText(LPMain
							.getTextRespectUISPr("iv.erinseratzuordnung.bereitszugeordnet")
							+ " "
							+ Helper.formatZahl(bdSummeZugeordnet, 2, LPMain
									.getInstance().getTheClient().getLocUi())
							+ " "
							+ LPMain.getInstance().getTheClient()
									.getSMandantenwaehrung());

			list.setListData(tempZeilen);
			this.setStatusbarPersonalIIdAnlegen(inseraterDto
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(inseraterDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(inseraterDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(inseraterDto.getTAendern());
		} else {
			wbuInserat.setOKey(null);
		}
	}

	/**
	 * dto2ComponentsAuftrag
	 */
	private void dto2ComponentsAuftrag() throws Throwable {
		if (inseratDto != null) {
			wtfAuftragNummer.setText(inseratDto.getCNr());
			wtfAuftragBezeichnung.setText(inseratDto.getCBez());

			wnfMenge.setBigDecimal(inseratDto.getNMenge());
			wnfPreisEK.setBigDecimal(inseratDto.getNNettoeinzelpreisEk());
			wnfPreisVK.setBigDecimal(inseratDto.getNNettoeinzelpreisVk());
			wnfNachlassKD.setDouble(inseratDto.getFKdNachlass());
			wnfNachlassLF.setDouble(inseratDto.getFLfNachlass());
			wnfRabattKD.setDouble(inseratDto.getFKdRabatt());
			wnfRabattLF.setDouble(inseratDto.getFLFRabatt());
			wnfZusatzRabattKD.setDouble(inseratDto.getFKdZusatzrabatt());
			wnfZusatzRabattLF.setDouble(inseratDto.getFLfZusatzrabatt());
			wcbWertaufteilen.setShort(inseratDto.getBWertaufteilen());
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragBezeichnung.setText(null);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_INSERAT)) {
			dialogQueryAuftrag(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_REST)) {
			wnfBetrag.setBigDecimal(wnfBetragOffen.getBigDecimal());
		}

		if (e.getSource().equals(wcbWertaufteilen)) {
			if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
					|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
				if (wcbWertaufteilen.isSelected()) {
					wnfBetrag.setEditable(true);
				} else {
					wnfBetrag.setEditable(false);
					betragSetzen();
				}
			}
		}
	}

	void dialogQueryAuftrag(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		QueryType[] qt = null;

		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("flrinserat.mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium("flrinserat.status_c_nr", true, "('"
				+ LocaleFac.STATUS_STORNIERT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		panelQueryFLRInserat = new PanelQueryFLR(qt, kriterien,
				QueryParameters.UC_ID_INSERATE_OHNE_ER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"iv.inserat.modulname"));

		FilterKriteriumDirekt fkDirekt1 = InseratFilterFactory.getInstance()
				.createFKDInseratnummer();

		FilterKriteriumDirekt fkDirekt2 = InseratFilterFactory.getInstance()
				.createFKDKunde();

		panelQueryFLRInserat.befuellePanelFilterkriterienDirekt(fkDirekt1,
				fkDirekt2);

		panelQueryFLRInserat.addDirektFilter(InseratFilterFactory.getInstance()
				.createFKDBestellnummer());

		panelQueryFLRInserat
				.befuelleFilterkriteriumSchnellansicht(InseratFilterFactory
						.getInstance()
						.createFKInserateEinesLieferantenUndDessenRechungsadresseOhneER(
								getTabbedPane().getEingangsrechnungDto()
										.getLieferantIId()));

		panelQueryFLRInserat.getCbSchnellansicht().setText(
				LPMain.getTextRespectUISPr("iv.inserater.nurdirekte"));

		panelQueryFLRInserat.eventActionRefresh(null, false);

		new DialogQuery(panelQueryFLRInserat);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRInserat) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeInserat(key);
				wcbWertaufteilen.setShort(inseratDto.getBWertaufteilen());
				if (Helper.short2boolean(inseratDto.getBWertaufteilen())) {
					wnfBetrag.setEditable(true);
				} else {
					wnfBetrag.setEditable(false);
				}

				betragSetzen();

			}
		}
	}

	private void betragSetzen() throws ExceptionLP, Throwable {
		// Preise berechnen
		if (inseratDto != null) {

			InseratartikelDto[] inseratartikelDtos = DelegateFactory
					.getInstance().getInseratDelegate()
					.inseratartikelFindByInseratIId(inseratDto.getIId());

			BigDecimal preisZusatzEK = new BigDecimal(0);

			for (int i = 0; i < inseratartikelDtos.length; i++) {
				preisZusatzEK = preisZusatzEK
						.add(inseratartikelDtos[i].getNMenge().multiply(
								inseratartikelDtos[i].getNNettoeinzelpreisEk()));

			}

			wnfBetrag.setBigDecimal(inseratDto.getErrechneterWertEK(
					Defaults.getInstance().getIUINachkommastellenPreiseEK())
					.add(preisZusatzEK));
		}
	}

	private void holeInserat(Object key) throws Throwable {
		if (key != null) {
			inseratDto = DelegateFactory.getInstance().getInseratDelegate()
					.inseratFindByPrimaryKey((Integer) key);
		} else {
			inseratDto = null;
		}
		dto2ComponentsAuftrag();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.

			leereAlleFelder(this);
			list.removeAll();
			list.setListData(new Object[0]);
			wlaWaehrung1.setText(getTabbedPane().getEingangsrechnungDto()
					.getWaehrungCNr());
			wlaWaehrung2.setText(getTabbedPane().getEingangsrechnungDto()
					.getWaehrungCNr());
			wcbWertaufteilen.setSelected(false);
			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtZuAuftraegenZugeordnet(
							getTabbedPane().getEingangsrechnungDto().getIId()));
			clearStatusbar();
		} else {
			// einen alten Eintrag laden.
			inseraterDto = DelegateFactory.getInstance().getInseratDelegate()
					.inseraterFindByPrimaryKey((Integer) key);
			dto2Components();
			getTabbedPane().enablePanels();
		}
		if (key != null && key.equals(LPMain.getLockMeForNew())) {
			// wenn nix drinsteht, dann den restwert vorschlagen
			wnfBetrag.setBigDecimal(wnfBetragOffen.getBigDecimal());
		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuInserat;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
	}
}
