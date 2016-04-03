package com.losextraditables.bu.instagrammers.view.presenter;

import com.karumi.rosie.domain.usecase.UseCaseHandler;
import com.losextraditables.bu.instagrammers.domain.model.SearchedInstagrammer;
import com.losextraditables.bu.instagrammers.domain.usecase.SearchInstagrammers;
import com.losextraditables.bu.instagrammers.view.model.mapper.SearchedInstagrammerModelMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;

public class SearchInstagrammersPresenterTest {

    public static final String QUERY = "query";
    public static final String ACCESS_TOKEN = "access_token";
    SearchInstagrammersPresenter searchInstagrammersPresenter;
    @Mock UseCaseHandler useCaseHandler;
    @Mock SearchInstagrammers searchInstagrammers;
    @Mock SearchedInstagrammerModelMapper searchedInstagrammerModelMapper;
    @Mock SearchInstagrammersPresenter.View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        searchInstagrammersPresenter = new SearchInstagrammersPresenter(useCaseHandler, searchInstagrammers, searchedInstagrammerModelMapper);
        searchInstagrammersPresenter.setView(view);
    }

    @Test
    public void shouldHideEmptyWhenSearch() throws Exception {
        searchInstagrammersPresenter.search(QUERY, ACCESS_TOKEN);

        verify(view).hideEmpty();
    }

    @Test
    public void shouldHideContentWhenSearch() throws Exception {
        searchInstagrammersPresenter.search(QUERY, ACCESS_TOKEN);

        verify(view).hideContent();
    }

    @Test
    public void shouldShowLoadingWhenSearch() throws Exception {
        searchInstagrammersPresenter.search(QUERY, ACCESS_TOKEN);

        verify(view).showLoading();
    }

    @Test
    public void shouldSetCurrentQueryWhenSearch() throws Exception {
        searchInstagrammersPresenter.search(QUERY, ACCESS_TOKEN);

        verify(view).setCurrentQuery(QUERY);
    }

    @Test
    public void shouldShowEmptyWhenSearchErrorCallback() throws Exception {
        searchInstagrammersPresenter.showErrorInView();

        verify(view).showEmpty();
    }

    @Test
    public void shouldShowConnectionErrorWhenSearchErrorCallback() throws Exception {
        searchInstagrammersPresenter.showErrorInView();

        verify(view).showConnectionError();
    }

    @Test
    public void shouldMapSearchedInstagrammersListWhenSearchSuccessCallback() throws Exception {
        searchInstagrammersPresenter.showInstagrammersInView(searchedInstagrammers());

        verify(searchedInstagrammerModelMapper).map(anyList());
    }

    @Test
    public void shouldShowContentInViewWhenSeachSuccessCallback() throws Exception {
        searchInstagrammersPresenter.showInstagrammersInView(searchedInstagrammers());

        verify(view).showContent();
    }

    @Test
    public void shouldRenderInstagrammersInViewWhenSeachSuccessCallback() throws Exception {
        searchInstagrammersPresenter.showInstagrammersInView(searchedInstagrammers());

        verify(view).renderInstagrammers(anyList());
    }

    private List<SearchedInstagrammer> searchedInstagrammers() {
        return Collections.singletonList(searchedInstagrammer());
    }

    private SearchedInstagrammer searchedInstagrammer() {
        return new SearchedInstagrammer();
    }
}
