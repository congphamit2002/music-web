package com.project.musicapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.musicapp.model.domain.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
