package dk.frankbille.mysqldumper.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public abstract class PersistedDocumentListener implements DocumentListener {

    @Override
    public void insertUpdate(final DocumentEvent e) {
        saveChanges(e);
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        saveChanges(e);
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
    }

    private void saveChanges(final DocumentEvent event) {
        final Document document = event.getDocument();
        try {
            final String text = document.getText(0, document.getLength());
            doSaveChanges(text);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void doSaveChanges(String text);
}
