package com.meemaw.auth.user.datasource;

import com.meemaw.auth.user.model.AuthUser;
import com.meemaw.auth.user.model.PhoneNumber;
import com.meemaw.auth.user.model.UserRole;
import com.meemaw.auth.user.model.UserWithLoginInformation;
import com.meemaw.shared.rest.query.SearchDTO;
import com.meemaw.shared.rest.query.UpdateDTO;
import com.meemaw.shared.sql.client.SqlTransaction;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface UserDatasource {

  CompletionStage<AuthUser> createUser(
      String email,
      String fullName,
      String organizationId,
      UserRole role,
      PhoneNumber phoneNumber,
      SqlTransaction transaction);

  CompletionStage<AuthUser> updateUser(UUID userId, UpdateDTO update);

  CompletionStage<AuthUser> updateUser(UUID userId, UpdateDTO update, SqlTransaction transaction);

  CompletionStage<Optional<AuthUser>> findUser(UUID userId);

  CompletionStage<Optional<AuthUser>> findUser(String email);

  CompletionStage<Optional<AuthUser>> findUser(String email, SqlTransaction transaction);

  CompletionStage<Optional<UserWithLoginInformation>> findUserWithLoginInformation(String email);

  CompletionStage<Collection<AuthUser>> searchOrganizationMembers(
      String organizationId, SearchDTO search);

  CompletionStage<Integer> count(String organizationId, SearchDTO search);
}
