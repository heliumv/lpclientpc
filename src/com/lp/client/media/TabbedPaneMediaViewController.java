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
package com.lp.client.media;

import java.awt.Dimension;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.media.action.SelectMoveToEmailPersonAction;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.media.service.HvProgressQueueReceiver;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.media.service.MediaStoreBelegDto;


public class TabbedPaneMediaViewController implements MessageListener {
	protected final LpLogger log = (LpLogger) LpLogger.getInstance(this.getClass());

	private InternalFrame internalFrame ;
	private TabbedPaneMediaController controller ;
	
	private JMenu selectMenu ;
	private PanelQueryFLR panelQueryFLREmailPersonal ;
	private PanelRetrieveEmailProgress emailProgress ;
	private PanelQuery panelQueryInboxAuswahl ;
	private PanelSplit panelSplitInboxAuswahl ;
	private PanelInboxAuswahlPreview panelInboxAuswahlPreview ;
	
	private PanelQuery panelQueryBelegAuswahl ;
	private PanelSplit panelSplitBelegAuswahl ;
	private PanelBelegAuswahlPreview panelBelegAuswahlPreview ;

	private FavoriteListPersonal favorites ;
	private EmailRetrieverContiniousWorker emailWorker ;
	private HvProgressQueueReceiver progressReceiver ;
	
	private static final String ACTION_RETRIEVE_EMAIL = PanelBasis.LEAVEALONE
			+ "special_action_retrieve_email";
	
	public TabbedPaneMediaViewController(InternalFrame internalFrame, TabbedPaneMediaController controller) {
		this.internalFrame = internalFrame ;
		this.controller = controller ;
		favorites = new FavoriteListPersonal(this) ;
		msgQueueInit() ;
	}

	private void msgQueueInit() {
		progressReceiver = new HvProgressQueueReceiver(EmailMediaFac.PROGRESS_QUEUENAME, this) ;
	}

	public InternalFrame getInternalFrame() {
		return internalFrame ;
	}
	
	public TabbedPaneMediaController getController() {
		return controller ;
	}

	public void setPanelQueryInboxAuswahl(PanelQuery queryAuswahl) {
		panelQueryInboxAuswahl = queryAuswahl ;
	}
	
	public PanelQuery getPanelQueryInboxAuswahl() {
		return panelQueryInboxAuswahl ;
	}
	
	public void setPanelSplitInboxAuswahl(PanelSplit panelSplit) {
		panelSplitInboxAuswahl = panelSplit ;
	}
	
	public PanelSplit getPanelSplitInboxAuswahl() {
		return panelSplitInboxAuswahl ;
	}
	
	public void setPanelInboxAuswahlPreview(PanelInboxAuswahlPreview panelAuswahl) {
		panelInboxAuswahlPreview = panelAuswahl ;
	}

	public PanelInboxAuswahlPreview getPanelInbxoAuswahlPreview() {
		return panelInboxAuswahlPreview ;
	}
	
	public void setPanelQueryBelegAuswahl(PanelQuery queryAuswahl) {
		panelQueryBelegAuswahl = queryAuswahl ;
	}
	
	public PanelQuery getPanelQueryBelegAuswahl() {
		return panelQueryBelegAuswahl ;
	}

	public void setPanelSplitBelegAuswahl(PanelSplit panelSplit) {
		panelSplitBelegAuswahl = panelSplit ;
	}
	
	public PanelSplit getPanelSplitBelegAuswahl() {
		return panelSplitBelegAuswahl ;
	}
	
	public void setPanelBelegAuswahlPreview(PanelBelegAuswahlPreview panelAuswahl) {
		panelBelegAuswahlPreview = panelAuswahl ;
	}

	public PanelBelegAuswahlPreview getPanelBelegAuswahlPreview() {
		return panelBelegAuswahlPreview ;
	}
	
	public JMenu getPersonalSelectMenu() {
		if(selectMenu == null) {
		    selectMenu = new JMenu("Verschieben...") ;
			selectMenu.setIcon(IconFactory.getMailForward());
		    selectMenu.addSeparator(); 
		    selectMenu.add(new JMenuItem(
		    		new SelectMoveToEmailPersonAction(this, getController(), "Mitarbeiter ausw\u00E4hlen..."))) ;	
		}
		
		return selectMenu ;		
	}
	
