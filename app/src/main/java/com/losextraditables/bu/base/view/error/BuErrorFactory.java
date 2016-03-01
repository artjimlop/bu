package com.losextraditables.bu.base.view.error;

import com.karumi.rosie.domain.usecase.error.ErrorFactory;
import com.losextraditables.bu.BuApiException;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import javax.inject.Inject;

public class BuErrorFactory extends ErrorFactory {

    @Inject
    public BuErrorFactory() {
    }

    @Override public Error create(Exception exception) {
        Throwable targetException = exception;
        if (exception instanceof InvocationTargetException) {
            targetException = ((InvocationTargetException) exception).getTargetException();
        }

        if (targetException instanceof BuApiException) {
            BuApiException marvelApiException = (BuApiException) targetException;
            if (marvelApiException.getCause() instanceof UnknownHostException) {
                return new ConnectionError();
            }
        }
        return new UnknownError();
    }
}
