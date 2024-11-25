package com.junyang.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionDigtalshieFirmwareEntity;

@Repository
public interface DigtalshieFirmwareDao extends BaseMapper<VersionDigtalshieFirmwareEntity>{

	@Select("select *,firmware_url as url from version_digtalshie_firmware where digtalshie_Id = #{digtalshieId}")
	List<VersionDigtalshieFirmwareEntity> findDigtalshieId(@Param("digtalshieId") Integer digtalshieId);

	@Update("update version_digtalshie_firmware set release_state = #{releaseState}")
	void updateState(@Param("releaseState") Integer releaseState);

}
