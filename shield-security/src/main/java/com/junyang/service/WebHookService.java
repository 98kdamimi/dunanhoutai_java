package com.junyang.service;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.HttpResponse;
import com.junyang.base.ResponseBase;
import com.junyang.entity.monitorEvent.AddAddressesQueryEntity;
import com.junyang.entity.monitorEvent.AddressesJpushEntity;
import com.junyang.entity.monitorEvent.BlockchainTransaction;

import io.swagger.annotations.ApiOperation;

/**
 * @category webhook交易监听
 * @author Hlin
 *
 */
@RequestMapping("/webhook")
public interface WebHookService {
	
//	@GetMapping("/findTaskList")
//	@ApiOperation(value = "获取任务列表",notes = "获取任务列表",response = ResponseBase.class)
//	HttpResponse findTaskList(String limit,String page);
//	
//	@PostMapping("/addTaskAddresses")
//	@ApiOperation(value = "添加任务/合约地址",notes = "添加任务/合约地址",response = ResponseBase.class)
//	ResponseBase addTaskAddresses(AddAddressesQueryEntity entity);
	
	@PostMapping("/addTaskAddresses")
	@ApiOperation(value = "添加任务/合约地址",notes = "添加任务/合约地址",response = ResponseBase.class)
	ResponseBase addTaskAddresses(AddAddressesQueryEntity entity);
	
	@PostMapping("/Callbacks")
	@ApiOperation(value = "任务回调",notes = "任务回调",response = ResponseBase.class)
	HttpResponse Callbacks(BlockchainTransaction payload);
	
	@PostMapping("/bindingAddresses")
	@ApiOperation(value = "账户地址绑定jpush",notes = "账户地址绑定jpush",response = ResponseBase.class)
	ResponseBase bindingAddresses(AddressesJpushEntity entity);
	
	

}
