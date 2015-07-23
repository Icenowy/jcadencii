/*
 * FormCompileResult.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.cadencii.
 *
 * org.kbinani.cadencii is free software; you can redistribute it and/or
 * modify it under the terms of the GPLv3 License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.cadencii;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BTextArea;


import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormCompileResult extends BDialog {
        public FormCompileResult( String message, String errors )
        {
super();
initialize();
registerEventHandlers();
setResources();
applyLanguage();
label1.setText( message );
textBox1.setText( errors );
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public void applyLanguage()
        {
textBox1.setText( _( "Script Compilation Result" ) );
        }

        private static String _( String id )
        {
return Messaging.getMessage( id );
        }

        private void setResources()
        {
        }

        private void registerEventHandlers()
        {
btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
        }


    private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private BLabel label1 = null;
	private BTextArea textBox1 = null;
	private BButton btnOK = null;


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(386, 309);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.EAST;
			gridBagConstraints2.insets = new Insets(0, 16, 16, 12);
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(10, 16, 16, 16);
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(23, 16, 0, 0);
			gridBagConstraints.gridy = 0;
			label1 = new BLabel();
			label1.setText("BLabel");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(label1, gridBagConstraints);
			jContentPane.add(getTextBox1(), gridBagConstraints1);
			jContentPane.add(getBtnOK(), gridBagConstraints2);
		}
		return jContentPane;
	}

	/**
	 * This method initializes textBox1	
	 * 	
	 * @return javax.swing.BTextArea	
	 */
	private BTextArea getTextBox1() {
		if (textBox1 == null) {
			textBox1 = new BTextArea();
			textBox1.setLineWrap(true);
		}
		return textBox1;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new BButton();
			btnOK.setText("OK");
			btnOK.setPreferredSize(new Dimension(100, 29));
		}
		return btnOK;
	}

    }

