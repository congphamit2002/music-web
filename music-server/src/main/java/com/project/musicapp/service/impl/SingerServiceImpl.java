package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.SingerMapper;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.SingerRequest;
import com.project.musicapp.service.MinioService;
import com.project.musicapp.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {
    private final SingerMapper singerMapper;

    private final MinioService minioService;

    @Override
    public Response addSinger(SingerRequest singerRequest) {
        Singer singer = new Singer();
        BeanUtils.copyProperties(singerRequest, singer);
        singer.setPic("/singer/singer.jpg");
        try {
            if (singerMapper.insert(singer) > 0) {
                return Response.success("Insert singer success");
            } else {
                return Response.error("Insert singer failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response allSinger() {
        return Response.success(null, singerMapper.selectList(null));
    }

    @Override
    public Response singerByName(String name) {
        try {
            QueryWrapper<Singer> wrapper = new QueryWrapper<>();
            wrapper.like("name", name);
            return Response.success(null, singerMapper.selectList(wrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response singerBySex(Integer sex) {
        try {
            QueryWrapper<Singer> wrapper = new QueryWrapper<>();
            wrapper.eq("sex", sex);
            return Response.success(null, singerMapper.selectList(wrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Singer getSingerById(Integer id) throws DataNotFoundException {
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Singer singer = singerMapper.selectOne(queryWrapper);
        if (singer == null) {
            throw new DataNotFoundException("Can not find singer by id " + id);
        }
        return singer;
    }

    @Override
    public Response updateSinger(int id, SingerRequest singerRequest) {
        try {
            Singer singer = this.getSingerById(id);
            BeanUtils.copyProperties(singerRequest, singer, "id");
            if(singerMapper.updateById(singer) > 0) {
                return Response.success("Update singer success");
            } else {
                return Response.error("Update singer failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSingerAvatar(int id, MultipartFile file) {
        try {
            Singer singer = this.getSingerById(id);
            minioService.uploadImage(file);
            singer.setPic("/singer/" + file.getOriginalFilename());
            if(singerMapper.updateById(singer) > 0) {
                return Response.success("Update singer avatar success");
            } else {
                return Response.error("Update singer avatar failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteSinger(int id) {
        try {
            this.getSingerById(id);
            if(singerMapper.deleteById(id) > 0) {
                return Response.success("Delete singer success");
            } else {
                return Response.error("Delete singer failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }
}
