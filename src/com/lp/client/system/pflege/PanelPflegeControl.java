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
package com.lp.client.system.pflege;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class PanelPflegeControl extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6771305663685579396L;
	public static final String START = "start";
	public static final String CANCEL = "cancel";

	private JButton jbStart;
	private JButton jbCancel;
	private JProgressBar jpBar;
	private JLabel jlTimeLeft;

	private Timer t;
	
	public PanelPflegeControl() {
		init();
		installComp();
	}
	
	private void init() {
		jbStart = new JButton("Start");
		jbCancel = new JButton("Stopp");
		jlTimeLeft = new JLabel();
		jpBar = new JProgressBar();

		jbStart.setActionCommand(START);
		jbCancel.setActionCommand(CANCEL);

		jbStart.setEnabled(false);
		jbCancel.setEnabled(false);
		jpBar.setEnabled(false);
	}
	
	public void setTimeLeft(long timeLeft) {
		jlTimeLeft.setText(String.format("%d:%02d:%02d", timeLeft/3600000, timeLeft%3600000/60000, timeLeft%60000/1000));
	}
	
	private void installComp() {
		setLayout(new GridBagLayout());

		jbStart.setPreferredSize(new Dimension(80, 20));
		jbStart.setMinimumSize(new Dimension(80, 20));
		
		jbCancel.setPreferredSize(new Dimension(80, 20));
		jbCancel.setMinimumSize(new Dimension(80, 20));
				
		add(jbStart);
		add(jbCancel);
		add(jpBar);

		add(jbStart, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 5, 5));
		add(jbCancel, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 5, 5));
		add(jpBar, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
		add(jlTimeLeft, new GridBagConstraints(3, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 5, 5));
	}
	
	public void setStartEnabled(boolean b) {
		jbStart.setEnabled(b);
	}
	
	public void setCancelEnabled(boolean b) {
		jbCancel.setEnabled(b);
	}
	
	public void setRunning(boolean running) {
		jlTimeLeft.setVisible(running) ;
	}
	
	public void setProgress(BoundedRangeModel brm) {
		if(t!=null) {
			t.cancel();
		}
		t = new Timer();
		if(brm==null) {
			jpBar.setString("");
			jpBar.setModel(new DefaultBoundedRangeModel());
			jpBar.setIndeterminate(false);
		} else {
			if(brm.getValue() == brm.getMinimum())
				jpBar.setIndeterminate(true);
			else {
				jpBar.setIndeterminate(false);
				t.schedule(new TimerTask() {
					
					@Override
					public void run() {
						jpBar.setIndeterminate(true);
					}
				}, 3000);
			}
			if(brm.getValue() == brm.getMaximum()) {
				jpBar.setString("Fertig!");
				jpBar.setIndeterminate(false) ;
				if(t!=null) {
					t.cancel();
				}
			} else {
				jpBar.setString(null);
			}
			jpBar.setModel(brm);
			jpBar.setStringPainted(true);
		}
	}

	public void addActionListener(ActionListener l) {
		jbStart.addActionListener(l);
		jbCancel.addActionListener(l);
	}
	
	public void removeActionListener(ActionListener l) {
		jbStart.removeActionListener(l);
		jbCancel.removeActionListener(l);
	}

	public void disableAll() {
		setProgress(null);
		setCancelEnabled(false);
		setStartEnabled(false);
	}
}
