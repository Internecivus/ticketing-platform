import React from 'react';
import Nav from 'react-bootstrap/Nav';
import NextLink from 'next/link';
import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

function NavigationLink(props) {
  const { href, children, onClick } = props;
  const router = useRouter();

  return (
    <NextLink href={href} passHref>
      <Nav.Link active={router.pathname === href} onClick={onClick}>
        {children}
      </Nav.Link>
    </NextLink>
  );
}

NavigationLink.propTypes = {
  children: PropTypes.node.isRequired,
  onClick: PropTypes.func,
};

NavigationLink.defaultProps = {
  onClick: null,
};

export default NavigationLink;
