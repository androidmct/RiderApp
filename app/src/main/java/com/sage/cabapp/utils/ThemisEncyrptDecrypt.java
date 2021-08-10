package com.sage.cabapp.utils;

import android.util.Base64;

import com.cossacklabs.themis.InvalidArgumentException;
import com.cossacklabs.themis.NullArgumentException;
import com.cossacklabs.themis.SecureCell;
import com.cossacklabs.themis.SecureCellData;
import com.cossacklabs.themis.SecureCellException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class ThemisEncyrptDecrypt {

    public static String encyrptString(String forEncrypt) throws InvalidArgumentException, SecureCellException, NullArgumentException {

        Charset charset = StandardCharsets.UTF_8;
        String pass = "sageridermindcrew";
        byte[] passKey = pass.getBytes(charset);

        byte[] context = null;

        SecureCell sc = new SecureCell(passKey, SecureCell.MODE_SEAL);

        SecureCellData protectedData = sc.protect(passKey, context, forEncrypt.getBytes(charset));
        return Base64.encodeToString(protectedData.getProtectedData(), Base64.NO_WRAP);

    }

    public static String decryptString(String forDecrypt) throws InvalidArgumentException, SecureCellException, NullArgumentException {

        Charset charset = StandardCharsets.UTF_8;
        String pass = "sageridermindcrew";

        byte[] passKey = pass.getBytes(charset);

        byte[] context = null;
        SecureCell sc = new SecureCell(passKey, SecureCell.MODE_SEAL);


        byte[] decodedString = Base64.decode(forDecrypt, Base64.NO_WRAP);
        SecureCellData protectedDataAgain = new SecureCellData(decodedString, null);

        byte[] unprotected = sc.unprotect(passKey, context, protectedDataAgain);
        return new String(unprotected, charset);
    }
}
