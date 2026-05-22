import withTranslation from "next-translate/withTranslation";
import Head from "next/head";
import React from "react";
import { Row } from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { PasswordResetAPI } from "../app/api";
import NextLink from "next/link";
import Forms from "../app/forms";
import redirect from "../app/isomorphic-redirect";
import Path from "../app/path";
import { Formik } from "formik";
import withAuthentication from "../components/withAuthentication";
import PublicLayout from "../layouts/PublicLayout";

class ChangePassword extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      passwordChanged: false,
    };
    this.changePassword = this.changePassword.bind(this);
  }

  static async getInitialProps({ query }) {
    const { token } = query;
    return {
      token,
    };
  }

  changePassword(values) {
    PasswordResetAPI.use({ newPassword: values.newPassword, tokenHash: this.props.token }).then(
      () => {
        this.setState({ passwordChanged: true });
      }
    );
  }

  passwordChanged() {
    const { t } = this.props.i18n;

    return (
      <div>
        <p className="text-center">{t("changePassword:passwordChangedMessage")}</p>

        <div className="d-grid">
          <Button
            onClick={() =>
              redirect({
                pathname: Path.Login,
              })
            }
          >
            {t("changePassword:login")}
          </Button>
        </div>
      </div>
    );
  }

  changePasswordForm() {
    const { t } = this.props.i18n;

    return (
      <Formik
        validationSchema={Forms.ChangePassword.schema}
        initialValues={Forms.ChangePassword.defaultValues}
        onSubmit={this.changePassword}
        enableReinitialize
        validateOnBlur={false}
        validateOnChange={false}
      >
        {({ values, errors, touched, handleChange, handleSubmit, handleBlur, isSubmitting }) => (
          <Form noValidate onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>{t("changePassword:oldPassword.placeholder")}</Form.Label>
              <Form.Control
                name="oldPassword"
                type="password"
                required
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.oldPassword}
                isInvalid={touched.oldPassword && errors.oldPassword}
              />
              <Form.Control.Feedback type="invalid">{errors.oldPassword}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>{t("changePassword:newPassword.placeholder")}</Form.Label>
              <Form.Control
                name="newPassword"
                type="password"
                required
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.newPassword}
                isInvalid={touched.newPassword && errors.newPassword}
              />
              <Form.Control.Feedback type="invalid">{errors.newPassword}</Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>{t("changePassword:newPasswordRepeat.placeholder")}</Form.Label>
              <Form.Control
                name={"newPasswordRepeat"}
                type="password"
                required
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.newPasswordRepeat}
                isInvalid={touched.newPasswordRepeat && errors.newPasswordRepeat}
              />
              <Form.Control.Feedback type="invalid">
                {errors.newPasswordRepeat}
              </Form.Control.Feedback>
            </Form.Group>

            <div className="d-grid">
              <Button variant="primary" type="submit" size="lg">
                {t("common:change")}
              </Button>
            </div>
          </Form>
        )}
      </Formik>
    );
  }

  render() {
    const { passwordChanged } = this.state;
    const { t } = this.props.i18n;

    return (
      <PublicLayout>
        <Head>
          <title>{t("changePassword:title")}</title>
        </Head>

        <Row className="justify-content-center">
          <Col lg="4">
            <h1 className="text-center mb-5">{t("changePassword:title")}</h1>
            {passwordChanged ? this.passwordChanged() : this.changePasswordForm()}
          </Col>
        </Row>
      </PublicLayout>
    );
  }
}

export default withTranslation(withAuthentication(null, ChangePassword));
