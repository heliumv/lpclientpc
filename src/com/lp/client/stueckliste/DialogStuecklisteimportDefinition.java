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
package com.lp.client.stueckliste;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;

//VS4E -- DO NOT REMOVE THIS LINE!
public class DialogStuecklisteimportDefinition extends JDialog {

		private static final long serialVersionUID = 1L;
		private JTextArea txtVorschau;
		private JTable tblVorschau;
		private JScrollPane jScrollPane0;
		private JLabel jLabel0;
		private JTextField txtAnzahlKopfzeilen;
		private JScrollPane jScrollPane1;
		private JButton btnBauform;
		private JButton btnMenge;
		private JButton btnBauteil;
		private JButton btnPosition;
		private JTextField txtEndeArtikel;
		private JTextField txtEndeBauform;
		private JTextField txtEndePosition;
		private JTextField txtEndeMenge;
		private JTextField txtStartMenge;
		private JTextField txtStartBauform;
		private JTextField txtStartArtikel;
		private JTextField txtStartPosition;
		private JLabel jLabel2;
		private JLabel jLabel1;
		private JLabel jLabel3;
		private JLabel jLabel4;
		private JButton btnVorschau;
		private JPanel jPanel0;
		private JLabel jLabel6;
		private JTextField txtTrennzeichen;
		private JCheckBox cbKopfMitFeldnamen;
		private JLabel jLabel7;
		private JTextField txtFile;
		private JButton btnOpen;
		private File sLastImportDirectory;
		private JCheckBox cbMengeEins;
		private JButton btnAbbrechen;
		private JButton btnImport;
		private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";

		private static final int iOffset = 4;
		private int iZeilenLaenge;
		private boolean bUnterschiedZeilen;
		private Integer stuecklisteIId;
		
		public DialogStuecklisteimportDefinition(Integer stuecklisteIId) {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setModal(true);
			this.stuecklisteIId = stuecklisteIId;
			initComponents();
		}

