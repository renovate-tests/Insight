package com.meemaw.session.sessions.datasource;

import java.util.concurrent.CompletionStage;

public interface SessionCountDatasource {

  CompletionStage<Long> incrementAndGet(String key);
}
