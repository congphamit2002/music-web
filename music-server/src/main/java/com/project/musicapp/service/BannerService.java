package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.model.domain.Banner;

import java.util.List;

public interface BannerService extends IService<Banner> {
    List<Banner> getAllBanner();
}
