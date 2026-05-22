import withTranslation from "next-translate/withTranslation";
import React from "react";
import { Container } from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Path from "../app/path";
import NavigationLink from "./NavigationLink";
import LanguageSelect from "./LanguageSelect";
import Icon from "./Icon";
import NavigationItem from "./NavigationItem";
import { AuthenticationAPI } from "../app/api";
import Router from "next/router";
import useTranslation from "next-translate/useTranslation";

function Navigation(props) {
  const logout = async (event) => {
    event.preventDefault();
    event.stopPropagation();

    await AuthenticationAPI.logout();
    await Router.push(Path.Login);
  };

  const { t } = useTranslation("common");

  return (
    <Navbar className="px-5" bg="dark" expand="lg" variant="dark">
      <Navbar.Brand href={Path.AllTickets}>{t("nav.title")}</Navbar.Brand>
      <Navbar.Toggle aria-controls="main-navbar" />
      <Navbar.Collapse id="main-navbar">
        <Nav className="me-auto">
          <NavigationLink href={Path.MyTickets}>{t("nav.myTickets")}</NavigationLink>
          <NavigationLink href={Path.AllTickets}>{t("nav.allTickets")}</NavigationLink>
          <NavigationLink href={Path.CreateTicket}>{t("nav.createTicket")}</NavigationLink>
          <NavDropdown title={t("nav.admin.administration")} id="admin-dropdown">
            <NavigationItem href={Path.Categories}>{t("nav.admin.categories")}</NavigationItem>
            <NavigationItem href={Path.Users}>{t("nav.admin.users")}</NavigationItem>
          </NavDropdown>
        </Nav>

        <Nav>
          <div className="d-block d-lg-none d-xl-none">
            <NavigationLink href={Path.Login}>{t("nav.account")}</NavigationLink>
          </div>
          <div className="py-2 py-lg-0">
            <LanguageSelect />
          </div>
          <div className="ms-2 mt-1 py-2 py-lg-0 d-lg-flex d-xl-flex d-none align-items-center">
            <Icon
              size="md"
              href={Path.Categories}
              variant={Icon.variant.ACCOUNT}
              tooltip={t("nav.account")}
            />
          </div>
          <NavigationLink href={Path.Logout} onClick={logout}>
            {t("nav.logout")}
          </NavigationLink>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default Navigation;
