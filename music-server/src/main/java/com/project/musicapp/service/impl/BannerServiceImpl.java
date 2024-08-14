package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.mapper.BannerMapper;
import com.project.musicapp.model.domain.Banner;
import com.project.musicapp.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    private final BannerMapper bannerMapper;

    @Override
    public List<Banner> getAllBanner() {
        return baseMapper.selectList(null);
    }
}
