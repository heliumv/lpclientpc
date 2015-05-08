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
 *******************************************************************************/
package com.lp.client.media;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

import net.miginfocom.swing.MigLayout;

public class PanelRetrieveEmailProgress extends JPanel implements ActionListener {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());

	private static final long serialVersionUID = -2936844146179285374L;

	private JProgressBar progressBar ;
	private WrapperButton wbuCancel ;
	private WrapperLabel wlaInfo ;
	private TabbedPaneMediaController controller ;
	
	public PanelRetrieveEmailProgress(TabbedPaneMediaController controller) {
		this.controller = controller ;
		
		jbInit() ;
	}
	
	private void jbInit() {
		setLayout(new MigLayout("wrap 2, filly", "[fill, 90%][fill]")) ;
		
		progressBar = new JProgressBar() ;
		add(progressBar) ;
		
//		wbuCancel = new WrapperButton("X") ;
		wbuCancel = new WrapperButton(new ImageIcon(getClass()
				.getResource("/com/lp/client/res/media_stop.png")));

		wbuCancel.addActionListener(this);
		add(wbuCancel, "growy, wrap") ;
		
		wlaInfo = new WrapperLabel() ;
		wlaInfo.setHorizontalAlignment(JLabel.LEFT) ;
		add(wlaInfo) ;
		
		wlaInfo.setText("in Bearbeitung...");
		HelperClient.setComponentNames(this);
	}
	
	public void setTotalCount(Integer totalCount, Integer value) {
		progressBar.setMaximum(totalCount);
		progressBar.setValue(value) ;
	}
	
	public void setTotalCount(final Integer totalCount) {
		log.info("Setting totalcount to " + totalCount) ;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setMaximum(totalCount);				
			}
		});
	}
	
	public void setValue(Integer value) {
		log.info("Setting value to " + value) ;
		progressBar.setValue(value) ;
	}
	
	public void setInfo(String info) {
		final String labelText = info.startsWith("value:") ?  info.substring(6) + " von " + progressBar.getMaximum() : info ;
		
//		if(info.startsWith("value:")) {
//			labelText = info.substring(6) + " von " + progressBar.getMaximum() ;
//		} else {
//			labelText = info ;
//		}
//		
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				wlaInfo.setText(labelText) ;
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(wbuCancel)) {
			cancelRetrieveEmails() ;
		}
	}
	
	private void cancelRetrieveEmails() {
		progressBar.setIndeterminate(true) ;
		try {
			controller.cancelRetrieveEmails() ;
		} catch(Throwable t) {
			log.error("Throwable", t) ;
		}
	}
}
