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
package com.lp.client.frame.component;

import static javafx.concurrent.Worker.State.FAILED;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.editor.PanelHtmlEditor;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.editor.util.TextBlockOverflowException;

public class WrapperHtmlField extends PanelBasis implements IControl {
	private static final long serialVersionUID = 7656269773690098160L;
	private static final String ACTION_SPECIAL_EDITORFIELD_EDIT = "action_special_editorfield_edit";
	private static final String ACTION_SPECIAL_EDITORFIELD_DEFAULT = "action_special_editorfield_default";

	private boolean isMandatoryField = false ;
	private boolean isActivatable = true;
	private WebPanelInstaller webPanel = null ;
	
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
//	private JPanel jpaWorkingOn = new JPanel();
	private WrapperButton wbuEdit = new WrapperButton();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDefault = new WrapperButton();
	private boolean bWithoutButtons = false;
	private boolean isEditMode = false ;
	
//	private JPanel installPanel = new JPanel() ;
	private JPanel installPanel = this ;
	
	public WrapperHtmlField(
			InternalFrame internalFrame, String addTitleI) throws Throwable {
		this(internalFrame, addTitleI, false);
	}

	public WrapperHtmlField(
			InternalFrame internalFrame, String addTitleI, boolean bWithoutButtons) throws Throwable {
		super(internalFrame, addTitleI);
		this.bWithoutButtons = bWithoutButtons;
	
		jbInit();
		initComponents();
		invalidate() ;

		addComponentListener(new ComponentListener() {			
			@Override
			public void componentShown(ComponentEvent arg0) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				Component c = e.getComponent() ;
				myLogger.info("componentresized to width: " + c.getWidth() + " height " + c.getHeight() + ".") ;
				
				if(webPanel != null) {
					webPanel.resizeTo(c.getWidth(), c.getHeight());
				}
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
		webPanel = new WebPanelInstaller(installPanel) ;
		SwingUtilities.invokeLater(webPanel);
	}
		
	public WrapperHtmlField(JPanel parentPanel) {
		if(parentPanel == null) throw new IllegalArgumentException("null") ;
		
		jbInit() ;
		webPanel = new WebPanelInstaller(installPanel) ;
		SwingUtilities.invokeLater(webPanel);
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return null ;
	}
	
	@Override
	public boolean isMandatoryField() {
		return isMandatoryField ;
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField ;
	}

	@Override
	public boolean isActivatable() {
		return isActivatable ;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable ;
	}

	public void setEditable(boolean bEnabled) {
		wbuDefault.setEnabled(bEnabled);
		wbuEdit.setEnabled(bEnabled);
	}
	
	@Override
	public void removeContent() throws Throwable {
		setContent("") ;
	}

	@Override
	public boolean hasContent() throws Throwable {
		return !getText().isEmpty() ;
	}
	
	public void setContent(final String webContent) {
		myLogger.info("setContent:" + webContent + ".");
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				getWebPanel().setContent(webContent);				
			}
		});
	}
	
	public void setText(final String text) {
		System.out.println("(" + this.toString() + ") setText to: <" + text + ">") ;
		setContent(text == null ? "" : text) ;
	}
	
	
	public String getText() throws TextBlockOverflowException {
		String s = getWebPanel().storeAndRetrieveContent() ;

		System.out.println("(" + this.toString() + ") getText <" + s + ">") ;
		return s ;
	}

	/**
	 * Auf Basis des Editor-Styles einen Plaintext zur&uuml;ckliefern
	 * 
	 * @return Text ohne HTML Encoding sofern im Editor isStyledText == false
	 * @throws TextBlockOverflowException
	 */
	public String getPlainText() throws TextBlockOverflowException {
		return getWebPanel().getContent() ;
	}

	public String getHtmlText() throws TextBlockOverflowException {
		String theText = getWebPanel().getContent() ;

		String content = "<html lang=\"de\">" +
				"<head>" +
				"</head>" +
				"<body>" +
				theText + 
				"</body>" +
				"</html>" ;		
		return content ;
	}
	
	public void startEditing() {
		System.out.println("Editing mode") ;
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				getWebPanel().activateEditMode();				
				isEditMode = true ;
			}
		});			
	}
	
	private WebPanelInstaller getWebPanel() {
		return webPanel ;
	}
	
	protected void eventKeyPressed(KeyEvent e) throws Throwable {
		int id = e.getID() ;

		String keyString;
		if (id == KeyEvent.KEY_TYPED) {
			char c = e.getKeyChar();
			keyString = "key character = '" + c + "'";
		} else {
			int keyCode = e.getKeyCode();
			keyString = "key code = " + keyCode + " ("
					+ KeyEvent.getKeyText(keyCode) + ")";
		}

		int modifiersEx = e.getModifiersEx();
		String modString = "extended modifiers = " + modifiersEx;
		String tmpString = KeyEvent.getModifiersExText(modifiersEx);
		if (tmpString.length() > 0) {
			modString += " (" + tmpString + ")";
		} else {
			modString += " (no extended modifiers)";
		}

		String actionString = "action key? ";
		if (e.isActionKey()) {
			actionString += "YES";
		} else {
			actionString += "NO";
		}

		System.out.println("keystring: " + keyString + " modString: " + modString + " tmpstring: " + tmpString + " actionString: " + actionString) ;
	}
	
	private void jbInit() {
		
		getInternalFrame().addItemChangedListener(this);


//		setLayout(gridBagLayout1);
//		add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
//				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
//						0, 0, 0, 0), 0, 0));
		if (!bWithoutButtons) {
			setLayout(gridBagLayout1) ;
			JPanel jpaWorkingOn = new JPanel() ;
			add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 0, 0), 0, 0));
			
			wbuEdit.setMinimumSize(new Dimension(23, 23));
			wbuEdit.setPreferredSize(new Dimension(23, 23));
			wbuEdit.setActionCommand(ACTION_SPECIAL_EDITORFIELD_EDIT);
			wbuEdit.setToolTipText(LPMain.getTextRespectUISPr("text.bearbeiten"));
			wbuEdit.setIcon(IconFactory.getEdit());
			wbuEdit.addActionListener(this);
			wbuEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke('T',
							java.awt.event.InputEvent.CTRL_MASK),
							ACTION_SPECIAL_EDITORFIELD_EDIT);
			wbuEdit.getActionMap().put(ACTION_SPECIAL_EDITORFIELD_EDIT,
					new ButtonAbstractAction(this, ACTION_SPECIAL_EDITORFIELD_EDIT));

			wbuDefault.setMinimumSize(new Dimension(23, 23));
			wbuDefault.setPreferredSize(new Dimension(23, 23));
			wbuDefault.setActionCommand(ACTION_SPECIAL_EDITORFIELD_DEFAULT);
			wbuDefault.setToolTipText(LPMain.getTextRespectUISPr("text.default"));
			wbuDefault.setIcon(IconFactory.getReset());
			wbuDefault.addActionListener(this);

			jpaWorkingOn.setLayout(gridBagLayout2);
			jpaWorkingOn.add(wbuEdit,
					new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0,
									0), 0, 0));
			jpaWorkingOn.add(wbuDefault,
					new GridBagConstraints(1, 1, 1, 1, 0.0, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0,
									0), 0, 0));

			JScrollPane scrollPane = new JScrollPane(installPanel) ;
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jpaWorkingOn.add(scrollPane, new GridBagConstraints(0, 0, 1, 2, 1.0,
					1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 2), 0, 0));
		} else {
//			setLayout(new FlowLayout());
//			add(installPanel) ;
			setLayout(new BorderLayout()) ;
//			add(installPanel, BorderLayout.CENTER) ;
//			jpaWorkingOn.add(installPanel, new GridBagConstraints(0, 0, 1, 2, 1.0,
//			1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//			new Insets(0, 0, 0, 2), 0, 0));
		}
		
		invalidate(); 
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_DEFAULT)) {
			// TODO: set Default Text
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_EDIT)) {
			
			getInternalFrame().showPanelDialog(
					new PanelHtmlEditor(getInternalFrame(), this, "Edit", getText(), PanelBasis.LOCK_NO_LOCKING)); 
			
			System.out.println("Leaved editing mode!") ;
		}
	}
	
	protected JComponent getFirstFocusableComponent() throws Exception {
		return installPanel ;
	}
	
	
	private class WebPanelInstaller implements Runnable {
		private JPanel theParentPanel ;
		private JFXPanel webPanel = null ;
		private Browser webBrowser = null ;
//		private WebEngine engine = null ;
//		private boolean isEditMode = false ;
//		private WebView webView = null ;
		
		private String queuedContent = null ;
		
		public WebPanelInstaller(JPanel parentPanel) {
			theParentPanel = parentPanel ;
		}

		@Override
		public void run() {
			installWebPanel() ;
			theParentPanel.add(webPanel, BorderLayout.CENTER) ;			
		}
		
		public void resizeTo(int width, int height) {
			if(webPanel != null) {
				myLogger.info("HtmlFieldSize: width " + webPanel.getWidth() + " height: " + webPanel.getHeight() + ".");
				myLogger.info("new jfxpanel size width " + width + " height " + height + ".");
				Scene s = webPanel.getScene() ;
				if(s != null) {
					myLogger.info("actual scene size width " + s.getWidth() + " height " + s.getHeight() + ".");
					webPanel.setSize(width, height);
					myLogger.info("new scene size width " + s.getWidth() + " height " + s.getHeight() + ".");
					
//					myLogger.info("webview actual size " + webView.isResizable() + " " + webView.getWidth() + " height " + webView.getHeight() + ".");
//					webView.setMinSize(width, height) ;
//					webView.setPrefSize(width, height);
//					webView.resize(width, height);
//					myLogger.info("webview new size " +webView.getWidth() + " height " + webView.getHeight() + "." );
//					webView.autosize();					
				}
//				webView.autosize();
			}
		}
		
		public void setContent(String webContent) {
			myLogger.info("setContent (" + (webBrowser != null) + ") + setContent" + webContent + ".");
			if(webBrowser != null) {
				webBrowser.setContent(webContent);
				queuedContent = null ;
			} else {
				queuedContent = webContent ;
			}
		}
		
		public String storeAndRetrieveContent() {
			return webBrowser.storeAndRetrieveContent() ;
		}
		
		public String getContent() {
			return webBrowser.getContent() ;
		}
		
		public void activateEditMode() {
			webBrowser.activateEditMode() ;
		}

		private void installWebPanel() {
			webPanel = new JFXPanel();
			Platform.setImplicitExit(false);
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					
					final Browser b = new Browser(getInternalFrame(), theParentPanel) ;
					Scene scene = new Scene(b, 750, 500, Color.web("#666970")) ;
					webPanel.setScene(scene) ;

				     scene.widthProperty().addListener(
				                new ChangeListener() {
				                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				                        Double w = (Double)newValue;
				    					myLogger.info("cangelistener width: " + oldValue  + " newValue: " + w) ;
				                        b.getWebView().setPrefWidth(w);
				                        b.adjustHeight() ;
//				                        webBrowser.getWebView().layout();
				                    }
				                });
//				     scene.heightProperty().addListener(
//				                new ChangeListener() {
//				                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//				                        Double w = (Double)newValue;
//				    					myLogger.info("cangelistener height: " + oldValue  + " newValue: " + w) ;
//				                        webBrowser.getWebView().setPrefHeight( w - 20 );
//				                        webBrowser.getWebView().layout();
//				                    }
//				                });

				     webBrowser = b ;
				     if(queuedContent != null) {
				    	 myLogger.info(">>> Setting queued content: " + queuedContent);
				    	 setContent(queuedContent) ;
				     } else {
				    	 myLogger.info(">>> Setting empty content");
				    	 setContent("") ;
				     }
				}
			});
		}	
	}	
}