	public PanelQueryFLR getPersonalWithEmailDialog() {
		return panelQueryFLREmailPersonal ;
	}
	
	public void showDialogPersonalWithEmail() throws Throwable {
		panelQueryFLREmailPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonalMitEmailAdresse(getInternalFrame(),
						true, false, null);
		new DialogQuery(panelQueryFLREmailPersonal) ;		
	}
	

	public void moveMyInboxElementToOtherInbox()  throws Throwable {
		if(panelQueryFLREmailPersonal == null) return ;
		if(getController().getInboxId() == null) return ;
		
		Integer personalId = (Integer) panelQueryFLREmailPersonal.getSelectedId() ;

		moveMyInboxElementToOtherInbox(personalId);
	}	

	public void moveMyInboxElementToOtherInbox(Integer otherPersonalId) throws Throwable {
		controller.moveMyInboxElementToOtherInbox(otherPersonalId);

		if(favorites.add(otherPersonalId)) {
			int count = getPersonalSelectMenu().getMenuComponentCount() ;
			while(count > 2) {
				getPersonalSelectMenu().remove(0) ;
				--count ;
			}

			List<JMenuItem> menuItems = favorites.asMenuItems() ;
			count = 0 ;
			for (JMenuItem jMenuItem : menuItems) {
				getPersonalSelectMenu().insert(jMenuItem, count);
				++count ;
			}
		}
		
		Object nextId = getPanelQueryInboxAuswahl().getId2SelectAfterDelete() ;
		getPanelQueryInboxAuswahl().setSelectedId(nextId);
		getPanelQueryInboxAuswahl().eventYouAreSelected(false) ;		
	}
	
	public void enableAutoReceive(boolean enable) throws Throwable {
		log.info("switch autoreceive to " + enable) ;
		if(enable) {
			if(emailWorker != null) {
				if(emailWorker.isCompleted()) {
					emailWorker = new EmailRetrieverContiniousWorker() ;
					emailWorker.execute();
				} else {
					emailWorker.enableEmailRetrieving(true) ;
				}
			} else {
				emailWorker = new EmailRetrieverContiniousWorker() ;
				emailWorker.execute();
			}
		} else {
			if(emailWorker != null) {
				emailWorker.enableEmailRetrieving(false) ;
			}
		}
	}
	

	public void startRetrievingEmails() {
		startRetrievingEmailsUi();
		
		EmailRetrieverWorker worker = new EmailRetrieverWorker() ;
		worker.execute() ;	
	}
	
	protected void startRetrievingEmailsUi() {
		retrieveEmailButtonBeDisabled();
		
		JPanel rightPanel = getPanelQueryInboxAuswahl().getToolBar().getToolsPanelRight() ;
		emailProgress = new PanelRetrieveEmailProgress(getController()) ;
		emailProgress.setPreferredSize(new Dimension(150, 60));
		emailProgress.setMinimumSize(new Dimension(80, 60));
		
		rightPanel.add(emailProgress, 0) ;
		rightPanel.invalidate();		
	}
	
	public void stopRetrievingEmails() throws Throwable {
		retrieveEmailButtonBeEnabled();		
		if(emailProgress == null) return ;
		
		JPanel rightPanel = getPanelQueryInboxAuswahl().getToolBar().getToolsPanelRight() ;
		rightPanel.remove(emailProgress);
//		rightPanel.invalidate();
//		getPanelQueryAuswahl().invalidate();
//		panelSplitAuswahl.eventYouAreSelected(false);
		getPanelSplitInboxAuswahl().eventYouAreSelected(false) ;
	}

	
	public void retrieveEmailButtonSetEnabled(boolean enable) {
		((LPButtonAction) getPanelQueryInboxAuswahl().getHmOfButtons().get(
				ACTION_RETRIEVE_EMAIL)).getButton().setEnabled(enable);		
	}
	
	public void retrieveEmailButtonBeDisabled() {
		retrieveEmailButtonSetEnabled(false);
	}

	public void retrieveEmailButtonBeEnabled() {
		retrieveEmailButtonSetEnabled(true);
	}
	
