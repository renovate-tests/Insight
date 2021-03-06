import React from 'react';
import type { Meta } from '@storybook/react';
import {
  ACTIVE_BUSINESS_SUBSCRIPTION,
  CANCELED_ENTERPRISE_SUBSCRIPTION,
  INCOMPLETE_ENTERPRISE_SUBSCRIPTION,
} from 'test/data/billing';

import { SubscriptionList } from './SubscriptionList';

export default {
  title: 'billing/components/SubscriptionList',
  component: SubscriptionList,
} as Meta;

export const Empty = () => <SubscriptionList subscriptions={[]} />;

export const NonEmpty = () => {
  return (
    <SubscriptionList
      subscriptions={[
        ACTIVE_BUSINESS_SUBSCRIPTION,
        CANCELED_ENTERPRISE_SUBSCRIPTION,
        INCOMPLETE_ENTERPRISE_SUBSCRIPTION,
      ]}
    />
  );
};
