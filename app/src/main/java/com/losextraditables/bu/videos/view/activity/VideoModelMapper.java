package com.losextraditables.bu.videos.view.activity;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.view.activity.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VideoModelMapper extends Mapper<Video, VideoModel> {

    @Inject VideoModelMapper() {}

    @Override
    public VideoModel map(Video value) {
        VideoModel videoModel = new VideoModel();

        videoModel.setImage(value.getImage());
        videoModel.setUrl(value.getUrl());
        videoModel.setTitle(value.getTitle());

        return videoModel;
    }

    @Override
    public Video reverseMap(VideoModel value) {
        Video video = new Video();

        video.setTitle(value.getTitle());
        video.setUrl(value.getUrl());
        video.setImage(value.getImage());

        return video;
    }

    public List<VideoModel> mapList(List<Video> videos) {
        ArrayList<VideoModel> videoModels = new ArrayList<>();
        for (Video video : videos) {
            videoModels.add(map(video));
        }
        return videoModels;
    }
}
