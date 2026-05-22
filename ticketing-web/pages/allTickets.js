import withTranslation from "next-translate/withTranslation";
import React from "react";
import { CategoriesAPI, TicketsAPI, UserAPI } from "../app/api";
import { Authentication, role } from "../app/authentication";
import TicketList from "../components/ticket/TicketList";
import withAuthentication from "../components/withAuthentication";

class AllTickets extends TicketList {
  static async getInitialProps() {
    const data = await Promise.all([this._loadTickets(), CategoriesAPI.getAll(), UserAPI.getAll()]);
    return {
      tickets: data[0],
      categories: data[1],
      users: data[2],
    };
  }

  constructor(props) {
    super(props);

    const { t } = this.props.i18n;
    this.state.title = t("allTickets:title");
    this.__loadTickets = AllTickets._loadTickets;
  }

  static async _loadTickets() {
    return await TicketsAPI.getAll();
  }
}

export default withTranslation(withAuthentication(role.TICKET_ADMINISTRATION, AllTickets));
