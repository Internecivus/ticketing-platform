import withTranslation from "next-translate/withTranslation";
import React from "react";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Application from "../app/application";
import redirect from "../app/isomorphic-redirect";
import Path from "../app/path";
import CategoryEditModal from "../components/category/CategoryEditModal";
import SpinnerButton from "../components/SpinnerButton";
import DefaultLayout from "../layouts/DefaultLayout";
import withAuthentication from "../components/withAuthentication";
import { CategoriesAPI, TicketsAPI, UserAPI } from "../app/api";
import Tables from "../app/tables";
import CreateModal from "../components/modal/CreateModal";
import { role } from "../app/authentication";
import DataTable from "../components/table/DataTable";
import Alert from "../components/Alert";
import Forms from "../app/forms";
import Head from "next/head";
import { Formik, Field } from "formik";

class CreateTicket extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      categories: props.categories,
      users: props.users,
      showAlert: false,
      alert: {
        message: null,
        variant: null,
      },
      createIsInProgress: false,
    };
    this.ticketCreated = this.ticketCreated.bind(this);
  }

  static async getInitialProps() {
    const data = await Promise.all([CategoriesAPI.getAll(), UserAPI.getAll()]);
    return {
      categories: data[0],
      users: data[1],
    };
  }

  ticketCreated(category) {
    const { t } = this.props.i18n;
    this.setState({
      alert: {
        message: t("message.created", { categoryName: category.name }),
        variant: "success",
      },
      showCategoryCreateModal: false,
      showAlert: true,
    });
  }

  categories() {
    return this.state.categories.map((category) => (
      <option value={category.id} key={category.id}>
        {category.name}
      </option>
    ));
  }

  users() {
    return this.state.users.map((user) => (
      <option value={user.id} key={user.id}>
        {user.username}
      </option>
    ));
  }

  async createTicket(values, { resetForm }) {
    const { t } = this.props.i18n;

    this.setState({
      createIsInProgress: true,
    });
    await TicketsAPI.create(values);
    this.setState({
      showAlert: true,
      alert: {
        variant: "success",
        message: t("tickets:message.created", { title: values.title }),
      },
      createIsInProgress: false,
    });
    resetForm();
  }

  navigateToList() {
    redirect({
      pathname: Path.AllTickets,
    });
  }

  render() {
    const { t } = this.props.i18n;
    const { showAlert, alert } = this.state;
    return (
      <DefaultLayout>
        <Head>
          <title>{t("createTicket:title")}</title>
        </Head>
        <Row>
          <Col className="mx-lg-5 mt-1">
            <h1 className="mb-5">{t("createTicket:title")}</h1>
            <Alert
              data={alert}
              show={showAlert}
              onClose={() => this.setState({ showAlert: false })}
            />
            <Button variant="secondary" type="submit" onClick={() => this.navigateToList()}>
              {t("tickets:backToList")}
            </Button>

            <Formik
              validationSchema={Forms.Ticket.schema}
              initialValues={Forms.Ticket.defaultValues}
              onSubmit={(values, actions) => this.createTicket(values, actions)}
              enableReinitialize
              validateOnBlur={false}
              validateOnChange={false}
            >
              {({ handleSubmit, values, errors, touched, handleChange, handleBlur }) => (
                <Form className="mt-3" noValidate onSubmit={handleSubmit}>
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
                      rows={10}
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
                    <Form.Control.Feedback type="invalid">
                      {errors.assignedUser}
                    </Form.Control.Feedback>
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

                  <SpinnerButton
                    className="w-100"
                    variant="primary"
                    type="submit"
                    size="lg"
                    isSpinning={this.state.createIsInProgress}
                    disabled={this.state.createIsInProgress}
                    label={t("common:actions.create")}
                    spinningLabel={t("common:actions.createProgress")}
                  />
                </Form>
              )}
            </Formik>
          </Col>
        </Row>
      </DefaultLayout>
    );
  }
}

export default withTranslation(withAuthentication(role.TICKET_ADMINISTRATION, CreateTicket));
