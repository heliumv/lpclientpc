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

import java.awt.Font;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.media.service.MediaEmailAttachmentDto;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.util.Helper;

public class PanelInboxAuswahlPreview extends PanelBasis implements ListSelectionListener, ISupportTransferableEmailMeta {
	private static final long serialVersionUID = 5883469233008504240L;

	private JLabel wlaVon ;
	private WrapperTextField wtfVon ;
	private JLabel wlaDatum ;
	private WrapperTextField wtfDatum ;
	private JLabel wlaAn ;
	private WrapperTextField wtfAn ;
	private JLabel wlaCC ;
	private WrapperTextField wtfCC ;
	private WrapperTextField wtfBetreff ;
	private WrapperTextField wtfAttachments ;
	private WrapperTextArea wtaText ;
	private WrapperHtmlField wwwText ;
	private JScrollPane scrollPane ;
	private JComponent activeTextComponent ;
	
	private TabbedPaneMediaController controller ;
	private TabbedPaneMediaViewController viewController ;
	
	private static final String ACTION_FORWARD_EMAIL = PanelBasis.ACTION_MY_OWN_NEW 
			+ "special_action_forward_email";

	public PanelInboxAuswahlPreview(InternalFrame internalFrame, 
			TabbedPaneMediaViewController viewController, TabbedPaneMediaController controller) throws Throwable {
		super(internalFrame, "Preview") ;

		this.controller = controller ;
		this.viewController = viewController ;
		jbInit() ;		
	}

	private void jbInit() throws Throwable {
		
		createAndSaveAndShowButton(
				"/com/lp/client/res/mail_forward.png",
				LPMain.getTextRespectUISPr("lp.weiterleiten"),
				ACTION_FORWARD_EMAIL, RechteFac.RECHT_MEDIA_EMAIL_CUD);	

		String[] buttonsIUse = { ACTION_DELETE, ACTION_PREVIOUS, ACTION_NEXT } ;
		enableToolsPanelButtons(buttonsIUse) ;
				
		setLayout(new MigLayout("wrap 4, hidemode 3, nocache", "[fill,5%][fill,71%][fill,7%][fill,15%]")) ;
		add(getToolsPanel(), "span") ;
		
		wlaVon = new JLabel(LPMain.getTextRespectUISPr("media.inbox.preview.von")) ;
		add(wlaVon) ;
		
		wtfVon = new WrapperTextField(1024) ;
		add(wtfVon);
		
		wlaDatum = new JLabel(LPMain.getTextRespectUISPr("media.inbox.preview.datum")) ;
		add(wlaDatum) ;
		wtfDatum = new WrapperTextField() ;
		add(wtfDatum, "wrap") ;
		
		wlaAn = new JLabel(LPMain.getTextRespectUISPr("media.inbox.preview.an")) ;
		add(wlaAn) ;
		wtfAn = new WrapperTextField(1024) ;
		add(wtfAn, "span 3, wrap") ;

		wlaCC = new JLabel(LPMain.getTextRespectUISPr("media.inbox.preview.cc")) ;
		add(wlaCC) ;
		
		wtfCC = new WrapperTextField(1024) ;
		add(wtfCC, "span 3, wrap") ;

		wtfBetreff = new WrapperTextField(1024) ;
		Font boldFont =  wtfBetreff.getFont().deriveFont(Font.BOLD) ;
		wtfBetreff.setFont(boldFont);
		add(wtfBetreff, "span 4, wrap") ;
		
		wtfAttachments = new WrapperTextField(1024) ;
		wtfAttachments.setVisible(false) ;
		add(wtfAttachments, "hidemode 3, span 4, wrap, growy") ;
		
		wtaText = new WrapperTextArea() ;
		wtaText.setLineWrap(true);
		scrollPane = new JScrollPane(wtaText) ;
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) ;		
		add(scrollPane, "hidemode 3, span 4, pushx, growy") ;
		scrollPane.setVisible(false) ;
		wwwText = new WrapperHtmlField(getInternalFrame(), "", true) ;
		add(wwwText, "span 4, push, growy") ;
		wwwText.setVisible(true) ;
		activeTextComponent = wwwText ;
		
