package com.example.dongju.keyman;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class keymankeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private int y1, y2;
    private int InitialPrimaryCode;

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean isCaps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        // kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, parent, false);
        keyboard = new Keyboard(this, R.xml.keyarrange);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.invalidateAllKeys();

        // keyboard = new Keyboard(this, R.xml.keyarrange);
        // kv.setOnKeyboardActionListener(this);
        return kv;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //x1 = (int) (event.getX());
                y1 = (int) (event.getY());
                break;

            case MotionEvent.ACTION_UP:
                //x2 = (int) (event.getX());
                y2 = (int) (event.getY());
                break;
        }
        return true;
    }

    @Override
    public void onKey (int primaryCode, int[] ints) {

    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode)
        {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onPress (int primaryCode) {
        playClick(primaryCode);
        InitialPrimaryCode = primaryCode;
    }

    @Override
    public void onRelease (int primaryCode) {
        InputConnection ic = getCurrentInputConnection();
        switch (InitialPrimaryCode)
        {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)InitialPrimaryCode;
                if(Character.isLetter(code) && isCaps){
                    code = Character.toUpperCase(code);
                }
                //if(y2 > y1+20){
                    ic.commitText(String.valueOf(code+1),1);
                //}
                //else{
                    //ic.commitText(String.valueOf(code),1);
                //}
        }
    }

    @Override
    public void onText (CharSequence text) {

    }

    @Override
    public void swipeLeft () {

    }

    @Override
    public void swipeRight () {

    }

    @Override
    public void swipeUp () {

    }

    @Override
    public void swipeDown () {

    }

}