class Browser extends Region {
	protected final LpLogger log = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private WebView browser = new WebView();
	private WebEngine webEngine = browser.getEngine();
	private boolean isEditMode = false ;
	private String cachedText = "" ;
	private String cachedScriptName ;
	private InternalFrame hvInternalFrame ;
	private JComponent hvWrappingComponent ;
	
	public Browser(InternalFrame internalFrame, JComponent wrappingComponent) {
		hvInternalFrame = internalFrame ;
		hvWrappingComponent = wrappingComponent ;
		
		// apply the styles
		getStyleClass().add("browser");
		// load the web page
		webEngine.setJavaScriptEnabled(true);
//		webEngine.load("http://www.oracle.com/products/index.html");
		// add the web view to the scene
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
		    @Override
		    public void changed(ObservableValue<? extends State> arg0, State oldState, State newState) {
		        if (newState == State.SUCCEEDED) {
		            adjustHeight();
		        }    
		    }
		});
		
		webEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {

			public void changed(
					ObservableValue<? extends Throwable> o,
					Throwable old, final Throwable value) {
				if (webEngine.getLoadWorker().getState() == FAILED) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane
									.showMessageDialog(hvInternalFrame
											,
											(value != null) ? webEngine
													.getLocation()
													+ "\n"
													+ value.getMessage()
													: webEngine.getLocation()
															+ "\nUnexpected error.",
											"Loading error...",
											JOptionPane.ERROR_MESSAGE);
						}
					});
				}
			}
		});
		getChildren().add(browser);
	}

	public WebView getWebView() {
		return browser ;
	}
	
	private Node createSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return 650;
	}

	@Override
	protected double computePrefHeight(double width) {
		return 400;
	}
	
	protected String getCachedText() {
		return cachedText ;
	}
	
	protected void setCachedText(String newText) {
		cachedText = newText ;
	}
	
	private String getScriptName() {
		String port = "8080" ;
		try {
			port = DelegateFactory.getInstance().getSystemDelegate().getServerWebPort() ;
		} catch(Throwable t) {
		}
		return "http://" + HelperClient.getServerName() + ":" + port + "/tinymce/tinymce.min.js";
	}

	private String getCachedScriptName() {
		if(cachedScriptName == null) {
			cachedScriptName = getScriptName() ;
		}
		return cachedScriptName ;
	}
	
	protected String getCssStyle() {
		return "<style>" +
				"blockquote { " +
				"font: 14px/22px normal helvetica, sans-serif; " +
				"margin-top: 10px; " +
				"margin-bottom: 10px; " +
				"margin-left: 50px; " +
				"padding-left: 15px; " +
				"border-left: 3px solid #ccc; " +
				"}" +
				"body { " +
				"margin: 0px; " +
				"padding: 0px; " +
				"overflow-y: scroll; " +
				"}" + 
				"</style>" ;
				
	}	
	
	protected String buildHtmlViewContent(String theText) {
		if(theText.startsWith("<html")) {
			String s = theText.replaceFirst("<body>", "<body><div id=\"hvdivsizer\">") ;
			s = s.replaceFirst("</body>", "</div></body>") ;
			return s ;
		}
		
		String content = "<html lang=\"de\">" +
			"<head>" +
			getCssStyle() +
			"</head>" +
			"<body><div id=\"hvdivsizer\">" +
			theText + 
			"</div></body>" +
			"</html>" ;
		return content ;
	}
	
	protected String buildHtmlEditContent(String theText) {
//		Dimension d = webPanel.getSize() ;
//		Dimension d1 = installPanel.getSize() ;
//		Dimension d2 = theParentPanel.getSize() ;
//		Dimension d3 = getInternalFrame().getContentPane().getSize() ;
		Dimension d4 = hvWrappingComponent.getSize() ;
		int w = (int) d4.getWidth() - 10 ;
		int h = (int) d4.getHeight() - 10 ;
		
		log.info("cangelistener width: " + w  + " height: " + h) ;

		String content = "<html lang=\"de\">" +
			"<head>" +
			"<script src=\"" + getCachedScriptName() + "\"></script>" +
			getCssStyle() +
			"<script>" +
			"tinymce.init({" + 
/*			"language:\"de\", width:\"" + (w - 10) + "\", height:\"" + (h - 10) + "\", selector:\"textarea\"," + */
			"language:\"de\", width:\"100%\", height:\"100%\", selector:\"textarea\"," +
			"mode : \"textareas\"," +
			"plugins: [" +
			"\"advlist autolink lists link image charmap print preview anchor\"," +
			"\"searchreplace visualblocks code fullscreen\"," +
			"\"insertdatetime media table contextmenu paste textcolor \"" +
			"]," +
			"menubar: \"edit insert view format table tools\"," + 
//			"toolbar_items_size: 'small', " + 
			"resize: \"both\"," +
			"theme_advanced_resizing: \"true\"," +
			"theme_advanced_sesizing_use_cookie: \"false\"," +
			"toolbar: \"undo redo | searchreplace | styleselect | fontselect fontsizeselect forecolor backcolor bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image \"" + 
			"});" +
			"function formSubmit() {" +
			"document.getElementById('mceform').submit() ; " +
			"return document.getElementById('mcedata').value ;" +
			"}" +
			"function ajaxSave() {" +
			"tinymce.triggerSave();" +
			"var ed = tinymce.get('mcedata') ;" +
			"return ed.getContent();" +
			"}" +
			"</script>" +
			"</head>" +
			"<body><div id=\"hvdivsizer\">" +
//			"<form id=\"mceform\" method=\"post\">" +
		    "<textarea id=\"mcedata\" style=\"width:100%\">" + theText + "</textarea>" +
//		    "</form>" +
			"</div></body>" +
			"</html>" ;
		return content ;
	}
	

	public void adjustHeight() {
	    Platform.runLater(new Runnable(){
	        @Override                                
	        public void run() {
	            try {
	                Object result = webEngine.executeScript(
	                    "document.getElementById('hvdivsizer').offsetHeight");
	                System.out.println("Got result " + result) ;
	                if(result instanceof Integer) {
	                    Integer i = (Integer) result;
	                    double height = new Double(i);
	                    height = height + 20;
	                    browser.setPrefHeight(height);
	                }
	            } catch (Exception e) {
	                // not important
	            } catch(Throwable t) {
	            	System.out.println("throwable" + t.getMessage()) ;
	            }
	        }               
	    });
	}
	
	public void setContent(final String webContent) {
		log.info("setContent: " + webContent + ".") ;
		setCachedText(webContent) ;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				isEditMode = false ;
				String content = buildHtmlViewContent(getCachedText()) ;
				webEngine.loadContent(content);
			}
		});
	}
	
	public String getContent() {
		return storeAndRetrieveContent() ;
	}
	
	public void storeContent() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Object o = webEngine.executeScript("formSubmit();") ;
				System.out.println("executed script " + o.toString() ) ;
			}
		});
	}

	public void activateEditMode() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				isEditMode = true ;
				String content = buildHtmlEditContent(getCachedText()) ;
				webEngine.loadContent(content);
			}
		});			
	}
	
	private class RunnableTextHolder extends SwingWorker<String, Object> {
		private String valueHolder ;

		@Override
		protected String doInBackground() throws Exception {
			final CountDownLatch latch = new CountDownLatch(1) ;
			final String[] innerResult = new String[1] ;
			
			Runnable theTask = new Runnable() {					
				@Override public void run() {
					try {
						String t = (String) webEngine.executeScript("ajaxSave();") ;
						innerResult[0] = t ;
						latch.countDown(); 
						System.out.println("Triggered latch") ;
					} catch(Exception e){
						System.out.println("Exception e " + e.getMessage()) ;
						latch.countDown();
					}
				}
			} ;
			
			Platform.runLater(theTask) ;
			latch.await();
			System.out.println("latch triggered") ;

			valueHolder = innerResult[0] ;
			
			System.out.println("valueHolder result = <" + valueHolder + ">") ;
			return innerResult[0] ;
		}
		
		@Override
		protected void done() {
			try {
				valueHolder = get() ;
			} catch(InterruptedException e) {					
			} catch(ExecutionException e) {
			}
		}
		
		public String getMyValue() {
			try {
				get() ;
			} catch(InterruptedException e) {
			} catch(ExecutionException e) {					
			}
			return valueHolder ;
		}		
	}
	
	public String storeAndRetrieveContent() {
		if(isEditMode) {
			RunnableTextHolder textHolder = new RunnableTextHolder() ;
			textHolder.execute() ;
			String t = textHolder.getMyValue() ;
			if(t != null) {
				setCachedText(t) ;
			}
		}

		String result = getCachedText() ;
		return result ;
	}
	
}