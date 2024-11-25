package com.junyang.dao.version;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionAndroidEntity;

@Repository
public interface VersionAndroidDao extends BaseMapper<VersionAndroidEntity>{

	@Select("select *,android_googlePlay as googlePlay,android_url as url "
			+ "from version_android where version_id = #{versionId}")
	VersionAndroidEntity findVersionId(@Param("versionId") Integer versionId);

	@Update("update version_android set release_state = #{releaseState}")
	void updateState(@Param("releaseState") Integer releaseState);

}
