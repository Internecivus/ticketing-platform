import withTranslation from "next-translate/withTranslation";
import React from "react";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import PropTypes from "prop-types";
import { Formik } from "formik";
import isFunction from "lodash/isFunction";
import SpinnerButton from "../SpinnerButton";
import ConfirmModal from "./ConfirmModal";
import LoadingModal from "./LoadingModal";
import makeCancelable from "../../app/cancelablePromise";
import Alert from "../Alert";
import { ApiError } from "../../app/api";

class EditModal extends React.Component {
  static initialState = {
    lastPersistedEntity: null,
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
    showConfirmDeleteModal: false,
  };

  constructor(props) {
    super(props);
    this.state = EditModal.initialState;
    this.delete = this.delete.bind(this);
    this.update = this.update.bind(this);
    this.load = this.load.bind(this);
    this.close = this.close.bind(this);
  }

  static getDerivedStateFromProps(props) {
    if (props.isUpdating) {
      return {
        transaction: {
          isInProgress: true,
          type: "UPDATE",
        },
      };
    }
    return null;
  }

  componentDidUpdate(prevProps) {
    const { show } = this.props;

    if (show && !prevProps.show) {
      this.reset();
      this.load();
    } else if (prevProps.show && !show) {
      this.cancelRequests();
    }
  }

  componentWillUnmount() {
    this.cancelRequests();
  }

  reset() {
    this.setState(EditModal.initialState);
  }

  cancelRequests() {
    if (this.loadEntityRequest) {
      this.loadEntityRequest.cancel();
    }
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

  async load() {
    const { load, onLoad } = this.props;

    this.beginTransaction("LOAD");
    this.loadEntityRequest = makeCancelable(load());
    try {
      const response = await this.loadEntityRequest.promise;
      if (onLoad) onLoad(response);
      this.setState({ lastPersistedEntity: response });
    } catch (error) {
      if (!(error instanceof ApiError)) return;
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

  async update(event) {
    const { update, onUpdate } = this.props;

    this.beginTransaction("UPDATE");
    try {
      const response = await update(event);
      if (onUpdate) onUpdate(response);
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

  async delete() {
    const { remove, onRemove } = this.props;
    const { lastPersistedEntity } = this.state;

    this.setState({ showConfirmDeleteModal: false });
    this.beginTransaction("DELETE");
    try {
      await remove();
      if (onRemove) onRemove(lastPersistedEntity);
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

  modalTitle() {
    const { title } = this.props;
    const { lastPersistedEntity } = this.state;

    if (isFunction(title)) {
      return title(lastPersistedEntity);
    }
    return title;
  }

  close() {
    const { transaction } = this.state;
    const { onClose } = this.props;

    if (!transaction.isInProgress) {
      onClose();
    }
  }

  modalContent() {
    const { t } = this.props.i18n;
    const { children, validationSchema, buttons, disableSave } = this.props;
    const { showConfirmDeleteModal, transaction, alert, showAlert, lastPersistedEntity } =
      this.state;

    return (
      <>
        <Modal.Header closeButton>
          <Modal.Title>{this.modalTitle()}</Modal.Title>
        </Modal.Header>
        <Formik
          validationSchema={validationSchema}
          initialValues={lastPersistedEntity}
          onSubmit={this.update}
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
                  onClose={() => this.setState({ showAlert: false })}
                />
                {children(actions)}
              </Modal.Body>
              <Modal.Footer>
                {buttons}
                <SpinnerButton
                  variant="danger"
                  isSpinning={transaction.type === "DELETE"}
                  disabled={transaction.isInProgress}
                  label={t("common:actions.delete")}
                  onClick={() => this.setState({ showConfirmDeleteModal: true })}
                  spinningLabel={t("common:actions.deleteProgress")}
                />

                {disableSave ? null : (
                  <SpinnerButton
                    variant="success"
                    type="submit"
                    isSpinning={transaction.type === "UPDATE"}
                    disabled={transaction.isInProgress}
                    label={t("common:actions.save")}
                    spinningLabel={t("common:actions.savingProgress")}
                  />
                )}
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
        <ConfirmModal
          show={showConfirmDeleteModal}
          onYes={this.delete}
          onClose={() => this.setState({ showConfirmDeleteModal: false })}
        />
      </>
    );
  }

  render() {
    const { show, size } = this.props;
    const { lastPersistedEntity, transaction } = this.state;

    return (
      <Modal show={show} size={size ?? "lg"} onHide={this.close}>
        {lastPersistedEntity && !(transaction.type === "LOAD") ? (
          this.modalContent()
        ) : (
          <LoadingModal size="lg" />
        )}
      </Modal>
    );
  }
}

EditModal.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.func]).isRequired,
  show: PropTypes.bool.isRequired,
  load: PropTypes.func.isRequired,
  update: PropTypes.func.isRequired,
  onUpdate: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  onRemove: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
  validationSchema: PropTypes.object.isRequired,
};

export default withTranslation(EditModal);
