import withTranslation from "next-translate/withTranslation";
import React from "react";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import PropTypes from "prop-types";
import { UserAPI } from "../../app/api";
import LoadingModal from "../modal/LoadingModal";
import makeCancelable from "../../app/cancelablePromise";

class AssignUserModal extends React.Component {
  static initialState = {
    users: null,
    selectedUserId: "",
    assignUserFormValidated: false,
  };

  constructor(props) {
    super(props);
    this.state = AssignUserModal.initialState;
    this.assignUser = this.assignUser.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { show } = this.props;

    if (!prevProps.show && show) {
      this.setState(AssignUserModal.initialState);
      this.loadUsers();
    } else if (prevProps.show && !show) {
      this.cancelRequests();
    }
  }

  componentWillUnmount() {
    this.cancelRequests();
  }

  cancelRequests() {
    if (this.loadUsersRequest) {
      this.loadUsersRequest.cancel();
    }
  }

  async loadUsers() {
    this.loadUsersRequest = makeCancelable(UserAPI.getAll());
    const users = await this.loadUsersRequest.promise;
    this.setState({ users });
  }

  assignUser(event) {
    event.preventDefault();
    event.stopPropagation();
    const { onAssign } = this.props;
    const { selectedUserId, users } = this.state;
    const { form } = event.currentTarget;

    this.setState({ assignUserFormValidated: true });
    if (form.checkValidity() === false) {
      return;
    }
    const selectedUser = users.find((user) => user.id === selectedUserId);
    if (onAssign) onAssign(selectedUser);
  }

  usersSelect() {
    const { t } = this.props.i18n;
    const { assignedUsers } = this.props;
    const { users, selectedUserId } = this.state;
    if (!Array.isArray(users) || !users.length) {
      return t("categories:noUsers");
    }
    const usersOptions = users.map((user) => {
      const userAlreadyAssigned = assignedUsers.find((assignedUser) => assignedUser.id === user.id);
      return (
        <option
          className={userAlreadyAssigned ? "text-success font-italic" : ""}
          disabled={userAlreadyAssigned}
          key={user.id}
          value={user.id}
        >
          {user.username}
          {userAlreadyAssigned ? ` - ${t("categories:userAlreadyAssigned")}` : null}
        </option>
      );
    });
    return (
      <>
        <Form.Select
          as="select"
          value={selectedUserId}
          onChange={(e) => this.setState({ selectedUserId: Number(e.target.value) })}
          required
        >
          <option value="" disabled>
            {t("categories:selectUser")}
          </option>
          {usersOptions}
        </Form.Select>
        <Form.Control.Feedback type="invalid">
          {t("categories:validation.userRequired")}
        </Form.Control.Feedback>
      </>
    );
  }

  modalContent() {
    const { t } = this.props.i18n;
    const { onClose } = this.props;
    const { assignUserFormValidated } = this.state;
    return (
      <>
        <Modal.Header closeButton>
          <Modal.Title>{t("categories:assignUser")}</Modal.Title>
        </Modal.Header>
        <Form noValidate validated={assignUserFormValidated} onSubmit={this.assignUser}>
          <Modal.Body>{this.usersSelect()}</Modal.Body>
          <Modal.Footer>
            <Button variant="success" type="submit" onClick={this.assignUser}>
              {t("common:actions.assign")}
            </Button>
            <Button variant="secondary" onClick={onClose}>
              {t("common:actions.close")}
            </Button>
          </Modal.Footer>
        </Form>
      </>
    );
  }

  render() {
    const { show, onClose } = this.props;
    const { users } = this.state;

    return (
      <Modal
        backdropClassName="modal-embedded-backdrop"
        className="modal-embedded"
        show={show}
        size="sm"
        onHide={onClose}
      >
        {users == null ? <LoadingModal size="xs" /> : this.modalContent()}
      </Modal>
    );
  }
}

AssignUserModal.propTypes = {
  assignedUsers: PropTypes.arrayOf(PropTypes.shape({ id: PropTypes.number.isRequired })).isRequired,
  show: PropTypes.bool.isRequired,
  onAssign: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default withTranslation(AssignUserModal);
