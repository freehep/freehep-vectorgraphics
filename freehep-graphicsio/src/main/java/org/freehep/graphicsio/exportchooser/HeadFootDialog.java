// Copyright 2001 freehep
package org.freehep.graphicsio.exportchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.freehep.graphics2d.TagString;
import org.freehep.graphicsio.MultiPageDocument;

/**
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/HeadFootDialog.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class HeadFootDialog extends JDialog implements ActionListener, ItemListener {

    private static final String[] HF_LABELS = { "Headline", "Footline" };

    private JTextField textField[][];
    private JCheckBox useCheckBox[];
    private TagString text[][];

    public HeadFootDialog() {
	super();
	setTitle("Head- and footlines");
	setModal(true);
	getContentPane().setLayout(new BorderLayout());

	textField = new JTextField[HF_LABELS.length][];
	useCheckBox = new JCheckBox[HF_LABELS.length];
	JPanel textPanel = new JPanel(new GridLayout(HF_LABELS.length*2,1));
	for (int i = 0; i < HF_LABELS.length; i++) {
	    JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    useCheckBox[i] = new JCheckBox();
	    useCheckBox[i].setSelected(false);
	    useCheckBox[i].addItemListener(this);
	    title.add(useCheckBox[i]);
	    title.add(new JLabel(HF_LABELS[i]));
	    textPanel.add(title);
	    JPanel textFieldPanel = new JPanel(new FlowLayout());
	    textField[i] = new JTextField[3];
	    for (int j = 0; j < textField[i].length; j++) {
		textField[i][j] = new JTextField(10);
		textField[i][j].setEnabled(false);
		textFieldPanel.add(textField[i][j]);
	    }
	    textPanel.add(textFieldPanel);
	}
	getContentPane().add(textPanel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton cancel = new JButton("Cancel");
	cancel.setActionCommand("cancel");
	cancel.addActionListener(this);
	buttonPanel.add(cancel);
	JButton ok = new JButton("Ok");
	ok.setActionCommand("ok");
	ok.addActionListener(this);
	buttonPanel.add(ok);
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	pack();

	text = new TagString[useCheckBox.length][];
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("ok")) {
	    for (int i = 0; i < useCheckBox.length; i++) {
		if (useCheckBox[i].isSelected()) {
		    text[i] = new TagString[3];
		    for (int j = 0; j < textField[i].length; j++) {
			text[i][j] = new TagString(textField[i][j].getText());
		    }
		} else {
		    text[i] = null;
		}
	    }
	    setVisible(false);
	} else if (e.getActionCommand().equals("cancel")) {
	    for (int i = 0; i < useCheckBox.length; i++) {
		useCheckBox[i].setSelected(text[i] != null);
		for (int j = 0; j < textField[i].length; j++) {
		    if (text[i] != null) {
			textField[i][j].setText(text[i][j].toString());
			textField[i][j].setEnabled(true);
		    } else {
			textField[i][j].setText("");
			textField[i][j].setEnabled(false);
		    }
		}
	    }
	    setVisible(false);
	}
    }

    public void configure(MultiPageDocument md) {
	if (text[0] != null) {
	    md.setHeader(new Font("times", Font.PLAIN, 10),
			 text[0][0],
			 text[0][1],
			 text[0][2],
			 1);
	}

	if (text[1] != null) {
	    md.setFooter(new Font("times", Font.PLAIN, 10),
			 text[1][0],
			 text[1][1],
			 text[1][2],
			 1);
	}

    }

    public void itemStateChanged(ItemEvent e) {
	for (int i = 0; i < useCheckBox.length; i++) {
	    if (e.getSource() == useCheckBox[i]) {
		for (int j = 0; j < textField[i].length; j++) {
		    textField[i][j].setEnabled(e.getStateChange() == ItemEvent.SELECTED);
		}
	    }
	}
    }

    public static void main(String[] argv) {
	JDialog d = new HeadFootDialog();
	d.setVisible(true);
    }

}
