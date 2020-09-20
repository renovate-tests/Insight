package com.meemaw.auth.sso.session.resource.v1;

import com.meemaw.auth.sso.session.model.SsoSession;
import com.meemaw.auth.sso.session.service.SsoService;
import com.meemaw.shared.context.RequestUtils;
import com.meemaw.shared.rest.response.Boom;
import com.meemaw.shared.rest.response.DataResponse;
import io.vertx.core.http.HttpServerRequest;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SsoResourceImpl implements SsoResource {

  @Inject SsoService ssoService;
  @Context HttpServerRequest request;
  @Context UriInfo info;

  @Override
  public CompletionStage<Response> login(String email, String password) {
    URI serverBaseURI = RequestUtils.getServerBaseURI(info, request);
    String cookieDomain = RequestUtils.parseCookieDomain(serverBaseURI);
    String ipAddress = RequestUtils.getRemoteAddress(request);
    URL refererURL =
        RequestUtils.parseRefererURL(request)
            .orElseThrow(() -> Boom.badRequest().message("referer required").exception());

    String relativeRedirect =
        Optional.ofNullable(RequestUtils.getQueryMap(refererURL.getQuery()).get("redirect"))
            .map(redirect -> URLDecoder.decode(redirect, StandardCharsets.UTF_8))
            .orElse("/");

    URL redirect =
        RequestUtils.sneakyURL(
            UriBuilder.fromUri(RequestUtils.parseBaseURL(refererURL))
                .path(relativeRedirect)
                .build());

    return ssoService
        .passwordLogin(email, password, ipAddress, redirect, serverBaseURI)
        .thenApply(loginResult -> loginResult.loginResponse(cookieDomain));
  }

  @Override
  public CompletionStage<Response> logout(String sessionId) {
    String cookieDomain = RequestUtils.parseCookieDomain(request.absoluteURI());
    return ssoService
        .logout(sessionId)
        .thenApply(
            maybeUser -> {
              ResponseBuilder builder =
                  maybeUser.isPresent()
                      ? Response.noContent()
                      : DataResponse.error(Boom.notFound()).builder();
              return builder.cookie(SsoSession.clearCookie(cookieDomain)).build();
            });
  }

  @Override
  public CompletionStage<Response> logoutFromAllDevices(String sessionId) {
    String cookieDomain = RequestUtils.parseCookieDomain(request.absoluteURI());
    return ssoService
        .logoutFromAllDevices(sessionId)
        .thenApply(
            sessions -> Response.noContent().cookie(SsoSession.clearCookie(cookieDomain)).build());
  }

  @Override
  public CompletionStage<Response> session(String sessionId) {
    String cookieDomain = RequestUtils.parseCookieDomain(request.absoluteURI());
    return ssoService
        .findSession(sessionId)
        .thenApply(
            maybeUser -> {
              if (maybeUser.isEmpty()) {
                log.debug("Session not found");
                return Response.noContent().cookie(SsoSession.clearCookie(cookieDomain)).build();
              }
              return DataResponse.ok(maybeUser.get());
            });
  }

  @Override
  public CompletionStage<Response> mySessions(String sessionId) {
    return ssoService.findSessions(sessionId).thenApply(DataResponse::ok);
  }
}
