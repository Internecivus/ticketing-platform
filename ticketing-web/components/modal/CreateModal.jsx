import withTranslation from "next-translate/withTranslation";
import React from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { Formik } from "formik";
import PropTypes from "prop-types";
import SpinnerButton from "../SpinnerButton";
import Alert from "../Alert";
import Forms from "../../app/forms";

class CreateModal extends React.Component {
  static initialState = {
    alert: {
      title: null,
      message: null,
      variant: null,
    },
    transaction: {
      isInProgress: false,
      type: null,
    },
    showAlert: false,
  };

  constructor(props) {
    super(props);
    this.state = CreateModal.initialState;
    this.create = this.create.bind(this);
    this.close = this.close.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { show } = this.props;

    if (show && !prevProps.show) {
      this.reset();
    }
  }

  close() {
    const { transaction } = this.state;
    const { onClose } = this.props;

    if (!transaction.isInProgress) {
      onClose();
    }
  }

  reset() {
    this.setState(CreateModal.initialState);
  }

  beginTransaction(type) {
    this.setState({
      transaction: {
        isInProgress: true,
        type,
      },
    });
  }

  endTransaction() {
    this.setState({
      transaction: {
        isInProgress: false,
        type: null,
      },
    });
  }

  async create(values) {
    const { create, onCreate } = this.props;

    this.beginTransaction("CREATE");
    try {
      const response = await create(values);
      if (onCreate) onCreate(response);
    } catch (error) {
      this.setState({
        showAlert: true,
        alert: {
          variant: "danger",
          message: error.response.message,
        },
      });
    } finally {
      this.endTransaction();
    }
  }

  render() {
    const { t } = this.props.i18n;
    const { show, children, title, validationSchema, initialValues } = this.props;
    const { transaction, showAlert, alert } = this.state;

    return (
      <Modal show={show} onHide={this.close}>
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Formik
          validationSchema={validationSchema}
          initialValues={initialValues}
          onSubmit={this.create}
          enableReinitialize
          validateOnBlur={false}
          validateOnChange={false}
        >
          {(actions) => (
            <Form noValidate onSubmit={actions.handleSubmit}>
              <Modal.Body>
                <Alert
                  data={alert}
                  show={showAlert}
                  onClose={() => this.setState({ showAlert: true })}
                />
                {children(actions)}
              </Modal.Body>
              <Modal.Footer>
                <SpinnerButton
                  variant="primary"
                  type="submit"
                  isSpinning={transaction.type === "CREATE"}
                  disabled={transaction.isInProgress}
                  label={t("common:actions.create")}
                  spinningLabel={t("common:actions.createProgress")}
                />
                <Button
                  variant="secondary"
                  onClick={this.close}
                  disabled={transaction.isInProgress}
                >
                  {t("common:actions.close")}
                </Button>
              </Modal.Footer>
            </Form>
          )}
        </Formik>
      </Modal>
    );
  }
}

CreateModal.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.func]).isRequired,
  show: PropTypes.bool.isRequired,
  create: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
  onCreate: PropTypes.func.isRequired,
  validationSchema: PropTypes.object.isRequired,
  initialValues: PropTypes.object.isRequired,
};

export default withTranslation(CreateModal);
