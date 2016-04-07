package com.losextraditables.bu.login.repository.datasource;

import javax.inject.Inject;

public class UserDatasourceFactory {

    @Inject
    public UserDatasourceFactory() {
    }

    public UserDatasource createDatasource() {
        return new FirebaseUserDataSource();
    }
}
