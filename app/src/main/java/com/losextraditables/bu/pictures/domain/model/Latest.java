package com.losextraditables.bu.pictures.domain.model;

import com.losextraditables.bu.videos.domain.model.Video;
import lombok.Data;

@Data
public class Latest {
  private Picture picture;
  private Video video;
  private Boolean hasPicture;
  private Boolean hasVideo;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Latest latest = new Latest();

    public Builder() {
      setDefaults();
    }

    private void setDefaults() {
      latest.picture = new Picture();
      latest.video = new Video();
      latest.hasPicture = false;
      latest.hasVideo = false;
    }

    public Builder picture(Picture picture) {
      latest.picture = picture;
      latest.hasPicture = true;
      return this;
    }

    public Builder video(Video video) {
      latest.video = video;
      latest.hasVideo = true;
      return this;
    }

    public Latest build() {
      return latest;
    }
  }
}
