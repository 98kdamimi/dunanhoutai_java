package com.junyang.dao.version;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junyang.entity.release.VersionEntity;

@Repository
public interface VersionDao extends BaseMapper<VersionEntity>{

	@Select("<script>"
			+ "select * from version_db where 1=1"
			+ "<if test = 'releaseState != null'> and release_state = #{releaseState}</if>"
			+ "<if test = 'begTime != null'> and date_format(set_time,'%Y-%m-%d')  &gt;= #{begTime} </if>"
			+ "<if test = 'endTime != null'> and date_format(set_time,'%Y-%m-%d')  &lt;= #{endTime} </if> "
			+ "order by set_time desc"
			+ "</script>")
	List<VersionEntity> findList(VersionEntity entity);

	@Select("select count(*) from version_db where release_state = #{releaseState}")
	int findOnlineNum(@Param("releaseState") Integer releaseState);

	@Update("update version_db set release_state = #{releaseState}")
	void updateState(@Param("releaseState") Integer releaseState);

}
