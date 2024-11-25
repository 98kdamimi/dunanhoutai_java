package com.junyang.dao.version;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionIosEntity;

@Repository
public interface VersionIosDao extends BaseMapper<VersionIosEntity>{

	@Select("select *,ios_url as url from version_ios where version_id = #{versionId}")
	VersionIosEntity findVersionId(@Param("versionId") Integer versionId);

	@Update("update version_ios set release_state = #{releaseState}")
	void updateState(@Param("releaseState") Integer releaseState);

}
