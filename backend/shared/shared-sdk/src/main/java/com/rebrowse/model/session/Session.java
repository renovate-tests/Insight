package com.rebrowse.model.session;

import com.rebrowse.net.ApiResource;
import com.rebrowse.net.RequestMethod;
import com.rebrowse.net.RequestOptions;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Session {

  UUID id;
  UUID deviceId;
  String organizationId;
  Location location;
  UserAgent userAgent;
  OffsetDateTime createdAt;

  public static CompletionStage<Session> retrieve(UUID id, RequestOptions requestOptions) {
    return ApiResource.request(
        RequestMethod.GET, String.format("/v1/sessions/%s", id), Session.class, requestOptions);
  }
}
