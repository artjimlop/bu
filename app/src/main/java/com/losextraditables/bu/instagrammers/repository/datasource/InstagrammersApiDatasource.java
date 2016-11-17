package com.losextraditables.bu.instagrammers.repository.datasource;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.losextraditables.bu.base.view.error.ConnectionError;
import com.losextraditables.bu.instagrammers.domain.model.Instagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammerResponse;
import com.losextraditables.bu.instagrammers.repository.service.InstagramApiService;
import com.losextraditables.bu.instagrammers.repository.service.InstagramServiceGenerator;
import com.losextraditables.bu.utils.FirebaseService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;
import rx.Subscriber;

public class InstagrammersApiDatasource implements InstagrammersDatasource {

  private final InstagramServiceGenerator serviceGenerator;
  private final FirebaseService firebaseService;
  private boolean changeMade = false;

  @Inject public InstagrammersApiDatasource(InstagramServiceGenerator serviceGenerator,
      FirebaseService firebaseService) {
    this.serviceGenerator = serviceGenerator;
    this.firebaseService = firebaseService;
  }

  @Override public Observable<List<Instagrammer>> getInstagrammers(final String uid) {
    return Observable.create(new Observable.OnSubscribe<List<Instagrammer>>() {
      @Override public void call(final Subscriber<? super List<Instagrammer>> subscriber) {
        final Firebase instagrammersReference = firebaseService.instagrammersReference(uid);
        instagrammersReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Instagrammer>> t =
                new GenericTypeIndicator<List<Instagrammer>>() {
                };
            List<Instagrammer> instagrammers = dataSnapshot.getValue(t);
            subscriber.onNext(instagrammers);
          }

          @Override public void onCancelled(FirebaseError firebaseError) {
            Crashlytics.log(firebaseError.getMessage());
            subscriber.onError(new ConnectionError());
          }
        });
      }
    });
  }

  @Override public List<SearchedInstagrammer> searchIntagrammers(String query, String accessToken) {
    InstagramApiService instagramService = serviceGenerator.createInstagramService();
    SearchedInstagrammerResponse searchedInstagrammerResponse =
        instagramService.searchInstagrammers(query, accessToken);
    return searchedInstagrammerResponse.getData();
  }

  @Override public PaginatedCollection<List<Instagrammer>> getPage(Page page) throws Exception {
    return null;
  }

  @Override public List<Instagrammer> getByKey(Integer key) throws Exception {
    return null;
  }

  @Override public Collection<List<Instagrammer>> getAll() throws Exception {
    return null;
  }

  @Override public Observable<Instagrammer> getInstagrammerFromScrap(final String url) {
    return Observable.just(getIntagrammerFromScrap(url));
  }

  @Override public Observable<List<Instagrammer>> getInstagrammersFromScrap(List<String> urls) {
    return Observable.just(getIntagrammersFromScrap(urls));
  }

  private List<Instagrammer> getIntagrammersFromScrap(List<String> urls) {
    List<Instagrammer> instagrammers = new ArrayList<>(urls.size());
    for (String url : urls) {
      instagrammers.add(getIntagrammerFromScrap(url));
    }
    return instagrammers;
  }

  @Override
  public Instagrammer getIntagrammerFromScrap(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      String username = "";
      String photo = "";
      Instagrammer instagrammer = new Instagrammer();
      for (Element meta : doc.select("meta")) {
        if (meta.attr("property").equals("al:android:url")) {
          String content = String.valueOf(meta.attr("content"));
          Pattern compile = Pattern.compile("https://www.instagram.com/");
          Matcher matcher = compile.matcher(content);
          if (matcher.find()) {
            username = content.substring(matcher.end() + 3, content.length() - 1);
          }
        }
        if (meta.attr("property").equals("og:image")) {
          photo = String.valueOf(meta.attr("content"));
        }

        if (!username.isEmpty() && !photo.isEmpty()) {
          instagrammer.setUserName(username);
          instagrammer.setProfilePicture(photo);
          instagrammer.setWebsite(url);
          break;
        }
      }
      return instagrammer;
    } catch (Exception e) {
      throw new ConnectionError();
    }
  }

  public Observable<Void> saveInstagrammer(final Instagrammer instagrammer, final String uid) {
    changeMade = false;
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        final Firebase instagrammersReference = firebaseService.instagrammersReference(uid);
        instagrammersReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Instagrammer>> t =
                new GenericTypeIndicator<List<Instagrammer>>() {
                };
            List<Instagrammer> instagrammers = dataSnapshot.getValue(t);
            if (!changeMade) {
              if (instagrammers != null) {
                instagrammers.add(instagrammer);
                instagrammersReference.setValue(instagrammers);
              } else {
                instagrammers = Collections.singletonList(instagrammer);
                instagrammersReference.setValue(instagrammers);
              }
              changeMade = true;
            }
            subscriber.onCompleted();
          }

          @Override public void onCancelled(FirebaseError firebaseError) {
            Crashlytics.log(firebaseError.getMessage());
            subscriber.onError(new ConnectionError());
          }
        });
      }
    });
  }

  public Observable<Void> removeInstagrammer(final String username, final String uid) {
    changeMade = false;
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        final Firebase instagrammersReference = firebaseService.instagrammersReference(uid);
        instagrammersReference.addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Instagrammer>> t =
                new GenericTypeIndicator<List<Instagrammer>>() {
                };
            List<Instagrammer> instagrammers = dataSnapshot.getValue(t);
            if (instagrammers != null) {
              List<Instagrammer> igs = new ArrayList<>();
              for (Instagrammer instagrammer : instagrammers) {
                if (!instagrammer.getUserName().equals(username)) {
                  igs.add(instagrammer);
                }
              }
              instagrammersReference.setValue(igs);
            }
            subscriber.onCompleted();
          }

          @Override public void onCancelled(FirebaseError firebaseError) {
            Crashlytics.log(firebaseError.getMessage());
            subscriber.onError(new ConnectionError());
          }
        });
      }
    });
  }

  public List<String> getInstagrammerPicturesFromScrap(String profileUrl) {
    HttpURLConnection urlConnection = null;
    ArrayList<String> pictures = new ArrayList<>();
    String json = "";
    try {
      URL url = new URL(profileUrl + "?__a=1");
      urlConnection = (HttpURLConnection) url.openConnection();
      InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
      BufferedReader buff = new BufferedReader(in);
      String line;
      do {
        line = buff.readLine();
        json += line;
      } while (line != null);

      JSONObject jsonObject = new JSONObject(json);
      JSONObject jsonUserObject = jsonObject.getJSONObject("user");
      JSONObject jsonMediaObject = jsonUserObject.getJSONObject("media");
      JSONArray jsonNodesArray = jsonMediaObject.getJSONArray("nodes");

      int lengthJsonArr = jsonNodesArray.length();

      for (int i = 0; i < lengthJsonArr; i++) {
        JSONObject jsonChildNode = jsonNodesArray.getJSONObject(i);
        String picture = jsonChildNode.optString("display_src");
        pictures.add(picture);
      }
    } catch (Exception e) {
      throw new ConnectionError();
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
    return pictures;
  }
}
