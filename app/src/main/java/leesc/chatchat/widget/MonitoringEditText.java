package leesc.chatchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * An EditText, which notifies when something was cut/copied/pasted inside it.
 * @author Lukas Knuth
 * @version 1.0
 */
public class MonitoringEditText extends EditText {

    private final Context context;
    private OnPasteListener mListener;
    public interface OnPasteListener {
        public abstract void onTextPaste(boolean flag);
    }

    /*
        Just the constructors to create a new EditText...
     */
    public MonitoringEditText(Context context) {
        super(context);
        this.context = context;
    }

    public MonitoringEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MonitoringEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    public void setOnPasteListener(OnPasteListener listener) {
        this.mListener = listener;
    }

    /**
     * <p>This is where the "magic" happens.</p>
     * <p>The menu used to cut/copy/paste is a normal ContextMenu, which allows us to
     *  overwrite the consuming method and react on the different events.</p>
     * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3_r1/android/widget/TextView.java#TextView.onTextContextMenuItem%28int%29">Original Implementation</a>
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id){
            case android.R.id.paste:
                onTextPaste();
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    /**
     * Text was pasted into the EditText.
     */
    public void onTextPaste(){
        if (mListener != null)
        mListener.onTextPaste(true);
    }
}