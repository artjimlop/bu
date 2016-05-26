package com.losextraditables.bu.pictures.domain.model.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.losextraditables.bu.pictures.domain.model.Picture;
import com.losextraditables.bu.pictures.model.PictureModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PictureModelMapper extends Mapper<Picture, PictureModel> {

  @Inject public PictureModelMapper() {
  }

  @Override public PictureModel map(Picture value) {
    PictureModel pictureModel = new PictureModel();
    pictureModel.setUsername(value.getUsername());
    pictureModel.setOriginalUrl(value.getOriginalUrl());
    pictureModel.setUrl(value.getUrl());
    return null;
  }

  @Override public Picture reverseMap(PictureModel value) {
    return null;
  }

  public List<PictureModel> listMap(List<Picture> values) {
    List<PictureModel> pictureModels = new ArrayList<>(values.size());
    for (Picture value : values) {
      pictureModels.add(map(value));
    }
    return pictureModels;
  }
}
