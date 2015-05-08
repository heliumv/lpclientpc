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

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Basisfenster fuer LP5 Positionen.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-03-28</p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version 1.0
 */
public class PanelPositionenLieferschein extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LieferscheinDto lieferscheinDto = null;
	private KundeDto kundeDto = null;
	private static final String ACTION_SPECIAL_LIEFERSCHEIN = "action_special_positionen_lieferschein";
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	private WrapperGotoButton wbuLieferschein = new WrapperGotoButton(
			WrapperGotoButton.GOTO_LIEFERSCHEIN_AUSWAHL);
	private WrapperTextField wtfLieferscheinNummer = new WrapperTextField();
	private WrapperTextField wtfLieferscheinBezeichnung = new WrapperTextField();
	private JPanel jpaWorkingOn = new JPanel(new GridBagLayout());

	private int iSpaltenbreite1 = 100; // default

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param iSpaltenbreiteI
	 *            int
	 * @throws Throwable
	 */
	public PanelPositionenLieferschein(InternalFrame internalFrame,
			int iSpaltenbreiteI) throws Throwable {
		super(internalFrame, null);

		iSpaltenbreite1 = iSpaltenbreiteI;

		try {
			jbInit();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame);
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		wbuLieferschein.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferschein"));
		wbuLieferschein.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.lieferschein.tooltip"));
		wbuLieferschein.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults
				.getInstance().getControlHeight()));
		wbuLieferschein.setPreferredSize(new Dimension(iSpaltenbreite1,
				Defaults.getInstance().getControlHeight()));
		wtfLieferscheinNummer.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfLieferscheinNummer.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfLieferscheinNummer.setActivatable(false);
		wtfLieferscheinBezeichnung.setActivatable(false);
		this.setLayout(new GridBagLayout());
		wbuLieferschein.addActionListener(this);
		wbuLieferschein.setActionCommand(ACTION_SPECIAL_LIEFERSCHEIN);
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuLieferschein, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferscheinNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferscheinBezeichnung, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERSCHEIN)) {
			dialogQueryLieferschein(e);
		}
	}

	/**
	 * Dialogfenster zur Lieferscheinauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryLieferschein(ActionEvent e) throws Throwable {
		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
				.createFKGelieferteLieferscheine();
		if (kundeDto.getPartnerDto().getLandplzortDto() != null) {

			FilterKriterium[] filters = new FilterKriterium[3];
			filters[0] = LieferscheinFilterFactory.getInstance()
					.createFKGelieferteLieferscheine()[0];
			filters[1] = LieferscheinFilterFactory.getInstance()
					.createFKGelieferteLieferscheine()[1];
			filters[2] = new FilterKriterium("flrkunderechnungsadresse" + "."
					+ KundeFac.FLR_PARTNER + "."
					+ PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
					+ SystemFac.FLR_LP_FLRLAND + "." + SystemFac.FLR_LP_LANDID,
					true, kundeDto.getPartnerDto().getLandplzortDto()
							.getIlandID().toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			fk = filters;
		}
		String sTitle = LPMain
				.getTextRespectUISPr("ls.print.listenichtverrechnet");
		panelQueryFLRLieferschein = LieferscheinFilterFactory
				.getInstance()
				.createPanelQueryFLRLieferschein(
						getInternalFrame(),
						fk,
						sTitle,
						new FilterKriterium(
								LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE,
								true, kundeDto.getIId().toString(),
								FilterKriterium.OPERATOR_EQUAL, false));

		panelQueryFLRLieferschein
				.getWcbVersteckteFelderAnzeigen()
				.setText(
						LPMain.getTextRespectUISPr("re.positionen.lsauswahl.allerechnungsadressen"));

		new DialogQuery(panelQueryFLRLieferschein);
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 */
	protected void eventItemchanged(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferschein) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				holeLieferschein(key);
			}
		}
	}

	/**
	 * holeLieferschein
	 * 
	 * @param key
	 *            Integer
	 */
	private void holeLieferschein(Object key) {
		try {
			if (key != null) {
				lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey((Integer) key);

				if (Helper.short2boolean(lieferscheinDto.getBVerrechenbar()) == false) {
					boolean b = DialogFactory
							.showModalJaNeinDialog(
									getInternalFrame(),
									LPMain.getTextRespectUISPr("rech.ls.nichtverrechenbar.trotzdem"),
									LPMain.getTextRespectUISPr("lp.frage"));
					if (b == false) {
						lieferscheinDto = null;
					}
				}

				dto2ComponentsLieferschein();
			}
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	/**
	 * Die Lieferscheindaten in die Felder schreiben
	 */
	private void dto2ComponentsLieferschein() {
		if (lieferscheinDto != null) {
			wtfLieferscheinNummer.setText(lieferscheinDto.getCNr());
			wtfLieferscheinBezeichnung.setText(lieferscheinDto
					.getCBezProjektbezeichnung());
			wbuLieferschein.setOKey(lieferscheinDto.getIId());
		} else {
			wtfLieferscheinNummer.setText(null);
			wtfLieferscheinBezeichnung.setText(null);
		}
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}

	public WrapperTextField getWtfLieferscheinNummer() {
		return wtfLieferscheinNummer;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public void setLieferscheinDto(LieferscheinDto lieferscheinDto) {
		this.lieferscheinDto = lieferscheinDto;
		dto2ComponentsLieferschein();
	}

	public void setVisible(boolean bVisible) {
		super.setVisible(bVisible);
		wtfLieferscheinNummer.setMandatoryField(bVisible);
	}
}
