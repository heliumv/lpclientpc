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
package com.lp.client.lieferschein;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogUnterschiedlicheKonditionen extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnLieferschein = new JButton();
	private JButton btnAuftrag = new JButton();

	public boolean bAbgebrochen = true;
	boolean bKonditionenUnterschiedlich = false;

	AuftragDto auftragNeuGewaehlt = null;
	LieferscheinDto lsDtoVorhanden = null;

	InternalFrameLieferschein internalFrame = null;

	public DialogUnterschiedlicheKonditionen(AuftragDto auftragNeuGewaehlt,
			LieferscheinDto lsDtoVorhanden,
			InternalFrameLieferschein internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"), true);

		this.internalFrame = internalFrame;
		this.auftragNeuGewaehlt = auftragNeuGewaehlt;
		this.lsDtoVorhanden = lsDtoVorhanden;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(600, 290));

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAuftrag)) {
			lsDtoVorhanden
					.setSpediteurIId(auftragNeuGewaehlt.getSpediteurIId());
			lsDtoVorhanden.setZahlungszielIId(auftragNeuGewaehlt
					.getZahlungszielIId());
			lsDtoVorhanden
					.setLieferartIId(auftragNeuGewaehlt.getLieferartIId());

			lsDtoVorhanden.setFVersteckterAufschlag(auftragNeuGewaehlt
					.getFVersteckterAufschlag());

			double dFaktor = 100.0;
			if (auftragNeuGewaehlt.getFAllgemeinerRabattsatz() != null) {
				dFaktor = dFaktor
						- auftragNeuGewaehlt.getFAllgemeinerRabattsatz();
			}
			if (auftragNeuGewaehlt.getFProjektierungsrabattsatz() != null) {
				double dFaktor2 = 100.0 - auftragNeuGewaehlt
						.getFProjektierungsrabattsatz();
				dFaktor = dFaktor * dFaktor2 / 100.0;
			}

			Double dAllgRabattGesamtAuftrag = new Double(100.0 - dFaktor);
			lsDtoVorhanden.setFAllgemeinerRabattsatz(dAllgRabattGesamtAuftrag);
			try {
				DelegateFactory.getInstance().getLsDelegate()
						.updateLieferschein(lsDtoVorhanden);

				DelegateFactory.getInstance().getLsDelegate()
						.updateLieferscheinKonditionen(lsDtoVorhanden.getIId());
			} catch (Throwable e1) {
				internalFrame.getTabbedPaneLieferschein().handleException(e1,
						false);
			}

			bAbgebrochen = false;
			this.setVisible(false);
		} else if (e.getSource().equals(btnLieferschein)) {
			bAbgebrochen = false;
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnLieferschein.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.lieferschein"));
		btnAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftrag"));
		btnLieferschein.addActionListener(this);
		btnAuftrag.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		String lieferartAuftragString = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(auftragNeuGewaehlt.getLieferartIId())
				.formatBez();
		String spediteurAuftragString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(auftragNeuGewaehlt.getSpediteurIId())
				.getCNamedesspediteurs();
		String zahlungszielAuftragString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(
						auftragNeuGewaehlt.getZahlungszielIId()).getCBez();

		String lieferartLieferscheinString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(lsDtoVorhanden.getLieferartIId())
				.formatBez();
		String spediteurLieferscheinString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(lsDtoVorhanden.getSpediteurIId())
				.getCNamedesspediteurs();
		String zahlungszielLieferscheinString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(
						lsDtoVorhanden.getZahlungszielIId()).getCBez();

		int iZeile = 0;

		WrapperLabel wla = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr(
						"ls.sammellieferschein.konditionenweichenab"));
		wla.setHorizontalAlignment(SwingConstants.LEFT);
		this.getContentPane().add(
				wla,
				new GridBagConstraints(0, iZeile, 4, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"label.lieferschein")),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"auft.auftrag")),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaLieferart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferart"));

		if (!auftragNeuGewaehlt.getLieferartIId().equals(
				lsDtoVorhanden.getLieferartIId())) {
			wlaLieferart.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaLieferart,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));

		this.getContentPane().add(
				new WrapperLabel(lieferartLieferscheinString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(lieferartAuftragString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						100, 0));

		iZeile++;

		WrapperLabel wlaZahlungsziel = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.zahlungsziel"));

		if (!auftragNeuGewaehlt.getZahlungszielIId().equals(
				lsDtoVorhanden.getZahlungszielIId())) {
			wlaZahlungsziel.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaZahlungsziel,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielLieferscheinString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielAuftragString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		WrapperLabel wlaSpediteur = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.spediteur"));

		if (!auftragNeuGewaehlt.getSpediteurIId().equals(
				lsDtoVorhanden.getSpediteurIId())) {
			wlaSpediteur.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaSpediteur,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(spediteurLieferscheinString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(spediteurAuftragString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		iZeile++;

		WrapperLabel wlaVersteckterAufschlag = new WrapperLabel(LPMain
				.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));

		if (auftragNeuGewaehlt.getFVersteckterAufschlag().doubleValue() != lsDtoVorhanden
				.getFVersteckterAufschlag()) {
			wlaVersteckterAufschlag.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaVersteckterAufschlag,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(lsDtoVorhanden
						.getFVersteckterAufschlag(), 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(
						auftragNeuGewaehlt.getFVersteckterAufschlag(), 2,
						LPMain.getTheClient().getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		double dFaktor = 100.0;
		if (auftragNeuGewaehlt.getFAllgemeinerRabattsatz() != null) {
			dFaktor = dFaktor - auftragNeuGewaehlt.getFAllgemeinerRabattsatz();
		}
		if (auftragNeuGewaehlt.getFProjektierungsrabattsatz() != null) {
			double dFaktor2 = 100.0 - auftragNeuGewaehlt
					.getFProjektierungsrabattsatz();
			dFaktor = dFaktor * dFaktor2 / 100.0;
		}

		Double dAllgRabattGesamtAuftrag = new Double(100.0 - dFaktor);

		WrapperLabel wlaAllgRabatt = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.allgemeinerrabatt"));

		if (dAllgRabattGesamtAuftrag.doubleValue() != lsDtoVorhanden
				.getFAllgemeinerRabattsatz()) {
			wlaAllgRabatt.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaAllgRabatt,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(lsDtoVorhanden
						.getFAllgemeinerRabattsatz(), 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(dAllgRabattGesamtAuftrag, 2,
						LPMain.getTheClient().getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaWelche = new WrapperLabel(
				LPMain.getInstance()
						.getTextRespectUISPr(
								"ls.sammellieferschein.konditionenweichenab.welche.uebernehmen"));
		wlaWelche.setHorizontalAlignment(SwingConstants.LEFT);

		this.getContentPane().add(
				wlaWelche,
				new GridBagConstraints(0, iZeile, 3, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------

		iZeile++;

		this.getContentPane().add(
				btnLieferschein,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnAuftrag,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
