package com.losextraditables.bu.pictures.domain.model.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.pictures.domain.model.Latest;
import com.losextraditables.bu.pictures.model.LatestItemModel;
import com.losextraditables.bu.pictures.model.PictureModel;
import com.losextraditables.bu.videos.view.model.VideoModel;
import com.losextraditables.bu.videos.view.model.mapper.VideoModelMapper;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LatestItemModelMapper extends Mapper<Latest, LatestItemModel> {

  private final PictureModelMapper pictureModelMapper;
  private final VideoModelMapper videoModelMapper;

  @Inject public LatestItemModelMapper(PictureModelMapper pictureModelMapper,
      VideoModelMapper videoModelMapper) {
    this.pictureModelMapper = pictureModelMapper;
    this.videoModelMapper = videoModelMapper;
  }

  @Override public LatestItemModel map(Latest value) {
    LatestItemModel latestItemModel = new LatestItemModel();
    if (value.getHasPicture()) {
      latestItemModel.setHasPicture(value.getHasPicture());
      latestItemModel.setHasVideo(value.getHasVideo());
      latestItemModel.setPicture(pictureModelMapper.map(value.getPicture()));
      latestItemModel.setVideo(new VideoModel());
    } else {
      latestItemModel.setHasPicture(value.getHasPicture());
      latestItemModel.setHasVideo(value.getHasVideo());
      latestItemModel.setVideo(videoModelMapper.map(value.getVideo()));
      latestItemModel.setPicture(new PictureModel());
    }
    return latestItemModel;
  }

  @Override public Latest reverseMap(LatestItemModel value) {
    return null;
  }

  public List<LatestItemModel> listMap(List<Latest> values) {
    List<LatestItemModel> pictureModels = new ArrayList<>(values.size());
    for (Latest value : values) {
      if (value != null) {
        pictureModels.add(map(value));
      }
    }
    return pictureModels;
  }
}
