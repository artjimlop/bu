package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import java.util.List;
import javax.inject.Inject;

public class GetProfilePicturesUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject public GetProfilePicturesUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void getProfilePictures(String profileUrl) {
    List<String> pictureObservable = instagrammersRepository.getInstagrammerProfilePictures(profileUrl);
    notifySuccess(pictureObservable);
  }
}