		add(getPanelStatusbar(), "span 4, bottom, pushy") ;
	}
	
	@Override
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			System.out.println("item: hole inbox-item in detail-panel  " + e.getSource()) ;			
//			if (e.getSource() == panelQueryFLRLager) {
//				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
//				holeLager((Integer) key);
//			} else if (e.getSource() == panelQueryFLRMontageart) {
//				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
//				holeMontageart((Integer) key);
//			}
		}
	}
	
	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false) ;

		dto2Components() ;
	}
	
	@Override
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if(!getController().hasInboxEntry()) return ; 
		
		if(!isLockedDlg()) {
			getController().removeInboxEntry((e.getModifiers() & ActionEvent.SHIFT_MASK) > 0) ;
			setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
		}		
	}
	
	@Override
	protected void eventActionNext(boolean next) throws Throwable {
		getViewController().selectNextEmail(next) ;
	}
	
	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_FORWARD_EMAIL)) {
			myLogger.info("ACTION_FORWARD_EMAIL") ;
			getViewController().showDialogPersonalWithEmail(); 
		}
	}
	
	private TabbedPaneMediaController getController() {
		return controller ;
	}
	
	private TabbedPaneMediaViewController getViewController() {
		return viewController ;
	}
	
//	private boolean isHtmlText(String content) {
//		if(content == null || content.isEmpty()) return false ;
//		
//		String s = content.substring(0, Math.min(20, content.length()) -1).trim().toLowerCase() ;
//		return s.startsWith("<html") || s.startsWith("<!doctype html") || s.startsWith("<meta http") ;
//	}
	
	private void clearFields() {
		wtfAn.setText("") ;
		wtfVon.setText("") ;
		wtfDatum.setText("") ;
		wtfBetreff.setText("");
		wtfAttachments.setVisible(false);
		wwwText.setText("");
		wtaText.setText("") ;
		activeTextComponent = wtaText ;
	}
		
	private void dto2Components() throws Throwable {
		MediaEmailMetaDto emailDto = getController().getEmailMetaDto() ;
		if(emailDto == null) {
			clearFields() ;
			return ;
		}
		
		wtfAn.setText(emailDto.getCTo());
		boolean ccVisible = emailDto.getCCc() != null && emailDto.getCCc().length() != 0 ;
		wlaCC.setVisible(ccVisible) ;
		wtfCC.setVisible(ccVisible) ;
		
		wtfCC.setText(emailDto.getCCc()) ;
		wtfVon.setText(emailDto.getCFrom());
			
//		wtfDatum.setText(emailDto.getTEmailDate().toString()) ;
		wtfDatum.setText(
				Helper.formatTimestamp(emailDto.getTEmailDate(), LPMain.getTheClient().getLocUi())) ; 
		wtfBetreff.setText(emailDto.getCSubject());
		String content = emailDto.getXContent() ;

		if(emailDto.getAttachments().isEmpty()) {
			wtfAttachments.setVisible(false) ;
		} else {
			String s = "" ;
			if(emailDto.getAttachments().size() == 1) {
				s += "1 Anhang:" ; 
			} else {
				s += emailDto.getAttachments().size() + " Anh\u00E4nge:" ;
			}
			
			boolean first = true ;
			for (MediaEmailAttachmentDto attachmentDto : emailDto.getAttachments()) {
				if(!first) {
					s += "," ;
				}
				s += " " + attachmentDto.getCName() + " (" + (attachmentDto.getSize() / 1000) + "KiB)" ;  
				first = false ;
			}
			
			wtfAttachments.setVisible(true) ;
			wtfAttachments.setText(s); 
		}
//		JCRMediaDto jcrDto = getController().getJCRMediaDto() ;
//		if(jcrDto != null)  {
//			content = new String(jcrDto.getContent()) ;
//		}

//		if(isHtmlText(content)) {
		if(emailDto.getBHtml() > 0) {
			activeTextComponent = wwwText ;
			wwwText.setText(content);
//			wwwText.setText("<html><body><h1>Hi!</h1></body></html>") ;
			scrollPane.setVisible(false) ;
			wwwText.setVisible(true);
		} else {
			activeTextComponent = scrollPane ;
			wtaText.setText(content) ;
			wwwText.setVisible(false) ;
			scrollPane.setVisible(true);
		}
		invalidate();
	}
	
	protected JComponent getFirstFocusableComponent() throws Exception {
		return activeTextComponent ;
	} 
	
	public void valueChanged(ListSelectionEvent e) {
		System.out.println("ListSelectionEvent:") ;
		
		if (e.getValueIsAdjusting() == false) {
		}
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MEDIAINBOX ;
	}
	
	@Override
	public MediaEmailMetaDto getTransferableEntity() {
		return getController().getEmailMetaDto() ;
	}
	
	@Override
	public void dropDone(Transferable data, int action) throws Throwable {
		myLogger.info("dropDone PanelAuswahlQuery action " + action + ".");
	}
}
