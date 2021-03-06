import React from 'react';
import { render } from 'test/utils';
import { sandbox } from '@rebrowse/testing';
import userEvent from '@testing-library/user-event';
import { StoryConfiguration } from '@rebrowse/storybook';
import { waitFor } from '@testing-library/react';
import { RenderableComponent } from '@rebrowse/next-testing';

import {
  Base,
  WithInvalidCodeError,
  WithMissingChallengeIdError,
  WithExpiredChallengeError,
} from './VerificationPage.stories';

describe('<VerificationPage />', () => {
  const renderVerificationPage = <Props, T, S extends StoryConfiguration<T>>(
    component: RenderableComponent<Props, T, S>
  ) => {
    const result = render(component);

    const codeInput = () =>
      result.container.querySelector(
        'input[aria-label="Please enter your pin code"]'
      ) as HTMLInputElement;

    const submitButton = result.getByText('Submit');

    return { submitButton, codeInput, ...result };
  };

  it('should redirect to dest on totp challenge complete', async () => {
    const { challengeComplete } = Base.story.setupMocks(sandbox);
    const { codeInput, submitButton, replace } = renderVerificationPage(
      <Base />
    );

    userEvent.type(codeInput(), '123456');
    userEvent.click(submitButton);

    await waitFor(() => {
      sandbox.assert.calledWithExactly(challengeComplete, 'totp', 123456);
      sandbox.assert.calledWithExactly(replace, '/');
    });
  });

  it('should redirect to dest on sms challenge complete', async () => {
    const { challengeComplete } = Base.story.setupMocks(sandbox);
    const {
      codeInput,
      submitButton,
      replace,
      getByText,
      findByText,
    } = renderVerificationPage(<Base />);

    userEvent.click(getByText('Text message'));
    await findByText('Mobile verification code');
    userEvent.type(codeInput(), '123456');
    userEvent.click(submitButton);

    await waitFor(() => {
      sandbox.assert.calledWithExactly(challengeComplete, 'sms', 123456);
      sandbox.assert.calledWithExactly(replace, '/');
    });
  });

  it('Should handle invalid code error', async () => {
    const { challengeComplete } = WithInvalidCodeError.story.setupMocks(
      sandbox
    );
    const { codeInput, submitButton, findByText } = renderVerificationPage(
      <WithInvalidCodeError />
    );

    userEvent.type(codeInput(), '123456');
    userEvent.click(submitButton);

    await findByText('Invalid code');
    sandbox.assert.calledWithExactly(challengeComplete, 'totp', 123456);
  });

  it('Should handle missing challangeId id error', async () => {
    const { challengeComplete } = WithExpiredChallengeError.story.setupMocks(
      sandbox
    );
    const { codeInput, submitButton, replace } = renderVerificationPage(
      <WithExpiredChallengeError />
    );

    userEvent.type(codeInput(), '123456');
    userEvent.click(submitButton);

    await waitFor(() => {
      sandbox.assert.calledWithExactly(challengeComplete, 'totp', 123456);
      sandbox.assert.calledWithExactly(replace, '/login?redirect=%2F');
    });
  });

  it('Should handle expired challenge session error', async () => {
    const { challengeComplete } = WithMissingChallengeIdError.story.setupMocks(
      sandbox
    );
    const { codeInput, submitButton, replace } = renderVerificationPage(
      <WithMissingChallengeIdError />
    );

    userEvent.type(codeInput(), '123456');
    userEvent.click(submitButton);

    await waitFor(() => {
      sandbox.assert.calledWithExactly(challengeComplete, 'totp', 123456);
      sandbox.assert.calledWithExactly(replace, '/login?redirect=%2F');
    });
  });
});
