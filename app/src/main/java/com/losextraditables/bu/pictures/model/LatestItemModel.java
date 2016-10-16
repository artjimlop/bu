package com.losextraditables.bu.pictures.model;

import com.losextraditables.bu.videos.view.model.VideoModel;
import lombok.Data;

@Data
public class LatestItemModel {
  private PictureModel picture;
  private VideoModel video;
  private Boolean hasPicture;
  private Boolean hasVideo;
}
