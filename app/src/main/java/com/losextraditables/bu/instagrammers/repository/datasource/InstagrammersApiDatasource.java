package com.losextraditables.bu.instagrammers.repository.datasource;

import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.losextraditables.bu.base.view.error.ConnectionError;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammerResponse;
import com.losextraditables.bu.instagrammers.repository.service.InstagramApiService;
import com.losextraditables.bu.instagrammers.repository.service.InstagramServiceGenerator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;

public class InstagrammersApiDatasource implements InstagrammersDatasource {

  private final InstagramServiceGenerator serviceGenerator;

  @Inject
  public InstagrammersApiDatasource(InstagramServiceGenerator serviceGenerator) {
    this.serviceGenerator = serviceGenerator;
  }

  @Override
  public List<Instagrammer> getInstagrammers() {
    //TODO: this method it's still a fake, it will be implemented when Firebase is added
    Instagrammer instagrammer = new Instagrammer();
    instagrammer.setBio("bio");
    instagrammer.setFullName("fullname");
    instagrammer.setUserName("username");
    instagrammer.setProfilePicture(
        "http://developer.android.com/assets/images/android_logo@2x.png");
    instagrammer.setUserId("userId");
    instagrammer.setWebsite("website");
    instagrammer.setKey("key");
    return Arrays.asList(instagrammer);
  }

  @Override
  public List<SearchedInstagrammer> searchIntagrammers(String query, String accessToken) {
    InstagramApiService instagramService = serviceGenerator.createInstagramService();
    SearchedInstagrammerResponse searchedInstagrammerResponse =
        instagramService.searchInstagrammers(query, accessToken);
    return searchedInstagrammerResponse.getData();
  }

  @Override
  public PaginatedCollection<List<Instagrammer>> getPage(Page page) throws Exception {
    return null;
  }

  @Override
  public List<Instagrammer> getByKey(Integer key) throws Exception {
    return null;
  }

  @Override
  public Collection<List<Instagrammer>> getAll() throws Exception {
    return null;
  }

  @Override public Observable<Instagrammer> getInstagrammerFromScrap(final String url) {
    return Observable.just(getIntagrammerFromScrap(url));
  }

  private Instagrammer getIntagrammerFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String username = "";
      String photo = "";
      Instagrammer instagrammer = new Instagrammer();
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("al:android:url")) {
          String content = String.valueOf(meta.attr("content"));
          Pattern compile = Pattern.compile("https://www.instagram.com/_u/");
          Matcher matcher = compile.matcher(content);
          username = content.substring(0,matcher.end());
        }
        if (meta.attr("property").equals("og:image")) {
          photo = String.valueOf(meta.attr("content"));
        }

        if (!username.isEmpty() && !photo.isEmpty()) {
          instagrammer.setUserName(username);
          instagrammer.setProfilePicture(photo);
          break;
        }
      }
      return instagrammer;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }
}