	public void selectNextEmail(boolean next) throws Throwable {
		Object key = null ;
		PanelQuery pq = getPanelQueryInboxAuswahl();
		if (next == true) {
			key = pq.holeKeyNaechsteZeile();
		} else {
			key = pq.holeKeyVorherigeZeile();
		}

		if (key != null) {
			pq.setSelectedId(key);
			getPanelSplitInboxAuswahl().eventYouAreSelected(false) ;			
		}		
	}
	
	public void loadInboxId(Integer inboxId) throws Throwable {
		getController().loadEmailMetaFromServer(inboxId) ;
		MediaEmailMetaDto emailMetaDto = getController().getEmailMetaDto() ;
		if(emailMetaDto != null) {
			getInternalFrame().setKeyWasForLockMe(inboxId.toString());
			panelInboxAuswahlPreview.setKeyWhenDetailPanel(inboxId.toString());
			panelSplitInboxAuswahl.setKeyWhenDetailPanel(inboxId.toString());
			panelSplitInboxAuswahl.updateButtons();
			panelInboxAuswahlPreview.eventYouAreSelected(false);
		}		
	}
	
	public void loadBelegId(Integer belegId) throws Throwable {
		getController().loadEmailReferenceFromServer(belegId) ;
		MediaStoreBelegDto storeBelegDto = getController().getBelegStoreDto() ;
		if(storeBelegDto != null) {
			getInternalFrame().setKeyWasForLockMe(belegId.toString());
			panelBelegAuswahlPreview.setKeyWhenDetailPanel(belegId.toString());
			panelSplitBelegAuswahl.setKeyWhenDetailPanel(belegId.toString());
			panelSplitBelegAuswahl.updateButtons();
			panelBelegAuswahlPreview.eventYouAreSelected(false);
		}		
	}
	
	public void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			log.info(msg.getText());
			msg.acknowledge();

			if (emailProgress != null) {
				String info = msg.getText();
				if (info.startsWith("count:")) {
					emailProgress.setTotalCount(Integer.parseInt(info
							.substring(6)));
					return;
				}

				if (info.startsWith("value:")) {
					emailProgress.setValue(Integer.parseInt(info.substring(6)));
					emailProgress.setInfo(info);
					return;
				}

				if (info.startsWith("info:")) {
					emailProgress.setInfo(info.substring(5));
					return;
				}
			}

		} catch (JMSException e) {
			log.error("JMSException", e);
		}
	}	
	
	private class EmailRetrieverWorker extends SwingWorker<Void, Void> {
		public EmailRetrieverWorker() {
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			try {
				getController().retrieveEmails() ;
			} catch(Throwable t) {
				
			} finally {
				try {
					stopRetrievingEmails(); 				
				} catch(Throwable t) {}
			}
			return null;
		}
	}
	
	private class EmailRetrieverContiniousWorker extends SwingWorker<Void, Void> {
		private volatile boolean doRun;
		private boolean completed ;
		private boolean enableEmailRetrieving ;
		
		public EmailRetrieverContiniousWorker() {
		}

		public void stop() {
			Object lock = new Object();
			synchronized (lock) {
				doRun = false ;
			}
		}
		
		public void enableEmailRetrieving(boolean enable) {
			this.enableEmailRetrieving = enable ;
		}
		
		public boolean isCompleted() {
			return completed ;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			try {
				doRun = true ;
				completed = false ;
				enableEmailRetrieving = true ;
				
				while(doRun) {
					if(enableEmailRetrieving) {
						startRetrievingEmailsUi() ;
						getController().retrieveEmails() ;
						publish();
					}
					
					for(int i = 0 ; i < 5 * 60; i++) {
						Thread.sleep(1000);
						if(!doRun) break ;
					}
				}
				completed = true ;
			} catch(Throwable t) {
				log.error("Throwable doInBackground, retrieveEmail", t) ;
			}
			
			return null;
		}
		
		@Override
		protected void process(List<Void> chunks) {
			try {
				stopRetrievingEmails() ;			
			} catch(Throwable t) {
				log.error("Throwable on stopRetrievingEmails", t) ;
			}
		}
	}
}
