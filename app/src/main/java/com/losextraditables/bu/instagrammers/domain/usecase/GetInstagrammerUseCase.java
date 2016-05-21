package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import javax.inject.Inject;
import rx.Observable;

public class GetInstagrammerUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject public GetInstagrammerUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void obtainPicture(String url, String uid) {
    Observable<Instagrammer> instagrammerObservable = instagrammersRepository.getInstagrammer(url);
    notifySuccess(instagrammerObservable);
  }
}
