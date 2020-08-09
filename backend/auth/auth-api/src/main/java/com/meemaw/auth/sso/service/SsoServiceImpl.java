package com.meemaw.auth.sso.service;

import com.meemaw.auth.password.service.PasswordService;
import com.meemaw.auth.signup.service.SignUpService;
import com.meemaw.auth.sso.datasource.SsoDatasource;
import com.meemaw.auth.sso.model.SsoUser;
import com.meemaw.auth.user.datasource.UserDatasource;
import com.meemaw.auth.user.model.AuthUser;
import com.meemaw.shared.logging.LoggingConstants;
import com.meemaw.shared.rest.response.Boom;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.MDC;

@ApplicationScoped
@Slf4j
public class SsoServiceImpl implements SsoService {

  @Inject SsoDatasource ssoDatasource;
  @Inject PasswordService passwordService;
  @Inject UserDatasource userDatasource;
  @Inject SignUpService signUpService;

  @Override
  @Traced
  @Timed(name = "createSession", description = "A measure of how long it takes to create session")
  public CompletionStage<String> createSession(AuthUser user) {
    MDC.put(LoggingConstants.USER_ID, user.getId().toString());
    MDC.put(LoggingConstants.USER_EMAIL, user.getEmail());
    MDC.put(LoggingConstants.ORGANIZATION_ID, user.getOrganizationId());
    log.info("[AUTH]: Creating session for user id: {} email: {}", user.getId(), user.getEmail());
    return ssoDatasource
        .createSession(user)
        .thenApply(
            sessionId -> {
              MDC.put(LoggingConstants.SSO_SESSION_ID, sessionId);
              return sessionId;
            });
  }

  @Override
  @Traced
  @Timed(name = "findSession", description = "A measure of how long it takes to do logout")
  public CompletionStage<Optional<AuthUser>> findSession(String sessionId) {
    MDC.put(LoggingConstants.SSO_SESSION_ID, sessionId);
    log.info("[AUTH]: Find session: {}", sessionId);
    return ssoDatasource
        .findSession(sessionId)
        .thenApply(maybeSsoUser -> maybeSsoUser.map(SsoUser::dto));
  }

  @Override
  @Traced
  @Timed(name = "findSessions", description = "A measure of how long it takes to find SSO sessions")
  public CompletionStage<Set<String>> findSessions(String sessionId) {
    MDC.put(LoggingConstants.SSO_SESSION_ID, sessionId);
    log.info("[AUTH]: Find all sessions related to session: {}", sessionId);
    return ssoDatasource
        .findSession(sessionId)
        .thenCompose(
            ssoUser ->
                ssoDatasource.getAllSessionsForUser(
                    ssoUser.orElseThrow(() -> Boom.notFound().exception()).getId()));
  }

  @Override
  @Traced
  @Timed(name = "logout", description = "A measure of how long it takes to do logout")
  public CompletionStage<Optional<SsoUser>> logout(String sessionId) {
    MDC.put(LoggingConstants.SSO_SESSION_ID, sessionId);
    log.info("[AUTH]: Logout attempt for session: {}", sessionId);
    return ssoDatasource.deleteSession(sessionId);
  }

  @Override
  @Traced
  @Timed(
      name = "logoutFromAllDevices",
      description = "A measure of how long it takes to do a logout from all devices")
  public CompletionStage<Set<String>> logoutUserFromAllDevices(UUID userId) {
    MDC.put(LoggingConstants.USER_ID, userId.toString());
    log.info("[AUTH]: Logout from all devices userId: {}", userId);
    return ssoDatasource
        .deleteAllSessionsForUser(userId)
        .thenApply(
            deletedSessions -> {
              if (deletedSessions.size() > 0) {
                log.info(
                    "[AUTH]: Successfully logged out of {} devices  userId: {}",
                    deletedSessions.size(),
                    userId);
              }
              return deletedSessions;
            });
  }

  @Override
  @Traced
  @Timed(name = "login", description = "A measure of how long it takes to do a login")
  public CompletionStage<String> login(String email, String password, String ipAddress) {
    MDC.put(LoggingConstants.USER_EMAIL, email);
    log.info("[AUTH]: Login attempt for user: {}", email);
    return passwordService
        .verifyPassword(email, password)
        .thenCompose(
            authUser ->
                this.createSession(authUser)
                    .thenApply(
                        sessionId -> {
                          log.info("[AUTH]: Successful login for user: {}", email);
                          return sessionId;
                        }));
  }

  @Override
  @Traced
  @Timed(name = "socialLogin", description = "A measure of how long it takes to do social login")
  public CompletionStage<String> socialLogin(String email, String fullName) {
    MDC.put(LoggingConstants.USER_EMAIL, email);
    log.info("[AUTH]: Social login attempt for user: {}", email);
    return socialFindOrSignUpUser(email, fullName)
        .thenCompose(this::createSession)
        .thenApply(
            sessionId -> {
              log.info("[AUTH]: Successful social login for user: {}", email);
              return sessionId;
            });
  }

  private CompletionStage<AuthUser> socialFindOrSignUpUser(String email, String fullName) {
    return userDatasource
        .findUser(email)
        .thenCompose(
            maybeUser -> {
              if (maybeUser.isPresent()) {
                AuthUser user = maybeUser.get();
                log.info("[AUTH]: Social login linked with an existing user: {}", user.getId());
                return CompletableFuture.completedFuture(user);
              }
              return signUpService.socialSignUp(email, fullName);
            });
  }
}