import withTranslation from "next-translate/withTranslation";
import React from "react";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { role } from "../app/authentication";
import AuditedEntityMetadata from "../components/AuditedEntityMetadata";
import withAuthentication from "../components/withAuthentication";
import Alert from "../components/Alert";
import Form from "react-bootstrap/Form";
import DataTable from "../components/table/Table";
import DefaultLayout from "../layouts/DefaultLayout";
import { GroupAPI, UserAPI } from "../app/api";
import Tables from "../app/tables";
import CreateModal from "../components/modal/CreateModal";
import EditModal from "../components/modal/EditModal";
import MultipleCheckboxList from "../components/MultipleCheckboxList";
import Forms from "../app/forms";
import Head from "next/head";

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUserEditModal: false,
      showUserCreateModal: false,
      users: props.users,
      showAlert: false,
      alert: {
        message: null,
        variant: null,
      },
    };
    this.groups = props.groups;
    this.userCreated = this.userCreated.bind(this);
    this.userDeleted = this.userDeleted.bind(this);
    this.userUpdated = this.userUpdated.bind(this);
    this.modalTitle = this.modalTitle.bind(this);
    this.createUser = this.createUser.bind(this);
    this.loadUser = this.loadUser.bind(this);
    this.updateUser = this.updateUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
  }

  static async getInitialProps() {
    const users = await UserAPI.getAll();
    const groups = await GroupAPI.getAll();
    return { users, groups };
  }

  selectUser(id) {
    this.setState({
      selectedUserId: id,
      showUserEditModal: true,
    });
  }

  loadUsers() {
    UserAPI.getAll().then((users) => this.setState({ users }));
  }

  userCreated(user) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        title: t("users:message.created", { username: user.username }),
        variant: "success",
      },
      showUserCreateModal: false,
      showAlert: true,
    });
    this.loadUsers();
  }

  userUpdated(user) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("users:message.updated", { username: user.username }),
        variant: "success",
      },
      showUserEditModal: false,
      showAlert: true,
    });
    this.loadUsers();
  }

  userDeleted(user) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("users:message.deleted", { username: user.username }),
        variant: "success",
      },
      showUserEditModal: false,
      showAlert: true,
    });
    this.loadUsers();
  }

  modalTitle(user) {
    const { t } = this.props.i18n;
    return `${t("users:user")}\u00A0-\u00A0${user.username}`;
  }

  groups;

  async createUser(user) {
    const userData = {
      username: user.username,
      email: user.email,
      groups: user.groups.map((group) => group.id),
    };
    return UserAPI.create(userData);
  }

  async updateUser(user) {
    const { selectedUserId } = this.state;

    const userData = {
      username: user.username,
      email: user.email,
      groups: user.groups.map((group) => group.id),
    };

    return UserAPI.update(selectedUserId, userData);
  }

  async deleteUser() {
    const { selectedUserId } = this.state;
    return UserAPI.delete(selectedUserId);
  }

  async loadUser() {
    const { selectedUserId } = this.state;
    return UserAPI.getOne(selectedUserId);
  }

  render() {
    const { t } = this.props.i18n;
    const { users, showAlert, alert } = this.state;
    const { showUserCreateModal, showUserEditModal } = this.state;
    return (
      <DefaultLayout>
        <Head>
          <title>{t("users:title")}</title>
        </Head>
        <Row>
          <Col className="mx-lg-5 mt-1">
            <h1 className="mb-5">{t("users:title")}</h1>
            <Alert
              data={alert}
              show={showAlert}
              onClose={() => this.setState({ showAlert: false })}
            />
            <Button
              variant="success"
              className="mb-3"
              onClick={() => this.setState({ showUserCreateModal: true })}
            >
              {t("users:createUser")}
            </Button>
            <DataTable
              data={users}
              columns={Tables.Users.columns}
              noDataPlaceholderText={Tables.Users.noDataPlaceholder}
              events={{ onClick: (id) => this.selectUser(id) }}
            />
            <CreateModal
              title={t("users:newUser")}
              show={showUserCreateModal}
              validationSchema={Forms.User.schema}
              initialValues={Forms.User.defaultValues}
              create={UserAPI.create}
              onCreate={this.userCreated}
              onClose={() => this.setState({ showUserCreateModal: false })}
            >
              {({ values, errors, touched, handleChange, handleBlur, setFieldValue }) => (
                <>
                  <Form.Group>
                    <Form.Label column="md">{t("users:username.name")}</Form.Label>
                    <Form.Control
                      required
                      placeholder={t("users:username.placeholder")}
                      value={values.username}
                      onBlur={handleBlur}
                      name="username"
                      onChange={handleChange}
                      isInvalid={touched.username && errors.username}
                    />
                    <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group>
                    <Form.Label column="md">{t("users:email.name")}</Form.Label>
                    <Form.Control
                      required
                      placeholder={t("users:email.placeholder")}
                      name="email"
                      value={values.email}
                      onBlur={handleBlur}
                      onChange={handleChange}
                      isInvalid={touched.email && errors.email}
                    />
                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group>
                    <Form.Label column="md">{t("users:groups.name")}</Form.Label>
                    <MultipleCheckboxList
                      labels="groups.name"
                      list={this.groups}
                      value={values.groups}
                      onChange={(groups) => setFieldValue("groups", groups)}
                      isInvalid={touched.groups && errors.groups}
                    />
                    <Form.Control.Feedback type="invalid">{errors.groups}</Form.Control.Feedback>
                  </Form.Group>
                </>
              )}
            </CreateModal>
            <EditModal
              title={this.modalTitle}
              show={showUserEditModal}
              load={this.loadUser}
              update={this.updateUser}
              onUpdate={this.userUpdated}
              remove={this.deleteUser}
              onRemove={this.userDeleted}
              onClose={() => this.setState({ showUserEditModal: false })}
              validationSchema={Forms.User.schema}
            >
              {({ values, errors, touched, handleChange, handleBlur, setFieldValue }) =>
                values && (
                  <>
                    <fieldset disabled>
                      <Form.Group>
                        <Form.Label column="md">{t("users:workload")}</Form.Label>
                        <Form.Control
                          onChange={() => null}
                          value={values.workload}
                          name="workload"
                        />
                      </Form.Group>
                    </fieldset>

                    <Form.Group>
                      <Form.Label column="md">{t("users:username.name")}</Form.Label>
                      <Form.Control
                        required
                        placeholder={t("users:username.placeholder")}
                        value={values.username}
                        onBlur={handleBlur}
                        name="username"
                        onChange={handleChange}
                        isInvalid={touched.username && errors.username}
                      />
                      <Form.Control.Feedback type="invalid">
                        {errors.username}
                      </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                      <Form.Label column="md">{t("users:email.name")}</Form.Label>
                      <Form.Control
                        required
                        placeholder={t("users:email.placeholder")}
                        name="email"
                        value={values.email}
                        onBlur={handleBlur}
                        onChange={handleChange}
                        isInvalid={touched.email && errors.email}
                      />
                      <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                      <Form.Label column="md">{t("users:groups.name")}</Form.Label>
                      <MultipleCheckboxList
                        required
                        labels="groups.name"
                        list={this.groups}
                        value={values.groups}
                        onChange={(groups) => setFieldValue("groups", groups)}
                        isInvalid={touched.groups && errors.groups}
                      />
                      <Form.Control.Feedback type="invalid">{errors.groups}</Form.Control.Feedback>
                    </Form.Group>

                    <AuditedEntityMetadata entityData={values} />
                  </>
                )
              }
            </EditModal>
          </Col>
        </Row>
      </DefaultLayout>
    );
  }
}

export default withTranslation(withAuthentication(role.USER_ADMINISTRATION, Users));
