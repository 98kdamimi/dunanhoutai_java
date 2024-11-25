package com.junyang.dao.version;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionAndroidGoogleEntity;

@Repository
public interface AndroidGoogleDao extends BaseMapper<VersionAndroidGoogleEntity>{

	@Select("select *,google_url as url from version_android_google where android_version_id = #{androidVersionId}")
	VersionAndroidGoogleEntity findAndroid(@Param("androidVersionId") Integer androidVersionId);

	@Update("update version_android_google set release_state = #{releaseState}")
	void updateState(@Param("releaseState") Integer releaseState);

}
