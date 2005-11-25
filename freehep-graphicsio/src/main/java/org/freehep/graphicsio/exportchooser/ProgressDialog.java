// Copyright 2001 freehep
package org.freehep.graphicsio.exportchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 *  A dialog showing a progress bar and a label. Pressing the cancel button
 *  will call the cancel method on an <tt>ExportGraphicsFileTypeAdaptor.CancelThread</tt>.
 *  Exceptions occuring in another thread can be reported to this class and at the end of the
 *  thread queried.
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ProgressDialog.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class ProgressDialog extends JDialog {

    private JProgressBar progressBar;
    private JLabel label;
    private int progress;
    private IOException exception;
    private AbstractExportFileType.CancelThread thread;
    private Component parent;

    public ProgressDialog(Component parent, int steps, String string) {
    	setTitle("Exporting file...");
    	setModal(true);
    	this.parent = parent;
    	this.exception = null;
    	this.progress = 0;
    	getContentPane().setLayout(new BorderLayout());
    	JPanel center = new JPanel(new GridLayout(2,1));
    	progressBar = new JProgressBar(0, steps);
    	center.add(progressBar);
    	label = new JLabel(string);
    	center.add(label);
    	getContentPane().add(center, BorderLayout.CENTER);
    	JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JButton cancel = new JButton("Cancel");
    	cancel.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) { cancel(); }
	    });
    	buttons.add(cancel);
    	getContentPane().add(buttons, BorderLayout.SOUTH);
    	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    	pack();

    	if (parent != null) {
    	    Point pLoc = parent.getLocation();
    	    setLocation(pLoc.x + parent.getWidth()/2 - getWidth()/2,
    			pLoc.y + parent.getHeight()/2 - getHeight()/2);
    	}
    }

    public void step(String string) {
    	progressBar.setValue(++progress);
    	label.setText(string);
    	repaint();
    }

    public void exceptionOccured(IOException e) {
    	this.exception = e;
    }

    public IOException getException() {
    	return exception;
    }

    public void interruptOnCancel(AbstractExportFileType.CancelThread thread) {
    	this.thread = thread;
    }

    public void cancel() {
    	if (thread != null) thread.cancel();
    	try {
    	    thread.join();
    	} catch (InterruptedException e) {
    	    e.printStackTrace();
    	}
    	dispose();
    }
}
