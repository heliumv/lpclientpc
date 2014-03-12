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
package com.lp.client.lieferschein;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.BigDecimal4;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogPositionenBarcodeerfassung extends JDialog implements
		ActionListener, KeyListener, TableModelListener {

	private static final long serialVersionUID = 1L;
	protected String ACTION_DELETE = "action_delete";
	protected String ACTION_ADD_FROM_HAND = "ACTION_ADD_FROM_HAND";

	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public List<SeriennrChargennrMitMengeDto> alSeriennummernReturn = null;

	JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTableSnrChnrs = new JTable();
	JButton jButtonUebernehmen = new JButton();
	protected JLabel jLabelGesamtMenge = new JLabel();
	public WrapperSNRField tfSnrchnr = null;

	String[] colNames = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	Integer lagerIId = null;
	public Integer artikelIId = null;
	protected InternalFrame internalFrame = null;

	protected boolean areDifferentItemsAllowed = false;
	protected IArtikelIIdFinder artikelFinder;
	protected boolean bFuehrendeNullenWegschneiden = false;

	public DialogPositionenBarcodeerfassung(Integer lagerIId,
			InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		if (alSeriennummern != null) {
			this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>(
					alSeriennummern);
		} else {
			this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
		}
		this.lagerIId = lagerIId;
		this.internalFrame = internalFrame;
		setArtikelFinder(new FindAbzubuchendenArtikel());

		colNames = new String[] { LPMain.getInstance().getTextRespectUISPr(
				"artikel.seriennummer") };

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bFuehrendeNullenWegschneiden = true;
		}

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(300, 500);
		refreshFromArrayList();
		jTableSnrChnrs.setRowSelectionAllowed(true);

		jScrollPane.getViewport().add(jTableSnrChnrs);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		tfSnrchnr.requestFocusInWindow();

		SwingUtilities.invokeLater(new RequestFocusLater(tfSnrchnr));
		jButtonUebernehmen.grabFocus();
	}

	private static class RequestFocusLater implements Runnable {
		private Component comp;

		RequestFocusLater(Component src) {
			comp = src;
		}

		public void run() {
			comp.requestFocusInWindow();
		}
	}

	public boolean sindVerschiedeneArtikelErlaubt() {
		return areDifferentItemsAllowed;
	}

	public void setSindVerschiedeneArtikelErlaubt(boolean erlaubt) {
		areDifferentItemsAllowed = erlaubt;
	}

	/**
	 * Seriennnummern/Chargen vorbesetzen
	 * 
	 * @param snrs
	 *            ist die Liste der Seriennummern/Chargen
	 */
	public void setSeriennummern(List<SeriennrChargennrMitMengeDto> snrs) {
		alSeriennummern = snrs;
		refreshFromArrayList();
	}

	public void setArtikelFinder(IArtikelIIdFinder artikelFinder) {
		this.artikelFinder = artikelFinder;
	}

	public void dispose() {
		// Beim expliziten Close die moeglicherweise erfassten Artikel verwerfen
		alSeriennummern.clear();
		super.dispose();
	}

	public void tableChanged(TableModelEvent e) {

		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
			// Mengen updaten
			if (e.getSource().equals(jTableSnrChnrs.getModel())) {
				for (int i = 0; i < alSeriennummern.size(); i++) {

					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof String) {
						alSeriennummern.get(i).setNMenge(
								new BigDecimal4((String) jTableSnrChnrs
										.getModel().getValueAt(i, 1)));
					}
				}
			}

			refreshFromArrayList();
		}

	}

	public void add2List(SeriennrChargennrMitMengeDto dto) throws Throwable {

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);

		Integer ichnrlaenge = (Integer) parameter.getCWertAsObject();
		if (dto.getCSeriennrChargennr().length() < ichnrlaenge) {

			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("artikel.error.snrzukurz")
					+ " " + ichnrlaenge);
			return;

		}
		// Pruefen, ob bereits vorhanden
		for (int i = 0; i < alSeriennummern.size(); i++) {
			if (alSeriennummern.get(i).getCSeriennrChargennr().equals(
					dto.getCSeriennrChargennr())) {
				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.hint"), LPMain
						.getTextRespectUISPr("artikel.snr.doppelt")
						+ " " + alSeriennummern.get(i).getCSeriennrChargennr());
				return;
			}

		}
		alSeriennummern.add(dto);

		refreshFromArrayList();

	}

	protected void actionDelete(ActionEvent e) {
		int[] zeilen = jTableSnrChnrs.getSelectedRows();
		for (int i = zeilen.length; i > 0; i--) {
			alSeriennummern.remove(zeilen[i - 1]);

		}
		refreshFromArrayList();
	}

	protected void addNewItem(Integer artikelIId, String serialnumber)
			throws Throwable {
		add2List(new SeriennrChargennrMitMengeDto(serialnumber, BigDecimal.ONE));
	}

	protected Integer findArtikelIIdWithSerialnumber(String serialnumber)
			throws Throwable {
		return artikelFinder.findArtikelIIdWithSerialnumber(serialnumber);
		// Integer artikelIIdGefunden = DelegateFactory.getInstance()
		// .getLagerDelegate()
		// .getArtikelIIdUeberSeriennummer(serialnumber);
		// return artikelIIdGefunden ;
	}

	protected void actionAddFromHand(ActionEvent e) throws ExceptionLP,
			Throwable {
		if (tfSnrchnr.getText() == null) {

			DialogFactory.showModalDialog(LPMain
					.getTextRespectUISPr("lp.error"), LPMain
					.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
			return;
		}

		String[] snrs = tfSnrchnr.erzeugeSeriennummernArray(null, false);

		for (int i = 0; i < snrs.length; i++) {
			
			if (bFuehrendeNullenWegschneiden) {
				snrs[i] = snrs[i].replaceFirst("0*", "");
			}
			
			Integer artikelIIdGefunden = findArtikelIIdWithSerialnumber(snrs[i]);
			// Integer artikelIIdGefunden = DelegateFactory.getInstance()
			// .getLagerDelegate()
			// .getArtikelIIdUeberSeriennummer(snrs[i]);

			if (artikelIIdGefunden == null) {
				// Keine Artikelnummer gefunden
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain
										.getTextRespectUISPr("artikel.barcoderfassung.error.keinesnrgefunden")
										+ " " + snrs[i]);

				return;
			} else {

				if (artikelIId == null) {

					// Erster Artikel
					artikelIId = artikelIIdGefunden;
					addNewItem(artikelIIdGefunden, snrs[i]);
				} else {

					if (artikelIIdGefunden.equals(artikelIId)) {
						// hinzufuegen
						addNewItem(artikelIIdGefunden, snrs[i]);
					} else {
						// Fehler, anderer Artikel, abbruch
						if (!sindVerschiedeneArtikelErlaubt()) {
							DialogFactory
									.showModalDialog(
											LPMain
													.getTextRespectUISPr("lp.error"),
											LPMain
													.getTextRespectUISPr("artikel.barcoderfassung.error.verschiedeneartikel"));

							return;
						} else {
							addNewItem(artikelIIdGefunden, snrs[i]);
						}
					}

				}

			}

		}

		tfSnrchnr.setText(null);
		refreshFromArrayList();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_DELETE)) {
				actionDelete(e);
			} else if (e.getActionCommand().equals(ACTION_ADD_FROM_HAND)) {
				actionAddFromHand(e);
			}
		} catch (Throwable e1) {
			internalFrame.handleException(e1, true);
		}

	}

	protected void setGesamtMengeText(BigDecimal bdGesamt) throws Throwable {
		jLabelGesamtMenge.setText(Helper.formatZahl(bdGesamt, 4, LPMain
				.getInstance().getTheClient().getLocUi()));
	}

	public BigDecimal getGesamtMenge() {
		BigDecimal total = BigDecimal.ZERO;
		if (null == alSeriennummern)
			return total;

		for (SeriennrChargennrMitMengeDto serialnr : alSeriennummern) {
			if (null != serialnr) {
				total = total.add(serialnr.getNMenge());
			}
		}

		return total;
	}

	private void refreshFromArrayList() {
		Object[][] data = null;

		BigDecimal bdGesamt = new BigDecimal(0);

		if (alSeriennummern != null) {
			data = new String[alSeriennummern.size()][1];
			for (int i = 0; i < alSeriennummern.size(); i++) {
				if (alSeriennummern.get(i) != null) {
					data[i][0] = alSeriennummern.get(i).getCSeriennrChargennr();

					bdGesamt = bdGesamt.add(alSeriennummern.get(i).getNMenge());
				}
			}
		} else {
			data = new String[0][1];
		}

		jTableSnrChnrs = new JTable(new MyTableModel2(colNames, data));
		jTableSnrChnrs.getModel().addTableModelListener(this);
		jTableSnrChnrs.setRowSelectionAllowed(true);
		jTableSnrChnrs.repaint();
		jScrollPane.getViewport().add(jTableSnrChnrs);
		//SP942
		jTableSnrChnrs.scrollRectToVisible( jTableSnrChnrs.getCellRect( jTableSnrChnrs.getRowCount()-1, 0, true ) );//
		
		
		SwingUtilities.updateComponentTreeUI(jScrollPane);

		try {
			setGesamtMengeText(bdGesamt);
		} catch (Throwable e) {
			internalFrame.handleException(e, true);
		}

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogPositionenBarcodeerfassung_jButtonUebernehmen_actionAdapter(
						this));
		add(panel1);

		// Loeschen
		JButton buttonEntfernen = HelperClient.createButton(new ImageIcon(
				getClass().getResource("/com/lp/client/res/delete2.png")),
				LPMain.getTextRespectUISPr("artikel.snr.entfernen"),
				ACTION_DELETE);
		buttonEntfernen.setEnabled(true);
		buttonEntfernen.addActionListener(this);

		jLabelGesamtMenge.setMinimumSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));
		jLabelGesamtMenge.setPreferredSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));

		panel1.add(buttonEntfernen, new GridBagConstraints(0, 0, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		panel1.add(buttonEntfernen, new GridBagConstraints(3, 0, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		panel1.add(jLabelGesamtMenge, new GridBagConstraints(2, 0, 1, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(jScrollPane, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		tfSnrchnr = new WrapperSNRField();
		tfSnrchnr.setMandatoryField(true);
		tfSnrchnr.addKeyListener(this);

		JButton buttonNeuAusString = HelperClient.createButton(new ImageIcon(
				getClass().getResource("/com/lp/client/res/plus_sign.png")),
				LPMain.getTextRespectUISPr("lp.new"), ACTION_ADD_FROM_HAND);
		buttonNeuAusString.setEnabled(true);
		buttonNeuAusString.addActionListener(this);

		panel1.add(tfSnrchnr, new GridBagConstraints(1, 3, 1, 1, 1.0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 150, 0));

		panel1.add(new JLabel(LPMain.getInstance().getTextRespectUISPr(
				"bes.seriennummer_short")), new GridBagConstraints(1, 2, 1, 1,
				1.0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 0, 2, 0), 0, 0));

		panel1.add(buttonNeuAusString, new GridBagConstraints(3, 3, 1, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == tfSnrchnr) {

				actionPerformed(new ActionEvent(tfSnrchnr, 0,
						ACTION_ADD_FROM_HAND));

				tfSnrchnr.requestFocus();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		if (tfSnrchnr.getText() != null) {
			actionPerformed(new ActionEvent(this, 0, ACTION_ADD_FROM_HAND));
		}

		alSeriennummernReturn = alSeriennummern;
		setVisible(false);
	}

}

class DialogPositionenBarcodeerfassung_jButtonUebernehmen_actionAdapter
		implements ActionListener {
	private DialogPositionenBarcodeerfassung adaptee;

	DialogPositionenBarcodeerfassung_jButtonUebernehmen_actionAdapter(
			DialogPositionenBarcodeerfassung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel2 extends AbstractTableModel {
	private String[] columnNames = null;
	private Object[][] data = null;

	public MyTableModel2(String[] columnNames, Object[][] data) {
		this.columnNames = columnNames;
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {

		return false;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
