// Copyright 2002, SLAC, Stanford University, U.S.A.
package org.freehep.graphicsio.test;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestLabels.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestLabels extends TestingPanel {

    public TestLabels(String[] args) throws Exception {

        super(args);
        setName("Labels");

        JPanel testPanel = this;

        JLabel label1 = new JLabel("TestLabel1");
        // label1.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(label1);

        JLabel label2 = new JLabel("TestLabel2");
        // label2.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(label2);
        label2.setBorder(BorderFactory.createEtchedBorder());

        JLabel label3 = new JLabel("TestLabel3");
        // label3.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(label3);
        label3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JComponent label4 = new SpecialLabel("TestLabel4", "Prefix");
        // label4.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(label4);

        JButton button1 = new JButton("TestButton1");
        // button1.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(button1);

        JButton button2 = new JButton("TestButton2");
        // button2.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(button2);
        button2.setBorder(BorderFactory.createEtchedBorder());

        JButton button3 = new JButton("TestButton3");
        // button3.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        testPanel.add(button3);
        button3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    public static void main(String[] args) throws Exception {
        new TestLabels(args).runTest();
    }

    class SpecialLabel extends JComponent {

        public SpecialLabel(final String text, final String prefix,
                final int align) {
            sourceText = text;
//            this.prefix = prefix;
            setLayout(new BorderLayout());
            label = new JLabel(text, align);
            label.setForeground(getForeground());
            add(label, BorderLayout.CENTER);
        }

        public SpecialLabel(final String text, final String prefix) {
            this(text, prefix, JLabel.CENTER);
        }

        void edit() {
            textField = new JTextField(sourceText);
            textField.setFont(label.getFont());
            textField.setBorder(null);
            textField.setHorizontalAlignment(label.getHorizontalAlignment());
            textField.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent a) {
                    finishTextEdit();
                }
            });
            textField.addFocusListener(new FocusAdapter() {
                public void focusLost(final FocusEvent fe) {
                    finishTextEdit();
                }
            });
            remove(label);
            add(textField, BorderLayout.CENTER);
            textField.requestFocus();
            revalidate();
            textField.getCaret().setVisible(true);
        }

        public void mouseEventNotify(final MouseEvent me) {
            if (me.getID() == MouseEvent.MOUSE_CLICKED && textField == null
                    && me.getClickCount() == 2) {
                edit();
            }
        }

        private void finishTextEdit() {
            if (textField != null) {
                JTextField text = textField;
                textField = null; // protect against recursive call

                setText(text.getText());
                remove(text);
                add(label, BorderLayout.CENTER);
                invalidate();
                getParent().validate();
                repaint();
            }
        }

        public Font getFont() {
            return label.getFont();
        }

        public void setFont(final Font f) {
            label.setFont(f);
        }

        public String getText() {
            return label.getText();
        }

        public void setText(final String text) {
            sourceText = text;
            label.setText(text);
        }

        private String sourceText;

        private JLabel label;

        private JTextField textField;

//        private String prefix;
    }
}
