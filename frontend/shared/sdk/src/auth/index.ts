import { organizationsResource } from './organization/resource';
import { passwordResource } from './password/resource';
import { signupResource } from './signup/resource';
import { tfaSetupResource, tfaChallengeResource } from './tfa';
import { userResource } from './user/resource';
import { ssoSetupResource, ssoSessionResource, ssoTokenResource } from './sso';

export * from './organization';
export * from './password';
export * from './signup';
export * from './sso';
export * from './tfa';
export * from './user';

export const createAuthClient = (authApiBaseURL: string) => {
  return {
    organization: organizationsResource(authApiBaseURL),
    password: passwordResource(authApiBaseURL),
    signup: signupResource(authApiBaseURL),
    sso: {
      session: ssoSessionResource(authApiBaseURL),
      setup: ssoSetupResource(authApiBaseURL),
      token: ssoTokenResource(authApiBaseURL),
    },
    tfa: {
      setup: tfaSetupResource(authApiBaseURL),
      challenge: tfaChallengeResource(authApiBaseURL),
    },
    user: userResource(authApiBaseURL),
  };
};
