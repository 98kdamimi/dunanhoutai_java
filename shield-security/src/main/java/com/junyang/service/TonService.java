package com.junyang.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;

/**
 * @category ton链
 * @author Hlin
 *
 */
@RequestMapping("/ton")
public interface TonService {
	
	@GetMapping("/addWallet")
	ResponseBase addWallet() throws InvalidKeyException, NoSuchAlgorithmException;

}
