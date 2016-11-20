package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import javax.inject.Inject;
import rx.Observable;

public class SaveInstagrammerUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject public SaveInstagrammerUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void saveInstagrammer(Instagrammer instagrammer, String uid) {
    Observable<Void> pictureObservable =
        instagrammersRepository.saveInstagrammer(instagrammer, uid);
    notifySuccess(pictureObservable);
  }
}
