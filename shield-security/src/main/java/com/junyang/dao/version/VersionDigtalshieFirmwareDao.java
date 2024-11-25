package com.junyang.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionDigtalshieFirmwareEntity;
import com.junyang.entity.release.VersionEntity;

@Repository
public interface VersionDigtalshieFirmwareDao extends BaseMapper<VersionDigtalshieFirmwareEntity>{

	@Select("<script>"
			+ "select *,firmware_url as url from version_digtalshie_firmware where 1=1 "
			+ "<if test = 'releaseState != null'> and release_state = #{releaseState}</if>"
			+ "order by set_time desc "
			+ "</script>")
	List<VersionDigtalshieFirmwareEntity> findList(VersionEntity entity);

	@Update("update version_digtalshie_firmware set release_state = #{releaseState}")
	void updateStateAll(@Param("releaseState") Integer releaseState);

}