		private void readfile() {
			txtVorschau.setText("");
			Highlighter h = txtVorschau.getHighlighter();
			h.removeAllHighlights();
			StringBuffer sb = new StringBuffer();
			iZeilenLaenge = -1;
			bUnterschiedZeilen = false;
			// max. Zeilenlaenge ermitteln
			try {
				BufferedReader reader = new BufferedReader(new FileReader(txtFile.getText()));
				String sLine;
				int i = Integer.parseInt(txtAnzahlKopfzeilen.getText());
				int j = 1;
				while ( (sLine = reader.readLine()) != null) {
					if (i==0) {
						if (sLine.trim().length() > 0) {
							if (iZeilenLaenge == -1) { 
								iZeilenLaenge = sLine.length();
							} else {
								if (iZeilenLaenge < sLine.length()) {
									bUnterschiedZeilen = true;
									iZeilenLaenge = sLine.length();
								} else if (iZeilenLaenge > sLine.length()) {
									bUnterschiedZeilen = true;
								}
							}
						}
					} else {
						if (sLine.trim().length() != 0) {
							i--;
							j++;
						}
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(new FileReader(txtFile.getText()));
				String sLine;
				int i = Integer.parseInt(txtAnzahlKopfzeilen.getText());
				int j = 1;
				while ( (sLine = reader.readLine()) != null) {
					if (i==0) {
						if (sLine.trim().length() > 0) {
							if (sLine.length() < iZeilenLaenge) {
								StringBuffer temp = new StringBuffer(sLine);
								temp.setLength(iZeilenLaenge);
								sLine = temp.toString();
							}
								
							sb.append(String.format("%3d ", j++));
							sb.append(sLine);
							sb.append("\n");
						}
					} else {
						if (sLine.trim().length() != 0) {
							i--;
							j++;
						}
					}
				}
				txtVorschau.setText(new String(sb));
				txtVorschau.setCaretPosition(0);
				h = txtVorschau.getHighlighter();
				for (int ii=0; ii<txtVorschau.getLineCount(); ii++) {
					try {
						int iS = txtVorschau.getLineStartOffset(ii);
						h.addHighlight(iS,iS+iOffset,new DefaultHighlighter.DefaultHighlightPainter(Color.gray));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (bUnterschiedZeilen)
					JOptionPane.showMessageDialog(this, "Die Importdatei hat unterschiedliche Zeilenl\u00E4ngen.", "Info", JOptionPane.OK_OPTION );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void initComponents() {
			setTitle("Import Spezifikation");
			setMinimumSize(new Dimension(800, 600));
			setLayout(new GroupLayout());
			add(getTxtFile(), new Constraints(new Leading(128, 361, 10, 10), new Leading(12, 519, 519)));
			add(getJLabel7(), new Constraints(new Leading(48, 10, 10), new Leading(14, 517, 517)));
			add(getJLabel0(), new Constraints(new Leading(10, 12, 12), new Leading(38, 333, 495)));
			add(getTxtAnzahlKopfzeilen(), new Constraints(new Leading(128, 64, 12, 12), new Leading(36, 333, 495)));
			add(getJLabel6(), new Constraints(new Leading(201, 10, 10), new Leading(38, 333, 495)));
			add(getTxtTrennzeichen(), new Constraints(new Leading(285, 25, 10, 10), new Leading(36, 333, 495)));
			add(getCbKopfMitFeldnamen(), new Constraints(new Leading(320, 8, 8), new Leading(34, 329, 491)));
			add(getBtnOpen(), new Constraints(new Leading(501, 12, 12), new Leading(9, 327, 2898)));
			add(getJPanel0(), new Constraints(new Leading(12, 485, 10, 10), new Leading(298, 147, 10, 10)));
			add(getJScrollPane1(), new Constraints(new Bilateral(10, 12, 22), new Leading(68, 231, 10, 10)));
			add(getJScrollPane0(), new Constraints(new Bilateral(11, 9, 22), new Bilateral(446, 44, 26)));
			add(getBtnVorschau(), new Constraints(new Trailing(12, 100, 460, 509), new Leading(406, 28, 76, 469)));
			add(getBtnAbbrechen(), new Constraints(new Trailing(12, 100, 12, 12), new Trailing(12, 444, 469)));
			add(getBtnImport(), new Constraints(new Trailing(118, 100, 12, 12), new Trailing(12, 102, 469)));
			setSize(878, 600);
		}

		private JButton getBtnImport() {
			if (btnImport == null) {
				btnImport = new JButton();
				btnImport.setText("Import");
				btnImport.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnImportActionActionPerformed(event);
					}
				});
			}
			return btnImport;
		}

		private JButton getBtnAbbrechen() {
			if (btnAbbrechen == null) {
				btnAbbrechen = new JButton();
				btnAbbrechen.setText("Abbrechen");
				btnAbbrechen.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnAbbrechenActionActionPerformed(event);
					}
				});
			}
			return btnAbbrechen;
		}

		private JCheckBox getCbMengeEins() {
			if (cbMengeEins == null) {
				cbMengeEins = new JCheckBox();
				cbMengeEins.setSelected(true);
				cbMengeEins.setText("Menge=1");
			}
			return cbMengeEins;
		}

