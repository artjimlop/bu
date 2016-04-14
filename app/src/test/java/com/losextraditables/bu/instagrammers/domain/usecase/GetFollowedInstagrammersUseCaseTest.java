package com.losextraditables.bu.instagrammers.domain.usecase;

import com.losextraditables.bu.instagrammers.repository.InstagrammersRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class GetFollowedInstagrammersUseCaseTest {

    @Mock
    InstagrammersRepository instagrammersRepository;

    private GetFollowedInstagrammersUseCase interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new GetFollowedInstagrammersUseCase(instagrammersRepository);
    }

    @Test
    public void shouldGetInstagrammersFromRepositoryWhenGetInstagrammersCalled() throws Exception {
        interactor.getInstagrammersFromRepository();

        verify(instagrammersRepository).getInstagrammers();
    }
}
