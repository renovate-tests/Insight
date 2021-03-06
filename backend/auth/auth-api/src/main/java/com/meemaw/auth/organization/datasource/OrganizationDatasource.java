package com.meemaw.auth.organization.datasource;

import com.meemaw.auth.organization.model.CreateOrganizationParams;
import com.meemaw.auth.organization.model.Organization;
import com.meemaw.shared.rest.query.UpdateDTO;
import com.meemaw.shared.sql.client.SqlTransaction;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface OrganizationDatasource {

  CompletionStage<Organization> createOrganization(
      CreateOrganizationParams params, SqlTransaction transaction);

  CompletionStage<Organization> createOrganization(CreateOrganizationParams params);

  CompletionStage<Optional<Organization>> updateOrganization(
      String organizationId, UpdateDTO update);

  CompletionStage<Optional<Organization>> findOrganization(String organizationId);

  CompletionStage<Optional<Organization>> findOrganization(
      String organizationId, SqlTransaction transaction);

  CompletionStage<Boolean> delete(String id, SqlTransaction transaction);

  CompletionStage<SqlTransaction> transaction();
}
