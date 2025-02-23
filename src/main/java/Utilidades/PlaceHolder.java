package Utilidades;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceHolder extends JTextField
{
    private final String placeholder;
    private boolean showingPlaceholder;

    public PlaceHolder(String placeholder)
    {
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        setPlaceholder();

        addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (showingPlaceholder)
                {
                    setText("");
                    setForeground(Color.BLACK);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if (getText().isEmpty())
                {
                    setPlaceholder();
                }
            }
        });

        getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (showingPlaceholder)
                {
                    SwingUtilities.invokeLater(() ->
                    {
                        setText("");
                        setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                if (getText().isEmpty() && !showingPlaceholder)
                {
                    setPlaceholderWithDelay();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                // Not needed for plain text components
            }
        });
    }

    private void setPlaceholder()
    {
        setText(placeholder);
        setForeground(Color.GRAY);
        showingPlaceholder = true;
        SwingUtilities.invokeLater(() -> setCaretPosition(0));
    }

    private void setPlaceholderWithDelay()
    {
        Timer timer = new Timer(100, e ->
        {
            if (getText().isEmpty())
            {
                setPlaceholder();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public String getText()
    {
        return showingPlaceholder ? "" : super.getText();
    }
}
