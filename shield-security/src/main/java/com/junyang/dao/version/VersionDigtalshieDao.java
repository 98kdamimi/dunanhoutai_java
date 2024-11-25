package com.junyang.dao.version;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionDigtalshieEntity;

@Repository
public interface VersionDigtalshieDao extends BaseMapper<VersionDigtalshieEntity>{

	@Update("update version_digtalshie set release_state = #{releaseState}")
	void updateStateAll(@Param("releaseState") Integer releaseState);

}
