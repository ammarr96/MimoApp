package com.amar.mimoapp.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class FillInBlanksEditText extends AppCompatEditText
        implements View.OnFocusChangeListener, TextWatcher {
    private int mLastSelStart;
    private int mLastSelEnd;
    private BlanksSpan mSpans[];
    private Editable mUndoChange;
    private BlanksSpan mWatcherSpan;

    public FillInBlanksEditText(Context context) {
        super(context);
        init();
    }

    public FillInBlanksEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FillInBlanksEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSpans = setSpans();
        setOnFocusChangeListener(this);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        mSpans = null;
        super.onRestoreInstanceState(state);
        Editable e = getEditableText();
        mSpans = e.getSpans(0, e.length(), BlanksSpan.class);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            addTextChangedListener(this);
            if (findInSpan(getSelectionStart(), getSelectionEnd()) != null) {
                mLastSelStart = getSelectionStart();
                mLastSelEnd = getSelectionEnd();
            } else if (findInSpan(mLastSelStart, mLastSelEnd) == null) {
                setSelection(getEditableText().getSpanStart(mSpans[0]));
            }
        } else {
            removeTextChangedListener(this);
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (!isFocused() || mSpans == null ||
                (getSelectionStart() == mLastSelStart && getSelectionEnd() == mLastSelEnd)) {
            return;
        }

        // The selection must be completely within a Blankspan.
        final BlanksSpan span = findInSpan(selStart, selEnd);
        if (span == null) {
            // Current selection is not within a Blankspan. Restore selection to prior location.
            moveCursor(mLastSelStart);
        } else if (selStart > getEditableText().getSpanStart(span) + span.getDataLength()) {
            // Acceptable location for selection (within a Blankspan).
            // Make sure that the cursor is at the end of the entered data.  mLastSelStart = getEditableText().getSpanStart(span) + span.getDataLength();
            mLastSelEnd = mLastSelStart;
            moveCursor(mLastSelStart);

        } else {
            // Just capture the placement.
            mLastSelStart = selStart;
            mLastSelEnd = selEnd;
        }
        super.onSelectionChanged(mLastSelStart, mLastSelEnd);
    }

    // Safely move the cursor without directly invoking setSelection from onSelectionChange.
    private void moveCursor(final int selStart) {
        post(new Runnable() {
            @Override
            public void run() {
                setSelection(selStart);
            }
        });
        // Stop cursor form jumping on move.
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    @Nullable
    private BlanksSpan findInSpan(int selStart, int selEnd) {
        for (BlanksSpan span : mSpans) {
            if (selStart >= getEditableText().getSpanStart(span) &&
                    selEnd <= getEditableText().getSpanEnd(span)) {
                return span;
            }
        }
        return null;
    }

    // Set up a Blankspan to cover each occurrence of BLANKS_TOKEN.
    private BlanksSpan[] setSpans() {
        Editable e = getEditableText();
        String s = e.toString();
        int offset = 0;
        int blanksOffset;

        while ((blanksOffset = s.substring(offset).indexOf(BLANKS_TOKEN)) != -1) {
            offset += blanksOffset;
            e.setSpan(new BlanksSpan(Typeface.BOLD), offset, offset + BLANKS_TOKEN.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            offset += BLANKS_TOKEN.length();
        }
        return e.getSpans(0, e.length(), BlanksSpan.class);
    }

    // Check change to make sure that it is acceptable to us.
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mWatcherSpan = findInSpan(start, start + count);
        if (mWatcherSpan == null) {
            // Change outside of a Blankspan. Just put things back the way they were.
            // Do this in afterTextChaanged.  mUndoChange = Editable.Factory.getInstance().newEditable(s);
        } else {
            // Change is OK. Track data length.
            mWatcherSpan.adjustDataLength(count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing...
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mUndoChange == null) {
            // The change is legal. Modify the contents of the span to the format we want.
            CharSequence newContents = mWatcherSpan.getFormattedContent(s);
            if (newContents != null) {
                removeTextChangedListener(this);
                int selection = getSelectionStart();
                s.replace(s.getSpanStart(mWatcherSpan), s.getSpanEnd(mWatcherSpan), newContents);
                setSelection(selection);
                addTextChangedListener(this);
            }
        } else {
            // Illegal change - put things back the way they were.
            removeTextChangedListener(this);
            setText(mUndoChange);
            mUndoChange = null;
            addTextChangedListener(this);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class BlanksSpan extends StyleSpan {
        private int mDataLength;

        public BlanksSpan(int style) {
            super(style);
        }

        @SuppressWarnings("unused")
        public BlanksSpan(@NonNull Parcel src) {
            super(src);
        }

        public void adjustDataLength(int count, int after) {
            mDataLength += after - count;
        }

        @Nullable
        public CharSequence getFormattedContent(Editable e) {
            if (mDataLength == 0) {
                return BLANKS_TOKEN;
            }
            int spanStart = e.getSpanStart(this);
            return (e.getSpanEnd(this) - spanStart > mDataLength)
                    ? e.subSequence(spanStart, spanStart + mDataLength)
                    : null;
        }

        public int getDataLength() {
            return mDataLength;
        }

    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private static final String TAG = "FillInBlanksEditText";
    private static final String BLANKS_TOKEN = "_____";

}