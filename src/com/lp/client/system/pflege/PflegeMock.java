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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PflegeMock extends PflegefunktionSupportsProgressBar implements
		KeyListener {

	private JPanel panel;
	private Timer timer;
	private JEditorPane console;
	private JLabel lDelay;
	private JTextField tfDelay;
	private JLabel lCounts;
	private JTextField tfCounts;

	private DefaultBoundedRangeModel progress;

	private int times;

	@Override
	public String getKategorie() {
		return "Mock";
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getBeschreibung() {
		return "Dies ist nur ein Test. <b>Nicht</b> mehr, und nicht weniger.";
	}

	@Override
	public JPanel getPanel() {
		if (panel == null)
			createPanel();
		return panel;
	}

	private void createPanel() {
		panel = new JPanel();
		console = new JEditorPane();
		lDelay = new JLabel("delay");
		tfDelay = new JTextField();
		lCounts = new JLabel("times");
		tfCounts = new JTextField();

		tfDelay.addKeyListener(this);
		tfCounts.addKeyListener(this);

		console.setEditable(false);
		console.setContentType("text/html");

		panel.setLayout(new GridBagLayout());
		panel.add(lDelay, new GridBagConstraints(0, 0, 1, 1, 0.25, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
		panel.add(tfDelay, new GridBagConstraints(1, 0, 1, 1, 0.25, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
		panel.add(lCounts, new GridBagConstraints(2, 0, 1, 1, 0.25, 0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
		panel.add(tfCounts, new GridBagConstraints(3, 0, 1, 1, 0.25, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
		panel.add(console, new GridBagConstraints(0, 1, 4, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 5, 5));

	}

	@Override
	public boolean isStartable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() {
		timer = new Timer();
		times = Integer.parseInt(tfCounts.getText());
		progress.setMinimum(0);
		progress.setMaximum(times);
		progress.setValue(0);
		
		fireStartedEvent(new EventObject(this));

		tfCounts.setEnabled(false);
		tfDelay.setEnabled(false);

		timer.schedule(new TimerTask() {

			int count = 0;

			@Override
			public void run() {
				console.setText(console.getText() + "<br/>"+String.valueOf(count));
				System.out.println();
				progress.setValue(count);
				
				fireProgressEvent(new EventObject(this));
				if (count >= times) {
					done();
				}
				count++;
			}
		}, 0, Integer.parseInt(tfDelay.getText()));
	}

	private void done() {
		timer.cancel();
		fireDoneEvent(new EventObject(this));
		tfCounts.setEnabled(true);
		tfDelay.setEnabled(true);
		fireEnabledStartableEvent(new EventObject(this));
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eventYouAreSelected() {
		tfCounts.setText("100");
		tfDelay.setText("100");
	}

	@Override
	public void init() {
		createPanel();
		progress = new DefaultBoundedRangeModel();
		fireEnabledStartableEvent(new EventObject(this));
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		if (arg0.getSource().equals(tfCounts)
				|| arg0.getSource().equals(tfDelay)) {
			JTextField tf = (JTextField)arg0.getSource();
			try {
				Integer.parseInt(tf.getText());
				tf.setBackground(Color.white);
				if(!tfCounts.getText().isEmpty()
						&& !tfDelay.getText().isEmpty())
					fireEnabledStartableEvent(new EventObject(this));
			} catch (NumberFormatException e) {
				tf.setBackground(Color.red);
				fireDisableStartableEvent(new EventObject(this));
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundedRangeModel getProgress() {
		return progress;
	}

	@Override
	public void cancel() {
		timer.cancel();
		fireCanceledEvent(new EventObject(this));
		tfCounts.setEnabled(true);
		tfDelay.setEnabled(true);
		fireEnabledStartableEvent(new EventObject(this));
		
	}
}
