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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogAlleGeraeteseriennummernEinerLosablieferungAnzeigen extends
		JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTableSnrChnrs = new JTable();
	JButton jButtonUebernehmen = new JButton();

	public DialogAlleGeraeteseriennummernEinerLosablieferungAnzeigen(Integer losablieferungIId,
			List<SeriennrChargennrMitMengeDto> list) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("fert.ablieferung.geraetesnrs"), true);
		
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(600, 600);
		ArrayList<String[]> daten = new ArrayList<String[]>();

		for (int i = 0; i < list.size(); i++) {

			String[] zeile = new String[2];

			zeile[0] = list.get(i).getCSeriennrChargennr();

			GeraetesnrDto[] algsnr = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getGeraeteseriennummerEinerLagerbewegung(
							LocaleFac.BELEGART_LOSABLIEFERUNG,
							losablieferungIId,
							 list.get(i).getCSeriennrChargennr());
			
		

			if (algsnr != null && algsnr.length > 0) {

				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(algsnr[0].getArtikelIId());

				zeile[1] = Helper.fitString2Length(
						artikelDto.formatArtikelbezeichnung(), 25, ' ')
						+ " = " + algsnr[0].getCSnr();

				daten.add(zeile);

				for (int z = 1; z < algsnr.length; z++) {

					zeile = new String[2];

					zeile[0] = "";

					ArtikelDto artikelDtoZeile = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(
									algsnr[z].getArtikelIId());

					zeile[1] = Helper.fitString2Length(
							artikelDtoZeile.formatArtikelbezeichnung(), 25, ' ')
							+ " = " + algsnr[z].getCSnr();

					daten.add(zeile);

				}

			} else {
				zeile[1] = "";

				daten.add(zeile);

			}

		}

		String[][] data = new String[daten.size()][2];
		data = (String[][]) daten.toArray(data);

		String[] colNames = new String[] {
				LPMain.getInstance()
						.getTextRespectUISPr("fert.ablieferung.geraetesnrs.snr"),
				LPMain.getInstance().getTextRespectUISPr(
						"fert.ablieferung.geraetesnrs") };
		jTableSnrChnrs = new JTable(data, colNames);
		jTableSnrChnrs.setRowSelectionAllowed(true);

		jTableSnrChnrs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jScrollPane.getViewport().add(jTableSnrChnrs);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		jButtonUebernehmen
				.addActionListener(new DialogSnr_jButtonUebernehmen_actionAdapter(
						this));
		add(panel1);
		panel1.add(jScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {

		setVisible(false);
	}

}

class DialogSnr_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogAlleGeraeteseriennummernEinerLosablieferungAnzeigen adaptee;

	DialogSnr_jButtonUebernehmen_actionAdapter(
			DialogAlleGeraeteseriennummernEinerLosablieferungAnzeigen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}
