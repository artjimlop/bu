package com.losextraditables.bu.videos.view.model.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.videos.domain.model.Video;
import com.losextraditables.bu.videos.view.model.VideoModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class VideoModelMapper extends Mapper<Video, VideoModel> {

  @Inject public VideoModelMapper() {
  }

  @Override public VideoModel map(Video video) {
    VideoModel videoModel = new VideoModel();
    videoModel.setTitle(video.getTitle());
    videoModel.setImage(video.getImage());
    videoModel.setUrl(video.getUrl());
    return videoModel;
  }

  @Override public Video reverseMap(VideoModel videoModel) {
    Video video = new Video();
    video.setTitle(videoModel.getTitle());
    video.setImage(videoModel.getImage());
    video.setUrl(videoModel.getUrl());
    return video;
  }

  public List<VideoModel> mapList(List<Video> videos) {
    List<VideoModel> videoModels = new ArrayList<>();
    for (Video video : videos) {
      if (video.getUrl() != null && !video.getUrl().isEmpty()) {
        videoModels.add(map(video));
      }
    }
    return videoModels;
  }
}
