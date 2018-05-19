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
        kv.setPreviewEnabled(false);
        kv.setOnKeyboardActionListener(this);
        kv.invalidateAllKeys();

        // keyboard = new Keyboard(this, R.xml.keyarrange);
        // kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey (int primaryCode, int[] ints) {
        playClick(primaryCode);
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
        InitialPrimaryCode = primaryCode;
    }

    @Override
    public void onRelease (int primaryCode) {
        InputConnection ic = getCurrentInputConnection();
        char code;
        switch (InitialPrimaryCode)
        {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                return;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                return;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return;

            case 97: // a, b, c
                if(primaryCode == 101){ // swipe up to e
                    code = (char)(InitialPrimaryCode +1);
                }
                else if(InitialPrimaryCode < primaryCode) { // swipe up further
                    code = (char) (InitialPrimaryCode +2);
                }
                else {
                    code = (char)InitialPrimaryCode;
                }
                break;
            case 105: //i, j
                if(primaryCode < InitialPrimaryCode){ // swipe down...
                    code = (char)(InitialPrimaryCode +1);
                }
                else{
                    code = (char)InitialPrimaryCode;
                }
                break;
            case 104: // h, only downward swipe is.
            case 108: // l
            case 110: // n
                if(primaryCode < InitialPrimaryCode){ //swipe down!
                    code = (char)(InitialPrimaryCode -1);
                }
                else{
                    code = (char)InitialPrimaryCode;
                }
                break;
            case 114: // r, for temporary solution below.
                if(primaryCode != InitialPrimaryCode){ //swipe down!
                    code = (char)(InitialPrimaryCode -1);
                }
                else{
                    code = (char)InitialPrimaryCode;
                }
                break;
            case 111: // o, only upward swipe is.
                if(InitialPrimaryCode < primaryCode) { // swipe up
                    code = (char)(InitialPrimaryCode +1);
                }
                else{
                    code = (char)InitialPrimaryCode;
                }
                break;
            case 121: // y.. it's with only upward swipe, but no keys above. so this is a temporary solution, making r key wider.
                if(primaryCode == 114){ // swipe up to r
                    code = (char)(InitialPrimaryCode +1);
                }
                else {
                    code = (char)InitialPrimaryCode;
                }
                break;
            default:
                if(InitialPrimaryCode < primaryCode){ // swipe up!
                    code = (char)(InitialPrimaryCode +1);
                }
                else if(primaryCode < InitialPrimaryCode){ // swipe down!
                    code = (char)(InitialPrimaryCode -1);
                }
                else{
                    code = (char)InitialPrimaryCode;
                }
        }

        if(Character.isLetter(code) && isCaps){
            code = Character.toUpperCase(code);
        }
        ic.commitText(String.valueOf(code),1);

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
