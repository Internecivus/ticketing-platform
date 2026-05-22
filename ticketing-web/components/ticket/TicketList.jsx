import withTranslation from "next-translate/withTranslation";
import NextLink from "next/link";
import React from "react";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import redirect from "../../app/isomorphic-redirect";
import Path from "../../app/path";
import CategoryEditModal from "../category/CategoryEditModal";
import DefaultLayout from "../../layouts/DefaultLayout";
import withAuthentication from "../withAuthentication";
import { CategoriesAPI, TicketsAPI, UserAPI } from "../../app/api";
import Tables from "../../app/tables";
import CreateModal from "../modal/CreateModal";
import { role } from "../../app/authentication";
import DataTable from "../table/DataTable";
import Alert from "../Alert";
import Forms from "../../app/forms";
import Head from "next/head";
import TicketEditModal from "./TicketEditModal";

class TicketList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showTicketEditModal: false,
      tickets: props.tickets,
      categories: props.categories,
      users: props.users,
      isLoadingTickets: false,
      showAlert: false,
      alert: {
        message: null,
        variant: null,
      },
    };
    this.tickedDeleted = this.tickedDeleted.bind(this);
    this.ticketUpdated = this.ticketUpdated.bind(this);
  }

  static async getInitialProps() {
    const data = await Promise.all([this._loadTickets(), CategoriesAPI.getAll(), UserAPI.getAll()]);
    return {
      tickets: data[0],
      categories: data[1],
      users: data[2],
    };
  }

  selectTicket(id) {
    this.setState({
      selectedTicketId: id,
      showTicketEditModal: true,
    });
  }

  async loadTickets() {
    this.setState({ isLoadingTickets: true });
    const tickets = await this.__loadTickets();
    this.setState({
      tickets,
      isLoadingTickets: false,
    });
  }

  _transformTicketsForTable(ticketsData) {
    const { t } = this.props.i18n;

    return ticketsData.map((ticketData) => {
      return {
        id: ticketData.id,
        priority: t(`tickets:priority.${ticketData.priority}`),
        status: t(`tickets:status.${ticketData.status}`),
        assignedUser: ticketData.assignedUser?.username,
        title: ticketData.title,
        category: ticketData.category?.name,
      };
    });
  }

  ticketUpdated(ticket) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("tickets:message.updated", { title: ticket.title }),
        variant: "success",
      },
      showTicketEditModal: false,
      showAlert: true,
    });
    this.loadTickets();
  }

  tickedDeleted(ticket) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("tickets:message.deleted", { title: ticket.title }),
        variant: "success",
      },
      showTicketEditModal: false,
      showAlert: true,
    });
    this.loadTickets();
  }

  navigateToCreateTicket() {
    redirect({
      pathname: Path.CreateTicket,
    });
  }

  render() {
    const { t } = this.props.i18n;
    const {
      tickets,
      showAlert,
      users,
      categories,
      showTicketEditModal,
      selectedTicketId,
      alert,
      isLoadingTickets,
    } = this.state;
    return (
      <DefaultLayout>
        <Head>
          <title>{this.state.title}</title>
        </Head>
        <Row>
          <Col className="mx-lg-5 mt-1">
            <h1 className="mb-5">{this.state.title}</h1>
            <Alert
              data={alert}
              show={showAlert}
              onClose={() => this.setState({ showAlert: false })}
            />
            <Button
              variant="success"
              className="mb-3"
              onClick={() => this.navigateToCreateTicket()}
            >
              {t("tickets:createTicket")}
            </Button>
            <DataTable.Table
              data={this._transformTicketsForTable(tickets)}
              columns={Tables.Tickets.columns}
              events={{ onClick: (id) => this.selectTicket(id) }}
              isLoading={isLoadingTickets}
            />
            <TicketEditModal
              ticketId={selectedTicketId}
              show={showTicketEditModal}
              users={users}
              categories={categories}
              onUpdate={this.ticketUpdated}
              onRemove={this.tickedDeleted}
              onClose={() => this.setState({ showTicketEditModal: false })}
            />
          </Col>
        </Row>
      </DefaultLayout>
    );
  }
}

export default TicketList;
