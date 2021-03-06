import React, { forwardRef } from 'react';

export type Props = Omit<
  React.DetailedHTMLProps<
    React.AnchorHTMLAttributes<HTMLAnchorElement>,
    HTMLAnchorElement
  >,
  'href'
> & {
  link: string;
};

export const ExternalLink = forwardRef<HTMLAnchorElement, Props>(
  ({ link, children, ...rest }, ref) => {
    return (
      <a
        target="_blank"
        rel="noreferrer noopener"
        href={link}
        ref={ref}
        {...rest}
      >
        {children}
      </a>
    );
  }
);
