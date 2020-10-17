import React, { useRef, useState } from 'react';
import {
  Button,
  SpacedBetween,
  expandBorderRadius,
  Flex,
} from '@insight/elements';
import { Radio, RadioGroup } from 'baseui/radio';
import type {
  AvatarDTO,
  AvatarType,
  Organization,
  OrganizationDTO,
} from '@insight/types';
import { Block } from 'baseui/block';
import { Avatar } from 'baseui/avatar';
import { useStyletron } from 'baseui';
import {
  fileToBase64,
  getCroppedImageAsDataUrl,
  ImageCrop,
} from 'shared/utils/image';
import { FileUploader } from 'baseui/file-uploader';
import dynamic from 'next/dynamic';
import Divider from 'shared/components/Divider';
import type { Crop } from 'react-image-crop';
import { AuthApi } from 'api';
import { toaster } from 'baseui/toast';
import { Controller, useForm } from 'react-hook-form';
import { REQUIRED_VALIDATION } from 'modules/auth/validation/base';
import FormError from 'shared/components/FormError';

const LazyImageCrop = dynamic(
  () => import('modules/settings/components/ImageCrop')
);

type SetupAvatarFormValues = {
  type: AvatarType;
  crop?: Crop;
  avatar?: string;
};

type Props = {
  organization: Organization;
  setOrganization: (organization: OrganizationDTO) => void;
};

export const SetupAvatar = ({ organization, setOrganization }: Props) => {
  const imageRef = useRef<HTMLImageElement>(null);
  const [_css, theme] = useStyletron();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { control, watch, handleSubmit, errors } = useForm<
    SetupAvatarFormValues
  >({
    defaultValues: {
      type: organization.avatar?.type || 'initials',
      avatar:
        organization?.avatar?.type === 'avatar'
          ? organization.avatar.image
          : undefined,
    },
  });

  const onSubmit = handleSubmit(async ({ type, avatar, crop }) => {
    let avatarSetup: AvatarDTO | undefined;
    if (type === 'avatar') {
      if (!imageRef.current) {
        return;
      }

      if (crop && crop.height && crop.width) {
        const image = await getCroppedImageAsDataUrl(
          imageRef.current,
          crop as ImageCrop
        );
        avatarSetup = { type: 'avatar', image };
      } else {
        avatarSetup = { type: 'avatar', image: avatar as string };
      }
    } else {
      avatarSetup = { type: 'initials' };
    }

    if (JSON.stringify(avatarSetup) === JSON.stringify(organization.avatar)) {
      toaster.positive('Successfuly saved avatar preferences', {});
      return;
    }

    setIsSubmitting(true);

    AuthApi.organization
      .setupAvatar(avatarSetup)
      .then((updatedOrganization) => {
        setOrganization(updatedOrganization);
        toaster.positive('Successfuly saved avatar preferences', {});
      })
      .finally(() => setIsSubmitting(false));
  });

  const avatar = watch('avatar');
  const avatarType = watch('type');

  return (
    <form onSubmit={onSubmit}>
      <SpacedBetween>
        <Controller
          control={control}
          rules={REQUIRED_VALIDATION}
          name="type"
          as={
            <RadioGroup>
              <Radio value="initials">Use initials</Radio>
              <Radio value="avatar">Upload avatar</Radio>
            </RadioGroup>
          }
        />

        <Block>
          {avatarType === 'initials' ? (
            <Avatar
              name={organization.name || 'O'}
              size="70px"
              overrides={{
                Root: {
                  style: {
                    ...expandBorderRadius('8px'),
                    backgroundColor: theme.colors.accent600,
                  },
                },
              }}
            />
          ) : (
            <Controller
              rules={REQUIRED_VALIDATION}
              name="avatar"
              control={control}
              render={(props) => (
                <>
                  <FileUploader
                    name="avatar"
                    accept={['image/png', 'image/png', 'image/jpeg']}
                    multiple={false}
                    onDrop={([acceptedFile]) => {
                      if (acceptedFile) {
                        fileToBase64(acceptedFile).then(props.onChange);
                      }
                    }}
                  />
                  {errors.avatar?.message && (
                    <FormError error={{ message: errors.avatar.message }} />
                  )}
                </>
              )}
            />
          )}
        </Block>
      </SpacedBetween>
      {avatarType === 'avatar' && avatar && (
        <Flex
          width="100%"
          height="auto"
          marginTop="32px"
          justifyContent="center"
          $style={{
            backgroundSize: '20px 20px',
            backgroundPosition: '0px 0px, 0px 10px, 10px -10px, -10px 0px',
            backgroundImage:
              'linear-gradient(45deg, rgb(238, 238, 238) 25%, rgba(0, 0, 0, 0) 25%), linear-gradient(-45deg, rgb(238, 238, 238) 25%, rgba(0, 0, 0, 0) 25%), linear-gradient(45deg, rgba(0, 0, 0, 0) 75%, rgb(238, 238, 238) 75%), linear-gradient(-45deg, rgba(0, 0, 0, 0) 75%, rgb(238, 238, 238) 75%)',
          }}
        >
          <Controller
            name="crop"
            control={control}
            render={(props) => {
              return (
                <LazyImageCrop
                  src={avatar}
                  style={{ maxWidth: '100%', maxHeight: '100%' }}
                  crop={props.value}
                  onChange={props.onChange}
                  forwardedRef={imageRef}
                />
              );
            }}
          />
        </Flex>
      )}
      <Divider />
      <SpacedBetween>
        <div />
        <Button isLoading={isSubmitting} type="submit">
          Save Avatar
        </Button>
      </SpacedBetween>
    </form>
  );
};