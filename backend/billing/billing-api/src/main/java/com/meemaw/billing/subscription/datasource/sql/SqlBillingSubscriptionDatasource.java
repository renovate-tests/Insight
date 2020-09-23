package com.meemaw.billing.subscription.datasource.sql;

import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.CANCELED_AT;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.CREATED_AT;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.CURRENT_PERIOD_ENDS;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.CUSTOMER_EXTERNAL_ID;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.CUSTOMER_INTERNAL_ID;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.FIELDS;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.ID;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.INSERT_FIELDS;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.PLAN;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.PRICE_ID;
import static com.meemaw.billing.subscription.datasource.sql.SqlBillingSubscriptionTable.TABLE;

import com.meemaw.billing.subscription.datasource.BillingSubscriptionDatasource;
import com.meemaw.billing.subscription.model.BillingSubscription;
import com.meemaw.billing.subscription.model.CreateBillingSubscriptionParams;
import com.meemaw.billing.subscription.model.SubscriptionPlan;
import com.meemaw.shared.sql.client.SqlPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jooq.Query;

@ApplicationScoped
public class SqlBillingSubscriptionDatasource implements BillingSubscriptionDatasource {

  @Inject SqlPool sqlPool;

  @Override
  public CompletionStage<BillingSubscription> create(CreateBillingSubscriptionParams params) {
    Query query =
        sqlPool
            .getContext()
            .insertInto(TABLE)
            .columns(INSERT_FIELDS)
            .values(
                params.getId(),
                params.getPlan().getKey(),
                params.getCustomerExternalId(),
                params.getCustomerInternalId(),
                params.getPriceId(),
                params.getCurrentPeriodEnd())
            .returning(FIELDS);

    return sqlPool.execute(query).thenApply(rows -> mapBillingSubscription(rows.iterator().next()));
  }

  @Override
  public CompletionStage<Optional<BillingSubscription>> findByCustomerInternalId(
      String customerInternalId) {
    Query query =
        sqlPool.getContext().selectFrom(TABLE).where(CUSTOMER_INTERNAL_ID.eq(customerInternalId));
    return sqlPool.execute(query).thenApply(this::onFindBillingSubscription);
  }

  private Optional<BillingSubscription> onFindBillingSubscription(RowSet<Row> rows) {
    if (!rows.iterator().hasNext()) {
      return Optional.empty();
    }

    Row row = rows.iterator().next();
    return Optional.of(mapBillingSubscription(row));
  }

  public static BillingSubscription mapBillingSubscription(Row row) {
    return new BillingSubscription(
        row.getString(ID.getName()),
        SubscriptionPlan.fromString(row.getString(PLAN.getName())),
        row.getString(CUSTOMER_EXTERNAL_ID.getName()),
        row.getString(CUSTOMER_INTERNAL_ID.getName()),
        row.getString(PRICE_ID.getName()),
        row.getLong(CURRENT_PERIOD_ENDS.getName()),
        row.getOffsetDateTime(CREATED_AT.getName()),
        row.getOffsetDateTime(CANCELED_AT.getName()));
  }
}