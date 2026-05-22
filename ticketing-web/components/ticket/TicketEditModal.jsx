import withTranslation from "next-translate/withTranslation";
import React from "react";
import PropTypes from "prop-types";
import { Button, Form } from "react-bootstrap";
import Tables from "../../app/tables";
import AuditedEntityMetadata from "../AuditedEntityMetadata";
import EditModal from "../modal/EditModal";
import { CategoriesAPI, TicketsAPI, UserAPI } from "../../app/api";
import SpinnerButton from "../SpinnerButton";
import DataTable from "../table/DataTable";
import Forms from "../../app/forms";

class TicketEditModal extends React.Component {
  static initialState = {
    ticket: null,
  };

  static PriorityOptionValueMap = {
    LOW: 1,
    MEDIUM: 2,
    HIGH: 3,
  };

  static OptionValuePriorityMap = {
    1: "LOW",
    2: "MEDIUM",
    3: "HIGH",
  };

  constructor(props) {
    super(props);
    this.state = TicketEditModal.initialState;
    this.modalTitle = this.modalTitle.bind(this);
    this.updateTicket = this.updateTicket.bind(this);
    this.loadTicket = this.loadTicket.bind(this);
    this.deleteTicket = this.deleteTicket.bind(this);
    this.modalButtons = this.modalButtons.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { show } = this.props;

    if (!prevProps.show && show) {
      this.reset();
    }
  }

  reset() {
    this.setState(TicketEditModal.initialState);
  }

  modalTitle(ticket) {
    const { t } = this.props.i18n;
    return `${t("tickets:ticket")}\u00A0-\u00A0${ticket.title}`;
  }

  async deleteTicket() {
    const { ticketId } = this.props;
    return TicketsAPI.delete(ticketId);
  }

  async loadTicket() {
    const { ticketId } = this.props;
    return this._transformTicketsForForm(await TicketsAPI.getOne(ticketId));
  }

  async updateTicket(ticket) {
    const { ticketId } = this.props;

    const ticketData = {
      content: ticket.content,
      title: ticket.title,
      assignedUser: parseInt(ticket.assignedUser),
      category: parseInt(ticket.category),
      priority: TicketEditModal.OptionValuePriorityMap[ticket.priority],
    };
    return TicketsAPI.update(ticketId, ticketData);
  }

  _transformTicketsForForm(ticketData) {
    const isResolved = ticketData.resolved;
    this.setState({ isResolved });

    const formData = Object.assign({}, ticketData);
    Object.assign(formData, {
      priority: TicketEditModal.PriorityOptionValueMap[ticketData.priority],
      assignedUser: ticketData.assignedUser?.id,
      category: ticketData.category?.id,
    });

    return formData;
  }

  categories() {
    return this.props.categories.map((category) => (
      <option value={category.id} key={category.id}>
        {category.name}
      </option>
    ));
  }

  users() {
    return this.props.users.map((user) => (
      <option value={user.id} key={user.id}>
        {user.username}
      </option>
    ));
  }

  modalButtons() {
    const { t } = this.props.i18n;

    return !this.state.isResolved ? (
      <Button variant="warning" onClick={() => this.resolveTicket()}>
        {t("common:actions.resolve")}
      </Button>
    ) : null;
  }

  async resolveTicket() {
    await TicketsAPI.resolve(this.props.ticketId);
    this.setState({ isResolved: true });
  }

  render() {
    const { show, onClose, onUpdate, onRemove } = this.props;
    const { isResolved } = this.state;
    const { t } = this.props.i18n;

    return (
      <EditModal
        buttons={this.modalButtons()}
        title={this.modalTitle}
        update={this.updateTicket}
        onUpdate={onUpdate}
        remove={this.deleteTicket}
        size={"xl"}
        onRemove={onRemove}
        onClose={onClose}
        show={show}
        load={this.loadTicket}
        disableSave={isResolved}
        validationSchema={Forms.Ticket.schema}
      >
        {({ values, errors, touched, handleChange, handleBlur }) =>
          values && (
            <fieldset disabled={isResolved}>
              <fieldset disabled>
                <Form.Group className="mb-3">
                  <Form.Label column="md">{t("tickets:status.name")}</Form.Label>
                  <Form.Control
                    required
                    value={t(`tickets:status.${values.status}`)}
                    onChange={() => null}
                  />
                </Form.Group>

                {values.resolved ? (
                  <Form.Group className="mb-3">
                    <Form.Label column="md">{t("tickets:resolvedDate")}</Form.Label>
                    <Form.Control required value={values.resolved} onChange={() => null} />
                  </Form.Group>
                ) : null}
              </fieldset>

              <Form.Group className="mb-3">
                <Form.Label column="md">{t("tickets:title.name")}</Form.Label>
                <Form.Control
                  required
                  name="title"
                  onBlur={handleBlur}
                  placeholder={t("tickets:title.placeholder")}
                  value={values.title}
                  onChange={handleChange}
                  isInvalid={touched.title && errors.title}
                />
                <Form.Control.Feedback type="invalid">{errors.title}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label column="md">{t("tickets:content.name")}</Form.Label>
                <Form.Control
                  required
                  name="content"
                  as="textarea"
                  rows={14}
                  onBlur={handleBlur}
                  placeholder={t("tickets:content.placeholder")}
                  value={values.content}
                  onChange={handleChange}
                  isInvalid={touched.content && errors.content}
                />
                <Form.Control.Feedback type="invalid">{errors.content}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label column="md">{t("tickets:category.name")}</Form.Label>
                <Form.Select
                  name="category"
                  feedback={errors.category}
                  value={values.category ?? ""}
                  onChange={handleChange}
                  isInvalid={touched.category && errors.category}
                >
                  <option disabled value="">
                    {t("tickets:category.placeholder")}
                  </option>
                  {this.categories()}
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.category}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label column="md">{t("tickets:assignedUser.name")}</Form.Label>
                <Form.Select
                  name="assignedUser"
                  feedback={errors.assignedUser}
                  value={values.assignedUser ?? ""}
                  onChange={handleChange}
                  isInvalid={touched.assignedUser && errors.assignedUser}
                >
                  <option disabled value="">
                    {t("tickets:assignedUser.placeholder")}
                  </option>
                  <option value="">{t("common:none")}</option>
                  {this.users()}
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.assignedUser}</Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label column="md">{t("tickets:priority.name")}</Form.Label>
                <Form.Select
                  name="priority"
                  onChange={handleChange}
                  feedback={errors.priority}
                  value={values.priority ?? ""}
                  isInvalid={touched.priority && errors.priority}
                >
                  <option disabled value="">
                    {t("tickets:priority.placeholder")}
                  </option>
                  <option value="1">{t("tickets:priority.LOW")}</option>
                  <option value="2">{t("tickets:priority.MEDIUM")}</option>
                  <option value="3">{t("tickets:priority.HIGH")}</option>
                </Form.Select>
                <Form.Control.Feedback type="invalid">{errors.priority}</Form.Control.Feedback>
              </Form.Group>

              <AuditedEntityMetadata entityData={values} />
            </fieldset>
          )
        }
      </EditModal>
    );
  }
}

TicketEditModal.propTypes = {
  ticketId: PropTypes.number,
  show: PropTypes.bool,
  onUpdate: PropTypes.func,
  onRemove: PropTypes.func,
  onClose: PropTypes.func.isRequired,
};

TicketEditModal.defaultProps = {
  ticketId: null,
  show: true,
  onUpdate: null,
  onRemove: null,
};

export default withTranslation(TicketEditModal);
