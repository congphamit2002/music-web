package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banners")
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("")
    public Response getAllBanners() {
        return Response.success("Get all banners success", bannerService.getAllBanner());
    }
}
