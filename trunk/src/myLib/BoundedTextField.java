package myLib;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Un classe pour définir un champ texte où l'on controle le maximum de caractères.
 * @author Rémi TANIWAKI
 *
 */
@SuppressWarnings("serial")
public class BoundedTextField extends JTextField implements
    BoundedPlainDocument.InsertErrorListener {
  public BoundedTextField() {
    this(null, 0, 0);
  }

  public BoundedTextField(String text, int columns, int maxLength) {
    super(null, text, columns);

    if (text != null && maxLength == 0) {
      maxLength = text.length();
    }
    BoundedPlainDocument plainDoc = (BoundedPlainDocument) getDocument();
    plainDoc.setMaxLength(maxLength);

    plainDoc.addInsertErrorListener(this);
  }

  public BoundedTextField(int columns, int maxLength) {
    this(null, columns, maxLength);
  }

  public BoundedTextField(String text, int maxLength) {
    this(text, 0, maxLength);
  }

  public void setMaxLength(int maxLength) {
    ((BoundedPlainDocument) getDocument()).setMaxLength(maxLength);
  }

  public int getMaxLength() {
    return ((BoundedPlainDocument) getDocument()).getMaxLength();
  }

  // Override to handle insertion error
  public void insertFailed(BoundedPlainDocument doc, int offset, String str,
      AttributeSet a) {
    // By default, just beep
    Toolkit.getDefaultToolkit().beep();
  }

  // Method to create default model
  protected Document createDefaultModel() {
    return new BoundedPlainDocument();
  }
}

@SuppressWarnings("serial")
class BoundedPlainDocument extends PlainDocument {
  public BoundedPlainDocument() {
    // Default constructor - must use setMaxLength later
    this.maxLength = 0;
  }

  public BoundedPlainDocument(int maxLength) {
    this.maxLength = maxLength;
  }

  public BoundedPlainDocument(AbstractDocument.Content content, int maxLength) {
    super(content);
    if (content.length() > maxLength) {
      throw new IllegalArgumentException(
          "Initial content larger than maximum size");
    }
    this.maxLength = maxLength;
  }

  public void setMaxLength(int maxLength) {
    if (getLength() > maxLength) {
      throw new IllegalArgumentException(
          "Current content larger than new maximum size");
    }

    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return maxLength;
  }

  public void insertString(int offset, String str, AttributeSet a)
      throws BadLocationException {
    if (str == null) {
      return;
    }

    // Note: be careful here - the content always has a
    // trailing newline, which should not be counted!
    int capacity = maxLength + 1 - getContent().length();
    if (capacity >= str.length()) {
      // It all fits
      super.insertString(offset, str, a);
    } else {
      // It doesn't all fit. Add as much as we can.
      if (capacity > 0) {
        super.insertString(offset, str.substring(0, capacity), a);
      }

      // Finally, signal an error.
      if (errorListener != null) {
        errorListener.insertFailed(this, offset, str, a);
      }
    }
  }

  public void addInsertErrorListener(InsertErrorListener l) {
    if (errorListener == null) {
      errorListener = l;
      return;
    }
    throw new IllegalArgumentException(
        "InsertErrorListener already registered");
  }

  public void removeInsertErrorListener(InsertErrorListener l) {
    if (errorListener == l) {
      errorListener = null;
    }
  }

  public interface InsertErrorListener {
    public abstract void insertFailed(BoundedPlainDocument doc, int offset,
        String str, AttributeSet a);
  }

  protected InsertErrorListener errorListener; // Unicast listener

  protected int maxLength;
}
