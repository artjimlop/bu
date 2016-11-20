package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class RefreshProfilesUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject public RefreshProfilesUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void getProfilePictures(List<Instagrammer> instagrammersToRefresh) {
    List<String> profileUrls = new ArrayList<>(instagrammersToRefresh.size());

    for (Instagrammer instagrammer : instagrammersToRefresh) {
      profileUrls.add(instagrammer.getWebsite());
    }
    Observable<List<Instagrammer>> instagrammersObservable =
        instagrammersRepository.getInstagrammers(profileUrls);
    notifySuccess(instagrammersObservable);
  }
}
