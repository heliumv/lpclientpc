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
 package com.lp.client.frame.assistent.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.AssistentController;
import com.lp.client.frame.assistent.IAssistentListener;
import com.lp.client.frame.assistent.IPageNavigationUpdateListener;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

public class AssistentView extends PanelDialog implements IPageNavigationUpdateListener,
		IAssistentListener {
	
	private static final long serialVersionUID = 3163237078630781307L;
	private JLabel title; 
	private WrapperButton prev;
	private WrapperButton next;
	private WrapperButton finish;
	private WrapperButton cancel;
	
	private AssistentPageView pageView;
	
	private AssistentController controller;
	
	public AssistentView(InternalFrame iframe, String add2Title, AssistentController controller) throws Throwable {
		super(iframe, add2Title, false);
		this.controller = controller;
		controller.addAssistentListener(this);
		controller.addNavigationUpdateListener(this);
		setBorder(BorderFactory.createEmptyBorder());
		
		initView();
		
		pageSwitched(); //UI Update
		
		
	}
	
	private void initView() {
		
		title = new JLabel();
		Font titleFont = title.getFont();
		titleFont = titleFont.deriveFont(titleFont.getSize2D()*1.5f).deriveFont(Font.BOLD);
		title.setFont(titleFont);

		prev = new WrapperButton(LPMain.getTextRespectUISPr("lp.zurueck"));
		next = new WrapperButton(LPMain.getTextRespectUISPr("lp.weiter"));
		finish = new WrapperButton(LPMain.getTextRespectUISPr("lp.beenden"));
		cancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		
		NavigationActionListener nal = new NavigationActionListener();
		prev.addActionListener(nal);
		next.addActionListener(nal);
		finish.addActionListener(nal);
		cancel.addActionListener(nal);
		
		removeAll();
		
		setLayout(new MigLayout("wrap 4, hidemode 1, nocache", "[]push[fill|fill|fill]","[][fill][fill,grow][fill][]"));
		add(title, "span, left");
		add(new JSeparator(), "span, wrap, growx");
		add(new JSeparator(), "newline, span, growx");
		add(prev, "w 20%!, skip, bottom");
		add(next, "w 20%!, split 2, bottom, gapright 0");
		add(finish, "w 20%!, bottom, gapleft 0");
		add(cancel, "w 20%!, bottom");
	}

	@Override
	public void pageSwitched() {
		if(pageView != null)
			remove(pageView);
		add(controller.getActivePageView(), "cell 0 2 4 1, growx, growy");
		pageView = controller.getActivePageView();
		title.setText(pageView.getTitle());
		updateNavigation();
	}

	@Override
	public void canceled() {
		exit();
	}
	
	@Override
	protected void eventActionEscape(ActionEvent e) throws Throwable {
		cancelIfSureDialog();
	}
	
	/**
	 * &Uuml;berschrieben, damit beim versuchten Schliessen des InternalFrames 
	 * gefragt wird, ob der Benutzer sicher ist.
	 */
	@Override
	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		if(cancelIfSureDialog()) {
			return null;
		}
		return new PropertyVetoException("", null);
	}
	
	/**
	 * Zeigt einen Dialog mit der Frage ob wirklich abgebrochen werden soll.
	 * Ist der Anwender sicher, wird der Controller dar&uuml;ber informiert.
	 */
	protected boolean cancelIfSureDialog() {
		if(DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain.getTextRespectUISPr("lp.warning.abbrechen"))) {
			try {
				controller.cancel();
			} catch (Throwable e) {
				handleException(e, true);
			}
			return true;
		}
		return false;
	}

	@Override
	public void finished() {
		exit();
	}
	
	protected void exit() {
		controller = null;
		try {
			getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_YOU_ARE_SELECTED);
			ActionEvent e = new ActionEvent(this, 0, ACTION_SPECIAL_CLOSE_PANELDIALOG);
			super.eventActionEscape(e);
		} catch (Throwable t) {
			getInternalFrame().handleException(t, true);
		}
	}

	@Override
	public void navigationUpdateEvent(EventObject e) {
		updateNavigation();
	}
	
	protected void updateNavigation() {
		prev.setVisible(!controller.isFirstPage());
		next.setVisible(!controller.isLastPage());
		finish.setVisible(controller.isLastPage());
		
		prev.setEnabled(controller.isPrevAllowed());
		next.setEnabled(controller.isNextAllowed());
		finish.setEnabled(controller.isNextAllowed());
		cancel.setEnabled(controller.isCancelAllowed());
		invalidate();
	}
	
	private void setBusy(boolean b) {
		if (b) {
			getInternalFrame().getFrameProgress().start(
					LPMain.getTextRespectUISPr("lp.working"));
		} else {
			getInternalFrame().getFrameProgress().pause();
		}
	}
	
	private class NavigationActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setBusy(true);
			try {
				if(e.getSource() == prev)
					controller.gotoPrevPage();
				else if(e.getSource() == next)
					controller.gotoNextPage();
				else if(e.getSource() == finish)
					controller.finish();
				else if(e.getSource() == cancel)
					cancelIfSureDialog();
				setBusy(false);
			} catch (Throwable t) {
				handleException(t, false);
			}
		}
		
	}

}
