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
package com.lp.client.system;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVWriter;

@SuppressWarnings("static-access")
public class DialogExtraliste extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String LINE_BREAK = "\n";
	String CELL_BREAK = "\t";
	private WrapperTable table = null;
	private Integer extralisteIId = null;
	private JScrollPane jScrollPane1 = new JScrollPane();
	private boolean bError = false;
	private File sLetzteDatei = null;
	private String ACTION_CSV_EXPORT = "CSV_EXPORT";
	private String ACTION_PRINT = "ACTION_PRINT";
	private String ACTION_COPY_TO_CLIPBOARD = "ACTION_COPY_TO_CLIPBOARD";
	private String ACTION_COPY_TO_CLIPBOARD_HTML = "ACTION_COPY_TO_CLIPBOARD_HTML";
	public ExtralisteRueckgabeTabelleDto extralisteRueckgabeTabelleDto = null;
	public boolean bPrint = false;

	public DialogExtraliste(Integer extralisteIId) throws Throwable {
		super(LPMain.getInstance().getDesktop(), true);
		this.extralisteIId = extralisteIId;

		if (extralisteIId != null) {

			ExtralisteDto extralisteDto = DelegateFactory.getInstance()
					.getSystemDelegate()
					.extralisteFindByPrimaryKey(extralisteIId);
			if (extralisteDto.getXQuery() != null) {

				setTitle(extralisteDto.getCBez());

				if (extralisteDto.getIDialogbreite() != null) {
					this.setPreferredSize(new Dimension(extralisteDto
							.getIDialogbreite(), 500));
				} else {

					this.setPreferredSize(new Dimension(300, 500));
				}
				Rectangle r = LPMain.getInstance().getDesktop().getBounds();
				r.y = (int) r.getY() + 100;
				setBounds(r);

				setDefaultCloseOperation(DISPOSE_ON_CLOSE);

				jbInit();
				pack();
				setVisible(true);
			} else {
				// Meldung -> Kein Query

				setVisible(false);
			}
		} else {
			setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_CSV_EXPORT)) {

			try {

				if (extralisteRueckgabeTabelleDto != null
						&& extralisteRueckgabeTabelleDto.getData() != null) {
					JFileChooser fc = new JFileChooser();

					fc.setApproveButtonText(LPMain.getInstance()
							.getTextRespectUISPr("lp.report.save"));
					fc.setDialogTitle(LPMain.getInstance().getTextRespectUISPr(
							"lp.report.save"));

					if (sLetzteDatei != null) {
						fc.setCurrentDirectory(sLetzteDatei);
					}
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							return f.getName().toLowerCase().endsWith(".csv")
									|| f.isDirectory();
						}

						public String getDescription() {
							return "CSV-Dateien";
						}
					});

					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();

						sLetzteDatei = file;

						LPCSVWriter writer = new LPCSVWriter(new FileWriter(
								file), ',', LPCSVWriter.DEFAULT_QUOTE_CHARACTER);

						writer.writeNext(extralisteRueckgabeTabelleDto
								.getColumnNames());

						for (int i = 0; i < extralisteRueckgabeTabelleDto
								.getData().length; i++) {
							Object[] zeile = extralisteRueckgabeTabelleDto
									.getData()[i];

							String[] strArray = new String[zeile.length];

							for (int j = 0; j < zeile.length; j++) {
								if (zeile[j] != null) {
									strArray[j] = zeile[j].toString();
								} else {
									strArray[j] = "";
								}
							}

							writer.writeNext(strArray);

						}

						writer.close();

						DialogFactory.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.hinweis"),
								LPMain.getInstance().getTextRespectUISPr(
										"system.extraliste.gespeichert")
										+ " (" + file.getAbsolutePath() + ") ");

					}

				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} else if (e.getActionCommand().equals(ACTION_PRINT)) {

			bPrint = true;
			this.setVisible(false);

		} else if (e.getActionCommand().equals(ACTION_COPY_TO_CLIPBOARD)) {

			StringBuffer excelStr = new StringBuffer();

			if (extralisteRueckgabeTabelleDto != null
					&& extralisteRueckgabeTabelleDto.getData() != null) {

				// Ueberschriften
				for (int i = 0; i < extralisteRueckgabeTabelleDto
						.getColumnNames().length; i++) {
					excelStr.append(extralisteRueckgabeTabelleDto
							.getColumnNames()[i]);
					if (i < extralisteRueckgabeTabelleDto.getColumnNames().length - 1) {
						excelStr.append(CELL_BREAK);
					}

				}
				excelStr.append(LINE_BREAK);
				// Daten
				for (int i = 0; i < extralisteRueckgabeTabelleDto.getData().length; i++) {
					Object[] zeile = extralisteRueckgabeTabelleDto.getData()[i];

					for (int j = 0; j < zeile.length; j++) {

						if (zeile[j] instanceof Number) {
							try {
								excelStr.append(Helper.formatZahl(
										(Number) zeile[j], LPMain
												.getTheClient().getLocUi()));
							} catch (Throwable e1) {
								//
							}
						} else if (zeile[j] instanceof String) {
							String s = (String) zeile[j];
							excelStr.append("\""
									+ escape(s.replaceAll("\"", "\"\"")) + "\"");
						} else {
							excelStr.append(escape(zeile[j]));
						}

						if (j < zeile.length - 1) {
							excelStr.append(CELL_BREAK);
						}
					}
					excelStr.append(LINE_BREAK);
				}
			}

			Toolkit.getDefaultToolkit()
					.getSystemClipboard()
					.setContents(new StringSelection(excelStr.toString()), null);

		} else if (e.getActionCommand().equals(ACTION_COPY_TO_CLIPBOARD_HTML)) {
			StringBuffer htmlStr = new StringBuffer("<tr>");
			htmlStr.append(LINE_BREAK);
			if (extralisteRueckgabeTabelleDto != null
					&& extralisteRueckgabeTabelleDto.getData() != null) {

				// Ueberschriften
				for (int i = 0; i < extralisteRueckgabeTabelleDto
						.getColumnNames().length; i++) {

					htmlStr.append("  <td>");

					htmlStr.append(extralisteRueckgabeTabelleDto
							.getColumnNames()[i]);
					htmlStr.append("</td>");
					htmlStr.append(LINE_BREAK);

				}
				htmlStr.append("</tr>");
				htmlStr.append(LINE_BREAK);
				// Daten
				for (int i = 0; i < extralisteRueckgabeTabelleDto.getData().length; i++) {
					Object[] zeile = extralisteRueckgabeTabelleDto.getData()[i];

					htmlStr.append("<tr>");
					htmlStr.append(LINE_BREAK);

					for (int j = 0; j < zeile.length; j++) {
						htmlStr.append("  <td>");
						if (zeile[j] instanceof Number) {
							try {
								htmlStr.append(Helper.formatZahl(
										(Number) zeile[j], LPMain
												.getTheClient().getLocUi()));
							} catch (Throwable e1) {
								//
							}
						} if (zeile[j] instanceof Number) {
							try {
								htmlStr.append(Helper.formatZahl(
										(Number) zeile[j], LPMain
												.getTheClient().getLocUi()));
							} catch (Throwable e1) {
								//
							}
						} else if (zeile[j] instanceof String) {
							String s = (String) zeile[j];
							s=s.replaceAll(LINE_BREAK, "<br>");
							htmlStr.append(s);
						} else {
							htmlStr.append(escape(zeile[j]));
						}

						htmlStr.append("</td>");
						htmlStr.append(LINE_BREAK);
					}
					htmlStr.append("</tr>");
				}
			}

			htmlStr.append("</tr>");

			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(new StringSelection(htmlStr.toString()), null);
		}
	}

	private String escape(Object cell) {
		if (cell == null) {
			return "";
		} else {
			return cell.toString().replace(LINE_BREAK, " ")
					.replace(CELL_BREAK, " ");
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	public void setVisible(boolean b) {
		if (b == false) {
			super.setVisible(b);
		} else {
			if (bError == false) {
				super.setVisible(true);
			} else {
				super.setVisible(false);
				this.dispose();
			}
		}
	}

	private void jbInit() throws Throwable {

		ImageIcon iiCopy = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/copy.png"));
		ImageIcon iiCopyHtml = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/download.png"));

		JButton buttoncc = new JButton();
		buttoncc.setToolTipText(LPMain
				.getTextRespectUISPr("lp.inzwischenablagekopieren"));
		buttoncc.setActionCommand(ACTION_COPY_TO_CLIPBOARD);
		buttoncc.setIcon(iiCopy);
		buttoncc.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttoncc.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttoncc.addActionListener(this);

		JButton buttoncchtml = new JButton();
		buttoncchtml.setToolTipText(LPMain
				.getTextRespectUISPr("lp.inzwischenablagekopieren.htmltable"));
		buttoncchtml.setActionCommand(ACTION_COPY_TO_CLIPBOARD_HTML);
		buttoncchtml.setIcon(iiCopyHtml);
		buttoncchtml
				.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttoncchtml.setPreferredSize(HelperClient
				.getToolsPanelButtonDimension());
		buttoncchtml.addActionListener(this);

		ImageIcon ii = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/goto.png"));

		JButton buttoncsv = new JButton();
		buttoncsv.setToolTipText("CSV");
		buttoncsv.setActionCommand(ACTION_CSV_EXPORT);
		buttoncsv.setIcon(ii);

		buttoncsv.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttoncsv.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttoncsv.addActionListener(this);

		// PJ 14531

		ImageIcon iiPrint = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/table_sql_view.png"));

		JButton buttonPrint = new JButton();
		buttonPrint.setToolTipText(LPMain.getTextRespectUISPr("lp.printer"));
		buttonPrint.setActionCommand(ACTION_PRINT);
		buttonPrint.setIcon(iiPrint);

		buttonPrint.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonPrint.setPreferredSize(HelperClient
				.getToolsPanelButtonDimension());
		buttonPrint.addActionListener(this);

		// Daten holen
		try {
			extralisteRueckgabeTabelleDto = DelegateFactory.getInstance()
					.getSystemDelegate().generiereExtraliste(extralisteIId);
		} catch (ExceptionLP ex) {
			bError = true;
			if (ex.getICode() == EJBExceptionLP.FEHLER_HIBERNATE) {

				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"),
						"Hibernate-Exception: " + ex.getSMsg());

				this.dispose();
				setVisible(false);
			} else {
				throw ex;
			}

		}
		if (extralisteRueckgabeTabelleDto != null
				&& extralisteRueckgabeTabelleDto.getData() != null) {
			table = new WrapperTable(null,
					extralisteRueckgabeTabelleDto.getData(),
					extralisteRueckgabeTabelleDto.getColumnNames());
		} else {
			table = new WrapperTable(null);
		}

		table.setCellSelectionEnabled(false);

		table.setAutoCreateRowSorter(true);

		jScrollPane1.getViewport().add(table);

		JPanel jpaWorkingOn = new JPanel();
		GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		add(jpaWorkingOn);

		jpaWorkingOn.add(buttoncsv, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(buttoncc, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(buttoncchtml, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(buttonPrint, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(jScrollPane1, new GridBagConstraints(0, 1, 3, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 000, 0));

	}

}
