package com.junyang.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionTypeEntity;

@Repository
public interface VersionTypeDao extends BaseMapper<VersionTypeEntity>{

	@Select("select * from version_type where version_id = #{versionId}")
	List<VersionTypeEntity> findVersionId(@Param("versionId") Integer versionId);

}
