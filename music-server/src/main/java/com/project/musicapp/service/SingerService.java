package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.request.SingerRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SingerService extends IService<Singer> {
    Response addSinger(SingerRequest singerRequest);
    Response allSinger();
    Response singerByName(String name);
    Response singerBySex(Integer sex);
    Singer getSingerById(Integer id) throws DataNotFoundException;
    Response updateSinger(int id, SingerRequest singerRequest);
    Response updateSingerAvatar(int id, MultipartFile file);
    Response deleteSinger(int id);
}
