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
package com.lp.client.system.pflege;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;

/**
 * Dieses Panel beinhaltet die Pflegefunktionen im Modul System
 * @author Robert
 *
 */
public class PanelPflegeNew extends PanelBasis implements PflegeEventListener,
		TreeSelectionListener {

	private static final long serialVersionUID = -2589285363549775241L;

	private JPanel panelPflege;
	private JSplitPane splitPane;
	private JEditorPane textBeschreibung;
	private PanelPflegeControl panelControl;
	private JTreePflege tree;
	private IPflegefunktion pflege;

	public PanelPflegeNew(InternalFrame iFrame, String add2Title)
			throws Throwable {
		super(iFrame, add2Title, null);
		jbInit();

		tree.register(new DokumentenpfadeErneuern());
		tree.register(new DokumenteImportieren());
//		tree.register(new PflegeMock());

	}

	private void selectPflegefunktion(IPflegefunktion pflege) {
		this.pflege = pflege;
		panelPflege.removeAll();
		textBeschreibung.setText("");
		
		if(pflege != null) {
			pflege.addPflegeEventListener(this);
			pflege.init();
			panelPflege.add(pflege.getPanel(), BorderLayout.CENTER);
			
			if(pflege.getBeschreibung()!=null) {
				textBeschreibung.setText(pflege.getBeschreibung());
				textBeschreibung.setVisible(!pflege.getBeschreibung().isEmpty());
			}
			
			panelControl.setProgress(((PflegefunktionSupportsProgressBar)pflege).getProgress());
			pflege.eventYouAreSelected();
			updateControlPanel();
		} else {
			textBeschreibung.setVisible(false);
			panelControl.disableAll();
		}

		validate();
		repaint();
	}

	private void jbInit() {
		setLayout(new BorderLayout());

		panelControl = new PanelPflegeControl();
		splitPane = new JSplitPane();
		textBeschreibung = new JEditorPane();
		panelPflege = new JPanel();
		tree = new JTreePflege();
		
		splitPane.setLeftComponent(tree);
		splitPane.setBorder(BorderFactory.createEmptyBorder());

		//panelControl.setBorder(BorderFactory.createLoweredBevelBorder());
		textBeschreibung.setBorder(BorderFactory.createLoweredBevelBorder());
		panelPflege.setBorder(BorderFactory.createLoweredBevelBorder());
		tree.setBorder(BorderFactory.createLoweredBevelBorder());
		
		textBeschreibung.setEditable(false);
		textBeschreibung.setVisible(false);
		textBeschreibung.setContentType("text/html");
		
		panelPflege.setLayout(new BorderLayout());
		
		panelControl.setPreferredSize(new Dimension(0, 30));
		panelControl.setMinimumSize(new Dimension(0, 30));
		panelControl.addActionListener(this);
		
		textBeschreibung.setPreferredSize(new Dimension(0, 100));
		textBeschreibung.setMinimumSize(new Dimension(0, 100));

		tree.setPreferredSize(new Dimension(200, 0));
		tree.setMinimumSize(new Dimension(100, 0));
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);

		Insets insets = new Insets(0, 0, 0, 0);

//		add(tree, new GridBagConstraints(0, 0, 1, 3, 0, 1,
//				GridBagConstraints.WEST, GridBagConstraints.VERTICAL, insets,
//				5, 5));
		
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new GridBagLayout());
		rightPane.add(textBeschreibung, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, insets, 5, 5));
		rightPane.add(panelPflege, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 5, 5));
		rightPane.add(panelControl, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				insets, 5, 5));
		splitPane.setRightComponent(rightPane);
		add(splitPane);
		
//		add(splitPane, new GridBagConstraints(0, 0, 1, 1, 0, 0,
//				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 5, 5));

	}
	
	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		String command = e.getActionCommand();
		if(command.equals(PanelPflegeControl.START)) {
			pflege.run();
		}else if(command.equals(PanelPflegeControl.CANCEL)) {
			pflege.cancel();
		}
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SYSTEM;
	}

	@Override
	public void done(EventObject e) {
		updateControlPanel();
		tree.setEnabled(true);
	}

	@Override
	public void started(EventObject e) {
		updateControlPanel();
		tree.setEnabled(false);
	}

	@Override
	public void canceled(EventObject e) {
		updateControlPanel();
	}

	@Override
	public void enabledStartable(EventObject e) {
		updateControlPanel();
	}

	@Override
	public void disabledStartable(EventObject e) {
		updateControlPanel();
	}


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object selectedUserObject = ((DefaultMutableTreeNode) e.getPath()
				.getLastPathComponent()).getUserObject();
		if(selectedUserObject instanceof IPflegefunktion)
			selectPflegefunktion((IPflegefunktion)selectedUserObject);
		else
			selectPflegefunktion(null);
	}
	
	private void updateControlPanel() {
		if(pflege!=null) {
			panelControl.setStartEnabled(pflege.isStartable());
			panelControl.setCancelEnabled(pflege.isRunning());
		} else {
			panelControl.disableAll();
		}
	}

	@Override
	public void progress(EventObject e) {
		BoundedRangeModel brm = ((PflegefunktionSupportsProgressBar)pflege).getProgress();
		panelControl.setProgress(brm);
		
		long startTime = ((PflegefunktionSupportsProgressBar)pflege).getStartTime();
		long uptime = System.currentTimeMillis() - startTime;
		int steps = brm.getValue() - brm.getMinimum();
		int stepsLeft = brm.getMaximum() - brm.getValue();
		float timePerStep = uptime / (steps == 0 ? 1 : steps);
		panelControl.setTimeLeft((long)(stepsLeft*timePerStep));
		repaint();
	}
}
