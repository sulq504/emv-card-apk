package com.emvlab.hcecard;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = (TextView) findViewById(R.id.status);
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            status.setText(R.string.no_nfc);
        } else if (!adapter.isEnabled()) {
            status.setText(R.string.nfc_off);
        } else {
            status.setText(R.string.ready);
        }
    }
}
