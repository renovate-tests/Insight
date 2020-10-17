import React, { useCallback, useMemo, useState } from 'react';
import { Block } from 'baseui/block';
import YourPlan from 'modules/billing/components/YourPlan';
import { addDays } from 'date-fns';
import { Modal } from 'baseui/modal';
import { CheckoutForm } from 'modules/billing/components/CheckoutForm';
import { Card, StyledBody } from 'baseui/card';
import { SubscriptionList } from 'modules/billing/components/SubscriptionList';
import { toaster } from 'baseui/toast';
import type {
  Organization,
  PlanDTO,
  Subscription,
  SubscriptionPlan,
} from '@insight/types';

type Props = {
  subscriptions: Subscription[];
  organization: Organization;
  plan: PlanDTO;
  revalidateSubscriptions: () => void;
  revalidateActivePlan: () => void;
  setActivePlan: (plan: PlanDTO) => void;
};

export const BillingSubscription = ({
  organization,
  plan,
  subscriptions,
  revalidateSubscriptions,
  revalidateActivePlan,
  setActivePlan,
}: Props) => {
  const [isUpgrading, setIsUpgrading] = useState(false);
  const onUpgradeClick = useCallback(() => setIsUpgrading(true), []);
  const resetsOn = useMemo(() => addDays(organization.createdAt, 30), [
    organization.createdAt,
  ]);

  const onPaymentIntentSucceeded = useCallback(
    (planType: SubscriptionPlan) => {
      revalidateSubscriptions();
      setIsUpgrading(false);
      toaster.positive(
        `Successfully upgraded to ${planType} plan. It might take a moment for the change to propagete through our systems.`,
        { autoHideDuration: 10000 }
      );

      // After 10 seconds webhook should surely be processed
      // TODO: find more elegant solution to this, e.g. websockets
      setTimeout(() => {
        revalidateSubscriptions();
        revalidateActivePlan();
      }, 10000);
    },
    [revalidateActivePlan, revalidateSubscriptions]
  );

  const onPlanUpgraded = useCallback(
    (upgradedPlan: PlanDTO) => {
      revalidateSubscriptions();
      setActivePlan(upgradedPlan);
      setIsUpgrading(false);
      toaster.positive(`Successfully upgraded to ${upgradedPlan.type} plan`, {
        autoHideDuration: 10000,
      });
    },
    [setActivePlan, revalidateSubscriptions]
  );

  return (
    <Block>
      <YourPlan
        sessionsUsed={0}
        plan={plan.type}
        dataRetention={plan.dataRetention}
        resetsOn={resetsOn}
        onUpgradeClick={onUpgradeClick}
        isLoading={false}
      />
      {plan?.type !== 'enterprise' && (
        <Modal isOpen={isUpgrading} onClose={() => setIsUpgrading(false)}>
          <CheckoutForm
            onPlanUpgraded={onPlanUpgraded}
            onPaymentIntentSucceeded={onPaymentIntentSucceeded}
          />
        </Modal>
      )}

      <Card
        title="Subscriptions"
        overrides={{ Root: { style: { marginTop: '20px' } } }}
      >
        <StyledBody>
          <SubscriptionList subscriptions={subscriptions} />
        </StyledBody>
      </Card>
    </Block>
  );
};