package GUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import Files.DatConLog;

@SuppressWarnings("serial")
public class LoggingPanel extends JScrollPane {

    private static JTextPane logArea = null;

    //    private DatCon datCon = null;

    public static LoggingPanel instance;

    public LoggingPanel() {
        //        this.datCon = datCon;
        instance = this;
        setPreferredSize(new Dimension(400, 100));
        logArea = new JTextPane();
        setViewportView(logArea);
    }

    private static void appendTo(final String msg, final Color c) {
        if (SwingUtilities.isEventDispatchThread()) {
            appendInEDT(msg, c);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    appendInEDT(msg, c);
                }
            });
        }
    }

    private static void appendInEDT(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily,
                "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment,
                StyleConstants.ALIGN_JUSTIFIED);

        int len = logArea.getDocument().getLength();
        logArea.setCaretPosition(len);
        logArea.setCharacterAttributes(aset, false);
        logArea.replaceSelection(msg);
    }

    public void clear() {
        logArea.setText("");
    }

    public void Info(String msg) {
        if (msg.length() > 0) {
            appendTo(msg + System.lineSeparator(), Color.BLACK);
            DatConLog.Log(msg);
        }
    }

    public void Error(String error) {
        appendTo(error + System.lineSeparator(), Color.RED);
        DatConLog.Error(error);
    }

    public void Exception(Exception e, String msg) {
    	appendTo((msg != null ? msg : e.getMessage()) + System.lineSeparator(), Color.RED);
        DatConLog.Exception(e, msg);
    }
}
