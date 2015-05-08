package net.miginfocom.layout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.lp.client.pc.LPMain;

/*
* License (BSD):
* ==============
*
* Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (miglayout (at) miginfocom (dot) com)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification,
* are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright notice, this list
* of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this
* list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* Neither the name of the MiG InfoCom AB nor the names of its contributors may be
* used to endorse or promote products derived from this software without specific
* prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
* INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
* BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
* OF SUCH DAMAGE.
*
* @version 1.0
* @author Mikael Grev, MiG InfoCom AB
* Date: 2006-sep-08
*/
public class ConstraintListener extends MouseAdapter {
	private static final Font BUTT_FONT = new Font("monospaced", Font.PLAIN, 12);

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			react(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			react(e);
	}

	public void react(MouseEvent e) {
		JComponent c = (JComponent) e.getSource();
		LayoutManager lm = null;
		do {
			lm = c.getLayout();
			if (lm instanceof MigLayout)
				break;
			lm = c.getParent().getLayout();
			if (lm instanceof MigLayout)
				break;
			if (c.getParent() instanceof JComponent)
				c = (JComponent) c.getParent();
			else break;
		} while (c != null);

		if (lm instanceof MigLayout) {
			MigLayout layout = (MigLayout) lm;
			boolean isComp = layout.isManagingComponent(c);

			Object compConstr = isComp ? layout.getComponentConstraints(c)
					: null;
			if (isComp && compConstr == null)
				compConstr = "";

			Object rowsConstr = isComp ? null : layout.getRowConstraints();
			Object colsConstr = isComp ? null : layout.getColumnConstraints();
			Object layoutConstr = isComp ? null : layout.getLayoutConstraints();

			ConstraintsDialog cDlg = new ConstraintsDialog(LPMain.getInstance()
					.getDesktop(),
					layoutConstr instanceof LC ? IDEUtil.getConstraintString(
							(LC) layoutConstr, false) : (String) layoutConstr,
					rowsConstr instanceof AC ? IDEUtil.getConstraintString(
							(AC) rowsConstr, false, false)
							: (String) rowsConstr,
					colsConstr instanceof AC ? IDEUtil.getConstraintString(
							(AC) colsConstr, false, false)
							: (String) colsConstr,
					compConstr instanceof CC ? IDEUtil.getConstraintString(
							(CC) compConstr, false) : (String) compConstr, c.getName());

			cDlg.pack();
			cDlg.setLocationRelativeTo(c);

			if (cDlg.showDialog()) {
				try {
					if (isComp) {
						String constrStr = cDlg.componentConstrTF.getText()
								.trim();
						layout.setComponentConstraints(c, constrStr);
						if (c instanceof JButton) {
							c.setFont(BUTT_FONT);
							((JButton) c)
									.setText(constrStr.length() == 0 ? "<Empty>"
											: constrStr);
						}
						System.out.println(c.getName() + ": " + constrStr);
					} else {
						layout.setLayoutConstraints(cDlg.layoutConstrTF
								.getText());
						layout.setRowConstraints(cDlg.rowsConstrTF.getText());
						layout.setColumnConstraints(cDlg.colsConstrTF.getText());
						System.out.println(c.getName() + ": " + 
								"\"" + cDlg.layoutConstrTF.getText() + "\", " +
								"\"" + cDlg.colsConstrTF.getText() + "\", " +
								"\"" + cDlg.rowsConstrTF.getText() + "\"");
					}
				} catch (Exception ex) {
					StringWriter sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					JOptionPane.showMessageDialog(
							SwingUtilities.getWindowAncestor(c), sw.toString(),
							"Error parsing Constraint!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				c.invalidate();
				c.getParent().validate();
			}
		}
	}

	private static class ConstraintsDialog extends JDialog implements
			ActionListener, KeyEventDispatcher {
		private static final long serialVersionUID = 859299123870864018L;
		private static final Color ERROR_COLOR = new Color(255, 180, 180);
		private final JPanel mainPanel = new JPanel(new MigLayout(
				"fillx,flowy,ins dialog", "[fill]", "2[]2"));
		final JTextField layoutConstrTF;
		final JTextField rowsConstrTF;
		final JTextField colsConstrTF;
		final JTextField componentConstrTF;

		private final JButton okButt = new JButton("OK");
		private final JButton cancelButt = new JButton("Cancel");

		private boolean okPressed = false;

		public ConstraintsDialog(Frame owner, String layoutConstr,
				String rowsConstr, String colsConstr, String compConstr, String compName) {
			super(owner, (compConstr != null ? "Edit Component Constraints"
					: "Edit Container Constraints") + ": " + compName, true);

			layoutConstrTF = createConstraintField(layoutConstr);
			rowsConstrTF = createConstraintField(rowsConstr);
			colsConstrTF = createConstraintField(colsConstr);
			componentConstrTF = createConstraintField(compConstr);

			if (componentConstrTF != null) {
				mainPanel.add(new JLabel("Component Constraints"));
				mainPanel.add(componentConstrTF);
			}

			if (layoutConstrTF != null) {
				mainPanel.add(new JLabel("Layout Constraints"));
				mainPanel.add(layoutConstrTF);
			}

			if (colsConstrTF != null) {
				mainPanel.add(new JLabel("Column Constraints"), "gaptop unrel");
				mainPanel.add(colsConstrTF);
			}

			if (rowsConstrTF != null) {
				mainPanel.add(new JLabel("Row Constraints"), "gaptop unrel");
				mainPanel.add(rowsConstrTF);
			}

			mainPanel.add(okButt, "tag ok,split,flowx,gaptop 15");
			mainPanel.add(cancelButt, "tag cancel,gaptop 15");

			setContentPane(mainPanel);

			okButt.addActionListener(this);
			cancelButt.addActionListener(this);
		}

		public void addNotify() {
			super.addNotify();
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.addKeyEventDispatcher(this);
		}

		public void removeNotify() {
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.removeKeyEventDispatcher(this);
			super.removeNotify();
		}

		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				dispose();
			return false;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButt)
				okPressed = true;
			dispose();
		}

		private JTextField createConstraintField(String text) {
			if (text == null)
				return null;

			final JTextField tf = new JTextField(text, 50);
			tf.setFont(new Font("monospaced", Font.PLAIN, 12));

			tf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						okButt.doClick();
						return;
					}

					javax.swing.Timer timer = new javax.swing.Timer(50,
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									String constr = tf.getText();
									try {
										if (tf == layoutConstrTF) {
											ConstraintParser
													.parseLayoutConstraint(constr);
										} else if (tf == rowsConstrTF) {
											ConstraintParser
													.parseRowConstraints(constr);
										} else if (tf == colsConstrTF) {
											ConstraintParser
													.parseColumnConstraints(constr);
										} else if (tf == componentConstrTF) {
											ConstraintParser
													.parseComponentConstraint(constr);
										}

										tf.setBackground(Color.WHITE);
										okButt.setEnabled(true);
									} catch (Exception ex) {
										tf.setBackground(ERROR_COLOR);
										okButt.setEnabled(false);
									}
								}
							});
					timer.setRepeats(false);
					timer.start();
				}
			});

			return tf;
		}

		private boolean showDialog() {
			setVisible(true);
			return okPressed;
		}
	}

}
