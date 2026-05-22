import React from 'react';
import NextLink from 'next/link';
import { useRouter } from 'next/router';
import NavDropdown from 'react-bootstrap/NavDropdown';

function NavigationItem(props) {
  const { href, children } = props;
  const router = useRouter();

  return (
    <NextLink href={href} passHref>
      <NavDropdown.Item active={router.pathname === href}>
        {children}
      </NavDropdown.Item>
    </NextLink>
  );
}

export default NavigationItem;
