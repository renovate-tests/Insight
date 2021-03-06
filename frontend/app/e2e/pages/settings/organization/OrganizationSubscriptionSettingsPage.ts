import {
  queryByPlaceholderText,
  queryByText,
  within,
} from '@testing-library/testcafe';
import { Selector } from 'testcafe';

import { ORGANIZATION_SETTINGS_BILLING_SUBSCRIPTION_PAGE } from '../../../../src/shared/constants/routes';

import { AbstractOrganizationSettingsPage } from './AbstractOrganizationSettingsPage';

export class OrganizationSubscriptionSettingsPage extends AbstractOrganizationSettingsPage {
  public readonly header = this.withinContainer.queryByText('Usage & Billing');

  public readonly checkoutForm = {
    cardNumberInputElement: queryByPlaceholderText('Card number'),
    exipiryInputElement: queryByPlaceholderText('MM / YY'),
    cvcInputElement: queryByPlaceholderText('CVC'),
    iframe: Selector(
      'iframe[src^="https://js.stripe.com/v3/elements-inner-card"]'
    ),
    payButton: queryByText('Pay'),
    threedSecure: {
      outerIframe: Selector(
        'iframe[src^="https://js.stripe.com/v3/three-ds-2-challenge"]'
      ),
      innerIframe: Selector('iframe[id="challengeFrame"]'),
      completeButton: queryByText('Complete'),
      failButton: queryByText('Fail'),
      failMessage: queryByText(
        'We are unable to authenticate your payment method. Please choose a different payment method and try again.'
      ),
    },
  };

  public readonly terminateButton = queryByText('Terminate');
  public readonly invoicesTab = queryByText('Invoices');

  public readonly invoiceDetails = {
    downloadButton: queryByText('Download...'),
    downloadReceipt: queryByText('Receipt PDF'),
    downloadInvoice: queryByText('Invoice PDF'),
  };

  public readonly upgradeButton = this.withinContainer.queryByText('Upgrade');
  public readonly planUpgradedToBusinessMessage = queryByText(
    'Successfully upgraded to business plan'
  );
  public readonly planUpgradedToBusinessPropagationMessage = queryByText(
    'Successfully upgraded to business plan. It might take a moment for the change to propagete through our systems.'
  );

  public readonly subscriptions = {
    header: this.withinContainer.queryByText('Subscriptions'),
    nth: (n: number) =>
      within(this.container.find('ul.subscriptions').child(n)),
  };
}

export default new OrganizationSubscriptionSettingsPage(
  ORGANIZATION_SETTINGS_BILLING_SUBSCRIPTION_PAGE
);
