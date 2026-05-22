import { Formik } from "formik";
import Head from "next/head";
import NextLink from "next/link";
import { withRouter } from "next/router";
import React from "react";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import { notifyIfError } from "../app/api";
import { Authentication } from "../app/authentication";
import Forms from "../app/forms";
import Navigation from "../app/navigation";
import Path from "../app/path";
import withAuthentication from "../components/withAuthentication";
import Alert from "../components/Alert";
import SpinnerButton from "../components/SpinnerButton";
import PublicLayout from "../layouts/PublicLayout";
import withTranslation from "next-translate/withTranslation";

class Login extends React.Component {
  static async getInitialProps(context) {
    if (Authentication.isLoggedIn()) {
      await Navigation.redirectBackTo(context.req?.query?.redirect, context.res);
    }
    return {};
  }

  constructor(props) {
    super(props);
    this.state = {
      alert: {
        title: null,
        message: null,
        variant: null,
      },
      showAlert: false,
    };
    this.login = this.login.bind(this);
  }

  async login(values) {
    await Authentication.login(values.usernameOrEmail, values.password);
    const { router } = this.props;
    const { redirect } = router.query;
    await Navigation.redirectBackTo(redirect);
  }

  render() {
    const { t } = this.props.i18n;
    const { alert, showAlert } = this.state;

    return (
      <PublicLayout>
        <Head>
          <title>{t("login:title")}</title>
        </Head>

        <Row className="justify-content-center">
          <Col lg="4">
            <h1 className="text-center mb-5">{t("login:title")}</h1>
            <Alert
              data={alert}
              show={showAlert}
              onClose={() => this.setState({ showAlert: false })}
            />
            <Formik
              validationSchema={Forms.Login.schema}
              onSubmit={this.login}
              initialValues={Forms.Login.defaultValues}
              enableReinitialize
              validateOnBlur={false}
              validateOnChange={false}
            >
              {({
                values,
                errors,
                touched,
                handleChange,
                handleSubmit,
                handleBlur,
                isSubmitting,
              }) => (
                <Form noValidate onSubmit={handleSubmit}>
                  <Form.Group className="mb-2">
                    <Form.Control
                      placeholder={t("login:usernameOrEmail.placeholder")}
                      autoFocus
                      required
                      name="usernameOrEmail"
                      onChange={handleChange}
                      onBlur={handleBlur}
                      value={values.usernameOrEmail}
                      isInvalid={touched.usernameOrEmail && errors.usernameOrEmail}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.usernameOrEmail}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group>
                    <Form.Control
                      placeholder={t("login:password.placeholder")}
                      type="password"
                      required
                      name="password"
                      onChange={handleChange}
                      onBlur={handleBlur}
                      value={values.password}
                      isInvalid={touched.password && errors.password}
                    />
                    <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                  </Form.Group>
                  <div className="d-grid gap-2 mt-2">
                    <SpinnerButton
                      size="lg"
                      variant="primary"
                      type="submit"
                      isSpinning={isSubmitting}
                      label={t("login:login")}
                      spinningLabel={t("login:loginProgress")}
                    />
                    <NextLink href={Path.ResetPassword} passHref>
                      <a className="ms-2">
                        <small>{t("login:forgotPassword")}</small>
                      </a>
                    </NextLink>
                  </div>
                </Form>
              )}
            </Formik>
          </Col>
        </Row>
      </PublicLayout>
    );
  }
}

export default withTranslation(withRouter(withAuthentication(null, Login)));
