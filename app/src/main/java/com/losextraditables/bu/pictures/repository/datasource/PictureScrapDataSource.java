package com.losextraditables.bu.pictures.repository.datasource;

import com.losextraditables.bu.base.view.error.ConnectionError;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;

public class PictureScrapDataSource implements PictureDataSource {

  @Inject public PictureScrapDataSource() {
  }

  @Override public Observable<String> getPictureFromScrap(final String url) {
    return Observable.just(getUrlFromScrap(url));
  }

  private String getUrlFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String possibleUrl = "";
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("og:image")) {
          possibleUrl = String.valueOf(meta.attr("content"));
          break;
        }
      }
      return possibleUrl;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }
}
