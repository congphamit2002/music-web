package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.constant.Constants;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.SongMapper;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.domain.Song;
import com.project.musicapp.model.request.SongRequest;
import com.project.musicapp.service.MinioService;
import com.project.musicapp.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    private final SongMapper songMapper;

    private final MinioService minioService;

    @Override
    public Response addSong(SongRequest songRequest, MultipartFile lyricFile, MultipartFile audioFile) {
        Song song = new Song();
        BeanUtils.copyProperties(songRequest, song);
        song.setPic("/img/songPic/tubiao.jpg");
        song.setUrl(Constants.UPLOAD_SONG_IMAGE + audioFile.getOriginalFilename());

        if (lyricFile != null && "[00:00:00]暂无歌词".equals(song.getLyric())) {
            byte[] fileContent = new byte[0];
            try {
                fileContent = lyricFile.getBytes();
                String content = new String(fileContent, "GB2312");
                song.setLyric(content);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if("File uploaded successfully!".equals(minioService.uploadSongImage(audioFile)) && songMapper.insert(song) > 0) {
            return Response.success("Insert song successfully");
        } else {
            return Response.error("Insert song failed");
        }
    }

    @Override
    public Response updateSong(int id, SongRequest songRequest) {
        try {
            Song song = this.getSongById(id);
            BeanUtils.copyProperties(songRequest, song, "id");
            if (songMapper.updateById(song) > 0) {
                return Response.success("Update song successfully");
            } else {
                return Response.error("Update song failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSongUrl(int songId, MultipartFile songFile) {
        try {
            Song song = this.getSongById(songId);
            minioService.uploadSongAudio(songFile);
            song.setUrl(Constants.UPLOAD_SONG_AUDIO + songFile.getOriginalFilename());
            if(songMapper.updateById(song) > 0) {
                return Response.success("Update song url success");
            } else {
                return Response.error("Update song url failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSongPicture(int songId, MultipartFile picture) {
        try {
            Song song = this.getSongById(songId);
            minioService.uploadSongImage(picture);
            song.setPic(Constants.UPLOAD_SONG_IMAGE + picture.getOriginalFilename());
            if(songMapper.updateById(song) > 0) {
                return Response.success("Update song picture success");
            } else {
                return Response.error("Update song picture failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSongLyric(int id, MultipartFile lyricFile) {
        try {
            Song song = this.getSongById(id);
            if (lyricFile != null && "[00:00:00]暂无歌词".equals(song.getLyric())) {
                byte[] fileContent = new byte[0];
                fileContent = lyricFile.getBytes();
                String content = new String(fileContent, "GB2312");
                song.setLyric(content);
            }
            if(songMapper.updateById(song) > 0) {
                return Response.success("Update song lyric success");
            } else {
                return Response.error("Update song lyric failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException | RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteSong(int id) {
        try {
            Song song = this.getSongById(id);
            List<String> deleteFileName = new ArrayList<>();
            deleteFileName.add(song.getPic());
            deleteFileName.add(song.getUrl());
            if (songMapper.deleteById(song) > 0) {
                minioService.removeMultipleFile(deleteFileName);
                return Response.success("Delete song success");
            }
            return Response.error("Delete song failed");
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response allSong() {
        try {
            return Response.success(null, songMapper.selectList(null));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response songOfSingerId(int singerId) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id", singerId);
        try {
            return Response.success(null, songMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response songOfId(Integer id) {
        try {
            return Response.success(null, this.getSongById(id));
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    // Pending, check UI
    @Override
    public Response songlikeName(String name) {
        try {
            QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", name);
            return Response.success(null, songMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Song getSongById(int id) throws DataNotFoundException {
       Song song = songMapper.selectById(id);
       if (song == null) {
           throw new DataNotFoundException("Can not find song by id: " + id);
       }
       return song;
    }
}
