package com.junyang.query;
import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.constants.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 分页条件
 * @author csz
 *
 */
@Data
@ApiModel(value = "分页查询条件",description = "分页查询条件")
public class PageQueryHelperEntity {
	
	@TableField(exist = false)
	@ApiModelProperty(name = "pageNumber",value = "分页页码",required = false,dataType = "Integer")
	private Integer pageNumber = Constants.PAGENUMBER;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "pageSize",value = "分页数量",required = false,dataType = "Integer")
	private Integer pageSize =Constants.PAGESIZE;

	
	
	

}