		private JButton getBtnOpen() {
			if (btnOpen == null) {
				btnOpen = new JButton();
				btnOpen.setText("...");
				btnOpen.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnOpenActionActionPerformed(event);
					}
				});
			}
			return btnOpen;
		}

		private JTextField getTxtFile() {
			if (txtFile == null) {
				txtFile = new JTextField();
				txtFile.setEditable(false);
			}
			return txtFile;
		}

		private JLabel getJLabel7() {
			if (jLabel7 == null) {
				jLabel7 = new JLabel();
				jLabel7.setText("Dateiname:");
			}
			return jLabel7;
		}

		private JCheckBox getCbKopfMitFeldnamen() {
			if (cbKopfMitFeldnamen == null) {
				cbKopfMitFeldnamen = new JCheckBox();
				cbKopfMitFeldnamen.setText("Kopfzeile mit Feldnamen:");
			}
			return cbKopfMitFeldnamen;
		}

		private JTextField getTxtTrennzeichen() {
			if (txtTrennzeichen == null) {
				txtTrennzeichen = new JTextField();
				txtTrennzeichen.addFocusListener(new FocusAdapter() {
		
					public void focusLost(FocusEvent event) {
						txtTrennzeichenFocusFocusLost(event);
					}
				});
			}
			return txtTrennzeichen;
		}

		private JLabel getJLabel6() {
			if (jLabel6 == null) {
				jLabel6 = new JLabel();
				jLabel6.setText("Trennzeichen:");
			}
			return jLabel6;
		}

		private JPanel getJPanel0() {
			if (jPanel0 == null) {
				jPanel0 = new JPanel();
				jPanel0.setLayout(new GroupLayout());
				jPanel0.add(getBtnBauteil(), new Constraints(new Leading(295, 12, 12), new Leading(7, 12, 12)));
				jPanel0.add(getBtnBauform(), new Constraints(new Leading(295, 12, 12), new Leading(36, 12, 12)));
				jPanel0.add(getBtnMenge(), new Constraints(new Leading(295, 12, 12), new Leading(66, 10, 10)));
				jPanel0.add(getBtnPosition(), new Constraints(new Leading(295, 12, 12), new Leading(96, 12, 12)));
				jPanel0.add(getTxtEndePosition(), new Constraints(new Leading(213, 70, 12, 12), new Leading(99, 12, 12)));
				jPanel0.add(getTxtEndeMenge(), new Constraints(new Leading(213, 70, 12, 12), new Leading(70, 12, 12)));
				jPanel0.add(getTxtEndeBauform(), new Constraints(new Leading(213, 70, 12, 12), new Leading(40, 12, 12)));
				jPanel0.add(getTxtEndeArtikel(), new Constraints(new Leading(213, 70, 12, 12), new Leading(10, 12, 12)));
				jPanel0.add(getTxtStartPosition(), new Constraints(new Leading(132, 72, 12, 12), new Leading(99, 12, 12)));
				jPanel0.add(getTxtStartMenge(), new Constraints(new Leading(132, 72, 12, 12), new Leading(70, 12, 12)));
				jPanel0.add(getTxtStartBauform(), new Constraints(new Leading(132, 72, 12, 12), new Leading(39, 12, 12)));
				jPanel0.add(getTxtStartArtikel(), new Constraints(new Leading(132, 72, 12, 12), new Leading(10, 12, 12)));
				jPanel0.add(getJLabel4(), new Constraints(new Leading(16, 12, 12), new Leading(101, 12, 12)));
				jPanel0.add(getJLabel3(), new Constraints(new Leading(79, 12, 12), new Leading(71, 12, 12)));
				jPanel0.add(getJLabel2(), new Constraints(new Leading(68, 12, 12), new Leading(41, 12, 12)));
				jPanel0.add(getCbMengeEins(), new Constraints(new Leading(351, 8, 8), new Leading(67, 8, 8)));
				jPanel0.add(getJLabel1(), new Constraints(new Leading(78, 12, 12), new Leading(12, 12, 12)));
			}
			return jPanel0;
		}

		private JButton getBtnVorschau() {
			if (btnVorschau == null) {
				btnVorschau = new JButton();
				btnVorschau.setText("Vorschau");
				btnVorschau.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						cmdVorschauActionActionPerformed(event);
					}
				});
			}
			return btnVorschau;
		}

		private JLabel getJLabel4() {
			if (jLabel4 == null) {
				jLabel4 = new JLabel();
				jLabel4.setText("Positionsnummer:");
			}
			return jLabel4;
		}

		private JLabel getJLabel3() {
			if (jLabel3 == null) {
				jLabel3 = new JLabel();
				jLabel3.setText("Menge:");
			}
			return jLabel3;
		}

		private JLabel getJLabel1() {
			if (jLabel1 == null) {
				jLabel1 = new JLabel();
				jLabel1.setText("Bauteil:");
			}
			return jLabel1;
		}

		private JLabel getJLabel2() {
			if (jLabel2 == null) {
				jLabel2 = new JLabel();
				jLabel2.setText("Bauform:");
			}
			return jLabel2;
		}

		private JTextField getTxtStartPosition() {
			if (txtStartPosition == null) {
				txtStartPosition = new JTextField();
			}
			return txtStartPosition;
		}

		private JTextField getTxtStartArtikel() {
			if (txtStartArtikel == null) {
				txtStartArtikel = new JTextField();
			}
			return txtStartArtikel;
		}

		private JTextField getTxtStartBauform() {
			if (txtStartBauform == null) {
				txtStartBauform = new JTextField();
			}
			return txtStartBauform;
		}

		private JTextField getTxtStartMenge() {
			if (txtStartMenge == null) {
				txtStartMenge = new JTextField();
			}
			return txtStartMenge;
		}

		private JTextField getTxtEndeMenge() {
			if (txtEndeMenge == null) {
				txtEndeMenge = new JTextField();
			}
			return txtEndeMenge;
		}

		private JTextField getTxtEndePosition() {
			if (txtEndePosition == null) {
				txtEndePosition = new JTextField();
			}
			return txtEndePosition;
		}

		private JTextField getTxtEndeBauform() {
			if (txtEndeBauform == null) {
				txtEndeBauform = new JTextField();
			}
			return txtEndeBauform;
		}

		private JTextField getTxtEndeArtikel() {
			if (txtEndeArtikel == null) {
				txtEndeArtikel = new JTextField();
			}
			return txtEndeArtikel;
		}

		private JButton getBtnPosition() {
			if (btnPosition == null) {
				btnPosition = new JButton();
				btnPosition.setText("<<");
				btnPosition.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnPositionActionActionPerformed(event);
					}
				});
			}
			return btnPosition;
		}

		private JButton getBtnMenge() {
			if (btnMenge == null) {
				btnMenge = new JButton();
				btnMenge.setText("<<");
				btnMenge.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnMengeActionActionPerformed(event);
					}
				});
			}
			return btnMenge;
		}

		private JButton getBtnBauform() {
			if (btnBauform == null) {
				btnBauform = new JButton();
				btnBauform.setText("<<");
				btnBauform.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnBauformActionActionPerformed(event);
					}
				});
			}
			return btnBauform;
		}

		private JButton getBtnBauteil() {
			if (btnBauteil == null) {
				btnBauteil = new JButton();
				btnBauteil.setText("<<");
				btnBauteil.addActionListener(new ActionListener() {
		
					public void actionPerformed(ActionEvent event) {
						btnArtikelnummerActionActionPerformed(event);
					}
				});
			}
			return btnBauteil;
		}

		private JScrollPane getJScrollPane1() {
			if (jScrollPane1 == null) {
				jScrollPane1 = new JScrollPane();
				jScrollPane1.setViewportView(getTxtVorschau());
			}
			return jScrollPane1;
		}

		private JTextField getTxtAnzahlKopfzeilen() {
			if (txtAnzahlKopfzeilen == null) {
				txtAnzahlKopfzeilen = new JTextField();
				txtAnzahlKopfzeilen.setText("0");
				txtAnzahlKopfzeilen.addFocusListener(new FocusAdapter() {
		
					public void focusLost(FocusEvent event) {
						txtAnzahlKopfzeilenFocusFocusLost(event);
					}
				});
			}
			return txtAnzahlKopfzeilen;
		}

		private JLabel getJLabel0() {
			if (jLabel0 == null) {
				jLabel0 = new JLabel();
				jLabel0.setText("Anzahl Kopfzeilen:");
			}
			return jLabel0;
		}

		private JScrollPane getJScrollPane0() {
			if (jScrollPane0 == null) {
				jScrollPane0 = new JScrollPane();
				jScrollPane0.setViewportView(getTblVorschau());
			}
			return jScrollPane0;
		}

		private JTable getTblVorschau() {
			if (tblVorschau == null) {
				tblVorschau = new JTable();
				tblVorschau.setModel(new DefaultTableModel(new Object[][] { { "", "", "", "" }, { "", "", "", "" }, }, new String[] { "Bauteil", "Bauform", "Menge", "Positionsnummer" }) {
					private static final long serialVersionUID = 1L;
					Class<?>[] types = new Class<?>[] { Object.class, Object.class, Object.class, Object.class };
		
					public Class<?> getColumnClass(int columnIndex) {
						return types[columnIndex];
					}
				});
			}
			return tblVorschau;
		}

		private JTextArea getTxtVorschau() {
			if (txtVorschau == null) {
				txtVorschau = new JTextArea();
				txtVorschau.setFont(new Font("Monospaced", Font.PLAIN, 12));
				txtVorschau.setEditable(false);
			}
			return txtVorschau;
		}

		private static void installLnF() {
			try {
				String lnfClassname = PREFERRED_LOOK_AND_FEEL;
				if (lnfClassname == null)
					lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
			} catch (Exception e) {
				System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
						+ " on this platform:" + e.getMessage());
			}
		}

		/**
		 * Main entry of the class.
		 * Note: This class is only created so that you can easily preview the result at runtime.
		 * It is not expected to be managed by the designer.
		 * You can modify it as you like.
		 */
		public static void main(String[] args) {
			installLnF();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					DialogStuecklisteimportDefinition frame = new DialogStuecklisteimportDefinition(new Integer(11));
					frame.setDefaultCloseOperation(DialogStuecklisteimportDefinition.EXIT_ON_CLOSE);
					frame.setTitle("test");
					frame.getContentPane().setPreferredSize(frame.getSize());
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
			});
		}

		private void txtAnzahlKopfzeilenFocusFocusLost(FocusEvent event) {
			readfile();
		}

		private void btnArtikelnummerActionActionPerformed(ActionEvent event) {
			if (txtTrennzeichen.getText().length() == 0) {
				if (checkSelection()) {
					txtStartArtikel.setText(String.format("%d",getStart()));
					txtEndeArtikel.setText(String.format("%d",getEnd()));
				}
			} else {
				txtStartArtikel.setText(String.format("%d",getStartTrennzeichen(txtTrennzeichen.getText())));
				txtEndeArtikel.setText(String.format("%d",getEndeTrennzeichen(txtTrennzeichen.getText())));
			}
		}

		private void btnBauformActionActionPerformed(ActionEvent event) {
			if (txtTrennzeichen.getText().length() == 0) {
				if (checkSelection()) {
					txtStartBauform.setText(String.format("%d",getStart()));
					txtEndeBauform.setText(String.format("%d",getEnd()));
				}
			} else {
				txtStartBauform.setText(String.format("%d",getStartTrennzeichen(txtTrennzeichen.getText())));
				txtEndeBauform.setText(String.format("%d",getEndeTrennzeichen(txtTrennzeichen.getText())));
			}
		}

		private void btnMengeActionActionPerformed(ActionEvent event) {
			if (txtTrennzeichen.getText().length() == 0) {
				if (checkSelection()) {
					cbMengeEins.setSelected(false);
					txtStartMenge.setText(String.format("%d",getStart()));
					txtEndeMenge.setText(String.format("%d",getEnd()));
				}
			} else {
				cbMengeEins.setSelected(false);
				txtStartMenge.setText(String.format("%d",getStartTrennzeichen(txtTrennzeichen.getText())));
				txtEndeMenge.setText(String.format("%d",getEndeTrennzeichen(txtTrennzeichen.getText())));
			}
		}

		private void btnPositionActionActionPerformed(ActionEvent event) {
			if (txtTrennzeichen.getText().length() == 0) {
				if (checkSelection()) {
					txtStartPosition.setText(String.format("%d",getStart()));
					txtEndePosition.setText(String.format("%d",getEnd()));
				}
			} else {
				txtStartPosition.setText(String.format("%d",getStartTrennzeichen(txtTrennzeichen.getText())));
				txtEndePosition.setText(String.format("%d",getEndeTrennzeichen(txtTrennzeichen.getText())));
			}
		}
		
		private boolean checkSelection() {
			boolean check = (txtVorschau.getSelectionEnd()>txtVorschau.getSelectionStart());
			if (!check)	JOptionPane.showMessageDialog(this, "Bitte w\u00E4hlen Sie einen Bereich aus.", "Info", JOptionPane.OK_OPTION );
			return check;
		}
		
		private int getStart() {
			int i = txtVorschau.getSelectionStart();
			int j = txtVorschau.getText().substring(0, i).lastIndexOf("\n");
			return i-j-iOffset;
		}
		
		private int getStartTrennzeichen(String trennzeichen) {
			int i = txtVorschau.getSelectionStart();
			int j = txtVorschau.getText().substring(0, i).lastIndexOf(trennzeichen);
			int k = txtVorschau.getText().substring(0, i).lastIndexOf("\n");
			if (k > j)
				return 1;
			return j-k-iOffset+trennzeichen.length();
		}
		
		private int getEnd() {
			return getStart() + txtVorschau.getSelectionEnd() - txtVorschau.getSelectionStart()-1;
		}

		private int getEndeTrennzeichen(String trennzeichen) {
			int i = txtVorschau.getSelectionStart();
			int j = txtVorschau.getText().indexOf(trennzeichen, i);
			int k = txtVorschau.getText().indexOf("\n", i);
			if (k < j)
				j = k;
			k = txtVorschau.getText().substring(0, j).lastIndexOf("\n");
			return j-k-iOffset;
		}
		
		private void cmdVorschauActionActionPerformed(ActionEvent event) {
			String[] as;
			as = txtVorschau.getText().split("\n");
			int[][] ai ={{0,0},{0,0},{0,0},{0,0}};
			if (txtStartArtikel.getText().length()>0)	ai[0][0] = Integer.parseInt(txtStartArtikel.getText())+iOffset-1;
			if (txtEndeArtikel.getText().length()>0)	ai[0][1] = Integer.parseInt(txtEndeArtikel.getText())+iOffset;
			if (txtStartBauform.getText().length()>0)	ai[1][0] = Integer.parseInt(txtStartBauform.getText())+iOffset-1;
			if (txtEndeBauform.getText().length()>0)	ai[1][1] = Integer.parseInt(txtEndeBauform.getText())+iOffset;
			if (txtStartMenge.getText().length()>0)		ai[2][0] = Integer.parseInt(txtStartMenge.getText())+iOffset-1;
			if (txtEndeMenge.getText().length()>0)		ai[2][1] = Integer.parseInt(txtEndeMenge.getText())+iOffset;
			if (txtStartPosition.getText().length()>0)	ai[3][0] = Integer.parseInt(txtStartPosition.getText())+iOffset-1;
			if (txtEndePosition.getText().length()>0)	ai[3][1] = Integer.parseInt(txtEndePosition.getText())+iOffset;
			
			DefaultTableModel model = (DefaultTableModel)tblVorschau.getModel();
			while (model.getRowCount()>0)
				model.removeRow(0);
		
			int skipped = 0;
			for (int i=0; i < as.length; i++) {
				try {
					Object[] ao = new Object[]{as[i].substring(ai[0][0],ai[0][1]).trim(),
							as[i].substring(ai[1][0],ai[1][1]).trim(),
							(cbMengeEins.isSelected() ? "1" : as[i].substring(ai[2][0],ai[2][1]).trim()),
							as[i].substring(ai[3][0],ai[3][1]).trim()};
					
					((DefaultTableModel)tblVorschau.getModel()).addRow(ao);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					skipped++;
				}
			}
			if (skipped > 0)
				JOptionPane.showMessageDialog(this, String.format("%d Zeilen wurden aufgrund von Formatfehlern ignoriert.",skipped), "Info", JOptionPane.OK_OPTION );
		}

		private void btnOpenActionActionPerformed(ActionEvent event) {
			File[] files = HelperClient.chooseFile(this, HelperClient.FILE_FILTER_CSV, false);
			File f = null;
			if (files != null && files.length>0) {
				f = files[0];
			}
			if (f != null) {
				txtFile.setText(f.getPath());
				txtAnzahlKopfzeilen.setText("0");
				readfile();
			}
		}

		private void btnImportActionActionPerformed(ActionEvent event) {
			this.setVisible(false);
			DialogStuecklisteimportImport dsi = new DialogStuecklisteimportImport(LPMain.getInstance().getDesktop(), true, stuecklisteIId);
			try {
				Vector<Vector> v = (Vector<Vector>)((DefaultTableModel) tblVorschau.getModel()).getDataVector();
				dsi.setTabledata(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dsi.setLocationRelativeTo(LPMain.getInstance().getDesktop());
			dsi.setVisible(true);
			this.setVisible(true);
		}

		private void btnAbbrechenActionActionPerformed(ActionEvent event) {
			this.dispose();
		}

		private void txtTrennzeichenFocusFocusLost(FocusEvent event) {
		}

	}


