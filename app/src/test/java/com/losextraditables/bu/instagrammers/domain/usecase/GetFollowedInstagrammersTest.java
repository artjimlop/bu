package com.losextraditables.bu.instagrammers.domain.usecase;

import com.losextraditables.bu.instagrammers.repository.FollowedInstagrammersRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class GetFollowedInstagrammersTest {

    @Mock
    FollowedInstagrammersRepository followedInstagrammersRepository;

    private GetFollowedInstagrammers interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new GetFollowedInstagrammers(followedInstagrammersRepository);
    }

    @Test
    public void shouldGetInstagrammersFromRepositoryWhenGetInstagrammersCalled() throws Exception {
        interactor.getInstagrammersFromRepository();

        verify(followedInstagrammersRepository).getInstagrammers();
    }
}
