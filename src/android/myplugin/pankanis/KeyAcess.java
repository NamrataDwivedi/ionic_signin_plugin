package com.pankanis;

import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * This class echoes a string called from JavaScript.
 */
public class KeyAcess extends CordovaPlugin  {
    
    Key public_key,private_key;
    
    //  public static HashMap staticKeyMap;
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("generateKey")) {
            String message = args.getString(0);
            this.generateKey(message, callbackContext);
            return true;
        } if (action.equals("generateSig")) {
            String key = args.getString(0); // PrivateKey
            String value=args.getString(1);
            try {
                this.generateSig(key,value, callbackContext);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }else  if (action.equals("validateSig")) {
            String key = args.getString(0);
            String kval = args.getString(1);
            String uniqueKey=args.getString(2);
            try {
                this.validateSig(key,kval,uniqueKey, callbackContext);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
    private void generateSig(
                             String uiprivateKey
                             ,   String uiactualData // namrata
                             ,   CallbackContext callbackContext
                             ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        
        PNSignature pnsig=new PNSignature();
        PrivateKey priv=null;
        try {
            
            priv=pnsig.loadPrivateKey(uiprivateKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        byte[] realSig = pnsig.generateSignature(priv,uiactualData);
        
        String newToken = Base64.encodeToString(realSig,Base64.DEFAULT);
        
        //Base
        callbackContext.success(newToken);
        
    }
    
    private void validateSig(
                             String actualSig
                             ,  String publicKey
                             ,  String uniqKey
                             ,   CallbackContext callbackContext
                             ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        PNSignature pnsig=new PNSignature();
        String returnVal="1";
        
        byte[] newToken=Base64.decode(actualSig,Base64.DEFAULT);
        //boolean isSuccess = pnsig.validateSignature((PublicKey) staticKeyMap.get("PUBLIC"),newToken,"Namrata");
        
        boolean isSuccess = pnsig.validateSignature( publicKey,newToken,uniqKey);
        
        if(isSuccess){
            returnVal="0";
        }
        callbackContext.success(returnVal);
        
    }
    
    private byte[] base64Decode(String key) {
        return Base64.decode(key, Base64.DEFAULT);
    }
    
    
    private void generateKey(final String orgKey, final CallbackContext callbackContext) {
        final JSONObject jsonObject=new JSONObject();
        
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PNSignature pnsig = new PNSignature();
                    ArrayList pList = pnsig.generateKeys("");
                    PrivateKey priv = (PrivateKey) pList.get(0);
                    PublicKey pub = (PublicKey) pList.get(1);
                    
                    
                    byte[] realSig = pnsig.generateSignature(priv,orgKey);
                    
                    /*jsonObject.put("privatekey",priv.getEncoded().toString());
                     jsonObject.put("publickey",pub.getEncoded().toString());*/
                    
                    
                    jsonObject.put("privatekey",pnsig.savePrivateKey(priv));
                    jsonObject.put("publickey",pnsig.savePublicKey(pub));
                    
                    
                    String json=jsonObject.toString();
                    
                    callbackContext.success(json);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }
    
    private String base64Encode(byte[] packed) {
        return Base64.encodeToString(packed, Base64.DEFAULT);
    }
    
    
}
