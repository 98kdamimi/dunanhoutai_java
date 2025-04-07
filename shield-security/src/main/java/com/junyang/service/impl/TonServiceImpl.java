package com.junyang.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.ton.java.address.Address;
import org.ton.java.mnemonic.Mnemonic;
import org.ton.java.mnemonic.Pair;
import org.ton.java.smartcontract.types.WalletV4R2Config;
import org.ton.java.smartcontract.wallet.v4.WalletV4R2;

import com.iwebpp.crypto.TweetNacl.Signature.KeyPair;
import com.iwebpp.crypto.TweetNaclFast;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.service.TonService;

@RestController
@Transactional
@CrossOrigin
public class TonServiceImpl extends BaseApiService implements TonService{

	@Override
	public ResponseBase addWallet() throws InvalidKeyException, NoSuchAlgorithmException {
		 // 1. 生成助记词（24 个单词）
        List<String> mnemonic = Mnemonic.generate(24);
        System.out.println("助记词: " + String.join(" ", mnemonic));

        // 2. 通过助记词获取密钥对（公钥 + 私钥）
        Pair keyPair = Mnemonic.toKeyPair(mnemonic);
        System.out.println("私钥: " + Arrays.toString(keyPair.getSecretKey()));
        System.out.println("公钥: " + Arrays.toString(keyPair.getPublicKey()));
		return null;

        // 3. 生成钱包地址（V4 版本）
//        Wallet wallet = new WalletV4Contract(keyPair);
//        Address walletAddress = wallet.getAddress();
//
//        System.out.println("钱包地址: " + walletAddress.toString());

       
	}
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
//		 List<String> mnemonic = Mnemonic.generate(24);
		 List<String> mnemonic = Arrays.asList(
	                "artwork", "people", "where", "dolphin", "assume", "develop", "polar",
	                "link", "company", "boil", "over", "shed", "clump", "fall", "seed", "flag",
	                "private", "opinion", "budget", "follow", "install", "skill", "agent", "garbage"
	            );

	            // Step 2: Generate key pair from mnemonic
	            Pair keyPair = Mnemonic.toKeyPair(mnemonic);
	            System.out.println("Private Key: " + Arrays.toString(keyPair.getSecretKey())); 
	            System.out.println("Public Key: " + Arrays.toString(keyPair.getPublicKey()));

	            // Step 3: Initialize TonLibService
//	            TonLibService tonLibService = new TonLibService(true, keyPair.getSecretKey());

	            // Step 4: Initialize WalletV4R2
	            WalletV4R2 wallet = WalletV4R2.builder()
//	                    .tonlib(tonLibService.getTonlib())
	                    .keyPair(TweetNaclFast.Signature.keyPair_fromSeed(keyPair.getSecretKey()))
	                    .build();

	            // Step 5: Print wallet address
	            System.out.println("Wallet Address: " + wallet.getAddress());
	     
	}

}
