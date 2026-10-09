package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>ConcreteMediator</b>: GoF's {@code FontDialogDirector}. It creates the widgets and
 * holds the whole behavior of the dialog in one method.
 * <p>
 * The widgets are general: the same {@link ListBox}, {@link EntryField} and {@link Button}
 * could be used in any other dialog with a different director.
 */
public final class FontDialogDirector extends DialogDirector {

    public final List<String> log = new ArrayList<>();
    public final ListBox fontList = new ListBox(this);
    public final EntryField fontName = new EntryField(this);
    public final Button ok = new Button(this);
    public final Button cancel = new Button(this);

    public FontDialogDirector() {
        cancel.setEnabled(true);
    }

    @Override
    public void widgetChanged(Widget widget) {
        if (widget == fontList) {
            fontName.setText(fontList.selection());
            ok.setEnabled(true);
        } else if (widget == fontName) {
            ok.setEnabled(!fontName.text().isEmpty());
        } else if (widget == ok) {
            log.add("font set to " + fontName.text());
        } else if (widget == cancel) {
            log.add("dialog closed");
        }
    }
}
