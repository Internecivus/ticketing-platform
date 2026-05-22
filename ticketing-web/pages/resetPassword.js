import withTranslation from "next-translate/withTranslation";
import Row from "react-bootstrap/Row";
import Form from "react-bootstrap/Form";
import Col from "react-bootstrap/Col";
import React from "react";
import { Formik } from "formik";
import { role } from "../app/authentication";
import Navigation from "../app/navigation";
import withAuthentication from "../components/withAuthentication";
import PublicLayout from "../layouts/PublicLayout";
import { PasswordResetAPI } from "../app/api";
import Forms from "../app/forms";
import SpinnerButton from "../components/SpinnerButton";
import Head from "next/head";
import Alert from "react-bootstrap/Alert";

class ResetPassword extends React.Component {
  static async getInitialProps() {
    return {};
  }

  constructor(props) {
    super(props);
    this.state = {
      resetPasswordSent: false,
    };
    this.forgotPassword = this.forgotPassword.bind(this);
  }

  async forgotPassword(values) {
    await PasswordResetAPI.create(values.email);
    this.setState({ resetPasswordSent: true });
  }

  resetPasswordSent() {
    const { t } = this.props.i18n;

    return (
      <Alert variant="success">
        <p>{t("resetPassword:resetPasswordSent.0")}</p>
        <p>{t("resetPassword:resetPasswordSent.1")}</p>
      </Alert>
    );
  }

  forgotPasswordForm() {
    const { t } = this.props.i18n;

    return (
      <Formik
        validationSchema={Forms.ForgotPassword.schema}
        onSubmit={this.forgotPassword}
        initialValues={Forms.ForgotPassword.defaultValues}
        validateOnBlur={false}
        validateOnChange={false}
      >
        {({ values, errors, touched, handleChange, handleSubmit, handleBlur, isSubmitting }) => (
          <Form className="d-grid gap-2" noValidate onSubmit={handleSubmit}>
            <Form.Group>
              <Form.Control
                placeholder={t("resetPassword:email.placeholder")}
                type="email"
                required
                name="email"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.email}
                isInvalid={touched.email && errors.email}
              />
              <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
            </Form.Group>
            <SpinnerButton
              className="mt-2"
              variant="primary"
              size="lg"
              type="submit"
              isSpinning={isSubmitting}
              label={t("resetPassword:reset")}
              spinningLabel={t("resetPassword:resetProgress")}
            />
          </Form>
        )}
      </Formik>
    );
  }

  render() {
    const { resetPasswordSent } = this.state;
    const { t } = this.props.i18n;

    return (
      <PublicLayout>
        <Head>
          <title>{t("resetPassword:title")}</title>
        </Head>

        <Row className="justify-content-center">
          <Col lg="4">
            <h1 className="text-center mb-5">{t("resetPassword:title")}</h1>
            {resetPasswordSent ? this.resetPasswordSent() : this.forgotPasswordForm()}
          </Col>
        </Row>
      </PublicLayout>
    );
  }
}

export default withTranslation(withAuthentication(null, ResetPassword));
