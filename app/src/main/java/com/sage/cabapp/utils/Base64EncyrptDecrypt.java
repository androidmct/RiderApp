package com.sage.cabapp.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by Maroof Ahmed Siddique on 26-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class Base64EncyrptDecrypt {

    public static String encyrpt(String text) {
        byte[] encrpt = text.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(encrpt, Base64.DEFAULT).replace("\n","");
    }

    public static String decrypt(String text) {
        byte[] decrypt = Base64.decode(text, Base64.DEFAULT);
        return new String(decrypt, StandardCharsets.UTF_8).replace("\n","");

    }
}
