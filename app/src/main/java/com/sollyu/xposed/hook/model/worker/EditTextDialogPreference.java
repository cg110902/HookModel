package com.sollyu.xposed.hook.model.worker;

import android.content.Context;
import android.preference.DialogPreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wangsy on 15/2/6.
 */
public class EditTextDialogPreference extends DialogPreference
{
    //Layout Fields
    private final RelativeLayout layout = new RelativeLayout(this.getContext());
    private final EditText editText = new EditText(this.getContext());
    private final Button button = new Button(this.getContext());


    //Called when addPreferencesFromResource() is called. Initializes basic paramaters
    public EditTextDialogPreference(final Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setPersistent(true);
        button.setText("ok");
        button.setId(0x001);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    //Create the Dialog view
    @Override
    protected View onCreateDialogView()
    {
        layout.addView(editText);
        layout.addView(button);

        RelativeLayout.LayoutParams buttonParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout.LayoutParams editParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        editParams.addRule(RelativeLayout.LEFT_OF, button.getId());
        editParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, button.getId());
        editParams.addRule(RelativeLayout.ALIGN_BOTTOM, button.getId());

        return layout;
    }

    //Attach persisted values to Dialog
    @Override
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);
        editText.setText(getPersistedString(""), TextView.BufferType.NORMAL);
    }

    //persist values and disassemble views
    @Override
    protected void onDialogClosed(boolean positiveresult)
    {
        super.onDialogClosed(positiveresult);
        if (positiveresult && shouldPersist())
        {
            String value = editText.getText().toString();
            if (callChangeListener(value))
                persistString(value);
        }

        ((ViewGroup) editText.getParent()).removeView(editText);
        ((ViewGroup) button.getParent()).removeView(button);
        ((ViewGroup) layout.getParent()).removeView(layout);

        notifyChanged();
    }

    public void setText(String string)
    {
        editText.setText(string);
    }

    public void setButtonText(String string)
    {
        button.setText(string);
    }

    public void setButtonOnClickListener(View.OnClickListener clickListener)
    {
        button.setOnClickListener(clickListener);
    }
}
