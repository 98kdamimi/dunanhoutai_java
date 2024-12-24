package com.junyang.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.response.DicEntity;
import com.junyang.enums.LanguageEnums;
import com.junyang.service.DicService;

@RestController
@Transactional
@CrossOrigin
public class DicServiceImpl extends BaseApiService implements DicService{

	@Override
	public ResponseBase getLanguage() {
		List<DicEntity> list = LanguageEnums.getList();
		return setResultSuccess(list);
	}

}
