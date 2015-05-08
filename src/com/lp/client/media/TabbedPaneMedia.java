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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TabbedPaneMedia extends TabbedPane {
	private static final long serialVersionUID = -164576184991037149L;

	private PanelSplit panelSplitInboxAuswahl = null ;
	private DragPanelQuery mediaInboxAuswahl = null; 
	private PanelInboxAuswahlPreview  panelInboxAuswahlPreview = null ;
	
	private PanelSplit panelSplitBelegAuswahl = null ;
	private PanelQuery mediaBelegAuswahl = null; 
	private PanelBelegAuswahlPreview  panelBelegAuswahlPreview = null ;
	
	
	private TabbedPaneMediaController controller ;
	private TabbedPaneMediaViewController viewController ;
//	private PanelRetrieveEmailProgress emailProgress ;
	private WrapperCheckBox wcbAutoReceive ;
	private JPopupMenu popupMenu ;
	
	public static final int IDX_PANEL_MEDIAINBOXAUSWAHL = 0;
	public static final int IDX_PANEL_MEDIABELEGAUSWAHL = 1 ;
	
	private static final String ACTION_RETRIEVE_EMAIL = PanelBasis.LEAVEALONE
			+ "special_action_retrieve_email";

	public TabbedPaneMedia(InternalFrame internalFrame) throws Throwable {
		super(internalFrame, LPMain.getTextRespectUISPr("media.inbox.modulname")) ;

		controller = new TabbedPaneMediaController(internalFrame) ;
		viewController = new TabbedPaneMediaViewController(internalFrame, controller) ;
		
		jbInit() ;
		initComponents() ;
	}
	

	protected TabbedPaneMediaController getController() {
		return controller ;
	}
	
	protected TabbedPaneMediaViewController getViewController() {
		return viewController ;
	}
	
	private void jbInit() throws Throwable {
	    popupMenu = new JPopupMenu();
	    ActionListener menuListener = new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	        System.out.println("Popup menu item ["
	            + event.getActionCommand() + "] was pressed.");
	      }
	    };
	    
	    JMenuItem item;
	    popupMenu.add(item = new JMenuItem(new MoveToKurzbriefAction(controller, "Kurzbrief")));
	    item.addActionListener(menuListener);

	    popupMenu.add(getViewController().getPersonalSelectMenu()) ;	    
	    popupMenu.add(item = new JMenuItem("Projekt"));

	    item.setActionCommand("MoveToProjekt");
	    item.addActionListener(menuListener);
	    popupMenu.add(item = new JMenuItem("Right"));
	    item.addActionListener(menuListener);
	    popupMenu.add(item = new JMenuItem("Full"));
	    item.addActionListener(menuListener);
	    popupMenu.addSeparator();
	    popupMenu.add(item = new JMenuItem("Settings . . ."));
	    item.addActionListener(menuListener);

	    popupMenu.setLabel("Justification");
	    popupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));

		refreshInboxAuswahl() ;
		
		insertTab(LPMain.getTextRespectUISPr("media.inbox.auswahl"), null,
				getViewController().getPanelSplitInboxAuswahl(),
				LPMain.getTextRespectUISPr("media.inbox.auswahl"),
				IDX_PANEL_MEDIAINBOXAUSWAHL);

		insertTab(LPMain.getTextRespectUISPr("media.beleg.auswahl"), null,
				null,
				LPMain.getTextRespectUISPr("media.beleg.auswahl"),
				IDX_PANEL_MEDIABELEGAUSWAHL);

		resetDtos() ;
		setKeyWasForLockMe() ;
		
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}
	
	private void resetDtos() {
	}
	
	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = mediaInboxAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	
	@Override
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if(e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if(e.getSource() == getPanelInboxQueryAuswahl()) {
				Object key = getPanelInboxQueryAuswahl().getSelectedId() ;
				System.out.println("detail: hole inbox-item " + key) ;
			} else  if(e.getSource() == getViewController().getPersonalWithEmailDialog()){
				getViewController().moveMyInboxElementToOtherInbox() ;
				Object next = getPanelInboxQueryAuswahl().getId2SelectAfterDelete() ;
				getPanelInboxQueryAuswahl().setSelectedId(next) ;				
			}			
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if(e.getSource() == getPanelInboxQueryAuswahl()) {
				Object key = getPanelInboxQueryAuswahl().getSelectedId() ;
				System.out.println("item: hole inbox-item " + key) ;
				getViewController().loadInboxId((Integer) key) ;
//				initializeDtos((Integer) key) ;
				
				if(e.getEvent() != null && SwingUtilities.isRightMouseButton(e.getEvent())) {
					MouseEvent me = e.getEvent() ;
			        popupMenu.show(me.getComponent(), me.getX(), me.getY()) ;		           					
				}
			} else if(e.getSource() == getPanelBelegQueryAuswahl()) {
				Object key = getPanelBelegQueryAuswahl().getSelectedId() ;
				System.out.println("item: hole beleg-item " + key) ;
				getViewController().loadBelegId((Integer) key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String aspectInfo = ((ISourceEvent) e.getSource()).getAspect() ;
			if(ACTION_RETRIEVE_EMAIL.equals(aspectInfo)) {
				getViewController().startRetrievingEmails(); 
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if(e.getSource() == getPanelInboxAuswahlPreview()) {
				if(getPanelInboxAuswahlPreview().getKeyWhenDetailPanel() == null) {
					Object next = getPanelInboxQueryAuswahl().getId2SelectAfterDelete() ;
					getPanelInboxQueryAuswahl().setSelectedId(next) ;
				}
				setKeyWasForLockMe();
				panelSplitInboxAuswahl.eventYouAreSelected(false);
			}
		} else if(e.getID() == ItemChangedEvent.ACTION_DROP_DONE){
			myLogger.info("Drop done on " + e.getSource()) ;
			
			Object next = getPanelInboxQueryAuswahl().getId2SelectAfterDelete() ;
			getPanelInboxQueryAuswahl().setSelectedId(next) ;
		}
	}
	
	private void initializeDtos(Integer inboxId) throws Throwable {
		getController().loadEmailMetaFromServer(inboxId) ;
		MediaEmailMetaDto emailMetaDto = getController().getEmailMetaDto() ;
		if(emailMetaDto != null) {
			getInternalFrameMedia().setKeyWasForLockMe(inboxId.toString());
			getPanelInboxAuswahlPreview().setKeyWhenDetailPanel(inboxId.toString());
			panelSplitInboxAuswahl.setKeyWhenDetailPanel(inboxId.toString());
			panelSplitInboxAuswahl.updateButtons();
			getPanelInboxAuswahlPreview().eventYouAreSelected(false);
		}
	}	
	

	private PanelQuery getPanelInboxQueryAuswahl() throws Throwable {
		if (mediaInboxAuswahl == null) {

			FilterKriterium[] filterAuswahl = MediaFilterFactory.getInstance().createFKMyInbox() ;
			String[] aWhichButtonIUse = {  };

			mediaInboxAuswahl = new DragPanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_MEDIA_INBOX, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("media.inbox.auswahl"), 
					true, MediaFilterFactory.getInstance().createFKVInbox(), null);

			mediaInboxAuswahl.createAndSaveAndShowButton(
					"/com/lp/client/res/inbox_into16x16.png",
					LPMain.getTextRespectUISPr("media.inbox.empfangen"),
					ACTION_RETRIEVE_EMAIL, null);
			((LPButtonAction) mediaInboxAuswahl.getHmOfButtons().get(
					ACTION_RETRIEVE_EMAIL)).getButton().setEnabled(true);		
			createAutoReceiveButton(mediaInboxAuswahl) ;
			
			FilterKriteriumDirekt fkFrom = MediaFilterFactory.getInstance().createFKDMetaFrom() ;
			FilterKriteriumDirekt fkSubject = MediaFilterFactory.getInstance().createFKDMetaSubject() ;
			mediaInboxAuswahl.befuellePanelFilterkriterienDirekt(fkFrom, fkSubject);			
			mediaInboxAuswahl.installDragSupport(getController());
			
			getViewController().setPanelQueryInboxAuswahl(mediaInboxAuswahl);
		}
		
		return mediaInboxAuswahl ;
	}
	
	private void createAutoReceiveButton(PanelQuery queryPanel) {
		try {
			JPanel toolsPanel = queryPanel.getToolBar().getToolsPanelLeft() ;
			toolsPanel.add(getAutoReceiveCheckbox()) ;
		} catch(Exception e){
		}
	}
	
	private WrapperCheckBox getAutoReceiveCheckbox() {
		if(null == wcbAutoReceive) {
			wcbAutoReceive = new WrapperCheckBox(
					LPMain.getTextRespectUISPr("media.inbox.autoempfang"));
			wcbAutoReceive.setSelected(false);
			wcbAutoReceive.setActivatable(true);
			wcbAutoReceive.setEnabled(true);
			Dimension d = new Dimension(
					150, Defaults.getInstance().getControlHeight());
			wcbAutoReceive.setPreferredSize(d);
			wcbAutoReceive.setMinimumSize(d);
			wcbAutoReceive.setMnemonic('A');

			wcbAutoReceive.addActionListener(this);			
		}
		return wcbAutoReceive ;
	}
	
	private PanelBasis getPanelInboxAuswahlPreview() throws Throwable {
		if(panelInboxAuswahlPreview == null) {
			panelInboxAuswahlPreview = new PanelInboxAuswahlPreview(getInternalFrame(), getViewController(), getController()) ;
			getViewController().setPanelInboxAuswahlPreview(panelInboxAuswahlPreview);
		}
		return panelInboxAuswahlPreview ;
	}
	
	private PanelSplit getPanelSplitInboxAuswahl(boolean needInstantiationIfNull) throws Throwable {
		if(panelSplitInboxAuswahl == null && needInstantiationIfNull) {
			panelSplitInboxAuswahl = new PanelSplit(getInternalFrame(), getPanelInboxAuswahlPreview(), getPanelInboxQueryAuswahl(), 400) ;
			panelSplitInboxAuswahl.beOneTouchExpandable();
			panelSplitInboxAuswahl.getPanelSplit().addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
//					myLogger.info("Splitpane propertychange on " + evt.getPropertyName()) ;
					if("dividerLocation".equals(evt.getPropertyName())) {
						int newValue = (Integer) evt.getNewValue() ;
						if(newValue == 1) {
							myLogger.info("Splitpane: messagview maximized!") ;							
						}
					}
				}
			});
			getViewController().setPanelSplitInboxAuswahl(panelSplitInboxAuswahl) ;			
		}
		
		return panelSplitInboxAuswahl ;
	}
	
	private void refreshInboxAuswahl() throws Throwable {
		getPanelSplitInboxAuswahl(true) ;
	}

	private PanelBasis getPanelBelegAuswahlPreview() throws Throwable {
		if(panelBelegAuswahlPreview == null) {
			panelBelegAuswahlPreview = new PanelBelegAuswahlPreview(getInternalFrame(), getViewController(), getController()) ;
			getViewController().setPanelBelegAuswahlPreview(panelBelegAuswahlPreview);
		}
		return panelBelegAuswahlPreview ;
	}
	
	private PanelSplit getPanelSplitBelegAuswahl(boolean needInstantiationIfNull) throws Throwable {
		if(panelSplitBelegAuswahl == null && needInstantiationIfNull) {
			panelSplitBelegAuswahl = new PanelSplit(getInternalFrame(), getPanelBelegAuswahlPreview(), getPanelBelegQueryAuswahl(), 400) ;
			panelSplitBelegAuswahl.beOneTouchExpandable();
			panelSplitBelegAuswahl.getPanelSplit().addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
//					myLogger.info("Splitpane propertychange on " + evt.getPropertyName()) ;
					if("dividerLocation".equals(evt.getPropertyName())) {
						int newValue = (Integer) evt.getNewValue() ;
						if(newValue == 1) {
							myLogger.info("Splitpane: messagview maximized!") ;							
						}
					}
				}
			});
			setComponentAt(IDX_PANEL_MEDIABELEGAUSWAHL, panelSplitBelegAuswahl);			
			getViewController().setPanelSplitBelegAuswahl(panelSplitBelegAuswahl) ;
		}
		
		return panelSplitBelegAuswahl ;
	}
	
	private void refreshBelegAuswahl() throws Throwable {
		getPanelSplitBelegAuswahl(true) ;
	}
	

	private PanelQuery getPanelBelegQueryAuswahl() throws Throwable {
		if (mediaBelegAuswahl == null) {
			FilterKriterium[] filterAuswahl = MediaFilterFactory.getInstance().createFKMyStoreBeleg() ;
			String[] aWhichButtonIUse = {} ;

			mediaBelegAuswahl = new PanelQuery(null, filterAuswahl,
					QueryParameters.UC_ID_MEDIA_BELEG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("media.beleg.auswahl"), 
					true);
			
			FilterKriteriumDirekt fkFrom = MediaFilterFactory.getInstance().createFKDStoreBelegFrom() ;
			FilterKriteriumDirekt fkSubject = MediaFilterFactory.getInstance().createFKDStoreBelegSubject() ;
			mediaBelegAuswahl.befuellePanelFilterkriterienDirekt(fkFrom, fkSubject);			
			
			getViewController().setPanelQueryBelegAuswahl(mediaBelegAuswahl);
		}
		
		return mediaBelegAuswahl ;
	}
	
	public InternalFrameMedia getInternalFrameMedia() throws Throwable {
		return (InternalFrameMedia) getInternalFrame() ;
	}
	
	@Override
	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
		return wrapperMenuBar ;
	}

	@Override
	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if(e.getSource() == wcbAutoReceive) {
			getViewController().enableAutoReceive(wcbAutoReceive.isSelected());
		}
	}


	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
			case IDX_PANEL_MEDIAINBOXAUSWAHL:
				refreshInboxAuswahl();
				mediaInboxAuswahl.eventYouAreSelected(false);
				mediaInboxAuswahl.updateButtons();
			break;
			
			case IDX_PANEL_MEDIABELEGAUSWAHL:
				refreshBelegAuswahl();
				mediaBelegAuswahl.eventYouAreSelected(false);
				mediaBelegAuswahl.updateButtons();
			break ;
		}
	}
}
