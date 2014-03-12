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
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.NumberColumnFormat;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.BigDecimal4;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogSerienChargenauswahl extends JDialog implements
		ActionListener, KeyListener, TableModelListener {

	private static final long serialVersionUID = 1L;
	private String ACTION_NEW_FROM_LAGER = "action_new_from_lager";
	private String ACTION_DELETE = "action_delete";
	private String ACTION_ADD_FROM_HAND = "ACTION_ADD_FROM_HAND";

	ArtikelDto artikelDto = null;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public List<SeriennrChargennrMitMengeDto> alSeriennummernReturn = null;

	JScrollPane jScrollPane1 = new JScrollPane();
	WrapperTableEditable jTableSnrChnrs = new WrapperTableEditable();
	JButton jButtonUebernehmen = new JButton();
	JLabel jLabelGesamtMenge = new JLabel();
	JLabel jLabelBenoetigt = new JLabel();
	WrapperNumberField wnfSnrchnr = null;
	public WrapperSNRField tfSnrchnr = null;
	public WrapperCheckBox wcbRueckgabe = new WrapperCheckBox();
	private WrapperDateField wdfMHD = new WrapperDateField();

	String[] colNames = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummernBereitsSelektiert = null;
	Integer artikelIId = null;
	Integer lagerIId = null;
	InternalFrame internalFrame = null;
	private boolean bMindesthaltbarkeitsdatum = false;
	private boolean bFuehrendeNullenWegschneiden = false;
	boolean selektierteNichtAnzeigen = false;

	public JLabel getLabelBenoetigt() {
		return jLabelBenoetigt;
	}

	public DialogSerienChargenauswahl(Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennummern,
			boolean bMultiselection, boolean selektierteNichtAnzeigen,
			InternalFrame internalFrame, WrapperNumberField wnfBeleg)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.selektierteNichtAnzeigen = selektierteNichtAnzeigen;

		if (alSeriennummern != null) {

			if (selektierteNichtAnzeigen == true) {
				alSeriennummernBereitsSelektiert = new ArrayList<SeriennrChargennrMitMengeDto>(
						alSeriennummern);
				this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
			} else {
				this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>(
						alSeriennummern);
			}

		} else {
			this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
			alSeriennummernBereitsSelektiert = new ArrayList<SeriennrChargennrMitMengeDto>();
		}

		this.artikelIId = artikelIId;
		this.lagerIId = lagerIId;
		this.internalFrame = internalFrame;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bMindesthaltbarkeitsdatum = true;
		}
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bFuehrendeNullenWegschneiden = true;
		}
		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(artikelIId);

		setTitle("Auswahl Seriennummern f\u00FCr Artikel "
				+ artikelDto.formatArtikelbezeichnung());

		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			colNames = new String[] { LPMain.getInstance().getTextRespectUISPr(
					"artikel.seriennummer") };
		} else {
			colNames = new String[] {
					LPMain.getInstance().getTextRespectUISPr(
							"lp.chargennummer_lang"),
					LPMain.getInstance().getTextRespectUISPr("lp.menge") };
		}
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(500, 500);
		refreshFromArrayList();

		if (wnfBeleg != null && wnfBeleg.getBigDecimal() != null
				&& wnfBeleg.getBigDecimal().doubleValue() < 0) {
			wcbRueckgabe.setSelected(true);
		}
		jTableSnrChnrs.setRowSelectionAllowed(true);

		if (bMultiselection) {
			jTableSnrChnrs
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			jTableSnrChnrs
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		jScrollPane.getViewport().add(jTableSnrChnrs);

		if (jTableSnrChnrs.getModel().getRowCount() > 0) {
			jTableSnrChnrs.changeSelection(0, 0, false, false);
		}
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
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof BigDecimal) {
						alSeriennummern.get(i).setNMenge(
								(BigDecimal) jTableSnrChnrs.getModel()
										.getValueAt(i, 1));
					}
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof Double) {
						alSeriennummern.get(i).setNMenge(
								new BigDecimal4((Double) jTableSnrChnrs
										.getModel().getValueAt(i, 1)));
					}
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof Long) {
						alSeriennummern.get(i).setNMenge(
								new BigDecimal4((Long) jTableSnrChnrs
										.getModel().getValueAt(i, 1)));
					}
				}
			}

			refreshFromArrayList();

			jTableSnrChnrs.changeSelection(e.getFirstRow(), e.getColumn(),
					false, false);
			requestFocusInWindow();
		}

	}

	public boolean add2List(SeriennrChargennrMitMengeDto dto) throws Throwable {
		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);

			Integer ichnrlaenge = (Integer) parameter.getCWertAsObject();
			if (dto.getCSeriennrChargennr().length() < ichnrlaenge) {

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("artikel.error.snrzukurz")
								+ " " + ichnrlaenge);
				return false;

			}
			// Pruefen, ob bereits vorhanden
			for (int i = 0; i < alSeriennummern.size(); i++) {

				if (alSeriennummern.get(i).getCSeriennrChargennr()
						.equals(dto.getCSeriennrChargennr())) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("artikel.snr.doppelt")
									+ " "
									+ alSeriennummern.get(i)
											.getCSeriennrChargennr());
					return false;
				}

			}
			alSeriennummern.add(dto);

			refreshFromArrayList();

			// Focus in Zelle setzen
			jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel()
					.getRowCount() - 1, 0, false, false);

			if (jTableSnrChnrs.getModel().getRowCount() > 0) {

				jTableSnrChnrs.requestFocus();
				DefaultCellEditor ed = (DefaultCellEditor) jTableSnrChnrs
						.getCellEditor(
								jTableSnrChnrs.getModel().getRowCount() - 1, 0);

				ed.shouldSelectCell(new ListSelectionEvent(this, jTableSnrChnrs
						.getModel().getRowCount() - 1, jTableSnrChnrs
						.getModel().getRowCount() - 1, true));

			}

		} else {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE);

			Integer ichnrlaenge = (Integer) parameter.getCWertAsObject();
			if (dto.getCSeriennrChargennr().length() < ichnrlaenge) {

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("artikel.error.chnrzukurz")
								+ " " + ichnrlaenge);
				return false;

			}
			alSeriennummern.add(dto);

			refreshFromArrayList();
			// Focus in Zelle setzen
			jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel()
					.getRowCount() - 1, 1, false, false);

			if (jTableSnrChnrs.getModel().getRowCount() > 0) {
				jTableSnrChnrs.editCellAt(jTableSnrChnrs.getModel()
						.getRowCount() - 1, 1);

				jTableSnrChnrs.requestFocus();
				DefaultCellEditor ed = (DefaultCellEditor) jTableSnrChnrs
						.getCellEditor(
								jTableSnrChnrs.getModel().getRowCount() - 1, 1);

				ed.shouldSelectCell(new ListSelectionEvent(this, jTableSnrChnrs
						.getModel().getRowCount() - 1, jTableSnrChnrs
						.getModel().getRowCount() - 1, true));

			}

		}

		return true;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_DELETE)) {

				int[] zeilen = jTableSnrChnrs.getSelectedRows();
				for (int i = zeilen.length; i > 0; i--) {
					alSeriennummern.remove(zeilen[i - 1]);

				}
				refreshFromArrayList();

				jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel()
						.getRowCount() - 1, jTableSnrChnrs.getModel()
						.getColumnCount(), false, false);

			} else if (e.getActionCommand().equals(ACTION_ADD_FROM_HAND)) {

				if (bMindesthaltbarkeitsdatum == true) {
					if (tfSnrchnr.getText() == null
							|| wnfSnrchnr.getBigDecimal() == null
							|| wdfMHD.getTimestamp() == null) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				} else {
					if (tfSnrchnr.getText() == null
							|| wnfSnrchnr.getBigDecimal() == null) {

						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				}

				if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

					String[] snrs = tfSnrchnr.erzeugeSeriennummernArray(null,
							false);

					for (int i = 0; i < snrs.length; i++) {

						if (bFuehrendeNullenWegschneiden) {
							snrs[i] = snrs[i].replaceFirst("0*", "");
						}

						boolean bHatFunktioniert = add2List(new SeriennrChargennrMitMengeDto(
								snrs[i], new BigDecimal(1)));
						if (bHatFunktioniert == false) {
							break;
						}
					}
				} else {

					if (bMindesthaltbarkeitsdatum == true) {

						StringBuffer s = new StringBuffer();

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(wdfMHD.getTimestamp().getTime());

						int iTag = c.get(Calendar.DAY_OF_MONTH);
						int iMonat = c.get(Calendar.MONTH) + 1;
						int iJahr = c.get(Calendar.YEAR);

						s.append(iJahr);

						if (iMonat < 10) {
							s.append("0").append(iMonat);
						} else {
							s.append(iMonat);
						}
						if (iTag < 10) {
							s.append("0").append(iTag);
						} else {
							s.append(iTag);
						}

						if (tfSnrchnr.getText() != null) {
							s.append(tfSnrchnr.getText());
						}
						add2List(new SeriennrChargennrMitMengeDto(
								new String(s), wnfSnrchnr.getBigDecimal()));
					} else {
						add2List(new SeriennrChargennrMitMengeDto(
								tfSnrchnr.getText(), wnfSnrchnr.getBigDecimal()));
					}

				}
				tfSnrchnr.setText(null);

			} else if (e.getActionCommand().equals(ACTION_NEW_FROM_LAGER)) {

				DialogSnrChnrauswahl d = null;
				if (selektierteNichtAnzeigen == true) {
					d = new DialogSnrChnrauswahl(artikelIId, lagerIId, true,
							alSeriennummernBereitsSelektiert);

				} else {
					d = new DialogSnrChnrauswahl(artikelIId, lagerIId, true,
							alSeriennummern);
				}

				LPMain.getInstance().getDesktop()
						.platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				List<SeriennrChargennrMitMengeDto> s = d.alSeriennummern;

				for (int i = 0; i < s.size(); i++) {
					add2List(s.get(i));
				}

			}
		} catch (EJBExceptionLP e1) {
			internalFrame.handleException(new ExceptionLP(e1.getCode(), e1),
					false);

		} catch (Throwable e1) {
			internalFrame.handleException(e1, false);
		}

	}

	private void refreshFromArrayList() {
		Object[][] data = null;

		BigDecimal bdGesamt = new BigDecimal(0);

		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			if (alSeriennummern != null) {
				data = new String[alSeriennummern.size()][1];
				for (int i = 0; i < alSeriennummern.size(); i++) {
					if (alSeriennummern.get(i) != null) {
						data[i][0] = alSeriennummern.get(i)
								.getCSeriennrChargennr();

						bdGesamt = bdGesamt.add(alSeriennummern.get(i)
								.getNMenge());
					}
				}
			} else {
				data = new String[0][1];
			}

		} else {
			if (alSeriennummern != null) {
				data = new Object[alSeriennummern.size()][2];
				for (int i = 0; i < alSeriennummern.size(); i++) {
					if (alSeriennummern.get(i) != null) {
						data[i][0] = alSeriennummern.get(i)
								.getCSeriennrChargennr();

						data[i][1] = new BigDecimal4(alSeriennummern.get(i)
								.getNMenge());
						bdGesamt = bdGesamt.add(alSeriennummern.get(i)
								.getNMenge());
					}
				}
			} else {
				data = new Object[0][2];
			}

		}

		jTableSnrChnrs = new WrapperTableEditable(new MyTableModel1(colNames,
				data));

		jTableSnrChnrs.getModel().addTableModelListener(this);

		if (!Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

			try {
				NumberColumnFormat numberCF2 = new NumberColumnFormat(
						"###,###,###.0000");
				TableColumn tc = jTableSnrChnrs.getColumnModel().getColumn(1);
				tc.setCellRenderer(numberCF2.getRenderer());
				tc.setCellEditor(numberCF2.getEditor());

			} catch (Throwable e1) {
				internalFrame.handleException(e1, true);
			}
			jTableSnrChnrs.setSurrendersFocusOnKeystroke(true);
		}
		jTableSnrChnrs.setRowSelectionAllowed(true);
		jTableSnrChnrs.repaint();

		jScrollPane.getViewport().add(jTableSnrChnrs);
		SwingUtilities.updateComponentTreeUI(jScrollPane);

		try {
			jLabelGesamtMenge.setText("Ausgew\u00E4hlt: "
					+ Helper.formatZahl(bdGesamt, 4, LPMain.getInstance()
							.getTheClient().getLocUi()));
		} catch (Throwable e) {
			internalFrame.handleException(e, true);
		}

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogSerienChargenauswahl_jButtonUebernehmen_actionAdapter(
						this));
		jButtonUebernehmen.setMnemonic('B');

		add(panel1);

		// Loeschen
		JButton buttonEntfernen = HelperClient.createButton(new ImageIcon(
				getClass().getResource("/com/lp/client/res/delete2.png")),
				LPMain.getTextRespectUISPr("artikel.snr.entfernen"),
				ACTION_DELETE);
		buttonEntfernen.setEnabled(true);
		buttonEntfernen.addActionListener(this);

		buttonEntfernen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke('D',
						java.awt.event.InputEvent.CTRL_MASK), ACTION_DELETE);
		buttonEntfernen.getActionMap().put(ACTION_DELETE,
				new ButtonAbstractAction(this, ACTION_DELETE));

		panel1.add(buttonEntfernen, new GridBagConstraints(0, 0, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		JButton buttonNeuAusLager = HelperClient.createButton(new ImageIcon(
				getClass().getResource("/com/lp/client/res/document.png")),
				LPMain.getTextRespectUISPr("artikel.snr.neuauslager"),
				ACTION_NEW_FROM_LAGER);
		buttonNeuAusLager.setEnabled(true);
		buttonNeuAusLager.addActionListener(this);
		buttonNeuAusLager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke('N',
						java.awt.event.InputEvent.CTRL_MASK),
						ACTION_NEW_FROM_LAGER);
		buttonNeuAusLager.getActionMap().put(ACTION_NEW_FROM_LAGER,
				new ButtonAbstractAction(this, ACTION_NEW_FROM_LAGER));

		panel1.add(buttonEntfernen, new GridBagConstraints(3, 0, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(buttonNeuAusLager, new GridBagConstraints(0, 0, 1, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(jLabelBenoetigt, new GridBagConstraints(1, 0, 1, 1, 1.0, 0,
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

		panel1.add(new JLabel(LPMain.getTextRespectUISPr("label.menge")),
				new GridBagConstraints(2, 2, 1, 1, 1.0, 0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 0, 2, 0), 0, 0));

		panel1.add(tfSnrchnr, new GridBagConstraints(1, 3, 1, 1, 1.0, 0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 150, 0));

		wdfMHD.setMandatoryField(true);

		wnfSnrchnr = new WrapperNumberField();
		wnfSnrchnr.setFractionDigits(4);
		wnfSnrchnr.setMinimumValue(0);
		wnfSnrchnr.setMandatoryField(true);
		wnfSnrchnr.addKeyListener(this);

		JLabel labnelsnrChnr = null;

		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

			labnelsnrChnr = new JLabel(LPMain.getInstance()
					.getTextRespectUISPr("bes.seriennummer_short"));

			panel1.add(labnelsnrChnr, new GridBagConstraints(1, 2, 1, 1, 1.0,
					0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 0, 2, 0), 0, 0));

			wnfSnrchnr.setBigDecimal(new BigDecimal(1));
			labnelsnrChnr.setLabelFor(tfSnrchnr);
		} else {
			labnelsnrChnr = new JLabel(LPMain.getInstance()
					.getTextRespectUISPr("lp.chargennummer_lang"));
			panel1.add(labnelsnrChnr, new GridBagConstraints(1, 2, 1, 1, 1.0,
					0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 0, 2, 0), 0, 0));
			labnelsnrChnr.setLabelFor(tfSnrchnr);

			if (bMindesthaltbarkeitsdatum == true) {
				panel1.add(
						new JLabel(LPMain.getInstance().getTextRespectUISPr(
								"artikel.journal.mindesthaltbarkeit")),
						new GridBagConstraints(0, 2, 1, 1, 1.0, 0,
								GridBagConstraints.WEST,
								GridBagConstraints.BOTH,
								new Insets(2, 0, 2, 0), 0, 0));
				panel1.add(wdfMHD, new GridBagConstraints(0, 3, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), -10, 0));

			}
		}

		labnelsnrChnr.setDisplayedMnemonic('N');

		panel1.add(wnfSnrchnr, new GridBagConstraints(2, 3, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 2, 0, 0), 50, 0));

		wcbRueckgabe.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.rueckgabe"));

		if (internalFrame instanceof InternalFrameLieferschein) {
			panel1.add(wcbRueckgabe, new GridBagConstraints(3, 2, 1, 1, 1.0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 2, 0, 0), 50, 0));
		}

		panel1.add(buttonNeuAusString, new GridBagConstraints(3, 3, 1, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 4, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == tfSnrchnr || e.getSource() == wnfSnrchnr) {

				actionPerformed(new ActionEvent(tfSnrchnr, 0,
						ACTION_ADD_FROM_HAND));

				tfSnrchnr.requestFocus();
			}

		}

		if (e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			int iRowCount = jTableSnrChnrs.getRowCount();
			int iSelectedRow = jTableSnrChnrs.getSelectedRow();
			if (e.getKeyCode() == KeyEvent.VK_UP && iSelectedRow > 0) {
				jTableSnrChnrs.setRowSelectionInterval(iSelectedRow - 1,
						iSelectedRow - 1);

			} else if (e.getKeyCode() == KeyEvent.VK_DOWN
					&& iSelectedRow < (iRowCount - 1)) {
				jTableSnrChnrs.setRowSelectionInterval(iSelectedRow + 1,
						iSelectedRow + 1);

			}
			int indexOfSelectedRow = this.jTableSnrChnrs.getSelectedRow();
			jTableSnrChnrs.scrollRectToVisible(jTableSnrChnrs.getCellRect(
					indexOfSelectedRow, 0, true));
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		if (tfSnrchnr.getText() != null && wnfSnrchnr.getText() != null) {
			actionPerformed(new ActionEvent(this, 0, ACTION_ADD_FROM_HAND));
		}

		alSeriennummernReturn = alSeriennummern;
		setVisible(false);
	}

	public class ButtonAbstractAction extends AbstractAction {
		private static final long serialVersionUID = -8681526632257782909L;
		private DialogSerienChargenauswahl adaptee;
		private String sActionCommand = null;

		public ButtonAbstractAction(DialogSerienChargenauswahl adaptee,
				String sActionCommandI) {
			this.adaptee = adaptee;
			sActionCommand = sActionCommandI;
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			ActionEvent ae = new ActionEvent(this, 1, sActionCommand);
			adaptee.actionPerformed(ae);
		}

		public void setSActionCommand(String sActionCommand) {

			this.sActionCommand = sActionCommand;
		}

		public String getSActionCommand() {
			return sActionCommand;
		}
	}

}

class DialogSerienChargenauswahl_jButtonUebernehmen_actionAdapter implements
		ActionListener {
	private DialogSerienChargenauswahl adaptee;

	DialogSerienChargenauswahl_jButtonUebernehmen_actionAdapter(
			DialogSerienChargenauswahl adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel1 extends AbstractTableModel {
	private static final long serialVersionUID = -6311652299531724223L;
	private String[] columnNames = null;
	private Object[][] data = null;

	public MyTableModel1(String[] columnNames, Object[][] data) {

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

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col == 1) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
