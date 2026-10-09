package dev.kaldiroglu.dp.behavioral.mediator.gof.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * GoF's motivation, before the pattern: a font dialog whose widgets call each other.
 * <p>
 * The list box knows the entry field, the entry field knows the OK button, the OK button
 * knows the dialog. Each widget class is written for this dialog and cannot be used in
 * another one, and the dialog's behavior is spread over all of them.
 */
public final class FontDialog {

    public final List<String> log = new ArrayList<>();
    public final ListBox fontList;
    public final EntryField fontName;
    public final Button ok;

    public FontDialog() {
        ok = new Button(this);
        fontName = new EntryField(ok);
        fontList = new ListBox(fontName);
    }

    void apply(String font) {
        log.add("font set to " + font);
    }

    public static final class ListBox {
        private final EntryField fontName;

        ListBox(EntryField fontName) {
            this.fontName = fontName;
        }

        public void select(String font) {
            fontName.setText(font);          // the list box updates the entry field itself
        }
    }

    public static final class EntryField {
        private final Button ok;
        private String text = "";

        EntryField(Button ok) {
            this.ok = ok;
        }

        public void setText(String text) {
            this.text = text;
            ok.setEnabled(!text.isEmpty());  // the entry field enables the button itself
        }

        public String text() {
            return text;
        }
    }

    public static final class Button {
        private final FontDialog dialog;
        private boolean enabled;

        Button(FontDialog dialog) {
            this.dialog = dialog;
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean enabled() {
            return enabled;
        }

        public void click(String font) {
            if (enabled) {
                dialog.apply(font);
            }
        }
    }
}
