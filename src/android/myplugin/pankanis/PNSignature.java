package com.pankanis;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

public class PNSignature {
    
    public static final String KEYPROVIDER 						= 	"RSA";
    public static final String KEYALGO 							= 	"SHA256withRSA";
    
    public ArrayList generateKeys(String uniqueValue){
        
        KeyPairGenerator keyGen = null;
        SecureRandom random = null;
        try {
            keyGen = KeyPairGenerator.getInstance(KEYPROVIDER);
            random = new SecureRandom();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();
        ArrayList myKeys = new ArrayList();
        myKeys.add(priv);
        myKeys.add(pub);
        return myKeys;
    }
    //-------------------------------------------------------------------------
    public byte[] generateSignature(PrivateKey priv, String uniqueKey){
        
        
        Signature dsa;
        byte[] realSig = null;
        try {
            dsa = Signature.getInstance(KEYALGO);
            dsa.initSign(priv);
            if(uniqueKey == null) {
                FileInputStream fis = new FileInputStream("/Users/sachin.rajwade/Downloads/IsoDemo.java");
                BufferedInputStream bufin = new BufferedInputStream(fis);
                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    dsa.update(buffer, 0, len);
                };
                
                bufin.close();
            } else {
                dsa.update(uniqueKey.getBytes("UTF-8"));
            }
            /* Now that all the data to be signed has been read in,
             generate a signature for it */
            
            realSig = dsa.sign();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return realSig;
    }
    //-------------------------------------------------------------------------
    public boolean validateSignature(PublicKey pub, byte[] realSig, String uniqueKey){
        
        
        
        Signature dsa1;
        boolean isSuccess = false;
        try {
            dsa1 = Signature.getInstance(KEYALGO);
            
            dsa1.initVerify(pub);
            if(uniqueKey == null) {
                FileInputStream fis1 = new FileInputStream("/Users/sachin.rajwade/Downloads/IsoDemo.java");
                BufferedInputStream bufin1 = new BufferedInputStream(fis1);
                byte[] buffer1 = new byte[1024];
                int len1;
                while (bufin1.available() != 0) {
                    len1 = bufin1.read(buffer1);
                    dsa1.update(buffer1, 0, len1);
                };
                
                bufin1.close();
            } else {
                dsa1.update(uniqueKey.getBytes("UTF-8"));
            }
            isSuccess = dsa1.verify(realSig);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isSuccess;
    }
    
    
    public boolean validateSignature(String pubString, byte[] realSig, String uniqueKey){
        
        PublicKey pub= null;
        try {
            pub = loadPublicKey(pubString);
            
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Signature dsa1;
        boolean isSuccess = false;
        try {
            dsa1 = Signature.getInstance(KEYALGO);
            
            dsa1.initVerify(pub);
            if(uniqueKey == null) {
                FileInputStream fis1 = new FileInputStream("/Users/sachin.rajwade/Downloads/IsoDemo.java");
                BufferedInputStream bufin1 = new BufferedInputStream(fis1);
                byte[] buffer1 = new byte[1024];
                int len1;
                while (bufin1.available() != 0) {
                    len1 = bufin1.read(buffer1);
                    dsa1.update(buffer1, 0, len1);
                };
                
                bufin1.close();
            } else {
                dsa1.update(uniqueKey.getBytes("UTF-8"));
            }
            isSuccess = dsa1.verify(realSig);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isSuccess;
    }
    
    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.decode(key64,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance(KEYPROVIDER);
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }
    
    
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.decode(stored,Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance(KEYPROVIDER);
        return fact.generatePublic(spec);
    }
    
    public static String savePrivateKey(PrivateKey priv) {
        String key64 = "";
        try {
            KeyFactory fact = KeyFactory.getInstance(KEYPROVIDER);
            PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
            byte[] packed = spec.getEncoded();
            key64 =  Base64.encodeToString(packed,Base64.DEFAULT);
            
            Arrays.fill(packed, (byte) 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return key64;
    }
    
    
    public static String savePublicKey(PublicKey publ)  {
        X509EncodedKeySpec spec=null;
        try {
            KeyFactory fact = KeyFactory.getInstance(KEYPROVIDER);
            spec = fact.getKeySpec(publ,
                                   X509EncodedKeySpec.class);
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return  Base64.encodeToString(spec.getEncoded(),Base64.DEFAULT);
    }
    
    
}
