package com.losextraditables.bu.instagrammers.domain.usecase;

import com.losextraditables.bu.instagrammers.repository.FollowedInstagrammersRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class GetFollowedInstagrammersUseCaseTest {

    @Mock
    FollowedInstagrammersRepository followedInstagrammersRepository;

    private GetFollowedInstagrammersUseCase interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new GetFollowedInstagrammersUseCase(followedInstagrammersRepository);
    }

    @Test
    public void shouldGetInstagrammersFromRepositoryWhenGetInstagrammersCalled() throws Exception {
        interactor.getInstagrammersFromRepository();

        verify(followedInstagrammersRepository).getInstagrammers();
    }
}
