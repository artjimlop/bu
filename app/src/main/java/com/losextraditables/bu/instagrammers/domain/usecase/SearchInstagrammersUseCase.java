package com.losextraditables.bu.instagrammers.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import java.util.List;
import javax.inject.Inject;

public class SearchInstagrammersUseCase extends RosieUseCase {

  private final InstagrammersRepository instagrammersRepository;

  @Inject
  public SearchInstagrammersUseCase(InstagrammersRepository instagrammersRepository) {
    this.instagrammersRepository = instagrammersRepository;
  }

  @UseCase
  public void getInstagrammers(String query, String accessToken) throws Exception {
    List<SearchedInstagrammer> instagrammers =
        instagrammersRepository.searchInstagrammers(query, accessToken);
    notifySuccess(instagrammers);
  }
}
