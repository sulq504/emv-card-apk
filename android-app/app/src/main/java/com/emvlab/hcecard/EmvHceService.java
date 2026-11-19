package com.emvlab.hcecard;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import emvcardsimulator.api.ApduScript;
import emvcardsimulator.api.EmvCard;

public class EmvHceService extends HostApduService {
    private static final String TAG = "EmvHceCard";
    private static EmvCard card = null;

    private static void ensureCard() throws Exception {
        synchronized (EmvHceService.class) {
            if (card == null) {
                card = new EmvCard(EmvCard.PROTOCOL_CONTACTLESS);
                card.personalize(ApduScript.bundled("card_setup_ppse_apdus.yaml"));
                card.personalize(ApduScript.bundled("card_setup_app_apdus.yaml"));
                Log.i(TAG, "EMV card personalized, ready");
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ensureCard();
        } catch (Throwable t) {
            Log.e(TAG, "Card personalization failed", t);
        }
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        try {
            ensureCard();
            byte[] response = card.transmit(commandApdu);
            Log.i(TAG, "C-APDU: " + toHex(commandApdu) + "  R-APDU: " + toHex(response));
            return response;
        } catch (Throwable t) {
            Log.e(TAG, "APDU processing failed", t);
            return new byte[] { (byte) 0x6F, 0x00 };
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "Field deactivated, reason=" + reason);
    }

    private static String toHex(byte[] b) {
        if (b == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (byte x : b) {
            sb.append(String.format("%02X", x));
        }
        return sb.toString();
    }
}
