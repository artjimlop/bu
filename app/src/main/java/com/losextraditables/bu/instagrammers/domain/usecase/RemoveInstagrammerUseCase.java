package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import javax.inject.Inject;
import rx.Observable;

public class RemoveInstagrammerUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject public RemoveInstagrammerUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void removeInstagrammer(String uid, String username) {
    Observable<Void> pictureObservable = instagrammersRepository.removeInstagrammer(uid, username);
    notifySuccess(pictureObservable);
  }
}
